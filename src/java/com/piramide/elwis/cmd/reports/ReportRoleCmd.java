package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.admin.RoleUtilCmd;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.reportmanager.ReportRole;
import com.piramide.elwis.domain.reportmanager.ReportRoleHome;
import com.piramide.elwis.domain.reportmanager.ReportRolePK;
import com.piramide.elwis.dto.admin.RoleDTO;
import com.piramide.elwis.dto.reports.ReportRoleDTO;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class ReportRoleCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        String op = this.getOp();
        if ("checkDuplicated".equals(op)) {
            Integer reportId = (Integer) paramDTO.get("reportId");
            List<Integer> roleIds = (List<Integer>) paramDTO.get("roleIds");

            checkDuplicated(reportId, roleIds, sessionContext);
        }
        if ("checkRolesExistence".equals(op)) {
            List<Integer> roleIds = (List<Integer>) paramDTO.get("roleIds");
            checkRolesExistence(roleIds, sessionContext);
        }

        if ("create".equals(op)) {
            List<Integer> roleIds = (List<Integer>) paramDTO.get("roleIds");
            Integer reportId = (Integer) paramDTO.get("reportId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            addRoles(roleIds, reportId, companyId);
        }

        if ("delete".equals(op)) {
            Integer roleId = paramDTO.getAsInt("roleId");
            Integer reportId = paramDTO.getAsInt("reportId");

            delete(roleId, reportId, sessionContext);
        }

        if ("read".equals(op)) {
            Integer roleId = paramDTO.getAsInt("roleId");
            Integer reportId = paramDTO.getAsInt("reportId");

            read(roleId, reportId, sessionContext);
        }

        if ("readSelected".equals(op)) {
            List<Integer> roleIds = (List<Integer>) paramDTO.get("roleIds");
            readSelectedRoles(roleIds, sessionContext);
        }

        if ("deleteSelectedElements".equals(op)) {
            List<Integer> rolesIds = searchRoleIdsInParamDTO();
            Integer reportId = paramDTO.getAsInt("reportId");
            deleteSelectedRoles(reportId, rolesIds);
        }
    }

    private List<Integer> searchRoleIdsInParamDTO() {
        List<Integer> result = new ArrayList<Integer>();
        for (Object o : paramDTO.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            if (entry.getKey().toString().contains("roleId")) {
                result.add(new Integer(entry.getValue().toString()));
            }
        }

        return result;
    }

    private void deleteSelectedRoles(Integer reportId, List<Integer> roleIds) {
        ReportRoleHome home =
                (ReportRoleHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_REPORTROLE);

        for (Integer roleId : roleIds) {
            ReportRolePK pk = new ReportRolePK();
            pk.roleId = roleId;
            pk.reportId = reportId;

            try {
                ReportRole reporRole = home.findByPrimaryKey(pk);
                reporRole.remove();
            } catch (FinderException e) {
                log.debug("Delete ReportRole - reportId=" + reportId + " roleId=" + roleId + " status FAIL");
            } catch (RemoveException e) {
                log.debug("Delete ReportRole - reportId=" + reportId + " roleId=" + roleId + " status FAIL", e);
            }
        }
    }

    private void readSelectedRoles(List<Integer> roleIds, SessionContext sessionContext) {
        List<RoleDTO> selectedRoles = new ArrayList<RoleDTO>();

        for (Integer roleId : roleIds) {
            RoleDTO roleDTO = readRole(roleId, sessionContext);
            if (null != roleDTO) {
                selectedRoles.add(roleDTO);
            }
        }

        resultDTO.put("selectedRoles", selectedRoles);
    }


    private void read(Integer roleId, Integer reportId, SessionContext ctx) {
        ReportRoleHome home =
                (ReportRoleHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_REPORTROLE);
        ReportRolePK reportRolePK = new ReportRolePK();
        reportRolePK.reportId = reportId;
        reportRolePK.roleId = roleId;

        try {
            home.findByPrimaryKey(reportRolePK);
            RoleDTO roleDTO = readRole(roleId, ctx);
            resultDTO.putAll(roleDTO);
        } catch (FinderException e) {
            log.debug("ReportRole reportId=" + reportId + " roleId=" + roleId +
                    " deleted by anotherUser");
            resultDTO.setForward("Fail");
            resultDTO.addResultMessage("customMsg.itemNotFound");
        }
    }

    private void delete(Integer roleId, Integer reportId, SessionContext ctx) {
        ReportRoleHome home =
                (ReportRoleHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_REPORTROLE);
        ReportRolePK reportRolePK = new ReportRolePK();
        reportRolePK.reportId = reportId;
        reportRolePK.roleId = roleId;

        try {
            ReportRole reportRole = home.findByPrimaryKey(reportRolePK);
            reportRole.remove();
        } catch (FinderException e) {
            log.debug("ReportRole reportId=" + reportId + " roleId=" + roleId +
                    " deleted by anotherUser");
            resultDTO.setForward("Fail");
            resultDTO.addResultMessage("customMsg.itemNotFound");
        } catch (RemoveException e) {
            log.debug("ReportRole reportId=" + reportId + " roleId=" + roleId +
                    " Cannot deleted ", e);
        }
    }

    private void addRoles(List<Integer> roleIds, Integer reportId, Integer companyId) {
        for (Integer roleId : roleIds) {
            ReportRoleDTO reportRoleDTO = new ReportRoleDTO();
            reportRoleDTO.put("roleId", roleId);
            reportRoleDTO.put("reportId", reportId);
            reportRoleDTO.put("companyId", companyId);
            ExtendedCRUDDirector.i.create(reportRoleDTO, resultDTO, false);
        }
    }

    /**
     * This method verifies that all roles to added exists in data base
     *
     * @param roleIds List that contain roles identifiers
     * @param ctx     session context to invoke another command <code>RoleCmd<code>.
     */
    private void checkRolesExistence(List<Integer> roleIds, SessionContext ctx) {
        Boolean allRolesExists = true;
        for (Integer roleId : roleIds) {
            RoleDTO roleDTO = readRole(roleId, ctx);
            if (null == roleDTO) {
                allRolesExists = false;
                break;
            }
        }

        resultDTO.put("allRolesExists", allRolesExists);
    }


    /**
     * This method verifies that there are no duplicate items
     * when adding a new role to report.
     * <p/>
     * Also added in <code>resultDTO</code> object, the  result of the method, and the name of the duplicate if exists.
     * Key = 'duplicated' key that contain method result
     * Key = 'duplicatedRoleName' contain duplicated role name.
     *
     * @param reportId report Identifier.
     * @param roleIds  List of role identifier.
     * @param ctx      session context to invoke another command <code>RoleCmd<code>.
     * @return true if report contain some roles selected, false in another case.
     */
    private Boolean checkDuplicated(Integer reportId, List<Integer> roleIds, SessionContext ctx) {
        ReportRoleHome reportRoleHome =
                (ReportRoleHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_REPORTROLE);
        Boolean duplicated = false;

        for (Integer roleId : roleIds) {
            ReportRolePK reportRolePK = new ReportRolePK();
            reportRolePK.reportId = reportId;
            reportRolePK.roleId = roleId;

            try {
                reportRoleHome.findByPrimaryKey(reportRolePK);
                RoleDTO roleDTO = readRole(roleId, ctx);
                if (null != roleDTO) {
                    String duplicatedRoleName = (String) roleDTO.get("roleName");
                    duplicated = true;
                    resultDTO.put("duplicatedRoleName", duplicatedRoleName);
                }
                break;
            } catch (FinderException e) {
                log.debug("Save RoleId=" + roleId + " reportId=" + reportId + " status OK");
            }
        }

        resultDTO.put("duplicated", duplicated);
        return duplicated;
    }

    private RoleDTO readRole(Integer roleId, SessionContext ctx) {
        RoleUtilCmd roleUtilCmd = new RoleUtilCmd();
        roleUtilCmd.setOp("readRole");
        roleUtilCmd.putParam("roleId", roleId);
        roleUtilCmd.executeInStateless(ctx);
        ResultDTO result = roleUtilCmd.getResultDTO();
        return (RoleDTO) result.get("roleDTO");
    }

    public boolean isStateful() {
        return false;
    }
}
