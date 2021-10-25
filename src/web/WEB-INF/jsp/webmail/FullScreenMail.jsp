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

<form class="form-horizontal">

    <div class="">
        <label class="${app2:getFormSmallLabelClasses()} label-left">
            <fmt:message key="Mail.from"/>
        </label>

        <div class="col-xs-12 col-sm-10 form-control-static">
            <c:out value="${emailForm.dtoMap['from']}"/>
            <div class="col-xs-1 pull-right">
                <c:if test="${dto.mailPriority==MAIL_PRIORITY_HIGHT}">
                    <img src="${baselayout}/img/webmail/prio_high.gif" border="0" alt=""/>
                </c:if>
            </div>
        </div>

    </div>
    <div class="">
        <label class="${app2:getFormSmallLabelClasses()} label-left">
            <fmt:message key="Mail.date"/>
        </label>

        <div class="${app2:getFormBigContainClasses(true)}">
            <fmt:message var="dateTimePattern" key="Webmail.mail.dateTimePatternNoTimezone"/>
            <c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
            <c:set var="locale" value="${sessionScope.user.valueMap['locale']}"/>
            <c:out value="${app2:getLocaleFormattedDateTime(dto.sentDate, timeZone, dateTimePattern,locale)}"/>
        </div>
    </div>
    <div class="">
        <label class="${app2:getFormSmallLabelClasses()} label-left">
            <fmt:message key="Mail.to"/>
        </label>

        <div class="${app2:getFormBigContainClasses(true)}">
            <c:choose>
                <c:when test="${not empty param['dto(toEmail)']}">
                    <fanta:label var="personal" listName="mailRecipientList" module="/webmail" patron="0"
                                 columnOrder="personal">
                        <fanta:parameter field="mailId" value="${param['dto(mailId)']}"/>
                        <fanta:parameter field="email" value="${param['dto(toEmail)']}"/>
                    </fanta:label>
                    <c:choose>
                        <c:when test="${personal != param['dto(toEmail)']}">
                            <c:out value="\"${personal}\" <${param['dto(toEmail)']}>"/>
                        </c:when>
                        <c:otherwise>
                            <c:out value="${param['dto(toEmail)']}"/>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <c:out value="${emailForm.dtoMap['to']}"/>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <!--This param is coming from Communication-->
    <c:if test="${empty param['dto(toEmail)']}">
        <div class="">
            <label class="${app2:getFormSmallLabelClasses()} label-left">
                <fmt:message key="Mail.Cc"/>
            </label>

            <div class="${app2:getFormBigContainClasses(true)}">
                <c:out value="${emailForm.dtoMap['cc']}"/>
            </div>
        </div>
    </c:if>
    <div class="clearfix"></div>
    <div class="">
        <label class="${app2:getFormSmallLabelClasses()} label-left">
            <fmt:message key="Mail.subject"/>
        </label>

        <div class="${app2:getFormBigContainClasses(true)}">
            <c:out value="${emailForm.dtoMap['mailSubject']}"/>
        </div>
    </div>
    <div class="clearfix"></div>
    <div class="">
        <c:if test="${null != emailForm.dtoMap['attachments']}">
            <c:set var="attachments" value="${emailForm.dtoMap['attachments']}" scope="request"/>
        </c:if>
        <c:import url="/WEB-INF/jsp/webmail/AttachmentReadFragment.jsp"/>
    </div>
<div class="clearfix"></div>

    <c:choose>
        <c:when test="${emailForm.dtoMap['bodyType'] == BODY_TYPE_HTML}">
            <div>
                <c:import
                        url="/webmail/Mail/ViewMailBody.do?dto(mailId)=${emailForm.dtoMap['mailId']}&dto(op)=readBody"/>
            </div>
        </c:when>
        <c:otherwise>
            <div class="col-xs-12 panel panel-default">
                <p style="word-wrap: break-word; ">
                    <c:out value="${app2:convertTextToHtml(emailForm.dtoMap['body'])}" escapeXml="false"/>
                </p>
            </div>
        </c:otherwise>
    </c:choose>
</form>