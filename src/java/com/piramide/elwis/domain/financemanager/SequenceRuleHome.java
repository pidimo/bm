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

public interface SequenceRuleHome extends EJBLocalHome {
    SequenceRule create(ComponentDTO dto) throws CreateException;

    SequenceRule findByPrimaryKey(Integer key) throws FinderException;

    SequenceRule findByFormat(String format, Integer companyId) throws FinderException;

    SequenceRule findByType(Integer type, Integer companyId) throws FinderException;

    Collection findByDebitorId(Integer debitorId) throws FinderException;
}
