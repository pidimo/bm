/**
 * @author : ivan
 *
 * Jatun S.R.L.
 */
package com.piramide.elwis.domain.financemanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface InvoiceFreeNumberHome extends EJBLocalHome {
    InvoiceFreeNumber create(ComponentDTO dto) throws CreateException;

    InvoiceFreeNumber findByPrimaryKey(Integer key) throws FinderException;

    Collection findByRangeFormat(Integer sequenceRuleId, Integer companyId) throws FinderException;

    Collection findInvoiceFreeNumberByRange(Integer sequenceRuleId,
                                            Integer startDate,
                                            Integer endDate,
                                            Integer number,
                                            Integer companyId) throws FinderException;

    InvoiceFreeNumber findInvoiceFreeNumber(Integer sequenceRuleId,
                                            Integer number,
                                            Integer companyId) throws FinderException;

    public Integer selectGetNumberByRange(Integer sequenceRuleId,
                                          Integer startDate,
                                          Integer endDate,
                                          Integer companyId) throws FinderException;

    public Integer selectGetNumber(Integer sequenceRuleId, Integer companyId) throws FinderException;
}
