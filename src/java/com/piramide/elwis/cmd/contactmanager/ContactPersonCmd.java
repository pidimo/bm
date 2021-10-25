package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.catalogmanager.TelecomTypeSelectCmd;
import com.piramide.elwis.domain.contactmanager.ContactPerson;
import com.piramide.elwis.domain.contactmanager.ContactPersonPK;
import com.piramide.elwis.domain.contactmanager.Telecom;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.ContactPersonDTO;
import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.dto.contactmanager.TelecomWrapperDTO;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.*;

/**
 * Execute business logic for read contact person.
 *
 * @author Fernando Montaño
 * @version $Id: ContactPersonCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class ContactPersonCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing command with Operation = " + getOp());
        log.debug("contactPersonId to read = " + paramDTO.get("contactPersonId"));
        log.debug("addressId container of before contactpersonid= " + paramDTO.get("addressId"));

        if (paramDTO.get("withReferences") != null) {// checking if is referenced
            log.debug("with references = true");
            ContactPersonDTO dto = new ContactPersonDTO(paramDTO);
            IntegrityReferentialChecker.i.check(dto, resultDTO);
        }
        if (resultDTO.isFailure()) {
            return;
        }
        ContactPersonPK pK = new ContactPersonPK(new Integer(paramDTO.get("addressId").toString()),
                new Integer(paramDTO.get("contactPersonId").toString()));
        ContactPersonDTO contactPersonDTO = new ContactPersonDTO(paramDTO);
        contactPersonDTO.setPrimKey(pK);
        ContactPerson contactPerson = (ContactPerson) CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, contactPersonDTO, resultDTO);

        if (resultDTO.isFailure()) { // if contact person has been deleted by other user
            return;
        }

        Collection telecoms = contactPerson.getTelecoms();
        Map telecomMap = new LinkedHashMap();
        TelecomDTO telecomDTO = null;
        for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
            Telecom telecom = (Telecom) iterator.next();
            TelecomTypeSelectCmd cmd = new TelecomTypeSelectCmd();
            cmd.putParam(TelecomTypeSelectCmd.SELECT_TYPE, TelecomTypeSelectCmd.TYPE_SINGLE);
            cmd.putParam("companyId", telecom.getCompanyId());
            cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, paramDTO.get("locale"));
            cmd.putParam("telecomTypeId", telecom.getTelecomTypeId());
            cmd.executeInStateless(ctx);
            TelecomTypeDTO telecomTypeDTO = (TelecomTypeDTO) cmd.getResultDTO().get(TelecomTypeSelectCmd.RESULT);
            telecomDTO = new TelecomDTO(telecom.getTelecomId().toString(), telecom.getData(),
                    telecom.getDescription(), telecom.getPredetermined().booleanValue());
            TelecomWrapperDTO.addToMapTelecomDTO(telecomMap, telecomDTO, telecomTypeDTO);
        }

        resultDTO.put("contactPersonTelecomMap", telecomMap);
        resultDTO.put("addressId", contactPerson.getContactPerson().getAddressId());
        CRUDDirector.i.read(new AddressDTO(resultDTO), resultDTO);

        // restoring addressId of address that contact person belongs.
        resultDTO.put("addressId", contactPerson.getAddressId());

        if (resultDTO.get("birthday") != null) {
            if (resultDTO.get("birthday").toString().length() <= 5) {
                resultDTO.put("dateWithoutYear", "true");
            }
        }

        if (contactPerson.getContactPerson().getFreeText() != null &&
                contactPerson.getContactPerson().getFreeText().getValue() != null) {
            resultDTO.put("note", new String(contactPerson.getContactPerson().getFreeText().getValue()));
        }

        resultDTO.put("privateVersion", resultDTO.get("version"));

        resultDTO.put("isActive", contactPerson.getActive());
        resultDTO.put("version", contactPerson.getVersion());

        // read contacperson categoryfieldValues
        String finderName = "findByAddressIdAndContactPersonId";
        Object[] params = new Object[]{contactPerson.getAddressId(),
                contactPerson.getContactPersonId(),
                contactPerson.getCompanyId()};

        List paramsAsList = Arrays.asList(params);

        CategoryUtilCmd myCmd = new CategoryUtilCmd();
        myCmd.putParam("finderName", finderName);
        myCmd.putParam("params", paramsAsList);
        myCmd.setOp("readCAtegoryFieldValues");
        myCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = myCmd.getResultDTO();
        resultDTO.putAll(myResultDTO);

    }

    public boolean isStateful() {
        return false;
    }
}
