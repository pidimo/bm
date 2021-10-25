<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <div class="table-responsive">
        <c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
        <fmt:message var="dateTimePattern" key="dateTimePattern"/>
        <fanta:table mode="bootstrap" styleClass="${app2:getFantabulousTableClases()}"
                     width="100%" id="history" action="Article/HistoryList.do" imgPath="${baselayout}">
            <fanta:dataColumn name="logDateTime" styleClass="listItem${expireClass}" title="History.date"
                              headerStyle="listHeader" width="30%" orderable="true" renderData="false">
                ${app2:getDateWithTimeZone(history.logDateTime, timeZone, dateTimePattern)}
            </fanta:dataColumn>
            <fanta:dataColumn name="ownerName" styleClass="listItem" title="History.User"
                              headerStyle="listHeader"
                              width="40%" orderable="true"/>
            <fanta:dataColumn name="action" styleClass="listItem2" title="History.action"
                              headerStyle="listHeader" width="30%" orderable="false" renderData="false">
                <app:historyActionValues actionValue="${history.action}"/>
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>
