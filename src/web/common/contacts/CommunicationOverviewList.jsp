<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>
<calendar:initialize/>

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


<br>
<table width="90%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
<tr>
    <td height="20" class="title" colspan="4"><fmt:message key="${windowTitle}"/></td>
</tr>
<html:form action="/Communication/overviewSearch/List.do" focus="parameter(note)">
    <TR>
        <TD width="15%" class="label"><fmt:message key="Document.subject"/></TD>
        <TD class="contain" width="35%">
            <html:text property="parameter(note)" styleClass="largeText" tabindex="1"/>
        </TD>
        <TD width="15%" class="label"><fmt:message key="Communication.type"/></TD>
        <TD width="35%" class="contain">
            <c:set var="communicationTypes" value="${app2:defaultCommunicationTypes(pageContext.request)}"/>
            <html:select property="parameter(type)" styleClass="mediumSelect"
                         tabindex="6">
                <html:option value="">&nbsp;</html:option>
                <html:options collection="communicationTypes" property="value" labelProperty="label"/>
            </html:select>
        </TD>

    </TR>
    <TR>
        <TD width="15%" class="label"><fmt:message key="Contact.title"/></TD>
        <TD width="35%" class="contain">
            <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
            <app:text property="parameter(address)" styleId="fieldAddressName_id" styleClass="middleText"
                      maxlength="40" readonly="true"/>
            <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"
                              hide="false" tabindex="2"/>
            <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                   titleKey="Common.clear" tabindex="3"/>
        </TD>
        <TD width="15%" class="label"><fmt:message key="Document.inout"/></TD>
        <TD class="contain" width="35%">
            <html:select property="parameter(inOut)" styleClass="smallSelect" tabindex="7">
                <html:option value="">&nbsp;</html:option>
                <html:option value="1"><fmt:message key="Document.in"/> </html:option>
                <html:option value="0"><fmt:message key="Document.out"/> </html:option>
            </html:select>
        </TD>
    </TR>
    <TR>
        <TD width="15%" class="label"><fmt:message key="Document.employee"/></TD>
        <TD class="contain" width="35%">
            <fanta:select property="parameter(employeeId)" listName="employeeBaseList"
                          labelProperty="employeeName" valueProperty="employeeId" styleClass="mediumSelect"
                          module="/contacts" firstEmpty="true" tabIndex="4">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </TD>

        <TD width="15%" class="label"><fmt:message key="Document.date"/></TD>
        <TD width="35%" class="contain">
            <fmt:message key="datePattern" var="datePattern"/>
            <fmt:message key="Common.from"/>
            &nbsp;
            <app:dateText property="parameter(dateFrom)" maxlength="10" tabindex="8" styleId="startRange"
                          calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
            &nbsp;
            <fmt:message key="Common.to"/>
            &nbsp;
            <app:dateText property="parameter(dateTo)" maxlength="10" tabindex="9" styleId="endRange"
                          calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        </TD>
    </TR>
    <tr>
        <TD width="15%" class="label"><fmt:message key="Campaign"/></TD>
        <TD width="35%" class="contain">
            <fanta:select property="parameter(campaignId)" listName="lightCampaignList" tabIndex="5"
                          labelProperty="campaignName" valueProperty="campaignId" styleClass="mediumSelect"
                          module="/campaign" firstEmpty="true">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </TD>
        <TD colspan="2" class="contain">&nbsp;</TD>
    </tr>
    <tr>
        <td colspan="4" align="center" class="alpha">
            <fanta:alphabet action="/Communication/overviewSearch/List.do" parameterName="noteAlpha"/>
        </td>
    </tr>
    <tr>
        <td colspan="4" class="button">
            <html:submit styleClass="button" tabindex="10"><fmt:message key="Common.go"/></html:submit>
            <html:button property="reset1" tabindex="11" styleClass="button" onclick="myReset()"><fmt:message
                    key="Common.clear"/></html:button>
        </td>
    </tr>

</html:form>
<tr>
    <td colspan="4" align="center">
        <fanta:table list="communicationOverviewList" width="100%" id="communication"
                     action="Communication/overviewSearch/List.do"
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
                                        action="MainCommunication/Forward/Update.do?contactId=${communication.addressId}&dto(note)=${app2:encode(communication.note)}&dto(contactId)=${communication.contactId}&dto(type)=${communication.type}${caseId}&tabKey=Contacts.Tab.communications"
                                        styleClass="listItemCenter" headerStyle="listHeader" width="25%"
                                        image="${baselayout}/img/edit.gif"/>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="COMMUNICATION" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete"
                                        action="MainCommunication/Forward/Delete.do?isFromCommunicationOverview=true&contactId=${communication.addressId}&dto(note)=${app2:encode(communication.note)}&dto(contactId)=${communication.contactId}&dto(type)=${communication.type}&dto(withReferences)=true${caseId}&tabKey=Contacts.Tab.communications"
                                        styleClass="listItemCenter" headerStyle="listHeader" width="25%"
                                        image="${baselayout}/img/delete.gif"/>
                </app2:checkAccessRight>

<%--
                <c:if test="${hasCommunicationExecute}">
                    <fanta:actionColumn name="generate" styleClass="listItemCenter" headerStyle="listHeader" width="25%"
                                        render="false">
                        <c:if test="${(communication.templateId != null && communication.templateId != '') || (communication.type==LETTER && app2:isCampaignGenerationCommunication(communication.contactId))}">
                            <a href="javascript:go(${communication.contactId}, ${communication.type}, ${communication.addressId})"
                               title="${communication.status=='1' ? viewDocumentKey : generateDocumentKey}">
                                <img src="<c:out value="${sessionScope.baselayout}"/>/img/${communication.status=='1' ? 'openfile.png' : 'openfilegrey.png'}"
                                     alt="" border="0"/>
                            </a>
                        </c:if>
                    </fanta:actionColumn>
                </c:if>
--%>
<%--
                <c:if test="${param.module == 'contacts'}">
                    <app2:checkAccessRight functionality="TASK" permission="CREATE">
                        <fanta:actionColumn name="createTask" title="Scheduler.task.new"
                                            action="Task/Forward/Create.do?index=${indexTask}&dto(from)=contacts&dto(contactPersonId)=${communication.contactPersonId}&dto(processId)=${communication.processId}&dto(processName)=${communication.processName}&tabKey=Scheduler.Tasks"
                                            styleClass="listItemCenter" headerStyle="listHeader" width="25%"
                                            image="${baselayout}/img/newtask.png"/>
                    </app2:checkAccessRight>
                </c:if>
--%>
            </fanta:columnGroup>


            <fanta:dataColumn name="note"
                              action="MainCommunication/Forward/Update.do?contactId=${communication.addressId}&dto(note)=${app2:encode(communication.note)}&dto(contactId)=${communication.contactId}&dto(type)=${communication.type}${caseId}&tabKey=Contacts.Tab.communications"
                              title="Document.subject" styleClass="listItem" headerStyle="listHeader"
                              width="20%" orderable="true"/>

            <fanta:dataColumn name="contactName" styleClass="listItem" title="Contact.title"
                              headerStyle="listHeader" width="20%" orderable="true"/>

            <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="Document.contactPerson"
                              headerStyle="listHeader" width="18%" orderable="true"/>

            <fanta:dataColumn name="employeeName" styleClass="listItem" title="Document.employee"
                              headerStyle="listHeader" width="15%" orderable="true" maxLength="25"/>

<%--
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
--%>

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
                    <html:img src="${baselayout}/img/out_.gif" border="0" altKey="Document.out"
                              titleKey="Document.out"/>
                </c:if>
                <c:if test="${communication.inOut=='1'}">
                    <html:img src="${baselayout}/img/in_.gif" border="0" altKey="Document.in" titleKey="Document.in"/>
                </c:if>
            </fanta:dataColumn>
        </fanta:table>
    </td>
</tr>
</table>