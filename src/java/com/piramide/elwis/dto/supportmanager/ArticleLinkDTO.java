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
 * Time: 2:15:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArticleLinkDTO extends ComponentDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_ARTICLELINK = "linkId";

    /**
     * Creates an instance.
     */
    public ArticleLinkDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ArticleLinkDTO(DTO dto) {
        super.putAll(dto);
    }

    public ArticleLinkDTO(Integer id) {
        setPrimKey(id);
    }

    public ArticleLinkDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_ARTICLELINK;
    }


    public String getJNDIName() {
        return SupportConstants.JNDI_ARTICLELINK;
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
        resultDTO.addResultMessage("customMsg.NotFound", get("comment"));
    }


    public ComponentDTO createDTO() {
        return new ArticleLinkDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}