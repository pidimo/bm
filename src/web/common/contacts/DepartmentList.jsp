<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/contacts/contact.jsp"/>



<table width="75%" border="0" align="center" cellpadding="10" cellspacing="0">
    <tr>
        <td align="center"><br>
            <app2:checkAccessRight functionality="DEPARTMENT" permission="CREATE">
                <table width="97%" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <html:form styleId="CREATE_NEW_DEPARTMENT" action="/Organization/Department/Forward/Create">
                            <td class="button">
                                <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
                            </td>
                        </html:form>
                    </tr>
                </table>
            </app2:checkAccessRight>


            <fanta:table list="departmentList" width="97%" id="department" action="Organization/Department/List.do"
                         imgPath="${baselayout}" align="center">
                <c:set var="editLink"
                       value="Organization/Department/Forward/Update.do?dto(departmentId)=${department.departmentId}&dto(name)=${app2:encode(department.name)}"/>
                <fanta:columnGroup headerStyle="listHeader" width="5%" title="Common.action">
                    <app2:checkAccessRight functionality="DEPARTMENT" permission="VIEW">
                        <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem"
                                            headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="DEPARTMENT" permission="DELETE">
                        <fanta:actionColumn name="" title="Common.delete"
                                            action="Organization/Department/Forward/Delete.do?dto(withReferences)=true&dto(departmentId)=${department.departmentId}&dto(name)=${app2:encode(department.name)}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editLink}" styleClass="listItem" title="Department.name"
                                  headerStyle="listHeader" width="35%" orderable="true">
                </fanta:dataColumn>
                <fanta:dataColumn name="parentName" styleClass="listItem" title="Department.parentName"
                                  headerStyle="listHeader" width="30%" orderable="true">
                </fanta:dataColumn>
                <fanta:dataColumn name="managerName" styleClass="listItem2"
                                  title="Contact.Organization.Department.manager" headerStyle="listHeader" width="35%"
                                  orderable="true">
                </fanta:dataColumn>
            </fanta:table>

            <app2:checkAccessRight functionality="DEPARTMENT" permission="CREATE">
                <table width="97%" border="0" cellpadding="2" cellspacing="0"> <!--Button create down -->
                    <tr>
                        <html:form styleId="CREATE_NEW_DEPARTMENT" action="/Organization/Department/Forward/Create">
                            <td class="button"><!--Button create up -->
                                <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
                            </td>
                        </html:form>
                    </tr>
                </table>
            </app2:checkAccessRight>

        </td>
    </tr>
</table>
