package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class DownloadCategoryFieldValueAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm defaultForm = (DefaultForm) form;
        Integer attachId = Integer.valueOf(defaultForm.getDto("attachId").toString());


        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        CategoryUtilCmd categoryUtilCmd = new CategoryUtilCmd();
        categoryUtilCmd.setOp("downloadAttach");
        categoryUtilCmd.putParam("attachId", attachId);
        categoryUtilCmd.putParam("companyId", companyId);
        ResultDTO resultDTO = BusinessDelegate.i.execute(categoryUtilCmd, request);

        InputStream fileStream = (InputStream) resultDTO.get("attach");
        String fileName = (String) resultDTO.get("filename");
        if (null == fileName) {
            fileName = "unknownFileName";
        }

        if (null == fileStream) {
            ActionErrors errors = new ActionErrors();
            errors.add("fileNotFound", new ActionError("Common.error.fileNotFound"));
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        int fileSize = Integer.parseInt(resultDTO.get("attachSize").toString());

        MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);

        ServletOutputStream os = response.getOutputStream();
        try {
            MimeTypeUtil.copy(os, fileStream);
        }
        catch (IOException e) {
            log.debug("Write File in " + ServletOutputStream.class.getName() + " Object FAIL ", e);
        } finally {
            os.flush();
            fileStream.close();
        }
        return null;
    }
}
