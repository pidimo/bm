package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.contactmanager.delegate.dataimport.DataImportDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2.5
 */
public class ImportProfileProgressBarAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ImportProfileProgressBarAction................" + request.getParameterMap());
        ActionForward forward = null;

        DefaultForm defaultForm = (DefaultForm) form;

        Long importStartTime = Long.valueOf(defaultForm.getDto("importStartTime").toString());
        Integer totalRows = DataImportDelegate.i.readImportProfileTotalRecord(importStartTime);

        setXmlResponse(totalRows, response);
        return forward;
    }

    private void setXmlResponse(Integer totalRows, HttpServletResponse response) {
        StringBuffer xmlResponse = new StringBuffer();

        xmlResponse.append("<?xml version=\"1.0\" ?>\n");
        xmlResponse.append("<importProgressBar>");
        xmlResponse.append("<summary ")
                .append(" totalRecords=\"").append(totalRows).append("\"")
                .append(">\n");
        xmlResponse.append("</summary>");
        xmlResponse.append("</importProgressBar>");

        setDataInResponse(response, "text/xml", xmlResponse.toString());
    }

    private void setDataInResponse(HttpServletResponse response, String contentType, String data) {
        log.debug("Response Value:\n" + data);

        response.setContentType(contentType);
        try {
            PrintWriter write = response.getWriter();
            write.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
