package com.piramide.elwis.web.restfulservice.contact;

/**
 * POJO for JSON mapping
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class WebDocumentJSON {
    private String generationId;
    private String document;
    private String documentName;

    public WebDocumentJSON() {

    }

    public String getGenerationId() {
        return generationId;
    }

    public void setGenerationId(String generationId) {
        this.generationId = generationId;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @Override
    public String toString() {
        return new StringBuffer(" generationId : ").append(this.generationId)
                .append("\n documentName : ").append(this.documentName).toString();
    }
}
