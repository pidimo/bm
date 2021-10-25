<%@ page import="com.piramide.elwis.utils.ProjectConstants" %>
<%@ include file="/Includes.jsp" %>
<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/cacheable/hashtable.js"/>
<tags:jscript language="JavaScript" src="/js/cacheable/jquery.numberformatter-1.2.4.min.js"/>

<c:set var="projects" value="${app2:getProjectsByUser(pageContext.request)}"/>
<c:set var="confirmedStatus" value="<%=ProjectConstants.ProjectTimeStatus.CONFIRMED.getAsString()%>"/>
<c:set var="not_confirmedStatus" value="<%=ProjectConstants.ProjectTimeStatus.NOT_CONFIRMED.getAsString()%>"/>
<c:set var="invoicedStatus" value="<%=ProjectConstants.ProjectTimeStatus.INVOICED.getAsString()%>"/>
<c:set var="projectTimeStatuses" value="${app2:getProjectTimeStatuses(pageContext.request)}"/>
<c:set var="sessionUserAddressId" value="${sessionScope.user.valueMap['userAddressId']}"/>
<c:set var="sessionUserId" value="${sessionScope.user.valueMap['userId']}"/>
<c:set var="userLocale" value="${sessionScope.user.valueMap['locale']}"/>

<%
    pageContext.setAttribute("minutesList", JSPHelper.getInterval5Minutes());
    pageContext.setAttribute("hourList", JSPHelper.getHours());
%>

<fmt:message var="datePattern" key="datePattern" scope="request"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces" scope="request"/>

<tags:initSelectPopup/>
<calendar:initialize/>

<app2:jScriptUrl url="/projects/ProjectTime/CheckLimit/Ajax.do?projectId=${param.projectId}" var="jsCheckLimitUrl">
    <app2:jScriptUrlParam param="assigneeId" value="assigneeId"/>
    <app2:jScriptUrlParam param="subProjectId" value="subProjectId"/>
    <app2:jScriptUrlParam param="toBeInvoiced" value="toBeInvoiced"/>
</app2:jScriptUrl>


<script language="JavaScript" type="text/javascript">
    <!--
    function onSubmit(obj) {
        document.getElementById("changeProjectId").value = "true";
        document.getElementById("projectTimeRegistrationId").submit();
    }
    function enableDraftEmail() {
        document.getElementById("isDraftEmailId").value = "true";
    }

    //time range manage functions
    function fromHourChange() {
        var fromHH = $("#fromHour_id").val();
        var fromMM = $("#fromMin_id").val();
        var toHH = $("#toHour_id").val();
        var toMM = $("#toMin_id").val();

        if(fromHH != '') {
            if(fromMM == '') {
                $("#fromMin_id").val(0);
            }
            if(toHH == '') {
                $("#toHour_id").val(parseInt(fromHH) + 1);
            }
            if(toMM == '') {
                $("#toMin_id").val(0);
            }
        }
        calculateTimeSpent();
    }

    function generalTimeChange() {
        calculateTimeSpent();
    }

    function calculateTimeSpent() {
        var fromHH = $("#fromHour_id").val();
        var fromMM = $("#fromMin_id").val();
        var toHH = $("#toHour_id").val();
        var toMM = $("#toMin_id").val();

        if(fromHH != '' && fromMM != '' && toHH != '' && toMM != '') {
            var startHour = parseInt(fromHH);
            var startMin = parseInt(fromMM);
            var endHour = parseInt(toHH);
            var endMin = parseInt(toMM);

            var totalHours = 0;
            if((endHour > startHour) || (endHour == startHour && endMin > startMin)) {
                var totalHourAsMin = (endHour - startHour) * 60;

                if(startMin > endMin) {
                    var minutes = startMin - endMin;
                    totalHours = (totalHourAsMin - minutes) / 60;
                } else {
                    var minutes = endMin - startMin;
                    totalHours = (totalHourAsMin + minutes) / 60;
                }

                //fix to two decimals
                totalHours = totalHours.toFixed(2);
                totalHours = roundToFixedTime(totalHours);
            }

            $("#time_id").val(formattedNumberByLocale(totalHours));
        }
    }

    /*
     * use jquery number formatted, thi depends of hashtable.js
     * */
    function formattedNumberByLocale(number) {
        var result = $.formatNumber(number, {format: "${numberFormat}", locale: "${userLocale}"});
        return result;
    }

    function roundToFixedTime(totalHours) {
        var totalHourFixed = parseFloat(totalHours);

        if(totalHourFixed > 0) {
            var twoDecimal = getSecondDecimalPlace(totalHourFixed);
            while (twoDecimal != 0 && twoDecimal != 5) {
                totalHourFixed = totalHourFixed - 0.01;
                twoDecimal = getSecondDecimalPlace(totalHourFixed);
            }
            totalHourFixed = totalHourFixed.toFixed(2);
        }
        return totalHourFixed;
    }

    function getSecondDecimalPlace(totalHours) {
        var x = totalHours * 100;
        var y = 10;
        var z = Math.round(x % y);
        return z;
    }

    //check time limit exceeded and define font color as red
    function checkLimitExceeded() {
        var assigneeId = $("#assigneeId_key").val();
        var subProjectId = $("#subProjectId_key").val();
        var toBeInvoiced = $("#toBeInvoiced_key").prop("checked");

        if(assigneeId != undefined && assigneeId != '' && subProjectId != undefined && subProjectId != '') {
            makeCheckLimitAjaxRequest(${jsCheckLimitUrl});
        }
    }

    function makeCheckLimitAjaxRequest(urlAddress) {
        $.ajax({
            async:true,
            type: "POST",
            data: " ",
            dataType: "xml",
            url:urlAddress,
            success: function(data, status) {
                processCheckLimitXMLDoc(data);
            }
        });
    }

    function processCheckLimitXMLDoc(xmldoc) {

        if (xmldoc.getElementsByTagName('checkLimit').length > 0) {

            //process xml document with jQuery
            $(xmldoc).find('limitExceeded').each(function() {

                var isLimitExceeded = $(this).attr('value');
                if('true' == isLimitExceeded) {
                    $("#time_id").css("color","#ff0000");
                } else {
                    $("#time_id").css("color","");
                }
            });
        }
    }
    //-->
</script>


<html:form action="${action}" focus="dto(projectId)" styleId="projectTimeRegistrationId">
<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(sessionUserId)" value="${sessionUserId}"/>

<c:if test="${'update'== op || 'delete'== op}">
    <html:hidden property="dto(timeId)"/>
    <html:hidden property="dto(oldStatus)"/>
    <html:hidden property="dto(projectStatus)"/>
</c:if>

<c:if test="${'update' == op}">
    <html:hidden property="dto(version)"/>
</c:if>

<c:if test="${'delete' == op}">
    <html:hidden property="dto(withReferences)" value="true"/>
</c:if>

<table border="0" cellpadding="0" cellspacing="0" width="65%" align="center" class="container">
<tr>
    <td class="button" width="100%" colspan="2">
        <c:if test="${not empty projectTimeRegistrationForm.dtoMap['projectId']}">
            <c:set var="operation" value="${op}" scope="request"/>
            <c:set var="projectId" value="${projectTimeRegistrationForm.dtoMap['projectId']}" scope="request"/>
            <c:if test="${'update' == op || 'delete' == op}">
                <c:set var="toBeInvoiced" value="${projectTimeRegistrationForm.dtoMap['toBeInvoiced']}"
                       scope="request"/>
                <c:set var="projectTimeStatus" value="${projectTimeRegistrationForm.dtoMap['status']}" scope="request"/>
                <c:set var="projectStatus" value="${projectTimeRegistrationForm.dtoMap['projectStatus']}"
                       scope="request"/>
                <c:set var="userId" value="${projectTimeRegistrationForm.dtoMap['userId']}" scope="request"/>
                <c:set var="assigneeId" value="${projectTimeRegistrationForm.dtoMap['assigneeId']}" scope="request"/>
            </c:if>
            <c:import url="/common/projects/ProjectTimeButtonFragment.jsp"/>
        </c:if>

        <html:cancel styleClass="button" tabindex="24">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
<TR>
    <TD colspan="2" class="title">
        <c:out value="${title}"/>
    </TD>
</TR>
<tr>
    <TD class="label" width="25%">
        <fmt:message key="ProjectTime.projectName"/>
    </TD>
    <TD class="contain" width="75%">
        <html:hidden property="dto(changeProject)" value="false" styleId="changeProjectId"/>
        <html:select property="dto(projectId)"
                     styleClass="middleSelect"
                     readonly="${'delete' == op || 'update' == op}"
                     tabindex="1"
                     onchange="javascript:onSubmit();">
            <html:option value=""/>
            <html:options collection="projects" property="projectId" labelProperty="projectName"/>
        </html:select>
    </TD>
</tr>
<c:if test="${not empty projectTimeRegistrationForm.dtoMap['projectId']}">
    <TR>
        <TD class="label" width="25%">
            <fmt:message key="ProjectTime.userName"/>
        </TD>
        <TD class="contain" width="75%">
            <html:hidden property="dto(userId)"/>

            <app:text property="dto(userName)"
                      styleClass="mediumText"
                      maxlength="20"
                      readonly="true"
                      view="true"/>
        </TD>
    </TR>

    <c:if test="${not empty projectTimeRegistrationForm.dtoMap['createdDate']}">
        <tr>
            <td class="label">
                <fmt:message key="ProjectTime.dateCreated"/>
            </td>
            <td class="contain">
                <app:dateText property="dto(createdDate)"
                              datePatternKey="${datePattern}"
                              styleClass="text"
                              maxlength="10"
                              view="true"/>
            </td>
        </tr>
    </c:if>

    <tr>
        <td class="label">
            <fmt:message key="ProjectTime.assignee"/>
        </td>
        <td class="contain">
            <c:set var="updateAssigneeField"
                   value="${sessionUserId == projectTimeRegistrationForm.dtoMap['userId']}"/>
            <fanta:select property="dto(assigneeId)"
                          styleId="assigneeId_key"
                          listName="projectUserList"
                          labelProperty="userName"
                          valueProperty="addressId"
                          styleClass="middleSelect"
                          module="/projects"
                          firstEmpty="true"
                          readOnly="${op == 'delete' || !canEditPage || !updateAssigneeField}"
                          onChange="checkLimitExceeded()"
                          tabIndex="1">
                <fanta:parameter field="companyId"
                                 value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="projectId"
                                 value="${param.projectId}"/>
            </fanta:select>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="ProjectTime.date"/>
        </td>
        <td class="contain">
            <app:dateText property="dto(date)"
                          styleId="dateId"
                          calendarPicker="${'delete' != op && canEditPage}"
                          datePatternKey="${datePattern}"
                          styleClass="text"
                          maxlength="10"
                          currentDate="true"
                          view="${'delete' == op || !canEditPage}"
                          tabindex="4"/>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="ProjectTime.fromToTime"/>
        </td>
        <td class="contain">
            <fmt:message key="ProjectTime.fromTime"/>
            <html:select property="dto(fromHour)" styleId="fromHour_id" onchange="fromHourChange()" onkeyup="fromHourChange()" tabindex="4"
                         styleClass="select" readonly="${'delete' == op || !canEditPage}" style="width:43">
                <html:option value="">&nbsp;</html:option>
                <html:options collection="hourList" property="value" labelProperty="label"/>
            </html:select>
            :
            <html:select property="dto(fromMin)" styleId="fromMin_id" onchange="generalTimeChange()" onkeyup="generalTimeChange()" styleClass="select"
                         tabindex="4" readonly="${'delete' == op || !canEditPage}" style="width:43">
                <html:option value="">&nbsp;</html:option>
                <html:options collection="minutesList" property="value" labelProperty="label"/>
            </html:select>

            &nbsp;
            &nbsp;

            <fmt:message key="ProjectTime.toTime"/>
            <html:select property="dto(toHour)" styleId="toHour_id" onchange="generalTimeChange()" onkeyup="generalTimeChange()" tabindex="4"
                         styleClass="select" readonly="${'delete' == op || !canEditPage}" style="width:43">
                <html:option value="">&nbsp;</html:option>
                <html:options collection="hourList" property="value" labelProperty="label"/>
            </html:select>
            :
            <html:select property="dto(toMin)" styleId="toMin_id" onchange="generalTimeChange()" onkeyup="generalTimeChange()" styleClass="select"
                         tabindex="4" readonly="${'delete' == op || !canEditPage}" style="width:43">
                <html:option value="">&nbsp;</html:option>
                <html:options collection="minutesList" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="ProjectTime.timeSpent"/>
        </td>
        <td class="contain">
            <app:numberText property="dto(time)"
                            styleId="time_id"
                            styleClass="numberText"
                            maxlength="12"
                            numberType="decimal"
                            maxInt="4"
                            maxFloat="2"
                            view="${'delete' == op || !canEditPage}"
                            tabindex="5"/>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="ProjectTime.toBeInvoiced"/>
        </td>
        <td class="contain">
            <html:hidden property="dto(disableInvoiceable)"/>
            <c:if test="${true == projectTimeRegistrationForm.dtoMap['disableInvoiceable']}">
                <html:hidden property="dto(toBeInvoiced)" styleId="toBeInvoicedId"/>
            </c:if>
            <html:checkbox property="dto(toBeInvoiced)"
                           styleId="toBeInvoiced_key"
                           styleClass="radio"
                           disabled="${op == 'delete' || true == projectTimeRegistrationForm.dtoMap['disableInvoiceable'] || !canEditPage}"
                           tabindex="6"
                           value="true"/>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="ProjectTime.activityName"/>
        </td>
        <td class="contain">
            <fanta:select property="dto(activityId)"
                          listName="projectActivityList"
                          labelProperty="activityName"
                          valueProperty="projectActivityId"
                          firstEmpty="true"
                          styleClass="middleSelect"
                          module="/projects"
                          readOnly="${'delete' == op || !canEditPage}"
                          tabIndex="8">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="projectId" value="${param.projectId}"/>
            </fanta:select>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="ProjectTime.subProjectName"/>
        </td>
        <td class="contain">
            <fanta:select property="dto(subProjectId)"
                          styleId="subProjectId_key"
                          listName="subProjectList"
                          labelProperty="subProjectName"
                          valueProperty="subProjectId"
                          firstEmpty="true"
                          styleClass="middleSelect"
                          module="/projects"
                          readOnly="${'delete' == op || !canEditPage}"
                          onChange="checkLimitExceeded()"
                          tabIndex="9">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="projectId" value="${param.projectId}"/>
            </fanta:select>
        </td>
    </tr>
    <c:if test="${confirmedStatus == projectTimeRegistrationForm.dtoMap['status'] || invoicedStatus == projectTimeRegistrationForm.dtoMap['status'] || not_confirmedStatus == projectTimeRegistrationForm.dtoMap['status']}">
        <tr>
            <td class="label">
                <fmt:message key="ProjectTime.confirmedBy"/>
            </td>
            <td class="contain">
                <html:hidden property="dto(confirmedById)"/>

                <app:text property="dto(confirmedByName)"
                          styleClass="mediumText"
                          maxlength="20"
                          readonly="true"
                          view="true"/>
            </td>
        </tr>
    </c:if>
    <tr>
        <td class="label">
            <fmt:message key="ProjectTime.status"/>
        </td>
        <td class="contain">
            <html:select property="dto(status)"
                         styleClass="mediumSelect"
                         styleId="payMethodId"
                         readonly="true"
                         tabindex="10">
                <html:option value=""/>
                <html:options collection="projectTimeStatuses"
                              property="value"
                              labelProperty="label"/>
            </html:select>
        </td>
    </tr>

    <c:if test="${not empty projectTimeRegistrationForm.dtoMap['releasedUserId']}">
        <tr>
            <td class="label">
                <fmt:message key="ProjectTime.releasedUser"/>
            </td>
            <td class="contain">
                <html:hidden property="dto(releasedUserId)"/>

                <app:text property="dto(releasedUserName)"
                          styleClass="mediumText"
                          readonly="true"
                          view="true"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="ProjectTime.releasedDate"/>
            </td>
            <td class="contain">
                <app:dateText property="dto(releasedDate)"
                              datePatternKey="${datePattern}"
                              styleClass="text"
                              maxlength="10"
                              view="true"/>
            </td>
        </tr>
    </c:if>

    <tr>
        <td class="topLabel" colspan="2">
            <fmt:message key="ProjectTime.description"/>
            <html:textarea property="dto(description)"
                           styleClass="mediumDetailHigh"
                           style="height:120px;width:99%;"
                           readonly="${'delete' == op || !canEditPage}"
                           tabindex="11"/>
        </td>
    </tr>
</c:if>


<tr>
    <td class="button" width="100%" colspan="2">
        <c:if test="${not empty projectTimeRegistrationForm.dtoMap['projectId']}">
            <c:set var="operation" value="${op}" scope="request"/>
            <c:set var="projectId" value="${projectTimeRegistrationForm.dtoMap['projectId']}" scope="request"/>
            <c:if test="${'update' == op || 'delete' == op}">
                <c:set var="toBeInvoiced" value="${projectTimeRegistrationForm.dtoMap['toBeInvoiced']}"
                       scope="request"/>
                <c:set var="projectTimeStatus" value="${projectTimeRegistrationForm.dtoMap['status']}" scope="request"/>
                <c:set var="projectStatus" value="${projectTimeRegistrationForm.dtoMap['projectStatus']}"
                       scope="request"/>
                <c:set var="userId" value="${projectTimeRegistrationForm.dtoMap['userId']}" scope="request"/>
                <c:set var="assigneeId" value="${projectTimeRegistrationForm.dtoMap['assigneeId']}" scope="request"/>
            </c:if>
            <c:import url="/common/projects/ProjectTimeButtonFragment.jsp"/>
        </c:if>
        <html:cancel styleClass="button" tabindex="13">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
</table>
</html:form>
