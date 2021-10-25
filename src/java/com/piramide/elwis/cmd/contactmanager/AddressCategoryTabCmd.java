package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryTabManagerCmd;
import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.utils.ContactConstants;

import javax.ejb.EJBLocalObject;
import javax.ejb.SessionContext;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class AddressCategoryTabCmd extends CategoryTabManagerCmd {
    @Override
    protected EJBLocalObject findEJBLocalObject(SessionContext ctx) {
        Integer addressId = EJBCommandUtil.i.getValueAsInteger(this, "addressId");

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPrimKey(addressId);

        return ExtendedCRUDDirector.i.read(addressDTO, resultDTO, true);
    }

    @Override
    protected String getValuesFinderMethodName() {
        return "findValueByAddressId";
    }

    @Override
    protected String getEntityFinderMethodName() {
        return "findByAddressId";
    }

    @Override
    protected Integer getEntityId(EJBLocalObject ejbLocalObject, SessionContext ctx) {
        return ((Address) ejbLocalObject).getAddressId();
    }

    @Override
    protected Integer getCompanyId(EJBLocalObject ejbLocalObject, SessionContext ctx) {
        return ((Address) ejbLocalObject).getCompanyId();
    }

    @Override
    protected String getEntityIdFieldName() {
        return "addressId";
    }

    @Override
    protected void setTabValidationFailureForward(EJBLocalObject ejbLocalObject) {
        if (ContactConstants.ADDRESSTYPE_PERSON.equals(((Address) ejbLocalObject).getAddressType())) {
            resultDTO.setForward("PersonDetail");
        }
        if (ContactConstants.ADDRESSTYPE_ORGANIZATION.equals(((Address) ejbLocalObject).getAddressType())) {
            resultDTO.setForward("OrganizationDetail");
        }
    }

    @Override
    public boolean isStateful() {
        return false;
    }
}
