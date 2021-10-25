<%@ include file="/Includes.jsp" %>

<c:url var="urlCancel" value="/webmail/Mail/MailAdvancedSearch.do?mailSearch=true&mailAdvancedSearch=true"/>

<html:form action="${action}?mailSearch=true&mailAdvancedSearch=true" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="moveToTrash"/>
        <c:forEach var="key" items="${emailIdentifiersAdvancedList}">
            <html:hidden property="dto(mailIds)" value="${key}" styleId="dto(mailIds)"/>
        </c:forEach>
        <div class="${app2:getFormPanelClasses()}">
            <legend class="title">
                    ${title}
            </legend>
            <div class="${app2:getFormGroupClasses()}">
                <div class="${app2:getFormContainClasses(null)}">
                    <c:set var="numberOfEmails" value="${emailIdentifiersAdvancedSize}"/>
                    <fmt:message key="Webmail.email.moveSearchEmailsToTrash">
                        <fmt:param value="${numberOfEmails}"/>
                    </fmt:message>
                </div>
            </div>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="delete" property="dto(delete)" functionality="MAIL" styleClass="button ${app2:getFormButtonClasses()}"
                                 tabindex="3"><fmt:message key="Common.delete"/></app2:securitySubmit>
            <html:button property="" styleClass="button ${app2:getFormButtonCancelClasses()}" onclick="location.href='${urlCancel}'">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </div>
    </div>
</html:form>