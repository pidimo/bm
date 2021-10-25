package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * This Class represents the TelecomType Entity Bean
 *
 * @author yumi
 * @version $Id: TelecomTypeBean.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public abstract class TelecomTypeBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setTelecomTypeId(PKGenerator.i.nextKey(CatalogConstants.TABLE_TELECOMTYPE));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
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

    public abstract Integer getTelecomTypeId();

    public abstract void setTelecomTypeId(Integer telecomTypeId);

    public abstract String getTelecomTypeName();

    public abstract void setTelecomTypeName(String name);

    public abstract String getPosition();

    public abstract void setPosition(String position);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer versionId);

    public abstract Integer getLangTextId();

    public abstract void setLangTextId(Integer langTextId);

    public abstract String getType();

    public abstract void setType(String type);
}
