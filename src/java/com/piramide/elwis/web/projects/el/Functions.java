package com.piramide.elwis.web.projects.el;

import com.piramide.elwis.cmd.projects.ProjectTimeLimitCmd;
import com.piramide.elwis.cmd.projects.ProjectUtilsCmd;
import com.piramide.elwis.dto.projects.ProjectDTO;
import com.piramide.elwis.dto.projects.ProjectTimeLimitDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ProjectConstants;
import com.piramide.elwis.utils.ProjectUserPermissionUtil;
import com.piramide.elwis.utils.webmail.SecureConnectionType;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.piramide.elwis.utils.ProjectConstants.*;
import static com.piramide.elwis.utils.ProjectUserPermissionUtil.Permission;

/**
 * Project module EL utility functions
 *
 * @author Fernando Montao
 * @version $Id: Functions.java 2009-02-21 18:02:43 $
 */
public class Functions {

    private static Log log = LogFactory.getLog(Functions.class);

    /**
     * Return the label value list of the to be invoiced project types
     *
     * @param servletRequest the request
     * @return the list of types
     */
    public static List getToBeInvoicedTypes(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List<LabelValueBean> types = new ArrayList<LabelValueBean>();
        types.add(new LabelValueBean(JSPHelper.getMessage(request, "Project.toBeInvoiced.allTimes"),
                ToBeInvoicedType.ALL_TIMES.getAsString()));
        types.add(new LabelValueBean(JSPHelper.getMessage(request, "Project.toBeInvoiced.noTimes"),
                ToBeInvoicedType.NO_TIMES.getAsString()));
        types.add(new LabelValueBean(JSPHelper.getMessage(request, "Project.toBeInvoiced.depends"),
                ToBeInvoicedType.DEPENDS.getAsString()));
        return types;
    }

    /**
     * Return the label value list of the project statuses
     *
     * @param servletRequest the request
     * @return the list of types
     */
    public static List getProjectStatuses(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List<LabelValueBean> types = new ArrayList<LabelValueBean>();
        types.add(new LabelValueBean(JSPHelper.getMessage(request, "Project.status.entered"),
                ProjectStatus.ENTERED.getAsString()));
        types.add(new LabelValueBean(JSPHelper.getMessage(request, "Project.status.opened"),
                ProjectStatus.OPENED.getAsString()));
        types.add(new LabelValueBean(JSPHelper.getMessage(request, "Project.status.closed"),
                ProjectStatus.CLOSED.getAsString()));
        types.add(new LabelValueBean(JSPHelper.getMessage(request, "Project.status.finished"),
                ProjectStatus.FINISHED.getAsString()));
        types.add(new LabelValueBean(JSPHelper.getMessage(request, "Project.status.invoiced"),
                ProjectStatus.INVOICED.getAsString()));

        return types;
    }

    /**
     * Return the label of the project status based on its value code.
     *
     * @param servletRequest the request
     * @param status         the status of the project
     * @return the label of the status
     */
    public static String getProjectStatusLabel(javax.servlet.ServletRequest servletRequest, Object status) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        int statusValue = Integer.valueOf(String.valueOf(status));
        if (statusValue == ProjectStatus.ENTERED.getValue()) {
            return JSPHelper.getMessage(request, "Project.status.entered");
        } else if (statusValue == ProjectStatus.OPENED.getValue()) {
            return JSPHelper.getMessage(request, "Project.status.opened");
        } else if (statusValue == ProjectStatus.CLOSED.getValue()) {
            return JSPHelper.getMessage(request, "Project.status.closed");
        } else if (statusValue == ProjectStatus.FINISHED.getValue()) {
            return JSPHelper.getMessage(request, "Project.status.finished");
        } else {
            return JSPHelper.getMessage(request, "Project.status.invoiced");
        }
    }

    /**
     * This method check if project exists
     *
     * @param projectId project identifier
     * @return true if exists false in another case
     */
    public static boolean existsProject(Object projectId) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(
                ProjectConstants.TABLE_PROJECT,
                "projectid",
                projectId,
                errors, new ActionError("Project.NotFound"));
        return errors.isEmpty();
    }

    /**
     * Return project associated to projectId
     *
     * @param request   The request
     * @param projectId project identifier
     * @return ProjectDTO object if project exists null in another case
     */
    public static ProjectDTO getProject(HttpServletRequest request, String projectId) {
        ProjectUtilsCmd projectUtilsCmd = new ProjectUtilsCmd();
        projectUtilsCmd.putParam("projectId", Integer.valueOf(projectId));
        projectUtilsCmd.setOp("getProject");

        ProjectDTO projectDTO = null;
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(projectUtilsCmd, request);
            projectDTO = (ProjectDTO) resultDTO.get("getProject");
        } catch (AppLevelException e) {
            log.error("-> Execute " + ProjectUtilsCmd.class.getName() + " FAIL ", e);
        }

        return projectDTO;
    }

    /**
     * This method retuns Strig object that contain the all permissions of project user
     *
     * @param servletRequest ServletRequest Object
     * @param permission     String than contain the permision to process
     * @return Strig object that contain the all permissions of project user
     */
    public static String getProjectUserPermissions(javax.servlet.ServletRequest servletRequest,
                                                   String permission) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        int permissionValue = 0;
        if (null != permission && !"".equals(permission.trim())) {
            permissionValue = Integer.valueOf(permission);
        }

        String result = "";

        List<ProjectUserPermissionUtil.Permission> permissions =
                ProjectUserPermissionUtil.extractPermissions(permissionValue);
        for (int i = 0; i < permissions.size(); i++) {
            ProjectUserPermissionUtil.Permission element = permissions.get(i);
            if (Permission.ADMIN.equals(element)) {
                result = JSPHelper.getMessage(request, "ProjectAssignee.permission.admin") + result;
            }
            if (Permission.CONFIRM.equals(element)) {
                result = JSPHelper.getMessage(request, "ProjectAssignee.permission.confirm") + result;
            }
            if (Permission.NEW.equals(element)) {
                result = JSPHelper.getMessage(request, "ProjectAssignee.permission.new") + result;
            }
            if (Permission.VIEW.equals(element)) {
                result = JSPHelper.getMessage(request, "ProjectAssignee.permission.view") + result;
            }

            if (i < permissions.size() - 1) {
                result = ", " + result;
            }
        }

        return result;
    }

    /**
     * Return the label value list of the project time statuses
     *
     * @param servletRequest the request
     * @return the list of types
     */
    public static List getProjectTimeStatuses(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List<LabelValueBean> types = new ArrayList<LabelValueBean>();
        types.add(new LabelValueBean(JSPHelper.getMessage(request, "ProjectTime.status.entered"),
                ProjectTimeStatus.ENTERED.getAsString()));
        types.add(new LabelValueBean(JSPHelper.getMessage(request, "ProjectTime.status.released"),
                ProjectTimeStatus.RELEASED.getAsString()));
        types.add(new LabelValueBean(JSPHelper.getMessage(request, "ProjectTime.status.confirmed"),
                ProjectTimeStatus.CONFIRMED.getAsString()));
        types.add(new LabelValueBean(JSPHelper.getMessage(request, "ProjectTime.status.notConfirmed"),
                ProjectTimeStatus.NOT_CONFIRMED.getAsString()));
        types.add(new LabelValueBean(JSPHelper.getMessage(request, "ProjectTime.status.invoiced"),
                ProjectTimeStatus.INVOICED.getAsString()));
        return types;
    }

    public static boolean hasProjectUserPermission(String projectId,
                                                   ProjectUserPermissionUtil.Permission permission,
                                                   javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User sessionUser = RequestUtils.getUser(request);
        Integer userId = (Integer) sessionUser.getValue(Constants.USERID);
        Integer addressId = (Integer) sessionUser.getValue(Constants.USER_ADDRESSID);

        ProjectDTO projectDTO = getProject(request, projectId);
        if (null != projectDTO) {
            Integer responsibleId = (Integer) projectDTO.get("responsibleId");
            if (responsibleId.equals(userId)) {
                return true;
            }
        }

        ProjectUtilsCmd projectUtilsCmd = new ProjectUtilsCmd();
        projectUtilsCmd.putParam("projectId", Integer.valueOf(projectId));
        projectUtilsCmd.putParam("addressId", addressId);
        projectUtilsCmd.putParam("permission", permission.getValue());
        projectUtilsCmd.setOp("hasProjectUserPermission");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(projectUtilsCmd, request);
            return (Boolean) resultDTO.get("hasProjectUserPermission");
        } catch (AppLevelException e) {
            log.error("-> Execute " + ProjectUtilsCmd.class.getName() + " FAIL", e);
        }

        return false;
    }

    public static boolean hasProjectUserPermission(String projectId, String permissionName,
                                                   javax.servlet.ServletRequest servletRequest) {
        ProjectUserPermissionUtil.Permission permission = Permission.getPermission(permissionName);
        return hasProjectUserPermission(projectId, permission, servletRequest);
    }

    public static boolean isUserOfProject(String projectId, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User sessionUser = RequestUtils.getUser(request);
        Integer userId = (Integer) sessionUser.getValue(Constants.USERID);
        Integer addressId = (Integer) sessionUser.getValue(Constants.USER_ADDRESSID);
        Integer companyId = (Integer) sessionUser.getValue(Constants.COMPANYID);

        ProjectDTO projectDTO = getProject(request, projectId);
        if (null != projectDTO) {
            Integer responsibleId = (Integer) projectDTO.get("responsibleId");
            if (responsibleId.equals(userId)) {
                return true;
            }
        }

        ProjectUtilsCmd projectUtilsCmd = new ProjectUtilsCmd();
        projectUtilsCmd.putParam("projectId", Integer.valueOf(projectId));
        projectUtilsCmd.putParam("addressId", addressId);
        projectUtilsCmd.putParam("companyId", companyId);
        projectUtilsCmd.setOp("isUserOfProject");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(projectUtilsCmd, request);
            return (Boolean) resultDTO.get("isUserOfProject");
        } catch (AppLevelException e) {
            log.error("-> Execute " + ProjectUtilsCmd.class.getName() + " FAIL", e);
        }

        return false;
    }

    public static void setDefaultValuesForProjectUsers(DefaultForm defaultForm,
                                                       HttpServletRequest request) {

        String addressId = (String) defaultForm.getDto("addressId");
        if (GenericValidator.isBlankOrNull(addressId)) {
            defaultForm.setDto("responsibleAddressId", "");

            defaultForm.setDto(ProjectUserPermissionUtil.Permission.NEW.getName().toLowerCase(), false);
            defaultForm.setDto(ProjectUserPermissionUtil.Permission.VIEW.getName().toLowerCase(), false);
            defaultForm.setDto(ProjectUserPermissionUtil.Permission.CONFIRM.getName().toLowerCase(), false);
            defaultForm.setDto(ProjectUserPermissionUtil.Permission.ADMIN.getName().toLowerCase(), false);
            return;
        }

        String projectId = request.getParameter("projectId");
        ProjectDTO projectDTO = getProject(request, projectId);
        Integer responsibleAddressId = (Integer) projectDTO.get("responsibleAddressId");

        defaultForm.setDto("responsibleAddressId", responsibleAddressId);
        defaultForm.setDto("userName", com.piramide.elwis.web.contactmanager.el.Functions.getAddressName(addressId));
        defaultForm.setDto(ProjectUserPermissionUtil.Permission.NEW.getName().toLowerCase(), true);
        defaultForm.setDto(ProjectUserPermissionUtil.Permission.VIEW.getName().toLowerCase(), false);
        defaultForm.setDto(ProjectUserPermissionUtil.Permission.CONFIRM.getName().toLowerCase(), false);
        defaultForm.setDto(ProjectUserPermissionUtil.Permission.ADMIN.getName().toLowerCase(), false);

        if (addressId.equals(responsibleAddressId.toString())) {
            defaultForm.setDto(ProjectUserPermissionUtil.Permission.VIEW.getName().toLowerCase(), true);
            defaultForm.setDto(ProjectUserPermissionUtil.Permission.CONFIRM.getName().toLowerCase(), true);
            defaultForm.setDto(ProjectUserPermissionUtil.Permission.ADMIN.getName().toLowerCase(), true);
        }
    }

    public static List getProjectsByUser(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User sessionUser = RequestUtils.getUser(request);
        Integer userId = (Integer) sessionUser.getValue(Constants.USERID);
        Integer addressId = (Integer) sessionUser.getValue(Constants.USER_ADDRESSID);
        Integer companyId = (Integer) sessionUser.getValue(Constants.COMPANYID);

        ProjectUtilsCmd projectUtilsCmd = new ProjectUtilsCmd();
        projectUtilsCmd.putParam("responsibleId", userId);
        projectUtilsCmd.putParam("addressId", addressId);
        projectUtilsCmd.putParam("companyId", companyId);
        projectUtilsCmd.setOp("getProjectsByUser");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(projectUtilsCmd, request);
            List<ProjectDTO> projectDTOs = (List<ProjectDTO>) resultDTO.get("getProjectsByUser");
            return projectDTOs;

        } catch (AppLevelException e) {
            log.error("-> Execute " + ProjectUtilsCmd.class.getName() + " FAIL ", e);
        }

        return new ArrayList();
    }

    /**
     * Return the mail account secure connection types
     *

     * @return the list of types
     */
    public static List getMailAccountSecureConnectionTypes() {
        List<LabelValueBean> types = new ArrayList<LabelValueBean>();
        types.add(new LabelValueBean(SecureConnectionType.SSL.label(), SecureConnectionType.SSL.value().toString()));
        types.add(new LabelValueBean(SecureConnectionType.STARTTLS.label(), SecureConnectionType.STARTTLS.value().toString()));
        return types;
    }

    public static Map calculateProjectTimesByAssigneeSubProject(Object projectId, Object assigneeId, Object subProjectId) {
        return calculateProjectTimesByAssigneeSubProject(projectId, assigneeId, subProjectId, null);
    }

    public static Map calculateProjectTimesByAssigneeSubProject(Object projectId, Object assigneeId, Object subProjectId, Object timeId) {
        log.debug("Calculate total times registered on projectId:" + projectId + " assigneeId:" + assigneeId + " subProjectId:" + subProjectId + " timeId:" + timeId);

        BigDecimal totalInvoiceTime = new BigDecimal(0);
        BigDecimal totalNoInvoiceTime = new BigDecimal(0);

        if (projectId != null && !GenericValidator.isBlankOrNull(projectId.toString())
                && assigneeId != null && !GenericValidator.isBlankOrNull(assigneeId.toString())
                && subProjectId != null && !GenericValidator.isBlankOrNull(subProjectId.toString())) {

            ProjectUtilsCmd projectUtilsCmd = new ProjectUtilsCmd();
            projectUtilsCmd.setOp("sumProjectTimesByAssigneeSubProject");
            projectUtilsCmd.putParam("projectId", projectId);
            projectUtilsCmd.putParam("assigneeId", assigneeId);
            projectUtilsCmd.putParam("subProjectId", subProjectId);

            if (timeId != null) {
                projectUtilsCmd.putParam("timeId", timeId);
            }

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(projectUtilsCmd, null);
                totalInvoiceTime = (BigDecimal) resultDTO.get("sumInvoiceTimes");
                totalNoInvoiceTime = (BigDecimal) resultDTO.get("sumNoInvoiceTimes");

            } catch (AppLevelException e) {
                log.error("-> Execute " + ProjectUtilsCmd.class.getName() + " FAIL ", e);
            }
        }

        Map map = new HashMap();
        map.put("totalInvoiceTime", totalInvoiceTime);
        map.put("totalNoInvoiceTime", totalNoInvoiceTime);

        return map;
    }

    public static Map calculateProjectTimesBySubProject(Object projectId, Object subProjectId) {
        log.debug("Calculate total times registered on projectId:" + projectId + " subProjectId:" + subProjectId);

        BigDecimal totalInvoiceTime = new BigDecimal(0);
        BigDecimal totalNoInvoiceTime = new BigDecimal(0);

        if (projectId != null && !GenericValidator.isBlankOrNull(projectId.toString())
                && subProjectId != null && !GenericValidator.isBlankOrNull(subProjectId.toString())) {

            ProjectUtilsCmd projectUtilsCmd = new ProjectUtilsCmd();
            projectUtilsCmd.setOp("sumProjectTimesBySubProject");
            projectUtilsCmd.putParam("projectId", projectId);
            projectUtilsCmd.putParam("subProjectId", subProjectId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(projectUtilsCmd, null);
                totalInvoiceTime = (BigDecimal) resultDTO.get("sumInvoiceTimes");
                totalNoInvoiceTime = (BigDecimal) resultDTO.get("sumNoInvoiceTimes");

            } catch (AppLevelException e) {
                log.error("-> Execute " + ProjectUtilsCmd.class.getName() + " FAIL ", e);
            }
        }

        Map map = new HashMap();
        map.put("totalInvoiceTime", totalInvoiceTime);
        map.put("totalNoInvoiceTime", totalNoInvoiceTime);

        return map;
    }

    public static ProjectTimeLimitDTO readProjectTimeLimitByAssigneeSubProject(Object projectId, Object assigneeId, Object subProjectId) {
        ProjectTimeLimitDTO projectTimeLimitDTO = null;

        ProjectTimeLimitCmd limitCmd = new ProjectTimeLimitCmd();
        limitCmd.setOp("readByAssigneeSubProject");
        limitCmd.putParam("projectId", projectId);
        limitCmd.putParam("assigneeId", assigneeId);
        limitCmd.putParam("subProjectId", subProjectId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(limitCmd, null);
            projectTimeLimitDTO = (ProjectTimeLimitDTO) resultDTO.get("dtoProjectTimeLimit");

        } catch (AppLevelException e) {
            log.error("-> Execute " + ProjectTimeLimitCmd.class.getName() + " FAIL ", e);
        }

        return projectTimeLimitDTO;
    }


}
