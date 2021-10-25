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
 * @version $Id: StyleBean.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java ,v 1.1 22-04-2005 10:15:21 AM miky Exp $
 */

public abstract class StyleBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        this.setStyleId(PKGenerator.i.nextKey(UIManagerConstants.TABLE_STYLE));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public StyleBean() {
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

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Integer getStyleId();

    public abstract void setStyleId(Integer styleId);

    public abstract Integer getStyleSheetId();

    public abstract void setStyleSheetId(Integer styleSheetId);

    public abstract Collection getStyleAttributes();

    public abstract void setStyleAttributes(Collection styleAttributes);

    public abstract StyleSheet getStyleSheet();

    public abstract void setStyleSheet(StyleSheet styleSheet);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);
}
