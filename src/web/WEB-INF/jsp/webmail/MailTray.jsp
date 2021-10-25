<%@ include file="/Includes.jsp" %>
<%@ page import="com.piramide.elwis.utils.ContactConstants,
                 com.piramide.elwis.utils.WebMailConstants,
                 java.util.List" %>

<app:url var="emailComposeUrl" value="/webmail/Mail/Forward/ComposeEmail.do" contextRelative="true"
         addModuleParams="false" addModuleName="false"/>

<script language="JavaScript" type="text/javascript">
    function check() {
        field = document.getElementById('mailTrayForm').selectedMails;
        guia = document.getElementById('mailTrayForm').mail;
        var i;
        if (guia.checked) {
            for (i = 0; i < field.length; i++)
                field[i].checked = true;
        }
        else {
            for (i = 0; i < field.length; i++)
                field[i].checked = false;
        }
    }

    function setHiddenValue(hiddenId, hiddenValue) {
        document.getElementById(hiddenId).value = hiddenValue;
        document.getElementById('mailTrayForm').submit();
    }

    function moveToFolderAction() {
        document.getElementById('moveToButtonId').value = "true";
        document.getElementById('mailTrayForm').submit();
    }

    function testSubmit() {
        var test = (document.getElementById('moveToButtonId').value == "true");
        return (test);
    }

    function redirectComposeEmail() {
        location.href = '${emailComposeUrl}';
    }
</script>
<%--MarkAs Constants for select box--%>
<% List markAsList = JSPHelper.getMarkAsList(request);
    request.setAttribute("markAsList", markAsList);%>
<%--Filter Constants for select box--%>
<% List mailTrayFilterList = JSPHelper.getMailTrayFilterList(request);
    request.setAttribute("mailTrayFilterList", mailTrayFilterList);%>

<%--Folder_type's--%>
<c:set var="FOLDER_INBOX" value="<%=WebMailConstants.FOLDER_INBOX%>"/>
<c:set var="FOLDER_SENDITEMS" value="<%=WebMailConstants.FOLDER_SENDITEMS%>"/>
<c:set var="FOLDER_DRAFTITEMS" value="<%=WebMailConstants.FOLDER_DRAFTITEMS%>"/>
<c:set var="FOLDER_TRASH" value="<%=WebMailConstants.FOLDER_TRASH%>"/>
<c:set var="FOLDER_OUTBOX" value="<%=WebMailConstants.FOLDER_OUTBOX%>"/>
<c:set var="FOLDER_DEFAULT" value="<%=WebMailConstants.FOLDER_DEFAULT%>"/>

<%--AddressType constants--%>
<c:set var="ADDRESSTYPE_PERSON" value="<%=ContactConstants.ADDRESSTYPE_PERSON%>"/>
<c:set var="ADDRESSTYPE_ORGANIZATION" value="<%=ContactConstants.ADDRESSTYPE_ORGANIZATION%>"/>

<html:form action="/Mail/MailTray.do?folderId=${mailTrayForm.dto.folderId}" styleId="mailTrayForm"
           onsubmit="return testSubmit()">
    <html:hidden property="moveToButton" styleId="moveToButtonId"/>
    <div id="MailTray.jsp" class="wrapperSearch">

        <div id="MailTray.jsp">
            <fieldset>
                <legend class="title">
                    <c:choose>
                        <c:when test="${mailTrayForm.dto.folderType==FOLDER_INBOX}">
                            <fmt:message key="Webmail.folder.inbox"/> ${systemFolderCounter['inboxSize']}
                            <fmt:message
                                    key="Webmail.tray.mails"/>
                        </c:when>
                        <c:when test="${mailTrayForm.dto.folderType==FOLDER_SENDITEMS}">
                            <fmt:message key="Webmail.folder.sendItems"/> ${systemFolderCounter['sentSize']}
                            <fmt:message key="Webmail.tray.mails"/>
                        </c:when>
                        <c:when test="${mailTrayForm.dto.folderType==FOLDER_DRAFTITEMS}">
                            <fmt:message key="Webmail.folder.draftItems"/> ${systemFolderCounter['draftSize']}
                            <fmt:message key="Webmail.tray.mails"/>
                        </c:when>
                        <c:when test="${mailTrayForm.dto.folderType==FOLDER_TRASH}">
                            <fmt:message key="Webmail.folder.trash"/> ${systemFolderCounter['trashSize']}
                            <fmt:message
                                    key="Webmail.tray.mails"/>
                        </c:when>

                        <c:when test="${mailTrayForm.dto.folderType==FOLDER_OUTBOX}">
                            <fmt:message key="Webmail.folder.outbox"/> ${systemFolderCounter['outboxSize']}
                            <fmt:message
                                    key="Webmail.tray.mails"/>
                        </c:when>

                        <c:when test="${mailTrayForm.dto.folderType==FOLDER_DEFAULT}">
                            ${mailTrayForm.dto.folderName} ${mailTrayForm.dto.folderSize} <fmt:message
                                key="Webmail.tray.mails"/>
                        </c:when>
                    </c:choose>

                </legend>
            </fieldset>
        </div>

        <div id="MailTray.jsp">
            <c:choose>
                <c:when test="${mailTrayForm.dto.folderType== FOLDER_TRASH}">
                    <div class="col-xs-6 col-sm-3 col-md-1 col-lg-1 pull-left paddingRemove">
                        <app2:checkAccessRight functionality="MAIL" permission="DELETE">
                            <div class="col-xs-4 paddingRemove">
                                <html:hidden property="delete" styleId="deleteId"/>
                                <html:link styleClass="btn btn-link" titleKey="Common.delete" href="#"
                                           onclick="javascript:setHiddenValue('deleteId','delete');"
                                           style="text-decoration: none;">
                                    <span class="glyphicon glyphicon-trash"></span>
                                </html:link>
                            </div>
                            <div class="col-xs-8 paddingRemove">
                                <html:hidden property="emptyFolder" styleId="emptyFolderId"/>
                                <html:link styleClass="btn btn-link" titleKey="Webmail.tray.empty" href="#"
                                           onclick="javascript:setHiddenValue('emptyFolderId','emptyFolder');"
                                           style="text-decoration: none;">
                                    <span class="glyphicon glyphicon-trash"></span>
                                    <span class="glyphicon glyphicon-trash"></span>
                                    <html:hidden property="toEmptyFolder" value="${mailTrayForm.dto.folderId}"/>
                                </html:link>
                            </div>
                        </app2:checkAccessRight>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="col-xs-6 col-sm-3 col-md-1 col-lg-1 pull-left paddingRemove">
                        <div class="col-xs-6 paddingRemove">
                            <html:hidden property="moveToTrash" styleId="moveToTrashId"/>
                            <c:set var="trash" value="${Webmail.moveToTrash}"/>
                            <html:link styleClass="btn btn-link" href="#" titleKey="Webmail.moveToTrash"
                                       onclick="javascript:setHiddenValue('moveToTrashId','moveToTrash');"
                                       style="text-decoration: none;">
                                <span class="glyphicon glyphicon-trash"></span>
                            </html:link>
                        </div>
                        <div class="col-xs-6 paddingRemove">
                            <html:hidden property="emptyFolderToTrash" styleId="emptyFolderToTrashId"/>
                            <html:link styleClass="btn btn-link" href="#" titleKey="Webmail.moveAllToTrash"
                                       onclick="javascript:setHiddenValue('emptyFolderToTrashId','emptyFolderToTrash');"
                                       style="text-decoration: none;">
                                <span class="glyphicon glyphicon-trash"></span>
                                <span class="glyphicon glyphicon-trash"></span>
                            </html:link>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>

            <div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 marginButton">
                <html:button styleClass="${app2:getFormButtonClasses()}" property="dto(emailComposeLink)"
                             onclick="redirectComposeEmail()" style="white-space: normal;">
                    <fmt:message key="Webmail.compose"/>
                </html:button>
            </div>

            <c:if test="${mailTrayForm.dto.folderType!=FOLDER_OUTBOX}">
                <div class="col-xs-12 col-sm-6 col-md-3 col-lg-3 marginButton">
                    <div class="row">
                        <c:set var="valueList3" value="${mailTrayForm.dto.folderConstants}"/>
                        <div class="col-xs-6">
                            <html:button styleClass="${app2:getFormButtonClasses()}" property="dto(button)"
                                         onclick="moveToFolderAction()" style="white-space: normal;">
                                <fmt:message key="Webmail.tray.moveTo"/>
                            </html:button>
                        </div>
                        <div class="col-xs-6">
                            <html:select property="moveTo" styleClass="${app2:getFormSelectClasses()} webmailSelect">
                                <option value=""></option>
                                <html:option value="${systemFolderCounter['inboxId']}"><fmt:message
                                        key="Webmail.folder.inbox"/>
                                </html:option>
                                <html:option value="${systemFolderCounter['sentId']}"><fmt:message
                                        key="Webmail.folder.sendItems"/></html:option>
                                <html:option value="${systemFolderCounter['draftId']}"><fmt:message
                                        key="Webmail.folder.draftItems"/></html:option>
                                <html:option value="${systemFolderCounter['trashId']}"><fmt:message
                                        key="Webmail.folder.trash"/>
                                </html:option>
                                <html:options collection="valueList3" property="mailFolderId"
                                              labelProperty="mailFolderName"/>
                            </html:select>
                        </div>
                    </div>
                </div>
            </c:if>
            <div class="col-xs-12 col-sm-6 col-md-3 col-lg-3 marginButton">
                <label class="control-label col-xs-4 paddingLeftRemove form-control-static"
                       style="text-align: right;">
                    <fmt:message key="Webmail.tray.filter"/>
                </label>

                <div class="col-xs-8 paddingRemove">
                    <html:select property="mailFilter" styleClass="${app2:getFormSelectClasses()} webmailSelect"
                                 onchange="submit()">
                        <html:options collection="mailTrayFilterList" property="value" labelProperty="label"/>
                    </html:select>
                </div>
            </div>
            <c:choose>
                <c:when test="${mailTrayForm.dto.folderType!=FOLDER_SENDITEMS && mailTrayForm.dto.folderType!=FOLDER_DRAFTITEMS && mailTrayForm.dto.folderType!=FOLDER_OUTBOX}">
                    <div class="col-xs-12 col-sm-6 col-md-3 col-lg-3">
                        <label class="control-label col-xs-4 col-sm-7 paddingLeftRemove form-control-static"
                               style="text-align: right;">
                            <fmt:message key="Webmail.tray.markAs"/>
                        </label>

                        <div class="col-xs-8 col-sm-5 paddingRemove">
                            <c:set var="valueList2" value="${mailTrayForm.dto.actionConstants}"/>
                            <html:select property="markAs" styleClass="${app2:getFormSelectClasses()} webmailSelect"
                                         onchange="submit()">
                                <option value=""></option>
                                <html:options collection="markAsList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:if test="${mailTrayForm.dto.folderType==FOLDER_OUTBOX}">
                        <%--<TD width="33%" class="contain">&nbsp;</TD>--%>
                    </c:if>
                    <%--<TD width="33%" class="contain">&nbsp;</TD>--%>
                </c:otherwise>
            </c:choose>
        </div>

    </div>

    <div id="emailListId">
        <c:import url="/WEB-INF/jsp/webmail/WebmailListFragment.jsp"/>
    </div>

</html:form>