package com.piramide.elwis.cmd.projects;

import com.piramide.elwis.domain.project.*;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.projects.ProjectDTO;
import com.piramide.elwis.utils.BigDecimalUtils;
import com.piramide.elwis.utils.ProjectConstants;
import com.piramide.elwis.utils.ProjectUserPermissionUtil;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.piramide.elwis.utils.ProjectConstants.ProjectTimeStatus;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProjectUtilsCmd extends ProjectManagerCmd {
    private Log log = LogFactory.getLog(ProjectUtilsCmd.class);

    @Override
    public void executeInStateless(SessionContext sessionContext) {
        if ("getProject".equals(getOp())) {
            Integer projectId = (Integer) paramDTO.get("projectId");
            getProject(projectId);
        }
        if ("hasProjectUserPermission".equals(getOp())) {
            Integer projectId = (Integer) paramDTO.get("projectId");
            Integer addressId = (Integer) paramDTO.get("addressId");
            Integer permissionValue = (Integer) paramDTO.get("permission");
            hasProjectUserPermission(projectId,
                    addressId,
                    ProjectUserPermissionUtil.Permission.getPermission(permissionValue));
        }
        if ("sumProjectTimesByDate".equals(getOp())) {
            Integer projectId = (Integer) paramDTO.get("projectId");
            Integer assigneeId = (Integer) paramDTO.get("assigneeId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            Integer date = (Integer) paramDTO.get("date");
            Integer projectTimeId = (Integer) paramDTO.get("projectTimeId");
            String op = (String) paramDTO.get("operator");
            sumProjectTimesByDate(projectId, assigneeId, companyId, date, projectTimeId, op);
        }
        if ("sumProjectTimesByProject".equals(getOp())) {
            Integer projectId = (Integer) paramDTO.get("projectId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            Integer projectTimeId = (Integer) paramDTO.get("projectTimeId");
            sumProjectTimesByProject(projectId, companyId, projectTimeId);
        }
        if ("isUserOfProject".equals(getOp())) {
            Integer addressId = (Integer) paramDTO.get("addressId");
            Integer projectId = (Integer) paramDTO.get("projectId");
            Integer companyId = (Integer) paramDTO.get("companyId");

            isUserOfProject(addressId, projectId, companyId);
        }
        if ("getProjectsByUser".equals(getOp())) {
            Integer responsibleId = (Integer) paramDTO.get("responsibleId");
            Integer addressId = (Integer) paramDTO.get("addressId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            getProjectsByUser(responsibleId, addressId, companyId);
        }

        if ("sumProjectTimesByAssigneeSubProject".equals(getOp())) {
            sumTimesByAssigneeSubProject();
        }

        if ("sumProjectTimesBySubProject".equals(getOp())) {
            sumTimesBySubProject();
        }

    }

    private void getProjectsByUser(Integer responsibleId, Integer addressId, Integer companyId) {
        ProjectHome projectHome =
                (ProjectHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT);

        List<ProjectDTO> projects = new ArrayList<ProjectDTO>();

        List<Integer> cacheList = new ArrayList<Integer>();
        try {
            List userProjects = (List) projectHome.findByResposibleId(responsibleId, companyId);
            for (int i = 0; i < userProjects.size(); i++) {
                Project project = (Project) userProjects.get(i);
                cacheList.add(project.getProjectId());
                ProjectDTO dto = new ProjectDTO();
                dto.put("projectId", project.getProjectId());
                dto.put("projectName", project.getName());
                projects.add(dto);
            }
        } catch (FinderException e) {
            log.debug("Read UserProjects Fail");
        }


        ProjectAssigneeHome projectAssigneeHome =
                (ProjectAssigneeHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_ASSIGNEE);

        try {
            List assignedProjects = (List) projectAssigneeHome.findAssigneedProjects(addressId, companyId);
            for (int i = 0; i < assignedProjects.size(); i++) {
                ProjectAssignee assignee = (ProjectAssignee) assignedProjects.get(i);
                if (cacheList.contains(assignee.getProjectId())) {
                    continue;
                }

                cacheList.add(assignee.getProjectId());

                ProjectDTO dto = new ProjectDTO();
                dto.put("projectId", assignee.getProjectId());
                dto.put("projectName", assignee.getProject().getName());
                projects.add(dto);
            }
        } catch (FinderException e) {
            log.debug("Read Assigned Projects FAIL");
        }

        resultDTO.put("getProjectsByUser", projects);
    }

    private boolean isUserOfProject(Integer addressId, Integer projectId, Integer companyId) {
        ProjectAssigneeHome projectAssigneeHome =
                (ProjectAssigneeHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_ASSIGNEE);

        boolean result = false;
        try {
            ProjectAssignee projectAssignee =
                    projectAssigneeHome.findByAddressId(addressId, projectId, companyId);

            if (null != projectAssignee) {
                result = true;
            }
        } catch (FinderException e) {
            result = false;
        }

        log.debug("-> Person  addressId=" + addressId + "  is user for project projectId=" + projectId + ": " + result);
        resultDTO.put("isUserOfProject", result);
        return result;
    }

    @Override
    protected ProjectDTO getProject(Integer projectId) {
        ProjectDTO projectDTO = super.getProject(projectId);
        if (null != projectDTO) {
            UserDTO userDTO = getUserDTO((Integer) projectDTO.get("responsibleId"));
            projectDTO.put("responsibleAddressId", userDTO.get("addressId"));
            resultDTO.put("getProject", projectDTO);
        }


        return projectDTO;
    }

    private boolean hasProjectUserPermission(Integer projectId,
                                             Integer addressId,
                                             ProjectUserPermissionUtil.Permission permission) {

        ProjectAssigneeHome projectAssigneeHome =
                (ProjectAssigneeHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_ASSIGNEE);

        ProjectAssigneePK projectAssigneePK = new ProjectAssigneePK();
        projectAssigneePK.projectId = projectId;
        projectAssigneePK.addressId = addressId;

        ProjectAssignee projectAssignee;
        try {
            projectAssignee = projectAssigneeHome.findByPrimaryKey(projectAssigneePK);
        } catch (FinderException e) {
            resultDTO.put("hasProjectUserPermission", false);
            return false;
        }
        boolean hasProjectUserPermission =
                ProjectUserPermissionUtil.hasPermission(projectAssignee.getPermission(), permission);
        log.debug("-> Permission: " +
                permission.getName() +
                " for addressId=" +
                addressId +
                " in projectId =" + projectId + " is " + hasProjectUserPermission);

        resultDTO.put("hasProjectUserPermission", hasProjectUserPermission);

        return hasProjectUserPermission;
    }

    private void sumProjectTimesByProject(Integer projectId,
                                          Integer companyId,
                                          Integer projectTimeId) {
        BigDecimal toBeInvoicedEqualTrue = sumProjectTimesByProject(projectId, companyId, true);
        BigDecimal toBeInvoicedEqualFalse = sumProjectTimesByProject(projectId, companyId, false);

        ProjectTime projectTime = getProjectTime(projectTimeId);
        if (null != projectTime &&
                (ProjectTimeStatus.RELEASED.getValue() == projectTime.getStatus() ||
                        ProjectTimeStatus.CONFIRMED.getValue() == projectTime.getStatus())
                ) {
            if (projectTime.getToBeInvoiced()) {
                toBeInvoicedEqualTrue = BigDecimalUtils.subtract(toBeInvoicedEqualTrue, projectTime.getTime());
            } else {
                toBeInvoicedEqualFalse = BigDecimalUtils.subtract(toBeInvoicedEqualFalse, projectTime.getTime());
            }
        }

        resultDTO.put("toBeInvoicedEqualTrue", toBeInvoicedEqualTrue);
        resultDTO.put("toBeInvoicedEqualFalse", toBeInvoicedEqualFalse);
    }

    protected BigDecimal sumProjectTimesByDate(Integer projectId,
                                               Integer assigneeId,
                                               Integer companyId,
                                               Integer date,
                                               Integer projectTimeId,
                                               String op) {
        ProjectTimeHome projectTimeHome =
                (ProjectTimeHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_TIME);

        BigDecimal result = new BigDecimal(0);

        try {
            result = projectTimeHome.selectSumTimes(projectId, assigneeId, companyId, date);
            if (null == result) {
                result = new BigDecimal(0);
            }
        } catch (FinderException e) {
            log.debug("-> Cannot sum projectTimes for userId=" + assigneeId);
        }

        if (result.compareTo(new BigDecimal(0)) == 0) {
            resultDTO.put("sumProjectTimesByDate", result);
            return result;
        }

        if ("create".equals(op)) {
            resultDTO.put("sumProjectTimesByDate", result);
            return result;
        }

        if ("update".equals(op)) {
            ProjectTime projectTime = getProjectTime(projectTimeId);
            result = BigDecimalUtils.subtract(result, projectTime.getTime());
            resultDTO.put("sumProjectTimesByDate", result);
            return result;
        }

        resultDTO.put("sumProjectTimesByDate", result);
        return result;
    }

    private void sumTimesByAssigneeSubProject() {
        Integer projectId = new Integer(paramDTO.get("projectId").toString());
        Integer assigneeId = new Integer(paramDTO.get("assigneeId").toString());
        Integer subProjectId = new Integer(paramDTO.get("subProjectId").toString());
        Integer timeId = -1;

        if (paramDTO.get("timeId") != null && !"".equals(paramDTO.get("timeId"))) {
            timeId = new Integer(paramDTO.get("timeId").toString());
        }

        BigDecimal invoiceTimes = null;
        BigDecimal noInvoiceTimes = null;

        ProjectTimeHome projectTimeHome = (ProjectTimeHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_TIME);

        try {
            invoiceTimes = projectTimeHome.selectSumTimesByProjectAssigneeSubProject(projectId, assigneeId, subProjectId, true, timeId);
            noInvoiceTimes = projectTimeHome.selectSumTimesByProjectAssigneeSubProject(projectId, assigneeId, subProjectId, false, timeId);
        } catch (FinderException e) {
            log.debug("Error in sum times...", e);
        }

        if (invoiceTimes == null) {
            invoiceTimes = new BigDecimal(0);
        }
        if (noInvoiceTimes == null) {
            noInvoiceTimes = new BigDecimal(0);
        }

        resultDTO.put("sumInvoiceTimes", invoiceTimes);
        resultDTO.put("sumNoInvoiceTimes", noInvoiceTimes);
    }

    private void sumTimesBySubProject() {
        Integer projectId = new Integer(paramDTO.get("projectId").toString());
        Integer subProjectId = new Integer(paramDTO.get("subProjectId").toString());

        BigDecimal invoiceTimes = null;
        BigDecimal noInvoiceTimes = null;

        ProjectTimeHome projectTimeHome = (ProjectTimeHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_TIME);

        try {
            invoiceTimes = projectTimeHome.selectSumTimesByProjectSubProject(projectId, subProjectId, true);
            noInvoiceTimes = projectTimeHome.selectSumTimesByProjectSubProject(projectId, subProjectId, false);
        } catch (FinderException e) {
            log.debug("Error in sum times...", e);
        }

        if (invoiceTimes == null) {
            invoiceTimes = new BigDecimal(0);
        }
        if (noInvoiceTimes == null) {
            noInvoiceTimes = new BigDecimal(0);
        }

        resultDTO.put("sumInvoiceTimes", invoiceTimes);
        resultDTO.put("sumNoInvoiceTimes", noInvoiceTimes);
    }

    public boolean isStateful() {
        return false;
    }
}
