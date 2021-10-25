<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>

<tags:jscript language="JavaScript" src="/js/contacts/contact.jsp"/>

<c:set var="MAIL_PRIORITY_HIGHT" value="<%=WebMailConstants.MAIL_PRIORITY_HIGHT%>"/>
<c:set var="MAIL_PRIORITY_DEFAULT" value="<%=WebMailConstants.MAIL_PRIORITY_DEFAULT%>"/>
<c:set var="BODY_TYPE_HTML" value="<%=WebMailConstants.BODY_TYPE_HTML%>"/>
<c:set var="BODY_TYPE_TEXT" value="<%=WebMailConstants.BODY_TYPE_TEXT%>"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<c:set var="locale" value="${sessionScope.user.valueMap['locale']}"/>

<%--
emailPriority   = email priority emailForm.dtoMap['mailPriority']
fromAddressId   =  address identifier emailForm.dtoMap['fromAddressId']
fromAddressType = address type ( person or organization )emailForm.dtoMap['fromAddressType']
fromContactPersonId = the contact peson id  ( person or organization )emailForm.dtoMap['fromContactPersonId']
sentDate        = sent date of email emailForm.dtoMap['sentDate']
bodyType        = body type (html o plain text) emailForm.dtoMap['bodyType']
mailId          = email identifier emailForm.dtoMap['mailId']
attachments     = attachments list emailForm.dtoMap['attachments']
htmlBodyPage    = header for read html body
showRecipientsCC= 'true' show cc recipients, 'false' hidde cc recipients
--%>
<c:if test="${empty showRecipientsCC}">
    <c:set var="showRecipientsCC" value="true" scope="request"/>
</c:if>

<script type="text/javascript">

    $(function(){
        $('#bodyIFrameId').load(function(){
            resizeBodyIframe();
        });
    });

    $( window ).load(function() {
        <c:if test="${bodyType == BODY_TYPE_TEXT}">
            resizeBodyText();
        </c:if>
    });

    function resizeBodyIframe() {
        var ifrm = document.getElementById("bodyIFrameId");
        var doc = ifrm.contentDocument ? ifrm.contentDocument : ifrm.contentWindow.document;
        var h = getDocHeight( doc );

        h = h + 20;
        if(h > 350) {
            $("#bodyIFrameId").css({ height: h +'px'});
        }
    }

    function resizeBodyText() {
        var h = $("#bodyTextId").prop('scrollHeight');
        h = h + 10;
        if(h > 350) {
            $("#bodyTextId").css({ height: h +'px'});
        }
    }

    function getDocHeight(doc) {
        doc = doc || document;

        var body = doc.body, html = doc.documentElement;
        var height = Math.max( body.scrollHeight, body.offsetHeight, html.clientHeight, html.scrollHeight, html.offsetHeight );

        return height;
    }

</script>

<tr>
    <TD class="label" width="15%">
        <fmt:message key="Mail.from"/>
    </TD>
    <TD class="contain" width="75%">
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td>
                    <app:text property="dto(from)"
                              styleClass="largeText"
                              maxlength="80"
                              view="${true}"/>
                    &nbsp;

                    <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                        <c:choose>
                            <c:when test="${not empty fromContactPersonId}">
                                <app2:checkAccessRight functionality="CONTACTPERSON" permission="VIEW">
                                    <c:set var="imageNamePrefix" value="person"/>
                                    <c:set var="contactDetailLink"
                                           value="/contacts/ContactPerson/Forward/Update.do?dto(addressId)=${fromAddressId}&dto(contactPersonId)=${fromContactPersonId}&contactId=${fromAddressId}&dto(addressType)=${fromAddressType}&tabKey=Contacts.Tab.contactPersons"/>
                                </app2:checkAccessRight>
                            </c:when>
                            <c:when test="${fromAddressType == personType}">
                                <c:set var="imageNamePrefix" value="person-private"/>
                                <c:set var="contactDetailLink"
                                       value="/contacts/Person/Forward/Update.do?contactId=${fromAddressId}&dto(addressId)=${fromAddressId}&dto(addressType)=${fromAddressType}&index=0"/>
                            </c:when>
                            <c:when test="${fromAddressType == organizationType}">
                                <c:set var="imageNamePrefix" value="org"/>
                                <c:set var="contactDetailLink"
                                       value="/contacts/Organization/Forward/Update.do?contactId=${fromAddressId}&dto(addressId)=${fromAddressId}&dto(addressType)=${fromAddressType}&index=0"/>
                            </c:when>
                        </c:choose>

                        <c:if test="${not empty contactDetailLink}">
                            <a href="<c:url value="${contactDetailLink}"/>"
                               title="<fmt:message key="Webmail.contactInformation"/>">
                                <html:img src="${baselayout}/img/${imageNamePrefix}.gif"
                                          titleKey="Webmail.contactInformation" border="0" height="14"/>
                            </a>
                        </c:if>

                    </app2:checkAccessRight>
                </td>
                <TD align="right">
                    <c:if test="${emailPriority == MAIL_PRIORITY_HIGHT}">
                        <img src="${baselayout}/img/webmail/prio_high.gif" border="0" alt=""/>
                    </c:if>
                </TD>
            </tr>
        </table>
    </TD>

</tr>
<tr>
    <TD class="label" width="15%">
        <fmt:message key="Mail.date"/>
    </TD>
    <TD class="contain" width="85%">
        <fmt:message var="dateTimePattern" key="Webmail.mail.dateTimePatternNoTimezone"/>
        <c:set var="formattedDate"
               value="${app2:getLocaleFormattedDateTime(sentDate, timeZone, dateTimePattern, locale)}"/>
        <c:out value="${formattedDate}"/>
    </TD>
</tr>
<TR>
    <TD class="label" width="15%">
        <fmt:message key="Mail.to"/>
    </TD>
    <TD class="contain" width="85%">
        <app:text property="dto(to)"
                  styleClass="largeText"
                  view="${true}"/>
    </TD>
</TR>
<c:if test="${true == showRecipientsCC}">
    <TR>
        <TD class="label" width="15%">
            <fmt:message key="Mail.Cc"/>
        </TD>
        <TD class="contain" width="85%">
            <app:text property="dto(cc)"
                      styleClass="largeText"
                      view="${true}"/>
        </TD>
    </TR>
</c:if>
<TR>
    <TD class="label" width="15%">
        <fmt:message key="Mail.subject"/>
    </TD>
    <TD class="contain" width="85%">
        <app:text property="dto(mailSubject)"
                  styleClass="largeText"
                  view="${true}"/>
    </TD>
</TR>
<tr>
    <td colspan="2">
        <c:if test="${null != attachments}">
            <c:set var="attachments" value="${attachments}" scope="request"/>
        </c:if>
        <c:import url="/common/webmail/AttachmentReadFragment.jsp"/>
    </td>
</tr>
<TR>
    <c:choose>
        <c:when test="${bodyType == BODY_TYPE_HTML}">
            <TD colspan="2" class="webmailHtmlBody">
                <iframe name="frame1"
                        id="bodyIFrameId"
                        src="<c:url value="${htmlBodyPage}" />"
                        style="width:100%;height:100%;background-color:#ffffff" scrolling="auto"
                        frameborder="0">
                </iframe>
            </TD>
        </c:when>
        <c:otherwise>
            <TD colspan="2">
                <html:textarea property="dto(body)"
                               styleId="bodyTextId"
                               styleClass="webmailBody"
                               readonly="true"
                               style="background-color:#ffffff"/>
            </TD>
        </c:otherwise>
    </c:choose>
</TR>
<%--
<tr>
    <td colspan="2">
        <c:set var="enableAddAttachLink" value="false" scope="request"/>
        <c:set var="enableCheckBoxes" value="false" scope="request"/>
        <c:if test="${null != attachments}">
            <c:set var="attachments" value="${attachments}" scope="request"/>
        </c:if>
        <c:import url="/common/webmail/AttachmentFragment.jsp"/>
    </td>
</tr>
--%>

