package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.domain.reportmanager.Filter;
import com.piramide.elwis.domain.reportmanager.FilterHome;
import com.piramide.elwis.dto.reports.FilterDTO;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Jatun S.R.L.
 * Cmd to read all report filters defined as IsParameters
 *
 * @author Miky
 * @version $Id: FilterAllReadCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class FilterAllReadCmd extends FilterCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing FilterAllReadCmd................" + paramDTO);

        Integer reportId = Integer.valueOf(paramDTO.get("reportId").toString());
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());

        FilterHome home = (FilterHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_FILTER);

        Collection filters = new ArrayList();
        List dtos = new ArrayList();
        try {
            filters = home.findByReportIdAndIsParameter(reportId, companyId);
        } catch (FinderException fe) {
            log.debug("Error in finder execute...", fe);
            resultDTO.setResultAsFailure();
        }

        for (Iterator iterator = filters.iterator(); iterator.hasNext();) {
            Filter filter = (Filter) iterator.next();
            FilterDTO filterDTO = new FilterDTO();
            DTOFactory.i.copyToDTO(filter, filterDTO);
            filterDTO.put("path", new String(filter.getPath()));

            //add filter values
            String parsedFilterValue = readFilterValues(filter);
            if (parsedFilterValue != null) {
                filterDTO.put("filterValue", parsedFilterValue);
            }
            dtos.add(filterDTO);
        }
        resultDTO.put("filterDTOList", dtos);

    }
}
