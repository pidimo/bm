<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<%--<tags:initSelectPopup/>--%>
<%--<calendar:initialize/>--%>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>

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
<c:set var="personType" value="<%=ContactConstants.ADDRESSTYPE_PERSON%>"/>

<%--another way to import a js source file without inserting the js code here--%>
<app2:jScriptUrl
        url="${commURL}?dto(view)=true&generate=true&dto(generate)=true&dto(op)=update&dto(userId)=${sessionScope.user.valueMap['userId']}&dto(companyId)=${sessionScope.user.valueMap['companyId']}&processId=${param.processId}&addressId=${param.addressId}&caseId=${param.caseId}&module=${param.module}&index=${param.index}&campaignId=${param.campaignId}"
        var="jsCommunicationUrl">
    <app2:jScriptUrlParam param="dto(contactId)" value="id"/>
    <app2:jScriptUrlParam param="dto(type)" value="type"/>
    <app2:jScriptUrlParam param="contactId" value="addressId"/>
    <app2:jScriptUrlParam param="dto(addressId)" value="addressId"/>
</app2:jScriptUrl>
<tags:jscript language="JavaScript" src="/js/contacts/document.jsp"/>

<script>
    function go(id, type, addressId) {
        location.href = ${jsCommunicationUrl};
    }
</script>
<html:form action="/Communication/overviewSearch/List.do"
           focus="parameter(note)"
           styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="${windowTitle}"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="note_id">
                        <fmt:message key="Document.subject"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(note)"
                                   styleId="note_id"
                                   styleClass="${app2:getFormInputClasses()} largeText"
                                   tabindex="1"/>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="type_id">
                        <fmt:message key="Communication.type"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <c:set var="communicationTypes" value="${app2:defaultCommunicationTypes(pageContext.request)}"/>
                        <html:select property="parameter(type)"
                                     styleId="type_id"
                                     styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                     tabindex="2">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="communicationTypes" property="value" labelProperty="label"/>
                        </html:select>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                        <fmt:message key="Contact.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">

                        <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>

                        <div class="input-group">
                            <app:text property="parameter(address)"
                                      styleId="fieldAddressName_id"
                                      styleClass="${app2:getFormInputClasses()} middleText"
                                      maxlength="40"
                                      readonly="true"/>
                                    <span class="input-group-btn">
                                        <tags:bootstrapSelectPopup styleId="SearchAddress_id"
                                                                   url="/contacts/SearchAddress.do"
                                                                   name="searchAddress"
                                                                   modalTitleKey="Contact.Title.search"
                                                                   isLargeModal="true"
                                                                   styleClass="${app2:getFormButtonClasses()}"
                                                                   titleKey="Common.search"
                                                                   hide="false" tabindex="3"/>

                                        <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                        nameFieldId="fieldAddressName_id"
                                                                        titleKey="Common.clear"
                                                                        tabindex="4"/>
                                    </span>
                        </div>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="inOut_id">
                        <fmt:message key="Document.inout"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:select property="parameter(inOut)"
                                     styleId="inOut_id"
                                     styleClass="${app2:getFormSelectClasses()} smallSelect"
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
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="employeeId_id">
                        <fmt:message key="Document.employee"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(employeeId)"
                                      listName="employeeBaseList"
                                      labelProperty="employeeName"
                                      valueProperty="employeeId"
                                      styleId="employeeId_id"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      module="/contacts"
                                      firstEmpty="true"
                                      tabIndex="6">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startRange">
                        <fmt:message key="Document.date"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <fmt:message key="datePattern" var="datePattern"/>
                                <fmt:message key="Common.from" var="from"/>

                                <div class="input-group date">
                                    <app:dateText property="parameter(dateFrom)"
                                                  maxlength="10"
                                                  tabindex="7"
                                                  placeHolder="from"
                                                  styleId="startRange"
                                                  calendarPicker="true"
                                                  mode="bootstrap"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()} dateText"
                                                  convert="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <fmt:message var="to" key="Common.to"/>

                                <div class="input-group date">
                                    <app:dateText property="parameter(dateTo)"
                                                  maxlength="10"
                                                  tabindex="8"
                                                  placeHolder="to"
                                                  styleId="endRange"
                                                  mode="bootstrap"
                                                  calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()} dateText"
                                                  convert="true"/>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="campaignId_id">
                        <fmt:message key="Campaign"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(campaignId)"
                                      listName="lightCampaignList"
                                      tabIndex="9"
                                      styleId="campaignId_id"
                                      labelProperty="campaignName"
                                      valueProperty="campaignId"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      module="/campaign"
                                      firstEmpty="true">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>
            </div>
            <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="10">
                <fmt:message key="Common.go"/>
            </html:submit>
            <html:button property="reset1" tabindex="11" styleClass="${app2:getFormButtonClasses()}"
                         onclick="myReset()">
                <fmt:message key="Common.clear"/>
            </html:button>
        </fieldset>
    </div>
    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="/Communication/overviewSearch/List.do"
                        parameterName="noteAlpha"
                        mode="bootstrap"/>
    </div>

</html:form>
<div class="table-responsive">
    <fanta:table mode="bootstrap" list="communicationOverviewList"
                 width="100%"
                 id="communication"
                 styleClass="${app2:getFantabulousTableClases()}"
                 action="Communication/overviewSearch/List.do"
                 imgPath="${baselayout}"
                 align="center">

        <c:set var="urlUpdateSalesProcessAction" value="SalesProcess/Action/Forward/Update.do?cmd=true&index=3"/>
        <c:set var="urlUpdateSalesProcess" value="SalesProcess/Forward/Update.do?index=3"/>

        <%--address link--%>
        <c:choose>
            <c:when test="${personType == communication.contactAddressType}">
                <c:set var="addressEditLink"
                       value="/contacts/Person/Forward/Update.do?contactId=${communication.addressId}&dto(addressId)=${communication.addressId}&dto(addressType)=${communication.contactAddressType}&dto(name1)=${app2:encode(communication.contactName1)}&dto(name2)=${app2:encode(communication.contactName2)}&dto(name3)=${app2:encode(communication.contactName3)}&index=0"/>
            </c:when>
            <c:otherwise>
                <c:set var="addressEditLink"
                       value="/contacts/Organization/Forward/Update.do?contactId=${communication.addressId}&dto(addressId)=${communication.addressId}&dto(addressType)=${communication.contactAddressType}&dto(name1)=${app2:encode(communication.contactName1)}&dto(name2)=${app2:encode(communication.contactName2)}&dto(name3)=${app2:encode(communication.contactName3)}&index=0"/>
            </c:otherwise>
        </c:choose>

        <%--contact person link--%>
        <c:if test="${not empty communication.contactPersonId}">
            <c:set var="editContactPersonUrl"
                   value="/contacts/ContactPerson/Forward/Update.do?contactId=${communication.addressId}&dto(addressId)=${communication.addressId}&dto(contactPersonId)=${communication.contactPersonId}&tabKey=Contacts.Tab.contactPersons"/>
        </c:if>

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
                                    action="MainCommunication/Forward/Update.do?contactId=${communication.addressId}&dto(note)=${app2:encode(communication.note)}&dto(contactId)=${communication.contactId}&dto(type)=${communication.type}${caseId}&tabKey=Contacts.Tab.communications"
                                    styleClass="listItemCenter" headerStyle="listHeader" width="25%"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>

            <app2:checkAccessRight functionality="COMMUNICATION" permission="DELETE">
                <fanta:actionColumn name="delete" title="Common.delete"
                                    action="MainCommunication/Forward/Delete.do?isFromCommunicationOverview=true&contactId=${communication.addressId}&dto(note)=${app2:encode(communication.note)}&dto(contactId)=${communication.contactId}&dto(type)=${communication.type}&dto(withReferences)=true${caseId}&tabKey=Contacts.Tab.communications"
                                    styleClass="listItemCenter" headerStyle="listHeader" width="25%"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
            </app2:checkAccessRight>

        </fanta:columnGroup>


        <fanta:dataColumn name="note"
                          action="MainCommunication/Forward/Update.do?contactId=${communication.addressId}&dto(note)=${app2:encode(communication.note)}&dto(contactId)=${communication.contactId}&dto(type)=${communication.type}${caseId}&tabKey=Contacts.Tab.communications"
                          title="Document.subject" styleClass="listItem" headerStyle="listHeader"
                          width="20%" orderable="true"/>

        <fanta:dataColumn name="contactName" styleClass="listItem" title="Contact.title"
                          headerStyle="listHeader" width="20%" orderable="true" renderData="false">
            <fanta:textShorter title="${communication.contactName}">
                <app:link action="${addressEditLink}" contextRelative="true" addModuleName="false">
                    <c:out value="${communication.contactName}"/>
                </app:link>
            </fanta:textShorter>
        </fanta:dataColumn>

        <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="Document.contactPerson"
                          headerStyle="listHeader" width="18%" orderable="true" renderData="false">
            <fanta:textShorter title="${communication.contactPersonName}">
                <app:link action="${editContactPersonUrl}" contextRelative="true" addModuleName="false">
                    <c:out value="${communication.contactPersonName}"/>
                </app:link>
            </fanta:textShorter>
        </fanta:dataColumn>

        <fanta:dataColumn name="employeeName" styleClass="listItem" title="Document.employee"
                          headerStyle="listHeader" width="15%" orderable="true" maxLength="25"/>

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
