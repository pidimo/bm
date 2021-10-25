package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;
import java.util.Iterator;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: FilterBean.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java ,v 1.1 02-02-2005 04:21:54 PM miky Exp $
 */

public abstract class FilterBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        this.setFilterId(PKGenerator.i.nextKey(WebMailConstants.TABLE_FILTER));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public FilterBean() {
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

    public abstract Integer getFilterId();

    public abstract void setFilterId(Integer filterId);

    public abstract String getFilterName();

    public abstract void setFilterName(String filterName);

    public abstract Integer getFolderId();

    public abstract void setFolderId(Integer folderId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Collection getConditions();

    public abstract void setConditions(Collection conditions);


    public boolean evaluateConditions(String from, String to, String cc, String subject, String body) {
        Collection myConditions = this.getConditions();
        boolean result = false;
        for (Iterator iterator = myConditions.iterator(); iterator.hasNext();) {
            Condition condition = (Condition) iterator.next();

            if (condition.getConditionNameKey().equals(Integer.valueOf(WebMailConstants.FROM_PART))) {
                result = evaluate(from, condition);
            }

            if (condition.getConditionNameKey().equals(Integer.valueOf(WebMailConstants.TO_o_CC_PART))) {
                result = evaluate(to, condition);
                if (!result) {
                    result = evaluate(cc, condition);
                }
            }

            if (condition.getConditionNameKey().equals(Integer.valueOf(WebMailConstants.SUBJECT_PART))) {
                result = evaluate(subject, condition);
            }


            if (result) {
                break;
            }
        }
        return result;
    }

    public boolean evaluate(String param, Condition condition) {
        param = param.trim();
        boolean result = false;
        if (condition.getConditionKey().equals(Integer.valueOf(WebMailConstants.CONDITION_CONTAIN))) {
            if (param.indexOf(condition.getConditionText()) != -1) {
                result = true;
            }
        }
        if (condition.getConditionKey().equals(Integer.valueOf(WebMailConstants.CONDITION_NOT_CONTAIN))) {
            if (param.indexOf(condition.getConditionText()) == -1) {
                result = true;
            }
        }
        if (condition.getConditionKey().equals(Integer.valueOf(WebMailConstants.CONDITION_BEGIN_WITH))) {
            if (condition.getConditionNameKey().equals(Integer.valueOf(WebMailConstants.FROM_PART)) ||
                    condition.getConditionNameKey().equals(Integer.valueOf(WebMailConstants.TO_o_CC_PART))) {
                if (param.startsWith("\"")) {
                    param = param.substring(1).trim(); //if start whit quotes, quit this to apply the filter
                }
            }
            if (param.startsWith(condition.getConditionText())) {
                result = true;
            }
        }
        if (condition.getConditionKey().equals(Integer.valueOf(WebMailConstants.CONDITION_TERMINATED_IN))) {
            if (param.endsWith(condition.getConditionText())) {
                result = true;
            }
        }

        return result;
    }

}
