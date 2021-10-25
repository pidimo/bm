package com.piramide.elwis.web.common.taglib;

import com.piramide.elwis.domain.campaignmanager.CampaignCriterion;
import com.piramide.elwis.domain.campaignmanager.CampaignCriterionHome;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.campaignmanager.el.Functions;
import com.piramide.elwis.web.common.util.FormatUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.html.BaseHandlerTag;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.*;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CriteriaValueTag.java 10245 2012-07-19 23:10:58Z miguel $
 */

public class CriteriaValueTag extends BaseHandlerTag {

    private Log log = LogFactory.getLog(this.getClass());

    String campCriterionValueId;
    String categoryId;
    String criterionId;

    public CriteriaValueTag() {
        this.campCriterionValueId = null;
        this.categoryId = null;
        this.criterionId = null;
    }

    public String getCriterionId() {
        return criterionId;
    }

    public void setCriterionId(String criterionId) {
        this.criterionId = criterionId;
    }

    public String getCampCriterionValueId() {
        return campCriterionValueId;
    }

    public void setCampCriterionValueId(String campCriterionValueId) {
        this.campCriterionValueId = campCriterionValueId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int doEndTag() throws JspException {
        doTag();
        return (EVAL_PAGE);
    }

    public void release() {
        super.release();
    }

    public int doStartTag() throws JspException {
        return (SKIP_BODY);
    }

    public void doTag() {
        StringBuffer sql1 = new StringBuffer();
        StringBuffer sql2 = new StringBuffer();
        StringBuffer sql3 = new StringBuffer();
        StringBuffer sql4 = new StringBuffer();
        List result = new ArrayList();
        List res = new ArrayList();
        List resp = new ArrayList();
        String ids = "";
        String id = "";
        String tablename = "";
        String fieldname = "";
        String field = "";
        String fieldType = "";
        StringBuffer label = new StringBuffer();
        int resultSize = 0;
        Locale locale = (Locale) Config.get(pageContext.getSession(), Config.FMT_LOCALE);

        if (criterionId != "") {
            CampaignCriterionHome criterionHome = (CampaignCriterionHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCRITERION);
            try {
                CampaignCriterion criterion = criterionHome.findByPrimaryKey(new Integer(criterionId));
                ids = new String(criterion.getCampaignFreeText().getValue());
            } catch (FinderException e) {
                log.debug(" ...  criteria not found ...");
            }

            if ((!"".equals(campCriterionValueId) || campCriterionValueId != null) && ("".equals(categoryId) || categoryId == null)) {
                sql4.append("select campcriterionvalueid from campcriterion where criterionid =").append(criterionId);
                res = QueryUtil.i.executeQuery(sql4.toString());
                Map mapi = (Map) res.get(0);
                sql1.append("select tablename, field, fieldname, fieldtype from campcriterionvalue where campcriterionvalueid=")
                        .append(label.append(mapi.get("campcriterionvalueid")));

                resp = QueryUtil.i.executeQuery(sql1.toString());

                Iterator ite = resp.iterator();
                label = new StringBuffer();

                if (ite.hasNext()) {
                    Map item = (Map) ite.next();
                    fieldname = (String) item.get("fieldname");
                    tablename = (String) item.get("tablename");
                    field = (String) item.get("field");
                    fieldType = (String) item.get("fieldtype");
                }

                StringTokenizer st = new StringTokenizer(ids, ",");
                resultSize = st.countTokens();
                if ("code".equals(fieldname)) {
                    if ("4".equals(ids)) {
                        label.append(JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Contact.customer"));
                    } else if ("16".equals(ids)) {
                        label.append(JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Contact.supplier"));
                    }
                } else if ("inuse".equals(fieldname)) {
                    if ("1".equals(ids)) {
                        label.append(JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "product.inUse"));
                    } else if ("0".equals(ids)) {
                        label.append(JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "product.notInUse"));
                    }
                } else if (CampaignConstants.FIELD_RELATION_EXISTS.equals(fieldType)) {
                    label = new StringBuffer();
                    label.append(Functions.composeRelationExistsOperatorValue((String) mapi.get("campcriterionvalueid"), (HttpServletRequest) pageContext.getRequest()));

                } else if (!"0".equals(fieldname) && !"4".equals(fieldType)) {  //only selection criteria
                    while (st.hasMoreTokens()) {
                        id = st.nextToken();
                        sql2 = new StringBuffer();
                        if ("recorduserid".equals(field)) {
                            sql2.append("select addressid as addressid from elwisuser ")
                                    .append("where userid=").append(id).append(";");
                            result = QueryUtil.i.executeQuery(sql2.toString());
                            sql2 = new StringBuffer();
                            Map map = (Map) result.get(0);
                            sql2.append("select name1 as name1, name2 as name2, name3 as name3 from address where addressid="
                                    + map.get("addressid")).append(";");
                        } else if ("employeeid".equals(field) || "partnerid".equals(field)) {
                            sql2.append("select addresstype as addresstype, addressid as field, name1 as name1, name2 as name2, name3 as name3 from address ")
                                    .append("where addressid=").append(id).append(";");
                        } else {
                            sql2.append("select ").append(fieldname).append(" from ").append(tablename)
                                    .append(" where ").append(field).append("=").append(id).append(" order by ").append(fieldname).append(" asc ");
                        }
                        result = QueryUtil.i.executeQuery(sql2.toString());

                        if (result.size() > 0) {
                            Iterator iterator = result.iterator();
                            Map map = (Map) iterator.next();
                            if ("employeeid".equals(field) || "partnerid".equals(field) || "recorduserid".equals(field)) {
                                label.append(getNameFormat(map.get("name1"), map.get("name2"), map.get("name3")));
                            } else {
                                label.append(map.get(fieldname));
                            }
                        } else if (result.size() == 0) {
                            label.append("<span style=\"color:#FF0000\">");
                            label.append(JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "CampaignCriteria.removeReference"));
                            label.append("</span>");
                        }

                        if (st.hasMoreTokens()) {
                            label.append(", ");
                        }
                    }
                } else if ("3".equals(fieldType)) {
                    //ids => criteria ids.
                    String dateFormat;
                    label = new StringBuffer();
                    StringTokenizer tokenizer = new StringTokenizer(ids, ",");
                    while (tokenizer.hasMoreTokens()) {
                        String a = tokenizer.nextToken();
                        Integer dateIni1 = new Integer(a);
                        Date date = DateUtils.integerToDate(dateIni1);
                        dateFormat = DateUtils.parseDate(date, JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "datePattern"));
                        label.append(dateFormat);
                        if (tokenizer.hasMoreTokens()) {
                            label.append(", ");
                        }
                    }
                } else if ("2".equals(fieldType)) {
                    label = new StringBuffer();
                    StringTokenizer tokenizer = new StringTokenizer(ids, ",");
                    while (tokenizer.hasMoreTokens()) {
                        String a = tokenizer.nextToken();
                        label.append(FormatUtils.formatingDecimalNumber(a, locale, 11, 2));
                        if (tokenizer.hasMoreTokens()) {
                            label.append(", ");
                        }
                    }
                } else {
                    label = new StringBuffer();
                    label.append(ids);
                }
            }
            //query from category table
            if ((!"".equals(categoryId) || categoryId != null) && ("".equals(campCriterionValueId) || campCriterionValueId == null)) {
                label = new StringBuffer();
                StringTokenizer st = new StringTokenizer(ids, ",");
                result = new ArrayList();
                resultSize = st.countTokens();
                String categoryType;
                sql3 = new StringBuffer();
                sql3.append("select categorytype as categorytype, categoryid as categoryid from category where categoryid=" + categoryId);
                result = QueryUtil.i.executeQuery(sql3.toString());
                Iterator i = result.iterator();
                Map m = (Map) i.next();
                categoryType = m.get("categorytype").toString();

                if ("5".equals(categoryType) || "6".equals(categoryType)) {
                    while (st.hasMoreTokens()) {
                        sql3 = new StringBuffer();
                        result = new ArrayList();
                        id = st.nextToken();
                        sql3.append("select categoryvaluename from categoryvalue where categoryvalueid=").append(id).append(" order by categoryvaluename asc");
                        result = QueryUtil.i.executeQuery(sql3.toString());
                        Iterator iterator = result.iterator();
                        if (result.size() > 0) {
                            Map map = (Map) iterator.next();
                            label.append(map.get("categoryvaluename"));
                        } else if (result.size() == 0) {
                            label.append("<span style=\"color:#FF0000\">");
                            label.append(JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "CampaignCriteria.removeReference"));
                            label.append("</span>");
                        }

                        if (st.hasMoreTokens()) {
                            label.append(", ");
                        }
                    }
                } else if ("3".equals(categoryType)) {
                    String dateFormat;
                    label = new StringBuffer();
                    StringTokenizer tokenizer = new StringTokenizer(ids, ",");
                    while (tokenizer.hasMoreTokens()) {
                        String a = tokenizer.nextToken();
                        Integer dateIni1 = new Integer(a);
                        Date date = DateUtils.integerToDate(dateIni1);
                        dateFormat = DateUtils.parseDate(date, JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "datePattern"));
                        label.append(dateFormat);
                        if (tokenizer.hasMoreTokens()) {
                            label.append(", ");
                        }
                    }
                } else if ("2".equals(categoryType)) {
                    label = new StringBuffer();
                    StringTokenizer tokenizer = new StringTokenizer(ids, ",");
                    while (tokenizer.hasMoreTokens()) {
                        String a = tokenizer.nextToken();
                        label.append(FormatUtils.formatingDecimalNumber(a, locale, 11, 2));
                        if (tokenizer.hasMoreTokens()) {
                            label.append(", ");
                        }
                    }
                } else {
                    label = new StringBuffer();
                    label.append(ids);
                }
            }
            JspWriter out = pageContext.getOut();
            try {
                out.print(label.toString());   //the one that send the values concatenated by commas to intefaz
            } catch (IOException e) {
                log.debug(" ... can't print ...");
            }

        }
    }

    private String getNameFormat(Object name1, Object name2, Object name3) {
        StringBuffer name = new StringBuffer();
        name.append(name1.toString());
        if (name2 != null && !CampaignConstants.EMPTY.equals(name2)) {
            name.append(" ").append(name2.toString());
        }
        return name.toString();
    }
}
