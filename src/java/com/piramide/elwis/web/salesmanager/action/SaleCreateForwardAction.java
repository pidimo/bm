package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.cmd.salesmanager.SalesProcessCmd;
import com.piramide.elwis.dto.catalogmanager.CurrencyDTO;
import com.piramide.elwis.dto.contactmanager.CompanyDTO;
import com.piramide.elwis.dto.salesmanager.SalesProcessDTO;
import com.piramide.elwis.web.salesmanager.form.SaleForm;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SaleCreateForwardAction extends ForwardAction {
    private Log log = LogFactory.getLog(SaleCreateForwardAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        SaleForm saleForm = (SaleForm) form;
        Integer processId = getProcessId(request);

        if (null == processId) {
            CurrencyDTO basicCurrencyDTO =
                    com.piramide.elwis.web.catalogmanager.el.Functions.getBasicCurrency(request);
            if (null != basicCurrencyDTO) {
                saleForm.setDto("currencyId", basicCurrencyDTO.get("currencyId"));
            }

            CompanyDTO companyDTO =
                    com.piramide.elwis.web.contactmanager.el.Functions.getCompanyConfiguration(request);

            saleForm.setDto("netGross", companyDTO.get("netGross"));
            return super.execute(mapping, form, request, response);
        }

        SalesProcessDTO salesProcessDTO = getSalesProcess(processId, request);

        saleForm.setDto("addressId", salesProcessDTO.get("addressId"));
        saleForm.setDto("sellerId", salesProcessDTO.get("employeeId"));
        saleForm.setDto("processName", salesProcessDTO.get("processName"));
        saleForm.setDto("processId", processId);

        return super.execute(mapping, saleForm, request, response);
    }


    private Integer getProcessId(HttpServletRequest request) {
        String processId = request.getParameter("processId");
        try {
            return Integer.valueOf(processId);
        } catch (NumberFormatException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }


    private SalesProcessDTO getSalesProcess(Integer processId, HttpServletRequest request) {
        SalesProcessDTO salesProcessDTO = new SalesProcessDTO();
        SalesProcessCmd salesProcessCmd = new SalesProcessCmd();
        salesProcessCmd.setOp("read");
        salesProcessCmd.putParam("processId", processId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(salesProcessCmd, request);
            salesProcessDTO.putAll(resultDTO);
        } catch (AppLevelException e) {
            log.error("-> Execute " + SalesProcessCmd.class.getName() + " FAIL");
        }

        return salesProcessDTO;
    }
}
