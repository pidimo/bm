<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <legend class="title">
        <fmt:message key="Webmail.systemFolder"/>
    </legend>
    <div class="table-responsive">
        <table class="${app2:getFantabulousTableClases()}" width="100%">
            <tr align="center">
                <th width="75%" class="listHeader">
                    <fmt:message key="Webmail.folder.title.name"/>
                </th>
                <th width="10%" class="listHeader">
                    <fmt:message key="Webmail.folder.mails"/>
                </th>
                <th width="15%" class="listHeader">
                    <fmt:message key="Webmail.folder.mailsSize"/>
                </th>
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
    </div>
    <html:form action="/Mail/Forward/CreateFolder.do">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="create" functionality="WEBMAILFOLDER"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 property="dto(new)">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>
    <legend class="title">
        <fmt:message key="Webmail.customFolder"/>
    </legend>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="userFoldersGroupedList" width="100%" id="listF" action="Mail/Forward/ListFolder.do"
                     styleClass="${app2:getFantabulousTableClases()}"
                     imgPath="${baselayout}" align="center">

            <c:set var="urlEdit"
                   value="Mail/Forward/UpdateFolder.do?dto(folderId)=${listF.id_folder}&dto(folderName)=${app2:encode(listF.name_folder)}&dto(op)=read"/>
            <c:set var="urlDelete"
                   value="Mail/Forward/DeleteFolder.do?dto(folderId)=${listF.id_folder}&dto(folderName)=${app2:encode(listF.name_folder)}&dto(op)=readDelete"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="WEBMAILFOLDER" permission="VIEW">
                    <fanta:actionColumn name="edit" label="Common.update" title="Common.update" action="${urlEdit}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="WEBMAILFOLDER" permission="DELETE">
                    <fanta:actionColumn name="delete" label="Common.delete" title="Common.delete"
                                        action="${urlDelete}" styleClass="listItem" headerStyle="listHeader"
                                        width="50%" glyphiconClass="${app2:getClassGlyphTrash()}"/>
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
    </div>
</div>
