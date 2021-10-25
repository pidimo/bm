/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CampaignHome.java 12581 2016-08-26 22:42:33Z miguel ${NAME}.java, v 2.0 17-jun-2004 16:07:47 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CampaignHome extends EJBLocalHome {

    public Collection findAll() throws FinderException;

    Campaign findByPrimaryKey(Integer key) throws FinderException;

    public Campaign create(ComponentDTO dto) throws CreateException;

    Collection findByEmployeeId(Integer employeeId) throws FinderException;

    Collection findByCompanyIdAndRecordDate(Integer companyId, Integer startRecordDate, Integer endRecordDate) throws FinderException;
}
