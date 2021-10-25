<%@ include file="/Includes.jsp" %>


<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <br>

            <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
                <TR>
                    <html:form action="/SalesProcess/List.do" focus="parameter(processName)">
                        <td class="label"><fmt:message key="Common.search"/></td>
                        <td align="left" class="contain">
                            <html:text property="parameter(processName)" styleClass="largeText" maxlength="80"/>
                            &nbsp;
                            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
                        </td>
                    </html:form>
                </TR>
                <tr>
                    <td colspan="2" align="center" class="alpha">
                        <fanta:alphabet action="SalesProcess/List.do" parameterName="processNameAlpha"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>


    <tr>
        <td>
            <br>
            <app2:checkAccessRight functionality="SALESPROCESS" permission="CREATE">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <html:form action="/SalesProcess/Forward/Create.do">
                            <td class="button">
                                <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
                            </td>
                        </html:form>
                    </tr>
                </table>
            </app2:checkAccessRight>


            <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
            <fmt:message var="datePattern" key="datePattern"/>
            <fanta:table list="salesProcessList" width="100%" id="process" action="SalesProcess/List.do"
                         imgPath="${baselayout}" align="center">

                <%-- edit url to redirect to sales module--%>
                <c:set var="editLink"
                       value="sales/SalesProcess/Forward/Update.do?processId=${process.processId}&dto(processId)=${process.processId}&dto(processName)=${app2:encode(process.processName)}&addressId=${process.addressId}&index=0"/>
                <c:set var="deleteLink"
                       value="SalesProcess/Forward/Delete.do?processId=${process.processId}&dto(withReferences)=true&dto(processId)=${process.processId}&dto(processName)=${app2:encode(process.processName)}&dto(isAction)=0"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update" styleClass="listItem" render="false"
                                            headerStyle="listHeader" width="50%" >
                            <app:link action="${editLink}" addModuleName="false" contextRelative="true">
                                <html:img src="${baselayout}/img/edit.gif" titleKey="Common.update" border="0"/>
                            </app:link>
                        </fanta:actionColumn>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="SALESPROCESS" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete" action="${deleteLink}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="processName" styleClass="listItem" renderData="false"
                                  title="SalesProcess.name" headerStyle="listHeader" width="20%" orderable="true" maxLength="40">
                    <app:link action="${editLink}" addModuleName="false" contextRelative="true">
                        <c:out value="${process.processName}"/>
                    </app:link>
                </fanta:dataColumn>
                <fanta:dataColumn name="employeeName" styleClass="listItem" title="SalesProcess.employee"
                                  headerStyle="listHeader" width="20%" orderable="true"/>
                <fanta:dataColumn name="priorityName" styleClass="listItem" title="SalesProcess.priority"
                                  headerStyle="listHeader" width="10%" orderable="true"/>
                <fanta:dataColumn name="probability" styleClass="listItemRight" title="SalesProcess.probability"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    ${process.probability}&nbsp;<fmt:message key="Common.probabilitySymbol"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="startDate" styleClass="listItem" title="SalesProcess.startDate"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatDate var="dateValue" value="${app2:intToDate(process.startDate)}"
                                    pattern="${datePattern}"/>${dateValue}&nbsp;
                </fanta:dataColumn>
                <fanta:dataColumn name="endDate" styleClass="listItem" title="SalesProcess.endDate"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatDate var="dateValue" value="${app2:intToDate(process.endDate)}"
                                    pattern="${datePattern}"/>
                    ${dateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="value" styleClass="listItemRight" title="SalesProcess.value"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatNumber var="numberValue" value="${process.value}" type="number"
                                      pattern="${numberFormat}"/>
                    ${numberValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="statusName" styleClass="listItem2" title="SalesProcess.status"
                                  headerStyle="listHeader" width="10%" orderable="true"/>
            </fanta:table>

            <app2:checkAccessRight functionality="SALESPROCESS" permission="CREATE">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <html:form action="/SalesProcess/Forward/Create.do">
                            <td class="button">
                                <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
                            </td>
                        </html:form>
                    </tr>
                </table>
            </app2:checkAccessRight>

        </td>

    </tr>
</table>

