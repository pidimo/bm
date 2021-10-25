<%@ page import="com.piramide.elwis.utils.AdminConstants" %>
<%@ page language="java" session="true" %>
<%@ include file="/Includes.jsp" %>

<%
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
%>

<c:set var="defaultTemplate" value="<%=AdminConstants.CompanyTemplateTypes.defaultTemplate.getConstantAsString()%>"/>
<c:set var="trialTemplate" value="<%=AdminConstants.CompanyTemplateTypes.trialTemplate.getConstantAsString()%>"/>

<c:set var="path" value="${pageContext.request.contextPath}"/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<html:form action="/Company/List.do" focus="parameter(name1@_name2@_login)" styleClass="form-horizontal">

    <legend class="title">
        <fmt:message key="Admin.Company.Title"/>
    </legend>

    <div class="${app2:getSearchWrapperClasses()}">
        <div class="form-group col-sm-4">
            <label class="${app2:getFormLabelClasses()} label-left" for="search_id">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormContainClasses(false)}">
                <html:text
                        property="parameter(name1@_name2@_login)"
                        styleClass="${app2:getFormInputClasses()} largeText" styleId="search_id"
                        maxlength="40" tabindex="1"/>
            </div>
        </div>

        <div class="form-group col-sm-4">
            <label class="${app2:getFormLabelClasses()}">
                <fmt:message key="Company.companyType"/>
            </label>

            <div class="${app2:getFormContainClasses(false)}">
                <c:set var="companyTypes" value="${app2:getCompanyTypes(pageContext.request)}"/>
                <html:select property="parameter(companyType)" styleId="companyType_id"
                             styleClass="${app2:getFormSelectClasses()} mediumSelect"
                             tabindex="2">
                    <html:option value="">&nbsp;</html:option>
                    <html:options collection="companyTypes" property="value" labelProperty="label"/>
                </html:select>
            </div>
        </div>

        <div class="form-group col-sm-3">
            <label class="${app2:getFormLabelClasses()}" for="activeId">

            </label>

            <div class="${app2:getFormContainClasses(false)}">
                <html:select property="parameter(active)" styleClass="shortSelect ${app2:getFormSelectClasses()}"
                             tabindex="3"
                             styleId="activeId">
                    <html:options collection="activeList" property="value" labelProperty="label"/>
                </html:select>
            </div>
        </div>

        <div class="form-group col-sm-1">
            <div class="col-sm-12">
                <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="4">
                    <fmt:message key="Common.go"/>
                </html:submit>
            </div>
        </div>
    </div>
</html:form>

<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet action="Company/List.do" parameterName="name1Alpha" mode="bootstrap"/>
</div>

<html:form action="/Company/Forward/Create.do?op=create">
    <div class="${app2:getFormGroupClasses()}">
        <app2:securitySubmit operation="CREATE" functionality="COMPANY"
                             styleClass="${app2:getFormButtonClasses()}">
            <fmt:message key="Common.new"/>
        </app2:securitySubmit>
    </div>
</html:form>

<fmt:message var="datePattern" key="datePattern"/>
<div class="table-responsive">
    <fanta:table mode="bootstrap" styleClass="${app2:getFantabulousTableClases()}"
                 list="companyList" width="100%" id="companyValue" action="Company/List.do"
                 imgPath="${baselayout}" align="center">
        <c:set var="editLink"
               value="Company/Forward/Update.do?dto(companyId)=${companyValue.companyId}&dto(companyName)=${app2:encode(companyValue.companyName)}"/>
        <c:set var="deleteLink"
               value="/Company/Forward/Delete.do?dto(companyId)=${companyValue.companyId}&dto(companyName)=${app2:encode(companyValue.companyName)}"/>
        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <app2:checkAccessRight functionality="COMPANY" permission="VIEW">
                <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem"
                                    headerStyle="listHeader" width="33%"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="COMPANY" permission="DELETE">
                <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="33%">
                    <c:if test="${companyValue.isDefault==0}">
                        <html:link
                                page="${deleteLink}"
                                titleKey="Common.delete">
                            <span class="${app2:getClassGlyphTrash()}"></span>
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
                    <span class="${app2:getClassGlyphOk()}"></span>
                </c:when>
                <c:otherwise>
                    &nbsp;
                </c:otherwise>
            </c:choose>
        </fanta:dataColumn>
        <fanta:dataColumn name="companyType" styleClass="listItem" title="Company.companyType" headerStyle="listHeader"
                          width="8%" orderable="true" renderData="false">
            <c:out value="${app2:getCompanyTypeMessage(companyValue.companyType, pageContext.request)}"/>
        </fanta:dataColumn>

        <fanta:dataColumn name="usersAllowed" styleClass="listItemRight" title="Company.usersAllowed"
                          headerStyle="listHeader" width="7%" orderable="true"/>
        <fanta:dataColumn name="recordDate" styleClass="listItem" title="Common.creationDate"
                          headerStyle="listHeader" width="11%" orderable="true" renderData="false">
            <fmt:formatDate var="dateRecordDate" value="${app2:intToDate(companyValue.recordDate)}"
                            pattern="${datePattern}"/>
            ${dateRecordDate}&nbsp;
        </fanta:dataColumn>
        <fanta:dataColumn name="startLicense" styleClass="listItem" title="Company.startLicenseDate"
                          headerStyle="listHeader" width="11%" orderable="true" renderData="false">
            <fmt:formatDate var="dateStartLicense" value="${app2:intToDate(companyValue.startLicense)}"
                            pattern="${datePattern}"/>
            ${dateStartLicense}&nbsp;
        </fanta:dataColumn>
        <fanta:dataColumn name="finishLicense" styleClass="listItem" title="Company.finishLicenseDate"
                          headerStyle="listHeader" width="11%" orderable="true" renderData="false">
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
</div>

<html:form action="/Company/Forward/Create.do?op=create">
    <div class="${app2:getFormGroupClasses()}"><!--Button create up -->
        <app2:securitySubmit operation="CREATE" functionality="COMPANY" styleClass="${app2:getFormButtonClasses()}">
            <fmt:message key="Common.new"/>
        </app2:securitySubmit>
    </div>
</html:form>

