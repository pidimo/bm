package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.dynamicsearch.action.DynamicSearchListAction;
import com.piramide.elwis.web.common.dynamicsearch.form.DynamicSearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages request from user in contact manager module.
 * Mainly this class check existence of working addressId, if address has been deleted by other user
 * redirect with error message, otherwise call super and execute respective command.
 *
 * @author Fernando Montaño?
 * @version $Id: AddressAdvancedSearchAction.java 10438 2014-04-29 22:34:40Z miguel $
 */

public class AddressAdvancedSearchAction extends DynamicSearchListAction {

    /**
     * Check if addressId exists, if exists continue, otherwise cancel operation and return contact search page
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("AddressAdvancedSearchAction execution...");

        return super.execute(mapping, form, request, response);
    }

    @Override
    protected void processBeforeExecuteList(DynamicSearchForm dynamicSearchForm, HttpServletRequest request) {
        String telecom = (String) dynamicSearchForm.getPropertyFilterMap().get("telecomNumberContain");
        if (telecom != null && telecom.trim().length() > 0) {
            StringBuffer sb = new StringBuffer();
            char[] t = telecom.toCharArray();
            for (int i = 0; i < t.length; i++) {
                sb.append("%").append(t[i]);
            }
            addFilter("telecomNumberP", sb.toString());
        }

        //if additional address as selected as filter
        String additionalAddressName = (String) dynamicSearchForm.getPropertyFilterMap().get("addAddressNameContain");
        if (additionalAddressName != null && additionalAddressName.trim().length() > 0) {
            addFilter("evaluateAddAddress", "true");
        }

    }

}