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

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0">
    <tr>
        <td>
            <html:form action="/ReportRole/List.do" styleId="mainForm">
                <html:hidden property="dto(reportId)" value="${param.reportId}"/>
                <table width="60%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <TR>
                        <TD class="button">
                            <app2:checkAccessRight functionality="REPORTROLE" permission="CREATE">
                                <html:button property="dto(add)" styleClass="button" onclick="javascript:jump(this);">
                                    <fmt:message key="Common.add"/>
                                </html:button>
                            </app2:checkAccessRight>
                            <app2:checkAccessRight functionality="REPORTROLE" permission="DELETE">
                                <html:submit property="dto(delete)" styleClass="button">
                                    <fmt:message key="Common.deleteSelected"/>
                                </html:submit>
                            </app2:checkAccessRight>
                        </TD>
                    </TR>
                </table>
                <TABLE border="0" cellpadding="0" cellspacing="0" width="60%" align="center">
                    <tr>
                        <td>
                            <fanta:table list="reportRoleList"
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
                                                              styleClass="radio listItemCenter"/>

                                        <fanta:actionColumn name="delete" title="Common.delete" action="${deleteAction}"
                                                            styleClass="listItem" headerStyle="listHeader"
                                                            image="${baselayout}/img/delete.gif"/>

                                    </fanta:columnGroup>
                                </app2:checkAccessRight>

                                <fanta:dataColumn name="roleName" styleClass="listItem" title="ReporRole.name"
                                                  headerStyle="listHeader" width="95%" orderable="true" maxLength="25"/>
                            </fanta:table>
                        </td>
                    </tr>
                </table>
            </html:form>
        </td>
    </tr>
</table>