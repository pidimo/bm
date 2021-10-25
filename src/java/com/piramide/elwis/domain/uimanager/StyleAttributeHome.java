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
 * @version $Id: StyleAttributeHome.java 12510 2016-02-25 20:44:58Z miguel ${NAME}.java ,v 1.1 22-04-2005 10:17:58 AM miky Exp $
 */

public interface StyleAttributeHome extends EJBLocalHome {
    StyleAttribute findByPrimaryKey(Integer key) throws FinderException;

    public StyleAttribute create(ComponentDTO dto) throws CreateException;

    Collection findByStyleSheetIdAndClassNameAndAttributeName(Integer styleSheetId, String className, String attributeName) throws FinderException;
}
