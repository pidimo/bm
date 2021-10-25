package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public interface ImportRecordHome extends EJBLocalHome {
    ImportRecord create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.contactmanager.ImportRecord findByPrimaryKey(Integer key) throws FinderException;

    Collection findByImportProfile(Integer profileId) throws FinderException;

    Collection findByImportProfileChilds(Integer profileId) throws FinderException;

    ImportRecord findByIdentityKeyProfileId(String identityKey, Integer profileId, Integer companyId) throws FinderException;

    Collection findByImportProfileRecordTypeWithoutDuplicates(Integer profileId, Integer companyId, Integer recordType) throws FinderException;

    Collection findByParentImportRecordId(Integer parentImportRecordId, Integer companyId) throws FinderException;

    Collection findByParentImportRecordIdWithoutDuplicates(Integer parentImportRecordId, Integer companyId) throws FinderException;

    Collection findByParentImportRecordIdDuplicates(Integer parentImportRecordId, Integer companyId) throws FinderException;

    Collection findByOrganizationId(Integer organizationId) throws FinderException;

    Integer selectCountByProfileId(Integer profileId) throws FinderException;

    Integer selectCountByProfileIdDuplicates(Integer profileId) throws FinderException;

}
