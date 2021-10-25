<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ attribute name="styleId" required="true" %>
<%@ attribute name="url" %>
<%@ attribute name="name" %>
<%@ attribute name="titleKey" %>
<%@ attribute name="hide" %>
<%@ attribute name="submitOnSelect" %>
<%@ attribute name="imgPath" %>

<%@ attribute name="tabindex" %>
<%@ attribute name="showCloseButton" %>
<%@ attribute name="isLargeModal" %>
<%@ attribute name="modalTitleKey" %>
<%@ attribute name="glyphiconClass" %>
<%@ attribute name="styleClass" %>
<%@ attribute name="onShowJSFunction"%>
<%@ attribute name="onSelectJSFunction" %>
<%@ attribute name="customLaunchMethodName" %>

<c:if test="${!hide}">
    <c:if test="${not empty tabindex}">
        <c:set var="tabindex" value=" tabindex=\"${tabindex}\""/>
    </c:if>

    <c:if test="${empty submitOnSelect}">
        <c:set var="submitOnSelect" value="false"/>
    </c:if>
    <c:if test="${empty modalTitleKey}">
        <c:set var="modalTitleKey" value="${titleKey}"/>
    </c:if>

    <!-- Modal -->
    <div class="modal fade ${'true' eq isLargeModal ? 'bs-example-modal-lg' : ''}" id="${styleId}" tabindex="-1"
         role="dialog" aria-labelledby="${styleId}Label">
        <div class="modal-dialog ${'true' eq isLargeModal ? 'modal-lg' : ''}" role="document">
            <div class="modal-content">
                <div class="modal-header titleModalClearfix">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="${styleId}Label">
                        <c:if test="${not empty modalTitleKey}">
                            <fmt:message key="${modalTitleKey}"/>
                        </c:if>
                    </h4>
                </div>
                <div class="modal-body">
                    <div class="embed-responsive embed-responsive-16by9" id="${styleId}IframePopupContain">
                        <iframe class="embed-responsive-item modalIframeContent" frameborder="0" scrolling="no"
                                name="${name}" id="${styleId}IframePopup">

                        </iframe>
                    </div>
                </div>
                <c:if test="${'true' eq showCloseButton}">
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            <fmt:message key="Common.close"/>
                        </button>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <c:url var="urlIframeLoad" value='${url}'/>
    <c:choose>
        <c:when test="${not empty customLaunchMethodName}">
            <c:set var="launchMethod" value="javascript:${customLaunchMethodName}('${styleId}', '${urlIframeLoad}', '${name}', '${submitOnSelect}')"/>
        </c:when>
        <c:otherwise>
            <c:set var="launchMethod" value="javascript:launchBootstrapPopup('${styleId}', '${urlIframeLoad}', '${name}', '${submitOnSelect}')"/>
        </c:otherwise>
    </c:choose>

    <c:if test="${(not empty onShowJSFunction) or (not empty onSelectJSFunction)}">
        <c:choose>
            <c:when test="${not empty customLaunchMethodName}">
                <c:set var="launchMethod" value="javascript:${customLaunchMethodName}('${styleId}', '${urlIframeLoad}', '${name}', '${submitOnSelect}','${not empty onShowJSFunction ? onShowJSFunction : null}', '${not empty onSelectJSFunction ? onSelectJSFunction : null}')"/>
            </c:when>
            <c:otherwise>
                <c:set var="launchMethod" value="javascript:launchBootstrapPopupExtended('${styleId}', '${urlIframeLoad}', '${name}', '${submitOnSelect}','${not empty onShowJSFunction ? onShowJSFunction : null}', '${not empty onSelectJSFunction ? onSelectJSFunction : null}')"/>
            </c:otherwise>
        </c:choose>
    </c:if>

    <a href="${launchMethod};"
       title="<fmt:message   key="${titleKey}"/>" ${(tabindex != null) ? tabindex : ''} class="${(not empty styleClass) ? styleClass : app2:getFormButtonClasses() }">
        <c:choose>
            <c:when test="${not empty glyphiconClass}">
                <span class="glyphicon ${glyphiconClass}"></span>
            </c:when>
            <c:otherwise>
                <span class="glyphicon glyphicon-search"></span>
            </c:otherwise>
        </c:choose>
    </a>
</c:if>
