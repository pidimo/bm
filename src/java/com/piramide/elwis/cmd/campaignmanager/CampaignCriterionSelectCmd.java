package com.piramide.elwis.cmd.campaignmanager;

import net.java.dev.strutsejb.EJBCommand;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;

import javax.ejb.SessionContext;
import java.util.*;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CampaignCriterionSelectCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignCriterionSelectCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        String field = "";
        String tablename = "";
        String typefield = "";
        String fieldname = "";
        List rs = null;
        String sql = "";
        String description;
        ArrayList list = new ArrayList();

        String value = paramDTO.getAsString("values");
        if (value == null || "-1".equals(value) || "".equals(value)) {

            resultDTO.put("campaignId", paramDTO.get("campaignId"));
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("Campaign.emptyList");
            resultDTO.setForward("fail");
            return;
        }

        StringTokenizer tokenizer = new StringTokenizer(value, ",");
        field = tokenizer.nextToken();
        tablename = tokenizer.nextToken();
        typefield = tokenizer.nextToken();
        fieldname = tokenizer.nextToken();
        String title = tokenizer.nextToken();
        String campCriterionValueId = tokenizer.nextToken();

        if ("0".equals(typefield)) {
            if (!"categoryvalue".equals(tablename)) {
                sql = "select " + fieldname + (fieldname.equals(field) ? "" : "," + field) + " from " + tablename + " where companyid= " + paramDTO.getAsInt("companyId") + " order by " + fieldname;
            } else {
                sql = "select " + fieldname + ", categoryvalueid from " + tablename + " where categoryid=" + field + " and companyid=" + paramDTO.getAsInt("companyId") + " order by " + fieldname;

                resultDTO.put("isCategory", new Integer(1));
                resultDTO.put("categoryId", field);
                resultDTO.put("campCriterionValueId", campCriterionValueId);
                field = "categoryvalueid";
            }
            rs = QueryUtil.i.executeQuery(sql);
        }
        if (!"0".equals(typefield)) {
            resultDTO.put("field", field);
            resultDTO.put("tablename", tablename);
            resultDTO.put("typefield", typefield);
            resultDTO.put("title", title);
            resultDTO.put("campCriterionValueId", campCriterionValueId);
            resultDTO.setForward("campaignCriterionSimple");
        } else if ("0".equals(typefield)) {
            for (Iterator iterator = rs.iterator(); iterator.hasNext();) {
                Map result = (Map) iterator.next();
                description = (String) result.get(fieldname);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append((String) result.get(field));
                list.add(new LabelValueBean(description, stringBuffer.toString()));
            }
            resultDTO.put("title", title);
            resultDTO.put("campCriterionValueId", campCriterionValueId);
            resultDTO.put("valueList", list);// order to init the left list.
            resultDTO.setForward("campaignCriterion");
        }
    }

    public boolean isStateful() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
