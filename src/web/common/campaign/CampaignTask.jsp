<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup />
<tags:initSelectPopupAdvanced fields="fieldProcessId_id, fieldProcessName_id, percent_id, fieldAddressId_id, fieldAddressName_id"/>
<%
pageContext.setAttribute("statusList", JSPHelper.getPriorityStatusList(request));
pageContext.setAttribute("intervalMinList", JSPHelper.getIntervalMin());
pageContext.setAttribute("hourList", JSPHelper.getHours());
%>

<script language="JavaScript">
function habilita()
{
 document.getElementById('dueDate_').style.display = "";
}

function deshabilita()
{
  document.getElementById('dueDate_').style.display = "none";
}

function setUserCreateName(selectBox){
    document.getElementById('userCreateName').value = selectBox.options[selectBox.selectedIndex].text;
}

</script>

<html:form action="/Campaign/Task/Create.do" focus="dto(title)">
 <%--<html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}"/>--%>
 <%--<html:hidden property="dto(type)" value="${type}"  />--%>

 <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
 <html:hidden property="dto(activityId)"/>
 <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>

<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
<td>
<br>
<table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
 <TR>
     <TD class="button" nowrap>
         <app2:securitySubmit operation="${op}" property="dto(save)"  functionality="TASK" styleClass="button" tabindex="1">
             <fmt:message key="Campaign.activity.task.buttonCreate"/>
         </app2:securitySubmit>
        <html:cancel  styleClass="button" tabindex="3">
            <fmt:message    key="Common.cancel"/>
        </html:cancel>
      </TD>
 </TR>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
<tr>
    <td colspan="4" class="title">
        <fmt:message key="Campaign.activity.task.title.createConfirmation"/>
    </td>
</tr>
<tr>
     <td  width="18%" class="label"><fmt:message key="Task.taskName"/></TD>
     <td  width="40%" class="contain">
        <app:text property="dto(title)" styleClass="middleText" maxlength="40" tabindex="4" view="${op == 'delete'}"/>
     </td>
     <td class="label" width="14%" ><fmt:message   key="Task.status"/></TD>
     <td class="contain" width="30%">
        <html:select property="dto(status)" styleClass="select" readonly="${op == 'delete'}" tabindex="16">
            <html:option value="" />
            <html:options collection="statusList"  property="value" labelProperty="label" />
        </html:select>
    </td>
</tr>
<fmt:message var="datePattern" key="datePattern"/>
<tr>
    <td class="label" ><fmt:message key="Task.startDate"/></td>
    <td class="contain">
        <app:dateText property="dto(startDate)" styleId="startDate" calendarPicker="${op != 'delete'}" datePatternKey="${datePattern}" styleClass="dateText" view="${op == 'delete'}" maxlength="10" tabindex="5" currentDate="true"/>

        &nbsp;
        <html:select property="dto(startHour)" tabindex="6"  styleClass="select" readonly="${op == 'delete'}" styleId="startHour" style="width:40">
            <html:options collection="hourList"  property="value" labelProperty="label"/>
        </html:select> :
        <html:select property="dto(startMin)"  tabindex="7" styleClass="select" readonly="${op == 'delete'}" styleId="startMin" style="width:40" >
            <html:options collection="intervalMinList"  property="value" labelProperty="label"/>
        </html:select>

    </td>
    <td class="label" ><fmt:message   key="Task.percent"/></TD>
    <td class="contain">
        <c:set var="percents" value="${app2:defaultProbabilities()}"/>
        <html:select property="dto(percent)" styleClass="select" readonly="${op == 'delete'}" tabindex="17">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="percents"  property="value" labelProperty="label"/>
        </html:select>
        <fmt:message key="Common.probabilitySymbol"/>
    </td>
</tr>

<tr>
    <td class="topLabel" rowspan="2"><fmt:message   key="Task.expireDate"/></TD>
    <td class="containTop" rowspan="2">
        <html:radio property="dto(date)" styleId="noDate"  value="false" onclick="deshabilita()" styleClass="radio" tabindex="8" disabled="${op == 'delete'}"/>
        <fmt:message   key="Task.NoDueDate"/>
&nbsp;&nbsp;
        <html:radio property="dto(date)" styleId="date"  value="true" onclick="habilita()" styleClass="radio" tabindex="9" disabled="${op == 'delete'}" />
            <fmt:message   key="Task.Date"/>
<div id="dueDate_"  ${taskForm.dtoMap.date == 'false' ? "style=\"display: none;\"":""}>
            <br>
            <app:dateText property="dto(expireDate)" styleId="expireDate" calendarPicker="${op != 'delete'}" datePatternKey="${datePattern}" styleClass="dateText" view="${op == 'delete'}" maxlength="11" tabindex="10" currentDate="true"/>
            &nbsp;
            <html:select property="dto(expireHour)" styleId="expireHour" tabindex="11"  styleClass="select" readonly="${op == 'delete'}"  style="width:40">
                <html:options collection="hourList"  property="value" labelProperty="label"/>
            </html:select> :
            <html:select property="dto(expireMin)" styleId="expireMin" tabindex="12" styleClass="select" readonly="${op == 'delete'}"  style="width:40" >
                <html:options collection="intervalMinList"  property="value" labelProperty="label"/>
            </html:select>
</div>
        </td>
    <td class="label" ><fmt:message   key="Task.priority"/></TD>
    <td class="contain">
        <fanta:select property="dto(priorityId)" listName="selectPriorityList"
            labelProperty="name" valueProperty="id" styleClass="select"
            readOnly="${op == 'delete'}" module="/catalogs" firstEmpty="true" tabIndex="18">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
            <fanta:parameter field="type" value="SCHEDULER"/>
        </fanta:select>
    </TD>
</TR>
<tr>
    <TD class="topLabel"><fmt:message   key="Task.taskType"/></TD>
    <TD class="contain" >
        <fanta:select property="dto(taskTypeId)" listName="taskTypeList"
            labelProperty="taskTypeName" valueProperty="taskTypeId" styleClass="select"
            readOnly="${op == 'delete'}" module="/catalogs" firstEmpty="true" tabIndex="19">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
        </fanta:select>
    </TD>
</tr>
<tr>
    <TD class="label"><fmt:message   key="Campaign.activity.task.createdBy"/></TD>
    <TD class="contain" colspan="3">
        <c:set var="users" value="${app2:getActivityCreateTaskUsers(campaignTaskForm.dtoMap['activityId'], pageContext.request)}"/>
        <html:hidden property="dto(taskCreateUserName)" styleId="userCreateName"/>
        <html:select property="dto(taskCreateUserId)" styleClass="middleSelect" tabindex="13" readonly="${op == 'delete'}"
                onchange="javascript:setUserCreateName(this);" onkeyup="javascript:setUserCreateName(this);">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="users" property="value" labelProperty="label"/>
        </html:select>
    </TD>
</tr>
<tr>
    <td class="topLabel" colspan="4">
        <fmt:message key="Campaign.activity.task.description"/><br>
        <html:textarea property="dto(descriptionText)" styleClass="mediumDetail" style="height:120px;width:99%;"
                       tabindex="20"
                       readonly="${op == 'delete'}"/>
    </td>
</tr>
</table>
</td>
</tr>
<tr><td>
<table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
 <tr>
     <td class="button" nowrap>
        <app2:securitySubmit operation="${op}" property="dto(save)"  functionality="TASK" styleClass="button" tabindex="25">
            <fmt:message key="Campaign.activity.task.buttonCreate"/>
        </app2:securitySubmit>
        <html:cancel  styleClass="button" tabindex="27" >
            <fmt:message    key="Common.cancel"/>
        </html:cancel>
      </td>
   </tr>
</table>
</td></tr>
</table>
</html:form>
