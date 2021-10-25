/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: ContractEndReminderServiceBean.java  06-nov-2009 17:55:46$
 */
package com.piramide.elwis.service.sales;

import com.piramide.elwis.cmd.contactmanager.ReadCompanyContractReminderUserInfoCmd;
import com.piramide.elwis.cmd.utils.Email;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.admin.CompanyHome;
import com.piramide.elwis.domain.catalogmanager.Currency;
import com.piramide.elwis.domain.catalogmanager.CurrencyHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.domain.salesmanager.ProductContract;
import com.piramide.elwis.domain.salesmanager.ProductContractHome;
import com.piramide.elwis.domain.salesmanager.SalePosition;
import com.piramide.elwis.domain.salesmanager.SalePositionHome;
import com.piramide.elwis.service.sales.utils.ContractEndReminderMessage;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class ContractEndReminderServiceBean implements SessionBean {
    private Log log = LogFactory.getLog(ContractEndReminderServiceBean.class);
    private SessionContext ctx;

    public ContractEndReminderServiceBean() {
    }

    public void ejbCreate() throws CreateException {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException {
        this.ctx = sessionContext;
    }

    public void ejbRemove() throws EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    /**
     * Process contract to be remindered, filter all contracts in range -12+ hours
     * @param timeOfCall Date of service call
     * @param intervalBetweenChecks long interval
     */
    public void performContractEndReminder(Date timeOfCall, long intervalBetweenChecks) {
        log.debug("Perform contract end reminder service............." + timeOfCall + " <-> " + intervalBetweenChecks);

        DateTime callDateTime = new DateTime(timeOfCall.getTime());
        log.debug("call DATETIME" + callDateTime);
        log.debug("MINUS 12:" + callDateTime.minusHours(12));
        log.debug("PLUS 12:" + callDateTime.plusHours(12));

        //to find all contracts define range -12+ hours, this to make match with all time zones
        long initialTime = callDateTime.minusHours(12).getMillis();
        long endingTime = callDateTime.plusHours(12).getMillis();

        ProductContractHome contractHome = (ProductContractHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PRODUCTCONTRACT);

        Collection contractsInTime = null;
        try {
            contractsInTime = contractHome.findAllBetweenReminderTime(initialTime, endingTime);
        } catch (FinderException e) {
            contractsInTime = new ArrayList();
        }

        log.debug("Contracts match with time:" + contractsInTime);

        for (Iterator iterator = contractsInTime.iterator(); iterator.hasNext();) {
            ProductContract contract = (ProductContract) iterator.next();
            processContractAndSendReminder(contract, callDateTime);
        }
    }

    /**
     * Process contract and verify if this should be send. There is used the remainder receiver time zone
     * to verify if should be send
     *
     * @param contract contract
     * @param callDateTime call date time
     */
    private void processContractAndSendReminder(ProductContract contract, DateTime callDateTime) {

        Company company = readCompany(contract.getCompanyId());

        ReadCompanyContractReminderUserInfoCmd reminderUserInfoCmd = new ReadCompanyContractReminderUserInfoCmd();
        reminderUserInfoCmd.putParam("companyId", company.getCompanyId());
        reminderUserInfoCmd.executeInStateless(ctx);

        ResultDTO resultDTO = reminderUserInfoCmd.getResultDTO();
        if ((Boolean) resultDTO.get("existContractReminderUser")) {
            String userTimeZone = (String) resultDTO.get("timeZone");
            String userFavoriteLanguage = (String) resultDTO.get("favoriteLanguage");

            DateTimeZone userDateTimeZone;
            if (userTimeZone != null) {
                userDateTimeZone = DateTimeZone.forID(userTimeZone);
            } else {
                userDateTimeZone = DateTimeZone.getDefault();
            }

            //move the call reminder time to user time zone
            DateTime userDateTime = callDateTime.toDateTime(userDateTimeZone);
            log.debug("User Date Time to remind:" + userDateTime);

            //define range of -30+ min, this is 1 hour
            long iniTime = userDateTime.minusMinutes(30).getMillis();
            long endTime = userDateTime.plusMinutes(30).getMillis();

            if (contract.getReminderTime() >= iniTime && contract.getReminderTime() < endTime) {
                sendContractReminder(contract, company, userFavoriteLanguage, userDateTimeZone);
            }

        } else {
            log.warn("Could not be sent reminder of contract, company " + company.getLogin() + " has not defined contract end reminder setting");
        }
    }

    /**
     * Send the contract reminder email
     * @param contract contract
     * @param company company
     * @param userFavoriteLanguage system view language
     * @param userDateTimeZone time zone
     */
    private void sendContractReminder(ProductContract contract, Company company, String userFavoriteLanguage, DateTimeZone userDateTimeZone) {

        String reminderMailFrom = ConfigurationFactory.getConfigurationManager().getValue("elwis.system.fromEmailSender");
        ContractEndReminderMessage reminderMsg = composeReminderMessage(contract, userFavoriteLanguage);

        Email email = new Email(company.getEmailContract(), reminderMailFrom, reminderMsg.getSubject(),
                reminderMsg.getMessage(), "text/plain", userDateTimeZone.toTimeZone());

        try {
            JMSUtil.sendToJMSQueue(WebMailConstants.JNDI_SENDSIMPLEMAILMDB, email, false);
        } catch (Exception e) {
            log.info("Error in send contract reminder email..");
        }
    }

    /**
     * Wrapper of reminder message
     * @param contract contract
     * @param userFavoriteLanguage system view language
     * @return ContractEndReminderMessage
     */
    private ContractEndReminderMessage composeReminderMessage(ProductContract contract, String userFavoriteLanguage) {
        ContractEndReminderMessage reminderMessage = new ContractEndReminderMessage(userFavoriteLanguage, contract.getDaysToRemind());

        reminderMessage.setNumber(contract.getContractNumber());
        reminderMessage.setContactName(readAddressName(contract.getAddressId()));
        reminderMessage.setContactPersonName(readAddressName(contract.getContactPersonId()));
        reminderMessage.setProductName(readProductName(contract.getSalePositionId()));
        reminderMessage.setPrice(contract.getPrice());
        reminderMessage.setDiscount(contract.getDiscount());
        reminderMessage.setSellerName(readAddressName(contract.getSellerId()));
        reminderMessage.setCurrency(readCurrencyName(contract.getCurrencyId()));
        reminderMessage.setPayPeriod(contract.getPayPeriod());
        reminderMessage.setPayStartDate(contract.getPayStartDate());
        reminderMessage.setInvocedUntil(contract.getInvoicedUntil());
        reminderMessage.setContractEndDate(contract.getContractEndDate());

        return reminderMessage;
    }

    private Company readCompany(Integer companyId) {
        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
        Company company = null;
        try {
            company = companyHome.findByPrimaryKey(companyId);
        } catch (FinderException e) {
            log.debug("Error in find company.." + companyId);
        }
        return company;
    }

    private String readAddressName(Integer addressId) {
        String addressName = null;
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        if (addressId != null) {
            try {
                Address address = addressHome.findByPrimaryKey(addressId);
                addressName = address.getName();
            } catch (FinderException e) {
                log.debug("Error in find address.." + addressId);
            }
        }
        return addressName;
   }

    private String readProductName(Integer salePositionId) {
        String productName = null;
        SalePositionHome salePositionHome = (SalePositionHome)  EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);
        ProductHome productHome = (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
        if (salePositionId != null) {
            try {
                SalePosition salePosition = salePositionHome.findByPrimaryKey(salePositionId);
                Product product = productHome.findByPrimaryKey(salePosition.getProductId());
                productName = product.getProductName();
            } catch (FinderException e) {
                log.debug("Error in find sale position.." + salePositionId);
            }
        }
        return productName;
    }

    private String readCurrencyName(Integer currencyId) {
        String currencyName = null;
        CurrencyHome currencyHome = (CurrencyHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CURRENCY);
        if (currencyId != null) {
            try {
                Currency currency = currencyHome.findByPrimaryKey(currencyId);
                currencyName = currency.getCurrencyName();
            } catch (FinderException e) {
                log.debug("Error in find currency.." + currencyId);
            }
        }
        return currencyName;
   }

}
