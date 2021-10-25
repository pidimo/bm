<%@ include file="/Includes.jsp" %>

<table width="90%" border="0" align="center" cellpadding="10" cellspacing="0">
  <tr>
    <td align="center"> <br>
        <app2:checkAccessRight functionality="OFFICE" permission="CREATE">
            <table width="97%" border="0" cellpadding="2" cellspacing="0">
               <tr>
                 <html:form styleId="CREATE_NEW_OFFICE" action="/Organization/Office/Forward/Create">
                  <td class="button"><!--Button create up -->
                     <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
                  </td>
                </html:form>
              </tr>
            </table>
        </app2:checkAccessRight>



       <fanta:table list="officeList" width="97%" id="office" action="Organization/Office/List.do" imgPath="${baselayout}" align="center">
        <c:set var="editLink" value="Organization/Office/Forward/Update.do?dto(officeId)=${office.officeId}&dto(name)=${app2:encode(office.name)}"/>
        <fanta:columnGroup headerStyle="listHeader" width="5%" title="Common.action">
            <app2:checkAccessRight functionality="OFFICE" permission="VIEW">
                <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem" headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="OFFICE" permission="DELETE">
                <fanta:actionColumn name="" title="Common.delete" action="Organization/Office/Forward/Delete.do?dto(officeId)=${office.officeId}&dto(name)=${app2:encode(office.name)}&dto(withReferences)=true" styleClass="listItem" headerStyle="listHeader" width="50%" image="${baselayout}/img/delete.gif"/>
            </app2:checkAccessRight>
        </fanta:columnGroup>
        <fanta:dataColumn name="name" action="${editLink}" styleClass="listItem" title="Office.name"  headerStyle="listHeader" width="50%" orderable="true">
        </fanta:dataColumn>
        <fanta:dataColumn name="supervisorName" styleClass="listItem" title="Office.supervisor"  headerStyle="listHeader" width="45%" orderable="true">
        </fanta:dataColumn>
    </fanta:table>

    <app2:checkAccessRight functionality="OFFICE" permission="CREATE">
        <table width="97%" border="0" cellpadding="2" cellspacing="0"> <!--Button create down -->
            <tr>
              <html:form styleId="CREATE_NEW_OFFICE" action="/Organization/Office/Forward/Create">
              <td class="button"><!--Button create up -->
                 <html:submit styleClass="button"><fmt:message   key="Common.new"/></html:submit>
              </td>
            </html:form>
            </tr>
        </table>
    </app2:checkAccessRight>

    </td>
  </tr>
</table>