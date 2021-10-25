package com.piramide.elwis.cmd.common;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.DuplicatedEntryChecker;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import net.java.dev.strutsejb.dto.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJBLocalObject;
import java.lang.reflect.Method;

/**
 * Provides common business logic (Entity Bean access) and forwards executions and results
 * for CRUD (create, read, update and delete)
 * operations. This class is based in net.java.dev.strutsejb.ui.CRUDDirector adding
 * additional functionality.
 *
 * @author Fernando Montaño
 * @version $Id: ExtendedCRUDDirector.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class ExtendedCRUDDirector {

    private Log log = LogFactory.getLog(this.getClass());
    /**
     * Singleton instance.
     */
    public static final ExtendedCRUDDirector i = new ExtendedCRUDDirector();

    private ExtendedCRUDDirector() {
    }

    public static final String OP_CREATE = "create";
    public static final String OP_READ = "read";
    public static final String OP_UPDATE = "update";
    public static final String OP_DELETE = "delete";


    /**
     * Does CRUD operation and adds result message to ResultDTO.
     *
     * @param op        op (operation) to be executed
     * @param dto       ComponentDTO from which this retrieves metadata
     * @param resultDTO ResultDTO to which this puts result
     * @return EJBLocalObject target EJB
     */
    public EJBLocalObject doCRUD(String op,
                                 ComponentDTO dto,
                                 ResultDTO resultDTO) {

        return doCRUD(op, dto, resultDTO, false, false, false, true);
    }

    /**
     * Does CRUD operation and adds result message to ResultDTO.
     *
     * @param op              op (operation) to be executed
     * @param dto             ComponentDTO that provides parameters of CRUD operations
     * @param resultDTO       ResultDTO that will receive result of CRUD operations
     * @param checkVersion    true, execute version control in UPDATE operation, false no control is needed
     * @param checkDuplicate  true, execute duplicate item control in UPDATE Or CREATE operation, false no control is needed
     * @param checkReferences true, execute check integrity referential for a item in READ or DELETE operation, false no control is needed
     * @param isClearingForm  flag that is true if data from ParamDTO must be clearing after update, false in other hand.
     * @return EJBLocalObject target EJB
     */
    public EJBLocalObject doCRUD(String op, ComponentDTO dto, ResultDTO resultDTO, boolean checkVersion,
                                 boolean checkDuplicate, boolean checkReferences, boolean isClearingForm) {

        //dispatch operation
        if (op.equals(OP_CREATE)) {
            return create(dto, resultDTO, checkDuplicate);
        } else if (op.equals(OP_UPDATE)) {
            return update(dto, resultDTO, checkDuplicate, checkVersion, isClearingForm, null);
        } else if (op.equals(OP_DELETE)) {
            delete(dto, resultDTO, checkReferences, null);
            return null;
        } else {
            return read(dto, resultDTO, checkReferences); //default
        }
    }

    /**
     * Does CRUD operation and adds result message to ResultDTO.
     *
     * @param op              op (operation) to be executed
     * @param dto             ComponentDTO that provides parameters of CRUD operations
     * @param resultDTO       ResultDTO that will receive result of CRUD operations
     * @param checkVersion    true, execute version control in UPDATE operation, false no control is needed
     * @param checkDuplicate  true, execute duplicate item control in UPDATE Or CREATE operation, false no control is needed
     * @param checkReferences true, execute check integrity referential for a item in READ or DELETE operation, false no control is needed
     * @param isClearingForm  flag that is true if data from ParamDTO must be clearing after update, false in other hand.
     * @param NotFoundForward string that will be used when bean is not found. Overrides setResultAsFailure default forward
     * @return EJBLocalObject target EJB
     */
    public EJBLocalObject doCRUD(String op, ComponentDTO dto, ResultDTO resultDTO, boolean checkVersion,
                                 boolean checkDuplicate, boolean checkReferences, boolean isClearingForm, String NotFoundForward) {

        //dispatch operation
        if (op.equals(OP_CREATE)) {
            return create(dto, resultDTO, checkDuplicate);
        } else if (op.equals(OP_UPDATE)) {
            return update(dto, resultDTO, checkDuplicate, checkVersion, isClearingForm, NotFoundForward);
        } else if (op.equals(OP_DELETE)) {
            delete(dto, resultDTO, checkReferences, NotFoundForward);
            return null;
        } else {
            return read(dto, resultDTO, checkReferences); //default
        }
    }


    public EJBLocalObject doCRUD(String op, ComponentDTO dto, ResultDTO resultDTO, boolean checkReferences, boolean isClearingForm, String NotFoundForward) {
        //dispatch operation
        if (op.equals(OP_CREATE)) {
            return create(dto, resultDTO, false);
        } else if (op.equals(OP_UPDATE)) {
            return update(dto, resultDTO, false, true, isClearingForm, NotFoundForward);
        } else if (op.equals(OP_DELETE)) {
            delete(dto, resultDTO, checkReferences, NotFoundForward);
            return null;
        } else {
            return read2(dto, resultDTO, checkReferences); //default
        }
    }


    /**
     * Does create operation and adds result message to ResultDTO.
     *
     * @param dto             ComponentDTO that provides parameters for CRUD operations
     * @param resultDTO       ResultDTO that will receive result of CRUD operations
     * @param checkDuplicated flag that is true if duplicated control before creation is needed, false otherwise
     * @return EJBLocalObject that is created by this operation
     */
    public EJBLocalObject create(ComponentDTO dto, ResultDTO resultDTO, boolean checkDuplicated) {

        if (checkDuplicated) {
            DuplicatedEntryChecker.i.check(dto, resultDTO, dto.getAsString("op"));
            if (resultDTO.isFailure()) {
                return null;
            }
        }

        EJBLocalObject ejbObj = null;
        try {
            //create
            ejbObj = createInternal(dto, resultDTO);

            //Add ResultMessage and clear form
            dto.addCreatedMsgTo(resultDTO);
            resultDTO.isClearingForm = true;

        } catch (EJBFactoryException e) {
            //handle duplicated key
            resultDTO.putAll(dto);
            resultDTO.setResultAsFailure();
            dto.addDuplicatedMsgTo(resultDTO);
        }
        return ejbObj;
    }


    /**
     * Creates an Entity Bean
     *
     * @param dto       the component DTO to use for create
     * @param resultDTO the result DTo
     * @return the EjbLocalObject
     */
    private EJBLocalObject createInternal(ComponentDTO dto, ResultDTO resultDTO) {

        //Create EJB
        EJBLocalObject ejbObj = EJBFactory.i.createEJB(dto);
        DTOFactory.i.copyToDTO(ejbObj, resultDTO);
        return ejbObj;
    }

    /**
     * Does read operation and adds result message to ResultDTO.
     *
     * @param dto             ComponentDTO that provides parameters for CRUD operations
     * @param resultDTO       ResultDTO that will receive result of CRUD operations
     * @param checkReferences flag that indicate  if is reading the dto for delete, if true check references before read, otherwise do not control
     * @return EJBLocalObject that is read by this operation
     */
    public EJBLocalObject read(ComponentDTO dto, ResultDTO resultDTO, boolean checkReferences) {

        //if paramDTO has "withReferences" key when is reading then check references
        if (checkReferences && dto.get("withReferences") != null) {
            IntegrityReferentialChecker.i.check(dto, resultDTO);
            if (resultDTO.isFailure()) //if it has references
            {
                return null;
            }
        }


        //read EJB
        EJBLocalObject ejbObj = null;
        try {
            ejbObj = EJBFactory.i.findEJB(dto);
            dto.renderDTO(ejbObj);
            resultDTO.putAll(dto);
            dto.addReadMsgTo(resultDTO);

        } catch (EJBFactoryException e) {
            //handle object not found
            resultDTO.putAll(dto);
            resultDTO.setResultAsFailure();
            dto.addNotFoundMsgTo(resultDTO);
        }
        return ejbObj;
    }

    public EJBLocalObject read2(ComponentDTO dto, ResultDTO resultDTO, boolean checkReferences) {

        //if paramDTO has "withReferences" key when is reading then check references
        if (checkReferences) {
            IntegrityReferentialChecker.i.check(dto, resultDTO);
            if (resultDTO.isFailure()) //if it has references
            {
                return null;
            }
        }

        //read EJB
        EJBLocalObject ejbObj = null;
        try {
            ejbObj = EJBFactory.i.findEJB(dto);
            dto.renderDTO(ejbObj);
            resultDTO.putAll(dto);
            dto.addReadMsgTo(resultDTO);

        } catch (EJBFactoryException e) {
            //handle object not found
            resultDTO.putAll(dto);
            resultDTO.setResultAsFailure();
            dto.addNotFoundMsgTo(resultDTO);
        }
        return ejbObj;
    }


    /**
     * Does update operation and adds result message to ResultDTO.
     *
     * @param dto             ComponentDTO that provides parameters for CRUD operations
     * @param resultDTO       ResultDTO that will receive result of CRUD operations
     * @param checkDuplicate  true, execute duplicate item control, false no control is needed
     * @param checkVersion    true, execute check version control for a item, false no control is needed
     * @param isClearingForm  flag that is true if data from ParamDTO must be clearing after update, false in other hand.
     * @param NotFoundForward string that will be used when bean is not found. Overrides setResultAsFailure default forward
     * @return EJBLocalObject that is updated by this operation
     */
    public EJBLocalObject update(ComponentDTO dto, ResultDTO resultDTO, boolean checkDuplicate, boolean checkVersion,
                                 boolean isClearingForm, String NotFoundForward) {

        //check duplicate
        if (checkDuplicate) {
            DuplicatedEntryChecker.i.check(dto, resultDTO, dto.getAsString("op"));
            if (resultDTO.isFailure()) {
                return null;
            }
        }

        //update EJB
        final EJBLocalObject ejbObj;
        try {
            ejbObj = EJBFactory.i.findEJB(dto); //select the object to update

            //check version
            if (checkVersion) {
                try {
                    Method getVersionMethod = ejbObj.getClass().getMethod("getVersion", new Class[0]);
                    Integer beanVersion = (Integer) getVersionMethod.invoke(ejbObj, new Object[0]);
                    int currentVersion = Integer.parseInt(dto.get("version").toString());

                    if (beanVersion.intValue() != currentVersion) {
                        DTOFactory.i.copyToDTO(ejbObj, resultDTO);
                        resultDTO.addResultMessage("Common.error.concurrency"); // concurrency message
                        resultDTO.isClearingForm = true;
                        resultDTO.setResultAsFailure();
                        return ejbObj;
                    } else {
                        // update the next value in componentDTO for Bean update
                        dto.put("version", new Integer(++currentVersion));
                        resultDTO.put("version", dto.get("version"));
                    }

                } catch (Exception e) {
                    log.error("Unexpected error doing check version", e);
                }

            }


            ExtendedDTOFactory.i.copyFromDTO(dto, ejbObj, true); //update

        } catch (EJBFactoryException e) {
            //handle object not found
            if (NotFoundForward != null) {
                resultDTO.isClearingForm = true;
                resultDTO.setForward(NotFoundForward);
                dto.addNotFoundMsgTo(resultDTO);
            } else {
                resultDTO.putAll(dto);
                dto.addNotFoundMsgTo(resultDTO);
                resultDTO.setResultAsFailure();
            }


            return null;
        }

        //Add ResultMessage and clear form
        dto.addUpdatedMsgTo(resultDTO);
        resultDTO.isClearingForm = isClearingForm;
        /*if (isClearingForm)
            resultDTO.isClearingForm = true;
        else
            resultDTO.isClearingForm = false;*/
        return ejbObj;
    }

    /**
     * Does delete operation and adds result message to ResultDTO.
     *
     * @param dto             ComponentDTO that provides parameters for CRUD operations
     * @param resultDTO       ResultDTO that will receive result of CRUD operations
     * @param checkReferences true for check references, false otherwise
     * @param NotFoundForward string that will be used when bean is not found. Overrides setResultAsFailure default forward
     */
    public void delete(ComponentDTO dto, ResultDTO resultDTO, boolean checkReferences, String NotFoundForward) {
        //removes EJB
        try {
            //check references before remove
            if (checkReferences) {
                IntegrityReferentialChecker.i.check(dto, resultDTO);
                if (resultDTO.isFailure()) //if it has references
                {
                    return;
                }
            }

            //make sure there is the record
            EJBFactory.i.findEJB(dto);

            //delete it
            EJBFactory.i.removeEJB(dto);

        } catch (EJBFactoryException e) {

            //handle object not found
            if (NotFoundForward != null) {
                resultDTO.isClearingForm = true;
                resultDTO.setResultAsFailure();
                resultDTO.setForward(NotFoundForward);
                dto.addNotFoundMsgTo(resultDTO);
            } else {
                resultDTO.putAll(dto);
                resultDTO.setResultAsFailure();
                dto.addNotFoundMsgTo(resultDTO);
            }
            return;
        }
        //Add ResultMessage and clear form
        dto.addDeletedMsgTo(resultDTO);
        resultDTO.isClearingForm = true;
    }


}
