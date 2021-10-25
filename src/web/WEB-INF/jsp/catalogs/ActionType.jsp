<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapSelectPopup/>

<c:set var="helpUrl" value="/catalogs/ActionTypeNumberRuleHelp.jsp"/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="sequenceRuleResetTypes" value="${app2:getSequeceRuleResetTypes(pageContext.request)}"/>

<html:form action="${action}" focus="dto(actionTypeName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <html:hidden property="dto(op)" value="${op}"/>

                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

                <c:if test="${('update' == op) || ('delete' == op)}">
                    <html:hidden property="dto(actionTypeId)"/>
                    <html:hidden property="dto(numberId)"/>
                </c:if>

                <c:if test="${'update' == op}">
                    <html:hidden property="dto(version)"/>
                </c:if>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="actionTypeName_id">
                        <fmt:message key="ActionType.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(actionTypeName)"
                                  styleClass="largetext ${app2:getFormInputClasses()}"
                                  styleId="actionTypeName_id"
                                  maxlength="80"
                                  view="${'delete' == op}"
                                  tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="probability_id">
                        <fmt:message key="ActionType.probability"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <c:set var="probabilities" value="${app2:defaultProbabilities()}"/>
                        <html:select property="dto(probability)"
                                     styleId="probability_id"
                                     styleClass="shortSelect ${app2:getFormInputClasses()}"
                                     readonly="${op == 'delete'}"
                                     tabindex="2">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="probabilities" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="sequence_id">
                        <fmt:message key="Common.probabilitySymbol"/>
                        <fmt:message key="ActionType.sequence"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(sequence)"
                                  styleId="sequence_id"
                                  styleClass="${app2:getFormInputClasses()} numberText"
                                  maxlength="4"
                                  view="${'delete' == op}"
                                  tabindex="3"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="format_id">
                        <fmt:message key="ActionType.format"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <div class="input-group">
                            <app:text property="dto(format)"
                                      styleId="format_id"
                                      styleClass="largeText ${app2:getFormInputClasses()}"
                                      maxlength="149"
                                      view="${'delete' == op}"
                                      tabindex="4"/>

                            <c:if test="${'delete' != op}">
                            <span class="input-group-btn">
                                <tags:bootstrapSelectPopup
                                        styleId="SequenceRuleFormatFieldHelp_id"
                                        url="${helpUrl}"
                                        name="SequenceRuleFormatFieldHelp"
                                        titleKey="SequenceRule.format.formatHelp"
                                        glyphiconClass="glyphicon-question-sign"
                                        tabindex="4"/>
                            </span>
                            </c:if>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="resetType_id">
                        <fmt:message key="ActionType.resetType"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <html:select property="dto(resetType)"
                                     styleId="resetType_id"
                                     styleClass="select ${app2:getFormSelectClasses()}"
                                     readonly="${'delete' == op}"
                                     tabindex="5">
                            <html:option value=""/>
                            <html:options collection="sequenceRuleResetTypes"
                                          property="value"
                                          labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="startNumber_id">
                        <fmt:message key="ActionType.startNumber"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:numberText property="dto(startNumber)"
                                        styleId="startNumber_id"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        maxlength="8"
                                        numberType="integer"
                                        view="${'delete' == op}"
                                        tabindex="6"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <c:if test="${'create' != op}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="lastNumber_id">
                            <fmt:message key="ActionType.lastNumber"/>
                        </label>

                        <div class="${app2:getFormContainClasses(true)}">
                            <app:numberText property="dto(lastNumber)"
                                            styleId="lastNumber_id"
                                            styleClass="numberText ${app2:getFormInputClasses()} text-right"
                                            maxlength="8"
                                            numberType="integer"
                                            view="true"
                                            tabindex="6"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="templateId_id">
                        <fmt:message key="ActionType.templateToSendEmail"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <c:set var="mediatype_HTML" value="<%=CatalogConstants.MediaType.HTML.getConstantAsString()%>"/>
                        <fanta:select property="dto(templateId)"
                                      styleId="templateId_id"
                                      listName="templateList"
                                      module="/catalogs"
                                      labelProperty="description"
                                      valueProperty="id"
                                      firstEmpty="true"
                                      readOnly="${op == 'delete'}"
                                      styleClass="shortSelect ${app2:getFormSelectClasses()}"
                                      tabIndex="6">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="mediaType" value="${mediatype_HTML}"/>
                        </fanta:select>

                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <c:if test="${'update' == op && null != actionTypeForm.dtoMap['lastDate']}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="ActionType.lastDate"/>
                        </label>
                        <html:hidden property="dto(lastDate)"/>
                        <div class="${app2:getFormContainClasses('update' == op || op == 'delete')}">
                            <fmt:formatDate var="dateValue"
                                            value="${app2:intToDate(actionTypeForm.dtoMap['lastDate'])}"
                                            pattern="${datePattern}"/>
                                ${dateValue}
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="ACTIONTYPE"
                                 styleClass="button ${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="ACTIONTYPE"
                                     styleClass="button ${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"><fmt:message
                        key="Common.saveAndNew"/></app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="actionTypeForm"/>