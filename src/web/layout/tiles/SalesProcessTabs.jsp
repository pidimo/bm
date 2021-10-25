<%@ page import="com.piramide.elwis.cmd.salesmanager.LightlySalesProcessCmd,
                 net.java.dev.strutsejb.dto.ResultDTO,
                 net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>

<c:set var="tabHeaderLabel" value="SalesProcess" scope="request"/> <%--send the app. resource key.--%>
<%
    boolean errorPage = false;
    LightlySalesProcessCmd lightlySalesProcessCmd = new LightlySalesProcessCmd();
    lightlySalesProcessCmd.putParam("processId", request.getParameter("processId"));
    try {
        ResultDTO resultDTO = BusinessDelegate.i.execute(lightlySalesProcessCmd, request);
        request.setAttribute("processName", resultDTO.get("processName"));
        request.setAttribute("probability", resultDTO.get("probability"));
        request.setAttribute("processAddressId", resultDTO.get("addressId"));
        //address info
        request.setAttribute("addressName", resultDTO.get("addressName"));
        request.setAttribute("city", resultDTO.get("city"));
        request.setAttribute("countryCode", resultDTO.get("countryCode"));

        if (resultDTO.isFailure())
            errorPage = true;
    } catch (Exception e) {
        errorPage = true;
    }
    request.setAttribute("errorPage", new Boolean(errorPage));
%>

<c:if test="${!errorPage}">
    <c:set var="tabHeaderValue" scope="request">
        ${processName}
        (${addressName}
        <c:if test="${countryCode!= null}">
            / <c:out value="${countryCode}"/>
        </c:if>
        <c:if test="${city != null && countryCode != null}">
            - <c:out value="${city}"/>
        </c:if>
        <c:if test="${city != null && countryCode == null}">
            / <c:out value="${city}"/>
        </c:if>)
    </c:set>
    <c:import url="${layout}/TabHeader.jsp"/>


    <jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
    <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
        <c:set target="${tabItems}" property="SalesProcess.Tab.detail" value="/SalesProcess/Forward/Update.do"/>
    </app2:checkAccessRight>

    <app2:checkAccessRight functionality="CATEGORYTAB" permission="VIEW">
        <%
            String processId = request.getParameter("processId");
            List paramsList = new ArrayList();
            paramsList.add(Integer.valueOf(processId));
            request.setAttribute("paramsList", paramsList);
        %>
        <c:set var="categoryTabs"
               value="${app2:getCategoryTabs('6', 'findValueBySalesProcessId', paramsList, pageContext.request)}"/>
        <c:forEach var="tab" items="${categoryTabs}">
            <c:set target="${tabItems}" property="001@100-${tab.label}"
                   value="/CategoryTab/Forward/Update.do?categoryTabId=${tab.categoryTabId}&dto(categoryTabId)=${tab.categoryTabId}"/>
        </c:forEach>
    </app2:checkAccessRight>


    <app2:checkAccessRight functionality="COMMUNICATION" permission="VIEW">
        <c:set target="${tabItems}" property="Contacts.Tab.communications" value="/Communication/List.do"/>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="TASK" permission="VIEW">
        <c:set target="${tabItems}" property="Scheduler.Tasks" value="/TaskList.do?simple=true"/>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="SALESPROCESSEVALUATION" permission="EXECUTE">
        <c:set target="${tabItems}" property="SalesProcess.Title.evaluation" value="/SalesProcess/Evaluation.do"/>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="SALE" permission="VIEW">
        <c:set target="${tabItems}" property="SalesProcess.tab.sale" value="/SalesProcess/Sale/List.do"/>
    </app2:checkAccessRight>

    <jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>
    <c:set target="${tabParams}" property="dto(processId)" value="${param.processId}"/>

    <c:if test="${empty param.addressId}">
        <c:set target="${tabParams}" property="addressId" value="${processAddressId}"/>
    </c:if>

    <c:import url="${sessionScope.layout}/submenu.jsp"/>
</c:if>


