package com.piramide.elwis.dto.supportmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 2:15:00 PM
 * To change this template use File | Settings | File Templates.
 */

public class ArticleQuestionDTO extends ComponentDTO implements IntegrityReferentialDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_ARTICLEQUESTION = "questionId";

    /**
     * Creates an instance.
     */
    public ArticleQuestionDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ArticleQuestionDTO(DTO dto) {
        super.putAll(dto);
    }

    public ArticleQuestionDTO(Integer id) {
        setPrimKey(id);
    }

    public ArticleQuestionDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_ARTICLEQUESTION;
    }

    public String getJNDIName() {
        return SupportConstants.JNDI_ARTICLEQUESTION;
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
        resultDTO.addResultMessage("customMsg.NotFound", get("summary"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("summary"));
    }

    public ComponentDTO createDTO() {
        return new ArticleQuestionDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(SupportConstants.TABLE_ARTICLE, "rootquestionid");
        return tables;
    }
}