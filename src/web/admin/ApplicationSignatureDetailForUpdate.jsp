<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.admin.generalMailSignature" scope="request"/>

<fmt:message var="title" key="ApplicationSignature.update.title" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/ApplicationSignature/Update.do" scope="request"/>
<c:set var="op" value="update" scope="request"/>

<c:set var="pagetitle" value="ApplicationSignature.update.title" scope="request"/>
<c:set var="windowTitle" value="ApplicationSignature.update.title" scope="request"/>

<c:forEach var="systemLanguage" items="${applicationSignatureForm.dtoMap['systemLanguages']}">
    <tags:initTinyMCE4 textAreaId="signature_field_${systemLanguage.key}"
                           addUserEmailStylePlugin="false"
                           addBrowseImagePlugin="false"/>
</c:forEach>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/admin/ApplicationSignature.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/admin/ApplicationSignature.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>