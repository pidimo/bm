<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>
<%--<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>--%>

<c:set var="timeZone" value="${timeZone}"/>
<script language="JavaScript">
    function onSubmit() {
        document.forms[1].submit();
    }

    function myReset() {
        var form = document.appointmentListForm;
        for (i = 0; i < form.elements.length; i++) {

            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].name == "parameter(isRecurrence)") {
                form.elements[i].checked = false;
            } else if (form.elements[i].name == "beginsWithParam(title)") {
                form.elements[i].checked = false;
            } else if (form.elements[i].name == "parameter(contactPersonId)") {
                form.elements[i].options.length = 1;
            } else if (form.elements[i].name == "parameter(addressId)") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].selectedIndex = 0;
            }
        }
    }

    function clearContactValues() {
        document.getElementById('fieldAddressId_id').value = "";
        document.getElementById('fieldAddressName_id').value = "";
        document.getElementById('contactPersonId_id').options.length = 1;
    }
</script>

<c:set var="returnType" value="<%= SchedulerConstants.RETURN_ADVANCED_SEARCHLIST%>"/>

<html:form action="/AppointmentAdvancedList.do" focus="parameter(title)" styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="Scheduler.appointment.advancedSearch"/>
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="title_id">
                        <fmt:message key="Appointment.name"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                        <html:text property="parameter(title)"
                                   styleId="title_id"
                                   styleClass="largeText ${app2:getFormInputClasses()}"
                                   tabindex="1"/>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="appTypeId_id">
                        <fmt:message key="Appointment.appType"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                        <fanta:select property="parameter(appTypeId)"
                                      styleId="appTypeId_id"
                                      listName="appointmentTypeList"
                                      labelProperty="name" valueProperty="appointmentTypeId"
                                      styleClass="largeSelect ${app2:getFormSelectClasses()}"
                                      readOnly="${op == 'delete' || onlyView}" module="/scheduler" firstEmpty="true"
                                      tabIndex="2">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                        <fmt:message key="Appointment.contact"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                        <div class="input-group">
                            <app:text property="parameter(contact_Name_Field)" styleId="fieldAddressName_id"
                                      styleClass="largeText ${app2:getFormInputClasses()}"
                                      maxlength="40" tabindex="3" view="${readOnly}" readonly="true"/>
                            <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
                    <span class="input-group-btn">
                        <tags:bootstrapSelectPopup styleId="searchAddress_id"
                                                   url="/contacts/SearchAddress.do"
                                                   name="searchAddress"
                                                   titleKey="Common.search"
                                                   isLargeModal="true"
                                                   modalTitleKey="Contact.Title.search"
                                                   submitOnSelect="true"/>
                        <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                                        titleKey="Common.clear" onclick="clearContactValues()"/>
                    </span>
                        </div>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="isRecurrence_id">
                        <fmt:message key="Appointment.repeat"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">

                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="parameter(isRecurrence)"
                                               styleId="isRecurrence_id"
                                               tabindex="4" styleClass="radio">
                                </html:checkbox>
                                <label for="isRecurrence_id"></label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="contactPersonId_id">
                        <fmt:message key="Appointment.contactPerson"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                        <fanta:select property="parameter(contactPersonId)" styleId="contactPersonId_id" tabIndex="5"
                                      listName="searchContactPersonList" module="/contacts" firstEmpty="true"
                                      labelProperty="contactPersonName" valueProperty="contactPersonId"
                                      styleClass="largeSelect ${app2:getFormSelectClasses()}"
                                      readOnly="${readOnly}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId"
                                             value="${not empty appointmentListForm.params.addressId?appointmentListForm.params.addressId:0}"/>
                            <%--<fanta:parameter field="addressId" value="${not empty addressId?addressId:0}"/>--%>
                        </fanta:select>
                    </div>
                </div>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Appointment.startDate"/>

                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="datePattern" var="datePattern"/>
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(startDateMillis)"
                                                  maxlength="10"
                                                  placeHolder="${from}"
                                                  mode="bootstrap"
                                                  tabindex="6" styleId="startDate"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  parseLongAsDate="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.to" var="to"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(endDateMillis)" maxlength="10"
                                                  placeHolder="${to}"
                                                  mode="bootstrap"
                                                  tabindex="7" styleId="endDate"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  parseLongAsDate="true"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="createdById_id">
                        <fmt:message key="Appointment.createdBy"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                        <fanta:select property="parameter(createdById)"
                                      styleId="createdById_id"
                                      listName="userBaseList"
                                      labelProperty="name" valueProperty="id"
                                      styleClass="largeSelect ${app2:getFormSelectClasses()}"
                                      tabIndex="8" firstEmpty="true"
                                      module="/admin">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>
            </div>
        </fieldset>

        <div class="col-xs-12 row">
            <html:submit styleClass="button ${app2:getFormButtonClasses()}" tabindex="10" property="submitButton">
                <fmt:message key="Common.go"/>
            </html:submit>
            <html:button property="reset1" tabindex="11" styleClass="button ${app2:getFormButtonClasses()}"
                         onclick="myReset()">
                <fmt:message key="Common.clear"/>
            </html:button>
        </div>
    </div>
    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="AppointmentAdvancedList.do" mode="bootstrap" parameterName="titleAlphabet"/>
    </div>
</html:form>

<app2:checkAccessRight functionality="APPOINTMENT" permission="CREATE">
    <%-- rightAddAppointment and back variables set on SchedulerShortCuts.jsp--%>
    <c:if test="${rightAddAppointment}">

        <c:set var="newButtonsTable" scope="page">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <app:url
                        value="/Appointment/Forward/Create.do?create=true&dto(returnType)=${returnType}&type=5&advancedListForward=AppointmentAdvancedSearch${back}"
                        addModuleParams="false" var="newAppointmentUrl"/>
                <input type="button" class="button ${app2:getFormButtonClasses()}"
                       value="<fmt:message key="Common.new"/>"
                       onclick="window.location ='${newAppointmentUrl}'">
            </div>
        </c:set>

    </c:if>
</app2:checkAccessRight>
<c:out value="${newButtonsTable}" escapeXml="false"/>

<c:set var="userPermissionIsNULL"
       value="${publicSchedulerUserPermission == null && privateSchedulerUserPermission == null}"/>

<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<div class="table-responsive">
    <fanta:table mode="bootstrap" width="100%" id="appointment"
                 styleClass="${app2:getFantabulousTableClases()}"
                 action="AppointmentAdvancedList.do" imgPath="${baselayout}">
        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

            <c:set var="isPublicApp" value="${appointment.private eq '0'}"/>
            <c:set var="rightEditAppointment"
                   value="${userPermissionIsNULL ? true : app2:hasEditAppointmentPermission(publicSchedulerUserPermission, privateSchedulerUserPermission, isPublicApp)}"/>
            <c:set var="rightDelAppointment"
                   value="${userPermissionIsNULL ? true : app2:hasDelAppointmentPermission(publicSchedulerUserPermission, privateSchedulerUserPermission, isPublicApp)}"/>
            <c:set var="onlyAnonymousPermission"
                   value="${userPermissionIsNULL ? false : app2:hasOnlyAnonymousAppPermission(publicSchedulerUserPermission, privateSchedulerUserPermission, isPublicApp)}"/>
            <c:set var="renderRow" value="${onlyAnonymousPermission ? 'false' :'true'}"/>

            <c:set var="isOwner" value="${sessionScope.user.valueMap.schedulerUserId == appointment.appUserId}"/>
            <c:set var="editAction"
                   value="Appointment/Forward/Update.do?type=6&dto(appointmentId)=${appointment.appointmentId}&dto(title)=${app2:encode(appointment.title)}&update=true${not (isOwner && rightEditAppointment) ? '&onlyView=true' : ''}&advancedListForward=AppointmentAdvancedSearch"/>
            <c:set var="deleteAction"
                   value="Appointment/Forward/Delete.do?type=6&dto(withReferences)=true&dto(appointmentId)=${appointment.appointmentId}&dto(title)=${app2:encode(appointment.title)}&operation=delete&fromView=true&advancedListForward=AppointmentAdvancedSearch"/>
            <app2:checkAccessRight functionality="APPOINTMENT" permission="VIEW">
                <c:choose>
                    <c:when test="${onlyAnonymousPermission}">
                        <fanta:actionColumn name="update" styleClass="listItem" headerStyle="listHeader" width="50%">
                            &nbsp;
                        </fanta:actionColumn>
                    </c:when>
                    <c:otherwise>
                        <fanta:actionColumn name="update" label="Common.update" action="${editAction}"
                                            title="Common.update"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            glyphiconClass="${app2:getClassGlyphEdit()}"/>
                    </c:otherwise>
                </c:choose>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="APPOINTMENT" permission="DELETE">
                <fanta:actionColumn name="delete" styleClass="listItem" headerStyle="listHeader" width="50%">
                    <c:choose>
                        <c:when test="${rightDelAppointment and (appointment.appUserId == sessionScope.user.valueMap['schedulerUserId'])}">
                            <html:link action="${deleteAction}" titleKey="Common.delete">
                                <span class="${app2:getClassGlyphTrash()}"></span>
                            </html:link>
                        </c:when>
                        <c:otherwise>
                            &nbsp;
                        </c:otherwise>
                    </c:choose>
                </fanta:actionColumn>
            </app2:checkAccessRight>
        </fanta:columnGroup>
        <fanta:dataColumn name="title" action="${editAction}" styleClass="listItem" title="Appointment.name"
                          headerStyle="listHeader" width="30%" orderable="true" maxLength="50"
                          renderData="${renderRow}" renderUrl="${renderRow}">
            <fmt:message key="Scheduler.appointment.anonymous"/>
        </fanta:dataColumn>
        <fanta:dataColumn name="appTypeName" styleClass="listItem" title="Appointment.appType"
                          headerStyle="listHeader" width="15%" orderable="true"
                          renderData="${renderRow}">
            <fmt:message key="Scheduler.appointment.anonymous"/>
        </fanta:dataColumn>
        <fanta:dataColumn name="ruleType" styleClass="listItem" title="Appointment.periodRecurrence"
                          headerStyle="listHeader"
                          width="20%" orderable="false" renderData="false">
            <c:choose>
                <c:when test="${onlyAnonymousPermission}">
                    <fmt:message key="Scheduler.appointment.anonymous"/>
                </c:when>
                <c:when test="${appointment.ruleType == '1'}">
                    <fmt:message key="Scheduler.calendarView.daily"/>
                </c:when>
                <c:when test="${appointment.ruleType == '2'}">
                    <fmt:message key="Scheduler.calendarView.weekly"/>
                </c:when>
                <c:when test="${appointment.ruleType == '3'}">
                    <fmt:message key="Scheduler.calendarView.monthly"/>
                </c:when>
                <c:when test="${appointment.ruleType == '4'}">
                    <fmt:message key="Scheduler.calendarView.yearly"/>
                </c:when>
                <c:otherwise>
                    &nbsp;
                </c:otherwise>
            </c:choose>
        </fanta:dataColumn>
        <fanta:dataColumn name="startDate" styleClass="listItem" title="Appointment.startDate"
                          headerStyle="listHeader"
                          width="15%" orderable="true" renderData="false">
            <fmt:message var="datePattern" key="datePattern"/>
            <c:choose>
                <c:when test="${appointment.isAllDay == '1'}">
                    ${app2:getDateWithTimeZone(appointment.startDate, timeZone, datePattern)}
                </c:when>
                <c:otherwise>
                    ${app2:getDateWithTimeZone(appointment.startDate, timeZone, dateTimePattern)}
                </c:otherwise>
            </c:choose>
        </fanta:dataColumn>
        <fanta:dataColumn name="endDate" styleClass="listItem2" title="Appointment.endDate"
                          headerStyle="listHeader" width="15%" orderable="true" renderData="false">

            <c:choose>
                <c:when test="${appointment.isAllDay == '1'}">
                    ${app2:getDateWithTimeZone(appointment.endDate, timeZone, datePattern)}
                </c:when>
                <c:otherwise>
                    ${app2:getDateWithTimeZone(appointment.endDate, timeZone, dateTimePattern)}
                </c:otherwise>
            </c:choose>
        </fanta:dataColumn>
    </fanta:table>
</div>
<div class="${app2:getFormButtonWrapperClasses()}">
    <c:out value="${newButtonsTable}" escapeXml="false"/>
</div>
