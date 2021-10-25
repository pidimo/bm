package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.dto.webmailmanager.UserMailDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * @author : ivan
 * @version $Id: PreferencesCmd.java 10480 2014-08-14 18:52:29Z miguel ${time}
 */
public class PreferencesCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;
        UserMailDTO userMailDTO = getUSerMailDTO();

        if ("update".equals(getOp())) {
            isRead = false;
            update(userMailDTO);
        }

        if (isRead) {
            read(userMailDTO);
        }
    }

    private void update(UserMailDTO userMailDTO) {
        ExtendedCRUDDirector.i.update(userMailDTO, resultDTO, false, false, false, "Fail");
        if (!resultDTO.isFailure() && !"Fail".equals(resultDTO.getForward())) {
            resultDTO.addResultMessage("Common.changesOK");
        }
    }

    private UserMailDTO getUSerMailDTO() {
        UserMailDTO userMailDTO = new UserMailDTO();

        userMailDTO.put("userMailId", EJBCommandUtil.i.getValueAsInteger(this, "userId"));
        userMailDTO.put("replyMode", EJBCommandUtil.i.getValueAsBoolean(this, "replyMode"));
        userMailDTO.put("saveSendItem", EJBCommandUtil.i.getValueAsBoolean(this, "saveSendItem"));
        userMailDTO.put("emptyTrashLogout", EJBCommandUtil.i.getValueAsBoolean(this, "emptyTrashLogout"));
        userMailDTO.put("showPopNotification", EJBCommandUtil.i.getValueAsBoolean(this, "showPopNotification"));
        userMailDTO.put("backgroundDownload", EJBCommandUtil.i.getValueAsBoolean(this, "backgroundDownload"));

        EJBCommandUtil.i.setValueAsInteger(this, userMailDTO, "editMode");
        userMailDTO.put("editorFont", paramDTO.get("editorFont"));
        userMailDTO.put("editorFontSize", paramDTO.get("editorFontSize"));


        return userMailDTO;
    }

    public boolean isStateful() {
        return false;
    }

    private void read(UserMailDTO userMailDTO) {
        ExtendedCRUDDirector.i.read(userMailDTO, resultDTO, false);
    }

}
