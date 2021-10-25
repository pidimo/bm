<%@ page language="java" errorPage="/WEB-INF/jsp/JSPErrorPage.jsp" %>
<%@ include file="/Includes.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <c:import url="/WEB-INF/jsp/layout/ui/head.jsp"/>
    <title>
        <c:if test="${windowTitle != null}">
            <fmt:message key="${windowTitle}"/>
        </c:if>
    </title>
</head>
<body class="bg-body body-popup" <c:out value="${jsLoad}" escapeXml="false"/>>
<div>
    <div class="container" id="onlyBodySimpleError">
        <c:import url="/WEB-INF/jsp/layout/ui/simpleerror.jsp"/>
    </div>

    <div class="container-fluid" id="onlyBodyContainerId">
        <c:catch>
            <c:import url="${body}"/>
        </c:catch>
    </div>
</div>
<script type='text/javascript'>
    $(document).bind("DOMSubtreeModified",function(){
        resizeIframeModalBootstrap();
    });
    $(window).on("load", function () {
        resizeIframeModalBootstrap();
    });
    $(window).resize(function () {
        resizeIframeModalBootstrap();
    });
    function resizeIframeModalBootstrap() {
        var popupIframeId = window.frameElement.id;
        if (strEndsWith(popupIframeId, "IframePopup")) {
            parent.recalculateModalHeigth($("#onlyBodyContainerId").height()+$("#onlyBodySimpleError").height(), popupIframeId+"Contain");
        }
    }
    function strEndsWith(str, suffix) {
        return str.match(suffix + "$") == suffix;
    }
    function recalculateModalHeigth(height,div){
        $("#"+div).css({height: height +50+ "px"});
    }
</script>
</body>
</html>
