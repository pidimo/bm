package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.domain.admin.*;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.contactmanager.Telecom;
import com.piramide.elwis.domain.contactmanager.TelecomHome;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.CompanyDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.TelecomType;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public class CompanyDeleteCmd extends EJBCommand {
    private Log log = LogFactory.getLog(CompanyDeleteCmd.class);

    public void executeInStateless(SessionContext sessionContext) {
        String op = this.getOp();
        boolean read = true;

        if ("checkCompanyForDelete".equals(op)) {
            read = false;
            Integer companyId = (Integer) paramDTO.get("companyId");
            String companyName = (String) paramDTO.get("companyName");
            checkCompanyForDelete(companyId, companyName);
        }
        if (read) {
            Integer companyId = paramDTO.getAsInt("companyId");
            String companyName = (String) paramDTO.get("companyName");
            read(companyId, companyName);
        }
    }

    private boolean checkCompanyForDelete(Integer companyId, String companyName) {
        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_COMPANY);
        try {
            Company company = companyHome.findByPrimaryKey(companyId);
            if (company.getIsDefault()) {
                resultDTO.setForward("Fail");
                resultDTO.addResultMessage("Company.error.deleteDefaultCompany");
                return false;
            }

            int connectedUsers = countConnectedUsers(companyId);
            if (connectedUsers > 0) {
                resultDTO.setForward("Fail");
                resultDTO.addResultMessage("Company.error.conectedUsers");
                log.debug("-> One or More Users Connected for companyId=" + companyId +
                        " OK,  Setting up Forward=" + resultDTO.getForward());
                return false;
            }
        } catch (FinderException e) {
            log.debug("-> Read Company companyId=" + companyId + " FAIL");
            resultDTO.setForward("Fail");
            resultDTO.addResultMessage("error.SelectedNotFound", companyName);
            log.debug("-> Read Company for companyId=" + companyId +
                    " FAIL, Setting up Forward=" + resultDTO.getForward());
            return false;
        }

        return true;
    }


    private Integer countConnectedUsers(Integer companyId) {
        UserSessionLogHome sessionLogHome =
                (UserSessionLogHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERSESSIONLOG);
        Integer result = 1;

        Collection connectedUsers;
        try {
            connectedUsers = sessionLogHome.findConnectedUsers(companyId);
            if (null != connectedUsers) {
                result = connectedUsers.size();
                resultDTO.put("connectecUsers", connectedUsers.size());
            }
        } catch (FinderException e) {
            log.debug("-> Read connected users for companyId=" + companyId + " FAIL", e);
        }

        return result;
    }

    private void read(Integer companyId, String companyName) {

        Company company = getCompany(companyId);
        //company was deleted by other user
        if (null == company) {
            resultDTO.setForward("Fail");
            resultDTO.addResultMessage("error.SelectedNotFound", companyName);
            return;
        }

        //exists users are working in the company
        Integer connectedUsers = countConnectedUsers(companyId);
        if (connectedUsers != 0) {
            resultDTO.setForward("Fail");
            resultDTO.addResultMessage("Company.error.conectedUsers");
            return;
        }

        CompanyDTO companyDTO = new CompanyDTO();
        DTOFactory.i.copyToDTO(company, companyDTO);

        AddressDTO addressCompanyDTO = new AddressDTO();
        Address addressCompany = getAddress(company.getCompanyId());
        DTOFactory.i.copyToDTO(addressCompany, addressCompanyDTO);

        User rootUser = getRootUser(company.getCompanyId());
        Address addressRoot = getAddress(rootUser.getAddressId());
        resultDTO.put("rootName1", addressRoot.getName1());
        resultDTO.put("rootName2", addressRoot.getName2());
        resultDTO.put("userName", rootUser.getUserLogin());
        resultDTO.put("email", getDefaultEmail(addressCompany.getAddressId(), addressRoot.getAddressId()));

        resultDTO.putAll(companyDTO);
        resultDTO.putAll(addressCompanyDTO);

        List modules = new ArrayList();
        for (Object object : company.getCompanyModules()) {
            CompanyModule module = (CompanyModule) object;
            if (null != module.getActive() && module.getActive()) {
                modules.add(module.getModuleId());
                resultDTO.put(
                        module.getSystemModule().getNameKey().replace('.', '_') + "_" + module.getModuleId(),
                        module.getMainTableRecordsLimit());
            }
        }

        resultDTO.put("modules", modules);


    }

    private String getDefaultEmail(Integer companyAddressId, Integer rootUserAddressId) {
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        List telecoms = null;
        String defaultEmail = "";
        try {
            telecoms = (List) telecomHome.findContactPersonTelecomsByTelecomTypeType(companyAddressId, rootUserAddressId, TelecomType.EMAIL_TYPE);
        } catch (FinderException e) {
            //no exists telecoms for the root user
        }
        if (null == telecoms) {
            return defaultEmail;
        }

        for (int i = 0; i < telecoms.size(); i++) {
            Telecom telecom = (Telecom) telecoms.get(i);
            if (null != telecom.getPredetermined() && telecom.getPredetermined()) {
                defaultEmail = telecom.getData();
                break;
            }
        }

        return defaultEmail;
    }


    private Address getAddress(Integer addressId) {
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        try {
            return addressHome.findByPrimaryKey(addressId);
        } catch (FinderException e) {
            return null;
        }
    }

    private Company getCompany(Integer companyId) {
        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_COMPANY);
        try {
            return companyHome.findByPrimaryKey(companyId);
        } catch (FinderException e) {
            return null;
        }
    }

    private User getRootUser(Integer companyId) {
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        try {
            return userHome.findRootUserByCompany(companyId);
        } catch (FinderException e) {
            return null;
        }
    }

    public boolean isStateful() {
        return false;
    }
}
