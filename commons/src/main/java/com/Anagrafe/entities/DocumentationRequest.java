
package com.Anagrafe.entities;

import java.util.List;

import com.Anagrafe.entities.enums.DocumentOperation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentationRequest {

  private List<Document> documents;
  private DocumentOperation operation;
  private BaseUser user;
  private Integer size;

  /**
   * this constructor is used to create a request for a batch of documents
   * the documents must belong to the same user
   * 
   * @param documents
   * @param operation
   * @throws IllegalArgumentException
   *
   */
  public DocumentationRequest(List<Document> documents, DocumentOperation operation) throws IllegalArgumentException {
    this.documents = documents;
    this.operation = operation;
    this.user = documents.get(0).getOwner();
    for (Document document : documents) {
      if (this.user != document.getOwner()) {
        throw new IllegalArgumentException("All documents must belong to the same user");
      }
    }

    this.size = documents.size();

  }

}
