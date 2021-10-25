package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.CampaignContact;
import com.piramide.elwis.domain.campaignmanager.CampaignContactPK;
import com.piramide.elwis.dto.campaignmanager.CampaignContactDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Jatun S.R.L.
 * assign user to selected campaign contact
 *
 * @author Miky
 * @version $Id: ActivityContactResponsibleCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ActivityContactResponsibleCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ActivityContactResponsibleCmd................" + paramDTO);

        Integer campaignId = new Integer(paramDTO.getAsInt("campaignId"));
        Integer userId = new Integer(paramDTO.get("userId").toString());

        List values = null;
        if (paramDTO.get("selected") != null) {
            values = (List) paramDTO.get("selected");
            List viewContactsIdList = new ArrayList((List) paramDTO.get("listViewContactIds"));
            log.debug("view contact list size:" + viewContactsIdList.size());

            for (Iterator iterator = values.iterator(); iterator.hasNext();) {
                Integer campContactId = new Integer((String) iterator.next());
                CampaignContactPK pk = new CampaignContactPK(campContactId, campaignId);
                CampaignContactDTO campContactDTO = new CampaignContactDTO();
                campContactDTO.setPrimKey(pk);

                ResultDTO contResultDTO = new ResultDTO();
                CampaignContact campContact = (CampaignContact) ExtendedCRUDDirector.i.read(campContactDTO, contResultDTO, false);
                if (campContact != null) {
                    if (campContact.getUserId() == null) {
                        CampaignContactDTO dto = new CampaignContactDTO();
                        dto.setPrimKey(pk);
                        dto.put("userId", userId);
                        dto.put("version", campContact.getVersion());

                        ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_UPDATE, dto, new ResultDTO(), true, false, false, false);
                    } else if (!userId.equals(campContact.getUserId())) {
                        //campaign contact has modified by other user
                        resultDTO.put("someModified", "true");
                    }

                    //remove assigned contact
                    viewContactsIdList = removeAssignedContactFromViewList(campContact.getCampaignContactId(), viewContactsIdList);
                } else {
                    //message camp contact has be removed by other user
                    resultDTO.put("someDeleted", "true");
                }
            }

            log.debug("view contact list size:" + viewContactsIdList.size());
            //remove assignation if is necessary
            for (Iterator iterator = viewContactsIdList.iterator(); iterator.hasNext();) {
                Integer campContactId = new Integer((String) iterator.next());
                CampaignContactPK pk = new CampaignContactPK(campContactId, campaignId);
                CampaignContactDTO campContactDTO = new CampaignContactDTO();
                campContactDTO.setPrimKey(pk);

                ResultDTO contResultDTO = new ResultDTO();
                CampaignContact campContact = (CampaignContact) ExtendedCRUDDirector.i.read(campContactDTO, contResultDTO, false);
                if (campContact != null) {
                    if (userId.equals(campContact.getUserId())) {
                        campContact.setUserId(null);
                    }
                }
            }
        }
    }

    /**
     * remove this campaign contact id of this list
     *
     * @param campContactId
     * @param viewCampContactIdList
     * @return list
     */
    private List removeAssignedContactFromViewList(Integer campContactId, List viewCampContactIdList) {
        for (Iterator iterator = viewCampContactIdList.iterator(); iterator.hasNext();) {
            Integer viewCampContactId = new Integer((String) iterator.next());
            if (campContactId.equals(viewCampContactId)) {
                iterator.remove();
                break;
            }
        }
        return viewCampContactIdList;
    }

    public boolean isStateful() {
        return false;
    }
}