<%@ tag import="com.piramide.elwis.utils.ContactConstants" %>
<%--
Tag to set in request context relative address edit url
--%>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

<%@ attribute name="varName" required="true" %>
<%@ attribute name="addressId" required="true" %>
<%@ attribute name="addressType" required="true" %>
<%@ attribute name="name1" required="true" %>
<%@ attribute name="name2" required="true" %>
<%@ attribute name="name3" required="true" %>


<c:set var="personType" value="<%=ContactConstants.ADDRESSTYPE_PERSON%>"/>

<c:choose>
    <c:when test="${personType == addressType}">
        <c:set var="editUrlTagVar" scope="request"
               value="/contacts/Person/Forward/Update.do?contactId=${addressId}&dto(addressId)=${addressId}&dto(addressType)=${addressType}&dto(name1)=${app2:encode(name1)}&dto(name2)=${app2:encode(name2)}&dto(name3)=${app2:encode(name3)}&index=0"/>
    </c:when>
    <c:otherwise>
        <c:set var="editUrlTagVar" scope="request"
               value="/contacts/Organization/Forward/Update.do?contactId=${addressId}&dto(addressId)=${addressId}&dto(addressType)=${addressType}&dto(name1)=${app2:encode(name1)}&dto(name2)=${app2:encode(name2)}&dto(name3)=${app2:encode(name3)}&index=0"/>
    </c:otherwise>
</c:choose>

<%
    request.setAttribute(varName, request.getAttribute("editUrlTagVar"));
%>

