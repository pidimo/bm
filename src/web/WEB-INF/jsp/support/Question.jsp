<%@ include file="/Includes.jsp" %>
<c:set var="context" value="<%=request.getContextPath()%>"/>

<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<%
    pageContext.setAttribute("publishedList", JSPHelper.getPublishedQuestionList(request));
%>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="${action}" focus="dto(summary)" styleClass="form-horizontal">

        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
        <fmt:message var="dateTimePattern" key="dateTimePattern"/>

        <c:if test="${'update' == op || 'delete' == op}">
            <html:hidden property="dto(version)"/>
            <html:hidden property="dto(questionId)"/>
            <html:hidden property="dto(createUserId)"/>
            <html:hidden property="dto(createDateTime)"/>
        </c:if>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${'create' != op && questionForm.dtoMap.createUserId == sessionScope.user.valueMap['userId']}">
                <app2:securitySubmit operation="${op}" functionality="QUESTION"
                                     styleClass="${app2:getFormButtonClasses()}">
                    ${button}
                </app2:securitySubmit>
            </c:if>

            <c:if test="${('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId']) && questionForm.dtoMap.article != 'false'}">
                <app2:checkAccessRight functionality="ARTICLE" permission="CREATE">
                    <c:url var="url"
                           value="/support/Question/Article.do?dto(questionId)=${questionForm.dtoMap.questionId}&dto(summary)=${app2:encode(param['dto(summary)'])}&question=true"/>
                    <html:button property="dto(answer)" styleClass="${app2:getFormButtonClasses()}"
                                 onclick="location.href='${url}'">
                        <fmt:message key="Question.answer"/>
                    </html:button>
                </app2:checkAccessRight>
            </c:if>

            <c:if test="${'create' == op}">
                <app2:securitySubmit operation="${op}" functionality="QUESTION"
                                     styleClass="${app2:getFormButtonClasses()}">
                    ${button}
                </app2:securitySubmit>
            </c:if>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="11">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <c:if test="${'create' != op}">
                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="ownerName_id">
                                <fmt:message key="Question.askedBy"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <app:text property="dto(ownerName)"
                                          styleClass="mediumText ${app2:getFormInputClasses()}"
                                          maxlength="40"
                                          styleId="ownerName_id"
                                          tabindex="1"
                                          view="${true}"/>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>

                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Question.AskedOn"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                    ${app2:getDateWithTimeZone(questionForm.dtoMap.createDateTime, timeZone, dateTimePattern)}
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                </c:if>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="summary_id">
                            <fmt:message key="Question.summary"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || ('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId']))}">
                            <app:text property="dto(summary)"
                                      styleClass="middleText ${app2:getFormInputClasses()}"
                                      maxlength="40"
                                      styleId="summary_id"
                                      tabindex="3"
                                      view="${op == 'delete' || ('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId'])}"/>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="categoryId_id">
                            <fmt:message key="Article.categoryName"/>
                        </label>


                        <c:choose>
                            <c:when test="${'delete' != op && (questionForm.dtoMap.createUserId == sessionScope.user.valueMap['userId'] || 'create' == op)}">
                                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                                    <fanta:select property="dto(categoryId)"
                                                  listName="articleCategoryList"
                                                  firstEmpty="true"
                                                  styleId="categoryId_id"
                                                  labelProperty="name"
                                                  valueProperty="id"
                                                  module="/catalogs"
                                                  styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                                  tabIndex="4">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                        <fanta:tree columnId="id" columnParentId="parentCategoryId"
                                                    separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
                                    </fanta:select>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </c:when>

                            <c:otherwise>
                                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                    <fanta:select property="dto(categoryId)"
                                                  listName="articleCategorySimpleList"
                                                  labelProperty="name"
                                                  styleId="categoryId_id"
                                                  valueProperty="id"
                                                  tabIndex="4"
                                                  module="/catalogs"
                                                  styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                                  readOnly="true">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                    </fanta:select>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_name">
                            <fmt:message key="Article.productName"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || ('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId']))}">
                            <div class="input-group">
                                <app:text property="dto(productName)"
                                          styleId="field_name"
                                          styleClass="mediumText ${app2:getFormInputClasses()}"
                                          maxlength="40"
                                          readonly="true"
                                          tabindex="5"
                                          view="${op == 'delete' || ('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId'])}"/>

                                <html:hidden property="dto(productId)" styleId="field_key"/>
                                <html:hidden property="dto(1)" styleId="field_versionNumber"/>
                                <html:hidden property="dto(2)" styleId="field_unitId"/>
                                <html:hidden property="dto(3)" styleId="field_price"/>
                                <span class="input-group-btn">
                                    <tags:bootstrapSelectPopup url="/products/SearchProduct.do"
                                                               name="SearchProduct"
                                                               isLargeModal="true"
                                                               modalTitleKey="Product.Title.SimpleSearch"
                                                               styleId="selectPopupProductId"
                                                               tabindex="5"
                                                               titleKey="Common.search"
                                                               hide="${op == 'delete' || ('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId'])}"/>
                                    <tags:clearBootstrapSelectPopup keyFieldId="field_key"
                                                                    nameFieldId="field_name"
                                                                    tabindex="5"
                                                                    titleKey="Common.clear"
                                                                    hide="${op == 'delete' || ('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId'])}"/>
                                </span>
                            </div>

                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="published_id">
                            <fmt:message key="Article.published"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || ('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId']))}">
                            <html:select property="dto(published)"
                                         styleId="published_id"
                                         styleClass="halfMiddleTextSelect ${app2:getFormSelectClasses()}"
                                         readonly="${op == 'delete' || ('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId'])}"
                                         tabindex="6">
                                <html:option value=""/>
                                <html:options collection="publishedList" property="value" labelProperty="label"/>
                            </html:select>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row col-xs-12">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="control-label col-xs-12 label-left" for="detail_id">
                            <fmt:message key="Question.detail"/>
                        </label>

                        <div class="col-xs-12">
                            <html:textarea property="dto(detail)"
                                           styleId="detail_id"
                                           styleClass="${app2:getFormInputClasses()}"
                                           readonly="${op == 'delete' || ('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId'])}"
                                           tabindex="7"
                                           style="height:120px;width:100%;"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${'create' != op && questionForm.dtoMap.createUserId == sessionScope.user.valueMap['userId']}">
                <app2:securitySubmit operation="${op}" functionality="QUESTION"
                                     styleClass="${app2:getFormButtonClasses()}">
                    ${button}
                </app2:securitySubmit>
            </c:if>

            <c:if test="${('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId']) && questionForm.dtoMap.article != 'false'}">
                <app2:checkAccessRight functionality="ARTICLE" permission="CREATE">
                    <c:url var="url"
                           value="/support/Question/Article.do?dto(questionId)=${questionForm.dtoMap.questionId}&dto(summary)=${app2:encode(param['dto(summary)'])}&question=true"/>
                    <html:button property="dto(answer)" styleClass="${app2:getFormButtonClasses()}"
                                 onclick="location.href='${url}'">
                        <fmt:message key="Question.answer"/>
                    </html:button>
                </app2:checkAccessRight>
            </c:if>

            <c:if test="${'create' == op}">
                <app2:securitySubmit operation="${op}" functionality="QUESTION"
                                     styleClass="${app2:getFormButtonClasses()}">
                    ${button}
                </app2:securitySubmit>
            </c:if>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="11">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </html:form>
</div>

<tags:jQueryValidation formName="questionForm"/>