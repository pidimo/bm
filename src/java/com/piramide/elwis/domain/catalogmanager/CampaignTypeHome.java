/**
 * Created by IntelliJ IDEA.
 * @author: ivan
 * Date: 23-10-2006
 * Time: 03:25:57 PM
 * To change this template use File | Settings | File Templates.
 */
package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CampaignTypeHome extends EJBLocalHome {
    public CampaignType create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.catalogmanager.CampaignType findByPrimaryKey(Integer key) throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;
}
