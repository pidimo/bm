package com.piramide.elwis.cmd.dashboard;

import com.piramide.elwis.domain.dashboard.DashboardContainer;
import com.piramide.elwis.domain.dashboard.DashboardContainerHome;
import com.piramide.elwis.utils.DashboardConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : ivan
 *         Date: Aug 31, 2006
 *         Time: 11:07:26 AM
 */
public class FindDashboardCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        int userId = (Integer) paramDTO.get("userId");
        int companydId = (Integer) paramDTO.get("companyId");

        DashboardContainerHome h =
                (DashboardContainerHome) EJBFactory.i.getEJBLocalHome(DashboardConstants.JNDI_DASHBOARDCONTAINER);

        DashboardContainer c = null;
        try {
            c = h.findByUserIdCompanyId(userId, companydId);
        } catch (FinderException fe) {
            log.debug("The user : " + userId + " from the company : " + companydId + " it does not have assigned dashboard.");
        }

        if (null == c) {
            try {
                c = h.create(userId, companydId);
            } catch (CreateException ce) {
                ce.printStackTrace();
            }
        }

        Map m = new HashMap();
        if (c != null) {
            m.put("dashboardContainerId", c.getDashboardContainerId());
            m.put("userId", c.getUserId());
            m.put("companyIdId", c.getCompanyId());
        }
        resultDTO.put("dashboardContainer", m);
    }

    public boolean isStateful() {
        return false;
    }
}
