<%@ include file="/Includes.jsp" %>
<script>
    function select(id, name) {
        parent.selectField('fieldEmployeeId_id', id, 'fieldEmployeeName_id', name);
    }
</script>
<div class="${app2:getListWrapperClasses()}">
    <html:form action="/User/Create/ImportEmployee.do" focus="parameter(name1@_name2)" styleClass="form-horizontal">
        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="fieldSearch_id">
                <fmt:message key="Common.search"/>
            </label>
            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(name1@_name2)"
                               styleClass="largeText ${app2:getFormInputClasses()}"
                               styleId="fieldSearch_id"
                               maxlength="40"/>
                    <span class="input-group-btn">
                        <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                                key="Common.go"/></html:submit>
                    </span>
                </div>
            </div>
        </div>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="User/Create/ImportEmployee.do" mode="bootstrap" parameterName="name1"/>
    </div>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="searchNotUserEmployeeList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%"
                     id="employee"
                     action="User/Create/ImportEmployee.do"
                     imgPath="${baselayout}"
                     align="center">
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
    </div>
</div>

