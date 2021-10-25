package com.piramide.elwis.web.reports.form;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10.1
 */
public class QueryParamForm extends FilterCreateForm {

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate QueryParamForm......." + getDtoMap());

        return super.validate(mapping, request);
    }

    @Override
    protected boolean isAllowedNullValues() {
        return true;
    }
}
