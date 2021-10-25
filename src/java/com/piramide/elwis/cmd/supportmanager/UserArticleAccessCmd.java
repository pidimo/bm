package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.domain.supportmanager.UserArticleAccess;
import com.piramide.elwis.domain.supportmanager.UserArticleAccessHome;
import com.piramide.elwis.dto.supportmanager.UserArticleAccessDTO;
import com.piramide.elwis.utils.SupportConstants;
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
 * @version 5.4
 */
public class UserArticleAccessCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing UserArticleAccessCmd................" + paramDTO);

        if ("assignArticleUserGroups".equals(getOp())) {
            assignUserGroupsToAddress();
        }
        if ("readArticleUserGroups".equals(getOp())) {
            readUserGroupsOfArticle();
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void assignUserGroupsToAddress() {
        List<Integer> userGroupIdList = (List<Integer>) paramDTO.get("articleAccessUserGroupIdList");
        Integer articleId = new Integer(paramDTO.get("articleId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        deleteUserArticleAccess(articleId);

        for (Integer userGroupId : userGroupIdList) {
            createUserArticleAccess(articleId, userGroupId, companyId);
        }

        //set in result dto created groups
        readUserGroupsOfArticle();
    }

    private void readUserGroupsOfArticle() {
        Integer articleId = new Integer(paramDTO.get("articleId").toString());
        List<Integer> userGroupIdList = new ArrayList<Integer>();

        UserArticleAccessHome userArticleAccessHome = (UserArticleAccessHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_USERARTICLEACCESS);
        Collection collection = null;
        try {
            collection = userArticleAccessHome.findUserArticleAccessByArticle(articleId);
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            UserArticleAccess userArticleAccess = (UserArticleAccess) iterator.next();
            userGroupIdList.add(userArticleAccess.getUserGroupId());
        }

        resultDTO.put("articleAccessUserGroupIdList", userGroupIdList);
    }

    private UserArticleAccess createUserArticleAccess(Integer articleId, Integer userGroupId, Integer companyId) {
        UserArticleAccessHome userArticleAccessHome = (UserArticleAccessHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_USERARTICLEACCESS);
        UserArticleAccessDTO dto = new UserArticleAccessDTO();
        dto.put("articleId", articleId);
        dto.put("userGroupId", userGroupId);
        dto.put("companyId", companyId);

        try {
            return userArticleAccessHome.create(dto);
        } catch (CreateException e) {
            log.debug("Error in create user article access..." + articleId, e);
        }
        return null;
    }

    private void deleteUserArticleAccess(Integer articleId) {
        UserArticleAccessHome userArticleAccessHome = (UserArticleAccessHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_USERARTICLEACCESS);

        Collection collection = null;
        try {
            collection = userArticleAccessHome.findUserArticleAccessByArticle(articleId);
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            UserArticleAccess userArticleAccess = (UserArticleAccess) iterator.next();
            try {
                userArticleAccess.remove();
            } catch (RemoveException e) {
                log.debug("Error in delete user article access..", e);
            }
        }
    }
}
