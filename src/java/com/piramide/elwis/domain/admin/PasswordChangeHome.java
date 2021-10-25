package com.piramide.elwis.domain.admin;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public interface PasswordChangeHome extends EJBLocalHome {
    public PasswordChange create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.admin.PasswordChange findByPrimaryKey(Integer key) throws FinderException;
}
