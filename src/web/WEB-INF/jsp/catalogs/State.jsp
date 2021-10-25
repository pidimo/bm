<%@ page import="com.piramide.elwis.utils.SupportConstants" %>
<%@ page import="com.piramide.elwis.web.admin.session.User" %>
<%@ page import="com.piramide.elwis.web.common.validator.DataBaseValidator" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ include file="/Includes.jsp" %>

<c:if test="${'create' != op}">
    <c:set var="isOpen" value="${stateForm.dtoMap.stageType == 0}"/>
</c:if>


<%
    Map conditions = new HashMap();
    conditions.put("companyid", ((User) request.getSession().getAttribute("user")).getValue("companyId"));
    conditions.put("type", Integer.toString(SupportConstants.SUPPORT_TYPE_STATE));
    boolean hasOpenState = DataBaseValidator.i.isDuplicate(SupportConstants.TABLE_STATE, "stagetype", Integer.toString(SupportConstants.OPEN_STATE),
            null, null, conditions, false);
    Boolean isOpen = (Boolean) pageContext.getAttribute("isOpen");
    if (isOpen != null) {
        hasOpenState = hasOpenState && !isOpen.booleanValue();
    }
    pageContext.setAttribute("stages", JSPHelper.getStageTypesState(request, hasOpenState));
%>

<html:form action="${action}" focus="dto(stateName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <c:if test="${('update' == op) || ('delete' == op)}">
                    <c:set var="haveFirstTranslation" value="${stateForm.dtoMap['haveFirstTranslation']}"/>
                    <html:hidden property="dto(haveFirstTranslation)" value="${haveFirstTranslation}"/>
                    <html:hidden property="dto(langTextId)"/>
                </c:if>
                <html:hidden property="dto(op)" value="${op}"/>
                <c:if test="${type != null}">
                    <html:hidden property="dto(stateType)" value="${type}"/>
                </c:if>
                <c:if test="${('update' == op) || ('delete' == op)}">
                    <html:hidden property="dto(stateId)"/>
                </c:if>
                <c:if test="${'update' == op}">
                    <html:hidden property="dto(version)"/>
                    <html:hidden property="dto(oldStageType)"/>
                </c:if>
                <c:if test="${'delete' == op}">
                    <html:hidden property="dto(withReferences)" value="true"/>
                </c:if>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="stateName_id">
                        <fmt:message key="State.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(stateName)" styleId="stateName_id"
                                  styleClass="largetext ${app2:getFormInputClasses()}"
                                  maxlength="40" tabindex="1"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="sequence_id">
                        <fmt:message key="State.sequence"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(sequence)" styleId="sequence_id"
                                  styleClass="${app2:getFormInputClasses()} numberText"
                                  tabindex="2"
                                  maxlength="6" view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="stageType_id">
                        <fmt:message key="State.stageType"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <html:select property="dto(stageType)" styleId="stageType_id"
                                     styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                     readonly="${op == 'delete'}" tabindex="3">
                            <html:option value=""/>
                            <html:options collection="stages" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="languageId_id">
                        <fmt:message key="State.language"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete'|| true == haveFirstTranslation)}">
                        <fanta:select property="dto(languageId)" listName="systemLanguageList"
                                      labelProperty="name" valueProperty="id"
                                      firstEmpty="true"
                                      styleClass="${app2:getFormSelectClasses()}"
                                      styleId="languageId_id"
                                      readOnly="${op == 'delete'|| true == haveFirstTranslation}"
                                      tabIndex="4">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="STATE"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 tabindex="5" property="dto(save)">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="STATE"
                                     styleClass="button ${app2:getFormButtonClasses()}"
                                     tabindex="6" property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="7"><fmt:message
                    key="Common.cancel"/>
            </html:cancel>
            <input type="hidden" name="cancel" value="cancel"/>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="stateForm"/>


