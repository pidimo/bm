package com.piramide.elwis.web.webmail.form;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: FilterForm.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class FilterForm extends WebmailDefaultForm {

    public void setIdentifyCondition(Object[] identify) {
        if (identify != null) {
            setDto("identifyCondition", Arrays.asList(identify));
        } else {
            setDto("identifyCondition", new ArrayList());
        }
    }

    public Object[] getIdentifyCondition() {
        List identify = (List) getDto("identifyCondition");
        return (identify != null ? identify.toArray() : new ArrayList().toArray());
    }

    public void setUserFolder(Object[] folder) {
        if (folder != null) {
            setDto("userFolder", Arrays.asList(folder));
        } else {
            setDto("userFolder", new ArrayList());
        }
    }

    public Object[] getUserFolder() {
        List folder = (List) getDto("userFolder");
        return (folder != null ? folder.toArray() : new ArrayList().toArray());
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("FilterForm.validate(ActionMapping, HttpServletRequest)");
        log.debug("FilterForm.getDtoMap()  : " + this.getDtoMap());


        ActionErrors errors = super.validate(mapping, request);

        if (!conditionsTextIsValid(errors)) {
            errors.add("text", new ActionError("Webmail.filter.condition.required"));
        }

        //set user folders
        List folders = (List) getDto("userFolder");
        log.debug("foldesessssssssssssssssssss" + getDto("userFolder"));
        if (folders != null && !folders.isEmpty()) {
            List folderList = new ArrayList();
            for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
                String s = (String) iterator.next();
                String[] values = s.split("<s>");
                Map mapFolder = new HashMap(2);
                mapFolder.put("userFolderId", values[0]);
                mapFolder.put("userFolder", values[1]);
                log.debug("valuessssssssssssssssssssssssssssssssssssss::" + values[0] + " " + values[1]);
                folderList.add(mapFolder);
            }
            request.setAttribute("formUserFolders", folderList);
        }

        return errors;
    }

    private boolean conditionsTextIsValid(ActionErrors errors) {
        log.debug("conditionsTextIsValid(ActionErrors)");

        boolean res = false;

        List identifyTemp = (List) getDto("identifyCondition");
        if (identifyTemp != null && !identifyTemp.isEmpty()) {
            for (Iterator iterator = identifyTemp.iterator(); iterator.hasNext();) {
                String identify = iterator.next().toString();
                String value = (String) getDto("text" + identify);
                if (!GenericValidator.isBlankOrNull(value)) {
                    res = true;
                    if (GenericValidator.isBlankOrNull(getDto("conditionkey" + identify).toString())) {
                        errors.add("conditionkey", new ActionError("Webmail.filter.conditionKey.required", value));
                    }
                }
            }
        }

        log.debug("conditionsTextIsValid() = " + res);
        return res;
    }

}
