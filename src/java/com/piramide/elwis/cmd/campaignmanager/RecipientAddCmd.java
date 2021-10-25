package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.CampaignContactHome;
import com.piramide.elwis.dto.campaignmanager.CampaignContactDTO;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


/**
 * @author yumi
 * @version $Id: RecipientAddCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class RecipientAddCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        List values = null;
        StringTokenizer tokenizer = null;
        log.debug(" ...      RecipientAddCMD    execute  ...");

        if (paramDTO.get("excludes") != null) {
            values = (List) paramDTO.get("excludes");
            String imp = null;
            Integer cPersonId = null;
            CampaignContactDTO dto = new CampaignContactDTO(paramDTO);
            dto.put("state", new Integer(1));
            CampaignContactHome campContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);

            for (Iterator iterator = values.iterator(); iterator.hasNext();) {

                imp = (String) iterator.next();
                tokenizer = new StringTokenizer(imp, ",");
                String addressId = tokenizer.nextToken();
                String contactPersonId = null;
                Collection campContacts = null;
                boolean create = true;

                if (tokenizer.hasMoreElements()) {
                    contactPersonId = tokenizer.nextToken();
                    cPersonId = new Integer(contactPersonId);
                }
                try {
                    if (cPersonId != null) {
                        campContacts = campContactHome.findByCampaignIdContactPersonId(new Integer(paramDTO.get("companyId").toString()),
                                new Integer(paramDTO.get("campaignId").toString()),
                                new Integer(addressId), cPersonId);
                    } else {
                        campContacts = campContactHome.findByCampaignIdContactPersonNULL(new Integer(paramDTO.get("companyId").toString()),
                                new Integer(paramDTO.get("campaignId").toString()),
                                new Integer(addressId));
                    }
                    if (campContacts.size() > 0) {
                        create = false;
                    }

                } catch (FinderException e) {
                    log.debug(" ... campContact not found  ... ");
                }

                log.debug("... create new campContact .......");
                if (create) {
                    dto.put("addressId", new Integer(addressId));
                    if (contactPersonId != null) {
                        dto.put("contactPersonId", new Integer(contactPersonId));
                    } else {
                        dto.put("contactPersonId", null);
                    }
                    ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_CREATE, dto, resultDTO, false, false, false, false);
                } else {
                    resultDTO.put("campaignId", paramDTO.get("campaignId"));
                    resultDTO.put("already", CampaignConstants.TRUEVALUE);
                }
            }
        }
    }

    public boolean isStateful() {
        return false;
    }

}
