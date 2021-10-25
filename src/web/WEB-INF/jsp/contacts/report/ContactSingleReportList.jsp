<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<script language="JavaScript" type="text/javascript">
    function changeCommunicationsRowConfigState(checkBoxElement) {
        if (checkBoxElement.checked) {
            document.getElementById("communicationsNumberTF").style.display = "";
        }
        else {
            document.getElementById("communicationsNumberTF").style.display = "none";
        }
    }
    function thisFormReset(id) {
        var form = document.getElementById(id);
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            }
            else if (form.elements[i].type == "select-one") {
                if (form.elements[i].id != 'format' && form.elements[i].id != 'pageSize') {
                    form.elements[i].options.length = 0;
                }
            }
            else if (form.elements[i].type == "checkbox") {
                form.elements[i].checked = false;
            }
            else if (form.elements[i].type == "hidden" && (form.elements[i].id).substring(0, 6) != "report") {
                form.elements[i].value = "";
            }
        }
        document.getElementById("communicationsNumberTF").style.display = "none";
    }
</script>
<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="/Report/ContactSingleList/Execute.do" focus="parameter(addressId_Selected)"
               styleId="contactSingleList" styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Contact.Report.ContactSingleList"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                            <fmt:message key="Contact"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <%-- Used when coming from the contact or contact person detail. See Contact shortcuts which passes the params --%>
                            <c:if test="${not empty contactSingleReportListForm.params['addressId_Selected'] && empty contactSingleReportListForm.params['address']}">
                                <c:set target="${contactSingleReportListForm.params}" property="address"
                                       value="${app2:getAddressName(contactSingleReportListForm.params['addressId_Selected'])}"/>
                            </c:if>
                            <div class="input-group">
                                <app:text property="parameter(address)" styleId="fieldAddressName_id"
                                          styleClass="middleText form-control"
                                          maxlength="40" tabindex="1" readonly="true"/>
                                <html:hidden property="parameter(addressId_Selected)" styleId="fieldAddressId_id"/>
                            <span class="input-group-btn">
                                 <tags:bootstrapSelectPopup styleId="searchAddress_id"
                                                            url="/contacts/SearchAddress.do"
                                                            name="searchAddress"
                                                            isLargeModal="true"
                                                            titleKey="Contact.Title.search"
                                                            hide="false" submitOnSelect="true" tabindex="2"/>
                                 <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                 nameFieldId="fieldAddressName_id"
                                                                 titleKey="Common.clear"
                                                                 submitOnClear="true"
                                                                 tabindex="2"/>
                            </span>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="showCommunications_id">
                            <fmt:message key="Contact.Report.showCommunications"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="parameter(showCommunications)" value="true"
                                                   styleId="showCommunications_id"
                                                   onclick="changeCommunicationsRowConfigState(this)" tabindex="3"/>
                                    <label for="showCommunications_id"></label>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <c:if test="${not empty contactSingleReportListForm.params['addressId_Selected']}">
                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="contactPersonId_selected_id">
                                <fmt:message key="ProductCustomer.contactPerson"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <fanta:select property="parameter(contactPersonId_selected)" tabIndex="4"
                                              styleId="contactPersonId_selected_id"
                                              listName="searchContactPersonList"
                                              module="/contacts" firstEmpty="true" labelProperty="contactPersonName"
                                              valueProperty="contactPersonId"
                                              styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                              readOnly="${op == 'delete'}">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="addressId"
                                                     value="${contactSingleReportListForm.params['addressId_Selected']}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                </c:if>
                <c:choose>
                <c:when test="${contactSingleReportListForm.params['showCommunications']}">
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}" id="communicationsNumberTF">
                        </c:when>
                        <c:otherwise>
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}" style="display:none;"
                                 id="communicationsNumberTF">
                                </c:otherwise>
                                </c:choose>
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="communicationsLimit_id">
                                    <fmt:message key="ContactSingleReport.numberOfCommunications"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                    <app:numberText property="parameter(communicationsLimit)"
                                                    styleId="communicationsLimit_id"
                                                    maxInt="10" maxlength="10"
                                                    numberType="integer"
                                                    styleClass="numberText form-control" tabindex="5"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                            <c:set var="reportFormats" value="${contactSingleReportListForm.reportFormats}"
                                   scope="request"/>
                            <c:set var="pageSizes" value="${contactSingleReportListForm.pageSizes}" scope="request"/>

            </fieldset>
            <tags:bootstrapReportOptionsTag/>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="button ${app2:getFormButtonClasses()}" property="parameter(generate)"
                         tabindex="57"><fmt:message
                    key="Campaign.Report.generate"/></html:submit>
            <html:button property="reset1" tabindex="58" styleClass="button ${app2:getFormButtonClasses()}"
                         onclick="thisFormReset('contactSingleList')">
                <fmt:message
                        key="Common.clear"/></html:button>
        </div>
        <titus:reportInitializeConstantsTag/>

        <titus:reportTag id="constactSingleList" title="Contact.Report.ContactSingleList"
                         pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
    </html:form>
</div>

<tags:jQueryValidation formName="contactSingleReportListForm" isValidate="true" isAllValidation="false"/>
