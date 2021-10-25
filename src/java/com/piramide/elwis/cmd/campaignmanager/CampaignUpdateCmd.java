package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.dto.campaignmanager.CampaignDTO;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CampaignUpdateCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class CampaignUpdateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        CampaignDTO campaignDTO = new CampaignDTO(paramDTO);

        CampaignHome campaignHome = (CampaignHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN);
        Campaign campaign = null;
        try {
            campaign = campaignHome.findByPrimaryKey(new Integer(paramDTO.get("campaignId").toString()));
        } catch (FinderException e) {
        }

        if (campaign != null) {
            campaignDTO.remove("recordDate");
            campaignDTO.remove("closeDate");
            campaignDTO.remove("awaitedUtility");
            campaignDTO.remove("realCost");
            ExtendedCRUDDirector.i.update(campaignDTO, resultDTO, false, true, false, "fail");

            //check version control
            if (!resultDTO.isFailure()) {
                int concludedStatus = new Integer(CampaignConstants.SENT);
                if (concludedStatus == campaign.getStatus()) {
                    Collection c = campaign.getCampaignActivity();

                    double d = 0.00;

                    for (Iterator it = c.iterator(); it.hasNext();) {
                        CampaignActivity ac = (CampaignActivity) it.next();
                        if (null != ac.getCost()) {
                            d += ac.getCost().doubleValue();
                        }
                    }
                    campaign.setRealCost(new BigDecimal(d));
                    double profits = campaign.getBudgetCost().doubleValue() - d;
                    campaign.setAwaitedUtility(new BigDecimal(profits));
                    campaign.setCloseDate(DateUtils.dateToInteger(new Date()));
                }

                String remarkValue = (String) paramDTO.get("remarkValue");
                if (null != remarkValue && !"".equals(remarkValue.trim())) {
                    if (null != campaign.getCampaignFreeText()) {
                        campaign.getCampaignFreeText().setValue(remarkValue.getBytes());
                    } else {
                        CampaignFreeTextHome frHome = (CampaignFreeTextHome)
                                EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);
                        try {
                            CampaignFreeText f = frHome.create(remarkValue.getBytes(),
                                    new Integer(paramDTO.getAsInt("companyId")),
                                    new Integer(FreeTextTypes.FREETEXT_CAMPAIGN));
                            campaign.setRemark(f.getFreeTextId());
                        } catch (CreateException e) {
                            log.error("Cannot create CampaignFreeText object for " + campaign.getCampaignId() + "\n", e);
                        }
                    }
                }
            } else {
                if (null != campaign.getCampaignFreeText()) {
                    resultDTO.put("remarkValue", new String(campaign.getCampaignFreeText().getValue()));
                    resultDTO.put("remark", campaign.getRemark());
                }
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
