<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="reportQueryParamList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%"
                     id="listF"
                     action="Report/QueryParam/List.do"
                     imgPath="${baselayout}"
                     align="center">

            <app:url var="urlEdit"
                     value="/Report/QueryParam/Forward/Update.do?dto(reportQueryParamId)=${listF.reportQueryParamId}&dto(parameterName)=${app2:encode(listF.parameterName)}&dto(op)=readAll"
                     enableEncodeURL="false"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="${queryParamFunctionality}" permission="VIEW">
                    <fanta:actionColumn name="edit" label="Common.update" title="Common.update" action="${urlEdit}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="parameterName" title="Report.queryParam.parameterName" action="${urlEdit}"
                              styleClass="listItem"
                              headerStyle="listHeader" width="30%" orderable="true">
            </fanta:dataColumn>

            <fanta:dataColumn name="label" title="Report.filter.label" styleClass="listItem"
                              headerStyle="listHeader" width="30%" orderable="true">
            </fanta:dataColumn>

            <fanta:dataColumn name="" title="Report.filter.value" styleClass="listItem2" renderData="false"
                              headerStyle="listHeader" width="35%" orderable="false">
                <c:if test="${not empty listF.filterId}">
                    <c:set var="values"
                           value="${app2:getValuesOfFilter(listF.filterId,listF.filterType,pageContext.request)}"/>
                    <c:set var="showMessage"
                           value="${app2:composeFilterValueToView(values,listF.operator,listF.tableRef,listF.columnRef,listF.path,listF.columnType,listF.filterType,pageContext.request)}"/>
                    <c:choose>
                        <c:when test="${app2:isDbFilter(listF.tableRef,listF.columnRef,pageContext.request) and (empty showMessage) and (not empty values)}">
                                <span style="color:#FF0000">
                                    <fmt:message key="Report.filter.error.removedReference"/>
                                </span>
                        </c:when>
                        <c:otherwise>
                            <c:out value="${showMessage}"/>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </fanta:dataColumn>

        </fanta:table>
    </div>
</div>

