package com.piramide.elwis.service.support;

import javax.ejb.Local;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
@Local
public interface SupportCaseIntegrationService {

    Integer getCompanyIdByCompanyLogin(String companyLogin);

    Integer getUserIdByAddressId(Integer addressId, Integer companyId);

    Integer getAddressIdBySearchNamePersonType(String searchName, Integer personTypeId, Integer companyId);

    Integer getAddressIdBySearchNameNotPersonType(String searchName, Integer personTypeId, Integer companyId);

    boolean createSupportCase(Integer companyId,
                              String caseTitle,
                              Integer openByUserId,
                              Integer caseTypeId,
                              Integer severityId,
                              Integer productId,
                              Integer priorityId,
                              Integer addressId,
                              Integer toUserId,
                              Integer stateId,
                              Integer openDate,
                              Integer workLevelId,
                              Integer expireDate,
                              String caseDescription
    );
}
