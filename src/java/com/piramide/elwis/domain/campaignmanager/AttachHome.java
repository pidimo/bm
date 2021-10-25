/**
 * @author: ivan
 * Date: 25-10-2006: 11:21:52 AM
 */
package com.piramide.elwis.domain.campaignmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface AttachHome extends EJBLocalHome {
    public Attach create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.campaignmanager.Attach findByPrimaryKey(Integer key) throws FinderException;
}
