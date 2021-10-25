package com.piramide.elwis.dto.salesmanager;

import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Jan 26, 2005
 * Time: 9:33:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class ActionPositionDTO extends ComponentDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_POSITIONID = "positionId";

    /**
     * Creates an instance.
     */
    public ActionPositionDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ActionPositionDTO(DTO dto) {
        super.putAll(dto);
    }

    public ActionPositionDTO(Integer id) {
        setPrimKey(id);
    }

    public ActionPositionDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_POSITIONID;
    }

    public String getJNDIName() {
        return SalesConstants.JNDI_ACTIONPOSITION;
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
        resultDTO.addResultMessage("customMsg.NotFound", get("productName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("productName"));
    }

    public ComponentDTO createDTO() {
        return new ActionPositionDTO();
    }


    public void parseValues() {
        if ("".equals(this.get("description"))) {
            this.put("description", null);
        }

        super.convertPrimKeyToInteger();
    }

}

