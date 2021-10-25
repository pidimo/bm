package com.piramide.elwis.cmd.dashboard;

import com.piramide.elwis.domain.dashboard.*;
import com.piramide.elwis.utils.DashboardConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author : ivan
 *         Date: Aug 31, 2006
 *         Time: 9:48:14 AM
 */
public class ComponentReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("executeInStateless(javax.ejb.SessionContext)");
        log.debug("paramDTO: " + paramDTO);

        int dbComponentId = new Integer(paramDTO.get("dbComponentId").toString());

        ComponentHome cHome = (ComponentHome) EJBFactory.i.getEJBLocalHome(DashboardConstants.JNDI_COMPONENT);

        ComponentColumnHome colHome = (ComponentColumnHome) EJBFactory.i.getEJBLocalHome(DashboardConstants.JNDI_COMPONENTCOLUMN);
        try {
            Component dbComponent = cHome.findByPrimaryKey(dbComponentId);

            Map componentMap = new HashMap();
            componentMap.put("XmlComponentId", dbComponent.getXmlComponentId());
            componentMap.put("id", dbComponent.getComponentId());

            Collection columns = colHome.findByComponentId(dbComponent.getComponentId());

            List columnList = new ArrayList();
            for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
                Map map = new HashMap();
                ComponentColumn column = (ComponentColumn) iterator.next();
                map.put("XmlColumnId", column.getXmlColumnId());
                map.put("order", column.getOrd());
                map.put("id", column.getComponentColumnId());
                columnList.add(map);
            }

            Collection filters = dbComponent.getFilters();

            List filterList = new ArrayList();
            for (Iterator it = filters.iterator(); it.hasNext();) {
                Map map = new HashMap();
                Filter filter = (Filter) it.next();

                map.put("value", filter.getVal());
                map.put("isRange", filter.getIsRange());
                map.put("id", filter.getFilterId());
                map.put("name", filter.getName());
                filterList.add(map);
            }

            resultDTO.put("COMPONENT_INFO", componentMap);
            resultDTO.put("COLUMNS", columnList);
            resultDTO.put("FILTERS", filterList);
        } catch (FinderException e) {
            e.printStackTrace();
            resultDTO.setResultAsFailure();
        }
    }

    public boolean isStateful() {
        return false;
    }
}
