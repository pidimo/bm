${sessionScope[param.var]}
<%
    request.getSession().removeAttribute(request.getParameter("var"));
%>
