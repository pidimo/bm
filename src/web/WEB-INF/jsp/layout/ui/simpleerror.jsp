<%@ page import="com.piramide.elwis.utils.Constants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="MSG_ERROR" value="<%=Constants.MessageContextType.ERROR.getConstantAsString()%>" scope="request"/>
<c:set var="MSG_WARNING" value="<%=Constants.MessageContextType.WARNING.getConstantAsString()%>" scope="request"/>
<c:set var="MSG_INFO" value="<%=Constants.MessageContextType.INFO.getConstantAsString()%>" scope="request"/>
<c:set var="MSG_SUCCESS" value="<%=Constants.MessageContextType.SUCCESS.getConstantAsString()%>" scope="request"/>

<%-- Writing the errors --%>

<c:if test="${skipErrors == null}">

    <c:choose>
        <c:when test="${MSG_WARNING eq messageContextType}">
            <c:set var="alertContextStyle" value="alert-warning" scope="request"/>
        </c:when>
        <c:when test="${MSG_INFO eq messageContextType}">
            <c:set var="alertContextStyle" value="alert-info" scope="request"/>
        </c:when>
        <c:when test="${MSG_SUCCESS eq messageContextType}">
            <c:set var="alertContextStyle" value="alert-success" scope="request"/>
        </c:when>
        <c:otherwise>
            <c:set var="alertContextStyle" value="alert-danger" scope="request"/>
        </c:otherwise>
    </c:choose>

    <logic:messagesPresent message="false">
        <div class="col-md-8 col-md-offset-2">

            <app2:messages id="message" message="false">
                <div class="alert ${alertContextStyle} alert-dismissible fade in" role="alert">
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                    &nbsp;
                    <c:out value="${message}" escapeXml="false"/>
                </div>
            </app2:messages>
        </div>
    </logic:messagesPresent>
</c:if>
