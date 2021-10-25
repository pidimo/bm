package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a Template DTO
 *
 * @author yumi
 * @version $Id: TemplateDTO.java 12682 2017-05-22 22:58:15Z miguel $
 */
public class TemplateDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_TEMPLATELIST = "templateList";
    public static final String KEY_TEMPLATEID = "templateId";


    public TemplateDTO() {
    }

    public TemplateDTO(Integer key) {
        setPrimKey(key);
    }

    public TemplateDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_TEMPLATEID;
    }

    public String getDTOListName() {
        return KEY_TEMPLATELIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_TEMPLATE;
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
        resultDTO.addResultMessage("msg.Duplicated", get("description"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("description"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("description"));
    }

    public ComponentDTO createDTO() {
        return new TemplateDTO();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_CONTACT, "templateid");
        tables.put(ContactConstants.TABLE_COMPANY, "invmailtemplatid");
        tables.put(SalesConstants.TABLE_ACTIONTYPE, "templateid");
        return tables;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_TEMPLATE;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

}