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
    $(function () {
        $('#bodyIFrameId').load(function () {
            resizeBodyIframe();
            resizeWebmailHtmlBody();
        });
    });
    $("#webmailHtmlBody").resize(function(){
        resizeWebmailHtmlBody();
    });
    function resizeWebmailHtmlBody(){
        var heigthBodyIFrameId=0;
        heigthBodyIFrameId=$('#bodyIFrameId').contents().find('body').height();
        $("#webmailHtmlBody").css({height: heigthBodyIFrameId+50+ "px"});
    }
    $(window).load(function () {
        <c:if test="${bodyType == BODY_TYPE_TEXT}">
        resizeBodyText();
        </c:if>
    });

    function resizeBodyIframe() {
        var ifrm = document.getElementById("bodyIFrameId");
        var doc = ifrm.contentDocument ? ifrm.contentDocument : ifrm.contentWindow.document;
        var h = getDocHeight(doc);

        h = h + 20;
        if (h > 350) {
            $("#bodyIFrameId").css({height: h + 'px'});
        }
    }

    function resizeBodyText() {
        var h = $("#bodyTextId").prop('scrollHeight');
        h = h + 10;
        if (h > 350) {
            $("#bodyTextId").css({height: h + 'px'});
        }
    }

    function getDocHeight(doc) {
        doc = doc || document;

        var body = doc.body, html = doc.documentElement;
        var height = Math.max(body.scrollHeight, body.offsetHeight, html.clientHeight, html.scrollHeight, html.offsetHeight);

        return height;
    }

</script>
<c:choose>
    <c:when test="${not empty modeLabel}">
        <c:set var="modeLabelReadEmail" value="${app2:getFormLabelClasses()}" scope="request"/>
        <c:set var="modeContainReadEmail" value="${app2:getFormContainClasses(true)}" scope="request"/>
        <c:set var="divEnvolve" value="form-group"/>
    </c:when>
    <c:otherwise>
        <c:set var="modeLabelReadEmail" value="control-label col-sm-2 label-left" scope="request"/>
        <c:set var="modeContainReadEmail" value="col-sm-9 form-control-static" scope="request"/>
        <c:set var="divEnvolve" value="row col-xs-12"/>
    </c:otherwise>
</c:choose>
<fieldset>
    <div class="${divEnvolve}">
        <label class="${modeLabelReadEmail}">
            <fmt:message key="Mail.from"/>
        </label>

        <div class="${modeContainReadEmail}">
            <div class="row col-xs-11">
                <app:text property="dto(from)"
                          styleClass="largeText ${app2:getFormInputClasses()}"
                          maxlength="80"
                          view="${true}"/>
                <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                    <c:choose>
                        <c:when test="${not empty fromContactPersonId}">
                            <app2:checkAccessRight functionality="CONTACTPERSON" permission="VIEW">
                                <c:set var="imageNamePrefix" value="${app2:getClassGlyphPerson()}"/>
                                <c:set var="contactDetailLink"
                                       value="/contacts/ContactPerson/Forward/Update.do?dto(addressId)=${fromAddressId}&dto(contactPersonId)=${fromContactPersonId}&contactId=${fromAddressId}&dto(addressType)=${fromAddressType}&tabKey=Contacts.Tab.contactPersons"/>
                            </app2:checkAccessRight>
                        </c:when>
                        <c:when test="${fromAddressType == personType}">
                            <c:set var="imageNamePrefix" value="${app2:getClassGlyphPrivatePerson()}"/>
                            <c:set var="contactDetailLink"
                                   value="/contacts/Person/Forward/Update.do?contactId=${fromAddressId}&dto(addressId)=${fromAddressId}&dto(addressType)=${fromAddressType}&index=0"/>
                        </c:when>
                        <c:when test="${fromAddressType == organizationType}">
                            <c:set var="imageNamePrefix" value="${app2:getClassGlyphOrganization()}"/>
                            <c:set var="contactDetailLink"
                                   value="/contacts/Organization/Forward/Update.do?contactId=${fromAddressId}&dto(addressId)=${fromAddressId}&dto(addressType)=${fromAddressType}&index=0"/>
                        </c:when>
                    </c:choose>

                    <c:if test="${not empty contactDetailLink}">
                        <a href="<c:url value="${contactDetailLink}"/>"
                           title="<fmt:message key="Webmail.contactInformation"/>">
                            <span class="${imageNamePrefix}" title="<fmt:message key="Webmail.contactInformation"/>"></span>
                        </a>
                    </c:if>

                </app2:checkAccessRight>
            </div>
            <span class="pull-right">
                <c:if test="${emailPriority == MAIL_PRIORITY_HIGHT}">
                    <img src="${baselayout}/img/webmail/prio_high.gif" border="0" alt=""/>
                </c:if>
            </span>
        </div>
    </div>
    <div class="${divEnvolve}">
        <label class="${modeLabelReadEmail}">
            <fmt:message key="Mail.date"/>
        </label>

        <div class="${modeContainReadEmail}">
            <fmt:message var="dateTimePattern" key="Webmail.mail.dateTimePatternNoTimezone"/>
            <c:set var="formattedDate"
                   value="${app2:getLocaleFormattedDateTime(sentDate, timeZone, dateTimePattern, locale)}"/>
            <c:out value="${formattedDate}"/>
        </div>
    </div>
    <div class="${divEnvolve}">
        <label class="${modeLabelReadEmail}">
            <fmt:message key="Mail.to"/>
        </label>

        <div class="${modeContainReadEmail}">
            <app:text property="dto(to)"
                      styleClass="largeText ${app2:getFormInputClasses()}"
                      view="${true}"/>
        </div>
    </div>
    <c:if test="${true == showRecipientsCC}">
        <div class="${divEnvolve}">
            <label class="${modeLabelReadEmail}">
                <fmt:message key="Mail.Cc"/>
            </label>

            <div class="${modeContainReadEmail}">
                <app:text property="dto(cc)"
                          styleClass="largeText ${app2:getFormInputClasses()}"
                          view="${true}"/>
            </div>
        </div>
    </c:if>
    <div class="${divEnvolve}">
        <label class="${modeLabelReadEmail}">
            <fmt:message key="Mail.subject"/>
        </label>

        <div class="${modeContainReadEmail}">
            <app:text property="dto(mailSubject)"
                      styleClass="largeText ${app2:getFormInputClasses()}"
                      view="${true}"/>
        </div>
    </div>
    <c:if test="${null != attachments}">
        <c:set var="attachments" value="${attachments}" scope="request"/>
    </c:if>
    <c:import url="/WEB-INF/jsp/webmail/AttachmentReadFragment.jsp"/>
    <c:choose>
        <c:when test="${bodyType == BODY_TYPE_HTML}">
            <div class="embed-responsive embed-responsive-16by9 col-xs-12" id="webmailHtmlBody" style="min-height: 350px">
                <iframe class="embed-responsive-item" name="frame1"
                        id="bodyIFrameId"
                        src="<c:url value="${htmlBodyPage}" />"
                        style="width:100%;height:100%;background-color:#ffffff" scrolling="auto"
                        frameborder="0">
                </iframe>
            </div>
        </c:when>
        <c:otherwise>
            <div class="form-group">
                <div class="col-xs-12">
                    <html:textarea property="dto(body)"
                                   styleId="bodyTextId"
                                   styleClass="webmailBody ${app2:getFormInputClasses()}"
                                   readonly="true"
                                   style="background-color:#ffffff;height: 350px;"/>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</fieldset>

