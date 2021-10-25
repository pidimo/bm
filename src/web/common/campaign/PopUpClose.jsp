<%@ page import="java.util.Map,
                 java.util.HashMap"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<br/>
<c:if test="${hasErrors != null}">
<table width="80%" border="0" align="center" cellpadding="2" cellspacing="0" class="searchContainer">
    <tr>
        <td align="center" >
        <a href="javascript:window.close();"><strong><fmt:message    key="Common.close"/></strong></a>
        </td>
    </tr>
</table>
</c:if>
