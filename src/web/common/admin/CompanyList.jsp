<%@ page import="com.piramide.elwis.utils.AdminConstants" %>
<%@ page language="java" session="true" %>
<%@ include file="/Includes.jsp" %>

<c:set var="defaultTemplate" value="<%=AdminConstants.CompanyTemplateTypes.defaultTemplate.getConstantAsString()%>"/>
<c:set var="trialTemplate" value="<%=AdminConstants.CompanyTemplateTypes.trialTemplate.getConstantAsString()%>"/>

<c:set var="path" value="${pageContext.request.contextPath}"/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<br>
<table width="98%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
<tr>
    <td height="20" class="title" colspan="2">
        <fmt:message key="Admin.Company.Title"/>

    </td>
</tr>
<html:form action="/Company/List.do" focus="parameter(name1@_name2@_login)">
    <TR>
        <td class="label" width="15%"><fmt:message key="Common.search"/></td>
        <td align="left" class="contain" width="85%">
            <html:text property="parameter(name1@_name2@_login)" styleClass="largeText" maxlength="40"/>&nbsp;
            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
        </td>
    </TR>
</html:form>
<tr>
    <td colspan="2" align="center" class="alpha">
        <fanta:alphabet action="Company/List.do" parameterName="name1Alpha"/>
    </td>
</tr>

<tr>
    <html:form action="/Company/Forward/Create.do?op=create">
        <td class="button" colspan="2"><br>
            <app2:securitySubmit operation="CREATE" functionality="COMPANY" styleClass="button">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </td>
    </html:form>
</tr>

<tr>
    <td colspan="2">
        <fmt:message var="datePattern" key="datePattern"/>
        <fanta:table list="companyList" width="100%" id="companyValue" action="Company/List.do"
                     imgPath="${baselayout}" align="center">
            <c:set var="editLink"
                   value="Company/Forward/Update.do?dto(companyId)=${companyValue.companyId}&dto(companyName)=${app2:encode(companyValue.companyName)}"/>
            <c:set var="deleteLink"
                   value="/Company/Forward/Delete.do?dto(companyId)=${companyValue.companyId}&dto(companyName)=${app2:encode(companyValue.companyName)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="COMPANY" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem"
                                        headerStyle="listHeader" width="33%" image="${baselayout}/img/edit.gif"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="COMPANY" permission="DELETE">
                    <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="33%">
                        <c:if test="${companyValue.isDefault==0}">
                            <html:link
                                    page="${deleteLink}"
                                    titleKey="Common.delete">
                                <html:img src="${baselayout}/img/delete.gif" titleKey="Common.delete"
                                          border="0"/>
                            </html:link>
                        </c:if>
                    </fanta:actionColumn>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="companyName" action="${editLink}" styleClass="listItem" title="Company.name"
                              headerStyle="listHeader" width="15%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="login" styleClass="listItem" title="Company.login" headerStyle="listHeader"
                              width="13%" orderable="true"/>
            <fanta:dataColumn name="active" styleClass="listItem" title="Company.active" headerStyle="listHeader"
                              width="5%" orderable="true" renderData="false">
                <c:choose>
                    <c:when test="${companyValue.active== 1}">
                        <img align="center" src="<c:out value="${sessionScope.baselayout}"/>/img/check.gif" alt=""/>
                    </c:when>
                    <c:otherwise>
                        &nbsp;
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>
            <fanta:dataColumn name="trial" styleClass="listItem" title="Company.trial" headerStyle="listHeader"
                              width="5%" orderable="true" renderData="false">
                <c:choose>
                    <c:when test="${companyValue.trial== 1}">
                        <img align="center" src="<c:out value="${sessionScope.baselayout}"/>/img/check.gif" alt=""/>
                    </c:when>
                    <c:otherwise>
                        &nbsp;
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>

            <fanta:dataColumn name="usersAllowed" styleClass="listItemRight" title="Company.usersAllowed"
                              headerStyle="listHeader" width="7%" orderable="true"/>
            <fanta:dataColumn name="recordDate" styleClass="listItem" title="Common.creationDate"
                              headerStyle="listHeader" width="12%" orderable="true" renderData="false">
                <fmt:formatDate var="dateRecordDate" value="${app2:intToDate(companyValue.recordDate)}"
                                pattern="${datePattern}"/>
                ${dateRecordDate}&nbsp;
            </fanta:dataColumn>
            <fanta:dataColumn name="startLicense" styleClass="listItem" title="Company.startLicenseDate"
                              headerStyle="listHeader" width="12%" orderable="true" renderData="false">
                <fmt:formatDate var="dateStartLicense" value="${app2:intToDate(companyValue.startLicense)}"
                                pattern="${datePattern}"/>
                ${dateStartLicense}&nbsp;
            </fanta:dataColumn>
            <fanta:dataColumn name="finishLicense" styleClass="listItem" title="Company.finishLicenseDate"
                              headerStyle="listHeader" width="12%" orderable="true" renderData="false">
                <fmt:formatDate var="datefinishLicense" value="${app2:intToDate(companyValue.finishLicense)}"
                                pattern="${datePattern}"/>
                ${datefinishLicense}&nbsp;
            </fanta:dataColumn>
            <fanta:dataColumn name="copyTemplateCompany" styleClass="listItem2" title="Company.templateType"
                              headerStyle="listHeader" width="15%" orderable="true" renderData="false">
                <c:choose>
                    <c:when test="${defaultTemplate == companyValue.copyTemplateCompany}">
                        <fmt:message key="Company.commonTemplateList"/>&nbsp;(<fmt:message
                            key="${app2:getSystemLanguageResource(companyValue.templateLanguage)}"/>)
                    </c:when>
                    <c:when test="${trialTemplate == companyValue.copyTemplateCompany}">
                        <fmt:message key="Company.trialTemplateList"/>&nbsp;(<fmt:message
                            key="${app2:getSystemLanguageResource(companyValue.templateLanguage)}"/>)
                    </c:when>
                    <c:otherwise>
                        &nbsp;
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>
        </fanta:table>
    </td>
</tr>
<tr>
    <html:form action="/Company/Forward/Create.do?op=create">
        <td class="button" colspan="2"><!--Button create up -->
            <app2:securitySubmit operation="CREATE" functionality="COMPANY" styleClass="button">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </td>
    </html:form>
</tr>
</table>
