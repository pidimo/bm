<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ attribute name="keyFieldId" %>
<%@ attribute name="nameFieldId" %>
<%@ attribute name="titleKey" %>
<%@ attribute name="hide" %>
<%@ attribute name="submitOnClear" %>
<%@ attribute name="tabindex" %>
<%@ attribute name="onclick" %>
<%@ attribute name="name" %>
<%@ attribute name="glyphiconClass" %>
<%@ attribute name="styleClass" %>
<%@ attribute name="onClearJSFunction" %>


<c:if test="${!hide}">
    <c:if test="${tabindex != null}">
        <c:set var="tabindex" value=" tabindex=\"${tabindex}\""/>
    </c:if>

    <c:if test="${empty submitOnClear}">
        <c:set var="submitOnClear" value="false"/>
    </c:if>

    <c:set var="launchMethod" value="javascript:clearSelectPopup('${keyFieldId}', '${nameFieldId}', '${submitOnClear}', '${name}');"/>
    <c:if test="${not empty onClearJSFunction}">
        <c:set var="launchMethod" value="javascript:clearSelectPopupExtended('${keyFieldId}', '${nameFieldId}', '${submitOnClear}', '${name}', '${not empty onClearJSFunction ? onClearJSFunction : null}');"/>
    </c:if>

    <%--<a href="javascript:clearSelectPopup('${keyFieldId}', '${nameFieldId}' ${(submitOnClear != null && submitOnClear=='true') ? ', \'true\'' : ', \'false\''}, '${name}');"--%>
    <a href="${launchMethod}"
       onclick="${(onclick != null) ? onclick : '' }"
       title="<fmt:message   key="${titleKey}"/>" ${(tabindex != null) ? tabindex : ''} class="${(not empty styleClass) ? styleClass : app2:getFormButtonClasses()}">

    <c:choose>
        <c:when test="${not empty glyphiconClass}">
            <span class="glyphicon ${glyphiconClass}"></span>
        </c:when>
        <c:otherwise>
            <span class="glyphicon glyphicon-erase" ></span>
        </c:otherwise>
    </c:choose>

    </a>
</c:if>


