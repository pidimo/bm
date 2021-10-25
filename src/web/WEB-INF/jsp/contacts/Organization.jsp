<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>

<tags:jscript language="JavaScript" src="/js/contacts/address.js"/>
<tags:jscript language="JavaScript" src="/js/contacts/userAddressAccessRight.js"/>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapFile/>

<html:form action="${action}"
           focus="${addressForm.dtoMap['addTelecom'] != null ? 'dto(telecomTypeId)' : 'dto(name1)'}"
           enctype="multipart/form-data"
           styleClass="form-horizontal">
    <div id="formPanel">

        <html:hidden property="dto(customerConfirmationFlag)" styleId="customerConfirmationFlagId"/>
        <html:hidden property="dto(supplierConfirmationFlag)" styleId="supplierConfirmationFlagId"/>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:choose>
                <c:when test="${onlyView !=null}">
                    <html:button property="" onclick="parent.hideBootstrapPopup();"
                                 styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.ok"/>
                    </html:button>
                </c:when>
                <c:otherwise>
                    <app2:securitySubmit operation="${op}" functionality="CONTACT" property="dto(save)"
                                         styleClass="${app2:getFormButtonClasses()}" styleId="saveButtonId"
                                         onclick="javascript:fillMultipleSelectValues();">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                    <app2:checkAccessRight functionality="CONTACT" permission="DELETE">
                        <c:if test="${op == 'update' && param.contactId != sessionScope.user.valueMap['companyId']}">
                            <html:submit property="dto(delete)" styleClass="${app2:getFormButtonClasses()}  cancel"
                                         onclick="javascript:fillMultipleSelectValues();">
                                <fmt:message key="Common.delete"/>
                            </html:submit>
                        </c:if>
                    </app2:checkAccessRight>
                    <c:if test="${op == 'create' || op == 'delete'}">
                        <c:choose>
                            <c:when test="${isCreatedByPopup}"> <%--creation from popup window--%>
                                <html:button property="" onclick="parent.hideBootstrapPopup();"
                                             styleClass="${app2:getFormButtonCancelClasses()}">
                                    <fmt:message key="Common.cancel"/>
                                </html:button>
                            </c:when>
                            <c:otherwise>
                                <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                                    <fmt:message key="Common.cancel"/>
                                </html:cancel>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="${app2:getFormPanelClasses()}">

                <%--<table id="Organization.jsp" border="0" cellpadding="0" cellspacing="0" width="800" align="center" class="container">--%>
            <html:hidden property="dto(op)" value="${op}"/>
            <html:hidden property="dto(addressType)" value="${organizationType}"/>
            <html:hidden property="dto(userTypeValue)" value="0"/>
            <c:if test="${op == 'create'}">
                <html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}"/>
            </c:if>
            <c:if test="${op == 'update'}">
                <html:hidden property="dto(lastModificationUserId)" value="${sessionScope.user.valueMap['userId']}"/>
                <jsp:useBean id="now" class="java.util.Date"/>
                <html:hidden property="dto(lastUpdateDate)" value="${now}"/>
                <html:hidden property="dto(version)"/>
                <html:hidden property="dto(recordUserId)"/>
            </c:if>
            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

            <c:if test="${'update' == op || op == 'delete'}">
                <html:hidden property="dto(addressId)"/>
            </c:if>
            <c:if test="${isCreatedByPopup}"> <%--for popup creation--%>
                <html:hidden property="dto(isCreatedByPopup)" value="true"/>
            </c:if>

            <fieldset>

                <legend class="title">
                    <c:choose>
                        <c:when test="${addressForm.dtoMap['code'] == 9 && addressForm.dtoMap['addressId'] == sessionScope.user.valueMap['companyId']}">
                            <fmt:message key="Common.company"/>
                        </c:when>
                        <c:otherwise>
                            <c:out value="${title}"/>
                        </c:otherwise>
                    </c:choose>
                </legend>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="name1_id">
                            <fmt:message key="Contact.Organization.name"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:text property="dto(name1)"
                                      styleClass="${app2:getFormInputClasses()} middleText"
                                      maxlength="60"
                                      tabindex="1"
                                      styleId="name1_id"
                                      view="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()} hidden-xs hidden-sm">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="searchName_id">
                            <fmt:message key="Contact.Organization.searchName"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <app:text property="dto(searchName)"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="60"
                                      tabindex="2"
                                      styleId="searchName_id"
                                      view="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="name2_id"></label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:text property="dto(name2)"
                                      styleClass="${app2:getFormInputClasses()} middleText"
                                      maxlength="60"
                                      tabindex="3"
                                      styleId="name2_id"
                                      view="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()} hidden-xs hidden-sm">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="keywords_id">
                            <fmt:message key="Contact.keywords"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <app:text property="dto(keywords)"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="50"
                                      tabindex="4"
                                      styleId="keywords_id"
                                      view="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="name3_id"></label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:text property="dto(name3)"
                                      styleClass="${app2:getFormInputClasses()} middleText"
                                      maxlength="60"
                                      tabindex="5"
                                      styleId="name3_id"
                                      view="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="languageId_id">
                            <fmt:message key="Contact.language"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <fanta:select property="dto(languageId)"
                                          listName="languageBaseList"
                                          firstEmpty="true"
                                          labelProperty="name"
                                          valueProperty="id"
                                          styleId="languageId_id"
                                          module="/catalogs"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          readOnly="${op == 'delete'}"
                                          tabIndex="6">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="countryId">
                            <fmt:message key="Contact.country"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <fanta:select property="countryId"
                                          listName="countryBasicList"
                                          firstEmpty="true"
                                          labelProperty="name"
                                          valueProperty="id"
                                          module="/catalogs"
                                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                                          tabIndex="7"
                                          readOnly="${op == 'delete'}"
                                          styleId="countryId">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="validFromId">
                            <fmt:message key="Contact.Organization.foundation"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <div class="input-group date">
                                <c:choose>
                                    <c:when test="${not empty addressForm.dtoMap['dateWithoutYear']}">
                                        <app:dateText property="dto(birthday)"
                                                      datePatternKey="withoutYearPattern"
                                                      view="${'delete' == op}"
                                                      styleId="validFromId"
                                                      styleClass="text ${app2:getFormInputClasses()} datepicker mediumText"
                                                      maxlength="10"
                                                      withOutYear="${true}"
                                                      tabindex="8"
                                                      mode="bootstrap"
                                                      convert="true"
                                                      calendarPicker="${op != 'delete'}"/>

                                    </c:when>
                                    <c:otherwise>
                                        <app:dateText property="dto(birthday)"
                                                      datePatternKey="datePattern"
                                                      view="${'delete' == op}"
                                                      styleId="validFromId"
                                                      styleClass="text ${app2:getFormInputClasses()} datepicker mediumText"
                                                      maxlength="10"
                                                      tabindex="8"
                                                      mode="bootstrap"
                                                      convert="true"
                                                      calendarPicker="${op != 'delete'}"/>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">

                    <div class="row threeSmallGrid col-xs-12 col-sm-12 col-md-6">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="zipId">
                            <fmt:message key="Contact.cityZip"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app2:checkAccessRight functionality="CITY" permission="VIEW" var="hasCityAccess"/>
                            <html:hidden property="cityId" styleId="cityId"/>
                            <html:hidden property="beforeZip" styleId="beforeZipId"/>
                            <div class="row">
                                <div class="col-xs-12 col-sm-3 wrapperButton">
                                    <app:text property="zip"
                                              styleClass="${app2:getFormInputClasses()} zipText"
                                              maxlength="10"
                                              titleKey="Contact.zip"
                                              tabindex="9"
                                              view="${op == 'delete'}"
                                              readonly="${!hasCityAccess}"
                                              styleId="zipId"/>
                                </div>
                                <div class="col-xs-12 col-sm-7 wrapperButton">
                                    <app:text property="city"
                                              styleClass="${app2:getFormInputClasses()} cityNameText"
                                              maxlength="40"
                                              titleKey="Contact.city"
                                              tabindex="10"
                                              view="${op == 'delete'}"
                                              readonly="${!hasCityAccess}"
                                              styleId="cityNameId"/>
                                </div>
                                <div class="col-xs-12 col-sm-2">
                                    <c:if test="${op != 'delete' && hasCityAccess}">
                                        <div class="pull-right">
                                            <tags:bootstrapSearchCity countryIdField="countryId"
                                                                      cityIdField="cityId"
                                                                      zipField="zipId"
                                                                      titleKey="City.Title.search"
                                                                      cityField="cityNameId"
                                                                      beforeZip="beforeZipId"
                                                                      message="empty"
                                                                      modalTitleKey="City.Title.search"
                                                                      iframeId="iframeId"
                                                                      submitOnSelect="true"
                                                                      tabIndex="10"/>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="isCustomer_id">
                            <fmt:message key="Contact.type"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <c:choose>
                                    <c:when test="${(addressForm.dtoMap['code'] != 9 && addressForm.dtoMap['addressId'] != sessionScope.user.valueMap['companyId'])}">
                                        <app2:checkAccessRight functionality="CUSTOMER" permission="CREATE"
                                                               var="hasCustomerCreate"/>
                                        <c:choose>
                                            <c:when test="${op == 'update' && addressForm.dtoMap['isCustomer']}">
                                                <div class="col-xs-12 col-sm-6">
                                                    <div class="radiocheck">
                                                        <div class="checkbox checkbox-default">
                                                            <html:checkbox styleClass="radio"
                                                                           property="dto(isCustomer)"
                                                                           styleId="isCustomer_id"
                                                                           disabled="${op == 'delete' || !hasCustomerCreate}"
                                                                           tabindex="11"/>
                                                            <label for="isCustomer_id"><fmt:message
                                                                    key="Contact.customer"/></label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="col-xs-12 col-sm-6">
                                                    <div class="radiocheck">
                                                        <div class="checkbox checkbox-default">
                                                            <html:checkbox styleClass="radio"
                                                                           property="dto(isCustomer)"
                                                                           tabindex="12"
                                                                           styleId="isCustomer_id"
                                                                           disabled="${op == 'delete' || !hasCustomerCreate}"/>
                                                            <label for="isCustomer_id"><fmt:message
                                                                    key="Contact.customer"/></label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:if test="${!hasCustomerCreate && addressForm.dtoMap['isCustomer']}">
                                            <html:hidden property="dto(isCustomer)"/>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="radiocheck">
                                                <div class="checkbox checkbox-default">
                                                    <html:checkbox styleClass="radio"
                                                                   property="dto(isCustomer)"
                                                                   styleId="isCustomer_id"
                                                                   disabled="true"/>
                                                    <label for="isCustomer_id"> <fmt:message
                                                            key="Contact.customer"/> </label>
                                                </div>
                                            </div>
                                        </div>
                                        <html:hidden property="dto(isCompany)" value="true"/>
                                        <c:if test="${addressForm.dtoMap['isCustomer']}">
                                            <html:hidden property="dto(isCustomer)"/>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${(addressForm.dtoMap['code'] != 9 && addressForm.dtoMap['addressId'] != sessionScope.user.valueMap['companyId'])}">
                                        <app2:checkAccessRight functionality="SUPPLIER" permission="CREATE"
                                                               var="hasSupplierCreate"/>
                                        <c:choose>
                                            <c:when test="${op == 'update' && addressForm.dtoMap['isSupplier']}">
                                                <div class="col-xs-12 col-sm-6">
                                                    <div class="radiocheck">
                                                        <div class="checkbox checkbox-default">
                                                            <html:checkbox styleClass="radio"
                                                                           property="dto(isSupplier)"
                                                                           styleId="isSupplier_id"
                                                                           disabled="${op == 'delete' || !hasSupplierCreate}"
                                                                           tabindex="13"/>
                                                            <label for="isSupplier_id"><fmt:message
                                                                    key="Contact.supplier"/></label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="col-xs-12 col-sm-6">
                                                    <div class="radiocheck">
                                                        <div class="checkbox checkbox-default">
                                                            <html:checkbox styleClass="radio"
                                                                           property="dto(isSupplier)"
                                                                           tabindex="14"
                                                                           styleId="isSupplier_id"
                                                                           disabled="${op == 'delete' || !hasSupplierCreate}"/>
                                                            <label for="isSupplier_id"><fmt:message
                                                                    key="Contact.supplier"/></label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:if test="${!hasSupplierCreate && addressForm.dtoMap['isSupplier']}">
                                            <html:hidden property="dto(isSupplier)"/>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="radiocheck">
                                                <div class="checkbox checkbox-default">
                                                    <html:checkbox styleClass="radio"
                                                                   property="dto(isSupplier)"
                                                                   styleId="isSupplier_id"
                                                                   disabled="true"/>
                                                    <label for="isSupplier_id"><fmt:message
                                                            key="Contact.supplier"/></label>
                                                </div>
                                            </div>
                                        </div>
                                        <html:hidden property="dto(isCompany)" value="true"/>
                                        <c:if test="${addressForm.dtoMap['isSupplier']}">
                                            <html:hidden property="dto(isSupplier)"/>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>

                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="street_id">
                            <fmt:message key="Contact.streetNumber"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-8 wrapperButton">
                                    <app:text property="dto(street)"
                                              styleClass="${app2:getFormInputClasses()} cityNameText"
                                              maxlength="40"
                                              titleKey="Contact.street"
                                              tabindex="15"
                                              styleId="street_id"
                                              view="${op == 'delete'}"/>
                                </div>
                                <c:choose>
                                    <c:when test="${op == 'update'}">
                                        <div class="col-xs-11 col-sm-3 wrapperButton">
                                            <app:text property="dto(houseNumber)"
                                                      styleClass="${app2:getFormInputClasses()} zipText"
                                                      maxlength="10"
                                                      titleKey="Contact.houseNumber"
                                                      tabindex="16"
                                                      view="${op == 'delete'}"/>
                                        </div>
                                        <c:import url="/WEB-INF/jsp/layout/tiles/map24.jsp"/>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="col-xs-12 col-sm-4 wrapperButton">
                                            <app:text property="dto(houseNumber)"
                                                      styleClass="${app2:getFormInputClasses()} zipText"
                                                      maxlength="10"
                                                      titleKey="Contact.houseNumber"
                                                      tabindex="16"
                                                      view="${op == 'delete'}"/>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="isActive_id">
                            <fmt:message key="Contact.status"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <c:if test="${op != 'create'}">
                                <c:choose>
                                    <c:when test="${(addressForm.dtoMap['code'] != 9 && addressForm.dtoMap['addressId'] != sessionScope.user.valueMap['companyId'])}">
                                        <div class="row col-xs-12 col-sm-6">
                                            <div class="radiocheck">
                                                <div class="checkbox checkbox-default">
                                                    <html:checkbox styleClass="radio"
                                                                   styleId="isActive_id"
                                                                   property="dto(isActive)"
                                                                   disabled="${op == 'delete'}"
                                                                   tabindex="17"/>
                                                    <label for="isActive_id"><fmt:message key="Contact.active"/></label>
                                                </div>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <html:hidden property="dto(isActive)"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="additionalAddressLine_id">
                            <fmt:message key="Contact.additionalAddressLine"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:text property="dto(additionalAddressLine)"
                                      styleClass="${app2:getFormInputClasses()} middleText"
                                      maxlength="100"
                                      styleId="additionalAddressLine_id"
                                      tabindex="18" view="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>


            </fieldset>

                <%--Telecoms area--%>
            <c:set var="lastTabIndex" value="21" scope="request"/>
            <c:set var="isType" value="contact" scope="request"/>
            <html:hidden property="initTelecoms"/>
            <c:set var="telecomItems" value="${addressForm.telecomMap}" scope="request"/>
            <c:import url="/WEB-INF/jsp/contacts/TelecomTile.jsp"/>

            <fieldset>

                <legend class="title">
                    <fmt:message key="Contact.additionalInfo"/>
                </legend>
                    <%--<div class="col-sm-12 col-md-8 col-md-offset-2">--%>

                <html:hidden property="dto(imageId)"/>
                <html:hidden property="dto(imageRemovedFlag)" styleId="imageRemovedFlagId"/>
                <c:set var="imageId" value="${addressForm.dtoMap['imageId']}"/>

                <div class="${app2:getFormGroupClasses()}">
                    <html:hidden property="dto(imageId)"/>
                    <html:hidden property="dto(imageRemovedFlag)" styleId="imageRemovedFlagId"/>
                    <c:set var="imageId" value="${addressForm.dtoMap['imageId']}"/>

                    <div class="${app2:getFormLabelRenderCategory()}">

                        <c:if test="${imageId != null && imageId != '' && addressForm.dtoMap['imageRemovedFlag']!='true'}">
                            <td class="topLabel" style="border: 0px;" width="135" nowrap>
                                <div id="imageDivId">
                                        <%--&nbsp;<br>--%>
                                    <html:img page="/DownloadAddressImage.do?dto(freeTextId)=${imageId}"
                                              styleClass="img-responsive img-thumbnail"
                                              styleId="imageId"
                                              width="110px"/>
                                    <c:if test="${op != 'delete'}">
                                        <a href="javascript:hideImageDiv();" class="clearfix marginTop">
                                <span title="<fmt:message key="Contact.removePhoto"/>"
                                      class="glyphicon glyphicon-trash" align="top">
                                </span>
                                        </a>
                                    </c:if>
                                </div>
                            </td>
                        </c:if>
                    </div>

                    <c:if test="${'delete' != op}">
                        <div class="${app2:getFormContainRenderCategory(null)}">
                            <label class="control-label">
                                <fmt:message key="Contact.photo"/>
                            </label>
                            <tags:bootstrapFile property="imageFile"
                                                accept="gif, jpeg"
                                                tabIndex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                                styleId="file_id"
                                                glyphiconClass="glyphicon-picture"/>
                                    <span>
                                        <fmt:message key="Common.fileUpload.info">
                                            <fmt:param value="200 KB"/>
                                            <fmt:param value="gif, jpeg, png"/>
                                        </fmt:message>
                                    </span>
                            <span class="glyphicon form-control-feedback iconValidation centerIconValidation"></span>
                        </div>
                    </c:if>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="note_id">
                        <fmt:message key="Contact.notes"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(op == 'delete')}">
                        <html:textarea property="dto(note)"
                                       styleId="note_id"
                                       styleClass="${app2:getFormInputClasses()} mediumDetailHigh"
                                       tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                       readonly="${op == 'delete'}"
                                       style="height:110px;"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <app2:checkAccessRight functionality="WAYDESCRIPTIONVIEW" permission="VIEW" var="hasWayDescrViewPermission"/>
                <c:choose>
                    <c:when test="${hasWayDescrViewPermission}">
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelRenderCategory()}" for="wayDescription_id">
                                <fmt:message key="Contact.wayDescription"/>
                            </label>
                            <div class="${app2:getFormContainRenderCategory(op == 'delete')}">
                                <html:textarea property="dto(wayDescription)"
                                               styleId="wayDescription_id"
                                               styleClass="${app2:getFormInputClasses()} mediumDetailHigh"
                                               tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                               readonly="${op == 'delete'}"
                                               style="height:110px;"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <%--this is to maintain old values of wayDescription--%>
                        <html:hidden property="dto(wayDescription)"/>
                    </c:otherwise>
                </c:choose>

                    <%--</div>--%>
                <div>
                    <!--added by ivan-->
                    <c:set var="elementCounter" value="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                           scope="request"/>
                    <c:set var="ajaxAction" value="/contacts/MainPageReadSubCategories.do" scope="request"/>
                    <c:set var="downloadAction"
                           value="/contacts/MainPage/Organization/DownloadCategoryFieldValue.do?dto(addressId)=${param.contactId}"
                           scope="request"/>
                    <c:set var="formName" value="addressForm" scope="request"/>
                    <c:set var="table" value="<%=ContactConstants.ADDRESS_CATEGORY%>" scope="request"/>
                    <c:set var="secondTable" value="<%=ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY%>"
                           scope="request"/>
                    <c:set var="operation" value="${op}" scope="request"/>
                    <c:set var="labelWidth" value="15" scope="request"/>
                    <c:set var="containWidth" value="85" scope="request"/>
                    <c:set var="generalWidth" value="${250}" scope="request"/>
                    <c:import url="/WEB-INF/jsp/catalogs/CategoryUtil.jsp"/>
                    <c:set var="lastTabIndex" value="${elementCounter+1}" scope="request"/>
                </div>


            </fieldset>


            <fieldset>
                    <%--data level access rights security--%>
                <legend class="title">
                    <fmt:message key="Contact.accessRight.security"/>
                </legend>

                <div>
                    <div class="form-group">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Contact.accessRight.userGroupsWithAccess"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <html:hidden property="dto(accessUserGroupIds)" styleId="accessUserGroupIds_Id"/>

                                ${app2:buildAddressUserGroupAccessRightValues(addressForm.dtoMap['accessUserGroupIds'], pageContext.request)}
                            <c:set var="selectedElms" value="${selectedUserGroupList}"/>
                            <c:set var="availableElms" value="${availableUserGroupList}"/>
                            <table class="col-xs-12">
                                <tr>
                                    <td width="47.5%"><fmt:message key="common.selected"/></td>
                                    <td width="5%"></td>
                                    <td width="47.5%"><fmt:message key="common.available"/></td>
                                </tr>
                                <tr>
                                    <td>
                                        <html:select property="dto(selectedUserGroup)"
                                                     styleId="selectedUserGroup_Id"
                                                     multiple="true"
                                                     styleClass="${app2:getFormSelectClasses()} middleMultipleSelect"
                                                     tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                                     disabled="${op == 'delete'}">

                                            <html:options collection="selectedElms" property="value"
                                                          labelProperty="label"/>
                                        </html:select>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </td>
                                    <td>
                                        <html:button property="select"
                                                     onclick="javascript:moveOptionSelected('availableUserGroup_Id','selectedUserGroup_Id');"
                                                     styleClass="${app2:getFormButtonClasses()} adminLeftArrow"
                                                     titleKey="Common.add"
                                                     disabled="${op == 'delete'}"><
                                        </html:button>
                                        <br>
                                        <br>
                                        <html:button property="select"
                                                     onclick="javascript:moveOptionSelected('selectedUserGroup_Id','availableUserGroup_Id');"
                                                     styleClass="${app2:getFormButtonClasses()} adminRighttArrow"
                                                     titleKey="Common.delete"
                                                     disabled="${op == 'delete'}">>
                                        </html:button>
                                    </td>
                                    <td>
                                        <html:select property="dto(availableUserGroup)"
                                                     styleId="availableUserGroup_Id"
                                                     multiple="true"
                                                     styleClass="${app2:getFormSelectClasses()} middleMultipleSelect"
                                                     tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                                     disabled="${op == 'delete'}">

                                            <html:options collection="availableElms" property="value"
                                                          labelProperty="label"/>
                                        </html:select>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </td>
                                </tr>
                            </table>
                            <script type="text/javascript">
                                fillUserAddressAccessRight();
                            </script>
                        </div>
                    </div>
                </div>

            </fieldset>

                <%--audit info--%>
            <c:if test="${op != 'create'}">
                <c:set var="auditAddressId" value="${addressForm.dtoMap['addressId']}" scope="request"/>
                <c:import url="/WEB-INF/jsp/contacts/AuditInformationFragment.jsp"/>
            </c:if>

        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:choose>
                <c:when test="${onlyView !=null}">
                    <html:button property="" onclick="parent.hideBootstrapPopup();"
                                 styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.ok"/>
                    </html:button>
                </c:when>
                <c:otherwise>
                    <app2:securitySubmit operation="${op}" functionality="CONTACT" property="dto(save)"
                                         styleClass="${app2:getFormButtonClasses()}"
                                         tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                         onclick="javascript:fillMultipleSelectValues();">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                    <app2:checkAccessRight functionality="CONTACT" permission="DELETE">
                        <c:if test="${op == 'update' && param.contactId != sessionScope.user.valueMap['companyId']}">
                            <html:submit property="dto(delete)" styleClass="${app2:getFormButtonClasses()}  cancel"
                                         tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                         onclick="javascript:fillMultipleSelectValues();">
                                <fmt:message key="Common.delete"/>
                            </html:submit>
                        </c:if>
                    </app2:checkAccessRight>
                    <c:if test="${op == 'create' || op == 'delete'}">
                        <c:choose>
                            <c:when test="${isCreatedByPopup}"> <%--creation from popup window--%>
                                <html:button property="" onclick="parent.hideBootstrapPopup();"
                                             tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                             styleClass="${app2:getFormButtonCancelClasses()}">
                                    <fmt:message key="Common.cancel"/>
                                </html:button>
                            </c:when>
                            <c:otherwise>
                                <html:cancel styleClass="${app2:getFormButtonCancelClasses()}"
                                             tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                    <fmt:message key="Common.cancel"/>
                                </html:cancel>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <c:if test="${duplicatedAddressesList != null}">
        <div id="duplicatedListPanel">

            <div class="wrapperButton">
                <app2:checkAccessRight functionality="CONTACT" permission="CREATE">
                    <html:submit property="dto(save)" styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.saveAnyway"/>
                    </html:submit>
                </app2:checkAccessRight>

                <html:button property="return" styleClass="${app2:getFormButtonClasses()}" onclick="showAddressForm();">
                    <fmt:message key="Common.edit"/>
                </html:button>

                <c:choose>
                    <c:when test="${isCreatedByPopup}"> <%--creation from popup window--%>
                        <html:button property="" onclick="parent.hideBootstrapPopup();"
                                     styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.cancel"/>
                        </html:button>
                    </c:when>
                    <c:otherwise>
                        <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                            <fmt:message key="Common.cancel"/>
                        </html:cancel>
                    </c:otherwise>
                </c:choose>

                <html:hidden property="confirmDuplicatedCreation" value="true"
                             styleId="confirmDuplicatedCreationId"/>
            </div>
            <c:import url="/WEB-INF/jsp/contacts/DuplicatedAddressesList.jsp"/>
        </div>
    </c:if>


</html:form>
<tags:jQueryValidation formName="addressForm"/>
