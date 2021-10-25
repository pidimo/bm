package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * This Class represents the Local interface of the CostCenter Entity Bean
 *
 * @author Ivan
 * @version $Id: CostCenter.java 1922 2004-07-19 21:20:07Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface CostCenter extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getCostCenterId();

    void setCostCenterId(Integer costCenterId);

    String getCostCenterName();

    void setCostCenterName(String name);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getCostCencers();

    void setCostCencers(Collection costCencers);

    CostCenter getCostCencer();

    void setCostCencer(CostCenter costCencer);

    Integer getParentCostCenterId();

    void setParentCostCenterId(Integer parentCostCenterId);
}
