/**
 *
 * @author Miky
 * @version $Id: ${NAME}.java 2009-06-26 05:54:58 PM $
 */
package com.piramide.elwis.domain.campaignmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CampaignGenTextImgHome extends EJBLocalHome {
    CampaignGenTextImg create(ComponentDTO dto) throws CreateException;

    CampaignGenTextImg findByPrimaryKey(CampaignGenTextImgPK key) throws FinderException;

    Collection findByCampaignGenTextId(Integer campaignGenTextId) throws FinderException;
}
