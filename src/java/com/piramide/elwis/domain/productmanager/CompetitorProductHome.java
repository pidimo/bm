/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CompetitorProductHome.java 10417 2014-04-09 02:17:26Z miguel ${NAME}.java, v 2.0 23-ago-2004 14:22:02 Yumi Exp $
 */
package com.piramide.elwis.domain.productmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CompetitorProductHome extends EJBLocalHome {

    CompetitorProduct findByPrimaryKey(Integer key) throws FinderException;

    CompetitorProduct findByCompetitorProductNameCompetitorId(Integer companyId, String productName, Integer competitorId) throws FinderException;

    CompetitorProduct create(ComponentDTO dto) throws CreateException;

    Collection findByCompetitorId(Integer competitorId) throws FinderException;
}
