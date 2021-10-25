<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">

    <html:form action="/Organization/Employee/List.do"
               focus="parameter(name1@_name2@_searchName)"
               styleClass="form-horizontal">
        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="name1@_name2@_searchName">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group col-xs-12">
                    <html:text property="parameter(name1@_name2@_searchName)"
                               styleClass="largeText form-control"
                               maxlength="40"
                               styleId="name1@_name2@_searchName"/>
                    <span class="input-group-btn">
                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                    </span>
                </div>
            </div>
        </div>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="Organization/Employee/List.do"
                            parameterName="name1Alpha"
                            mode="bootstrap"/>
        </div>

    </html:form>

    <app2:checkAccessRight functionality="EMPLOYEE" permission="CREATE">
        <html:form action="Organization/Employee/SearchContact.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Contact.Organization.Employee.addEmployee"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <fmt:message var="datePattern" key="datePattern"/>
    <app2:checkAccessRight functionality="EMPLOYEE" permission="VIEW" var="hasViewPermission"/>
    <app2:checkAccessRight functionality="EMPLOYEE" permission="DELETE" var="hasDeletePermission"/>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" width="97%"
                     id="employee"
                     action="Organization/Employee/List.do"
                     imgPath="${baselayout}"
                     align="center"
                     styleClass="${app2:getFantabulousTableClases()}">

            <c:set var="editLink"
                   value="Organization/Employee/Forward/Update.do?dto(employeeId)=${employee.employeeId}&dto(employeeName)=${app2:encode(employee.employeeName)}"/>
            <fanta:columnGroup headerStyle="listHeader" width="5%" title="Common.action">

                <c:if test="${hasViewPermission}">
                    <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem"
                                        headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </c:if>
                <c:if test="${hasDeletePermission}">
                    <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="50%">
                        <c:choose>
                            <c:when test="${employee.employeeId != sessionScope.user.valueMap['userAddressId']}">
                                <app:link
                                        page="/Organization/Employee/Forward/Delete.do?dto(employeeId)=${employee.employeeId}&dto(employeeName)=${app2:encode(employee.employeeName)}&dto(withReferences)=true&dto(userTypeValue)=1"
                                        titleKey="Common.delete">
                                    <span class="${app2:getClassGlyphTrash()}"></span>
                                </app:link>
                            </c:when>
                            <c:otherwise>
                                &nbsp;
                            </c:otherwise>
                        </c:choose>
                    </fanta:actionColumn>
                </c:if>
            </fanta:columnGroup>
            <fanta:dataColumn name="employeeName" action="${editLink}" maxLength="40" styleClass="listItem"
                              title="Employee.name" headerStyle="listHeader" width="36%" orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="officeName" styleClass="listItem" title="Employee.officeName"
                              headerStyle="listHeader" width="20%" orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="departmentName" styleClass="listItem" title="Employee.DepartmentName"
                              headerStyle="listHeader" width="20%" orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="hireDate" styleClass="listItem"
                              title="Contact.Organization.Employee.hireDate"
                              headerStyle="listHeader" width="12%" orderable="true" renderData="false">
                <fmt:formatDate value="${app2:intToDate(employee.hireDate)}" pattern="${datePattern}"/>&nbsp;
            </fanta:dataColumn>
            <fanta:dataColumn name="endDate" styleClass="listItem" title="Contact.Organization.Employee.endDate"
                              headerStyle="listHeader" width="12%" orderable="true" renderData="false">
                <fmt:formatDate value="${app2:intToDate(employee.endDate)}" pattern="${datePattern}"/>&nbsp;
            </fanta:dataColumn>
            <fanta:dataColumn name="" styleClass="listItem2Center" title="Common.active"
                              headerStyle="listHeader"
                              width="5%" renderData="false">
                <c:choose>
                    <c:when test="${empty employee.endDate || employee.endDate > app2:getCurrentDateAsInteger(pageContext.request)}">
                        <span class="${app2:getClassGlyphOk()}"></span>
                    </c:when>
                    <c:otherwise>
                        &nbsp;
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>
        </fanta:table>
    </div>
    <app2:checkAccessRight functionality="EMPLOYEE" permission="CREATE">
        <html:form styleId="CREATE_ADD_EMPLOYEE" action="/Organization/Employee/SearchContact.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Contact.Organization.Employee.addEmployee"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>