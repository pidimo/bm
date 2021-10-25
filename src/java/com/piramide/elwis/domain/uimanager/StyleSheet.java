package com.piramide.elwis.domain.uimanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: StyleSheet.java 12199 2016-01-20 21:56:44Z miguel ${NAME}.java ,v 1.1 22-04-2005 10:09:16 AM miky Exp $
 */

public interface StyleSheet extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getStyleSheetId();

    void setStyleSheetId(Integer styleSheetId);

    Integer getUserId();

    void setUserId(Integer userId);

    Collection getStyles();

    void setStyles(Collection styles);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getStyleSheetType();

    void setStyleSheetType(Integer styleSheetType);
}
