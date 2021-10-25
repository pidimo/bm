<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>

<%
    List telecomTypeList = JSPHelper.getTelecomTypeTypes(request);
    request.setAttribute("systemTelecomType", telecomTypeList);
%>

<html:form action="${action}" focus="dto(telecomTypeName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <html:hidden property="dto(actualType)" value="${telecomTypeForm.dtoMap['type']}"/>
                <html:hidden property="dto(op)" value="${op}"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

                <c:if test="${('update' == op) || ('delete' == op)}">
                    <html:hidden property="dto(telecomTypeId)"/>

                    <c:set var="haveFirstTranslation" value="${telecomTypeForm.dtoMap['haveFirstTranslation']}"/>
                    <html:hidden property="dto(haveFirstTranslation)" value="${haveFirstTranslation}"/>
                    <html:hidden property="dto(langTextId)"/>
                </c:if>

                <c:if test="${'update' == op}">
                    <html:hidden property="dto(version)"/>
                </c:if>

                <c:if test="${'delete' == op}">
                    <html:hidden property="dto(withReferences)" value="true"/>
                </c:if>

                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="telecomTypeName_id">
                        <fmt:message key="TelecomType.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(telecomTypeName)" styleId="telecomTypeName_id"
                                  styleClass="largetext ${app2:getFormInputClasses()}" maxlength="20"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="position_id">
                        <fmt:message key="TelecomType.position"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(position)" styleId="position_id"
                                  styleClass="${app2:getFormInputClasses()} numberTextLarge"
                                  maxlength="4"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="type_id">
                        <fmt:message key="TelecomType.type"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete' || op == 'update')}">
                        <c:if test="${op == 'create' || op == 'update'}">
                            <html:select property="dto(type)" styleId="type_id"
                                         styleClass="select ${app2:getFormSelectClasses()}">
                                <html:option value=""/>
                                <html:options collection="systemTelecomType" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </c:if>

                        <c:if test="${op == 'delete'}">
                            <c:set var="telecomTypeConstant" value="${telecomTypeForm.dtoMap['type']}" scope="request"/>
                            <%
                                String constant = (String) request.getAttribute("telecomTypeConstant");
                                String key = JSPHelper.getTelecomType(constant, request);
                                request.setAttribute("constantKey", key);
                            %>
                            ${constantKey}
                        </c:if>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="languageId_id">
                        <fmt:message key="TelecomType.language"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete'|| true == haveFirstTranslation)}">
                        <fanta:select property="dto(languageId)" listName="systemLanguageList" labelProperty="name"
                                      valueProperty="id"
                                      firstEmpty="true" styleClass="select ${app2:getFormSelectClasses()}"
                                      styleId="languageId_id"
                                      readOnly="${op == 'delete'|| true == haveFirstTranslation}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="TELECOMTYPE"
                                 styleClass="button ${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="TELECOMTYPE"
                                     styleClass="button ${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"><fmt:message
                        key="Common.saveAndNew"/></app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="telecomTypeForm"/>



