package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.campaignmanager.CampaignActivity;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityUser;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityUserPK;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityUserDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.Iterator;
import java.util.List;

/**
 * Jatun S.R.L.
 * cmd to add activity user from internal user
 *
 * @author Miky
 * @version $Id: ActivityUserAddCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ActivityUserAddCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ActivityUserAddCmd................" + paramDTO);

        Integer activityId = new Integer(paramDTO.get("activityId").toString());

        if (paramDTO.get("selected") != null) {
            List values = (List) paramDTO.get("selected");

            CampaignActivity activity = (CampaignActivity) ExtendedCRUDDirector.i.read(new CampaignActivityDTO(activityId), resultDTO, false);
            if (activity != null) {

                for (Iterator iterator = values.iterator(); iterator.hasNext();) {
                    Integer userId = new Integer(iterator.next().toString());

                    User user = (User) ExtendedCRUDDirector.i.read(new UserDTO(userId), new ResultDTO(), false);
                    if (user != null) {

                        CampaignActivityUserPK activityUserPK = new CampaignActivityUserPK(userId, activityId);
                        CampaignActivityUserDTO activityUserDTO = new CampaignActivityUserDTO();
                        activityUserDTO.setPrimKey(activityUserPK);

                        CampaignActivityUser activityUser = (CampaignActivityUser) ExtendedCRUDDirector.i.read(activityUserDTO, new ResultDTO(), false);
                        if (activityUser == null) {
                            activityUserDTO = new CampaignActivityUserDTO();
                            activityUserDTO.put("activityId", activityId);
                            activityUserDTO.put("userId", userId);
                            activityUserDTO.put("companyId", activity.getCompanyId());

                            //create
                            ExtendedCRUDDirector.i.create(activityUserDTO, resultDTO, false);
                        } else {
                            //message already exist this camp contact
                            resultDTO.put("alreadyExist", "true");
                        }
                    } else {
                        resultDTO.put("userDeleted", "true");
                    }
                }

                resultDTO.put("successful", "true");
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
