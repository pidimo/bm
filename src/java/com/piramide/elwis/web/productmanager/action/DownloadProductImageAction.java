package com.piramide.elwis.web.productmanager.action;

import com.piramide.elwis.cmd.productmanager.DownloadProductImageCmd;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.common.util.ThumbImageUtil;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: DownloadProductImageAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class DownloadProductImageAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing DownloadProductImageAction...");
        DownloadProductImageCmd cmd = new DownloadProductImageCmd();
        cmd.putParam(((DefaultForm) form).getDtoMap());
        ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
        if (resultDTO.isFailure()) {
            return null;
        }
        ArrayByteWrapper byteWrapper = (ArrayByteWrapper) resultDTO.get("image");
        byte[] image = byteWrapper.getFileData();

        String fileName = resultDTO.get("fileName").toString();
        String thumb = resultDTO.get("thumbnail").toString();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if ("true".equals(thumb)) {
            InputStream fileInput = new ByteArrayInputStream(image);
            ThumbImageUtil thumbImageUtil = new ThumbImageUtil(fileInput, 250);
            thumbImageUtil.writeThumb(out);
        }


        if (fileName.length() != 0 || request.getParameter("isCompanyLogo") != null) {
            if (request.getParameter("isCompanyLogo") != null) {
                response.setHeader("Cache-Control", "max-age=3600");
            } else {
                response.setHeader("Cache-Control", "max-age=0");
            }
            response.setContentType("image/pjpeg,gif; charset=UTF-8");
            String contentType = "inline; filename=" + fileName;

            response.setHeader("Content-disposition", contentType);
            response.setContentLength(image.length);
        }

        ServletOutputStream os = response.getOutputStream();
        if ("true".equals(thumb)) {
            os.write(out.toByteArray());
        } else {
            os.write(image);
        }
        os.flush();
        return null;
    }
}
