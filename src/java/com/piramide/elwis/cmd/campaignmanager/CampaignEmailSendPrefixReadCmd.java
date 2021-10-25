package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.webmailmanager.MailAccount;
import com.piramide.elwis.domain.webmailmanager.MailAccountHome;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Cmd to read email from personal
 * @author Miguel A. Rojas Cardenas
 * @version 4.7
 */
public class CampaignEmailSendPrefixReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CampaignEmailSendPrefixReadCmd..... " + paramDTO);
        Integer employeeId = new Integer(paramDTO.get("employeeId").toString());
        Integer senderPrefixType = new Integer(paramDTO.get("senderPrefixType").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        String senderPrefix = null;

        Address employeeAddress = findAddress(employeeId);
        if (employeeAddress != null) {
            if (CampaignConstants.SenderPrefixType.SENDERNAME.equal(senderPrefixType)) {
                senderPrefix = employeeAddress.getName();

            } else if (CampaignConstants.SenderPrefixType.MAILACCOUNTPREFIX.equal(senderPrefixType)) {
                senderPrefix = getPrefixDefaultMailAccount(employeeId, companyId);
                if (senderPrefix == null) {
                    senderPrefix = employeeAddress.getName();
                }
            }
        }

        log.debug("The Sender Prefix.." + senderPrefix);

        resultDTO.put("senderPrefixRead", senderPrefix);
    }

    private String getPrefixDefaultMailAccount(Integer addressId, Integer companyId) {
        String prefix = null;
        User user = findUserByAddressId(addressId, companyId);
        if (user != null) {
            MailAccount mailAccount = getDefaultMailAccount(user.getUserId(), companyId);
            if (mailAccount != null) {
                prefix = mailAccount.getPrefix();
            }
        }

        return prefix;
    }

    private Address findAddress(Integer addressId) {
        Address address = null;
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        try {
            address = addressHome.findByPrimaryKey(addressId);
        } catch (FinderException e) {
            log.debug("Not found address with id:" + addressId, e);
        }

        return address;
    }

    private User findUserByAddressId(Integer addressId, Integer companyId) {
        User user = null;
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        try {
            user = userHome.findByAddressId(companyId, addressId);
        } catch (FinderException e) {
            log.debug("Error in find user with address..." + addressId + "-" + companyId, e);
        }
        return user;
    }

    private MailAccount getDefaultMailAccount(Integer userMailId, Integer companyId) {
        MailAccountHome accountHome = (MailAccountHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILACCOUNT);
        try {
            return accountHome.findDefaultAccount(userMailId, companyId);
        } catch (FinderException e) {
            log.debug("Cannot find default account for user " + userMailId + " in company " + companyId);
        }
        return null;
    }

    public boolean isStateful() {
        return false;
    }
}
