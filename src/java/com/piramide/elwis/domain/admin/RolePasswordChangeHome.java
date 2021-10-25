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
public interface RolePasswordChangeHome extends EJBLocalHome {
    public RolePasswordChange create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.admin.RolePasswordChange findByPrimaryKey(RolePasswordChangePK key) throws FinderException;

    public Collection findRoleByPasswordChange(Integer passwordChangeId, Integer companyId) throws FinderException;

    public Collection findPasswordChangeByRole(Integer roleId, Integer companyId) throws FinderException;

    public Collection findPasswordChangeByRoleAndTime(Integer roleId, Long millis) throws FinderException;
}
