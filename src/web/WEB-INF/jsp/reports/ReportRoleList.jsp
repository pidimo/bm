<%@ include file="/Includes.jsp" %>
<app:url var="addRole" value="/ReportRole/RolesList.do"/>
<script language="JavaScript">
    <!--
    function jump(obj) {
        window.location = '${addRole}';
    }

    function check() {
        var field = document.getElementById('mainForm').selectedRoles;
        var guia = document.getElementById('mainForm').role;

        var i;

        if (guia.checked) {
            for (i = 0; i < field.length; i++)
                field[i].checked = true;
        } else {
            for (i = 0; i < field.length; i++)
                field[i].checked = false;
        }
    }
    //-->
</script>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/ReportRole/List.do" styleId="mainForm">
        <html:hidden property="dto(reportId)" value="${param.reportId}"/>
        <div class="${app2:getFormGroupClasses()}">
            <app2:checkAccessRight functionality="REPORTROLE" permission="CREATE">
                <html:button property="dto(add)" styleClass="${app2:getFormButtonClasses()}"
                             onclick="javascript:jump(this);">
                    <fmt:message key="Common.add"/>
                </html:button>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="REPORTROLE" permission="DELETE">
                <html:submit property="dto(delete)" styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.deleteSelected"/>
                </html:submit>
            </app2:checkAccessRight>
        </div>

        <div class="table-responsive">
            <fanta:table mode="bootstrap" styleClass="${app2:getFantabulousTableClases()}"
                         list="reportRoleList"
                         width="100%"
                         id="reporRole"
                         action="ReportRole/List.do" imgPath="${baselayout}">
                <c:set var="deleteAction"
                       value="ReportRole/Forward/Delete.do?dto(op)=read&dto(roleId)=${reporRole.roleId}&dto(reportId)=${reporRole.reportId}"/>

                <app2:checkAccessRight functionality="REPORTROLE" permission="DELETE">
                    <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                        <fanta:checkBoxColumn name="role" id="selectedRoles"
                                              onClick="javascript:check();"
                                              property="roleId" headerStyle="listHeader"
                                              styleClass="listItemCenter"/>

                        <fanta:actionColumn name="delete" title="Common.delete" action="${deleteAction}"
                                            styleClass="listItem" headerStyle="listHeader" glyphiconClass="${app2:getClassGlyphTrash()}"/>

                    </fanta:columnGroup>
                </app2:checkAccessRight>

                <fanta:dataColumn name="roleName" styleClass="listItem" title="ReporRole.name"
                                  headerStyle="listHeader" width="95%" orderable="true" maxLength="25"/>
            </fanta:table>
        </div>
    </html:form>
</div>
