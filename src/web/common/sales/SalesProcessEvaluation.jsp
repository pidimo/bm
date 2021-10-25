<%@ page import="java.io.PrintWriter,
                 java.util.*,
                 com.piramide.elwis.web.salesmanager.report.SalesProcessEvaluationChart,
                 net.java.dev.strutsejb.web.DefaultForm"%>
<%@ include file="/Includes.jsp" %>

<%

        String period = "2";
        if(request.getParameter("dto(period)") != null)
            period = request.getParameter("dto(period)");

        Map dtoMap = (Map) request.getAttribute("dto");
        List result = (List) dtoMap.get("resultList");

        String filename = null;
        String graphURL = null;
        if(!result.isEmpty()) {
            filename = SalesProcessEvaluationChart.generateXYChart(result, request, new PrintWriter(out), period);
            graphURL = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
        }
    pageContext.setAttribute("filename", filename);
    if (graphURL != null)
        pageContext.setAttribute("graphURL", response.encodeRedirectURL(graphURL));
      DefaultForm form = (DefaultForm)request.getAttribute("form");
      form.setDto("period", period);

%>

<br/>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">

<tr>
    <td align="center" >
        <c:choose>
	        <c:when test="${filename == null}">
                <c:choose>
                    <c:when test="${isSalesProcess}">
                        <fmt:message key="SalesProcess.evaluation.noactions"/>
                    </c:when>
                    <c:otherwise>
                        <html:form action="/SalesProcess/Evaluation.do?dto(processId)=${param['dto(processId)']}">
                            <fmt:message key="SalesProcess.evaluation.noactions"/>
                        <html:submit property="cancelEvaluation" styleClass="button"><fmt:message   key="Common.cancel"/></html:submit>
                    </html:form>
                    </c:otherwise>
                </c:choose>
	        </c:when>
	        <c:otherwise>
                <img src="${graphURL}" width="550" height="350" border=0 usemap="#${filename}" alt="<fmt:message key="SalesProcess.Title.evaluation"/>">
	        </c:otherwise>
	    </c:choose>

    </td>
 </tr>
<c:if test="${filename != null}">
<tr>
    <td align="center">
    <br>
        <html:form action="/SalesProcess/Evaluation.do?dto(processId)=${param['dto(processId)']}" focus="dto(period)">
       <table width="50%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">


        <tr>
            <td height="20" class="title" colspan="2" >
                <fmt:message key="SalesProcess.Title.chartOptions"/>
            </td>
        </tr>
        <TR>
                <td class="label"><fmt:message key="SalesProcess.evaluation.periodLength"/></td>
                <td class="contain" nowrap>
                    <html:select property="dto(period)" styleClass="select" tabindex="1">
                        <html:option value="1"><fmt:message key="Common.day"/></html:option>
                        <html:option value="2"><fmt:message key="Common.week"/></html:option>
                        <html:option value="3"><fmt:message key="Common.month"/></html:option>
                        <html:option value="4"><fmt:message key="Common.year"/></html:option>
                    </html:select>
                 </td>

         </TR>


         <TR>
            <TD class="button" colspan="2">
                <app2:checkAccessRight functionality="SALESPROCESSEVALUATION" permission="EXECUTE">
                    <html:submit property="execute" styleClass="button"><fmt:message key="Common.refresh"/></html:submit>
                </app2:checkAccessRight>
                <c:if test="${!isSalesProcess}">
                    <html:submit property="cancelEvaluation" styleClass="button"><fmt:message   key="Common.cancel"/></html:submit>
                </c:if>
            </TD>
         </TR>
        </table>
        </html:form>
    </td>
</tr>

</c:if>

</table>
<br>



