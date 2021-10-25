/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.salesmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface PaymentStepHome extends EJBLocalHome {
    public PaymentStep create(ComponentDTO dto) throws CreateException;

    PaymentStep findByPrimaryKey(Integer key) throws FinderException;

    Collection findByContractId(Integer contractId, Integer companyId) throws FinderException;

    Collection findByContractIdWithFilterDate(Integer contractId, Integer filterDate) throws FinderException;
}
