package com.piramide.elwis.domain.reportmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author : ivan
 * @version : $Id ${NAME} ${time}
 */

public interface Chart extends EJBLocalObject {

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getChartId();

    void setChartId(Integer chartId);

    Integer getReportId();

    void setReportId(Integer reportId);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getCategoryId();

    void setCategoryId(Integer categoryId);

    Boolean getIsEnable();

    void setIsEnable(Boolean isEnable);

    Integer getOrientation();

    void setOrientation(Integer orientation);

    Integer getPosition();

    void setPosition(Integer position);

    Integer getSerieId();

    void setSerieId(Integer serieId);

    Boolean getShowOnlyChart();

    void setShowOnlyChart(Boolean showOnlyChart);

    String getTitle();

    void setTitle(String title);

    String getAxisXLabel();

    void setAxisXLabel(String xAxisLabel);

    Integer getValueXId();

    void setValueXId(Integer xValueId);

    String getAxisYLabel();

    void setAxisYLabel(String yAxisLabel);

    Integer getValueYId();

    void setValueYId(Integer yValueId);

    Integer getValueZId();

    void setValueZId(Integer zValueId);

    Integer getChartType();

    void setChartType(Integer chartType);

    ColumnGroup getSerie();

    void setSerie(ColumnGroup serie);

    ColumnGroup getCategory();

    void setCategory(ColumnGroup category);

    void setXValue(Totalize xValue);

    Totalize getXValue();

    Totalize getYValue();

    void setYValue(Totalize yValue);

    Totalize getZValue();

    void setZValue(Totalize zValue);

    Report getReport();

    void setReport(Report report);
}
