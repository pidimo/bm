<%@ page import="javax.servlet.http.Cookie" %>
<%@ include file="/Includes.jsp" %>
<logic:messagesPresent message="false">
    <div class="error">
        <app2:messages id="message" message="false">
            <bean:define id="messageHtml" name="message"/>
            <c:out value="${message}" escapeXml="false"/> <br/>
        </app2:messages>
    </div>
</logic:messagesPresent>