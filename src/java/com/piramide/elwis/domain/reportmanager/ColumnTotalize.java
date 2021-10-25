package com.piramide.elwis.domain.reportmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author : ivan
 * @version : $Id ${NAME} ${time}
 */

public interface ColumnTotalize extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getColumnId();

    void setColumnId(Integer columnId);

    Integer getTotalizeId();

    void setTotalizeId(Integer totalizeId);

}
