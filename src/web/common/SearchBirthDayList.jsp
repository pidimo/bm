<%@ page import="com.piramide.elwis.utils.ContactConstants,
                 com.piramide.elwis.utils.DateUtils,
                 java.util.Date" %>
<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>
<br>
<table width="80%" border="0" align="center" cellpadding="1" cellspacing="0">
    <tr>
        <td>
            <table width="90%" border="0" align="center" cellpadding="0" cellspacing="1" class="container">
                <tr>
                    <td class="title">
                        <fmt:message key="Contact.birthDayList"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="3" cellspacing="0" class="container">
                            <html:form action="/SearchBirthDayList.do" focus="parameter(increment)">
                                <TR>
                                    <td class="label">
                                        <fmt:message key="Contact.birthDay"/>
                                    </td>
                                    <td align="left" class="contain" colspan="3">
                                        <html:text property="parameter(decrement)" styleClass="zipText" maxlength="2"
                                                   style="width:30px"/>
                                        <fmt:message key="Common.beforeDays"/>

                                    </td>
                                    <td align="left" class="contain" colspan="3">
                                        <html:text property="parameter(increment)" styleClass="zipText" maxlength="2"
                                                   style="width:30px"/>
                                        <fmt:message key="Common.laterDays"/>
                                        <html:submit styleClass="button">
                                            <fmt:message key="Common.go"/>
                                        </html:submit>
                                        <html:cancel styleClass="button">
                                            <fmt:message key="Common.cancel"/>
                                        </html:cancel>
                                    </td>
                                </TR>
                            </html:form>
                        </table>
                    </td>
                </tr>
            </table>
            <br>
            <TABLE id="BirthDayList.jsp" border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
                <tr align="center">
                    <td>
                        <fanta:table list="birthdayList" width="100%" id="contact" action="SearchBirthDayList.do"
                                     imgPath="${baselayout}">
                            <c:choose>
                                <c:when test="${contact.type == personType }">
                                    <c:set var="editAction"
                                           value="contacts/Person/Forward/Update.do?contactId=${contact.id}&m=1&index=0&dto(addressId)=${contact.id}&dto(addressType)=${contact.type}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}"/>
                                </c:when>
                                <c:when test="${contact.type == organizationType}">
                                    <c:set var="editAction"
                                           value="contacts/Organization/Forward/Update.do?contactId=${contact.id}&m=1&index=0&dto(addressId)=${contact.id}&dto(addressType)=${contact.type}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}"/>
                                </c:when>
                            </c:choose>
                            <fanta:dataColumn name="addressName" action="${editAction}" styleClass="listItem"
                                              title="Contact.name" headerStyle="listHeader" width="55%" orderable="true"
                                              maxLength="30">
                            </fanta:dataColumn>
                            <fanta:dataColumn name="birthday" styleClass="listItem" title="Contact.Person.birthday"
                                              headerStyle="listHeader" width="30%" orderable="true" renderData="false">
                                <c:set var="birtdayLength" value="${fn:length(contact.birtdayComplete)}"/>
                                <fmt:message var="datePattern" key="datePattern"/>
                                <c:if test="${birtdayLength != 8}">
                                    <fmt:message var="datePattern" key="withoutYearPattern"/>
                                </c:if>
                                <fmt:formatDate var="dateValue" value="${app2:intToDate(contact.birtdayComplete)}"
                                                pattern="${datePattern}"/>
                                ${dateValue}&nbsp;
                            </fanta:dataColumn>
                            <fanta:dataColumn name="age" styleClass="listItem2" title="Contact.age"
                                              headerStyle="listHeader" width="10%" orderable="true" renderData="false"
                                              style="text-align:right">
                                <c:set var="myAge" value="${fn:length(contact.age)}"/>
                                <c:if test="${myAge < 4}">
                                    ${contact.age}
                                </c:if>
                                &nbsp;
                            </fanta:dataColumn>
                        </fanta:table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
