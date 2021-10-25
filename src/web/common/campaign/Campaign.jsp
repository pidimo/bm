<%@ page import="com.piramide.elwis.utils.DateUtils,
                 net.java.dev.strutsejb.web.DefaultForm" %>
<%@ page import="java.util.Date" %>

<%@ include file="/Includes.jsp" %>

<fmt:message var="datePattern" key="datePattern" scope="request"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:formatNumber pattern="${numberFormat}" value="${0}" var="test" type="number"/>
<c:set var="statusList" value="${app2:getCampaignStatusList(pageContext.request)}"/>

<c:set var="userAddresId" value="${sessionScope.user.valueMap['userAddressId']}" scope="request"/>
<%
    String datePattern = (String) request.getAttribute("datePattern");
    String showDate = DateUtils.parseDate(new Date(), datePattern);

    Integer integetDate = com.piramide.elwis.utils.DateUtils.dateToInteger(new Date());
    request.setAttribute("showDate", showDate);
    request.setAttribute("integerDate", integetDate);


    String op = (String) request.getAttribute("op");
    if ("create".equals(op)) {
        Integer userAddressId = new Integer(request.getAttribute("userAddresId").toString());
        DefaultForm f = (DefaultForm) request.getAttribute("form");
        if (null != f)
            f.setDto("employeeId", userAddressId);
    }
%>


<html:form action="${action}" focus="dto(campaignName)">
<table width="85%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>
<table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
    <TR>
        <TD class="button" nowrap>
            <app2:securitySubmit operation="${op}" functionality="CAMPAIGN" styleClass="button" tabindex="13">
                ${button}
            </app2:securitySubmit>
            <c:if test="${'update' != op}">
                <html:cancel styleClass="button" tabindex="14">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </c:if>
        </TD>
    </TR>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
<html:hidden property="dto(op)" value="${op}"/>

<c:if test="${'update' == op || 'delete' == op}">
    <html:hidden property="dto(version)"/>
    <html:hidden property="dto(campaignId)"/>
</c:if>

<tr>
    <TD colspan="4" class="title">
        <c:out value="${title}"/>
    </TD>
</tr>
<c:choose>
<c:when test="${'create'== op}">
<tr>
    <TD width="20%" class="label">
        <fmt:message key="Campaign.mailing"/>
    </TD>
    <TD width="30%" class="contain">
        <app:text property="dto(campaignName)" styleClass="middleText" maxlength="40"
                  tabindex="1"
                  view="${op == 'delete'}"/>
    </TD>
    <TD width="20%" class="label">
        <fmt:message key="Campaign.dateCreation"/>
    </TD>
    <TD width="30%" class="contain">
            ${showDate}
        <html:hidden property="dto(recordDate)" value="${integerDate}"/>
    </TD>
</tr>
<tr>
    <tags:initSelectPopup/>

    <td class="label">
        <fmt:message key="Campaign.copyOf"/>
    </td>
    <td class="contain">
        <html:hidden property="dto(copyCampaignId)" styleId="fieldCampaignCopyId_id"/>
        <app:text property="dto(copyCampaignName)" styleClass="mediumText" maxlength="40" readonly="true"
                  styleId="fieldCampaignName_id"/>
        <tags:selectPopup url="/campaign/Campaign/Forward/Copy.do" name="searchAddress" titleKey="Common.search"
                          hide="${op != 'create'}" heigth="500" width="750" tabindex="2"/>
        <tags:clearSelectPopup keyFieldId="fieldCampaignCopyId_id" nameFieldId="fieldCampaignName_id"
                               titleKey="Common.clear" hide="${op != 'create'}" tabindex="2"/>
    </td>
    <td class="label">
        <fmt:message key="Campaign.updatedDate"/>
    </td>
    <TD class="contain">
        <c:if test="${null != campaignForm.dtoMap['changeDate']}">
            <c:set var="updateDate" value="${campaignForm.dtoMap['changeDate']}" scope="request"/>
            <%
                Integer i = new Integer(request.getAttribute("updateDate").toString());
                String fd = DateUtils.parseDate(DateUtils.integerToDate(i), datePattern);
                request.setAttribute("formattedDate", fd);
            %>
            ${formattedDate}
        </c:if>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="Campaign.responsibleEmployee"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(employeeId)" listName="employeeBaseList" tabIndex="3"
                      labelProperty="employeeName" valueProperty="employeeId" styleClass="middleSelect"
                      value="${sessionScope.user.valueMap['userAddressId']}" module="/contacts">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
    <td class="label">
        <fmt:message key="Campaign.closeDate"/>
    </td>
    <td class="contain">
        <app:dateText
                property="dto(closeDate)"
                datePatternKey="${datePattern}" view="${true}" styleId="dateTextId_${i.count}"
                styleClass="dateText" tabindex="9"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Campaign.type"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(typeId)" listName="campaignTypeList"
                      valueProperty="id" labelProperty="title" firstEmpty="true"
                      styleClass="mediumSelect" module="/catalogs" tabIndex="4" readOnly="${'delete' == op}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
    <td class="label">
        <fmt:message key="Campaign.budgetedCost"/>
    </td>
    <td class="contain">
        <app:numberText property="dto(budgetCost)" styleClass="numberText" maxlength="10"
                        view="${'delete' == op}" numberType="decimal" maxInt="10" maxFloat="2" tabindex="6"/>
    </td>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="Campaign.status"/>
    </TD>
    <TD class="contain">
        <html:select property="dto(status)" styleClass="mediumSelect" readonly="${op == 'delete'}" tabindex="5">
            <html:options collection="statusList" property="value" labelProperty="label"/>
        </html:select>
    </TD>
    <td class="label">
        <fmt:message key="Campaign.profits"/>
    </td>
    <td class="contain">
            ${test}
        <html:hidden property="dto(awaitedUtility)" value="0"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Campaign.totalContacts"/>
    </td>
    <td class="contain">
        <app:text
                property="dto(numberContacts)"
                styleClass="numberText"
                view="${true}"/>
    </td>
    <td class="label">
        <fmt:message key="Campaign.realCost"/>
    </td>
    <td class="contain">
            ${test}
    </td>
</tr>
</c:when>
<c:otherwise>
<tr>
    <TD width="20%" class="label">
        <fmt:message key="Campaign.mailing"/>
    </TD>
    <TD width="30%" class="contain">
        <app:text property="dto(campaignName)" styleClass="middleText" maxlength="40"
                  tabindex="1"
                  view="${op == 'delete'}"/>
    </TD>
    <TD width="20%" class="label">
        <fmt:message key="Campaign.dateCreation"/>
    </TD>
    <TD width="30%" class="contain">
        <c:set var="recordDate" value="${campaignForm.dtoMap['recordDate']}" scope="request"/>
        <%
            Integer i = new Integer(request.getAttribute("recordDate").toString());
            String fd = DateUtils.parseDate(DateUtils.integerToDate(i), datePattern);
            request.setAttribute("formattedRecordDate", fd);
        %>
        <html:hidden property="dto(recordDate)"/>
            ${formattedRecordDate}
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="Campaign.responsibleEmployee"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(employeeId)" listName="employeeBaseList" tabIndex="2"
                      labelProperty="employeeName" valueProperty="employeeId" styleClass="middleSelect"
                      readOnly="${op == 'delete'}" module="/contacts">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
    <td class="label">
        <fmt:message key="Campaign.updatedDate"/>
    </td>
    <TD class="contain">
        <c:if test="${null != campaignForm.dtoMap['changeDate']}">
            <c:set var="updateDate" value="${campaignForm.dtoMap['changeDate']}" scope="request"/>
            <%
                Integer ii = new Integer(request.getAttribute("updateDate").toString());
                String fdd = DateUtils.parseDate(DateUtils.integerToDate(ii), datePattern);
                request.setAttribute("formattedDate", fdd);
            %>
            ${formattedDate}
        </c:if>
        <html:hidden property="dto(changeDate)" value="${integerDate}"/>
    </TD>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Campaign.type"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(typeId)" listName="campaignTypeList"
                      valueProperty="id" labelProperty="title" firstEmpty="true"
                      styleClass="mediumSelect" module="/catalogs" tabIndex="3" readOnly="${'delete' == op}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
    <td class="label">
        <fmt:message key="Campaign.closeDate"/>
    </td>
    <td class="contain">
        <app:dateText
                property="dto(closeDate)"
                datePatternKey="${datePattern}" view="${true}" styleId="dateTextId_${i.count}"
                styleClass="dateText" tabindex="9"/>
    </td>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="Campaign.status"/>
    </TD>
    <TD class="contain">
        <html:select property="dto(status)" styleClass="mediumSelect" readonly="${op == 'delete'}" tabindex="4">
            <html:options collection="statusList" property="value" labelProperty="label"/>
        </html:select>
    </TD>
    <td class="label">
        <fmt:message key="Campaign.budgetedCost"/>
    </td>
    <td class="contain">
        <app:numberText property="dto(budgetCost)" styleClass="numberText" maxlength="12"
                        view="${'delete' == op}" numberType="decimal" maxInt="10" maxFloat="2" tabindex="10"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Campaign.totalContacts"/>
    </td>
    <td class="contain">
        <app:text
                property="dto(numberContacts)"
                styleClass="numberText"
                view="${true}" tabindex="6"/>
    </td>
    <td class="label">
        <fmt:message key="Campaign.profits"/>
    </td>
    <td class="contain">
        <app:numberText property="dto(awaitedUtility)" styleClass="numberText" maxlength="12"
                        view="true" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Campaign.realCost"/>
    </td>
    <td class="contain" colspan="3">
        <app:numberText property="dto(realCost)" styleClass="numberText" maxlength="12"
                        view="true" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
</tr>
</c:otherwise>
</c:choose>
<tr>
    <TD class="topLabel" colspan="4">
        <fmt:message key="Campaign.remark"/>
        <html:textarea property="dto(remarkValue)" styleClass="tinyDetail" readonly="${op == 'delete'}"
                       style="height:100px;width:99%" tabindex="12"/>
        <html:hidden property="dto(remark)"/>
    </TD>
</tr>

</table>
<table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
    <TR>
        <TD class="button" nowrap>
            <app2:securitySubmit operation="${op}" functionality="CAMPAIGN" styleClass="button" tabindex="15">
                ${button}
            </app2:securitySubmit>
            <c:if test="${'update' != op}">
                <html:cancel styleClass="button" tabindex="16">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </c:if>
        </TD>
    </TR>
</table>
</td>
</tr>
</table>
</html:form>
