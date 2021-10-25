<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>

<script>
    function myReset() {
        var form = document.communicationAdvancedSearchForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            } else if (form.elements[i].name == "parameter(contactPersonId)") {
                form.elements[i].value = "";
            }
        }
    }
</script>

<app2:checkAccessRight functionality="COMMUNICATION" permission="EXECUTE" var="hasCommunicationExecute"/>
<c:set var="LETTER" value="<%= com.piramide.elwis.utils.CommunicationTypes.LETTER %>"/>
<c:set var="WEB_DOCUMENT" value="<%= com.piramide.elwis.utils.CommunicationTypes.WEB_DOCUMENT%>"/>

<%--another way to import a js source file without inserting the js code here--%>
<app2:jScriptUrl
        url="${commURL}?dto(view)=true&generate=true&dto(generate)=true&dto(op)=update&contactId=${param.contactId}&dto(addressId)=${param.contactId}&dto(userId)=${sessionScope.user.valueMap['userId']}&dto(companyId)=${sessionScope.user.valueMap['companyId']}&processId=${param.processId}&addressId=${param.addressId}&caseId=${param.caseId}&module=${param.module}&index=${param.index}&campaignId=${param.campaignId}"
        var="jsCommunicationUrl">
    <app2:jScriptUrlParam param="dto(contactId)" value="id"/>
    <app2:jScriptUrlParam param="dto(type)" value="type"/>
</app2:jScriptUrl>
<tags:jscript language="JavaScript" src="/js/contacts/document.jsp"/>

<script>
    function go(id, type) {
        location.href = ${jsCommunicationUrl};
    }
</script>

<html:form action="/Communication/AdvancedSearch.do" focus="parameter(note)" styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="${windowTitle}"/>
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Document.subject"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(note)" styleClass="largeText ${app2:getFormInputClasses()}"
                                   tabindex="1"/>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Communication.type"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <c:set var="communicationTypes" value="${app2:defaultCommunicationTypes(pageContext.request)}"/>
                        <html:select property="parameter(type)" styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                     tabindex="2">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="communicationTypes" property="value" labelProperty="label"/>
                        </html:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="ContactPerson.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="input-group">
                            <app:text property="parameter(contactPersonName_field)"
                                      styleClass="mediumText ${app2:getFormInputClasses()}"
                                      readonly="true" styleId="fieldContactPersonName_id" tabindex="3"/>
                            <html:hidden property="parameter(contactPersonId)" styleId="fieldContactPersonId_id"/>
                    <span class="input-group-btn">
                        <tags:bootstrapSelectPopup
                                styleId="searchContactPerson_id"
                                tabindex="4"
                                isLargeModal="true"
                                url="/contacts/ContactPerson/Search.do?contactId=${isFromSalesProcess ? param.addressId : param.contactId}"
                                name="searchContactPerson"
                                modalTitleKey="ContactPerson.search"
                                titleKey="Common.search"/>
                        <tags:clearBootstrapSelectPopup
                                keyFieldId="fieldContactPersonId_id"
                                nameFieldId="fieldContactPersonName_id"
                                titleKey="Common.clear" tabindex="4"/>
                    </span>
                        </div>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Document.inout"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:select property="parameter(inOut)" styleClass="smallSelect ${app2:getFormSelectClasses()}"
                                     tabindex="5">
                            <html:option value="">&nbsp;</html:option>
                            <html:option value="1"><fmt:message key="Document.in"/> </html:option>
                            <html:option value="0"><fmt:message key="Document.out"/> </html:option>
                        </html:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Document.employee"/>
                    </label>

                    <div class="c${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(employeeId)" listName="employeeBaseList"
                                      labelProperty="employeeName" valueProperty="employeeId"
                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                      module="/contacts" firstEmpty="true" tabIndex="6">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Document.date"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fmt:message key="datePattern" var="datePattern"/>

                        <fmt:message key="Common.from" var="from"/>

                        <div class="row">
                            <div class="col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <app:dateText property="parameter(dateFrom)"
                                                  maxlength="10" tabindex="7"
                                                  styleId="startRange"
                                                  calendarPicker="true"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>
                            <fmt:message key="Common.to" var="to"/>

                            <div class="col-sm-6">
                                <div class="input-group date">
                                    <app:dateText property="parameter(dateTo)"
                                                  maxlength="10" tabindex="8"
                                                  styleId="endRange"
                                                  calendarPicker="true"
                                                  mode="bootstrap"
                                                  placeHolder="${to}"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="SalesProcess"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(processName)"
                                   styleClass="mediumText ${app2:getFormInputClasses()}" tabindex="9"/>
                    </div>
                </div>
            </div>
        </fieldset>
        <div class="row">
            <div class="col-xs-12">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}" tabindex="10"><fmt:message
                        key="Common.go"/></html:submit>
                <html:button property="reset1" tabindex="11" styleClass="button ${app2:getFormButtonClasses()}"
                             onclick="myReset()"><fmt:message
                        key="Common.clear"/></html:button>
            </div>
        </div>
    </div>
    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet
                mode="bootstrap"
                action="/Communication/AdvancedSearch.do"
                parameterName="noteAlpha"/>
    </div>
</html:form>

<div class="table-responsive">
    <fanta:table mode="bootstrap" list="communicationAdvancedSearchList"
                 styleClass="${app2:getFantabulousTableClases()}"
                 width="100%" id="communication"
                 action="Communication/AdvancedSearch.do"
                 imgPath="${baselayout}" align="center">
        <c:set var="urlUpdateSalesProcessAction" value="SalesProcess/Action/Forward/Update.do?cmd=true&index=3"/>
        <c:set var="urlUpdateSalesProcess" value="SalesProcess/Forward/Update.do?index=3"/>

        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <c:remove var="caseId"/>
            <c:if test="${not empty communication.caseId}">
                <c:set var="caseId" value="&dto(caseId)=${communication.caseId}"/>
                <c:if test="${not empty communication.activityId}">
                    <c:set var="caseId" value="${caseId}&dto(activityId)=${communication.activityId}"/>
                </c:if>
            </c:if>
            <app2:checkAccessRight functionality="COMMUNICATION" permission="VIEW">
                <fanta:actionColumn name="update" title="Common.update"
                                    action="MainCommunication/Forward/Update.do?dto(note)=${app2:encode(communication.note)}&dto(contactId)=${communication.contactId}&dto(type)=${communication.type}${caseId}&tabKey=Contacts.Tab.communications"
                                    styleClass="listItemCenter" headerStyle="listHeader" width="25%"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>

            <app2:checkAccessRight functionality="COMMUNICATION" permission="DELETE">
                <fanta:actionColumn name="delete" title="Common.delete"
                                    action="MainCommunication/Forward/Delete.do?dto(note)=${app2:encode(communication.note)}&dto(contactId)=${communication.contactId}&dto(type)=${communication.type}&dto(withReferences)=true${caseId}&tabKey=Contacts.Tab.communications"
                                    styleClass="listItemCenter" headerStyle="listHeader" width="25%"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
            </app2:checkAccessRight>

            <c:if test="${hasCommunicationExecute}">
                <c:choose>
                    <c:when test="${WEB_DOCUMENT == communication.type}">
                        <fanta:actionColumn name="generate" styleClass="listItemCenter" headerStyle="listHeader"
                                            width="25%"
                                            render="false">

                            <c:set var="docInfo" value="${app2:getCommunicationDocumentInfo(communication.contactId)}"/>
                            <c:if test="${not empty docInfo.freeTextId}">
                                <app:link
                                        action="contacts/Download/WebDocument.do?dto(freeTextId)=${docInfo.freeTextId}&dto(contactId)=${communication.contactId}"
                                        title="${downloadMsg}" contextRelative="true">
                                    <span class="${app2:getClassGlyphOpen()}"></span>
                                </app:link>
                            </c:if>
                        </fanta:actionColumn>
                    </c:when>

                    <c:otherwise>
                        <fanta:actionColumn name="generate" styleClass="listItemCenter" headerStyle="listHeader"
                                            width="25%"
                                            render="false">
                            <c:if test="${(communication.templateId != null && communication.templateId != '') || (communication.type==LETTER && app2:isCampaignGenerationCommunication(communication.contactId))}">
                                <a href="javascript:go(${communication.contactId}, ${communication.type})"
                                   title="${communication.status=='1' ? viewDocumentKey : generateDocumentKey}">
                                    <span class="${communication.status=='1' ? app2:getClassGlyphDownload() : app2:getClassGlyphGenerate()}"></span>
                                </a>
                            </c:if>
                        </fanta:actionColumn>
                    </c:otherwise>
                </c:choose>
            </c:if>

            <c:if test="${param.module == 'contacts'}">
                <app2:checkAccessRight functionality="TASK" permission="CREATE">
                    <fanta:actionColumn name="createTask" title="Scheduler.task.new"
                                        action="Task/Forward/Create.do?index=${indexTask}&dto(from)=contacts&dto(contactPersonId)=${communication.contactPersonId}&dto(processId)=${communication.processId}&dto(processName)=${communication.processName}&tabKey=Scheduler.Tasks"
                                        styleClass="listItemCenter" headerStyle="listHeader" width="25%"
                                        glyphiconClass="${app2:getClassGlyphListAlt()}"/>
                </app2:checkAccessRight>
            </c:if>
        </fanta:columnGroup>


        <fanta:dataColumn name="note"
                          action="MainCommunication/Forward/Update.do?dto(note)=${app2:encode(communication.note)}&dto(contactId)=${communication.contactId}&dto(type)=${communication.type}${caseId}&tabKey=Contacts.Tab.communications"
                          title="Document.subject" styleClass="listItem" headerStyle="listHeader"
                          width="${noteColumnWidth}" orderable="true"/>

        <c:if test="${isSupport || isFromCampaign}">
            <fanta:dataColumn name="contactName" styleClass="listItem" title="Contact.title"
                              headerStyle="listHeader" width="20%" orderable="true"/>
        </c:if>

        <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="Document.contactPerson"
                          headerStyle="listHeader" width="18%" orderable="true"/>

        <fanta:dataColumn name="employeeName" styleClass="listItem" title="Document.employee"
                          headerStyle="listHeader" width="15%" orderable="true" maxLength="25"/>

        <c:if test="${isFromContacts}">
            <fanta:dataColumn name="processName" styleClass="listItem" title="SalesProcess" headerStyle="listHeader"
                              width="20%"
                              orderable="true" renderData="false">
                <fanta:textShorter title="${communication.processName}">
                    <c:if test="${not empty communication.processId}">
                        <c:choose>
                            <c:when test="${communication.isAction == '1'}">
                                <app2:checkAccessRight functionality="SALESPROCESSACTION" permission="UPDATE">
                                    <c:set var="hasActionAccess" value="true"/>
                                    <app:link
                                            action="${urlUpdateSalesProcessAction}&dto(processId)=${communication.processId}&dto(processName)=${app2:encode(communication.processName)}&dto(contactId)=${communication.contactId}&dto(note)=${app2:encode(communication.note)}&dto(type)=${communication.type}"
                                            title="${app2:encode(communication.processName)}">
                                        ${communication.processName}
                                    </app:link>
                                </app2:checkAccessRight>
                                <c:if test="${hasActionAccess == null}">
                                    ${communication.processName}
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <app2:checkAccessRight functionality="SALESPROCESS" permission="UPDATE">
                                    <c:set var="hasProcessAccess" value="true"/>
                                    <app:link
                                            action="${urlUpdateSalesProcess}&processId=${communication.processId}&dto(processId)=${communication.processId}&dto(processName)=${app2:encode(communication.processName)}"
                                            title="${app2:encode(communication.processName)}">
                                        ${communication.processName}
                                    </app:link>
                                </app2:checkAccessRight>
                                <c:if test="${hasProcessAccess == null}">
                                    ${communication.processName}
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </fanta:textShorter>
            </fanta:dataColumn>
        </c:if>

        <fanta:dataColumn name="date" styleClass="listItem" title="Document.date"
                          headerStyle="listHeader" width="10%" orderable="true" renderData="false">
            <fmt:formatDate value="${app2:intToDate(communication.date)}" pattern="${datePattern}"/>
        </fanta:dataColumn>

        <fanta:dataColumn name="type" styleClass="listItem" title="Document.mediaType" headerStyle="listHeader"
                          width="7%"
                          orderable="false" renderData="false">

            <c:choose>
                <c:when test="${communication.type=='0'}">
                    <fmt:message key="Communication.type.phone" var="typeName"/>
                </c:when>
                <c:when test="${communication.type=='1'}">
                    <fmt:message key="Communication.type.meeting" var="typeName"/>
                </c:when>
                <c:when test="${communication.type=='2'}">
                    <fmt:message key="Communication.type.fax" var="typeName"/>
                </c:when>
                <c:when test="${communication.type=='3'}">
                    <fmt:message key="Communication.type.letter" var="typeName"/>
                </c:when>
                <c:when test="${communication.type=='5'}">
                    <fmt:message key="Communication.type.email" var="typeName"/>
                </c:when>
                <c:when test="${communication.type=='6'}">
                    <fmt:message key="Communication.type.document" var="typeName"/>
                </c:when>
                <c:when test="${communication.type == WEB_DOCUMENT}">
                    <fmt:message key="Communication.type.webDocument" var="typeName"/>
                </c:when>
                <c:otherwise>
                    <fmt:message key="Communication.type.other" var="typeName"/>
                </c:otherwise>
            </c:choose>
            <fanta:textShorter title="${typeName}">${typeName}</fanta:textShorter>
        </fanta:dataColumn>
        <fanta:dataColumn name="inOut" styleClass="listItem2Center" title="Document.inout" headerStyle="listHeader"
                          width="5%"
                          orderable="false" renderData="false">
            <c:if test="${communication.inOut=='0'}">
                <fmt:message key="Document.out" var="document_out"/>
                <span title="${document_out}" class="${app2:getClassGlyphOpen()}"></span>
            </c:if>
            <c:if test="${communication.inOut=='1'}">
                <fmt:message key="Document.in" var="document_in"/>
                <span title="${document_in}" class="${app2:getClassGlyphSave()}"></span>
            </c:if>
        </fanta:dataColumn>
    </fanta:table>
</div>