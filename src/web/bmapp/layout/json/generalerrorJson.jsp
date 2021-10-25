<%@ include file="/Includes.jsp" %>

<c:set var="indexError" value="${0}" scope="request"/>

<c:if test="${skipErrors == null}">
  <logic:messagesPresent message="false">
    "errorsArray": [
                <app2:messagesMobil id="message" message="false">

                  <c:if test="${indexError > 0}">, </c:if>
                  {
                    "error" : "${app2:escapeJSON(message)}"
                  }

                  <c:set var="indexError" value="${1}" scope="request"/>
                </app2:messagesMobil>
    ],
  </logic:messagesPresent>
</c:if>
