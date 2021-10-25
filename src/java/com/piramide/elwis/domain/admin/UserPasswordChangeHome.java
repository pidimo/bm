package com.piramide.elwis.domain.admin;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public interface UserPasswordChangeHome extends EJBLocalHome {
    public UserPasswordChange create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.admin.UserPasswordChange findByPrimaryKey(com.piramide.elwis.domain.admin.UserPasswordChangePK key) throws FinderException;

    public Collection findUserByPasswordChange(Integer passwordChangeId, Integer companyId) throws FinderException;

    public Collection findPasswordChangeByUser(Integer userId, Integer companyId) throws FinderException;
}
