package com.piramide.elwis.dto.reports;

import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Nov 16, 2005
 * Time: 1:42:24 PM
 * To change this template use File | Settings | File Templates.
 */

public class ChartDTO extends ComponentDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_CHARTID = "chartId";

    /**
     * Creates an instance.
     */
    public ChartDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ChartDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ChartDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_CHARTID;
    }


    public String getJNDIName() {
        return ReportConstants.JNDI_CHART;
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
        resultDTO.addResultMessage("customMsg.NotFound", get("title"));
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Referenced");
    }
}
