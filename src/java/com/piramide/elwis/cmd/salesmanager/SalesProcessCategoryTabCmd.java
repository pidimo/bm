package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryTabManagerCmd;
import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.salesmanager.SalesProcess;
import com.piramide.elwis.domain.salesmanager.SalesProcessHome;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.EJBFactory;

import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class SalesProcessCategoryTabCmd extends CategoryTabManagerCmd {

    @Override
    protected EJBLocalObject findEJBLocalObject(SessionContext ctx) {
        Integer processId = EJBCommandUtil.i.getValueAsInteger(this, "processId");

        SalesProcessHome salesProcessHome =
                (SalesProcessHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALESPROCESS);
        try {
            return salesProcessHome.findByPrimaryKey(processId);
        } catch (FinderException e) {
            return null;
        }
    }

    @Override
    protected String getValuesFinderMethodName() {
        return "findValueBySalesProcessId";
    }

    @Override
    protected String getEntityFinderMethodName() {
        return "findBySalesProcessId";
    }

    @Override
    protected Integer getEntityId(EJBLocalObject ejbLocalObject, SessionContext ctx) {
        return ((SalesProcess) ejbLocalObject).getProcessId();
    }

    @Override
    protected Integer getCompanyId(EJBLocalObject ejbLocalObject, SessionContext ctx) {
        return ((SalesProcess) ejbLocalObject).getCompanyId();
    }

    @Override
    protected String getEntityIdFieldName() {
        return "processId";
    }

    @Override
    public boolean isStateful() {
        return false;
    }
}
