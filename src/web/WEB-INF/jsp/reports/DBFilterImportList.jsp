<%@ page import="com.jatun.titus.listgenerator.Constants"%>
<%@ include file="/Includes.jsp" %>
<% request.setAttribute("LIST_NAME", Constants.EXTERNAL_LIST_NAME);%>

<script language="JavaScript">
    <!--
    function select(fieldIdValue, fieldValue){
        var namePage=window.name;

        parent.putPopupValue(fieldIdValue, fieldValue, namePage);
    }
    //-->
</script>

<div class="${app2:getListWrapperClasses()}">

        <c:set var="columns" value="${columnList}"/>
        <!--count columns-->
        <c:forEach var="cList" items="${columns}" varStatus="x">
            <c:if test="${x.last}">
                <c:set var="count" value="${x.count}"/>
            </c:if>
        </c:forEach>
        <html:form action="Report/Filter/ImportDBFilter.do" focus="parameter(${searchParam})" styleClass="form-horizontal">
        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="searchParam_id">
                <fmt:message key="Common.for"/>
            </label>
            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(${searchParam})" styleClass="largeText ${app2:getFormInputClasses()}" styleId="searchParam_id"/>
                    <span class="input-group-btn">
                        <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message key="Common.go"/></html:submit>
                    </span>
                </div>
            </div>
        </div>
        </html:form>


    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="Report/Filter/ImportDBFilter.do" mode="bootstrap" parameterName="${alphabetParam}"/>
    </div>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="${LIST_NAME}"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%"
                     id="listE"
                     action="Report/Filter/ImportDBFilter.do"
                     imgPath="${baselayout}"
                     align="center">
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="1%">
                <c:choose>
                    <c:when test="${not empty isCompoundColumn}">
                        <c:set var="labelValue" value="${app2:compoundColumnConverter(listE,isCompoundColumn,columnAliasMap,pageContext.request)}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="labelValue" value="${app2:applyConverterDbToViewInSimpleField(searchParamPath, app2:getMapValueByKey(listE,searchParam), pageContext.request)}"/>
                    </c:otherwise>
                </c:choose>
                <fanta:actionColumn name="" title="Common.select" useJScript="true" action="javascript:select('${app2:getPrimaryKeyValue(listE, primaryKeyList)}','${app2:jscriptEncode(labelValue)}');"
                                    styleClass="listItem" headerStyle="listHeader" width="100%"
                                    glyphiconClass="${app2:getClassGlyphImport()}"/>
            </fanta:columnGroup>

            <c:forEach var="cList" items="${columns}" varStatus="x">

                <c:set var="customStyle" value=""/>
                <c:if test="${cList.isNumericType}">
                    <c:set var="customStyle" value="text-align:right"/>
                </c:if>
                <c:choose>
                    <c:when test="${not empty cList.compoundColumn}">
                        <fanta:dataColumn name="${cList.columnOrder}" styleClass="${(x.last)?'listItem2':'listItem'}"
                                          title="${cList.resource}"
                                          headerStyle="listHeader" width="${100/count}%" renderData="false" orderable="true" style="${customStyle}">
                            <c:out value="${app2:compoundColumnConverter(listE,cList.compoundColumn,cList.aliasMap,pageContext.request)}"/>
                        </fanta:dataColumn>
                    </c:when>
                    <c:otherwise>
                        <fanta:dataColumn name="${cList.column}" styleClass="${(x.last)?'listItem2':'listItem'}" title="${cList.resource}"
                                          headerStyle="listHeader" width="${100/count}%" renderData="false" orderable="true" style="${customStyle}">
                            <c:out value="${app2:applyConverterDbToViewInSimpleField(cList.fieldPath, app2:getMapValueByKey(listE,cList.column), pageContext.request)}"/>
                        </fanta:dataColumn>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </fanta:table>
    </div>
</div>


