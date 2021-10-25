package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.catalogmanager.LangText;
import com.piramide.elwis.domain.catalogmanager.LangTextHome;
import com.piramide.elwis.domain.contactmanager.Telecom;
import com.piramide.elwis.domain.contactmanager.TelecomHome;
import com.piramide.elwis.domain.contactmanager.TelecomType;
import com.piramide.elwis.domain.contactmanager.TelecomTypeHome;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L.
 * Read all telecoms numbers connected with \n character, this is to contact summary report
 *
 * @author Miky
 * @version $Id: ReadAddressTelecomNumberCmd.java 17-jul-2008 15:07:00 $
 */
public class ReadAddressTelecomNumberCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing ReadAddressTelecomNumberCmd....." + paramDTO);

        boolean isContactPerson = ("isContactPerson".equals(getOp()));

        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        String isoLanguage = (String) paramDTO.get("isoLanguage");
        Integer addressId = new Integer(paramDTO.get("addressId").toString());
        Integer contactPersonId = null;
        if (isContactPerson) {
            contactPersonId = new Integer(paramDTO.get("contactPersonId").toString());
        }

        TelecomTypeHome telecomTypeHome = (TelecomTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TELECOMTYPE);
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

        Collection telecomTypes = null;
        try {
            telecomTypes = telecomTypeHome.findByCompanyIdOrderBySequence(companyId);
        } catch (FinderException e) {
            log.debug("Not found telecom types..." + e);
            telecomTypes = new ArrayList();
        }

        Map<String, String> addressNumbersMap = new HashMap<String, String>();
        Map<String, String> telecomTypeNameMap = new HashMap<String, String>();

        for (Iterator iterator = telecomTypes.iterator(); iterator.hasNext();) {
            TelecomType telecomType = (TelecomType) iterator.next();
            Collection telecoms;
            try {
                if (isContactPerson) {
                    telecoms = telecomHome.findAllContactPersonTelecomsByTypeId(addressId, contactPersonId, telecomType.getTelecomTypeId());
                } else {
                    telecoms = telecomHome.findAllAddressTelecomsByTypeId(addressId, telecomType.getTelecomTypeId());
                }
            } catch (FinderException e) {
                log.debug("Not found telecoms " + e);
                telecoms = new ArrayList();
            }

            String allTelecomNumber = "";
            for (Iterator iterator2 = telecoms.iterator(); iterator2.hasNext();) {
                Telecom telecom = (Telecom) iterator2.next();
                allTelecomNumber += telecom.getData();

                if (iterator2.hasNext()) {
                    allTelecomNumber += "\n";
                }
            }

            addressNumbersMap.put(telecomType.getTelecomTypeId().toString(), allTelecomNumber);
            telecomTypeNameMap.put(telecomType.getTelecomTypeId().toString(), readTelecomTypeName(telecomType, isoLanguage));
        }

        resultDTO.put("addressNumberMap", addressNumbersMap);
        resultDTO.put("telecomTypeNameMap", telecomTypeNameMap);
    }

    private String readTelecomTypeName(TelecomType telecomType, String isoLanguage) {
        LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

        String telecomTypeName;
        if (telecomType.getLangTextId() != null && isoLanguage != null) {
            try {
                LangText langText = langTextHome.findByLangTextIdAndLanguageRelatedToUI(telecomType.getLangTextId(), isoLanguage);
                telecomTypeName = (langText.getText() != null && !"".equals(langText.getText().trim())) ? langText.getText() : telecomType.getTelecomTypeName();
            } catch (FinderException e) {
                telecomTypeName = telecomType.getTelecomTypeName();
            }
        } else {
            telecomTypeName = telecomType.getTelecomTypeName();
        }
        return telecomTypeName;
    }


    public boolean isStateful() {
        return false;
    }
}
