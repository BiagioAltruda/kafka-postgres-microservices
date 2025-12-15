package com.Anagrafe.AdminService.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Anagrafe.AdminService.service.UserService;
import com.Anagrafe.entities.BaseUser;
import com.Anagrafe.entities.Document;
import com.Anagrafe.entities.DocumentationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import okhttp3.OkHttpClient;
import okhttp3.Request;

@RestController
@RequestMapping("/documentation")
public class DocumentationController {

  private final KafkaTemplate<String, DocumentationRequest> kafkaDocumentationRequestTemplate;
  private final UserService userService;

  public DocumentationController(KafkaTemplate<String, DocumentationRequest> kafkaDocumentationRequestTemplate,
      UserService userService) {
    this.kafkaDocumentationRequestTemplate = kafkaDocumentationRequestTemplate;
    this.userService = userService;
  }

  @GetMapping("/get-docs")
  public ResponseEntity<List<Document>> getUserDocuments() {

    try {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      @SuppressWarnings("unused")
      BaseUser user = (BaseUser) auth.getPrincipal();

      /**
       * TODO: get document with kafka
       */

    } catch (Exception e) {
      throw new AccessDeniedException("User not logged in");
    }

    return null;
  }

  @PostMapping("/upload")
  public ResponseEntity<String> uploadDocument(@RequestBody DocumentationRequest request) {

    // max size of documents to be processes
    // synchronously
    final Integer syncrhronousLimit = 500;
    /**
     * TODO: check document size, if size is above some value,
     * defer the operation to another service with kafka
     */
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

  @SuppressWarnings("unused")
  private Document createDocument(BaseUser owner, String documentType, LocalDateTime creationDate,
      LocalDateTime expirationDate) {

    return null;
  }

  private String forwardDocumentRequest(DocumentationRequest request) throws IOException {

    String url = "http://localhost:8082/documentation/upload";

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());

    BaseUser user = userService.findUserByUsername(request.getDocuments().get(0).getOwner().getUsername()).get();
    request.setUser(user);

    System.out.println("**********************************************************************");
    System.out.println("User:" + request.getUser().getChangeLog());
    System.out.println("**********************************************************************");

    String json = mapper.writeValueAsString(request);
    System.out.println("sending: " + json);

    Request newRequest = new Request.Builder()
        .url(url)
        .post(okhttp3.RequestBody.create(json, okhttp3.MediaType.parse("application/json")))
        .build();

    OkHttpClient client = new OkHttpClient();

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
