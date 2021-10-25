<%@ include file="/Includes.jsp" %>
<script language="JavaScript" type="text/javascript">

    function moveSelectedOption(sourceId, targetId) {
        var sourceBox = document.getElementById(sourceId);
        var targetBox = document.getElementById(targetId);
        var i = 0;
        while (i < sourceBox.options.length) {
            var opt = sourceBox.options[i];
            if (opt.selected) {
                var nOpt = new Option();
                nOpt.text = opt.text;
                nOpt.value = opt.value;
                targetBox.options[targetBox.options.length] = nOpt;
                sourceBox.remove(i);
            } else {
                i = i + 1;
            }
        }
    }

    function send() {
        setAllAsSelected('selectedUser_id');
    }

    function setAllAsSelected(boxId){
        var tbox = document.getElementById(boxId);
        for (var i = 0; i < tbox.options.length; i++) {
            tbox.options[i].selected = true;
        }
    }

</script>


<html:form action="/Calendar/Overview.do" focus="dto(outputViewType)">
    <table cellSpacing=0 cellPadding=4 width="70%" border=0 align="center">
        <tr>
            <td class="button">
                <app2:checkAccessRight functionality="APPOINTMENT" permission="VIEW">
                    <html:submit onclick="javascript:send()"  styleClass="button" tabindex="2">
                        <fmt:message key="Scheduler.overviewCalendar.view"/>
                    </html:submit>
                </app2:checkAccessRight>
            </td>
        </tr>
    </table>

    <table border="0" cellpadding="0" cellspacing="0" width="70%" align="center" class="container">
        <tr>
            <td colspan="2" class="title">
                <fmt:message key="Scheduler.overviewCalendar.title"/>
            </td>
        </tr>
        <tr>
            <td class="label" width="30%">
                <fmt:message key="Scheduler.overviewCalendar.outputView"/>
            </td>
            <td class="contain" width="70%">
                <c:set var="viewTypes" value="${app2:getOverviewCalendarOutputTypes(pageContext.request)}" scope="request"/>
                <html:select property="dto(outputViewType)" styleId="pageOrientation" styleClass="mediumSelect"
                             tabindex="3">
                    <option value="">&nbsp;</option>
                    <html:options collection="viewTypes" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Scheduler.overviewCalendar.appointmentType"/>
            </td>
            <td class="contain">
                <fanta:select property="dto(appointmentTypeId)" listName="appointmentTypeList"
                    labelProperty="name" valueProperty="appointmentTypeId" styleClass="mediumSelect"
                    module="/scheduler" firstEmpty="true" tabIndex="4">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
                </fanta:select>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Scheduler.overviewCalendar.overviewOf"/>
            </td>
            <td class="contain">
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td>
                            <fmt:message key="Scheduler.overviewCalendar.selected"/>
                            <br>
                            <html:select property="selectedUser"
                                    multiple="true" styleClass="multipleSelectFilter" tabindex="5"
                                    styleId="selectedUser_id" style="width:205">
                                <c:if test="${not empty selectedList}">
                                    <html:options collection="selectedList" property="value" labelProperty="label"/>
                                </c:if>
                            </html:select>
                        </td>
                        <td>
                            <html:button property="select"
                                         onclick="javascript:moveSelectedOption('availableUser_id','selectedUser_id');"
                                         styleClass="adminLeftArrow" titleKey="Common.add">&nbsp;
                            </html:button>
                            <br>
                            <br>
                            <html:button property="select"
                                         onclick="javascript:moveSelectedOption('selectedUser_id','availableUser_id');"
                                         styleClass="adminRighttArrow" titleKey="Common.delete">&nbsp;
                            </html:button>
                        </td>
                        <td>
                            <fmt:message key="Scheduler.overviewCalendar.available"/>
                            <br>
                            <c:if test="${availableUserList == null}">
                                <c:set var="availableUserList" value="${app2:getOverviewCalendarUsers(pageContext.request)}"/>
                            </c:if>
                            <html:select property="availableUser"
                                    multiple="true" styleClass="multipleSelectFilter" tabindex="6"
                                    styleId="availableUser_id" style="width:205">
                                    <html:options collection="availableUserList" property="value" labelProperty="label"/>
                            </html:select>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <table cellSpacing=0 cellPadding=4 width="70%" border=0 align="center">
        <tr>
            <td class="button">
                <app2:checkAccessRight functionality="APPOINTMENT" permission="VIEW">
                    <html:submit onclick="javascript:send()"  styleClass="button" tabindex="7">
                        <fmt:message key="Scheduler.overviewCalendar.view"/>
                    </html:submit>
                </app2:checkAccessRight>
            </td>
        </tr>
    </table>

</html:form>
