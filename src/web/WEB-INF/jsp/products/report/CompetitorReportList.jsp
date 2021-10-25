<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<script language="JavaScript" type="text/javascript">
    function changeStartDateValue() {
        $("#endRange").val($("#startRange").val());
    }
</script>

<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<html:form action="/Report/CompetitorList/Execute.do" focus="parameter(productName)" styleId="productStyle"
           styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="Product.Report.CompetitorList"/>
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_name">
                        <fmt:message key="Product.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="input-group">
                            <app:text property="parameter(productName)" styleId="field_name"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40"
                                      readonly="true" tabindex="1"/>
                            <html:hidden property="parameter(productId)" styleId="field_key"/>
                            <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
                            <html:hidden property="parameter(2)" styleId="field_unitId"/>
                            <html:hidden property="parameter(3)" styleId="field_price"/>
                        <span class="input-group-btn">
                            <tags:bootstrapSelectPopup styleId="SearchProduct_id" url="/products/SearchProduct.do"
                                                       name="SearchProduct"
                                                       titleKey="Common.search" tabindex="1"
                                                       modalTitleKey="Product.Title.SimpleSearch" isLargeModal="true"/>
                            <tags:clearBootstrapSelectPopup keyFieldId="field_key" tabindex="1" nameFieldId="field_name"
                                                            titleKey="Common.clear"/>
                        </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startRange">
                        <fmt:message key="Competitor.entryDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <div class="input-group date">
                                    <fmt:message var="datePattern" key="datePattern"/>
                                    <fmt:message key="Common.from" var="from"/>
                                    <app:dateText property="parameter(startRange)" maxlength="10" tabindex="2"
                                                  styleId="startRange"
                                                  onchange="changeStartDateValue()"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  currentDate="false"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.to" var="to"/>
                                    <app:dateText property="parameter(endRange)" maxlength="10" tabindex="3"
                                                  styleId="endRange"
                                                  mode="bootstrap"
                                                  placeHolder="${to}"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  currentDate="false"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
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
                                <app:numberText property="parameter(price1)" styleId="price1_id"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="4"
                                                maxlength="12"
                                                numberType="decimal" maxInt="10" maxFloat="2"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="price2">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:numberText property="parameter(price2)" styleId="price2"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="5"
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
            <titus:reportGroupSortColumnTag name="product_Name" labelKey="Competitor.product" isDefault="true"
                                            defaultOrder="true" isDefaultGrouping="true"/>
            <titus:reportGroupSortColumnTag name="productName" labelKey="Competitor.competitorProduct"/>
            <titus:reportGroupSortColumnTag name="competitorName" labelKey="Competitor.contact"/>
            <titus:reportGroupSortColumnTag name="entryDate" labelKey="Competitor.entryDate" isDate="true"/>
            <titus:reportGroupSortColumnTag name="price" labelKey="Competitor.price"/>
        </titus:reportGroupSortTag>

        <c:set var="reportFormats" value="${competitorReportListForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${competitorReportListForm.pageSizes}" scope="request"/>

        <tags:bootstrapReportOptionsTag/>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
            <fmt:message
                    key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                     onclick="formReset('productStyle')">
            <fmt:message key="Common.clear"/>
        </html:button>
    </div>
    <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
    <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
    <titus:reportInitializeConstantsTag/>
    <titus:reportTag id="competitorReportList" title="Product.Report.CompetitorList"
                     locale="${sessionScope.user.valueMap['locale']}"
                     timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

    <titus:reportFieldTag name="product_Name" resourceKey="Competitor.product" type="${FIELD_TYPE_STRING}"
                          width="20"
                          fieldPosition="1"/>
    <titus:reportFieldTag name="productName" resourceKey="Competitor.competitorProduct" type="${FIELD_TYPE_STRING}"
                          width="25"
                          fieldPosition="2"/>
    <titus:reportFieldTag name="competitorName" resourceKey="Competitor.contact" type="${FIELD_TYPE_STRING}"
                          width="25" fieldPosition="3"/>
    <titus:reportFieldTag name="entryDate" resourceKey="Competitor.entryDate" type="${FIELD_TYPE_DATEINT}"
                          patternKey="datePattern" width="15" fieldPosition="4"/>
    <titus:reportFieldTag name="price" resourceKey="Competitor.price" type="${FIELD_TYPE_DECIMALNUMBER}"
                          patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}" width="15"
                          fieldPosition="5"/>
</html:form>

