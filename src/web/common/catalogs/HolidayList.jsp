<%@ page import="java.util.Map" %>
<%@ include file="/Includes.jsp" %>
<%
    String[] monthNames = JSPHelper.getOnlyMonthNames(request);
    String[] dayNames = JSPHelper.getOnlyDayNames(request, true);
    String[] numberWeeks = JSPHelper.getOnlyNumberWeekList(request);
    String ruleDateType = JSPHelper.getMessage(request, "holiday.rule.dateType");
    String ruleDayWeekType = JSPHelper.getMessage(request, "holiday.rule.dayWeekType");
%>
<app2:jScriptUrl url="obj" var="jsJumpUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(countryId)" value="field"/>
</app2:jScriptUrl>
<script language="JavaScript">
    <!--
    function jump(obj)
    {
        field = document.getElementById('searchForm').country.value;
        holidayField = document.getElementById('searchForm').holiday.value;
        window.location=${jsJumpUrl};
    }
    //-->

</script>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>
<html:form action="/Holiday/List.do" styleId="searchForm" focus="parameter(countryId)">
    <TABLE border="0" cellpadding="3" cellspacing="0" width="70%" class="searchContainer" align="center">
        <tr>
            <td class="label">
                <fmt:message key="Contact.country"/>
            </td>
            <td align="left" class="contain">
                <fmt:message key="holiday.select.indepent" var="indepent"/>
                <fanta:select property="parameter(countryId)" tabIndex="1" listName="countryList" labelProperty="name"
                              valueProperty="id" firstEmpty="true" styleClass="mediumSelect" styleId="country">
                    <fanta:addItem key="indepent" value="${indepent}"/>
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Common.title"/>
            </td>
            <td align="left" class="contain">
                <html:text property="parameter(title)" styleClass="mediumText" titleKey="Common.title" tabindex="2"
                           styleId="holiday"/>
                <html:submit styleClass="button">
                    <fmt:message key="Common.go"/>
                </html:submit>
                &nbsp;
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center" class="alpha">
                <fanta:alphabet action="Holiday/List.do" parameterName="titleAlpha" onClick="jump(this);return false;"/>
            </td>
        </tr>
    </table>
    <app2:checkAccessRight functionality="HOLIDAY" permission="CREATE">
        <TABLE border="0" cellpadding="2" cellspacing="0" width="70%" class="container" align="center">
            <TR>
                <td colspan="2" class="button">
                    <html:submit property="parameter(new)" styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </td>
            </TR>
        </TABLE>
    </app2:checkAccessRight>
</html:form>
<TABLE border="0" cellpadding="0" cellspacing="0" width="70%" class="container" align="center">
    <tr>
        <td align="center">
            <fanta:table width="100%" id="Holiday" action="Holiday/List.do" imgPath="${baselayout}">
                <c:set var="editAction"
                       value="Holiday/Forward/Update.do?dto(holidayId)=${Holiday.id}&dto(title)=${app2:encode(Holiday.title)}"/>
                <c:set var="deleteAction"
                       value="Holiday/Forward/Delete.do?dto(holidayId)=${Holiday.id}&dto(title)=${app2:encode(Holiday.title)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="HOLIDAY" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                            headerStyle="listHeader" image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="HOLIDAY" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="title" action="${editAction}" styleClass="listItem" title="holiday.title"
                                  headerStyle="listHeader" width="40%" orderable="true" maxLength="25"/>
                <fanta:dataColumn name="country" styleClass="listItem" title="City.country" headerStyle="listHeader"
                                  width="20%" orderable="true"/>
                <fanta:dataColumn name="" styleClass="listItem2" title="holiday.rule" headerStyle="listHeader"
                                  width="35%" orderable="false" renderData="false">
                    <%
                        try {
                            Map holiday = (Map) pageContext.getAttribute("Holiday");
                            String day = (String) holiday.get("day");
                            String month;
                            String occurrence = (String) holiday.get("occurrence");
                            String type = (String) holiday.get("type");
                            String moveToMonday = (String) holiday.get("moveToMonday");

                            if (occurrence == null) {
                                occurrence = "";
                            }

                            month = monthNames[Integer.parseInt((String) holiday.get("month")) - 1];

                            String ruleKey = ruleDateType;
                            String occurrenceKey = "\\{year\\}";


                            if ("1".equals(type)) {
                                //dayWeekType
                                ruleKey = ruleDayWeekType;
                                occurrenceKey = "\\{week\\}";
                                day = dayNames[Integer.parseInt(day) - 1];

                                if (occurrence.length() > 0) {
                                    occurrence = numberWeeks[Integer.parseInt(occurrence) - 1];
                                }
                            }

                            out.print(ruleKey.replaceFirst("\\{day\\}", day).replaceFirst("\\{month\\}", month).replaceFirst(
                                    occurrenceKey, occurrence));

                            if ("1".equals(moveToMonday)) {
                                out.print("&nbsp;(");
                                out.print(JSPHelper.getMessage(request, "holiday.celebration"));
                                out.print(")");
                            }
                        } catch (Exception e) {
                        }
                    %>
                    &nbsp;
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
    <%--<tr>
        <td <c:out value="${sessionScope.listshadow}" escapeXml="false" />><img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
    </tr>--%>
</TABLE>
</td>
</tr>
</table>
