<%@ include file="/Includes.jsp" %>
<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="PAYCONDITION" permission="CREATE">
        <html:form styleId="CREATE_NEW_PAYCONDITION" action="/PayCondition/Forward/Create.do?op=create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
    <div id="PayConditionList.jsp" class="table-responsive">
        <fanta:table mode="bootstrap" list="payConditionList" width="100%" id="payCondition"
                     styleClass="${app2:getFantabulousTableClases()}"
                     action="PayCondition/List.do"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="PayCondition/Forward/Update.do?dto(payConditionId)=${payCondition.id}&dto(payConditionName)=${app2:encode(payCondition.name)}"/>
            <c:set var="deleteAction"
                   value="PayCondition/Forward/Delete.do?dto(withReferences)=true&dto(payConditionId)=${payCondition.id}&dto(payConditionName)=${app2:encode(payCondition.name)}"/>
            <c:set var="translationAction"
                   value="PayCondition/Forward/translation.do?op=create&dto(payConditionId)=${payCondition.id}&dto(payConditionName)=${app2:encode(payCondition.name)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                <app2:checkAccessRight functionality="PAYCONDITION" permission="VIEW">
                    <fanta:actionColumn name="trans" title="Common.translate" action="${translationAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphRefresh()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="PAYCONDITION" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="PAYCONDITION" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem"
                              title="PayCondition.PayCondition" headerStyle="listHeader" width="30%"
                              orderable="true" maxLength="20"/>
            <fanta:dataColumn name="payDaysColumn" styleClass="listItem" title="PayCondition.PayDays"
                              headerStyle="listHeader" width="20%" orderable="true" style="text-align:right"/>
            <fanta:dataColumn name="payDaysDiscountColumn" styleClass="listItem"
                              title="PayCondition.payDaysDiscount" headerStyle="listHeader" width="30%"
                              orderable="true" style="text-align:right"/>
            <fanta:dataColumn name="discountColumn" styleClass="listItem2" title="PayCondition.Discount"
                              headerStyle="listHeader" width="20%" orderable="true" maxLength="18"
                              style="text-align:right" renderData="false">
                <c:set var="numberValue" value=""/>
                <c:if test="${payCondition.discountColumn != null}">
                    <fmt:formatNumber var="numberValue" value="${payCondition.discountColumn}" type="number"
                                      pattern="${numberFormat}"/>
                </c:if>
                ${numberValue}&nbsp;
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>

