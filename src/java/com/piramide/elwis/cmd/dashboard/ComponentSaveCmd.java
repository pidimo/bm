package com.piramide.elwis.cmd.dashboard;

import com.piramide.elwis.domain.dashboard.*;
import com.piramide.elwis.utils.DashboardConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author : ivan
 *         Date: Aug 31, 2006
 *         Time: 9:48:31 AM
 */
public class ComponentSaveCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("executeInStateless(SessionContext ctx)");

        Integer dbComponentId = new Integer((String) paramDTO.get("dbComponentId"));

        ComponentHome home = (ComponentHome) EJBFactory.i.getEJBLocalHome(DashboardConstants.JNDI_COMPONENT);

        ComponentColumnHome columnHome = (ComponentColumnHome) EJBFactory.i.getEJBLocalHome(DashboardConstants.JNDI_COMPONENTCOLUMN);

        Component dbComponent = null;


        List<Map> filtersMapList = (List<Map>) paramDTO.get("FILTERS");
        try {
            dbComponent = home.findByPrimaryKey(dbComponentId);
        } catch (FinderException e) {
            e.printStackTrace();
        }


        if (null != dbComponent) {
            Collection filters = dbComponent.getFilters();

            for (Iterator it = filters.iterator(); it.hasNext();) {
                Filter f = (Filter) it.next();
                try {
                    f.remove();
                } catch (RemoveException e) {
                    //e.printStackTrace();
                }
                it = filters.iterator();
            }

            Collection myNewFilters = createFilters(filtersMapList, dbComponent);
            dbComponent.setFilters(myNewFilters);


            List<Map> visibleColumns = (List<Map>) paramDTO.get("VISIBLE_COLUMNS");
            List<Map> orderableColumns = (List<Map>) paramDTO.get("ORDERABLE_COLUMNS");

            Collection columnsAssigned = dbComponent.getColumns();
            for (Iterator it = columnsAssigned.iterator(); it.hasNext();) {
                ComponentColumn col = (ComponentColumn) it.next();

                if (!contain(visibleColumns, col.getXmlColumnId()) &&
                        !contain(orderableColumns, col.getXmlColumnId())) {
                    try {
                        col.remove();
                    } catch (RemoveException re) {
                        //re.printStackTrace();
                    }
                    it = columnsAssigned.iterator();
                }
            }


            if (null != visibleColumns) {
                for (Map colMap : visibleColumns) {
                    int xmlId = new Integer(colMap.get("XMLID").toString());
                    int position = new Integer(colMap.get("POSITION").toString());
                    String name = (String) colMap.get("COLUMN_NAME");
                    try {
                        ComponentColumn column = columnHome.findByComponentIdAndXMLColumnId(dbComponentId, xmlId);
                        column.setPosition(position);
                    } catch (FinderException e) {
                        //e.printStackTrace();
                        try {
                            ComponentColumn column = columnHome.create(xmlId, position, null);
                            column.setComponent(dbComponent);
                            column.setCompanyId(dbComponent.getCompanyId());
                        } catch (CreateException ce) {
                            //ce.printStackTrace();
                        }
                    }
                }
            }


            if (null != orderableColumns) {
                for (Map colMap : orderableColumns) {
                    int xmlId = new Integer(colMap.get("XMLID").toString());
                    String order = (String) colMap.get("ORDER");
                    try {
                        ComponentColumn column = columnHome.findByComponentIdAndXMLColumnId(dbComponentId, xmlId);
                        column.getOrd();
                        column.setOrd(order);
                    } catch (FinderException fe) {
                        //fe.printStackTrace();
                        try {
                            ComponentColumn column = columnHome.create(xmlId, null, order);
                            column.setComponent(dbComponent);
                            column.setCompanyId(dbComponent.getCompanyId());
                        } catch (CreateException ce) {
                            //ce.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    private boolean contain(List<Map> list, int id) {
        if (null != list) {
            for (Map m : list) {
                try {
                    int mapXMLId = new Integer(m.get("XMLID").toString());
                    if (mapXMLId == id) {
                        return true;
                    }
                }
                catch (NullPointerException npe) {
                }
            }
        }
        return false;
    }

    private Collection createFilters(List<Map> list, Component c) {
        Collection filters = new ArrayList();
        FilterHome home = (FilterHome) EJBFactory.i.getEJBLocalHome(DashboardConstants.JNDI_FILTER);
        for (Map map : list) {
            String name = (String) map.get("FILTER_NAME");
            String value = String.valueOf(map.get("VALUE"));
            Boolean isRange = (Boolean) map.get("ISRANGE");
            try {
                Filter filter = home.create(name, value, isRange);
                filter.setCompanyId(c.getCompanyId());
                filters.add(filter);
            } catch (CreateException e) {
                //e.printStackTrace();
            }
        }

        return filters;
    }

    public boolean isStateful() {
        return false;
    }

}
