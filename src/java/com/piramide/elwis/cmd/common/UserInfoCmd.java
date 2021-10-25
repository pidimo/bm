package com.piramide.elwis.cmd.common;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.catalogmanager.Country;
import com.piramide.elwis.domain.catalogmanager.CountryHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: UserInfoCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */

public class
        UserInfoCmd extends EJBCommand {
    private Log log = LogFactory.getLog(UserInfoCmd.class);

    public void execute(SessionContext ctx) {
        try {
            executeInStateless(ctx);
        } catch (AppLevelException e) {
            log.fatal("Database inconcistence!!... ");
        }
    }

    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        try {
            User user = userHome.findByPrimaryKey((Integer) paramDTO.get("userId"));
            if (user.getHolidayCountryId() != null) {
                CountryHome countryHome = (CountryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_COUNTRY);
                Country country = countryHome.findByPrimaryKey(user.getHolidayCountryId());
                resultDTO.put("countryId", country.getCountryId());
                resultDTO.put("countryCode", country.getCountryAreaCode());
            }
            resultDTO.put("timeZone", user.getTimeZone());

            resultDTO.put("dayFragmentation", user.getDayFragmentation());
            resultDTO.put("finalDayOfWork", user.getFinalDayOfWork());
            resultDTO.put("initialDayOfWork", user.getInitialDayOfWork());
            resultDTO.put("language", user.getFavoriteLanguage());
            resultDTO.put(Constants.USER_ADDRESSID, user.getAddressId());
            resultDTO.put("userName", getUserName(user));
            resultDTO.put("userLogin", user.getUserLogin());

            if (CampaignConstants.TRUEVALUE.equals(paramDTO.get("isAppointment"))) {
                if ("false".equals(paramDTO.get("sameUser"))) {
                    resultDTO.put("calendarDefaultView", user.getCalendarDefaultView());
                } else if (!"true".equals(paramDTO.get("create"))) {
                    User otherUser = userHome.findByPrimaryKey((Integer) paramDTO.get("otherUserId"));
                    resultDTO.put("calendarDefaultView", otherUser.getCalendarDefaultView());
                }
            }
        } catch (FinderException e) {
            resultDTO.setResultAsFailure();
        }
    }

    private String getUserName(User user) {
        String userName = "";
        if (user != null) {
            AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
            try {
                Address address = addressHome.findByPrimaryKey(user.getAddressId());
                userName = address.getName();
            } catch (FinderException e) {
                log.debug("Error in find address..." + e);
            }
        }
        return userName;
    }

    public boolean isStateful() {
        return false;
    }
}
