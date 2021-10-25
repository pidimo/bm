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
 * Time: 11:35:15 AM
 * To change this template use File | Settings | File Templates.
 */

public interface ArticleLinkHome extends EJBLocalHome {

    Collection findAll() throws FinderException;

    ArticleLink create(ComponentDTO dto) throws CreateException;

    ArticleLink findByPrimaryKey(Integer key) throws FinderException;
}
