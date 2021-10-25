<%@ include file="/Includes.jsp" %>

<c:set var="categoryId" value="${param['dto(categoryId)']}"/>
<c:set var="categoryName" value="${param['dto(categoryName)']}"/>

<html:form action="${action}" focus="dto(categoryValueName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(categoryId)"/>
        <html:hidden property="dto(categoryName)"/>
        <html:hidden property="dto(tableId)"/>
        <html:hidden property="dto(op)" value="${op}"/>

        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(categoryValueId)" styleId="categoryValueId"/>
        </c:if>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="CategoryValue.category"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                        <html:hidden property="dto(categoryId)" value="${categoryId}"/>
                            ${categoryName}
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="categoryValueName_id">
                        <fmt:message key="CategoryValue.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(categoryValueName)" styleClass="${app2:getFormInputClasses()} largetext"
                                  styleId="categoryValueName_id"
                                  maxlength="40"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
            <fieldset>
                <c:set var="hasSubCategories"
                       value="${app2:hasSubCategories(categoryValueForm.dtoMap.categoryId, pageContext.request)}"/>
                <html:hidden property="dto(hasSubCategories)" value="${hasSubCategories}"/>
                <c:if test="${hasSubCategories}">
                    <c:if test="${null != childrenCategories && fn:length(childrenCategories) > 0}">
                        <legend class="title">
                            <fmt:message key="CategoryValue.relateToSubCategory"/>
                        </legend>
                    </c:if>
                    <c:forEach var="childrenCategory" items="${childrenCategories}">
                        <c:set var="canUpdateCategory" value="canUpdateCategory_${childrenCategory.categoryId}"/>
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}" for="selectedCategories">
                                <c:out value="${childrenCategory.categoryName}"/>
                                <html:hidden property="dto(categoryName_${childrenCategory.categoryId})"
                                             value="${childrenCategory.categoryName}"/>
                                <html:hidden property="dto(categoryIds)" value="${childrenCategory.categoryId}"
                                             styleId="dto(categoryIds)"/>
                            </label>

                            <div class="${app2:getFormContainClasses(null)}">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(${childrenCategory.categoryId})"
                                                   value="true"
                                                   styleClass="checkbox"
                                                   styleId="selectedCategories"
                                                   disabled="${'delete' == op || false == categoryValueForm.dtoMap[canUpdateCategory]}"/>
                                    <label for="selectedCategories"></label>
                                    <c:if test="${'delete' == op || false == categoryValueForm.dtoMap[canUpdateCategory]}">
                                        <html:hidden property="dto(canUpdateCategory_${childrenCategory.categoryId})"/>
                                    </c:if>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:if>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="CATEGORYVALUE"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 property="create">${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="CATEGORYVALUE"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="categoryValueForm"/>