<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/contacts/contact.jsp"/>


<table width="90%" border="0" align="center" cellspacing="0" cellpadding="10">
    <tr>
        <td align="left">
            <html:form action="Organization/Employee/SearchContact" focus="parameter(lastName)">
                <table border="0" cellpadding="3" cellspacing="0" width="97%" align="center" class="container">
                    <tr>
                        <td colspan="4" class="title"><c:out value="${title}"/></td>
                    </tr>
                    <tr>
                        <td width="15%" class="label"><fmt:message key="Contact.Person.lastname"/></td>
                        <td width="35%" class="contain">
                            <html:text property="parameter(lastName)" styleClass="middleText" maxlength="40"
                                       tabindex="1"/>
                        </td>

                        <td width="15%" class="label"><fmt:message key="Contact.Person.searchName"/></td>
                        <td width="35%" class="contain">
                            <html:text property="parameter(searchName)" styleClass="middleText" maxlength="60"
                                       tabindex="3"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label"><fmt:message key="Contact.Person.firstname"/></td>
                        <td class="contain" colspan="3">
                            <html:text property="parameter(firstName)" styleClass="middleText" maxlength="40"
                                       tabindex="2"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="button" colspan="4">
                            <html:submit property="parameter(searchButton)" styleClass="button" tabindex="4">
                                <fmt:message key="Common.search"/></html:submit>
                            <html:cancel styleClass="button" tabindex="5"><fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </td>
                    </tr>
                </table>
            </html:form>

            <fanta:table list="searchNotContactPersonList" width="97%" id="contactPerson"
                         action="Organization/Employee/SearchContact.do" imgPath="${baselayout}" align="center">
                <app2:checkAccessRight functionality="EMPLOYEE" permission="CREATE">
                    <fanta:columnGroup headerStyle="listHeader" width="5%" title="Common.action">
                        <fanta:actionColumn name=""
                                            action="Employee/Import.do?dto(employeeAddresId)=${contactPerson.contactPersonId}&dto(name1)=${app2:encode(contactPerson.lastName)}&dto(name2)=${app2:encode(contactPerson.firstName)}"
                                            title="Common.import" styleClass="listItemCenter" headerStyle="listHeader"
                                            image="${baselayout}/img/import.gif"/>
                    </fanta:columnGroup>
                </app2:checkAccessRight>
                <fanta:dataColumn name="contactPersonName" useJScript="true"
                                  action="javascript:viewContactDetailInfo(1,${contactPerson.contactPersonId});"
                                  styleClass="listItem" title="ContactPerson.name" headerStyle="listHeader" width="45%"
                                  orderable="true" maxLength="40"/>
                <fanta:dataColumn name="department" styleClass="listItem" title="ContactPerson.department"
                                  headerStyle="listHeader" width="25%" orderable="true"/>
                <fanta:dataColumn name="function" styleClass="listItem2" title="ContactPerson.function"
                                  headerStyle="listHeader" width="25%" orderable="true"/>
            </fanta:table>
        </td>
    </tr>
</table>