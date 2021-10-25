<%@ page import="com.piramide.elwis.web.dashboard.component.util.Constant" %>
<%@ include file="/Includes.jsp" %>

<c:set var="VIEW_SELECTED_EMPLOYEE" value="<%=Constant.BirthdayViewType.SELECTED_EMPLOYEE.getConstantAsString()%>" scope="request"/>

<script language="JavaScript">
    function birthdaySubmitPreProcess() {
        var viewType = $("#viewTypeBox").val();

        if(viewType == '${VIEW_SELECTED_EMPLOYEE}') {
            fillSelectedEmployeeIds();
        } else {
            emptyEmployeeValues();
        }
    }

    function moveOptionSelected(sourceId, targetId) {
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

    function fillSelectedEmployeeIds() {
        var employeeIds = '';
        $("#selectedEmployee_Id").find("option").each(function() {
            if(employeeIds.length > 0) {
                employeeIds = employeeIds + ",";
            }
            employeeIds = employeeIds + $(this).val();
        });

        //set in hidden dto property
        $("#birthdayEmployeeIds_Id").val(employeeIds);
    }

    function emptyEmployeeValues() {
        $("#birthdayEmployeeIds_Id").val("");
        $('#notRelatedCheckbox').prop('checked', false);
    }

    function viewBoxSelected(selectBox) {
        if(selectBox.value == '${VIEW_SELECTED_EMPLOYEE}'){
            document.getElementById('trRelatedEmployee').style.display = '';
            document.getElementById('trNotRelatedEmployee').style.display = '';
        } else {
            document.getElementById('trRelatedEmployee').style.display = 'none';
            document.getElementById('trNotRelatedEmployee').style.display = 'none';
        }
    }

</script>



<div class="${app2:getFormGroupClasses()}">
    <label class="${app2:getFormLabelClasses()}">
        <fmt:message key="dashboard.birthDay.view"/>
    </label>
    <div class="${app2:getFormContainClasses(null)}">
        <c:set var="viewTypeList" value="${app2:getDashboardBirthdayViewTypes(pageContext.request)}"/>
        <html:select property="dto(birthdayViewType)"
                     styleId="viewTypeBox"
                     styleClass="middleSelect ${app2:getFormSelectClasses()}"
                     onchange="javascript:viewBoxSelected(this);"
                     onkeyup="javascript:viewBoxSelected(this);">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="viewTypeList" property="value" labelProperty="label"/>
        </html:select>
    </div>
</div>

<c:set var="displayTr" value="display:none" scope="request"/>
<c:if test="${componentForm.dtoMap['birthdayViewType'] eq VIEW_SELECTED_EMPLOYEE}">
    <c:set var="displayTr" value="" scope="request"/>
</c:if>

    <div class="${app2:getFormGroupClasses()}" id="trRelatedEmployee" style="${displayTr}">
        <label class="${app2:getFormLabelClasses()}">
            <fmt:message key="dashboard.birthDay.relatedToEmployee"/>
        </label>
        <div class="${app2:getFormContainClasses(null)}">
            <html:hidden property="dto(birthdayEmployeeIds)" styleId="birthdayEmployeeIds_Id"/>

            ${app2:buildDashboardBirthdayEmployeeViewValues(componentForm.dtoMap['birthdayEmployeeIds'], pageContext.request)}
            <c:set var="selectedElms" value="${selectedEmployeeList}"/>
            <c:set var="availableElms" value="${availableEmployeeList}"/>

            <table width="100%">
                <tr>
                    <td width="49%">
                        <fmt:message key="common.selected"/>
                        <br>
                        <html:select property="dto(selectedEmployee)"
                                     styleId="selectedEmployee_Id"
                                     multiple="true"
                                     styleClass="middleMultipleSelect ${app2:getFormSelectClasses()}">
                            <html:options collection="selectedElms" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                    <td width="1%">
                        <br>
                        <html:button property="select"
                                     value="&#xf053;"
                                     onclick="javascript:moveOptionSelected('availableEmployee_Id','selectedEmployee_Id');"
                                     styleClass="fa fa-lg ${app2:getFormButtonClasses()}"
                                     titleKey="Common.add">
                        </html:button>
                        <br>
                        <br>
                        <html:button property="select"
                                     value="&#xf054;"
                                     onclick="javascript:moveOptionSelected('selectedEmployee_Id','availableEmployee_Id');"
                                     styleClass="fa fa-lg ${app2:getFormButtonClasses()}"
                                     titleKey="Common.delete">
                        </html:button>
                    </td>
                    <td width="49%">
                        <fmt:message key="common.available"/>
                        <br>
                        <html:select property="dto(availableEmployee)"
                                     styleId="availableEmployee_Id"
                                     multiple="true"
                                     styleClass="middleMultipleSelect ${app2:getFormSelectClasses()}">

                            <html:options collection="availableElms" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
            </table>
        </div>
    </div>


<div class="${app2:getFormGroupClasses()}" id="trNotRelatedEmployee" style="${displayTr}">
    <label class="${app2:getFormLabelClasses()}" for="notRelatedCheckbox">
        <fmt:message key="dashboard.birthDay.notRelatedToEmployee"/>
    </label>
    <div class="${app2:getFormContainClasses(null)}">
        <div class="radiocheck">
            <div class="checkbox checkbox-default">
                <html:checkbox property="dto(viewNotRelatedEmployee)"
                               styleId="notRelatedCheckbox"
                               value="true"
                               styleClass="radio"/>
                <label for="viewNotRelatedEmployee_id"></label>
            </div>
        </div>
    </div>
</div>
