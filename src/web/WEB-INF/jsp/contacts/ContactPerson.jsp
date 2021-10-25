<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapFile/>
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<tags:jscript language="JavaScript" src="/js/contacts/address.js"/>

<c:set var="newAddress" value="${contactPersonForm.dtoMap['newAddress']}"/>
<c:set var="importAddress" value="${contactPersonForm.dtoMap['importAddress']}"/>


<c:choose>
    <c:when test="${op == 'update'}">
        <c:set var="toFocus" value="dto(name1)"/>
    </c:when>
    <c:when test="${importAddress != null && op == 'create'}">
        <c:set var="toFocus" value="dto(departmentId)"/>
    </c:when>
    <c:otherwise>
        <c:set var="toFocus" value="dto(salutationId)"/>
    </c:otherwise>
</c:choose>
<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="${action}"
               focus="${contactPersonForm.dtoMap['addTelecom'] != null ? 'dto(telecomTypeId)' : toFocus}"
               enctype="multipart/form-data" styleClass="form-horizontal">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="CONTACTPERSON" property="dto(save)"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 onclick="javascript:fillMultipleSelectValues();">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
            <c:if test="${op == 'update'}">
                <%-- Edit personal info link--%>
                <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                    <html:link
                            page="/Person/Forward/Update.do?dto(onlyViewDetail)=true&index=0&contactId=${contactPersonForm.dtoMap['contactPersonId']}&dto(addressId)=${contactPersonForm.dtoMap['contactPersonId']}&dto(sourceAddressId)=${param.contactId}"
                            styleClass="btn btn-link">
                        <span class="glyphicon glyphicon-edit"></span>
                        <fmt:message key="ContactPerson.personalInfo"/>
                    </html:link>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="CATEGORYTAB" permission="VIEW">
                    <c:set var="contactPersonId"
                           value="${contactPersonForm.dtoMap['contactPersonId']}"
                           scope="request"/>
                    <app2:categoryTabLink id="contactPersonCategoryLinkId"
                                          action="/contacts/ContactPerson/CategoryTab/Forward/Update.do?index=${param.index}&contactId=${param.contactId}&dto(addressId)=${param.contactId}&dto(contactPersonId)=${contactPersonForm.dtoMap['contactPersonId']}&contactPersonId=${contactPersonForm.dtoMap['contactPersonId']}&tabKey=Contacts.Tab.contactPersons"
                                          categoryConstant="2"
                                          finderName="findValueByContactPersonId"
                                          showStartSeparator="${true}"
                                          styleClass="btn btn-link">
                        <app2:categoryFinderValue forId="contactPersonCategoryLinkId"
                                                  value="${param.contactId}"/>
                        <app2:categoryFinderValue forId="contactPersonCategoryLinkId"
                                                  value="${contactPersonId}"/>
                    </app2:categoryTabLink>
                </app2:checkAccessRight>
            </c:if>
        </div>
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(addressType)" value="${personType}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

        <c:if test="${'create' == op}">
            <html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}"/>
            <c:choose>
                <c:when test="${newAddress != null}">
                    <html:hidden property="dto(newAddress)" value="true"/>
                    <html:hidden property="dto(addressId)" value="${param.contactId}"/>
                    <%--<html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}"/>--%>
                </c:when>
                <c:when test="${importAddress != null}">
                    <html:hidden property="dto(importAddress)" value="true"/>
                    <html:hidden property="dto(addressId)" value="${param.contactId}"/>
                    <html:hidden property="dto(addressIdToImport)"
                                 value="${contactPersonForm.dtoMap['addressIdToImport']}"/>
                </c:when>
            </c:choose>
        </c:if>
        <%--<c:if test="${'create' != op}">--%>
        <html:hidden property="dto(contactPersonId)"/>
        <html:hidden property="dto(addressId)" value="${param.contactId}"/>
        <html:hidden property="dto(version)"/>

        <%--related to private address of the contact person--%>
        <html:hidden property="dto(lastModificationUserId)"
                     value="${sessionScope.user.valueMap['userId']}"/>
        <html:hidden property="dto(privateVersion)"/>
        <jsp:useBean id="now" class="java.util.Date"/>
        <html:hidden property="dto(lastUpdateDate)" value="${now}"/>
        <%--</c:if>--%>
        <div class="${app2:getFormPanelClasses()}">
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

                        <div class="${app2:getFormContainClassesTwoColumns((importAddress != null && op == 'create') || op== 'delete')}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <fanta:select property="dto(salutationId)"
                                                  styleId="salutationId_id"
                                                  listName="salutationBaseList"
                                                  firstEmpty="true"
                                                  labelProperty="label"
                                                  tabIndex="1"
                                                  valueProperty="id" module="/catalogs"
                                                  styleClass="halfMiddleTextSelect ${app2:getFormSelectClasses()}"
                                                  readOnly="${(importAddress != null && op == 'create') || op== 'delete'}">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                    </fanta:select>
                                </div>
                                <c:if test="${op == 'delete'}"></c:if>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <fanta:select property="dto(titleId)" listName="titleList" firstEmpty="true"
                                                  labelProperty="name"
                                                  tabIndex="2"
                                                  valueProperty="id" module="/catalogs"
                                                  styleClass="halfMiddleTextSelect ${app2:getFormSelectClasses()}"
                                                  readOnly="${(importAddress != null && op == 'create') || op== 'delete'}">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                    </fanta:select>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()} hidden-sm hidden-xs">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="searchName_id">
                            <fmt:message key="Contact.Person.searchName"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns((importAddress != null && op=='create') || op == 'delete')}">
                            <app:text property="dto(searchName)"
                                      styleId="searchName_id"
                                      tabindex="3"
                                      styleClass="mediumText ${app2:getFormInputClasses()}" maxlength="60"
                                      view="${(importAddress != null && op=='create') || op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="name1_id">
                            <fmt:message key="Contact.Person.lastname"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns((importAddress != null && op=='create') || op == 'delete')}">
                            <app:text property="dto(name1)"
                                      styleId="name1_id"
                                      tabindex="4"
                                      styleClass="middleText ${app2:getFormInputClasses()}" maxlength="60"
                                      view="${(importAddress != null && op=='create') || op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()} hidden-sm hidden-xs">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="keywords_id">
                            <fmt:message key="Contact.keywords"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns((importAddress != null && op=='create') || op == 'delete')}">
                            <app:text property="dto(keywords)" styleId="keywords_id"
                                      styleClass="mediumText ${app2:getFormInputClasses()}" maxlength="50"
                                      tabindex="5"
                                      view="${(importAddress != null && op=='create') || op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="name2_id">
                            <fmt:message key="Contact.Person.firstname"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns( (importAddress != null && op=='create') || op == 'delete')}">
                            <app:text property="dto(name2)" styleId="name2_id"
                                      styleClass="middleText ${app2:getFormInputClasses()}"
                                      maxlength="60"
                                      tabindex="6"
                                      view="${(importAddress != null && op=='create') || op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="education_id">
                            <fmt:message key="Contact.Person.education"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns( (importAddress != null && op == 'create') || op== 'delete')}">
                            <app:text property="dto(education)" styleId="education_id"
                                      styleClass="mediumText ${app2:getFormInputClasses()}" maxlength="40"
                                      tabindex="7"
                                      view="${(importAddress != null && op == 'create') || op== 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="departmentId_id">
                            <fmt:message key="ContactPerson.department"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">

                            <c:set var="addressInfoMap" value="${app2:getAddressMap(param.contactId)}"/>
                            <tags:bootstrapSexyCombo targetStyleId="departmentId_id" inputName="dto(departmentName)" hiddenName="dto(departmentId)" maxlength="30"
                                                     enableSexyCombo="${ organizationType == addressInfoMap.addressType and op != 'delete'}"/>

                            <fanta:select property="dto(departmentId)"
                                          styleId="departmentId_id"
                                          listName="departmentBaseList" firstEmpty="true"
                                          labelProperty="name"
                                          tabIndex="8"
                                          valueProperty="departmentId"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                          readOnly="${op == 'delete'}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="addressId"
                                                 value="${not empty param.contactId?param.contactId:0}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="languageId_id">
                            <fmt:message key="Contact.language"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns((importAddress != null && op == 'create') || op== 'delete')}">
                            <fanta:select property="dto(languageId)"
                                          styleId="languageId_id"
                                          tabIndex="9"
                                          listName="languageBaseList" firstEmpty="true"
                                          labelProperty="name"
                                          valueProperty="id" module="/catalogs"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          readOnly="${(importAddress != null && op == 'create') || op== 'delete'}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="function_id">
                            <fmt:message key="ContactPerson.function"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}" nowrap>
                            <app:text property="dto(function)"
                                      styleId="function_id"
                                      tabindex="10"
                                      styleClass="middleText ${app2:getFormInputClasses()}" maxlength="40"
                                      view="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="dateTextId">
                            <fmt:message key="Contact.Person.birthday"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">

                            <c:choose>
                                <c:when test="${not empty importAddress}">
                                    <c:choose>
                                        <c:when test="${not empty contactPersonForm.dtoMap['dateWithoutYear']}">
                                            <app:dateText property="dto(birthday)" datePatternKey="withoutYearPattern"
                                                          view="${'delete' == op || not empty importAddress}"
                                                          styleId="dateTextId"
                                                          tabindex="11"
                                                          styleClass="" maxlength="10" withOutYear="true"
                                                          calendarPicker="true"/>
                                        </c:when>
                                        <c:otherwise>
                                            <app:dateText property="dto(birthday)" datePatternKey="datePattern"
                                                          view="${'delete' == op || not empty importAddress}"
                                                          styleId="dateTextId"
                                                          tabindex="12"
                                                          calendarPicker="true"
                                                          styleClass="${app2:getFormInputClasses()}" maxlength="10"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${not empty contactPersonForm.dtoMap['dateWithoutYear']}">
                                            <div class="input-group date">
                                                <app:dateText property="dto(birthday)"
                                                              datePatternKey="withoutYearPattern"
                                                              view="${'delete' == op}"
                                                              styleId="dateTextId"
                                                              tabindex="11"
                                                              mode="bootstrap"
                                                              styleClass="${app2:getFormInputClasses()}" maxlength="10"
                                                              withOutYear="true"
                                                              calendarPicker="true"/>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="input-group date">
                                                <app:dateText property="dto(birthday)" datePatternKey="datePattern"
                                                              view="${'delete' == op}"
                                                              styleId="dateTextId"
                                                              tabindex="12"
                                                              mode="bootstrap"
                                                              calendarPicker="true"
                                                              styleClass="${app2:getFormInputClasses()}"
                                                              maxlength="10"/>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="personTypeId_id">
                            <fmt:message key="ContactPerson.personType"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <fanta:select property="dto(personTypeId)" listName="personTypeList" firstEmpty="true"
                                          labelProperty="name"
                                          styleId="personTypeId_id"
                                          tabIndex="13"
                                          module="/catalogs" valueProperty="id"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                          readOnly="${op == 'delete'}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="isActive_id">
                            <fmt:message key="Contact.status"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(isActive)" disabled="${op == 'delete'}"
                                               styleClass="radio" tabindex="14" styleId="isActive_id"/>
                                <label for="isActive_id">
                                    <fmt:message key="Contact.active"/>
                                </label>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="addAddressLine_id">
                            <fmt:message key="ContactPerson.additionalAddressLine"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:text property="dto(addAddressLine)"
                                      styleId="addAddressLine_id"
                                      tabindex="15" styleClass="middleText ${app2:getFormInputClasses()}"
                                      maxlength="100"
                                      view="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="additionalAddressId_id">
                            <fmt:message key="ContactPerson.additionalAddress"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <fanta:select property="dto(additionalAddressId)"
                                          styleId="additionalAddressId_id"
                                          listName="additionalAddressToContactPersonList"
                                          labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                                          firstEmpty="true" styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          tabIndex="16"
                                          readOnly="${'delete' == op}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="addressId"
                                                 value="${not empty param.contactId ? param.contactId : 0}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                    <%--Telecoms area--%>
                <c:set var="lastTabIndex" value="15" scope="request"/>
                <c:set var="isType" value="contactPerson" scope="request"/>
                <c:set var="contactPersonId" scope="request"
                       value="${contactPersonForm.dtoMap['contactPersonId']}"/>

                <c:set var="contactPersonName" value="${contactPersonForm.dtoMap['name1']}" scope="request"/>
                <c:if test="${contactPersonForm.dtoMap['name2'] != null}">
                    <c:set var="contactPersonName"
                           value="${contactPersonName}, ${contactPersonForm.dtoMap['name2']}" scope="request"/>
                </c:if>
            </fieldset>
            <html:hidden property="initTelecoms"/>
            <c:set var="telecomItems" value="${contactPersonForm.telecomMap}" scope="request"/>
            <c:import url="/WEB-INF/jsp/contacts/TelecomTile.jsp"/>
            <fieldset>
                <legend class="title">
                    <fmt:message key="Contact.additionalInfo"/>
                </legend>
                <div class="">
                    <html:hidden property="dto(imageId)"/>
                    <html:hidden property="dto(imageRemovedFlag)" styleId="imageRemovedFlagId"/>
                    <c:set var="imageId" value="${contactPersonForm.dtoMap['imageId']}"/>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="control-label col-xs-12 col-sm-4 col-md-4" for="">
                            <c:if test="${imageId != null && imageId != '' && contactPersonForm.dtoMap['imageRemovedFlag']!='true'}">
                                <div id="imageDivId">
                                    <html:img page="/DownloadAddressImage.do?dto(freeTextId)=${imageId}"
                                              border="0"
                                              styleClass="img-responsive img-thumbnail"
                                              width="110px"
                                              styleId="imageId"/>
                                    <c:if test="${!((importAddress != null && op == 'create') || op== 'delete')}">
                                        <a href="javascript:hideImageDiv();">
                                        <span class="glyphicon glyphicon-trash"
                                              alt="<fmt:message key="Contact.removePhoto"/>"
                                              title="<fmt:message key="Contact.removePhoto"/>">
                                        </span>
                                        </a>
                                    </c:if>
                                </div>
                            </c:if>
                        </label>
                        <c:if test="${op != 'delete'}">
                            <div class="parentElementInputSearch col-xs-11 col-sm-7 col-md-6">
                                <label class="control-label" for="imageFile_id">
                                    <fmt:message key="Contact.photo"/>
                                </label>

                                <div class="">
                                    <tags:bootstrapFile property="imageFile"
                                                        glyphiconClass="glyphicon-picture"
                                                        tabIndex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                                        styleId="imageFile_id"/>
                                    <fmt:message key="Common.fileUpload.info">
                                        <fmt:param value="200 KB"/>
                                        <fmt:param value="gif, jpeg, png"/>
                                    </fmt:message>
                                    <span class="glyphicon form-control-feedback iconValidation centerIconValidation"></span>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="control-label col-xs-12 col-sm-4 col-md-4" for="">
                            <fmt:message key="Contact.notes"/>
                        </label>

                        <div class="parentElementInputSearch col-xs-11 col-sm-7 col-md-6">
                            <html:textarea property="dto(note)"
                                           styleClass="mediumDetailHigh ${app2:getFormInputClasses()}"
                                           rows="5"
                                           tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                           readonly="${(importAddress != null && op == 'create') || op== 'delete'}"
                                           style="form-control"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <!--added by ivan-->
                    <c:set var="elementCounter" value="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                           scope="request"/>
                    <c:set var="ajaxAction" value="/contacts/ContactPerson/MainPageReadSubCategories.do"
                           scope="request"/>
                    <c:set var="downloadAction"
                           value="/contacts/ContactPerson/MainPage/DownloadCategoryFieldValue.do?dto(addressId)=${param.contactId}&contactId=${param.contactId}&dto(contactPersonId)=${contactPersonId}&contactPersonId=${contactPersonId}"
                           scope="request"/>
                    <c:set var="formName" value="contactPersonForm" scope="request"/>
                    <c:set var="table" value="<%=ContactConstants.CONTACTPERSON_CATEGORY%>" scope="request"/>
                    <c:set var="secondTable" value="<%=ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY%>"
                           scope="request"/>
                    <c:set var="operation" value="${op}" scope="request"/>
                    <c:set var="labelWidth" value="15" scope="request"/>
                    <c:set var="containWidth" value="85" scope="request"/>
                    <c:set var="generalWidth" value="${250}" scope="request"/>
                    <c:import url="/WEB-INF/jsp/catalogs/CategoryUtil.jsp"/>
                </div>
                <c:set var="lastTabIndex" value="${elementCounter+1}" scope="request"/>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="CONTACTPERSON" property="dto(save)"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                 onclick="javascript:fillMultipleSelectValues();">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"
                         tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </html:form>
</div>

<tags:jQueryValidation formName="contactPersonForm"/>



