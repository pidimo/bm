<%@ include file="/Includes.jsp" %>

<script>

    function select(id, name) {
        opener.selectField('user_key', id, 'user_name', name);
    }
</script>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="2">
            <fmt:message key="SalesProcess.Title.simpleSearch"/>
        </td>
    </tr>
    <TR>
        <td class="label"><fmt:message key="Common.search"/></td>
        <html:form action="/SalesProcess/SimpleSearchProcess.do" focus="parameter(processName)">
            <td class="contain" nowrap>
                <html:text property="parameter(processName)" styleClass="largeText"/>&nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
            </td>
        </html:form>
    </TR>
    <TR>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet
                    action="SalesProcess/SimpleSearchProcess.do"
                    parameterName="processNameAlpha"/>
        </td>
    </TR>
    <TR>
        <td colspan="2" align="center">
            <br/>
            <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
            <fmt:message var="datePattern" key="datePattern"/>
            <fanta:table width="100%" id="process" action="SalesProcess/SimpleSearchProcess.do" imgPath="${baselayout}"
                         align="center">
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <fanta:actionColumn name="" title="Common.select" useJScript="true"
                    action="javascript:select('${process.processId}', '${app2:jscriptEncode(process.processName)}');"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        image="${baselayout}/img/import.gif"/>
                </fanta:columnGroup>
                <fanta:dataColumn name="processName" styleClass="listItem"
                                  title="SalesProcess.name" headerStyle="listHeader" width="15%" orderable="true"
                                  maxLength="40"/>
                <fanta:dataColumn name="addressName" styleClass="listItem" title="SalesProcess.contact"
                                  headerStyle="listHeader" width="15%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="employeeName" styleClass="listItem" title="SalesProcess.employee"
                                  headerStyle="listHeader" width="15%" orderable="true"/>
                <fanta:dataColumn name="priorityName" styleClass="listItem" title="SalesProcess.priority"
                                  headerStyle="listHeader" width="10%" orderable="true"/>
                <fanta:dataColumn name="probability" styleClass="listItemRight" title="SalesProcess.probability"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    ${process.probability}&nbsp;<fmt:message key="Common.probabilitySymbol"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="startDate" styleClass="listItem" title="SalesProcess.startDate"
                                  headerStyle="listHeader" width="5%" orderable="true" renderData="false">
                    <fmt:formatDate  value="${app2:intToDate(process.startDate)}" pattern="${datePattern}"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="endDate" styleClass="listItem" title="SalesProcess.endDate"
                                  headerStyle="listHeader" width="5%" orderable="true" renderData="false">
                    <fmt:formatDate value="${app2:intToDate(process.date)}" pattern="${datePattern}"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="value" styleClass="listItemRight" title="SalesProcess.value"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                        <fmt:formatNumber  value="${process.value}" type="number" pattern="${numberFormat}"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="statusName" styleClass="listItem2" title="SalesProcess.status"
                                  headerStyle="listHeader" width="10%" orderable="true"/>
            </fanta:table>
        </TD>
    </tr>
</table>

