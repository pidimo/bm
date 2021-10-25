package com.piramide.elwis.web.restfulservice.contact;

import com.piramide.elwis.cmd.contactmanager.UploadWebDocumentCmd;
import com.piramide.elwis.utils.ArrayByteWrapper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Rest web service to upload web document in bm
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
@Path("/services")
public class UploadWebDocumentWSBean {
    private Log log = LogFactory.getLog(this.getClass());

    @POST
    @Path("/saveWebDocument")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveGeneratedWebDocument(WebDocumentJSON webDocumentJSON) {
        log.debug("Execute restful service saveWebDocument..." + webDocumentJSON);

        String failMessage = null;
        try {
            failMessage = uploadWebDocumentProcess(webDocumentJSON);
        } catch (Exception e) {
            log.warn("Unexpected erro when try upload web document..", e);
            failMessage = "Unexpected error";
        }

        //Build the response
        Response response;
        if (failMessage != null) {
            response = Response.status(500).entity("FAIL : " + failMessage).build();
        } else {
            response = Response.status(200).entity("SUCCESS").build();
        }

        return response;
    }

    /**
     * Upload document in data base
     * @param webDocumentJSON doc
     * @return message
     * @throws Exception
     */
    private String uploadWebDocumentProcess(WebDocumentJSON webDocumentJSON) throws Exception {
        String failMessage = null;

        String webGenerateUUID = webDocumentJSON.getGenerationId();
        ArrayByteWrapper arrayByteWrapper = composeDocumentByteWrapper(webDocumentJSON);

        if (arrayByteWrapper != null) {
            UploadWebDocumentCmd uploadWebDocumentCmd = new UploadWebDocumentCmd();
            uploadWebDocumentCmd.setOp("saveContactWebDocument");
            uploadWebDocumentCmd.putParam("webGenerateUUID", webGenerateUUID);
            uploadWebDocumentCmd.putParam("documentWrapper", arrayByteWrapper);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(uploadWebDocumentCmd, null);
                if (resultDTO.isFailure()) {
                    failMessage = (String) resultDTO.get("failCause");
                }
            } catch (AppLevelException e) {
                log.warn("Error in execute cmd UploadWebDocumentCmd...", e);
                failMessage = "Unexpected error";
            }
        } else {
            failMessage = "Document is invalid";
        }

        return failMessage;
    }

    private ArrayByteWrapper composeDocumentByteWrapper(WebDocumentJSON webDocumentJSON) {
        ArrayByteWrapper arrayByteWrapper = null;

        if (webDocumentJSON.getDocument() != null && webDocumentJSON.getDocument().length() > 0) {
            arrayByteWrapper = new ArrayByteWrapper();
            arrayByteWrapper.setFileData(decodeBase64File(webDocumentJSON.getDocument()));

            if (webDocumentJSON.getDocumentName() != null) {
                arrayByteWrapper.setFileName(webDocumentJSON.getDocumentName());
            }
        }
        return arrayByteWrapper;
    }

    private byte[] decodeBase64File(String base64File) {
        return Base64.decodeBase64(base64File.getBytes());
    }

}
