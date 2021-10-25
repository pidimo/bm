package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryTabCmd;
import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.ContactPerson;
import com.piramide.elwis.domain.contactmanager.ContactPersonPK;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.ContactPersonDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;

import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ContactPersonCategoryFieldValueCmd extends EJBCommand {

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;
        String op = getOp();
        if ("update".equals(op)) {
            isRead = false;
            update(ctx);
        }
        if (isRead) {
            read(ctx);
        }
    }

    private void update(SessionContext ctx) {
        //update the contact person data
        ContactPersonPK pK = new ContactPersonPK(new Integer(paramDTO.get("addressId").toString()),
                new Integer(paramDTO.get("contactPersonId").toString()));

        paramDTO.remove("addressId"); //this invalid copy of primary key into Bean in update operation
        paramDTO.remove("contactPersonId"); //this invalid copy of primary key into Bean in update operation
        ContactPersonDTO contactPersonDTO = new ContactPersonDTO();
        contactPersonDTO.setPrimKey(pK);


        ContactPerson contactPerson = (ContactPerson) ExtendedCRUDDirector.i.read(contactPersonDTO, resultDTO, false);

        if (null == contactPerson) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.put("addressId", pK.contactPersonId);
            ExtendedCRUDDirector.i.read(addressDTO, resultDTO, false);
            resultDTO.setForward("Detail");
            return;
        }


        CategoryTabCmd categoryTabCmd = new CategoryTabCmd();
        categoryTabCmd.setOp("checkCategoryTab");
        categoryTabCmd.putParam("finderName", "findValueByContactPersonId");
        categoryTabCmd.putParam("params", new Object[]{contactPerson.getAddressId(),
                contactPerson.getContactPersonId()});
        categoryTabCmd.putParam("categoryTabId", Integer.valueOf(paramDTO.get("categoryTabId").toString()));
        categoryTabCmd.executeInStateless(ctx);
        if (null != categoryTabCmd.getResultDTO().get("errorMessage")) {
            resultDTO.addResultMessage(categoryTabCmd.getResultDTO().get("errorMessage").toString());
            resultDTO.setForward("Detail");
            return;
        }


        String finderName = "findByAddressIdAndContactPersonId";
        Object[] params = new Object[]{contactPerson.getAddressId(),
                contactPerson.getContactPersonId(),
                contactPerson.getCompanyId()};
        List paramsAsList = Arrays.asList(params);

        List<Map> sourceValues = new ArrayList<Map>();
        Map m1 = new HashMap();
        m1.put("identifier", "addressId");
        m1.put("value", contactPerson.getAddressId());
        sourceValues.add(m1);
        Map m2 = new HashMap();
        m2.put("identifier", "contactPersonId");
        m2.put("value", contactPerson.getContactPersonId());
        sourceValues.add(m2);

        CategoryUtilCmd myCmd = new CategoryUtilCmd();
        myCmd.putParam("sourceValues", sourceValues);
        myCmd.putParam("companyId", contactPerson.getCompanyId());
        myCmd.putParam(paramDTO);
        myCmd.putParam("finderName", finderName);
        myCmd.putParam("params", paramsAsList);
        myCmd.setOp("updateValues");
        myCmd.executeInStateless(ctx);

        resultDTO.put("op", "read");
        if ("Fail".equals(myCmd.getResultDTO().getForward())) {
            resultDTO.setForward("Fail");
            resultDTO.addResultMessage("Common.error.concurrency");
            return;
        }

        resultDTO.addResultMessage("Common.changesOK");
    }

    private void read(SessionContext ctx) {
        ContactPersonPK pK = new ContactPersonPK(new Integer(paramDTO.get("addressId").toString()),
                new Integer(paramDTO.get("contactPersonId").toString()));

        paramDTO.remove("addressId"); //this invalid copy of primary key into Bean in update operation
        paramDTO.remove("contactPersonId"); //this invalid copy of primary key into Bean in update operation
        ContactPersonDTO contactPersonDTO = new ContactPersonDTO();
        contactPersonDTO.setPrimKey(pK);

        ContactPerson contactPerson = (ContactPerson) ExtendedCRUDDirector.i.read(contactPersonDTO, resultDTO, false);

        if (null == contactPerson) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.put("addressId", pK.contactPersonId);
            ExtendedCRUDDirector.i.read(addressDTO, resultDTO, false);
            resultDTO.setForward("Detail");
            return;
        }

        CategoryTabCmd categoryTabCmd = new CategoryTabCmd();
        categoryTabCmd.setOp("checkCategoryTab");
        categoryTabCmd.putParam("finderName", "findValueByContactPersonId");
        categoryTabCmd.putParam("params", new Object[]{contactPerson.getAddressId(),
                contactPerson.getContactPersonId()});
        categoryTabCmd.putParam("categoryTabId", Integer.valueOf(paramDTO.get("categoryTabId").toString()));
        categoryTabCmd.executeInStateless(ctx);
        if (null != categoryTabCmd.getResultDTO().get("errorMessage")) {
            resultDTO.addResultMessage(categoryTabCmd.getResultDTO().get("errorMessage").toString());
            resultDTO.setForward("Detail");
            return;
        }

        // read contactperson categoryfieldValues
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
