<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
    <td>

<html:form action="/Report/Totalize/Create.do">

    <html:hidden property="dto(op)" value="create"/>
    <html:hidden property="dto(reportId)" value="${param.reportId}"/>
    <html:hidden property="dto(sumVersion)" styleId="sumVersionId"/>

    <table cellSpacing=0 cellPadding=4 width="70%" border=0 align="center">
    <tr>
        <td class="button">
            <app2:securitySubmit operation="UPDATE" functionality="TOTALIZE"
                                 styleClass="button"><fmt:message key="Common.save"/></app2:securitySubmit>
        <%--<html:submit styleClass="button"><fmt:message   key="Common.save"/></html:submit>--%>
        </td>
    </tr>
    </table>

    <table border="0" cellpadding="0" cellspacing="0" width="70%" align="center" class="container">
    <tr>
        <td class="title" colspan="5" ><fmt:message key="Totalize.title"/></td>
    </tr>

    <fmt:message key="Totalize.sum" var="sum" />
    <fmt:message key="Totalize.average" var="average" />
    <fmt:message key="Totalize.largestValue" var="largestValue" />
    <fmt:message key="Totalize.smallestValue" var="smallestValue" />


    <tr>
    <td class="label" width="50%"><fmt:message key="Totalize.column"/></td>
    <td class="label" width="12%">${sum}</td>
    <td class="label" width="12%">${average}</td>
    <td class="label" width="12%">${largestValue}</td>
    <td class="label" width="14%">${smallestValue}</td>
    </tr>

    <html:hidden property="dto(msgSum)" value="${sum}"/>
    <html:hidden property="dto(msgAverage)" value="${average}"/>
    <html:hidden property="dto(msgLargestValue)" value="${largestValue}"/>
    <html:hidden property="dto(msgSmallestValue)" value="${smallestValue}"/>



    <c:forEach  var="column" items="${dto.structure}">
    <c:set var="columnName" value="${app2:composeColumnLabelByTitusPath(column.path, column.label, pageContext.request)}"/>
    <html:hidden property="dto(columnName_${column.columnId})" value="${columnName}" styleId="columnNameId${column.columnId}"/>

        <tr>
            <td class="contain" >${columnName}</td>
            <c:forEach var="totalize" items="${column.totalizers}" >

                <c:if test="${null != totalize.totalizeId}" >
                    <td class="contain" align="center" >
                    <c:set var="checkIdentifier" value="${column.columnId}_${totalize.totalizeId}_${totalize.totalizeType}_${totalize.version}"/>
                    <html:hidden property="dto(checked_${checkIdentifier})" value="true" styleId="checkedId${checkIdentifier}"/>
                    <input type="checkbox" name="dto(checkBox_${checkIdentifier})"
class="adminCheckBox"  checked  >
                    </td>
                </c:if>
                <c:if test="${null == totalize.totalizeId}" >
                    <td class="contain" align="center" >
                    <input type="checkbox" name="dto(checkBox_${column.columnId}_NONE_${totalize.totalizeType}_NONE)"
class="adminCheckBox" >
                    </td>
                </c:if>

            </c:forEach>
        </tr>
    </c:forEach>


    </table>

    <table border="0" cellpadding="0" cellspacing="0" width="70%" align="center" class="container">
    <tr>
        <td class="title" colspan="5" ><fmt:message key="Totalize.general.title"/></td>
    </tr>
    <tr>
        <fmt:message key="Totalize.sumRecords" var="resource" />
        <html:hidden property="dto(sumRecordsResource)" value="${resource}"/>  
        <td class="label" width="30%"  >${resource}</td>
        <td class="contain" width="70%"  ><html:checkbox property="dto(sumRecords)" styleClass="adminCheckBox"/>  </td>
    </tr>
    </table>

    <table cellSpacing=0 cellPadding=4 width="70%" border=0 align="center">
    <tr>
        <td class="button">
            <app2:securitySubmit operation="UPDATE" functionality="TOTALIZE"
                                 styleClass="button"><fmt:message key="Common.save"/></app2:securitySubmit>
        <%--<html:submit styleClass="button"><fmt:message   key="Common.save"/></html:submit>--%>
        </td>
    </tr>
    </table>

</html:form>
    </td>
</tr>
</table>
