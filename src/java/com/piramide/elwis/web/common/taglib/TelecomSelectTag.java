package com.piramide.elwis.web.common.taglib;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.contactmanager.form.AddressContactPersonHelper;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.*;

/**
 * @author Fernando Montaño
 * @version TelecomSelectTag.java, v 2.0 Jun 18, 2004 11:13:00 AM
 */

public class TelecomSelectTag extends TagSupport {

    Log log = LogFactory.getLog(TelecomSelectTag.class);
    String name;
    String property;
    String numberColumn;
    String styleClass;
    String tabindex;
    boolean readonly;
    String telecomType;
    String addressId;
    String contactPersonId;
    String maxLength;
    String optionStyleClass;
    boolean showPrivate; //used for contact person only (enable show contact person private numbers display)
    String telecomIdColumn;
    boolean showDescription;
    String styleId;
    String resultIsEmptyKey;
    boolean groupedByTelecomType;
    boolean firstEmpty;
    boolean selectPredetermined;
    boolean showOwner; //used for contact person only (enable show contact person private numbers display)

    boolean alreadyHasSelected; //flag to verify if this tag alredy has predetermined selected

    public TelecomSelectTag() {
        name = "org.apache.struts.taglib.html.BEAN";
        property = null;
        numberColumn = null;
        styleClass = null;
        tabindex = null;
        telecomType = null;
        addressId = null;
        contactPersonId = null;
        maxLength = null;
        optionStyleClass = null;
        showPrivate = false;
        telecomIdColumn = null;
        showDescription = false;
        styleId = null;
        resultIsEmptyKey = null;
        groupedByTelecomType = false;
        firstEmpty = false;
        selectPredetermined = false;
        alreadyHasSelected = false;
        showOwner = false;
        readonly = false;

    }

    public int doEndTag() throws JspException {
        doTag();
        return (EVAL_PAGE);
    }

    /**
     * @throws javax.servlet.jsp.JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        alreadyHasSelected = false;
        /*evaluateExpressions();*/
        return (SKIP_BODY);

    }

    public void release() {
        super.release();
    }


    public void doTag() {

        if (addressId != null && !"".equals(addressId)) {

            try {
                User user = (User) pageContext.getSession().getAttribute(Constants.USER_KEY);
                String beanPropertyId;
                Object bean = null;
                try {
                    bean = RequestUtils.lookup(pageContext, name, null);
                    String beanProperty[] = BeanUtils.getArrayProperty(bean, property);
                    beanPropertyId = beanProperty[0];
                } catch (Exception e) {
                    beanPropertyId = "";
                }

                String sql = buildSql(addressId, contactPersonId, selectPredetermined, user);
                List result = QueryUtil.i.executeQuery(sql);

                // to show the private address numbers of a contactperson
                List showPrivateResult = new ArrayList();
                if (showPrivate && contactPersonId != null && !"".equals(contactPersonId)) {
                    String showPrivateSQL = buildSql(contactPersonId, null, false, user);
                    showPrivateResult = QueryUtil.i.executeQuery(showPrivateSQL);
                }

                // additionally to show the private numbers of a Address
                List showOwnerResult = new ArrayList();
                if (showOwner && contactPersonId != null && !"".equals(contactPersonId)) {
                    String showOwnerSQL = buildSql(addressId, null, false, user);
                    showOwnerResult = QueryUtil.i.executeQuery(showOwnerSQL);
                }

                try {
                    JspWriter out = pageContext.getOut();

                    List allResult = new ArrayList();
                    allResult.addAll(result);
                    allResult.addAll(showPrivateResult);
                    allResult.addAll(showOwnerResult);

                    if (readonly) {
                        Iterator iterator = allResult.iterator();
                        while (iterator.hasNext()) {
                            Map item = (Map) iterator.next();
                            String value = getOptionValue(item);
                            if (value.equals(beanPropertyId)) {
                                out.println("<INPUT TYPE=\"HIDDEN\"" + " NAME=\"" + property + "\" VALUE=\"" + value + "\">");
                                out.print(prepareStringToPrint(item.get("number"), item.get("description")));
                                break;
                            }
                        }

                    } else {
                        if (resultIsEmptyKey != null && allResult.isEmpty()) {
                            out.print(JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), resultIsEmptyKey));

                        } else {

                            out.println("<SELECT NAME=\"" + property + "\"" + ((styleClass != null) ? " class=\"" +
                                    styleClass + "\" " : "") + ((tabindex != null) ? " tabindex=\"" + tabindex + "\"" : "") +
                                    (styleId != null ? " id=\"" + styleId + "\" " : "") +
                                    ((readonly) ? " disabled = \"" + "disabled\"" : "") +
                                    " >");

                            if (result.isEmpty() || firstEmpty) {
                                out.print("<OPTION VALUE=\"\"" + ((optionStyleClass != null) ? " class=\"" +
                                        optionStyleClass + "\" " : "") + ">&nbsp;</OPTION>\n");
                            }

                            boolean setPredetermined = !(containsBeanPropertyIdInResult(allResult, beanPropertyId));

                            out.println(renderOptions(result, beanPropertyId, setPredetermined));

                            //if show private is enabled, then show the private number in the select list.
                            if (showPrivate && !showPrivateResult.isEmpty()) {
                                out.println(composeGroupSeparator());
                                out.println(renderOptions(showPrivateResult, beanPropertyId, setPredetermined));
                            }

                            //if show owner is enabled
                            if (showOwner && !showOwnerResult.isEmpty()) {
                                out.println(composeGroupSeparator());
                                out.println(renderOptions(showOwnerResult, beanPropertyId, setPredetermined));
                            }

                            out.println("</SELECT>");
                        }
                    }
                } catch (java.io.IOException ex) {
                    log.error("Error trying to rendering the select telecom element", ex);
                }
            } catch (Exception e) {
                log.error("Error retrieving the telecom list", e);
            }
        }
    }

    private String buildSql(String idAddress, String idContactPerson, boolean withPredetermined, User user) {

        String withContactPerson = " IS NULL";
        if (idContactPerson != null && !"".equals(idContactPerson)) {
            withContactPerson = "=" + idContactPerson;
        }

        StringBuffer sql = new StringBuffer()
                .append("SELECT ")
                .append(telecomIdColumn != null ? "tc." + telecomIdColumn + " As id, " : "")
                .append("tc.")
                .append(numberColumn)
                .append(" As number,")
                .append(withPredetermined ? " tc.predetermined As predetermined, " : "")
                .append(" tc.description As description, ")
                .append(" tct.telecomtypeid As telecomtypeid ")
                .append(" FROM  telecom As tc, telecomtype As tct  ")
                .append(" WHERE tc.companyid=")
                .append(user.getValue("companyId"))
                .append(" AND tc.telecomtypeid = tct.telecomtypeid")
                .append(telecomType != null ? " AND tct.type = '" + telecomType + "'" : "")
                .append(" AND tc.addressid=")//show all telecoms
                .append(idAddress)
                .append(" AND tc.contactpersonid")
                .append(withContactPerson)
                .append(telecomType != null ? " ORDER BY " + (withPredetermined ? "tct.telecomtypepos, " : "") + "tc." + numberColumn : " ORDER BY tct.telecomtypepos, tc." +
                        numberColumn)
                .append(" ASC;");

        return new String(sql);
    }

    /**
     * Prepare the output string for print, putting "..." if maxlenght is exceeded.
     *
     * @param number      the telecom number
     * @param description the telecom description
     * @return the choped string
     */
    private String prepareStringToPrint(Object number, Object description) {
        String result = "";
        if (number != null && description != null) {
            result = number.toString();
            if (!"".equals(String.valueOf(description).trim()) && showDescription) {
                result += " (" + description + ")";
            }
            if (maxLength != null) {
                return JSPHelper.chopString(result, Integer.parseInt(maxLength));
            } else {
                return result;
            }
        } else if (number != null && description == null) { //description is null
            result = number.toString();
            if (maxLength != null) {
                return JSPHelper.chopString(result, Integer.parseInt(maxLength));
            } else {
                return result;
            }
        }
        return result;
    }

    private String renderOptions(List result, String beanPropertyId, boolean setPredetermined) {
        StringBuffer html = new StringBuffer();

        if (!groupedByTelecomType) {
            Iterator it = result.iterator();
            while (it.hasNext()) {
                Map item = (Map) it.next();
                html.append(renderOption(item, beanPropertyId, setPredetermined)); //render option
            }
        } else {

            Map groups = processWithGroups(result);
            for (Iterator iterator = groups.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                html.append("<OPTGROUP label=\"")
                        .append(getTelecomTypeName(entry.getKey()))
                        .append("\">");
                List items = (LinkedList) entry.getValue();
                for (Iterator iterator1 = items.iterator(); iterator1.hasNext();) {
                    Map item = (Map) iterator1.next();
                    html.append(renderOption(item, beanPropertyId, setPredetermined));
                }
                html.append("</OPTGROUP>\n");
            }
        }
        return new String(html);
    }

    /**
     * Render OPTION html tag
     *
     * @param item           map of items
     * @param beanPropertyId the bean property
     * @return the string of option rendered
     */
    private String renderOption(Map item, String beanPropertyId, boolean setPredetermied) {
        StringBuffer output = new StringBuffer();
        String optionValue = getOptionValue(item);
        output.append("\t<OPTION value=\"")
                .append(optionValue)
                .append("\"")
                .append((optionStyleClass != null) ? " class=\"" + optionStyleClass + "\" " : "");
        if (optionValue.equals(beanPropertyId)) {
            output.append(" selected");
        } else if (setPredetermied && hasPredeterminedSelect(item)) {
            output.append(" selected");
        }
        output.append(">")
                .append(prepareStringToPrint(item.get("number"), item.get("description")))
                .append("</OPTION>\n");
        return new String(output);
    }

    private String getOptionValue(Map item) {
        return String.valueOf(((telecomIdColumn != null) ? item.get("id") : item.get("number")));
    }

    private String composeGroupSeparator() {
        StringBuffer html = new StringBuffer();
        html.append("<OPTGROUP label=\"").append("---").append("\">");
        html.append("</OPTGROUP>\n");
        return new String(html);
    }

    /**
     * return the telecomtype name
     *
     * @param telecomTypeId the id of telecomtype
     * @return the telecomtype name
     */
    private String getTelecomTypeName(Object telecomTypeId) {
        Map telecomTypeNamesById = null;
        if (pageContext.getAttribute("telecomTypeNamesById") != null) {
            telecomTypeNamesById = (HashMap) pageContext.getAttribute("telecomTypeNamesById");
        } else {
            telecomTypeNamesById = new HashMap();
            pageContext.setAttribute("telecomTypeNamesById", telecomTypeNamesById);
        }

        if (telecomTypeNamesById.containsKey(telecomTypeId)) {
            return (String) telecomTypeNamesById.get(telecomTypeId);
        } else {
            String telecomTypeName = AddressContactPersonHelper.getTelecomTypeName(((HttpServletRequest)
                    pageContext.getRequest()), telecomTypeId);
            telecomTypeNamesById.put(telecomTypeId, telecomTypeName);
            return telecomTypeName;
        }
    }

    /**
     * Group the list of telecoms by telecomtypeid
     *
     * @param list the result query list
     * @return a map having telecomTypeId as key and a list of same items as value.
     */
    private Map processWithGroups(Collection list) {
        Map groups = new LinkedHashMap();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Map item = (Map) it.next();
            Object telecomTypeId = item.get("telecomtypeid");
            if (groups.containsKey(telecomTypeId)) {
                List groupItems = (LinkedList) groups.get(telecomTypeId);
                groupItems.add(item);
            } else {
                List groupItems = new LinkedList();
                groupItems.add(item);
                groups.put(telecomTypeId, groupItems);
            }
        }
        return groups;
    }

    /**
     * verif if selectpredetermined attribute is enable and if is predetermined telecom
     *
     * @param item map of sql execute result
     * @return true or false
     */
    private boolean hasPredeterminedSelect(Map item) {

        if (!alreadyHasSelected && telecomType != null && !groupedByTelecomType && selectPredetermined && item.containsKey("predetermined")) {
            String value = (String) item.get("predetermined");
            if ("1".equals(value)) {
                alreadyHasSelected = true;
                return true;
            }
        }
        return false;
    }

    /**
     * verif if result contains beanPropertyId
     *
     * @param result
     * @param beanPropertyId
     * @return true or false
     */
    private boolean containsBeanPropertyIdInResult(List result, String beanPropertyId) {
        for (Iterator iterator = result.iterator(); iterator.hasNext();) {
            Map item = (Map) iterator.next();
            String optionValue = String.valueOf(((telecomIdColumn != null) ? item.get("id") : item.get("number")));
            if (optionValue.equals(beanPropertyId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFirstEmpty() {
        return firstEmpty;
    }

    public void setFirstEmpty(boolean firstEmpty) {
        this.firstEmpty = firstEmpty;
    }

    public boolean isGroupedByTelecomType() {
        return groupedByTelecomType;
    }

    public void setGroupedByTelecomType(boolean groupedByTelecomType) {
        this.groupedByTelecomType = groupedByTelecomType;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getNumberColumn() {
        return numberColumn;
    }

    public void setNumberColumn(String numberColumn) {
        this.numberColumn = numberColumn;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getTabindex() {
        return tabindex;
    }

    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String getTelecomType() {
        return telecomType;
    }

    public void setTelecomType(String telecomType) {
        this.telecomType = telecomType;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getContactPersonId() {
        return contactPersonId;
    }

    public void setContactPersonId(String contactPersonId) {
        this.contactPersonId = contactPersonId;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public String getOptionStyleClass() {
        return optionStyleClass;
    }

    public void setOptionStyleClass(String optionStyleClass) {
        this.optionStyleClass = optionStyleClass;
    }

    public boolean isShowPrivate() {
        return showPrivate;
    }

    public void setShowPrivate(boolean showPrivate) {
        this.showPrivate = showPrivate;
    }

    public String getTelecomIdColumn() {
        return telecomIdColumn;
    }

    public void setTelecomIdColumn(String telecomIdColumn) {
        this.telecomIdColumn = telecomIdColumn;
    }

    public boolean isShowDescription() {
        return showDescription;
    }

    public void setShowDescription(boolean showDescription) {
        this.showDescription = showDescription;
    }

    public void setResultIsEmptyKey(String showText) {
        this.resultIsEmptyKey = showText;
    }

    public String getResultIsEmptyKey() {
        return resultIsEmptyKey;
    }

    public boolean isSelectPredetermined() {
        return selectPredetermined;
    }

    public void setSelectPredetermined(boolean selectPredetermined) {
        this.selectPredetermined = selectPredetermined;
    }

    public boolean isShowOwner() {
        return showOwner;
    }

    public void setShowOwner(boolean showOwner) {
        this.showOwner = showOwner;
    }
}
