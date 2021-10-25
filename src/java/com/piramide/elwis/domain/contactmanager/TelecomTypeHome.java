package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * This Class represents the Home interface of TelecomType Entity Bean
 *
 * @author yumi
 * @version $Id: TelecomTypeHome.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface TelecomTypeHome extends EJBLocalHome {
    public TelecomType create(ComponentDTO dto) throws CreateException;

    TelecomType findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;

    public Collection findByCompanyIdOrderBySequence(Integer companyId) throws FinderException;

    public Collection findByCompanyIdAndTypeOrderBySequence(Integer companyId, String type) throws FinderException;

}