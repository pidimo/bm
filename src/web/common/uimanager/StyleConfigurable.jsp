<%@ page import="com.piramide.elwis.web.uimanager.form.StyleSheetForm"%>
<%@ include file="/Includes.jsp"%>

<%
    StyleSheetForm styleSheetForm = (StyleSheetForm)request.getAttribute("styleSheetForm");
    //set from StyleSheetForwardAction class
    if (styleSheetForm.getDto("sectionMap") != null || request.getAttribute("hasErrors") != null) {
        request.setAttribute("sectionWithData", new Boolean(true));
    }
%>

<html:form action="${action}">

<c:set var="op" value="create"/>
<c:if test="${ styleSheetForm.dtoMap['styleSheetId'] != null }">
    <html:hidden property="dto(styleSheetId)"/>
       <%--to check version--%>
    <html:hidden property="dto(version)"/>
    <c:set var="op" value="update"/>
</c:if>
<html:hidden property="dto(op)" value="${op}"/>

<html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
<html:hidden property="dto(companyStyle)" value="${companyStyleKey}"/>
<html:hidden property="dto(flagChangeCompanyStyle)" value="${flagChangeCompanyStyle}"/>
<html:hidden property="dto(styleSheetType)" value="${app2:getStyleSheetTypeByUIMode(pageContext.request)}"/>


<c:choose>
    <c:when test="${(not empty companyStyleKey)}">
        <%--param only to company--%>
        <c:set var="paramCompanyStyle" value="companyStyleKey=${companyStyleKey}"/>
        <app2:checkAccessRight functionality="COMPANYINTERFACE" permission="UPDATE">
            <c:set var="updateCheckAccessRight" value="true" scope="request"/>
        </app2:checkAccessRight>
    </c:when>
    <c:otherwise>
        <app2:checkAccessRight functionality="USERINTERFACE" permission="UPDATE">
            <c:set var="updateCheckAccessRight" value="true" scope="request"/>
        </app2:checkAccessRight>
    </c:otherwise>
</c:choose>

<c:choose>
<c:when test="${ sectionWithData }">
    <table cellSpacing=0 cellPadding=3 width="80%" border=0 align="center">
        <tr>
            <c:if test="${(not empty flagChangeCompanyStyle) && flagChangeCompanyStyle=='true'}">
                <td class="pageHeaderValue">
                    <fmt:message key="UIManager.Inf.ChangeCompanyStyle"/>
                </td>
            </c:if>
            <td align="right">
                <c:if test="${updateCheckAccessRight == 'true'}">
                    <c:choose>
                        <c:when test="${(not empty companyStyleKey)}">
                            <app:link action="/UIManager/Company/Confirmation.do" styleClass="folderTabLink"><fmt:message key="UIManager.RestorePredeterminedAll"/></app:link>
                        </c:when>
                        <c:otherwise>
                            <app:link action="/UIManager/User/Confirmation.do" styleClass="folderTabLink"><fmt:message key="UIManager.RestorePredeterminedAll"/></app:link>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </td>
        </tr>
    </table>
    <table cellSpacing="0" cellPadding="0" width="80%" border="0" align="center"  >
        <tr>
            <td colspan="2" class="title">
                <fmt:message key="UIManager.ManagerStyle"/>
            </td>
        </tr>
        <tr>
            <td class="nopaddingContainTop" width="30%" >
                <table width="100%" >
                        <c:set var="listSect" value="${app2:getSectionListOfXmlFile(pageContext.request)}"/>
                        <c:forEach var="list"  items="${listSect}">
                            <tr>
                                <c:choose>
                                    <c:when test="${ list.value eq param['paramSection']}">
                                        <c:set var="tdClassId"  value="selectedContain"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="tdClassId"  value=""/>
                                    </c:otherwise>
                                </c:choose>
                                <td class="contain" id="${tdClassId}">
                                    <c:choose>
                                        <c:when test="${(not empty companyStyleKey)}">
                                            <app:link action="/UIManager/Forward/CompanyStyleSheet.do?paramSection=${app2:encode(list.value)}&${paramCompanyStyle}"><fmt:message key="${list.label}"/></app:link>
                                        </c:when>
                                        <c:otherwise>
                                            <app:link action="/UIManager/Forward/UserStyleSheet.do?paramSection=${app2:encode(list.value)}"><fmt:message key="${list.label}"/></app:link>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                </table>
            </td>
            <td width="70%" class="labelWithoutBorder" >
                <table width="100%" >
                    <tr>
                        <td>
                            <c:if test="${ param['paramSection'] != 'null' }">
                                <%--stylesheet area--%>
                                <c:import url="/common/uimanager/StyleSheet.jsp"/>
                            </c:if>
                        </td>
                    </tr>
               </table>
            </td>
        </tr>

    </table>
</c:when>
<c:otherwise>
    <%--restore all confirmation--%>
    <table cellSpacing=0 cellPadding=0 width="80%" border=0 align="center" class="container">
        <tr>
            <td class="title">
                <fmt:message key="UIManager.Confirmation"/>
            </td>
        </tr>
        <tr>
            <td class="contain">
                <fmt:message key="UIManager.MsgConfirmation.RestoreAll"/>
            </td>
        </tr>
    </table>
    <table cellSpacing=0 cellPadding=4 width="80%" border=0 align="center">
        <tr>
            <td class="button">
                <c:if test="${updateCheckAccessRight == 'true'}">
                    <html:submit property="dto(restoreAll)" styleClass="button" ><fmt:message key="UIManager.RestorePredeterminedAll"/></html:submit>
                    <html:cancel styleClass="button" ><fmt:message   key="Common.cancel"/></html:cancel>
                </c:if>
            </td>
        </tr>
    </table>
</c:otherwise>
</c:choose>

</html:form>