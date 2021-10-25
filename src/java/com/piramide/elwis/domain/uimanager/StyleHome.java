package com.piramide.elwis.domain.uimanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: StyleHome.java 12548 2016-05-12 22:50:06Z miguel ${NAME}.java ,v 1.1 22-04-2005 10:15:22 AM miky Exp $
 */

public interface StyleHome extends EJBLocalHome {
    Style findByPrimaryKey(Integer key) throws FinderException;

    public Style create(ComponentDTO dto) throws CreateException;

    Style findByStyleSheetIdAndCompanyIdAndName(Integer styleSheetId, Integer companyId, String name) throws FinderException;

    Collection findByStyleSheetIdAndName(Integer styleSheetId, String name) throws FinderException;

    Collection findByNameOfCompany(String name, Integer companyId, Integer styleSheetType) throws FinderException;

    Collection findByNameOfUser(String name, Integer userId, Integer companyId, Integer styleSheetType) throws FinderException;

}
