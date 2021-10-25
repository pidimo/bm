package com.piramide.elwis.cmd.projects;

import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.project.*;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.projects.ProjectDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ProjectConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.math.BigDecimal;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public abstract class ProjectManagerCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ProjectManagerCmd.class);

    protected ProjectDTO getProject(Integer projectId) {
        ProjectHome projectHome =
                (ProjectHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT);
        ProjectDTO projectDTO = null;
        try {
            Project project = projectHome.findByPrimaryKey(projectId);
            projectDTO = new ProjectDTO();
            DTOFactory.i.copyToDTO(project, projectDTO);
        } catch (FinderException e) {
            log.debug("-> Read Project projectId=" + projectId + " FAIL.");
        }

        return projectDTO;
    }

    protected UserDTO getUserDTO(Integer userId) {
        UserHome userHome =
                (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        UserDTO userDTO = null;
        try {
            User user = userHome.findByPrimaryKey(userId);
            userDTO = new UserDTO();
            DTOFactory.i.copyToDTO(user, userDTO);
        } catch (FinderException e) {
            log.debug("-> Read User " + userId + " FAIL ");
        }

        return userDTO;
    }

    protected ProjectTime getProjectTime(Integer projectTimeId) {
        if (null == projectTimeId) {
            return null;
        }

        ProjectTimeHome projectTimeHome =
                (ProjectTimeHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_TIME);

        try {
            return projectTimeHome.findByPrimaryKey(projectTimeId);
        } catch (FinderException e) {
            return null;
        }
    }

    protected String readAddressName(Integer addressId, SessionContext ctx) {
        LightlyAddressCmd addressCmd = new LightlyAddressCmd();
        addressCmd.putParam("addressId", addressId);

        addressCmd.executeInStateless(ctx);

        ResultDTO customResultDTO = addressCmd.getResultDTO();
        return (String) customResultDTO.get("addressName");
    }

    protected ProjectFreeText createFreeText(String text, Integer companyId, Integer freeTextType) {
        ProjectFreeTextHome projectFreeTextHome =
                (ProjectFreeTextHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_FREETEXT);
        ProjectFreeText freeText = null;
        if (null != text && !"".equals(text.trim())) {
            try {
                freeText = projectFreeTextHome.create(text.getBytes(), companyId, freeTextType);
            } catch (CreateException e) {
                log.error("-> Execute FinanceFreeTextHome.create() Fail", e);
            }
        }
        return freeText;
    }

    protected String readProjectFreeText(ProjectFreeText projectFreeText) {
        if (null == projectFreeText) {
            return "";
        }

        return new String(projectFreeText.getValue());
    }

    protected BigDecimal sumProjectTimesByProject(Integer projectId,
                                                  Integer companyId,
                                                  Boolean toBeInvoiced) {
        ProjectTimeHome projectTime =
                (ProjectTimeHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_TIME);

        BigDecimal result = new BigDecimal(0);
        try {
            result = projectTime.selectSumTimesByProject(projectId, companyId, toBeInvoiced);
            if (null == result) {
                result = new BigDecimal(0);
            }
        } catch (FinderException e) {
            log.debug("-> Cannot sum projectTimes for projectId=" + projectId);
        }

        return result;
    }
}
