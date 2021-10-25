<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:jscript language="JavaScript" src="/js/contacts/searchcity.jsp"/>

<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>
<%
    pageContext.setAttribute("contactTypeList", JSPHelper.getContactTypeList(request));
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
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
    <html:form action="/Report/ContactPersonList/Execute.do"
               focus="parameter(addressId)"
               styleId="contactReportList"
               styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <legend class="title">
                <fmt:message key="Contact.Report.ContactPersonList"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                        <fmt:message key="Appointment.contact"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">

                        <div class="input-group">

                            <app:text property="parameter(address)"
                                      styleId="fieldAddressName_id"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40"
                                      tabindex="1" readonly="true"/>
                            <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
                            <span class="input-group-btn">
                                <tags:bootstrapSelectPopup url="/contacts/SearchAddress.do"
                                                           name="searchAddress"
                                                           titleKey="Common.search"
                                                           modalTitleKey="Contact.Title.search"
                                                           hide="false"
                                                           submitOnSelect="true"
                                                           styleClass="${app2:getFormButtonClasses()}"
                                                           styleId="contactSelectPopup_id"
                                                           isLargeModal="true"/>
                                <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                                                titleKey="Common.clear"
                                                                submitOnClear="true"
                                                                hide="false"
                                                                styleClass="${app2:getFormButtonClasses()}"/>
                            </span>
                        </div>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="languageId_id">
                        <fmt:message key="Contact.language"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <fanta:select property="parameter(languageId)"
                                      listName="languageBaseList"
                                      firstEmpty="true"
                                      labelProperty="name"
                                      valueProperty="id"
                                      module="/catalogs"
                                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                                      tabIndex="2"
                                      styleId="languageId_id">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="department_Id">
                        <fmt:message key="ContactPerson.department"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(false)}">
                        <fanta:select property="parameter(department_Id)"
                                      listName="departmentBaseList"
                                      firstEmpty="true"
                                      labelProperty="name"
                                      valueProperty="departmentId"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      tabIndex="3"
                                      styleId="department_Id">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId"
                                             value="${not empty contactReportListForm.params.addressId?contactReportListForm.params.addressId:0}"/>
                        </fanta:select>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="countryId">
                        <fmt:message key="Contact.country"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <fanta:select property="parameter(countryId)"
                                      styleId="countryId"
                                      listName="countryBasicList"
                                      firstEmpty="true"
                                      labelProperty="name"
                                      valueProperty="id"
                                      module="/catalogs"
                                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                                      tabIndex="4">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="personTypeId_id">
                        <fmt:message key="ContactPerson.personType"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(false)}">
                        <fanta:select property="parameter(personTypeId)"
                                      listName="personTypeList"
                                      firstEmpty="true"
                                      styleId="personTypeId_id"
                                      labelProperty="name"
                                      module="/catalogs"
                                      valueProperty="id"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      tabIndex="5">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="zip_id">
                        <fmt:message key="Contact.cityZip"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-4 wrapperButton">
                                <app:text property="parameter(zip)"
                                          styleClass="${app2:getFormInputClasses()} zipText"
                                          maxlength="10"
                                          styleId="zip_id"
                                          titleKey="Contact.zip"
                                          tabindex="6"/>
                            </div>

                            <div class="col-xs-12 col-sm-8 wrapperButton">
                                <app:text property="parameter(cityName)"
                                          styleClass="${app2:getFormInputClasses()} cityNameText"
                                          tabindex="6"/>
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

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <c:choose>
                            <c:when test="${contactReportListForm.params.active==null}">
                                <html:select property="parameter(active)"
                                             value="1"
                                             styleId="active_id"
                                             styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                             tabindex="7">
                                    <html:options collection="activeList" property="value" labelProperty="label"/>
                                </html:select>
                            </c:when>
                            <c:otherwise>
                                <html:select property="parameter(active)"
                                             styleId="active_id"
                                             styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                             tabindex="7">
                                    <html:options collection="activeList" property="value" labelProperty="label"/>
                                </html:select>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="row col-xs-12 col-sm-12 col-md-6">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startRange">
                        <fmt:message key="Common.creationDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <fmt:message key="datePattern" var="datePattern"/>
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(startRange)"
                                                  maxlength="10"
                                                  tabindex="8"
                                                  styleId="startRange"
                                                  placeHolder="${from}"
                                                  calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()} dateText"
                                                  convert="true"
                                                  mode="bootstrap"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <fmt:message key="Common.to" var="to"/>

                                <div class="input-group date">
                                    <app:dateText property="parameter(endRange)"
                                                  maxlength="10"
                                                  tabindex="9"
                                                  styleId="endRange"
                                                  placeHolder="${to}"
                                                  calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()} dateText"
                                                  convert="true"
                                                  mode="bootstrap"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="keywords_id">
                        <fmt:message key="Contact.keywords"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(keywords)"
                                   styleClass="${app2:getFormInputClasses()} mediumText"
                                   maxlength="50"
                                   styleId="keywords_id"
                                   tabindex="10"/>
                    </div>
                </div>
            </div>

            <c:set var="reportFormats" value="${contactReportListForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${contactReportListForm.pageSizes}" scope="request"/>

            <titus:reportGroupSortTag mode="bootstrap" width="100%"
                                      tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="contactName" labelKey="Contact" isDefault="true"
                                                defaultOrder="true" isDefaultGrouping="true"/>
                <titus:reportGroupSortColumnTag name="contactPersonName" labelKey="ContactPerson"/>
                <titus:reportGroupSortColumnTag name="department" labelKey="ContactPerson.department"/>
                <titus:reportGroupSortColumnTag name="function" labelKey="ContactPerson.function"/>
                <titus:reportGroupSortColumnTag name="active" labelKey="Common.active"/>
            </titus:reportGroupSortTag>

            <tags:bootstrapReportOptionsTag/>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
                <fmt:message key="Campaign.Report.generate"/>
            </html:submit>
            <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                         onclick="formReset('contactReportList')">
                <fmt:message key="Common.clear"/>
            </html:button>
        </div>

        <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
        <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="contactPersonReportList" title="Contact.Report.ContactPersonList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="contactName" resourceKey="Contact" type="${FIELD_TYPE_STRING}" width="25"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="contactPersonName" resourceKey="ContactPerson" type="${FIELD_TYPE_STRING}"
                              width="25"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="department" resourceKey="ContactPerson.department" type="${FIELD_TYPE_STRING}"
                              width="20"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="function" resourceKey="ContactPerson.function" type="${FIELD_TYPE_STRING}"
                              width="20"
                              fieldPosition="4"/>
        <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}" width="10"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"
                              fieldPosition="5"/>
    </html:form>

</div>
