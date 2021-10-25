<%@ page import="com.piramide.elwis.cmd.contactmanager.AddressRoutePageCmd,
                 com.piramide.elwis.utils.Constants,
                 com.piramide.elwis.web.admin.session.User,
                 net.java.dev.strutsejb.AppLevelException,
                 net.java.dev.strutsejb.dto.ResultDTO,
                 net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<%

    Object companyId = ((User) request.getSession().getAttribute(Constants.USER_KEY)).getValue("companyId");
    Object addressId = request.getParameter("contactId");
    Object locale = ((User) request.getSession().getAttribute(Constants.USER_KEY)).getValue("locale");
    String urlRoute = null;

    if(companyId != null && addressId != null) //try to show if data allows it
    {
        try {
            AddressRoutePageCmd routeCmd = new AddressRoutePageCmd();
            routeCmd.putParam("companyId", companyId);
            routeCmd.putParam("addressId", addressId);
            routeCmd.putParam("locale", locale);
            ResultDTO routeResultDTO = BusinessDelegate.i.execute(routeCmd, request);
            urlRoute = routeResultDTO.getAsString("routeURL");

        } catch(AppLevelException e) {
            //nothing to do
        }
    }
%>
<% if(urlRoute != null && !"".equals(urlRoute.trim())) { %>
<a href="<%= urlRoute %>" target="_blank">
    <img src="<c:out value="${sessionScope.baselayout}"/>/img/map.jpg"  alt="<fmt:message    key="Contact.map24.traceRoute"/>" title="<fmt:message    key="Contact.map24.traceRoute"/>" border="0" align="bottom" width="14" height="11"/>
</a>
<% } %>