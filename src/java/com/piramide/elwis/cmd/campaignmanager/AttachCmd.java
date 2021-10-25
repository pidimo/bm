package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.domain.campaignmanager.Attach;
import com.piramide.elwis.domain.campaignmanager.CampaignFreeText;
import com.piramide.elwis.domain.campaignmanager.CampaignFreeTextHome;
import com.piramide.elwis.dto.campaignmanager.AttachDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;

/**
 * @author: ivan
 * Date: 25-10-2006: 01:02:30 PM
 */

public class AttachCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;
        if ("create".equals(getOp())) {
            isRead = false;
            create(ctx);
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update();
        }

        if ("delete".equals(getOp())) {
            isRead = false;
            delete();
        }
        if (isRead && null != paramDTO.get("attachId")) {
            read();
        }
    }

    private void read() {
        AttachDTO dto = new AttachDTO(paramDTO);
        ExtendedCRUDDirector.i.read(dto, resultDTO, false);
    }

    private void create(SessionContext ctx) {
        //recocer from paramDTO file can be attached
        ArrayByteWrapper file = (ArrayByteWrapper) paramDTO.get("file");

        // remove file key from paramDTO object
        paramDTO.remove("file");

        int companyId = new Integer(paramDTO.get("companyId").toString());
        CampaignFreeTextHome h =
                (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);
        try {
            //create CampaignFreeText object
            CampaignFreeText f = h.create(file.getFileData(), companyId, FreeTextTypes.FREETEXT_CAMPAIGN);
            AttachDTO dto = new AttachDTO(paramDTO);
            dto.put("freeTextId", f.getFreeTextId());
            ExtendedCRUDDirector.i.create(dto, resultDTO, false);
        } catch (CreateException e) {
            ctx.setRollbackOnly();
            log.error("Cannot create FreeText for attach in campaing module ", e);
        }
    }

    private void update() {
        ArrayByteWrapper file = (ArrayByteWrapper) paramDTO.get("file");
        paramDTO.remove("file");

        AttachDTO dto = new AttachDTO(paramDTO);
        Attach a = (Attach) ExtendedCRUDDirector.i.update(dto, resultDTO, false, true, false, "Fail");
        if (null != a) {
            if (!resultDTO.isFailure() && null != file) {
                a.getCampaignFreeText().setValue(file.getFileData());
            } else {
                paramDTO.setOp("read");
                read();
            }
        }
    }

    private void delete() {
        AttachDTO dto = new AttachDTO(paramDTO);
        ExtendedCRUDDirector.i.delete(dto, resultDTO, false, "Fail");
    }

    public boolean isStateful() {
        return false;
    }
}
