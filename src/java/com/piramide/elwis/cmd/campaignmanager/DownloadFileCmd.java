package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.DownloadFreeTextCmd;
import com.piramide.elwis.domain.campaignmanager.CampaignFreeText;
import com.piramide.elwis.domain.campaignmanager.CampaignFreeTextHome;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author: ivan
 * Date: 26-10-2006: 11:02:00 AM
 */
public class DownloadFileCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        int freeTextId = (Integer) paramDTO.get("freeTextId");

        CampaignConstants.DocumentType.WORD.getConstantAsString();
        if (null != paramDTO.get("documentType") &&
                CampaignConstants.DocumentType.WORD.getConstantAsString().equals(paramDTO.get("documentType").toString())) {
            DownloadFreeTextCmd cmd = new DownloadFreeTextCmd();
            cmd.putParam("type", "camp");
            cmd.putParam("fid", String.valueOf(freeTextId));
            cmd.executeInStateless(ctx);
            if (!cmd.getResultDTO().isFailure()) {
                ByteArrayOutputStream out = (ByteArrayOutputStream) cmd.getOutputStream();
                byte[] b = out.toByteArray();

                InputStream in = new ByteArrayInputStream(b);
                resultDTO.put("inputStream", in);
                resultDTO.put("fileSize", b.length);
            } else {
                resultDTO.setResultAsFailure();
            }

        } else {

            CampaignFreeTextHome h =
                    (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);
            try {
                CampaignFreeText frt = h.findByPrimaryKey(freeTextId);
                InputStream in = new ByteArrayInputStream(frt.getValue());
                resultDTO.put("inputStream", in);
                resultDTO.put("fileSize", frt.getValue().length);
            } catch (FinderException e) {
                resultDTO.setResultAsFailure();
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
