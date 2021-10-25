package com.piramide.elwis.web.common.action;


import com.piramide.elwis.cmd.common.DownloadFreeTextCmd;
import com.piramide.elwis.cmd.utils.CampaignResultPageList;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * @author Tayes
 * @version $Id: DownloadFreeTextAction.java,v 1.8.4.10 2006/05/29 23:05:05
 */
public class DownloadFreeTextAction extends AbstractDefaultAction {

    private boolean deleteDir(Integer campaignId) {
        File dir = new File(CampaignResultPageList.pathCampaignTemplateFolder(null, campaignId)); //todo: se coloco null para corregir el error, esto fue modificado por alex y creo q no se usa por el momento......
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = new File(dir, children[i]).delete();
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if ("camp".equals(request.getParameter("dto(type)"))) {

            /*ActionErrors errors = new ActionErrors();
            errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN, "campaignid",
                     request.getParameter("campaignId"), errors, new ActionError("error.CampaignSession.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("MainSearch");
            }*/

            String cid = request.getParameter("dto(cid)");
            try {
                Integer campaignId = new Integer(cid);
                deleteDir(campaignId);
            } catch (Exception e) {
                log.debug("Can't delete cache:");
            }
        }

        DefaultForm defaultForm = (DefaultForm) form;

        DownloadFreeTextCmd cmd = new DownloadFreeTextCmd();
        cmd.putParam(defaultForm.getDtoMap());
        cmd.putParam("update", request.getSession().getAttribute("UPDATE_FIELDS"));
        ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
        request.getSession().removeAttribute("UPDATE_FIELDS");

        if (resultDTO.isFailure()) {
            log.debug("Fail Download Document");
            ActionErrors errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
            saveErrors(request, errors);

            if ("camp".equals(request.getParameter("dto(type)"))) {
                return new ActionForwardParameters().add("campaignId", request.getParameter("campaignId"))
                        .forward(mapping.findForward("Fail"));
            } else {
                return mapping.findForward("fail");
            }
        }

        ByteArrayOutputStream stream = (ByteArrayOutputStream) cmd.getOutputStream();
        //FileOutputStream outputStream = new FileOutputStream("down_" + System.currentTimeMillis() + ".doc");
        //stream.writeTo(outputStream);
        log.debug("Sending document");

        String fileName = getDocumentName((String) defaultForm.getDto("fid"));
        int fileSize = stream.size();
        MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);
        ServletOutputStream os = response.getOutputStream();
        stream.writeTo(os);
        os.flush();
        os.close();
        stream.close();
        return null;
    }

    /**
     * Get document name from freetext id
     * @param freeTextId id
     * @return Strign
     */
    private String getDocumentName(String freeTextId) {
        log.debug("Get document name with freetext...." + freeTextId);
        String fileName = null;
        if (!GenericValidator.isBlankOrNull(freeTextId)) {
            fileName = Functions.getFreTextDocumentName(Integer.valueOf(freeTextId));
        }
        if (fileName == null) {
            fileName = "Letter.doc";
        }
        return fileName;
    }

}