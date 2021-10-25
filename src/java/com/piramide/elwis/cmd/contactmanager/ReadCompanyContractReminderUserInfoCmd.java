package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.admin.CompanyHome;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.contactmanager.Telecom;
import com.piramide.elwis.domain.contactmanager.TelecomHome;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 * Cmd to read user info related to company contract end reminder email configuration
 *
 * @author Miky
 * @version $Id: ReadCompanyContractReminderUserInfoCmd.java  11-nov-2009 18:00:31$
 */
public class ReadCompanyContractReminderUserInfoCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ReadCompanyContractReminderUserInfoCmd................" + paramDTO);

        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
        String emailReminder = (String) paramDTO.get("reminderEmail");

        if (emailReminder == null) {
            Company company = readCompany(companyId);
            emailReminder = company.getEmailContract();
        }

        boolean existReminderUser = false;
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

        if (emailReminder != null) {
            Collection telecoms = null;
            try {
                telecoms = telecomHome.findAllContactPersonTelecomsOfAddress(emailReminder, companyId);
            } catch (FinderException e) {
                log.debug("Error in find telecoms..");
                telecoms = new ArrayList();
            }

            //itereate telecoms and find user related to the contact person of company
            for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
                Telecom telecom = (Telecom) iterator.next();
                if (telecom.getContactPersonId() != null) {
                    User user = readUserByAddressId(telecom.getContactPersonId(), companyId);

                    if (user != null && user.getActive()) {
                        existReminderUser = true;
                        String favoriteLang = user.getFavoriteLanguage();
                        String timeZone = user.getTimeZone();

                        User rootCompanyUser = readRootCompanyUser(companyId);
                        if (rootCompanyUser != null) {
                            if (favoriteLang == null) {
                                favoriteLang = rootCompanyUser.getFavoriteLanguage();
                            }
                            if (timeZone == null) {
                                timeZone = rootCompanyUser.getTimeZone();
                            }
                        }

                        resultDTO.put("favoriteLanguage", favoriteLang);
                        resultDTO.put("timeZone", timeZone);
                        break;
                    }
                }
            }
        }

        resultDTO.put("existContractReminderUser", existReminderUser);
    }

    private User readUserByAddressId(Integer addressId, Integer companyId) {
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        User user = null;
        try {
            user = userHome.findByAddressId(companyId, addressId);
        } catch (FinderException e) {
            log.debug("Not found user with addressId.. " + addressId);
        }
        return user;
    }

    private Company readCompany(Integer companyId) {
        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
        Company company = null;
        try {
            company = companyHome.findByPrimaryKey(companyId);
        } catch (FinderException e) {
            log.debug("Not found company.. " + companyId);
        }
        return company;
    }

    private User readRootCompanyUser(Integer companyId) {
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        User user = null;
        try {
            user = userHome.findRootUserByCompany(companyId);
        } catch (FinderException e) {
            log.debug("Not found root user of company.. " + companyId);
        }
        return user;
    }
}
