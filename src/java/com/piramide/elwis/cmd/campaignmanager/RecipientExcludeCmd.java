package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.CampaignContact;
import com.piramide.elwis.domain.campaignmanager.CampaignContactHome;
import com.piramide.elwis.domain.campaignmanager.CampaignContactPK;
import com.piramide.elwis.dto.campaignmanager.CampaignContactDTO;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Iterator;
import java.util.List;

/**
 * @author Yumi
 * @version $Id: RecipientExcludeCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class RecipientExcludeCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("recipientExcludeCmd .... execute ...");

        if ("exclude".equals(getOp())) {
            List values = null;

            if (paramDTO.get("excludes") != null) {
                values = (List) paramDTO.get("excludes");
                String exclude = null;

                CampaignContactHome campContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);

                for (Iterator iterator = values.iterator(); iterator.hasNext();) {
                    try {
                        exclude = (String) iterator.next();

                        CampaignContact campaignContact = (CampaignContact) campContactHome.findByCampaignContactKey(new Integer(paramDTO.get("campaignId").toString()), new Integer(exclude));
                        campaignContact.remove();
                    } catch (FinderException e) {
                    } catch (RemoveException e) {
                    }
                }
            }
            if (paramDTO.get("campaignContactId") != null && !"".equals(paramDTO.get("campaignContactId"))) {
                CampaignContactPK pk = new CampaignContactPK(new Integer(paramDTO.getAsInt("campaignContactId")), new Integer(paramDTO.getAsInt("campaignId")));
                CampaignContactDTO dto = new CampaignContactDTO(paramDTO);
                dto.setPrimKey(pk);
                ExtendedCRUDDirector.i.delete(dto, resultDTO, false, "Fail");
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
