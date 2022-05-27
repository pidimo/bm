<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapFile/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>

<app2:jScriptUrl url="/contacts/ContactPerson/Ajax/Select.do?op=${op}" var="jsContactPersonUrl"
                 addModuleParams="false">
    <app2:jScriptUrlParam param="addressId" value="address_id"/>
</app2:jScriptUrl>


<script type="text/javascript">

    function searchContactPerson(objSelect) {
        var address_id = objSelect.value;
        makePOSTAjaxContactPersonRequest(${jsContactPersonUrl}, "");
    }


    function makePOSTAjaxContactPersonRequest(urlAddress, parameters) {
        $.ajax({
            async: true,
            type: "POST",
            dataType: "html",
            data: parameters,
            url: urlAddress,
            beforeSend: setAjaxLoadMessage,
            success: function (data) {
                setContactPersonHtmlSelect(data);
                hideAjaxMessage();
            },
            error: function (ajaxRequest) {
                ajaxErrorProcess(ajaxRequest.status);
            }
        });
    }

    function setContactPersonHtmlSelect(data) {
        document.getElementById('contactPersonSelectDiv').innerHTML = data;
    }

    function hideAjaxMessage() {
        var messagesDiv = document.getElementById('ajaxMessageId');
        messagesDiv.style.display = 'none';
    }

    function setAjaxLoadMessage() {
        var messagesDiv = document.getElementById('ajaxMessageId');
        messagesDiv.innerHTML = '${app2:buildAJAXMessage('Common.message.loading',pageContext.request)}';
        messagesDiv.style.display = 'inline';
    }

    function ajaxErrorProcess(status) {
        var messagesDiv = document.getElementById('ajaxMessageId');
        if (status == 404 || status == 302) {
            messagesDiv.innerHTML = '${app2:buildAJAXMessage('Common.sessionExpired',pageContext.request)}';
        } else {
            messagesDiv.innerHTML = '${app2:buildAJAXMessage('error.tooltip.unexpected',pageContext.request)}';
        }
        messagesDiv.style.display = 'inline';
    }

</script>
<html:form action="${action}" focus="dto(number)" enctype="multipart/form-data" styleClass="form-horizontal">
    <div class="${app2:getFormClassesLarge()}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="update" functionality="CUSTOMER" property="dto(save)"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 tabindex="2"
                                 onclick="javascript:fillMultipleSelectValues();">
                <c:out value="${save}"/>
            </app2:securitySubmit>
                <%--top links--%>
            <app2:checkAccessRight functionality="CATEGORYTAB" permission="VIEW">
                <app2:categoryTabLink id="customerCategoryLinkId"
                                      action="/contacts/Customer/CategoryTab/Forward/Update.do?index=${param.index}&contactId=${param.contactId}&dto(addressId)=${param.contactId}"
                                      categoryConstant="1"
                                      finderName="findValueByCustomerId"
                                      styleClass="folderTabLink btn btn-link">
                    <app2:categoryFinderValue forId="customerCategoryLinkId" value="${param.contactId}"/>
                </app2:categoryTabLink>
            </app2:checkAccessRight>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <html:hidden property="dto(locale)" value="${sessionScope.user.valueMap['locale']}"/>
                <html:hidden property="dto(customerId)" value="${param.contactId}"/>
                <html:hidden property="dto(addressId)" value="${param.contactId}"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                <html:hidden property="dto(addressType)" value="${addressType}"/>
                <html:hidden property="dto(name1)" value="${name1}"/>
                <html:hidden property="dto(name2)" value="${name2}"/>
                <html:hidden property="dto(name3)" value="${name3}"/>
                <html:hidden property="dto(version)"/>
                <html:hidden property="dto(op)" value="update"/>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="number_id">
                            <fmt:message key="Customer.number"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <html:text property="dto(number)"
                                       styleId="number_id"
                                       styleClass="numberText form-control"
                                       maxlength="20"
                                       tabindex="4"
                                       readonly="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="customerTypeId_id">
                            <fmt:message key="Customer.type"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <app:catalogSelect property="dto(customerTypeId)" styleId="customerTypeId_id"
                                               catalogTable="customertype" idColumn="customertypeid"
                                               labelColumn="customertypename"
                                               styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="5"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="priorityId_id">
                            <fmt:message key="Customer.priority"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <fanta:select property="dto(priorityId)"
                                          listName="priorityList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          styleId="priorityId_id"
                                          readOnly="${op == 'delete'}"
                                          module="/catalogs"
                                          firstEmpty="true"
                                          tabIndex="6">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="type" value="CUSTOMER"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="branchId_id">
                            <fmt:message key="Customer.branch"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <app:catalogSelect property="dto(branchId)"
                                               styleId="branchId_id"
                                               catalogTable="branch"
                                               idColumn="branchid"
                                               labelColumn="branchname"
                                               styleClass="largeSelect ${app2:getFormSelectClasses()}"
                                               tabindex="7"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="sourceId_id">
                            <fmt:message key="Customer.source"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <app:catalogSelect property="dto(sourceId)"
                                               styleId="sourceId_id"
                                               catalogTable="addresssource"
                                               idColumn="sourceid"
                                               labelColumn="sourcename"
                                               styleClass="largeSelect ${app2:getFormSelectClasses()}"
                                               tabindex="8"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="payConditionId_id">
                            <fmt:message key="Customer.payCondition"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <app:catalogSelect property="dto(payConditionId)"
                                               styleId="payConditionId_id"
                                               catalogTable="paycondition"
                                               idColumn="payconditionid"
                                               labelColumn="conditionname"
                                               styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                               tabindex="9"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="payMoralityId_id">
                            <fmt:message key="Customer.payMorality"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <app:catalogSelect property="dto(payMoralityId)"
                                               styleId="payMoralityId_id"
                                               catalogTable="paymorality"
                                               idColumn="paymoralityid"
                                               labelColumn="paymoralityname"
                                               styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                               tabindex="10"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="employeeId_id">
                            <fmt:message key="Customer.employee"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <fanta:select property="dto(employeeId)"
                                          styleId="employeeId_id"
                                          listName="employeeBaseList"
                                          labelProperty="employeeName"
                                          valueProperty="employeeId"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          readOnly="${op == 'delete'}"
                                          module="/contacts"
                                          tabIndex="11"
                                          firstEmpty="true">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="expectedTurnOver_id">
                            <fmt:message key="Customer.expectedTurnOver"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <app:numberText property="dto(expectedTurnOver)"
                                            styleClass="numberText form-control"
                                            styleId="expectedTurnOver_id"
                                            maxlength="14"
                                            view="${'delete' == op}"
                                            numberType="decimal"
                                            tabindex="12"
                                            maxInt="11"
                                            maxFloat="2"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <c:if test="${addressType == organizationType}">
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelRenderCategory()}" for="numberOfEmployees_id">
                                <fmt:message key="Customer.numberOfEmpoyees"/>
                            </label>

                            <div class="${app2:getFormContainRenderCategory(null)}">
                                <html:text property="dto(numberOfEmployees)"
                                           styleId="numberOfEmployees_id"
                                           styleClass="numberText form-control" maxlength="6"
                                           tabindex="15"
                                           readonly="${op == 'delete'}" style="text-align:right"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelRenderCategory()}" for="taxNumber_id">
                                <fmt:message key="Contact.taxNumber"/>
                            </label>

                            <div class="${app2:getFormContainRenderCategory(null)}">
                                <html:text property="dto(taxNumber)" styleId="taxNumber_id"
                                           styleClass="numberText form-control"
                                           maxlength="20"
                                           tabindex="14"
                                           readonly="${op == 'delete'}"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </c:if>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="searchPartner_id">
                            <fmt:message key="Customer.partner"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <html:hidden property="dto(partnerId)" styleId="fieldAddressId_id"/>
                            <div class="input-group">
                                <app:text property="dto(partnerName)" styleClass="mediumText form-control"
                                          maxlength="40"
                                          readonly="true"
                                          tabindex="14"
                                          view="${op =='delete'}" styleId="fieldAddressName_id"/>
                        <span class="input-group-btn">
                            <tags:bootstrapSelectPopup styleId="searchPartner_id"
                                                       url="/contacts/SearchAddress.do"
                                                       name="searchPartner"
                                                       isLargeModal="true"
                                                       titleKey="Common.search"
                                                       modalTitleKey="Contact.Title.search"
                                                       hide="${op == 'delete'}"/>
                            <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                            nameFieldId="fieldAddressName_id"
                                                            titleKey="Common.clear" hide="${op == 'delete'}"/>
                        </span>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="invoiceAddressId_id">
                            <fmt:message key="Customer.invoiceAddress"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <fanta:select property="dto(invoiceAddressId)" listName="invoiceAddressRelationList"
                                          labelProperty="relatedAddressName" valueProperty="relatedAddressId"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          styleId="invoiceAddressId_id"
                                          readOnly="${op == 'delete'}"
                                          onChange="searchContactPerson(this)"
                                          module="/contacts" tabIndex="15" firstEmpty="true">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="addressId" value="${param.contactId}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="invoiceContactPersonId_id">
                            <fmt:message key="Customer.invoiceContactPerson"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <div id="ajaxMessageId" class="messageToolTip" style="display:none; position:absolute; ">
                                <fmt:message key="Common.message.loading"/>
                            </div>
                            <div id="contactPersonSelectDiv">
                                <fanta:select property="dto(invoiceContactPersonId)" listName="searchContactPersonList"
                                              module="/contacts" firstEmpty="true"
                                              labelProperty="contactPersonName"
                                              valueProperty="contactPersonId"
                                              styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                              styleId="invoiceContactPersonId_id"
                                              tabIndex="15"
                                              readOnly="${'delete' == op}">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="addressId"
                                                     value="${not empty customerForm.dtoMap['invoiceAddressId']?customerForm.dtoMap['invoiceAddressId']:0}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="additionalAddressId_id">
                            <fmt:message key="Customer.invoiceAdditionalAddress"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <fanta:select property="dto(additionalAddressId)" listName="additionalAddressSelectList"
                                          labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                                          preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                                          firstEmpty="true" styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          styleId="additionalAddressId_id"
                                          readOnly="${'delete' == op}" tabIndex="15">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="addressId"
                                                 value="${not empty param.contactId ? param.contactId : 0}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="invoiceShipping_id">
                            <fmt:message key="Customer.invoiceShipping"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <c:set var="invoiceShippingTypes" value="${app2:getCustomerInvoiceShippingList(pageContext.request)}"/>
                            <html:select property="dto(invoiceShipping)" styleId="invoiceShipping_id"
                                         styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="15"
                                         readonly="${'delete' == op}">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="invoiceShippingTypes" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
					<div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="referenceId">
                            <fmt:message key="Customer.referenceid"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <html:text property="dto(referenceId)"
                                            styleId="referenceId"
                                            styleClass="numberText form-control"
                                            maxlength="40"
                                            tabindex="15"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="discount_name">
                            <fmt:message key="Customer.defaultDiscount"/>
                            (<c:out value="%"/>)
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <app:numberText property="dto(defaultDiscount)"
                                            styleId="discount_name"
                                            styleClass="numberText form-control"
                                            numberType="decimal"
                                            maxInt="10"
                                            maxFloat="2"
                                            view="${op == 'delete'}"
                                            tabindex="16"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <c:set var="elementCounter" value="${15}" scope="request"/>
                    <c:set var="ajaxAction" value="/contacts/Customer/MainPageReadSubCategories.do" scope="request"/>
                    <c:set var="downloadAction"
                           value="/contacts/Customer/MainPage/DownloadCategoryFieldValue.do?dto(addressId)=${param.contactId}"
                           scope="request"/>
                    <c:set var="formName" value="customerForm" scope="request"/>
                    <c:set var="table" value="<%=ContactConstants.CUSTOMER_CATEGORY%>" scope="request"/>
                    <c:set var="operation" value="update" scope="request"/>
                    <c:set var="labelWidth" value="20" scope="request"/>
                    <c:set var="containWidth" value="80" scope="request"/>
                    <c:set var="generalWidth" value="${200}" scope="request"/>
                    <c:import url="/WEB-INF/jsp/catalogs/CategoryUtil.jsp"/>
                    <c:set var="lastTabIndex" value="${elementCounter+1}" scope="request"/>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="update" functionality="CUSTOMER" property="dto(save)"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 tabindex="${tabindex+1}"
                                 onclick="javascript:fillMultipleSelectValues();">
                <c:out value="${save}"/>
            </app2:securitySubmit>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="customerForm" isValidate="true"/>

