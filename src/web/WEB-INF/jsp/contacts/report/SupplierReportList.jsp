<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced
        fields="fieldProcessId_id, fieldProcessName_id, percent_id, fieldAddressId_id, fieldAddressName_id"/>
<tags:jscript language="JavaScript" src="/js/contacts/searchcity.jsp"/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<%
    pageContext.setAttribute("contactTypeList", JSPHelper.getContactTypeList(request));
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
    pageContext.setAttribute("addressTypeList", JSPHelper.getAddressTypeList(request));
%>

<script>
    function goSubmit() {
        document.forms[0].submit();
    }
</script>
<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="/Report/SupplierList/Execute.do" focus="parameter(countryId)" styleId="supplierReport"
               styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Contact.Report.SupplierList"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="countryId">
                            <fmt:message key="Contact.country"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(countryId)" styleId="countryId"
                                          listName="countryBasicList"
                                          firstEmpty="true" labelProperty="name" valueProperty="id" module="/catalogs"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}" tabIndex="1">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="active_id">
                            <fmt:message key="Contact.status"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <c:choose>
                                <c:when test="${listForm.params.active==null}">
                                    <html:select property="parameter(active)" value="1"
                                                 styleId="active_id"
                                                 styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="2">
                                        <html:options collection="activeList" property="value" labelProperty="label"/>
                                    </html:select>
                                </c:when>
                                <c:otherwise>
                                    <html:select property="parameter(active)"
                                                 styleId="active_id"
                                                 styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="2">
                                        <html:options collection="activeList" property="value" labelProperty="label"/>
                                    </html:select>
                                </c:otherwise>
                            </c:choose>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="zip_id">
                            <fmt:message key="Contact.cityZip"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-5">
                                    <app:text property="parameter(zip)"
                                              styleId="zip_id"
                                              styleClass="zipText form-control" maxlength="10"
                                              titleKey="Contact.zip"
                                              tabindex="3"/>
                                </div>
                                <div class="col-xs-7">
                                    <app:text property="parameter(cityName)"
                                              styleId="cityName_id"
                                              styleClass="cityNameText form-control"
                                              tabindex="3"/>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="supplierTypeId_id">
                            <fmt:message key="Supplier.type"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <app:catalogSelect property="parameter(supplierTypeId)" catalogTable="suppliertype"
                                               styleId="supplierTypeId_id"
                                               idColumn="suppliertypeid" labelColumn="suppliertypename"
                                               styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                               tabindex="4"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="languageId_id">
                            <fmt:message key="Contact.language"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(languageId)"
                                          styleId="languageId_id"
                                          listName="languageBaseList" firstEmpty="true"
                                          labelProperty="name" valueProperty="id" module="/catalogs"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                          tabIndex="5">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="branchId_id">
                            <fmt:message key="Supplier.branch"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <app:catalogSelect property="parameter(branchId)"
                                               styleId="branchId_id"
                                               catalogTable="branch" idColumn="branchid"
                                               labelColumn="branchname"
                                               styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="6"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>

                        </div>
                    </div>
                </div>
            </fieldset>
            <c:set var="reportFormats" value="${supplierReportForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${supplierReportForm.pageSizes}" scope="request"/>
            <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                      tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="supplierName" labelKey="Supplier"/>
                <titus:reportGroupSortColumnTag name="branchName" labelKey="Supplier.branch"/>
                <titus:reportGroupSortColumnTag name="supplierTypeName" labelKey="Supplier.type"/>
                <titus:reportGroupSortColumnTag name="countryCode" labelKey="Contact.countryCode"/>
                <titus:reportGroupSortColumnTag name="zip" labelKey="Contact.zip"/>
                <titus:reportGroupSortColumnTag name="cityName" labelKey="Contact.city"/>
                <titus:reportGroupSortColumnTag name="addressType" labelKey="Contact.type"/>
                <titus:reportGroupSortColumnTag name="active" labelKey="Common.active"/>
            </titus:reportGroupSortTag>

            <tags:bootstrapReportOptionsTag/>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="button ${app2:getFormButtonClasses()}" property="parameter(generate)"
                         tabindex="57"><fmt:message
                    key="Campaign.Report.generate"/>
            </html:submit>
            <html:button property="reset1" tabindex="58" styleClass="button ${app2:getFormButtonClasses()}"
                         onclick="formReset('supplierReport')">
                <fmt:message key="Common.clear"/>
            </html:button>
        </div>

        <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
        <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
        <c:set var="organization"><fmt:message key="Contact.organization"/></c:set>
        <c:set var="person"><fmt:message key="Contact.person"/></c:set>

        <titus:reportInitializeConstantsTag/>

        <titus:reportTag id="supplierReportList" title="Product.Report.SupplierList"
                         pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="supplierName" resourceKey="Supplier" type="${FIELD_TYPE_STRING}" width="26"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="branchName" resourceKey="Supplier.branch" type="${FIELD_TYPE_STRING}" width="12"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="supplierTypeName" resourceKey="Supplier.type" type="${FIELD_TYPE_STRING}" width="15"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="countryCode" resourceKey="Contact.countryCode" type="${FIELD_TYPE_STRING}"
                              width="10" fieldPosition="4"/>
        <titus:reportFieldTag name="zip" resourceKey="Contact.zip" type="${FIELD_TYPE_STRING}" width="7"
                              fieldPosition="5"/>
        <titus:reportFieldTag name="cityName" resourceKey="Contact.city" type="${FIELD_TYPE_STRING}" width="8"
                              fieldPosition="6"/>
        <titus:reportFieldTag name="addressType" resourceKey="Contact.type" type="${FIELD_TYPE_STRING}"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource addressType [${person}] [${organization}] [1] [0]"
                              width="12" fieldPosition="7"/>
        <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"
                              width="8" fieldPosition="8"/>
    </html:form>
</div>
<tags:jQueryValidation formName="supplierReportForm" />