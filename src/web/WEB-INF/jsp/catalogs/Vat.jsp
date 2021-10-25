<%@ include file="/Includes.jsp" %>


<html:form action="${action}" focus="dto(vatLabel)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <c:set var="vatId" value="${vatForm.dtoMap['vatId']}"/>
                <c:set var="vatLabel" value="${vatForm.dtoMap['vatLabel']}"/>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div id="vat.jsp">
                    <html:hidden property="dto(op)" value="${op}"/>
                    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                        <%--if update action or delete action--%>
                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(vatId)"/>
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
                        <label class="${app2:getFormLabelClasses()}" for="vatLabel_id">
                            <fmt:message key="Vat.name"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <app:text property="dto(vatLabel)"
                                      styleId="vatLabel_id"
                                      styleClass="largetext
                                  ${app2:getFormInputClasses()}"
                                      maxlength="40"
                                      view="${'delete' == op}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="vatDescription_id">
                            <fmt:message key="Vat.description"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <app:text property="dto(vatDescription)"
                                      styleId="vatDescription_id"
                                      styleClass="largetext  ${app2:getFormInputClasses()}"
                                      maxlength="80"
                                      view="${'delete' == op}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="taxKey_id">
                            <fmt:message key="Vat.taxKey"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <app:numberText property="dto(taxKey)"
                                            styleId="taxKey_id"
                                            styleClass="numberText ${app2:getFormInputClasses()}"
                                            maxlength="8"
                                            numberType="integer"
                                            view="${'delete' == op}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="VAT"
                                 styleClass="button ${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="VAT"
                                     styleClass="button ${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"><fmt:message key="Common.saveAndNew"/></app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>
<app2:checkAccessRight functionality="VATRATE" permission="VIEW">
    <c:if test="${op=='update'}">
        <div class="embed-responsive embed-responsive-16by9 col-xs-12">
            <iframe class="embed-responsive-item" name="frame1"
                    src="<app:url value="VatRate/List.do?parameter(vatRateVatId)=${vatId}&dto(vatId)=${vatId}&dto(vatLabel)=${app2:encode(vatLabel)}" />"
                    scrolling="no" frameborder="0" id="iFrameId">
            </iframe>
        </div>
    </c:if>
</app2:checkAccessRight>

<tags:jQueryValidation formName="vatForm"/>



