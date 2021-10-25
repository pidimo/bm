/**
 *
 * @author Miky
 * @version $Id: ${NAME}.java 2009-06-23 05:13:24 PM $
 */
package com.piramide.elwis.domain.campaignmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CampaignTextImgHome extends EJBLocalHome {
    CampaignTextImg create(ComponentDTO dto) throws CreateException;

    CampaignTextImg findByPrimaryKey(CampaignTextImgPK key) throws FinderException;

    Collection findByTemplateIdLanguageId(Integer templateId, Integer languageId) throws FinderException;
}
