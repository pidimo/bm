<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0">
    <tr>
        <td>
            <app2:checkAccessRight functionality="CONTRACTTYPE" permission="CREATE">
                <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <TR>

                        <html:form action="/ContractType/Forward/Create.do">
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
                        <fanta:table list="contractTypeList" width="100%" id="contractType"
                                     action="ContractType/List.do"
                                     imgPath="${baselayout}">
                            <c:set var="editAction"
                                   value="ContractType/Forward/Update.do?dto(contractTypeId)=${contractType.contractTypeId}&dto(name)=${app2:encode(contractType.name)}"/>
                            <c:set var="deleteAction"
                                   value="ContractType/Forward/Delete.do?dto(withReferences)=true&dto(contractTypeId)=${contractType.contractTypeId}&dto(name)=${app2:encode(contractType.name)}"/>
                            <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                                <app2:checkAccessRight functionality="CONTRACTTYPE" permission="VIEW">
                                    <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/edit.gif"/>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="CONTRACTTYPE" permission="DELETE">
                                    <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/delete.gif"/>
                                </app2:checkAccessRight>
                            </fanta:columnGroup>

                            <fanta:dataColumn name="name"
                                              action="${editAction}" styleClass="listItem"
                                              title="ContractType.name" headerStyle="listHeader" width="70%"
                                              orderable="true" maxLength="25">
                            </fanta:dataColumn>

                            <fanta:dataColumn name="tobeInvoiced" styleClass="listItem2"
                                              title="ContractType.tobeInvoiced"
                                              headerStyle="listHeader" width="25%" renderData="false">
                                <c:if test="${contractType.tobeInvoiced == '1'}">
                                    <img src='<c:url value="${sessionScope.layout}/img/check.gif"/>'/>&nbsp;
                                </c:if>
                            </fanta:dataColumn>
                        </fanta:table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

