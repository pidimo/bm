package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.reportmanager.Filter;
import com.piramide.elwis.domain.reportmanager.Report;
import com.piramide.elwis.dto.supportmanager.ArticleDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Nov 15, 2005
 * Time: 5:50:46 PM
 * To change this template use File | Settings | File Templates.
 */

public class ColumnListReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing read ColumnListReadCmd   .... command");

        Map map = new HashMap();
        ArrayList filterList = new ArrayList();
        Report report = (Report) ExtendedCRUDDirector.i.read(new ArticleDTO(paramDTO), resultDTO, false);
        Collection filters = (Collection) report.getFilters();

        for (Iterator i = filters.iterator(); i.hasNext();) {
            Filter filter = null;
            map = new HashMap();
            filter = (Filter) i.next();
            map.put("module", filter.getTableReference());
            map.put("field", filter.getColumnReference());
            map.put("operator", filter.getOperator());
            map.put("type", filter.getColumnType());
            //map.put("value", filter.getFilterValue());
            filterList.add(map);
        }
        resultDTO.put("filterList", filterList);
    }

    public boolean isStateful() {
        return false;
    }
}

