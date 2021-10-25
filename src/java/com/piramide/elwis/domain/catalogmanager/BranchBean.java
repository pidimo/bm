package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * This Class represents the Branch Entity Bean
 *
 * @author Ivan
 * @version $Id: BranchBean.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public abstract class BranchBean implements EntityBean {

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setBranchId(PKGenerator.i.nextKey(CatalogConstants.TABLE_BRANCH));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) {

    }

    public BranchBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
    }

    public void unsetEntityContext() throws EJBException {
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

    public abstract Integer getBranchId();

    public abstract void setBranchId(Integer branchId);

    public abstract Integer getGroup();

    public abstract void setGroup(Integer branchGroup);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer versionId);

    public abstract String getBranchName();

    public abstract void setBranchName(String nameId);

    /*public void checkVersion(int version) throws ConcurrencyException {
        int currentVersion = getVersion();
        if (version != currentVersion) {
            throw new ConcurrencyException(getBranchDTO());
        }
        setVersion(getVersion() + 1);
    }*/
}
