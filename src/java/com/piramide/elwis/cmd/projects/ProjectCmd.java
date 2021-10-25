package com.piramide.elwis.cmd.projects;

import com.piramide.elwis.cmd.common.FreeTextCmdUtil;
import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Customer;
import com.piramide.elwis.domain.project.*;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import com.piramide.elwis.dto.projects.ProjectDTO;
import com.piramide.elwis.utils.CodeUtil;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.ProjectConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Project command which stores main project functionalities related to CRUD
 *
 * @author Fernando Montao
 * @version $Id: ProjectCmd.java 2009-02-21 15:11:48 $
 */
public class ProjectCmd extends GeneralCmd {

    private Log log = LogFactory.getLog(ProjectCmd.class);

    @Override
    public void executeInStateless(SessionContext ctx) {
        super.setOp(this.getOp());
        super.checkDuplicate = false;
        super.isClearingForm = false;

        paramDTO.remove("totalInvoice");
        paramDTO.remove("totalNoInvoice");

        if (CRUDDirector.OP_CREATE.equals(getOp()) || CRUDDirector.OP_UPDATE.equals(getOp())) {
            readOrCreateCustomer(ctx, paramDTO.get("customerId"));
        }

        if (CRUDDirector.OP_DELETE.equals(getOp())) {
            deleteProjectInformation(Integer.valueOf(paramDTO.get("projectId").toString()));
        }

        Project project = (Project) super.execute(ctx, new ProjectDTO(paramDTO));

        if (CRUDDirector.OP_CREATE.equals(getOp()) || CRUDDirector.OP_UPDATE.equals(getOp())) {
            createOrUpdateResponsible(ctx, project);
        }

        if (project != null && project.getCustomerId() != null) { //when reading
            readOrCreateCustomer(ctx, project.getCustomerId());
        }

        FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, project, "DescriptionText", ProjectFreeText.class,
                ProjectConstants.JNDI_FREETEXT, FreeTextTypes.FREETEXT_PROJECT, "description");
    }

    private void createOrUpdateResponsible(SessionContext ctx, Project project) {
        if (null == project) {
            return;
        }

        if (resultDTO.isFailure()) {
            return;
        }

        if ("Fail".equals(resultDTO.getForward())) {
            return;
        }

        ProjectAssigneeCmd projectAssigneeCmd = new ProjectAssigneeCmd();
        projectAssigneeCmd.setOp("createOrUpdate");
        projectAssigneeCmd.putParam("projectId", project.getProjectId());
        projectAssigneeCmd.executeInStateless(ctx);
    }

    protected void readOrCreateCustomer(SessionContext ctx, Object customerId) {
        if (customerId != null && customerId.toString().trim().length() > 0) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.put("addressId", customerId);
            try {
                Address address = (Address) EJBFactory.i.findEJB(addressDTO);
                resultDTO.put("customerName", address.getName());

                if (!CodeUtil.isCustomer(address.getCode())) {
                    CustomerDTO customerDTO = new CustomerDTO();
                    customerDTO.put(CustomerDTO.KEY_CUSTOMERID, address.getAddressId());
                    customerDTO.put("companyId", address.getCompanyId());
                    Customer customer = (Customer) EJBFactory.i.createEJB(customerDTO);
                    String newCustomerNumber = InvoiceUtil.i.getCustomerNumber(customer.getCompanyId());
                    if (null != newCustomerNumber) {
                        customer.setNumber(newCustomerNumber);
                    }
                    address.setCode((byte) (address.getCode() + CodeUtil.customer));
                }
            } catch (Exception e) {
                ctx.setRollbackOnly();
                addressDTO.addNotFoundMsgTo(resultDTO);
                resultDTO.setResultAsFailure();
            }
        }
    }

    private void deleteProjectInformation(Integer projectId) {
        ProjectHome projectHome =
                (ProjectHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT);
        try {
            Project project = projectHome.findByPrimaryKey(projectId);
            if (null == project) {
                return;
            }
            removeProjectTimes(project.getProjectId(), project.getCompanyId());
            removeProjectTimeLimits(project.getProjectId(), project.getCompanyId());
            removeProjectUsers(project.getProjectId(), project.getCompanyId());
            removeProjectActivities(project.getProjectId(), project.getCompanyId());
            removeSubProjects(project.getProjectId(), project.getCompanyId());
        } catch (FinderException e) {
            return;
        }
    }

    private void removeProjectUsers(Integer projectId, Integer companyId) {
        ProjectAssigneeHome projectAssigneeHome =
                (ProjectAssigneeHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_ASSIGNEE);
        try {
            Collection projectAssignees = projectAssigneeHome.findByProjectId(projectId, companyId);
            removeObjects(projectAssignees);
        } catch (FinderException e) {
            log.debug("-> Cannot read ProjectUsers for projectId=" + projectId);
        }
    }

    private void removeProjectTimes(Integer projectId, Integer companyId) {
        ProjectTimeHome projectTimeHome =
                (ProjectTimeHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_TIME);
        try {
            Collection objects = projectTimeHome.findByProjectId(projectId, companyId);
            removeObjects(objects);
        } catch (FinderException e) {
            log.debug("-> Cannot read ProjectTimes for projectId=" + projectId);
        }
    }

    private void removeProjectTimeLimits(Integer projectId, Integer companyId) {
        ProjectTimeLimitHome projectTimeLimitHome =
                (ProjectTimeLimitHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_TIME_LIMIT);
        try {
            Collection objects = projectTimeLimitHome.findByProjectId(projectId, companyId);
            removeObjects(objects);
        } catch (FinderException e) {
            log.debug("-> Cannot read ProjectTimes for projectId=" + projectId);
        }
    }

    private void removeSubProjects(Integer projectId, Integer companyId) {
        SubProjectHome subProjectHome =
                (SubProjectHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_SUB_PROJECT);
        try {
            Collection objects = subProjectHome.findByProjectId(projectId, companyId);
            removeObjects(objects);
        } catch (FinderException e) {
            log.debug("-> Cannot read SubProject for projectId=" + projectId);
        }
    }

    private void removeProjectActivities(Integer projectId, Integer companyId) {
        ProjectActivityHome projectActivityHome =
                (ProjectActivityHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_ACTIVITY);

        try {
            Collection objects = projectActivityHome.findByProjectId(projectId, companyId);
            removeObjects(objects);
        } catch (FinderException e) {
            log.debug("-> Cannot read activities for projectId=" + projectId);
        }
    }


    private void removeObjects(Collection objects) {
        List elements = new ArrayList(objects);
        for (int i = 0; i < elements.size(); i++) {
            try {
                EJBLocalObject object = (EJBLocalObject) elements.get(i);
                object.remove();
            } catch (RemoveException e) {
                log.error("-> Remove Object FAIL", e);
                break;
            }
        }
    }


}
