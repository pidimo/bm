package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.contactmanager.ContactPerson;
import com.piramide.elwis.domain.contactmanager.ContactPersonHome;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public class LightlyContactPersonCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing LightlyContactPersonCmd..." + paramDTO);

        if ("getBySearchNamePersonType".equals(this.getOp())) {
            getBySearchNamePersonType();
        } else if ("getBySearchNameNotPersonType".equals(this.getOp())) {
            getBySearchNameNotPersonType();
        }
    }

    private void getBySearchNamePersonType() {
        String searchName = (String) paramDTO.get("searchName");
        Integer personTypeId = EJBCommandUtil.i.getValueAsInteger(this, "personTypeId");
        Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");

        ContactPersonHome contactPersonHome = (ContactPersonHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTPERSON);
        try {
            ContactPerson contactPerson = contactPersonHome.findBySearchNameAndPersonType(searchName, personTypeId, companyId);
            if (contactPerson != null) {
                DTOFactory.i.copyToDTO(contactPerson, resultDTO);
            }
        } catch (FinderException e) {
            resultDTO.setResultAsFailure();
            log.debug("Error in find contact person... ", e);
        }
    }

    private void getBySearchNameNotPersonType() {
        String searchName = (String) paramDTO.get("searchName");
        Integer personTypeId = EJBCommandUtil.i.getValueAsInteger(this, "personTypeId");
        Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");

        ContactPersonHome contactPersonHome = (ContactPersonHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTPERSON);
        try {
            ContactPerson contactPerson = contactPersonHome.findBySearchNameAndNotPersonType(searchName, personTypeId, companyId);
            if (contactPerson != null) {
                DTOFactory.i.copyToDTO(contactPerson, resultDTO);
            }
        } catch (FinderException e) {
            resultDTO.setResultAsFailure();
            log.debug("Error in find contact person... ", e);
        }
    }

    public boolean isStateful() {
        return false;
    }
}
