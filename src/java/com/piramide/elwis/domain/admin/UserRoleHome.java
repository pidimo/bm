/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.admin;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface UserRoleHome extends EJBLocalHome {
    public UserRole create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.admin.UserRole findByPrimaryKey(UserRolePK key) throws FinderException;

    public Collection findRolesByUser(Integer userId, Integer companyId) throws FinderException;

    public Collection findUsersByRole(Integer roleId, Integer companyId) throws FinderException;
}
