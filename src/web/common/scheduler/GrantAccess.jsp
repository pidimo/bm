<%@ include file="/Includes.jsp" %>

<br/>
<tags:initSelectPopup/>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>
<html:form action="${action}">
<table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

<html:hidden property="dto(op)" value="${op}"/>

<c:if test="${('update' == op) || ('delete' == op)}">
    <html:hidden property="dto(viewUserId)"/>
</c:if>


<TR>
    <TD colspan="6" class="title">
        <c:out value="${title}"/>
    </TD>
</TR>

<TR>
    <TD class="label" width="30%" nowrap>
        <fmt:message key="Scheduler.grantAccess.userName"/>
    </TD>
    <TD class="contain" width="70%" colspan="5">
        <html:hidden property="dto(viewUserId)" styleId="fieldViewUserId_id"/>
        <app:text property="dto(viewUserName)" styleClass="mediumText" maxlength="20" readonly="true"
                  view="${'delete' == op || 'update' == op}" styleId="fieldViewUserName_id" />
        <c:if test="${'create' == op}">
            <tags:selectPopup
                    url="/scheduler/GrantAccess/ImportUserList.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(userId)=${sessionScope.user.valueMap['userId']}&parameter(ownUserId)=${sessionScope.user.valueMap['userId']}"
                    name="searchUser"
                    titleKey="Scheduler.grantAccess.searchUser"
                    width="630"
                    heigth="480"
                    imgWidth="17"
                    imgHeight="19"/>

            <tags:clearSelectPopup keyFieldId="fieldViewUserId_id"
                                   nameFieldId="fieldViewUserName_id"
                                   titleKey="Common.clear"
                                   hide="${op == 'delete' || op=='update' }"/>

        </c:if>

    </TD>
</TR>
<TR>
    <TD colspan="6" class="title">
        <fmt:message key="Scheduler.grantAccess.permissions"/>
    </TD>
</TR>
<tr>
    <td class="label">
        <fmt:message key="Scheduler.grantAccess.publicAppointment"/>
    </td>
    <td class="contain" width="75">
        <html:checkbox property="dto(anonym)" styleClass="radio"
                       disabled="${op == 'delete'}"
                       tabindex="1">
            <fmt:message key="Scheduler.grantAccess.anonym"/>
        </html:checkbox>
    </td>
    <td class="contain" width="75">
        <html:checkbox property="dto(read)" styleClass="radio"
                       disabled="${op == 'delete'}"
                       tabindex="2">
            <fmt:message key="Scheduler.grantAccess.read"/>
        </html:checkbox>
    </td>
    <td class="contain" width="75">
        <html:checkbox property="dto(add)" styleClass="radio"
                       disabled="${op == 'delete'}"
                       tabindex="3">
            <fmt:message key="Scheduler.grantAccess.add"/>
        </html:checkbox>
    </td>
    <td class="contain" width="75">
        <html:checkbox property="dto(edit)" styleClass="radio"
                       disabled="${op == 'delete'}"
                       tabindex="4">
            <fmt:message key="Scheduler.grantAccess.edit"/>
        </html:checkbox>
    </td>
    <td class="contain" width="75">
        <html:checkbox property="dto(delete)" styleClass="radio"
                       disabled="${op == 'delete'}"
                       tabindex="5">
            <fmt:message key="Scheduler.grantAccess.delete"/>
        </html:checkbox>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Scheduler.grantAccess.privateAppointment"/>
    </td>
    <td class="contain" width="75">
        <html:checkbox property="dto(privateAnonym)" styleClass="radio"
                       disabled="${op == 'delete'}"
                       tabindex="6">
            <fmt:message key="Scheduler.grantAccess.anonym"/>
        </html:checkbox>
    </td>
    <td class="contain" width="75">
        <html:checkbox property="dto(privateRead)" styleClass="radio"
                       disabled="${op == 'delete'}"
                       tabindex="7">
            <fmt:message key="Scheduler.grantAccess.read"/>
        </html:checkbox>
    </td>
    <td class="contain" width="75">
        <html:checkbox property="dto(privateAdd)" styleClass="radio"
                       disabled="${op == 'delete'}"
                       tabindex="8">
            <fmt:message key="Scheduler.grantAccess.add"/>
        </html:checkbox>
    </td>
    <td class="contain" width="75">
        <html:checkbox property="dto(privateEdit)" styleClass="radio"
                       disabled="${op == 'delete'}"
                       tabindex="9">
            <fmt:message key="Scheduler.grantAccess.edit"/>
        </html:checkbox>
    </td>
    <td class="contain" width="75">
        <html:checkbox property="dto(privateDelete)" styleClass="radio"
                       disabled="${op == 'delete'}"
                       tabindex="10">
            <fmt:message key="Scheduler.grantAccess.delete"/>
        </html:checkbox>
    </td>
</tr>
<tr>
    <td colspan="2"
            <c:out value="${sessionScope.listshadow}" escapeXml="false"/>
            ><img
            src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
</tr>
</table>
<%--CREATE, CANCEL, SAVE AND NEW buttons--%>
<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="${op}" functionality="GRANTACCESS" styleClass="button"
                                 tabindex="11">
                ${button}
            </app2:securitySubmit>

                <%--<c:if test="${op == 'create'}" >
                    <app2:securitySubmit operation="${op}" functionality="LANGUAGE" styleClass="button" property="SaveAndNew"><fmt:message   key="Common.saveAndNew"/></app2:securitySubmit>
                </c:if>--%>
            <html:cancel styleClass="button" tabindex="12">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </TD>
    </TR>
</table>
</html:form>

</td>
</tr>
</table>