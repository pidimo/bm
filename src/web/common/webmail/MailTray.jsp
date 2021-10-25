<%@ include file="/Includes.jsp" %>
<%@ page import="com.piramide.elwis.utils.ContactConstants,
                 com.piramide.elwis.utils.WebMailConstants,
                 java.util.List" %>

<script language="JavaScript" type="text/javascript">
    function check()
    {
        field = document.getElementById('mailTrayForm').selectedMails;
        guia = document.getElementById('mailTrayForm').mail;
        var i;
        if (guia.checked)
        {
            for (i = 0; i < field.length; i++)
                field[i].checked = true;
        }
        else
        {
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
        return(test);
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
    <table id="MailTray.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
        <TR>
            <td>
                <table id="MailTray.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" heigth="50%"
                       align="center">
                    <TR>
                        <TD class="title">
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

                        </TD>
                    </TR>
                </table>
            </td>
        </TR>
        <TR>
            <TD>
                <table id="MailTray.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" align="center"
                       class="container">
                    <TR>
                        <c:choose>
                            <c:when test="${mailTrayForm.dto.folderType== FOLDER_TRASH}">
                                <td class="contain" nowrap="true">
                                    <app2:checkAccessRight functionality="MAIL" permission="DELETE">
                                        <html:hidden property="delete" styleId="deleteId"/>
                                        <a href="#" onclick="javascript:setHiddenValue('deleteId','delete');"
                                           style="text-decoration: none;">
                                            <html:img src="${baselayout}/img/delete.gif"
                                                      titleKey="Common.delete"
                                                      border="0" styleClass="imageButton"
                                                      style="vertical-align:middle;"/>
                                        </a>

                                        <html:hidden property="emptyFolder" styleId="emptyFolderId"/>
                                        <a href="#" onclick="javascript:setHiddenValue('emptyFolderId','emptyFolder');"
                                           style="text-decoration: none;">
                                            <html:img src="${baselayout}/img/deleteall.gif"
                                                      titleKey="Webmail.tray.empty"
                                                      border="0" styleClass="imageButton"
                                                      style="vertical-align:middle;"/>
                                            <html:hidden property="toEmptyFolder" value="${mailTrayForm.dto.folderId}"/>
                                        </a>
                                    </app2:checkAccessRight>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td class="contain" nowrap="true">
                                    <html:hidden property="moveToTrash" styleId="moveToTrashId"/>
                                    <a href="#" onclick="javascript:setHiddenValue('moveToTrashId','moveToTrash');"
                                       style="text-decoration: none;">
                                        <html:img src="${baselayout}/img/delete.gif"
                                                  titleKey="Webmail.moveToTrash"
                                                  border="0" styleClass="imageButton" style="vertical-align:middle;"/>
                                    </a>

                                    <html:hidden property="emptyFolderToTrash" styleId="emptyFolderToTrashId"/>
                                    <a href="#"
                                       onclick="javascript:setHiddenValue('emptyFolderToTrashId','emptyFolderToTrash');"
                                       style="text-decoration: none;">
                                        <html:img src="${baselayout}/img/deleteall.gif"
                                                  titleKey="Webmail.moveAllToTrash"
                                                  border="0" styleClass="imageButton" style="vertical-align:middle;"/>
                                    </a>
                                </td>
                            </c:otherwise>
                        </c:choose>

                        <c:if test="${mailTrayForm.dto.folderType!=FOLDER_OUTBOX}">
                            <TD width="9%" class="label" style="text-align:right;">
                                <c:set var="valueList3" value="${mailTrayForm.dto.folderConstants}"/>
                                <html:button styleClass="button" property="dto(button)" onclick="moveToFolderAction()">
                                    <fmt:message key="Webmail.tray.moveTo"/>
                                </html:button>
                            </TD>
                            <TD width="24%" class="contain">
                                <html:select property="moveTo" styleClass="webmailSelect">
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
                            </TD>
                        </c:if>

                        <TD width="9%" class="label">
                            <fmt:message key="Webmail.tray.filter"/>
                        </TD>
                        <TD width="24%" class="contain">
                            <html:select property="mailFilter" styleClass="webmailSelect" onchange="submit()">
                                <html:options collection="mailTrayFilterList" property="value" labelProperty="label"/>
                            </html:select>
                        </TD>
                        <c:choose>
                            <c:when test="${mailTrayForm.dto.folderType!=FOLDER_SENDITEMS && mailTrayForm.dto.folderType!=FOLDER_DRAFTITEMS && mailTrayForm.dto.folderType!=FOLDER_OUTBOX}">
                                <TD width="9%" class="label">
                                    <fmt:message key="Webmail.tray.markAs"/>
                                </TD>
                                <TD width="24%" class="contain">
                                    <c:set var="valueList2" value="${mailTrayForm.dto.actionConstants}"/>
                                    <html:select property="markAs" styleClass="webmailSelect" onchange="submit()">
                                        <option value=""></option>
                                        <html:options collection="markAsList" property="value" labelProperty="label"/>
                                    </html:select>
                                </TD>
                            </c:when>
                            <c:otherwise>
                                <c:if test="${mailTrayForm.dto.folderType==FOLDER_OUTBOX}">
                                    <TD width="33%" class="contain">&nbsp;</TD>
                                </c:if>
                                <TD width="33%" class="contain">&nbsp;</TD>
                            </c:otherwise>
                        </c:choose>

                    </TR>
                </table>
                <br>
            </TD>
        </TR>
    </table>
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
            <td>
                <div id="emailListId" width="100%">
                    <c:import url="/common/webmail/WebmailListFragment.jsp"/>
                </div>
            </td>
        </tr>
    </table>
</html:form>