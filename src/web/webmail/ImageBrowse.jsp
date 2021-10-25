<%@ include file="/Includes.jsp" %>

<c:set var="windowTitle" value="Mail.browseImage.title" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="body" value="/js/htmleditor/externalpopups/ImageBrowse.jsp" scope="request"/>
<c:import url="${sessionScope.layout}/OnlyBody.jsp"/>
