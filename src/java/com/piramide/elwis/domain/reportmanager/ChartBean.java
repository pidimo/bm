package com.piramide.elwis.domain.reportmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * @author : ivan
 * @version : $Id ${NAME} ${time}
 */

public abstract class ChartBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setChartId(PKGenerator.i.nextKey(ReportConstants.TABLE_CHART));
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getChartId();

    public abstract void setChartId(Integer chartId);

    public abstract Integer getReportId();

    public abstract void setReportId(Integer reportId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getCategoryId();

    public abstract void setCategoryId(Integer categoryId);

    public abstract Boolean getIsEnable();

    public abstract void setIsEnable(Boolean isEnable);

    public abstract Integer getOrientation();

    public abstract void setOrientation(Integer orientation);

    public abstract Integer getPosition();

    public abstract void setPosition(Integer position);

    public abstract Integer getSerieId();

    public abstract void setSerieId(Integer serieId);

    public abstract Boolean getShowOnlyChart();

    public abstract void setShowOnlyChart(Boolean showOnlyChart);

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract String getAxisXLabel();

    public abstract void setAxisXLabel(String xAxisLabel);

    public abstract Integer getValueXId();

    public abstract void setValueXId(Integer xValueId);

    public abstract String getAxisYLabel();

    public abstract void setAxisYLabel(String yAxisLabel);

    public abstract Integer getValueYId();

    public abstract void setValueYId(Integer yValueId);

    public abstract Integer getValueZId();

    public abstract void setValueZId(Integer zValueId);

    public abstract Integer getChartType();

    public abstract void setChartType(Integer chartType);

    public abstract ColumnGroup getSerie();

    public abstract void setSerie(ColumnGroup serie);

    public abstract ColumnGroup getCategory();

    public abstract void setCategory(ColumnGroup category);

    public abstract void setXValue(Totalize xValue);

    public abstract Totalize getXValue();

    public abstract Totalize getYValue();

    public abstract void setYValue(Totalize yValue);

    public abstract Totalize getZValue();

    public abstract void setZValue(Totalize zValue);

    public abstract Report getReport();

    public abstract void setReport(Report report);
}
