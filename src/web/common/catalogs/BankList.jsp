<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <TABLE id="BankList.jsp" border="0" cellpadding="3" cellspacing="0" width="80%" class="container"
                   align="center">
                <html:form action="/Bank/List.do">
                    <html:hidden property="listName" value="bankList"/>
                    <tr>
                        <td class="label"><fmt:message key="Bank.name"/></td>
                        <td align="left" class="contain">
                            <html:text property="parameter(bankName)" styleClass="largeText"/>
                        </td>
                        <td class="label"><fmt:message key="Bank.code"/></td>
                        <td align="left" class="contain">
                            <html:text property="parameter(bankCode)" styleClass="mediumText"/>
                            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
                        </td>
                    </tr>
                    <tr>

                    </tr>
                    <tr>
                        <td colspan="4" align="center" class="alpha">
                            <fanta:alphabet action="Bank/List.do" parameterName="bankNameAlpha"/>
                        </td>
                    </tr>
                </html:form>
            </table>

            <app2:checkAccessRight functionality="BANK" permission="CREATE">
                <TABLE id="BankList.jsp" border="0" cellpadding="2" cellspacing="0" width="80%" class="container"
                       align="center">
                    <TR>
                        <html:form styleId="CREATE_NEW_BANK" action="/Bank/Forward/Create.do?op=create">
                            <TD class="button">
                                <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>&nbsp;
                            </TD>
                        </html:form>
                    </TR>
                </table>
            </app2:checkAccessRight>


            <TABLE id="BankList.jsp" border="0" cellpadding="0" cellspacing="0" width="80%" class="container"
                   align="center">
                <tr>
                    <td align="center">
                        <fanta:table list="bankList" width="100%" id="bank" action="Bank/List.do"
                                     imgPath="${baselayout}">
                            <c:set var="editAction"
                                   value="Bank/Forward/Update.do?dto(bankId)=${bank.id}&dto(bankName)=${app2:encode(bank.name)}"/>
                            <c:set var="deleteAction"
                                   value="Bank/Forward/Delete.do?dto(withReferences)=true&dto(bankId)=${bank.id}&dto(bankName)=${app2:encode(bank.name)}"/>
                            <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                                <app2:checkAccessRight functionality="BANK" permission="VIEW">
                                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/edit.gif"/>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="BANK" permission="DELETE">
                                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/delete.gif"/>
                                </app2:checkAccessRight>
                            </fanta:columnGroup>
                            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="Bank.name"
                                              headerStyle="listHeader" width="35%" orderable="true" maxLength="25"/>
                            <fanta:dataColumn name="code" styleClass="listItem" title="Bank.code"
                                              headerStyle="listHeader" width="10%" orderable="true"/>
                            <fanta:dataColumn name="label" styleClass="listItem" title="Bank.label"
                                              headerStyle="listHeader" width="25%" orderable="true"/>
                            <fanta:dataColumn name="internationalCode" styleClass="listItem2"
                                              title="Bank.internationalCode" headerStyle="listHeader" width="30%"
                                              orderable="true" maxLength="18"/>
                        </fanta:table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</TABLE>
