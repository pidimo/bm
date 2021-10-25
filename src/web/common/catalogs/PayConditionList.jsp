<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
    <td>
<app2:checkAccessRight functionality="PAYCONDITION" permission="CREATE">
    <table width="70%" border="0" align="center" cellpadding="2" cellspacing="0">
    <TR>
    <html:form styleId="CREATE_NEW_PAYCONDITION" action="/PayCondition/Forward/Create.do?op=create">
            <TD colspan="6" class="button">
            <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
            </TD>
    </html:form>
    </TR>
    </table>
</app2:checkAccessRight>

<fmt:message   var="numberFormat" key="numberFormat.2DecimalPlaces"/>
    <TABLE id="PayConditionList.jsp" border="0" cellpadding="0" cellspacing="0" width="70%" class="container" align="center">
    <TR align="center">
    <td>
        <fanta:table list="payConditionList" width="100%" id="payCondition" action="PayCondition/List.do" imgPath="${baselayout}"  >
                <c:set var="editAction" value="PayCondition/Forward/Update.do?dto(payConditionId)=${payCondition.id}&dto(payConditionName)=${app2:encode(payCondition.name)}"/>
                <c:set var="deleteAction" value="PayCondition/Forward/Delete.do?dto(withReferences)=true&dto(payConditionId)=${payCondition.id}&dto(payConditionName)=${app2:encode(payCondition.name)}"/>
                <c:set var="translationAction" value="PayCondition/Forward/translation.do?op=create&dto(payConditionId)=${payCondition.id}&dto(payConditionName)=${app2:encode(payCondition.name)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="PAYCONDITION" permission="VIEW">
                        <fanta:actionColumn name="trans" title="Common.translate" action="${translationAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/tournicoti.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="PAYCONDITION" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="PAYCONDITION" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="PayCondition.PayCondition"  headerStyle="listHeader" width="30%" orderable="true" maxLength="20"/>
                <fanta:dataColumn name="payDaysColumn" styleClass="listItem" title="PayCondition.PayDays"  headerStyle="listHeader" width="20%" orderable="true" style="text-align:right"/>
                <fanta:dataColumn name="payDaysDiscountColumn" styleClass="listItem" title="PayCondition.payDaysDiscount"  headerStyle="listHeader" width="30%" orderable="true" style="text-align:right"/>
                <fanta:dataColumn name="discountColumn" styleClass="listItem2" title="PayCondition.Discount" headerStyle="listHeader" width="20%" orderable="true" maxLength="18" style="text-align:right" renderData="false">
                    <c:set var="numberValue" value=""/>
                    <c:if test="${payCondition.discountColumn != null}">
                        <fmt:formatNumber var="numberValue" value="${payCondition.discountColumn}" type="number" pattern="${numberFormat}" />
                    </c:if>
                    ${numberValue}&nbsp;
                </fanta:dataColumn>
            </fanta:table>
       </td>
    </tr>
    <tr>
        <td <c:out value="${sessionScope.listshadow}" escapeXml="false" />><img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
    </tr>
    </table>
    </td>
</tr>
</table>

