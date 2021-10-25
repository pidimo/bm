package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.dto.contactmanager.TelecomWrapperDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.catalogmanager.form.CategoryFieldValueForm;
import com.piramide.elwis.web.catalogmanager.form.CategoryFormUtil;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.upload.FormFile;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Contact person Form handler
 *
 * @author Fernando Montaño
 * @version $Id: ContactPersonForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ContactPersonForm extends DefaultForm {
    private Log log = LogFactory.getLog(ContactPersonForm.class);
    private Map telecomMap;
    private FormFile imageFile;

    public ContactPersonForm() {
        super();
        telecomMap = new LinkedHashMap();
    }

    public FormFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(FormFile imageFile) {
        this.imageFile = imageFile;
    }

    public TelecomWrapperDTO getTelecom(String key) {
        if (telecomMap.containsKey(key)) {
            return (TelecomWrapperDTO) telecomMap.get(key);
        } else {
            telecomMap.put(key, new TelecomWrapperDTO());
        }
        return (TelecomWrapperDTO) telecomMap.get(key);

    }

    public void setTelecom(String key, TelecomWrapperDTO value) {
        telecomMap.put(key, value);
    }


    public Map getTelecomMap() {
        return telecomMap;
    }

    public void setTelecomMap(Map telecomMap) {
        this.telecomMap = telecomMap;
    }

    public boolean getInitTelecoms() {

        Object telecomMap = super.getDto("contactPersonTelecomMap");
        if (telecomMap != null) {
            this.telecomMap = (Map) telecomMap;
        }

        TelecomWrapperDTO.sortTelecomMapByPosition(this.telecomMap);
        return true;
    }

    /**
     * Validate the input fields and set defaults values to dtoMap.
     */
    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 javax.servlet.http.HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        super.setDto("locale", user.getValue("locale"));

        if (super.getDto("isActive") != null) {
            super.setDto("active", new Boolean(true));
        } else {
            super.setDto("active", new Boolean(false));
        }

        if (getDto("lastUpdateDate") != null) { // if is in update mode send It.
            super.setDto("lastUpdateDate", new Date());
        }

        log.debug("Validate has been executed...");


        if (getDto("save") != null && !"delete".equals(getDto("op"))) {
            errors = super.validate(mapping, request);

            String datePattern = JSPHelper.getMessage(request, "datePattern");
            String withoutYearPattern = JSPHelper.getMessage(request, "withoutYearPattern");
            log.debug("Contact Person validate has been executed...");
            // validating the Date
            Date aDate = null;
            boolean isWithOutYear = false;
            String dateString = (String) getDto("birthday");
            if (!GenericValidator.isBlankOrNull(dateString)) {
                try {
                    if (dateString.length() < datePattern.trim().length()) {
                        aDate = DateUtils.formatDate(dateString, withoutYearPattern.trim(), true);
                        isWithOutYear = true;
                    } else {
                        aDate = DateUtils.formatDate(dateString, datePattern.trim(), false);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                if (aDate == null) {
                    errors.add("dateText", new ActionError("Address.error.Person.birthday", datePattern,
                            withoutYearPattern));
                } else {
                    if (isWithOutYear) {
                        super.setDto("birthday", DateUtils.dateToIntegerWithoutYear(aDate));
                        super.setDto("dateWithoutYear", "true"); //define as without year for when a jsp be invoked again.
                    } else {
                        super.setDto("birthday", DateUtils.dateToInteger(aDate));
                    }
                }

            }

            //validating telecoms
            errors = AddressContactPersonHelper.validateTelecoms(errors, request, getDtoMap(), telecomMap, user);

            /** validate the image if it is setted */
            errors = AddressContactPersonHelper.validateImageFile(errors, getDtoMap(), imageFile);


            //categoryfieldValues validator
            setDto("pageCategoryIds", CategoryFieldValueForm.getPageCategories(request));
            CategoryFormUtil utilValidator = new CategoryFormUtil(this.getDtoMap(), request);
            List<ActionError> l = utilValidator.validateCategoryFields();
            int counter = 0;
            for (ActionError r : l) {
                errors.add("catValidation_" + counter, r);
                counter++;
            }
            if (errors.isEmpty()) {
                getDtoMap().putAll(utilValidator.getDateOptionsAsInteger());
                getDtoMap().putAll(utilValidator.getAttachmentsDTOs());
            } else {
                utilValidator.restoreAttachmentFields();
            }


        } else {
            //categoryfieldValues validator
            setDto("pageCategoryIds", CategoryFieldValueForm.getPageCategories(request));
            CategoryFormUtil utilValidator = new CategoryFormUtil(this.getDtoMap(), request);
            utilValidator.validateCategoryFields();
            utilValidator.restoreAttachmentFields();
        }
        return errors;

    }

}
