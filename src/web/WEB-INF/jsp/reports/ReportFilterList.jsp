<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="FILTER" permission="CREATE">
        <html:form action="/Report/Filter/Forward/Create.do" styleClass="form-horizontal">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="reportFilterList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%" id="listF" action="Report/Filter/List.do"
                     imgPath="${baselayout}"
                     align="center">

            <app:url var="urlEdit"
                     value="/Report/Filter/Forward/Update.do?dto(filterId)=${listF.id_filter}&dto(op)=read&dto(aliasCondition)=${app2:encode(listF.aliasCondition)}"
                     enableEncodeURL="false"/>
            <app:url var="urlDelete"
                     value="/Report/Filter/Forward/Delete.do?dto(filterId)=${listF.id_filter}&dto(op)=read&dto(aliasCondition)=${app2:encode(listF.aliasCondition)}"
                     enableEncodeURL="false"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="FILTER" permission="VIEW">
                    <fanta:actionColumn name="edit" label="Common.update" title="Common.update" action="${urlEdit}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%" glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="FILTER" permission="DELETE">
                    <fanta:actionColumn name="delete" label="Common.delete" title="Common.delete"
                                        action="${urlDelete}" styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="label" title="Report.filter.label" action="${urlEdit}" styleClass="listItem"
                              headerStyle="listHeader" width="40%" orderable="true">
            </fanta:dataColumn>

            <fanta:dataColumn name="" title="Report.filter.operator" styleClass="listItem" renderData="false"
                              headerStyle="listHeader" width="15%">
                <c:out value="${app2:getOperatorMenssage(listF.operator,pageContext.request)}"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="" title="Report.filter.value" styleClass="listItem2" renderData="false"
                              headerStyle="listHeader" width="40%" orderable="false">
                <c:set var="values"
                       value="${app2:getValuesOfFilter(listF.id_filter,listF.filterType,pageContext.request)}"/>
                <c:set var="showMessage"
                       value="${app2:composeFilterValueToView(values,listF.operator,listF.tableRef,listF.columnRef,listF.path,listF.columnType,listF.filterType,pageContext.request)}"/>
                <c:choose>
                    <c:when test="${app2:isDbFilter(listF.tableRef,listF.columnRef,pageContext.request) and (empty showMessage)}">
                            <span style="color:#FF0000">
                                <fmt:message key="Report.filter.error.removedReference"/>
                            </span>
                    </c:when>
                    <c:otherwise>
                        <c:out value="${showMessage}"/>
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>

        </fanta:table>
    </div>
</div>