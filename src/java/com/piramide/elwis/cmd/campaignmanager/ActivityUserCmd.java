package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityUser;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityUserPK;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityUserDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 * class to only read and delete action
 *
 * @author Miky
 * @version $Id: ActivityUserCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ActivityUserCmd extends GeneralCmd {

    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ActivityUserCmd................" + paramDTO);

        CampaignActivityUserDTO activityUserDTO = new CampaignActivityUserDTO(paramDTO);

        if (paramDTO.get("activityId") != null && paramDTO.get("userId") != null) {
            Integer activityId = new Integer(paramDTO.get("activityId").toString());
            Integer userId = new Integer(paramDTO.get("userId").toString());

            CampaignActivityUserPK activityUserPK = new CampaignActivityUserPK(userId, activityId);
            activityUserDTO.setPrimKey(activityUserPK);
            if ("update".equals(getOp())) {
                activityUserDTO.remove("activityId");
                activityUserDTO.remove("userId");
            }
        }

        //execute cmd
        super.setOp(this.getOp());
        super.checkDuplicate = false;
        super.isClearingForm = false;
        CampaignActivityUser activityUser = (CampaignActivityUser) super.execute(ctx, activityUserDTO);

        if (activityUser != null) {
            if (activityUser.getUserId() != null) {
                User user = (User) EJBFactory.i.findEJB(new UserDTO(activityUser.getUserId()));
                readAddressName(user.getAddressId(), "userName");
            }
        }
    }

    private void readAddressName(Integer addressId, String field) {
        Address address = (Address) ExtendedCRUDDirector.i.read(new AddressDTO(addressId), new ResultDTO(), false);
        if (address != null) {
            resultDTO.put(field, address.getName());
        }
    }
}
