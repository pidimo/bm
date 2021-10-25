package com.piramide.elwis.domain.salesmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.math.BigDecimal;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: ActionPositionBean.java 9856 2009-11-12 22:37:48Z ivan ${NAME}.java, v 2.0 24-01-2005 03:54:51 PM Fernando Montaño Exp $
 */


public abstract class ActionPositionBean implements EntityBean {

    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setPositionId(PKGenerator.i.nextKey(SalesConstants.TABLE_ACTIONPOSITION));
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

    public abstract BigDecimal getAmount();

    public abstract void setAmount(BigDecimal amount);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getContactId();

    public abstract void setContactId(Integer contactId);

    public abstract Integer getDescriptionId();

    public abstract void setDescriptionId(Integer descriptionId);

    public abstract Integer getPositionId();

    public abstract void setPositionId(Integer positionId);

    public abstract java.math.BigDecimal getPrice();

    public abstract void setPrice(java.math.BigDecimal price);

    public abstract Integer getProductId();

    public abstract void setProductId(Integer productId);

    public abstract Integer getProcessId();

    public abstract void setProcessId(Integer processId);

    public abstract java.math.BigDecimal getTotalPrice();

    public abstract void setTotalPrice(java.math.BigDecimal totalPrice);

    public abstract Integer getUnit();

    public abstract void setUnit(Integer unit);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Action getAction();

    public abstract void setAction(Action action);

    public abstract SalesFreeText getDescriptionText();

    public void setDescriptionText(EJBLocalObject descriptionText) {
        setDescriptionText((SalesFreeText) descriptionText);
    }

    public abstract Integer ejbSelectMaxPositionNumber(Integer processId, Integer contactId, Integer companyId) throws FinderException;

    public Integer ejbHomeSelectMaxPositionNumber(Integer processId, Integer contactId, Integer companyId) throws FinderException {
        return ejbSelectMaxPositionNumber(processId, contactId, companyId);
    }

    public abstract void setDescriptionText(SalesFreeText descriptionText);

    public abstract java.math.BigDecimal getDiscount();

    public abstract void setDiscount(java.math.BigDecimal discount);

    public abstract Integer getNumber();

    public abstract void setNumber(Integer number);

    public abstract java.math.BigDecimal getUnitPriceGross();

    public abstract void setUnitPriceGross(java.math.BigDecimal unitPriceGross);

    public abstract java.math.BigDecimal getTotalPriceGross();

    public abstract void setTotalPriceGross(java.math.BigDecimal totalPriceGross);
}
