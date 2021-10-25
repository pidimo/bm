<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@include file="/Includes.jsp" %>

<c:set var="NORMAL_TYPE" value="<%=ContactConstants.AdditionalAddressType.NORMAL.getConstant()%>"/>
<c:set var="MAIN_TYPE" value="<%=ContactConstants.AdditionalAddressType.MAIN.getConstant()%>"/>

<html:form action="${action}" focus="dto(name)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(addressId)" value="${param.contactId}"/>

        <c:if test="${'create' == op}">
            <html:hidden property="dto(additionalAddressType)" value="${NORMAL_TYPE}"/>
        </c:if>

        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(additionalAddressId)"/>
            <html:hidden property="dto(additionalAddressType)"/>
        </c:if>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>

        <c:set var="readOnly" value="${op == 'delete'}" scope="request"/>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="ADDITIONALADDRESS"
                                 tabindex="20" styleClass="${app2:getFormButtonClasses()}">
                ${button}
            </app2:securitySubmit>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="21">
                <fmt:message key="Common.cancel"/>
            </html:cancel>

        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="name_id">
                        <fmt:message key="AdditionalAddress.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <c:choose>
                            <c:when test="${MAIN_TYPE eq additionalAddressForm.dtoMap['additionalAddressType']}">
                                <html:hidden property="dto(name)"/>
                                <fmt:message key="AdditionalAddress.item.mainAddress"/>
                            </c:when>
                            <c:otherwise>
                                <app:text property="dto(name)" styleId="name_id"
                                          styleClass="${app2:getFormInputClasses()} largeText"
                                          maxlength="100"
                                          tabindex="1" view="${op == 'delete'}"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="street_id">
                        <fmt:message key="AdditionalAddress.street"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <app:text property="dto(street)" styleId="street_id"
                                  styleClass="${app2:getFormInputClasses()} largeText"
                                  maxlength="50" titleKey="Contact.street"
                                  tabindex="1"
                                  view="${op == 'delete'}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="houseNumber_id">
                        <fmt:message key="AdditionalAddress.houseNumber"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <app:text property="dto(houseNumber)" styleId="houseNumber_id"
                                  styleClass="${app2:getFormInputClasses()} zipText"
                                  maxlength="10" titleKey="Contact.houseNumber"
                                  tabindex="2" view="${op == 'delete'}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="additionalAddressLine_id">
                        <fmt:message key="AdditionalAddress.additionalAddressLine"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <app:text property="dto(additionalAddressLine)" styleId="additionalAddressLine_id"
                                  styleClass="${app2:getFormInputClasses()} largeText" maxlength="100"
                                  tabindex="3" view="${op == 'delete'}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="countryId">
                        <fmt:message key="Contact.country"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <fanta:select property="countryId" listName="countryBasicList" firstEmpty="true"
                                      labelProperty="name"
                                      valueProperty="id" module="/catalogs"
                                      styleClass="${app2:getFormSelectClasses()} middleSelect" tabIndex="4"
                                      readOnly="${op == 'delete'}" styleId="countryId">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="row threeSmallGrid">
                    <label class="${app2:getFormLabelClasses()}" for="zipId">
                        <fmt:message key="Contact.cityZip"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <app2:checkAccessRight functionality="CITY" permission="VIEW" var="hasCityAccess"/>
                        <html:hidden property="cityId" styleId="cityId"/>
                        <html:hidden property="beforeZip" styleId="beforeZipId"/>
                        <div class="row">
                            <div class="col-xs-12 col-sm-3 wrapperButton">
                                <app:text property="zip" styleClass="${app2:getFormInputClasses()} zipText"
                                          maxlength="10"
                                          titleKey="Contact.zip"
                                          tabindex="5"
                                          view="${op == 'delete'}" readonly="${!hasCityAccess}" styleId="zipId"/>
                            </div>
                            <div class="col-xs-12 col-sm-7 wrapperButton">
                                <app:text property="city" styleClass="${app2:getFormInputClasses()} cityNameText"
                                          maxlength="40"
                                          titleKey="Contact.city" tabindex="6"
                                          view="${op == 'delete'}" readonly="${!hasCityAccess}" styleId="cityNameId"/>
                            </div>
                            <div class="col-xs-12 col-sm-2">
                                <c:if test="${op != 'delete' && hasCityAccess}">
                                    <div class="pull-right">
                                        <tags:bootstrapSearchCity countryIdField="countryId"
                                                                  cityIdField="cityId"
                                                                  zipField="zipId"
                                                                  cityField="cityNameId"
                                                                  beforeZip="beforeZipId"
                                                                  titleKey="City.Title.search"
                                                                  modalTitleKey="City.Title.search"
                                                                  message="empty"
                                                                  tabIndex="7"
                                                                  iframeId="iframeId"
                                                                  submitOnSelect="true"/>
                                    </div>
                                </c:if>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="isDefault_id">
                        <fmt:message key="AdditionalAddress.default"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(isDefault)" styleId="isDefault_id"
                                               disabled="${op == 'delete'}"
                                               tabindex="8"/>
                                <label for="isDefault_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()} topLabel" for="comment_id">
                        <fmt:message key="AdditionalAddress.comment"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <html:textarea property="dto(comment)"
                                       styleId="comment_id"
                                       styleClass="${app2:getFormInputClasses()} mediumDetailHigh"
                                       tabindex="9"
                                       style="height:120px;"
                                       readonly="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <app2:securitySubmit operation="${op}" functionality="ADDITIONALADDRESS"
                                     tabindex="10" styleClass="${app2:getFormButtonClasses()}">
                    ${button}
                </app2:securitySubmit>

                <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="11">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="additionalAddressForm"/>