<%@ include file="/Includes.jsp" %>

<script>
    function check()
    {
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
</script>

<html:form action="/ReportRole/RolesList.do" styleId="mainForm">
    <html:hidden property="dto(reportId)" value="${param.reportId}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    
    <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td>
                <table width="60%" border="0" align="center" cellpadding="0" cellspacing="0" class="searchContainer">
                    <tr>
                        <td class="button">
                            <html:submit property="dto(addRoles)" styleClass="button">
                                <fmt:message key="Common.add"/>
                            </html:submit>
                            <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <fanta:table list="roleList" width="100%" id="role"
                                         action="ReportRole/RolesList.do"
                                         imgPath="${baselayout}" align="center" withCheckBox="true" withContext="false">

                                <fanta:checkBoxColumn styleClass="radio listItemCenter" name="role"
                                                      id="selectedRoles" onClick="javascript:check();"
                                                      property="roleId" headerStyle="listHeader"
                                                      width="2%"/>
                                <fanta:dataColumn name="roleName" styleClass="listItem2" title="ReporRole.name"
                                                  headerStyle="listHeader" width="95%" orderable="true"/>
                            </fanta:table>
                        </td>
                    </tr>
                </table>

            </td>
            <html:hidden property="name_${contact.checkBoxIdentifier}"
                         value="${contact.addressName1}" styleId="nameSId_${contact.checkBoxIdentifier}"/>
        </tr>
    </table>
</html:form>