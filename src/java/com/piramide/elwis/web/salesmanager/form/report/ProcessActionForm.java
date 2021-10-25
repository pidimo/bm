package com.piramide.elwis.web.salesmanager.form.report;

import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Nov 23, 2005
 * Time: 9:46:38 AM
 * To change this template use File | Settings | File Templates.
 */

public class ProcessActionForm extends com.jatun.titus.web.form.ReportGeneratorForm {

    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 HttpServletRequest request) {
        return super.validate(mapping, request);
    }
}
