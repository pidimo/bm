package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.cmd.catalogmanager.CurrencyCmd;
import com.piramide.elwis.dto.catalogmanager.CurrencyDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.admin.session.User;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: IncomingInvoiceCreateForwardAction.java 03-mar-2009 14:51:17
 */
public class IncomingInvoiceCreateForwardAction extends ForwardAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("IncomingInvoiceCreateForwardAction execute..........");
        DefaultForm defaultForm = (DefaultForm) form;
        if (!defaultForm.getDtoMap().containsKey("currencyId")) { //if first time
            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            Integer companyId = new Integer(user.getValue("companyId").toString());

            CurrencyCmd currencyCmd = new CurrencyCmd();
            currencyCmd.setOp("getBasicCurrency");
            currencyCmd.putParam("companyId", companyId);
            BusinessDelegate.i.execute(currencyCmd, request);
            ResultDTO resultDTO = currencyCmd.getResultDTO();
            if (resultDTO.containsKey("getBasicCurrency")) {
                if (resultDTO.get("getBasicCurrency") != null) {
                    CurrencyDTO currencyDTO = (CurrencyDTO) resultDTO.get("getBasicCurrency");
                    defaultForm.setDto("currencyId", currencyDTO.get("currencyId"));
                }
                /*else{
                    ActionErrors errors = new ActionErrors();
                    errors.add("defaultCurrencyNotFound", new ActionError("Invoice.basicCurrencyNotFound"));
                    saveErrors(request, errors);
                }*/
            }
        }
        if (!defaultForm.getDtoMap().containsKey("type")) {
            defaultForm.setDto("type", FinanceConstants.InvoiceType.Invoice.getConstantAsString());
        }

        ActionForward actionForward = super.execute(mapping, form, request, response);
        return (actionForward);

    }
}
