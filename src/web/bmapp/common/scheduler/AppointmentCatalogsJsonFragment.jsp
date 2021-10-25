<%@ include file="/Includes.jsp" %>

<%
    pageContext.setAttribute("reminderTypeList", JSPHelper.getReminderTypeList(request));
    pageContext.setAttribute("intervalMinList", JSPHelper.getIntervalMin());
    pageContext.setAttribute("hourList", JSPHelper.getHours());
    pageContext.setAttribute("timeBeforeList", JSPHelper.getTimeBefore());
%>

<fanta:list listName="appointmentTypeList" module="/scheduler">
  <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
</fanta:list>

<fanta:list listName="selectPriorityList" module="/catalogs">
  <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
  <fanta:parameter field="type" value="SCHEDULER"/>
</fanta:list>


"appointmentTypeArray" : [
    <c:forEach var="item" items="${appointmentTypeList.result}" varStatus="statusVar">
      <c:if test="${statusVar.index > 0}">, </c:if>
      {
      "appointmentTypeId" : "${item.appointmentTypeId}",
      "name" : "${app2:escapeJSON(item.name)}"
      }
    </c:forEach>
    ],

"priorityArray" : [
    <c:forEach var="item" items="${selectPriorityList.result}" varStatus="statusVar">
      <c:if test="${statusVar.index > 0}">, </c:if>
      {
      "priorityId" : "${item.id}",
      "name" : "${app2:escapeJSON(item.name)}"
      }
    </c:forEach>
    ],

"reminderTypeArray" : [
    <c:forEach var="item" items="${reminderTypeList}" varStatus="statusVar">
      <c:if test="${statusVar.index > 0}">, </c:if>
      {
      "value" : "${item.value}",
      "label" : "${app2:escapeJSON(item.label)}"
      }
    </c:forEach>
    ],

"reminderTimeArray" : [
    <c:forEach var="item" items="${timeBeforeList}" varStatus="statusVar">
      <c:if test="${statusVar.index > 0}">, </c:if>
      {
      "value" : "${item.value}",
      "label" : "${app2:escapeJSON(item.label)}"
      }
    </c:forEach>
    ],

"hoursArray" : [
    <c:forEach var="item" items="${hourList}" varStatus="statusVar">
      <c:if test="${statusVar.index > 0}">, </c:if>
      {
      "value" : "${item.value}",
      "label" : "${app2:escapeJSON(item.label)}"
      }
    </c:forEach>
    ],

"minutesArray" : [
    <c:forEach var="item" items="${intervalMinList}" varStatus="statusVar">
      <c:if test="${statusVar.index > 0}">, </c:if>
      {
      "value" : "${item.value}",
      "label" : "${app2:escapeJSON(item.label)}"
      }
    </c:forEach>
    ]
