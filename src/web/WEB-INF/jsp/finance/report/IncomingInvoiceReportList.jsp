<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>

<script type="text/javascript">
    function myReset() {
        var form = document.incomingInvoiceReportForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].name == "parameter(currencyId)") {
                form.elements[i].options.selectedIndex = 0;
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
        if (document.getElementById("fieldAddressId_id") != null && document.getElementById("fieldAddressId_id") != "") {
            document.getElementById("fieldAddressId_id").value = "";
        }
    }
</script>
<c:set var="invoiceTypeList" value="${app2:getInvoiceTypes(pageContext.request)}"/>

<html:form action="/Report/IncomingInvoiceList/Execute.do" focus="parameter(supplierName_FIELD)"
           styleId="invoiceFormId" styleClass="form-horizontal">

    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="${pagetitle}"/>
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                        <fmt:message key="Finance.incomingInvoice.supplierName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="input-group">
                            <app:text property="parameter(supplierName_FIELD)"
                                      styleId="fieldAddressName_id"
                                      styleClass="mediumText ${app2:getFormInputClasses()}"
                                      readonly="true" tabindex="1"/>
                            <html:hidden property="parameter(supplierId)" styleId="fieldAddressId_id"/>
                   <span class="input-group-btn">
                       <tags:bootstrapSelectPopup url="/contacts/SearchAddress.do"
                                                  isLargeModal="true"
                                                  styleId="SearchSupplier_id"
                                                  name="SearchSupplier"
                                                  titleKey="Common.search"
                                                  modalTitleKey="Contact.Title.search"
                                                  tabindex="2"/>
                       <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                                       titleKey="Common.clear" tabindex="3"/>
                   </span>
                        </div>
                    </div>
                </div>
                <!--*******************COLUMN DIVISION************************-->
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="type_id">
                        <fmt:message key="Finance.incomingInvoice.type"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:select property="parameter(type)"
                                     styleId="type_id"
                                     styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                     tabindex="4">
                            <html:option value=""/>
                            <html:options collection="invoiceTypeList" property="value" labelProperty="label"/>
                        </html:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="currencyId_id">
                        <fmt:message key="Finance.incomingInvoice.currency"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(currencyId)"
                                      styleId="currencyId_id"
                                      listName="basicCurrencyList"
                                      labelProperty="name"
                                      valueProperty="id"
                                      firstEmpty="true"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      module="/catalogs"
                                      tabIndex="5">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>
                <!--*******************COLUMN DIVISION************************-->
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startInvoiceDate">
                        <fmt:message key="Finance.incomingInvoice.invoiceDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fmt:message key="datePattern" var="datePattern"/>
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(startInvoiceDate)"
                                                  maxlength="10" tabindex="6"
                                                  styleId="startInvoiceDate"
                                                  calendarPicker="true"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.to" var="to"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(endInvoiceDate)"
                                                  maxlength="10" tabindex="8"
                                                  styleId="endInvoiceDate"
                                                  calendarPicker="true"
                                                  mode="bootstrap"
                                                  placeHolder="${to}"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="amountNet1_id">
                        <fmt:message key="Finance.incomingInvoice.amountNet"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="amountNet1_id">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:numberText property="parameter(amountNet1)"
                                                styleId="amountNet1_id"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberInputWidth"
                                                tabindex="9" maxlength="12"
                                                numberType="decimal" maxInt="10" maxFloat="2"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="amountNet2_id">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:numberText property="parameter(amountNet2)"
                                                styleId="amountNet2_id"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberInputWidth"
                                                tabindex="10"
                                                maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
                            </div>
                        </div>
                    </div>
                </div>
                <!--*******************COLUMN DIVISION************************-->
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startPayDate">
                        <fmt:message key="Finance.incomingInvoice.receiptDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fmt:message key="datePattern" var="datePattern"/>

                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(startReceiptDate)"
                                                  maxlength="10" tabindex="11"
                                                  styleId="startPayDate"
                                                  calendarPicker="true"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.to" var="to"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(endReceiptDate)"
                                                  maxlength="10" tabindex="12"
                                                  styleId="endPayDate"
                                                  calendarPicker="true"
                                                  mode="bootstrap"
                                                  placeHolder="${to}"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="amountGross1_id">
                        <fmt:message key="Finance.incomingInvoice.amountGross"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="amountGross1_id">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:numberText property="parameter(amountGross1)"
                                                styleId="amountGross1_id"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberInputWidth"
                                                tabindex="13" maxlength="12"
                                                numberType="decimal" maxInt="10" maxFloat="2"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="amountGross2_id">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:numberText property="parameter(amountGross2)"
                                                styleId="amountGross2_id"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberInputWidth"
                                                tabindex="14"
                                                maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
                            </div>
                        </div>
                    </div>
                </div>
                <!--*******************COLUMN DIVISION************************-->
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startPaidUntilDate">
                        <fmt:message key="Finance.incomingInvoice.paidUntil"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fmt:message key="datePattern" var="datePattern"/>
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(startPaidUntilDate)" maxlength="10" tabindex="15"
                                                  styleId="startPaidUntilDate"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.to" var="to"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(endPaidUntilDate)" maxlength="10" tabindex="16"
                                                  styleId="endPaidUntilDate"
                                                  mode="bootstrap"
                                                  placeHolder="${to}"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="openAmount1_id">
                        <fmt:message key="Finance.incomingInvoice.openAmount"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="openAmount1_id">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:numberText property="parameter(openAmount1)"
                                                styleId="openAmount1_id"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberInputWidth"
                                                tabindex="17" maxlength="12"
                                                numberType="decimal" maxInt="10" maxFloat="2"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="openAmount2_id">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:numberText property="parameter(openAmount2)"
                                                styleId="openAmount2_id"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberInputWidth"
                                                tabindex="18"
                                                maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>
        <c:set var="reportFormats" value="${incomingInvoiceReportForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${incomingInvoiceReportForm.pageSizes}" scope="request"/>

        <titus:reportGroupSortTag width="100%" tableStyleClass="${app2:getTableClasesIntoForm()}"
                                  mode="bootstrap">
            <titus:reportGroupSortColumnTag name="INVOICENUMBER"
                                            labelKey="Finance.incomingInvoice.invoiceNumber"/>
            <titus:reportGroupSortColumnTag name="SUPPLIERNAME"
                                            labelKey="Finance.incomingInvoice.supplierName"/>
            <titus:reportGroupSortColumnTag name="invoiceDate" labelKey="Finance.incomingInvoice.invoiceDate"
                                            isDate="true" orderColumnName="INVOICEDATE"/>
            <titus:reportGroupSortColumnTag name="receiptDate" labelKey="Finance.incomingInvoice.receiptDate"
                                            isDate="true" orderColumnName="RECEIPTDATE"/>
            <titus:reportGroupSortColumnTag name="paidUntil" labelKey="Finance.incomingInvoice.paidUntil"
                                            isDate="true" orderColumnName="PAIDUNTILDATE"/>
            <titus:reportGroupSortColumnTag name="CURRENCYLABEL" labelKey="Finance.incomingInvoice.currency"/>
            <titus:reportGroupSortColumnTag name="amountNet" labelKey="Finance.incomingInvoice.amountNet"
                                            orderColumnName="AMOUNTNET"/>
            <titus:reportGroupSortColumnTag name="amountGross" labelKey="Finance.incomingInvoice.amountGross"
                                            orderColumnName="AMOUNTGROSS"/>
            <titus:reportGroupSortColumnTag name="openAmount" labelKey="Finance.incomingInvoice.openAmount"
                                            orderColumnName="OPENAMOUNT"/>
        </titus:reportGroupSortTag>

        <tags:bootstrapReportOptionsTag/>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit styleClass="button ${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="reset1" tabindex="58" styleClass="button ${app2:getFormButtonClasses()}"
                     onclick="myReset()">
            <fmt:message key="Common.clear"/>
        </html:button>
    </div>

    <titus:reportInitializeConstantsTag/>
    <titus:reportTag id="incomingInvoiceReportList" title="Finance.Report.incomingInvoiceList"
                     pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                     locale="${sessionScope.user.valueMap['locale']}"
                     timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
    <titus:reportFieldTag name="INVOICENUMBER" resourceKey="Finance.incomingInvoice.invoiceNumber"
                          type="${FIELD_TYPE_STRING}" width="10" fieldPosition="1"/>
    <titus:reportFieldTag name="SUPPLIERNAME" resourceKey="Finance.incomingInvoice.supplierName"
                          type="${FIELD_TYPE_STRING}" width="18" fieldPosition="2"/>
    <titus:reportFieldTag name="invoiceDate" resourceKey="Finance.incomingInvoice.invoiceDate"
                          type="${FIELD_TYPE_DATEINT}" width="8" fieldPosition="3" patternKey="datePattern"/>
    <titus:reportFieldTag name="paidUntil" resourceKey="Finance.incomingInvoice.paidUntil"
                          type="${FIELD_TYPE_DATEINT}" width="8" fieldPosition="4" patternKey="datePattern"/>
    <titus:reportFieldTag name="TYPE" resourceKey="Finance.incomingInvoice.type" type="${FIELD_TYPE_STRING}"
                          width="4" fieldPosition="5"
                          conditionMethod="com.piramide.elwis.web.salesmanager.el.Functions.getInvoiceTypeName TYPE [${sessionScope.user.valueMap['locale']}]"/>
    <titus:reportFieldTag name="CURRENCYLABEL" resourceKey="Finance.incomingInvoice.currency"
                          type="${FIELD_TYPE_STRING}" width="8" fieldPosition="6"/>
    <titus:reportFieldTag name="amountNet" resourceKey="Finance.incomingInvoice.amountNet"
                          type="${FIELD_TYPE_DECIMALNUMBER}" width="9" fieldPosition="7"
                          patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"/>
    <titus:reportFieldTag name="VATAMOUNT" resourceKey="Finance.incomingInvoice.vatAmount"
                          type="${FIELD_TYPE_CALCULATEDDECIMALNUMBER}" width="9" fieldPosition="8"
                          patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"
                          conditionMethod="com.piramide.elwis.utils.ReportHelper.substractDecimalAmounts amountGross amountNet"/>
    <titus:reportFieldTag name="amountGross" resourceKey="Finance.incomingInvoice.amountGross"
                          type="${FIELD_TYPE_DECIMALNUMBER}" width="9" fieldPosition="9"
                          patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"/>
    <titus:reportFieldTag name="openAmount" resourceKey="Finance.incomingInvoice.openAmount"
                          type="${FIELD_TYPE_DECIMALNUMBER}" width="9" fieldPosition="10"
                          patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"/>
    <titus:reportFieldTag name="receiptDate" resourceKey="Finance.incomingInvoice.receiptDate"
                          type="${FIELD_TYPE_DATEINT}" width="8" fieldPosition="11" patternKey="datePattern"/>
</html:form>