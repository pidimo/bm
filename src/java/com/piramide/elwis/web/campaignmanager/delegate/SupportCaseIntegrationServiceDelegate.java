package com.piramide.elwis.web.campaignmanager.delegate;

import com.piramide.elwis.service.support.SupportCaseIntegrationService;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.ServiceLocator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.NamingException;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public class SupportCaseIntegrationServiceDelegate {
    private Log log = LogFactory.getLog(SupportCaseIntegrationServiceDelegate.class);
    public static final SupportCaseIntegrationServiceDelegate i = new SupportCaseIntegrationServiceDelegate();

    private SupportCaseIntegrationServiceDelegate() {
    }

    public SupportCaseIntegrationService getSupportCaseIntegrationService() {
        SupportCaseIntegrationService service = null;
        try {
            service = (SupportCaseIntegrationService) ServiceLocator.i.lookup(SupportConstants.JNDI_SUPPORTCASEINTEGRATIONSERVICE);
        } catch (NamingException e) {
            log.error("Error trying to instantiate the support integration service", e);
        }
        return service;

    }

}
