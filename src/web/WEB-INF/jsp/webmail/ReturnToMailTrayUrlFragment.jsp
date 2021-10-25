<%@ include file="/Includes.jsp" %>

<%--This fragment contains urls and parameters for some redirection logic
Parameter: mailSearch => When mailSearch=true then return to mails simple search
Parameter: mailAdvancedSearch => When mailAdvancedSearch=true then return to mails advanced search
--%>

<%
    Object mailFilter = request.getParameter("mailFilter");
    request.setAttribute("mailFilter", ((mailFilter != null && mailFilter.toString().length() > 0) ? mailFilter.toString() : "0"));
    Object mailSearch = request.getParameter("mailSearch");
    Object mailAdvancedSearch = request.getParameter("mailAdvancedSearch");
    request.setAttribute("mailSearch", ((mailSearch != null && mailSearch.toString().length() > 0) ? mailSearch.toString() : "false"));
    request.setAttribute("searchText", request.getParameter("searchText"));
    request.setAttribute("searchFilter", request.getParameter("searchFilter"));
    request.setAttribute("searchFolder", request.getParameter("searchFolder"));
    request.setAttribute("mailAdvancedSearch", ((mailAdvancedSearch != null && mailAdvancedSearch.toString().length() > 0) ? mailAdvancedSearch.toString() : "false"));
%>

<c:choose>
    <c:when test="${mailAdvancedSearch}">
        <c:url var="urlCancel" value="/webmail/Mail/MailAdvancedSearch.do" scope="request">
            <c:param name="mailSearch" value="${mailSearch}"/>
            <c:param name="mailAdvancedSearch" value="${mailAdvancedSearch}"/>
        </c:url>
    </c:when>
    <c:when test="${mailSearch}">
        <c:url var="urlCancel" value="/webmail/Mail/SearchResult.do" scope="request">
            <c:param name="searchText" value="${searchText}"/>
            <c:param name="searchFilter" value="${searchFilter}"/>
            <c:param name="searchFolder" value="${searchFolder}"/>
            <c:param name="mailFilter" value="${mailFilter}"/>
            <c:param name="mailSearch" value="${mailSearch}"/>
            <c:param name="returning" value="true"/>
            <c:param name="mailAdvancedSearch" value="${mailAdvancedSearch}"/>
        </c:url>
    </c:when>
    <c:otherwise>
        <c:url var="urlCancel" value="/webmail/Mail/MailTray.do" scope="request">
            <c:param name="folderId" value="${folderView}"/>
            <c:param name="index" value="${0}"/>
            <c:param name="mailFilter" value="${mailFilter}"/>
            <c:param name="returning" value="true"/>
            <c:param name="mailAdvancedSearch" value="${mailAdvancedSearch}"/>
        </c:url>
    </c:otherwise>
</c:choose>