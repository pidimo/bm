package com.piramide.elwis.web.financemanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
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
 * @version $Id: InvoicePrintForm.java 03-oct-2008 17:28:51 $
 */
public class InvoicePrintForm extends DefaultForm {
    private Log log = LogFactory.getLog(this.getClass());

    public Object[] getSelected() {
        List list = (List) this.getDto("selected");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setSelected(Object[] checkArray) {
        if (checkArray != null) {
            this.setDto("selected", Arrays.asList(checkArray));
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate InvoicePrintForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        if (getDto("selected") == null) {
            setDto("selected", new ArrayList());
        }

        if (!Boolean.valueOf(getDto("isPrintAll").toString())) {
            //validate when selected print button has been pressed
            if (((List) getDto("selected")).isEmpty()) {
                errors.add("empty", new ActionError("Invoice.print.emptyError"));
            }
        }

        return errors;
    }
}

