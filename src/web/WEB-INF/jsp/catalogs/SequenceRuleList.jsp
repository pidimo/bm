<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<fmt:message var="datePattern" key="datePattern"/>
<c:set var="customerType" value="<%=FinanceConstants.SequenceRuleType.CUSTOMER.getConstantAsString()%>"/>
<c:set var="articleType" value="<%=FinanceConstants.SequenceRuleType.ARTICLE.getConstantAsString()%>"/>
<c:set var="supportCaseType" value="<%=FinanceConstants.SequenceRuleType.SUPPORT_CASE.getConstantAsString()%>"/>
<c:set var="voucherType" value="<%=FinanceConstants.SequenceRuleType.VOUCHER.getConstantAsString()%>"/>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="SEQUENCERULE" permission="CREATE">
        <html:form action="/SequenceRule/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <c:set var="sequenceRuleTypeList" value="${app2:getSequenceRuleTypes(pageContext.request)}"/>
    <c:set var="sequenceRuleResetTypes" value="${app2:getSequeceRuleResetTypes(pageContext.request)}"/>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="sequenceRuleList"
                     width="100%"
                     id="sequenceRule"
                     styleClass="${app2:getFantabulousTableClases()}"
                     action="SequenceRule/List.do"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="SequenceRule/Forward/Update.do?dto(numberId)=${sequenceRule.numberId}&dto(label)=${app2:encode(sequenceRule.label)}"/>
            <c:set var="deleteAction"
                   value="/catalogs/SequenceRule/Forward/Delete.do?dto(withReferences)=true&dto(numberId)=${sequenceRule.numberId}&dto(label)=${app2:encode(sequenceRule.label)}"/>
            <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                <app2:checkAccessRight functionality="SEQUENCERULE" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="SEQUENCERULE" permission="DELETE">
                    <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader" render="false">
                        <c:choose>
                            <c:when test="${voucherType == sequenceRule.type}">
                                <app:link action="${deleteAction}" titleKey="Common.delete" contextRelative="true">
                                    <span class="${app2:getClassGlyphTrash()}"></span>
                                </app:link>
                            </c:when>
                            <c:otherwise>
                                &nbsp;
                            </c:otherwise>
                        </c:choose>
                    </fanta:actionColumn>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="label"
                              action="${editAction}"
                              styleClass="listItem"
                              title="SequenceRule.label" headerStyle="listHeader"
                              orderable="true"
                              maxLength="25"
                              width="25%"/>
            <fanta:dataColumn name="type"
                              styleClass="listItem"
                              title="SequenceRule.type" headerStyle="listHeader"
                              orderable="true" maxLength="25" renderData="false"
                              width="15%">
                ${app2:searchLabel(sequenceRuleTypeList, sequenceRule.type)}
            </fanta:dataColumn>
            <fanta:dataColumn name="format"
                              styleClass="listItem"
                              title="SequenceRule.format" headerStyle="listHeader"
                              orderable="true" maxLength="25"
                              width="25%"/>
            <fanta:dataColumn name="lastDate"
                              styleClass="listItem"
                              title="SequenceRule.lastDate" headerStyle="listHeader"
                              orderable="true" maxLength="25" renderData="false"
                              width="10%">
                <fmt:formatDate var="dateValue" value="${app2:intToDate(sequenceRule.lastDate)}"
                                pattern="${datePattern}"/>
                ${dateValue}
            </fanta:dataColumn>
            <fanta:dataColumn name="lastNumber"
                              styleClass="listItem"
                              title="SequenceRule.lastNumber" headerStyle="listHeader"
                              orderable="true" maxLength="25"
                              width="10%"/>
            <fanta:dataColumn name="resetType"
                              styleClass="listItem2"
                              title="SequenceRule.resetType" headerStyle="listHeader"
                              orderable="true" maxLength="25" renderData="false"
                              width="10%">
                ${app2:searchLabel(sequenceRuleResetTypes, sequenceRule.resetType)}
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>


