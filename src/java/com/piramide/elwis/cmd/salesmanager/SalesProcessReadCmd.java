package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.salesmanager.SalesProcess;
import com.piramide.elwis.domain.salesmanager.SalesProcessHome;
import com.piramide.elwis.dto.salesmanager.SalesProcessDTO;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SalesProcessReadCmd extends SaleManagerCmd {
    private Log log = LogFactory.getLog(SalesProcessReadCmd.class);

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;
        if ("getSalesProcessDTO".equals(getOp())) {
            isRead = false;
            Integer salesProcessId = EJBCommandUtil.i.getValueAsInteger(this, "salesProcessId");
            getSalesProcessDTO(salesProcessId, ctx);
        }
    }

    private void getSalesProcessDTO(Integer salesProcessId, SessionContext ctx) {
        SalesProcess salesProcess = getSalesProcess(salesProcessId);
        if (null != salesProcess) {
            SalesProcessDTO salesProcessDTO = new SalesProcessDTO();
            DTOFactory.i.copyToDTO(salesProcess, salesProcessDTO);

            //setting up in salesProcessDTO the name of the salesprocess contact to display it in the ui
            salesProcessDTO.put("addressName", readAddressName(salesProcess.getAddressId(), ctx));

            resultDTO.put("getSalesProcessDTO", salesProcessDTO);
        }
    }

    private SalesProcess getSalesProcess(Integer salesProcessId) {
        SalesProcessHome salesProcessHome =
                (SalesProcessHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALESPROCESS);
        try {
            return salesProcessHome.findByPrimaryKey(salesProcessId);
        } catch (FinderException e) {
            log.debug("-> Read SalesProcess salesProcessId=" + salesProcessId + " FAIL ");
            return null;
        }
    }

    public boolean isStateful() {
        return false;
    }
}
