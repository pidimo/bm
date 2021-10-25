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
 * Time: 2:16:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArticleHistoryDTO extends ComponentDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_ARTICLEHISTORY = "historyId";

    /**
     * Creates an instance.
     */
    public ArticleHistoryDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ArticleHistoryDTO(DTO dto) {
        super.putAll(dto);
    }

    public ArticleHistoryDTO(Integer id) {
        setPrimKey(id);
    }

    public ArticleHistoryDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_ARTICLEHISTORY;
    }


    public String getJNDIName() {
        return SupportConstants.JNDI_ARTICLEHISTORY;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public ComponentDTO createDTO() {
        return new ArticleHistoryDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}