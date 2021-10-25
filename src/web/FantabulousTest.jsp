<%@ page import="java.util.ArrayList,
                 org.alfacentauro.fantabulous.controller.ResultList"%>
<%@ taglib uri="/WEB-INF/fantabulous.tld" prefix="table" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<link href="<%= request.getContextPath() %>/style.css" rel="stylesheet">

<html>
<head>
<title>
Fantabuluos Test
</title>
</head>

<body marginheight="0" marginwidth="0" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<h1>Fantabulous Test:</h1>
<form action="<c:url value="/FantabulousTest.do"/>" method="post" >
<table>
<tr>
<td>Path Config:</td>
<td><input type="text" name="pathConfig" size="100"  value="<c:out value="${PathConfig}"/>" ></td>
</tr>
<tr>
<td>List Name:</td>
<td><input type="text" name="listName" size="100"  value="<c:out value="${ListName}"/>" ></td>
</tr>
<tr>
<td>Param List:</td>
<td><input type="textarea" name="parameters" size="100"  value="<c:out value="${Parameters}"/>"></td>
</tr>
<tr>
<td colspan="2" align="center" ><input type="submit" ></td>
</tr>
</table>
</form>


<c:if test="${FANTABULOUS_ERROR!=null}">
<h1>Error:</h1>
<%out.print(request.getAttribute("FANTABULOUS_ERROR"));%>
</c:if>

<c:if test="${ListName!=null}">

<%
    String listName = (String) request.getAttribute("ListName");
        ResultList resultList =  (ResultList) request.getAttribute(listName);
    if(resultList!=null)
        pageContext.setAttribute("collection", resultList.getResult());
    else
    pageContext.setAttribute("collection", new ArrayList());
%>

<table>
<c:forEach var="result" items="${collection}" varStatus="index" >
    <c:if test="${index.index==0}">
    <tr>
    <c:forEach var="item" items="${result}" >
    <td><strong><c:out value="${item.key}"/><strong></td>
    </c:forEach>
    </tr>
    </c:if>
    <tr>
    <c:forEach var="item1" items="${result}" >
    <td><c:out value="${item1.value}"/></td>
    </c:forEach>
    </tr>
</c:forEach>
</table>
</c:if>
</body>
</html>