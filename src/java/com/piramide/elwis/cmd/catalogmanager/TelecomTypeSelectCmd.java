package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.catalogmanager.LangText;
import com.piramide.elwis.domain.catalogmanager.LangTextHome;
import com.piramide.elwis.domain.contactmanager.TelecomType;
import com.piramide.elwis.domain.contactmanager.TelecomTypeHome;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Select a single telecomtype  or a list ot telecomtypes given a language iso
 *
 * @author Fernando Montaño
 * @version $Id: TelecomTypeSelectCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class TelecomTypeSelectCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(TelecomTypeSelectCmd.class);
    public static final String SELECT_TYPE = "type";
    /**
     * If type is single return a telecomtypeDTO
     */
    public static final String TYPE_SINGLE = "one";
    /**
     * If type is list return a LinkedList of telecomtypeDTO's
     */
    public static final String TYPE_LIST = "list";
    public static final String ISO_LANGUAGE = "languageIso";
    public static final String RESULT = "result";
    public static final String TYPE = "typeTelecom";

    public void executeInStateless(SessionContext ctx) {
        log.debug("Select TelecomType [type= " + paramDTO.get(SELECT_TYPE) + ", language=" + paramDTO.get(ISO_LANGUAGE)
                + ", companyId=" + paramDTO.get("companyId") + ", telecomTypeId=" + paramDTO.get("telecomTypeId") + "]");

        TelecomTypeHome telecomTypeHome = (TelecomTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TELECOMTYPE);
        LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

        TelecomTypeDTO telecomTypeDTO = null;
        //select list of telecoms
        if (TYPE_LIST.equals((String) paramDTO.get(SELECT_TYPE))) {
            List result = new LinkedList();

            try {
                Collection telecomTypes;
                if (paramDTO.get(TYPE) != null) {
                    telecomTypes = telecomTypeHome.findByCompanyIdAndTypeOrderBySequence(Integer.valueOf(paramDTO.get("companyId").toString()), paramDTO.get(TYPE).toString());
                } else {
                    telecomTypes = telecomTypeHome.findByCompanyIdOrderBySequence(Integer.valueOf(paramDTO.get("companyId").toString()));
                }

                for (Iterator iterator = telecomTypes.iterator(); iterator.hasNext();) {
                    TelecomType telecomType = (TelecomType) iterator.next();
                    telecomTypeDTO = new TelecomTypeDTO();
                    telecomTypeDTO.put("telecomTypeId", telecomType.getTelecomTypeId());
                    readTelecomTypeName(telecomType, telecomTypeDTO, (String) paramDTO.get(ISO_LANGUAGE), langTextHome);
                    telecomTypeDTO.put("telecomTypeType", telecomType.getType());
                    telecomTypeDTO.put("telecomTypePosition", telecomType.getPosition() != null ? telecomType.getPosition() : "0");
                    result.add(telecomTypeDTO);
                }
            } catch (FinderException e) {
                //it lets empty result, no telecomtypes found for company
            }
            resultDTO.put(RESULT, result);

        } else if (TYPE_SINGLE.equals((String) paramDTO.get(SELECT_TYPE))) {

            try {
                TelecomType telecomType = telecomTypeHome.findByPrimaryKey(Integer.valueOf(paramDTO.get("telecomTypeId").toString()));
                telecomTypeDTO = new TelecomTypeDTO();
                telecomTypeDTO.put("telecomTypeId", telecomType.getTelecomTypeId());
                readTelecomTypeName(telecomType, telecomTypeDTO, (String) paramDTO.get(ISO_LANGUAGE), langTextHome);
                telecomTypeDTO.put("telecomTypeType", telecomType.getType());
                telecomTypeDTO.put("telecomTypePosition", telecomType.getPosition() != null ? telecomType.getPosition() : "0");
                resultDTO.put(RESULT, telecomTypeDTO);
            } catch (FinderException e) {
                log.debug("Telecomtypeid was not found, so it was deleted by other user.");
                resultDTO.put(RESULT, null);
            }
        }
    }

    private void readTelecomTypeName(TelecomType telecomType, TelecomTypeDTO telecomTypeDTO, String isoLanguage,
                                     LangTextHome langTextHome) {

        if (telecomType.getLangTextId() != null && isoLanguage != null) {
            try {
                LangText langText = langTextHome.findByLangTextIdAndLanguageRelatedToUI(telecomType.getLangTextId(), isoLanguage);
                telecomTypeDTO.put("telecomTypeName", (langText.getText() != null && !"".equals(langText.getText().trim()))
                        ? langText.getText() : telecomType.getTelecomTypeName());
            } catch (FinderException e) {
                telecomTypeDTO.put("telecomTypeName", telecomType.getTelecomTypeName());
            }
        } else {
            telecomTypeDTO.put("telecomTypeName", telecomType.getTelecomTypeName());
        }
    }

    public boolean isStateful() {
        return false;
    }
}