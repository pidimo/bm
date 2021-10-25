/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface InvoiceTemplateHome extends EJBLocalHome {

    InvoiceTemplate create(ComponentDTO dto) throws CreateException;

    InvoiceTemplate findByPrimaryKey(Integer key) throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;
}
