<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>
<calendar:initialize/>
<br/>

<html:form action="/SalesProcess/CreateMany.do" focus="dto(processName)">
<fmt:message var="datePattern" key="datePattern"/>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>
<table cellSpacing="0" cellPadding="4" width="800" border="0" align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="${op}" functionality="SALESPROCESS" property="dto(save)" styleClass="button"
                                 styleId="saveButtonId"><c:out value="${button}"/></app2:securitySubmit>
            <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
        </TD>
    </TR>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="800" align="center" class="container">
<%--<html:hidden property="dto(op)" value="${op}"/>--%>
<html:hidden property="dto(campaignId)"/>
<html:hidden property="dto(activityId)"/>
<TR>
    <TD colspan="4" class="title">
        <c:out value="${title}"/>
    </TD>
</TR>
<TR>
    <TD class="label" width="15%"><fmt:message key="SalesProcess.name"/></TD>
    <TD class="contain" width="35%">
        <app:text property="dto(processName)" styleClass="mediumText" maxlength="80" tabindex="1"
                  view="${op == 'delete'}"/>
    </TD>
    <TD class="label" width="15%"><fmt:message key="SalesProcess.priority"/></TD>
    <TD class="contain" width="35%">
        <fanta:select property="dto(priorityId)" listName="sProcessPriorityList"
                      labelProperty="name" valueProperty="id" styleClass="mediumSelect"
                      readOnly="${op == 'delete'}" module="/sales" firstEmpty="true" tabIndex="6">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
</TR>
<TR>
    <TD class="label"><fmt:message key="SalesProcess.probability"/></TD>
    <TD class="contain">
        <c:set var="probabilities" value="${app2:defaultProbabilities()}"/>
        <html:select property="dto(probability)" styleClass="select" readonly="${op == 'delete'}" tabindex="3">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="probabilities" property="value" labelProperty="label"/>
        </html:select>
        <fmt:message key="Common.probabilitySymbol"/>
    </TD>

    <TD class="label"><fmt:message key="SalesProcess.startDate"/></TD>
    <TD class="contain">
        <app:dateText property="dto(startDate)" styleId="startDate" calendarPicker="${op != 'delete'}"
                      datePatternKey="${datePattern}" styleClass="text" view="${op == 'delete'}" maxlength="10"
                      tabindex="9" currentDate="true"/>
    </TD>
</TR>
<TR>

    <TD class="label"><fmt:message key="SalesProcess.status"/></TD>
    <TD class="contain">
        <fanta:select property="dto(statusId)" listName="statusList"
                      labelProperty="statusName" valueProperty="statusId" styleClass="select"
                      readOnly="${op == 'delete'}" module="/sales" firstEmpty="true" tabIndex="4">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>

    <TD class="label"><fmt:message key="SalesProcess.endDate"/></TD>
    <TD class="contain">
        <app:dateText property="dto(endDate)" styleId="endDate" calendarPicker="${op != 'delete'}"
                      datePatternKey="${datePattern}" styleClass="text" view="${op == 'delete'}" maxlength="10"
                      tabindex="10"/>
    </TD>

</TR>
<TR>
    <TD class="label"><fmt:message key="SalesProcess.value"/></TD>
    <TD class="contain" colspan="3">
        <app:numberText property="dto(value)" styleClass="text" maxlength="12" view="${'delete' == op}"
                        numberType="decimal" maxInt="10" maxFloat="2" tabindex="5"/>
    </TD>
</TR>

</table>

<table cellSpacing=0 cellPadding=4 width="800" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="${op}" functionality="SALESPROCESS" property="dto(save)" styleClass="button"
                                 styleId="saveButtonId" tabindex="12"><c:out value="${button}"/></app2:securitySubmit>
            <html:cancel styleClass="button" tabindex="14"><fmt:message key="Common.cancel"/></html:cancel>
        </TD>
    </TR>
</table>

</td>
</tr>
</table>
</html:form>
