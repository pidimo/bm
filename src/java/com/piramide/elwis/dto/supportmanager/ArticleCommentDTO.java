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
 * Time: 2:15:22 PM
 * To change this template use File | Settings | File Templates.
 */

public class ArticleCommentDTO extends ComponentDTO {
    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_ARTICLECOMMENT = "commentId";

    /**
     * Creates an instance.
     */
    public ArticleCommentDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ArticleCommentDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_ARTICLECOMMENT;
    }

    public String getJNDIName() {
        return SupportConstants.JNDI_ARTICLECOMMENT;
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
        resultDTO.addResultMessage("msg.Duplicated", get("detail"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("Article.Comment.NotFound");
    }

    public ComponentDTO createDTO() {
        return new ArticleCommentDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {

    }
}

