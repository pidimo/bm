package com.piramide.elwis.web.supportmanager.el;

import com.piramide.elwis.cmd.admin.UserGroupReadCmd;
import com.piramide.elwis.cmd.supportmanager.SupportCatalogSelectCmd;
import com.piramide.elwis.dto.admin.UserGroupDTO;
import com.piramide.elwis.dto.catalogmanager.PriorityDTO;
import com.piramide.elwis.dto.supportmanager.StateDTO;
import com.piramide.elwis.dto.supportmanager.SupportCaseSeverityDTO;
import com.piramide.elwis.dto.supportmanager.SupportCaseTypeDTO;
import com.piramide.elwis.dto.supportmanager.SupportCaseWorkLevelDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SortUtils;
import com.piramide.elwis.utils.SupportConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelSecurity;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: ivan
 */
public class Functions {
    private static Log log = LogFactory.getLog(Functions.class);

    /**
     * This method returns <code>List</code> of <code>LabelValueBean</code> objects, can contain
     * support priority options to be selected in user interface.
     *
     * @param servletRequest page servlet request
     * @return <code>List</code> of <code>LabelValueBean</code>
     */
    public static List getSupportPriority(javax.servlet.ServletRequest servletRequest) {
        return getSupportCatalog(servletRequest, new PriorityDTO(), "priorityId", "langTextId", "priorityName", "readAll", null);
    }

    /**
     * This method returns <code>List</code> of <code>LabelValueBean</code> objects, can contain support
     * case type options to be selected in user interface.
     *
     * @param servletRequest page servlet request
     * @return <code>List</code> of <code>LabelValueBean</code>
     */
    public static List getSupportCaseType(javax.servlet.ServletRequest servletRequest) {
        return getSupportCatalog(servletRequest, new SupportCaseTypeDTO(), "caseTypeId", "langTextId", "caseTypeName", "readAll", null);
    }

    /**
     * This method returns <code>List</code> of <code>LabelValueBean</code> objects, can contain support
     * severity case options to be selected in user interface.
     *
     * @param servletRequest page servlet request
     * @return <code>List</code> of <code>LabelValueBean</code>
     */
    public static List getSupportSeverityCase(javax.servlet.ServletRequest servletRequest) {
        return getSupportCatalog(servletRequest, new SupportCaseSeverityDTO(), "severityId", "langTextId", "severityName", "readAll", null);
    }

    /**
     * This method returns <code>List</code> of <code>LabelValueBean</code> objects, can contain support
     * work level options to be selected in user interface.
     *
     * @param servletRequest page servlet request
     * @return <code>List</code> of <code>LabelValueBean</code>
     */
    public static List getSupportWorkLevel(javax.servlet.ServletRequest servletRequest) {
        return getSupportCatalog(servletRequest, new SupportCaseWorkLevelDTO(), "workLevelId", "langTextId", "workLevelName", "readAll", null);
    }


    /**
     * This method returns <code>List</code> of <code>LabelValueBean</code> objects, can contain support
     * status options to be selected in user interface.
     *
     * @param servletRequest page servlet request
     * @return <code>List</code> of <code>LabelValueBean</code>
     */
    public static List getSupportStatus(javax.servlet.ServletRequest servletRequest) {
        return getSupportCatalog(servletRequest, new StateDTO(), "stateId", "langTextId", "stateName", "readAll", null);
    }


    /**
     * This methos build and return <code>List</code> of <code>LabelValueBean</code> objects, can contain support
     * catalog options to be selected in user interface.
     * <p/>
     * The catalogs must be translations and this translations must be associated to user interface language.
     *
     * @param servletRequest         servletRequest page servlet request
     * @param dto                    <code>ComponentDTO</code> children  ej: <code>new SupportCaseTypeDTO()</code>
     * @param ejbIdentifierFieldName name of identifier field of catalog
     * @param langTextIdentifierName name of langtext field of catalog
     * @param ejbTextName            name of text field of catalog
     * @return <code>List</code> of <code>LabelValueBean</code>
     */
    private static List getSupportCatalog(javax.servlet.ServletRequest servletRequest,
                                          ComponentDTO dto,
                                          String ejbIdentifierFieldName,
                                          String langTextIdentifierName,
                                          String ejbTextName,
                                          String operation,
                                          Integer primaryKey) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        Integer companyId = (Integer) user.getValue("companyId");
        String isoLanguage = (String) user.getValue("locale");

        //recover translations according to iso language
        SupportCatalogSelectCmd cmd = new SupportCatalogSelectCmd();
        cmd.putParam("companyId", companyId);
        cmd.putParam("componentDTO", dto);
        cmd.putParam("isoLanguage", isoLanguage);
        cmd.putParam("ejbIdentifierFieldName", ejbIdentifierFieldName);
        cmd.putParam("langTextIdentifierName", langTextIdentifierName);
        cmd.putParam("ejbTextName", ejbTextName);
        cmd.putParam("op", operation);
        if (null != primaryKey) {
            cmd.putParam("primaryKey", primaryKey);
        }

        //build result list
        List result = new ArrayList();
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            List translations = (List) resultDTO.get(SupportCatalogSelectCmd.TRANSLATIONS);
            for (int i = 0; i < translations.size(); i++) {
                Map translation = (Map) translations.get(i);
                String id = translation.get(ejbIdentifierFieldName).toString();
                String name = translation.get(SupportCatalogSelectCmd.TEXT_FIELD_NAME).toString();
                result.add(new LabelValueBean(name, id));
            }
        } catch (AppLevelException e) {
            log.debug("Cannot read Support Catalog...");
        }
        return result;
    }

    public static void buildArticleUserGroupAccessRightValues(String userGroupIds, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        UserGroupReadCmd userGroupReadCmd = new UserGroupReadCmd();
        userGroupReadCmd.setOp("userGroupArticle");
        userGroupReadCmd.putParam("companyId", companyId);

        List<UserGroupDTO> companyUserGroupsList = new ArrayList<UserGroupDTO>();
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(userGroupReadCmd, request);
            if (resultDTO.get("articleUserGroups") != null) {
                companyUserGroupsList = (List<UserGroupDTO>) resultDTO.get("articleUserGroups");
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + UserGroupReadCmd.class.getName() + " FAIL", e);
        }

        //process the selected user groups
        String[] userGroupIdArray = new String[0];
        if (userGroupIds != null) {
            userGroupIdArray = userGroupIds.split(",");
        }

        ArrayList<LabelValueBean> selectedList = new ArrayList<LabelValueBean>();
        ArrayList<LabelValueBean> availableList = new ArrayList<LabelValueBean>();

        for (UserGroupDTO userGroupDTO : companyUserGroupsList) {
            String userGroupId = userGroupDTO.get("userGroupId").toString();
            LabelValueBean labelValueBean = new LabelValueBean(userGroupDTO.get("groupName").toString(), userGroupId);

            boolean isSelected = existItemInArray(userGroupIdArray, userGroupId);
            if (isSelected) {
                selectedList.add(labelValueBean);
            } else {
                availableList.add(labelValueBean);
            }
        }

        //add creator user item
        LabelValueBean creatorLabelValueBean = new LabelValueBean(JSPHelper.getMessage(request, "Article.accessRight.creatorUser"), SupportConstants.ARTICLE_ACCESS_CREATOR_USER_KEY.toString());
        boolean isCreatorSelected = existItemInArray(userGroupIdArray, SupportConstants.ARTICLE_ACCESS_CREATOR_USER_KEY.toString());
        if (isCreatorSelected) {
            selectedList.add(creatorLabelValueBean);
        } else {
            availableList.add(creatorLabelValueBean);
        }

        request.setAttribute("selectedArticleUserGroupList", SortUtils.orderByProperty(selectedList, "label"));
        request.setAttribute("availableArticleUserGroupList", SortUtils.orderByProperty(availableList, "label"));
    }

    private static boolean existItemInArray(String[] array, String value) {
        if (value != null) {
            for (String arrayValue : array) {
                if (value.equals(arrayValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean userWithAccessToArticleOnDataLevel(String articleId, HttpServletRequest request) {
        boolean userWithAccess = false;

        if (!GenericValidator.isBlankOrNull(articleId)) {
            userWithAccess = AccessRightDataLevelSecurity.i.userWithAccessToArticleOnDataLevel(new Integer(articleId), request);
        }
        return userWithAccess;
    }

}
