package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.catalogmanager.WebDocumentCmd;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.ReadCategoryDocumentValues;
import com.piramide.elwis.cmd.common.ReadTemplateValues;
import com.piramide.elwis.cmd.utils.DocumentTemplateUtil;
import com.piramide.elwis.cmd.utils.VariableConstants;
import com.piramide.elwis.domain.contactmanager.AdditionalAddress;
import com.piramide.elwis.domain.contactmanager.AdditionalAddressHome;
import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.dto.catalogmanager.WebParameterDTO;
import com.piramide.elwis.dto.catalogmanager.WebParameterWrapperDTO;
import com.piramide.elwis.dto.contactmanager.ContactDTO;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cmd to execute the web document. Read all variable values
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class ContactWebDocumentExecuteCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ContactWebDocumentExecuteCmd..........." + paramDTO);

        Integer contactId = Integer.valueOf(paramDTO.get("contactId").toString());
        Integer userAddressId = Integer.valueOf(paramDTO.get("userAddressId").toString());
        String requestLocale = paramDTO.get("requestLocale").toString();

        Contact contact = (Contact) ExtendedCRUDDirector.i.read(new ContactDTO(contactId), resultDTO, false);

        if (contact != null && contact.getWebDocumentId() != null) {

            Map variableValuesMap = processAndGetVariableValues(contact, userAddressId, requestLocale);
            readWebParameters(contact.getWebDocumentId(), ctx);

            resultDTO.put("mapVariableValues", variableValuesMap);
        }
    }

    private Map processAndGetVariableValues(Contact contact, Integer userAddressId, String requestLocale) {
        Map result = new HashMap();

        boolean withSalesProcess = false;
        Integer laguageId = getDefaultLanguageId(contact);

        try {
            ReadTemplateValues readTemplateValues = new ReadTemplateValues(contact.getCompanyId(), userAddressId, contact.getEmployeeId(), false, withSalesProcess, requestLocale, false);
            if (contact.getAdditionalAddressId() != null) {
                readTemplateValues.setAdditionalAddress(findAdditionalAddress(contact.getAdditionalAddressId()));
            }

            result = readTemplateValues.getFieldValuesAsMap(contact.getAddressId(), contact.getContactPersonId(), laguageId, null);

            //add category field values
            ReadCategoryDocumentValues readCategoryDocumentValues = new ReadCategoryDocumentValues(contact.getCompanyId(), readTemplateValues.getLocale());
            Map categoryValuesMap = readCategoryDocumentValues.getAddressCategoryValues(contact.getAddressId(), VariableConstants.FIELD_CATEGORY_ADDRESS_PREFIX);

            result.putAll(categoryValuesMap);

        } catch (FinderException e) {
            log.warn("Error when calaculate values for bm variables...", e);
            result = new HashMap();
        }

        return result;
    }

    private Integer getDefaultLanguageId(Contact contact) {
        return DocumentTemplateUtil.getDefaultLanguageId(contact.getAddressId(), contact.getContactPersonId(), contact.getCompanyId(), contact.getEmployeeId());
    }

    private void readWebParameters(Integer webDocumentId, SessionContext ctx) {

        WebDocumentCmd webDocumentCmd = new WebDocumentCmd();
        webDocumentCmd.setOp("read");
        webDocumentCmd.putParam("webDocumentId", webDocumentId);

        webDocumentCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = webDocumentCmd.getResultDTO();

        if (myResultDTO.get("webParameterWrapper") != null) {
            WebParameterWrapperDTO wrapperDTO = (WebParameterWrapperDTO) myResultDTO.get("webParameterWrapper");
            List<WebParameterDTO> webParameterDTOList = wrapperDTO.getAllWebParameters();

            resultDTO.put("listWebParameterDTO", webParameterDTOList);
            resultDTO.put("webDocumentMap", myResultDTO);
        }
    }

    private AdditionalAddress findAdditionalAddress(Integer additionalAddressId) {
        AdditionalAddress additionalAddress = null;
        AdditionalAddressHome additionalAddressHome = (AdditionalAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDITIONALADDRESS);

        if (additionalAddressId != null) {
            try {
                additionalAddress = additionalAddressHome.findByPrimaryKey(additionalAddressId);
            } catch (FinderException e) {
                log.debug("Not found additional address " + additionalAddressId, e);
            }
        }
        return additionalAddress;
    }

    public boolean isStateful() {
        return false;
    }
}
