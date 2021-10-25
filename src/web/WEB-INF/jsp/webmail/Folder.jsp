<%@ include file="/Includes.jsp" %>
<c:choose>
    <c:when test="${op=='delete'}">
        <c:set var="focusElementName" value="dto(submit)"/>
    </c:when>
    <c:otherwise>
        <c:set var="focusElementName" value="dto(folderName)"/>
    </c:otherwise>
</c:choose>


<html:form action="${action}" focus="${focusElementName}" styleClass="form-horizontal">
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
    <div class="${app2:getFormClasses()}">
        <fieldset>
            <div class="${app2:getFormPanelClasses()}">
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <c:if test="${op!='delete'}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Webmail.folder.name"/>
                        </label>

                        <div class="${app2:getFormContainClasses(op == 'delete')}">
                            <app:text property="dto(folderName)" styleClass="middleText ${app2:getFormInputClasses()}"
                                      maxlength="50" tabindex="1"
                                      view="${op == 'delete'}"/>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Webmail.folder.columnToShow"/>
                        </label>

                        <div class="${app2:getFormContainClasses(op == 'delete')}">
                            <c:set var="columnShowList" value="${app2:getColumnToShowTypes(pageContext.request)}"/>
                            <html:select property="dto(columnToShow)"
                                         styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                         tabindex="2"
                                         readonly="${op == 'delete'}">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="columnShowList" property="value" labelProperty="label"/>
                            </html:select>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Webmail.folder.parentFolder"/>
                        </label>

                        <div class="${app2:getFormContainClasses(op == 'delete')}">
                            <c:set var="foldersList"
                                   value="${app2:getFoldersForParent(pageContext.request, sessionScope.user.valueMap['userId'], FolderForm.dtoMap['folderId'])}"/>
                            <html:select property="dto(parentFolderId)"
                                         styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                         tabindex="3"
                                         readonly="${op == 'delete'}">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="foldersList" property="folderId" labelProperty="folderName"/>
                            </html:select>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
                <c:if test="${op=='delete'}">
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
                </c:if>
            </div>
            <div class="row col-xs-12">
                <app2:securitySubmit operation="${op}" functionality="WEBMAILFOLDER"
                                     styleClass="button ${app2:getFormButtonClasses()} marginButton" tabindex="2"
                                     property="dto(submit)">
                    <c:out value="${button}"/></app2:securitySubmit>
                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}" functionality="WEBMAILFOLDER"
                                         styleClass="button ${app2:getFormButtonClasses()} marginButton"
                                         property="SaveAndNew" tabindex="3"><fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:submit property="dto(cancel)"
                             styleClass="button ${app2:getFormButtonCancelClasses()} marginButton"
                             tabindex="4"><fmt:message key="Common.cancel"/>
                </html:submit>
            </div>
        </fieldset>
    </div>
</html:form>
<tags:jQueryValidation formName="FolderForm"/>