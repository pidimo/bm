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
 * Time: 11:46:31 AM
 * To change this template use File | Settings | File Templates.
 */

public interface ArticleRatingHome extends EJBLocalHome {

    Collection findAll() throws FinderException;

    ArticleRating create(ComponentDTO dto) throws CreateException;

    ArticleRating findByPrimaryKey(ArticleRatingPK key) throws FinderException;

    ArticleRating findByArticleIdAndUserId(Integer userId, Integer articleId) throws FinderException;

}
