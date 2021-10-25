<%@ include file="/Includes.jsp" %>
<fmt:message var="datePattern" key="datePattern"/>

<c:set var="isFromAjax" value="${'true' eq param.isFromAjaxRequest}"/>

<c:if test="${isFromAjax}">
    <c:set var="dynamicSearchName" value="${param.dynamicSearchName}" scope="request"/>
    <c:set var="customSearchConfigUrl" value="${param.configUrlParam}" scope="request"/>
    <c:set var="fieldAlias" value="${param.fieldProperty}" scope="request"/>
    <c:set var="fieldOperator" value="${param.operatorProperty}" scope="request"/>
</c:if>

<c:if test="${not empty fieldOperator}">

    <tags:dynamicSearchableForm addForm="${isFromAjax}">
        <%--import the custom search config if this is configured--%>
        <%--In this jsp config file can be acces to 'fieldAlias', 'fieldOperator', etc.. in request--%>
        <c:if test="${not empty customSearchConfigUrl}">
            <c:set var="customProcessedVar">
                <c:import url="${customSearchConfigUrl}"/>
            </c:set>
        </c:if>

        <c:set var="isCustomSearchConfig" value="${not empty customProcessedVar}" scope="request"/>

        <c:if test="${isCustomSearchConfig}">
            <c:out value="${customProcessedVar}" escapeXml="false"/>
        </c:if>

        <html:hidden property="parameter(isCustomSearch_${fieldAlias})"
                     value="${isCustomSearchConfig ? 'true' : 'false'}"/>

        <%--if custom processed is empty, continue--%>
        <c:if test="${not isCustomSearchConfig}">
            <c:if test="${app2:isDynamicSearchOneBoxView(fieldOperator)}">
                <c:choose>
                    <c:when test="${app2:searchFieldIsDateType(dynamicSearchName, fieldAlias, pageContext.request)}">
                        <app:dateText property="parameter(${fieldAlias})" styleId="date" calendarPicker="true"
                                      datePatternKey="${datePattern}" convert="true"
                                      styleClass="text" maxlength="10"/>
                    </c:when>
                    <c:when test="${app2:searchFieldIsDecimalType(dynamicSearchName, fieldAlias, pageContext.request)}">
                        <app:numberText property="parameter(${fieldAlias})" styleClass="numberTextMedium" numberType="decimal"
                                maxInt="10" maxFloat="2"/>
                    </c:when>
                    <c:when test="${app2:searchFieldIsIntegerType(dynamicSearchName, fieldAlias, pageContext.request)}">
                        <app:text property="parameter(${fieldAlias})" styleClass="numberTextMedium"/>
                    </c:when>
                    <c:otherwise>
                        <app:text property="parameter(${fieldAlias})" styleClass="mediumText" maxlength="100"/>
                    </c:otherwise>
                </c:choose>
            </c:if>

            <c:if test="${app2:isDynamicSearchTwoBoxView(fieldOperator)}">
                <c:choose>
                    <c:when test="${app2:searchFieldIsDateType(dynamicSearchName, fieldAlias, pageContext.request)}">
                        <fmt:message key="Report.filter.from"/>&nbsp;
                        <app:dateText property="parameter(${fieldAlias}_1)" styleId="date1" calendarPicker="true"
                                      datePatternKey="${datePattern}" convert="true"
                                      styleClass="shortText" maxlength="10"/>
                        &nbsp;
                        <fmt:message key="Report.filter.to"/>
                        &nbsp;
                        <app:dateText property="parameter(${fieldAlias}_2)" styleId="date2" calendarPicker="true"
                                      datePatternKey="${datePattern}" convert="true"
                                      styleClass="shortText" maxlength="10"/>
                    </c:when>

                    <c:when test="${app2:searchFieldIsDecimalType(dynamicSearchName, fieldAlias, pageContext.request)}">
                        <fmt:message key="Report.filter.from"/>&nbsp;
                        <app:numberText property="parameter(${fieldAlias}_1)" styleClass="numberText" numberType="decimal"
                                maxInt="10" maxFloat="2"/>
                        &nbsp;
                        <fmt:message key="Report.filter.to"/>
                        &nbsp;
                        <app:numberText property="parameter(${fieldAlias}_2)" styleClass="numberText" numberType="decimal"
                                maxInt="10" maxFloat="2"/>
                    </c:when>

                    <c:when test="${app2:searchFieldIsIntegerType(dynamicSearchName, fieldAlias, pageContext.request)}">
                        <fmt:message key="Report.filter.from"/>&nbsp;
                        <app:text property="parameter(${fieldAlias}_1)" styleClass="numberText"/>
                        &nbsp;
                        <fmt:message key="Report.filter.to"/>
                        &nbsp;
                        <app:text property="parameter(${fieldAlias}_2)" styleClass="numberText"/>
                    </c:when>

                    <c:otherwise>
                        <fmt:message key="Report.filter.from"/>&nbsp;
                        <app:text property="parameter(${fieldAlias}_1)" styleClass="shortText" maxlength="100"/>
                        &nbsp;
                        <fmt:message key="Report.filter.to"/>
                        &nbsp;
                        <app:text property="parameter(${fieldAlias}_2)" styleClass="shortText" maxlength="100"/>
                    </c:otherwise>
                </c:choose>
            </c:if>

            <c:if test="${app2:isDynamicSearchSelectView(fieldOperator)}">
                <c:set var="categoryVar" value="${app2:findCategoryFieldWithValues(dynamicSearchName, fieldAlias, pageContext.request)}"/>
                <c:choose>
                    <c:when test="${not empty categoryVar}">
                        <c:set var="categoryValuesVar" value="${app2:getCategoryValues(categoryVar.categoryId, pageContext.request)}"/>

                        <html:select property="parameter(${fieldAlias})"
                                     styleClass="mediumSelect">
                            <html:option value=""/>
                            <c:forEach var="value" items="${categoryValuesVar}">
                                <html:option value="${value.categoryValueId}">${value.categoryValueName}</html:option>
                            </c:forEach>
                        </html:select>
                    </c:when>
                </c:choose>
            </c:if>
        </c:if>
    </tags:dynamicSearchableForm>

</c:if>

