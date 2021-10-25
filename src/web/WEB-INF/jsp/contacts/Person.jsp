<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapFile/>

<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>

<tags:jscript language="JavaScript" src="/js/contacts/address.js"/>
<tags:jscript language="JavaScript" src="/js/contacts/userAddressAccessRight.js"/>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="${action}"
               focus="${addressForm.dtoMap['addTelecom'] != null ? 'dto(telecomTypeId)' : 'dto(name1)'}"
               enctype="multipart/form-data" styleClass="form-horizontal">

        <html:hidden property="dto(customerConfirmationFlag)" styleId="customerConfirmationFlagId"/>
        <html:hidden property="dto(supplierConfirmationFlag)" styleId="supplierConfirmationFlagId"/>
        <div id="formPanel">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <c:choose>
                    <c:when test="${onlyView != null}">
                    </c:when>
                    <c:otherwise>
                        <app2:securitySubmit operation="${op}" functionality="CONTACT" property="dto(save)"
                                             styleClass="${app2:getFormButtonClasses()}" styleId="saveButtonId"
                                             onclick="javascript:fillMultipleSelectValues();">
                            <c:out value="${button}"/>
                        </app2:securitySubmit>
                        <c:if test="${op == 'update' && param['dto(onlyViewDetail)'] != 'true'}">
                            <app2:checkAccessRight functionality="CONTACT" permission="DELETE">
                                <html:submit property="dto(delete)" styleClass="${app2:getFormButtonClasses()} cancel"
                                             onclick="javascript:fillMultipleSelectValues();">
                                    <fmt:message key="Common.delete"/>
                                </html:submit>
                            </app2:checkAccessRight>
                        </c:if>
                        <c:if test="${op == 'create' || op == 'delete' || param['dto(onlyViewDetail)'] == true}">
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
                <div id="Person.jsp">
                    <html:hidden property="dto(op)" value="${op}"/>
                    <html:hidden property="dto(addressType)" value="${personType}"/>
                    <html:hidden property="dto(userTypeValue)" value="0"/>
                    <c:if test="${op == 'create'}">
                        <html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}"/>
                    </c:if>
                    <c:if test="${op == 'update'}">
                        <html:hidden property="dto(lastModificationUserId)"
                                     value="${sessionScope.user.valueMap['userId']}"/>
                        <jsp:useBean id="now" class="java.util.Date"/>
                        <html:hidden property="dto(lastUpdateDate)" value="${now}"/>
                        <html:hidden property="dto(version)"/>
                        <html:hidden property="dto(recordUserId)"/>
                        <%--if comming from contactperson "edit personal info", save the address to return it as contact person--%>
                        <html:hidden property="dto(sourceAddressId)"/>
                        <html:hidden property="dto(sourceEmployeeId)"/>
                        <html:hidden property="dto(onlyViewDetail)"/>
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
                            <c:out value="${title}"/>
                        </legend>
                        <div class="row">
                            <div class="${app2:getRowClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="salutationId_id">
                                    <fmt:message key="Contact.Person.salutation"/>
                                    /
                                    <fmt:message key="Contact.Person.title"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6 marginButton">
                                            <fanta:select property="dto(salutationId)" listName="salutationBaseList"
                                                          styleId="salutationId_id"
                                                          firstEmpty="true"
                                                          labelProperty="label"
                                                          valueProperty="id" module="/catalogs"
                                                          styleClass="${app2:getFormSelectClasses()} halfMiddleTextSelect"
                                                          readOnly="${op == 'delete'}" tabIndex="1">
                                                <fanta:parameter field="companyId"
                                                                 value="${sessionScope.user.valueMap['companyId']}"/>

                                            </fanta:select>
                                        </div>
                                        <c:if test="${op == 'delete'}">
                                        </c:if>
                                        <div class="col-xs-12 col-sm-6 marginButton">
                                            <fanta:select property="dto(titleId)" listName="titleList" firstEmpty="true"
                                                          labelProperty="name"
                                                          valueProperty="id" module="/catalogs"
                                                          styleClass="${app2:getFormSelectClasses()} halfMiddleTextSelect"
                                                          readOnly="${op == 'delete'}" tabIndex="2">
                                                <fanta:parameter field="companyId"
                                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                                            </fanta:select>
                                        </div>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()} hidden-xs hidden-sm">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="searchName_id">
                                    <fmt:message key="Contact.Person.searchName"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <app:text property="dto(searchName)" styleId="searchName_id"
                                              styleClass="${app2:getFormInputClasses()} mediumText" maxlength="60"
                                              tabindex="3"
                                              view="${op == 'delete'}"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="name1_id">
                                    <fmt:message key="Contact.Person.lastname"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <app:text property="dto(name1)" styleId="name1_id"
                                              styleClass="${app2:getFormInputClasses()} middleText" maxlength="60"
                                              tabindex="4"
                                              view="${op == 'delete'}"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()} hidden-xs hidden-sm">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="keywords_id">
                                    <fmt:message key="Contact.keywords"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <app:text property="dto(keywords)" styleId="keywords_id"
                                              styleClass="${app2:getFormInputClasses()} mediumText" maxlength="50"
                                              tabindex="5"
                                              view="${op == 'delete'}"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="name2_id">
                                    <fmt:message key="Contact.Person.firstname"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <app:text property="dto(name2)" styleId="name2_id"
                                              styleClass="${app2:getFormInputClasses()} middleText" maxlength="60"
                                              tabindex="6"
                                              view="${op == 'delete'}"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                    <html:hidden property="dto(name3)"/>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="education_id">
                                    <fmt:message key="Contact.Person.education"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <app:text property="dto(education)" styleId="education_id"
                                              styleClass="${app2:getFormInputClasses()} mediumText" maxlength="40"
                                              tabindex="7"
                                              view="${op == 'delete'}"/>
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
                                    <fanta:select property="countryId" listName="countryBasicList" firstEmpty="true"
                                                  labelProperty="name"
                                                  valueProperty="id" module="/catalogs"
                                                  styleClass="${app2:getFormSelectClasses()} middleSelect" tabIndex="8"
                                                  readOnly="${op == 'delete'}" styleId="countryId">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                    </fanta:select>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="languageId_id">
                                    <fmt:message key="Contact.language"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <fanta:select property="dto(languageId)" styleId="languageId_id"
                                                  listName="languageBaseList"
                                                  firstEmpty="true"
                                                  labelProperty="name"
                                                  valueProperty="id" module="/catalogs"
                                                  styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                                  readOnly="${op == 'delete'}"
                                                  tabIndex="9">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                    </fanta:select>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="${app2:getRowClassesTwoColumns()} threeSmallGrid">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="zipId">
                                    <fmt:message key="Contact.cityZip"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <app2:checkAccessRight functionality="CITY" permission="VIEW" var="hasCityAccess"/>
                                    <html:hidden property="cityId" styleId="cityId"/>
                                    <html:hidden property="beforeZip" styleId="beforeZipId"/>
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-3 marginButton">
                                            <app:text property="zip" styleClass="${app2:getFormInputClasses()} zipText"
                                                      maxlength="10" titleKey="Contact.zip"
                                                      tabindex="10"
                                                      view="${op == 'delete'}" readonly="${!hasCityAccess}"
                                                      styleId="zipId"/>
                                        </div>
                                        <div class="col-xs-12 col-sm-7 marginButton">
                                            <app:text property="city"
                                                      styleClass="${app2:getFormInputClasses()} cityNameText"
                                                      maxlength="40"
                                                      titleKey="Contact.city"
                                                      tabindex="11"
                                                      view="${op == 'delete'}" readonly="${!hasCityAccess}"
                                                      styleId="cityNameId"/>
                                        </div>
                                        <div class="col-xs-12 col-sm-2 marginButton">
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
                                                                              tabIndex="12"
                                                                              iframeId="iframeId"
                                                                              submitOnSelect="true"/>
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="validFromId">
                                    <fmt:message key="Contact.Person.birthday"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <c:choose>
                                        <c:when test="${not empty addressForm.dtoMap['dateWithoutYear']}">
                                            <div class="input-group date">
                                                <app:dateText property="dto(birthday)"
                                                              datePatternKey="withoutYearPattern"
                                                              view="${'delete' == op}"
                                                              mode="bootstrap"
                                                              calendarPicker="${op != 'delete'}"
                                                              styleId="validFromId"
                                                              styleClass="${app2:getFormInputClasses()} datepicker mediumText"
                                                              maxlength="10"
                                                              withOutYear="${true}"
                                                              tabindex="13"/>
                                            </div>
                                            <span class="glyphicon form-control-feedback iconValidation"></span>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="input-group date">
                                                <app:dateText property="dto(birthday)" datePatternKey="datePattern"
                                                              view="${'delete' == op}"
                                                              mode="bootstrap"
                                                              calendarPicker="${op != 'delete'}"
                                                              styleId="validFromId"
                                                              styleClass="${app2:getFormInputClasses()} datepicker mediumText"
                                                              maxlength="10"
                                                              tabindex="13"/>
                                            </div>
                                            <span class="glyphicon form-control-feedback iconValidation"></span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="${app2:getRowClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="street_id">
                                    <fmt:message key="Contact.street"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-8 marginButton">
                                            <app:text property="dto(street)"
                                                      styleId="street_id"
                                                      styleClass="${app2:getFormInputClasses()} cityNameText"
                                                      maxlength="40"
                                                      titleKey="Contact.street" tabindex="14"
                                                      view="${op == 'delete'}"/>
                                        </div>

                                        <c:choose>
                                            <c:when test="${op == 'update'}">
                                                <div class="col-xs-11 col-sm-3 marginButton">
                                                    <app:text property="dto(houseNumber)"
                                                              styleClass="${app2:getFormInputClasses()} zipText"
                                                              maxlength="10"
                                                              titleKey="Contact.houseNumber"
                                                              tabindex="15" view="${op == 'delete'}"/>
                                                </div>
                                                <c:import url="/WEB-INF/jsp/layout/tiles/map24.jsp"/>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="col-xs-12 col-sm-4 marginButton">
                                                    <app:text property="dto(houseNumber)"
                                                              styleClass="${app2:getFormInputClasses()} zipText"
                                                              maxlength="10"
                                                              titleKey="Contact.houseNumber"
                                                              tabindex="15" view="${op == 'delete'}"/>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}">
                                    <fmt:message key="Contact.type"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6">
                                            <app2:checkAccessRight functionality="CUSTOMER" permission="CREATE"
                                                                   var="hasCustomerCreate"/>
                                            <c:choose>
                                                <c:when test="${op == 'update' && addressForm.dtoMap['isCustomer']}">
                                                    <div class="radiocheck pull-left">
                                                        <div class="checkbox checkbox-default">
                                                            <html:checkbox styleId="isCustomer_id"
                                                                           property="dto(isCustomer)"
                                                                           disabled="${op == 'delete' || !hasCustomerCreate}"
                                                                           tabindex="16"/>
                                                            <label for="isCustomer_id"><fmt:message
                                                                    key="Contact.customer"/></label>
                                                        </div>
                                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="radiocheck pull-left">
                                                        <div class="checkbox checkbox-default">
                                                            <html:checkbox styleId="isCustomer_id"
                                                                           property="dto(isCustomer)"
                                                                           tabindex="16"
                                                                           disabled="${op == 'delete' || !hasCustomerCreate}"/>
                                                            <label for="isCustomer_id"><fmt:message
                                                                    key="Contact.customer"/></label>
                                                        </div>
                                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:if test="${!hasCustomerCreate && addressForm.dtoMap['isCustomer']}">
                                                <html:hidden property="dto(isCustomer)"/>
                                            </c:if>
                                        </div>
                                        <div class="col-xs-12 col-sm-6">
                                            <app2:checkAccessRight functionality="SUPPLIER" permission="CREATE"
                                                                   var="hasSupplierCreate"/>
                                            <c:choose>
                                                <c:when test="${op == 'update' && addressForm.dtoMap['isSupplier']}">
                                                    <div class="radiocheck pull-left">
                                                        <div class="checkbox checkbox-default">
                                                            <html:checkbox styleId="isSupplier_id"
                                                                           property="dto(isSupplier)"
                                                                           disabled="${op == 'delete' || !hasSupplierCreate}"
                                                                           tabindex="17"/>
                                                            <label for="isSupplier_id">
                                                                <fmt:message key="Contact.supplier"/>
                                                            </label>
                                                        </div>
                                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="radiocheck pull-left">
                                                        <div class="checkbox checkbox-default">
                                                            <html:checkbox styleId="isSupplier_id"
                                                                           property="dto(isSupplier)"
                                                                           tabindex="17"
                                                                           disabled="${op == 'delete' || !hasSupplierCreate}"/>
                                                            <label for="isSupplier_id">
                                                                <fmt:message key="Contact.supplier"/>
                                                            </label>
                                                        </div>
                                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:if test="${!hasSupplierCreate && addressForm.dtoMap['isSupplier']}">
                                                <html:hidden property="dto(isSupplier)"/>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="additionalAddressLine_id">
                                    <fmt:message key="Contact.additionalAddressLine"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <app:text property="dto(additionalAddressLine)" styleId="additionalAddressLine_id"
                                              styleClass="${app2:getFormInputClasses()} middleText" maxlength="100"
                                              tabindex="18" view="${op == 'delete'}"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}">
                                    <fmt:message key="Contact.status"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6">
                                            <c:if test="${op != 'create'}">
                                                <div class="radiocheck pull-left">
                                                    <div class="checkbox checkbox-default">
                                                        <html:checkbox styleId="isActive_id" property="dto(isActive)"
                                                                       disabled="${op == 'delete'}"
                                                                       tabindex="19"/>
                                                        <label for="isActive_id">
                                                            <fmt:message key="Contact.active"/>
                                                        </label>
                                                    </div>
                                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                                </div>
                                            </c:if>
                                        </div>
                                            <%--
                                                            <td width="50%" class="contain" style="padding:0;height:0">
                                                                <c:choose>
                                                                    <c:when test="${op == 'create'}">
                                                                        <html:checkbox styleClass="radio" property="dto(isPrivate)" tabindex="22"/>
                                                                        &nbsp;
                                                                        <fmt:message key="Contact.private"/>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <html:checkbox styleClass="radio" property="dto(isPrivate)" tabindex="22"
                                                                                       disabled="${op == 'delete' || (addressForm.dtoMap['recordUserId'] != sessionScope.user.valueMap['userId'])}"/>
                                                                        &nbsp;
                                                                        <fmt:message key="Contact.private"/>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                            --%>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                        <%--Telecoms area--%>
                    <c:set var="lastTabIndex" value="22" scope="request"/>
                    <c:set var="isType" value="contact" scope="request"/>
                    <html:hidden property="initTelecoms"/>
                    <c:set var="telecomItems" value="${addressForm.telecomMap}" scope="request"/>
                    <c:import url="/WEB-INF/jsp/contacts/TelecomTile.jsp"/>

                    <fieldset>
                        <legend class="title">
                            <fmt:message key="Contact.additionalInfo"/>
                        </legend>
                            <%--<div class="col-sm-12 col-md-8 col-md-offset-2">--%>
                        <div class="${app2:getFormGroupClasses()}">
                            <html:hidden property="dto(imageId)"/>
                            <html:hidden property="dto(imageRemovedFlag)" styleId="imageRemovedFlagId"/>
                            <c:set var="imageId" value="${addressForm.dtoMap['imageId']}"/>

                            <div class="${app2:getFormLabelRenderCategory()}">
                                <c:if test="${imageId != null && imageId != '' && addressForm.dtoMap['imageRemovedFlag']!='true'}">
                                    <div id="imageDivId">
                                        <html:img page="/DownloadAddressImage.do?dto(freeTextId)=${imageId}"
                                                  styleId="imageId"
                                                  styleClass="img-responsive img-thumbnail" width="110px"/>
                                        <c:if test="${op != 'delete'}">
                                            <a href="javascript:hideImageDiv();" class="clearfix marginTop">
                                            <span title="<fmt:message key="Contact.removePhoto"/>"
                                                  class="glyphicon glyphicon-trash" align="top">
                                            </span>
                                            </a>
                                        </c:if>
                                    </div>
                                </c:if>
                            </div>
                            <c:if test="${'delete' != op}">
                                <div class="${app2:getFormContainRenderCategory(null)}">
                                    <label class="control-label">
                                        <fmt:message key="Contact.photo"/>
                                    </label>
                                    <tags:bootstrapFile property="imageFile"
                                                        accept="application/msword"
                                                        tabIndex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                                        styleId="file_id"
                                                        glyphiconClass="glyphicon-picture"
                                            />
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

                        <div>
                            <!--added by ivan-->
                            <c:set var="elementCounter" value="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                   scope="request"/>
                            <c:set var="downloadAction"
                                   value="/contacts/MainPage/Person/DownloadCategoryFieldValue.do?dto(addressId)=${param.contactId}"
                                   scope="request"/>
                            <c:set var="ajaxAction" value="/contacts/MainPageReadSubCategories.do" scope="request"/>
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
                            <%--</div>--%>
                    </fieldset>
                        <%--data level access rights security--%>

                    <fieldset>
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
            </div>
            <div class="${app2:getFormButtonWrapperClasses()}">
                <c:choose>
                    <c:when test="${onlyView !=null}">
                    </c:when>
                    <c:otherwise>
                        <app2:securitySubmit operation="${op}" functionality="CONTACT" property="dto(save)"
                                             styleClass="${app2:getFormButtonClasses()}"
                                             tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                             onclick="javascript:fillMultipleSelectValues();">
                            <c:out value="${button}"/>
                        </app2:securitySubmit>
                        <app2:checkAccessRight functionality="CONTACT" permission="DELETE">
                            <c:if test="${op == 'update' && param['dto(onlyViewDetail)'] != 'true'}">
                                <html:submit property="dto(delete)" styleClass="${app2:getFormButtonClasses()}  cancel"
                                             tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                             onclick="javascript:fillMultipleSelectValues();">
                                    <fmt:message key="Common.delete"/>
                                </html:submit>
                            </c:if>
                        </app2:checkAccessRight>
                        <c:if test="${op == 'create' || op == 'delete' || param['dto(onlyViewDetail)'] == true}">
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
            <c:if test="${op == 'update' || onlyView}">
                <br>
                <app:jobInformationTableTag tableStyleClass="${app2:getFantabulousTableClases()} container"
                                            titleKey="Person.jobInformation.summary"
                                            addressId="${addressForm.dtoMap['addressId']}"
                                            tableAlign="center"
                                            mode="bootstrap"
                                            tdTitleStyleClass="summaryTitle" tdStyleClass="label"
                                            tdEndStyleClass="label2"
                                            enableLinks="${onlyView != null ? false : true}"/>
            </c:if>
        </div>

        <div id="duplicatedListPanel">
            <c:if test="${duplicatedAddressesList != null}">
                <div class="${app2:getFormButtonWrapperClasses()}">
                    <app2:checkAccessRight functionality="CONTACT" permission="CREATE">
                        <html:submit property="dto(save)" styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.saveAnyway"/>
                        </html:submit>
                    </app2:checkAccessRight>
                    <html:button property="return" styleClass="${app2:getFormButtonClasses()}"
                                 onclick="showAddressForm();">
                        <fmt:message key="Common.edit"/>
                    </html:button>
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

                    <html:hidden property="confirmDuplicatedCreation" value="true"
                                 styleId="confirmDuplicatedCreationId"/>
                </div>
                <c:import url="/WEB-INF/jsp/contacts/DuplicatedAddressesList.jsp"/>
            </c:if>
        </div>
    </html:form>
</div>

<tags:jQueryValidation formName="addressForm"/>