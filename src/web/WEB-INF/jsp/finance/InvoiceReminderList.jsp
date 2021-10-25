<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="INVOICEREMINDER" permission="CREATE">
        <html:form action="/InvoiceReminder/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="invoiceReminderList"
                     width="100%"
                     styleClass="${app2:getFantabulousTableClases()}"
                     id="invoiceReminder"
                     action="InvoiceReminder/List.do"
                     imgPath="${baselayout}"
                     align="center">

            <c:set var="editLink"
                   value="InvoiceReminder/Forward/Update.do?reminderId=${invoiceReminder.reminderId}&dto(reminderId)=${invoiceReminder.reminderId}&dto(remindelLevelName)=${app2:encode(invoiceReminder.remindelLevelName)}"/>
            <c:set var="deleteLink"
                   value="InvoiceReminder/Forward/Delete.do?reminderId=${invoiceReminder.reminderId}&dto(reminderId)=${invoiceReminder.reminderId}&dto(withReferences)=true&dto(remindelLevelName)=${app2:encode(invoiceReminder.remindelLevelName)}"/>
            <c:set var="downloadAction"
                   value="Download/InvoiceReminder/Document.do?dto(freeTextId)=${invoiceReminder.documentId}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="INVOICEREMINDER" permission="VIEW">
                    <fanta:actionColumn name="edit"
                                        title="Common.update"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="33%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="INVOICEREMINDER" permission="DELETE">
                    <fanta:actionColumn name="delete"
                                        title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="33%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>

                <fanta:actionColumn name="download" title="Common.download"
                                    action="${downloadAction}"
                                    styleClass="listItem"
                                    headerStyle="listHeader"
                                    width="34%"
                                    render="${not empty invoiceReminder.documentId}"
                                    glyphiconClass="${app2:getClassGlyphDownload()}"/>

            </fanta:columnGroup>
            <fanta:dataColumn name="remindelLevelName" action="${editLink}"
                              styleClass="listItem" title="InvoiceReminder.title"
                              headerStyle="listHeader" width="45%" orderable="true"
                              maxLength="70"/>
            <fanta:dataColumn name="level"
                              styleClass="listItemRight" title="InvoiceReminder.level"
                              headerStyle="listHeader" width="20%" orderable="true"
                              maxLength="70"/>
            <fanta:dataColumn name="date"
                              styleClass="listItem2" title="InvoiceReminder.date"
                              headerStyle="listHeader" width="30%" orderable="true"
                              maxLength="70" renderData="false">
                <fmt:formatDate var="dateValue" value="${app2:intToDate(invoiceReminder.date)}"
                                pattern="${datePattern}"/>
                ${dateValue}
            </fanta:dataColumn>
        </fanta:table>
    </div>
    <app2:checkAccessRight functionality="INVOICEREMINDER" permission="CREATE">
        <html:form action="/InvoiceReminder/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>