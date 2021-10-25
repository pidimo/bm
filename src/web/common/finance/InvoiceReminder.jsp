<%@ include file="/Includes.jsp" %>
<calendar:initialize/>

<app2:jScriptUrl url="/finance/Download/InvoiceReminder/Document.do" var="jsDownloadUrl">
    <app2:jScriptUrlParam param="dto(freeTextId)" value="id_freeText"/>
</app2:jScriptUrl>
<script language="JavaScript" type="text/javascript">
<!--
function downloadReminder(id_freeText) {
    location.href = ${jsDownloadUrl};
}
//-->
</script>

<fmt:message var="datePattern" key="datePattern"/>

<html:form action="${action}" focus="${'create' == op ? 'dto(reminderLevelId)' : 'dto(date)' }">

    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(invoiceId)" value="${param.invoiceId}"/>

    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(reminderId)"/>
        <html:hidden property="dto(remindelLevelName)" styleId="3"/>
        <html:hidden property="dto(level)"/>
    </c:if>
    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
        <html:hidden property="dto(documentId)"/>
        <c:set var="documentId" value="${invoiceReminderForm.dtoMap['documentId']}"/>
        <c:if test="${not empty msgConfirmation}">
            <html:hidden property="dto(reGenerate)" value="true"/>
        </c:if>
    </c:if>
    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>

    <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
        <tr>
            <td colspan="2" class="button">
                <app2:securitySubmit operation="${op}"
                                     functionality="INVOICEREMINDER"
                                     styleClass="button"
                                     property="save" tabindex="8">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${'update' == op or 'create' == op}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="INVOICEREMINDER"
                                         styleClass="button"
                                         property="generate" tabindex="9">
                        <fmt:message key="InvoiceReminder.generate"/>
                    </app2:securitySubmit>

                    <c:if test="${not empty documentId}">
                        <app:url var="urlDownload" value="/Download/InvoiceReminder/Document.do?dto(freeTextId)=${documentId}"/>
                        <app2:checkAccessRight functionality="INVOICEREMINDER" permission="VIEW">
                            <html:button property="" styleClass="button" onclick="location.href='${urlDownload}'" tabindex="10">
                                <fmt:message key="InvoiceReminder.open"/>
                            </html:button>
                        </app2:checkAccessRight>
                    </c:if>
                </c:if>

                <html:cancel styleClass="button" tabindex="11">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
        <TR>
            <TD colspan="2" class="title">
                <c:out value="${title}"/>
            </TD>
        </TR>
        <tr>
            <td class="label" width="25%">
                <fmt:message key="InvoiceReminder.level"/>
            </td>
            <td class="contain" width="75%">
                <fanta:select property="dto(reminderLevelId)"
                              listName="reminderLevelList"
                              labelProperty="name"
                              valueProperty="reminderLevelId"
                              styleClass="middleSelect"
                              module="/catalogs"
                              firstEmpty="true"
                              readOnly="${'delete' == op || 'update' == op}"
                        tabIndex="1">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
        </tr>
        <tr>
            <TD class="label" width="25%">
                <fmt:message key="InvoiceReminder.date"/>
            </TD>
            <TD class="contain" width="75%">
                <app:dateText property="dto(date)"
                              styleId="startDate"
                              calendarPicker="${op != 'delete'}"
                              datePatternKey="${datePattern}"
                              styleClass="text"
                              maxlength="10"
                              currentDate="true"
                              view="${'delete' == op}"
                        tabindex="2"/>
            </TD>
        </tr>
        <tr>
            <td class="topLabel" colspan="2">
                <fmt:message key="InvoiceReminder.text"/>
                <html:textarea property="dto(text)"
                               styleClass="mediumDetailHigh"
                               style="height:120px;width:99%;"
                               readonly="${'delete'== op}" tabindex="3"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="button">
                <app2:securitySubmit operation="${op}"
                                     functionality="INVOICEREMINDER"
                                     styleClass="button"
                                     property="save" tabindex="4">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${'update' == op  or 'create' == op}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="INVOICEREMINDER"
                                         styleClass="button"
                                         property="generate" tabindex="5">
                        <fmt:message key="InvoiceReminder.generate"/>
                    </app2:securitySubmit>

                    <c:if test="${not empty documentId}">
                        <app2:checkAccessRight functionality="INVOICEREMINDER" permission="VIEW">
                            <html:button property="" styleClass="button" onclick="location.href='${urlDownload}'" tabindex="6">
                                <fmt:message key="InvoiceReminder.open"/>
                            </html:button>
                        </app2:checkAccessRight>
                    </c:if>
                </c:if>

                <html:cancel styleClass="button" tabindex="7">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>
</html:form>