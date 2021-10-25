<%@ include file="/Includes.jsp" %>

<%
    if (request.getAttribute("fromAError") == null) {
        Object mailSearch = request.getParameter("mailSearch");
        request.setAttribute("mailSearch", (mailSearch != null ? mailSearch.toString() : "false"));
    }
%>

<c:set var="selectedFolderId" value="${folderView}" scope="request"/>
<c:set var="emailReadFilter" value="${param.mailFilter}" scope="request"/>
<c:if test="${mailSearch == 'true'}">
    <c:set var="enableFolderSelector" value="false" scope="request"/>
</c:if>

<div id="emailfolderId">
    <c:import url="/WEB-INF/jsp/webmail/WebmailFolderFragment.jsp"/>
</div>
