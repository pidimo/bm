<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>
<%
    List list = JSPHelper.getLanguageList(request);
    request.setAttribute("systemLanguageList", list);
%>

<html:form action="${action}" focus="dto(languageName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <html:hidden property="dto(op)" value="${op}"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                    <%--if update action or delete action--%>
                <c:if test="${('update' == op) || ('delete' == op)}">
                    <html:hidden property="dto(languageId)"/>
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
                    <label class="${app2:getFormLabelClasses()}" for="languageName_id">
                        <fmt:message key="Language.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(languageName)" styleId="languageName_id"
                                  styleClass="${app2:getFormInputClasses()}"
                                  maxlength="20" view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                    <%--Language iso--%>
                <c:set var="key">
                    <fmt:message key="Language.iso"/>
                </c:set>
                <c:if test="${op=='delete'}">
                    <c:set var="visible" value="true"/>
                </c:if>
                <app2:systemLanguage label="${key}"
                                     languageId="${languageForm.dtoMap['languageId']}"
                                     property="dto(languageIso)"
                                     operation="${op}"
                                     containStypeClass="contain"
                                     labelStyleClass="label"
                                     mode="bootstrap"
                                     selectStyleClass="${app2:getFormSelectClasses()}"
                                     visible="${visible}"/>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="isDefault_id">
                        <fmt:message key="Common.default"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(isDefault)"
                                               styleId="isDefault_id"
                                               disabled="${visible}"
                                               tabindex="5"/>
                                <label for="isDefault_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="LANGUAGE"
                                 styleClass="${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="LANGUAGE"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"><fmt:message
                        key="Common.saveAndNew"/></app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="languageForm"/>




