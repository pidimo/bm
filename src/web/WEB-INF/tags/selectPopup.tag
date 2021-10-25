<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ attribute name="url" %>
<%@ attribute name="name" %>
<%@ attribute name="titleKey" %>
<%@ attribute name="hide" %>
<%@ attribute name="submitOnSelect" %>
<%@ attribute name="width" %>
<%@ attribute name="heigth" %>
<%@ attribute name="scrollbars" %>
<%@ attribute name="imgPath" %>
<%@ attribute name="imgWidth" %>
<%@ attribute name="imgHeight" %>
<%@ attribute name="tabindex" %>
<%@ attribute name="onSelectJSFunction" %>
<c:if test="${!hide}">
    <c:if test="${tabindex != null}">
        <c:set var="tabindex" value=" tabindex=\"${tabindex}\""/>
    </c:if>

    <c:if test="${empty submitOnSelect}">
        <c:set var="submitOnSelect" value="false"/>
    </c:if>

    <c:url var="urlPopup" value='${url}'/>
    <c:set var="launchMethod" value="javascript:selectPopup('${urlPopup}', '${name}', '${submitOnSelect}', ${width != null ? width : '660'}, ${(heigth != null) ? heigth : '480'},${(scrollbars != null) ? scrollbars: '1'});"/>

    <c:if test="${not empty onSelectJSFunction}">
        <c:set var="launchMethod" value="javascript:selectPopupExtended('${urlPopup}', '${name}', '${submitOnSelect}', ${width != null ? width : '660'}, ${(heigth != null) ? heigth : '480'},${(scrollbars != null) ? scrollbars: '1'}, '${not empty onSelectJSFunction ? onSelectJSFunction : null}');"/>
    </c:if>

    <%--<a href="javascript:selectPopup('<c:url value="${url}"/>', '${name}' ${(submitOnSelect != null && submitOnSelect=='true') ? ', \'true\'' : ', \'false\''}, ${width != null ? width : '660'}, ${(heigth != null) ? heigth : '480'},${(scrollbars != null) ? scrollbars: '1'});"--%>
    <a href="${launchMethod}"
       title="<fmt:message   key="${titleKey}"/>" ${(tabindex != null) ? tabindex : ''}>
        <img src="<c:out value="${sessionScope.baselayout}"/>${(imgPath != null) ? imgPath: '/img/search.gif'}"
             alt="<fmt:message   key="${titleKey}"/>" border="0" align="middle"
             width="${(imgWidth != null) ? imgWidth : 18 }" height="${(imgHeight != null) ? imgHeight : 19 }"/>
    </a>
</c:if>


