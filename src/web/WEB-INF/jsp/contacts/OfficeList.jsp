<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="OFFICE" permission="CREATE">
        <html:form styleId="CREATE_NEW_OFFICE" action="/Organization/Office/Forward/Create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="officeList" styleClass="${app2:getFantabulousTableClases()}" width="100%" id="office"
                     action="Organization/Office/List.do" imgPath="${baselayout}">
            <c:set var="editLink"
                   value="Organization/Office/Forward/Update.do?dto(officeId)=${office.officeId}&dto(name)=${app2:encode(office.name)}"/>
            <fanta:columnGroup headerStyle="listHeader" width="5%" title="Common.action">
                <app2:checkAccessRight functionality="OFFICE" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem"
                                        headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="OFFICE" permission="DELETE">
                    <fanta:actionColumn name="" title="Common.delete"
                                        action="Organization/Office/Forward/Delete.do?dto(officeId)=${office.officeId}&dto(name)=${app2:encode(office.name)}&dto(withReferences)=true"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editLink}" styleClass="listItem" title="Office.name"
                              headerStyle="listHeader" width="50%" orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="supervisorName" styleClass="listItem" title="Office.supervisor"
                              headerStyle="listHeader"
                              width="45%" orderable="true">
            </fanta:dataColumn>
        </fanta:table>
    </div>
    <app2:checkAccessRight functionality="OFFICE" permission="CREATE"> <!--Button create down -->
        <html:form styleId="CREATE_NEW_OFFICE" action="/Organization/Office/Forward/Create">
            <div class="${app2:getFormButtonWrapperClasses()}"><!--Button create up -->
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>