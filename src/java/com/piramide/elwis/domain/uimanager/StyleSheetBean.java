package com.piramide.elwis.domain.uimanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.UIManagerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: StyleSheetBean.java 12199 2016-01-20 21:56:44Z miguel ${NAME}.java ,v 1.1 22-04-2005 10:09:14 AM miky Exp $
 */

public abstract class StyleSheetBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        this.setStyleSheetId(PKGenerator.i.nextKey(UIManagerConstants.TABLE_STYLESHEET));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public StyleSheetBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        this.entityContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getStyleSheetId();

    public abstract void setStyleSheetId(Integer styleSheetId);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Collection getStyles();

    public abstract void setStyles(Collection styles);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getStyleSheetType();

    public abstract void setStyleSheetType(Integer styleSheetType);
}
