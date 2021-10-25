<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>
<tags:initSelectPopupEven fields="user_key, user_name"/>

<%
    pageContext.setAttribute("activeList", JSPHelper.getActiveOrNoList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>


<script>
    function goSubmit(obj) {
        if (obj.options[obj.selectedIndex].value != "") {
            document.forms[0].submit();
        }
    }
</script>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="/Report/ActionPositionList/Execute.do" focus="parameter(contactId)" styleId="processStyle"
               styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="SalesProcess.Report.ActionPositionList"/>
                </legend>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="user_name">
                            <fmt:message key="Document.salesAsociated"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
                            <html:hidden property="parameter(2)" styleId="field_price"/>
                            <html:hidden property="parameter(3)" styleId="field_unitId"/>
                            <html:hidden property="parameter(processId)" styleId="user_key"/>
                            <html:hidden property="parameter(processName)" styleId="user_name"/>
                            <div class="input-group">
                                <app:text property="parameter(processName)"
                                          styleClass="${app2:getFormInputClasses()} middleText"
                                          maxlength="30"
                                          styleId="user_name"
                                          tabindex="1" disabled="true"/>
                                <span class="input-group-btn">
                                    <tags:bootstrapSelectPopup url="/sales/SalesProcess/SimpleSearchProcess.do"
                                                               styleId="searchSalesProcess_id"
                                                               name="searchSalesProcess"
                                                               titleKey="Common.search"
                                                               modalTitleKey="SalesProcess.Title.simpleSearch"
                                                               tabindex="2"
                                                               submitOnSelect="true"
                                                               isLargeModal="true"/>
                                    <tags:clearBootstrapSelectPopup keyFieldId="user_key"
                                                                    styleClass="${app2:getFormButtonClasses()}"
                                                                    nameFieldId="user_name"
                                                                    titleKey="Common.clear"
                                                                    submitOnClear="true"
                                                                    tabindex="3"
                                                                    glyphiconClass="glyphicon-erase"/>
                                </span>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Product.price"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="price1_id">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(price1)"
                                                    styleId="price1_id"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="4"
                                                    maxlength="12"
                                                    numberType="decimal" maxInt="10" maxFloat="2"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="price2">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(price2)" styleId="price2"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="5"
                                                    maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="contactId_id">
                            <fmt:message key="Common.action"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(contactId)" styleId="contactId_id" listName="actionList"
                                          firstEmpty="true"
                                          labelProperty="note"
                                          valueProperty="contactId"
                                          module="/sales" styleClass="${app2:getFormSelectClasses()} middleSelect"
                                          tabIndex="6">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="processId"
                                                 value="${not empty actionPositionReportForm.params.processId?actionPositionReportForm.params.processId:0}"/>
                            </fanta:select>
                        </div>
                    </div>
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="ActionPosition.quantity"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="amount1_id">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(amount1)"
                                                    styleId="amount1_id"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="7"
                                                    maxlength="12"
                                                    numberType="integer"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="amount2">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(amount2)" styleId="amount2"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="8"
                                                    maxlength="12" numberType="integer"/>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_name">
                            <fmt:message key="Product.title"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:hidden property="parameter(productId)" styleId="field_key"/>
                            <div class="input-group">
                                <app:text property="parameter(productName)"
                                          styleClass="${app2:getFormInputClasses()} middleText" tabindex="9"
                                          readonly="true"
                                          styleId="field_name"/>
                                <span class="input-group-btn">
                                    <tags:bootstrapSelectPopup styleId="SearchProduct_id"
                                                               isLargeModal="true"
                                                               url="/products/SearchProduct.do"
                                                               name="SearchProduct"
                                                               tabindex="10"
                                                               titleKey="Common.search"
                                                               modalTitleKey="Product.Title.SimpleSearch"/>
                                    <tags:clearBootstrapSelectPopup keyFieldId="field_key"
                                                                    styleClass="${app2:getFormButtonClasses()}"
                                                                    nameFieldId="field_name"
                                                                    tabindex="11"
                                                                    titleKey="Common.clear"
                                                                    glyphiconClass="glyphicon-erase"/>
                                </span>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="ActionPosition.totalPrice"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="totalPrice1_id">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(totalPrice1)"
                                                    styleId="totalPrice1_id"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="12"
                                                    maxlength="12"
                                                    numberType="decimal" maxInt="10" maxFloat="2"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="totalPrice2">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(totalPrice2)" styleId="totalPrice2"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="13"
                                                    maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>

            <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                      tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="processName" labelKey="SalesProcess.name" isDefault="true"
                                                defaultOrder="true" isDefaultGrouping="true"/>
                <titus:reportGroupSortColumnTag name="note" labelKey="SalesProcessAction.title"/>
                <titus:reportGroupSortColumnTag name="productName" labelKey="ActionPosition.product"/>
                <titus:reportGroupSortColumnTag name="unit" labelKey="ActionPosition.unit"/>
                <titus:reportGroupSortColumnTag name="amount" labelKey="ActionPosition.quantity"/>
                <titus:reportGroupSortColumnTag name="price" labelKey="Competitor.price"/>
                <titus:reportGroupSortColumnTag name="totalPrice" labelKey="ActionPosition.totalPrice"/>
            </titus:reportGroupSortTag>

            <c:set var="reportFormats" value="${actionPositionReportForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${actionPositionReportForm.pageSizes}" scope="request"/>

            <tags:bootstrapReportOptionsTag/>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
                <fmt:message key="Campaign.Report.generate"/>
            </html:submit>
            <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                         onclick="formReset('processStyle')">
                <fmt:message key="Common.clear"/>
            </html:button>
        </div>

        <c:set var="simple"><fmt:message key="contact.payment.payMethod.single"/></c:set>
        <c:set var="partial"><fmt:message key="contact.payment.payMethod.partial"/></c:set>
        <c:set var="periodic"><fmt:message key="contact.payment.payMethod.periodic"/></c:set>


        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="actionPositionReportList" title="SalesProcess.Report.ActionPositionList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
        <titus:reportFieldTag name="processName" resourceKey="SalesProcess.processName" type="${FIELD_TYPE_STRING}"
                              width="20"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="note" resourceKey="SalesProcessAction.title" type="${FIELD_TYPE_STRING}"
                              width="20"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="productName" resourceKey="ActionPosition.product" type="${FIELD_TYPE_STRING}"
                              width="20"
                              fieldPosition="3"/>
        <%--<titus:reportFieldTag name="productVersion" resourceKey="ActionPosition.version" type="${FIELD_TYPE_STRING}" width="10"
                              fieldPosition="4"/>--%>
        <titus:reportFieldTag name="unit" resourceKey="ActionPosition.unit" type="${FIELD_TYPE_STRING}" width="9"
                              fieldPosition="5"/>
        <titus:reportFieldTag name="amount" resourceKey="ActionPosition.quantity" type="${FIELD_TYPE_DECIMALNUMBER}"
                              patternKey="numberFormat.2DecimalPlaces"
                              align="${FIELD_ALIGN_RIGHT}"
                              width="7" fieldPosition="6"/>
        <titus:reportFieldTag name="price" resourceKey="Competitor.price" type="${FIELD_TYPE_DECIMALNUMBER}"
                              patternKey="numberFormat.4DecimalPlaces" align="${FIELD_ALIGN_RIGHT}" width="7"
                              fieldPosition="7"/>
        <titus:reportFieldTag name="totalPrice" resourceKey="ActionPosition.totalPrice"
                              type="${FIELD_TYPE_DECIMALNUMBER}"
                              patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}" width="7"
                              fieldPosition="8"/>

    </html:form>
</div>

<tags:jQueryValidation formName="actionPositionReportForm"/>