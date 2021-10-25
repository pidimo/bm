<%@ include file="/Includes.jsp" %>

<c:set var="roleId"><%=request.getAttribute("roleId")%>
</c:set>
<c:set var="listSize"><%=request.getAttribute("userNameListSize")%>
</c:set>
<div class="${app2:getFormClasses()}">
    <html:form action="/RoleUser/DeleteAll.do" styleClass="form-horizontal">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(roleId)" value="${roleId}"/>
        <html:hidden property="dto(userListSize)" value="${listSize}"/>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="control-label col-xs-12 label-left">
                        <fmt:message key="Contact.name"/>
                    </label>
                </div>
                <c:forEach var="myUser" items="${userNameList}" varStatus="index">
                    <div class="${app2:getFormGroupClasses()}">
                        <div class="${app2:getFormContainClasses(true)}">
                            <html:hidden property="dto(userId_${index.count})" value="${myUser.userId}"/>
                                ${myUser.userName}
                        </div>
                    </div>
                </c:forEach>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
                <%--<html:submit styleClass="button">${button}</html:submit>--%>
            <app2:securitySubmit operation="UPDATE" functionality="USER"
                                 styleClass="button ${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </html:form>
</div>

