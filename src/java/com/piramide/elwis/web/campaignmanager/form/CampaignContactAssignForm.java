package com.piramide.elwis.web.campaignmanager.form;

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
 * @version $Id: CampaignContactAssignForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CampaignContactAssignForm extends DefaultForm {
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

    public Object[] getListViewContactIds() {
        List list = (List) this.getDto("listViewContactIds");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setListViewContactIds(Object[] list) {
        if (list != null) {
            this.setDto("listViewContactIds", Arrays.asList(list));
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate CampaignContactAssignForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        if (getDto("selected") == null) {
            setDto("selected", new ArrayList());
        }

        if (getDto("listViewContactIds") == null) {
            log.debug("campaign contact list view is null...");
            setDto("listViewContactIds", new ArrayList());
        }

        return errors;
    }
}

