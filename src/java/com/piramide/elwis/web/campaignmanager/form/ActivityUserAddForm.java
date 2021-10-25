package com.piramide.elwis.web.campaignmanager.form;

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
 * multiple select to add users in activity
 *
 * @author Miky
 * @version $Id: ActivityUserAddForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ActivityUserAddForm extends DefaultForm {
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
        log.debug("Excecuting validate ActivityUserAddForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        if (getDto("selected") == null || ((List) getDto("selected")).isEmpty()) {
            errors.add("empty", new ActionError("Campaign.activity.addUser.emptySelected"));
            setDto("selected", new ArrayList());
        }

        return errors;
    }
}

