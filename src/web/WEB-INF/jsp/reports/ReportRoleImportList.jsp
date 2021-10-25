<%@ include file="/Includes.jsp" %>

<script>
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
</script>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/ReportRole/RolesList.do" styleId="mainForm">
        <html:hidden property="dto(reportId)" value="${param.reportId}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

        <div class="${app2:getFormGroupClasses()}">
            <html:submit property="dto(addRoles)" styleClass="${app2:getFormButtonClasses()}">
                <fmt:message key="Common.add"/>
            </html:submit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="table-responsive">
            <fanta:table mode="bootstrap" styleClass="${app2:getFantabulousTableClases()}" list="roleList"
                         width="100%" id="role"
                         action="ReportRole/RolesList.do"
                         imgPath="${baselayout}" align="center" withCheckBox="true"
                         withContext="false">

                <fanta:checkBoxColumn styleClass="listItemCenter" name="role"
                                      id="selectedRoles" onClick="javascript:check();"
                                      property="roleId" headerStyle="listHeader"
                                      width="2%"/>
                <fanta:dataColumn name="roleName" styleClass="listItem2" title="ReporRole.name"
                                  headerStyle="listHeader" width="95%" orderable="true"/>
            </fanta:table>
        </div>

        <html:hidden property="name_${contact.checkBoxIdentifier}"
                     value="${contact.addressName1}" styleId="nameSId_${contact.checkBoxIdentifier}"/>

    </html:form>
</div>