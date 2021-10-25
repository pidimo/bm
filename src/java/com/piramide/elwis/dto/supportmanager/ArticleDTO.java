package com.piramide.elwis.dto.supportmanager;

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
 * Time: 2:09:42 PM
 * To change this template use File | Settings | File Templates.
 */

public class ArticleDTO extends ComponentDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_ARTICLE = "articleId";

    /**
     * Creates an instance.
     */
    public ArticleDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ArticleDTO(DTO dto) {
        super.putAll(dto);
    }

    public ArticleDTO(Integer id) {
        setPrimKey(id);
    }

    public ArticleDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_ARTICLE;
    }


    public String getJNDIName() {
        return SupportConstants.JNDI_ARTICLE;
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

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        /*tables.put(SupportConstants.TABLE_ARTICLERELATED, "relatedarticleid");*/
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("articleTitle"));
    }

    public ComponentDTO createDTO() {
        return new ArticleDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}