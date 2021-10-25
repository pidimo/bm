package com.piramide.elwis.web.financemanager.form;

import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: ReminderBulkCreationForm.java 04-nov-2008 14:49:24 $
 */
public class ReminderBulkCreationForm extends DefaultForm {
    private Log log = LogFactory.getLog(this.getClass());

    public Object[] getIdInvoice() {
        List list = (List) this.getDto("idsSelected");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setIdInvoice(Object[] checkArray) {
        if (checkArray != null) {
            this.setDto("idsSelected", Arrays.asList(checkArray));
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate ReminderBulkCreationForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        if (getDto("idsSelected") == null) {
            setDto("idsSelected", new ArrayList());
        }

        //add in request selected invoices to remind
        request.setAttribute("invoiceIdList", (List) getDto("idsSelected"));

        //add in DTO parameters to generate reminder document
        User user = RequestUtils.getUser(request);
        setDto("userId", user.getValue("userId"));
        setDto("userAddressId", user.getValue("userAddressId"));
        setDto("requestLocale", user.getValue("locale"));
        setDto("reminderLabel", JSPHelper.getMessage(request, "InvoiceReminder.reminder"));

        return errors;
    }
}

