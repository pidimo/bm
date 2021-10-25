<%@ include file="/Includes.jsp" %>

<c:if test="${empty buttonsTabIndex}">
    <c:set var="buttonsTabIndex" value="40" scope="request"/>
</c:if>

<c:if test="${op == 'create' || op == 'update'}">
    <app2:securitySubmit operation="${op}"
                         functionality="JRXMLREPORT"
                         property="dto(save)"
                         styleId="saveButtonId"
                         tabindex="${buttonsTabIndex}"
                         styleClass="${app2:getFormButtonClasses()}">
        ${button}
    </app2:securitySubmit>
</c:if>
<c:if test="${op == 'delete'}">
    <app2:securitySubmit operation="${op}"
                         functionality="JRXMLREPORT"
                         property="dto(delete)"
                         styleId="saveButtonId"
                         tabindex="${buttonsTabIndex}"
                         styleClass="${app2:getFormButtonClasses()}">
        ${button}
    </app2:securitySubmit>
</c:if>
