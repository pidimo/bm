package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.utils.InvoiceUtil;

import javax.ejb.SessionContext;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProductContractNumberGeneratorCmd extends ProductContractCmd {
    @Override
    public void executeInStateless(SessionContext sessionContext) {
        Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
        String customerNumber = (String) paramDTO.get("customerNumber");
        generateContractNumber(companyId, customerNumber);
    }

    private void generateContractNumber(Integer companyId, String customerNumber) {
        String number = InvoiceUtil.i.getProductContractNumber(companyId, customerNumber);
        if (null != number) {
            resultDTO.put("generateContractNumber", number);
        }
    }

    public boolean isStateful() {
        return false;
    }
}
