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

public interface InvoiceHome extends EJBLocalHome {
    Invoice create(ComponentDTO dto) throws CreateException;

    Invoice findByPrimaryKey(Integer key) throws FinderException;

    public Invoice findByNumber(String number, Integer companyId) throws FinderException;

    public Integer selectGetLastRuleNumberByInvoiceDate(Integer startDate,
                                                        Integer endDate,
                                                        Integer sequenceRuleId,
                                                        Integer companyId) throws FinderException;

    public Integer selectGetLastRuleNumberByRuleFormat(Integer sequenceRuleId,
                                                       Integer companyId) throws FinderException;

    public Collection findByCreditNoteOfId(Integer invoiceId, Integer companyId) throws FinderException;

    public Collection findByAddress(Integer addressId) throws FinderException;

    public Collection findByContactPerson(Integer addressId, Integer contactPersonId) throws FinderException;

    public Collection findBySentAddress(Integer sentAddressId) throws FinderException;

    public Collection findBySentContactPerson(Integer sentAddressId, Integer sentContactPersonId) throws FinderException;

    public Collection findByAdditionalAddressId(Integer additionalAddressId) throws FinderException;
}
