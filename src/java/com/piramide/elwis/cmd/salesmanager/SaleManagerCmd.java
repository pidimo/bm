package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.cmd.productmanager.ProductReadLightCmd;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.domain.financemanager.Invoice;
import com.piramide.elwis.domain.financemanager.InvoicePosition;
import com.piramide.elwis.domain.financemanager.InvoicePositionHome;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.domain.salesmanager.*;
import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import com.piramide.elwis.dto.contactmanager.SupplierDTO;
import com.piramide.elwis.dto.productmanager.ProductDTO;
import com.piramide.elwis.dto.salesmanager.SaleDTO;
import com.piramide.elwis.dto.salesmanager.SalePositionDTO;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public abstract class SaleManagerCmd extends EJBCommand {
    private static Log log = LogFactory.getLog(SaleCmd.class);

    protected Customer getCustomer(Integer addressId) {
        Customer customer = null;
        try {
            customer = searchCustomer(addressId);
        } catch (FinderException e) {
            Address address = getAddress(addressId);
            if (null != address && !CodeUtil.isCustomer(address.getCode())) {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.put(CustomerDTO.KEY_CUSTOMERID, address.getAddressId());
                customerDTO.put("companyId", address.getCompanyId());
                customer = (Customer) EJBFactory.i.createEJB(customerDTO);
                String newCustomerNumber = InvoiceUtil.i.getCustomerNumber(customer.getCompanyId());
                if (null != newCustomerNumber) {
                    customer.setNumber(newCustomerNumber);
                }
                address.setCode((byte) (address.getCode() + CodeUtil.customer));
                address.setVersion(address.getVersion() + 1);
            }
        }

        return customer;
    }

    protected Supplier getSupplier(Integer addressId) {
        Supplier supplier = null;
        try {
            supplier = searchSupplier(addressId);
        } catch (FinderException e) {
            Address address = getAddress(addressId);
            if (null != address && !CodeUtil.isSupplier(address.getCode())) {
                SupplierDTO supplierDTO = new SupplierDTO();
                supplierDTO.put(SupplierDTO.KEY_SUPPLIERID, address.getAddressId());
                supplierDTO.put("companyId", paramDTO.get("companyId"));
                supplier = (Supplier) EJBFactory.i.createEJB(supplierDTO);
                address.setCode((byte) (address.getCode() + CodeUtil.supplier));
                address.setVersion(address.getVersion() + 1);
            }
        }

        return supplier;
    }

    /**
     * @param text
     * @param companyId
     * @return
     * @deprecated
     */
    protected Integer createSaleFreeText(String text, Integer companyId) {
        if (null == text || "".equals(text.trim())) {
            return null;
        }

        SalesFreeTextHome salesFreeTextHome =
                (SalesFreeTextHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_FREETEXT);
        SalesFreeText freeText;
        try {
            freeText = salesFreeTextHome.create(text.getBytes(), companyId, FreeTextTypes.FREETEXT_SALES);
            return freeText.getFreeTextId();

        } catch (CreateException e) {
            log.error("-> Create Notes field Fail", e);
        }
        return null;
    }

    protected Integer createSaleFreeText(String text, Integer companyId, int saleFreeTextType) {
        if (null == text || "".equals(text.trim())) {
            return null;
        }

        SalesFreeTextHome salesFreeTextHome =
                (SalesFreeTextHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_FREETEXT);
        SalesFreeText freeText;
        try {
            freeText = salesFreeTextHome.create(text.getBytes(), companyId, saleFreeTextType);
            return freeText.getFreeTextId();

        } catch (CreateException e) {
            log.error("-> Create Notes field Fail", e);
        }
        return null;
    }

    private Address getAddress(Integer addressId) {
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        try {
            return addressHome.findByPrimaryKey(addressId);
        } catch (FinderException e) {
            log.error("-> Read address addressId=" + addressId + " FAIL");
        }

        return null;
    }

    private Customer searchCustomer(Integer addressId) throws FinderException {
        CustomerHome customerHome =
                (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);
        return customerHome.findByPrimaryKey(addressId);
    }

    private Supplier searchSupplier(Integer addressId) throws FinderException {
        SupplierHome supplierHome =
                (SupplierHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_SUPPLIER);

        return supplierHome.findByPrimaryKey(addressId);
    }

    protected InvoicePosition getPositiveInvoicePosition(PaymentStep paymentStep) {
        InvoicePositionHome invoicePositionHome =
                (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);

        try {
            return invoicePositionHome.findByPayStepId(paymentStep.getPayStepId(),
                    paymentStep.getCompanyId());
        } catch (FinderException e) {
            log.debug("-> Execute InvoicePositionHome.findByPayStepId[paySteptId=" +
                    paymentStep.getPayStepId() +
                    ", companyId=" + paymentStep.getCompanyId() + "] FAIL");
            return null;
        }
    }

    protected List getInvoicePositions(PaymentStep paymentStep, ProductContract productContract) {
        InvoicePositionHome invoicePositionHome =
                (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
        try {
            return (List) invoicePositionHome.findByPaymentStepAndContract(productContract.getContractId(),
                    paymentStep.getPayStepId(),
                    productContract.getCompanyId());
        } catch (FinderException e) {
            return new ArrayList();
        }
    }

    protected List<InvoicePosition> getAllInvoicePositions(ProductContract productContract) {
        List partialResult = new ArrayList();

        if (isPartialFixed(productContract)) {
            List paymentSteps = getPaymentSteps(productContract.getContractId(),
                    productContract.getCompanyId());
            for (int i = 0; i < paymentSteps.size(); i++) {
                PaymentStep paymentStep = (PaymentStep) paymentSteps.get(i);
                List invoicePositions = getInvoicePositions(paymentStep, productContract);
                partialResult.addAll(invoicePositions);
            }
        } else {
            InvoicePositionHome invoicePositionHome =
                    (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
            try {
                Collection elements = invoicePositionHome.findByContractId(productContract.getContractId(),
                        productContract.getCompanyId());
                partialResult.addAll(elements);
            } catch (FinderException e) {
                //
            }
        }

        List<InvoicePosition> invoicePositions = new ArrayList<InvoicePosition>();
        for (int i = 0; i < partialResult.size(); i++) {
            invoicePositions.add((InvoicePosition) partialResult.get(i));
        }

        return invoicePositions;
    }

    protected List<InvoicePosition> getPositiveInvoicePositions(ProductContract productContract) {
        List partialResult = new ArrayList();
        if (isPartialFixed(productContract)) {
            List paymentSteps = getPaymentSteps(productContract.getContractId(),
                    productContract.getCompanyId());
            for (int i = 0; i < paymentSteps.size(); i++) {
                PaymentStep paymentStep = (PaymentStep) paymentSteps.get(i);
                InvoicePosition invoicePosition = getPositiveInvoicePosition(paymentStep);
                if (null != invoicePosition) {
                    partialResult.add(invoicePosition);
                }
            }
        } else {
            InvoicePositionHome invoicePositionHome =
                    (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
            try {
                Collection elements = invoicePositionHome.findByContractIdPositive(
                        productContract.getContractId(),
                        productContract.getCompanyId());
                partialResult.addAll(elements);
            } catch (FinderException e) {
                //
            }
        }

        List<InvoicePosition> invoicePositions = new ArrayList<InvoicePosition>();
        for (int i = 0; i < partialResult.size(); i++) {
            invoicePositions.add((InvoicePosition) partialResult.get(i));
        }

        return invoicePositions;
    }


    /*protected List<InvoicePosition> getInvoicePositions(ProductContract productContract) {
        List<InvoicePosition> invoicePositions = new ArrayList<InvoicePosition>();

        if (SalesConstants.PayMethod.PartialFixed.equal(productContract.getPayMethod())) {
            Collection paySteps = productContract.getPaymentSteps();
            for (Object object : paySteps) {
                PaymentStep paymentStep = (PaymentStep) object;
                InvoicePosition invoicePosition = getPositiveInvoicePosition(paymentStep);
                if (null != invoicePosition) {
                    invoicePositions.add(invoicePosition);
                }
            }

            return invoicePositions;
        }

        InvoicePositionHome invoicePositionHome =
                (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
        try {
            Collection elements = invoicePositionHome.findByContractId(productContract.getContractId(),
                    productContract.getCompanyId());

            for (Object object : elements) {
                InvoicePosition invoicePosition = (InvoicePosition) object;
                invoicePositions.add(invoicePosition);
            }
        } catch (FinderException e) {
            log.debug("-> Execute InvoicePositionHome.findByContractId[contractId=" +
                    productContract.getContractId() + ", companyId=" + productContract.getCompanyId() + "] FAIL");
        }

        return invoicePositions;
    }*/

    protected boolean isProductContractRelatedWithInvoicePositions(ProductContract productContract) {
        return !getPositiveInvoicePositions(productContract).isEmpty();
    }

    protected boolean isProductContractRelatedWithInvoicePositions(List productContracts) {
        for (int i = 0; i < productContracts.size(); i++) {
            ProductContract productContract = (ProductContract) productContracts.get(i);
            if (isProductContractRelatedWithInvoicePositions(productContract)) {
                return true;
            }
        }

        return false;
    }

    protected void canChangePayMethod(Integer salePositionId) {
        boolean canDeleteSalePosition = canDeleteSaleposition(salePositionId);
        if (canDeleteSalePosition) {
            resultDTO.put("canChangePayMethod", true);
        } else {
            resultDTO.put("canChangePayMethod", false);
        }
    }

    protected boolean canDeleteSaleposition(SalePosition salePosition) {
        boolean isSalePositionRelatedWithInvoicePositions = isSalePositionRelatedWithInvoicePositions(salePosition);

        if (isSalePositionRelatedWithInvoicePositions) {
            return false;
        }

        Collection productcontracts = salePosition.getProductContracts();
        for (Object object : productcontracts) {
            ProductContract productContract = (ProductContract) object;
            if (isProductContractRelatedWithInvoicePositions(productContract)) {
                return false;
            }
        }
        return true;
    }

    protected boolean canDeleteSaleposition(Integer salPositionId) {
        SalePositionHome salePositionHome =
                (SalePositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);
        SalePosition salePosition = null;
        try {
            salePosition = salePositionHome.findByPrimaryKey(salPositionId);
        } catch (FinderException e) {
            log.debug("-> Could not find SalePosition=" + salPositionId + " Object");
        }

        if (null != salePosition) {
            return canDeleteSaleposition(salePosition);
        }

        return false;
    }

    protected boolean isSaleRelatedWithSalePositions(Sale sale) {
        SalePositionHome salePositionHome =
                (SalePositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);
        try {
            Collection salePositions = salePositionHome.findBySaleId(sale.getSaleId(), sale.getCompanyId());

            return null != salePositions && !salePositions.isEmpty();
        } catch (FinderException e) {
            return false;
        }
    }

    protected boolean isSalePositionRelatedWithInvoicePositions(List salePositions) {
        InvoicePositionHome invoicePositionHome =
                (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);

        for (int i = 0; i < salePositions.size(); i++) {
            SalePosition salePosition = (SalePosition) salePositions.get(i);
            try {
                InvoicePosition invoicePosition = invoicePositionHome.findBySalePositionId(
                        salePosition.getSalePositionId(),
                        salePosition.getCompanyId());

                if (null != invoicePosition) {
                    return true;
                }
            } catch (FinderException e) {
                //
            }
        }
        return false;
    }

    protected boolean isSalePositionRelatedWithInvoicePositions(SalePosition salePosition) {
        InvoicePositionHome invoicePositionHome =
                (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
        try {
            InvoicePosition invoicePosition = invoicePositionHome.findBySalePositionId(
                    salePosition.getSalePositionId(),
                    salePosition.getCompanyId());

            if (null != invoicePosition) {
                return true;
            }
        } catch (FinderException e) {
            //
        }
        return false;
    }

    protected boolean canDeleteSale(Sale sale) {
        SalePositionHome salePositionHome =
                (SalePositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);
        try {
            Collection salePositions = salePositionHome.findBySaleId(sale.getSaleId(), sale.getCompanyId());

            for (Object object : salePositions) {
                SalePosition salePosition = (SalePosition) object;
                if (!canDeleteSaleposition(salePosition)) {
                    return false;
                }
            }
        } catch (FinderException e) {
            log.debug("-> Find SalePositions [saleId=" +
                    sale.getSaleId() + ", companyId=" + sale.getCompanyId() + "] FAIL");
        }
        return true;
    }

    protected SalePositionDTO getSalePositionDTO(Integer salePositionId, SessionContext ctx) {
        SalePositionHome salePositionHome =
                (SalePositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);

        SalePositionDTO salePositionDTO = null;
        try {
            SalePosition salePosition = salePositionHome.findByPrimaryKey(salePositionId);
            salePositionDTO = new SalePositionDTO();
            DTOFactory.i.copyToDTO(salePosition, salePositionDTO);

            //use in ui to show customer name
            salePositionDTO.put("customerName", readAddressName(salePosition.getCustomerId(), ctx));
            //use in ui to show product name
            salePositionDTO.put("productName", readProductName(salePosition.getProductId(), ctx));

        } catch (FinderException e) {
            log.debug("-> Read SalePosition [salePositionId=" + salePositionId + "] FAIL");
        }

        return salePositionDTO;
    }

    protected SaleDTO getSaleDTO(Integer saleId) {
        SaleHome saleHome =
                (SaleHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALE);
        SaleDTO saleDTO = null;
        try {
            Sale sale = saleHome.findByPrimaryKey(saleId);
            saleDTO = new SaleDTO();
            DTOFactory.i.copyToDTO(sale, saleDTO);

        } catch (FinderException e) {
            log.debug("-> Read Sale [saleId=" + saleId + "] FAIL");
        }
        return saleDTO;
    }

    protected Sale getSale(Integer saleId) {
        SaleHome saleHome =
                (SaleHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALE);
        Sale sale = null;
        try {
            sale = saleHome.findByPrimaryKey(saleId);
        } catch (FinderException e) {
            log.debug("-> Read Sale [saleId=" + saleId + "] FAIL");
        }
        return sale;
    }

    protected List getSalePositions(Integer saleId, Integer companyId) {
        SalePositionHome salePositionHome =
                (SalePositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);
        try {
            return (List) salePositionHome.findBySaleId(saleId, companyId);
        } catch (FinderException e) {
            return new ArrayList();
        }
    }

    protected List getSingleProductContracts(Integer salePosititionId, Integer companyId) {
        ProductContractHome productContractHome =
                (ProductContractHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PRODUCTCONTRACT);
        try {
            return (List) productContractHome.findSingleContracts(salePosititionId, companyId);
        } catch (FinderException e) {
            return new ArrayList();
        }
    }

    protected List getProductContracts(Integer salePositionId, Integer companyId) {
        ProductContractHome productContractHome =
                (ProductContractHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PRODUCTCONTRACT);

        try {
            return (List) productContractHome.findBySalePositionId(salePositionId, companyId);
        } catch (FinderException e) {
            return new ArrayList();
        }
    }

    protected String readAddressName(Integer addressId, SessionContext ctx) {
        LightlyAddressCmd addressCmd = new LightlyAddressCmd();
        addressCmd.putParam("addressId", addressId);

        addressCmd.executeInStateless(ctx);

        ResultDTO customResultDTO = addressCmd.getResultDTO();
        return (String) customResultDTO.get("addressName");
    }

    protected String readProductName(Integer productId, SessionContext ctx) {
        ProductReadLightCmd productReadLightCmd = new ProductReadLightCmd();
        productReadLightCmd.putParam("productId", productId);
        productReadLightCmd.executeInStateless(ctx);
        return (String) productReadLightCmd.getResultDTO().get("productName");
    }

    protected ProductDTO getProduct(Integer productId) {
        ProductHome productHome = (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
        try {
            Product product = productHome.findByPrimaryKey(productId);
            ProductDTO productDTO = new ProductDTO();
            DTOFactory.i.copyToDTO(product, productDTO);
            return productDTO;
        } catch (FinderException e) {
            log.debug("-> Read Product with productId=" + productId + " FAIL");
        }

        return null;
    }

    protected String readSaleFreeText(Integer freetextId) {
        SalesFreeTextHome salesFreeTextHome =
                (SalesFreeTextHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_FREETEXT);

        try {
            SalesFreeText salesFreeText = salesFreeTextHome.findByPrimaryKey(freetextId);
            return new String(salesFreeText.getValue());
        } catch (FinderException e) {
            log.debug("-> Read SalesFreeText freeTextId=" + freetextId + " FAIL");
        }
        return "";
    }

    protected String readSaleName(Integer saleId) {
        SaleDTO saleDTO = getSaleDTO(saleId);
        return (String) saleDTO.get("title");
    }

    protected String getSalePositionProductName(Integer salePositionId, SessionContext ctx) {
        SalePositionDTO salePositionDTO = getSalePositionDTO(salePositionId, ctx);
        if (null == salePositionDTO) {
            return "";
        }

        return (String) salePositionDTO.get("productName");
    }

    protected boolean changeCustomer(Integer saleId, Integer customerId) {
        SaleDTO saleDTO = getSaleDTO(saleId);
        if (null == saleDTO) {
            return false;
        }

        Integer actualCustomerId = (Integer) saleDTO.get("customerId");
        return !actualCustomerId.equals(customerId);

    }

    protected boolean isPartialFixed(ProductContract productContract) {
        return SalesConstants.PayMethod.PartialFixed.equal(productContract.getPayMethod());
    }

    protected boolean isCreditNote(Invoice invoice) {
        return FinanceConstants.InvoiceType.CreditNote.equal(invoice.getType());
    }

    protected List getPaymentSteps(Integer contractId, Integer companyId) {
        PaymentStepHome paymentStepHome =
                (PaymentStepHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PAYMENTSTEP);
        List paymentSteps = new ArrayList();

        try {
            paymentSteps = (List) paymentStepHome.findByContractId(contractId, companyId);
        } catch (FinderException e) {
            //
        }

        return paymentSteps;
    }
}
