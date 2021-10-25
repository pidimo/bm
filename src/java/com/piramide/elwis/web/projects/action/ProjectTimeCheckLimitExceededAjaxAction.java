package com.piramide.elwis.web.projects.action;

import com.piramide.elwis.dto.projects.ProjectTimeLimitDTO;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.projects.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Action to check limit exceeded of time registered
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public class ProjectTimeCheckLimitExceededAjaxAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ProjectTimeCheckLimitExceededAjaxAction................" + request.getParameterMap());
        log.debug("toBeInvoiced :" + request.getParameter("toBeInvoiced"));

        ActionForward forward = null;

        DefaultForm defaultForm = (DefaultForm) form;

        boolean toBeInvoiced = false;
        if ("true".equals(request.getParameter("toBeInvoiced"))) {
            toBeInvoiced = true;
        }

        String projectId = request.getParameter("projectId");
        String assigneeId = request.getParameter("assigneeId");
        String subProjectId = request.getParameter("subProjectId");

        checkLimitExceededByAssignee(projectId, assigneeId, subProjectId, toBeInvoiced, request, response);

        return forward;
    }

    private void checkLimitExceededByAssignee(String projectId, String assigneeId, String subProjectId, boolean toBeInvoiced, HttpServletRequest request, HttpServletResponse response) {
        boolean isLimitExceeded = false;

        if (!GenericValidator.isBlankOrNull(projectId)
                && !GenericValidator.isBlankOrNull(assigneeId)
                && !GenericValidator.isBlankOrNull(subProjectId)) {

            ProjectTimeLimitDTO readTimeLimitDto = Functions.readProjectTimeLimitByAssigneeSubProject(projectId, assigneeId, subProjectId);

            if (readTimeLimitDto != null) {
                Boolean hasTimeLimit = (Boolean) readTimeLimitDto.get("hasTimeLimit");

                Map previousTotalMap = Functions.calculateProjectTimesByAssigneeSubProject(projectId, assigneeId, subProjectId);

                BigDecimal invoiceLimit = (BigDecimal) readTimeLimitDto.get("invoiceLimit");
                BigDecimal noInvoiceLimit = (BigDecimal) readTimeLimitDto.get("noInvoiceLimit");

                if (toBeInvoiced) {
                    BigDecimal total = (BigDecimal) previousTotalMap.get("totalInvoiceTime");
                    isLimitExceeded = limitHasExceeded(total, invoiceLimit);
                } else {
                    BigDecimal total = (BigDecimal) previousTotalMap.get("totalNoInvoiceTime");
                    isLimitExceeded = limitHasExceeded(total, noInvoiceLimit);
                }
            }
        }

        setXmlResponse(isLimitExceeded, request, response);
    }

    private boolean limitHasExceeded(BigDecimal total, BigDecimal limit) {
        boolean isExceeded = false;
        if (total != null && limit != null) {
            if (total.compareTo(limit) == 1) {
                isExceeded = true;
            }
        }
        return isExceeded;
    }

    private void setXmlResponse(boolean isLimitExceeded, HttpServletRequest request, HttpServletResponse response) {
        StringBuffer xmlResponse = new StringBuffer();

        xmlResponse.append("<?xml version=\"1.0\" ?>\n");
        xmlResponse.append("<checkLimit>");
        xmlResponse.append("<limitExceeded ")
                .append(" value=\"").append(isLimitExceeded).append("\"")
                .append("/>\n");
        xmlResponse.append("</checkLimit>");

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
