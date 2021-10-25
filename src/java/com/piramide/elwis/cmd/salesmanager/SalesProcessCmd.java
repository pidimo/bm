package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.common.FreeTextCmdUtil;
import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Customer;
import com.piramide.elwis.domain.salesmanager.SalesFreeText;
import com.piramide.elwis.domain.salesmanager.SalesProcess;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import com.piramide.elwis.dto.salesmanager.SalesProcessDTO;
import com.piramide.elwis.utils.CodeUtil;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJBLocalObject;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, control concurrency,
 * referencial integriry and entry duplicated); all relatinated with the Sales process
 * <p/>
 *
 * @author Fernando Montaño
 * @version $Id: SalesProcessCmd.java 10013 2010-12-14 18:08:15Z ivan $
 */
public class SalesProcessCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(SalesProcessCmd.class);

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing SalesProcessCmd...");
        super.setOp(this.getOp());
        super.checkDuplicate = false;
        super.isClearingForm = false;
        if (null != paramDTO.get("withReferences") && "".equals(paramDTO.get("withReferences"))) {
            super.checkReferences = false;
        }

        SalesProcess salesProcess = (SalesProcess) super.execute(ctx, new SalesProcessDTO(paramDTO));

        //read the address name
        if (salesProcess != null && salesProcess.getAddressId() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.put("addressId", salesProcess.getAddressId());
            try {

                Address address = (Address) EJBFactory.i.findEJB(addressDTO);
                resultDTO.put("addressName", address.getName());

                /*
                If the operation is create, then check if the addres is a customer, if it is not, then create
                the customer and update the code of the address.
                 */
                if (CRUDDirector.OP_CREATE.equals(getOp())) {
                    if (!CodeUtil.isCustomer(address.getCode())) {
                        CustomerDTO customerDTO = new CustomerDTO();
                        customerDTO.put(CustomerDTO.KEY_CUSTOMERID, address.getAddressId());
                        customerDTO.put("companyId", address.getCompanyId());

                        Customer customer = (Customer) EJBFactory.i.createEJB(customerDTO);
                        String newCustomerNumber = InvoiceUtil.i.getCustomerNumber(customer.getCompanyId());
                        if (null != newCustomerNumber) {
                            customer.setNumber(newCustomerNumber);
                        }

                        address.setCode((byte) (address.getCode() + CodeUtil.customer));
                    }

                    createCategoryValues(salesProcess, ctx);
                }

            } catch (Exception e) {
                ctx.setRollbackOnly();//if no address is found for a sales process there's something wrong.
                addressDTO.addNotFoundMsgTo(resultDTO);
                resultDTO.setResultAsFailure();
            }
        }

        FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, salesProcess, "DescriptionText", SalesFreeText.class,
                SalesConstants.JNDI_FREETEXT, FreeTextTypes.FREETEXT_SALES, "description");
    }

    public boolean isStateful() {
        return false;
    }

    @Override
    protected EJBLocalObject read(ComponentDTO dto, SessionContext ctx) {
        SalesProcess salesProcess = (SalesProcess) super.read(dto, ctx);
        if (null != salesProcess) {
            readCategoryValues(salesProcess, ctx);
        }

        return salesProcess;
    }

    @Override
    protected EJBLocalObject update(ComponentDTO dto, SessionContext ctx) {
        SalesProcess salesProcess = (SalesProcess) super.update(dto, ctx);
        if (null != salesProcess) {
            updateCategoryValues(salesProcess, ctx);
        }

        return salesProcess;
    }

    @SuppressWarnings(value = "unchecked")
    private void createCategoryValues(SalesProcess salesProcess, SessionContext ctx) {
        List<Map> sourceValues = new ArrayList<Map>();
        Map mapValues = new HashMap();
        mapValues.put("identifier", "processId");
        mapValues.put("value", salesProcess.getProcessId());
        sourceValues.add(mapValues);
        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.putParam("sourceValues", sourceValues);
        cmd.putParam(paramDTO);
        cmd.putParam("companyId", salesProcess.getCompanyId());
        cmd.setOp("createValues");
        cmd.executeInStateless(ctx);
    }

    @SuppressWarnings(value = "unchecked")
    private void readCategoryValues(SalesProcess salesProcess, SessionContext ctx) {
        List paramsAsList = Arrays.asList(salesProcess.getProcessId(), salesProcess.getCompanyId());

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.putParam("finderName", "findBySalesProcessId");
        cmd.putParam("params", paramsAsList);
        cmd.setOp("readCAtegoryFieldValues");
        cmd.executeInStateless(ctx);
        ResultDTO myResultDTO = cmd.getResultDTO();
        resultDTO.putAll(myResultDTO);
    }

    @SuppressWarnings(value = "unchecked")
    private void updateCategoryValues(SalesProcess salesProcess, SessionContext ctx) {
        List paramsAsList = Arrays.asList(salesProcess.getProcessId(), salesProcess.getCompanyId());

        List<Map> sourceValues = new ArrayList<Map>();
        Map mapValues = new HashMap();
        mapValues.put("identifier", "processId");
        mapValues.put("value", salesProcess.getProcessId());
        sourceValues.add(mapValues);

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.putParam("sourceValues", sourceValues);
        cmd.putParam("companyId", salesProcess.getCompanyId());
        cmd.putParam(paramDTO);
        cmd.putParam("finderName", "findBySalesProcessId");
        cmd.putParam("params", paramsAsList);
        cmd.setOp("updateValues");

        cmd.executeInStateless(ctx);
        resultDTO.put("processId", salesProcess.getProcessId());
    }
}
