<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>

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

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/Invoice/RemindersList.do" focus="parameter(numberFrom)" styleClass="form-horizontal">

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Invoice.reminders"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="numberFrom_id">
                            <fmt:message key="Invoice.reminders.number"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6">
                                    <label class="control-label" for="numberFrom_id">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <html:text property="parameter(numberFrom)"
                                               styleId="numberFrom_id"
                                               styleClass="shortText ${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                               tabindex="1"/>
                                </div>
                                <div class="col-xs-12 col-sm-6">
                                    <label class="control-label" for="numberTo_id">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <html:text property="parameter(numberTo)"
                                               styleId="numberTo_id"
                                               styleClass="shortText ${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                               tabindex="2"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="reminderLevel_id">
                            <fmt:message key="Invoice.reminders.level"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(reminderLevel)"
                                          styleId="reminderLevel_id"
                                          listName="reminderLevelList"
                                          labelProperty="name"
                                          valueProperty="level"
                                          styleClass="select ${app2:getFormSelectClasses()}"
                                          module="/catalogs"
                                          firstEmpty="true" tabIndex="3">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="reminderDateFrom">
                            <fmt:message key="Invoice.reminders.reminderDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fmt:message key="Common.from" var="from"/>
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 wrapperButton">
                                    <div class="input-group date">
                                        <app:dateText property="parameter(reminderDateFrom)"
                                                      maxlength="10"
                                                      styleId="reminderDateFrom"
                                                      calendarPicker="true"
                                                      mode="bootstrap"
                                                      placeHolder="${from}"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="dateText ${app2:getFormInputClasses()}"
                                                      convert="true" tabindex="4"/>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-6">
                                    <fmt:message key="Common.to" var="to"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(reminderDateTo)"
                                                      maxlength="10"
                                                      styleId="reminderDateTo"
                                                      mode="bootstrap"
                                                      placeHolder="${to}"
                                                      calendarPicker="true"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="dateText ${app2:getFormInputClasses()}"
                                                      convert="true" tabindex="5"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="isReminderGenerated_id">
                            <fmt:message key="Invoice.reminders.isGenerated"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:select property="parameter(isReminderGenerated)"
                                         styleId="isReminderGenerated_id"
                                         styleClass="select ${app2:getFormSelectClasses()}" tabindex="6">
                                <html:option value=""></html:option>
                                <html:option value="<%=SalesConstants.REMINDER_GENERATED%>"><fmt:message
                                        key="Common.yes"/>
                                </html:option>
                                <html:option value="<%=SalesConstants.REMINDER_NOT_GENERATED%>"><fmt:message
                                        key="Common.no"/></html:option>
                            </html:select>
                        </div>
                    </div>
                </div>
            </fieldset>
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button  ${app2:getFormButtonClasses()}" tabindex="7"><fmt:message
                        key="Common.go"/></html:submit>
                <html:button property="reset1" tabindex="8" styleClass="button ${app2:getFormButtonClasses()}"
                             onclick="resetFields();">
                    <fmt:message key="Common.clear"/>
                </html:button>
            </div>
        </div>

    </html:form>

    <c:set var="maxLevel" value="${app2:getMaxReminderLevel(pageContext.request)}"/>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="remindersList"
                     width="100%"
                     styleClass="${app2:getFantabulousTableLargeClases()}"
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

            <%--address link--%>
            <tags:addressEditContextRelativeUrl varName="addressEditLink" addressId="${invoice.addressId}" addressType="${invoice.addressType}" name1="${invoice.addressName1}" name2="${invoice.addressName2}" name3="${invoice.addressName3}"/>

            <app2:checkAccessRight functionality="INVOICEREMINDER" permission="VIEW" var="hasViewPermission"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="INVOICEREMINDER" permission="VIEW">
                    <fanta:actionColumn name="edit"
                                        title="Common.update"
                                        action="${editLink}"
                                        styleClass="listItem${lightClass}"
                                        headerStyle="listHeader"
                                        width="33%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="INVOICEREMINDER" permission="DELETE">
                    <fanta:actionColumn name="delete"
                                        title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem${lightClass}"
                                        headerStyle="listHeader"
                                        width="33%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>

                <fanta:actionColumn name="download" title="Common.download"
                                    action="${downloadAction}"
                                    styleClass="listItem${lightClass}"
                                    headerStyle="listHeader"
                                    width="34%"
                                    image="${baselayout}/img/${not empty invoice.reminderDocumentId ? 'openfile.png':'openfilegrey.png'}"
                        glyphiconClass="${not empty invoice.reminderDocumentId ? app2:getClassGlyphDownload() : app2:getClassGlyphGenerate()}"/>
            </fanta:columnGroup>

            <c:choose>
                <c:when test="${hasViewPermission}">
                    <fanta:dataColumn name="number" action="${editLink}" styleClass="listItem${lightClass}"
                                      title="Invoice.number"
                                      headerStyle="listHeader" width="12%" orderable="true" maxLength="40"/>
                </c:when>
                <c:otherwise>
                    <fanta:dataColumn name="number" styleClass="listItem${lightClass}" title="Invoice.number"
                                      headerStyle="listHeader" width="12%" orderable="true" maxLength="40"/>
                </c:otherwise>
            </c:choose>

            <fanta:dataColumn name="invoiceTitle" styleClass="listItem${lightClass}" title="Invoice.title"
                              headerStyle="listHeader"
                              width="10%"
                              orderable="true"/>

            <fanta:dataColumn name="invoiceDate" styleClass="listItem${lightClass}" title="Invoice.invoiceDate"
                              headerStyle="listHeader" width="8%" orderable="true" renderData="false">
                <fmt:formatDate var="dateValue" value="${app2:intToDate(invoice.invoiceDate)}"
                                pattern="${datePattern}"/>
                ${dateValue}
            </fanta:dataColumn>
            <fanta:dataColumn name="addressName" styleClass="listItem${lightClass}" title="Invoice.contact"
                              headerStyle="listHeader"
                              width="12%"
                              orderable="true" renderData="false">
                <fanta:textShorter title="${invoice.addressName}">
                    <app:link action="${addressEditLink}" contextRelative="true" addModuleName="false">
                        <c:out value="${invoice.addressName}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="ownReminderDate" styleClass="listItem${lightClass}"
                              title="Invoice.reminders.dateOfReminder"
                              headerStyle="listHeader" width="8%" orderable="true" renderData="false">
                <fmt:formatDate var="remDateValue" value="${app2:intToDate(invoice.ownReminderDate)}"
                                pattern="${datePattern}"/>
                ${remDateValue}
            </fanta:dataColumn>
            <fanta:dataColumn name="levelName" styleClass="listItem${lightClass}" title="Invoice.reminders.reminder"
                              headerStyle="listHeader" width="10%" orderable="true"/>
            <fanta:dataColumn name="currencyName" styleClass="listItem${lightClass}" title="Invoice.currency"
                              headerStyle="listHeader"
                              width="8%"
                              orderable="true"/>
            <fanta:dataColumn name="totalAmountGross" styleClass="listItemRight${lightClass}"
                              title="Invoice.totalAmountGross"
                              headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                <fmt:formatNumber var="totalAmountGross" value="${invoice.totalAmountGross}" type="number"
                                  pattern="${numberFormat}"/>
                ${totalAmountGross}
            </fanta:dataColumn>
            <fanta:dataColumn name="openAmount" styleClass="listItemRight${lightClass}" title="Invoice.openAmount"
                              headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                <fmt:formatNumber var="openAmount" value="${invoice.openAmount}" type="number"
                                  pattern="${numberFormat}"/>
                ${openAmount}
            </fanta:dataColumn>
            <fanta:dataColumn name="reminderDate" styleClass="listItem2${lightClass}" title="Invoice.reminderDate"
                              headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                <fmt:formatDate var="reminderDateValue" value="${app2:intToDate(invoice.reminderDate)}"
                                pattern="${datePattern}"/>
                ${reminderDateValue}
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>