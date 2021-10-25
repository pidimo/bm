/**
 * @author Fernando Montaño   17:27:27
 * @version 2.0
 */
package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface OfficeHome extends EJBLocalHome {
    Office create(ComponentDTO dto) throws CreateException;

    Office findByPrimaryKey(Integer key) throws FinderException;

    Collection findByCompanyId(Integer companyId, Integer organizationId) throws FinderException;

    Collection findByOrganizationId(Integer organizationId) throws FinderException;

    Collection findBySupervisorId(Integer supervisorId) throws FinderException;
}
