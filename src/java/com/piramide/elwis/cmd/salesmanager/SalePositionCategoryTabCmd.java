package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryTabManagerCmd;
import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.salesmanager.SalePosition;
import com.piramide.elwis.domain.salesmanager.SalePositionHome;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.1
 */
public class SalePositionCategoryTabCmd extends CategoryTabManagerCmd {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    protected EJBLocalObject findEJBLocalObject(SessionContext ctx) {
        Integer salePositionId = EJBCommandUtil.i.getValueAsInteger(this, "salePositionId");

        SalePositionHome salePositionHome = (SalePositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);
        if (salePositionId != null) {
            try {
                return salePositionHome.findByPrimaryKey(salePositionId);
            } catch (FinderException e) {
                log.debug("Not found SalePosition for category tab:" + salePositionId + e);
            }
        }
        return null;
    }

    @Override
    protected String getValuesFinderMethodName() {
        return "findValueBySalePositionId";
    }

    @Override
    protected String getEntityFinderMethodName() {
        return "findBySalePositionId";
    }

    @Override
    protected Integer getEntityId(EJBLocalObject ejbLocalObject, SessionContext ctx) {
        return ((SalePosition) ejbLocalObject).getSalePositionId();
    }

    @Override
    protected Integer getCompanyId(EJBLocalObject ejbLocalObject, SessionContext ctx) {
        return ((SalePosition) ejbLocalObject).getCompanyId();
    }

    @Override
    protected String getEntityIdFieldName() {
        return "salePositionId";
    }

    @Override
    public boolean isStateful() {
        return false;
    }
}
