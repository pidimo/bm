/**
 * @author : Ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

public abstract class UidlTrackBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setUidlTrackId(PKGenerator.i.nextKey(WebMailConstants.TABLE_UIDLTRACK));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public UidlTrackBean() {
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

    public abstract Integer getUidlTrackId();

    public abstract void setUidlTrackId(Integer uidlTrackId);

    public abstract String getUidl();

    public abstract void setUidl(String uidl);

    public abstract Integer getMailAccountId();

    public abstract void setMailAccountId(Integer mailAccountId);

    public abstract Collection ejbSelectGetUidls(Integer mailAccountId, Integer companyId) throws FinderException;

    public Collection ejbHomeSelectGetUidls(Integer mailAccountId, Integer companyId) throws FinderException {
        return ejbSelectGetUidls(mailAccountId, companyId);
    }

    public abstract Long getDeleteFromPopAtTime();

    public abstract void setDeleteFromPopAtTime(Long deleteFromPopAtTime);
}
