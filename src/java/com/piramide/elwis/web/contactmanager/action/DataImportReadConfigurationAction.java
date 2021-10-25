package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.contactmanager.el.DataImportHelper;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class DataImportReadConfigurationAction extends DefaultAction {


    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String htmlResult = getMultipleSelect(request);

        //Select Option;
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "max-age=0");
        try {
            PrintWriter writer = response.getWriter();
            writer.write(Functions.filterAjaxResponse(htmlResult));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return null;
    }

    private String getMultipleSelect(HttpServletRequest request) {
        DataImportHelper dataImportHelper = new DataImportHelper();
        String configurationType = request.getParameter("configurationType");

        if (GenericValidator.isBlankOrNull(configurationType)) {
            return dataImportHelper.getEmptyMultipleSelect();
        }

        if (ContactConstants.ImportProfileType.ORGANIZATION.equal(configurationType)) {
            return dataImportHelper.getOrganizationMultipleSelect(request);
        }
        if (ContactConstants.ImportProfileType.PERSON.equal(configurationType)) {
            return dataImportHelper.getContactMultipleSelect(request);
        }
        if (ContactConstants.ImportProfileType.ORGANIZATION_AND_CONTACT_PERSON.equal(configurationType)) {
            return dataImportHelper.getOrganizationAndContactPersonMultipleSelect(request);
        }

        return dataImportHelper.getEmptyMultipleSelect();
    }
}
