package com.piramide.elwis.domain.reportmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author : ivan
 * @version : $Id ${NAME} ${time}
 */

public interface ColumnGroup extends EJBLocalObject {
    Integer getAxis();

    void setAxis(Integer axis);

    Integer getColumnGroupId();

    void setColumnGroupId(Integer columnGroupId);

    Boolean getColumnOrder();

    void setColumnOrder(Boolean columnOrder);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getGroupByDate();

    void setGroupByDate(Integer groupByDate);

    Integer getColumnId();

    void setColumnId(Integer reportColumnId);

    Integer getSequence();

    void setSequence(Integer squence);

    Integer getVersion();

    void setVersion(Integer version);

    Column getColumn();

    void setColumn(Column group);

    Chart getChartSerie();

    void setChartSerie(Chart chartSerie);

    Chart getChartCategory();

    void setChartCategory(Chart chartCategory);
}
