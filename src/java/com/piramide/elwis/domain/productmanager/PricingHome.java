package com.piramide.elwis.domain.productmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * AlfaCentauro Team
 *
 * @author Ernesto
 * @version $Id: PricingHome.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v 2.0 23-ago-2004 9:26:06 Ernesto Exp $
 */

public interface PricingHome extends EJBLocalHome {
    Pricing create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.productmanager.Pricing findByPrimaryKey(com.piramide.elwis.domain.productmanager.PricingPK key) throws FinderException;
}
