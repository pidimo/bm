package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.reportmanager.FilterHome;
import com.piramide.elwis.dto.reports.FilterDTO;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Jatun s.r.l.
 *
 * @author miky
 * @version $Id: FilterUpdateCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class FilterUpdateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing FilterCreateCmd................" + paramDTO);

        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
        Integer reportId = Integer.valueOf(paramDTO.get("reportId").toString());

        FilterHome filterHome = (FilterHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_FILTER);
        Integer sequence = null;
        try {
            sequence = new Integer((filterHome.selectMaxSequence(reportId, companyId)).intValue() + 1);
        } catch (FinderException e) {
            sequence = new Integer(1);
            log.debug("Not sequence defined.............");
        }
        String aliasCondition = ReportConstants.ALIAS_FILTER + sequence;

        FilterDTO filterDTO = new FilterDTO();
        filterDTO.putAll(paramDTO);

        filterDTO.remove("path");
        filterDTO.put("tempPath", paramDTO.get("path")); //used to set from bean
        filterDTO.put("sequence", sequence);
        filterDTO.put("aliasCondition", aliasCondition);

        ExtendedCRUDDirector.i.create(filterDTO, resultDTO, true);
    }
}
