package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.contactmanager.form.AddressForm;
import net.java.dev.strutsejb.web.DefaultAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Delete Address Action
 *
 * @author Fernando Montaño
 * @version $Id: AddressDeleteAction.java 9124 2009-04-17 00:35:24Z fernando $
 */
public class AddressDeleteAction extends DefaultAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        AddressForm addressForm = (AddressForm) form;
        if (isCancelled(request)) {
            return new ActionForwardParameters()
                    .add("dto(addressId)", addressForm.getDto("addressId").toString())
                    .add("dto(addressType)", addressForm.getDto("addressType").toString())
                    .add("dto(name1)", addressForm.getDto("name1").toString())
                    .add("dto(name2)", addressForm.getDto("name2").toString())
                    .add("dto(name3)", addressForm.getDto("name3").toString())
                    .forward(mapping.findForward("Cancel"));
        } else {
            return super.execute(mapping, form, request, response);
        }

    }

}
