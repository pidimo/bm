<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<tags:jscript language="JavaScript" src="/js/contacts/searchcity.jsp"/>

<%
    pageContext.setAttribute("contactTypeList", JSPHelper.getContactTypeList(request));
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
    pageContext.setAttribute("addressTypeList", JSPHelper.getAddressTypeList(request));
%>
<script>

    function goSubmit(obj) {
        if (obj.options[obj.selectedIndex].value != "") {
            document.forms[0].submit();
        }
    }
</script>
<tags:initBootstrapDatepicker/>
<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="/Report/ContactList/Execute.do" focus="countryId" styleId="contactReportList"
               styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fielset>
                <legend class="title">
                    <fmt:message key="Contact.Report.ContactList"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Contact.country"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(countryId)" styleId="countryId"
                                          listName="countryBasicList"
                                          firstEmpty="true"
                                          labelProperty="name" valueProperty="id" module="/catalogs"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                          tabIndex="1">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Contact.type"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:select property="parameter(code)"
                                         styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                         tabindex="2">
                                <html:option value=""/>
                                <html:options collection="contactTypeList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Contact.cityZip"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-sm-5 wrapperButton">
                                    <app:text property="parameter(zip)" styleClass="zipText form-control" maxlength="10"
                                              titleKey="Contact.zip"
                                              tabindex="2"/>
                                </div>
                                <div class="col-sm-7 wrapperButton">
                                    <app:text property="parameter(cityName)" styleClass="cityNameText form-control"
                                              tabindex="3"/>
                                </div>
                            </div>

                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Contact.status"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <c:choose>
                                <c:when test="${contactReportListForm.params.active==null}">
                                    <html:select property="parameter(active)" value="1"
                                                 styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="4">
                                        <html:options collection="activeList" property="value" labelProperty="label"/>
                                    </html:select>
                                </c:when>
                                <c:otherwise>
                                    <html:select property="parameter(active)"
                                                 styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="4">
                                        <html:options collection="activeList" property="value" labelProperty="label"/>
                                    </html:select>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Contact.language"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(languageId)" listName="languageBaseList" firstEmpty="true"
                                          labelProperty="name" valueProperty="id" module="/catalogs"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          tabIndex="5">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Contact.keywords"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(keywords)" styleClass="mediumText form-control"
                                       maxlength="50"
                                       tabindex="6"/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Common.for"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:select property="parameter(addressType)"
                                         styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="7">
                                <html:option value=""/>
                                <html:options collection="addressTypeList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Campaign.dateCreation"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fmt:message key="datePattern" var="datePattern"/>
                            <div class="row">
                                <div class="col-sm-6 wrapperButton">
                                    <fmt:message key="Common.from" var="from"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(startRange)"
                                                      maxlength="10" tabindex="8" styleId="startRange"
                                                      mode="bootstrap"
                                                      placeHolder="${from}"
                                                      calendarPicker="true" datePatternKey="${datePattern}"
                                                      styleClass="dateText form-control"
                                                      convert="true"/>
                                    </div>
                                </div>
                                <div class="col-sm-6 wrapperButton">
                                    <fmt:message key="Common.to" var="to"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(endRange)" maxlength="9" tabindex="10"
                                                      styleId="endRange"
                                                      mode="bootstrap"
                                                      placeHolder="${to}"
                                                      calendarPicker="true" datePatternKey="${datePattern}"
                                                      styleClass="dateText form-control"
                                                      convert="true"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </fielset>
            <c:set var="reportFormats" value="${contactReportListForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${contactReportListForm.pageSizes}" scope="request"/>

            <titus:reportGroupSortTag mode="bootstrap" width="100%"
                                      tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="addressName" labelKey="Contact.name"/>
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
                    key="Campaign.Report.generate"/></html:submit>
            <html:button property="reset1" tabindex="58" styleClass="button ${app2:getFormButtonClasses()}"
                         onclick="formReset('contactReportList')">
                <fmt:message key="Common.clear"/></html:button>
        </div>
        <c:set var="myActive">
            <fmt:message key="Common.active"/>
        </c:set>
        <c:set var="unactive">
            <fmt:message key="Common.inactive"/>
        </c:set>
        <c:set var="organization">
            <fmt:message key="Contact.organization"/>
        </c:set>
        <c:set var="person">
            <fmt:message key="Contact.person"/>
        </c:set>

        <titus:reportInitializeConstantsTag/>

        <titus:reportTag id="contactReportList" title="Contact.Report.ContactList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="addressName" resourceKey="Contact.name" type="${FIELD_TYPE_STRING}"
                              width="30"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="countryCode" resourceKey="Contact.countryCode"
                              type="${FIELD_TYPE_STRING}"
                              width="10"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="zip" resourceKey="Contact.zip" type="${FIELD_TYPE_STRING}" width="10"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="cityName" resourceKey="Contact.city" type="${FIELD_TYPE_STRING}"
                              width="20"
                              fieldPosition="4"/>
        <titus:reportFieldTag name="addressType" resourceKey="Contact.type" type="${FIELD_TYPE_STRING}"
                              width="20"
                              fieldPosition="5"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource addressType [${person}] [${organization}] [1] [0]"/>
        <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}"
                              width="10"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"
                              fieldPosition="6"/>
    </html:form>
</div>