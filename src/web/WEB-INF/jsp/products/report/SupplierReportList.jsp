<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<%
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<script>
    function goSubmit() {
        document.forms[0].submit();
    }
</script>

<div class="${app2:getFormWrapperTwoColumns()}">

    <html:form action="/Report/SupplierList/Execute.do"
               focus="parameter(active)"
               styleId="productStyle"
               styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Product.Report.SupplierList"/>
                </legend>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_name">
                            <fmt:message key="Product.title"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(false)}">
                            <div class="input-group">
                                <app:text property="parameter(productName)"
                                          styleId="field_name"
                                          styleClass="mediumText ${app2:getFormInputClasses()}"
                                          maxlength="40"
                                          readonly="true"/>

                                <html:hidden property="parameter(productId)" styleId="field_key"/>
                                <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
                                <html:hidden property="parameter(2)" styleId="field_unitId"/>
                                <html:hidden property="parameter(3)" styleId="field_price"/>

                                <span class="input-group-btn">
                                    <tags:bootstrapSelectPopup url="/products/SearchProduct.do"
                                                               name="SearchProduct"
                                                               isLargeModal="true"
                                                               styleId="productSelectPopup_id"
                                                               styleClass="${app2:getFormButtonClasses()}"
                                                               modalTitleKey="Product.Title.SimpleSearch"
                                                               titleKey="Common.search"/>

                                    <tags:clearBootstrapSelectPopup keyFieldId="field_key"
                                                                    nameFieldId="field_name"
                                                                    styleClass="${app2:getFormButtonClasses()}"
                                                                    titleKey="Common.clear"/>

                                </span>
                            </div>
                        </div>
                    </div>

                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="discount1_id">
                            <fmt:message key="SupplierProduct.discount"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(false)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.from"/>
                                    </label>

                                    <app:numberText property="parameter(discount1)"
                                                    styleClass="numberText ${app2:getFormInputClasses()} numberInputWidth"
                                                    tabindex="1"
                                                    maxlength="15"
                                                    styleId="discount1_id"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"/>
                                </div>

                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.to"/>
                                    </label>

                                    <app:numberText property="parameter(discount2)"
                                                    styleClass="numberText ${app2:getFormInputClasses()} numberInputWidth"
                                                    styleId="discount2"
                                                    tabindex="2"
                                                    maxlength="15"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="active_id">
                            <fmt:message key="Contact.status"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(false)}">
                            <c:choose>
                                <c:when test="${supplierReportListForm.params.active==null}">
                                    <html:select property="parameter(active)"
                                                 value="1"
                                                 styleId="active_id"
                                                 styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                                 tabindex="3">
                                        <html:options collection="activeList" property="value" labelProperty="label"/>
                                    </html:select>
                                </c:when>

                                <c:otherwise>
                                    <html:select property="parameter(active)"
                                                 styleId="active_id"
                                                 styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                                 tabindex="3">
                                        <html:options collection="activeList" property="value" labelProperty="label"/>
                                    </html:select>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="price1_id">
                            <fmt:message key="Product.price"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(false)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.from"/>
                                    </label>

                                    <app:numberText property="parameter(price1_id)"
                                                    styleClass="numberText ${app2:getFormInputClasses()} numberInputWidth"
                                                    tabindex="4"
                                                    styleId="price1_id"
                                                    maxlength="15"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"/>
                                </div>

                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.to"/>
                                    </label>

                                    <app:numberText property="parameter(price2)"
                                                    styleClass="numberText ${app2:getFormInputClasses()} numberInputWidth"
                                                    styleId="price2"
                                                    tabindex="5"
                                                    maxlength="15"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </fieldset>
            <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                      tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="productName" labelKey="Article.productName"
                                                isDefault="true" defaultOrder="true"
                                                isDefaultGrouping="true"/>
                <titus:reportGroupSortColumnTag name="supplierName" labelKey="Supplier"/>
                <titus:reportGroupSortColumnTag name="contact" labelKey="ContactPerson"/>
                <titus:reportGroupSortColumnTag name="price" labelKey="Product.price"/>
                <titus:reportGroupSortColumnTag name="partNumber" labelKey="SupplierProduct.orderNumber"/>
                <titus:reportGroupSortColumnTag name="active" labelKey="Common.active"/>
            </titus:reportGroupSortTag>

            <c:set var="reportFormats" value="${supplierReportListForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${supplierReportListForm.pageSizes}" scope="request"/>

            <tags:bootstrapReportOptionsTag/>

            <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
            <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
            <titus:reportInitializeConstantsTag/>
            <titus:reportTag id="supplierReportList" title="Product.Report.SupplierList"
                             locale="${sessionScope.user.valueMap['locale']}"
                             timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
            <titus:reportFieldTag name="productName" resourceKey="Article.productName" type="${FIELD_TYPE_STRING}"
                                  width="20"
                                  fieldPosition="1"/>
            <titus:reportFieldTag name="supplierName" resourceKey="Supplier" type="${FIELD_TYPE_STRING}" width="20"
                                  fieldPosition="2"/>
            <titus:reportFieldTag name="contact" resourceKey="ContactPerson" type="${FIELD_TYPE_STRING}" width="20"
                                  fieldPosition="3"/>
            <titus:reportFieldTag name="price" resourceKey="Product.price" align="${FIELD_ALIGN_RIGHT}"
                                  patternKey="numberFormat.2DecimalPlaces" type="${FIELD_TYPE_DECIMALNUMBER}"
                                  width="15"
                                  fieldPosition="4"/>
            <titus:reportFieldTag name="partNumber" resourceKey="SupplierProduct.orderNumber"
                                  type="${FIELD_TYPE_STRING}"
                                  width="15"
                                  fieldPosition="5"/>
            <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}" width="10"
                                  conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"
                                  fieldPosition="6"/>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)"
                         tabindex="57"><fmt:message
                    key="Campaign.Report.generate"/></html:submit>
            <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                         onclick="formReset('productStyle')">
                <fmt:message
                        key="Common.clear"/></html:button>
        </div>
    </html:form>

</div>