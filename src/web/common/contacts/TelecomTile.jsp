<%@ include file="/Includes.jsp" %>

<c:if test="${!(empty telecomItems && op == 'delete')}">
    <TR>
        <TD class="title">&nbsp;</TD>
        <TD class="title"><fmt:message key="Contact.telecoms"/></TD>
        <TD colspan="2" class="title" style="padding-left:25;"><fmt:message key="Contact.telecom.description"/></TD>
    </TR>
</c:if>

<c:set var="phone" value="<%= com.piramide.elwis.utils.CommunicationTypes.PHONE %>" scope="page"/>
<c:set var="meeting" value="<%= com.piramide.elwis.utils.CommunicationTypes.MEETING %>" scope="page"/>
<c:set var="fax" value="<%= com.piramide.elwis.utils.CommunicationTypes.FAX %>" scope="page"/>
<c:set var="letter" value="<%= com.piramide.elwis.utils.CommunicationTypes.LETTER %>" scope="page"/>
<c:set var="other" value="<%= com.piramide.elwis.utils.CommunicationTypes.OTHER %>" scope="page"/>
<c:set var="email" value="<%= com.piramide.elwis.utils.CommunicationTypes.EMAIL %>" scope="page"/>

<c:forEach var="item" items="${telecomItems}">
    <c:if test="${item.value.size > 0}">
        <TR>
            <TD class="topLabel" rowspan="${item.value.size + 1}">
                <c:out value="${item.value.telecomTypeName}"/>
                <html:hidden property="telecom(${item.key}).telecomTypeId"/>
                <html:hidden property="telecom(${item.key}).telecomTypeName"/>
                <html:hidden property="telecom(${item.key}).telecomTypeType"/>
                <html:hidden property="telecom(${item.key}).telecomTypePosition"/>
            </TD>
        </TR>
        <c:forEach var="telecom" items="${item.value.telecoms}" varStatus="i">
            <TR>
                <TD class="containTop" colspan="3">
                    <html:hidden property="telecom(${item.key}).telecom[${i.index}].telecomId"/>
                    <app:text property="telecom(${item.key}).telecom[${i.index}].data" titleKey="Telecom.value"
                              styleClass="largeText" maxlength="100" view="${op == 'delete'}"
                              tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                              styleId="${item.value.telecomTypeId}${i.index}Id"/>
                    &nbsp;
                    <app:text property="telecom(${item.key}).telecom[${i.index}].description"
                              titleKey="Telecom.description" styleClass="middleText" maxlength="200"
                              view="${op == 'delete'}" tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"/>
                    <c:if test="${op != 'delete'}">
                        <input type="radio" name="telecom(${item.key}).predeterminedIndex" value="${i.index}"
                               class="radio"
                               title="<fmt:message key="Common.predetermined"/>"  ${telecom.predetermined ? 'checked' : ''}
                               tabIndex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                        <html:submit property="dto(removeTelecom${item.key}@${telecom.telecomId}#${i.index})" value=" "
                                     styleClass="minusButton" titleKey="Contact.removeTelecom"
                                     disabled="${op == 'delete'}"
                                     tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                     onclick="javascript:fillMultipleSelectValues();"/>
                        <c:if test="${'LINK' == item.value.telecomTypeType}">
                            <a href="javascript:openURL('${item.value.telecomTypeId}${i.index}Id')"
                               tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                <img src="<c:url value="/layout/ui/img/link.gif"/>"
                                     alt="<fmt:message   key="Common.openLink"/>"
                                     title="<fmt:message   key="Common.openLink"/>" border="0" align="middle"/>
                            </a>
                        </c:if>
                    </c:if>
                    <c:if test="${op == 'update' && telecom.telecomId != ''}">
                        <c:choose>
                            <c:when test="${isType == 'contact'}">
                                <c:set var="action"
                                       value="/MainCommunication/Forward/Create.do?index=2&tabKey=Contacts.Tab.communications&dto(type)="/>
                                <c:set var="action2"
                                       value="/MainCommunication/Forward/Create2.do?index=2&tabKey=Contacts.Tab.communications&dto(type)="/>

                                <app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
                                    <c:choose>
                                        <c:when test="${'PHONE' == item.value.telecomTypeType}">
                                            <app:link
                                                    action="${action}${phone}&dto(addressId)=${param.contactId}&dto(contactNumber)=${app2:encode(telecom.data)}"
                                                    tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                <img src="${sessionScope.baselayout}/img/notes.gif"
                                                     alt="<fmt:message   key="Document.newTracking"/>"
                                                     title="<fmt:message   key="Document.newTracking"/>" border="0"
                                                     align="middle"/>
                                            </app:link>
                                            <a href="javascript:callto('${item.value.telecomTypeId}${i.index}Id', 'callto')"
                                               tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                <img src="${sessionScope.baselayout}/img/phone2.gif"
                                                     alt="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                                     title="<fmt:message   key="Contact.newVOIP.phoneCall"/>" border="0"
                                                     align="middle"/>
                                            </a>
                                            <a href="javascript:callto('${item.value.telecomTypeId}${i.index}Id', 'tel')"
                                               tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                <img src="<c:url value="/layout/ui/img/mobile.gif"/>"
                                                     alt="<fmt:message   key="Contact.telecom.mobileCall"/>"
                                                     title="<fmt:message   key="Contact.telecom.mobileCall"/>"
                                                     border="0" align="middle"/>
                                            </a>
                                        </c:when>
                                        <c:when test="${'FAX' == item.value.telecomTypeType}">
                                            <app:link action="${action}${fax}"
                                                      tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                <img src="${sessionScope.baselayout}/img/notes.gif"
                                                     alt="<fmt:message   key="Document.newTracking"/>"
                                                     title="<fmt:message   key="Document.newTracking"/>" border="0"
                                                     align="middle"/>
                                            </app:link>
                                        </c:when>
                                        <c:when test="${'EMAIL' == item.value.telecomTypeType}">
                                            <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                                                <app:link
                                                        action="${action2}${email}&dto(addressId)=${param.contactId}&dto(selectedEmail)=${app2:encode(telecom.data)}"
                                                        tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                    <img src="${sessionScope.baselayout}/img/arrobaB.gif"
                                                         alt="<fmt:message   key="Document.newMail"/>"
                                                         title="<fmt:message   key="Document.newMail"/>" border="0"
                                                         align="middle"/>
                                                </app:link>
                                            </app2:checkAccessRight>
                                        </c:when>
                                        <c:when test="${'OTHER' == item.value.telecomTypeType}">
                                            <app:link action="${action}${other}"
                                                      tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                <img src="${sessionScope.baselayout}/img/notes.gif"
                                                     alt="<fmt:message   key="Document.newTracking"/>"
                                                     title="<fmt:message   key="Document.newTracking"/>" border="0"
                                                     align="middle"/>
                                            </app:link>
                                        </c:when>
                                    </c:choose>
                                </app2:checkAccessRight>
                            </c:when>
                            <c:otherwise>
                                <c:set var="action"
                                       value="/MainCommunication/Forward/Create.do?index=2&tabKey=Contacts.Tab.communications&dto(contactPersonId)=${contactPersonId}&dto(contactPersonName)=${app2:encode(contactPersonName)}&dto(type)="/>
                                <c:set var="action2"
                                       value="/MainCommunication/Forward/Create2.do?index=2&tabKey=Contacts.Tab.communications&dto(contactPersonId)=${contactPersonId}&dto(contactPersonName)=${app2:encode(contactPersonName)}&dto(type)="/>
                                <app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
                                    <c:choose>
                                        <c:when test="${'PHONE' == item.value.telecomTypeType}">
                                            <app:link
                                                    action="${action}${phone}&dto(addressId)=${param.contactId}&dto(contactNumber)=${app2:encode(telecom.data)}"
                                                    tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                <img src="${sessionScope.baselayout}/img/notes.gif"
                                                     alt="<fmt:message   key="Document.newTracking"/>"
                                                     title="<fmt:message   key="Document.newTracking"/>" border="0"
                                                     align="middle"/>
                                            </app:link>
                                            <a href="javascript:callto('${item.value.telecomTypeId}${i.index}Id', 'callto')"
                                               tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                <img src="${sessionScope.baselayout}/img/phone2.gif"
                                                     alt="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                                     title="<fmt:message   key="Contact.newVOIP.phoneCall"/>" border="0"
                                                     align="middle"/>
                                            </a>
                                            <a href="javascript:callto('${item.value.telecomTypeId}${i.index}Id', 'tel')"
                                               tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                <img src="<c:url value="/layout/ui/img/mobile.gif"/>"
                                                     alt="<fmt:message   key="Contact.telecom.mobileCall"/>"
                                                     title="<fmt:message   key="Contact.telecom.mobileCall"/>"
                                                     border="0" align="middle"/>
                                            </a>
                                        </c:when>
                                        <c:when test="${'FAX' == item.value.telecomTypeType}">
                                            <app:link action="${action}${fax}"
                                                      tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                <img src="${sessionScope.baselayout}/img/notes.gif"
                                                     alt="<fmt:message   key="Document.newTracking"/>"
                                                     title="<fmt:message   key="Document.newTracking"/>" border="0"
                                                     align="middle"/>
                                            </app:link>
                                        </c:when>
                                        <c:when test="${'EMAIL' == item.value.telecomTypeType}">
                                            <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                                                <app:link
                                                        action="${action2}${email}&dto(addressId)=${param.contactId}&dto(selectedEmail)=${app2:encode(telecom.data)}"
                                                        tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                    <img src="${sessionScope.baselayout}/img/arrobaB.gif"
                                                         alt="<fmt:message   key="Document.newMail"/>"
                                                         title="<fmt:message   key="Document.newMail"/>" border="0"
                                                         align="middle"/>
                                                </app:link>
                                            </app2:checkAccessRight>
                                        </c:when>
                                        <c:when test="${'OTHER' == item.value.telecomTypeType}">
                                            <app:link action="${action}${other}"
                                                      tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                <img src="${sessionScope.baselayout}/img/notes.gif"
                                                     alt="<fmt:message   key="Document.newTracking"/>"
                                                     title="<fmt:message   key="Document.newTracking"/>" border="0"
                                                     align="middle"/>
                                            </app:link>
                                        </c:when>
                                    </c:choose>
                                </app2:checkAccessRight>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </TD>
            </TR>
        </c:forEach>
    </c:if>
</c:forEach>
<c:if test="${op != 'delete'}">
    <TR>
        <TD class="label">
            <c:set var="telecomTypes" value="${app2:getTelecomTypes(pageContext.request)}"/>
            <html:select property="dto(telecomTypeId)" styleClass="shortSelect" readonly="${op == 'delete'}"
                         tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                <html:option value="">&nbsp;</html:option>
                <html:options collection="telecomTypes" property="value" labelProperty="label"/>
            </html:select>
        </TD>
        <TD class="contain" colspan="3">
            <html:text property="dto(telecomValue)" titleKey="Telecom.value" styleClass="largeText" maxlength="100"
                       readonly="${op == 'delete'}" tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"/>
            &nbsp;
            <html:text property="dto(telecomDescription)" titleKey="Telecom.description" styleClass="middleText"
                       maxlength="200" readonly="${op == 'delete'}"
                       tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"/>
            <html:submit property="dto(addTelecom)" value=" " styleClass="plusButton" titleKey="Contact.addTecom"
                         disabled="${op == 'delete'}" tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                         onclick="javascript:fillMultipleSelectValues();"/>
        </TD>
    </TR>
</c:if>