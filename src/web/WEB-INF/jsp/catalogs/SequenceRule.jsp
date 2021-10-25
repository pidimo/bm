<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>

<script language="JavaScript">
    function onSubmit() {
        document.getElementById('sequenceRuleFormId').submit();
    }

    function hideSequenceRuleFormPanel() {
        document.getElementById('formPanel').style.visibility = "hidden";
        document.getElementById('formPanel').style.position = "absolute";
    }
</script>

<fmt:message var="datePattern" key="datePattern"/>
<c:set var="sequenceRuleTypeList" value="${app2:getSequenceRuleTypes(pageContext.request)}"/>
<c:set var="sequenceRuleResetTypes" value="${app2:getSequeceRuleResetTypes(pageContext.request)}"/>
<c:set var="voucherType" value="<%=FinanceConstants.SequenceRuleType.VOUCHER.getConstantAsString()%>"/>
<c:set var="customerType" value="<%=FinanceConstants.SequenceRuleType.CUSTOMER.getConstantAsString()%>"/>
<c:set var="articleType" value="<%=FinanceConstants.SequenceRuleType.ARTICLE.getConstantAsString()%>"/>
<c:set var="supportCaseType" value="<%=FinanceConstants.SequenceRuleType.SUPPORT_CASE.getConstantAsString()%>"/>
<c:set var="contractNumber"
       value="<%=FinanceConstants.SequenceRuleType.PRODUCT_CONTRACT_NUMBER.getConstantAsString()%>"/>

<c:set var="helpUrl" value="/catalogs/SequenceRuleFormatField.jsp"/>
<c:if test="${customerType == sequenceRuleForm.dtoMap['type'] || articleType == sequenceRuleForm.dtoMap['type'] || supportCaseType == sequenceRuleForm.dtoMap['type']}">
    <c:set var="helpUrl" value="/catalogs/CustomerSequenceRuleHelp.jsp"/>
</c:if>
<c:if test="${contractNumber == sequenceRuleForm.dtoMap['type']}">
    <c:set var="helpUrl" value="/catalogs/ProductContractNumberRuleHelp.jsp"/>
</c:if>

<html:form action="${action}" focus="dto(label)" styleId="sequenceRuleFormId" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(numberId)"/>
        </c:if>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
            <html:hidden property="dto(confirmatioMsg)" value="${sequenceRuleForm.dtoMap['confirmatioMsg']}"/>
        </c:if>
        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="label_id">
                        <fmt:message key="SequenceRule.label"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(label)" styleId="label_id"
                                  styleClass="${app2:getFormInputClasses()} largeText"
                                  maxlength="149" view="${'delete' == op}" tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="type_id">
                        <fmt:message key="SequenceRule.type"/>
                    </label>

                    <div class="${app2:getFormContainClasses('create' != op)}">
                        <html:select property="dto(type)" styleId="type_id" styleClass="${app2:getFormSelectClasses()}"
                                     readonly="${'create' != op}" onchange="onSubmit();" tabindex="2">
                            <html:option value=""/>
                            <html:options collection="sequenceRuleTypeList" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="format_id">
                        <fmt:message key="SequenceRule.format"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <div class="input-group">
                            <app:text property="dto(format)" styleId="format_id"
                                      styleClass="${app2:getFormInputClasses()} largeText"
                                      maxlength="149" view="${'delete' == op}" tabindex="2"/>
                            <c:if test="${'delete' != op}">
                            <span class="input-group-btn">
                              <tags:bootstrapSelectPopup styleId="SequenceRuleFormatFieldHelp_id"
                                                         styleClass="${app2:getFormButtonClasses()}" url="${helpUrl}"
                                                         name="SequenceRuleFormatFieldHelp"
                                                         titleKey="SequenceRule.format.formatHelp"
                                                         glyphiconClass="glyphicon-question-sign"
                                                         tabindex="2"/>
                            </span>
                            </c:if>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <c:if test="${voucherType == sequenceRuleForm.dtoMap['type'] || contractNumber == sequenceRuleForm.dtoMap['type']}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="resetType_id">
                            <fmt:message key="SequenceRule.resetType"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <html:select property="dto(resetType)"
                                         styleId="resetType_id"
                                         styleClass="${app2:getFormSelectClasses()}"
                                         readonly="${'delete' == op}"
                                         tabindex="3">
                                <html:option value=""/>
                                <html:options collection="sequenceRuleResetTypes" property="value"
                                              labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="startNumber_id">
                            <fmt:message key="SequenceRule.startNumber"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <app:numberText property="dto(startNumber)"
                                            styleId="startNumber_id"
                                            styleClass="${app2:getFormInputClasses()} numberText"
                                            maxlength="8"
                                            numberType="integer"
                                            view="${'delete' == op}"
                                            tabindex="4"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
                <c:if test="${'create' != op}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="lastNumber_id">
                            <fmt:message key="SequenceRule.lastNumber"/>
                        </label>

                        <div class="${app2:getFormContainClasses('create' != op)}">
                            <app:numberText property="dto(lastNumber)"
                                            styleId="lastNumber_id"
                                            styleClass="${app2:getFormInputClasses()} numberText"
                                            maxlength="8"
                                            numberType="integer"
                                            view="true"
                                            tabindex="5"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
                <c:if test="${'update' == op && null != sequenceRuleForm.dtoMap['lastDate']}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="SequenceRule.lastDate"/>
                        </label>

                        <div class="${app2:getFormContainClasses(true)}">
                            <fmt:formatDate var="dateValue"
                                            value="${app2:intToDate(sequenceRuleForm.dtoMap['lastDate'])}"
                                            pattern="${datePattern}"/>
                            <html:hidden property="dto(lastDate)"/>
                                ${dateValue}
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
                    <%--debitor info only to voucher type--%>
                <c:if test="${voucherType == sequenceRuleForm.dtoMap['type'] and app2:hasAssignedLexwareModule(pageContext.request)}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="fieldAddressName_id">
                            <fmt:message key="SequenceRule.debitor"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <div class="input-group">
                                <app:text property="dto(debitorName)"
                                          styleClass="${app2:getFormInputClasses()} middleText" maxlength="40"
                                          readonly="true"
                                          styleId="fieldAddressName_id"
                                          view="${'delete' == op}"
                                          tabindex="6"/>
                                <html:hidden property="dto(debitorId)" styleId="fieldAddressId_id"/>
                                <span class="input-group-btn">
                                   <tags:bootstrapSelectPopup styleId="searchAddress_id"
                                                              styleClass="${app2:getFormButtonClasses()}"
                                                              url="/contacts/SearchAddress.do"
                                                              name="searchAddress"
                                                              titleKey="Contact.Title.search"
                                                              tabindex="7"
                                                              hide="${'delete' == op}"/>
                                   <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                   styleClass="${app2:getFormButtonClasses()}"
                                                                   nameFieldId="fieldAddressName_id"
                                                                   titleKey="Common.clear"
                                                                   tabindex="8"
                                                                   hide="${'delete' == op}"/>
                                </span>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="debitorNumber_id">
                            <fmt:message key="SequenceRule.debitorNumber"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <app:text property="dto(debitorNumber)"
                                      styleId="debitorNumber_id"
                                      styleClass="${app2:getFormInputClasses()} middleText"
                                      maxlength="20"
                                      view="${'delete' == op}"
                                      tabindex="9"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit property="save"
                                 operation="${op}"
                                 functionality="SEQUENCERULE"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="12">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="SEQUENCERULE"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"
                                     tabindex="13">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="14">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
    <div id="numberAvailableToFormatPanel">
        <c:if test="${numberAvailableToFormatMsg eq 'true'}">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <app2:securitySubmit property="dto(deleteAllNumbers)" operation="${op}"
                                     functionality="SEQUENCERULE" styleClass="${app2:getFormButtonClasses()}"
                                     tabindex="15">
                    <fmt:message key="SequenceRule.button.deleteAllfreeNumber"/>
                </app2:securitySubmit>
                <app2:securitySubmit property="dto(updateNumberFormat)" operation="${op}"
                                     functionality="SEQUENCERULE" styleClass="${app2:getFormButtonClasses()}"
                                     tabindex="16">
                    <fmt:message key="SequenceRule.button.updatefreeNumber"/>
                </app2:securitySubmit>
                <html:cancel styleClass="${app2:getFormButtonClasses()}" tabindex="17">
                    <fmt:message key="SequenceRule.button.cancelChangeFormat"/>
                </html:cancel>
                <html:hidden property="dto(confirmChangeFormat)" value="true" styleId="confirmChangeFormatId"/>
            </div>
        </c:if>
    </div>
</html:form>
<tags:jQueryValidation formName="sequenceRuleForm"/>

