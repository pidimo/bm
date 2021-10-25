package com.piramide.elwis.domain.uimanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: Style.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java ,v 1.1 22-04-2005 10:15:22 AM miky Exp $
 */

public interface Style extends EJBLocalObject {
    String getName();

    void setName(String name);

    Integer getStyleId();

    void setStyleId(Integer styleId);

    Integer getStyleSheetId();

    void setStyleSheetId(Integer styleSheetId);

    Collection getStyleAttributes();

    void setStyleAttributes(Collection styleAttributes);

    StyleSheet getStyleSheet();

    void setStyleSheet(StyleSheet styleSheet);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);
}
