package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.CampaignActivity;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityUser;
import com.piramide.elwis.domain.campaignmanager.CampaignContactHome;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityDTO;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 * cmd to verify the assign percent of activity user
 *
 * @author Miky
 * @version $Id: ActivityUserPercentCmd.java 10484 2014-08-28 22:51:28Z miguel $
 */
public class ActivityUserPercentCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ActivityUserPercentCmd................" + paramDTO);
        Integer activityId = new Integer((String) paramDTO.get("activityId"));
        CampaignActivity activity = (CampaignActivity) ExtendedCRUDDirector.i.read(new CampaignActivityDTO(activityId), resultDTO, false);

        if (activity != null) {
            Collection activityUsers = activity.getActivityUsers();
            //log.debug("ACCCCCCCCCCCCCCCCCUUUUUUUUUUUUUUUUUUUU:" + activityUsers.size());
            if (activityUsers.size() > 0) {

                int sumPercent = 0;
                boolean hasError = false;
                for (Iterator iterator = activityUsers.iterator(); iterator.hasNext();) {
                    CampaignActivityUser activityUser = (CampaignActivityUser) iterator.next();
                    if (activityUser.getPercent() != null) {
                        sumPercent = sumPercent + activityUser.getPercent().intValue();
                        if (sumPercent > 100) {
                            resultDTO.put("exceedPercent", "true");
                            hasError = true;
                            break;
                        }
                    } else {
                        resultDTO.put("nullPercent", "true");
                        hasError = true;
                        break;
                    }
                }

                if (!hasError && sumPercent < 100) {
                    resultDTO.put("needPercent", "true");
                }

                ///verif if exist campaign contacts to assign responsibles
                if (!existContactWithoutResponsible(activity)) {
                    resultDTO.put("emptyContact", "true");
                }
            } else {
                resultDTO.put("emptyUser", "true");
            }

        }
    }

    private boolean existContactWithoutResponsible(CampaignActivity activity) {
        CampaignContactHome campContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);
        try {
            Integer count = campContactHome.selectCountByActivityIdWithoutResponsible(activity.getActivityId());
            if (count != null && count > 0) {
                return true;
            }
        } catch (FinderException ignore) {
        }
        return false;
    }

    public boolean isStateful() {
        return false;
    }
}