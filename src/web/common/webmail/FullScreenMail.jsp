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

<table width="100%">
    <tr>
        <td width="100%" class="contain" style="text-align:right; padding-right: 15px;">
            <a href="javascript:window.close();" title="<fmt:message key="Common.close"/>"><fmt:message
                    key="Common.close"/></a>
        </td>
    </tr>
</table>

<TABLE border="0" cellpadding="0" cellspacing="0" align="center" width="100%">
    <tr>
        <td class="title" colspan="3">
            <fmt:message key="Mail.Title.read"/>
        </td>
    </tr>
    <TR>
        <TD width="15%" class="label"><fmt:message key="Mail.from"/></TD>
        <TD width="80%" class="contain">
            <c:out value="${emailForm.dtoMap['from']}"/>
        </TD>
        <TD width="5%" class="contain" style="text-align:right;">
            <c:if test="${dto.mailPriority==MAIL_PRIORITY_HIGHT}">
                <img src="${baselayout}/img/webmail/prio_high.gif" border="0" alt=""/>
            </c:if>&nbsp;
        </TD>
    </TR>
    <TR>
        <TD class="label"><fmt:message key="Mail.date"/></TD>
        <TD colspan="2" class="contain">
            <fmt:message var="dateTimePattern" key="Webmail.mail.dateTimePatternNoTimezone"/>
            <c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
            <c:set var="locale" value="${sessionScope.user.valueMap['locale']}"/>
            <c:out value="${app2:getLocaleFormattedDateTime(dto.sentDate, timeZone, dateTimePattern,locale)}"/>
        </TD>
    </TR>
    <TR>
        <TD class="label"><fmt:message key="Mail.to"/></TD>
        <TD colspan="2" class="contain">
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
    <!--This param is coming from Communication-->
    <c:if test="${empty param['dto(toEmail)']}">
        <TR>
            <TD class="label"><fmt:message key="Mail.Cc"/></TD>
            <TD colspan="2" class="contain">
                <c:out value="${emailForm.dtoMap['cc']}"/>&nbsp;
            </TD>
        </TR>
    </c:if>

    <TR>
        <TD class="label" style="border-bottom: 1px #E6E6E6 solid;"><fmt:message
                key="Mail.subject"/></TD>
        <TD colspan="2" class="contain">
            <c:out value="${emailForm.dtoMap['mailSubject']}"/>&nbsp;
        </TD>
    </TR>
    <tr>
        <td colspan="3">
            <c:if test="${null != emailForm.dtoMap['attachments']}">
                <c:set var="attachments" value="${emailForm.dtoMap['attachments']}" scope="request"/>
            </c:if>
            <c:import url="/common/webmail/AttachmentReadFragment.jsp"/>
        </td>
    </tr>
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
