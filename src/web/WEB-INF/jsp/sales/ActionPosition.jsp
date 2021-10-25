<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="actionNetGross"
       value="${app2:getNetGrossFieldFromAction(param['dto(contactId)'],param['dto(processId)'] ,pageContext.request)}"/>

<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>
<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>

<c:set var="useNetPrice" value="${netPrice == actionNetGross}"/>
<c:set var="useGrossPrice" value="${grossPrice == actionNetGross}"/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<html:form action="${action}" focus="dto(productName)" styleClass="form-horizontal">

    <html:hidden property="dto(op)" value="${op}"/>

    <html:hidden property="dto(actionNetGross)" value="${actionNetGross}" styleId="actionNetGrossId"/>

    <c:if test="${'update' == op || op == 'delete'}">
        <html:hidden property="dto(positionId)"/>
        <html:hidden property="dto(version)"/>
    </c:if>

    <html:hidden property="dto(processId)" value="${dto.processId}"/>
    <html:hidden property="dto(contactId)" value="${dto.contactId}"/>
    <html:hidden property="dto(note)" value="${dto.note}"/>
    <html:hidden property="dto(processName)" value="${dto.processName}"/>
    <html:hidden property="dto(addressId)"
                 value="${isSalesProcess ? param.addressId : param.contactId}"/> <%--verificar--%>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

    <html:hidden property="dto(price_old)" styleId="field_price"/>

    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="${op}"
                             functionality="SALESPROCESSPOSITION"
                             property="dto(save)"
                             styleClass="button ${app2:getFormButtonClasses()}"
                             styleId="saveButtonId">
            <c:out value="${button}"/>
        </app2:securitySubmit>
        <c:if test="${op == 'create'}">
            <app2:securitySubmit operation="${op}"
                                 functionality="SALESPROCESSPOSITION"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 property="SaveAndNew">
                <fmt:message key="Common.saveAndNew"/>
            </app2:securitySubmit>
        </c:if>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </div>

    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <c:out value="${title}"/>
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="ActionPosition.product"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                        <div class="input-group">
                            <app:text property="dto(productName)"
                                      styleClass="mediumText ${app2:getFormInputClasses()}"
                                      maxlength="40"
                                      readonly="true"
                                      tabindex="1"
                                      styleId="field_name"
                                      view="${op == 'delete'}"/>
                            <html:hidden property="dto(productId)" styleId="field_key"/>
                            <html:hidden property="dto(versionNumber)" styleId="field_versionNumber"/>
                                <span class="input-group-btn">
                                    <tags:bootstrapSelectPopup
                                            styleId="SearchProduct_id"
                                            url="/products/SearchProduct.do?contactId=${isSalesProcess ? param.addressId : param.contactId}"
                                            name="SearchProduct"
                                            isLargeModal="true"
                                            titleKey="Common.search"
                                            modalTitleKey="Product.Title.SimpleSearch"
                                            submitOnSelect="true"
                                            hide="${op == 'delete'}"
                                            tabindex="1"/>
                                    <tags:clearBootstrapSelectPopup keyFieldId="field_key"
                                                                    nameFieldId="field_name"
                                                                    titleKey="Common.clear"
                                                                    submitOnClear="true"
                                                                    hide="${op == 'delete'}"
                                                                    tabindex="1"/>
                                </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>

                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="ActionPosition.number"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                        <app:numberText property="dto(number)"
                                        styleClass="numberText ${app2:getFormInputClasses()}"
                                        maxlength="10"
                                        numberType="integer"
                                        view="${'delete' == op}"
                                        tabindex="2"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">

                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="ActionPosition.quantity"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                        <app:numberText property="dto(amount)"
                                        styleClass="numberText ${app2:getFormInputClasses()}"
                                        maxlength="18"
                                        view="${'delete' == op}"
                                        numberType="decimal"
                                        maxInt="10"
                                        maxFloat="2"
                                        tabindex="3"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>

                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="ActionPosition.unit"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                        <fanta:select property="dto(unit)"
                                      styleId="field_unitId"
                                      listName="productUnitList"
                                      firstEmpty="true"
                                      labelProperty="name"
                                      valueProperty="id"
                                      module="/catalogs"
                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                      readOnly="${op == 'delete'}" tabIndex="4">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <c:if test="${useNetPrice}">
                        <div class="clearfix">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="ActionPosition.unitPriceNet"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')} wrapperButton">
                                <html:hidden property="dto(unitPriceGross)"/>
                                <app:numberText property="dto(price)"
                                                styleClass="numberText ${app2:getFormInputClasses()}"
                                                maxlength="18"
                                                view="${'delete' == op}"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="4"
                                                tabindex="5"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${useGrossPrice}">
                        <div class="clearfix">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="ActionPosition.unitPriceGross"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                <html:hidden property="dto(price)"/>
                                <app:numberText property="dto(unitPriceGross)"
                                                styleClass="numberText ${app2:getFormInputClasses()} wrapperButton"
                                                maxlength="18"
                                                view="${'delete' == op}"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="4"
                                                tabindex="6"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </c:if>
                    <div class="clearfix">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="ActionPosition.discount"/>
                            (<c:out value="%"/>)
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:numberText property="dto(discount)"
                                            styleClass="numberText ${app2:getFormInputClasses()} wrapperButton"
                                            maxlength="18"
                                            view="${'delete' == op}"
                                            numberType="decimal"
                                            maxInt="10"
                                            maxFloat="2"
                                            tabindex="7"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>

                        </div>
                    </div>
                    <c:if test="${useNetPrice}">
                        <div class="clearfix">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="ActionPosition.totalPriceNet"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <html:hidden property="dto(totalPriceGross)"/>
                                <app:numberText property="dto(totalPrice)"
                                                styleId="totalPrice_name"
                                                styleClass="numberText ${app2:getFormInputClasses()} wrapperButton"
                                                tabindex="8"
                                                numberType="decimal"
                                                maxInt="18"
                                                maxFloat="2"
                                                readonly="true"
                                                view="true"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${useGrossPrice}">
                        <div class="clearfix">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="ActionPosition.totalPriceGross"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <html:hidden property="dto(totalPrice)"/>
                                <app:numberText property="dto(totalPriceGross)"
                                                styleId="totalPrice_name"
                                                styleClass="numberText ${app2:getFormInputClasses()} wrapperButton"
                                                tabindex="9"
                                                numberType="decimal"
                                                maxInt="18"
                                                maxFloat="2"
                                                readonly="true"
                                                view="true"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </c:if>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">

                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="ActionPosition.description"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                        <html:textarea property="dto(description)"
                                       rows="7"
                                       styleClass="tinyDetail ${app2:getFormInputClasses()}"
                                       readonly="${op == 'delete'}"
                                       tabindex="10"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>

                </div>
            </div>

            <div>

            </div>
            <div>

            </div>
        </fieldset>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="${op}"
                             tabindex="11"
                             functionality="SALESPROCESSPOSITION"
                             property="dto(save)"
                             styleClass="button ${app2:getFormButtonClasses()}"
                             styleId="saveButtonId">
            <c:out value="${button}"/>
        </app2:securitySubmit>
        <c:if test="${op == 'create'}">
            <app2:securitySubmit operation="${op}"
                                 functionality="SALESPROCESSPOSITION"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 property="SaveAndNew"
                                 tabindex="12">
                <fmt:message key="Common.saveAndNew"/>
            </app2:securitySubmit>
        </c:if>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="13">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </div>
</html:form>
<tags:jQueryValidation formName="actionPositionForm"/>
