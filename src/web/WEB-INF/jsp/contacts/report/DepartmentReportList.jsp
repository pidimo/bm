<%@ include file="/Includes.jsp" %>

<tags:initBootstrapSelectPopup/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<script>
    function goSubmit(obj) {
        if (obj.options[obj.selectedIndex].value != "") {
            document.forms[0].submit();
        }
    }
</script>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="/Report/DepartmentList/Execute.do" focus="parameter(address)" styleId="departmentReportList"
               styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Contact.Report.DepartmentList"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                            <fmt:message key="Contact"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="input-group">
                                <app:text property="parameter(address)" styleId="fieldAddressName_id"
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          maxlength="40" tabindex="1" readonly="true"/>
                                <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
                                <span class="input-group-btn">
                                    <tags:bootstrapSelectPopup styleId="searchAddress_id"
                                                               url="/contacts/OrganizationSearch.do"
                                                               name="searchAddress"
                                                               titleKey="Contact.Title.search"
                                                               isLargeModal="true"
                                                               tabindex="2"
                                                               hide="false" submitOnSelect="true"/>
                                    <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                    styleClass="${app2:getFormButtonClasses()}"
                                                                    nameFieldId="fieldAddressName_id"
                                                                    titleKey="Common.clear" submitOnClear="true"
                                                                    hide="false"
                                                                    tabindex="3"
                                                                    glyphiconClass="glyphicon-erase"/>
                                </span>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="parentId_id">
                            <fmt:message key="Department.departmentParent"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(parentId)" styleId="parentId_id"
                                          listName="departmentBaseList" firstEmpty="true"
                                          labelProperty="name" valueProperty="departmentId"
                                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                                          tabIndex="4">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="addressId"
                                                 value="${not empty departmentReportListForm.params.addressId?departmentReportListForm.params.addressId:0}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="contactPersonId_id">
                            <fmt:message key="Contact.Organization.Department.manager"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(contactPersonId)" styleId="contactPersonId_id"
                                          listName="contactPersonSimpleList"
                                          tabIndex="5"
                                          firstEmpty="true"
                                          labelProperty="contactPersonName" valueProperty="contactPersonId"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          module="/contacts">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="addressId"
                                                 value="${not empty departmentReportListForm.params.addressId?departmentReportListForm.params.addressId:0}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>

            <c:set var="reportFormats" value="${departmentReportListForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${departmentReportListForm.pageSizes}" scope="request"/>

            <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                      tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="contactName" labelKey="Contact" isDefault="true"
                                                isDefaultGrouping="true" defaultOrder="true"/>
                <titus:reportGroupSortColumnTag name="name" labelKey="Department.name"/>
                <titus:reportGroupSortColumnTag name="parentName" labelKey="Department.parentName"/>
                <titus:reportGroupSortColumnTag name="managerName"
                                                labelKey="Contact.Organization.Department.manager"/>
            </titus:reportGroupSortTag>

            <tags:bootstrapReportOptionsTag/>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
                <fmt:message key="Campaign.Report.generate"/>
            </html:submit>
            <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                         onclick="formReset('departmentReportList')">
                <fmt:message key="Common.clear"/>
            </html:button>
        </div>

        <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
        <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="departmentReportList" title="Contact.Report.DepartmentList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="contactName" resourceKey="Contact" type="${FIELD_TYPE_STRING}" width="20"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="name" resourceKey="Department.name" type="${FIELD_TYPE_STRING}" width="25"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="parentName" resourceKey="Department.parentName" type="${FIELD_TYPE_STRING}"
                              width="25" fieldPosition="3"/>
        <titus:reportFieldTag name="managerName" resourceKey="Contact.Organization.Department.manager"
                              type="${FIELD_TYPE_STRING}" width="30" fieldPosition="4"/>
    </html:form>
</div>
<tags:jQueryValidation formName="departmentReportListForm"/>