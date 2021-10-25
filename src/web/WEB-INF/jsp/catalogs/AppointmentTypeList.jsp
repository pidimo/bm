<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="${create}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="APPOINTMENTTYPE" permission="CREATE">
                <html:submit styleClass="${app2:getFormButtonClasses()}"><fmt:message key="Common.new"/></html:submit>
            </app2:checkAccessRight>
        </div>
    </html:form>

    <div class="table-responsive" id="AppointmentType.jsp">
        <fanta:table mode="bootstrap" list="appointmentTypeList"
                     width="100%"
                     styleClass="${app2:getFantabulousTableClases()}"
                     id="appointmentType" action="${action}"
                     imgPath="${baselayout}">

            <c:set var="editAction"
                   value="${edit}?dto(name)=${app2:encode(appointmentType.appointmentTypeName)}&dto(appointmentTypeId)=${appointmentType.appointmentTypeId}"/>
            <c:set var="deleteAction"
                   value="${delete}?dto(withReferences)=true&dto(name)=${app2:encode(appointmentType.appointmentTypeName)}&dto(appointmentTypeId)=${appointmentType.appointmentTypeId}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                <app2:checkAccessRight functionality="APPOINTMENTTYPE" permission="VIEW">
                    <fanta:actionColumn name=""
                                        title="Common.update"
                                        action="${editAction}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="APPOINTMENTTYPE" permission="DELETE">
                    <fanta:actionColumn name=""
                                        title="Common.delete"
                                        action="${deleteAction}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>

            </fanta:columnGroup>

            <fanta:dataColumn name="appointmentTypeName"
                              action="${editAction}"
                              styleClass="listItem"
                              title="AppointmentType.name"
                              headerStyle="listHeader"
                              width="85%"
                              orderable="true"
                              maxLength="30"/>

            <fanta:dataColumn name="appointmentTypeColor"
                              styleClass="listItem2Center"
                              headerStyle="listHeader"
                              title="AppointmentType.color"
                              renderData="false"
                              width="10%">

                <div style="background-color: ${appointmentType.appointmentTypeColor}">&nbsp;</div>

            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>