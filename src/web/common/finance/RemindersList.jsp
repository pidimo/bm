<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
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

    function resetFields() {
        var form = document.remindersListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }

    //-->
</script>

<fmt:message key="datePattern" var="datePattern"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<html:form action="/Invoice/RemindersList.do" focus="parameter(numberFrom)">
    <table width="99%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
        <tr>
            <td class="title" colspan="4">
                <fmt:message key="Invoice.reminders"/>
            </td>
        </tr>
        <tr>
            <td class="label" width="15%">
                <fmt:message key="Invoice.reminders.number"/>
            </td>
            <td class="contain" width="35%">
                <fmt:message key="Common.from"/>&nbsp;
                <html:text property="parameter(numberFrom)" styleClass="shortText" tabindex="1"/>
                &nbsp;
                <fmt:message key="Common.to"/>&nbsp;
                <html:text property="parameter(numberTo)" styleClass="shortText" tabindex="2"/>
            </td>
            <td class="label" width="15%">
                <fmt:message key="Invoice.reminders.level"/>
            </td>
            <td class="contain" width="35%">
                <fanta:select property="parameter(reminderLevel)"
                              listName="reminderLevelList"
                              labelProperty="name"
                              valueProperty="level"
                              styleClass="select"
                              module="/catalogs"
                              firstEmpty="true" tabIndex="5">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Invoice.reminders.reminderDate"/>
            </td>
            <td class="contain">
                <fmt:message key="Common.from"/>&nbsp;
                <app:dateText property="parameter(reminderDateFrom)" maxlength="10" styleId="reminderDateFrom"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true" tabindex="3"/>
                &nbsp;
                <fmt:message key="Common.to"/>&nbsp;
                <app:dateText property="parameter(reminderDateTo)" maxlength="10" styleId="reminderDateTo"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true" tabindex="4"/>
            </td>

            <td class="label">
                <fmt:message key="Invoice.reminders.isGenerated"/>
            </td>
            <td class="contain">
                <html:select property="parameter(isReminderGenerated)" styleClass="select" tabindex="6">
                    <html:option value="">&nbsp;</html:option>
                    <html:option value="<%=SalesConstants.REMINDER_GENERATED%>"><fmt:message key="Common.yes"/>
                    </html:option>
                    <html:option value="<%=SalesConstants.REMINDER_NOT_GENERATED%>"><fmt:message
                            key="Common.no"/></html:option>
                </html:select>
            </td>
        </tr>
        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" tabindex="7"><fmt:message key="Common.go"/></html:submit>
                <html:button property="reset1" tabindex="8" styleClass="button" onclick="resetFields();">
                    <fmt:message key="Common.clear"/>
                </html:button>
            </td>
        </tr>
    </table>
</html:form>


<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <c:set var="maxLevel" value="${app2:getMaxReminderLevel(pageContext.request)}"/>

            <fanta:table list="remindersList"
                         width="100%"
                         id="invoice"
                         action="Invoice/RemindersList.do"
                         imgPath="${baselayout}"
                         align="center">

                <c:set var="isLastLevel" value="${invoice.level >= maxLevel}"/>
                <c:remove var="lightClass"/>
                <c:if test="${isLastLevel}">
                    <c:set var="lightClass" value=" listItemHighlight"/>
                </c:if>

                <c:set var="editLink"
                       value="InvoiceReminder/Forward/Update.do?reminderId=${invoice.reminderId}&dto(reminderId)=${invoice.reminderId}&dto(remindelLevelName)=${app2:encode(invoice.levelName)}&invoiceId=${invoice.invoiceId}&tabKey=Invoice.Tab.Reminder"/>
                <c:set var="deleteLink"
                       value="InvoiceReminder/Forward/Delete.do?reminderId=${invoice.reminderId}&dto(reminderId)=${invoice.reminderId}&dto(withReferences)=true&dto(remindelLevelName)=${app2:encode(invoice.levelName)}&invoiceId=${invoice.invoiceId}&tabKey=Invoice.Tab.Reminder"/>
                <c:set var="downloadAction"
                       value="Invoice/Reminders/Generate/Document.do?dto(freeTextId)=${invoice.reminderDocumentId}&reminderId=${invoice.reminderId}&invoiceId=${invoice.invoiceId}"/>

                <app2:checkAccessRight functionality="INVOICEREMINDER" permission="VIEW" var="hasViewPermission"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="INVOICEREMINDER" permission="VIEW">
                        <fanta:actionColumn name="edit"
                                            title="Common.update"
                                            action="${editLink}"
                                            styleClass="listItem${lightClass}"
                                            headerStyle="listHeader"
                                            width="33%"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="INVOICEREMINDER" permission="DELETE">
                        <fanta:actionColumn name="delete"
                                            title="Common.delete"
                                            action="${deleteLink}"
                                            styleClass="listItem${lightClass}"
                                            headerStyle="listHeader"
                                            width="33%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>

                    <fanta:actionColumn name="download" title="Common.download"
                                        action="${downloadAction}"
                                        styleClass="listItem${lightClass}"
                                        headerStyle="listHeader"
                                        width="34%"
                                        image="${baselayout}/img/${not empty invoice.reminderDocumentId ? 'openfile.png':'openfilegrey.png'}"/>
                </fanta:columnGroup>

                <c:choose>
                    <c:when test="${hasViewPermission}">
                        <fanta:dataColumn name="number" action="${editLink}" styleClass="listItem${lightClass}"
                                          title="Invoice.number"
                                          headerStyle="listHeader" width="13%" orderable="true" maxLength="40"/>
                    </c:when>
                    <c:otherwise>
                        <fanta:dataColumn name="number" styleClass="listItem${lightClass}" title="Invoice.number"
                                          headerStyle="listHeader" width="13%" orderable="true" maxLength="40"/>
                    </c:otherwise>
                </c:choose>

                <fanta:dataColumn name="invoiceDate" styleClass="listItem${lightClass}" title="Invoice.invoiceDate"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatDate var="dateValue" value="${app2:intToDate(invoice.invoiceDate)}"
                                    pattern="${datePattern}"/>
                    ${dateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="addressName" styleClass="listItem${lightClass}" title="Invoice.contact"
                                  headerStyle="listHeader"
                                  width="12%"
                                  orderable="true"/>
                <fanta:dataColumn name="ownReminderDate" styleClass="listItem${lightClass}"
                                  title="Invoice.reminders.dateOfReminder"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatDate var="remDateValue" value="${app2:intToDate(invoice.ownReminderDate)}"
                                    pattern="${datePattern}"/>
                    ${remDateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="levelName" styleClass="listItem${lightClass}" title="Invoice.reminders.reminder"
                                  headerStyle="listHeader" width="10%" orderable="true"/>
                <fanta:dataColumn name="currencyName" styleClass="listItem${lightClass}" title="Invoice.currency"
                                  headerStyle="listHeader"
                                  width="10%"
                                  orderable="true"/>
                <fanta:dataColumn name="totalAmountGross" styleClass="listItemRight${lightClass}"
                                  title="Invoice.totalAmountGross"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatNumber var="totalAmountGross" value="${invoice.totalAmountGross}" type="number"
                                      pattern="${numberFormat}"/>
                    ${totalAmountGross}
                </fanta:dataColumn>
                <fanta:dataColumn name="openAmount" styleClass="listItemRight${lightClass}" title="Invoice.openAmount"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatNumber var="openAmount" value="${invoice.openAmount}" type="number"
                                      pattern="${numberFormat}"/>
                    ${openAmount}
                </fanta:dataColumn>
                <fanta:dataColumn name="reminderDate" styleClass="listItem2${lightClass}" title="Invoice.reminderDate"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatDate var="reminderDateValue" value="${app2:intToDate(invoice.reminderDate)}"
                                    pattern="${datePattern}"/>
                    ${reminderDateValue}
                </fanta:dataColumn>
            </fanta:table>

        </td>
    </tr>
</table>