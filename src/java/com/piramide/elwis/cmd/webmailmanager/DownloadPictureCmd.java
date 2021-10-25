package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.domain.webmailmanager.Attach;
import com.piramide.elwis.dto.webmailmanager.AttachDTO;
import com.piramide.elwis.utils.webmail.WebmailAttachUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;

import javax.ejb.SessionContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: DownloadPictureCmd.java 10225 2012-05-29 17:48:18Z miguel ${CLASS_NAME}.java,v 1.2 21-04-2005 10:19:38 AM ivan Exp $
 */
public class DownloadPictureCmd extends EJBCommand {

    public void executeInStateless(SessionContext ctx) {
        Integer attachId = Integer.valueOf((String) paramDTO.get("attachId"));

        //find the attach
        Attach attach = (Attach) EJBFactory.i.callFinder(new AttachDTO(),
                "findByPrimaryKey",
                new Object[]{attachId});

        if (null == attach) {
            return;
        }

        byte[] attachData = WebmailAttachUtil.i.getAttachData(attach);
        InputStream input = new ByteArrayInputStream(attachData);

        //returns as "image" the wrapper
        resultDTO.put("image", input);
        resultDTO.put("fileName", attach.getAttachName());
        if (null == attach.getSize()) {
            attach.setSize(attachData.length);
        }

        resultDTO.put("fileSize", attach.getSize());
    }

    public boolean isStateful() {
        return false;
    }
}
