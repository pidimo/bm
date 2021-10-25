package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: ConditionBean.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java ,v 1.1 02-02-2005 04:28:49 PM miky Exp $
 */

public abstract class ConditionBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        this.setConditionId(PKGenerator.i.nextKey(WebMailConstants.TABLE_CONDITION));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public ConditionBean() {
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

    public abstract Integer getConditionId();

    public abstract void setConditionId(Integer conditionId);

    public abstract Integer getConditionNameKey();

    public abstract void setConditionNameKey(Integer conditionNameKey);

    public abstract Integer getConditionKey();

    public abstract void setConditionKey(Integer conditionKey);

    public abstract String getConditionText();

    public abstract void setConditionText(String conditiontext);

    public abstract Integer getFilterId();

    public abstract void setFilterId(Integer filterId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);


}
