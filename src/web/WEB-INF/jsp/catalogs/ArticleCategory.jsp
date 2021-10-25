<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(categoryName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <html:hidden property="dto(op)" value="${op}"/>
                <c:if test="${('update' == op) || ('delete' == op)}">
                    <html:hidden property="dto(categoryId)"/>
                </c:if>
                <c:if test="${('update' == op)}">
                    <html:hidden property="dto(version)"/>
                </c:if>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="categoryName_id">
                        <fmt:message key="ArticleCategory.categoryName"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <app:text property="dto(categoryName)" view="${op =='delete'}" styleId="categoryName_id"
                                  styleClass="${app2:getFormInputClasses()} largeText" maxlength="40"
                                  tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="parentCategoryId_id">
                        <fmt:message key="ArticleCategory.parent"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <!-- Collection articleCategories-->
                        <fanta:select property="dto(parentCategoryId)" styleId="parentCategoryId_id"
                                      listName="articleCategoryList" firstEmpty="true"
                                      labelProperty="name" valueProperty="id" module="/catalogs"
                                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                                      tabIndex="2" readOnly="${op == 'delete'}"
                                      value="${articleCategoryForm.dtoMap.parentCategoryId}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <c:if test="${'create' != op}">
                                <fanta:parameter field="categoryIdAux"
                                                 value="${not empty articleCategoryForm.dtoMap.categoryId?articleCategoryForm.dtoMap.categoryId:0}"/>
                            </c:if>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="SUPPORTCATEGORY"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="3">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="SUPPORTCATEGORY"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"
                                     tabindex="4">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="5"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="articleCategoryForm"/>