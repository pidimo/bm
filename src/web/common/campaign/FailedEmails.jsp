<%@ page import="java.util.Map,
                 java.util.HashMap,
                 java.util.List,
                 java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<br/>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
<td>
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td height="20" class="title">
            <fmt:message   key="Campaign.msg.FailedRecipients"/>
            </td>               
        </tr>
<%--        <tr>
            <td align="center" >
            </br>
            <table width="70%" >
            <tr><td><fmt:message   key="Campaign.msg.FailedRecipients"/>
            </td></tr>
            </table>
            </br>
            </td>
        </tr>--%>
        <tr>
            <td>
            <%--<%
                List list = new ArrayList();
                list.add("Usuario uno");
                list.add("Usuario dos");
                list.add("Usuario tres");
                list.add("Usuario cuatro");
                list.add("Usuario seis");
                list.add("Usuario siete");
                list.add("Usuario ocho");
                list.add("Usuario nueve");
                list.add("Usuario diez");
                list.add("Usuario once");
                list.add("Usuario doce");
                list.add("Usuario trece");
                list.add("Usuario catorce");
                list.add("Usuario quince");
                list.add("Usuario diezseis");
                list.add("Usuario diessiete");
                request.getSession().setAttribute("noHasEmail", list);
            %>--%>
            <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0" class="searchContainer">
            <c:forEach items="${noHasEmail}" var="address">
                <tr >
                    <td class="contain" >
                    ${address}
                    </td>
                </tr>
            </c:forEach>
            </table>
            </td>
        </tr>
        <tr>
        <td align="center" >
        </br>
        <%--<html:link action="/Campaign/Cancel" ><fmt:message   key="Common.cancel"/></html:link>--%>
        <html:button styleClass="button" property="" onclick="window.close()" ><fmt:message   key="Common.close"/></html:button>
        </td>
        </tr>
    </table>
<c:remove var="noHasEmail" />