package com.piramide.elwis.dto.admin;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;

/**
 * User DTO
 *
 * @author Fernando Montaño
 * @version $Id: UserDTO.java 10523 2015-03-19 22:55:32Z miguel $
 */

public class UserDTO extends ComponentDTO implements IntegrityReferentialDTO {
    private Log log = LogFactory.getLog(UserDTO.class);
    public static final String KEY_USERID = "userId";

    /**
     * Creates an instance.
     */

    public UserDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */

    public UserDTO(DTO dto) {
        super.putAll(dto);
    }

    public UserDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public UserDTO(Integer id) {
        setPrimKey(id);
    }

    public String getPrimKeyName() {
        return KEY_USERID;
    }


    public String getJNDIName() {
        return AdminConstants.JNDI_USER;
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
        resultDTO.addResultMessage("User.Duplicated", get("employeeName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        if (get("employeeName") != null) {
            resultDTO.addResultMessage("customMsg.NotFound", get("employeeName"));
        } else if (get("addressName") != null) {
            resultDTO.addResultMessage("customMsg.NotFound", get("addressName"));
        }
    }

    public ComponentDTO createDTO() {
        return new UserDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {

        HashMap tables = new HashMap();

        tables.put(ContactConstants.TABLE_ADDRESS, "recorduser, lastmoduser");
        tables.put(ContactConstants.TABLE_CONTACTPERSON, "recorduserid");
        tables.put(WebMailConstants.TABLE_USERMAIL, "usermailid");

        /*for scheduler */
        tables.put(AdminConstants.TABLE_USEROFGROUP, "userid");
        tables.put(SchedulerConstants.TABLE_APPOINTMENT, "userid");
        tables.put(SchedulerConstants.TABLE_TASK, "userid");
        tables.put(SchedulerConstants.TABLE_SCHEDULERACCESS, "owneruserid, userid");
        tables.put(SchedulerConstants.TABLE_SCHEDULEDUSER, "userid");

        /*for article */
        tables.put(SupportConstants.TABLE_ARTICLERATING, "userid");
        tables.put(SupportConstants.TABLE_ARTICLEQUESTION, "createuserid");
        tables.put(SupportConstants.TABLE_ARTICLE, "createuserid, updateuserid");
        tables.put(SupportConstants.TABLE_ARTICLECOMMENT, "createuserid");
        tables.put(SupportConstants.TABLE_ARTICLEHISTORY, "userid");
        tables.put(SupportConstants.TABLE_ARTICLELINK, "createuserid");

        /*for case*/
        tables.put(SupportConstants.TABLE_SUPPORT_ATTACH, "userid");
        tables.put(SupportConstants.TABLE_SUPPORT_CASE, "touserid, openbyuserid, fromuserid");
        tables.put(SupportConstants.TABLE_SUPPORT_USER, "userid");
        tables.put(SupportConstants.TABLE_CASE_ACTIVITY, "fromuserid, touserid");

        /**stylesheet owner**/
        tables.put(UIManagerConstants.TABLE_STYLESHEET, "userid");

        /**campaign module**/
        tables.put(CampaignConstants.TABLE_CAMPAIGNACTIVITYUSER, "userid");
        tables.put(CampaignConstants.TABLE_CAMPAIGNACTIVITY, "userid");
        tables.put(CampaignConstants.TABLE_CAMPAIGNCONTACT, "userid");
        tables.put(CampaignConstants.TABLE_SENTLOGCONTACT, "userid");


        /*project module*/
        tables.put(ProjectConstants.TABLE_PROJECT_TIME, "userid, confirmedbyid, releaseduserid");

        tables.put(AdminConstants.TABLE_PASSWORDCHANGE, "userid");
        tables.put(ContactConstants.TABLE_DEDUPLICONTACT, "userid");


        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        if (get("employeeName") != null) {
            resultDTO.addResultMessage("customMsg.Referenced", get("employeeName"));
        } else if (get("addressName") != null) {
            resultDTO.addResultMessage("customMsg.Referenced", get("addressName"));
        } else {
            resultDTO.addResultMessage("msg.Referenced");
        }
    }
}
