<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ attribute name="styleId" required="true" %>
<%@ attribute name="name" %>
<%@ attribute name="hide" %>
<%@ attribute name="submitOnSelect" %>

<%@ attribute name="showCloseButton" %>
<%@ attribute name="isLargeModal" %>
<%@ attribute name="isFullScreen" %>
<%@ attribute name="modalTitleKey" %>

<c:if test="${!hide}">

  <c:choose>
    <c:when test="${not empty isFullScreen && 'true' eq isFullScreen}">
        <c:set var="modeModal" value="modal-fullscreen force-fullscreen"/>
    </c:when>
    <c:otherwise>
      <c:choose>
        <c:when test="">
          <c:set var="modeModal" value="bs-example-modal-lg"/>
        </c:when>
        <c:otherwise>
          <c:set var="modeModal" value=""/>
        </c:otherwise>
      </c:choose>
    </c:otherwise>
  </c:choose>

  <c:if test="${empty submitOnSelect}">
    <c:set var="submitOnSelect" value="false"/>
  </c:if>
  <c:if test="${empty modalTitleKey}">
    <c:set var="modalTitleKey" value="${titleKey}"/>
  </c:if>

  <!-- Modal -->
  <div class="modal fade ${modeModal}" id="${styleId}" tabindex="-1"
       role="dialog" aria-labelledby="${styleId}Label">
    <div class="modal-dialog ${'true' eq isLargeModal ? 'modal-lg' : ''}" role="document">
      <div class="modal-content">
        <div class="modal-header">
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
</c:if>

<style type="text/css">
  .modal.modal-fullscreen.force-fullscreen .modal-header{
    position: static;
  }
</style>
