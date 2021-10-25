<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key,field_name, field_versionNumber, field_price, field_unitId"/>
<tags:initBootstrapDatepicker/>

<script>
    function changeState() {
        document.getElementById("saveButtonId").click();
    }
</script>

<c:set var="isOpenForThisUser" value="${caseForm.dtoMap.openByUserId == sessionScope.user.valueMap.userId}"/>

<c:set var="isInternal" value="${sessionScope.user.valueMap.userType == 1}"/>
<c:set var="onlyView" value="${op == 'delete' || (op == 'update' && !isInternal)}" scope="request"/>

<c:if test="${!onlyView || isOpenForThisUser}">
    <tags:initTinyMCE4 textAreaId="description_text"/>
</c:if>


<fmt:message var="datePattern" key="datePattern"/>

<html:form action="${action}" focus="dto(caseTitle)" enctype="multipart/form-data" styleClass="form-horizontal">
    <html:hidden property="dto(op)" value="${op}" styleId="op"/>

    <c:if test="${op == 'update'}">
        <html:hidden property="dto(userAssigned)"/>
        <c:if test="${caseForm.dtoMap.new_activity != null}">
            <html:hidden property="dto(new_activity)"/>
        </c:if>
        <c:if test="${caseForm.dtoMap.isClosed != null}">
            <html:hidden property="dto(isClosed)"/>
        </c:if>
        <c:if test="${caseForm.dtoMap.reopenCase != null}">
            <html:hidden property="dto(reopenCase)"/>
        </c:if>
    </c:if>

    <c:if test="${!isInternal}">
        <input type="hidden" name="dto(ut)" value="1"/>
    </c:if>
    <c:if test="${'update' == op || 'delete' == op}">
        <html:hidden property="dto(version)" styleId="1"/>
        <html:hidden property="dto(caseId)" styleId="2"/>
    </c:if>

    <%--${caseForm.dtoMap}
    <hr>--%>

    <div class="${app2:getFormButtonWrapperClasses()}">
        <c:if test="${isInternal || isOpenForThisUser || op == 'create'}">
            <app2:securitySubmit operation="${op}" functionality="CASE" property="dto(save)"
                                 styleClass="button ${app2:getFormButtonClasses()}" styleId="saveButtonId">
                <c:out value="${button}"/>
            </app2:securitySubmit>
        </c:if>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"><fmt:message
                key="Common.cancel"/></html:cancel>
    </div>

    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <c:out value="${title}"/>
            </legend>

            <c:if test="${caseForm.dtoMap.n !=null && caseForm.dtoMap.n !=''}">
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Common.number"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(true)}">

                            <html:hidden property="dto(n)" write="true"/>
                        </div>
                    </div>
                </div>
            </c:if>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Common.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
                        <app:text property="dto(caseTitle)"
                                  styleClass="middleText ${app2:getFormInputClasses()}"
                                  maxlength="160"
                                  tabindex="1"
                                  view="${onlyView}"
                                  styleId="3"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Common.openBy"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <c:if test="${op == 'update'}">
                            <html:hidden property="dto(openByUserId)"/>
                        </c:if>
                        <c:choose>
                            <c:when test="${caseForm.dtoMap.openByUserId == null}">
                                <c:set var="openBy" value="${sessionScope.user.valueMap['userId']}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="openBy" value="${caseForm.dtoMap.openByUserId}"/>
                            </c:otherwise>
                        </c:choose>

                        <c:if test="${op == 'create'}">
                            <input type="hidden" name="dto(openByUserId)" value="${openBy}" styleId="4"/>
                        </c:if>

                        <fanta:label listName="userBaseList" module="/admin" patron="0" columnOrder="name">
                            <fanta:parameter field="userId" value="${openBy}"/>
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:label>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="CaseType.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
                        <c:set var="supportCaseTypes" value="${app2:getSupportCaseType(pageContext.request)}"/>
                        <html:select property="dto(caseTypeId)"
                                     styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                     readonly="${onlyView}"
                                     styleId="5"
                                     tabindex="2">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="supportCaseTypes" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="CaseSeverity.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
                        <c:set var="supportSeverityCase" value="${app2:getSupportSeverityCase(pageContext.request)}"/>
                        <html:select property="dto(severityId)"
                                     styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                     readonly="${onlyView}"
                                     styleId="6"
                                     tabindex="3">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="supportSeverityCase" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Product.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
                        <div class="input-group">
                            <app:text property="dto(productName)"
                                      styleId="field_name"
                                      styleClass="middleText ${app2:getFormInputClasses()}"
                                      maxlength="40"
                                      tabindex="4"
                                      readonly="true" view="${onlyView}"/>
                            <html:hidden property="dto(productId)" styleId="field_key"/>
                            <html:hidden property="dto(1)" styleId="field_versionNumber"/>
                            <html:hidden property="dto(2)" styleId="field_unitId"/>
                            <html:hidden property="dto(3)" styleId="field_price"/>
                            <span class="input-group-btn">
                                <tags:bootstrapSelectPopup styleId="SearchProduct_id"
                                                           url="/products/SearchProduct.do?product=true"
                                                           name="SearchProduct"
                                                           titleKey="Common.search"
                                                           modalTitleKey="Product.Title.SimpleSearch"
                                                           hide="${onlyView}"
                                                           isLargeModal="true"
                                                           submitOnSelect="true"/>
                                <tags:clearBootstrapSelectPopup keyFieldId="field_key"
                                                                nameFieldId="field_name"
                                                                titleKey="Common.clear"
                                                                hide="${onlyView}"/>
                            </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Priority.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
                        <c:set var="supportPriorities" value="${app2:getSupportPriority(pageContext.request)}"/>
                        <html:select property="dto(priorityId)"
                                     styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                     readonly="${onlyView}"
                                     styleId="7"
                                     tabindex="5">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="supportPriorities" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

            </div>

            <c:if test="${isInternal}">
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Common.keywords"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
                            <app:text property="dto(keywords)"
                                      styleClass="middleText ${app2:getFormInputClasses()}"
                                      maxlength="40"
                                      tabindex="6"
                                      view="${onlyView}" styleId="8"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Contact.title"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
                            <div class="input-group">
                                <app:text property="dto(contact)"
                                          styleId="fieldAddressName_id"
                                          styleClass="middleText ${app2:getFormInputClasses()}"
                                          tabindex="7"
                                          readonly="true"
                                          view="${onlyView}"/>
                                <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
                    <span class="input-group-btn">
                        <tags:bootstrapSelectPopup styleId="searchAddress_id"
                                                   url="/contacts/SearchAddress.do"
                                                   name="searchAddress"
                                                   titleKey="Common.search"
                                                   modalTitleKey="Contact.Title.search"
                                                   hide="${onlyView}"
                                                   isLargeModal="true"
                                                   submitOnSelect="true"/>
                        <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                        nameFieldId="fieldAddressName_id"
                                                        titleKey="Common.clear" submitOnClear="true"
                                                        hide="${onlyView}"/>
                    </span>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Common.assignedTo"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
                            <fmt:message var="externalUser" key="User.externalUser"/>
                            <fmt:message var="internalUser" key="User.intenalUser"/>

                            <fanta:select property="dto(toUserId)"
                                          listName="userBaseList"
                                          labelProperty="name"
                                          tabIndex="8"
                                          styleId="9"
                                          valueProperty="id"
                                          firstEmpty="true"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                          readOnly="${onlyView}"
                                          module="/admin"
                                          value="${sessionScope.user.valueMap['userId']}"
                                          withGroups="true">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:group groupName="${externalUser}" columnDiscriminator="type"
                                             valueDiscriminator="0"/>
                                <fanta:group groupName="${internalUser}" columnDiscriminator="type"
                                             valueDiscriminator="1"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="ContactPerson.title"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
                            <fanta:select property="dto(contactPersonId)"
                                          tabIndex="9"
                                          listName="searchContactPersonList"
                                          styleId="10"
                                          module="/contacts"
                                          firstEmpty="true"
                                          labelProperty="contactPersonName"
                                          valueProperty="contactPersonId"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                          readOnly="${onlyView}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap.companyId}"/>
                                <fanta:parameter field="addressId"
                                                 value="${not empty caseForm.dtoMap.addressId?caseForm.dtoMap.addressId:0}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="State.title"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
                            <c:set var="changeState"
                                   value="${caseForm.dtoMap.isClosed != null ? 'changeState()' : ''}"/>
                            <c:set var="supportStatus" value="${app2:getSupportStatus(pageContext.request)}"/>
                            <html:select property="dto(stateId)"
                                         styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                         readonly="${onlyView}"
                                         styleId="11"
                                         tabindex="10">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="supportStatus" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Common.totalHours"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
                            <app:text property="dto(totalHours)"
                                      styleClass="numberText ${app2:getFormInputClasses()}"
                                      maxlength="40"
                                      tabindex="11"
                                      view="${onlyView}" styleId="12"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </c:if>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Common.openDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
                        <div class="input-group date">
                            <app:dateText property="dto(openDate)"
                                          styleId="openDate"
                                          mode="bootstrap"
                                          calendarPicker="${!onlyView}"
                                          datePatternKey="${datePattern}"
                                          styleClass="middleText ${app2:getFormInputClasses()}"
                                          view="${onlyView}"
                                          maxlength="10"
                                          tabindex="12"
                                          currentDate="true"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="WorkLevel.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
                        <c:set var="supportWorkLevels" value="${app2:getSupportWorkLevel(pageContext.request)}"/>
                        <html:select property="dto(workLevelId)"
                                     styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                     readonly="${onlyView}"
                                     styleId="13"
                                     tabindex="13">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="supportWorkLevels" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Common.expireDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
                        <div class="input-group date">
                            <app:dateText property="dto(expireDate)"
                                          styleId="expireDate"
                                          mode="bootstrap"
                                          calendarPicker="${!onlyView}"
                                          datePatternKey="${datePattern}"
                                          styleClass="middleText ${app2:getFormInputClasses()}"
                                          view="${onlyView}"
                                          maxlength="10"
                                          tabindex="14"
                                          currentDate="false"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <c:choose>
                            <c:when test="${isInternal}">
                                <fmt:message key="Common.closeDate"/>
                            </c:when>
                            <c:when test="${!isInternal && op == 'update'}">
                                <fmt:message key="State.title"/>
                            </c:when>
                        </c:choose>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <c:choose>
                            <c:when test="${isInternal}">
                                <c:catch var="failDate">
                                    <fmt:formatDate value="${app2:intToDate(caseForm.dtoMap.cd)}"
                                                    pattern="${datePattern}"/>
                                </c:catch>
                                <c:if test="${failDate!=null}">
                                    ${caseForm.dtoMap.cd}
                                </c:if>
                                <html:hidden property="dto(cd)" styleId="14"/>
                            </c:when>
                            <c:when test="${!isInternal && op == 'update'}">
                                <html:hidden property="dto(stn)" write="true" styleId="15"/>
                            </c:when>
                        </c:choose>
                    </div>
                </div>

            </div>

        </fieldset>

        <c:if test="${caseForm.dtoMap.reopenCase != null}">
            <div class="">
                <div class="${app2:getFormGroupClasses()}">
                    <fmt:message key="SupportCase.reopenDescription"/>
                    <html:textarea property="dto(reopenDescription)"
                                   style="width:100%"
                                   styleClass="webmailBody"
                                   tabindex="30"
                                   styleId="16"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </c:if>

        <label class="${app2:getFormLabelClasses()} label-left" for="">
            <fmt:message key="Common.detail"/>
        </label>
        <c:choose>
            <c:when test="${(!onlyView || isOpenForThisUser ) && op != 'delete'}">
                <div class="col-xs-12">
                    <div class="${app2:getFormGroupClasses()}">
                        <html:textarea property="dto(caseDescription)"
                                       style="width:100%"
                                       styleClass="webmailBody"
                                       tabindex="25"
                                       styleId="description_text"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

            </c:when>
            <c:otherwise>
                <div class="embed-responsive well col-xs-12" style="height: 240px !important;">
                    <c:set var="cd" value="${caseForm.dtoMap.caseDescription}" scope="session"/>
                    <iframe class="embed-responsive-item" name="frame2"
                            src="<c:url value="/WEB-INF/jsp/support/PreviusDetail.jsp?var=cd" />"
                            style="width : 100%;height: 240px !important;background-color:#ffffff" scrolling="yes"
                            frameborder="1">
                    </iframe>
                </div>
            </c:otherwise>
        </c:choose>
    </div>


    <div class="${app2:getFormButtonWrapperClasses()}">
        <c:if test="${isInternal || isOpenForThisUser || op == 'create'}">
            <app2:securitySubmit operation="${op}" functionality="CASE" property="dto(save)"
                                 styleClass="button ${app2:getFormButtonClasses()}" styleId="saveButtonId">
                <c:out value="${button}"/>
            </app2:securitySubmit>
        </c:if>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"><fmt:message
                key="Common.cancel"/></html:cancel>
    </div>

</html:form>

<tags:jQueryValidation formName="caseForm"/>
