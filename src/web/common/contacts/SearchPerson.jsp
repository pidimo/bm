<%@ include file="/Includes.jsp"%>

<tags:jscript language="JavaScript" src="/js/contacts/contact.jsp"/>

<table width="90%" border="0" align="center" cellspacing="0" cellpadding="0">
  <tr>
    <td align="left">
    <html:form action="/ContactPerson/Add" focus="parameter(name1)">
     <table id="ContactPersonToAdd.jsp" border="0" cellpadding="0" cellspacing="1" width="97%" align="center" class="container">
        <tr >
          <td colspan="4" class="title"><fmt:message   key="ContactPerson.add"/></td>
        </tr>
        <tr>
          <td width="15%" class="label" ><fmt:message   key="Contact.Person.lastname"/></td>
          <td width="35%" class="contain">
             <html:text property="parameter(name1)" styleClass="middleText" maxlength="40"/>
          </td>
          <td width="15%" class="label" ><fmt:message   key="Contact.Person.firstname"/></td>
          <td width="35%" class="contain">
             <html:text property="parameter(name2)" styleClass="middleText" maxlength="40"/>
          </td>
        </tr>
        <tr>
          <td class="label" ><fmt:message   key="Contact.Person.searchName"/></td>
          <td class="contain" colspan="3">
             <html:text property="parameter(searchName)" styleClass="middleText" maxlength="40"/>
          </td>
        </tr>
      </table>
      <table width="97%" border="0" cellpadding="0" cellspacing="0">
            <tr >
              <td class="button">
                  <html:submit property="parameter(searchButton)" styleClass="button" tabindex="2" ><fmt:message   key="Common.search"/></html:submit>
                  <html:submit property="parameter(newButton)" styleClass="button" ><fmt:message key="Common.new"/></html:submit>
                  <html:cancel styleClass="button" ><fmt:message   key="Common.cancel"/></html:cancel>
              </td>
            </tr>
     </table>
    </html:form>

    <fanta:table list="personSearchList" width="97%" id="contact" action="ContactPerson/Add.do" imgPath="${baselayout}"  align="center">
             <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
             <fanta:actionColumn name="" action="ContactPerson/Import.do?dto(op)=&dto(addressIdToImport)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(importAddress)=true" title="Common.import" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/import.gif">
             </fanta:actionColumn>
            </fanta:columnGroup>
            <fanta:dataColumn name="personName" useJScript="true" action="javascript:viewContactDetailInfo(1,${contact.addressId});"  styleClass="listItem" title="Contact.name"  headerStyle="listHeader" width="60%" orderable="true" maxLength="40">
            </fanta:dataColumn>
            <fanta:dataColumn name="countryCode" styleClass="listItem" title="Contact.countryCode"  headerStyle="listHeader" width="7%" orderable="true" >
            </fanta:dataColumn>
            <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip"  headerStyle="listHeader" width="7%" orderable="true" >
            </fanta:dataColumn>
            <fanta:dataColumn name="cityName" styleClass="listItem" title="Contact.city"  headerStyle="listHeader" width="20%" orderable="true" >
            </fanta:dataColumn>
    </fanta:table>

    </td>
  </tr>

</table>