<%@ include file="/Includes.jsp" %>
<script>
    function select(id, name) {
        opener.selectField('fieldEmployeeId_id', id, 'fieldEmployeeName_id', name);
    }
</script>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="2">
            <fmt:message key="Contact.Organization.User.searchEmployee"/>
        </td>
    </tr>
    <html:form action="/User/Create/ImportEmployee.do" focus="parameter(name1@_name2)">
        <TR>
            <td class="label" width="15%"><fmt:message key="Common.search"/></td>
            <td align="left" class="contain" width="85%">
                <html:text property="parameter(name1@_name2)" styleClass="largeText" maxlength="40"/>&nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
            </td>
        </TR>
    </html:form>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="User/Create/ImportEmployee.do" parameterName="name1"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" align="center"><br>
            <fanta:table list="searchNotUserEmployeeList" width="100%" id="employee"
                         action="User/Create/ImportEmployee.do" imgPath="${baselayout}" align="center">
                <fanta:columnGroup headerStyle="listHeader" width="5%" title="Common.action">
                    <fanta:actionColumn name="" title="Common.select"
                                        useJScript="true"
                                        action="javascript:select('${employee.employeeId}', '${app2:jscriptEncode(employee.employeeName)}');"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        image="${baselayout}/img/import.gif"/>
                </fanta:columnGroup>
                <fanta:dataColumn name="employeeName" maxLength="40" styleClass="listItem" title="Employee.name"
                                  headerStyle="listHeader" width="40%" orderable="true"/>
                <fanta:dataColumn name="officeName" styleClass="listItem" title="Employee.officeName"
                                  headerStyle="listHeader" width="30%" orderable="true"/>
                <fanta:dataColumn name="departmentName" styleClass="listItem2" title="Employee.DepartmentName"
                                  headerStyle="listHeader" width="30%" orderable="true"/>
            </fanta:table>
        </td>
    </tr>
</table>
