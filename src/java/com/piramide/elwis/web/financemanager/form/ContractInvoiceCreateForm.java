package com.piramide.elwis.web.financemanager.form;

import com.piramide.elwis.dto.contactmanager.CompanyDTO;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.SystemLanguage;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: ContractInvoiceCreateForm.java 20-nov-2008 16:22:47 $
 */
public class ContractInvoiceCreateForm extends DefaultForm {
    private Log log = LogFactory.getLog(this.getClass());

    public Object[] getContractIds() {
        List list = (List) this.getDto("idsContract");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setContractIds(Object[] checkArray) {
        if (checkArray != null) {
            this.setDto("idsContract", Arrays.asList(checkArray));
        }
    }

    public Object[] getSalePositionIds() {
        List list = (List) this.getDto("idsSalePosition");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setSalePositionIds(Object[] checkArray) {
        if (checkArray != null) {
            this.setDto("idsSalePosition", Arrays.asList(checkArray));
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate ContractInvoiceCreateForm......." + getDtoMap());
        ActionErrors errors = new ActionErrors();

        if (getDto("idsContract") == null) {
            setDto("idsContract", new ArrayList());
        }

        if (getDto("idsSalePosition") == null) {
            setDto("idsSalePosition", new ArrayList());
        }

        //add in request contract ids, sale position ids to rewrite
        request.setAttribute("contractIdsList", (List) getDto("idsContract"));
        request.setAttribute("salePositionIdsList", (List) getDto("idsSalePosition"));


        errors = super.validate(mapping, request);

        if (errors.isEmpty()) {
            setParametersInDTO(request);
        }
        return errors;
    }

    private void setParametersInDTO(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        setDto("userId", user.getValue("userId"));
        setDto("userAddressId", user.getValue("userAddressId"));
        setDto("locale", user.getValue("locale"));

        Map textResourcesMap = new HashMap();
        textResourcesMap.put("textForCustomer", textResourcesByIsoLanguage("InvoicePosition.text.forCustomer"));
        textResourcesMap.put("textTimePeriod", textResourcesByIsoLanguage("InvoicePosition.text.timePeriod"));
        textResourcesMap.put("textContractFrom", textResourcesByIsoLanguage("InvoicePosition.text.contractFrom"));
        textResourcesMap.put("textDiscount", textResourcesByIsoLanguage("InvoicePosition.text.discount"));
        textResourcesMap.put("textDiscountPeriod", textResourcesByIsoLanguage("InvoicePosition.text.discountPeriod"));
        textResourcesMap.put("textPartialPayment", textResourcesByIsoLanguage("InvoicePosition.text.partialPayment"));

        setDto("textResourcesMap", textResourcesMap);

        //resource message for invoice title
        setDto("invoiceTitleMessage", JSPHelper.getMessage(request, "Invoice.titleMessage"));

    }

    private void updateSequenceRule(HttpServletRequest request) {
        String type = (String) getDto("type");
        if (GenericValidator.isBlankOrNull(type)) {
            return;
        }

        CompanyDTO companyDTO =
                com.piramide.elwis.web.contactmanager.el.Functions.getCompanyConfiguration(request);

        if (FinanceConstants.InvoiceType.Invoice.getConstantAsString().equals(type)) {
            setDto("sequenceRuleId", companyDTO.get("sequenceRuleIdForInvoice"));
        }

        if (FinanceConstants.InvoiceType.CreditNote.getConstantAsString().equals(type)) {
            setDto("sequenceRuleId", companyDTO.get("sequenceRuleIdForCreditNote"));
        }

    }

    private Map textResourcesByIsoLanguage(String resourceKey) {
        Map resourcesMap = new HashMap();
        for (Iterator iterator = SystemLanguage.systemLanguages.keySet().iterator(); iterator.hasNext();) {
            String isoLangKey = (String) iterator.next();
            Locale locale = new Locale(isoLangKey);
            resourcesMap.put(isoLangKey, JSPHelper.getMessage(locale, resourceKey));
        }
        return resourcesMap;
    }
}

