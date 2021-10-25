package com.piramide.elwis.web.productmanager.form;

import com.piramide.elwis.cmd.productmanager.ProductReadCmd;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.catalogmanager.form.CategoryFieldValueForm;
import com.piramide.elwis.web.catalogmanager.form.CategoryFormUtil;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.productmanager.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Product Form handler.
 *
 * @author Fernando Montaño
 * @version $Id: ProductForm.java 12479 2016-02-15 20:29:29Z miguel $
 */
public class ProductForm extends DefaultForm {

    private Log log = LogFactory.getLog(ProductForm.class);

    @Override
    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);
    }

    public ProductForm() {
        super();

    }

    /**
     * Validate the input fields and set defaults values to dtoMap.
     */
    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 javax.servlet.http.HttpServletRequest request) {
        log.debug("ProductForm validation execution...");
        ActionErrors errors = new ActionErrors();

        if (getDto("save") != null) {
            errors = super.validate(mapping, request);// validating with super class

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

                //validate event dates
                if (isEventProduct(request)) {
                    validateStartEndDates(errors, request);
                    validateClosingDate(errors, request);
                } else {
                    setDto("initDateTime", null);
                    setDto("endDateTime", null);
                    setDto("closingDateTime", null);
                    setDto("webSiteLink", null);
                    setDto("eventAddress", null);
                    setDto("eventMaxParticipant", null);
                }

            } else {
                utilValidator.restoreAttachmentFields();
            }
        }

        if (!errors.isEmpty() && "update".equals(getDto("op"))) {
            reReadProductCategoryValues(this, request);
        }
        return errors;
    }

    private void reReadProductCategoryValues(DefaultForm productForm, HttpServletRequest request) {
        ProductReadCmd readCmd = new ProductReadCmd();
        readCmd.putParam("productId", productForm.getDto("productId"));
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(readCmd, request);
            productForm.setDto("productCategoryValues", resultDTO.get("productCategoryValues"));
        } catch (AppLevelException e) {
        }
    }

    private void validateStartEndDates(ActionErrors errors, HttpServletRequest request) {
        ActionErrors dateErrors = new ActionErrors();

        Object initDateObj = getDto("initDate");
        Object endDateObj = getDto("endDate");

        if (initDateObj == null || GenericValidator.isBlankOrNull(initDateObj.toString())) {
            dateErrors.add("initRequired", new ActionError("errors.required", JSPHelper.getMessage(request, "Product.startDate")));
        }
        if (endDateObj == null || GenericValidator.isBlankOrNull(endDateObj.toString())) {
            dateErrors.add("endRequired", new ActionError("errors.required", JSPHelper.getMessage(request, "Product.endDate")));
        }

        if (dateErrors.isEmpty()) {
            Integer initDate = (Integer) initDateObj;
            Integer endDate = (Integer) endDateObj;
            if (initDate > endDate) {
                dateErrors.add("errorDate", new ActionError("Contact.Contract.compareDates",
                        JSPHelper.getMessage(request, "Product.startDate"),
                        JSPHelper.getMessage(request, "Product.endDate")));
            }

            if (initDate.equals(endDate)) {
                StringBuffer startT = new StringBuffer()
                        .append(getDto("initHour"))
                        .append(getDto("initMin"));

                StringBuffer endT = new StringBuffer()
                        .append(getDto("endHour"))
                        .append(getDto("endMin"));

                if (new Integer(startT.toString()) >= new Integer(endT.toString())) {
                    dateErrors.add("errorTime", new ActionError("Appointment.greater",
                            JSPHelper.getMessage(request, "Product.startTime"),
                            JSPHelper.getMessage(request, "Product.endTime")));
                }
            }

            if (dateErrors.isEmpty()) {
                //set in dto the date times
                setDto("initDateTime", composeDateTimeMillis(initDate, Integer.parseInt(getDto("initHour").toString()), Integer.parseInt(getDto("initMin").toString()), request));
                setDto("endDateTime", composeDateTimeMillis(endDate, Integer.parseInt(getDto("endHour").toString()), Integer.parseInt(getDto("endMin").toString()), request));
            }
        }

        errors.add(dateErrors);
    }

    private void validateClosingDate(ActionErrors errors, HttpServletRequest request) {
        ActionErrors dateErrors = new ActionErrors();

        Object closeDateObj = getDto("closeDate");

        //initialize date time
        setDto("closingDateTime", null);

        if (closeDateObj != null && !GenericValidator.isBlankOrNull(closeDateObj.toString())) {
            Integer closeDate = (Integer) closeDateObj;
            //set in dto
            setDto("closingDateTime", composeDateTimeMillis(closeDate, Integer.parseInt(getDto("closeHour").toString()), Integer.parseInt(getDto("closeMin").toString()), request));
        }
        errors.add(dateErrors);
    }

    private Long composeDateTimeMillis(Integer date, Integer hour, Integer min, HttpServletRequest request) {
        Long millis = null;

        User user = RequestUtils.getUser(request);
        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");
        if (dateTimeZone == null) {
            dateTimeZone = DateTimeZone.getDefault();
        }

        DateTime dateTime = DateUtils.integerToDateTime(date, hour, min, dateTimeZone);

        if (dateTime != null) {
            millis = dateTime.getMillis();
        }
        return millis;
    }

    private DateTime getCompleteDateTime(Integer date, String time, DateTimeZone zone) {
        String[] hourMinute = time.split(":");
        int yearMonthDay[] = DateUtils.getYearMonthDay(date);
        return new DateTime(yearMonthDay[0], yearMonthDay[1], yearMonthDay[2],
                Integer.parseInt(hourMinute[0]), Integer.parseInt(hourMinute[1]), 0, 0, zone);
    }


    private boolean isEventProduct(HttpServletRequest request) {
        boolean isEvent = false;
        Integer eventTypeId = Functions.findProductTypeIdOfEventType(request);
        Object productTypeId = getDto("productTypeId");

        if (eventTypeId != null && productTypeId != null && !GenericValidator.isBlankOrNull(productTypeId.toString())) {
            isEvent = eventTypeId.equals(Integer.valueOf(productTypeId.toString()));
        }
        return isEvent;
    }

}