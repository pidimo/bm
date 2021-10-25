package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.CategoryUtil;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.FormatUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.FieldChecks;
import com.piramide.elwis.web.common.validator.NumberFormatValidator;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.*;

/**
 * Author: ivan
 * Date: Oct 11, 2006 - 12:49:16 PM
 */
public class CategoryFormUtil {
    private Log log = LogFactory.getLog(this.getClass());

    private Map dtoMap;
    public final String categoryDtoConstant = "category_";
    public final String multipleSelectProperty = "multipleSelect_";
    public final String categoryNameConstant = "categoryName_";
    private Map multipleSelectOptions = new HashMap();
    private Map dateOptionsAsInteger = new HashMap();
    private Map attachmentsDTOs = new HashMap();
    private List keysSelected = new ArrayList();
    private HttpServletRequest request;
    private double maxAttachSize;
    private double attachSizeCounter = 0;


    public Map getMultipleSelectOptions() {
        return multipleSelectOptions;
    }

    public Map getAttachmentsDTOs() {
        return attachmentsDTOs;
    }

    public Map getDtoMap() {
        return dtoMap;
    }

    public Map getDateOptionsAsInteger() {
        return dateOptionsAsInteger;
    }

    public CategoryFormUtil(Map dtoMap, HttpServletRequest request) {
        this.dtoMap = dtoMap;
        this.request = request;
        User user = RequestUtils.getUser(request);
        maxAttachSize = Double.parseDouble(user.getValue("maxAttachSize").toString());
    }

    public List<ActionError> validateCategoryFields() {
        List<ActionError> l = new ArrayList<ActionError>();
        List<Map> categoryFields = CategoryUtil.recoverCategoryValues(dtoMap);
        if (null != categoryFields) {
            for (Map m : categoryFields) {
                int categoryId = new Integer(m.get("categoryId").toString());
                int type = new Integer(m.get("type").toString());
                Object value = m.get("value");
                String catUIName = m.get("name").toString();
                String attachId = (String) m.get("attachId");
                ActionError error = checkCategory(categoryId, type, value, catUIName, attachId);
                if (null != error) {
                    l.add(error);
                }
            }
        }
        if (!l.isEmpty()) {
            restoreAttachmentFields(categoryFields);
        }

        dtoMap.putAll(multipleSelectOptions);
        return l;
    }

    public void restoreAttachmentFields() {
        List<Map> categoryFields = CategoryUtil.recoverCategoryValues(dtoMap);
        restoreAttachmentFields(categoryFields);
    }

    private void restoreAttachmentFields(List<Map> categoryFields) {
        for (Map categoryField : categoryFields) {
            String categoryId = categoryField.get("categoryId").toString();
            String attachId = (String) categoryField.get("attachId");
            int type = new Integer(categoryField.get("type").toString());
            if (CatalogConstants.CategoryType.ATTACH.getConstantAsInt() == type) {
                if (null != attachId) {
                    dtoMap.put(categoryId, attachId);
                } else {
                    dtoMap.put(categoryId, "");
                }
            }

        }
    }

    private ActionError checkCategory(int categoryId, int type, Object value, String catIUName, String attachId) {

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.setOp("readCategory");
        cmd.putParam("categoryId", categoryId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            boolean existsCategory = (Boolean) resultDTO.get("existsCategory");
            if (!existsCategory) {
                return new ActionError("Category.error.notfoud", catIUName);
            } else {
                Map m = (Map) resultDTO.get("dbCategory");
                int dbType = (Integer) m.get("categoryType");
                if (type != dbType) {
                    return new ActionError("Category.error.type", catIUName);
                } else {
                    return validateValue(categoryId, value, type, catIUName, attachId);
                }
            }
        } catch (AppLevelException e) {
        }
        return null;
    }


    private ActionError validateValue(int categoryId, Object value, int type, String catIUName, String attachId) {
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        if ("".equals(value.toString().trim())) {
            return null;
        }


        if (CatalogConstants.CategoryType.INTEGER.getConstantAsInt() == type) {
            if (!GenericValidator.isInt(value.toString())) {
                return new ActionError("errors.integer", catIUName);
            }
        }
        if (CatalogConstants.CategoryType.DECIMAL.getConstantAsInt() == type) {
            if (!(NumberFormatValidator.VALID == NumberFormatValidator.i.validate(value.toString(), locale, 11, 2))) {
                return new ActionError("error.decimalNumber.invalid", catIUName);
            } else {
                String unformattedValue = FormatUtils.unformatingDecimalNumber(value.toString(), locale, 11, 2).toString();
                String dtoName = String.valueOf(categoryId);
                dtoMap.put(dtoName, unformattedValue);
            }
        }
        if (CatalogConstants.CategoryType.DATE.getConstantAsInt() == type) {
            Arg arg0 = new Arg();
            arg0.setKey(catIUName);
            Field f = new Field();
            f.setProperty("aaa1");
            f.addArg0(arg0);
            Map bean = new HashMap();
            bean.put("aaa1", value.toString());

            if (!FieldChecks.validDate(bean, new ValidatorAction(), f, new ActionErrors(), request)) {
                return new ActionError("errors.date", catIUName, JSPHelper.getMessage(request, "datePattern"));
            } else {
                Date d = DateUtils.formatDate(value.toString(), JSPHelper.getMessage(request, "datePattern"));
                Integer dAsInt = DateUtils.dateToInteger(d);

                String dtoName = String.valueOf(categoryId);
                dateOptionsAsInteger.put(dtoName, dAsInt);
            }
        }
        if (CatalogConstants.CategoryType.SINGLE_SELECT.getConstantAsInt() == type) {

            List<String> keys = new ArrayList<String>();
            if (null != value) {
                keys = Arrays.asList(new String[]{value.toString()});
            }

            if (!keys.isEmpty()) {
                CategoryUtilCmd cmd = new CategoryUtilCmd();
                cmd.putParam("categoryValuesKeys", keys);
                cmd.setOp("readCategoryValue");
                try {
                    ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
                    boolean existsAllCategoryValues = (Boolean) resultDTO.get("existsAllCategoryValues");
                    if (!existsAllCategoryValues) {
                        return new ActionError("Caregory.values.error", catIUName);
                    }
                } catch (AppLevelException e) {
                }
            }
        }
        if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() == type) {
            List<String> keys = (List<String>) value;

            String dtoName = String.valueOf(categoryId);

            multipleSelectOptions.put(dtoName, keys);

            if (null != keys && !keys.isEmpty()) {
                CategoryUtilCmd cmd = new CategoryUtilCmd();
                cmd.putParam("categoryValuesKeys", keys);
                cmd.setOp("readCategoryValue");
                try {
                    ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
                    boolean existsAllCategoryValues = (Boolean) resultDTO.get("existsAllCategoryValues");
                    if (!existsAllCategoryValues) {
                        return new ActionError("Caregory.values.error", catIUName);
                    }
                } catch (AppLevelException e) {
                }
            }
        }
        if (CatalogConstants.CategoryType.ATTACH.getConstantAsInt() == type) {
            FormFile file = (FormFile) value;
            if (file.getFileSize() == 0) {
                return new ActionError("Category.values.fileNotFound", catIUName);
            }

            double fileSize = Math.round(file.getFileSize() / (Math.pow(1024, 2)));
            attachSizeCounter += fileSize;
            if (maxAttachSize < attachSizeCounter) {
                return new ActionError("Common.Error_FilemaxLengthExceeded", String.valueOf(attachSizeCounter));
            }

            try {
                ArrayByteWrapper wrapper = new ArrayByteWrapper(file.getFileData());
                wrapper.setFileName(file.getFileName());
                String dtoName = String.valueOf(categoryId);
                attachmentsDTOs.put(dtoName, wrapper);
            } catch (IOException e) {
                log.debug("->Read File " + catIUName + " FAIL");
            }
        }

        return null;
    }


    public List getKeysSelected() {
        return keysSelected;
    }
}
