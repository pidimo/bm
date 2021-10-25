<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>
<c:import url="/common/webmail/ReturnToMailTrayUrlFragment.jsp"/>
<script>
    function check()
    {
        field = document.getElementById('listMailForm').selectedMails;
        guia = document.getElementById('listMailForm').mail;
        var i;
        if (null != field) {
            if (guia.checked)
            {
                for (i = 0; i < field.length; i++)
                    field[i].checked = true;
            }
            else
            {
                for (i = 0; i < field.length; i++)
                    field[i].checked = false;
            }
        }
    }


</script>
<tags:initSelectPopup/>
<tags:jscript language="JavaScript" src="/js/webmail/compose.jsp"/>
<br/>

<c:set var="MAIL_OUT" value="<%=String.valueOf(WebMailConstants.OUT_VALUE)%>"/>
<jsp:useBean id="functionalitiesToCheck" class="java.util.LinkedHashMap" scope="page"/>
<c:set target="${functionalitiesToCheck}" property="COMMUNICATION" value="CREATE"/>


<html:form action="${action}" styleId="listMailForm" focus="mail">

<c:set var="colSpan" value="3"/>
<c:if test="${app2:checkSomeAccessRights(pageContext.request, functionalitiesToCheck)}">
    <c:if test="${true == dto.saveSendItem}">
        <c:set var="saveSendItem" value="${dto.saveSendItem}"/>
        <c:set var="colSpan" value="4"/>
    </c:if>
</c:if>


<html:hidden property="dto(saveSendItem)" value="${saveSendItem}"/>

<table id="Send.jsp" border="0" cellpadding="0" cellspacing="0" width="95%" align="center" class="container">
    <tr>
        <td colspan="${colSpan}" class="title">
            <c:out value="${title}"/>
        </td>
    </tr>

    <th class='listHeader'>
        <input type=checkbox name="mail" value="1" onclick="javascript:check();" class="radio" tabindex="1">
    </th>

    <app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
        <c:if test="${true == saveSendItem}">
            <TH class='listHeader' width='5%'>
                <fmt:message key="Common.action"/>
            </TH>
        </c:if>
    </app2:checkAccessRight>


    <TH class='listHeader' width='45%'><fmt:message key="Common.email"/></TH>

    <TH class='listHeader' width='50%'><fmt:message key="Contact.name"/></TH>

    <c:choose>
        <c:when test="${dto.mailListDispatched != null}">
            <c:set var="list" value="${dto.mailListDispatched}"/>
        </c:when>
        <c:otherwise>
            <c:set var="list" value="${mailListDispatched}"/>
        </c:otherwise>
    </c:choose>

    <c:if test="${dto.mailId != null}">
        <c:set var="mailId" value="${dto.mailId}"/>
    </c:if>
    <html:hidden property="dto(mailId)" value="${mailId}"/>


    <c:forEach var="mailsSend" items="${list}">
        <tr class="listRow" height="20px">
            <html:hidden property="addresses"
                         value="${mailsSend.dirEmail}<,>${mailsSend.addressName}<,>${mailsSend.isContact}"
                         styleId="addresses_${mailsSend.dirEmail}_${mailsSend.addressName}_${mailsSend.isContact}_Id"/>

            <td class='listItemCenter' align="center">
                <c:choose>
                    <c:when test="${mailsSend.isContact == false}">
                        <input type="checkbox" name="selectedMails" value="${mailsSend.dirEmail}" class="radio"
                               id="selectedMails" align="center" tabindex="1">
                    </c:when>
                    <c:when test="${mailsSend.isContact == true}">
                        <img src='<c:out value="${sessionScope.baselayout}"/>/img/check.gif' title="<fmt:message key="Webmail.isContact"/>" >
                    </c:when>
                </c:choose>
            </td>


            <app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
                <c:if test="${true == saveSendItem}">
                    <td class='listItemCenter' align="center">&nbsp;
                        <c:choose>
                            <c:when test="${mailsSend.haveCommunication == false}">
                                <html:link
                                        action="/Mail/Forward/EmailCommunicationAfterSend.do?dto(mailId)=${mailsSend.mailId}&dto(email)=${mailsSend.dirEmail}&dto(inOut)=${MAIL_OUT}">
                                    <html:img src="${baselayout}/img/webmail/emailcommbroke.gif" border="0" titleKey="Webmail.communication.relateCommunication"/>
                                </html:link>
                            </c:when>
                            <c:when test="${mailsSend.haveCommunication == true}">
                                <html:link
                                        action="/Mail/Forward/EmailCommunicationAfterSend.do?dto(mailId)=${mailId}&dto(email)=${mailsSend.dirEmail}&dto(inOut)=${MAIL_OUT}">
                                    <html:img src="${baselayout}/img/webmail/emailcomm.gif" border="0" titleKey="Webmail.communication.relateCommunication"/>
                                </html:link>
                            </c:when>
                            <c:when test="${mailsSend.isContact == false}">
                                <html:link
                                        action="/Mail/Forward/EmailCommunicationAfterSend.do?dto(mailId)=${mailId}&dto(email)=${mailsSend.dirEmail}&dto(inOut)=${MAIL_OUT}">
                                    <html:img src="${baselayout}/img/webmail/emailcommbroke.gif" border="0" titleKey="Webmail.communication.relateCommunication"/>
                                </html:link>
                            </c:when>
                            <c:otherwise>
                                &nbsp;
                            </c:otherwise>
                        </c:choose>

                    </td>
                </c:if>
            </app2:checkAccessRight>


            <td class='listItem'>
                <c:out value="${mailsSend.dirEmail}"/>
            </td>
            <td class='listItem'>&nbsp;
                <c:out value="${mailsSend.addressName}"/>
            </td>

        </tr>
    </c:forEach>

</table>
<table cellSpacing=0 cellPadding=4 width="95%" border=0 align="center">
    <tr>
        <td class="button">
            <app2:securitySubmit styleId="buttonId" operation="create" functionality="CONTACT" styleClass="button"
                                 tabindex="1"><fmt:message key="Webmail.mail.addAtContact"/></app2:securitySubmit>
            <html:button property="" styleClass="button" onclick="location.href='${urlCancel}'" tabindex="1">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </td>
    </tr>
</table>

</html:form>

<script>
    var checkboxs = document.getElementById('listMailForm').selectedMails;;
    if (checkboxs == null) {
        document.getElementById('buttonId').style.display = "none";
    }
</script>
