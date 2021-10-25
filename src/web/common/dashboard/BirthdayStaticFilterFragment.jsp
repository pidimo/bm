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

<tr>
    <td class="label">
        <fmt:message key="dashboard.birthDay.view"/>
    </td>
    <td class="contain" colspan="3">
        <c:set var="viewTypeList" value="${app2:getDashboardBirthdayViewTypes(pageContext.request)}"/>
        <html:select property="dto(birthdayViewType)" styleId="viewTypeBox" styleClass="middleSelect"
                     onchange="javascript:viewBoxSelected(this);"
                     onkeyup="javascript:viewBoxSelected(this);">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="viewTypeList" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>

<c:set var="displayTr" value="display:none" scope="request"/>
<c:if test="${componentForm.dtoMap['birthdayViewType'] eq VIEW_SELECTED_EMPLOYEE}">
    <c:set var="displayTr" value="" scope="request"/>
</c:if>

<tr id="trRelatedEmployee" style="${displayTr}">
    <td class="label" style="vertical-align:top">
        <fmt:message key="dashboard.birthDay.relatedToEmployee"/>
    </td>
    <td class="contain" colspan="3">
        <html:hidden property="dto(birthdayEmployeeIds)" styleId="birthdayEmployeeIds_Id"/>

        ${app2:buildDashboardBirthdayEmployeeViewValues(componentForm.dtoMap['birthdayEmployeeIds'], pageContext.request)}
        <c:set var="selectedElms" value="${selectedEmployeeList}"/>
        <c:set var="availableElms" value="${availableEmployeeList}"/>

        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <fmt:message key="common.selected"/>
                    <br>
                    <html:select property="dto(selectedEmployee)"
                                 styleId="selectedEmployee_Id"
                                 multiple="true"
                                 styleClass="middleMultipleSelect">
                        <html:options collection="selectedElms" property="value" labelProperty="label"/>
                    </html:select>
                </td>
                <td>
                    <html:button property="select"
                                 onclick="javascript:moveOptionSelected('availableEmployee_Id','selectedEmployee_Id');"
                                 styleClass="adminLeftArrow" titleKey="Common.add">&nbsp;
                    </html:button>
                    <br>
                    <br>
                    <html:button property="select"
                                 onclick="javascript:moveOptionSelected('selectedEmployee_Id','availableEmployee_Id');"
                                 styleClass="adminRighttArrow" titleKey="Common.delete">&nbsp;
                    </html:button>
                </td>
                <td>
                    <fmt:message key="common.available"/>
                    <br>
                    <html:select property="dto(availableEmployee)"
                                 styleId="availableEmployee_Id"
                                 multiple="true"
                                 styleClass="middleMultipleSelect">

                        <html:options collection="availableElms" property="value" labelProperty="label"/>
                    </html:select>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr id="trNotRelatedEmployee" style="${displayTr}">
    <td class="label">
        <fmt:message key="dashboard.birthDay.notRelatedToEmployee"/>
    </td>
    <td class="contain" colspan="3">
        <html:checkbox property="dto(viewNotRelatedEmployee)" styleId="notRelatedCheckbox" value="true" styleClass="radio"/>
    </td>
</tr>
