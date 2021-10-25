<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(cityName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(cityId)"/>
        </c:if>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
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
                    <label class="${app2:getFormLabelClasses()}" for="cityName_id">
                        <fmt:message key="City.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(cityName)" styleId="cityName_id"
                                  styleClass="${app2:getFormInputClasses()} largetext"
                                  maxlength="40"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="cityZip_id">
                        <fmt:message key="City.zip"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(cityZip)" styleId="cityZip_id"
                                  styleClass="${app2:getFormInputClasses()} text" maxlength="10"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="countryId_id">
                        <fmt:message key="City.country"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <fanta:select property="dto(countryId)" styleId="countryId_id" listName="countryBasicList"
                                      labelProperty="name"
                                      valueProperty="id" firstEmpty="true" styleClass="${app2:getFormSelectClasses()}"
                                      readOnly="${'delete' == op}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </fanta:select>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="CITY" styleClass="${app2:getFormButtonClasses()}">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="CITY"
                                     styleClass="${app2:getFormButtonClasses()}" property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="cityForm"/>