package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.domain.campaignmanager.CampaignCriterion;
import com.piramide.elwis.domain.campaignmanager.CampaignCriterionHome;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 31-ene-2006
 * Time: 11:13:33
 * To change this template use File | Settings | File Templates.
 */

public class CampaignCriterionValueCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug(" ... CampaignCriterionValueCMD  execute ...");
        CampaignCriterionHome criterionHome = (CampaignCriterionHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCRITERION);
        CampaignCriterion criterion = null;
        String field = "";
        String tablename = "";
        String typefield = "";
        String fieldname = "";
        String value = "";
        String title = "";
        String campCriterionValueId = "";
        StringTokenizer tokenizer = null;
        boolean criterionCategory = false;
        boolean isUpdate = false;

        if (paramDTO.get("campaignCriterionId") != null) {

            isUpdate = true;
            String id = paramDTO.get("campaignCriterionId").toString();

            try { //es llamado desde UPDATE
                criterion = criterionHome.findByPrimaryKey(new Integer(id.toString()));
                if (criterion.getCampCriterionValueId() != null) {
                    criterionCategory = true;
                }
            } catch (FinderException e) {
                log.debug(" ... criterion not found ....");
            }
        }

        if ((paramDTO.get("values") == null || "-1".equals(paramDTO.get("values")) || "".equals(paramDTO.get("values"))) && !isUpdate) {
            resultDTO.addResultMessage("range", new ActionError("Campaign.emptyList"));
            resultDTO.setResultAsFailure();
            return;
        }
        if (paramDTO.get("values") != null) {
            //es llamado desde CREATE
            tokenizer = new StringTokenizer(paramDTO.get("values").toString(), ",");
            field = tokenizer.nextToken();
            tablename = tokenizer.nextToken();
            typefield = tokenizer.nextToken();
            fieldname = tokenizer.nextToken();
            title = tokenizer.nextToken();
            campCriterionValueId = tokenizer.nextToken();
        }
        Collection criterionValuesList = new ArrayList();
        Collection categoryList = new ArrayList();

//values: field,tablename,typefield,fieldName,title,campaignCriterionValueId.
//ej.     countryid,country,0,countryname,campaign.countryName,8
/*
leido un criterio verifica si existe uno o mas criterio de este tipo
si no existe manda a crear si existe manda a editar con los valores de todos los
criterios que ya existen, sin repetirlos. Manda una cadena con los todos los ids.
*/


        if (!"categoryvalue".equals(tablename) && !isUpdate) {
            try {   //for create and type =cmapaignCriterionValueID
                log.debug("... for create and type = category");
                criterionValuesList = criterionHome.findByManyCampaignIdAndCriterionValueId(new Integer(paramDTO.get("campaignId").toString()),
                        new Integer(campCriterionValueId.toString()));
            } catch (FinderException e) {
                log.debug("... for create and type = category  NOT FOUND");
            }
        } else if (criterionCategory && isUpdate) {
            try {  // for update and type=campaignCriterionValue
                log.debug(" ... for update and type=campaignCriterionValue ");
                criterionValuesList = criterionHome.findByManyCampaignIdAndCriterionValueId(new Integer(paramDTO.get("campaignId").toString()),
                        criterion.getCampCriterionValueId());
            } catch (FinderException e) {
                log.debug(" ... for update and type=campaignCriterionValue NOT FOUND");
            }
        } else if ("categoryvalue".equals(tablename) && !isUpdate) {
            try { // for create and type=categoryId
                log.debug(" ... criterion create ...  with categoryid");
                categoryList = criterionHome.findByManyCampaignIdAndCategoryId(new Integer(paramDTO.get("campaignId").toString()),
                        new Integer(field));
            } catch (FinderException e) {
                log.debug(" ... criterion create ...  with categoryid NOT FOUND");
            }
        } else if (!criterionCategory && isUpdate) {
            try {// for update and type=categoryId
                log.debug("... criterion update ...  with campaignCriterionValueID");
                categoryList = criterionHome.findByManyCampaignIdAndCategoryId(new Integer(paramDTO.get("campaignId").toString()), criterion.getCategoryId());
            } catch (FinderException e) {
                log.debug("... criterion update ...  with campaignCriterionValueID NOT FOUND");
            }
        }
        if (criterionValuesList.size() > 0 || categoryList.size() > 0) {
            log.debug(" ... criterio NOT ...  NULL ... then update ...");
            StringBuffer valueIds = new StringBuffer();
            //mandar a llamar actualizar
            if (categoryList.size() > 0) {
                resultDTO.put("ids", categoryList);
                for (Iterator iterator = categoryList.iterator(); iterator.hasNext();) {
                    criterion = null;
                    criterion = (CampaignCriterion) iterator.next();
                    valueIds.append(new String(criterion.getCampaignFreeText().getValue()));
                    if (iterator.hasNext()) {
                        valueIds.append(",");
                    }
                }
            }
            if (criterionValuesList.size() > 0) {
                resultDTO.put("ids", criterionValuesList);
                for (Iterator iterator = criterionValuesList.iterator(); iterator.hasNext();) {
                    criterion = (CampaignCriterion) iterator.next();
                    valueIds.append(new String(criterion.getCampaignFreeText().getValue()));
                    if (iterator.hasNext()) {
                        valueIds.append(",");
                    }
                }
            }
            String newID = "";

            if (isUpdate) {
                newID = valueIds.toString();
            } else {
                newID = valueIds.toString().replaceAll(new String(criterion.getCampaignFreeText().getValue()), " ");
            }

            resultDTO.put("criterionIsNull", new Boolean(false));
            resultDTO.put("campCriterionId", criterion.getCampaignCriterionId().toString());
            resultDTO.put("manyCriterias", "true");
            resultDTO.put("idList", newID.trim());
            return;

        } else {
            log.debug(" ... criterio NULL ... then create ...");
            resultDTO.put("criterionIsNull", new Boolean(true));
            return;
        }
    }

    public boolean isStateful() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
