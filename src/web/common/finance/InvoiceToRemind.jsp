<%@ include file="/Includes.jsp" %>
<fmt:message key="datePattern" var="datePattern"/>
<calendar:initialize/>

<html:form action="/Reminder/Bulk/Create.do" focus="dto(date)">

    <c:forEach var="id" items="${invoiceIdList}">
        <html:hidden property="idInvoice" value="${id}" styleId="invoiceId${id}"/>
    </c:forEach>

    <table cellSpacing=0 cellPadding=4 width="50%" border=0 align="center">
        <tr>
            <td class="button">
                <app2:securitySubmit property="dto(create)" operation="create" functionality="INVOICEREMINDER"
                                     styleClass="button" tabindex="1">
                    <fmt:message key="Reminder.bulkCreation.button"/>
                </app2:securitySubmit>

                <html:cancel styleClass="button" tabindex="2">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>

    <table width="50%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td class="title" colspan="2">
                <fmt:message key="Reminder.bulkCreation"/>
            </td>
        </tr>
        <tr>
            <td class="label" width="17%">
                <fmt:message key="Reminder.bulkCreation.date"/>
            </td>
            <td class="contain" width="33%">
                <app:dateText property="dto(date)" maxlength="10" styleId="bulkDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true" currentDate="true" tabindex="4"/>
            </td>
        </tr>
    </table>
    <table cellSpacing=0 cellPadding=4 width="50%" border=0 align="center">
        <tr>
            <td class="button">

                <app2:securitySubmit property="dto(create)" operation="create" functionality="INVOICEREMINDER"
                                     styleClass="button" tabindex="6">
                    <fmt:message key="Reminder.bulkCreation.button"/>
                </app2:securitySubmit>

                <html:cancel styleClass="button" tabindex="7">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>

</html:form>
