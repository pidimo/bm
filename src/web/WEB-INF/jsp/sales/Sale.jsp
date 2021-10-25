<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>

<script type="text/javascript">
    function reSubmit() {
        document.forms[0].submit();
    }
</script>
<fmt:message var="datePattern" key="datePattern"/>
<html:form action="${action}" focus="dto(title)" styleClass="form-horizontal">
    <div class="${app2:getFormClassesLarge()}">
        <html:hidden property="dto(op)" value="${op}"/>

        <c:if test="${'update'== op || 'delete'== op}">
            <html:hidden property="dto(showNetGrossWarningMessage)" styleId="showNetGrossWarningMessageId"/>
            <html:hidden property="dto(saleId)"/>
        </c:if>

        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>

        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>

        <c:if test="${null != saleForm.dtoMap['processId']}">
            <html:hidden property="dto(processId)"/>
        </c:if>

        <c:if test="${null != saleForm.dtoMap['contactId']}">
            <html:hidden property="dto(contactId)"/>
        </c:if>

        <div class="row">
            <div class="col-xs-12">
                <app2:securitySubmit operation="${op}" functionality="SALE" property="save"
                                     styleClass="button ${app2:getFormButtonClasses()} marginButton"
                                     tabindex="14">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${'update' == op}">
                    <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                        <html:submit property="invoiceFromSale" styleClass="button ${app2:getFormButtonClasses()} marginButton"
                                     tabindex="15">
                            <fmt:message key="Sale.invoice"/>
                        </html:submit>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                        <c:set var="invoiceInfo"
                               value="${app2:getOnlyOneSaleInvoiceInfoMap(param.saleId, pageContext.request)}"/>

                        <c:choose>
                            <c:when test="${invoiceInfo.saleInvoiceSize > 1}">
                                <app:url var="urlSaleInvoiceList"
                                         value="/Sale/Invoiced/List.do?tabKey=Sale.Tab.saleInvoices"/>
                                <html:button property="" styleClass="button ${app2:getFormButtonClasses()} marginButton" tabindex="16"
                                             onclick="location.href='${urlSaleInvoiceList}'">
                                    <fmt:message key="Sale.viewInvoices"/>
                                </html:button>
                            </c:when>
                            <c:when test="${not empty invoiceInfo.documentId}">
                                <app:url var="urlDownloadDocument"
                                         value="/finance/Download/Invoice/Document.do?dto(freeTextId)=${invoiceInfo.documentId}&dto(invoiceId)=${invoiceInfo.invoiceId}"
                                         contextRelative="true"/>
                                <html:button property="" styleClass="button ${app2:getFormButtonClasses()} marginButton" tabindex="16"
                                             onclick="location.href='${urlDownloadDocument}'">
                                    <fmt:message key="Sale.openInvoice"/>
                                </html:button>
                            </c:when>
                        </c:choose>
                    </app2:checkAccessRight>
                </c:if>

                <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()} marginButton" tabindex="17">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="title_id">
                        <fmt:message key="Sale.title"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <app:text property="dto(title)"
                                  styleId="title_id"
                                  styleClass="middleText ${app2:getFormInputClasses()}"
                                  maxlength="40" tabindex="1" view="${'delete' == op}"/>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <c:if test="${null != saleForm.dtoMap['contactId'] && null != saleForm.dtoMap['processId']}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="contactId_id">
                            <fmt:message key="Sale.salesProcessAction"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(true)}">
                            <fanta:select property="dto(contactId)"
                                          styleId="contactId_id"
                                          listName="actionSelectList"
                                          module="/sales"
                                          firstEmpty="true"
                                          labelProperty="note"
                                          valueProperty="contactId"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                          readOnly="true">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="processId" value="${saleForm.dtoMap['processId']}"/>
                            </fanta:select>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="fieldAddressName_id">
                        <fmt:message key="Sale.customer"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <c:if test="${not empty saleForm.dtoMap['addressId'] and 'update' == op}">
                            <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                                <c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
                                </c:set>
                                <c:set var="addressMap" value="${app2:getAddressMap(saleForm.dtoMap['addressId'])}"/>
                                <c:choose>
                                    <c:when test="${personType == addressMap['addressType']}">
                                        <c:set var="addressEditLink"
                                               value="/contacts/Person/Forward/Update.do?contactId=${saleForm.dtoMap['addressId']}&dto(addressId)=${saleForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="addressEditLink"
                                               value="/contacts/Organization/Forward/Update.do?contactId=${saleForm.dtoMap['addressId']}&dto(addressId)=${saleForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                                    </c:otherwise>
                                </c:choose>
                            </app2:checkAccessRight>
                        </c:if>

                        <div class="input-group">
                            <app:text property="dto(addressName)"
                                      styleClass="middleText ${app2:getFormInputClasses()}" maxlength="40"
                                      readonly="true"
                                      styleId="fieldAddressName_id" view="${'delete' == op}" tabindex="2"/>
                            <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>

                        <span class="input-group-btn">
                             <c:if test="${not empty addressEditLink}">
                                 <app:link action="${addressEditLink}" contextRelative="true" titleKey="Common.edit"
                                           tabindex="2" styleClass="${app2:getFormButtonClasses()}">
                                     <span class="glyphicon glyphicon-edit"></span>
                                 </app:link>
                             </c:if>
                            <tags:bootstrapSelectPopup url="/contacts/SearchAddress.do"
                                                       name="searchAddress"
                                                       styleId="searchAddress_id"
                                                       titleKey="Common.search"
                                                       modalTitleKey="Contact.Title.search"
                                                       isLargeModal="true"
                                                       submitOnSelect="true"
                                                       tabindex="3" hide="${'delete' == op}"/>
                            <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                            nameFieldId="fieldAddressName_id"
                                                            name="searchAddress"
                                                            titleKey="Common.clear"
                                                            submitOnClear="true"
                                                            tabindex="4" hide="${'delete' == op}"/>
                        </span>
                        </div>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="contactPersonId_id">
                        <fmt:message key="Sale.contactPerson"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <fanta:select property="dto(contactPersonId)"
                                      styleId="contactPersonId_id"
                                      listName="searchContactPersonList"
                                      module="/contacts" firstEmpty="true"
                                      labelProperty="contactPersonName"
                                      valueProperty="contactPersonId"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}" tabIndex="5"
                                      readOnly="${'delete' == op}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId"
                                             value="${not empty saleForm.dtoMap['addressId']?saleForm.dtoMap['addressId']:0}"/>
                        </fanta:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="sellerId_id">
                        <fmt:message key="Sale.seller"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <fanta:select property="dto(sellerId)"
                                      styleId="sellerId_id"
                                      listName="employeeBaseList"
                                      labelProperty="employeeName"
                                      valueProperty="employeeId"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      value="${sessionScope.user.valueMap['userAddressId']}"
                                      module="/contacts"
                                      tabIndex="6"
                                      readOnly="${'delete' == op}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="sentAddressId_id">
                        <fmt:message key="Sale.sentAddress"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <fanta:select property="dto(sentAddressId)"
                                      styleId="sentAddressId_id"
                                      listName="invoiceAddressRelationList"
                                      labelProperty="relatedAddressName" valueProperty="relatedAddressId"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      readOnly="${op == 'delete'}"
                                      onChange="reSubmit()"
                                      module="/contacts" tabIndex="6" firstEmpty="true">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId"
                                             value="${not empty saleForm.dtoMap['addressId'] ? saleForm.dtoMap['addressId'] : 0}"/>
                        </fanta:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="sentContactPersonId_id">
                        <fmt:message key="Sale.sentContactPerson"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <fanta:select property="dto(sentContactPersonId)"
                                      styleId="sentContactPersonId_id"
                                      listName="searchContactPersonList"
                                      module="/contacts" firstEmpty="true"
                                      labelProperty="contactPersonName"
                                      valueProperty="contactPersonId"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      tabIndex="6"
                                      readOnly="${'delete' == op}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId"
                                             value="${not empty saleForm.dtoMap['sentAddressId'] ? saleForm.dtoMap['sentAddressId'] : 0}"/>
                        </fanta:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="additionalAddressId_id">
                        <fmt:message key="Sale.additionalAddress"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <fanta:select property="dto(additionalAddressId)"
                                      styleId="additionalAddressId_id"
                                      listName="additionalAddressSelectList"
                                      labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                                      preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                                      firstEmpty="true" styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      readOnly="${'delete' == op}" tabIndex="6">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId"
                                             value="${not empty saleForm.dtoMap['addressId'] ? saleForm.dtoMap['addressId'] : 0}"/>
                        </fanta:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>


                <c:if test="${not empty saleForm.dtoMap['processId']}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="processName_id">
                            <fmt:message key="Sale.salesProcess"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(true)}">
                            <div class="row col-xs-11">
                                <app:text property="dto(processName)"
                                          styleId="processName_id"
                                          styleClass="middleText ${app2:getFormInputClasses()}" maxlength="40"
                                          view="true"/>
                            </div>
                            <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
                                <c:if test="${'update' == op && not empty saleForm.dtoMap['processId']}">
                                    <c:set var="processEditLink"
                                           value="/sales/SalesProcess/Forward/Update.do?processId=${saleForm.dtoMap['processId']}&dto(processId)=${saleForm.dtoMap['processId']}&dto(processName)=${app2:encode(saleForm.dtoMap['processName'])}&addressId=${saleForm.dtoMap['addressId']}&index=0"/>
                                    <span class="pull-right">
                                        <app:link action="${processEditLink}" contextRelative="true"
                                                  titleKey="Common.edit">
                                            <span class="glyphicon glyphicon-edit"></span>
                                        </app:link>
                                    </span>
                                </c:if>
                            </app2:checkAccessRight>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="startDate">
                        <fmt:message key="Sale.saleDate"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <div class="input-group date">
                            <app:dateText property="dto(saleDate)"
                                          styleId="startDate"
                                          calendarPicker="${op != 'delete'}"
                                          datePatternKey="${datePattern}"
                                          styleClass="text ${app2:getFormInputClasses()}"
                                          maxlength="10"
                                          mode="bootstrap"
                                          tabindex="7"
                                          currentDate="true"
                                          view="${'delete' == op}"/>
                        </div>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="currencyId_id">
                        <fmt:message key="Sale.currency"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <fanta:select property="dto(currencyId)"
                                      styleId="currencyId_id"
                                      listName="basicCurrencyList"
                                      labelProperty="name"
                                      valueProperty="id"
                                      firstEmpty="true"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      module="/catalogs"
                                      readOnly="${op == 'delete'}"
                                      tabIndex="7">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="netGross_id">
                        <fmt:message key="Sale.netGross"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete'== op)}">
                        <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
                        <html:select property="dto(netGross)"
                                     styleId="netGross_id"
                                     styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                     readonly="${'delete' == op}"
                                     tabindex="8">
                            <html:option value=""/>
                            <html:options collection="netGrossOptions" property="value" labelProperty="label"/>
                        </html:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="text_id">
                        <fmt:message key="Sale.text"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete'== op)}">
                        <html:textarea property="dto(text)"
                                       styleId="text_id"
                                       styleClass="mediumDetailHigh ${app2:getFormInputClasses()}"
                                       rows="5"
                                       readonly="${'delete'== op}"
                                       tabindex="9"/>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">

        </div>
        <div class="row">
            <div class="col-xs-12">
                <app2:securitySubmit operation="${op}" functionality="SALE" property="save"
                                     styleClass="button ${app2:getFormButtonClasses()} marginButton"
                                     tabindex="10">
                    ${button}
                </app2:securitySubmit>
                <c:if test="${'update' == op}">
                    <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                        <html:submit property="invoiceFromSale" styleClass="button ${app2:getFormButtonClasses()} marginButton"
                                     tabindex="11">
                            <fmt:message key="Sale.invoice"/>
                        </html:submit>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                        <c:choose>
                            <c:when test="${invoiceInfo.saleInvoiceSize > 1}">
                                <html:button property="" styleClass="button ${app2:getFormButtonClasses()} marginButton" tabindex="12"
                                             onclick="location.href='${urlSaleInvoiceList}'">
                                    <fmt:message key="Sale.viewInvoices"/>
                                </html:button>
                            </c:when>
                            <c:when test="${not empty invoiceInfo.documentId}">
                                <html:button property="" styleClass="button ${app2:getFormButtonClasses()} marginButton" tabindex="12"
                                             onclick="location.href='${urlDownloadDocument}'">
                                    <fmt:message key="Sale.openInvoice"/>
                                </html:button>
                            </c:when>
                        </c:choose>
                    </app2:checkAccessRight>

                </c:if>

                <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()} marginButton" tabindex="13">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="saleForm"/>