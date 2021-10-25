<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>

<%@ attribute name="selectedBoxId" required="true" %>
<%@ attribute name="availableBoxId" required="true" %>
<%@ attribute name="titleKey" %>
<%@ attribute name="hide" %>
<%@ attribute name="tabindex" %>
<%@ attribute name="modalTitle" %>
<%@ attribute name="modalTitleKey" %>
<%@ attribute name="glyphiconClass" %>
<%@ attribute name="styleClass" %>
<%@ attribute name="onSelectJSFunction" %>

<c:if test="${!hide}">
    <c:if test="${not empty tabindex}">
        <c:set var="tabindex" value=" tabindex=\"${tabindex}\""/>
    </c:if>

    <c:if test="${empty titleKey}">
        <c:set var="titleKey" value="Common.edit"/>
    </c:if>

    <c:if test="${empty modalTitle and not empty modalTitleKey}">
        <fmt:message var="modalTitle" key="${modalTitleKey}"/>
    </c:if>

    <c:if test="${empty modalTitle and not empty titleKey}">
        <fmt:message var="modalTitle" key="${titleKey}"/>
    </c:if>

    <%--remove quotes characters '" of title --%>
    <c:set var="modalTitle" value="${fn:replace(modalTitle,'\\\'', '')}"/>
    <c:set var="modalTitle" value="${fn:replace(modalTitle,'\"', '')}"/>

    <c:set var="launchMethod" value="javascript:launchMultipleSelectModal('${selectedBoxId}', '${availableBoxId}', '${modalTitle}', '${not empty onSelectJSFunction ? onSelectJSFunction : null}');"/>

    <a href="${launchMethod}"
       style="float: right"
       title="<fmt:message   key="${titleKey}"/>" ${(tabindex != null) ? tabindex : ''} class="${(not empty styleClass) ? styleClass : app2:getFormButtonClasses() }">
        <c:choose>
            <c:when test="${not empty glyphiconClass}">
                <span class="glyphicon ${glyphiconClass}"></span>
            </c:when>
            <c:otherwise>
                <span class="glyphicon glyphicon-pencil"></span>
            </c:otherwise>
        </c:choose>
    </a>
</c:if>
