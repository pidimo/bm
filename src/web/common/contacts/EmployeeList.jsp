<%@ include file="/Includes.jsp" %>

<table width="90%" border="0" align="center" cellpadding="10" cellspacing="0">
    <tr>
        <td align="center"><br>
            <table width="97%" border="0" cellpadding="2" cellspacing="0">
                <tr>
                    <td colspan="2">

                        <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0"
                               class="searchContainer">

                            <TR>
                                <html:form action="/Organization/Employee/List.do"
                                           focus="parameter(name1@_name2@_searchName)">
                                    <td class="label" width="15%"><fmt:message key="Common.search"/></td>
                                    <td align="left" class="contain" width="100%">
                                        <html:text property="parameter(name1@_name2@_searchName)" styleClass="largeText"
                                                   maxlength="40"/>
                                        &nbsp;
                                        <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
                                        &nbsp;
                                    </td>
                                </html:form>
                            </TR>

                            <tr>
                                <td colspan="3" align="center" class="alpha">
                                    <fanta:alphabet action="Organization/Employee/List.do" parameterName="name1Alpha"/>
                                </td>
                            </tr>
                        </table>
                        <br>
                    </td>
                </tr>
                <app2:checkAccessRight functionality="EMPLOYEE" permission="CREATE">
                    <tr>
                        <html:form action="Organization/Employee/SearchContact.do">
                            <td class="button">
                                <html:submit styleClass="button"><fmt:message
                                        key="Contact.Organization.Employee.addEmployee"/></html:submit>
                            </td>
                        </html:form>
                    </tr>
                </app2:checkAccessRight>

            </table>
            <fmt:message var="datePattern" key="datePattern"/>
            <app2:checkAccessRight functionality="EMPLOYEE" permission="VIEW" var="hasViewPermission"/>
            <app2:checkAccessRight functionality="EMPLOYEE" permission="DELETE" var="hasDeletePermission"/>

            <fanta:table width="97%" id="employee" action="Organization/Employee/List.do"
                         imgPath="${baselayout}" align="center">
                <c:set var="editLink"
                       value="Organization/Employee/Forward/Update.do?dto(employeeId)=${employee.employeeId}&dto(employeeName)=${app2:encode(employee.employeeName)}"/>
                <fanta:columnGroup headerStyle="listHeader" width="5%" title="Common.action">

                    <c:if test="${hasViewPermission}">
                        <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem"
                                            headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif"/>
                    </c:if>
                    <c:if test="${hasDeletePermission}">
                        <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="50%">
                            <c:choose>
                                <c:when test="${employee.employeeId != sessionScope.user.valueMap['userAddressId']}">
                                    <app:link
                                            page="/Organization/Employee/Forward/Delete.do?dto(employeeId)=${employee.employeeId}&dto(employeeName)=${app2:encode(employee.employeeName)}&dto(withReferences)=true&dto(userTypeValue)=1"
                                            titleKey="Common.delete">
                                        <html:img src="${baselayout}/img/delete.gif" altKey="Common.delete" border="0"/>
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
                <fanta:dataColumn name="hireDate" styleClass="listItem" title="Contact.Organization.Employee.hireDate"
                                  headerStyle="listHeader" width="12%" orderable="true" renderData="false">
                    <fmt:formatDate value="${app2:intToDate(employee.hireDate)}" pattern="${datePattern}"/>&nbsp;
                </fanta:dataColumn>
                <fanta:dataColumn name="endDate" styleClass="listItem" title="Contact.Organization.Employee.endDate"
                                  headerStyle="listHeader" width="12%" orderable="true" renderData="false">
                    <fmt:formatDate value="${app2:intToDate(employee.endDate)}" pattern="${datePattern}"/>&nbsp;
                </fanta:dataColumn>
                <fanta:dataColumn name="" styleClass="listItem2Center" title="Common.active" headerStyle="listHeader"
                                  width="5%" renderData="false">
                    <c:choose>
                        <c:when test="${empty employee.endDate || employee.endDate > app2:getCurrentDateAsInteger(pageContext.request)}">
                            <img align="middle" src="<c:url value="/layout/ui/img/check.gif"/>" alt=""/>
                        </c:when>
                        <c:otherwise>
                            &nbsp;
                        </c:otherwise>
                    </c:choose>
                </fanta:dataColumn>
            </fanta:table>

            <app2:checkAccessRight functionality="EMPLOYEE" permission="CREATE">
                <table width="97%" border="0" cellpadding="2" cellspacing="0"> <!--Button create down -->
                    <tr>
                        <html:form styleId="CREATE_ADD_EMPLOYEE" action="/Organization/Employee/SearchContact.do">
                            <td class="button"><!--Button create up -->
                                <html:submit styleClass="button"><fmt:message
                                        key="Contact.Organization.Employee.addEmployee"/></html:submit>
                            </td>
                        </html:form>
                    </tr>
                </table>
            </app2:checkAccessRight>

        </td>
    </tr>
</table>
