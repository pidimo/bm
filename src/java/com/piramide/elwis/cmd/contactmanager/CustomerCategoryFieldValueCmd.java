package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryTabCmd;
import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.domain.contactmanager.Customer;
import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;

import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CustomerCategoryFieldValueCmd extends EJBCommand {
    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;
        String op = getOp();
        if ("update".equals(op)) {
            isRead = false;
            update(ctx);
        }
        if (isRead) {
            read(ctx);
        }
    }

    private void update(SessionContext ctx) {
        CustomerDTO customerDTO = new CustomerDTO(paramDTO);
        customerDTO.put("customerId", paramDTO.get("addressId"));
        Customer customer = (Customer) CRUDDirector.i.read(customerDTO, resultDTO); //if read from customer detail.

        if (null == customer) {
            resultDTO.put("op", "");
            resultDTO.setForward("Detail");
            return;
        }

        CategoryTabCmd categoryTabCmd = new CategoryTabCmd();
        categoryTabCmd.setOp("checkCategoryTab");
        categoryTabCmd.putParam("finderName", "findValueByCustomerId");
        categoryTabCmd.putParam("params", new Object[]{customer.getCustomerId()});
        categoryTabCmd.putParam("categoryTabId", Integer.valueOf(paramDTO.get("categoryTabId").toString()));
        categoryTabCmd.executeInStateless(ctx);
        if (null != categoryTabCmd.getResultDTO().get("errorMessage")) {
            resultDTO.addResultMessage(categoryTabCmd.getResultDTO().get("errorMessage").toString());
            resultDTO.setForward("Detail");
            return;
        }

        //update categoryfieldvalues object
        String finderName = "findByCustomerId";
        Object[] params = new Object[]{customer.getCustomerId(), customer.getCompanyId()};
        List paramsAsList = Arrays.asList(params);

        List<Map> sourceValues = new ArrayList<Map>();
        Map map = new HashMap();
        map.put("identifier", "customerId");
        map.put("value", customer.getCustomerId());
        sourceValues.add(map);

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.putParam("sourceValues", sourceValues);
        cmd.putParam("companyId", customer.getCompanyId());
        cmd.putParam(paramDTO);
        cmd.putParam("finderName", finderName);
        cmd.putParam("params", paramsAsList);
        cmd.setOp("updateValues");
        cmd.executeInStateless(ctx);

        resultDTO.put("op", "read");
        if ("Fail".equals(cmd.getResultDTO().getForward())) {
            resultDTO.setForward("Fail");
            resultDTO.addResultMessage("Common.error.concurrency");
            return;
        }

        resultDTO.addResultMessage("Common.changesOK");
    }

    private void read(SessionContext ctx) {
        CustomerDTO customerDTO = new CustomerDTO(paramDTO);
        customerDTO.put("customerId", paramDTO.get("addressId"));
        Customer customer = (Customer) CRUDDirector.i.read(customerDTO, resultDTO); //if read from customer detail.


        if (null == customer) {
            resultDTO.put("op", "");
            resultDTO.setForward("Detail");
            return;
        }

        CategoryTabCmd categoryTabCmd = new CategoryTabCmd();
        categoryTabCmd.setOp("checkCategoryTab");
        categoryTabCmd.putParam("finderName", "findValueByCustomerId");
        categoryTabCmd.putParam("params", new Object[]{customer.getCustomerId()});
        categoryTabCmd.putParam("categoryTabId", Integer.valueOf(paramDTO.get("categoryTabId").toString()));
        categoryTabCmd.executeInStateless(ctx);
        if (null != categoryTabCmd.getResultDTO().get("errorMessage")) {
            resultDTO.addResultMessage(categoryTabCmd.getResultDTO().get("errorMessage").toString());
            resultDTO.setForward("Detail");
            return;
        }

        //read categoryFieldValues object
        String finderName = "findByCustomerId";
        Object[] params = new Object[]{customer.getCustomerId(), customer.getCompanyId()};
        List paramsAsList = Arrays.asList(params);

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.putParam("finderName", finderName);
        cmd.putParam("params", paramsAsList);
        cmd.setOp("readCAtegoryFieldValues");
        cmd.executeInStateless(ctx);
        ResultDTO myResultDTO = cmd.getResultDTO();
        resultDTO.putAll(myResultDTO);
    }

    public boolean isStateful() {
        return false;
    }
}
