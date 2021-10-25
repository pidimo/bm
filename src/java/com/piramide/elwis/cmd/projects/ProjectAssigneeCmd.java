package com.piramide.elwis.cmd.projects;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.project.*;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.projects.ProjectAssigneeDTO;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import com.piramide.elwis.utils.ProjectConstants;
import com.piramide.elwis.utils.ProjectUserPermissionUtil;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.List;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ProjectAssigneeCmd extends ProjectManagerCmd {

    private Log log = LogFactory.getLog(ProjectManagerCmd.class);

    public void executeInStateless(SessionContext context) {
        boolean isRead = true;

        ProjectAssigneeDTO projectAssigneeDTO = getProjectAssigneeDTO();
        if ("create".equals(getOp())) {
            isRead = false;
            create(projectAssigneeDTO);
        }

        if ("update".equals(getOp())) {
            isRead = false;
            update(projectAssigneeDTO, context);
        }

        if ("delete".equals(getOp())) {
            isRead = false;
            delete(projectAssigneeDTO);
        }
        if ("createOrUpdate".equals(getOp())) {
            Integer projectId = (Integer) paramDTO.get("projectId");
            createOrUpdate(projectId);
        }

        if (isRead) {
            boolean withReferences = null != paramDTO.get("withReferences")
                    && "true".equals(paramDTO.get("withReferences").toString());
            read(projectAssigneeDTO, withReferences, context);
        }
    }

    private void createOrUpdate(Integer projectId) {
        ProjectHome projectHome =
                (ProjectHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT);
        Project project;
        try {
            project = projectHome.findByPrimaryKey(projectId);
        } catch (FinderException e) {
            return;
        }

        UserDTO userDTO = getUserDTO(project.getResponsibleId());
        Integer addressId = (Integer) userDTO.get("addressId");

        ProjectAssigneeHome projectAssigneeHome =
                (ProjectAssigneeHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_ASSIGNEE);

        ProjectAssigneePK pk = new ProjectAssigneePK();
        pk.addressId = addressId;
        pk.projectId = projectId;
        try {
            ProjectAssignee projectAssignee = projectAssigneeHome.findByPrimaryKey(pk);
            projectAssignee.setPermission(
                    Byte.valueOf(String.valueOf(ProjectUserPermissionUtil.getAllPermissions()))
            );
        } catch (FinderException e) {
            ProjectAssigneeDTO projectAssigneeDTO = new ProjectAssigneeDTO();
            projectAssigneeDTO.put("companyId", project.getCompanyId());
            projectAssigneeDTO.put("projectId", project.getProjectId());
            projectAssigneeDTO.put("addressId", addressId);
            projectAssigneeDTO.put("permission",
                    Byte.valueOf(String.valueOf(ProjectUserPermissionUtil.getAllPermissions())));

            try {
                projectAssigneeHome.create(projectAssigneeDTO);
            } catch (CreateException e1) {
                log.error("Cannot Create ProjectAssignee Object ", e1);
            }
        }
    }

    private void delete(ProjectAssigneeDTO projectAssigneeDTO) {

        IntegrityReferentialChecker.i.check(projectAssigneeDTO, resultDTO);
        if (resultDTO.isFailure()) {
            resultDTO.setForward("Fail");
            return;
        }

        ProjectAssigneePK projectAssigneePK = getProjectAssigneePK(projectAssigneeDTO);
        projectAssigneeDTO.put(projectAssigneeDTO.getPrimKeyName(), projectAssigneePK);

        ExtendedCRUDDirector.i.delete(projectAssigneeDTO, resultDTO, true, "Fail");

        //remove pk Object from resultDTO
        resultDTO.remove(projectAssigneeDTO.getPrimKeyName());
    }

    private void update(ProjectAssigneeDTO projectAssigneeDTO, SessionContext context) {
        ProjectAssigneePK projectAssigneePK = getProjectAssigneePK(projectAssigneeDTO);
        projectAssigneeDTO.put(projectAssigneeDTO.getPrimKeyName(), projectAssigneePK);

        ProjectAssignee projectAssignee =
                (ProjectAssignee) ExtendedCRUDDirector.i.update(projectAssigneeDTO, resultDTO, false, true, true, "Fail");

        //remove pk Object from resultDTO
        resultDTO.remove(projectAssigneeDTO.getPrimKeyName());

        //project user was deleted by another user
        if (null == projectAssignee) {
            resultDTO.setForward("Fail");
            return;
        }

        //version Error
        if (resultDTO.isFailure()) {
            projectAssigneeDTO.put("addressId", projectAssigneePK.addressId);
            projectAssigneeDTO.put("projectId", projectAssigneePK.projectId);
            read(projectAssigneeDTO, false, context);
        }
    }

    private void create(ProjectAssigneeDTO projectAssigneeDTO) {
        ExtendedCRUDDirector.i.create(projectAssigneeDTO, resultDTO, false);
    }

    private void read(ProjectAssigneeDTO projectAssigneeDTO, boolean checkReferences, SessionContext ctx) {

        if (checkReferences && projectAssigneeDTO.get("withReferences") != null) {
            IntegrityReferentialChecker.i.check(projectAssigneeDTO, resultDTO);
            if (resultDTO.isFailure()) {
                return;
            }
        }

        ProjectAssigneePK projectAssigneePK = getProjectAssigneePK(projectAssigneeDTO);
        projectAssigneeDTO.put(projectAssigneeDTO.getPrimKeyName(), projectAssigneePK);

        ProjectAssignee projectAssignee =
                (ProjectAssignee) ExtendedCRUDDirector.i.read(projectAssigneeDTO, resultDTO, checkReferences);

        //remove pk Object from resultDTO
        resultDTO.remove(projectAssigneeDTO.getPrimKeyName());

        //user was deleted by another user
        if (null == projectAssignee) {
            return;
        }

        //read enabled permissions for user
        List<ProjectUserPermissionUtil.Permission> enabledPermissions =
                ProjectUserPermissionUtil.getEnabledPermissions(projectAssignee.getPermission());
        setSelectedPermissions(enabledPermissions);

        UserDTO userDTO = getUserDTO(projectAssignee.getProject().getResponsibleId());
        //used to validate project responsible change
        resultDTO.put("responsibleAddressId", userDTO.get("addressId"));

        //used to show user name
        String userName = readAddressName(projectAssignee.getAddressId(), ctx);
        resultDTO.put("userName", userName);
    }

    private ProjectAssigneeDTO getProjectAssigneeDTO() {

        ProjectAssigneeDTO projectAssigneeDTO = new ProjectAssigneeDTO();
        EJBCommandUtil.i.setValueAsInteger(this, projectAssigneeDTO, "projectId");
        EJBCommandUtil.i.setValueAsInteger(this, projectAssigneeDTO, "addressId");
        EJBCommandUtil.i.setValueAsInteger(this, projectAssigneeDTO, "version");
        EJBCommandUtil.i.setValueAsInteger(this, projectAssigneeDTO, "companyId");

        projectAssigneeDTO.put("withReferences", paramDTO.get("withReferences"));
        //used to show user name in error messages
        projectAssigneeDTO.put("userName", paramDTO.get("userName"));

        int selectedPermissions = getSelectedPermissions();
        projectAssigneeDTO.put("permission", Byte.valueOf(String.valueOf(selectedPermissions)));

        return projectAssigneeDTO;
    }

    private int getSelectedPermissions() {
        int value = 0;
        List<ProjectUserPermissionUtil.Permission> permissions =
                ProjectUserPermissionUtil.Permission.getPermissions();

        for (ProjectUserPermissionUtil.Permission permission : permissions) {
            String element = (String) paramDTO.get(permission.getName().toLowerCase());
            if (null != element && !"".equals(element.trim())) {
                value += permission.getValue();
            }
        }
        return value;
    }

    private void setSelectedPermissions(List<ProjectUserPermissionUtil.Permission> permissions) {
        for (ProjectUserPermissionUtil.Permission permission : permissions) {
            resultDTO.put(permission.getName().toLowerCase(), true);
        }
    }

    private ProjectAssigneePK getProjectAssigneePK(ProjectAssigneeDTO projectAssigneeDTO) {
        ProjectAssigneePK projectAssigneePK = new ProjectAssigneePK();
        projectAssigneePK.projectId = (Integer) projectAssigneeDTO.get("projectId");
        projectAssigneePK.addressId = (Integer) projectAssigneeDTO.get("addressId");
        projectAssigneeDTO.remove("projectId");
        projectAssigneeDTO.remove("addressId");
        return projectAssigneePK;
    }


    public boolean isStateful() {
        return false;
    }
}
