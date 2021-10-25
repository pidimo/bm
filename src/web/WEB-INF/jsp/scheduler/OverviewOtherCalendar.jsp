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

    function setAllAsSelected(boxId) {
        var tbox = document.getElementById(boxId);
        for (var i = 0; i < tbox.options.length; i++) {
            tbox.options[i].selected = true;
        }
    }

</script>


<html:form action="/Calendar/Overview.do" focus="dto(outputViewType)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="APPOINTMENT" permission="VIEW">
                <html:submit onclick="javascript:send()" styleClass="${app2:getFormButtonClasses()}" tabindex="2">
                    <fmt:message key="Scheduler.overviewCalendar.view"/>
                </html:submit>
            </app2:checkAccessRight>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Scheduler.overviewCalendar.title"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="pageOrientation">
                        <fmt:message key="Scheduler.overviewCalendar.outputView"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                        <c:set var="viewTypes" value="${app2:getOverviewCalendarOutputTypes(pageContext.request)}"
                               scope="request"/>
                        <html:select property="dto(outputViewType)" styleId="pageOrientation"
                                     styleClass="${app2:getFormSelectClasses()}"
                                     tabindex="3">
                            <option value="">&nbsp;</option>
                            <html:options collection="viewTypes" property="value" labelProperty="label"/>
                        </html:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="appointmentTypeId_id">
                        <fmt:message key="Scheduler.overviewCalendar.appointmentType"/>
                    </label>
                    <div class="${app2:getFormContainClasses(true)}">
                        <fanta:select property="dto(appointmentTypeId)" styleId="appointmentTypeId_id" listName="appointmentTypeList"
                                      labelProperty="name" valueProperty="appointmentTypeId" styleClass="${app2:getFormSelectClasses()}"
                                      module="/scheduler" firstEmpty="true" tabIndex="4">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="Scheduler.overviewCalendar.overviewOf"/>
                    </label>
                    <div class="${app2:getFormContainClasses(true)}">
                        <table class="col-xs-12">
                            <tr>
                                <td>
                                    <fmt:message key="Scheduler.overviewCalendar.selected"/>
                                    <br>
                                    <html:select property="selectedUser"
                                                 multiple="true" styleClass="${app2:getFormSelectClasses()} multipleSelectFilter" tabindex="5"
                                                 styleId="selectedUser_id" style="width:205">
                                        <c:if test="${not empty selectedList}">
                                            <html:options collection="selectedList" property="value"
                                                          labelProperty="label"/>
                                        </c:if>
                                    </html:select>
                                </td>
                                <td class="text-center">
                                    <html:button property="select"
                                                 onclick="javascript:moveSelectedOption('availableUser_id','selectedUser_id');"
                                                 styleClass="${app2:getFormButtonClasses()} adminLeftArrow" titleKey="Common.add">&#60;
                                    </html:button>
                                    <br>
                                    <br>
                                    <html:button property="select"
                                                 onclick="javascript:moveSelectedOption('selectedUser_id','availableUser_id');"
                                                 styleClass="${app2:getFormButtonClasses()} adminRighttArrow" titleKey="Common.delete">&#62;
                                    </html:button>
                                </td>
                                <td>
                                    <fmt:message key="Scheduler.overviewCalendar.available"/>
                                    <br>
                                    <c:if test="${availableUserList == null}">
                                        <c:set var="availableUserList"
                                               value="${app2:getOverviewCalendarUsers(pageContext.request)}"/>
                                    </c:if>
                                    <html:select property="availableUser"
                                                 multiple="true" styleClass="${app2:getFormSelectClasses()} multipleSelectFilter" tabindex="6"
                                                 styleId="availableUser_id" style="width:205">
                                        <html:options collection="availableUserList" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="APPOINTMENT" permission="VIEW">
                <html:submit onclick="javascript:send()" styleClass="${app2:getFormButtonClasses()}" tabindex="7">
                    <fmt:message key="Scheduler.overviewCalendar.view"/>
                </html:submit>
            </app2:checkAccessRight>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="overviewCalendarForm"/>