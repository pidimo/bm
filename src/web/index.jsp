<%@ page import="com.piramide.elwis.utils.configuration.ConfigurationFactory" %>
<%@ page import="com.piramide.elwis.web.common.urlencrypt.UrlEncryptCipher" %>


<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<%--enable old index--%>
<%--
<c:set var="body" value="/common/index.jsp" scope="request"/>
<c:set var="urlBody" value="/layout/frontui/main.jsp"/>
--%>

<%--enable bootstrap index--%>
<c:set var="body" value="/WEB-INF/jsp/index.jsp" scope="request"/>
<c:set var="urlBody" value="/WEB-INF/jsp/layout/frontui/main.jsp"/>

<%
    if (request.getParameter("expired") != null) //sent the status Unauthorized in the header
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);


%>

<c:import url="${urlBody}"/>

<%


    if (request.getSession().getServletContext().getAttribute("homeSitePage") != null) {
        String homeUrl = ConfigurationFactory.getConfigurationManager().getValue("elwis.URL");
        if (!request.getParameterMap().containsKey("homePage")) {
            String message = null;
            if (request.getAttribute("errorContainerOutput") != null && !"".equals(request.getAttribute("errorContainerOutput").toString().trim()))
                message = "?message=" + UrlEncryptCipher.i.encrypt(request.getAttribute("errorContainerOutput").toString());
            response.sendRedirect(homeUrl + (message != null ? message : ""));
        }
    }

%>


