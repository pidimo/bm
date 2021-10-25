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
<body class="bg-body body-popup" <c:out value="${jsLoad}" escapeXml="false"/> style="background-color: transparent !important;">
<div class="main-container" id="iframeGetHeightStyleId">
    <c:catch>
        <c:import url="${body}"/>
    </c:catch>
</div>
<script type='text/javascript'>
    $(window).resize(function () {
        resizeIframeBootstrap();
    });
    $(window).load(function () {
        resizeIframeBootstrap();
    });
    function resizeIframeBootstrap() {
        var containerIframeBootstrap = $("#iframeGetHeightStyleId");
        var heigthContainerIframeBootstrap = ($(containerIframeBootstrap).height() + 50);
        var nodeIframe = window.frameElement;
        //console.log($(node).parent());
        $(nodeIframe).parent().css({height: heigthContainerIframeBootstrap + "px"});
    }
</script>
</body>
</html>
