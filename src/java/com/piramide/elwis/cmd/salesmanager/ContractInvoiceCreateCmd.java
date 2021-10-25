package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.InvoiceDocumentValues;
import com.piramide.elwis.cmd.common.SystemPattern;
import com.piramide.elwis.cmd.financemanager.InvoiceCreateCmd;
import com.piramide.elwis.cmd.financemanager.InvoiceDocumentCmd;
import com.piramide.elwis.cmd.financemanager.InvoicePositionCmd;
import com.piramide.elwis.cmd.salesmanager.util.*;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.catalogmanager.Currency;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.contactmanager.Customer;
import com.piramide.elwis.domain.contactmanager.CustomerHome;
import com.piramide.elwis.domain.financemanager.Invoice;
import com.piramide.elwis.domain.financemanager.InvoiceHome;
import com.piramide.elwis.domain.financemanager.InvoicePosition;
import com.piramide.elwis.domain.financemanager.InvoicePositionHome;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.domain.salesmanager.*;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeZone;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.math.BigDecimal;
import java.util.*;

/**
 * Jatun S.R.L.
 * Cmd to create invoices and your invoice positions based in contract and sale positions
 *
 * @author Miky
 * @version $Id: ContractInvoiceCreateCmd.java 20-nov-2008 17:08:50 $
 */
public class ContractInvoiceCreateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());
    private Integer contractDateFilter;
    private Map<String, List> failInfoMap;

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ContractInvoiceCreateCmd..........." + paramDTO);

        List contractIdsList = (List) paramDTO.get("idsContract");
        List salePositionIdList = (List) paramDTO.get("idsSalePosition");
        boolean isFromContractsToInvoice = ("true".equals(paramDTO.get("isFromContracts")));

        Integer userId = new Integer(paramDTO.get("userId").toString());
        DateTimeZone timeZone = getDateTimeZone(userId);
        contractDateFilter = new Integer(paramDTO.get("contractDateFilter").toString());

        //verifiy if current user is internal and employee, this is required to generate document
        if (!isInternalUser(userId)) {
            resultDTO.addResultMessage("Invoice.document.internalUserRequired");
            addFailForward(ctx);
            return;
        }

        List documentIdsList = new ArrayList();

        failInfoMap = new HashMap<String, List>();

        int totalProcessed = contractIdsList.size();
        int totalValidContracts = 0;
        int totalValidSalePositions = 0;
        int totalInvoiceCreated = 0;
        Integer lastInvoiceId = null;
        Integer lastInvoiceAddressId = null;
        Integer lastInvoiceContactPersonId = null;

        Map<String, List<Map>> addressGroupedMap = new HashMap<String, List<Map>>();
        groupContractsByAddress(contractIdsList, addressGroupedMap, isFromContractsToInvoice);

        //add in group by address
        if (salePositionIdList != null && !salePositionIdList.isEmpty()) {
            totalProcessed = totalProcessed + salePositionIdList.size();
            groupSalePositionsByAddress(salePositionIdList, addressGroupedMap);
        }

        if (!failInfoMap.isEmpty() && !"true".equals(paramDTO.get("invoiceAnyway"))) {
            resultDTO.setForward("Invalid");
            resultDTO.addResultMessage("ContractToInvoice.invalidError.generateAnyway");
            resultDTO.put("invalidToInvoiceMap", failInfoMap);
            resultDTO.putAll(paramDTO);
            return;
        }

        //process prouped contracts
        for (List<Map> contractsList : addressGroupedMap.values()) {
            Invoice invoice = null;
            for (Map contractInfoMap : contractsList) {

                if (contractInfoMap.containsKey("contractId")) {
                    Integer contractId = new Integer(contractInfoMap.get("contractId").toString());

                    //read contract
                    ProductContract contract = readProductContract(contractId);
                    //ignore if this has been deleted
                    if (contract != null) {
                        if (invoice == null) {
                            invoice = createInvoice(contract, ctx);
                        }
                        if (invoice != null) {
                            boolean hasCreatedPositions = createInvoicePositionForContract(invoice, contract, timeZone, ctx);
                            if (hasCreatedPositions) {
                                totalValidContracts++;
                            } else {
                                return; //error
                            }
                        } else {
                            return; //error
                        }
                    }

                } else if (contractInfoMap.containsKey("salePositionId")) {
                    Integer salePositionId = new Integer(contractInfoMap.get("salePositionId").toString());
                    SalePosition salePosition = readSalePosition(salePositionId);
                    if (salePosition != null) {
                        if (invoice == null) {
                            invoice = createInvoiceToSalePosition(salePosition, ctx);
                        }
                        if (invoice != null) {
                            boolean hasCreatedPositions = createInvoicePositionForSalePosition(invoice, salePosition, ctx);
                            if (hasCreatedPositions) {
                                totalValidSalePositions++;
                            } else {
                                return; //error
                            }
                        } else {
                            return; //error
                        }
                    }
                }
            }

            //generate invoice document
            if (invoice != null) {
                Integer documentId = generateInvoiceDocument(invoice, userId, ctx);
                if (documentId != null) {
                    documentIdsList.add(documentId);
                    totalInvoiceCreated++;
                    lastInvoiceId = invoice.getInvoiceId();
                    lastInvoiceAddressId = invoice.getAddressId();
                    lastInvoiceContactPersonId = invoice.getContactPersonId();
                } else {
                    //error in generate document
                    return;
                }
            }
        }


        resultDTO.put("documentGenIds", documentIdsList);

        resultDTO.put("totalProcessed", totalProcessed);
        resultDTO.put("totalCreated", totalInvoiceCreated);
        resultDTO.put("totalValidContract", totalValidContracts);
        resultDTO.put("totalValidSalePosition", totalValidSalePositions);
        resultDTO.put("totalFailed", (totalProcessed - totalValidContracts - totalValidSalePositions));

        resultDTO.put("invalidToInvoiceMap", failInfoMap);
        resultDTO.put("lastInvoiceId", (totalInvoiceCreated == 1) ? lastInvoiceId : null);
        resultDTO.put("lastInvoiceAddressId", (totalInvoiceCreated == 1) ? lastInvoiceAddressId : null);
        resultDTO.put("lastInvoiceContactPersonId", (totalInvoiceCreated == 1) ? lastInvoiceContactPersonId : null);
    }

    private void groupContractsByAddress(List<String> contractIdsList, Map<String, List<Map>> addressGroupedMap, boolean isFromContractsToInvoice) {

        for (String contractId : contractIdsList) {

            ProductContract contract = readProductContract(new Integer(contractId));
            if (contract == null) {
                continue;
            }
            //validate contract (paycondition, product account)
            if (!isValidContract(contract)) {
                continue;
            }

            SalePosition salePosition = readSalePosition(contract.getSalePositionId());

            //save in grouped list
            Map contractInfoMap = new HashMap();
            contractInfoMap.put("contractId", contract.getContractId());
            if (salePosition != null) {
                contractInfoMap.put("endCustomerId", salePosition.getCustomerId());
            }

            //elements to group
            LinkedList<Object> groupElementList = new LinkedList<Object>();
            groupElementList.add(contract.getAddressId());
            groupElementList.add(contract.getContactPersonId());
            groupElementList.add(contract.getCurrencyId());
            groupElementList.add(contract.getPayConditionId());
            groupElementList.add(contract.getNetGross());

            if (isFromContractsToInvoice) {
                //if is from contracts to invoice (finances), add group by pay method
                groupElementList.add(contract.getPayMethod());
            }

            String contractGrouping = contract.getGrouping();
            if (contractGrouping != null && !"".equals(contractGrouping.trim())) {
                groupElementList.add(contractGrouping.trim().toLowerCase());
            }

            String groupKey = composeGroupKey(groupElementList);

            if (addressGroupedMap.containsKey(groupKey)) {
                List idsList = addressGroupedMap.get(groupKey);
                idsList.add(contractInfoMap);
            } else {
                List<Map> idsList = new ArrayList<Map>();
                idsList.add(contractInfoMap);

                addressGroupedMap.put(groupKey, idsList);
            }

        }

        //order grouped contracts by end salePosition.customerId
        orderByEndCustomer(addressGroupedMap);
    }

    private void orderByEndCustomer(Map<String, List<Map>> addressGroupedMap) {

        for (Iterator<String> iterator = addressGroupedMap.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();

            List<Map> contractsList = addressGroupedMap.get(key);
            ArrayList<Map> sortedContractMapList = new ArrayList<Map>();

            for (Map contractInfoMap : contractsList) {
                Integer endCustomerId = (Integer) contractInfoMap.get("endCustomerId");

                Integer sortedIndex = getSortedIndexForEndCustomer(endCustomerId, sortedContractMapList);
                if (sortedIndex != null) {
                    sortedContractMapList.add(sortedIndex + 1, contractInfoMap);
                } else {
                    sortedContractMapList.add(contractInfoMap);
                }
            }

            //define the new ordered list
            addressGroupedMap.put(key, sortedContractMapList);
        }
    }

    private Integer getSortedIndexForEndCustomer(Integer customerId, ArrayList<Map> sortedContractMapList) {
        Integer sortedIndex = null;

        if (customerId != null) {
            for (int i = 0; i < sortedContractMapList.size(); i++) {
                Map contractInfoMap = sortedContractMapList.get(i);
                if (customerId.equals(contractInfoMap.get("endCustomerId"))) {
                    sortedIndex = i;
                }
            }
        }
        return sortedIndex;
    }

    private void groupSalePositionsByAddress(List<String> salePositionIdList, Map<String, List<Map>> addressGroupedMap) {

        for (String salePositionId : salePositionIdList) {

            SalePosition salePosition = readSalePosition(new Integer(salePositionId));
            if (salePosition == null) {
                continue;
            }

            Sale sale = readSale(salePosition.getSaleId());
            //validate sale position (paycondition, product account)
            if (!isValidSalePosition(salePosition, sale)) {
                continue;
            }

            //save in grouped list
            Map salePositionInfoMap = new HashMap();
            salePositionInfoMap.put("salePositionId", salePosition.getSalePositionId());

            Integer payConditionId = readCustomer(salePosition.getCustomerId()).getPayConditionId();

            //elements to group
            LinkedList<Object> groupElementList = new LinkedList<Object>();
            groupElementList.add(salePosition.getCustomerId());
            groupElementList.add(salePosition.getContactPersonId());
            groupElementList.add(sale.getCurrencyId());
            groupElementList.add(payConditionId);
            groupElementList.add(sale.getNetGross());

            String groupKey = composeGroupKey(groupElementList);

            if (addressGroupedMap.containsKey(groupKey)) {
                List idsList = addressGroupedMap.get(groupKey);
                idsList.add(salePositionInfoMap);
            } else {
                List<Map> idsList = new ArrayList<Map>();
                idsList.add(salePositionInfoMap);

                addressGroupedMap.put(groupKey, idsList);
            }

        }
    }

    private String composeGroupKey(LinkedList groupElementList) {
        String groupKey = "";
        for (Iterator iterator = groupElementList.iterator(); iterator.hasNext();) {
            Object groupElement = iterator.next();
            groupKey = groupKey + (groupElement != null ? groupElement.toString() : "NULL");
            if (iterator.hasNext()) {
                groupKey = groupKey + "-";
            }
        }
        return groupKey;
    }

    private boolean isValidContract(ProductContract contract) {
        boolean isValid = true;
        //validate pay condition
        if (!hasValidPayCondition(contract.getPayConditionId())) {
            addInvalidInfo("failPayCondition", contractFailInfoMap(contract));
            isValid = false;
        }


        //validate product account
        Product product = readProduct(contract);
        if (product == null || product.getAccountId() == null) {
            addInvalidInfo("failProductAccount", contractFailInfoMap(contract));
            isValid = false;
        }

        //validate contract vatRate
        if (!vatHasValidVatRate(contract.getVatId(), new Integer(paramDTO.get("invoiceDate").toString()))) {
            addInvalidInfo("failVatRate", contractFailInfoMap(contract));
            isValid = false;
        }
        return isValid;
    }

    private boolean isValidSalePosition(SalePosition salePosition, Sale sale) {
        boolean isValid = true;
        //validate pay condition
        Customer customer = readCustomer(salePosition.getCustomerId());
        if (customer == null || !hasValidPayCondition(customer.getPayConditionId())) {
            addInvalidInfo("failPayCondition", salePositionFailInfoMap(salePosition));
            isValid = false;
        }

        //validate product account
        Product product = readProduct(salePosition.getProductId());
        if (product.getAccountId() == null) {
            addInvalidInfo("failProductAccount", salePositionFailInfoMap(salePosition));
            isValid = false;
        }

        //validate contract vatRate
        if (!vatHasValidVatRate(salePosition.getVatId(), new Integer(paramDTO.get("invoiceDate").toString()))) {
            addInvalidInfo("failVatRate", salePositionFailInfoMap(salePosition));
            isValid = false;
        }

        //validate currency in sale
        if (sale.getCurrencyId() == null) {
            addInvalidInfo("failCurrency", salePositionFailInfoMap(salePosition));
            isValid = false;
        }

        //validate net-gross calculation in sale
        if (sale.getNetGross() == null) {
            addInvalidInfo("failNetGross", salePositionFailInfoMap(salePosition));
            isValid = false;
        }

        return isValid;
    }

    private Invoice createInvoice(ProductContract contract, SessionContext ctx) {
        Invoice invoice = null;
        Integer invoiceDate = (Integer) paramDTO.get("invoiceDate");

        InvoiceCreateCmd invoiceCreateCmd = new InvoiceCreateCmd();
        invoiceCreateCmd.putParam("type", paramDTO.get("type"));
        invoiceCreateCmd.putParam("addressId", contract.getAddressId());
        invoiceCreateCmd.putParam("contactPersonId", contract.getContactPersonId());
        invoiceCreateCmd.putParam("payConditionId", contract.getPayConditionId());
        invoiceCreateCmd.putParam("templateId", paramDTO.get("templateId"));
        invoiceCreateCmd.putParam("currencyId", contract.getCurrencyId());
        invoiceCreateCmd.putParam("invoiceDate", invoiceDate);
        invoiceCreateCmd.putParam("companyId", paramDTO.get("companyId"));
        invoiceCreateCmd.putParam("sequenceRuleId", paramDTO.get("sequenceRuleId"));
        invoiceCreateCmd.putParam("netGross", contract.getNetGross());
        invoiceCreateCmd.putParam("sentAddressId", contract.getSentAddressId());
        invoiceCreateCmd.putParam("sentContactPersonId", contract.getSentContactPersonId());
        invoiceCreateCmd.putParam("additionalAddressId", contract.getAdditionalAddressId());
        invoiceCreateCmd.putParam("title", composeInvoiceTitle(invoiceDate, contract.getSalePositionId()));

        invoiceCreateCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = invoiceCreateCmd.getResultDTO();

        if (myResultDTO.isFailure()) {
            addFailResultErrorMessage(myResultDTO, contract, ctx);
        } else {
            Integer invoiceId = new Integer(myResultDTO.get("invoiceId").toString());
            InvoiceHome invoiceHome = (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
            try {
                invoice = invoiceHome.findByPrimaryKey(invoiceId);
            } catch (FinderException e) {
                log.debug("Not found invoice:" + invoiceId, e);
            }
        }

        return invoice;
    }

    private Invoice createInvoiceToSalePosition(SalePosition salePosition, SessionContext ctx) {
        Invoice invoice = null;

        Customer customer = readCustomer(salePosition.getCustomerId());
        Sale sale = readSale(salePosition.getSaleId());

        InvoiceCreateCmd invoiceCreateCmd = new InvoiceCreateCmd();
        invoiceCreateCmd.putParam("type", paramDTO.get("type"));
        invoiceCreateCmd.putParam("addressId", salePosition.getCustomerId());
        invoiceCreateCmd.putParam("contactPersonId", salePosition.getContactPersonId());
        invoiceCreateCmd.putParam("payConditionId", customer.getPayConditionId());
        invoiceCreateCmd.putParam("templateId", paramDTO.get("templateId"));
        invoiceCreateCmd.putParam("currencyId", sale.getCurrencyId());
        invoiceCreateCmd.putParam("invoiceDate", paramDTO.get("invoiceDate"));
        invoiceCreateCmd.putParam("serviceDate", paramDTO.get("invoiceDate"));
        invoiceCreateCmd.putParam("companyId", paramDTO.get("companyId"));
        invoiceCreateCmd.putParam("sequenceRuleId", paramDTO.get("sequenceRuleId"));
        invoiceCreateCmd.putParam("netGross", sale.getNetGross());
        invoiceCreateCmd.putParam("sentAddressId", sale.getSentAddressId());
        invoiceCreateCmd.putParam("sentContactPersonId", sale.getSentContactPersonId());
        invoiceCreateCmd.putParam("additionalAddressId", sale.getAdditionalAddressId());
        invoiceCreateCmd.putParam("title", sale.getTitle());

        invoiceCreateCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = invoiceCreateCmd.getResultDTO();

        if (myResultDTO.isFailure()) {
            addFailResultErrorMessage(myResultDTO, salePosition, ctx);
        } else {
            Integer invoiceId = new Integer(myResultDTO.get("invoiceId").toString());
            InvoiceHome invoiceHome = (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
            try {
                invoice = invoiceHome.findByPrimaryKey(invoiceId);
            } catch (FinderException e) {
                log.debug("Not found invoice:" + invoiceId, e);
            }
        }

        return invoice;
    }

    private Integer generateInvoiceDocument(Invoice invoice, Integer userId, SessionContext ctx) {
        Integer documentId = null;

        InvoiceDocumentCmd documentCmd = new InvoiceDocumentCmd();
        documentCmd.putParam("invoiceId", invoice.getInvoiceId());
        documentCmd.putParam("userId", userId);
        documentCmd.putParam("companyId", invoice.getCompanyId());
        documentCmd.putParam("userAddressId", paramDTO.get("userAddressId"));
        documentCmd.putParam("requestLocale", paramDTO.get("locale"));

        documentCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = documentCmd.getResultDTO();

        if (myResultDTO.isFailure()) {
            addFailResultErrorMessage(myResultDTO, ctx);
        } else {
            documentId = (Integer) myResultDTO.get("documentId");
        }
        return documentId;
    }

    private ProductContract readProductContract(Integer contractId) {
        ProductContract contract = null;
        ProductContractHome productContractHome = (ProductContractHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PRODUCTCONTRACT);
        try {
            contract = productContractHome.findByPrimaryKey(contractId);
        } catch (FinderException e) {
            log.debug("Not found contract:" + contractId, e);
        }
        return contract;
    }

    private PaymentStep readPaymentStep(Integer payStepId) {
        PaymentStep paymentStep = null;
        PaymentStepHome paymentStepHome = (PaymentStepHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PAYMENTSTEP);
        try {
            paymentStep = paymentStepHome.findByPrimaryKey(payStepId);
        } catch (FinderException e) {
            log.debug("Not found PaymentStep:" + payStepId, e);
        }
        return paymentStep;
    }

    private boolean createInvoicePositionForContract(Invoice invoice, ProductContract contract, DateTimeZone timeZone, SessionContext ctx) {
        Product product = readProduct(contract);
        if (product.getAccountId() == null) {
            resultDTO.addResultMessage("InvoicePosition.create.accountError", product.getProductName());
            addFailForward(ctx);
            return false;
        }

        //process contract pay method
        ProcessContractPayMethodUtil processPayMethodUtil = null;
        InvoicePosition invoicePosition = null;
        try {
            processPayMethodUtil = new ProcessContractPayMethodUtil(contract, timeZone, contractDateFilter);
            ContractInvoiceData contractInvoiceData = processPayMethodUtil.processContract();
            invoicePosition = createInvoicePosition(invoice, product, contract, contractInvoiceData.getPaymentStepInvoiced(), contractInvoiceData, ctx);

            if (SalesConstants.PayMethod.PartialPeriodic.getConstant() == contract.getPayMethod()) {
                //process only to partial periodic method
                while (invoicePosition != null && processPayMethodUtil.isPartialPeriodicNotInvoiced(contract)) {
                    contractInvoiceData = processPayMethodUtil.processNextPartialPeriodic(contract);
                    invoicePosition = createInvoicePosition(invoice, product, contract, contractInvoiceData.getPaymentStepInvoiced(), contractInvoiceData, ctx);
                }

            } else if (SalesConstants.PayMethod.PartialFixed.getConstant() == contract.getPayMethod()) {
                while (invoicePosition != null && processPayMethodUtil.isPartialFixedNotInvoiced()) {
                    contractInvoiceData = processPayMethodUtil.processNextPartialFixed(contract);
                    invoicePosition = createInvoicePosition(invoice, product, contract, contractInvoiceData.getPaymentStepInvoiced(), contractInvoiceData, ctx);
                }

            } else if (SalesConstants.PayMethod.Periodic.getConstant() == contract.getPayMethod()) {
                //process next periodic method
                while (invoicePosition != null && processPayMethodUtil.isPeriodicNotInvoiced(contract)) {
                    contractInvoiceData = processPayMethodUtil.processNextPeriodic(contract);
                    invoicePosition = createInvoicePosition(invoice, product, contract, contractInvoiceData.getPaymentStepInvoiced(), contractInvoiceData, ctx);
                }
            }
        } catch (InvalidContractException e) {
            log.debug("Invalid contract...." + e);
            resultDTO.addResultMessage("Contract.toInvoice.create.invalidContract", composeContractKey(contract));
            addFailForward(ctx);
            return false;
        } catch (PeriodicPaymentException e) {
            log.debug("Invalid contract.... Periodic dates is not exact");
            resultDTO.addResultMessage("Contract.toInvoice.create.invalidContract", composeContractKey(contract));
            if (e.hasErrorMessages()) {
                addErrorMessages(e.getErrorMessageKeys());
            }
            addFailForward(ctx);
            return false;
        }
        return invoicePosition != null;
    }

    private InvoicePosition createInvoicePosition(Invoice invoice, Product product, ProductContract contract, PaymentStep paymentStep, ContractInvoiceData contractInvoiceData, SessionContext ctx) {
        InvoicePosition invoicePosition = null;
        SalePosition salePosition = readSalePosition(contract.getSalePositionId());

        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.putParam("op", "create");
        invoicePositionCmd.putParam("accountId", product.getAccountId());
        invoicePositionCmd.putParam("companyId", contract.getCompanyId());
        invoicePositionCmd.putParam("contractId", contract.getContractId());
        invoicePositionCmd.putParam("invoiceId", invoice.getInvoiceId());
        invoicePositionCmd.putParam("number", getNextPositionNumber(invoice, ctx));
        invoicePositionCmd.putParam("productId", salePosition.getProductId());
        invoicePositionCmd.putParam("quantity", contractInvoiceData.getQuantity());
        invoicePositionCmd.putParam("unit", readProductUnitText(product));
        invoicePositionCmd.putParam("vatId", contract.getVatId());
        invoicePositionCmd.putParam("discount", contract.getDiscount());
        invoicePositionCmd.putParam("discountValue", contractInvoiceData.getDiscountValue());
        invoicePositionCmd.putParam("productName", product.getProductName());
        invoicePositionCmd.putParam("salePositionId", contract.getSalePositionId());

        if (contract.getNetGross() != null && FinanceConstants.NetGrossFLag.GROSS.equal(contract.getNetGross())) {
            invoicePositionCmd.putParam("unitPriceGross", contractInvoiceData.getUnitPrice());
        } else {
            //this is net unit price
            invoicePositionCmd.putParam("unitPrice", contractInvoiceData.getUnitPrice());
            /////invoicePositionCmd.putParam("totalPrice", totalPrice); //this is calculated in invoice position
        }

        //invoice position text
        invoicePositionCmd.putParam("text", composeInvoicePositionText(contract, paymentStep, contractInvoiceData, invoice, ctx));
        if (paymentStep != null) {
            invoicePositionCmd.putParam("payStepId", paymentStep.getPayStepId());
        }

        invoicePositionCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = invoicePositionCmd.getResultDTO();

        if (myResultDTO.isFailure()) {
            addFailResultErrorMessage(myResultDTO, contract, ctx);
            return null;
        } else {
            Integer positionId = new Integer(myResultDTO.get("positionId").toString());
            InvoicePositionHome invoicePositionHome = (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
            try {
                invoicePosition = invoicePositionHome.findByPrimaryKey(positionId);
            } catch (FinderException e) {
                log.debug("Not found invoice position:" + positionId, e);
            }
        }

        if (invoicePosition != null) {
            //update contract fields
            updateContractFields(contract, invoicePosition, contractInvoiceData);

            //update invoice service date
            updateInvoiceServiceDateByContract(invoice, contract, contractInvoiceData);
        }

        return invoicePosition;
    }

    private boolean createInvoicePositionForSalePosition(Invoice invoice, SalePosition salePosition, SessionContext ctx) {
        InvoicePosition invoicePosition = null;

        Product product = readProduct(salePosition.getProductId());
        Sale sale = readSale(salePosition.getSaleId());
        SalePositionInvoiceData salePositionInvoiceData = processSalePositionAsSingleMethod(salePosition, sale);

        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.putParam("op", "create");
        invoicePositionCmd.putParam("accountId", product.getAccountId());
        invoicePositionCmd.putParam("companyId", salePosition.getCompanyId());
        invoicePositionCmd.putParam("salePositionId", salePosition.getSalePositionId());
        invoicePositionCmd.putParam("invoiceId", invoice.getInvoiceId());
        invoicePositionCmd.putParam("number", getNextPositionNumber(invoice, ctx));
        invoicePositionCmd.putParam("productId", product.getProductId());
        invoicePositionCmd.putParam("quantity", salePosition.getQuantity());
        invoicePositionCmd.putParam("unit", readProductUnitText(product));
        invoicePositionCmd.putParam("vatId", salePosition.getVatId());
        invoicePositionCmd.putParam("discount", salePosition.getDiscount());
        invoicePositionCmd.putParam("discountValue", salePositionInvoiceData.getDiscountValue());
        invoicePositionCmd.putParam("productName", product.getProductName());

        invoicePositionCmd.putParam("unitPriceGross", salePositionInvoiceData.getUnitPriceGross());
        invoicePositionCmd.putParam("unitPrice", salePositionInvoiceData.getUnitPrice());

        //invoice position text
        invoicePositionCmd.putParam("text", composeInvoicePositionText(sale, salePosition, salePositionInvoiceData));

        invoicePositionCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = invoicePositionCmd.getResultDTO();

        if (myResultDTO.isFailure()) {
            addFailResultErrorMessage(myResultDTO, salePosition, ctx);
        } else {
            Integer positionId = new Integer(myResultDTO.get("positionId").toString());
            InvoicePositionHome invoicePositionHome = (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
            try {
                invoicePosition = invoicePositionHome.findByPrimaryKey(positionId);
            } catch (FinderException e) {
                log.debug("Not found invoice position:" + positionId, e);
            }
        }

        return invoicePosition != null;
    }

    private void updateContractFields(ProductContract contract, InvoicePosition invoicePosition, ContractInvoiceData contractInvoiceData) {

        if (SalesConstants.PayMethod.Single.getConstant() == contract.getPayMethod()) {
            contract.setOpenAmount(ProcessContractPayMethodUtil.calculateSingleOpenAmount(contract));

        } else if (SalesConstants.PayMethod.Periodic.getConstant() == contract.getPayMethod()) {
            contract.setInvoicedUntil(contractInvoiceData.getInvoicedUntil());
            Integer nextInvoiceDate = ProcessContractPayMethodUtil.calculatePeriodicNextInvoiceDate(contract.getInvoiceDelay(), contract.getPayStartDate(), contractInvoiceData.getInvoicedUntil());
            contract.setNextInvoiceDate(nextInvoiceDate);

        } else if (SalesConstants.PayMethod.PartialPeriodic.getConstant() == contract.getPayMethod()) {
            contract.setInvoicedUntil(contractInvoiceData.getInvoicedUntil());
            contract.setOpenAmount(ProcessContractPayMethodUtil.calculatePartialPeriodicOpenAmount(contract));

        } else if (SalesConstants.PayMethod.PartialFixed.getConstant() == contract.getPayMethod()) {
            contract.setOpenAmount(ProcessContractPayMethodUtil.calculatePartialFixedOpenAmount(contract));
        }

        //add contract version
        contract.setVersion(contract.getVersion() + 1);
    }

    private void updateInvoiceServiceDateByContract(Invoice invoice, ProductContract contract, ContractInvoiceData contractInvoiceData) {
        Integer serviceDate = null;

        if (SalesConstants.PayMethod.Periodic.getConstant() == contract.getPayMethod()
                || SalesConstants.PayMethod.PartialPeriodic.getConstant() == contract.getPayMethod()) {

            Integer invoicedUntil = contractInvoiceData.getInvoicedUntil();
            if (invoice.getServiceDate() != null) {
                if (invoicedUntil != null && invoicedUntil > invoice.getServiceDate()) {
                    serviceDate = invoicedUntil;
                }
            } else {
                serviceDate = invoicedUntil;
            }

        } else {
            serviceDate = invoice.getInvoiceDate();
        }

        if (serviceDate != null) {
            invoice.setServiceDate(serviceDate);
        }
    }

    private Product readProduct(Integer productId) {
        Product product = null;
        ProductHome productHome = (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
        try {
            product = productHome.findByPrimaryKey(productId);
        } catch (FinderException e) {
            log.debug("Not found product:" + productId);
        }
        return product;
    }

    private Product readProduct(ProductContract contract) {
        if (contract.getSalePositionId() != null) {
            SalePosition salePosition = readSalePosition(contract.getSalePositionId());
            return readProduct(salePosition.getProductId());
        }
        return null;
    }

    private String readProductUnitText(Product product) {
        String unitText = null;
        ProductUnitHome productUnitHome = (ProductUnitHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_PRODUCTUNIT);
        if (product.getUnitId() != null) {
            try {
                ProductUnit productUnit = productUnitHome.findByPrimaryKey(product.getUnitId());
                unitText = productUnit.getUnitName();
            } catch (FinderException e) {
                log.debug("Not found product Unit:" + product.getUnitId());
            }
        }
        return unitText;
    }

    private Integer getNextPositionNumber(Invoice invoice, SessionContext ctx) {
        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.putParam("invoiceId", invoice.getInvoiceId());
        invoicePositionCmd.putParam("companyId", invoice.getCompanyId());
        invoicePositionCmd.setOp("getLastPositionNumber");

        invoicePositionCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = invoicePositionCmd.getResultDTO();
        Integer result = (Integer) myResultDTO.get("getLastPositionNumber");

        if (result == null) {
            result = 0;
        }

        return result + 1;
    }

    private DateTimeZone getDateTimeZone(Integer userId) {
        DateTimeZone dateTimeZone = DateTimeZone.getDefault();
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        try {
            User user = userHome.findByPrimaryKey(userId);
            if (user.getTimeZone() != null) {
                dateTimeZone = DateTimeZone.forID(user.getTimeZone());
            }
        } catch (FinderException e) {
            log.debug("Not found user:" + userId, e);
        }
        return dateTimeZone;
    }

    private String composeInvoicePositionText(ProductContract contract, PaymentStep paymentStep, ContractInvoiceData contractInvoiceData, Invoice invoice, SessionContext ctx) {
        StringBuffer text = new StringBuffer();

        String isoLanguage = getDefaultIsoLanguage(contract);

        String datePattern = SystemPattern.getDatePattern(isoLanguage);
        String decimalPattern = SystemPattern.getDecimalPattern(isoLanguage);

        String strForCustomer = getTextResource("textForCustomer", isoLanguage);
        String strTimePeriod = getTextResource("textTimePeriod", isoLanguage);
        String strContractFrom = getTextResource("textContractFrom", isoLanguage);
        String strDiscount = getTextResource("textDiscount", isoLanguage);
        String strDiscountPeriod = getTextResource("textDiscountPeriod", isoLanguage);
        String strPartialPayment = getTextResource("textPartialPayment", isoLanguage);

        Locale locale = new Locale(isoLanguage);
        if (locale == null) {
            String localeIso = paramDTO.get("locale").toString();
            locale = new Locale(localeIso);
        }

        String currencyName = readCurrencyName(contract.getCurrencyId());

        boolean isCustomerDefined = false;
        if (contract.getSalePositionId() != null) {
            SalePosition salePosition = readSalePosition(contract.getSalePositionId());
            if (salePosition.getSalesFreeText() != null) {
                text.append(new String(salePosition.getSalesFreeText().getValue()));
            }

            //customer name
            if (!contract.getAddressId().equals(salePosition.getCustomerId())) {
                text.append("\n").append(composeCustomerNameText(strForCustomer, salePosition.getCustomerId()));
                isCustomerDefined = true;
            }
        } else {
            text.append(readProductText(invoice, contract, ctx));
        }

        //read customer name if is required
        if (contract.getSentAddressId() != null && !contract.getSentAddressId().equals(contract.getAddressId()) && !isCustomerDefined) {
            text.append("\n").append(composeCustomerNameText(strForCustomer, contract.getAddressId()));
        }

        boolean addDiscount = true;
        if (SalesConstants.PayMethod.Periodic.getConstant() == contract.getPayMethod()
                || SalesConstants.PayMethod.PartialPeriodic.getConstant() == contract.getPayMethod()) {

            BigDecimal priceMonths = contractInvoiceData.getContractPrice();
            String months = contractInvoiceData.getPeriodicMonths().toString();

            //time period
            Map<String, String> valuesMap = new HashMap<String, String>();
            valuesMap.put("{START_PERIOD}", DateUtils.parseDate(contractInvoiceData.getInvoicedFrom(), datePattern));
            valuesMap.put("{END_PERIOD}", DateUtils.parseDate(contractInvoiceData.getInvoicedUntil(), datePattern));
            text.append("\n").append(replaceKeyVariables(strTimePeriod, valuesMap));

            //contract from
            if (contract.getOrderDate() != null) {
                valuesMap = new HashMap<String, String>();
                valuesMap.put("{ORDER_DATE}", DateUtils.parseDate(contract.getOrderDate(), datePattern));
                valuesMap.put("{MONTHS}", months);
                valuesMap.put("{PRICE_MONTHS}", FormatUtils.formatDecimal(priceMonths, locale, decimalPattern) + " " + currencyName);
                text.append("\n").append(replaceKeyVariables(strContractFrom, valuesMap));
            } else {
                text.append("\n").append(months).append(" x ").append(FormatUtils.formatDecimal(priceMonths, locale, decimalPattern) + " " + currencyName);
            }

            //discount
            addDiscount = false;
            if (contract.getDiscount() != null && contract.getDiscount().doubleValue() > 0) {
                valuesMap = new HashMap<String, String>();
                valuesMap.put("{DISCOUNT}", FormatUtils.formatDecimal(contract.getDiscount(), locale, decimalPattern));
                valuesMap.put("{MONTHS}", months);
                valuesMap.put("{DISC_VALUE}", FormatUtils.formatDecimal(contractInvoiceData.getUnitPriceDiscounted(), locale, decimalPattern) + " " + currencyName);
                text.append("\n").append(replaceKeyVariables(strDiscountPeriod, valuesMap));
            }

        } else if (SalesConstants.PayMethod.PartialPeriodic.getConstant() == contract.getPayMethod()) {
            Map<String, String> valuesMap = new HashMap<String, String>();
            valuesMap.put("{PART_AMOUNT}", FormatUtils.formatDecimal(contractInvoiceData.getInvoicePrice(), locale, decimalPattern) + " " + currencyName);
            valuesMap.put("{TOTAL_PRICE}", FormatUtils.formatDecimal(contract.getPrice(), locale, decimalPattern) + " " + currencyName);
            text.append("\n").append(replaceKeyVariables(strPartialPayment, valuesMap));

        } else if (SalesConstants.PayMethod.PartialFixed.getConstant() == contract.getPayMethod()) {
            String partialAmount = FormatUtils.formatDecimal(paymentStep.getPayAmount(), locale, decimalPattern) + " "
                    + (SalesConstants.AmounType.PERCENTAGE.getConstant() == contract.getAmounType() ? "%" : currencyName);

            Map<String, String> valuesMap = new HashMap<String, String>();
            valuesMap.put("{PART_AMOUNT}", partialAmount);
            valuesMap.put("{TOTAL_PRICE}", FormatUtils.formatDecimal(contract.getPrice(), locale, decimalPattern) + " " + currencyName);
            text.append("\n").append(replaceKeyVariables(strPartialPayment, valuesMap));
        }

        //discount
        if (addDiscount && contract.getDiscount() != null && contract.getDiscount().doubleValue() > 0) {
            Map<String, String> valuesMap = new HashMap<String, String>();
            valuesMap.put("{DISCOUNT}", FormatUtils.formatDecimal(contract.getDiscount(), locale, decimalPattern));
            valuesMap.put("{DISC_VALUE}", FormatUtils.formatDecimal(contractInvoiceData.getDiscountValue(), locale, decimalPattern) + " " + currencyName);
            text.append("\n").append(replaceKeyVariables(strDiscount, valuesMap));
        }

        return text.toString();
    }

    private String composeInvoicePositionText(Sale sale, SalePosition salePosition, SalePositionInvoiceData salePositionInvoiceData) {
        StringBuffer text = new StringBuffer();

        String isoLanguage = getDefaultIsoLanguage(salePosition);
        Locale locale = new Locale(isoLanguage);
        String currencyName = readCurrencyName(readSale(salePosition.getSaleId()).getCurrencyId());

        String decimalPattern = SystemPattern.getDecimalPattern(isoLanguage);
        String strDiscount = getTextResource("textDiscount", isoLanguage);
        String strForCustomer = getTextResource("textForCustomer", isoLanguage);

        if (salePosition.getSalesFreeText() != null) {
            text.append(new String(salePosition.getSalesFreeText().getValue()));
        }

        //read customer name if is required
        if (sale.getSentAddressId() != null && !sale.getSentAddressId().equals(sale.getCustomerId())) {
            text.append("\n").append(composeCustomerNameText(strForCustomer, sale.getCustomerId()));
        }

        //discount
        if (salePosition.getDiscount() != null && salePosition.getDiscount().doubleValue() > 0) {
            Map<String, String> valuesMap = new HashMap<String, String>();
            valuesMap.put("{DISCOUNT}", FormatUtils.formatDecimal(salePosition.getDiscount(), locale, decimalPattern));
            valuesMap.put("{DISC_VALUE}", FormatUtils.formatDecimal(salePositionInvoiceData.getDiscountValue(), locale, decimalPattern) + " " + currencyName);
            text.append("\n").append(replaceKeyVariables(strDiscount, valuesMap));
        }

        return text.toString();
    }

    private String composeCustomerNameText(String strForCustomer, Integer addressId) {
        String nameText = "";
        Map<String, String> valuesMap = new HashMap<String, String>();
        valuesMap.put("{NAME}", readAddressName(addressId));

        nameText = replaceKeyVariables(strForCustomer, valuesMap);
        return nameText;
    }

    private String replaceKeyVariables(String sourceText, Map<String, String> valuesMap) {
        String result = sourceText;
        for (String key : valuesMap.keySet()) {
            result = result.replace(key, valuesMap.get(key));
        }
        return result;
    }

    private SalePosition readSalePosition(Integer salePositionId) {
        SalePosition salePosition = null;
        SalePositionHome salePositionHome = (SalePositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);

        if (salePositionId != null) {
            try {
                salePosition = salePositionHome.findByPrimaryKey(salePositionId);
            } catch (FinderException e) {
                log.debug("Not found SalePosition:" + salePositionId);
            }
        }
        return salePosition;
    }

    private Sale readSale(Integer saleId) {
        Sale sale = null;
        SaleHome saleHome = (SaleHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALE);

        if (saleId != null) {
            try {
                sale = saleHome.findByPrimaryKey(saleId);
            } catch (FinderException e) {
                log.debug("Not found Sale:" + saleId);
            }
        }
        return sale;
    }

    private Customer readCustomer(Integer customerId) {
        Customer customer = null;
        CustomerHome customerHome = (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);
        try {
            customer = customerHome.findByPrimaryKey(customerId);
        } catch (FinderException e) {
            log.debug("Not found Customer:" + customerId);
        }
        return customer;
    }

    private String readAddressName(Integer addressId) {
        String addressName = null;
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        try {
            Address address = addressHome.findByPrimaryKey(addressId);
            addressName = address.getName();
        } catch (FinderException e) {
            log.debug("Not found Address:" + addressId, e);
        }
        return addressName;
    }

    private Address readAddress(Integer addressId) {
        Address address = null;
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        try {
            address = addressHome.findByPrimaryKey(addressId);
        } catch (FinderException e) {
            log.debug("Not found Address:" + addressId, e);
        }
        return address;
    }

    private String readCurrencyName(Integer currencyId) {
        String currencyName = "";
        CurrencyHome currencyHome = (CurrencyHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CURRENCY);
        try {
            Currency currency = currencyHome.findByPrimaryKey(currencyId);
            currencyName = currency.getCurrencyName();
        } catch (FinderException e) {
            log.debug("Not found Currency:" + currencyId, e);
        }
        return currencyName;
    }

    private String composeContractKey(ProductContract contract) {
        String key;
        if (contract.getContractNumber() != null) {
            key = contract.getContractNumber();
        } else {
            String contactName = readAddressName(contract.getAddressId());
            String productName = null;

            Product product = readProduct(contract);
            if (product != null) {
                productName = product.getProductName();
            }
            key = contactName + "/" + productName;
        }
        return key;
    }

    private boolean vatHasValidVatRate(Integer vatId, Integer invoiceDate) {
        VatRateHome vatRateHome = (VatRateHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_VATRATE);
        if (vatId != null) {
            try {
                Collection varRates = vatRateHome.findByVatId(vatId);
                for (Object object : varRates) {
                    VatRate vatRate = (VatRate) object;
                    if (vatRate.getValidFrom() <= invoiceDate) {
                        return (vatRate.getVatRate() != null);
                    }
                }
            } catch (FinderException e) {
                log.debug("Not found vatRates...", e);
            }
        }
        return false;
    }

    private boolean hasValidPayCondition(Integer payConditionId) {
        boolean validPayCondition = false;
        if (payConditionId != null) {
            PayConditionHome payConditionHome = (PayConditionHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_PAYCONDITION);
            try {
                PayCondition payCondition = payConditionHome.findByPrimaryKey(payConditionId);
                if (payCondition.getPayDays() != null) {
                    validPayCondition = true;
                }
            } catch (FinderException e) {
                log.debug("Not found pay contiton:" + payConditionId);
            }
        }
        return validPayCondition;
    }

    private String readProductText(Invoice invoice, ProductContract contract, SessionContext ctx) {
        SalePosition salePosition = readSalePosition(contract.getSalePositionId());

        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.putParam("productId", salePosition.getProductId());
        invoicePositionCmd.putParam("invoiceId", invoice.getInvoiceId());
        invoicePositionCmd.putParam("companyId", contract.getCompanyId());
        invoicePositionCmd.setOp("getInvoicePositionText");

        invoicePositionCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = invoicePositionCmd.getResultDTO();
        String text = (String) myResultDTO.get("getInvoicePositionText");
        return (text != null ? text : "");
    }

    private String getDefaultIsoLanguage(ProductContract contract) {
        return getDefaultIsoLanguage(contract.getAddressId(), contract.getCompanyId());
    }

    private String getDefaultIsoLanguage(SalePosition salePosition) {
        return getDefaultIsoLanguage(salePosition.getCustomerId(), salePosition.getCompanyId());
    }

    private String getDefaultIsoLanguage(Integer addressId, Integer companyId) {
        Address address = readAddress(addressId);
        Address companyAddress = readAddress(companyId);
        Address userAddress = readAddress(new Integer(paramDTO.get("userAddressId").toString()));

        String defaultIsoLang = InvoiceDocumentValues.getDefaultIsoLang(address, null, companyAddress, userAddress);
        if (defaultIsoLang == null) {
            defaultIsoLang = paramDTO.get("locale").toString();
        }

        return defaultIsoLang;
    }

    private String getTextResource(String key, String isoLanguage) {
        String defaultIsoLang = "en";
        String text = null;
        Map textResourcesMap = (Map) paramDTO.get("textResourcesMap");

        if (textResourcesMap.containsKey(key)) {
            Map textIsoMap = (Map) textResourcesMap.get(key);
            if (textIsoMap.containsKey(isoLanguage)) {
                text = (String) textIsoMap.get(isoLanguage);
            } else {
                text = (String) textIsoMap.get(defaultIsoLang);
            }
        }
        return (text != null ? text : "");
    }


    private void addFailResultErrorMessage(ResultDTO myResultDTO, SessionContext ctx) {

        if (myResultDTO != null && myResultDTO.hasResultMessage()) {
            for (Iterator iterator = myResultDTO.getResultMessages(); iterator.hasNext();) {
                ResultMessage resultMessage = (ResultMessage) iterator.next();
                resultDTO.addResultMessage(resultMessage);
            }
        }
        //set rollback
        addFailForward(ctx);
    }

    private void addFailResultErrorMessage(ResultDTO myResultDTO, ProductContract contract, SessionContext ctx) {
        if (contract != null) {
            resultDTO.addResultMessage("Contract.toInvoice.createError", composeContractKey(contract));
        }
        addFailResultErrorMessage(myResultDTO, ctx);
    }

    private void addFailResultErrorMessage(ResultDTO myResultDTO, SalePosition salePosition, SessionContext ctx) {
        if (salePosition != null) {
            resultDTO.addResultMessage("SalePosition.toInvoice.createError", composeSalePositionKey(salePosition));
        }
        addFailResultErrorMessage(myResultDTO, ctx);
    }

    private void addErrorMessages(List<String> errorMessageKeys) {
        for (String messageKey : errorMessageKeys) {
            resultDTO.addResultMessage(messageKey);
        }
    }

    private void addFailForward(SessionContext ctx) {
        resultDTO.setResultAsFailure();
        resultDTO.setForward("Fail");
        //set rollback
        ctx.setRollbackOnly();
    }

    private boolean isInternalUser(Integer userId) {
        boolean isInternal = false;
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        try {
            User user = userHome.findByPrimaryKey(userId);
            isInternal = AdminConstants.INTERNAL_USER.equals(user.getType().toString());
        } catch (FinderException e) {
            log.debug("Not found user ..." + userId);
            return false;
        }
        return isInternal;
    }

    private Map contractFailInfoMap(ProductContract contract) {
        Map map = new HashMap();
        map.put("isContract", "true");
        map.put("labelKey", composeContractKey(contract));
        map.put("contractId", contract.getContractId());

        if (contract.getSalePositionId() != null) {
            map.put("salePositionId", contract.getSalePositionId());

            SalePosition salePosition = readSalePosition(contract.getSalePositionId());
            Product product = readProduct(salePosition.getProductId());
            map.put("productName", product.getProductName());

            if (salePosition.getSaleId() != null) {
                map.put("saleId", salePosition.getSaleId());
            } else {

                map.put("productId", salePosition.getProductId());
                map.put("customerName", readAddressName(salePosition.getCustomerId()));
            }
        }
        return map;
    }

    private Map salePositionFailInfoMap(SalePosition salePosition) {
        Map map = new HashMap();
        map.put("isContract", "false");
        map.put("labelKey", composeSalePositionKey(salePosition));
        map.put("salePositionId", salePosition.getSalePositionId());
        map.put("saleId", salePosition.getSaleId());

        Product product = readProduct(salePosition.getProductId());
        map.put("productName", product.getProductName());

        return map;
    }

    private String composeSalePositionKey(SalePosition salePosition) {
        String contactName = readAddressName(salePosition.getCustomerId());
        String productName = null;
        Product product = readProduct(salePosition.getProductId());
        if (product != null) {
            productName = product.getProductName();
        }
        return contactName + "/" + productName;
    }

    private BigDecimal applySalePositionDiscount(BigDecimal amount, BigDecimal discountPercent) {
        BigDecimal discount = BigDecimalUtils.getPercentage(amount, discountPercent, 4);
        return BigDecimalUtils.subtract(amount, discount, 4);
    }

    private BigDecimal invoicePositionUnitPriceFromSalePosition(BigDecimal unitPrice, BigDecimal discount) {
        if (unitPrice != null && discount != null) {
            unitPrice = applySalePositionDiscount(unitPrice, discount);
        }
        return unitPrice;
    }

    private SalePositionInvoiceData processSalePositionAsSingleMethod(SalePosition salePosition, Sale sale) {
        SalePositionInvoiceData salePositionInvoiceData = new SalePositionInvoiceData();

        BigDecimal realUnitPrice;
        if (sale.getNetGross() != null && FinanceConstants.NetGrossFLag.GROSS.equal(sale.getNetGross())) {
            realUnitPrice = salePosition.getUnitPriceGross();
        } else {
            //this is net unit price
            realUnitPrice = salePosition.getUnitPrice();
        }

        salePositionInvoiceData.setContractPrice(realUnitPrice);
        salePositionInvoiceData.setUnitPrice(invoicePositionUnitPriceFromSalePosition(salePosition.getUnitPrice(), salePosition.getDiscount()));
        salePositionInvoiceData.setUnitPriceGross(invoicePositionUnitPriceFromSalePosition(salePosition.getUnitPriceGross(), salePosition.getDiscount()));

        BigDecimal discountValue = null;
        if (salePosition.getDiscount() != null) {
            BigDecimal unitPriceDiscounted = BigDecimalUtils.getPercentage(realUnitPrice, salePosition.getDiscount(), 4);
            discountValue = BigDecimalUtils.multiply(unitPriceDiscounted, salePosition.getQuantity());
            salePositionInvoiceData.setUnitPriceDiscounted(unitPriceDiscounted);
        }

        BigDecimal invoicePrice = BigDecimalUtils.multiply(realUnitPrice, salePosition.getQuantity());
        salePositionInvoiceData.setQuantity(salePosition.getQuantity());
        salePositionInvoiceData.setInvoicePrice(invoicePrice);
        salePositionInvoiceData.setDiscountValue(discountValue);

        return salePositionInvoiceData;
    }

    private void addInvalidInfo(String key, Map infoMap) {
        if (failInfoMap.containsKey(key)) {
            failInfoMap.get(key).add(infoMap);
        } else {
            List failList = new ArrayList();
            failList.add(infoMap);
            failInfoMap.put(key, failList);
        }
    }

    private String composeInvoiceTitle(Integer invoiceDate, Integer salePositionId) {
        String title = "";

        Sale sale = invoiceGenerationFromSale(salePositionId);

        if (sale != null) {
            //generation is from sale
            title = sale.getTitle();
        } else {
            //generation is from product contract
            if (paramDTO.get("invoiceTitleMessage") != null) {
                title = paramDTO.get("invoiceTitleMessage").toString();
            }

            if (invoiceDate != null) {
                int[] ymd = DateUtils.getYearMonthDay(invoiceDate);
                int year = ymd[0];
                int month = ymd[1];

                title = title + " " + (month < 10 ? "0" : "") + month + "/" + year;
            }
        }

        return title;
    }

    private Sale invoiceGenerationFromSale(Integer salePositionId) {
        Sale sale = null;

        SalePosition salePosition = readSalePosition(salePositionId);
        if (salePosition != null) {
            sale = readSale(salePosition.getSaleId());
        }
        return sale;
    }


    public boolean isStateful() {
        return false;
    }

}
