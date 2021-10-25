<%@ include file="/Includes.jsp" %>

${app2:readCacheInAJAXRequest(pageContext, pageContext.request)}

<tags:initBootstrapDatepicker/>
<c:set var="category" value="${app2:getCategory(param['dto(categoryId)'], pageContext.request)}"/>

<c:if test="${app2:isCategoryCompoundSelect(category.categoryType) || app2:isCategoryFreeText(category.categoryType)}">
    <c:set var="vAlign" value="style=\"vertical-align:top\""/>
</c:if>

<html:hidden property="dto(pageCategories)" value="${category.categoryId}" styleId="dto(pageCategories)"/>
<html:hidden property="dto(categoryType_${category.categoryId})" value="${category.categoryType}"/>
<html:hidden property="dto(categoryName_${category.categoryId})" value="${category.categoryName}"/>
<div class="${app2:getFormGroupClasses()}">
    <label class="${app2:getFormLabelRenderCategory()}">
        ${app2:filterAjaxResponse(category.categoryName)}
    </label>

    <div class="${app2:getFormContainRenderCategory('delete' == operation)}">
        <c:if test="${app2:isCategoryCompoundSelect(category.categoryType)}">
            <html:hidden property="dto(selected_${category.categoryId})"
                         value=""
                         styleId="hidden_selected_${category.categoryId}_Id"/>

            <c:set var="allCategoryValues"
                   value="${app2:getCategoryValues(category.categoryId, pageContext.request)}"/>
            <c:set var="selectedKeys"
                   value="${app2:getFormField(formName, category.categoryId, pageContext.request)}"
                   scope="request"/>

            ${app2:buildMultipleSelectValues(allCategoryValues, selectedKeys, pageContext.request)}

            <table class="col-xs-12">
                <tr>
                    <td class="col-xs-11 paddingLeftRemove paddingRightRemove">
                        <html:select
                                property="dto(${category.categoryId})"
                                multiple="true"
                                styleClass="multipleSelectFilter ${app2:getFormSelectClasses()}"
                                tabindex="${elementCounter}"
                                styleId="selected_${category.categoryId}_Id"
                                disabled="${'delete' == operation}">
                            <c:forEach var="value" items="${selectedElements}">
                                <html:option
                                        value="${value.categoryValueId}">${value.categoryValueName}</html:option>
                            </c:forEach>
                        </html:select>

                        <html:select
                                property="dto(residual_${category.categoryId})"
                                multiple="true"
                                styleClass="multipleSelectFilter ${app2:getFormSelectClasses()}"
                                tabindex="${elementCounter}"
                                styleId="residual_${category.categoryId}_Id"
                                disabled="${'delete' == operation}"
                                style="display:none;">
                            <c:forEach var="value" items="${residualElements}">
                                <html:option
                                        value="${value.categoryValueId}">${value.categoryValueName}</html:option>
                            </c:forEach>
                        </html:select>
                    </td>
                    <td class="col-xs-1 paddingLeftRemove paddingRightRemove" style="vertical-align: top">
                        <tags:bootstrapMultipleSelectModal selectedBoxId="selected_${category.categoryId}_Id"
                                                           availableBoxId="residual_${category.categoryId}_Id"
                                                           modalTitle="${category.categoryName}"
                                                           hide="${'delete' == operation}"/>
                    </td>
                </tr>
            </table>
        </c:if>

        <c:if test="${app2:isCategorySingleSelect(category.categoryType)}">
            <html:select property="dto(${category.categoryId})"
                         styleClass="select ${app2:getFormSelectClasses()}"
                         tabindex="${elementCounter}"
                         readonly="${'delete' == operation}"
                         style="width:${generalWidth};">
                <html:option value=""/>
                <c:forEach var="value"
                           items="${app2:getCategoryValues(category.categoryId, pageContext.request)}">
                    <html:option value="${value.categoryValueId}">${value.categoryValueName}</html:option>
                </c:forEach>
            </html:select>
        </c:if>

        <c:if test="${app2:isCategoryDate(category.categoryType)}">
            <fmt:message var="datePattern" key="datePattern"/>
            <div class="input-group date">
                <app:dateText
                        property="dto(${category.categoryId})"
                        datePatternKey="${datePattern}"
                        styleId="dateTextId_${category.categoryId}"
                        styleClass="${app2:getFormInputClasses()}"
                        maxlength="10"
                        mode="bootstrap"
                        calendarPicker="true"
                        tabindex="${elementCounter}"
                        view="${'delete' == operation}"/>
            </div>
        </c:if>

        <c:if test="${app2:isCategoryDecimal(category.categoryType)}">
            <app:numberText
                    property="dto(${category.categoryId})"
                    styleClass="numberText ${app2:getFormInputClasses()}"
                    numberType="decimal"
                    maxInt="10"
                    maxFloat="2"
                    tabindex="${elementCounter}"
                    view="${'delete' == operation}"/>
        </c:if>

        <c:if test="${app2:isCategoryInteger(category.categoryType)}">
            <app:text
                    property="dto(${category.categoryId})"
                    styleClass="numberText ${app2:getFormInputClasses()}"
                    tabindex="${elementCounter}"
                    view="${'delete' == operation}"/>
        </c:if>

        <c:if test="${app2:isCategoryText(category.categoryType)}">
            <app:text
                    property="dto(${category.categoryId})"
                    styleClass="text ${app2:getFormInputClasses()}"
                    maxlength="80"
                    tabindex="${elementCounter}"
                    view="${'delete' == operation}"/>
        </c:if>

        <c:if test="${app2:isCategoryLink(category.categoryType)}">
            <c:set var="linkVar" value="${app2:getFormField(formName, category.categoryId, pageContext.request)}"/>
            <c:if test="${null != linkVar && not empty linkVar}">
                <div class="row col-xs-11">
            </c:if>
            <app:text
                    property="dto(${category.categoryId})"
                    styleClass="text ${app2:getFormInputClasses()}"
                    tabindex="${elementCounter}"
                    view="${'delete' == operation}"
                    style="width:${generalWidth};"
                    maxlength="249"/>
            <c:if test="${null != linkVar && not empty linkVar}">
                </div>
            </c:if>
            <c:if test="${null != linkVar && not empty linkVar}">
                <c:if test="${fn:indexOf(linkVar, 'http://') == -1}">
                    <c:set var="linkVar" value="http://${linkVar}"/>
                </c:if>
                <span class="pull-right">
                    <a href="${linkVar}" target="_blank" class="folderTabLink ${app2:getFormButtonClasses()}" title="${linkVar}">
                        <span class="glyphicon glyphicon-globe"></span>
                    </a>
                </span>
            </c:if>
        </c:if>

        <c:if test="${app2:isCategoryFreeText(category.categoryType)}">
            <html:textarea
                    property="dto(${category.categoryId})"
                    styleClass="mediumDetail ${app2:getFormInputClasses()}"
                    tabindex="${elementCounter}"
                    readonly="${operation == 'delete'}"
                    style="height:70px;width:${generalWidth};"/>
        </c:if>

        <c:if test="${app2:isCategoryAttach(category.categoryType)}">
            <fmt:message key="Common.download" var="downloadMsg"/>
            <c:set var="attachId" value="${app2:getFormField(formName, category.categoryId, pageContext.request)}"/>

            <c:if test="${'delete' != operation}">
                <c:if test="${null != attachId && '' != attachId}">
                    <div class="row col-xs-9 col-lg-10">
                </c:if>
                <tags:bootstrapFile property="dto(${category.categoryId})"
                                    tabIndex="${elementCounter}"/>
                <c:if test="${null != attachId && '' != attachId}">
                    </div>
                </c:if>
            </c:if>

            <html:hidden property="dto(attachId_${category.categoryId})" value="${attachId}"
                         styleId="attachId_${category.categoryId}"/>
            <c:if test="${null != attachId && '' != attachId}">
                <div class="pull-right" id="downloadLinkId_${category.categoryId}">
                    <c:set var="character" value="?"/>
                    <c:if test="${fn:indexOf(downloadAction,'?') != -1}">
                        <c:set var="character" value="&"/>
                    </c:if>

                    <app:link
                            action="${downloadAction}${character}dto(attachId)=${attachId}&dto(fileName)=${app2:encode(category.categoryName)}"
                            contextRelative="true" title="${downloadMsg}" addModuleParams="true" styleClass="${app2:getFormButtonClasses()}">
                        <span alt="${downloadMsg}" class="glyphicon glyphicon-download-alt"></span>
                    </app:link>

                    <c:if test="${'delete' != operation}">
                        <a href="javascript:hideAttachDownloadDiv(${category.categoryId});" class="${app2:getFormButtonClasses()}">
                            <span alt="<fmt:message key="Category.error.deleteAttach"/>"
                                  title="<fmt:message key="Category.error.deleteAttach"/>" class="glyphicon glyphicon-trash"></span>
                        </a>
                    </c:if>
                </div>
            </c:if>
        </c:if>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>

