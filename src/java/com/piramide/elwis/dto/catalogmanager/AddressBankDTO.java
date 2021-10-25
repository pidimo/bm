package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a AddressBank DTO
 *
 * @author Ivan
 * @version $Id: AddressBankDTO.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class AddressBankDTO extends ComponentDTO {

    public static final String KEY_ADDRESSBANKLIST = "addressBankList";
    public static final String KEY_ADDRESSBANKID = "addressBankId";

    /**
     * Creates an instance.
     */
    public AddressBankDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public AddressBankDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty AddressBankDTO with specified languageId
     */
    public AddressBankDTO(Integer addressBankId) {
        setPrimKey(addressBankId);
    }

    public String getPrimKeyName() {
        return KEY_ADDRESSBANKID;
    }

    public String getDTOListName() {
        return KEY_ADDRESSBANKLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_ADDRESSBANK;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Duplicated", "AddressBank.id");
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", "AddressBank");
    }

    public ComponentDTO createDTO() {
        return new AddressBankDTO();
    }

    /**
     * Returns a List of all AddressBank.
     *
     * @return List of DTO.
     */
    public static List findAllAddressBankDTOs() {

        Collection col;
        try {
            col = (Collection) EJBFactory.i.callFinder(new AddressBankDTO(), "findAll");

        } catch (EJBFactoryException e) {
            col = new LinkedList();
        }
        return DTOFactory.i.createDTOs(col, AddressBankDTO.class);
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}