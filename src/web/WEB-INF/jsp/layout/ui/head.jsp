<%@ include file="/Includes.jsp" %>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="0">

<link rel="icon" href="<c:url value="/layout/ui/img/bmIcon.gif"/>" type="image/gif"/>

<%--
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<!-- Optional theme -->
<link rel="stylesheet" href="https://bootswatch.com/assets/css/bootswatch.min.css">
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>


<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.7.3/css/bootstrap-select.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.7.3/js/bootstrap-select.min.js"></script>
--%>

<%--miky, default bootstrap--%>
<%--
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">

<script src="https://code.jquery.com/jquery-2.1.4.min.js" defer></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js" defer></script>
--%>
<%----%>

<%--miky, simplex theme online--%>
<%--
<link rel="stylesheet" href="https://bootswatch.com/simplex/bootstrap.css">
<link rel="stylesheet" href="https://bootswatch.com/assets/css/bootswatch.min.css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.7.3/css/bootstrap-select.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.7.3/js/bootstrap-select.min.js"></script>
--%>

<%--jquery--%>
<script src="<c:url value="/js/cacheable/jquery-1.11.3.min.js"/>"></script>

<%--theme--%>
<link rel="stylesheet" href="<c:url value="/js/cacheable/bootstrap/3.3.5/themesimplex/bootstrap.min.css"/>">
<link rel="stylesheet" href="<c:url value="/js/cacheable/bootstrap/3.3.5/themesimplex/bootswatch.min.css"/>">

<%--bootstrap--%>
<script src="<c:url value="/js/cacheable/bootstrap/3.3.5/js/bootstrap.min.js"/>"></script>
<%--modal fullscreen--%>
<link rel="stylesheet" href="<c:url value="/js/cacheable/bootstrap/bootstrapmodalcarousel/css/bootstrap-modal-carousel.min.css"/>">
<script src="<c:url value="/js/cacheable/bootstrap/bootstrapmodalcarousel/js/bootstrap-modal-carousel.min.js"/>"></script>
<%--select bootstrap addon--%>
<link rel="stylesheet" href="<c:url value="/js/cacheable/bootstrap/bootstrapselect/1.7.3/bootstrap-select.min.css"/>">
<script src="<c:url value="/js/cacheable/bootstrap/bootstrapselect/1.7.3/bootstrap-select.min.js"/>"></script>

<%--insert submenu bootstrap addon--%>
<link rel="stylesheet" href="<c:url value="/js/cacheable/bootstrap/smartmenus/1.0.0-beta1/jquery.smartmenus.bootstrap.css"/>">
<script src="<c:url value="/js/cacheable/bootstrap/smartmenus/1.0.0-beta1/jquery.smartmenus.min.js"/>" defer="defer"></script>
<script src="<c:url value="/js/cacheable/bootstrap/smartmenus/1.0.0-beta1/jquery.smartmenus.bootstrap.min.js"/>" defer="defer"></script>

<%--insert menuBarCollapce bootstrap addon--%>
<script src="<c:url value="/js/cacheable/bootstrap/menuBarCollapce/1.0.0/menuBarCollapce.js"/>" defer="defer"></script>

<%--ckeckbox style--%>
<link rel="stylesheet" href="<c:url value="/js/cacheable/bootstrap/checkbox/awesome-bootstrap-checkbox-1.0.css"/>">

<%--bootstrap reaponsive tabs addon--%>
<link rel="stylesheet" href="<c:url value="/js/cacheable/bootstrap/bootstraptabdrop/css/tabdrop.css"/>">
<script src="<c:url value="/js/cacheable/bootstrap/bootstraptabdrop/js/bootstrap-tabdrop.js"/>"></script>

<tags:jQueryValidationHeader/>

<%--write tinyMCE init scripts--%>
<c:if test="${not empty initHtmlEditorItems}">
  <c:forEach items="${initHtmlEditorItems}" var="item">
    <c:out value="${item.value}" escapeXml="false"/>
  </c:forEach>
</c:if>


<%--write tinyMCE 4 init scripts--%>
<c:if test="${not empty tinyMCE4EditorItems}">
  <c:forEach items="${tinyMCE4EditorItems}" var="item">
    <c:out value="${item.value}" escapeXml="false"/>
  </c:forEach>
</c:if>

<link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>

<%--add custom style css configured--%>
<c:import url="/WEB-INF/jsp/layout/ui/AppStyleCSS.jsp"/>
<%--font awesome--%>
<link rel="stylesheet" href="<c:url value="/js/cacheable/bootstrap/font-awesome/4.4.0/css/font-awesome.min.css"/>">