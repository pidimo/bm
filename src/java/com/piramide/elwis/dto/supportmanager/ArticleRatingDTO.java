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
 * Time: 2:16:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArticleRatingDTO extends ComponentDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_ARTICLERATINGID = "articleRatingPK";

    /**
     * Creates an instance.
     */
    public ArticleRatingDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ArticleRatingDTO(DTO dto) {
        super.putAll(dto);
    }

    public ArticleRatingDTO(Integer id) {
        setPrimKey(id);
    }

    public ArticleRatingDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_ARTICLERATINGID;
    }


    public String getJNDIName() {
        return SupportConstants.JNDI_ARTICLERATING;
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
        return new ArticleRatingDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}

