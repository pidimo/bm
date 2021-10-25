package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.*;
import java.util.Collection;

/**
 * Class models a import profile configuration.
 * <p/>
 * This class contains the options selected by the user when he use the data import functionality.
 *
 * @author Ivan Alban
 * @version 4.2.1
 */
public abstract class ImportProfileBean implements EntityBean {
    private Log log = LogFactory.getLog(ImportProfileBean.class);
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        log.debug("**********************************");
        log.debug(dto);
        log.debug("**********************************");
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setProfileId(PKGenerator.i.nextKey(ContactConstants.TABLE_IMPORTPROFILE));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }


    public ImportProfileBean() {
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

    public abstract String getLabel();

    public abstract void setLabel(String label);

    public abstract Integer getProfileId();

    public abstract void setProfileId(Integer profileId);

    public abstract Integer getProfileType();

    public abstract void setProfileType(Integer profileType);

    public abstract Boolean getSkipFirstRow();

    public abstract void setSkipFirstRow(Boolean skipFirstRow);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getImportColumns();

    public abstract void setImportColumns(Collection importColumns);

    public abstract Boolean getCheckDuplicate();

    public abstract void setCheckDuplicate(Boolean checkDuplicate);

    public abstract Integer getTotalRecord();

    public abstract void setTotalRecord(Integer totalRecord);

    public abstract Long getImportStartTime();

    public abstract void setImportStartTime(Long importStartTime);
}
