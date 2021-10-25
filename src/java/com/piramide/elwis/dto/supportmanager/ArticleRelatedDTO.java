package com.piramide.elwis.dto.supportmanager;

import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 2:15:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArticleRelatedDTO extends ComponentDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_ARTICLERELATEDID = "articleRelatedPK";

    /**
     * Creates an instance.
     */
    public ArticleRelatedDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ArticleRelatedDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_ARTICLERELATEDID;
    }


    public String getJNDIName() {
        return SupportConstants.JNDI_ARTICLERELATED;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("articleTitle"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("articleTitle"));
    }

    public ComponentDTO createDTO() {
        return new ArticleRelatedDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}