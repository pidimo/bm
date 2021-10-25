<%@ include file="/Includes.jsp" %>

<br>
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="2">
            <fmt:message key="SalesProcess.Title.simpleSearch"/>
            <br>
        </td>
    </tr>
    <TR>
        <td class="label"><fmt:message key="Common.search"/></td>
        <html:form action="/SalesProcess/List.do" focus="parameter(processName)">
            <td class="contain" nowrap>
                <html:text property="parameter(processName)" styleClass="largeText"/>&nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
                <app:link action="/AdvancedSearch.do?advancedListForward=SalesProcessAdvancedSearch">&nbsp;<fmt:message key="Common.advancedSearch"/>
                </app:link>
            </td>
        </html:form>
    </TR>
    <TR>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="SalesProcess/List.do" parameterName="processNameAlpha"/>
        </td>
    </TR>
    <TR>
        <td colspan="2" align="center">
            <br/>
            <app2:checkAccessRight functionality="SALESPROCESS" permission="CREATE">
                <c:set var="newButtonsTable" scope="page">
                    <tags:buttonsTable>
                        <app:url value="/SalesProcess/Forward/Create.do"
                                 addModuleParams="false" var="newSalesProcessUrl"/>
                        <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                               onclick="window.location ='${newSalesProcessUrl}'">
                    </tags:buttonsTable>
                </c:set>
            </app2:checkAccessRight>
            <c:out value="${newButtonsTable}" escapeXml="false"/>

            <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
            <fmt:message var="datePattern" key="datePattern"/>
            <fanta:table list="salesProcessList" width="100%" id="process" action="SalesProcess/List.do"
                         imgPath="${baselayout}" align="center">
                <c:set var="editLink"
                       value="SalesProcess/Forward/Update.do?processId=${process.processId}&dto(processId)=${process.processId}&dto(processName)=${app2:encode(process.processName)}&addressId=${process.addressId}&index=0"/>
                <c:set var="deleteLink"
                       value="SalesProcess/Forward/Delete.do?processId=${process.processId}&dto(withReferences)=true&dto(processId)=${process.processId}&dto(processName)=${app2:encode(process.processName)}&addressId=${process.addressId}&dto(isAction)=0&index=0"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update" action="${editLink}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="SALESPROCESS" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete" action="${deleteLink}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="processName" action="${editLink}" styleClass="listItem"
                                  title="SalesProcess.name" headerStyle="listHeader" width="20%"
                                  orderable="true" maxLength="40"/>
                <fanta:dataColumn name="addressName" styleClass="listItem" title="SalesProcess.contact"
                                  headerStyle="listHeader" width="15%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="employeeName" styleClass="listItem" title="SalesProcess.employee"
                                  headerStyle="listHeader" width="15%" orderable="true"/>
                <fanta:dataColumn name="priorityName" styleClass="listItem" title="SalesProcess.priority"
                                  headerStyle="listHeader" width="8%" orderable="true"/>
                <fanta:dataColumn name="probability" styleClass="listItemRight"
                                  title="SalesProcess.probability" headerStyle="listHeader" width="8%"
                                  orderable="true" renderData="false" nowrap="nowrap">
                    ${process.probability}&nbsp;<fmt:message key="Common.probabilitySymbol"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="startDate" styleClass="listItem" title="SalesProcess.startDate"
                                  headerStyle="listHeader" width="7%" orderable="true" renderData="false"
                                  nowrap="nowrap">
                    <fmt:formatDate var="dateValue" value="${app2:intToDate(process.startDate)}"
                                    pattern="${datePattern}"/>${dateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="endDate" styleClass="listItem" title="SalesProcess.endDate"
                                  headerStyle="listHeader" width="7%" orderable="true" renderData="false"
                                  nowrap="nowrap">
                    <fmt:formatDate value="${app2:intToDate(process.endDate)}" pattern="${datePattern}"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="value" styleClass="listItemRight" title="SalesProcess.value"
                                  headerStyle="listHeader" width="8%" orderable="true" renderData="false">
                    <fmt:formatNumber value="${process.value}" type="number" pattern="${numberFormat}"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="statusName" styleClass="listItem2" title="SalesProcess.status"
                                  headerStyle="listHeader" width="7%" orderable="true"/>
            </fanta:table>
            <c:out value="${newButtonsTable}" escapeXml="false"/>
        </TD>
    </tr>
</table>

