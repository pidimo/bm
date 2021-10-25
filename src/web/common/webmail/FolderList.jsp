<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>


<table border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <tr>
        <td class="title" colspan="3"><fmt:message key="Webmail.systemFolder"/></td>
    </tr>
    <tr align="center">
        <td class="listHeader" width="75%"><fmt:message key="Webmail.folder.title.name"/></td>
        <td class="listHeader" width="10%"><fmt:message key="Webmail.folder.mails"/></td>
        <td class="listHeader" width="15%"><fmt:message key="Webmail.folder.mailsSize"/></td>
    </tr>
    <c:set var="defaultType" value="<%=WebMailConstants.FOLDER_DEFAULT%>"/>
    <fmt:message var="numberFormat" key="numberFormat.1DecimalPlacesOptional"/>

    <fanta:list listName="systemFoldersGroupedList" module="/webmail">
        <fanta:parameter field="userMailId" value="${sessionScope.user.valueMap['userId']}"/>
        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        <fanta:parameter field="folder_type" value="${empty defaultType?0:defaultType}"/>
    </fanta:list>
    <c:choose>
        <c:when test="${empty systemFoldersGroupedList.result}">
            <tr class="listRow">
                <td class="listItem" colspan="3">
                    <fmt:message key="Common.list.empty"/>
                </td>
            </tr>
        </c:when>
        <c:otherwise>
            <c:forEach var="sysFolder" items="${systemFoldersGroupedList.result}">
                <tr class="listRow">
                    <td class="listItem">
                            ${sysFolder.name_folder}
                    </td>
                    <td class="listItemRight">
                        <c:choose>
                            <c:when test="${not empty sysFolder.mailsNumber_folder}">
                                ${sysFolder.mailsNumber_folder}
                            </c:when>
                            <c:otherwise>
                                &nbsp;
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="listItem2Right">

                        <fmt:formatNumber var="numberValue"
                                          value="${app2:getMBytesValueFromBytes(sysFolder.totalSize_folder)}"
                                          type="number" pattern="${numberFormat}"/>

                            ${numberValue}&nbsp;<fmt:message key="Common.megabytes"/>
                    </td>
                </tr>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</table>
</br>


<table cellSpacing=0 cellPadding=2 width="60%" border=0 align="center">
    <TR>
        <html:form action="/Mail/Forward/CreateFolder.do">
            <TD class="button">
                <app2:securitySubmit operation="create" functionality="WEBMAILFOLDER" styleClass="button"
                                     property="dto(new)"><fmt:message key="Common.new"/></app2:securitySubmit>
            </TD>
        </html:form>
    </TR>
</table>

<table id="FolderList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <tr>
        <td class="title"><fmt:message key="Webmail.customFolder"/></td>
    </tr>
    <tr>
        <td>
            <fanta:table list="userFoldersGroupedList" width="100%" id="listF" action="Mail/Forward/ListFolder.do"
                         imgPath="${baselayout}" align="center">

                <c:set var="urlEdit"
                       value="Mail/Forward/UpdateFolder.do?dto(folderId)=${listF.id_folder}&dto(folderName)=${app2:encode(listF.name_folder)}&dto(op)=read"/>
                <c:set var="urlDelete"
                       value="Mail/Forward/DeleteFolder.do?dto(folderId)=${listF.id_folder}&dto(folderName)=${app2:encode(listF.name_folder)}&dto(op)=readDelete"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="WEBMAILFOLDER" permission="VIEW">
                        <fanta:actionColumn name="edit" label="Common.update" title="Common.update" action="${urlEdit}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="WEBMAILFOLDER" permission="DELETE">
                        <fanta:actionColumn name="delete" label="Common.delete" title="Common.delete"
                                            action="${urlDelete}" styleClass="listItem" headerStyle="listHeader"
                                            width="50%" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>

                </fanta:columnGroup>

                <fanta:dataColumn name="name_folder" title="Webmail.folder.title.name" action="${urlEdit}"
                                  styleClass="listItem"
                                  headerStyle="listHeader" width="50%" orderable="true">
                </fanta:dataColumn>
                <fanta:dataColumn name="columnToShow_folder" title="Webmail.folder.columnToShow" styleClass="listItem"
                                  headerStyle="listHeader" width="20%" orderable="true" renderData="false">
                    <c:out value="${app2:getColumnToShowLabel(listF.columnToShow_folder, pageContext.request)}"/>
                </fanta:dataColumn>

                <fanta:dataColumn name="mailsNumber_folder" title="Webmail.folder.mails" styleClass="listItemRight"
                                  headerStyle="listHeader" width="10%" orderable="true">
                </fanta:dataColumn>
                <fanta:dataColumn name="totalSize_folder" title="Webmail.folder.mailsSize" styleClass="listItem2Right"
                                  headerStyle="listHeader" width="15%" renderData="false" orderable="true">
                    <fmt:formatNumber var="numberValue" value="${app2:getMBytesValueFromBytes(listF.totalSize_folder)}"
                                      type="number" pattern="${numberFormat}"/>
                    ${numberValue}&nbsp;<fmt:message key="Common.megabytes"/>
                </fanta:dataColumn>

            </fanta:table>
        </td>
    </tr>
</table>

