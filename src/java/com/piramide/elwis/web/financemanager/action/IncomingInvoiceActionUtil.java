package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.cmd.financemanager.IncomingInvoiceCmd;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: IncomingInvoiceActionUtil.java 27-feb-2009 17:13:59
 */
public class IncomingInvoiceActionUtil {

    public static void readBasicInformation(HttpServletRequest request, Log log) {

        Object incomingInvoiceIdObj = request.getParameter("incomingInvoiceId");
        if (incomingInvoiceIdObj == null) {
            incomingInvoiceIdObj = request.getParameter("dto(incomingInvoiceId)");
        }
        ResultDTO resultDTO = new ResultDTO();
        boolean error = false;
        if (incomingInvoiceIdObj != null && incomingInvoiceIdObj.toString().length() > 0) {
            IncomingInvoiceCmd incomingInvoiceCmd = new IncomingInvoiceCmd();
            incomingInvoiceCmd.putParam("incomingInvoiceId", incomingInvoiceIdObj);
            incomingInvoiceCmd.putParam("op", "lightRead");
            try {
                resultDTO = BusinessDelegate.i.execute(incomingInvoiceCmd, request);
            } catch (AppLevelException e) {
                log.debug("Incoming invoice was deleted..........");
                error = true;
            }
        } else {
            error = true;
        }
        if (!error && !resultDTO.isFailure()) {
            request.setAttribute("invoiceNumber", resultDTO.get("invoiceNumber"));
            request.setAttribute("incomingInvoiceVersion", resultDTO.get("version"));
            request.setAttribute("incomingInvoiceId", incomingInvoiceIdObj);
            request.setAttribute("incomingInvoiceOpenAmount", resultDTO.get("openAmount"));
        }
    }
}
