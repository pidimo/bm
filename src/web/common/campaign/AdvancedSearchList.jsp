<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<calendar:initialize/>
<%
    pageContext.setAttribute("statusList", JSPHelper.getStatusList(request));
%>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<script language="JavaScript">
    function myReset() {
        var form = document.advancedListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>

<br>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
    <td height="20" class="title">
        <fmt:message key="Campaign.Title.advancedSearch"/>
        <br>
    </td>
</tr>
<tr>
<td>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0">
<html:form action="/AdvancedSearch.do" focus="parameter(campaignName)">
<fmt:message key="datePattern" var="datePattern"/>
<tr>
    <td width="15%" class="label"><fmt:message key="Campaign.mailing"/></td>
    <td width="34%" class="contain">
        <app:text property="parameter(campaignName)" styleClass="middleText" maxlength="40" tabindex="1"/>
    </td>
    <td width="15%" class="label"><fmt:message key="Campaign.closeDate"/></TD>
    <td width="36%" class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(endDate1)" maxlength="10" tabindex="9" styleId="endDate1"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endDate2)" maxlength="10" tabindex="10" styleId="endDate2"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Campaign.responsibleEmployee"/></td>
    <td class="contain">
        <fanta:select property="parameter(employeeId)" listName="employeeBaseList" tabIndex="2"
                      labelProperty="employeeName" valueProperty="employeeId" styleClass="mediumSelect"
                      module="/contacts" firstEmpty="true">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
    <td class="label"><fmt:message key="Campaign.budgetedCost"/></TD>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(budgetCost1)" styleClass="numberText" tabindex="11" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(budgetCost2)" styleId="price2" styleClass="numberText" tabindex="12"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Campaign.status"/></TD>
    <td class="contain">
        <html:select property="parameter(status)" styleClass="mediumSelect" tabindex="3">
            <html:option value=""/>
            <html:options collection="statusList" property="value" labelProperty="label"/>
        </html:select>
    </td>
    <td class="label"><fmt:message key="Campaign.profits"/></TD>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(awaitedUtility1)" styleClass="numberText" tabindex="13" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(awaitedUtility2)" styleId="price2" styleClass="numberText" tabindex="14"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Campaign.type"/>
    </td>
    <td class="contain">
        <fanta:select property="parameter(typeId)" listName="campaignTypeList"
                      valueProperty="id" labelProperty="title" firstEmpty="true"
                      styleClass="mediumSelect" module="/catalogs" tabIndex="4" readOnly="${'delete' == op}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
    <td class="label"><fmt:message key="Campaign.realCost"/></TD>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(realCost1)" styleClass="numberText" tabindex="15" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(realCost2)" styleId="price2" styleClass="numberText" tabindex="16"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Campaign.dateCreation"/></TD>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startDate1)" maxlength="10" tabindex="5" styleId="startDate1"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(startDate2)" maxlength="10" tabindex="6" styleId="startDate2"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
    <td class="label"><fmt:message key="Campaign.totalContacts"/></td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:text property="parameter(numberContacts1)" styleClass="numberText" tabindex="17"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:text property="parameter(numberContacts2)" styleClass="numberText" tabindex="18"/>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Campaign.updatedDate"/></td>
    <td class="contain" colspan="3">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(changeDate1)" maxlength="10" tabindex="7" styleId="changeDate1"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(changeDate2)" maxlength="10" tabindex="8" styleId="changeDate2"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
</tr>
<tr>
    <td colspan="4" align="center" class="alpha">
        <fanta:alphabet action="AdvancedSearch.do" addModuleName="campaign" parameterName="campaignNameAlpha"/>
    </td>
</tr>
<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" property="parameter(generate)" tabindex="19">
            <fmt:message key="Common.go"/>
        </html:submit>
        <html:button property="reset1" tabindex="20" styleClass="button" onclick="myReset()">
            <fmt:message key="Common.clear"/>
        </html:button>
    </td>
</tr>

</html:form>
</table>
<br/>
<app2:checkAccessRight functionality="CAMPAIGN" permission="CREATE">
    <c:set var="newButtonsTable" scope="page">
        <tags:buttonsTable>
            <app:url
                    value="/Campaign/Forward/Create.do?dto(operation)=create&dto(companyId)=${sessionScope.user.valueMap['companyId']}&advancedListForward=CampaignAdvancedSearch"
                    addModuleParams="false" var="newCampaignUrl"/>
            <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                   onclick="window.location ='${newCampaignUrl}'">
        </tags:buttonsTable>
    </c:set>
</app2:checkAccessRight>
<c:out value="${newButtonsTable}" escapeXml="false"/>
<fanta:table list="campaignReportList" width="100%" id="campaign" action="AdvancedSearch.do"
             imgPath="${baselayout}">
    <c:set var="editAction"
           value="Campaign/Forward/Update.do?campaignId=${campaign.campaignId}&dto(campaignId)=${campaign.campaignId}&dto(campaignName)=${app2:encode(campaign.campaignName)}&dto(operation)=update&dto(companyId)=${sessionScope.user.valueMap['companyId']}&index=0"/>
    <c:set var="deleteAction"
           value="Campaign/Forward/Delete.do?campaignId=${campaign.campaignId}&dto(withReferences)=true&dto(campaignId)=${campaign.campaignId}&dto(campaignName)=${app2:encode(campaign.campaignName)}&dto(companyId)=${sessionScope.user.valueMap['companyId']}&index=0"/>
    <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
        <app2:checkAccessRight functionality="CAMPAIGN" permission="VIEW">
            <fanta:actionColumn name="update" label="Common.update" action="${editAction}" title="Common.update"
                                styleClass="listItem" headerStyle="listHeader" width="50%"
                                image="${baselayout}/img/edit.gif"/>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="CAMPAIGN" permission="DELETE">
            <fanta:actionColumn name="del" label="Common.delete" action="${deleteAction}" title="Common.delete"
                                styleClass="listItem" headerStyle="listHeader" width="50%"
                                image="${baselayout}/img/delete.gif"/>
        </app2:checkAccessRight>
    </fanta:columnGroup>
    <fanta:dataColumn name="campaignName" action="${editAction}" styleClass="listItem" title="Campaign.mailing"
                      headerStyle="listHeader" width="25%" orderable="true">
        &nbsp;
    </fanta:dataColumn>
    <fanta:dataColumn name="employeeName" styleClass="listItem" title="Campaign.responsibleEmployee"
                      headerStyle="listHeader" width="25%" orderable="true"/>
    <fanta:dataColumn name="status" styleClass="listItem" title="Campaign.status" headerStyle="listHeader"
                      width="13%" orderable="true" renderData="false">
        <c:if test="${campaign.status == '1'}">
            <fmt:message key="Campaign.preparation"/>
        </c:if>
        <c:if test="${campaign.status == '2'}">
            <fmt:message key="Campaign.sent"/>
        </c:if>
        <c:if test="${campaign.status == '3'}">
            <fmt:message key="Campaign.cancel"/>
        </c:if>
    </fanta:dataColumn>
    <fanta:dataColumn name="startDate" styleClass="listItem" title="Campaign.dateCreation" headerStyle="listHeader"
                      width="13%" orderable="true" renderData="false">
        <fmt:message var="datePattern" key="datePattern"/>
        <fmt:formatDate var="dateValue1" value="${app2:intToDate(campaign.startDate)}" pattern="${datePattern}"/>
        ${dateValue1}&nbsp;
    </fanta:dataColumn>
    <fanta:dataColumn name="endDate" styleClass="listItem2" title="Campaign.closeDate" headerStyle="listHeader"
                      width="13%" orderable="true" renderData="false">
        <fmt:message var="datePattern" key="datePattern"/>
        <fmt:formatDate var="dateValue" value="${app2:intToDate(campaign.endDate)}" pattern="${datePattern}"/>
        ${dateValue}&nbsp;
    </fanta:dataColumn>


</fanta:table>
<c:out value="${newButtonsTable}" escapeXml="false"/>
</td>
</tr>
</table>
