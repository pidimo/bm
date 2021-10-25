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
 * @version $Id: StyleSheetHome.java 12517 2016-02-26 21:21:47Z miguel ${NAME}.java ,v 1.1 22-04-2005 10:09:16 AM miky Exp $
 */

public interface StyleSheetHome extends EJBLocalHome {
    StyleSheet findByPrimaryKey(Integer key) throws FinderException;

    public StyleSheet create(ComponentDTO dto) throws CreateException;

    StyleSheet findByUserIdAndCompanyId(Integer userId, Integer companyId, Integer styleSheetType) throws FinderException;

    StyleSheet findByCompanyId(Integer companyId, Integer styleSheetType) throws FinderException;

    Collection findAllByUserIdAndCompanyId(Integer userId, Integer companyId, Integer styleSheetType) throws FinderException;

    Collection findAllByCompanyId(Integer companyId, Integer styleSheetType) throws FinderException;

    Collection findByStyleSheetTypeAllCompanies(Integer styleSheetType) throws FinderException;

    Collection findByStyleSheetTypeAllUser(Integer styleSheetType) throws FinderException;

}
