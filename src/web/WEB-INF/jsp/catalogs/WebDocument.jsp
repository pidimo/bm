<%@ page import="com.piramide.elwis.cmd.utils.VariableConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="CONTACT_TYPE" value="<%=VariableConstants.VariableType.CONTACT.getConstant()%>"/>
<c:set var="COMPANY_TYPE" value="<%=VariableConstants.VariableType.COMPANY.getConstant()%>"/>
<c:set var="EMPLOYEE_TYPE" value="<%=VariableConstants.VariableType.EMPLOYEE.getConstant()%>"/>


<html:form action="${action}" focus="dto(name)" styleClass="form-horizontal">
    <html:hidden property="dto(op)" value="${op}"/>

    <c:if test="${'update'== op || 'delete'== op}">
        <html:hidden property="dto(webDocumentId)"/>
    </c:if>

    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>

    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="WEBDOCUMENT" property="save"
                                 styleClass="button ${app2:getFormButtonClasses()}" tabindex="13">
                ${button}
            </app2:securitySubmit>

            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="15">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <legend class="title">
                <c:out value="${title}"/>
            </legend>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}" for="name_id">
                    <fmt:message key="WebDocument.name"/>
                </label>

                <div class="${app2:getFormContainClasses('delete' == op)}">
                    <app:text property="dto(name)"
                              styleId="name_id"
                              styleClass="largetext ${app2:getFormInputClasses()}"
                              maxlength="100" view="${'delete' == op}"
                              tabindex="1"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}" for="url_id">
                    <fmt:message key="WebDocument.url"/>
                </label>

                <div class="${app2:getFormContainClasses('delete' == op)}">
                    <app:text property="dto(url)"
                              styleId="url_id"
                              styleClass="largetext ${app2:getFormInputClasses()}"
                              maxlength="250" view="${'delete' == op}" tabindex="2"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <legend class="title">
                <fmt:message key="WebDocument.contactParameters"/>
            </legend>
            <tags:webParametersBootstrap formObject="${webDocumentForm}" variableTypeConstant="${CONTACT_TYPE}"
                                         tabIndex="3"/>
            <legend class="title">
                <fmt:message key="WebDocument.companyParameters"/>
            </legend>
            <tags:webParametersBootstrap formObject="${webDocumentForm}" variableTypeConstant="${COMPANY_TYPE}"
                                         tabIndex="4"/>
            <legend class="title">
                <fmt:message key="WebDocument.employeeParameters"/>
            </legend>
            <tags:webParametersBootstrap formObject="${webDocumentForm}" variableTypeConstant="${EMPLOYEE_TYPE}"
                                         tabIndex="5"/>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="WEBDOCUMENT" property="save"
                                 styleClass="button ${app2:getFormButtonClasses()}" tabindex="10">
                ${button}
            </app2:securitySubmit>

            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="12">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="webDocumentForm"/>