<%@ include file="/Includes.jsp" %>
<fieldset>
    <legend class="title">
        <fmt:message key="Contact.telecoms"/>
    </legend>
</fieldset>
<div class="table-responsive">
    <div>
        <table class="${app2:getTableClasesIntoForm()} mobileTableMedium">
            <c:if test="${!(empty telecomItems && op == 'delete')}">
                <tr>
                    <th></th>
                    <th>
                        <fmt:message key="Contact.telecoms"/>
                    </th>
                    <th>
                        <fmt:message key="Contact.telecom.description"/>
                    </th>
                    <th colspan="2"></th>
                </tr>
            </c:if>

            <c:set var="phone" value="<%= com.piramide.elwis.utils.CommunicationTypes.PHONE %>" scope="page"/>
            <c:set var="meeting" value="<%= com.piramide.elwis.utils.CommunicationTypes.MEETING %>" scope="page"/>
            <c:set var="fax" value="<%= com.piramide.elwis.utils.CommunicationTypes.FAX %>" scope="page"/>
            <c:set var="letter" value="<%= com.piramide.elwis.utils.CommunicationTypes.LETTER %>" scope="page"/>
            <c:set var="other" value="<%= com.piramide.elwis.utils.CommunicationTypes.OTHER %>" scope="page"/>
            <c:set var="email" value="<%= com.piramide.elwis.utils.CommunicationTypes.EMAIL %>" scope="page"/>

            <c:forEach var="item" items="${telecomItems}">
                <c:if test="${item.value.size > 0}">
                    <tr>
                        <td rowspan="${item.value.size + 1}" class="control-label" style="vertical-align: top; padding-top: 13px !important;">
                            <c:out value="${item.value.telecomTypeName}"/>
                            <html:hidden property="telecom(${item.key}).telecomTypeId"/>
                            <html:hidden property="telecom(${item.key}).telecomTypeName"/>
                            <html:hidden property="telecom(${item.key}).telecomTypeType"/>
                            <html:hidden property="telecom(${item.key}).telecomTypePosition"/>
                        </td>
                    </tr>
                    <c:forEach var="telecom" items="${item.value.telecoms}" varStatus="i">
                        <tr>
                            <td>
                                <html:hidden property="telecom(${item.key}).telecom[${i.index}].telecomId"/>
                                <app:text property="telecom(${item.key}).telecom[${i.index}].data"
                                          titleKey="Telecom.value"
                                          styleClass="largeText ${app2:getFormInputClasses()}" maxlength="100"
                                          view="${op == 'delete'}"
                                          tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                          styleId="${item.value.telecomTypeId}${i.index}Id"/>
                            </td>
                            <td>
                                <app:text property="telecom(${item.key}).telecom[${i.index}].description"
                                          titleKey="Telecom.description"
                                          styleClass="middleText ${app2:getFormInputClasses()}"
                                          maxlength="200"
                                          view="${op == 'delete'}"
                                          tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"/>
                            </td>
                            <td>
                                <c:if test="${op != 'delete'}">
                                <div class="radiocheck">
                                    <div class="radio radio-default radio-inline">
                                        <input type="radio" name="telecom(${item.key}).predeterminedIndex"
                                               value="${i.index}"
                                               class="radio"
                                               title="<fmt:message key="Common.predetermined"/>"  ${telecom.predetermined ? 'checked' : ''}
                                               tabIndex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                        <label for="isDefault_id"></label>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <html:submit property="dto(removeTelecom${item.key}@${telecom.telecomId}#${i.index})"
                                             value="&#xe020;"
                                             tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                             styleClass="cancel glyphicon glyphicon-trash btn-link paddingRemove"
                                             styleId="removeTelecom${item.key}@${telecom.telecomId}#${i.index}_id"
                                             titleKey="Contact.removeTelecom"
                                             disabled="${op == 'delete'}"
                                             onclick="javascript:fillMultipleSelectValues();">
                                </html:submit>

                                <c:if test="${'LINK' == item.value.telecomTypeType}">
                                    <a href="javascript:openURL('${item.value.telecomTypeId}${i.index}Id')"
                                       class="btn-link"
                                       tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                       alt="<fmt:message   key="Common.openLink"/>"
                                       title="<fmt:message   key="Common.openLink"/>">
                                        <span class="glyphicon glyphicon-globe"></span>
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
                                                            <span class="glyphicon glyphicon-edit"
                                                                  alt="<fmt:message   key="Document.newTracking"/>"
                                                                  title="<fmt:message   key="Document.newTracking"/>"></span>
                                                        </app:link>
                                                        <a href="javascript:callto('${item.value.telecomTypeId}${i.index}Id', 'callto')"
                                                           tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                            <span class="glyphicon glyphicon-phone-alt"
                                                                  alt="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                                                  title="<fmt:message   key="Contact.newVOIP.phoneCall"/>">

                                                            </span>
                                                        </a>
                                                        <a href="javascript:callto('${item.value.telecomTypeId}${i.index}Id', 'tel')"
                                                           tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                            <span class="glyphicon glyphicon-phone"
                                                                  alt="<fmt:message   key="Contact.telecom.mobileCall"/>"
                                                                  title="<fmt:message   key="Contact.telecom.mobileCall"/>">

                                                            </span>
                                                        </a>
                                                    </c:when>
                                                    <c:when test="${'FAX' == item.value.telecomTypeType}">
                                                        <app:link action="${action}${fax}"
                                                                  tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                            <span class="glyphicon glyphicon-edit"
                                                                  alt="<fmt:message   key="Document.newTracking"/>"
                                                                  title="<fmt:message   key="Document.newTracking"/>">
                                                            </span>
                                                        </app:link>
                                                    </c:when>
                                                    <c:when test="${'EMAIL' == item.value.telecomTypeType}">

                                                        <app2:checkAccessRight functionality="MAIL"
                                                                               permission="EXECUTE">

                                                            <app:link titleKey="Document.newMail"
                                                                      action="${action2}${email}&dto(addressId)=${param.contactId}&dto(selectedEmail)=${app2:encode(telecom.data)}"
                                                                      tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                                <i class="fa fa-at fa-lg"></i>
                                                            </app:link>
                                                        </app2:checkAccessRight>
                                                    </c:when>
                                                    <c:when test="${'OTHER' == item.value.telecomTypeType}">
                                                        <app:link action="${action}${other}"
                                                                  tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                            <span class="glyphicon glyphicon-edit"
                                                                  alt="<fmt:message   key="Document.newTracking"/>"
                                                                  title="<fmt:message   key="Document.newTracking"/>">
                                                            </span>
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
                                                            <span class="glyphicon glyphicon-edit"
                                                                  alt="<fmt:message   key="Document.newTracking"/>"
                                                                  title="<fmt:message   key="Document.newTracking"/>">
                                                            </span>
                                                        </app:link>
                                                        <a href="javascript:callto('${item.value.telecomTypeId}${i.index}Id', 'callto')"
                                                           tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                            <span class="glyphicon glyphicon-phone-alt"
                                                                  alt="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                                                  title="<fmt:message   key="Contact.newVOIP.phoneCall"/>">
                                                            </span>
                                                        </a>
                                                        <a href="javascript:callto('${item.value.telecomTypeId}${i.index}Id', 'tel')"
                                                           tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                            <span class="glyphicon glyphicon-phone"
                                                                  alt="<fmt:message   key="Contact.telecom.mobileCall"/>"
                                                                  title="<fmt:message   key="Contact.telecom.mobileCall"/>">
                                                            </span>
                                                        </a>
                                                    </c:when>
                                                    <c:when test="${'FAX' == item.value.telecomTypeType}">
                                                        <app:link action="${action}${fax}"
                                                                  tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                            <span class="glyphicon glyphicon-edit"
                                                                  alt="<fmt:message   key="Document.newTracking"/>"
                                                                  title="<fmt:message   key="Document.newTracking"/>">
                                                            </span>
                                                        </app:link>
                                                    </c:when>
                                                    <c:when test="${'EMAIL' == item.value.telecomTypeType}">
                                                        <app2:checkAccessRight functionality="MAIL"
                                                                               permission="EXECUTE">
                                                            <app:link titleKey="Document.newMail"
                                                                      action="${action2}${email}&dto(addressId)=${param.contactId}&dto(selectedEmail)=${app2:encode(telecom.data)}"
                                                                      tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                                <i class="fa fa-at fa-lg"></i>
                                                            </app:link>
                                                        </app2:checkAccessRight>
                                                    </c:when>
                                                    <c:when test="${'OTHER' == item.value.telecomTypeType}">
                                                        <app:link action="${action}${other}"
                                                                  tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                                            <span class="glyphicon glyphicon-edit"
                                                                  alt="<fmt:message   key="Document.newTracking"/>"
                                                                  title="<fmt:message   key="Document.newTracking"/>">
                                                            </span>
                                                        </app:link>
                                                    </c:when>
                                                </c:choose>
                                            </app2:checkAccessRight>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
            </c:forEach>
            <c:if test="${op != 'delete'}">
                <tr>
                    <td>
                        <c:set var="telecomTypes" value="${app2:getTelecomTypes(pageContext.request)}"/>
                        <html:select property="dto(telecomTypeId)"
                                     styleClass="shortSelect ${app2:getFormSelectClasses()}"
                                     readonly="${op == 'delete'}"
                                     tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="telecomTypes" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                    <td>
                        <html:text property="dto(telecomValue)" titleKey="Telecom.value"
                                   styleClass="largeText ${app2:getFormInputClasses()}"
                                   maxlength="100"
                                   readonly="${op == 'delete'}"
                                   tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"/>
                    </td>
                    <td>
                        <html:text property="dto(telecomDescription)" titleKey="Telecom.description"
                                   styleClass="middleText ${app2:getFormInputClasses()}"
                                   maxlength="200" readonly="${op == 'delete'}"
                                   tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"/>
                    </td>
                    <td colspan="2">
                        <html:submit property="dto(addTelecom)" value="&#xf067;"
                                     tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                     styleClass="cancel fa fa-lg btn-link input_lineheight"
                                     styleId="addTelecom_id"
                                     titleKey="Contact.addTecom"
                                     disabled="${op == 'delete'}"
                                     onclick="javascript:fillMultipleSelectValues();"/>
                    </td>
                </tr>
            </c:if>
        </table>
    </div>
</div>