package com.piramide.elwis.cmd.utils;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.joda.time.DateTimeZone;

import javax.ejb.FinderException;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class SchedulerUtil {
    public static final SchedulerUtil i = new SchedulerUtil();

    private SchedulerUtil() {

    }

    public DateTimeZone getUserDateTimeZone(Integer userId) {
        DateTimeZone dateTimeZone = DateTimeZone.getDefault();

        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);

        try {
            User user = userHome.findByPrimaryKey(userId);
            if (null != user.getTimeZone() && !"".equals(user.getTimeZone().trim())) {
                dateTimeZone = DateTimeZone.forID(user.getTimeZone());
            }
        } catch (FinderException e) {
            //This exception never happen because the application never deletes the users.
        }

        return dateTimeZone;
    }
}
