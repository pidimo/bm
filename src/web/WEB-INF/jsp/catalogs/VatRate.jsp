<%@ include file="/Includes.jsp" %>
<%
    String vatRateVatId = request.getParameter("dto(vatId)");
    String vatLabel = request.getParameter("dto(vatLabel)");
    request.setAttribute("vatId", vatRateVatId);
    request.setAttribute("vatLabel", vatLabel);
%>
<tags:initBootstrapDatepicker />
<html:form action="${action}" focus="dto(validFrom)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <c:if test="${null != vatId}">
                    <html:hidden property="dto(vatId)" value="${vatId}"/>
                </c:if>
                <c:if test="${null == vatId}">
                    <c:set var="vatId" value="vatRateForm.dtoMap['vatId']"/>
                </c:if>
                <div id="CategoryValue.jsp">
                    <html:hidden property="dto(locale)" value="${sessionScope.user.valueMap['locale']}"/>
                    <html:hidden property="dto(op)" value="${op}"/>
                    <html:hidden property="dto(vatLabel)" value="${vatRateForm.dtoMap['vatLabel']}"/>
                    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                    <c:if test="${'update' == op}">
                        <html:hidden property="dto(vatrateId)"/>
                        <html:hidden property="dto(version)"/>
                    </c:if>
                    <c:if test="${'delete' == op}">
                        <html:hidden property="dto(vatrateId)"/>
                        <html:hidden property="dto(withReferences)" value="true"/>
                    </c:if>
                    <div class="${app2:getFormGroupClasses()}">
                        <label for="" class="${app2:getFormLabelClasses()}">
                            <fmt:message key="VatRate.vat"/>
                        </label>
                        <div class="${app2:getFormContainClasses(true)}">
                                ${vatLabel}
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="validFromId">
                            <fmt:message key="VatRate.validFrom"/>
                            <fmt:message var="datePattern" key="datePattern"/>
                        </label>

                        <div class="${app2:getFormContainClasses(op == 'delete')}">
                            <html:hidden property="dto(vatId)" value="${vatId}"/>
                            <html:hidden property="dto(datePattern)" value="${datePattern}"/>
                            <div class="input-group date">
                                <app:dateText property="dto(validFrom)"
                                              styleId="validFromId"
                                              mode="bootstrap"
                                              calendarPicker="${op != 'delete'}" datePatternKey="datePattern"
                                              styleClass="${app2:getFormInputClasses()} datepicker"
                                              view="${op == 'delete'}" maxlength="10"
                                              convert="true"
                                              currentDate="false"/>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="vatRate_id">
                            <fmt:message key="VatRate.vatRate"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <app:numberText property="dto(vatRate)" styleId="vatRate_id"
                                            styleClass="numberText ${app2:getFormInputClasses()}"
                                            maxlength="8" view="${'delete' == op}" numberType="decimal" maxInt="3"
                                            maxFloat="2"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="VATRATE"
                                 styleClass="button ${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="VATRATE"
                                     styleClass="button ${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"><fmt:message
                        key="Common.saveAndNew"/></app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="vatRateForm"/>
