<%@ include file="/Includes.jsp" %>
<c:choose>
    <c:when test="${op=='delete'}">
        <c:set var="focusElementName" value="dto(submit)"/>
    </c:when>
    <c:otherwise>
        <c:set var="focusElementName" value="dto(folderName)"/>
    </c:otherwise>
</c:choose>


<html:form action="${action}" focus="${focusElementName}">
    <%--if be go in from compose--%>
    <c:if test="${param['inCompose'] != null || setInForm != null }">
        <html:hidden property="dto(ofCompose)" value="compose"/>
    </c:if>

    <html:hidden property="dto(op)" value="${op}"/>
    <c:if test="${ op=='update' || op=='delete' }">
        <html:hidden property="dto(folderId)"/>
    </c:if>

    <c:if test="${ op=='create'}">
        <html:hidden property="dto(isOpen)" value="false"/>
    </c:if>

    <table id="Folder.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

        <TR>
            <TD colspan="2" class="title">
                <c:out value="${title}"/>
            </TD>
        </TR>
        <c:if test="${op!='delete'}">
            <TR>
                <TD class="label" width="30%">
                    <fmt:message key="Webmail.folder.name"/>
                </TD>
                <TD class="contain" width="70%">
                    <app:text property="dto(folderName)" styleClass="middleText" maxlength="50" tabindex="1"
                              view="${op == 'delete'}"/>
                </TD>
            </TR>
            <tr>
                <td class="label">
                    <fmt:message key="Webmail.folder.columnToShow"/>
                </td>
                <td class="contain">
                    <c:set var="columnShowList" value="${app2:getColumnToShowTypes(pageContext.request)}"/>
                    <html:select property="dto(columnToShow)" styleClass="middleSelect" tabindex="2" readonly="${op == 'delete'}">
                        <html:option value="">&nbsp;</html:option>
                        <html:options collection="columnShowList" property="value" labelProperty="label"/>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td class="label">
                    <fmt:message key="Webmail.folder.parentFolder"/>
                </td>
                <td class="contain">
                    <c:set var="foldersList" value="${app2:getFoldersForParent(pageContext.request, sessionScope.user.valueMap['userId'], FolderForm.dtoMap['folderId'])}"/>
                    <html:select property="dto(parentFolderId)" styleClass="middleSelect" tabindex="3" readonly="${op == 'delete'}">
                        <html:option value="">&nbsp;</html:option>
                        <html:options collection="foldersList" property="folderId" labelProperty="folderName"/>
                    </html:select>
                </td>
            </tr>
        </c:if>
        <c:if test="${op=='delete'}">
            <tr>
                <td colspan="2" class="contain" >
                    <fmt:message key="Webmail.deleteMailsFromFolder">
                        <fmt:param value="${FolderForm.dtoMap['folderMails']}"/>
                        <fmt:param value="${FolderForm.dtoMap['folderName']}"/>
                    </fmt:message><br/>
                    <c:if test="${FolderForm.dtoMap['folderMailContacts']>0}">
                        <fmt:message key="Webmail.deleteFolderMailContacts">
                            <fmt:param value="${FolderForm.dtoMap['folderMailContacts']}"/>
                        </fmt:message><br/>
                    </c:if>
                    <c:if test="${FolderForm.dtoMap['folderMailActions']>0}">
                        <fmt:message key="Webmail.deleteFolderMailActions">
                            <fmt:param value="${FolderForm.dtoMap['folderMailActions']}"/>
                        </fmt:message><br/>
                    </c:if>
                    <c:if test="${FolderForm.dtoMap['folderMailActivities']>0}">
                        <fmt:message key="Webmail.deleteFolderMailActivities">
                            <fmt:param value="${FolderForm.dtoMap['folderMailActivities']}"/>
                        </fmt:message><br/>
                    </c:if>
                    <c:if test="${not empty FolderForm.dtoMap['subFoldersList']}">
                        <fmt:message key="Webmail.deleteSubFolders"/><br/>
                        <c:forEach var="folder" items="${FolderForm.dtoMap['subFoldersList']}">
                            &nbsp;&nbsp;${folder.folderName}<br/>
                        </c:forEach>
                    </c:if>
                </td>
            </tr>
        </c:if>
    </table>
    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
        <TR>
            <TD class="button">
                <app2:securitySubmit operation="${op}" functionality="WEBMAILFOLDER" styleClass="button" tabindex="2" property="dto(submit)">
                    <c:out value="${button}"/></app2:securitySubmit>
                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}" functionality="WEBMAILFOLDER" styleClass="button"
                                         property="SaveAndNew" tabindex="3"><fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:submit property="dto(cancel)" styleClass="button" tabindex="4"><fmt:message key="Common.cancel"/>
                </html:submit>
            </TD>
        </TR>
    </table>

</html:form>