<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="/finance/Reminder/Bulk/MergeDocument.do" var="jsDownloadUrl">
    <app2:jScriptUrlParam param="reminderDocIds" value="ids"/>
</app2:jScriptUrl>
<script language="JavaScript" type="text/javascript">
<!--
function mergeReminderDocuments(ids) {
    location.href = ${jsDownloadUrl};
}
//-->
</script>


<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <tr>
        <td class="button">
            <app:url var="urlInvoiceToRemind" value="/Invoice/ToRemind/List.do"/>
            <html:button property="" styleClass="button" onclick="location.href='${urlInvoiceToRemind}'">
                <fmt:message key="Common.ok"/>
            </html:button>
        </td>
    </tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
    <tr>
        <td colspan="2" class="title">
            <fmt:message key="InvoiceReminder.summary.createdRemider"/>
        </td>
    </tr>
    <c:forEach var="summary" items="${dto.reminderSummary}">
        <tr>
            <td class="label">
                <c:out value="${summary.levelName}"/>
            </td>
            <td class="contain">
                <c:out value="${summary.total}"/>
                <fmt:message key="InvoiceReminder.summary.created"/>
            </td>
        </tr>
    </c:forEach>
    <tr>
        <td class="label">
            <fmt:message key="InvoiceReminder.summary.totalCreated"/>
        </td>
        <td class="contain">
            <c:out value="${dto.totalCreated}"/>
        </td>
    </tr>
</table>
<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <tr>
        <td class="button">
            <html:button property="" styleClass="button" onclick="location.href='${urlInvoiceToRemind}'">
                <fmt:message key="Common.ok"/>
            </html:button>
        </td>
    </tr>
</table>
