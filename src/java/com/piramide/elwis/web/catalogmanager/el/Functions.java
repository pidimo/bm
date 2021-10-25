package com.piramide.elwis.web.catalogmanager.el;

import com.piramide.elwis.cmd.catalogmanager.*;
import com.piramide.elwis.dto.catalogmanager.*;
import com.piramide.elwis.dto.catalogmanager.categoryUtil.WebCategoryTab;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import java.util.*;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class Functions {
    private static Log log = LogFactory.getLog(Functions.class);

    public static boolean isChildrenCategoryInOtherTab(String childrenCategoryId,
                                                       String categoryId,
                                                       String moduleTable,
                                                       javax.servlet.ServletRequest servletRequest) {
        log.debug("Executing isChildrenCategoryInOtherTab with moduletable:" + moduleTable);
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        CategoryCmd categoryCmd = new CategoryCmd();
        categoryCmd.setOp("isChildrenCategoryInOtherTab");
        categoryCmd.putParam("categoryId", Integer.valueOf(categoryId));
        categoryCmd.putParam("childrenCategoryId", Integer.valueOf(childrenCategoryId));
        if (!GenericValidator.isBlankOrNull(moduleTable)) {
            categoryCmd.putParam("moduleTable", moduleTable);
        }

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(categoryCmd, request);
            return (Boolean) resultDTO.get("isChildrenCategoryInOtherTab");
        } catch (AppLevelException e) {
            log.debug("->Execute " + CategoryCmd.class.getName() + "FAIL", e);
        }
        return true;
    }


    public static boolean checkRelation(String categoryValueId,
                                        String categoryId,
                                        javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        CategoryUtilCmd utilCmd = new CategoryUtilCmd();
        utilCmd.setOp("checkRelation");
        utilCmd.putParam("categoryValueId", Integer.valueOf(categoryValueId));
        utilCmd.putParam("categoryId", Integer.valueOf(categoryId));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(utilCmd, request);
            return (Boolean) resultDTO.get("checkRelation");
        } catch (AppLevelException e) {
            log.debug("->Execute " + CategoryUtilCmd.class.getName() + "FAIL", e);
        }
        return false;
    }

    public static String buildAJAXRequest(Map formMap, javax.servlet.ServletRequest servletRequest) {
        String requestParameters = "";

        for (Object object : formMap.entrySet()) {
            Map.Entry entry = (Map.Entry) object;
            String key = entry.getKey().toString();
            try {
                Integer.valueOf(key);
            } catch (NumberFormatException nfe) {
                continue;
            }
            Object value = formMap.get(key);
            if (null != value) {
                String paramValue = "";

                if (value instanceof List) {
                    paramValue = buildListParameter((List) value);
                }

                if (value instanceof Number) {
                    paramValue = value.toString();
                }

                if (value instanceof String) {
                    paramValue = com.piramide.elwis.web.common.el.Functions.encode(value.toString());
                }

                if (!"".equals(paramValue)) {
                    requestParameters += "\"" + key + "=\"" + "+encodeURI('" + paramValue + "')+\"&\"+";
                }
            }
        }

        //var poststr = "mytextarea1=" + encodeURI( document.getElementById("mytextarea1").value ) + "&mytextarea2=" + encodeURI( document.getElementById("mytextarea2").value );
        requestParameters += "\"auxparam=\"+encodeURI('none')";
        return requestParameters;
    }

    public static void setAJAXRequestCache(Map formMap, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Map cache = new HashMap();
        cache.putAll(formMap);
        log.debug("-----------> " + formMap);
        request.getSession().setAttribute("AJAXCache", formMap);
    }

    private static String buildListParameter(List list) {
        if (null == list) {
            return "[]";
        }

        String result = "";
        int index = 0;
        for (Object object : list) {
            result += object;
            if (index < list.size() - 1) {
                result += ",";
            }

            index++;
        }
        return "[" + result + "]";
    }

    private static List buildListFromParameter(String listParam) {
        if ("[]".equals(listParam)) {
            return new ArrayList();
        }

        String listElements = listParam.replaceAll("(\\[)", "").replaceAll("(\\])", "").trim();
        String[] listValues = listElements.split("(,)");
        if (null == listValues) {
            return new ArrayList();
        }

        return Arrays.asList(listValues);

    }

    public static String encodeDownloadAction(String downloadAction) {
        return downloadAction.replaceAll("(/)", "-").replaceAll("(\\?)", "#").replaceAll("(&)", "*").replaceAll("(\\.)", "@");
    }

    private static String decodeDownloadAction(String downloadAction) {
        return downloadAction.replaceAll("(-)", "/").replaceAll("(#)", "?").replaceAll("(\\*)", "&").replaceAll("(@)", ".");
    }

    public static String buildAJAXMessage(String key, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String html = "<table><tr><td class=\"message\"> " +
                JSPHelper.getMessage(request, key) +
                "</td></tr></table>";
        return html;
    }

    public static void readCacheInAJAXRequest(PageContext pageContext, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String formName = request.getParameter("formName");
        String operation = request.getParameter("operation");
        String encodedDownloadAction = request.getParameter("downloadAction");
        String labelWidth = request.getParameter("labelWidth");
        String containWidth = request.getParameter("containWidth");
        String generalWidth = request.getParameter("generalWidth");
        String downloadAction = "";
        if (null != encodedDownloadAction) {
            downloadAction = decodeDownloadAction(encodedDownloadAction);
        }

        Integer elementCounter = 1;
        try {
            elementCounter = Integer.valueOf(request.getParameter("elementCounter"));
        } catch (NumberFormatException nfe) {
        } catch (NullPointerException npe) {
        }


        Map requestParameters = request.getParameterMap();

        DefaultForm defaultForm = new DefaultForm();
        if (null != formName &&
                !"".equals(formName.trim()) &&
                null != request.getAttribute(formName)) {
            defaultForm = (DefaultForm) request.getAttribute(formName);
        }

        for (Object object : requestParameters.entrySet()) {
            Map.Entry entry = (Map.Entry) object;
            String parameterKey = entry.getKey().toString();
            String value = request.getParameter(parameterKey);

            //list Value
            if (value.indexOf('[') == 0 && value.indexOf(']') == value.length() - 1) {
                List valueAsList = buildListFromParameter(value);
                defaultForm.setDto(parameterKey, valueAsList);
                continue;
            }

            //is float
            if (value.indexOf('.') != -1) {
                try {
                    Float floatValue = Float.valueOf(value);
                    defaultForm.setDto(parameterKey, floatValue);
                    continue;
                } catch (NumberFormatException nfe) {
                }
            }

            //is integer
            try {
                Integer integerValue = Integer.valueOf(value);
                defaultForm.setDto(parameterKey, integerValue);
                continue;
            } catch (NumberFormatException nfe) {
            }

            //is string
            defaultForm.setDto(parameterKey, com.piramide.elwis.web.common.el.Functions.decode(value));
        }

        pageContext.setAttribute("org.apache.struts.taglib.html.BEAN", defaultForm, 2);

        request.setAttribute("formName", formName);
        request.setAttribute("operation", operation);
        request.setAttribute("elementCounter", elementCounter);
        request.setAttribute("downloadAction", downloadAction);
        request.setAttribute("labelWidth", labelWidth);
        request.setAttribute("containWidth", containWidth);
        request.setAttribute("generalWidth", generalWidth);
    }

    public static Object getFormField(String formName,
                                      String attributeName,
                                      javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Object formAsObject = request.getAttribute(formName);
        if (formAsObject instanceof DefaultForm) {
            DefaultForm defaultForm = (DefaultForm) formAsObject;
            return defaultForm.getDto(attributeName);
        }
        return null;
    }


    public static void buildMultipleSelectValues(List allValues,
                                                 List selectedValues,
                                                 javax.servlet.ServletRequest servletRequest) {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List<CategoryValueDTO> selectedElements = new ArrayList<CategoryValueDTO>();
        List<CategoryValueDTO> residualElements = new ArrayList<CategoryValueDTO>(allValues);

        List<Integer> selectedValuesAsInteger = new ArrayList<Integer>();
        if (null == selectedValues) {
            request.setAttribute("selectedElements", selectedElements);
            request.setAttribute("residualElements", residualElements);
            return;
        } else {
            for (Object object : selectedValues) {
                if (object instanceof Integer) {
                    selectedValuesAsInteger.add((Integer) object);
                    continue;
                }
                selectedValuesAsInteger.add(Integer.valueOf(object.toString()));
            }
        }


        if (null != allValues) {
            for (Object object : allValues) {
                CategoryValueDTO dto = (CategoryValueDTO) object;
                Integer categoryValueId = (Integer) dto.get("categoryValueId");
                if (selectedValuesAsInteger.contains(categoryValueId)) {
                    selectedElements.add(dto);
                    residualElements.remove(dto);
                }
            }
        }

        request.setAttribute("selectedElements", selectedElements);
        request.setAttribute("residualElements", residualElements);
    }

    public static List readChildrenCategories(String categoryValueId, javax.servlet.ServletRequest servletRequest) {

        if ("".equals(categoryValueId)) {
            return new ArrayList<Integer>();
        }

        List<Integer> childrenCategoryIds = new ArrayList<Integer>();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        CategoryUtilCmd categoryUtilCmd = new CategoryUtilCmd();
        categoryUtilCmd.setOp("readChildrenCategories");
        categoryUtilCmd.putParam("categoryValueId", Integer.valueOf(categoryValueId));
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(categoryUtilCmd, request);
            childrenCategoryIds = (List<Integer>) resultDTO.get("childrenCategoryIds");
        } catch (AppLevelException e) {
            log.debug("->Execute " + CategoryUtilCmd.class.getName() + "FAIL", e);
        }

        return childrenCategoryIds;
    }

    public static List buildCategoryTabs(String table, String finderName, List params, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);

        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.setOp("buildCategoryTabs");
        cmd.putParam("companyId", companyId);
        cmd.putParam("table", table);
        cmd.putParam("finderName", finderName);
        cmd.putParam("params", params.toArray());

        List<CategoryTabDTO> categoryTabs = new ArrayList<CategoryTabDTO>();
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            categoryTabs = (List<CategoryTabDTO>) resultDTO.get("categoryTabs");
        } catch (AppLevelException e) {
            log.debug("-> Execute " + CategoryUtilCmd.class.getName() + " FAIL");
        }
        return categoryTabs;
    }

    public static List buildCategoriesWithoutGroups(String table, String secondTable, javax.servlet.ServletRequest servletRequest) {
        log.debug("Executing buildCategoriesWithoutGroups with second table:" + secondTable);
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.setOp("readCategoryWithoutGroups");
        cmd.putParam("table", table);
        cmd.putParam("companyId", companyId);
        if (!GenericValidator.isBlankOrNull(secondTable)) {
            cmd.putParam("secondTable", secondTable);
        }

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            return (List) resultDTO.get("categoriesWithoutGroup");
        } catch (AppLevelException e) {
            log.debug("->Execute " + CategoryUtilCmd.class.getName() + " FAIL");
        }

        return new ArrayList();
    }

    public static List buildGroupsWithoutTabs(String table, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.setOp("readGroupsWithoutTabs");
        cmd.putParam("table", table);
        cmd.putParam("companyId", companyId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            return (List) resultDTO.get("categoryGroups");
        } catch (AppLevelException e) {
            log.debug("->Execute " + CategoryUtilCmd.class.getName() + " FAIL");
        }

        return new ArrayList();
    }


    public static WebCategoryTab buildCategoryTab(String categoryTabId,
                                                  javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.setOp("buildCategoryTab");
        cmd.putParam("categoryTabId", Integer.valueOf(categoryTabId));

        WebCategoryTab categoryTab = null;
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            categoryTab = (WebCategoryTab) resultDTO.get("categoryTab");
        } catch (AppLevelException e) {
            log.debug("-> Execute " + CategoryUtilCmd.class.getName() + " FAIL");
        }
        return categoryTab;
    }

    public static List readCategoryValues(String categoryId,
                                          javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        CategoryValueCmd categoryValueCmd = new CategoryValueCmd();
        categoryValueCmd.setOp("readCategoyValues");
        categoryValueCmd.putParam("categoryId", Integer.valueOf(categoryId));
        List<CategoryValueDTO> categoryValues = new ArrayList<CategoryValueDTO>();
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(categoryValueCmd, request);
            categoryValues = (List) resultDTO.get("categoryValues");
        } catch (AppLevelException e) {
            log.debug("-> Execute " + CategoryValueCmd.class.getName() + " FAIL");
        }

        return categoryValues;
    }

    public static CategoryDTO getCategory(String categoryId, javax.servlet.ServletRequest servletRequest) {
        return getCategory(categoryId, null, servletRequest);
    }

    public static CategoryDTO getCategory(String categoryId, String moduleTable, javax.servlet.ServletRequest servletRequest) {
        log.debug("Executing getCategory with module table:" + moduleTable + " categoryID=" + categoryId);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        CategoryCmd categoryCmd = new CategoryCmd();
        categoryCmd.setOp("readCategory");
        categoryCmd.putParam("categoryId", Integer.valueOf(categoryId));
        if (!GenericValidator.isBlankOrNull(moduleTable)) {
            categoryCmd.putParam("moduleTable", moduleTable);
        }

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(categoryCmd, request);
            CategoryDTO categoryDTO = (CategoryDTO) resultDTO.get("categoryDTO");
            return categoryDTO;
        } catch (AppLevelException e) {
            log.debug("->Execute " + CategoryCmd.class.getName() + " FAIL");
        }
        return null;
    }

    public static Boolean isCategorySingleSelect(String categoryType) {
        return CatalogConstants.CategoryType.SINGLE_SELECT.getConstant().equals(categoryType);
    }

    public static Boolean isCategoryCompoundSelect(String categoryType) {
        return CatalogConstants.CategoryType.COMPOUND_SELECT.getConstant().equals(categoryType);
    }

    public static Boolean isCategoryDate(String categoryType) {
        return CatalogConstants.CategoryType.DATE.getConstant().equals(categoryType);
    }

    public static Boolean isCategoryDecimal(String categoryType) {
        return CatalogConstants.CategoryType.DECIMAL.getConstant().equals(categoryType);
    }

    public static Boolean isCategoryInteger(String categoryType) {
        return CatalogConstants.CategoryType.INTEGER.getConstant().equals(categoryType);
    }

    public static Boolean isCategoryText(String categoryType) {
        return CatalogConstants.CategoryType.TEXT.getConstant().equals(categoryType);
    }

    public static Boolean isLinkValue(String categoryType) {
        return CatalogConstants.CategoryType.LINK_VALUE.getConstant().equals(categoryType);
    }

    public static Boolean isFreText(String categoryType) {
        return CatalogConstants.CategoryType.FREE_TEXT.getConstant().equals(categoryType);
    }

    public static Boolean isAttach(String categoryType) {
        return CatalogConstants.CategoryType.ATTACH.getConstant().equals(categoryType);
    }


    public static Boolean isAddressContactPersonCategory(String tableId) {
        return ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY.equals(tableId);
    }


    /*SequenceRule Constants*/
    public static List getSequenceRuleTypes(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        List<LabelValueBean> sequenceRuleTypes = new ArrayList<LabelValueBean>();
        sequenceRuleTypes.add(
                new LabelValueBean(
                        JSPHelper.getMessage(request, "SequenceRule.type.voucher"),
                        FinanceConstants.SequenceRuleType.VOUCHER.getConstantAsString()
                )
        );
        sequenceRuleTypes.add(
                new LabelValueBean(JSPHelper.getMessage(request, "SequenceRule.type.Customer"),
                        FinanceConstants.SequenceRuleType.CUSTOMER.getConstantAsString())
        );
        sequenceRuleTypes.add(
                new LabelValueBean(JSPHelper.getMessage(request, "SequenceRule.type.Article"),
                        FinanceConstants.SequenceRuleType.ARTICLE.getConstantAsString())
        );
        sequenceRuleTypes.add(
                new LabelValueBean(JSPHelper.getMessage(request, "SequenceRule.type.SupportCase"),
                        FinanceConstants.SequenceRuleType.SUPPORT_CASE.getConstantAsString())
        );
        sequenceRuleTypes.add(
                new LabelValueBean(JSPHelper.getMessage(request, "SequenceRule.type.ContractNumber"),
                        FinanceConstants.SequenceRuleType.PRODUCT_CONTRACT_NUMBER.getConstantAsString())
        );
        return sequenceRuleTypes;
    }

    /*Sequence Rule reset types*/
    public static List getSequeceRuleResetTypes(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        List<LabelValueBean> sequenceRuleResetTypes = new ArrayList<LabelValueBean>();
        sequenceRuleResetTypes.add(
                new LabelValueBean(JSPHelper.getMessage(request, "SequenceRule.resetType.daily"),
                        FinanceConstants.SequenceRuleResetType.Daily.getConstantAsString())
        );
        sequenceRuleResetTypes.add(
                new LabelValueBean(JSPHelper.getMessage(request, "SequenceRule.resetType.monthly"),
                        FinanceConstants.SequenceRuleResetType.Monthly.getConstantAsString())
        );
        sequenceRuleResetTypes.add(
                new LabelValueBean(JSPHelper.getMessage(request, "SequenceRule.resetType.yearly"),
                        FinanceConstants.SequenceRuleResetType.Yearly.getConstantAsString())
        );
        sequenceRuleResetTypes.add(
                new LabelValueBean(JSPHelper.getMessage(request, "SequenceRule.resetType.NoReset"),
                        FinanceConstants.SequenceRuleResetType.NoReset.getConstantAsString())
        );

        return sequenceRuleResetTypes;
    }

    public static String searchLabel(List labelValueList, String value) {
        if (null == labelValueList) {
            return "";
        }

        for (Object object : labelValueList) {
            LabelValueBean labelValueBean = (LabelValueBean) object;
            if (labelValueBean.getValue().equals(value)) {
                return labelValueBean.getLabel();
            }
        }

        return "";
    }


    /**
     * Read Languages for company
     *
     * @param servletRequest javax.servlet.ServletRequest Object
     * @return List of <code>LanguageDTO</code>
     */
    public static List getCompanyLanguages(javax.servlet.ServletRequest servletRequest) {
        List<LanguageDTO> languages = new ArrayList<LanguageDTO>();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        LanguageUtilCmd languageUtilCmd = new LanguageUtilCmd();
        languageUtilCmd.setOp("getCompanyLanguages");
        languageUtilCmd.putParam("companyId", companyId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(languageUtilCmd, request);
            languages = (List<LanguageDTO>) resultDTO.get("getCompanyLanguages");
        } catch (AppLevelException e) {
            log.debug("-> Execute " + LanguageUtilCmd.class.getName() + " FAIL", e);
        }

        return languages;
    }

    public static CurrencyDTO getBasicCurrency(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
        CurrencyCmd currencyCmd = new CurrencyCmd();
        currencyCmd.putParam("companyId", companyId);
        currencyCmd.setOp("getBasicCurrency");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(currencyCmd, request);
            return (CurrencyDTO) resultDTO.get("getBasicCurrency");
        } catch (AppLevelException e) {
            log.error("-> Execute " + CurrencyCmd.class.getName() + " FAIL");
        }
        return null;
    }

    public static List getTemplateMediaType(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List<LabelValueBean> resultList = new ArrayList<LabelValueBean>();
        resultList.add(new LabelValueBean(JSPHelper.getMessage(request, "Template.mediaType.word"), CatalogConstants.MediaType.WORD.getConstantAsString()));
        resultList.add(new LabelValueBean(JSPHelper.getMessage(request, "Template.mediaType.html"), CatalogConstants.MediaType.HTML.getConstantAsString()));
        return resultList;
    }

    public static String getMediaTypeMessage(String mediaTypeConstant, ServletRequest servletRequest) {
        String message = "";
        List<LabelValueBean> mediaTypeList = getTemplateMediaType(servletRequest);
        for (LabelValueBean labelValueBean : mediaTypeList) {
            if (labelValueBean.getValue().equals(mediaTypeConstant)) {
                message = labelValueBean.getLabel();
                break;
            }
        }
        return message;
    }

    public static List getAddressRelationTypeTypeList(HttpServletRequest request) {
        ArrayList list = new ArrayList();

        list.add(new LabelValueBean(JSPHelper.getMessage(request, CatalogConstants.AddressRelationTypeType.INVOICE_ADDRESS.getResource()), CatalogConstants.AddressRelationTypeType.INVOICE_ADDRESS.getConstantAsString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, CatalogConstants.AddressRelationTypeType.HIERACHY.getResource()), CatalogConstants.AddressRelationTypeType.HIERACHY.getConstantAsString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, CatalogConstants.AddressRelationTypeType.OTHERS.getResource()), CatalogConstants.AddressRelationTypeType.OTHERS.getConstantAsString()));

        return list;
    }

}
