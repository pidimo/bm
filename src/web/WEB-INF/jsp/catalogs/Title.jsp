<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(titleName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div id="Title.jsp">
                    <html:hidden property="dto(op)" value="${op}"/>
                        <%--if update action or delete action--%>
                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(titleId)"/>
                    </c:if>
                        <%--for the version control if update action--%>
                    <c:if test="${'update' == op}">
                        <html:hidden property="dto(version)"/>
                    </c:if>
                        <%--for the control referencial integrity if delete action--%>
                    <c:if test="${'delete' == op}">
                        <html:hidden property="dto(withReferences)" value="true"/>
                    </c:if>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="titleName_id">
                            <fmt:message key="Title.name"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <app:text property="dto(titleName)" styleId="titleName_id"
                                      styleClass="largetext form-control"
                                      maxlength="30"
                                      view="${'delete' == op}"/>
                            <c:out value="${sessionScope.listshadow}" escapeXml="false"/><img
                                src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5">
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="TITLE"
                                 styleClass="button ${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="TITLE"
                                     styleClass="button ${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"><fmt:message
                        key="Common.saveAndNew"/></app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="titleForm"/>



