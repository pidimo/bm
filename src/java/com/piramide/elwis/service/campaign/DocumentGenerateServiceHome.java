/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 26, 2004
 * Time: 9:56:49 AM
 * To change this template use File | Settings | File Templates.
 */
package com.piramide.elwis.service.campaign;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface DocumentGenerateServiceHome extends EJBLocalHome {
    com.piramide.elwis.service.campaign.DocumentGenerateService create() throws CreateException;
}
