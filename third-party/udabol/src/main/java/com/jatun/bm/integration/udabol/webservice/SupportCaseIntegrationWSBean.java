package com.jatun.bm.integration.udabol.webservice;

import com.jatun.bm.integration.udabol.delegate.SupportCaseIntegrationServiceDelegate;
import com.jatun.bm.integration.udabol.util.Messages;
import com.piramide.elwis.service.support.SupportCaseIntegrationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.annotations.Form;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
@Path("/supportcase")
public class SupportCaseIntegrationWSBean {
    private Log log = LogFactory.getLog(SupportCaseIntegrationWSBean.class);
    private static final String FAIL = "Failed";
    private static final String OK = "OK";

    @POST
    @Path(value = "/create")
    @Produces("text/plain")
    public String create(@Form SupportCaseForm form) {
        log.debug("Execute create in SupportCaseIntegrationWSBean.....");
        log.debug("Form Data:" + form.toString());

        Integer companyId;
        Integer openByAddressId;
        Integer toAddressId;
        Integer openByUserId;
        Integer toUserId;
        Integer contactRelatedAddressId;

        String errors = form.validate();
        if (errors != null) {
            return composeError(errors);

        } else {
            SupportCaseIntegrationService service = SupportCaseIntegrationServiceDelegate.i.getSupportCaseIntegrationService();

            companyId = service.getCompanyIdByCompanyLogin(form.getCompany());
            if (companyId == null) {
                return composeError(Messages.i.getMessage("error.invalid.company", form.getCompany()));
            }


            openByAddressId = service.getAddressIdBySearchNameNotPersonType(form.getOpenUserCode(), valueAsInteger(form.getPersonTypeId()), companyId);
            if (openByAddressId == null) {
                return composeError(Messages.i.getMessage("error.invalid.address", form.getOpenUserCode()));
            }

            toAddressId = service.getAddressIdBySearchNameNotPersonType(form.getAssignedUserCode(), valueAsInteger(form.getPersonTypeId()), companyId);
            if (toAddressId == null) {
                return composeError(Messages.i.getMessage("error.invalid.address", form.getAssignedUserCode()));
            }

            contactRelatedAddressId = service.getAddressIdBySearchNamePersonType(form.getContactCode(), valueAsInteger(form.getPersonTypeId()), companyId);
            if (contactRelatedAddressId == null) {
                return composeError(Messages.i.getMessage("error.invalid.address", form.getContactCode()));
            }

            openByUserId = service.getUserIdByAddressId(openByAddressId, companyId);
            if (openByUserId == null) {
                return composeError(Messages.i.getMessage("error.invalid.user", form.getOpenUserCode()));
            }

            toUserId = service.getUserIdByAddressId(toAddressId, companyId);
            if (toUserId == null) {
                return composeError(Messages.i.getMessage("error.invalid.user", form.getOpenUserCode()));
            }

            //if no errors create the support case
            boolean success = service.createSupportCase(companyId,
                    form.getTitle(),
                    openByUserId,
                    valueAsInteger(form.getCaseTypeId()),
                    valueAsInteger(form.getSeverityId()),
                    valueAsInteger(form.getProductId()),
                    valueAsInteger(form.getPriorityId()),
                    contactRelatedAddressId,
                    toUserId,
                    valueAsInteger(form.getStateId()),
                    valueAsInteger(form.getOpenDate()),
                    valueAsInteger(form.getWorkLevelId()),
                    valueAsInteger(form.getExpireDate()),
                    form.getDescription());

            if (!success) {
                return composeError(Messages.i.getMessage("error.supportCase.create"));
            }
        }

        return OK;
    }


    private String composeError(String message) {
        return FAIL + "\n" + message;
    }

    private Integer valueAsInteger(String value) {
        Integer intValue = null;
        if (value != null) {
            try {
                intValue = new Integer(value.trim());
            } catch (NumberFormatException e) {
                intValue = null;
            }
        }
        return intValue;
    }

    private boolean isIntegerValue(String value) {
        return valueAsInteger(value) != null;
    }
}
