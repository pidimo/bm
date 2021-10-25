package com.piramide.elwis.web.campaignmanager.delegate;

import com.piramide.elwis.service.campaign.CampaignMailerService;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.ServiceLocator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.NamingException;

/**
 * @author Fernando Javier Montaño Torrico
 * @version 1.0
 */
public class CampaignMailerServiceDelegate {
    private Log log = LogFactory.getLog(CampaignMailerServiceDelegate.class);
    public static final CampaignMailerServiceDelegate i = new CampaignMailerServiceDelegate();

    private CampaignMailerServiceDelegate() {
    }

    public CampaignMailerService getCampaignMailerService() {
        CampaignMailerService service = null;
        try {
            service = (CampaignMailerService) ServiceLocator.i.lookup(CampaignConstants.JNDI_CAMPAIGNMAILERSERVICE);
        } catch (NamingException e) {
            log.error("Error trying to instantiate the campaign mailer service", e);
        }
        return service;

    }

}
