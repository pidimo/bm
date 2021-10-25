package com.piramide.elwis.web.contactmanager.taglib;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelSecurity;
import com.piramide.elwis.web.common.el.StyleClassFunctions;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.alfacentauro.fantabulous.exception.ListStructureNotFoundException;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGenerator;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGeneratorManager;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.ResponseUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Tag to show a table with person job information like organisations or persons in that one person
 * is a contact person (job information).
 *
 * @author Fernando Montaño
 * @version $Id: PersonJobInformationTableTag.java 12414 2016-02-05 15:33:50Z ruth $
 */

public class PersonJobInformationTableTag extends TagSupport {

    private Log log = LogFactory.getLog(this.getClass());
    String tableStyleClass;
    String tableWith;
    String tableAlign;
    String tdTitleStyleClass;
    String tdStyleClass;
    String titleKey;
    String addressId;
    String tableCellpadding;
    String tableCellSpacing;
    String tdEndStyleClass;
    boolean enableLinks;
    //mode bootstrap
    String mode;

    public PersonJobInformationTableTag() {
        this.tableStyleClass = null;
        this.tableWith = null;
        this.tableAlign = null;
        this.tdTitleStyleClass = null;
        this.titleKey = null;
        this.addressId = null;
        this.tdStyleClass = null;
        this.tableCellpadding = null;
        this.tableCellSpacing = null;
        this.tdEndStyleClass = null;
        enableLinks = true;
    }

    public int doEndTag() throws JspException {
        try {
            doTag();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return (EVAL_PAGE);
    }

    public int doStartTag() throws JspException {
        return (SKIP_BODY);

    }

    public void release() {
        super.release();
    }


    public void doTag() throws UnsupportedEncodingException {
        Locale locale = (Locale) Config.get(((HttpServletRequest) pageContext.getRequest()).getSession(), Config.FMT_LOCALE);
        User user = (User) pageContext.getSession().getAttribute(Constants.USER_KEY);
        Integer userId = new Integer(user.getValue(Constants.USERID).toString());

        StringBuffer html = new StringBuffer();

        //sql by fantabulous list
        FantabulousManager fantabulousManager = FantabulousManager.loadFantabulousManager(pageContext.getSession().getServletContext(), "/contacts");

        ListStructure listStructure = null;
        try {
            listStructure = fantabulousManager.getList("personJobInformationList");
        } catch (ListStructureNotFoundException e) {
            log.warn("Not found list........... personJobInformationList", e);
        }

        //add access right data level security if is required
        listStructure = AccessRightDataLevelSecurity.i.processAddressAccessRightByList(listStructure, userId);

        Parameters parameters = new Parameters();
        parameters.addSearchParameter("companyId", user.getValue(Constants.COMPANYID).toString());
        parameters.addSearchParameter("currentAddressId", addressId);
        parameters.setGenerateHiddenOrders(false);

        SqlGenerator generator = SqlGeneratorManager.newInstance();

        String query = generator.generate(listStructure, parameters);

        List result = QueryUtil.i.executeQuery(query);

        if (result.size() > 0) {
            if (Constants.UIMode.BOOTSTRAP.getConstant().equals(getMode())){
                bootstrapMode(locale, html, result);
            }else {
                dafaultMode(locale, html, result);
            }
        }

    }

    private void dafaultMode(Locale locale, StringBuffer html, List result) throws UnsupportedEncodingException {
        html.append("<TABLE border=\"0\" ")
                .append((tableCellpadding != null) ? "cellpadding=\"" + tableCellpadding + "\"" :
                        "cellpadding=\"0\" ")
                .append((tableCellSpacing != null) ? "cellspacing=\"" + tableCellSpacing + "\"" :
                        "cellspacing=\"0\" ")
                .append((tableStyleClass != null) ? "class=\"" + tableStyleClass + "\"" : " ")
                .append((tableWith != null) ? "width=\"" + tableWith + "\"" : " ")
                .append((tableAlign != null) ? "align=\"" + tableAlign + "\"" : ">")
                .append("\n");

        html.append("<TR>\n<TD width=\"60%\" ")
                .append(((tdTitleStyleClass != null) ? "class=\"" + tdTitleStyleClass + "\">" : "> "))
                .append(JSPHelper.getMessage(locale, titleKey))
                .append("</TD>\n")
                .append("<TD width=\"30%\" ")
                .append(((tdTitleStyleClass != null) ? "class=\"" + tdTitleStyleClass + "\">" : "> "))
                .append(JSPHelper.getMessage(locale, "ContactPerson.function"))
                .append("&nbsp;</TD>\n")
                .append("<TD width=\"10%\" ")
                .append(((tdTitleStyleClass != null) ? "class=\"" + tdTitleStyleClass + "\">" : "> "))
                .append(JSPHelper.getMessage(locale, "Common.active"))
                .append("&nbsp;</TD>\n")
                .append("</TR>");


        for (Iterator iterator = result.iterator(); iterator.hasNext();) {
            Map item = (Map) iterator.next();
            html.append("<TR>\n<TD ").append((tdStyleClass != null) ? "class=\"" + tdStyleClass + "\">\n" : ">\n");

            String name1 = (item.get("name1") == null ? "" : item.get("name1").toString());
            String name2 = (item.get("name2") == null ? "" : item.get("name2").toString());
            String name3 = (item.get("name3") == null ? "" : item.get("name3").toString());

            String link = null;
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            String url = request.getContextPath() + "/contacts/";
            HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

            if (item.get("addresstype").toString().equals(ContactConstants.ADDRESSTYPE_PERSON)) {
                url += "ContactPerson/Forward/Update.do?dto(addressId)=" + item.get("addressid") + "&dto(contactPersonId)=" + addressId +
                        "&contactId=" + item.get("addressid") + "&tabKey=Contacts.Tab.contactPersons&dto(addressType)=" + item.get("addresstype") +
                        "&dto(name1)=" + URLEncoder.encode(name1, Constants.CHARSET_ENCODING) + "&dto(name2)=" + URLEncoder.encode(name2, Constants.CHARSET_ENCODING) + "&module=contacts";
                link = ((enableLinks) ? "<a href=\"" + response.encodeURL(url) + "\">" : "")
                        + ResponseUtils.filter(name1) + ((item.get("name2") != null) ? ", " + ResponseUtils.filter(item.get("name2").toString()) : "") + ((enableLinks) ? "</a>" : "");
            } else {
                url += "ContactPerson/Forward/Update.do?dto(addressId)=" + item.get("addressid") + "&dto(contactPersonId)=" + addressId +
                        "&contactId=" + item.get("addressid") + "&tabKey=Contacts.Tab.contactPersons&dto(addressType)=" + item.get("addresstype") +
                        "&dto(name1)=" + URLEncoder.encode(name1, Constants.CHARSET_ENCODING) + "&dto(name2)=" + URLEncoder.encode(name2, Constants.CHARSET_ENCODING) + "&dto(name3)=" + URLEncoder.encode(name3, Constants.CHARSET_ENCODING) + "&module=contacts";

                link = ((enableLinks) ? "<a href=\"" + response.encodeURL(url) + "\">" : "") + ResponseUtils.filter(name1) + " " + ResponseUtils.filter(name2) +
                        " " + ResponseUtils.filter(name3) + ((enableLinks) ? "</a>" : "");

            }
            html.append(link);
            html.append("&nbsp;</TD>\n").append("<TD ").append((tdStyleClass != null) ? "class=\"" + tdStyleClass + "\">" : ">")
                    .append((item.get("function") != null ? item.get("function").toString() : "&nbsp;"))
                    .append("&nbsp;</TD>\n").append("<TD ").append((tdEndStyleClass != null) ? "class=\"" + tdEndStyleClass + "\">" : ">")
                    .append(("1".equals(item.get("active")) ? "<img align=\"center\" src=\"" +
                            pageContext.getSession().getAttribute("baselayout") + "/img/check.gif\"/>" : "&nbsp;"))
                    .append("</TD>\n")
                    .append("</TR>\n");

        }

        html.append("</TABLE>\n");
        JspWriter out = pageContext.getOut();
        try {
            out.println(new String(html));
        } catch (IOException e) {
            log.error("Error rendering the html", e);
        }
//ContactPerson/Forward/Update.do?dto(addressId)=10021&dto(contactPersonId)=7862&contactId=10021&index=1&dto(addressType)=0&dto(name1)=JaTun+S.R.L.&dto(name2)=Innovacion+%26+desarrollo&dto(name3)=&module=contacts
//ContactPerson/Forward/Update.do?dto(addressId)=10021&dto(contactPersonId)=7862&contactId=&dto(addressType)=0&dto(name1)=&dto%28name2%29=&dto%28name3%29=&module=contacts
    }

    private void bootstrapMode(Locale locale, StringBuffer html, List result) throws UnsupportedEncodingException {
        html.append("<div class=\"table-responsive\"><table ")
                .append((tableStyleClass != null) ? "class=\"" + tableStyleClass + "\"" : " ")
                .append((tableWith != null) ? "width=\"" + tableWith + "\"" : " ")
                .append((tableAlign != null) ? "align=\"" + tableAlign + "\"" : ">")
                .append("\n");

        html.append("<tr>\n<th class=\"listHeader\" width=\"60%\">")
                .append(JSPHelper.getMessage(locale, titleKey))
                .append("</th>\n")
                .append("<th class=\"listHeader\" width=\"30%\">")
                .append(JSPHelper.getMessage(locale, "ContactPerson.function"))
                .append("</th>\n")
                .append("<th class=\"listHeader\" width=\"10%\">")
                .append(JSPHelper.getMessage(locale, "Common.active"))
                .append(" </th>\n")
                .append("</tr>");
        for (Iterator iterator = result.iterator(); iterator.hasNext();) {
            Map item = (Map) iterator.next();
            html.append("<tr class=\"listRow\">\n<td class=\"listItem\"> ");
            String name1 = (item.get("name1") == null ? "" : item.get("name1").toString());
            String name2 = (item.get("name2") == null ? "" : item.get("name2").toString());
            String name3 = (item.get("name3") == null ? "" : item.get("name3").toString());

            String link = null;
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            String url = request.getContextPath() + "/contacts/";
            HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

            if (item.get("addresstype").toString().equals(ContactConstants.ADDRESSTYPE_PERSON)) {
                url += "ContactPerson/Forward/Update.do?dto(addressId)=" + item.get("addressid") + "&dto(contactPersonId)=" + addressId +
                        "&contactId=" + item.get("addressid") + "&tabKey=Contacts.Tab.contactPersons&dto(addressType)=" + item.get("addresstype") +
                        "&dto(name1)=" + URLEncoder.encode(name1, Constants.CHARSET_ENCODING) + "&dto(name2)=" + URLEncoder.encode(name2, Constants.CHARSET_ENCODING) + "&module=contacts";
                link = ((enableLinks) ? "<a href=\"" + response.encodeURL(url) + "\">" : "")
                        + ResponseUtils.filter(name1) + ((item.get("name2") != null) ? ", " + ResponseUtils.filter(item.get("name2").toString()) : "") + ((enableLinks) ? "</a>" : "");
            } else {
                url += "ContactPerson/Forward/Update.do?dto(addressId)=" + item.get("addressid") + "&dto(contactPersonId)=" + addressId +
                        "&contactId=" + item.get("addressid") + "&tabKey=Contacts.Tab.contactPersons&dto(addressType)=" + item.get("addresstype") +
                        "&dto(name1)=" + URLEncoder.encode(name1, Constants.CHARSET_ENCODING) + "&dto(name2)=" + URLEncoder.encode(name2, Constants.CHARSET_ENCODING) + "&dto(name3)=" + URLEncoder.encode(name3, Constants.CHARSET_ENCODING) + "&module=contacts";

                link = ((enableLinks) ? "<a href=\"" + response.encodeURL(url) + "\">" : "") + ResponseUtils.filter(name1) + " " + ResponseUtils.filter(name2) +
                        " " + ResponseUtils.filter(name3) + ((enableLinks) ? "</a>" : "");

            }
            html.append(link);
            html.append("</td>\n <td>")
                    .append((item.get("function") != null ? item.get("function").toString() : "&nbsp;"))
                    .append("</td>\n<td class=\"listItemCenter\">")
                    .append(("1".equals(item.get("active")) ? "<span class=\"" + StyleClassFunctions.getClassGlyphOk() + "\" ></span>" : "&nbsp;"))
                    .append("</td>\n")
                    .append("</tr>\n");

        }

        html.append("</tabla>\n</div>\n");
        JspWriter out = pageContext.getOut();
        try {
            out.println(new String(html));
        } catch (IOException e) {
            log.error("Error rendering the html", e);
        }
//ContactPerson/Forward/Update.do?dto(addressId)=10021&dto(contactPersonId)=7862&contactId=10021&index=1&dto(addressType)=0&dto(name1)=JaTun+S.R.L.&dto(name2)=Innovacion+%26+desarrollo&dto(name3)=&module=contacts
//ContactPerson/Forward/Update.do?dto(addressId)=10021&dto(contactPersonId)=7862&contactId=&dto(addressType)=0&dto(name1)=&dto%28name2%29=&dto%28name3%29=&module=contacts
    }

    public String getTableStyleClass() {
        return tableStyleClass;
    }

    public void setTableStyleClass(String tableStyleClass) {
        this.tableStyleClass = tableStyleClass;
    }

    public String getTableWith() {
        return tableWith;
    }

    public void setTableWith(String tableWith) {
        this.tableWith = tableWith;
    }

    public String getTableAlign() {
        return tableAlign;
    }

    public void setTableAlign(String tableAlign) {
        this.tableAlign = tableAlign;
    }

    public String getTdTitleStyleClass() {
        return tdTitleStyleClass;
    }

    public void setTdTitleStyleClass(String tdTitleStyleClass) {
        this.tdTitleStyleClass = tdTitleStyleClass;
    }

    public String getTdStyleClass() {
        return tdStyleClass;
    }

    public void setTdStyleClass(String tdStyleClass) {
        this.tdStyleClass = tdStyleClass;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getTableCellpadding() {
        return tableCellpadding;
    }

    public void setTableCellpadding(String tableCellpadding) {
        this.tableCellpadding = tableCellpadding;
    }

    public String getTableCellSpacing() {
        return tableCellSpacing;
    }

    public void setTableCellSpacing(String tableCellSpacing) {
        this.tableCellSpacing = tableCellSpacing;
    }

    public String getTdEndStyleClass() {
        return tdEndStyleClass;
    }

    public void setTdEndStyleClass(String tdEndStyleClass) {
        this.tdEndStyleClass = tdEndStyleClass;
    }

    public boolean isEnableLinks() {
        return enableLinks;
    }

    public void setEnableLinks(boolean enableLinks) {
        this.enableLinks = enableLinks;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
