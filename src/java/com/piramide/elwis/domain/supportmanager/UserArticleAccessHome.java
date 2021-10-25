package com.piramide.elwis.domain.supportmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.5
 */
public interface UserArticleAccessHome extends EJBLocalHome {
    public UserArticleAccess create(ComponentDTO dto) throws CreateException;

    UserArticleAccess findByPrimaryKey(UserArticleAccessPK key) throws FinderException;

    public Collection findUserArticleAccessByArticle(Integer articleId) throws FinderException;

    public Collection findUserArticleAccessByUserGroup(Integer userGroupId) throws FinderException;
}
