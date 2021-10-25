<%@ include file="/Includes.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
<html:form action="/ChartType/Delete.do" focus="deleteButton">

<c:set var="op" value="delete"/>
<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(reportId)" value="${param['reportId']}"/>
<html:hidden property="dto(chartId)" />
<html:hidden property="dto(title)" />

    <tr>
        <td height="20" class="title"><fmt:message key="Report.chart.msg.delete"/></td>
    </tr>
    <tr>
        <td class="contain"><fmt:message key="Report.chart.msg.deleteConfirmation"/></td>
    </tr>
    <tr>
        <td  class="button">
            <app2:securitySubmit styleId="deleteButton" operation="UPDATE" functionality="CHART" styleClass="button" tabindex="1">
                <fmt:message key="Common.delete"/>
            </app2:securitySubmit>
            <html:cancel styleClass="button" tabindex="2"><fmt:message key="Common.cancel"/></html:cancel>
        </td>
    </tr>
</html:form>
</table>
