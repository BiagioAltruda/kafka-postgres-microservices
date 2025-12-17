package com.Anagrafe.AdminService.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Anagrafe.AdminService.service.JwtService;
import com.Anagrafe.AdminService.service.UserService;
import com.Anagrafe.entities.Document;
import com.Anagrafe.entities.DocumentationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
@RequestMapping("/documentation")
public class DocumentationController {

  private final KafkaTemplate<String, DocumentationRequest> kafkaDocumentationRequestTemplate;
  private final JwtService jwtService;
  private final UserService userService;
  private final OkHttpClient client;

  public DocumentationController(KafkaTemplate<String, DocumentationRequest> kafkaDocumentationRequestTemplate,
      JwtService jwtService, UserService userService) {
    this.kafkaDocumentationRequestTemplate = kafkaDocumentationRequestTemplate;
    this.jwtService = jwtService;
    this.userService = userService;
    this.client = new OkHttpClient();
  }

  @GetMapping("/get-docs")
  public List<Document> getUserDocuments(@RequestHeader String token) {
    String url = "http://localhost:8082/documentation/get-docs";
    try {
      userService.findUserByUsername(jwtService.getUsernameFromToken(token)).orElseThrow();
      Request newRequest = new Request.Builder()
          .url(url)
          .addHeader("token", token)
          .build();
      try (Response response = client.newCall(newRequest).execute()) {
        @SuppressWarnings("unchecked")
        List<Document> docs = (List<Document>) response.body();

        return docs;
      } catch (Exception e) {
        return null;
      }
    } catch (Exception e) {
      throw new AccessDeniedException("User not logged in");
    }
  }

  @PostMapping("/upload")
  public ResponseEntity<String> uploadDocument(@RequestBody DocumentationRequest request, @RequestHeader String token) {

    Long userId = userService
        .findUserByUsername(jwtService.getUsernameFromToken(token.replace("Bearer ", "")))
        .orElseThrow(() -> new AccessDeniedException("User not logged in"))
        .getId();
    request.setUserId(userId);
    // max size of documents to be processes
    // synchronously
    final Integer syncrhronousLimit = 1024 * 10;

    if (request.getSize() > syncrhronousLimit) {
      kafkaDocumentationRequestTemplate.send(request.getOperation().toString(), request);
      return ResponseEntity.ok("Document upload deferred to service. await results");
    } else {
      try {
        return ResponseEntity.ok(forwardDocumentRequest(request));
      } catch (IOException e) {
        return ResponseEntity.status(500).body("Error forwarding document request: " + e.getMessage());
      }
    }
  }

  private String forwardDocumentRequest(DocumentationRequest request) throws IOException {

    String url = "http://localhost:8082/documentation/upload";

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());

    String json = mapper.writeValueAsString(request);
    System.out.println("sending: " + json);

    Request newRequest = new Request.Builder()
        .url(url)
        .post(okhttp3.RequestBody.create(json, okhttp3.MediaType.parse("application/json")))
        .build();

    try (okhttp3.Response response = client.newCall(newRequest).execute()) {

      if (response.isSuccessful()) {
        return response.body().string();
      } else {
        throw new IOException("Unexpected code " + response);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "Could not forward request to document service";
    }
  }
}
