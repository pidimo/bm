<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
   <td>
   <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
       <tr>
            <html:form action="${create}">
                <TD colspan="6" class="button">
                <app2:checkAccessRight functionality="APPOINTMENTTYPE" permission="CREATE">
                    <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
                </app2:checkAccessRight>
                </TD>
            </html:form>
       </tr>
   </table>

   <TABLE id="AppointmentType.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
   <tr align="center">
       <td>
       <fanta:table list="appointmentTypeList" width="100%" id="appointmentType" action="${action}" imgPath="${baselayout}">
            <c:set var="editAction" value="${edit}?dto(name)=${app2:encode(appointmentType.appointmentTypeName)}&dto(appointmentTypeId)=${appointmentType.appointmentTypeId}"/>
            <c:set var="deleteAction" value="${delete}?dto(withReferences)=true&dto(name)=${app2:encode(appointmentType.appointmentTypeName)}&dto(appointmentTypeId)=${appointmentType.appointmentTypeId}"/>
               <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
               <app2:checkAccessRight functionality="APPOINTMENTTYPE" permission="VIEW">
                 <fanta:actionColumn name="" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
               </app2:checkAccessRight>
               <app2:checkAccessRight functionality="APPOINTMENTTYPE" permission="DELETE">
                   <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
               </app2:checkAccessRight>
               </fanta:columnGroup>
               <fanta:dataColumn name="appointmentTypeName" action="${editAction}" styleClass="listItem" title="AppointmentType.name"  headerStyle="listHeader" width="85%" orderable="true" maxLength="30" />
               <fanta:dataColumn name="appointmentTypeColor" styleClass="listItem2Center" headerStyle="listHeader" title="AppointmentType.color" renderData="false" width="10%">
                    <table border="0" bgcolor="${appointmentType.appointmentTypeColor}" width="60%" align="center" >
                        <tr>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
               </fanta:dataColumn>
           </fanta:table>
       </td>
   </tr>
   </table>
   </td>
</tr>
</table>
