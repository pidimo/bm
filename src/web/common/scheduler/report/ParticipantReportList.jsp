<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<%
    pageContext.setAttribute("typeUserList", JSPHelper.getUserTypeList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<table border="0" cellpadding="0" cellspacing="0" width="80%" class="container" align="center">
    <tr>
        <td height="20" class="title" colspan="4"><fmt:message key="Scheduler.Report.ParticipantList"/></td>
    </tr>

    <html:form action="/Report/ParticipantList/Execute.do?parameter(userId)=${sessionScope.user.valueMap['userId']}"
               focus="parameter(appointmentId)" styleId="schedulerStyle">
        <TR>
            <TD width="15%" class="label"><fmt:message key="Scheduler.Appointment"/></TD>
            <TD class="contain" width="35%">

                <html:hidden property="parameter(appointmentId)" styleId="appointmentId_id"/>
                <app:text property="parameter(appointmentName)" styleId="appointmentName_id" styleClass="mediumText"
                          maxlength="40" tabindex="1" readonly="true"/>
                <tags:selectPopup url="/scheduler/SearchAppointment.do" name="appointmentList" titleKey="Common.search"
                                  width="820" heigth="450"/>
                <tags:clearSelectPopup keyFieldId="appointmentId_id" nameFieldId="appointmentName_id"
                                       titleKey="Common.clear"/>

            </TD>
            <td class="label" width="15%"><fmt:message key="User.typeUser"/></td>
            <td class="contain" width="35%">
                <html:select property="parameter(type)" styleClass="mediumSelect" tabindex="3">
                    <html:option value=""/>
                    <html:options collection="typeUserList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </TR>
        <TR>
            <TD class="label">
                <fmt:message key="Appointment.groupName"/>
            </td>
            <td class="contain" colspan="3">
                <fanta:select property="parameter(userGroupId)" listName="userGroupList" tabIndex="2" firstEmpty="true"
                              labelProperty="groupName" valueProperty="userGroupId" styleClass="mediumSelect"
                              module="/admin">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </TD>
        </TR>

        <c:set var="reportFormats" value="${participantReportListForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${participantReportListForm.pageSizes}" scope="request"/>

        <tags:reportOptionsTag/>

        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" tabindex="31"><fmt:message key="Campaign.Report.generate"/>
                </html:submit>
                <html:button property="reset1" tabindex="32" styleClass="button" onclick="formReset('schedulerStyle')">
                    <fmt:message
                            key="Common.clear"/></html:button>
            </td>
        </tr>
        <c:set var="internalUser"><fmt:message key="User.intenalUser"/></c:set>
        <c:set var="externalUser"><fmt:message key="User.externalUser"/></c:set>

        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="participantReportList" title="Scheduler.Report.ParticipantList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
        <titus:reportFieldTag name="title" resourceKey="Scheduler.Appointment" type="${FIELD_TYPE_STRING}" width="0"
                              fieldPosition="20" isGroupingColumn="true"/>
        <titus:reportFieldTag name="userName" resourceKey="User.user" type="${FIELD_TYPE_STRING}" width="50"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="type" resourceKey="Appointment.appType" type="${FIELD_TYPE_STRING}" width="25"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource type [${internalUser}] [${externalUser}] [1] [0]"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="groupName" resourceKey="Appointment.groupName" type="${FIELD_TYPE_STRING}"
                              width="25" fieldPosition="4"/>
    </html:form>
</table>
