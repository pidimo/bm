package com.piramide.elwis.cmd.utils;

import com.piramide.elwis.domain.catalogmanager.Template;
import com.piramide.elwis.domain.catalogmanager.TemplateText;
import com.piramide.elwis.domain.catalogmanager.TemplateTextHome;
import com.piramide.elwis.domain.catalogmanager.TemplateTextPK;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import java.util.ArrayList;
import java.util.List;

/**
 * Util to work with templates
 *
 * @author Miky
 * @version $Id: DocumentTemplateUtil.java 2009-05-19 05:45:40 PM $
 */
public class DocumentTemplateUtil {
    private static Log log = LogFactory.getLog(DocumentTemplateUtil.class);

    public static Integer getDefaultLanguageId(Address address, Address contactPerson, Address company, Address employee) {
        Integer defaultLang = null;
        log.debug("getDefaultLang:");
        if (contactPerson != null && contactPerson.getLanguageId() != null) {
            defaultLang = contactPerson.getLanguageId();
        } else if (address.getLanguageId() != null) {
            defaultLang = address.getLanguageId();
        } else if (company.getLanguageId() != null) {
            defaultLang = company.getLanguageId();
        } else if (employee.getLanguageId() != null) {
            defaultLang = employee.getLanguageId();
        }
        return defaultLang;
    }

    public static Integer getDefaultLanguageId(Integer addressId, Integer contactPersonId, Integer companyId, Integer employeeId) {

        Address contactPerson = findAddress(contactPersonId);
        if (contactPerson != null && contactPerson.getLanguageId() != null) {
            return contactPerson.getLanguageId();
        }

        Address address = findAddress(addressId);
        if (address != null && address.getLanguageId() != null) {
            return address.getLanguageId();
        }

        Address company = findAddress(companyId);
        if (company != null && company.getLanguageId() != null) {
            return company.getLanguageId();
        }

        Address employee = findAddress(employeeId);
        if (employee != null && employee.getLanguageId() != null) {
            return employee.getLanguageId();
        }

        return null;
    }

    public static TemplateText getTemplateTextByLanguage(Template template, Address address, Address contactPerson, Address currentUser, Address company) {

        log.debug("[calculateAddressByLanguage]:Init");

        TemplateText templateText = null;
        if (template.getTemplateText().size() > 1) {
            log.debug("call:getDefaultLang");
            Integer defaultIdLang = getDefaultLanguageId(address, contactPerson, currentUser, company);
            log.debug("DefaultLang:" + defaultIdLang);

            TemplateTextHome templateTextHome = (TemplateTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TEMPLATETEXT);
            try {
                templateText = templateTextHome.findByPrimaryKey(new TemplateTextPK(defaultIdLang, template.getTemplateId()));
            } catch (FinderException e) {
            }
            if (templateText == null) {
                try {
                    templateText = templateTextHome.findDefaultTemplate(template.getTemplateId());
                } catch (FinderException e) {
                }
                if (templateText == null) {
                    try {
                        templateText = templateTextHome.findByPrimaryKey(new TemplateTextPK(company.getLanguageId(), template.getTemplateId()));
                    } catch (FinderException e) {
                    }
                    if (templateText == null) {
                        try {
                            templateText = templateTextHome.findByPrimaryKey(new TemplateTextPK(currentUser.getLanguageId(), template.getTemplateId()));
                        } catch (FinderException e) {
                        }
                        if (templateText == null) {
                            List templates = new ArrayList(template.getTemplateText());
                            templateText = (TemplateText) templates.get(0);
                        }
                    }
                }
            }
        } else {
            log.debug("Only one template..");
            templateText = (TemplateText) (new ArrayList(template.getTemplateText())).get(0);
        }
        log.debug("TemplateTextResultant:" + templateText);
        return templateText;
    }

    private static Address findAddress(Integer addressId) {
        Address address = null;
        if (addressId != null) {
            AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
            try {
                address = addressHome.findByPrimaryKey(addressId);
            } catch (FinderException e) {
                log.debug("Not found address with id:" + addressId, e);
            }
        }
        return address;
    }


}
