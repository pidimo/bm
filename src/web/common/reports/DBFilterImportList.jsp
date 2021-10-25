<%@ page import="com.jatun.titus.listgenerator.Constants"%>
<%@ include file="/Includes.jsp" %>
<% request.setAttribute("LIST_NAME", Constants.EXTERNAL_LIST_NAME);%>

<script language="JavaScript">
    <!--
    function select(fieldIdValue, fieldValue){
        var namePage=window.name;
        opener.putPopupValue(fieldIdValue, fieldValue, namePage);
    }
    //-->
</script>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td colspan="2" height="20" class="title">
            <fmt:message key="Report.filter.searchFilterValue"/>
        </td>
    </tr>
    <tr>
        <c:set var="columns" value="${columnList}"/>
        <!--count columns-->
        <c:forEach var="cList" items="${columns}" varStatus="x">
            <c:if test="${x.last}">
                <c:set var="count" value="${x.count}"/>
            </c:if>
        </c:forEach>
        <html:form action="Report/Filter/ImportDBFilter.do" focus="parameter(${searchParam})">
            <td class="label" width="10%"><fmt:message key="Common.for"/></td>
            <td class="contain" width="90%">
                <html:text property="parameter(${searchParam})" styleClass="largeText"/>
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
            </td>
        </html:form>
    </tr>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="Report/Filter/ImportDBFilter.do" parameterName="${alphabetParam}"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
                <tr>
                    <td>
                        <%--<fanta:table list="EXTERNALLIST" width="100%" id="listE" action="Report/Filter/ImportDBFilter.do"
                                     imgPath="${baselayout}" align="center">

                            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                                <fanta:actionColumn name="" title="Common.select" useJScript="true" action="javascript:select('aa', 'sss', 'aaa');"
                                    styleClass="listItem" headerStyle="listHeader" width="50%"
                                    image="${baselayout}/img/import.gif"/>
                            </fanta:columnGroup>

                            <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip"
                                              headerStyle="listHeader" width="40%" />
                            <fanta:dataColumn name="cityName" styleClass="listItem2" title="Contact.city"
                                              headerStyle="listHeader" width="40%" orderable="true"/>
                        </fanta:table>--%>

                        <br/>
                        <fanta:table list="${LIST_NAME}" width="100%" id="listE" action="Report/Filter/ImportDBFilter.do"
                                     imgPath="${baselayout}" align="center">
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
                                    image="${baselayout}/img/import.gif"/>
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
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

