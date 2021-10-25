package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.action.CheckEntriesForwardAction;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.JavaScriptEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * Action Forward class, used to put jscript to select recently created contact. This from a
 * popup creation window.
 *
 * @author Fernando Montaño
 * @version $Id: AddressCreateSelectPopupAction.java 11331 2015-10-23 18:57:59Z a.garcia $
 */

public class AddressCreateSelectPopupAction extends CheckEntriesForwardAction {
    private Log log = LogFactory.getLog(AddressCreateSelectPopupAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug(" AddressCreateSelectPopupAction executing...");

        Map dtoValues = (Map) request.getAttribute("dto"); //the DTO in the request, after creation.
        Object addressId = dtoValues.get("addressId");
        String addressName = (String) dtoValues.get("addressName");

        String stringOpenUrl;
        if (Functions.isBootstrapUIMode(request)) {
            stringOpenUrl = selectAndCloseModal(addressId, addressName);
        } else {
            stringOpenUrl = selectAndClosePopup( addressId,addressName);
        }

        request.setAttribute("jsLoad", stringOpenUrl);

        return super.execute(mapping, form, request, response); //execute the forward
    }

    private String selectAndClosePopup(Object addressId,String addressName) {
        StringBuffer openUrl = new StringBuffer();

        return openUrl.append("onload=\"")
                .append("opener.selectField('fieldAddressId_id', '").append(addressId)
                .append("', 'fieldAddressName_id', '").append(JavaScriptEncoder.encode(addressName)).append("');\"").toString();
    }

    private String selectAndCloseModal(Object addressId,String addressName){
        StringBuffer openUrl = new StringBuffer();

        return openUrl.append("onload=\"")
                .append("parent.selectField('fieldAddressId_id', '").append(addressId)
                .append("', 'fieldAddressName_id', '").append(JavaScriptEncoder.encode(addressName)).append("');\"").toString();
    }
}
