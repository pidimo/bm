package com.piramide.elwis.web.campaignmanager.util;

import com.piramide.elwis.utils.*;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.catalogmanager.el.Functions;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.JavaScriptEncoder;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.util.*;

/**
 * This class is responsible of to generate dynamically the javaScript code to build the criteria tree.
 * User: yumi
 * Date: 10-oct-2006
 * Time: 17:59:09
 */

public class CampaignCriterionTreeGenerate {

    private Log log = LogFactory.getLog(this.getClass());

    public String addNode(HttpServletRequest request) {
        log.debug(" ... addNode function ...");
        StringBuffer a = new StringBuffer();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        int criteriaType = 1;
        List criteriaUtils = new ArrayList();
        List nodeList = new ArrayList();
        int nodeSize = 5;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        List rs;
        criteriaUtils.add(new CriteriaUtil(1, JSPHelper.getMessage(locale, "Category.customer")));
        criteriaUtils.add(new CriteriaUtil(2, JSPHelper.getMessage(locale, "Category.contactPerson")));
        criteriaUtils.add(new CriteriaUtil(3, JSPHelper.getMessage(locale, "Category.product")));
        criteriaUtils.add(new CriteriaUtil(4, JSPHelper.getMessage(locale, "Category.address")));
        criteriaUtils.add(new CriteriaUtil(CampaignConstants.CampaignCriteriaType.ADDRESS_CONTACTPERSON.getConstant(), JSPHelper.getMessage(locale, "Category.addressContactPerson")));
        /* order table nodes */
        nodeList = SortUtils.orderByProperty((ArrayList) criteriaUtils, "descriptionKey");
        /* add root */
        a.append("a.add(").append("0, -1,'").append(JSPHelper.getMessage(locale, "Campaign.criterias")).append("',")
                .append("'javascript:emptyNode()');\n");
        /* add table node (contact, contactPerson, product, customer) */
        for (Iterator iterator = nodeList.iterator(); iterator.hasNext();) {
            CriteriaUtil criteriaUtil = (CriteriaUtil) iterator.next();
            a.append("a.add(").append(criteriaUtil.getCriteriaType() + ", 0,'").append(JavaScriptEncoder.encode(criteriaUtil.getDescriptionKey())).append("',");
            a.append("'javascript:emptyNode()');\n");
        }

        criteriaUtils = new ArrayList();
        while (criteriaType <= 5) {
//fill custom fields for to order
            rs = QueryUtil.i.executeQuery("select descriptionkey as descriptionkey, field as field, tablename as tablename, fieldtype as fieldtype, fieldname as fieldname, campcriterionvalueid as campcriterionvalueid from campcriterionvalue where tableid =" + criteriaType + " order by descriptionkey");
            for (Iterator iterator = rs.iterator(); iterator.hasNext();) {
                Map result = (Map) iterator.next();
                criteriaUtils.add(new CriteriaUtil(new Integer(result.get("fieldtype").toString()),
                        -100, new Integer(result.get("campcriterionvalueid").toString()),
                        criteriaType, JSPHelper.getMessage(locale, (String) result.get("descriptionkey"))));
            }
            criteriaType++;
        }

        //fill categories for to order
        criteriaUtils.addAll(readCategories(CampaignConstants.CampaignCriteriaType.CUSTOMER.getConstant(), user));
        criteriaUtils.addAll(readCategories(CampaignConstants.CampaignCriteriaType.CONTACTPERSON.getConstant(), user));
        criteriaUtils.addAll(readCategories(CampaignConstants.CampaignCriteriaType.PRODUCT.getConstant(), user));
        criteriaUtils.addAll(readCategories(CampaignConstants.CampaignCriteriaType.PRODUCT.getConstant(), Integer.valueOf(ContactConstants.SALE_POSITION_CATEGORY), user));
        criteriaUtils.addAll(readCategories(CampaignConstants.CampaignCriteriaType.ADDRESS.getConstant(), user));
        criteriaUtils.addAll(readCategories(CampaignConstants.CampaignCriteriaType.ADDRESS_CONTACTPERSON.getConstant(), user));

/*  order field by table  */
        List resultList = SortUtils.orderListWithDuplicate(criteriaUtils, "descriptionKey");
/*  a.add(position, type, name, 'javascript:setCriteriaType(categoryId, campcriteriaId, typeCriteria)') */
        for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
            nodeSize++;
            CriteriaUtil util = (CriteriaUtil) iterator.next();
            /* is criteria defined */
            if (util.getCategoryId() == -100) {
                a.append("a.add(").append(nodeSize).append(",").append(util.getCriteriaType()).append(",'")
                        .append(JavaScriptEncoder.encode(util.getDescriptionKey())).append("',")
                        .append("'javascript:setCriteriaType(-100,").append(util.getCriteriaId()).append(", ")
                        .append(util.getType()).append(")');\n");
            } else if (util.getCriteriaId() == -100) { /* is category criteria */
                a.append("a.add(").append(nodeSize).append(",").append(util.getCriteriaType()).append(",'")
                        .append(JavaScriptEncoder.encode(util.getDescriptionKey())).append("',")
                        .append("'javascript:setCriteriaType(").append(util.getCategoryId()).append(",-100,")
                        .append(util.getType()).append(")');\n");
            }
        }
        return a.toString();
    }

    /* this method write javaScript code */
    public String draw(HttpServletRequest request) {
        log.debug(" ... draw function execute ...");

        StringBuffer buffer = new StringBuffer();
        buffer.append("<script type=\"text/javascript\">\n")
                .append("a = new dTree('a');\n")
                .append("a.config.useCookies=false;\n");
        buffer.append(addNode(request));
        buffer.append("document.write(a);\n")
                .append("  </script>\n");
        return buffer.toString();
    }

    private List<CriteriaUtil> readCategories(Integer criteriaType, User user) {
        return readCategories(criteriaType, criteriaType, user);
    }

    private List<CriteriaUtil> readCategories(Integer criteriaType, Integer tableId, User user) {
        List<CriteriaUtil> categoryCriteriaList = new ArrayList<CriteriaUtil>();

        String sql = "select tableid as tableid, categoryname as categoryname, categoryid as categoryid, categorytype as type from category where tableid =" + tableId
                + " and companyid =" + user.getValue("companyId") + " order by categoryname";
        List rss = QueryUtil.i.executeQuery(sql);

        for (Iterator iterator = rss.iterator(); iterator.hasNext();) {
            Map result = (Map) iterator.next();
            //added by ivan to filter attach, link and note types
            String type = result.get("type").toString();
            if (Functions.isAttach(type) || Functions.isFreText(type) || Functions.isLinkValue(type)) {
                continue;
            }

            categoryCriteriaList.add(new CriteriaUtil(new Integer(type),
                    new Integer(result.get("categoryid").toString()),
                    -100,
                    criteriaType, (String) result.get("categoryname")));
        }

        return categoryCriteriaList;
    }

}