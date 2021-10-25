<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>

<c:import url="/WEB-INF/jsp/webmail/ReturnToMailTrayUrlFragment.jsp"/>

<script>
    function check() {
        field = document.getElementById('listMailForm').selectedMails;
        guia = document.getElementById('listMailForm').mail;
        var i;
        if (null != field) {
            if (guia.checked) {
                for (i = 0; i < field.length; i++)
                    field[i].checked = true;
            }
            else {
                for (i = 0; i < field.length; i++)
                    field[i].checked = false;
            }
        }
    }


</script>

<tags:initBootstrapSelectPopup/>
<tags:jscript language="JavaScript" src="/js/webmail/compose.jsp"/>

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

    <fieldset>
        <legend colspan="${colSpan}" class="title">
            <c:out value="${title}"/>
        </legend>
    </fieldset>
    <html:hidden property="dto(saveSendItem)" value="${saveSendItem}"/>
    <div class="table-responsive">
        <table id="Send.jsp" class="${app2:getFantabulousTableClases()}">
            <th class='listHeader'>
                <div class="checkbox checkbox-default listItemCheckbox">
                    <input type=checkbox name="mail" id="mail" value="1" onclick="javascript:check();" tabindex="1">
                    <label for="mail"></label>
                </div>
            </th>

            <app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
                <c:if test="${true == saveSendItem}">
                    <th class='listHeader' width='5%'>
                        <fmt:message key="Common.action"/>
                    </th>
                </c:if>
            </app2:checkAccessRight>


            <th class='listHeader'>
                <fmt:message key="Common.email"/>
            </th>

            <th class='listHeader'>
                <fmt:message key="Contact.name"/>
            </th>

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
                <tr class="listRow">
                    <html:hidden property="addresses"
                                 value="${mailsSend.dirEmail}<,>${mailsSend.addressName}<,>${mailsSend.isContact}"
                                 styleId="addresses_${mailsSend.dirEmail}_${mailsSend.addressName}_${mailsSend.isContact}_Id"/>

                    <td class='listItemCenter' align="center">
                        <c:choose>
                            <c:when test="${mailsSend.isContact == false}">
                                <div class="checkbox checkbox-default listItemCheckbox">
                                    <input type="checkbox" name="selectedMails" value="${mailsSend.dirEmail}"
                                           id="selectedMails" align="center" tabindex="1">
                                    <label for="selectedMails"></label>
                                </div>
                            </c:when>
                            <c:when test="${mailsSend.isContact == true}">
                                <span class="${app2:getClassGlyphOk()}" title="<fmt:message key="Webmail.isContact"/>"></span>
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
                                            <html:img src="${baselayout}/img/webmail/emailcommbroke.gif" border="0"
                                                      titleKey="Webmail.communication.relateCommunication"/>
                                        </html:link>
                                    </c:when>
                                    <c:when test="${mailsSend.haveCommunication == true}">
                                        <html:link
                                                action="/Mail/Forward/EmailCommunicationAfterSend.do?dto(mailId)=${mailId}&dto(email)=${mailsSend.dirEmail}&dto(inOut)=${MAIL_OUT}">
                                            <html:img src="${baselayout}/img/webmail/emailcomm.gif" border="0"
                                                      titleKey="Webmail.communication.relateCommunication"/>
                                        </html:link>
                                    </c:when>
                                    <c:when test="${mailsSend.isContact == false}">
                                        <html:link
                                                action="/Mail/Forward/EmailCommunicationAfterSend.do?dto(mailId)=${mailId}&dto(email)=${mailsSend.dirEmail}&dto(inOut)=${MAIL_OUT}">
                                            <html:img src="${baselayout}/img/webmail/emailcommbroke.gif" border="0"
                                                      titleKey="Webmail.communication.relateCommunication"/>
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
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit styleId="buttonId" operation="create" functionality="CONTACT"
                             styleClass="${app2:getFormButtonClasses()}"
                             tabindex="1">
            <fmt:message key="Webmail.mail.addAtContact"/>
        </app2:securitySubmit>
        <html:button property="" styleClass="${app2:getFormButtonCancelClasses()}"
                     onclick="location.href='${urlCancel}'"
                     tabindex="1">
            <fmt:message key="Common.cancel"/>
        </html:button>
    </div>

</html:form>

<script>
    var checkboxs = document.getElementById('listMailForm').selectedMails;

    if (checkboxs == null) {
        document.getElementById('buttonId').style.display = "none";
    }
</script>
