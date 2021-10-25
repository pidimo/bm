<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(active)">


    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(version)"/>
    <c:if test="${ op=='update' || op=='delete' }">
        <html:hidden property="dto(campaignContactId)"/>
        <html:hidden property="dto(campaignId)"/>
        <html:hidden property="dto(activityId)"/>
        <html:hidden property="dto(addressId)"/>
        <html:hidden property="dto(hasTask)"/>
    </c:if>
    <c:set var="withTask" value="${form.dtoMap['hasTask']}"/>

    <table border="0" cellpadding="0" cellspacing="0" width="70%" align="center" class="container">
        <tr>
            <td colspan="4" class="title">
                <c:out value="${title}"/>
            </td>
        </tr>
        <tr>
            <td class="label" width="15%">
                <fmt:message key="Campaign.company"/>
            </td>
            <td class="contain" width="30%">
                <app:text property="dto(contactName)" styleClass="middleText" maxlength="40" tabindex="1" view="true"/>
            </td>
            <td class="label" width="15%">
                <fmt:message key="Common.active"/>
            </td>
            <td class="contain" width="40%">
                <html:checkbox property="dto(active)" styleClass="radio" tabindex="3" disabled="${op == 'delete'}"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Campaign.contactPerson"/>
            </td>
            <td class="contain">
                <app:text property="dto(contactPersonName)" styleClass="middleText" maxlength="40" tabindex="2" view="true"/>
            </td>
            <td class="label">
                <fmt:message key="Campaign.activity.campContact.responsible"/>
            </td>
            <td class="contain">
                <fanta:select property="dto(userId)" listName="activityUserList" firstEmpty="true"
                              labelProperty="userName" valueProperty="userId"
                              styleClass="mediumSelect"
                              tabIndex="4"
                              module="/campaign"
                              readOnly="${op == 'delete' || withTask}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="activityId" value="${form.dtoMap['activityId']}"/>
                </fanta:select>
            </td>
        </tr>
    </table>
    <table cellSpacing=0 cellPadding=4 width="70%" border=0 align="center">
        <tr>
            <td class="button">

                <html:submit property="dto(${op})" styleClass="button" tabindex="7">
                    <c:out value="${button}"/>
                </html:submit>
                <html:cancel styleClass="button" tabindex="8">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>

</html:form>
