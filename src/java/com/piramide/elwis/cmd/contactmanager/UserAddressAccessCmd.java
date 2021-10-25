package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.contactmanager.UserAddressAccess;
import com.piramide.elwis.domain.contactmanager.UserAddressAccessHome;
import com.piramide.elwis.dto.contactmanager.UserAddressAccessDTO;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class UserAddressAccessCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing UserAddressAccessCmd................" + paramDTO);

        if ("assignUserGroups".equals(getOp())) {
            assignUserGroupsToAddress();
        }
        if ("readUserGroups".equals(getOp())) {
            readUserGroupsOfAddress();
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void assignUserGroupsToAddress() {
        List<Integer> userGroupIdList = (List<Integer>) paramDTO.get("accessUserGroupIdList");
        Integer addressId = new Integer(paramDTO.get("addressId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        deleteUserAddressAccess(addressId);

        for (Integer userGroupId : userGroupIdList) {
            createUserAddressAccess(addressId, userGroupId, companyId);
        }

        //set in result dto created groups
        readUserGroupsOfAddress();
    }

    private void readUserGroupsOfAddress() {
        Integer addressId = new Integer(paramDTO.get("addressId").toString());
        List<Integer> userGroupIdList = new ArrayList<Integer>();

        UserAddressAccessHome userAddressAccessHome = (UserAddressAccessHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_USERADDRESSACCESS);
        Collection collection = null;
        try {
            collection = userAddressAccessHome.findUserAddressAccessByAddress(addressId);
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            UserAddressAccess userAddressAccess = (UserAddressAccess) iterator.next();
            userGroupIdList.add(userAddressAccess.getUserGroupId());
        }

        resultDTO.put("accessUserGroupIdList", userGroupIdList);
    }

    private UserAddressAccess createUserAddressAccess(Integer addressId, Integer userGroupId, Integer companyId) {
        UserAddressAccessHome userAddressAccessHome = (UserAddressAccessHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_USERADDRESSACCESS);
        UserAddressAccessDTO dto = new UserAddressAccessDTO();
        dto.put("addressId", addressId);
        dto.put("userGroupId", userGroupId);
        dto.put("companyId", companyId);

        try {
            return userAddressAccessHome.create(dto);
        } catch (CreateException e) {
            log.debug("Error in create user address access..." + addressId, e);
        }
        return null;
    }

    private void deleteUserAddressAccess(Integer addressId) {
        UserAddressAccessHome userAddressAccessHome = (UserAddressAccessHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_USERADDRESSACCESS);

        Collection collection = null;
        try {
            collection = userAddressAccessHome.findUserAddressAccessByAddress(addressId);
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            UserAddressAccess userAddressAccess = (UserAddressAccess) iterator.next();
            try {
                userAddressAccess.remove();
            } catch (RemoveException e) {
                log.debug("Error in delete user address access..", e);
            }
        }
    }
}
