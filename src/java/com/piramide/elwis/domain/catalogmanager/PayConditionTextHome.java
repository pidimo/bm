/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface PayConditionTextHome extends EJBLocalHome {
    PayConditionText create(ComponentDTO dto) throws CreateException;

    PayConditionText findByPrimaryKey(PayConditionTextPK key) throws FinderException;

    Collection findByPayConditionId(Integer payConditionId, Integer companyId) throws FinderException;
}
