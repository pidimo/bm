<%@ include file="/Includes.jsp" %>
<c:set var="windowTitle" value="Common.globalError" scope="request"/>

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
<body class="bg-body">
<div>
    <div class="container">
        <div class="col-md-8 col-md-offset-2">
            <div class="alert alert-danger alert-dismissible fade in" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>

                <h4>
                    <fmt:message key="Common.globalError"/>
                </h4>
                <p>
                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                    &nbsp;
                    <fmt:message key="msg.ServerError"/>
                </p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
