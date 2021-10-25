<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.communication.delete" scope="request"/>

<fmt:message var="title" key="Communication.Title.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>
<c:set var="action" value="/MainCommunication/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>

<c:set var="windowTitle" value="Communication.Title.delete" scope="request"/>

<c:choose>
       <c:when test="${sessionScope.isBootstrapUI}">

              <!--communication page-->
              <c:set var="communicationButtons"
                     value="/WEB-INF/jsp/contacts/CommunicationContactButtons.jsp"
                     scope="request"/>
              <c:set var="moduleCommunication"
                     value="/WEB-INF/jsp/contacts/CommunicationContact.jsp"
                     scope="request"/>

              <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
              <c:set var="body" value="/WEB-INF/jsp/contacts/CommunicationTemplate.jsp" scope="request"/>
              <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
       </c:when>
       <c:otherwise>

              <!--communication page-->
              <c:set var="communicationButtons"
                     value="/common/contacts/CommunicationContactButtons.jsp"
                     scope="request"/>
              <c:set var="moduleCommunication"
                     value="/common/contacts/CommunicationContact.jsp"
                     scope="request"/>

              <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
              <c:set var="body" value="/common/contacts/CommunicationTemplate.jsp" scope="request"/>
              <c:import url="${sessionScope.layout}/main.jsp"/>
       </c:otherwise>
</c:choose>