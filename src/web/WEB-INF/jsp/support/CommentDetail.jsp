<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(detail)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">

        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(actionHistory)" value="${actionHistory}"/>
        <fmt:message var="dateTimePattern" key="dateTimePattern"/>
        <html:hidden property="dto(commentId)"/>
        <html:hidden property="dto(withReferences)" value="true"/>
        <html:hidden property="dto(detail)"/>
        <div class="${app2:getFormPanelClasses()}">
            <legend class="title">
                <c:out value="${title}"/>
            </legend>

            <div>
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Link.publishBy"/>
                </label>
                <div class="${app2:getFormContainClasses(true)}">
                    <app:text property="dto(ownerName)" styleClass="largetext" maxlength="20" view="${true}"/>
                </div>
            </div>
            <div>
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Link.publishDate"/>
                </label>
                <div class="${app2:getFormContainClasses(true)}">
                        ${app2:getDateWithTimeZone(commentForm.dtoMap.createDateTime, timeZone, dateTimePattern)}
                </div>
            </div>
            <div>
                <html:textarea property="dto(detail)" styleClass="${app2:getFormInputClasses()}" disabled="true" style="height:80px;width:100%;"/>
            </div>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="delete" functionality="ARTICLECOMMENT" styleClass="button ${app2:getFormButtonClasses()}" tabindex="10">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="12"><fmt:message key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>