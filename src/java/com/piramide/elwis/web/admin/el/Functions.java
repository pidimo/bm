package com.piramide.elwis.web.admin.el;

import com.piramide.elwis.cmd.admin.CompanyModuleCmd;
import com.piramide.elwis.cmd.admin.CompanyReadCmd;
import com.piramide.elwis.cmd.admin.CompanyReadLightCmd;
import com.piramide.elwis.cmd.admin.ReadUserCmd;
import com.piramide.elwis.cmd.campaignmanager.LightlyInternalUserCmd;
import com.piramide.elwis.cmd.common.config.SystemConstantCmd;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.common.config.SystemConstantDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SortUtils;
import com.piramide.elwis.utils.SystemLanguage;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.FantabulousUtil;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.util.LabelValueBean;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: ivan
 */
public class Functions {
    private static Log log = LogFactory.getLog(Functions.class);

    public static Boolean createTrialCompany() {
        SystemConstantCmd cmd = new SystemConstantCmd();
        cmd.putParam("type", AdminConstants.COMPANY_CREATION_TYPE);
        try {
            ResultDTO resulDTO = BusinessDelegate.i.execute(cmd, null);
            List constants = (List) resulDTO.get(AdminConstants.COMPANY_CREATION_TYPE);
            for (Object obj : constants) {
                SystemConstantDTO dto = (SystemConstantDTO) obj;
                if (dto.get("name").equals(AdminConstants.SYSTEMCONSTANT_TRIAL) && dto.get("value").equals("1")) {
                    return true;
                }
            }
        } catch (AppLevelException e) {
            log.error("Cannot read System constants \n", e);
        }
        return false;
    }

    /**
     * build <code>List</code> of <code>LabelValueBean</code> that contain company template types.
     * Company.commonTemplate = default company template
     * Company.trialTemplate = trial company template
     *
     * @param servletRequest
     * @return
     */
    public static List getCompanyTemplateTypes(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List result = new ArrayList();

        LabelValueBean commonTemplate = new LabelValueBean(JSPHelper.getMessage(request, "Company.commonTemplate"),
                AdminConstants.CompanyTemplateTypes.defaultTemplate.getConstantAsString());

        LabelValueBean trialTemplate = new LabelValueBean(JSPHelper.getMessage(request, "Company.trialTemplate"),
                AdminConstants.CompanyTemplateTypes.trialTemplate.getConstantAsString());
        result.add(trialTemplate);

        result.add(commonTemplate);

        return result;
    }

    public static String getSystemLanguageResource(String isoValue) {
        String resource = "";
        if (null != SystemLanguage.systemLanguages.get(isoValue)) {
            resource = (String) SystemLanguage.systemLanguages.get(isoValue);
        }
        return resource;
    }

    public static boolean isInternalUser(Object userId) {
        boolean isInternal = false;
        LightlyInternalUserCmd internalUserCmd = new LightlyInternalUserCmd();
        internalUserCmd.putParam("userId", userId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(internalUserCmd, null);
            if (!resultDTO.isFailure()) {
                isInternal = resultDTO.getAsBool("isInternalUser");
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd...", e);
        }

        return isInternal;
    }

    public static boolean hasAssignedFinanceModule(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);
        CompanyModuleCmd companyModuleCmd = new CompanyModuleCmd();
        companyModuleCmd.setOp("hasAssignedFinanceModule");
        companyModuleCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(companyModuleCmd, request);
            return (Boolean) resultDTO.get("hasAssignedFinanceModule");
        } catch (AppLevelException e) {
            log.error("-> Execute " + CompanyModuleCmd.class.getName() + " FAIL ", e);
            return false;
        }
    }

    public static boolean hasAssignedLexwareModule(ServletRequest servletRequest) {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);
        CompanyModuleCmd companyModuleCmd = new CompanyModuleCmd();
        companyModuleCmd.setOp("hasAssignedLexwareModule");
        companyModuleCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(companyModuleCmd, request);
            return (Boolean) resultDTO.get("hasAssignedLexwareModule");
        } catch (AppLevelException e) {
            log.error("-> Execute " + CompanyModuleCmd.class.getName() + " FAIL ", e);
            return false;
        }
    }

    public static String getCompanyTimeZone(Integer companyId, HttpServletRequest request) {
        CompanyReadCmd cmd = new CompanyReadCmd();
        cmd.setOp("getCompanyTimeZone");
        cmd.putParam("companyId", companyId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            return (String) resultDTO.get("getCompanyTimeZone");
        } catch (AppLevelException e) {
            log.error("Cannot execute " + CompanyReadCmd.class.getName() + " class with op='getCompanyTimeZone'.", e);
        }

        return null;
    }

    /**
     * Get the assigned and available roles for this password change
     * @param passwordChangeId
     * @param servletRequest
     * @return Map
     */
    public static Map<String, List<Map>> getPasswordChangeRoles(String passwordChangeId, ServletRequest servletRequest) {
        Map<String, List<Map>> resultMap = new HashMap<String, List<Map>>();
        List<Map> availableList = new ArrayList<Map>();
        List<Map> assignedList = new ArrayList<Map>();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.addSearchParameter("companyId", companyId.toString());
        fantabulousUtil.setModule("/admin");

        if (GenericValidator.isBlankOrNull(passwordChangeId)) {
            availableList = fantabulousUtil.getData(request, "roleList");
        } else {
            //add the filter
            fantabulousUtil.addSearchParameter("passwordChangeId", passwordChangeId);

            availableList = fantabulousUtil.getData(request, "availableRolePassChangeList");
            assignedList = fantabulousUtil.getData(request, "assignedRolePassChangeList");
        }

        resultMap.put("availableRolePassChange", availableList);
        resultMap.put("assignedRolePassChange", assignedList);

        return resultMap;
    }

    /**
     * List map of user password change, only if this user has been planned to change your password
     * @param userId
     * @param dateTimeZone
     * @param servletRequest
     * @return
     */
    public static List<Map> getPlannedUserPasswordChanges(Integer userId, DateTimeZone dateTimeZone, ServletRequest servletRequest) {
        List<Map> resultList;

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (dateTimeZone == null) {
            dateTimeZone = DateTimeZone.getDefault();
        }
        DateTime currentTime = new DateTime(System.currentTimeMillis(), dateTimeZone);

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.addSearchParameter("userId", userId.toString());
        fantabulousUtil.addSearchParameter("changeTime", String.valueOf(currentTime.getMillis()));
        fantabulousUtil.setModule("/admin");

        resultList = fantabulousUtil.getData(request, "plannedUserPasswordChangeList");
        if (resultList == null) {
            resultList = new ArrayList<Map>();
        }
        return resultList;
    }

    /**
     * Verify if this millis is old related to system millis
     * @param millis
     * @param servletRequest
     * @return true or false
     */
    public static boolean isOldRelatedToCurrentTime(Long millis, ServletRequest servletRequest) {
        boolean isOldTime = false;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (millis != null) {
            User user = RequestUtils.getUser(request);
            DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");

            DateTime dateTime = new DateTime(millis, dateTimeZone);
            DateTime currentDateTime = new DateTime(System.currentTimeMillis(), dateTimeZone);

            isOldTime = dateTime.isBefore(currentDateTime);
        }
        return isOldTime;
    }

    public static List getCompanyTypes(HttpServletRequest request) {
        ArrayList list = new ArrayList();

        for (int i = 0; i < AdminConstants.CompanyType.values().length; i++) {
            AdminConstants.CompanyType companyType = AdminConstants.CompanyType.values()[i];
            list.add(new LabelValueBean(JSPHelper.getMessage(request, companyType.getResource()), companyType.getConstant().toString()));
        }
        return list;
    }

    public static String getCompanyTypeMessage(String type, HttpServletRequest request) {
        String companyTypeMessage = "";
        AdminConstants.CompanyType companyType = AdminConstants.CompanyType.findCompanyType(type);
        if (companyType != null) {
            companyTypeMessage = JSPHelper.getMessage(request, companyType.getResource());
        }
        return companyTypeMessage;
    }

    public static List getUserGroupTypes(HttpServletRequest request) {
        ArrayList list = new ArrayList();

        for (int i = 0; i < AdminConstants.UserGroupType.values().length; i++) {
            AdminConstants.UserGroupType userGroupType = AdminConstants.UserGroupType.values()[i];
            list.add(new LabelValueBean(JSPHelper.getMessage(request, userGroupType.getResource()), userGroupType.getConstant().toString()));
        }
        return SortUtils.orderByProperty(list, "label");
    }

    public static String getUserGroupMessage(String groupType, HttpServletRequest request) {
        String groupTypeMessage = "";
        AdminConstants.UserGroupType userGroupType = AdminConstants.UserGroupType.findUserGroupType(groupType);
        if (userGroupType != null) {
            groupTypeMessage = JSPHelper.getMessage(request, userGroupType.getResource());
        }
        return groupTypeMessage;
    }

    public static boolean isRootUser(String userId) {
        boolean isRoot = false;

        if (userId != null) {
            ReadUserCmd readUserCmd = new ReadUserCmd();
            readUserCmd.putParam("userId", Integer.valueOf(userId));

            try {
                BusinessDelegate.i.execute(readUserCmd, null);
                UserDTO userDTO = (UserDTO) readUserCmd.getResultDTO().get("elwisUser");
                if (null != userDTO) {
                    Boolean isDefaultUser = (Boolean) userDTO.get("isDefaultUser");
                    if (isDefaultUser != null) {
                        isRoot = isDefaultUser;
                    }
                }
            } catch (AppLevelException e) {
                log.error("-> Cannot Execute ReadUserCmd..", e);
            }
        }

        return isRoot;
    }

    public static boolean isCompanyEnabledToMobileAccess(HttpServletRequest request) {
        boolean isEnabled = false;
        User user = RequestUtils.getUser(request);

        CompanyReadLightCmd companyReadLightCmd = new CompanyReadLightCmd();
        companyReadLightCmd.putParam("companyId", user.getValue(Constants.COMPANYID));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(companyReadLightCmd, request);
            if (resultDTO.get("mobileActive") != null) {
                isEnabled = (Boolean) resultDTO.get("mobileActive");
            }
        } catch (AppLevelException e) {
            log.error("Cannot execute " + CompanyReadLightCmd.class.getName(), e);
        }

        return isEnabled;
    }

    public static Map getUserMap(Object userId) {
        Map userMap = new HashMap();

        if (userId != null && !GenericValidator.isBlankOrNull(userId.toString())) {

            ReadUserCmd readUserCmd = new ReadUserCmd();
            readUserCmd.putParam("userId", Integer.valueOf(userId.toString()));

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(readUserCmd, null);

                if (resultDTO.get("elwisUser") != null) {
                    UserDTO userDTO = (UserDTO) readUserCmd.getResultDTO().get("elwisUser");
                    userMap.putAll(userDTO);
                }
            } catch (AppLevelException e) {
                log.error("-> Cannot Execute ReadUserCmd..", e);
            }
        }

        return userMap;
    }

    public static Map getUserMapByAddressId(Object addressId, HttpServletRequest request) {
        Map userMap = new HashMap();

        if (addressId != null && !GenericValidator.isBlankOrNull(addressId.toString())) {

            User user = RequestUtils.getUser(request);

            ReadUserCmd readUserCmd = new ReadUserCmd();
            readUserCmd.setOp("userByAddressId");
            readUserCmd.putParam("addressId", addressId);
            readUserCmd.putParam("companyId", user.getValue(Constants.COMPANYID));

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(readUserCmd, null);

                if (!resultDTO.isFailure()) {
                    userMap.putAll(resultDTO);
                }
            } catch (AppLevelException e) {
                log.error("-> Cannot Execute ReadUserCmd by addressId..", e);
            }
        }

        return userMap;
    }



}
