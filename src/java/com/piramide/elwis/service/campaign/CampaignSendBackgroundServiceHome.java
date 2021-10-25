package com.piramide.elwis.service.campaign;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.5
 */
public interface CampaignSendBackgroundServiceHome extends EJBLocalHome {
    CampaignSendBackgroundService create() throws CreateException;
}
