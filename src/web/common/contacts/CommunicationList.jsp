<%@ include file="/Includes.jsp" %>
<c:set var="LETTER" value="<%= com.piramide.elwis.utils.CommunicationTypes.LETTER%>"/>
<c:set var="DOCUMENT" value="<%= com.piramide.elwis.utils.CommunicationTypes.DOCUMENT%>"/>
<c:set var="WEB_DOCUMENT" value="<%= com.piramide.elwis.utils.CommunicationTypes.WEB_DOCUMENT%>"/>

<%--another way to import a js source file without inserting the js code here--%>
<app2:jScriptUrl
        url="${commURL}?dto(view)=true&generate=true&dto(generate)=true&dto(op)=update&contactId=${isFromSalesProcess ? param.addressId : param.contactId}&dto(addressId)=${isFromSalesProcess ? param.addressId : param.contactId}&dto(userId)=${sessionScope.user.valueMap['userId']}&dto(companyId)=${sessionScope.user.valueMap['companyId']}&processId=${param.processId}&addressId=${param.addressId}&caseId=${param.caseId}&module=${param.module}&index=${param.index}&campaignId=${param.campaignId}"
        var="jsCommunicationUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="dto(contactId)" value="id"/>
    <app2:jScriptUrlParam param="dto(type)" value="type"/>
</app2:jScriptUrl>
<tags:jscript language="JavaScript" src="/js/contacts/document.jsp"/>

<script>
    function go(id, type) {
        location.href = ${jsCommunicationUrl};
    }
</script>

<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
    <td><br>
        <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
            <TR>
                <td class="label">
                    <fmt:message key="Common.search"/>
                </td>
                <html:form action="/Communication/List.do" focus="parameter(note)">
                    <td class="contain" nowrap>
                        <html:text property="parameter(note)" styleClass="largeText"/>
                        &nbsp;
                        <c:if test="${!isFromContacts}">
                            <html:submit styleClass="button">
                                <fmt:message key="Common.go"/>
                            </html:submit>
                        </c:if>
                    </td>
                    <c:choose>
                        <c:when test="${isFromContacts}">
                            <td class="label">
                                <fmt:message key="SalesProcess"/>
                            </td>
                            <td class="contain" nowrap>
                                <html:text property="parameter(processName)" styleClass="mediumText"/>
                                &nbsp;
                                <html:submit styleClass="button">
                                    <fmt:message key="Common.go"/>
                                </html:submit>
                                &nbsp;
                                <app:link
                                        action="/Communication/AdvancedSearch.do?secondAdvancedListForward=CommAdvancedSearch"><fmt:message
                                        key="Common.advancedSearch"/> </app:link>
                            </td>
                        </c:when>
                    </c:choose>
                </html:form>
            </TR>
            <tr>
                <td colspan="4" align="center" class="alpha">
                    <fanta:alphabet action="Communication/List.do" parameterName="noteAlpha"/>
                </td>
            </tr>
        </table>
    </td>
</tr>

<tr>
<td>
<br/>

<table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
    <tr>
<%--
        <td align="left">
                   <c:if test="${param.module == 'contacts'}">
                   <app2:checkAccessRight functionality="TASK" permission="CREATE">
                       <app:link action="Task/Forward/Create?index=5&dto(from)=contacts" >
                           <fmt:message key="Scheduler.task.new"/>
                       </app:link>
                    </app2:checkAccessRight>
                  </c:if>
        </td>
--%>

        <app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
            <html:form action="/MainCommunication/Forward/Create.do">
                <td class="button">
                    <html:submit styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </td>
            </html:form>
        </app2:checkAccessRight>
    </tr>
</table>

<app2:checkAccessRight functionality="COMMUNICATION" permission="EXECUTE" var="hasCommunicationExecute"/>
<%-- HASTA AQUI LOS ICONOS --%>

<fmt:message var="datePattern" key="datePattern"/>
<fmt:message var="viewDocumentKey" key="Document.viewDocument"/>
<fmt:message var="generateDocumentKey" key="Document.generate"/>
<fmt:message key="Common.download" var="downloadMsg"/>

<c:choose>
    <c:when test="${code == 1 || code == 3}">
        <c:set var="indexTask" value="4"/>
    </c:when>
    <c:when test="${code == 21}">
        <c:set var="indexTask" value="6"/>
    </c:when>
    <c:otherwise>
        <c:set var="indexTask" value="5"/>
    </c:otherwise>
</c:choose>

<c:if test="${sessionScope.user.valueMap['companyId'] == param.contactId}">
    <c:set var="indexTask" value="${indexTask-1}"/>
</c:if>

<c:choose>
    <c:when test="${isFromSalesProcess}">
        <c:set var="noteColumnWidth" value="40%"/>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${isFromCampaign || isSupportCase}">
                <c:set var="noteColumnWidth" value="20%"/>
            </c:when>
            <c:otherwise>
                <c:set var="noteColumnWidth" value="20%"/>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>


<fanta:table list="communicationList" width="100%" id="communication" action="Communication/List.do"
             imgPath="${baselayout}" align="center">

    <c:set var="urlUpdateSalesProcessAction"
           value="SalesProcess/Action/Forward/Update.do?cmd=true&tabKey=Contacts.Tab.salesProcess"/>
    <c:set var="urlUpdateSalesProcess" value="SalesProcess/Forward/Update.do?tabKey=Contacts.Tab.salesProcess"/>

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
                                image="${baselayout}/img/edit.gif"/>
        </app2:checkAccessRight>

        <app2:checkAccessRight functionality="COMMUNICATION" permission="DELETE">

            <fanta:actionColumn name="delete" title="Common.delete"
                                action="MainCommunication/Forward/Delete.do?dto(note)=${app2:encode(communication.note)}&dto(contactId)=${communication.contactId}&dto(type)=${communication.type}&dto(withReferences)=true${caseId}&tabKey=Contacts.Tab.communications"
                                styleClass="listItemCenter" headerStyle="listHeader" width="25%"
                                image="${baselayout}/img/delete.gif"/>
        </app2:checkAccessRight>

        <c:choose>
            <c:when test="${WEB_DOCUMENT == communication.type}">
                <fanta:actionColumn name="generate"
                                    styleClass="listItemCenter"
                                    headerStyle="listHeader"
                                    width="25%"
                                    render="false">

                    <c:set var="docInfo" value="${app2:getCommunicationDocumentInfo(communication.contactId)}"/>
                    <c:if test="${not empty docInfo.freeTextId}">
                        <app:link action="contacts/Download/WebDocument.do?dto(freeTextId)=${docInfo.freeTextId}&dto(contactId)=${communication.contactId}"
                                  title="${downloadMsg}" contextRelative="true">
                            <img src="<c:out value="${sessionScope.baselayout}"/>/img/openfile.png" alt="${downloadMsg}" border="0" align="middle"/>
                        </app:link>
                    </c:if>
                </fanta:actionColumn>
            </c:when>

            <c:when test="${hasCommunicationExecute && DOCUMENT != communication.type}">
                <fanta:actionColumn name="generate" styleClass="listItemCenter" headerStyle="listHeader" width="25%"
                                    render="false">
                    <c:if test="${(communication.templateId != null && communication.templateId != '') || (communication.type==LETTER && app2:isCampaignGenerationCommunication(communication.contactId))}">
                        <a href="javascript:go(${communication.contactId}, ${communication.type})"
                           title="${communication.status=='1' ? viewDocumentKey : generateDocumentKey}">
                            <img src="<c:out value="${sessionScope.baselayout}"/>/img/${communication.status=='1' ? 'openfile.png' : 'openfilegrey.png'}"
                                 alt="" border="0"/>
                        </a>
                    </c:if>
                </fanta:actionColumn>
            </c:when>
            <c:when test="${DOCUMENT == communication.type}">
                <fanta:actionColumn name="generate"
                                    styleClass="listItemCenter"
                                    headerStyle="listHeader"
                                    width="25%"
                                    render="false">
                    <app:link
                            action="${downloadDocumentURL}&communicationId=${communication.contactId}&dto(contactId)=${communication.contactId}"
                            title="${downloadMsg}"
                            addModuleParams="true">
                        <img src="<c:out value="${sessionScope.baselayout}"/>/img/openfile.png"
                             alt="${downloadMsg}" border="0" align="middle"/>
                    </app:link>
                </fanta:actionColumn>
            </c:when>
        </c:choose>

        <c:if test="${param.module == 'contacts'}">
            <app2:checkAccessRight functionality="TASK" permission="CREATE">
                <fanta:actionColumn name="createTask" title="Scheduler.task.new"
                                    action="Task/Forward/Create.do?index=${indexTask}&dto(from)=contacts&dto(contactPersonId)=${communication.contactPersonId}&dto(processId)=${communication.processId}&dto(processName)=${communication.processName}&tabKey=Scheduler.Tasks"
                                    styleClass="listItemCenter" headerStyle="listHeader" width="25%"
                                    image="${baselayout}/img/newtask.png"/>
            </app2:checkAccessRight>
        </c:if>
    </fanta:columnGroup>

    <fanta:dataColumn name="note"
                      action="MainCommunication/Forward/Update.do?dto(note)=${app2:encode(communication.note)}&dto(contactId)=${communication.contactId}&dto(type)=${communication.type}${caseId}&tabKey=Contacts.Tab.communications"
                      title="Document.subject" styleClass="listItem" headerStyle="listHeader" width="${noteColumnWidth}"
                      orderable="true"/>

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

    <fanta:dataColumn name="type" styleClass="listItem" title="Document.mediaType" headerStyle="listHeader" width="7%"
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
            <html:img src="${baselayout}/img/out_.gif" border="0" altKey="Document.out" titleKey="Document.out"/>
        </c:if>
        <c:if test="${communication.inOut=='1'}">
            <html:img src="${baselayout}/img/in_.gif" border="0" altKey="Document.in" titleKey="Document.in"/>
        </c:if>
    </fanta:dataColumn>
</fanta:table>


<table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
    <tr>
        <app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
            <html:form action="/MainCommunication/Forward/Create.do">
                <td class="button">
                    <html:submit styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </td>
            </html:form>
        </app2:checkAccessRight>
    </tr>
</table>
</td>
</tr>
</table>
