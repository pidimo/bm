<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<app:url value="SalesProcess/Action/List.do" var="urlList" enableEncodeURL="false"/>

<table cellpadding="0" cellspacing="0" border="0" id="tableId" width="95%" align="center">
    <tr>
        <td align="left">
            <app2:checkAccessRight functionality="SALESPROCESSACTION" permission="CREATE">
                <app:url value="SalesProcess/Action/Forward/Create.do" var="url"/>
                <html:submit property="new" styleClass="button" onclick="window.parent.location='${url}'">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </app2:checkAccessRight>
        </td>
    </tr>

    <tr>
        <td>
            <table border="0" width="100%" bgcolor="black" class="container" align="center" cellpadding="0"
                   cellspacing="0">
                <tr>
                    <td class="title">
                        <fmt:message key="SalesProcessAction.plural"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <fanta:table list="actionList"
                                     width="100%"
                                     id="action"
                                     action="${urlList}"
                                     imgPath="${baselayout}"
                                     align="center"
                                     withContext="false">
                            <app:url
                                    value="SalesProcess/Action/Forward/Update.do?cmd=true&dto(contactId)=${action.contactId}&dto(processId)=${action.processId}&dto(note)=${app2:encode(action.note)}&dto(type)=${action.telecomType}"
                                    var="urlUpdate"/>
                            <app:url
                                    value="SalesProcess/Action/Forward/Delete.do?cmd=true&dto(contactId)=${action.contactId}&dto(processId)=${action.processId}&dto(note)=${app2:encode(action.note)}&dto(type)=${action.telecomType}&dto(withReferences)=true"
                                    var="urlDelete"/>

                            <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                                <app2:checkAccessRight functionality="SALESPROCESSACTION" permission="VIEW">
                                    <fanta:actionColumn name="update"
                                                        title="Common.update"
                                                        useJScript="true"
                                                        action="javascript:goParentURL('${urlUpdate}')"
                                                        styleClass="listItem"
                                                        headerStyle="listHeader"
                                                        image="${baselayout}/img/edit.gif"/>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="SALESPROCESSACTION" permission="DELETE">
                                    <fanta:actionColumn name="delete"
                                                        title="Common.delete"
                                                        useJScript="true"
                                                        action="javascript:goParentURL('${urlDelete}')"
                                                        styleClass="listItem"
                                                        headerStyle="listHeader"
                                                        image="${baselayout}/img/delete.gif"/>
                                </app2:checkAccessRight>
                            </fanta:columnGroup>
                            <fanta:dataColumn name="number"
                                              useJScript="true"
                                              action="javascript:goParentURL('${urlUpdate}')"
                                              title="SalesProcessAction.number"
                                              styleClass="listItem"
                                              headerStyle="listHeader"
                                              width="10%"
                                              orderable="true"
                                              maxLength="40"/>
                            <fanta:dataColumn name="note"
                                              useJScript="true"
                                              action="javascript:goParentURL('${urlUpdate}')"
                                              title="Document.description"
                                              styleClass="listItem"
                                              headerStyle="listHeader"
                                              width="10%"
                                              orderable="true"
                                              maxLength="40"/>
                            <fanta:dataColumn name="contactPersonName"
                                              styleClass="listItem"
                                              title="Document.contactPerson"
                                              headerStyle="listHeader"
                                              width="15%"
                                              orderable="true"/>
                            <fanta:dataColumn name="actionTypeName"
                                              styleClass="listItem"
                                              title="SalesProcessAction.actionType"
                                              headerStyle="listHeader"
                                              width="20%"
                                              orderable="true"
                                              maxLength="40"/>
                            <fanta:dataColumn name="employeeName"
                                              styleClass="listItem"
                                              title="Document.employee"
                                              headerStyle="listHeader"
                                              width="15%"
                                              orderable="true"
                                              maxLength="20"/>
                            <fanta:dataColumn name="date"
                                              styleClass="listItem"
                                              title="Document.date"
                                              headerStyle="listHeader"
                                              width="10%"
                                              orderable="true"
                                              renderData="false">
                                <fmt:formatDate var="dateValue"
                                                value="${app2:intToDate(action.date)}"
                                                pattern="${datePattern}"/>
                                ${dateValue}&nbsp;
                            </fanta:dataColumn>
                            <fanta:dataColumn name="value"
                                              styleClass="listItemRight"
                                              title="SalesProcess.value"
                                              headerStyle="listHeader"
                                              width="10%"
                                              orderable="true"
                                              renderData="false">
                                <fmt:formatNumber value="${action.value}" type="number" pattern="${numberFormat}"/>&nbsp;
                            </fanta:dataColumn>
                            <fanta:dataColumn name="active"
                                              styleClass="listItem"
                                              title="Common.active"
                                              headerStyle="listHeader"
                                              width="5%"
                                              renderData="false"
                                              style="text-align:center">
                                <c:choose>
                                    <c:when test="${action.active == 1}">
                                        <img align="center" src="${sessionScope.baselayout}/img/check.gif" alt=""/>
                                    </c:when>
                                    <c:otherwise>
                                        &nbsp;
                                    </c:otherwise>
                                </c:choose>
                            </fanta:dataColumn>
                            <fanta:dataColumn name="telecomType"
                                              styleClass="listItem2"
                                              title="Document.mediaType"
                                              headerStyle="listHeader"
                                              width="5%"
                                              orderable="false"
                                              renderData="false">
                                <c:choose>
                                    <c:when test="${action.telecomType=='0'}">
                                        <fmt:message key="Communication.type.phone" var="typeName"/>
                                    </c:when>
                                    <c:when test="${action.telecomType=='1'}">
                                        <fmt:message key="Communication.type.meeting" var="typeName"/>
                                    </c:when>
                                    <c:when test="${action.telecomType=='2'}">
                                        <fmt:message key="Communication.type.fax" var="typeName"/>
                                    </c:when>
                                    <c:when test="${action.telecomType=='3'}">
                                        <fmt:message key="Communication.type.letter" var="typeName"/>
                                    </c:when>
                                    <c:when test="${action.telecomType=='5'}">
                                        <fmt:message key="Communication.type.email" var="typeName"/>
                                    </c:when>
                                    <c:when test="${action.telecomType=='6'}">
                                        <fmt:message key="Communication.type.document" var="typeName"/>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:message key="Communication.type.other" var="typeName"/>
                                    </c:otherwise>
                                </c:choose>
                                <fanta:textShorter title="${typeName}">${typeName}</fanta:textShorter>
                            </fanta:dataColumn>
                        </fanta:table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<script>
    addAppPageEvent(window, 'load', incrementTableInIframe);
</script>