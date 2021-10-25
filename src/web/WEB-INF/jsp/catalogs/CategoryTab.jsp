<%@ include file="/Includes.jsp" %>

<c:set var="categoryAllTypeList" value="${app2:getCategoryTableTypes(pageContext.request)}"/>

<html:form action="${action}" focus="dto(label)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <c:if test="${'update' == op || 'delete' == op}">
            <html:hidden property="dto(categoryTabId)"/>
        </c:if>

        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
            <html:hidden property="dto(canUpdateLabel)"/>
        </c:if>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                        ${title}
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="label_id">
                        <fmt:message key="CategoryTab.label"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(label)" styleId="label_id"
                                  styleClass="${app2:getFormInputClasses()} text" maxlength="20"
                                  view="${'delete' == op}" tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="sequence_id">
                        <fmt:message key="CategoryTab.sequence"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:numberText property="dto(sequence)" styleId="sequence_id"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        numberType="integer" maxlength="3"
                                        tabindex="2" view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="table_id">
                        <fmt:message key="CategoryTab.table"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op || false == categoryTabForm.dtoMap.canUpdateLabel)}">
                        <html:select property="dto(table)"
                                     styleId="table_id"
                                     styleClass="${app2:getFormSelectClasses()}"
                                     readonly="${'delete' == op || false == categoryTabForm.dtoMap.canUpdateLabel}"
                                     tabindex="3">
                            <html:option value=""/>
                            <html:options collection="categoryAllTypeList" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="CATEGORYTAB"
                                 styleClass="${app2:getFormButtonClasses()}">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <c:if test="${'create' == op}">
                <app2:securitySubmit operation="CREATE"
                                     functionality="CATEGORYTAB"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="categoryTabForm"/>