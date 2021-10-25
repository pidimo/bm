<%@ include file="/Includes.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="80%" class="container" align="center">
    <tr>
        <td>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<fmt:message   var="dateTimePattern" key="dateTimePattern"/>
<fanta:table width="100%" id="history" action="Article/HistoryList.do" imgPath="${baselayout}">
   <fanta:dataColumn name="logDateTime" styleClass="listItem${expireClass}"  title="History.date" headerStyle="listHeader" width="30%" orderable="true" renderData="false">
     ${app2:getDateWithTimeZone(history.logDateTime, timeZone, dateTimePattern)}
   </fanta:dataColumn>
   <fanta:dataColumn name="ownerName" styleClass="listItem" title="History.User"  headerStyle="listHeader" width="40%" orderable="true"/>
   <fanta:dataColumn name="action" styleClass="listItem2" title="History.action"
                      headerStyle="listHeader" width="30%" orderable="false" renderData="false" >
        <app:historyActionValues actionValue="${history.action}"/>        
   </fanta:dataColumn>
</fanta:table>
        </td>
    </tr>
</table>
