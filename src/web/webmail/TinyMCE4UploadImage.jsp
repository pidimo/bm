<%@ include file="/Includes.jsp" %>

<c:set var="windowTitle" value="Mail.browseImage.title" scope="request"/>
<c:set var="op" value="create" scope="request"/>

<c:set var="body" value="/js/tinymce4/externalpopups/TinyMCE4UploadImage.jsp" scope="request"/>
<c:import url="/WEB-INF/jsp/layout/ui/OnlyBody.jsp"/>
