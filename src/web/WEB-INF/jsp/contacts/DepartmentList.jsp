<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/contacts/contact.jsp"/>
<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="DEPARTMENT" permission="CREATE">
        <html:form styleId="CREATE_NEW_DEPARTMENT" action="/Organization/Department/Forward/Create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="departmentList" width="97%" id="department" action="Organization/Department/List.do"
                     imgPath="${baselayout}" align="center" styleClass="${app2:getFantabulousTableClases()}">
            <c:set var="editLink"
                   value="Organization/Department/Forward/Update.do?dto(departmentId)=${department.departmentId}&dto(name)=${app2:encode(department.name)}"/>
            <fanta:columnGroup headerStyle="listHeader" width="5%" title="Common.action">
                <app2:checkAccessRight functionality="DEPARTMENT" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem"
                                        headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="DEPARTMENT" permission="DELETE">
                    <fanta:actionColumn name="" title="Common.delete"
                                        action="Organization/Department/Forward/Delete.do?dto(withReferences)=true&dto(departmentId)=${department.departmentId}&dto(name)=${app2:encode(department.name)}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
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
    </div>
    <app2:checkAccessRight functionality="DEPARTMENT" permission="CREATE">
        <html:form styleId="CREATE_NEW_DEPARTMENT" action="/Organization/Department/Forward/Create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>
