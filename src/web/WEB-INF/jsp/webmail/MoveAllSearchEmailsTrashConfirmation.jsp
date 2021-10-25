<%@ include file="/Includes.jsp" %>

<c:url var="urlCancel" value="/webmail/Mail/SearchResult.do?mailSearch=${true}">
    <c:param name="searchText" value="${param.searchText}"/>
    <c:param name="searchFilter" value="${param.searchFilter}"/>
    <c:param name="searchFolder" value="${param.searchFolder}"/>
    <c:param name="returning" value="true"/>
</c:url>

<html:form
        action="${action}?searchText=${param.searchText}&searchFilter=${param.searchFilter}&searchFolder=${param.searchFolder}"
        styleClass="form-horizontal">
    <html:hidden property="dto(op)" value="moveToTrash"/>
    <c:forEach var="key" items="${searchMailForm.dto['emailIdentifiers']}">
        <html:hidden property="dto(mailIds)" value="${key}" styleId="dto(mailIds)"/>
    </c:forEach>
    <div class="${app2:getFormClassesLarge()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                        ${title}
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <div class="${app2:getFormContainClasses(null)}">
                        <c:set var="numberOfEmails" value="${searchMailForm.dto['folderSize']}"/>
                        <fmt:message key="Webmail.email.moveSearchEmailsToTrash">
                            <fmt:param value="${numberOfEmails}"/>
                        </fmt:message>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="delete" property="dto(delete)" functionality="MAIL"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 tabindex="3"><fmt:message key="Common.delete"/></app2:securitySubmit>
            <html:button property="" styleClass="button ${app2:getFormButtonCancelClasses()}"
                         onclick="location.href='${urlCancel}'">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </div>
    </div>
</html:form>