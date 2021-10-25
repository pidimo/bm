<%@ include file="/Includes.jsp" %>
<%@ page import="java.util.List" %>

<%
    List searchItems = JSPHelper.getMessageSearchConstantsForWebmail(request);
    request.setAttribute("searchItems", searchItems);

    List searchFolders = JSPHelper.getFolderSearchConstantsForWebmail(request);
    request.setAttribute("searchFolders", searchFolders);
    if (request.getAttribute("fromAError") == null) {
        request.setAttribute("searchText", request.getParameter("searchText"));
        request.setAttribute("searchFilter", request.getParameter("searchFilter"));
        request.setAttribute("searchFolder", request.getParameter("searchFolder"));
    }

    String folderId = request.getParameter("folderId");
    if (null != folderId) {
        request.setAttribute("searchFolder", folderId);
    }
%>
<html:form action="/Mail/SearchResult.do?mailSearch=${true}" styleId="searchMForm">
    <TABLE width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
        <TR>
            <TD class="title">
                <fmt:message key="Webmail.search"/>
            </td>
        </TR>
        <TR>
            <TD class="contain">
                <fmt:message key="Common.search"/>
                <br>
                <app:text property="searchText" styleClass="shortText"
                          maxlength="50" value="${searchText}"
                          style="width:130px;"></app:text>
            </TD>
        </TR>
        <TR>
            <TD class="contain">

                <html:select property="searchFilter" styleClass="webmailSelect" value="${searchFilter}"
                             style="width:130px;">
                    <html:options collection="searchItems" property="value" labelProperty="label"/>
                </html:select>

            </TD>
        </TR>
        <TR>
            <TD class="contain" nowrap="nowrap">
                <html:select property="searchFolder" styleClass="webmailSelect" value="${searchFolder}"
                             style="width:130px;">
                    <html:option value="${systemFolderCounter['inboxId']}">
                        <fmt:message key="Webmail.folder.inbox"/>
                    </html:option>
                    <html:options collection="searchFolders" property="value" labelProperty="label"/>
                    <html:option value="${systemFolderCounter['sentId']}">
                        <fmt:message key="Webmail.folder.sendItems"/>
                    </html:option>
                    <html:option value="${systemFolderCounter['draftId']}">
                        <fmt:message
                                key="Webmail.folder.draftItems"/>
                    </html:option>
                    <html:option value="${systemFolderCounter['trashId']}">
                        <fmt:message key="Webmail.folder.trash"/>
                    </html:option>
                    <html:option value="${systemFolderCounter['outboxId']}">
                        <fmt:message key="Webmail.folder.outbox"/>
                    </html:option>
                    <c:forEach var="customFolder" items="${customFoldersList}">
                        <html:option value="${customFolder.folderId}">${customFolder.folderName}</html:option>
                    </c:forEach>
                </html:select>
                <html:img src="${baselayout}/img/search.gif" titleKey="Common.search"
                          onclick="javascript:document.getElementById('searchMForm').submit();"
                          border="0" styleClass="imageButton" style="vertical-align:middle;"/>
            </TD>
        </TR>
        <tr>
            <td class="contain" nowrap="nowrap">
                <app:link action="/Mail/MailAdvancedSearch.do?mailSearch=true&mailAdvancedSearch=true">
                    <fmt:message key="Common.advancedSearch"/>
                </app:link>
            </td>
        </tr>

    </TABLE>
</html:form>