/**
 * Jatun S.R.L
 *
 * @author ivan
 *
 */
package com.piramide.elwis.domain.salesmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface ActionTypeSequenceHome extends EJBLocalHome {
    ActionTypeSequence create(Integer actionTypeId, Integer numberId, Integer companyId) throws CreateException;

    ActionTypeSequence create(ComponentDTO dto) throws CreateException;

    ActionTypeSequence findByPrimaryKey(ActionTypeSequencePK key) throws FinderException;

    ActionTypeSequence findByActionTypeId(Integer actionTypeId, Integer companyId) throws FinderException;
}
