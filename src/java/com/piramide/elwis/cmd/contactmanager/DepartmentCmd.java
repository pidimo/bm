package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.ContactPerson;
import com.piramide.elwis.domain.contactmanager.ContactPersonPK;
import com.piramide.elwis.domain.contactmanager.Department;
import com.piramide.elwis.dto.contactmanager.ContactPersonDTO;
import com.piramide.elwis.dto.contactmanager.DepartmentDTO;
import com.piramide.elwis.utils.HashMapCleaner;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import com.piramide.elwis.utils.VersionControlChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Deparment Comand, bussines Logic encarge to manage Departments
 * Create, Update, Delete an Read operations
 *
 * @author Titus
 * @version DepartmentCmd.java, v 2.0 May 7, 2004 Time: 11:34:00 AM
 */
public class DepartmentCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        HashMapCleaner.clean(paramDTO);
        log.debug("Executing DepartmentCmd");
        /* Chek References from list departments layaout*/
        if (paramDTO.getAsBool("withReferences")) {
            IntegrityReferentialChecker.i.check(new DepartmentDTO(paramDTO), resultDTO);
            if (resultDTO.isFailure()) {
                return;
            }
        }
        /* Delete operation from update layout*/
        if (paramDTO.get("delete") != null || "delete".equals(getOp())) {
            DepartmentDTO dto = new DepartmentDTO(paramDTO);

            ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_DELETE, dto, resultDTO, false, false, true, false);// if Ok delete department
            if (resultDTO.isFailure()) { // if operayion fail
                resultDTO.setForward("Fail");
                return;
            }
            resultDTO.setForward("Success");
            return;
        }
        if ("update".equals(getOp())) {
            log.debug("update data");
            DepartmentDTO dto = new DepartmentDTO(paramDTO);
            VersionControlChecker.i.check(dto, resultDTO, paramDTO);
            if (resultDTO.get("EntityBeanNotFound") != null) { // bean not found in version control
                dto.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
                return;
            }
            if (resultDTO.isFailure()) {// if version control number not are equals, therefore only read again and return
                log.debug("version control checker has report an concurrent modification, reading department again");
                resultDTO.put("versionCheck", "ok");
                resultDTO.setForward("VersionUpdate"); // ready to read bean again
                Department department = (Department) CRUDDirector.i.read(dto, resultDTO); // read
                if (resultDTO.isFailure()) {
                    resultDTO.setForward("Fail"); // if department was deleted by other user
                    return;
                }
                resultDTO.put("departmentId", department.getDepartmentId());
                resultDTO.put("name", department.getName());
                resultDTO.put("managerId", department.getManagerId());
                CRUDDirector.i.read(new DepartmentDTO(resultDTO), resultDTO);
                paramDTO.setOp(CRUDDirector.OP_READ);
                return; // return
            }


            if (paramDTO.get("parentId") != null) {

                if ((paramDTO.get("parentId").toString()).equals(paramDTO.get("departmentId").toString())) {
                    paramDTO.put("parentId", null);
                }
            }


            dto.put("version", paramDTO.get("version"));
            //CRUDDirector.i.doCRUD(CRUDDirector.OP_UPDATE,dto,resultDTO);
            log.debug("dto update: " + dto);
            Department department = (Department) CRUDDirector.i.doCRUD(CRUDDirector.OP_UPDATE, dto, resultDTO);
            if (resultDTO.isFailure()) { // if bean was removed
                resultDTO.setForward("Fail"); // if address has been deleted by other user
                return;
            }
            if (department != null) {
                /*if(paramDTO.get("managerId")!=null){
                    ContactPersonPK pK = new ContactPersonPK(new Integer(paramDTO.getAsInt("organizationId")),
                            new Integer(paramDTO.get("managerId").toString()));
                    ContactPersonDTO contactPersonDTO = new ContactPersonDTO();
                    contactPersonDTO.setPrimKey(pK);

                    ContactPerson contactPerson = (ContactPerson) CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, contactPersonDTO,
                            resultDTO);
                    if (resultDTO.isFailure()) { // if bean was removed
                        resultDTO.setForward("Fail"); // if address has been deleted by other user
                        return;
                    }
                    department.setContactPerson(contactPerson);
                }*/
                if (department.getParentId() != null && paramDTO.get("parentId") == null) {
                    department.setParent(null);
                }
                if (department.getManagerId() != null && paramDTO.get("managerId") == null) {
                    department.setManagerId(null);
                }

                CRUDDirector.i.read(dto, resultDTO); // read updated values
            }
            return;

        }
        if ("create".equals(getOp())) {
            log.debug("create data");
            ContactPersonPK pK = null;
            ContactPersonDTO contactPersonDTO = new ContactPersonDTO();
            if (paramDTO.get("managerId") != null) {
                pK = new ContactPersonPK(new Integer(paramDTO.getAsInt("organizationId")),
                        new Integer(paramDTO.get("managerId").toString()));

                contactPersonDTO.setPrimKey(pK);
                paramDTO.put("managerId", null);
                CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, contactPersonDTO, resultDTO);
                if (resultDTO.isFailure()) { // if bean was removed
                    resultDTO.setForward("Fail"); // if address has been deleted by other user
                    return;
                }
            }
            Department department = (Department) CRUDDirector.i.doCRUD(getOp(), new DepartmentDTO(paramDTO), resultDTO);
            if (pK != null) {
                ContactPerson contactPerson = (ContactPerson) CRUDDirector.i.doCRUD(CRUDDirector.OP_READ,
                        contactPersonDTO, resultDTO);
                department.setContactPerson(contactPerson);
            }
            return;
        }

        CRUDDirector.i.doCRUD(getOp(), new DepartmentDTO(paramDTO), resultDTO);


    }

    public boolean isStateful() {
        return false;
    }
}
