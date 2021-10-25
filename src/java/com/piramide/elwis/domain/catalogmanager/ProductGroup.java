/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: ProductGroup.java 2453 2004-08-31 20:22:49Z ivan ${NAME}.java, v 2.0 16-ago-2004 16:54:38 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

public interface ProductGroup extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getGroupId();

    void setGroupId(Integer groupId);

    String getGroupName();

    void setGroupName(String groupName);

    Integer getParentGroupId();

    void setParentGroupId(Integer parentGroupId);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getChildProductGroupList();

    void setChildProductGroupList(Collection productGroup1);
}
