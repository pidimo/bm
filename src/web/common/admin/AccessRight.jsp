<%@ include file="/Includes.jsp" %>


<script language="JavaScript">
<!--
function check(actual, value){
    var x = document.getElementById('accessRightForm');
    for(i=0; i<x.elements.length; i++){
        var e = x.elements[i];

        if(e.id == value){

                if(actual.checked){
                    if(e.disabled == false)
                        e.checked = true;
                }else{
                    if(e.disabled == false)
                        e.checked = true;
                }

        }
        actual.checked = true;
    }
}

function uncheck(actual, value){
    var x = document.getElementById('accessRightForm');
    for(i=0; i<x.elements.length; i++){
        var e = x.elements[i];

        if(e.id == value){

                if(actual.checked){
                    if(e.disabled == false)
                        e.checked = false;
                }else{
                    if(e.disabled == false)
                        e.checked = false;
                }

        }
        actual.checked=false;
    }
}

function dependences(moduleid, functionid, permissionValue, box){
    var x = document.getElementById('accessRightForm');
    var boxid = box.id;
    if(permissionValue != 1){
        var permissionViewArray = getPermission(moduleid, functionid, boxid);
        var permissionView = permissionViewArray[0];
        if(null != permissionView){
            permissionView.checked = true;
        }
    }else{
        var permissionArray = getPermission(moduleid, functionid, boxid);
        for(i in permissionArray){
            if(i != 0){
                var element = permissionArray[i];
                if(null != element)
                    element.checked = false;
            }
        }
    }
}

function getPermission(moduleid, functionid, id){


    var x = document.getElementById('accessRightForm');

    var vp = null;
    var cp = null;
    var ep = null;
    var dp = null;
    var xp = null;

    var permissionArray = new Array(vp, cp, ep, dp, xp);

    for(i=0; i<x.elements.length; i++){
        var e = x.elements[i];
        if(e.id != id)
            continue;

        if(e.name == 'dto(checkBox_'+moduleid+'_'+functionid+'_'+1+')'){
            vp = e;
            permissionArray[0] = vp;
        }
        if(e.name == 'dto(checkBox_'+moduleid+'_'+functionid+'_'+2+')'){
            cp = e;
            permissionArray[1] = cp;
        }
        if(e.name == 'dto(checkBox_'+moduleid+'_'+functionid+'_'+4+')'){
            ep = e;
            permissionArray[2] = ep;
        }
        if(e.name == 'dto(checkBox_'+moduleid+'_'+functionid+'_'+8+')'){
            dp = e;
            permissionArray[3] = dp;
        }
        if(e.name == 'dto(checkBox_'+moduleid+'_'+functionid+'_'+16+')'){
            xp = e;
            permissionArray[4] = xp;
        }

    }
    return permissionArray;
}

//-->
</script>



<html:form action="/Role/AccessRight/Save.do?roleName=${param.roleName}" styleId="accessRightForm" >

<html:hidden property="dto(version)"/>

<table cellSpacing=0 cellPadding=4 width="80%" border=0 align="center">
    <TR>
        <TD class="button">


    <app2:securitySubmit operation="UPDATE" functionality="ACCESSRIGHT" styleClass="button" property="dto(save)">
        <c:out value="${save}"/>
    </app2:securitySubmit>



        </TD>
    </TR>
</table>

<html:hidden property="dto(numberOfModules)" value="${accessRightForm.dtoMap['numberOfModules']}"/>
<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
<html:hidden property="dto(roleId)" value="${accessRightForm.dtoMap['roleId']}"/>
<table width="80%" cellpadding="0" cellspacing="0" class="container" align="center"  >


<c:forEach var="companyModule" items="${dto.companyModuleDTOs}"  varStatus="moduleIndex" >

<html:hidden property="dto(companyModuleId${moduleIndex.count})" value="${companyModule.moduleId}" styleId="styleCompanyModuleId${moduleIndex.count}"/>
<tr>
    <td class="title">
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
            <tr>
                <td class="title" width="92%">
                    <fmt:message    key="${companyModule.nameKey}"/>
                </td>
                <td width="8%" align="center">
                    <input type="checkbox" name="all" title="<fmt:message key="Common.selectAll"/>" class="adminCheckBox" style="width: 12px;	height: 12px;" onclick="check(this,'box${moduleIndex.count}')" checked/>
                    <input type="checkbox" name="all" title="<fmt:message key="Common.UnselectAll"/> " class="adminCheckBox" style="width: 12px;	height: 12px;" onclick="uncheck(this,'box${moduleIndex.count}')" />
                </td>
            </tr>
        </table>

    </td>
</tr>
    <c:forEach var="companyModuleFunctions" items="${companyModule.systemFunctionDTOs}" varStatus="functionIndex" >
    <html:hidden property="dto(moduleFunctionId${functionIndex.count})" value="${companyModuleFunctions.functionId}" styleId="styleModuleFunctionId${functionIndex.count}"/>

    <tr>
        <td>
        <table width="100%" cellpadding="0" cellspacing="0" border="0">
        <c:choose>
            <c:when test="${sessionScope.user.valueMap['isDefaultCompany'] == false}">
                <c:if test="${companyModuleFunctions.functionCode != 'COMPANY' && companyModuleFunctions.functionCode != 'APPLICATIONSIGNATURE' && companyModuleFunctions.functionCode != 'JRXMLREPORT'}">
                    <tr>
                        <td width="40%" class="adminLabel" style="padding: 0px 0px 0px 7px;"><fmt:message key="${companyModuleFunctions.nameKey}"/></td>
                        <td class="adminContain" style="padding: 0px 0px 0px 0px; border:0px;" width="60%" nowrap>
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr>
                        <c:forEach var="permissionsAllowed" items="${app2:checkPermissionAllowed(companyModuleFunctions.permissionsAllowed, dto.accessRightsDTOs, companyModuleFunctions.functionId ,accessRightForm.dtoMap['roleId'])}" >
                                <c:if test="${permissionsAllowed.blank == true}" >
                                    <td width="5%" class="contain" style="padding: 0px 0px 0px 0px;">&nbsp;&nbsp;&nbsp;</td>
                                    <td width="15%" class="contain" style="padding: 0px 0px 0px 0px;">&nbsp;&nbsp;&nbsp;</td>
                                </c:if>
                                <c:choose>
                                    <c:when test="${companyModuleFunctions.disabled == true}">
                                        <c:if test="${permissionsAllowed.isChecked == true}">
                                            <td width="5%" class="contain" style="padding: 0px 0px 0px 0px;"><input id="box${moduleIndex.count}"  type="checkbox" name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})" class="adminCheckBox"  checked  disabled="disabled" onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}, this)"></td>
                                            <td width="15%" class="contain" style="padding: 0px 0px 0px 0px;"><fmt:message key="${permissionsAllowed.stringValue}"/></td>
                                            <html:hidden property="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})" value="true" styleId="styleCheckBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}"/>
                                        </c:if>
                                        <c:if test="${permissionsAllowed.isChecked == false}">
                                            <td width="5%" class="contain" style="padding: 0px 0px 0px 0px;"><input id="box${moduleIndex.count}" type="checkbox" name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})" class="adminCheckBox" onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}), this"></td>
                                            <td width="15%" class="contain" style="padding: 0px 0px 0px 0px;"><fmt:message key="${permissionsAllowed.stringValue}"/></td>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${permissionsAllowed.isChecked == true}">
                                            <td width="5%" class="contain" style="padding: 0px 0px 0px 0px;"><input id="box${moduleIndex.count}"  type="checkbox" name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})" class="adminCheckBox"  checked onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}, this)" ></td>
                                            <td width="15%" class="contain" style="padding: 0px 0px 0px 0px;"><fmt:message key="${permissionsAllowed.stringValue}"/></td>
                                        </c:if>
                                        <c:if test="${permissionsAllowed.isChecked == false}">
                                            <td width="5%" class="contain" style="padding: 0px 0px 0px 0px;"><input id="box${moduleIndex.count}" type="checkbox" name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})" class="adminCheckBox" onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}, this)"></td>
                                            <td width="15%" class="contain" style="padding: 0px 0px 0px 0px;"><fmt:message key="${permissionsAllowed.stringValue}"/></td>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                        </c:forEach>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </c:if>
            </c:when>
            <c:otherwise>
                <tr>
                    <td width="40%" class="adminLabel" style="padding: 0px 0px 0px 7px;" ><fmt:message key="${companyModuleFunctions.nameKey}"/></td>
                    <td class="adminContain" style="padding: 0px 0px 0px 0px; border:0px;" width="60%" nowrap>
                        <table cellpadding="0" cellspacing="0" border="0" width="100%">
                            <tr>
                    <c:forEach var="permissionsAllowed" items="${app2:checkPermissionAllowed(companyModuleFunctions.permissionsAllowed, dto.accessRightsDTOs, companyModuleFunctions.functionId ,accessRightForm.dtoMap['roleId'])}" >
                            <c:if test="${permissionsAllowed.blank == true}" >
                                <td width="5%" class="contain" style="padding: 0px 0px 0px 0px;" >&nbsp;&nbsp;&nbsp;</td>
                                <td width="15%" class="contain" style="padding: 0px 0px 0px 0px;" >&nbsp;&nbsp;&nbsp;</td>
                            </c:if>
                            <c:choose>
                                    <c:when test="${companyModuleFunctions.disabled == true}">
                                        <c:if test="${permissionsAllowed.isChecked == true}">
                                            <td width="5%" class="contain" style="padding: 0px 0px 0px 0px;"><input id="box${moduleIndex.count}"  type="checkbox" name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})" class="adminCheckBox"  checked  disabled="disabled" onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}, this)" ></td>
                                            <td width="15%" class="contain" style="padding: 0px 0px 0px 0px;"><fmt:message key="${permissionsAllowed.stringValue}"/></td>
                                            <html:hidden property="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})" value="true" styleId="styleCheckBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value}"/>
                                        </c:if>
                                        <c:if test="${permissionsAllowed.isChecked == false}">
                                            <td width="5%" class="contain" style="padding: 0px 0px 0px 0px;"><input id="box${moduleIndex.count}" type="checkbox" name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})" class="adminCheckBox" onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}, this)"></td>
                                            <td width="15%" class="contain" style="padding: 0px 0px 0px 0px;"><fmt:message key="${permissionsAllowed.stringValue}"/></td>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${permissionsAllowed.isChecked == true}">
                                            <td width="5%" class="contain" style="padding: 0px 0px 0px 0px;"><input id="box${moduleIndex.count}"  type="checkbox" name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})" class="adminCheckBox"  checked onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}, this)"></td>
                                            <td width="15%" class="contain" style="padding: 0px 0px 0px 0px;"><fmt:message key="${permissionsAllowed.stringValue}"/></td>
                                        </c:if>
                                        <c:if test="${permissionsAllowed.isChecked == false}">
                                            <td width="5%" class="contain" style="padding: 0px 0px 0px 0px;"><input id="box${moduleIndex.count}" type="checkbox" name="dto(checkBox_${companyModule.moduleId}_${companyModuleFunctions.functionId}_${permissionsAllowed.value})" class="adminCheckBox" onclick="dependences(${companyModule.moduleId}, ${companyModuleFunctions.functionId}, ${permissionsAllowed.value}, this)"></td>
                                            <td width="15%" class="contain" style="padding: 0px 0px 0px 0px;"><fmt:message key="${permissionsAllowed.stringValue}"/></td>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                    </c:forEach>
                            </tr>
                        </table>
                    </td>
                </tr>
            </c:otherwise>
        </c:choose>
        </table>
        </td>
    </tr>
    </c:forEach>
</c:forEach>
</table>

<table cellSpacing=0 cellPadding=4 width="80%" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="UPDATE" functionality="ACCESSRIGHT" styleClass="button" property="dto(save)">
                <c:out value="${save}"/>
            </app2:securitySubmit>
        </TD>
    </TR>
</table>
</html:form>