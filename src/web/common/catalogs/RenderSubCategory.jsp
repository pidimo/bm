<%@ include file="/Includes.jsp" %>

${app2:readCacheInAJAXRequest(pageContext, pageContext.request)}


<c:set var="category" value="${app2:getCategory(param['dto(categoryId)'], pageContext.request)}"/>

<c:if test="${app2:isCategoryCompoundSelect(category.categoryType) || app2:isCategoryFreeText(category.categoryType)}">
    <c:set var="vAlign" value="style=\"vertical-align:top\""/>
</c:if>

<html:hidden property="dto(pageCategories)" value="${category.categoryId}" styleId="dto(pageCategories)"/>
<html:hidden property="dto(categoryType_${category.categoryId})" value="${category.categoryType}"/>
<html:hidden property="dto(categoryName_${category.categoryId})" value="${category.categoryName}"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td class="label" ${vAlign} width="${labelWidth}%">${app2:filterAjaxResponse(category.categoryName)}</td>
        <td class="contain" width="${containWidth}%">
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

                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td>
                            <fmt:message key="common.selected"/>
                            <br>
                            <html:select
                                    property="dto(${category.categoryId})"
                                    multiple="true"
                                    styleClass="multipleSelectFilter"
                                    tabindex="${elementCounter}"
                                    styleId="selected_${category.categoryId}_Id"
                                    disabled="${'delete' == operation}"
                                    style="width:${generalWidth};">
                                <c:forEach var="value" items="${selectedElements}">
                                    <html:option
                                            value="${value.categoryValueId}">${value.categoryValueName}</html:option>
                                </c:forEach>
                            </html:select>

                        </td>
                        <td>
                            <html:button property="select"
                                         onclick="javascript:selectedOption_II('residual_${category.categoryId}_Id','selected_${category.categoryId}_Id');"
                                         styleClass="adminLeftArrow" titleKey="Common.add"
                                         disabled="${'delete' == operation}">&nbsp;
                            </html:button>
                            <br>
                            <br>
                            <html:button property="select"
                                         onclick="javascript:selectedOption_II('selected_${category.categoryId}_Id','residual_${category.categoryId}_Id');"
                                         styleClass="adminRighttArrow" titleKey="Common.delete"
                                         disabled="${'delete' == operation}">&nbsp;
                            </html:button>
                        </td>
                        <td>
                            <fmt:message key="common.available"/>
                            <br>
                            <html:select
                                    property="dto(residual_${category.categoryId})"
                                    multiple="true"
                                    styleClass="multipleSelectFilter"
                                    tabindex="${elementCounter}"
                                    styleId="residual_${category.categoryId}_Id"
                                    disabled="${'delete' == operation}"
                                    style="width:${generalWidth};">
                                <c:forEach var="value" items="${residualElements}">
                                    <html:option
                                            value="${value.categoryValueId}">${value.categoryValueName}</html:option>
                                </c:forEach>
                            </html:select>
                        </td>
                    </tr>
                </table>
            </c:if>

            <c:if test="${app2:isCategorySingleSelect(category.categoryType)}">
                <html:select property="dto(${category.categoryId})"
                             styleClass="select"
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
                <app:dateText
                        property="dto(${category.categoryId})"
                        datePatternKey="${datePattern}"
                        styleId="dateTextId_${category.categoryId}"
                        styleClass="dateText_${elementCounter}"
                        maxlength="10"
                        calendarPicker="true"
                        tabindex="${elementCounter}"
                        view="${'delete' == operation}"/>
            </c:if>

            <c:if test="${app2:isCategoryDecimal(category.categoryType)}">
                <app:numberText
                        property="dto(${category.categoryId})"
                        styleClass="numberText"
                        numberType="decimal"
                        maxInt="10"
                        maxFloat="2"
                        tabindex="${elementCounter}"
                        view="${'delete' == operation}"/>
            </c:if>

            <c:if test="${app2:isCategoryInteger(category.categoryType)}">
                <app:text
                        property="dto(${category.categoryId})"
                        styleClass="numberText"
                        tabindex="${elementCounter}"
                        view="${'delete' == operation}"/>
            </c:if>

            <c:if test="${app2:isCategoryText(category.categoryType)}">
                <app:text
                        property="dto(${category.categoryId})"
                        styleClass="text"
                        maxlength="80"
                        tabindex="${elementCounter}"
                        view="${'delete' == operation}"/>
            </c:if>

            <c:if test="${app2:isCategoryLink(category.categoryType)}">
                <app:text
                        property="dto(${category.categoryId})"
                        styleClass="text"
                        tabindex="${elementCounter}"
                        view="${'delete' == operation}"
                        style="width:${generalWidth};"
                        maxlength="249"/>
                <c:set var="linkVar" value="${app2:getFormField(formName, category.categoryId, pageContext.request)}"/>
                <c:if test="${null != linkVar && not empty linkVar}">
                    <c:if test="${fn:indexOf(linkVar, 'http://') == -1}">
                        <c:set var="linkVar" value="http://${linkVar}"/>
                    </c:if>
                    <a href="${linkVar}" target="_blank" class="folderTabLink" title="${linkVar}">
                        <img src="<c:out value="${sessionScope.baselayout}"/>/img/link.gif"
                             alt="${linkVar}" border="0" align="middle"/>
                    </a>
                </c:if>
            </c:if>

            <c:if test="${app2:isCategoryFreeText(category.categoryType)}">
                <html:textarea
                        property="dto(${category.categoryId})"
                        styleClass="mediumDetail"
                        tabindex="${elementCounter}"
                        readonly="${operation == 'delete'}"
                        style="height:70px;width:${generalWidth};"/>
            </c:if>

            <c:if test="${app2:isCategoryAttach(category.categoryType)}">
                <fmt:message key="Common.download" var="downloadMsg"/>
                <table border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <c:if test="${'delete' != operation}">
                            <td>
                                <html:file property="dto(${category.categoryId})" tabindex="${elementCounter}"/>&nbsp;
                            </td>
                        </c:if>
                        <td>
                            <c:set var="attachId"
                                   value="${app2:getFormField(formName, category.categoryId, pageContext.request)}"/>
                            <html:hidden property="dto(attachId_${category.categoryId})" value="${attachId}"
                                         styleId="attachId_${category.categoryId}"/>
                            <c:if test="${null != attachId && '' != attachId}">
                                <div id="downloadLinkId_${category.categoryId}">
                                    <c:set var="character" value="?"/>
                                    <c:if test="${fn:indexOf(downloadAction,'?') != -1}">
                                        <c:set var="character" value="&"/>
                                    </c:if>

                                    <app:link
                                            action="${downloadAction}${character}dto(attachId)=${attachId}&dto(fileName)=${app2:encode(category.categoryName)}"
                                            contextRelative="true" title="${downloadMsg}" addModuleParams="true">
                                        <img src="<c:out value="${sessionScope.baselayout}"/>/img/openfile.png"
                                             alt="${downloadMsg}" border="0" align="middle"/>
                                    </app:link>

                                    <c:if test="${'delete' != operation}">
                                        <a href="javascript:hideAttachDownloadDiv(${category.categoryId});">
                                            <img src="${sessionScope.baselayout}/img/tinydelete.gif"
                                                 alt="<fmt:message key="Category.error.deleteAttach"/>"
                                                 title="<fmt:message key="Category.error.deleteAttach"/>"
                                                 border="0" align="middle"/>
                                        </a>
                                    </c:if>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                </table>
            </c:if>

        </td>
    </tr>
</table>

