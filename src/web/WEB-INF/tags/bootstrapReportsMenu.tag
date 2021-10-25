<%@ tag import="com.piramide.elwis.utils.ReportConstants" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ taglib uri="/WEB-INF/fantabulous.tld" prefix="fanta" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ attribute name="module" required="true" description="module name of reports module (constants)" %>
<%@ attribute name="moduleContext" required="false" description="module context" %>

<c:set var="SOURCETYPE_JRXML" value="<%=ReportConstants.SourceType.JRXML.getConstantAsString()%>"/>

<c:set var="moduleContextVar" value="/${module}"/>
<c:if test="${not empty moduleContext}">
    <c:set var="moduleContextVar" value="${moduleContext}"/>
</c:if>

<fanta:list listName="reportsByUserRoles" module="/reports">
    <fanta:parameter field="userId" value="${sessionScope.user.valueMap['userId']}"/>
    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
    <fanta:parameter field="module" value="${module}"/>
</fanta:list>

<c:if test="${not empty reportsByUserRoles.result}">

    <li role="separator" class="divider"></li>

    <c:forEach var="report" items="${reportsByUserRoles.result}">
        <li>
            <c:set var="executeForwardAction" value="${moduleContextVar}/Report/Forward/Execute.do"/>
            <c:if test="${report.sourceType eq SOURCETYPE_JRXML}">
                <c:set var="executeForwardAction" value="${moduleContextVar}/Report/Jrxml/External/Forward/Execute.do"/>
            </c:if>

            <app:link page="${executeForwardAction}?dto(reportId)=${report.id}&reportId=${report.id}"
                      contextRelative="true" addModuleParams="false" titleKey="${report.name}">
                <c:out value="${report.name}"/>
            </app:link>
        </li>
    </c:forEach>
</c:if>




