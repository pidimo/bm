package com.piramide.elwis.web.restfulservice.contact.client;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;

/**
 * RestEasy client to test web service: Upload web document
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class UploadWebDocumentClient {


    public static void main(String[] args) {
        System.out.println("Executing client to send web document to server: \n");

        UploadWebDocumentClient client = new UploadWebDocumentClient();
        client.executeRestfulRequest();
    }

    public void executeRestfulRequest() {

        try {
            ResteasyClient client = new ResteasyClientBuilder().build();

            ResteasyWebTarget target = client.target("http://localhost:8080/bm/rest/services/saveWebDocument");

            Response response = target.request().post(Entity.entity(composeJSON(), "application/json"));

            if (response.getStatus() != 200) {
                System.out.println("Response:" + response.hasEntity());
                System.out.println("Response:" + response.readEntity(String.class));
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            System.out.println("Server response : \n");
            System.out.println(response.readEntity(String.class));

            response.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String composeJSON() throws Exception {

        String generationId = "da98daef-c751-4456-93be-13d24fa3a805";

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        jsonBuilder.append("\"generationId\" : ").append("\"").append(generationId).append("\"").append(",");
        jsonBuilder.append("\"documentName\" : ").append("\"webDocumentTest.pdf\"").append(",");
        jsonBuilder.append("\"document\" : ").append("\"").append(getFileInBase64Encoded()).append("\"");
        jsonBuilder.append("}");

        return jsonBuilder.toString();
    }

    private String getFileInBase64Encoded() throws Exception {
        File file_upload = new File("file_test.pdf");
        return convertFileToString(file_upload);
    }

    //Convert my file to a Base64 String
    private String convertFileToString(File file) throws Exception{
        byte[] bytes = readFile(file);
        return new String(Base64.encodeBase64(bytes));
    }

    private byte[] readFile(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        return IOUtils.toByteArray(fileInputStream);

    }
}
