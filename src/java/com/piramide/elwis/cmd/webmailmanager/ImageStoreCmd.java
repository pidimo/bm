package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.webmailmanager.Attach;
import com.piramide.elwis.domain.webmailmanager.AttachHome;
import com.piramide.elwis.domain.webmailmanager.ImageStore;
import com.piramide.elwis.domain.webmailmanager.ImageStoreHome;
import com.piramide.elwis.dto.webmailmanager.ImageStoreDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Cmd to manage webmail temporal images
 *
 * @author Miky
 * @version $Id: ImageStoreCmd.java 2009-05-25 03:16:49 PM $
 */
public class ImageStoreCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ImageStoreCmd....." + paramDTO);

        if ("create".equals(getOp())) {
            create();
        }

        if ("download".equals(getOp())) {
            download();
        }

        if ("deleteSession".equals(getOp())) {
            deleteBySessionId();
        }

        if ("delete".equals(getOp())) {
            delete();
        }

        if ("createMailAttach".equals(getOp())) {
            createMailAttach();
        }
    }

    private void create() {
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        String sessionId = paramDTO.get("sessionId").toString();
        ArrayByteWrapper wrapper = (ArrayByteWrapper) paramDTO.get("imageData");

        ImageStoreHome imageStoreHome = (ImageStoreHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_IMAGESTORE);

        try {
            ImageStore imageStore = imageStoreHome.create(wrapper.getFileData(), wrapper.getFileName(), companyId, sessionId, WebMailConstants.ImageStoreType.TEMPORAL_IMAGE.getConstant());
            resultDTO.put("imageStoreId", imageStore.getImageStoreId());
        } catch (CreateException e) {
            log.debug("Error in create temporalImage...", e);
            resultDTO.setResultAsFailure();
        }
    }

    private void download() {
        Integer imageStoreId = new Integer(paramDTO.get("imageStoreId").toString());
        ImageStoreHome imageStoreHome = (ImageStoreHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_IMAGESTORE);
        try {
            ImageStore imageStore = imageStoreHome.findByPrimaryKey(imageStoreId);

            ArrayByteWrapper wrapper = new ArrayByteWrapper(imageStore.getFileData());
            wrapper.setFileName(imageStore.getFileName());
            resultDTO.put("imageWrapper", wrapper);
        } catch (FinderException e) {
            resultDTO.setResultAsFailure();
        }
    }

    private void createMailAttach() {
        Integer imageStoreId = new Integer(paramDTO.get("imageStoreId").toString());
        Integer mailId = new Integer(paramDTO.get("mailId").toString());

        ImageStoreHome imageStoreHome = (ImageStoreHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_IMAGESTORE);

        ImageStore imageStore = null;
        try {
            imageStore = imageStoreHome.findByPrimaryKey(imageStoreId);
        } catch (FinderException e) {
            log.debug("Can't read temporal image:" + imageStoreId, e);
            resultDTO.setResultAsFailure();
        }

        if (imageStore != null) {
            //create temporal image as mail attach
            AttachHome attachHome = (AttachHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_ATTACH);
            try {
                Attach newAttach = attachHome.create(imageStore.getCompanyId(), mailId, imageStore.getFileName(), imageStore.getFileData());
                newAttach.setVisible(true); //true to inner mail images as attach

                resultDTO.put("attachId", newAttach.getAttachId());
                resultDTO.put("attachSize", newAttach.getSize());
            } catch (CreateException e) {
                log.debug("Create new Attachment Fail...", e);
                resultDTO.setResultAsFailure();
            }
        }
    }

    private void deleteBySessionId() {
        String sessionId = paramDTO.get("sessionId").toString();
        ImageStoreHome imageStoreHome = (ImageStoreHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_IMAGESTORE);

        Collection temporalImages;
        try {
            temporalImages = imageStoreHome.findBySessionId(sessionId, WebMailConstants.ImageStoreType.TEMPORAL_IMAGE.getConstant());
        } catch (FinderException e) {
            temporalImages = new ArrayList();
        }

        try {
            for (Iterator iterator = temporalImages.iterator(); iterator.hasNext();) {
                ImageStore imageStore = (ImageStore) iterator.next();
                imageStore.remove();
            }
        } catch (RemoveException e) {
            log.debug("Error in delete temporal images...", e);
            resultDTO.setResultAsFailure();
        }
    }

    private void delete() {
        Integer imageStoreId = new Integer(paramDTO.get("imageStoreId").toString());

        //delete if it not is referenced
        ExtendedCRUDDirector.i.delete(new ImageStoreDTO(imageStoreId), new ResultDTO(), true, null);
    }

}

