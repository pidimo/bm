<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0">
    <tr>
        <td>
            <app2:checkAccessRight functionality="ACCOUNT" permission="CREATE">
                <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <TR>
                        <html:form action="/Account/Forward/Create.do">
                            <TD colspan="6" class="button">
                                <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
                            </TD>
                        </html:form>
                    </TR>
                </table>
            </app2:checkAccessRight>
            <TABLE border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
                <tr>
                    <td>
                        <fanta:table list="accountList" width="100%" id="account"
                                     action="Account/List.do"
                                     imgPath="${baselayout}">
                            <c:set var="editAction"
                                   value="Account/Forward/Update.do?dto(accountId)=${account.accountId}&dto(name)=${app2:encode(account.name)}"/>
                            <c:set var="deleteAction"
                                   value="Account/Forward/Delete.do?dto(withReferences)=true&dto(accountId)=${account.accountId}&dto(name)=${app2:encode(account.name)}"/>
                            <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                                <app2:checkAccessRight functionality="ACCOUNT" permission="VIEW">
                                    <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/edit.gif"/>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="ACCOUNT" permission="DELETE">
                                    <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/delete.gif"/>
                                </app2:checkAccessRight>
                            </fanta:columnGroup>

                            <fanta:dataColumn name="name"
                                              action="${editAction}" styleClass="listItem"
                                              title="Account.name" headerStyle="listHeader" width="65%"
                                              orderable="true" maxLength="25">
                            </fanta:dataColumn>
                            <fanta:dataColumn name="number"
                                              action="${editAction}" styleClass="listItem2"
                                              title="Account.number" headerStyle="listHeader" width="30%"
                                              orderable="true" maxLength="25">
                            </fanta:dataColumn>
                        </fanta:table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

