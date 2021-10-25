package com.piramide.elwis.domain.supportmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 11:32:04 AM
 * To change this template use File | Settings | File Templates.
 */

public interface ArticleRelatedHome extends EJBLocalHome {

    ArticleRelated create(ComponentDTO dto) throws CreateException;

    ArticleRelated findByPrimaryKey(ArticleRelatedPK key) throws FinderException;

    Collection findByArticleId(Integer articleId, Integer companyId) throws FinderException;
}
