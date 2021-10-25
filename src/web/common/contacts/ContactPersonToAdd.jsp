<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/contacts/contact.jsp"/>

<table width="90%" border="0" align="center" cellspacing="0" cellpadding="3" class="container">
<tr>
<td colspan="4" class="title"><fmt:message key="ContactPerson.add"/></td>
</tr>

<html:form action="/ContactPerson/Add" focus="parameter(name1)">
<tr>
    <td width="15%" class="label"><fmt:message key="Contact.Person.lastname"/></td>
    <td width="35%" class="contain">
        <html:text property="parameter(name1)" styleClass="middleText" maxlength="40" tabindex="1" styleId="name1"/>
    </td>
    <td width="15%" class="label"><fmt:message key="Contact.Person.searchName"/></td>
    <td width="35%"  class="contain" >
        <html:text property="parameter(searchName)" styleClass="middleText" maxlength="40" tabindex="3" styleId="searchName"/>
    </td>
</tr>
<tr>
    <td  class="label"><fmt:message key="Contact.Person.firstname"/></td>
    <td class="contain" colspan="3">
        <html:text property="parameter(name2)" styleClass="middleText" maxlength="40" tabindex="2" styleId="name2"/>
    </td>
</tr>
<tr>
    <td class="button" colspan="4">
        <html:submit property="parameter(searchButton)" styleClass="button" tabindex="4" >
            <fmt:message  key="Common.search"/>
        </html:submit>
        <app2:checkAccessRight functionality="CONTACTPERSON" permission="CREATE">
            <html:submit property="parameter(newButton)" styleClass="button" tabindex="5">
                <fmt:message key="Common.new"/> 
            </html:submit>
        </app2:checkAccessRight>
        <html:cancel styleClass="button" tabindex="6"><fmt:message key="Common.cancel"/></html:cancel>
    </td>
</tr>
</html:form>
<tr>
    <td colspan="4">
    <br/>
    <fanta:table list="personSearchList_CPerson" width="100%" id="contact" action="ContactPerson/SearchContact.do" imgPath="${baselayout}" align="center">
        <app2:checkAccessRight functionality="CONTACTPERSON" permission="CREATE">
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <fanta:actionColumn name="import"
                                    action="ContactPerson/Import.do?dto(importAddress)=true&dto(addressIdToImport)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}"
                                    title="Common.import" styleClass="listItemCenter" headerStyle="listHeader"
                                    image="${baselayout}/img/import.gif"/>
            </fanta:columnGroup>
        </app2:checkAccessRight>
        <fanta:dataColumn name="personName" useJScript="true"
                          action="javascript:viewContactDetailInfo(1,${contact.addressId});" styleClass="listItem"
                          title="Contact.name" headerStyle="listHeader" width="50%" orderable="true" maxLength="40"/>
        <fanta:dataColumn name="countryCode" styleClass="listItem" title="Contact.countryCode"
                          headerStyle="listHeader" width="7%" orderable="true"/>
        <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip" headerStyle="listHeader"
                          width="7%" orderable="true"/>
        <fanta:dataColumn name="cityName" styleClass="listItem2" title="Contact.city" headerStyle="listHeader"
                          width="26%" orderable="true"/>
    </fanta:table>
    </td>
</tr>
</table>