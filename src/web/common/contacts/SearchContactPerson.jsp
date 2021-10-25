<%@ include file="/Includes.jsp" %>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="2">
            <fmt:message key="ContactPerson.search"/>
        </td>
    </tr>
    <TR>
        <td class="label"><fmt:message key="Common.search"/></td>
        <html:form action="/ContactPerson/Search.do" focus="parameter(lastName@_firstName@_searchName)">
            <td class="contain" nowrap>
                <html:text property="parameter(lastName@_firstName@_searchName)" styleClass="largeText"/>&nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
            </td>
        </html:form>
    </TR>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="ContactPerson/Search.do" parameterName="lastName"/>
        </td>
    </tr>

    <td colspan="2" align="center">
        <br/>
        <fanta:table list="searchContactPersonList" width="100%" id="contactPerson" action="ContactPerson/Search.do"
                     imgPath="${baselayout}" align="center">
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <fanta:actionColumn name="" title="Common.select" useJScript="true"
                                    action="javascript:opener.selectField('fieldContactPersonId_id', '${contactPerson.contactPersonId}', 'fieldContactPersonName_id', '${app2:jscriptEncode(contactPerson.contactPersonName)}');"
                                    styleClass="listItem" headerStyle="listHeader" width="50%"
                                    image="${baselayout}/img/import.gif"/>
            </fanta:columnGroup>
            <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="ContactPerson.name"
                              headerStyle="listHeader" width="45%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="department" styleClass="listItem" title="ContactPerson.department"
                              headerStyle="listHeader" width="25%" orderable="true"/>
            <fanta:dataColumn name="function" styleClass="listItem2" title="ContactPerson.function"
                              headerStyle="listHeader" width="25%" orderable="true"/>
        </fanta:table>
    </td>
</table>

