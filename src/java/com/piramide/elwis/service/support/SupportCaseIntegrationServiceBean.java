package com.piramide.elwis.service.support;

import com.piramide.elwis.cmd.admin.CompanyReadCmd;
import com.piramide.elwis.cmd.admin.ReadUserCmd;
import com.piramide.elwis.cmd.contactmanager.LightlyContactPersonCmd;
import com.piramide.elwis.cmd.supportmanager.SupportCaseCmd;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.ejb3.annotation.LocalBinding;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
@Stateless
@LocalBinding(jndiBinding = "ELWIS/SupportCaseIntegrationService/local")
@TransactionManagement(TransactionManagementType.BEAN)
public class SupportCaseIntegrationServiceBean implements SupportCaseIntegrationService {

    private Log log = LogFactory.getLog(SupportCaseIntegrationServiceBean.class);

    @Resource
    private SessionContext sessionContext;

    @Resource
    private UserTransaction userTransaction;


    public boolean createSupportCase(Integer companyId,
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
    ) {

        try {
            userTransaction.setTransactionTimeout(3600);
            userTransaction.begin();

            SupportCaseCmd caseCmd = new SupportCaseCmd();
            caseCmd.setOp("create");
            caseCmd.putParam("companyId", companyId);
            caseCmd.putParam("caseTitle", caseTitle);
            caseCmd.putParam("fromUserId", openByUserId);
            caseCmd.putParam("openByUserId", openByUserId);
            caseCmd.putParam("caseTypeId", caseTypeId);
            caseCmd.putParam("severityId", severityId);
            caseCmd.putParam("productId", productId);
            caseCmd.putParam("priorityId", priorityId);
            caseCmd.putParam("addressId", addressId);
            caseCmd.putParam("toUserId", toUserId);
            caseCmd.putParam("stateId", stateId);
            caseCmd.putParam("openDate", openDate);
            caseCmd.putParam("workLevelId", workLevelId);
            caseCmd.putParam("expireDate", expireDate);
            caseCmd.putParam("caseDescription", caseDescription);
            caseCmd.putParam("USER_SESSIONID", openByUserId);

            log.debug("PARAM IN CMD:" + caseCmd.getParamDTO());
            caseCmd.executeInStateless(sessionContext);

            userTransaction.commit();
        } catch (Exception e) {
            log.error("Error in create support case from service..", e);
            try {
                userTransaction.rollback();
            } catch (SystemException e1) {
                log.error("Canot set rollback..", e1);
            }
            return false;
        }

        return true;
    }

    public Integer getCompanyIdByCompanyLogin(String companyLogin) {
        log.debug("execute getCompanyIdByCompanyLogin......");

        Integer companyId = null;
        CompanyReadCmd companyReadCmd = new CompanyReadCmd();
        companyReadCmd.setOp("getCompanyByLogin");
        companyReadCmd.putParam("login", companyLogin);
        companyReadCmd.executeInStateless(sessionContext);
        ResultDTO resultDTO = companyReadCmd.getResultDTO();

        if (!resultDTO.isFailure()) {
            companyId = (Integer) resultDTO.get("companyId");
        }
        return companyId;
    }

    public Integer getUserIdByAddressId(Integer addressId, Integer companyId) {
        Integer userId = null;

        ReadUserCmd userCmd = new ReadUserCmd();
        userCmd.setOp("userByAddressId");
        userCmd.putParam("addressId", addressId);
        userCmd.putParam("companyId", companyId);
        userCmd.executeInStateless(sessionContext);
        ResultDTO resultDTO = userCmd.getResultDTO();

        if (!resultDTO.isFailure()) {
            userId = (Integer) resultDTO.get("userId");
        }
        return userId;
    }

    public Integer getAddressIdBySearchNamePersonType(String searchName, Integer personTypeId, Integer companyId) {
        Integer addressId = null;
        LightlyContactPersonCmd contactPersonCmd = new LightlyContactPersonCmd();
        contactPersonCmd.setOp("getBySearchNamePersonType");
        contactPersonCmd.putParam("searchName", searchName);
        contactPersonCmd.putParam("personTypeId", personTypeId);
        contactPersonCmd.putParam("companyId", companyId);
        contactPersonCmd.executeInStateless(sessionContext);
        ResultDTO resultDTO = contactPersonCmd.getResultDTO();

        if (!resultDTO.isFailure()) {
            addressId = (Integer) resultDTO.get("contactPersonId");
        }
        return addressId;
    }

    public Integer getAddressIdBySearchNameNotPersonType(String searchName, Integer personTypeId, Integer companyId) {
        Integer addressId = null;
        LightlyContactPersonCmd contactPersonCmd = new LightlyContactPersonCmd();
        contactPersonCmd.setOp("getBySearchNameNotPersonType");
        contactPersonCmd.putParam("searchName", searchName);
        contactPersonCmd.putParam("personTypeId", personTypeId);
        contactPersonCmd.putParam("companyId", companyId);
        contactPersonCmd.executeInStateless(sessionContext);
        ResultDTO resultDTO = contactPersonCmd.getResultDTO();

        if (!resultDTO.isFailure()) {
            addressId = (Integer) resultDTO.get("contactPersonId");
        }
        return addressId;
    }
}
