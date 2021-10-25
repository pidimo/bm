<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/fantabulous.tld" prefix="fanta" %>
<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<c:set var="BODY_TYPE_HTML" value="<%=WebMailConstants.BODY_TYPE_HTML%>"/>
<c:set var="MAIL_PRIORITY_HIGHT" value="<%=WebMailConstants.MAIL_PRIORITY_HIGHT%>"/>
<c:set var="UNKNOWN_NAME" value="unknown_name."/>

<fmt:message var="numberFormat" key="numberFormat.1DecimalPlacesOptional"/>
<html>
<head>
    <title><fmt:message key="Webmail.printMail"/></title>
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

<body style="margin-left:30px;margin-right:30px;margin-bottom:20px;margin-top:20px;" onload="window.print();">

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
    <TR>
        <TD colspan="3" class="mailPrintContain">&nbsp;</TD>
    </TR>
    <TR>
        <TD width="15%" class="mailPrintLabel"><fmt:message key="Mail.from"/></TD>
        <TD width="80%" class="mailPrintContain">
            <c:out value="${emailForm.dtoMap['from']}"/>
        </TD>
        <TD width="5%" class="mailPrintContain" style="text-align:right;">
            <c:if test="${dto.mailPriority==MAIL_PRIORITY_HIGHT}">
                <img src="${baselayout}/img/webmail/prio_high.gif" border="0" alt=""/>
            </c:if>&nbsp;
        </TD>
    </TR>
    <TR>
        <TD class="mailPrintLabel"><fmt:message key="Mail.date"/></TD>
        <TD colspan="2" class="mailPrintContain">
            <fmt:message var="dateTimePattern" key="Webmail.mail.dateTimePatternNoTimezone"/>
            <c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
            <c:set var="locale" value="${sessionScope.user.valueMap['locale']}"/>
            <c:out value="${app2:getLocaleFormattedDateTime(dto.sentDate, timeZone, dateTimePattern,locale)}"/>
        </TD>
    </TR>
    <TR>
        <TD class="mailPrintLabel"><fmt:message key="Mail.to"/></TD>
        <TD colspan="2" class="mailPrintContain">
            <c:choose>
                <c:when test="${not empty param['dto(toEmail)']}">
                    <fanta:label var="personal" listName="mailRecipientList" module="/webmail" patron="0"
                                 columnOrder="personal">
                        <fanta:parameter field="mailId" value="${param['dto(mailId)']}"/>
                        <fanta:parameter field="email" value="${param['dto(toEmail)']}"/>
                    </fanta:label>
                    <c:choose>
                        <c:when test="${personal != param['dto(toEmail)']}">
                            <c:out value="\"${personal}\" <${param['dto(toEmail)']}>"/>&nbsp;
                        </c:when>
                        <c:otherwise>
                            <c:out value="${param['dto(toEmail)']}"/>&nbsp;
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <c:out value="${emailForm.dtoMap['to']}"/>&nbsp;
                </c:otherwise>
            </c:choose>
        </TD>
    </TR>
    <!--This param is coming from Communication/mail printing-->
    <c:if test="${empty param['dto(toEmail)']}">
        <TR>
            <TD class="mailPrintLabel"><fmt:message key="Mail.Cc"/></TD>
            <TD colspan="2" class="mailPrintContain">
                <c:out value="${emailForm.dtoMap['cc']}"/>&nbsp;
            </TD>
        </TR>
    </c:if>

    <TR>
        <TD class="mailPrintLabel" style="border-bottom: 1px #E6E6E6 solid;"><fmt:message
                key="Mail.subject"/></TD>
        <TD colspan="2" class="mailPrintContain">
            <c:out value="${emailForm.dtoMap['mailSubject']}"/>&nbsp;
        </TD>
    </TR>
</TABLE>


<c:choose>
    <c:when test="${emailForm.dtoMap['bodyType'] == BODY_TYPE_HTML}">
        <div style="text-align:left;padding:7px;color:black;width:100%;">
            <c:import url="/webmail/Mail/ViewMailBody.do?dto(mailId)=${emailForm.dtoMap['mailId']}&dto(op)=readBody"/>
        </div>
    </c:when>
    <c:otherwise>
        <div style="text-align:left;padding:7px;color:black;width:100%;">
            <c:out value="${app2:convertTextToHtml(emailForm.dtoMap['body'])}" escapeXml="false"/>
        </div>
    </c:otherwise>
</c:choose>

<TABLE border="0" cellpadding="0" cellspacing="0" align="center" width="100%">
    <tr>
        <td class="mailPrintContain" colspan="2">&nbsp;</td>
    </tr>
    <tr>
        <TD class="mailPrintLabel" style="border-bottom: 1px #E6E6E6 solid;" colspan="2">
            <fmt:message key="Mail.attaches"/>
        </TD>
    </tr>
    <c:forEach var="attach" items="${emailForm.dtoMap['attachments']}">
        <c:if test="${false == attach.visible}">
            <tr>
                <td width="80%" class="mailPrintContain">
                    <img src="${baselayout}/img/webmail/paperclip.gif" border="0" alt="" align="middle"/>&nbsp;
                    <c:choose>
                        <c:when test="${fn:contains(attach.attachFile,UNKNOWN_NAME)}">
                            <fmt:message key="Webmail.Attach.unknownFile"/>
                        </c:when>
                        <c:otherwise>
                            <c:out value="${attach.attachFile}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td width="20%" class="mailPrintContain" style="text-align:right;">
                    <c:choose>
                        <c:when test="${attach.size<1024}">
                            ${1} <fmt:message key="Webmail.mailTray.Kb"/>
                        </c:when>
                        <c:otherwise>
                            <fmt:formatNumber
                                    value="${fn:substringBefore(attach.size/1024,'.')}"
                                    type="number" pattern="${numberFormat}"/>
                            <fmt:message key="Webmail.mailTray.Kb"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:if>
    </c:forEach>
</TABLE>
</body>
</html>