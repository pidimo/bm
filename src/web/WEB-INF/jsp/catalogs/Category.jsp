<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<script language="JavaScript">
    function onSubmitForm() {
        document.forms[0].submit();
    }
</script>

<c:set var="categoryAllTypeList" value="${app2:getCategoryTablesListExtended(pageContext.request)}"/>
<c:set var="categoryTypeList" value="${app2:getCategoryTypes(pageContext.request)}"/>

<c:set var="ADDRESS_CATEGORY" value="<%=ContactConstants.ADDRESS_CATEGORY%>"/>
<c:set var="CONTACTPERSON_CATEGORY" value="<%=ContactConstants.CONTACTPERSON_CATEGORY%>"/>

<html:form action="${action}" focus="dto(categoryName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <html:hidden property="dto(customerMSG)"
                             value='<%= JSPHelper.getMessage(request,"Category.customer")%>'/>
                <html:hidden property="dto(contactpersonMSG)"
                             value='<%= JSPHelper.getMessage(request,"Category.contactPerson")%>'/>
                <html:hidden property="dto(productMSG)"
                             value='<%= JSPHelper.getMessage(request,"Category.product")%>'/>
                <html:hidden property="dto(addressMSG)"
                             value='<%= JSPHelper.getMessage(request,"Category.address")%>'/>
                <html:hidden property="dto(op)" value="${op}"/>
                <c:if test="${'update'==op || 'delete'==op}">
                    <html:hidden property="dto(categoryId)"/>
                    <html:hidden property="dto(langTextId)"/>
                </c:if>
                <c:if test="${'update'==op}">
                    <html:hidden property="dto(version)"/>
                    <html:hidden property="dto(canUpdateLabel)"/>
                    <html:hidden property="dto(canUpdateType)"/>
                    <html:hidden property="dto(canUpdateHasSubCategories)"/>
                    <html:hidden property="dto(canUpdateParentCategory)"/>
                    <html:hidden property="dto(haveFirstTranslation)"/>
                </c:if>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="categoryName_id">
                        <fmt:message key="Category.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(categoryName)" styleId="categoryName_id"
                                  styleClass="${app2:getFormInputClasses()} largetext"
                                  maxlength="20"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="sequence_id">
                        <fmt:message key="Category.sequence"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:numberText property="dto(sequence)" styleId="sequence_id"
                                        styleClass="${app2:getFormInputClasses()} numberText" style="text"
                                        numberType="integer"
                                        maxlength="3"
                                        view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="languageId_id">
                        <fmt:message key="Category.language"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op || op == 'update')}">
                        <fanta:select property="dto(languageId)"
                                      listName="systemLanguageList"
                                      labelProperty="name"
                                      valueProperty="id"
                                      styleId="languageId_id"
                                      firstEmpty="true" styleClass="${app2:getFormSelectClasses()} select"
                                      readOnly="${op == 'delete'|| true == categoryForm.dtoMap['haveFirstTranslation']}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="table_id">
                        <fmt:message key="Category.label"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op || false == categoryForm.dtoMap['canUpdateLabel'])}">
                        <html:select property="dto(table)"
                                     styleClass="${app2:getFormSelectClasses()} select"
                                     styleId="table_id"
                                     readonly="${'delete' == op || false == categoryForm.dtoMap['canUpdateLabel']}"
                                     onchange="javascript:onSubmitForm();">
                            <html:option value=""/>
                            <html:options collection="categoryAllTypeList" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="categoryType_id">
                        <fmt:message key="Category.type"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op || false == categoryForm.dtoMap['canUpdateType'])}">
                        <html:select property="dto(categoryType)"
                                     styleId="categoryType_id"
                                     styleClass="${app2:getFormSelectClasses()} select"
                                     readonly="${'delete' == op || false == categoryForm.dtoMap['canUpdateType']}"
                                     onchange="javascript:onSubmitForm();">
                            <html:option value=""/>
                            <html:options collection="categoryTypeList" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <!--show this option only category type is single select-->
                <c:if test="${(null ==  categoryForm.dtoMap.parentCategory || empty categoryForm.dtoMap.parentCategory) && app2:isCategorySingleSelect(categoryForm.dtoMap.categoryType)}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()} " for="hasSubCategories_id">
                            <fmt:message key="Category.hasSubCategories"/>
                        </label>

                        <div class="${app2:getFormContainClasses(null)}">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(hasSubCategories)"
                                                   value="true"
                                                   onclick="javascript:onSubmitForm();"
                                                   styleClass="adminCheckBox"
                                                   styleId="hasSubCategories_id"
                                                   disabled="${'delete' == op || false == categoryForm.dtoMap.canUpdateHasSubCategories}"/>
                                    <label for="hasSubCategories_id"></label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <c:if test="${'delete' == op || false == categoryForm.dtoMap.canUpdateHasSubCategories}">
                        <html:hidden property="dto(hasSubCategories)"/>
                    </c:if>
                </c:if>

                <!--Show this option only for categories that can not be sub categories-->
                <c:if test="${'true' != categoryForm.dtoMap.hasSubCategories && not empty categoryForm.dtoMap.table}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="parentCategory_id">
                            <fmt:message key="Category.parentCategory"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op || false == categoryForm.dtoMap.canUpdateParentCategory)}">
                            <fanta:select property="dto(parentCategory)"
                                          listName="selectCategoryList"
                                          labelProperty="categoryName"
                                          valueProperty="categoryId"
                                          firstEmpty="true"
                                          styleId="parentCategory_id"
                                          styleClass="${app2:getFormSelectClasses()} select"
                                          readOnly="${'delete' == op || false == categoryForm.dtoMap.canUpdateParentCategory}"
                                          onChange="javascript:onSubmitForm();">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="tableId" value="${categoryForm.dtoMap.table}"/>
                                <c:if test="${'update' == op || 'delete' == op}">
                                    <fanta:parameter field="categoryId" value="${categoryForm.dtoMap.categoryId}"/>
                                    <c:if test="${false == categoryForm.dtoMap.canUpdateParentCategory}">
                                        <fanta:parameter field="parentCategory"
                                                         value="${categoryForm.dtoMap.parentCategory}"/>
                                    </c:if>
                                </c:if>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>

                <!--Show this option only for root categories-->
                <c:if test="${not empty categoryForm.dtoMap.table}">
                    <c:set var="IS_ADDRESSCONTACTPERSON"
                           value="${app2:isAddressContactPersonCategory(categoryForm.dtoMap.table)}"/>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="categoryGroupId">
                            <fmt:message key="${IS_ADDRESSCONTACTPERSON ? 'Category.Group.Address': 'Category.Group'}"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <fanta:select property="dto(categoryGroupId)"
                                          listName="categoryGroupList"
                                          labelProperty="label"
                                          valueProperty="categoryGroupId"
                                          firstEmpty="true"
                                          styleId="categoryGroupId"
                                          styleClass="${app2:getFormSelectClasses()} select"
                                          readOnly="${'delete' == op}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="tableId"
                                                 value="${IS_ADDRESSCONTACTPERSON ? ADDRESS_CATEGORY : categoryForm.dtoMap.table}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <c:if test="${IS_ADDRESSCONTACTPERSON}">
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}" for="secondGroupId_id">
                                <fmt:message key="Category.Group.ContactPerson"/>
                            </label>

                            <div class="${app2:getFormContainClasses('delete' == op)}">
                                <fanta:select property="dto(secondGroupId)"
                                              listName="categoryGroupList"
                                              labelProperty="label"
                                              valueProperty="categoryGroupId"
                                              firstEmpty="true"
                                              styleId="secondGroupId_id"
                                              styleClass="${app2:getFormSelectClasses()} select"
                                              readOnly="${'delete' == op}">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="tableId" value="${CONTACTPERSON_CATEGORY}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </c:if>
                </c:if>

                <c:if test="${'delete'== op && null != categoryForm.dtoMap['categoryValuesSize']}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Category.categoryValues"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}"
                            ${categoryForm.dtoMap['categoryValuesSize']}
                    </div>
                </c:if>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="descriptionText_id">
                        <fmt:message key="Category.description"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <html:textarea property="dto(descriptionText)" styleId="descriptionText_id"
                                       styleClass="${app2:getFormInputClasses()} mediumDetail"
                                       style="height:120px;width:99%;"
                                       tabindex="5"
                                       readonly="${op == 'delete'}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>

            <html:hidden property="dto(freeTextId)"/>

            <c:if test="${'true' != categoryForm.dtoMap.hasSubCategories && not empty categoryForm.dtoMap.table && not empty categoryForm.dtoMap.parentCategory}">
                <c:set var="hasSubCategories"
                       value="${app2:hasSubCategories(categoryForm.dtoMap.parentCategory, pageContext.request)}"/>
                <c:if test="${hasSubCategories}">
                    <c:set var="categoryValues"
                           value="${app2:getCategoryValues(categoryForm.dtoMap.parentCategory, pageContext.request)}"/>

                    <c:if test="${not empty categoryValues}">
                        <fieldset>
                            <legend class="title">
                                <fmt:message key="Category.relateToParentCategory"/>
                            </legend>
                                <c:forEach var="categoryValue" items="${categoryValues}">
                                    <c:set var="canUpdateCategoryValue"
                                           value="canUpdateCategoryValue_${categoryValue.categoryValueId}"/>
                                   <div class="${app2:getFormGroupClasses()}">
                                       <label class="${app2:getFormLabelClasses()}">
                                           <c:out value="${categoryValue.categoryValueName}"/>
                                       </label>
                                       <html:hidden property="dto(categoryValueName_${categoryValue.categoryValueId})"
                                                    value="${categoryValue.categoryValueName}"/>
                                       <html:hidden property="dto(categoryValueIds)"
                                                    value="${categoryValue.categoryValueId}"
                                                    styleId="dto(categoryValueIds)"/>
                                       <div class="${app2:getFormContainClasses(op == 'delete' || false == categoryForm.dtoMap[canUpdateCategoryValue])}">
                                           <div class="radiocheck">
                                               <div class="checkbox checkbox-default">
                                                   <html:checkbox property="dto(${categoryValue.categoryValueId})"
                                                                  value="true"
                                                                  styleClass="adminCheckBox"
                                                                  styleId="selectedCategories"
                                                                  disabled="${'delete' == op || false == categoryForm.dtoMap[canUpdateCategoryValue]}"/>
                                                   <label for="selectedCategories"></label>
                                               </div>
                                           </div>
                                       </div>
                                   </div>
                                    <c:if test="${'delete' == op || false == categoryForm.dtoMap[canUpdateCategoryValue]}">
                                        <html:hidden
                                                property="dto(canUpdateCategoryValue_${categoryValue.categoryValueId})"/>
                                        <html:hidden property="dto(${categoryValue.categoryValueId})"/>
                                    </c:if>
                                </c:forEach>
                        </fieldset>
                    </c:if>
                </c:if>
            </c:if>

        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit property="dto(save)" operation="${op}" functionality="CATEGORY"
                                 tabindex="6"
                                 styleClass="${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="CATEGORY"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew" tabindex="7">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="8">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>

</html:form>

<app2:checkAccessRight functionality="CATEGORYVALUE" permission="VIEW">
    <c:set var="type" value="${categoryForm.dtoMap['categoryType']}"/>
    <c:set var="categoryName"
           value="${categoryForm.dtoMap.categoryName}(${app2:getCategoryTableType(categoryForm.dtoMap.table, pageContext.request)})"/>
    <c:set var="categoryId" value="${categoryForm.dtoMap['categoryId']}"/>

    <c:if test="${op=='update' && (app2:isCategorySingleSelect(type) || app2:isCategoryCompoundSelect(type))}">
        <div class="embed-responsive embed-responsive-4by3 col-xs-12">
            <iframe class="embed-responsive-item"
                    name="frame1"
                    src="<app:url value="CategoryValue/List.do?categoryId=${categoryId}&dto(categoryId)=${categoryId}&dto(categoryName)=${app2:encode(categoryName)}&dto(categoryType)=${type}" />"
                    scrolling="no"
                    id="iFrameId"
                    frameborder="0">
            </iframe>
        </div>
    </c:if>
</app2:checkAccessRight>

<tags:jQueryValidation formName="categoryForm"/>
