package com.piramide.elwis.cmd.projects;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.project.ProjectTimeLimit;
import com.piramide.elwis.domain.project.ProjectTimeLimitHome;
import com.piramide.elwis.domain.project.SubProject;
import com.piramide.elwis.domain.project.SubProjectHome;
import com.piramide.elwis.dto.projects.ProjectTimeLimitDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.ProjectConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public class ProjectTimeLimitCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void executeInStateless(SessionContext context) {
        log.debug("Executing ProjectTimeLimitCmd................" + paramDTO);

        boolean isRead = true;
        ProjectTimeLimitDTO projectTimeLimitDTO = getProjectTimeLimitDTO();

        if ("create".equals(getOp())) {
            isRead = false;
            create(projectTimeLimitDTO);
        }

        if ("update".equals(getOp())) {
            isRead = false;
            update(projectTimeLimitDTO);
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(projectTimeLimitDTO);
        }

        if ("existAssigneeSubProject".equals(getOp())) {
            isRead = false;
            checkExistAssigneeSubProject();
        }

        if ("readByAssigneeSubProject".equals(getOp())) {
            isRead = false;
            readProjectTimeLimitByAssigneeSubProject();
        }

        if (isRead) {
            boolean withReferences = null != paramDTO.get("withReferences")
                    && "true".equals(paramDTO.get("withReferences").toString());
            read(projectTimeLimitDTO, withReferences);
        }
    }

    private void delete(ProjectTimeLimitDTO projectTimeLimitDTO) {
        ExtendedCRUDDirector.i.delete(projectTimeLimitDTO, resultDTO, true, "Fail");
    }

    private void update(ProjectTimeLimitDTO projectTimeLimitDTO) {
        ExtendedCRUDDirector.i.update(projectTimeLimitDTO, resultDTO, false, true, true, "Fail");
    }

    private void read(ProjectTimeLimitDTO projectTimeLimitDTO, boolean checkReferences) {
        ExtendedCRUDDirector.i.read(projectTimeLimitDTO, resultDTO, checkReferences);
    }

    private void create(ProjectTimeLimitDTO projectTimeLimitDTO) {
        ExtendedCRUDDirector.i.create(projectTimeLimitDTO, resultDTO, false);
    }

    private ProjectTimeLimitDTO getProjectTimeLimitDTO() {
        ProjectTimeLimitDTO projectTimeLimitDTO = new ProjectTimeLimitDTO(paramDTO);

        EJBCommandUtil.i.setValueAsInteger(this, projectTimeLimitDTO, "timeLimitId");

        return projectTimeLimitDTO;
    }

    private void checkExistAssigneeSubProject() {
        Integer projectId = new Integer(paramDTO.get("projectId").toString());
        Integer assigneeId = new Integer(paramDTO.get("assigneeId").toString());
        Integer subProjectId = new Integer(paramDTO.get("subProjectId").toString());

        Boolean existAssignee = Boolean.FALSE;

        if (!isSameProjectTimeLimitRecord(assigneeId, subProjectId)) {

            ProjectTimeLimitHome projectTimeLimitHome = (ProjectTimeLimitHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_TIME_LIMIT);
            Collection collection = null;
            try {
                collection = projectTimeLimitHome.findByProjectIdAssigneeIdSubProjectId(projectId, assigneeId, subProjectId);
            } catch (FinderException e) {
                collection = new ArrayList();
            }
            if (collection.size() > 0) {
                existAssignee = Boolean.TRUE;
            }
        }

        if (existAssignee) {
            Address address = findAddress(assigneeId);
            SubProject subProject = findSubProject(subProjectId);
            resultDTO.put("assigneeName", address != null ? address.getName() : "");
            resultDTO.put("subProjectName", subProject != null ? subProject.getName() : "");
        }

        resultDTO.put("existAssigned", existAssignee);
    }

    private boolean isSameProjectTimeLimitRecord(Integer assigneeId, Integer subProjectId) {
        boolean isSameRecord = false;

        Integer timeLimitId = null;
        if (paramDTO.containsKey("timeLimitId")) {
            timeLimitId = new Integer(paramDTO.get("timeLimitId").toString());
        }

        ProjectTimeLimit projectTimeLimit = findProjectTimeLimit(timeLimitId);
        if (projectTimeLimit != null) {
            if (assigneeId.equals(projectTimeLimit.getAssigneeId()) && subProjectId.equals(projectTimeLimit.getSubProjectId())) {
                isSameRecord = true;
            }
        }
        return isSameRecord;
    }

    private void readProjectTimeLimitByAssigneeSubProject() {
        ProjectTimeLimitDTO dto = null;

        Integer projectId = new Integer(paramDTO.get("projectId").toString());
        Integer assigneeId = new Integer(paramDTO.get("assigneeId").toString());
        Integer subProjectId = new Integer(paramDTO.get("subProjectId").toString());

        ProjectTimeLimitHome projectTimeLimitHome = (ProjectTimeLimitHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_TIME_LIMIT);
        Collection collection = null;
        try {
            collection = projectTimeLimitHome.findByProjectIdAssigneeIdSubProjectId(projectId, assigneeId, subProjectId);
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        if (collection.size() > 0) {
            ProjectTimeLimit projectTimeLimit = (ProjectTimeLimit) collection.iterator().next();
            dto = new ProjectTimeLimitDTO();
            DTOFactory.i.copyToDTO(projectTimeLimit, dto);
        }

        resultDTO.put("dtoProjectTimeLimit", dto);
    }

    private ProjectTimeLimit findProjectTimeLimit(Integer timeLimitId) {
        ProjectTimeLimit projectTimeLimit = null;
        ProjectTimeLimitHome projectTimeLimitHome = (ProjectTimeLimitHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_TIME_LIMIT);

        if (timeLimitId != null) {
            try {
                projectTimeLimit = projectTimeLimitHome.findByPrimaryKey(timeLimitId);
            } catch (FinderException e) {
                log.debug("Not found projectTimeLimit... " + timeLimitId);
            }
        }

        return projectTimeLimit;
    }

    private Address findAddress(Integer addressId) {
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        if (addressId != null) {
            try {
                return addressHome.findByPrimaryKey(addressId);
            } catch (FinderException e) {
                log.debug("Error in find contact: " + addressId, e);
            }
        }
        return null;
    }

    private SubProject findSubProject(Integer subProjectId) {
        SubProjectHome subProjectHome = (SubProjectHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_SUB_PROJECT);
        if (subProjectId != null) {
            try {
                return subProjectHome.findByPrimaryKey(subProjectId);
            } catch (FinderException e) {
                log.debug("Error in find sub project: " + subProjectId, e);
            }
        }
        return null;
    }


    public boolean isStateful() {
        return false;
    }
}
