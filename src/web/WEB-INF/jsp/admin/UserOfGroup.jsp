<%@ include file="/Includes.jsp" %>

<html:form action="${action}?userGroupId=${param.userGroupId}" focus="dto(name)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <html:hidden property="dto(op)" value="${op}"/>
            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
            <html:hidden property="dto(userId)" value="${param.userId}"/>
            <html:hidden property="dto(userGroupId)" value="${param.userGroupId}"/>
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="UserGroup.member.userName"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(op == 'delete')}">
                        <app:text property="dto(name)" value="${param.userName}"
                                  styleClass="${app2:getFormInputClasses()} middleText" maxlength="80"
                                  tabindex="1" view="${op == 'delete'}"/>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${op == 'delete'}">
                <html:submit property="dto(delete)" styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.delete"/>
                </html:submit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>