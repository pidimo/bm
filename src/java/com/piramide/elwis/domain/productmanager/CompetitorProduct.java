/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CompetitorProduct.java 2417 2004-08-26 18:16:34Z mauren ${NAME}.java, v 2.0 23-ago-2004 14:22:02 Yumi Exp $
 */
package com.piramide.elwis.domain.productmanager;

import javax.ejb.EJBLocalObject;

public interface CompetitorProduct extends EJBLocalObject {
    Integer getCompetitorProductId();

    void setCompetitorProductId(Integer competitorProductId);

    Integer getChangeDate();

    void setChangeDate(Integer changeDate);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getCompetitorId();

    void setCompetitorId(Integer competitorId);

    Integer getEntryDate();

    void setEntryDate(Integer entryDate);

    Integer getProductId();

    void setProductId(Integer productId);

    String getProductName();

    void setProductName(String productName);

    Integer getVersion();

    void setVersion(Integer version);

    java.math.BigDecimal getPrice();

    void setPrice(java.math.BigDecimal price);

    String getDescription();

    void setDescription(String description);

}
