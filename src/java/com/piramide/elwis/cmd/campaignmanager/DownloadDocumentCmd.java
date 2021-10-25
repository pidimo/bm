package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.domain.campaignmanager.CampaignFreeText;
import com.piramide.elwis.domain.campaignmanager.CampaignFreeTextHome;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 * Cmd to read an free text
 *
 * @author Miky
 * @version $Id: DownloadDocumentCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class DownloadDocumentCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing DownloadDocumentCmd....." + paramDTO);

        Integer freeTextId = new Integer(paramDTO.get("freeTextId").toString());
        CampaignFreeTextHome freeTextHome = (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);

        try {
            CampaignFreeText freeText = freeTextHome.findByPrimaryKey(freeTextId);
            resultDTO.put("freeText", new ArrayByteWrapper(freeText.getValue()));
        } catch (FinderException e) {
            resultDTO.setResultAsFailure();
        }
    }

    public boolean isStateful() {
        return false;
    }
}
