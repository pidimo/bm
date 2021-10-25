<%@ page import="com.piramide.elwis.web.salesmanager.report.SalesProcessEvaluationChart,
                 net.java.dev.strutsejb.web.DefaultForm,
                 java.io.PrintWriter,
                 java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ include file="/Includes.jsp" %>

<%

    String period = "2";
    if (request.getParameter("dto(period)") != null)
        period = request.getParameter("dto(period)");

    Map dtoMap = (Map) request.getAttribute("dto");
    List result = (List) dtoMap.get("resultList");

    String filename = null;
    String graphURL = null;
    if (!result.isEmpty()) {
        filename = SalesProcessEvaluationChart.generateXYChart(result, request, new PrintWriter(out), period);
        graphURL = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
    }
    pageContext.setAttribute("filename", filename);
    if (graphURL != null)
        pageContext.setAttribute("graphURL", response.encodeRedirectURL(graphURL));
    DefaultForm form = (DefaultForm) request.getAttribute("form");
    form.setDto("period", period);

%>

<div class="${filename == null ? '': 'table-responsive'}">
    <table width="95%"
           border="0"
           align="center"
           cellpadding="0"
           cellspacing="0"
           class="${app2:getFantabulousTableClases()}">

        <tr>
            <td align="center">
                <c:choose>
                    <c:when test="${filename == null}">
                        <c:choose>
                            <c:when test="${isSalesProcess}">
                                <fmt:message key="SalesProcess.evaluation.noactions"/>
                            </c:when>
                            <c:otherwise>
                                <html:form
                                        action="/SalesProcess/Evaluation.do?dto(processId)=${param['dto(processId)']}">
                                    <fmt:message key="SalesProcess.evaluation.noactions"/>
                                    <html:submit property="cancelEvaluation" styleClass="button ${app2:getFormButtonCancelClasses()}">
                                        <fmt:message key="Common.cancel"/>
                                    </html:submit>
                                </html:form>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <img src="${graphURL}" width="550" height="350" border=0 usemap="#${filename}"
                             alt="<fmt:message key=" SalesProcess.Title.evaluation"/>">
                    </c:otherwise>
                </c:choose>

            </td>
        </tr>
    </table>
</div>
<c:if test="${filename != null}">
    <html:form action="/SalesProcess/Evaluation.do?dto(processId)=${param['dto(processId)']}"
               focus="dto(period)"
               styleClass="form-horizontal">
        <div class="${app2:getFormClasses()}">

            <div class="${app2:getFormPanelClasses()}">

                <fieldset>

                    <legend class="title">
                        <fmt:message key="SalesProcess.Title.chartOptions"/>
                    </legend>

                    <div class="form-group">
                        <label class="${app2:getFormLabelClasses()}" for="file_id">
                            <fmt:message key="SalesProcess.evaluation.periodLength"/>
                        </label>

                        <%-- DEFINE HTML FILE --%>

                        <div class="${app2:getFormContainClasses('create' == op)}">
                            <html:select property="dto(period)"
                                         styleClass="${app2:getFormSelectClasses()} select"
                                         tabindex="1">
                                <html:option value="1"><fmt:message key="Common.day"/></html:option>
                                <html:option value="2"><fmt:message key="Common.week"/></html:option>
                                <html:option value="3"><fmt:message key="Common.month"/></html:option>
                                <html:option value="4"><fmt:message key="Common.year"/></html:option>
                            </html:select>
                        </div>
                    </div>

                </fieldset>
                <div class="${app2:getFormButtonWrapperClasses()}">

                    <app2:checkAccessRight functionality="SALESPROCESSEVALUATION" permission="EXECUTE">
                        <html:submit property="execute" styleClass="${app2:getFormButtonClasses()}"><fmt:message
                                key="Common.refresh"/></html:submit>
                    </app2:checkAccessRight>
                    <c:if test="${!isSalesProcess}">
                        <html:submit property="cancelEvaluation"
                                     styleClass="${app2:getFormButtonCancelClasses()}"><fmt:message
                                key="Common.cancel"/></html:submit>
                    </c:if>

                </div>

            </div>
        </div>
    </html:form>

</c:if>