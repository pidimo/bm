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

    <tags:bootstrapDynamicSearchableForm addForm="${isFromAjax}">
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
                       <div class="input-group date">
                           <app:dateText property="parameter(${fieldAlias})"
                                         styleId="date"
                                         mode="bootstrap"
                                         calendarPicker="true"
                                         datePatternKey="${datePattern}"
                                         convert="true"
                                         styleClass="text ${app2:getFormInputClasses()}" maxlength="10"/>
                       </div>
                    </c:when>
                    <c:when test="${app2:searchFieldIsDecimalType(dynamicSearchName, fieldAlias, pageContext.request)}">
                        <app:numberText
                                property="parameter(${fieldAlias})"
                                styleClass="numberTextMedium ${app2:getFormInputClasses()}"
                                numberType="decimal"
                                maxInt="10" maxFloat="2"/>
                    </c:when>
                    <c:when test="${app2:searchFieldIsIntegerType(dynamicSearchName, fieldAlias, pageContext.request)}">
                        <app:text property="parameter(${fieldAlias})"
                                  styleClass="numberTextMedium ${app2:getFormInputClasses()}"/>
                    </c:when>
                    <c:otherwise>
                        <app:text property="parameter(${fieldAlias})"
                                  styleClass="mediumText ${app2:getFormInputClasses()}" maxlength="100"/>
                    </c:otherwise>
                </c:choose>
            </c:if>

            <c:if test="${app2:isDynamicSearchTwoBoxView(fieldOperator)}">
                <c:choose>
                    <c:when test="${app2:searchFieldIsDateType(dynamicSearchName, fieldAlias, pageContext.request)}">
                        <fmt:message key="Report.filter.from" var="from"/>
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="input-group date">
                                    <app:dateText property="parameter(${fieldAlias}_1)"
                                                  styleId="date1"
                                                  calendarPicker="true"
                                                  placeHolder="${from}"
                                                  mode="bootstrap"
                                                  datePatternKey="${datePattern}" convert="true"
                                                  styleClass="shortText ${app2:getFormInputClasses()}" maxlength="10"/>
                                </div>
                            </div>
                            <fmt:message key="Report.filter.to" var="to"/>
                            <div class="col-xs-6">
                                <div class="input-group date">
                                    <app:dateText property="parameter(${fieldAlias}_2)"
                                                  styleId="date2"
                                                  placeHolder="${to}"
                                                  mode="bootstrap"
                                                  calendarPicker="true"
                                                  datePatternKey="${datePattern}" convert="true"
                                                  styleClass="shortText ${app2:getFormInputClasses()}" maxlength="10"/>
                                </div>
                            </div>
                        </div>
                    </c:when>

                    <c:when test="${app2:searchFieldIsDecimalType(dynamicSearchName, fieldAlias, pageContext.request)}">
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="row">
                                    <label class="control-label col-xs-3">
                                        <fmt:message key="Report.filter.from"/>
                                    </label>
                                   <div class="col-xs-9">
                                       <app:numberText property="parameter(${fieldAlias}_1)"
                                                       styleClass="numberText ${app2:getFormInputClasses()}"
                                                       numberType="decimal"
                                                       maxInt="10" maxFloat="2"/>
                                   </div>
                                </div>
                            </div>
                            <div class="col-xs-6">
                                <div class="row">
                                    <label class="control-label col-xs-3" for="${fieldAlias}_2_id">
                                        <fmt:message key="Report.filter.to"/>
                                    </label>
                                    <div class="col-xs-9">
                                        <app:numberText property="parameter(${fieldAlias}_2)"
                                                        styleId="${fieldAlias}_2_id"
                                                        styleClass="numberText ${app2:getFormInputClasses()}"
                                                        numberType="decimal"
                                                        maxInt="10" maxFloat="2"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>

                    <c:when test="${app2:searchFieldIsIntegerType(dynamicSearchName, fieldAlias, pageContext.request)}">
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="row">
                                    <label class="control-label col-xs-3" for="${fieldAlias}_2_id">
                                        <fmt:message key="Report.filter.from"/>
                                    </label>
                                    <div class="col-xs-9">
                                        <app:text property="parameter(${fieldAlias}_1)"
                                                  styleClass="numberText ${app2:getFormInputClasses()}"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-6">
                                <div class="row">
                                    <label class="control-label col-xs-3" for="${fieldAlias}_2_id">
                                        <fmt:message key="Report.filter.to"/>
                                    </label>
                                    <div class="col-xs-9">
                                        <app:text property="parameter(${fieldAlias}_2)"
                                                  styleId="${fieldAlias}_2_id"
                                                  styleClass="numberText ${app2:getFormInputClasses()}"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <fmt:message key="Report.filter.from"/>
                        <app:text property="parameter(${fieldAlias}_1)"
                                  styleClass="shortText ${app2:getFormInputClasses()}" maxlength="100"/>
                        <fmt:message key="Report.filter.to"/>
                        <app:text property="parameter(${fieldAlias}_2)"
                                  styleClass="shortText ${app2:getFormInputClasses()}" maxlength="100"/>
                    </c:otherwise>
                </c:choose>
            </c:if>

            <c:if test="${app2:isDynamicSearchSelectView(fieldOperator)}">
                <c:set var="categoryVar" value="${app2:findCategoryFieldWithValues(dynamicSearchName, fieldAlias, pageContext.request)}"/>
                <c:choose>
                    <c:when test="${not empty categoryVar}">
                        <c:set var="categoryValuesVar" value="${app2:getCategoryValues(categoryVar.categoryId, pageContext.request)}"/>

                        <html:select property="parameter(${fieldAlias})"
                                     styleClass="mediumSelect ${app2:getFormSelectClasses()}">
                            <html:option value=""/>
                            <c:forEach var="value" items="${categoryValuesVar}">
                                <html:option value="${value.categoryValueId}">${value.categoryValueName}</html:option>
                            </c:forEach>
                        </html:select>
                    </c:when>
                </c:choose>
            </c:if>
        </c:if>
    </tags:bootstrapDynamicSearchableForm>

</c:if>

