package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.FreeTextCmdUtil;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityDTO;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: ivan
 * Date: 19-oct-2006 - 13:56:37
 */
public class CampaignActivityCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        log.debug("paramDTO : " + paramDTO);
        String op = this.getOp();
        paramDTO.remove("numberContact");
        boolean isRead = true;
        if ("create".equals(op)) {
            isRead = false;
            create();
        }
        if ("update".equals(op)) {
            isRead = false;
            update();
        }
        if ("delete".equals(op)) {
            isRead = false;
            delete();
        }
        if ("checkRelations".equals(op)) {
            isRead = false;
            checkActivityRelations();
        }
        if ("deleteRelations".equals(op)) {
            isRead = false;
            deleteRelations();
        }
        if (isRead) {
            read();
        }
    }

    private void create() {
        CampaignActivityDTO dto = new CampaignActivityDTO(paramDTO);
        CampaignActivity object = (CampaignActivity) ExtendedCRUDDirector.i.create(dto, resultDTO, false);
        if (null != object) {
            FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, object, "CampaignFreeText", CampaignFreeTextHome.class,
                    CampaignConstants.JNDI_CAMPAIGN_FREETEXT, FreeTextTypes.FREETEXT_CAMPAIGN, "detail");
        }
    }

    private void read() {
        if (null != paramDTO.get("activityId")) {
            boolean checkReferences = false;
            if (null != paramDTO.get("withReferences")) {
                checkReferences = Boolean.valueOf(paramDTO.get("withReferences").toString());
            }
            CampaignActivityDTO dto = new CampaignActivityDTO(paramDTO);
            CampaignActivity obj = (CampaignActivity) ExtendedCRUDDirector.i.read(dto, resultDTO, checkReferences);
            if (null != obj) {
                FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, obj, "CampaignFreeText", CampaignFreeTextHome.class,
                        CampaignConstants.JNDI_CAMPAIGN_FREETEXT, FreeTextTypes.FREETEXT_CAMPAIGN, "detail");
            }
        }
    }

    private void update() {
        CampaignActivityDTO dto = new CampaignActivityDTO(paramDTO);
        CampaignActivity obj = (CampaignActivity) ExtendedCRUDDirector.i.update(dto, resultDTO, false, true, false, "Fail");
        if (null != obj) {
            if (!resultDTO.isFailure()) {
                FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, obj, "CampaignFreeText", CampaignFreeTextHome.class,
                        CampaignConstants.JNDI_CAMPAIGN_FREETEXT, FreeTextTypes.FREETEXT_CAMPAIGN, "detail");
            } else {
                paramDTO.setOp("read");
                read();
            }
        }
    }

    private void delete() {
        CampaignActivityDTO dto = new CampaignActivityDTO(paramDTO);
        CampaignActivity ac = (CampaignActivity) ExtendedCRUDDirector.i.read(dto, resultDTO, true);


        if (null != ac) {
            if (!resultDTO.isFailure()) {
                deleteRelations(ac);
                try {
                    if (null != ac.getCampaignFreeText()) {
                        ac.getCampaignFreeText().remove();
                    }
                    ac.remove();
                } catch (RemoveException e) {
                    log.error("Cannot remove campaignActivity object ", e);
                }
            }
        }
    }

    private boolean checkActivityRelations() {
        boolean result = false;
        int campaignId = new Integer(paramDTO.get("campaignId").toString());
        CampaignHome cH = (CampaignHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN);
        Campaign c = null;
        try {
            c = cH.findByPrimaryKey(campaignId);
        } catch (FinderException e) {
            log.debug("Cannot find Campaign object ", e);
        }

        if (null != c && !isFromCampaignCascadeDelete()) {
            for (Iterator it = c.getCampaignActivity().iterator(); it.hasNext(); ) {
                CampaignActivity ac = (CampaignActivity) it.next();
                CampaignActivityDTO dto = new CampaignActivityDTO();
                dto.put("activityId", ac.getActivityId());
                dto.put("withReferences", true);

                ExtendedCRUDDirector.i.read(dto, resultDTO, true);
                if (resultDTO.isFailure()) {
                    result = true;
                    resultDTO.put("hasRelations", true);
                    return result;
                }
            }
        }
        resultDTO.put("hasRelations", false);
        return result;
    }

    private boolean isFromCampaignCascadeDelete() {
        return "true".equals(paramDTO.get("isCampaignCascadeDelete"));
    }

    private void deleteRelations() {
        if (!checkActivityRelations()) {
            int campaignId = new Integer(paramDTO.get("campaignId").toString());
            CampaignHome cH = (CampaignHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN);
            Campaign c = null;
            try {
                c = cH.findByPrimaryKey(campaignId);
            } catch (FinderException e) {
                log.debug("Cannot find Campaign object ", e);
            }
            if (null != c) {
                List activityContacts = new ArrayList(c.getCampaignActivity());
                for (int i = 0; i < activityContacts.size(); i++) {
                    CampaignActivity ac = (CampaignActivity) activityContacts.get(i);
                    deleteRelations(ac);
                }
            }
        }
    }

    private void deleteRelations(CampaignActivity ac) {
        List campaignContacts = new ArrayList(ac.getCampaignContacts());
        for (int i = 0; i < campaignContacts.size(); i++) {
            CampaignContact c = (CampaignContact) campaignContacts.get(i);
            try {
                c.remove();
            } catch (RemoveException re) {
                log.error("Cannot remove campaignContact object ", re);
            }
        }

        List campaignActivityResponsibles = new ArrayList(ac.getActivityUsers());
        for (int i = 0; i < campaignActivityResponsibles.size(); i++) {
            CampaignActivityUser r = (CampaignActivityUser) campaignActivityResponsibles.get(i);
            try {
                r.remove();
            } catch (RemoveException re) {
                log.error("Cannot remove campaignActivityUser object ", re);
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
