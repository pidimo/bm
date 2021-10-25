<%@ page import="com.piramide.elwis.web.uimanager.form.StyleSheetForm" %>
<%@ include file="/Includes.jsp" %>

<%
    StyleSheetForm styleSheetForm = (StyleSheetForm) request.getAttribute("styleSheetForm");
    //set from StyleSheetForwardAction class
    if (styleSheetForm.getDto("sectionMap") != null || request.getAttribute("hasErrors") != null) {
        request.setAttribute("sectionWithData", new Boolean(true));
    }
%>

<html:form action="${action}" styleClass="form-horizontal">

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
            <div class="row">
                <c:if test="${(not empty flagChangeCompanyStyle) && flagChangeCompanyStyle=='true'}">
                    <div class="col-xs-12 col-sm-7 col-md-8">
                        <p class="pageHeaderValue">
                            <fmt:message key="UIManager.Inf.ChangeCompanyStyle"/>
                        </p>
                    </div>
                </c:if>

                <c:if test="${updateCheckAccessRight == 'true'}">
                    <div class="col-xs-12 col-sm-5 col-md-4 marginButton pull-right">
                        <c:choose>
                            <c:when test="${(not empty companyStyleKey)}">
                                <app:link action="/UIManager/Company/Confirmation.do"
                                          styleClass="folderTabLink pull-right">
                                    <fmt:message key="UIManager.RestorePredeterminedAll"/>
                                </app:link>
                            </c:when>
                            <c:otherwise>
                                <app:link action="/UIManager/User/Confirmation.do"
                                          styleClass="folderTabLink pull-right">
                                    <fmt:message key="UIManager.RestorePredeterminedAll"/>
                                </app:link>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
            </div>
            <div class="${app2:getFormPanelClasses()}">
                <fieldset>
                    <legend class="title aaaaaa">
                        <fmt:message key="UIManager.ManagerStyle"/>
                    </legend>
                </fieldset>
                <div class="nopaddingContainTop col-xs-12 col-sm-3 marginButton">
                    <ul class="nav">
                        <c:set var="listSect" value="${app2:getSectionListOfXmlFile(pageContext.request)}"/>
                        <c:forEach var="list" items="${listSect}">
                            <c:choose>
                                <c:when test="${ list.value eq param['paramSection']}">
                                    <c:set var="tdClassId" value="selectedContain"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="tdClassId" value=""/>
                                </c:otherwise>
                            </c:choose>
                            <li class="contain" id="${tdClassId}">
                                <c:choose>
                                    <c:when test="${(not empty companyStyleKey)}">
                                        <app:link
                                                action="/UIManager/Forward/CompanyStyleSheet.do?paramSection=${app2:encode(list.value)}&${paramCompanyStyle}">
                                            <fmt:message key="${list.label}"/>
                                        </app:link>
                                    </c:when>
                                    <c:otherwise>
                                        <app:link
                                                action="/UIManager/Forward/UserStyleSheet.do?paramSection=${app2:encode(list.value)}">
                                            <fmt:message key="${list.label}"/>
                                        </app:link>
                                    </c:otherwise>
                                </c:choose>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="labelWithoutBorder col-xs-12 col-sm-9">
                    <c:if test="${ param['paramSection'] != 'null' }">
                        <%--stylesheet area--%>
                        <c:import url="/WEB-INF/jsp/uimanager/StyleSheet.jsp"/>
                    </c:if>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <%--restore all confirmation--%>
            <div class="${app2:getFormClasses()}">
                <div class="${app2:getFormPanelClasses()}">
                    <fieldset>
                        <legend class="title bbbbb">
                            <fmt:message key="UIManager.Confirmation"/>
                        </legend>
                        <p>
                            <fmt:message key="UIManager.MsgConfirmation.RestoreAll"/>
                        </p>
                    </fieldset>
                </div>

                <div class="wrapperSearch">
                    <c:if test="${updateCheckAccessRight == 'true'}">
                        <html:submit property="dto(restoreAll)" styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="UIManager.RestorePredeterminedAll"/>
                        </html:submit>
                        <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                            <fmt:message key="Common.cancel"/>
                        </html:cancel>
                    </c:if>
                </div>
            </div>
        </c:otherwise>
    </c:choose>

</html:form>