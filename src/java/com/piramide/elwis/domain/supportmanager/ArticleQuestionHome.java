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
 * Time: 11:19:48 AM
 * To change this template use File | Settings | File Templates.
 */

public interface ArticleQuestionHome extends EJBLocalHome {

    Collection findAll() throws FinderException;

    ArticleQuestion create(ComponentDTO dto) throws CreateException;

    ArticleQuestion findByPrimaryKey(Integer key) throws FinderException;
}
