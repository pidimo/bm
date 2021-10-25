package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.cmd.common.DownloadFreeTextCmd;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ReminderTextDownloadAction extends ReminderLevelAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if (null != (forward = validateReminderLevelExistence(request, mapping))) {
            return forward;
        }

        String freeTextId = request.getParameter("dto(freeTextId)");

        DownloadFreeTextCmd cmd = new DownloadFreeTextCmd();
        cmd.putParam("type", "temp");
        cmd.putParam("fid", freeTextId);
        ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);

        if (resultDTO.isFailure()) {
            log.debug("Downlad InvoiceText freeText=" + freeTextId + " FAIL");
            ActionErrors errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
            saveErrors(request, errors);

            String reminderLevelId = request.getParameter("dto(reminderLevelId)");
            String name = request.getParameter("dto(name)");
            ActionForwardParameters parameters = new ActionForwardParameters();
            parameters.add("reminderLevelId", reminderLevelId).
                    add("dto(reminderLevelId)", reminderLevelId).
                    add("dto(name)", name).
                    add("dto(op)", "read");

            return parameters.forward(mapping.findForward("Fail"));
        }

        ByteArrayOutputStream stream = (ByteArrayOutputStream) cmd.getOutputStream();
        String fileName = getDocumentName(freeTextId);

        MimeTypeUtil.formatResponseToDownloadFile(fileName, stream.size(), response);

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
