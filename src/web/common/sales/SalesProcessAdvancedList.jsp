<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<script>
    function myReset() {
        var form = document.advancedListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>
<br>
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="4">
            <fmt:message key="SalesProcess.Title.simpleSearch"/>
        </td>
    </tr>
    <html:form action="/AdvancedSearch.do" focus="parameter(processName)">
        <TR>
            <TD width="15%" class="label"><fmt:message key="SalesProcess.name"/></TD>
            <TD class="contain" width="35%">
                <html:text property="parameter(processName)" styleClass="largeText" tabindex="1"/>
            </TD>
            <TD width="15%" class="label"><fmt:message key="SalesProcess.employee"/></TD>
            <TD width="35%" class="contain">
                <fanta:select property="parameter(processEmployeeId)" listName="employeeBaseList" module="/contacts"
                              labelProperty="employeeName" valueProperty="employeeId" styleClass="mediumSelect"
                              firstEmpty="true" tabIndex="7">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </TD>
        </TR>
        <TR>
            <TD class="label"><fmt:message key="SalesProcess.value"/></TD>
            <TD class="contain">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:numberText property="parameter(valueFrom)" numberType="decimal" maxInt="10" maxFloat="2"
                                styleClass="numberText" tabindex="2"/> &nbsp;<fmt:message key="Common.to"/>&nbsp;
                <app:numberText property="parameter(valueTo)" numberType="decimal" maxInt="10" maxFloat="2"
                                styleClass="numberText" tabindex="3"/>
            </TD>
            <TD width="15%" class="label"><fmt:message key="SalesProcess.contact"/></TD>
            <TD width="35%" class="contain">
                <html:text property="parameter(contactSearchName)" styleClass="largeText" tabindex="8"/>
            </TD>
        </TR>
        <TR>
            <TD class="label"><fmt:message key="SalesProcess.priority"/></TD>
            <TD class="contain">
                <fanta:select property="parameter(priorityId)" listName="sProcessPriorityList"
                              labelProperty="name" valueProperty="id" styleClass="mediumSelect"
                              module="/sales" firstEmpty="true" tabIndex="4">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </TD>
            <TD width="15%" class="label"><fmt:message key="Product.name"/></TD>
            <TD width="35%" class="contain">
                <html:text property="parameter(productName)" styleClass="largeText" tabindex="9"/>
            </TD>
        </TR>
        <TR>
            <TD class="label"><fmt:message key="SalesProcess.status"/></TD>
            <TD class="contain">
                <fanta:select property="parameter(statusId)" listName="statusList"
                              labelProperty="statusName" valueProperty="statusId" styleClass="mediumSelect"
                              module="/sales" firstEmpty="true" tabIndex="5">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </TD>
            <TD width="15%" class="label"><fmt:message key="SalesProcess.endDate"/></TD>
            <TD class="contain">
                <fmt:message key="Common.from"/>
                &nbsp;
                <fmt:message key="datePattern" var="datePattern"/>
                <app:dateText property="parameter(endDateFrom)" maxlength="10" tabindex="10" styleId="startDate"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true"/>
                &nbsp;<fmt:message key="Common.to"/>&nbsp;
                <app:dateText property="parameter(endDateTo)" maxlength="10" tabindex="11" styleId="endDate"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true"/>


            </TD>
        </TR>
        <TR>
            <TD class="label"><fmt:message key="SalesProcess.actionNumber"/></TD>
            <TD class="contain" colspan="3">
                <html:text property="parameter(number)" styleClass="largeText" tabindex="6"/>
            </TD>
        </TR>

        <TR>
            <td colspan="4" align="center" class="alpha">
                <fanta:alphabet action="AdvancedSearch.do" parameterName="processNameAlpha"/>
            </td>
        </TR>
        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" tabindex="12"><fmt:message key="Common.go"/></html:submit>
                <html:button property="reset1" tabindex="12" styleClass="button" onclick="myReset()"><fmt:message
                        key="Common.clear"/></html:button>
                &nbsp;
            </td>
        </tr>
    </html:form>
    <TR>
        <td colspan="4" align="center">
            <br/>
            <app2:checkAccessRight functionality="SALESPROCESS" permission="CREATE">
                <c:set var="newButtonsTable" scope="page">
                    <tags:buttonsTable>
                        <app:url value="/SalesProcess/Forward/Create.do?advancedListForward=SalesProcessAdvancedSearch"
                                 addModuleParams="false" var="newSalesProcessUrl"/>
                        <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                               onclick="window.location ='${newSalesProcessUrl}'">
                    </tags:buttonsTable>
                </c:set>
            </app2:checkAccessRight>
            <c:out value="${newButtonsTable}" escapeXml="false"/>

            <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
            <fmt:message var="datePattern" key="datePattern"/>
            <fanta:table list="salesProcessAdvancedList" width="100%" id="process" action="AdvancedSearch.do"
                         imgPath="${baselayout}" align="center">
                <c:set var="editLink"
                       value="SalesProcess/Forward/Update.do?processId=${process.processId}&dto(processId)=${process.processId}&dto(processName)=${app2:encode(process.processName)}&addressId=${process.addressId}"/>
                <c:set var="deleteLink"
                       value="SalesProcess/Forward/Delete.do?processId=${process.processId}&dto(withReferences)=true&dto(processId)=${process.processId}&dto(processName)=${app2:encode(process.processName)}&addressId=${process.addressId}&dto(isAction)=0"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update" action="${editLink}" styleClass="listItem"
                                            headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="SALESPROCESS" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete" action="${deleteLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader" width="50%" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="processName" action="${editLink}" styleClass="listItem"
                                  title="SalesProcess.name"
                                  headerStyle="listHeader" width="15%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="addressName" styleClass="listItem" title="SalesProcess.contact"
                                  headerStyle="listHeader" width="15%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="employeeName" styleClass="listItem" title="SalesProcess.employee"
                                  headerStyle="listHeader" width="15%" orderable="true"/>
                <fanta:dataColumn name="priorityName" styleClass="listItem" title="SalesProcess.priority"
                                  headerStyle="listHeader" width="8%" orderable="true"/>
                <fanta:dataColumn name="probability" styleClass="listItemRight" title="SalesProcess.probability"
                                  headerStyle="listHeader" width="8%" orderable="true" renderData="false">
                    ${process.probability}&nbsp;<fmt:message key="Common.probabilitySymbol"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="startDate" styleClass="listItem" title="SalesProcess.startDate"
                                  headerStyle="listHeader" width="7%" orderable="true" renderData="false">
                    <fmt:formatDate var="dateValue" value="${app2:intToDate(process.startDate)}"
                                    pattern="${datePattern}"/>${dateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="endDate" styleClass="listItem" title="SalesProcess.endDate"
                                  headerStyle="listHeader"
                                  width="7%" orderable="true" renderData="false">
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

