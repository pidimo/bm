package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.domain.admin.UserGroup;
import com.piramide.elwis.domain.admin.UserGroupHome;
import com.piramide.elwis.domain.contactmanager.UserAddressAccessHome;
import com.piramide.elwis.domain.schedulermanager.ScheduledUserHome;
import com.piramide.elwis.domain.supportmanager.UserArticleAccessHome;
import com.piramide.elwis.dto.admin.UserGroupDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class UserGroupReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing UserGroupReadCmd................" + paramDTO);

        if ("userGroupDataAccess".equals(getOp())) {
            findCompanyDataAccessUserGroups();
        }
        if ("groupAssignedInDataAccess".equals(getOp())) {
            groupAssignedInDataAccess();
        }
        if ("groupAssignedInScheduler".equals(getOp())) {
            groupAssignedInScheduler();
        }
        if ("userGroupArticle".equals(getOp())) {
            findCompanyArticleUserGroups();
        }
        if ("groupAssignedInArticle".equals(getOp())) {
            userGroupAssignedInArticle();
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void findCompanyDataAccessUserGroups() {
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());

        UserGroupHome userGroupHome = (UserGroupHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERGROUP);
        Collection userGroups = null;
        try {
            userGroups = userGroupHome.findByCompanyAndType(companyId, AdminConstants.UserGroupType.DATA_ACCESS_SECURITY.getConstant());
        } catch (FinderException e) {
            userGroups = new ArrayList();
        }

        List<UserGroupDTO> companyUserGroupsList = new ArrayList<UserGroupDTO>();

        for (Iterator iterator = userGroups.iterator(); iterator.hasNext();) {
            UserGroup userGroup = (UserGroup) iterator.next();
            UserGroupDTO userGroupDTO = new UserGroupDTO();
            DTOFactory.i.copyToDTO(userGroup, userGroupDTO);

            companyUserGroupsList.add(userGroupDTO);
        }

        resultDTO.put("companyUserGroups", companyUserGroupsList);
    }

    private void groupAssignedInDataAccess() {
        Integer userGroupId = Integer.valueOf(paramDTO.get("userGroupId").toString());

        UserAddressAccessHome userAddressAccessHome = (UserAddressAccessHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_USERADDRESSACCESS);
        Collection collection;
        try {
            collection = userAddressAccessHome.findUserAddressAccessByUserGroup(userGroupId);
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        resultDTO.put("hasAssignedDataAccess", Boolean.valueOf(!collection.isEmpty()));
    }

    private void groupAssignedInScheduler() {
        Integer userGroupId = Integer.valueOf(paramDTO.get("userGroupId").toString());
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());

        ScheduledUserHome scheduledUserHome = (ScheduledUserHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULEDUSER);
        Collection collection;
        try {
            collection = scheduledUserHome.findByUserGroupId(userGroupId, companyId);
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        resultDTO.put("hasAssignedScheduler", Boolean.valueOf(!collection.isEmpty()));
    }

    private void findCompanyArticleUserGroups() {
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());

        UserGroupHome userGroupHome = (UserGroupHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERGROUP);
        Collection userGroups = null;
        try {
            userGroups = userGroupHome.findByCompanyAndType(companyId, AdminConstants.UserGroupType.ARTICLE.getConstant());
        } catch (FinderException e) {
            userGroups = new ArrayList();
        }

        List<UserGroupDTO> userGroupDtoList = new ArrayList<UserGroupDTO>();

        for (Iterator iterator = userGroups.iterator(); iterator.hasNext();) {
            UserGroup userGroup = (UserGroup) iterator.next();
            UserGroupDTO userGroupDTO = new UserGroupDTO();
            DTOFactory.i.copyToDTO(userGroup, userGroupDTO);

            userGroupDtoList.add(userGroupDTO);
        }

        resultDTO.put("articleUserGroups", userGroupDtoList);
    }

    private void userGroupAssignedInArticle() {
        Integer userGroupId = Integer.valueOf(paramDTO.get("userGroupId").toString());

        UserArticleAccessHome userArticleAccessHome = (UserArticleAccessHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_USERARTICLEACCESS);
        Collection collection;
        try {
            collection = userArticleAccessHome.findUserArticleAccessByUserGroup(userGroupId);
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        resultDTO.put("hasAssignedArticle", Boolean.valueOf(!collection.isEmpty()));
    }

}
