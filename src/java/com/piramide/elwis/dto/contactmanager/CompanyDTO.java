package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Yumi
 * @version $Id: CompanyDTO.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class CompanyDTO extends ComponentDTO {
    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_COMPANYID = "companyId";

    /**
     * Creates an instance.
     */
    public CompanyDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public CompanyDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_COMPANYID;
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_COMPANY;
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
        resultDTO.addResultMessage("msg.Duplicated", "LoggedCompany");
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", "LoggedCompany");
    }

    public ComponentDTO createDTO() {
        return new CompanyDTO();
    }


    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

}

