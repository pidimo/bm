package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.dto.reports.FilterValueDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: FilterValueCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class FilterValueCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing FilterValueCmd................" + paramDTO);

        super.setOp(this.getOp());
        super.checkDuplicate = false;
        super.execute(ctx, new FilterValueDTO(paramDTO));
    }
}
