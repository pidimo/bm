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
<html:form action="/Mail/SearchResult.do?mailSearch=${true}" styleId="searchMForm" styleClass="form-horizontal">
    <fildset>

        <legend class="title">
            <fmt:message key="Webmail.search"/>
        </legend>

        <div class="${app2:getFormGroupClasses()}">
            <label class="col-xs-12" for="searchText_id">
                <fmt:message key="Common.search"/>
            </label>

            <div class="col-xs-12">
                <app:text property="searchText" styleId="searchText_id"
                          styleClass="${app2:getFormInputClasses()} shortText"
                          maxlength="50" value="${searchText}"></app:text>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
        <div class="${app2:getFormGroupClasses()}">
            <div class="col-xs-12">
                <html:select property="searchFilter" styleClass="${app2:getFormSelectClasses()} webmailSelect"
                             value="${searchFilter}">
                    <html:options collection="searchItems" property="value" labelProperty="label"/>
                </html:select>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
        <div class="${app2:getFormGroupClasses()}">
            <div class="col-xs-12">
                <html:select property="searchFolder" styleClass="${app2:getFormSelectClasses()} webmailSelect"
                             value="${searchFolder}">
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
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>

        </div>
        <div class="${app2:getFormGroupClasses()}">
            <div class="col-xs-12">
                <html:link styleClass="${app2:getFormButtonClasses()}" titleKey="Common.search" href="#"
                           onclick="javascript:document.getElementById('searchMForm').submit();">
                    <span class="glyphicon glyphicon-search"></span>
                </html:link>
            </div>
        </div>
        <div class="${app2:getFormGroupClasses()}">
            <div class="col-xs-12">
                <app:link styleClass="pull-left"
                          action="/Mail/MailAdvancedSearch.do?mailSearch=true&mailAdvancedSearch=true">
                    <fmt:message key="Common.advancedSearch"/>
                </app:link>
            </div>
        </div>
    </fildset>
</html:form>

<tags:jQueryValidation formName="searchMailForm"/>