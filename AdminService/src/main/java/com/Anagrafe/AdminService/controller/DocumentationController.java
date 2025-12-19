package com.Anagrafe.AdminService.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import okhttp3.ResponseBody;

@RestController
@RequestMapping("/documentation")
public class DocumentationController {

  private final KafkaTemplate<String, DocumentationRequest> kafkaDocumentationRequestTemplate;
  private final JwtService jwtService;
  private final UserService userService;
  private final OkHttpClient client;
  private final Logger docLogger = LogManager.getLogger("com.Anagrafe.docs");

  public DocumentationController(KafkaTemplate<String, DocumentationRequest> kafkaDocumentationRequestTemplate,
      JwtService jwtService, UserService userService) {
    this.kafkaDocumentationRequestTemplate = kafkaDocumentationRequestTemplate;
    this.jwtService = jwtService;
    this.userService = userService;
    this.client = new OkHttpClient();
  }

  @GetMapping("/get-docs")
  public ResponseEntity<List<Document>> getUserDocuments(@RequestHeader String token) {
    String url = "http://localhost:8082/documentation";
    if (userService.findUserByUsername(jwtService.getUsernameFromToken(token)).get() == null) {
      docLogger.error("User Not Logged in");
      throw new AccessDeniedException("User not logged in");
    }
    Long userId = userService
        .findUserByUsername(jwtService.getUsernameFromToken(token))
        .orElseThrow(() -> new AccessDeniedException("User not logged in"))
        .getId();

    Request newRequest = new Request.Builder()
        .url(url + "?userId=" + userId)
        .build();
    try (Response response = client.newCall(newRequest).execute()) {

      if (response.code() != 200) {
        docLogger.error("Document Service could not processes request. exited with code {} and message : {}",
            response.code(), response.message());
        return ResponseEntity.status(response.code()).body(null);
      }

      List<Document> docs = extractBody(response, Document.class);

      return ResponseEntity.ok(docs);
    } catch (Exception e) {
      docLogger.error("Error fetching documents: {}", e.getMessage());
      return ResponseEntity.status(500).body(null);
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
      docLogger.info("Document upload deferred to service. await results");
      return ResponseEntity.ok("Document upload deferred to service. await results");
    } else {
      try {
        docLogger.info("Document upload started");
        return ResponseEntity.ok(forwardDocumentRequest(request));
      } catch (IOException e) {
        docLogger.error("Document upload failed", e);
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
        docLogger.info("Document forwarded successfully");
        return response.body().string();
      } else {
        docLogger.error("Document upload failed", response);
        throw new IOException("Unexpected code " + response);
      }
    } catch (Exception e) {
      docLogger.error("Could not forward request to document service", e);
      return "Could not forward request to document service";
    }
  }

  // takes a response and extracts the body as a list of type T
  private <T> List<T> extractBody(Response request, Class<T> type) throws IOException {
    ResponseBody body = request.body();
    if (body == null) {
      throw new IOException("Request body is null");
    }

    String bodyString = body.string();

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    return mapper.readValue(bodyString,
        mapper.getTypeFactory().constructCollectionType(List.class, type));

  }
}
