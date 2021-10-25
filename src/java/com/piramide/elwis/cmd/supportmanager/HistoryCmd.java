package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.dto.supportmanager.ArticleHistoryDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 25, 2005
 * Time: 6:30:18 PM
 * To change this template use File | Settings | File Templates.
 */

public class HistoryCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

    }

    public void createHistory(ArticleHistoryDTO dto) {

        log.debug("HistoryCMD ExECUTE .......  function createHistory..");
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        User u = null;
        DateTimeZone zone = null;
        String timeZone = null;
        try {
            u = userHome.findByPrimaryKey(new Integer(dto.get("userId").toString()));
        } catch (FinderException e) {
            log.debug("userSession notFound ... " + e);
        }
        if (u != null) {
            timeZone = u.getTimeZone();
            zone = DateTimeZone.forID(timeZone);
        }
        if (timeZone == null) {
            zone = DateTimeZone.getDefault();
        }
        DateTime createDateTime = new DateTime(zone);
        dto.put("logDateTime", new Long(createDateTime.getMillis()));
        ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_CREATE, dto, resultDTO, false, false, false, false);
    }

    public boolean isStateful() {
        return false;
    }
}

