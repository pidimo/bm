<%@ tag import="com.piramide.elwis.utils.ReportConstants" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ taglib uri="/WEB-INF/fantabulous.tld" prefix="fanta" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ attribute name="module" required="true" description="module name of reports module (constants)" %>

<c:set var="SOURCETYPE_JRXML" value="<%=ReportConstants.SourceType.JRXML.getConstantAsString()%>"/>

<fanta:list listName="reportsByUserRoles" module="/reports">
    <fanta:parameter field="userId" value="${sessionScope.user.valueMap['userId']}"/>
    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
    <fanta:parameter field="module" value="${module}"/>
</fanta:list>

<c:if test="${not empty reportsByUserRoles.result}">
    <c:if test="${canPullDownMenuBeShown}">
        <hr/>
    </c:if>
    <c:forEach var="report" items="${reportsByUserRoles.result}">
        <div style="padding:3px 10px 3px 3px;">

            <c:set var="executeForwardAction" value="/Report/Forward/Execute.do"/>
            <c:if test="${report.sourceType eq SOURCETYPE_JRXML}">
                <c:set var="executeForwardAction" value="/Report/Jrxml/External/Forward/Execute.do"/>
            </c:if>

            <app:link page="${executeForwardAction}?dto(reportId)=${report.id}&reportId=${report.id}"
                      addModuleParams="false" titleKey="${report.name}">
                <c:out value="${report.name}"/>
            </app:link>
        </div>
    </c:forEach>
    <c:set var="canPullDownMenuBeShown" value="true" scope="request"/>
</c:if>




