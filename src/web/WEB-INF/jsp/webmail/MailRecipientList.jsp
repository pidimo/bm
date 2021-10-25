<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>

<c:import url="/WEB-INF/jsp/webmail/ReturnToMailTrayUrlFragment.jsp"/>

<c:set var="mailFilterURLFragment" value="&mailFilter=${mailFilter}"/>
<c:set var="mailSearchURLFragment" value="&mailSearch=${mailSearch}"/>
<c:set var="searchTextURLFragment" value="&searchText=${searchText}"/>
<c:set var="searchFilterURLFragment" value="&searchFilter=${searchFilter}"/>
<c:set var="searchFolderURLFragment" value="&searchFolder=${searchFolder}"/>
<c:set var="mailAdvancedSearchURLFragment" value="&mailAdvancedSearch=${mailAdvancedSearch}"/>
<c:set var="folderIdURLFragment" value="&folderId=${folderView}"/>

<c:set var="navigationURLParams"
       value="${mailFilterURLFragment}${mailSearchURLFragment}${not empty(searchText)?searchTextURLFragment:''}${not empty(searchFilter)?searchFilterURLFragment:''}${not empty(searchFolder)?searchFolderURLFragment:''}${mailAdvancedSearchURLFragment}${(mailSearch=='false' && mailAdvancedSearch=='false')?folderIdURLFragment:''}&returning=true"/>


<c:set var="MAIL_IN" value="<%=String.valueOf(WebMailConstants.IN_VALUE)%>"/>
<c:set var="MAIL_OUT" value="<%=String.valueOf(WebMailConstants.OUT_VALUE)%>"/>
<fmt:message key="Webmail.updateCommunications" var="communicationMessage"/>

<tags:initBootstrapSelectPopup/>

<tags:jscript language="JavaScript" src="/js/webmail/compose.jsp"/>

<html:form action="/Mail/CreateWebCommunications.do" styleId="listMailForm" styleClass="form-horizontal">

    <c:if test="${dto.mailId != null}">
        <c:set var="mailId" value="${dto.mailId}"/>
    </c:if>

    <html:hidden property="dto(mailId)" value="${mailId}"/>

    <c:if test="${dto.saveSendItem != null}">
        <c:set var="saveSendItem" value="${dto.saveSendItem}"/>
    </c:if>

    <c:if test="${dto.incomingOutgoing == MAIL_IN}">
        <c:set var="inOut" value="1"/>
    </c:if>
    <c:if test="${dto.incomingOutgoing == MAIL_OUT}">
        <c:set var="inOut" value="0"/>
    </c:if>


    <html:hidden property="dto(saveSendItem)" value="${saveSendItem}"/>

    <fmt:message var="dateTimePattern" key="Webmail.mail.dateTimePatternNoTimezone"/>
    <c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
    <c:set var="locale" value="${sessionScope.user.valueMap['locale']}"/>
    <c:set var="newDate" value="${app2:getLocaleFormattedDateTime(dto.dateTime, timeZone, dateTimePattern,locale)}"/>
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="Webmail.mailCommunications"/>
            </legend>

            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Webmail.tray.date"/>
                </label>

                <div class="${app2:getFormContainClasses(true)}">
                        ${newDate}
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Mail.subject"/>
                </label>

                <div class="${app2:getFormContainClasses(true)}">
                        ${dto.subject}
                </div>
            </div>
        </fieldset>

        <c:if test="${dto.incomingOutgoing == MAIL_IN}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Mail.from"/>
                </legend>
            </fieldset>
            <div>
                <div class="table-responsive">
                    <table class="${app2:getFantabulousTableClases()}">

                        <th class='listHeader' width='5%'><fmt:message key="Common.action"/></th>

                        <th class='listHeader' width='45%'><fmt:message key="Mail.from"/></th>

                        <th class='listHeader' width='50%'><fmt:message key="Contact.name"/></th>


                        <c:set var="fromList" value="${dto.mailListRecived}"/>
                        <c:forEach var="from" items="${fromList}">
                            <tr class="listRow">
                                <td class='listItem' align="center">&nbsp;
                                    <c:choose>
                                        <c:when test="${from.haveCommunication == false && saveSendItem == true  && dto.incomingOutgoing == MAIL_IN}">
                                            <html:link
                                                    action="/Mail/Forward/EmailCommunication.do?dto(mailId)=${mailId}&dto(email)=${from.dirEmail}&dto(inOut)=${inOut}">
                                                <html:img src="${baselayout}/img/webmail/emailcommbroke.gif" border="0"
                                                          title="${communicationMessage}"/>
                                            </html:link>
                                        </c:when>

                                        <c:when test="${from.haveCommunication == true && saveSendItem == true  && dto.incomingOutgoing == MAIL_IN}">
                                            <html:link
                                                    action="/Mail/Forward/EmailCommunication.do?dto(mailId)=${mailId}&dto(email)=${from.dirEmail}&dto(inOut)=${inOut}">
                                                <html:img src="${baselayout}/img/webmail/emailcomm.gif" border="0"
                                                          title="${communicationMessage}"/>
                                            </html:link>
                                        </c:when>

                                        <c:when test="${from.isContact == false && saveSendItem == true  && dto.incomingOutgoing == MAIL_IN}">
                                            <html:link
                                                    action="/Mail/Forward/EmailCommunication.do?dto(mailId)=${mailId}&dto(email)=${from.dirEmail}&dto(inOut)=${inOut}">
                                                <html:img src="${baselayout}/img/webmail/emailcommbroke.gif" border="0"
                                                          title="${communicationMessage}"/>
                                            </html:link>
                                        </c:when>

                                        <c:otherwise>
                                            &nbsp;
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class='listItem'><c:out value="${from.dirEmail}"/></td>
                                <td class='listItem2'>&nbsp;<c:out value="${from.addressName}"/></td>

                            </tr>
                        </c:forEach>

                    </table>
                </div>
            </div>
        </c:if>
        <c:if test="${dto.incomingOutgoing == MAIL_OUT}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Mail.to.plural"/>
                </legend>
            </fieldset>
            <div>
                <div class="table-responsive">
                    <table class="${app2:getFantabulousTableClases()}">

                        <th class='listHeader' width='5%'><fmt:message key="Common.action"/></th>
                        <th class='listHeader' width='45%'><fmt:message key="Mail.to"/></th>
                        <th class='listHeader' width='50%'><fmt:message key="Contact.name"/></th>

                        <c:set var="toList" value="${dto.mailListDispatched}"/>
                        <c:forEach var="mailsSend" items="${toList}">
                            <tr class="listRow">
                                <td class='listItem' align="center">&nbsp;
                                    <c:choose>
                                        <c:when test="${mailsSend.haveCommunication == false && saveSendItem == true && dto.incomingOutgoing == MAIL_OUT}">
                                            <html:link
                                                    action="/Mail/Forward/EmailCommunication.do?dto(mailId)=${mailId}&dto(email)=${mailsSend.dirEmail}&dto(inOut)=${inOut}">
                                                <html:img src="${baselayout}/img/webmail/emailcommbroke.gif" border="0"
                                                          title="${communicationMessage}"/>
                                            </html:link>
                                        </c:when>

                                        <c:when test="${mailsSend.haveCommunication == true && saveSendItem == true  && dto.incomingOutgoing == MAIL_OUT}">
                                            <html:link
                                                    action="/Mail/Forward/EmailCommunication.do?dto(mailId)=${mailId}&dto(email)=${mailsSend.dirEmail}&dto(inOut)=${inOut}">
                                                <html:img src="${baselayout}/img/webmail/emailcomm.gif" border="0"
                                                          title="${communicationMessage}"/>
                                            </html:link>
                                        </c:when>

                                        <c:when test="${mailsSend.isContact == false && saveSendItem == true  && dto.incomingOutgoing == MAIL_OUT}">
                                            <html:link
                                                    action="/Mail/Forward/EmailCommunication.do?dto(mailId)=${mailId}&dto(email)=${mailsSend.dirEmail}&dto(inOut)=${inOut}">
                                                <html:img src="${baselayout}/img/webmail/emailcommbroke.gif" border="0"
                                                          title="${communicationMessage}"/>
                                            </html:link>
                                        </c:when>
                                        <c:otherwise>
                                            &nbsp;
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class='listItem'><c:out value="${mailsSend.dirEmail}"/></td>
                                <td class='listItem2'>&nbsp;<c:out value="${mailsSend.addressName}"/></td>
                            </tr>
                        </c:forEach>

                    </table>
                </div>
            </div>
        </c:if>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:button property="" styleClass="${app2:getFormButtonClasses()}" onclick="location.href='${urlCancel}'">
            <fmt:message key="Common.cancel"/>
        </html:button>
            <%--<html:cancel styleClass="button" ><fmt:message   key="Common.cancel"/></html:cancel>--%>
    </div>

</html:form>