<%@ include file="/Includes.jsp" %>


<script language="JavaScript">
    <!--
    function check(actual, value) {
        var x = document.getElementById('accessRightForm');
        for (i = 0; i < x.elements.length; i++) {
            var e = x.elements[i];

            if (e.id == value) {

                if (actual.checked) {
                    if (e.disabled == false)
                        e.checked = true;
                } else {
                    if (e.disabled == false)
                        e.checked = true;
                }

            }
            actual.checked = true;
        }
    }

    function uncheck(actual, value) {
        var x = document.getElementById('accessRightForm');
        for (i = 0; i < x.elements.length; i++) {
            var e = x.elements[i];

            if (e.id == value) {

                if (actual.checked) {
                    if (e.disabled == false)
                        e.checked = false;
                } else {
                    if (e.disabled == false)
                        e.checked = false;
                }

            }
            actual.checked = false;
        }
    }

    function dependences(moduleid, functionid, permissionValue, box) {
        var x = document.getElementById('accessRightForm');
        var boxid = box.id;
        if (permissionValue != 1) {
            var permissionViewArray = getPermission(moduleid, functionid, boxid);
            var permissionView = permissionViewArray[0];
            if (null != permissionView) {
                permissionView.checked = true;
            }
        } else {
            var permissionArray = getPermission(moduleid, functionid, boxid);
            for (i in permissionArray) {
                if (i != 0) {
                    var element = permissionArray[i];
                    if (null != element)
                        element.checked = false;
                }
            }
        }
    }

    function getPermission(moduleid, functionid, id) {


        var x = document.getElementById('accessRightForm');

        var vp = null;
        var cp = null;
        var ep = null;
        var dp = null;
        var xp = null;

        var permissionArray = new Array(vp, cp, ep, dp, xp);

        for (i = 0; i < x.elements.length; i++) {
            var e = x.elements[i];
            if (e.id != id)
                continue;

            if (e.name == 'dto(checkBox_' + moduleid + '_' + functionid + '_' + 1 + ')') {
                vp = e;
                permissionArray[0] = vp;
            }
            if (e.name == 'dto(checkBox_' + moduleid + '_' + functionid + '_' + 2 + ')') {
                cp = e;
                permissionArray[1] = cp;
            }
            if (e.name == 'dto(checkBox_' + moduleid + '_' + functionid + '_' + 4 + ')') {
                ep = e;
                permissionArray[2] = ep;
            }
            if (e.name == 'dto(checkBox_' + moduleid + '_' + functionid + '_' + 8 + ')') {
                dp = e;
                permissionArray[3] = dp;
            }
            if (e.name == 'dto(checkBox_' + moduleid + '_' + functionid + '_' + 16 + ')') {
                xp = e;
                permissionArray[4] = xp;
            }

        }
        return permissionArray;
    }

    //-->
</script>


<html:form action="/Role/AccessRight/Save.do?roleName=${param.roleName}" styleId="accessRightForm" styleClass="">

    <html:hidden property="dto(version)"/>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="UPDATE" functionality="ACCESSRIGHT"
                             styleClass="button ${app2:getFormButtonClasses()}" property="dto(save)">
            <c:out value="${save}"/>
        </app2:securitySubmit>
    </div>
    <html:hidden property="dto(numberOfModules)" value="${accessRightForm.dtoMap['numberOfModules']}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <html:hidden property="dto(roleId)" value="${accessRightForm.dtoMap['roleId']}"/>
    <div class="${app2:getFormPanelClasses()}">
        <c:forEach var="companyModule" items="${dto.companyModuleDTOs}" varStatus="moduleIndex">

            <html:hidden property="dto(companyModuleId${moduleIndex.count})" value="${companyModule.moduleId}"
                         styleId="styleCompanyModuleId${moduleIndex.count}"/>
            <legend class="title">
                <fmt:message key="${companyModule.nameKey}"/>
                <div class="pull-right">
                    <div class="radio-inline">
                        <div class="checkbox checkboxLegend checkbox-default">
                            <input type="checkbox" name="all"
                                   title="<fmt:message key="Common.selectAll"/>"
                                   class="adminCheckBox"
                                   id="all_checked_${moduleIndex.count}"
                                   onclick="check(this,'box${moduleIndex.count}')" checked/>
                            <label for="all_checked_${moduleIndex.count}"></label>
                        </div>
                    </div>
                    <div class="radio-inline">
                        <div class="checkbox checkboxLegend checkbox-default">
                            <input type="checkbox" name="all_unchecker_${moduleIndex.count}"
                                   title="<fmt:message key="Common.UnselectAll"/> "
                                   class="adminCheckBox"
                                   onclick="uncheck(this,'box${moduleIndex.count}')"/>
                            <label for="all_unchecker_${moduleIndex.count}"></label>
                        </div>
                    </div>
                </div>
            </legend>
            <div class="">
                <div class="table-responsive">
                    <table width="100%" class="${app2:getTableClasesIntoForm()} ">
                        <c:forEach var="companyModuleFunctions" items="${companyModule.systemFunctionDTOs}"
                                   varStatus="functionIndex">
                            <html:hidden property="dto(moduleFunctionId${functionIndex.count})"
                                         value="${companyModuleFunctions.functionId}"
                                         styleId="styleModuleFunctionId${functionIndex.count}"/>

                            <c:choose>
                                <c:when test="${sessionScope.user.valueMap['isDefaultCompany'] == false}">
                                    <c:if test="${companyModuleFunctions.functionCode != 'COMPANY' && companyModuleFunctions.functionCode != 'APPLICATIONSIGNATURE' && companyModuleFunctions.functionCode != 'JRXMLREPORT'}">
                                        <tr>
                                            <td width="25%" class="control-label label-left">
                                                <fmt:message key="${companyModuleFunctions.nameKey}"/>
                                            </td>
                                            <c:forEach var="permissionsAllowed"
                                                       items="${app2:checkPermissionAllowed(companyModuleFunctions.permissionsAllowed, dto.accessRightsDTOs, companyModuleFunctions.functionId ,accessRightForm.dtoMap['roleId'])}">
                                                <c:if test="${permissionsAllowed.blank == true}">
                                                    <td width="15%">
                                                    </td>
                                                </c:if>
                                                <c:choose>
                                                    <c:when test="${companyModuleFunctions.disabled == true}">
                                                        <c:if test="${permissionsAllowed.isChecked == true}">
                                                            <td width="15%">
                                                                <div class="radiocheck">
                                                                    <div class="checkbox checkbox-default">
                                                                        <input
                                                                                id="box${moduleIndex.count}"
                                                                                type="checkbox"
                                                                                name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})"
                                                                                class="" checked
                                                                                disabled="disabled"
                                                                                onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}, this)">
                                                                        <label>
                                                                            <fmt:message
                                                                                    key="${permissionsAllowed.stringValue}"/>
                                                                        </label>
                                                                    </div>
                                                                </div>

                                                            </td>
                                                            <html:hidden
                                                                    property="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})"
                                                                    value="true"
                                                                    styleId="styleCheckBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}"/>
                                                        </c:if>
                                                        <c:if test="${permissionsAllowed.isChecked == false}">
                                                            <td width="15%">
                                                                <div class="radiocheck">
                                                                    <div class="checkbox checkbox-default">
                                                                        <input
                                                                                id="box${moduleIndex.count}"
                                                                                type="checkbox"
                                                                                name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})"
                                                                                class="adminCheckBox"
                                                                                onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}), this">
                                                                        <label>
                                                                            <fmt:message
                                                                                    key="${permissionsAllowed.stringValue}"/>
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </c:if>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:if test="${permissionsAllowed.isChecked == true}">
                                                            <td width="15%">
                                                                <div class="radiocheck">
                                                                    <div class="checkbox checkbox-default">
                                                                        <input
                                                                                id="box${moduleIndex.count}"
                                                                                type="checkbox"
                                                                                name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})"
                                                                                class="adminCheckBox"
                                                                                checked
                                                                                onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}, this)">
                                                                        <label>
                                                                            <fmt:message
                                                                                    key="${permissionsAllowed.stringValue}"/>
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </c:if>
                                                        <c:if test="${permissionsAllowed.isChecked == false}">
                                                            <td width="15%">
                                                                <div class="radiocheck">
                                                                    <div class="checkbox checkbox-default">
                                                                        <input
                                                                                id="box${moduleIndex.count}"
                                                                                type="checkbox"
                                                                                name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})"
                                                                                class="adminCheckBox"
                                                                                onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}, this)">
                                                                        <label>
                                                                            <fmt:message
                                                                                    key="${permissionsAllowed.stringValue}"/>
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </c:if>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </tr>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td width="25%" class="control-label label-left">
                                            <fmt:message key="${companyModuleFunctions.nameKey}"/>
                                        </td>

                                        <c:forEach var="permissionsAllowed"
                                                   items="${app2:checkPermissionAllowed(companyModuleFunctions.permissionsAllowed, dto.accessRightsDTOs, companyModuleFunctions.functionId ,accessRightForm.dtoMap['roleId'])}">
                                            <c:if test="${permissionsAllowed.blank == true}">
                                                <td width="15%">
                                                </td>
                                            </c:if>
                                            <c:choose>
                                                <c:when test="${companyModuleFunctions.disabled == true}">
                                                    <c:if test="${permissionsAllowed.isChecked == true}">
                                                        <td width="15%">
                                                            <div class="radiocheck">
                                                                <div class="checkbox checkbox-default">
                                                                    <input
                                                                            id="box${moduleIndex.count}"
                                                                            type="checkbox"
                                                                            name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})"
                                                                            class="adminCheckBox"
                                                                            checked
                                                                            disabled="disabled"
                                                                            onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}, this)">
                                                                    <label>
                                                                        <fmt:message
                                                                                key="${permissionsAllowed.stringValue}"/>
                                                                    </label>
                                                                </div>
                                                            </div>
                                                            <html:hidden
                                                                    property="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})"
                                                                    value="true"
                                                                    styleId="styleCheckBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value}"/>
                                                        </td>
                                                    </c:if>
                                                    <c:if test="${permissionsAllowed.isChecked == false}">
                                                        <td width="15%">
                                                            <div class="radiocheck">
                                                                <div class="checkbox checkbox-default">
                                                                    <input
                                                                            id="box${moduleIndex.count}"
                                                                            type="checkbox"
                                                                            name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})"
                                                                            class="adminCheckBox"
                                                                            onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}, this)">
                                                                    <label>
                                                                        <fmt:message
                                                                                key="${permissionsAllowed.stringValue}"/>
                                                                    </label>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </c:if>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:if test="${permissionsAllowed.isChecked == true}">
                                                        <td width="15%">
                                                            <div class="radiocheck">
                                                                <div class="checkbox checkbox-default">
                                                                    <input
                                                                            id="box${moduleIndex.count}"
                                                                            type="checkbox"
                                                                            name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})"
                                                                            class="adminCheckBox"
                                                                            checked
                                                                            onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}, this)">
                                                                    <label>
                                                                        <fmt:message
                                                                                key="${permissionsAllowed.stringValue}"/>
                                                                    </label>
                                                                </div>
                                                            </div>

                                                        </td>
                                                    </c:if>
                                                    <c:if test="${permissionsAllowed.isChecked == false}">
                                                        <td width="15%">
                                                            <div class="radiocheck">
                                                                <div class="checkbox checkbox-default">
                                                                    <input
                                                                            id="box${moduleIndex.count}"
                                                                            type="checkbox"
                                                                            name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})"
                                                                            class="adminCheckBox"
                                                                            onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}, this)">
                                                                    <label>
                                                                        <fmt:message
                                                                                key="${permissionsAllowed.stringValue}"/>
                                                                    </label>
                                                                </div>
                                                            </div>

                                                        </td>
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="UPDATE" functionality="ACCESSRIGHT"
                             styleClass="button ${app2:getFormButtonClasses()}"
                             property="dto(save)">
            <c:out value="${save}"/>
        </app2:securitySubmit>
    </div>

</html:form>
<style type="text/css">
    .table > tbody > tr > td{
        vertical-align: super;
    }
</style>