<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ page import="com.piramide.elwis.cmd.catalogmanager.LanguageUtilCmd" %>
<%@ page import="net.java.dev.strutsejb.dto.ResultDTO" %>
<%@ page import="net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ page import="net.java.dev.strutsejb.web.DefaultForm" %>
<c:set var="helpResourceKey" value="help.config.general.category.create" scope="request"/>

<c:set var="locale" value="${sessionScope.user.valueMap['locale']}" scope="request"/>
<c:set var="companyId" value="${sessionScope.user.valueMap['companyId']}" scope="request"/>
<%
    String iso = (String) request.getAttribute("locale");
    Integer companyId = (Integer) request.getAttribute("companyId");
    if (null != companyId && null != iso) {
        LanguageUtilCmd cmd = new LanguageUtilCmd();
        cmd.setOp("findSystemLanguageByIso");
        cmd.putParam("companyId", companyId);
        cmd.putParam("iso", iso);
        ResultDTO myResultDTO = BusinessDelegate.i.execute(cmd, request);
        Integer languageId = (Integer) myResultDTO.get("languageId");
        if (null != languageId) {
            DefaultForm f = (DefaultForm) request.getAttribute("categoryForm");
            f.setDto("languageId", languageId);
        }
    }
%>
<fmt:message var="title" key="Category.Title.create" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Category/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>

<c:set var="pagetitle" value="Common.configuration" scope="request"/>
<c:set var="windowTitle" value="Category.Title.create" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/catalogs/Category.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CatalogTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/Category.jsp" scope="request"/>
        <c:set var="tabs" value="/CatalogTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>