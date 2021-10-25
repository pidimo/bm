<%@ include file="/Includes.jsp" %>

<c:if test="${not empty tabs}">

    <c:import url="${tabs}"/>

    <c:if test="${param.index != null}">
        <c:set var="index" value="${param.index}" scope="request"/>
    </c:if>
    <c:if test="${param.index == null}">
        <c:set var="index" value="0" scope="request"/> <%--default--%>
    </c:if>

    <c:if test="${empty tabItems}">
        <c:set var="hasNoSubMenuTabs" value="${true}" scope="request"/>
    </c:if>

    <c:if test="${tabParams == null}">
        <jsp:useBean id="tabParams" class="java.util.LinkedHashMap"/>
    </c:if>

    <div class="container main-container">

        <c:if test="${not empty tabHeaderLabel}">
            <h4>
                <fmt:message key="${tabHeaderLabel}"/><c:if test="${not empty tabHeaderValue}">:</c:if>
                <c:out value="${tabHeaderValue}" escapeXml="false"/>
            </h4>
        </c:if>

        <ul class="nav nav-tabs">

            <c:forEach var="item" items="${tabItems}" varStatus="i">

                <li
                        <c:choose>
                            <c:when test="${item.key == param.tabKey}">
                                <c:set var="index" value="${i.index}" scope="request"/>
                                class="active"
                            </c:when>
                            <c:when test="${i.index == index && empty param.tabKey}">
                                class="active"
                            </c:when>
                        </c:choose> >
                    <c:set target="${tabParams}" property="index" value="${i.index}"/>
                    <app:link page="${item.value}" name="tabParams" styleClass="navTabsDetail">
                        <c:choose>
                            <c:when test="${fn:indexOf(item.key,'001@100-') == 0}">
                                <c:out value="${fn:substring(item.key, 8, fn:length(item.key))}"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="${item.key}"/>
                            </c:otherwise>
                        </c:choose>
                    </app:link>
                </li>

            </c:forEach>
        </ul>
    </div>
</c:if>


<%--
<c:if test="${param.index != null}">
    <c:set var="index" value="${param.index}" scope="request"/>
</c:if>
<c:if test="${param.index == null}">
    <c:set var="index" value="0" scope="request"/> &lt;%&ndash;default&ndash;%&gt;
</c:if>

<c:if test="${empty tabItems}">
    <c:set var="hasNoSubMenuTabs" value="${true}" scope="request"/>
</c:if>

<c:if test="${tabParams == null}">
    <jsp:useBean id="tabParams" class="java.util.LinkedHashMap"/>
</c:if>



<tr>
    <td valign="top" style="padding-top:3px;">
        <table width="100%" align="left" cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td class="folderTab" style="border-left: 0px;border-top: 0px;background: transparent;">
                    <img src="<c:url value="/layout/ui/img/spacer.gif"/>" alt="" width="3px"/>
                </td>

                <c:forEach var="item" items="${tabItems}" varStatus="i">
                    <td class="folderTab"
                            <c:choose>
                                <c:when test="${item.key == param.tabKey}">
                                    <c:set var="index" value="${i.index}" scope="request"/>
                                    id="current"
                                </c:when>
                                <c:when test="${i.index == index && param.tabKey == null}">
                                    id="current"
                                </c:when>
                                <c:otherwise>
                                    onmouseover="this.id='over';" onmouseout="this.id='';"
                                </c:otherwise>
                            </c:choose>
                        style="border-left: 1px solid #ffffff;text-align:center;" >
                        <c:set target="${tabParams}" property="index" value="${i.index}"/>
                        <app:link page="${item.value}" name="tabParams">
                            <c:choose>
                                <c:when test="${fn:indexOf(item.key,'001@100-') == 0}">
                                    <c:out value="${fn:substring(item.key, 8, fn:length(item.key))}"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="${item.key}"/>
                                </c:otherwise>
                            </c:choose>
                        </app:link>
                    </td>

                </c:forEach>
                <td class="folderTab" id="space" style="width:100%;border-top:0px;border-left:0px;border-right:0px;"><img
                        src="<c:url value="/layout/ui/img/spacer.gif"/>" alt=""/></td>
            </tr>
        </table>
    </td>
</tr>--%>
