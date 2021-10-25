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

<app:url var="urlInvoiceToRemind" value="/Invoice/ToRemind/List.do"/>
<div class="${app2:getFormClasses()} form-horizontal">
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:button property="" styleClass="button ${app2:getFormButtonClasses()}"
                     onclick="location.href='${urlInvoiceToRemind}'">
            <fmt:message key="Common.ok"/>
        </html:button>
    </div>
    <div class="${app2:getFormPanelClasses()}">
        <legend class="title">
            <fmt:message key="InvoiceReminder.summary.createdRemider"/>
        </legend>
        <c:forEach var="summary" items="${dto.reminderSummary}">
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <c:out value="${summary.levelName}"/>
                </label>
                <div class="${app2:getFormContainClasses(true)}">
                    <c:out value="${summary.total}"/>
                    <fmt:message key="InvoiceReminder.summary.created"/>
                </div>
            </div>
        </c:forEach>
        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelClasses()}">
                <fmt:message key="InvoiceReminder.summary.totalCreated"/>
            </label>
            <div class="${app2:getFormContainClasses(true)}">
                <c:out value="${dto.totalCreated}"/>
            </div>
        </div>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:button property="" styleClass="button ${app2:getFormButtonClasses()}"
                     onclick="location.href='${urlInvoiceToRemind}'">
            <fmt:message key="Common.ok"/>
        </html:button>
    </div>
</div>
