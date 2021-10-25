<%@ page import="net.java.dev.strutsejb.web.DefaultForm" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/fantabulous.tld" prefix="fanta" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

<c:set var="phone" value="<%= com.piramide.elwis.utils.CommunicationTypes.PHONE %>" scope="page"/>
<c:set var="meeting" value="<%= com.piramide.elwis.utils.CommunicationTypes.MEETING %>" scope="page"/>
<c:set var="other" value="<%= com.piramide.elwis.utils.CommunicationTypes.OTHER %>" scope="page"/>
<c:set var="document" value="<%= com.piramide.elwis.utils.CommunicationTypes.DOCUMENT %>" scope="page"/>

<fmt:message var="datePattern" key="datePattern"/>
<fmt:message var="numberFormat" key="numberFormat.1DecimalPlacesOptional"/>

<html>
<head>
    <title><fmt:message key="Webmail.printMail"/></title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/elwis_style.css"/>"/>
    <style type="text/css">
        TD.mailPrintLabel {
            padding: 0px 5px 0px 7px;
            font-family: Verdana, Helvetica, sans-serif;
            font-size: 11px;
            text-align: left;
            vertical-align: middle;
            height: 22px;
            font-weight: bold;
            border-bottom: 1px #FFFFFF solid;
            background-color: #E6E6E6;
        }

        TD.mailPrintContain {
            padding: 0px 5px 0px 7px;
            font-family: Verdana, Helvetica, sans-serif;
            font-size: 11px;
            text-align: left;
            vertical-align: middle;
            height: 22px;
            font-weight: lighter;
            border-bottom: 1px #E6E6E6 solid;
        }

        BODY {
            background-color: #ffffff;
        }
    </style>
</head>

<body style="margin-left:30px;margin-right:30px;margin-bottom:20;margin-top:20px;" onload="window.print();">
<%
    DefaultForm defaultForm = new DefaultForm();
    pageContext.setAttribute("org.apache.struts.taglib.html.BEAN", defaultForm, 2);
%>
<table width="100%">
    <tr>
        <td width="100%" style="text-align:right;">
            <a href="javascript:window.print();" title="<fmt:message key="Common.print"/>"><fmt:message
                    key="Common.print"/></a>
            -
            <a href="javascript:window.close();" title="<fmt:message key="Common.close"/>"><fmt:message
                    key="Common.close"/></a>
        </td>
    </tr>
    <tr>
        <td align="left" width="100%">
            <c:set var="labelCompanyLogoId" value="companyLogoId_${sessionScope.user.valueMap['companyId']}"/>
            <c:set var="logoId" value="${applicationScope[labelCompanyLogoId]}"/>
            <c:if test="${(not empty logoId)}">
                <c:set var="companyLogoChange" value="companyLogoStatus_${sessionScope.user.valueMap['companyId']}"/>
                <c:url var="urlCompanyLogo"
                       value="/Company/DownloadLogoImage.do?dto(freeTextId)=${logoId}&logoChangeCount=${applicationScope[companyLogoChange]}&isCompanyLogo=true"/>
                <img src="${urlCompanyLogo}" border="0" alt=""/>
            </c:if>
        </td>
    </tr>
</table>
<TABLE border="0" cellpadding="0" cellspacing="0" align="center" width="100%">
    <tr>
        <td class="mailPrintLabel" width="25%">
            <fmt:message key="Communication.type"/>
        </td>
        <td class="mailPrintContain" width="75%">
            <c:set var="communicationTypes" value="${app2:defaultCommunicationTypes(pageContext.request)}"/>
            <html:select property="dto(type)"
                         styleClass="mediumSelect"
                         value="${mainCommunicationForm.dtoMap['type']}"
                         readonly="true">
                <html:options collection="communicationTypes" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <td class="mailPrintLabel">
            <fmt:message key="Document.date"/>
        </td>
        <td class="mailPrintContain">
            <fmt:formatDate value="${app2:intToDate(mainCommunicationForm.dtoMap['dateStart'])}"
                            pattern="${datePattern}"/>
        </td>
    </tr>
    <tr>
        <td class="mailPrintLabel">
            <fmt:message key="Document.subject"/>
        </td>
        <td class="mailPrintContain">
            <c:out value="${mainCommunicationForm.dtoMap['note']}"/>
        </td>
    </tr>
    <tr>
        <td class="mailPrintLabel">
            <fmt:message key="Document.employee"/>
        </td>
        <td class="mailPrintContain">
            <fanta:select property="dto(employeeId)"
                          listName="employeeBaseList"
                          labelProperty="employeeName"
                          valueProperty="employeeId"
                          styleClass="mediumSelect"
                          readOnly="true"
                          value="${mainCommunicationForm.dtoMap['employeeId']}"
                          module="/contacts">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </td>
    </tr>
    <tr>
        <td class="mailPrintLabel">
            <fmt:message key="Document.contactPerson"/>
        </td>
        <td class="mailPrintContain">
            <c:out value="${mainCommunicationForm.dtoMap['contactPersonName']}"/>
        </td>
    </tr>
    <c:if test="${phone == mainCommunicationForm.dtoMap['type']}">
        <tr>
            <td class="mailPrintLabel">
                <fmt:message key="Document.phoneNumber"/>
            </td>
            <td class="mailPrintContain">
                <c:out value="${mainCommunicationForm.dtoMap['contactNumber']}"/>
            </td>
        </tr>
    </c:if>
    <c:if test="${document == mainCommunicationForm.dtoMap['type']}">
        <tr>
            <td class="mailPrintLabel">
                <fmt:message key="Communication.document"/>
            </td>
            <td class="mailPrintContain">
                <c:out value="${mainCommunicationForm.dtoMap['documentFileName']}"/>
            </td>
        </tr>
    </c:if>
    <c:if test="${mainCommunicationForm.dtoMap['processId'] != null && mainCommunicationForm.dtoMap['processId'] != ''}">
        <tr>
            <td class="mailPrintLabel">
                <fmt:message key="SalesProcess"/>
            </td>
            <td class="mailPrintContain">
                <c:out value="${mainCommunicationForm.dtoMap['processName']}"/>
            </td>
        </tr>
        <tr>
            <td class="mailPrintLabel">
                <fmt:message key="SalesProcess.probability"/>
            </td>
            <td class="mailPrintContain">
                <c:set var="probabilities" value="${app2:defaultProbabilities()}"/>
                <html:select property="dto(probability)"
                             styleClass="mediumSelect"
                             readonly="true"
                             tabindex="15"
                             value="${mainCommunicationForm.dtoMap['probability']}">
                    <html:options collection="probabilities" property="value" labelProperty="label"/>
                </html:select>
                <fmt:message key="Common.probabilitySymbol"/>
            </td>
        </tr>
    </c:if>
    <tr>
        <td class="mailPrintLabel">
            <fmt:message key="Document.inout"/>
        </td>
        <td class="mailPrintContain">
            <c:if test="${'1' == mainCommunicationForm.dtoMap['inOut']}">
                <fmt:message key="Document.in"/>
            </c:if>
            <c:if test="${'0' == mainCommunicationForm.dtoMap['inOut']}">
                <fmt:message key="Document.out"/>
            </c:if>
        </td>
    </tr>
</TABLE>
<div style="text-align:left;padding:7px;color:black;width:100%;">
    <c:out value="${app2:convertTextToHtml(mainCommunicationForm.dtoMap['freeText'])}" escapeXml="false"/>
</div>
</body>
</html>