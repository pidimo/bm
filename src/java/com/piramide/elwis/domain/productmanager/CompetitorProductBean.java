/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CompetitorProductBean.java 2436 2004-08-27 21:23:53Z mauren ${NAME}.java, v 2.0 23-ago-2004 14:22:02 Yumi Exp $
 */
package com.piramide.elwis.domain.productmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class CompetitorProductBean implements EntityBean {

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setCompetitorProductId(PKGenerator.i.nextKey(ProductConstants.TABLE_COMPETITORPRODUCT));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
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


    public abstract Integer getCompetitorProductId();

    public abstract void setCompetitorProductId(Integer competitorProductId);

    public abstract Integer getChangeDate();

    public abstract void setChangeDate(Integer changeDate);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getCompetitorId();

    public abstract void setCompetitorId(Integer competitorId);

    public abstract Integer getEntryDate();

    public abstract void setEntryDate(Integer entryDate);

    public abstract Integer getProductId();

    public abstract void setProductId(Integer productId);

    public abstract String getProductName();

    public abstract void setProductName(String productName);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract java.math.BigDecimal getPrice();

    public abstract void setPrice(java.math.BigDecimal price);

    public abstract String getDescription();

    public abstract void setDescription(String description);

}
