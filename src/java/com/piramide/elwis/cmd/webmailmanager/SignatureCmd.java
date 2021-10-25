package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.catalogmanager.FreeText;
import com.piramide.elwis.domain.catalogmanager.FreeTextHome;
import com.piramide.elwis.domain.webmailmanager.Signature;
import com.piramide.elwis.domain.webmailmanager.SignatureHome;
import com.piramide.elwis.dto.catalogmanager.FreeTextDTO;
import com.piramide.elwis.dto.webmailmanager.SignatureDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;

/**
 * This class perform the operations over a SignatureBean, the
 * operations are create, update, delete, read.
 *
 * @author Alvaro
 * @version $Id: SignatureCmd.java 9971 2010-07-03 22:25:30Z ivan $
 */
public class SignatureCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {

        String op = getOp();

        Boolean isDefault = (paramDTO.get("isDefault") != null ? true : false);

        SignatureDTO dto = new SignatureDTO();
        dto.put("signatureName", paramDTO.get("signatureName"));
        dto.put("userMailId", paramDTO.get("userMailId"));
        dto.put("companyId", paramDTO.get("companyId"));
        dto.put("textSignatureId", paramDTO.get("textSignatureId"));
        dto.put("htmlSignatureId", paramDTO.get("htmlSignatureId"));
        dto.put("mailAccountId", paramDTO.get("mailAccountId"));
        dto.put("isDefault", isDefault);

        String signature_id = null != paramDTO.get("signatureId") ?
                paramDTO.get("signatureId").toString() :
                null;

        if (op.equals("create")) {
            if (isDefault) {
                changeDefaultSignature(Integer.valueOf(paramDTO.get("userMailId").toString()),
                        Integer.valueOf(paramDTO.get("mailAccountId").toString()));
            }
            createSignature(dto, ctx);
        }

        if (op.equals("update")) {
            if (isDefault) {
                changeDefaultSignature(Integer.valueOf(paramDTO.get("userMailId").toString()),
                        Integer.valueOf(paramDTO.get("mailAccountId").toString()));
            }
            updateSignature(dto, signature_id, ctx);
        }

        if (op.equals("delete")) {
            deleteSignature(dto, signature_id, ctx);
        }

        if (op.equals("read")) {
            readSignature(signature_id);
        }

        if ("getDefaultSignature".equals(op)) {
            Integer mailAccountId = (Integer) paramDTO.get("mailAccountId");
            Integer userMailId = (Integer) paramDTO.get("userMailId");
            getDefaultSignature(userMailId, mailAccountId);
        }
    }

    /**
     * This method search defaultSignature for some mail account and
     * put in <code>resultDTO</code> default Signature identifier if not exists puts null
     *
     * @param userMailId    mail user identifier
     * @param mailAccountId mail account identifier
     * @return <code>Signature</code> object.
     */
    private Signature getDefaultSignature(Integer userMailId, Integer mailAccountId) {
        SignatureHome signatureHome =
                (SignatureHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_SIGNATURE);
        Signature signature = null;
        try {
            signature = signatureHome.findDefaultSignature(userMailId, mailAccountId);

            resultDTO.put("signatureId", signature.getSignatureId());
        } catch (FinderException e) {
            log.debug("Cannot find default signature for account " + mailAccountId + " and mail user " + userMailId);
        }
        return signature;
    }

    /**
     * Creates a signature and asignate it to a usermail
     *
     * @param dto is the SignatureDTO
     */
    public void createSignature(SignatureDTO dto, SessionContext ctx) {
        Signature signature = (Signature) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE, dto, new ResultDTO());

        String htmlMessage = paramDTO.get("signatureHtmlMessage").toString();

        String textMessage = paramDTO.get("signatureMessage").toString();


        FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
        try {
            FreeText freeText = freeTextHome.create(htmlMessage.getBytes(), signature.getCompanyId(), FreeTextTypes.FREETEXT_SIGNATURE);
            signature.setHtmlSignatureId(freeText.getFreeTextId());
            manageSignatureImages(signature.getSignatureId(), htmlMessage, ctx);
        } catch (CreateException e) {
            log.warn("Cannot create HtmlFreeText to signature.");
        }

        try {
            FreeText freeText = freeTextHome.create(textMessage.getBytes(), signature.getCompanyId(), FreeTextTypes.FREETEXT_SIGNATURE);
            signature.setTextSignatureId(freeText.getFreeTextId());
        } catch (CreateException e) {
            log.warn("Cannot create TextFreeText to signature.");
        }
    }

    /**
     * Updates a signature
     *
     * @param dto         is the SignatureDTO
     * @param signatureId is the id of the signature
     */
    public void updateSignature(SignatureDTO dto, String signatureId, SessionContext ctx) {
        Integer signatureId_int = new Integer(signatureId);
        dto.put("signatureId", signatureId_int);
        Signature signature = (Signature) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_UPDATE, dto, resultDTO);
        if (null != signature && !resultDTO.isFailure()) {
            String htmlMessage = paramDTO.get("signatureHtmlMessage").toString();

            String textMessage = paramDTO.get("signatureMessage").toString();

            updateSignatureFreeText(htmlMessage, signature.getHtmlSignatureId());
            updateSignatureFreeText(textMessage, signature.getTextSignatureId());
            manageSignatureImages(signature.getSignatureId(), htmlMessage, ctx);
        }
    }

    /**
     * Delete a signature
     *
     * @param dto         is the SignatureDTO
     * @param signatureId is the id of the signature
     */
    public void deleteSignature(SignatureDTO dto, String signatureId, SessionContext ctx) {
        Integer signatureId_int = new Integer(signatureId);

        Signature signature = readSignature(signatureId);
        if (null != signature && !resultDTO.isFailure()) {
            manageSignatureImages(signature.getSignatureId(), "", ctx);

            Integer htmlSignatureId = signature.getHtmlSignatureId();
            Integer textSignatureId = signature.getTextSignatureId();
            //clear all draft mails when signature is used
            signature.getMails().clear();

            dto.put("signatureId", signatureId_int);
            ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_DELETE, dto, resultDTO);

            deleteSignatureFreeText(htmlSignatureId);
            deleteSignatureFreeText(textSignatureId);
        }
    }

    /**
     * Read a signature
     *
     * @param signatureId is the id of the signature
     * @return is a SignatureBean
     */
    public Signature readSignature(String signatureId) {
        Integer signatureId_int = new Integer(signatureId);
        SignatureDTO dto = new SignatureDTO();
        dto.put("signatureId", signatureId_int);
        Signature signature = (Signature) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, dto, resultDTO);

        resultDTO.put("signatureHtmlMessage", readSignatureFreeText(signature.getHtmlSignatureId()));
        resultDTO.put("signatureMessage", readSignatureFreeText(signature.getTextSignatureId()));

        return (signature);
    }

    private String readSignatureFreeText(Integer freeTextId) {
        FreeText freeText = (FreeText)
                EJBFactory.i.callFinder(new FreeTextDTO(), "findByPrimaryKey", new Integer[]{freeTextId});

        return new String(freeText.getValue());
    }

    private void deleteSignatureFreeText(Integer freeTextId) {
        FreeText freeText = (FreeText)
                EJBFactory.i.callFinder(new FreeTextDTO(), "findByPrimaryKey", new Integer[]{freeTextId});
        try {
            freeText.remove();
        } catch (RemoveException e) {
            log.warn("Signature Freetext was deleted by other user.");
        }
    }

    private void updateSignatureFreeText(String message, Integer freeTextId) {
        FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
        try {
            freeTextHome.findByPrimaryKey(freeTextId).setValue(message.getBytes());
        } catch (FinderException e) {
            log.warn("Cannot find TextFreeText to signature " + freeTextId);
        }
    }


    private void changeDefaultSignature(Integer userMailId, Integer mailAccountId) {
        Signature signature = getDefaultSignature(userMailId, mailAccountId);
        if (null != signature) {
            signature.setIsDefault(false);
        }
    }

    private void manageSignatureImages(Integer signatureId, String htmlSignature, SessionContext ctx) {
        SignatureImageCmd signatureImageCmd = new SignatureImageCmd();
        signatureImageCmd.putParam("htmlSignature", htmlSignature);
        signatureImageCmd.putParam("signatureId", signatureId);
        signatureImageCmd.setOp("manageImages");
        signatureImageCmd.executeInStateless(ctx);
    }
}
