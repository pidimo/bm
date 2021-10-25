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
 * Time: 11:08:55 AM
 * To change this template use File | Settings | File Templates.
 */

public interface ArticleHome extends EJBLocalHome {

    Collection findAll() throws FinderException;

    Article create(ComponentDTO dto) throws CreateException;

    Article findByPrimaryKey(Integer key) throws FinderException;

    Article findByQuestionKey(Integer key) throws FinderException;

    String selectMaxArticleNumber(Integer companyId) throws FinderException;
}
