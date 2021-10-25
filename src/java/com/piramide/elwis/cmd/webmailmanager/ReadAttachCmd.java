package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.webmailmanager.Attach;
import com.piramide.elwis.dto.webmailmanager.AttachDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.webmail.WebmailAttachUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * AlfaCentauro Team
 * This class reads a attach
 *
 * @author Alvaro
 * @version $Id: ReadAttachCmd.java 10225 2012-05-29 17:48:18Z miguel $
 */
public class ReadAttachCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        String attachId = paramDTO.get("attachId").toString();
        readAttach(attachId);
    }

    /**
     * Read the attach on one mail, and return the attach info and a inputStream bind to
     * the blob in the resultDTO
     *
     * @param attachId Is the id of the attach
     */
    public void readAttach(String attachId) {
        AttachDTO attachDTO = new AttachDTO();
        attachDTO.put("attachId", new Integer(attachId));
        Attach attach = (Attach) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, attachDTO, new ResultDTO());

        byte[] attachData = WebmailAttachUtil.i.getAttachData(attach);

        ArrayByteWrapper byteWrapper = new ArrayByteWrapper(attachData);
        InputStream in = new ByteArrayInputStream(byteWrapper.getFileData());
        resultDTO.put("attachName", attach.getAttachName());
        resultDTO.put("attachStream", in);
        if (null == attach.getSize()) {
            Integer fileSize = attachData.length;
            attach.setSize(fileSize);
        }
        resultDTO.put("attachSize", attach.getSize());
    }
}
