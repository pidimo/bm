<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<fmt:message var="datePattern" key="datePattern"/>
<c:set var="customerType" value="<%=FinanceConstants.SequenceRuleType.CUSTOMER.getConstantAsString()%>"/>
<c:set var="articleType" value="<%=FinanceConstants.SequenceRuleType.ARTICLE.getConstantAsString()%>"/>
<c:set var="supportCaseType" value="<%=FinanceConstants.SequenceRuleType.SUPPORT_CASE.getConstantAsString()%>"/>
<c:set var="voucherType" value="<%=FinanceConstants.SequenceRuleType.VOUCHER.getConstantAsString()%>"/>

<app2:checkAccessRight functionality="SEQUENCERULE" permission="CREATE">
    <TABLE border="0" cellpadding="0" cellspacing="0" width="80%" class="container" align="center">
        <TR>
            <html:form action="/SequenceRule/Forward/Create.do">
                <TD class="button">
                    <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
                </TD>
            </html:form>
        </TR>
    </table>
</app2:checkAccessRight>

<TABLE border="0" cellpadding="0" cellspacing="0" width="80%" class="container" align="center">
    <tr>
        <td>
            <c:set var="sequenceRuleTypeList" value="${app2:getSequenceRuleTypes(pageContext.request)}"/>
            <c:set var="sequenceRuleResetTypes" value="${app2:getSequeceRuleResetTypes(pageContext.request)}"/>

            <fanta:table list="sequenceRuleList"
                         width="100%"
                         id="sequenceRule"
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
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="SEQUENCERULE" permission="DELETE">
                        <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                            styleClass="listItem" headerStyle="listHeader" render="false">
                            <c:choose>
                                <c:when test="${voucherType == sequenceRule.type}">
                                    <app:link action="${deleteAction}" contextRelative="true">
                                        <html:img src="${baselayout}/img/delete.gif"
                                                  titleKey="Common.delete" border="0"/>
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
        </td>
    </tr>
</table>

