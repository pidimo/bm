package com.piramide.elwis.domain.reportmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * @author : ivan
 * @version : $Id ${NAME} ${time}
 */

public interface Totalize extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getFormula();

    void setFormula(String formula);

    String getName();

    void setName(String name);

    Integer getReportId();

    void setReportId(Integer reportId);

    Integer getTotalizeId();

    void setTotalizeId(Integer totalizeId);

    Integer getTotalizeType();

    void setTotalizeType(Integer type);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getColumnTotalizers();

    void setColumnTotalizers(Collection columnTotalizers);

    Chart getChartXValue();

    void setChartXValue(Chart chartXValue);

    Chart getChartYValue();

    void setChartYValue(Chart chartYValue);

    Chart getChartZValue();

    void setChartZValue(Chart c);
}
