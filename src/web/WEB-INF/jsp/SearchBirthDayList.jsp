<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>
<div class="${app2:getListWrapperClasses()}">
    <html:form action="/SearchBirthDayList.do" focus="parameter(increment)" styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <legend class="title">
                <fmt:message key="Contact.birthDayList"/>
            </legend>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Contact.birthDay"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <label class="control-label" for="">
                                <fmt:message key="Common.beforeDays"/>
                            </label>
                            <html:text property="parameter(decrement)"
                                       styleClass="zipText ${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                       maxlength="2"/>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <label class="control-label" for="">
                                <fmt:message key="Common.laterDays"/>
                            </label>
                            <html:text property="parameter(increment)"
                                       styleClass="zipText ${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                       maxlength="2"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                <fmt:message key="Common.go"/>
            </html:submit>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </html:form>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="birthdayList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%"
                     id="contact"
                     action="SearchBirthDayList.do"
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
    </div>
</div>



