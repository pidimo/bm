<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>


<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">

    <tr>
        <td>

            <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
                <TR>

                    <td class="label"><fmt:message key="Common.search"/></td>
                    <html:form action="/CompetitorProduct/List.do" focus="parameter(productName)">
                        <td class="contain" colspan="3" nowrap>
                            <html:text property="parameter(productName)" styleClass="largeText"/>&nbsp;
                            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
                        </td>
                    </html:form>
                </TR>

                <!-- choose alphbet to simple and advanced search  -->
                <tr>
                    <td colspan="4" align="center" class="alpha">
                        <fanta:alphabet action="CompetitorProduct/List.do" parameterName="productNameAlpha"/>
                    </td>
                </tr>
            </table>

            <br/>
            <app2:checkAccessRight functionality="COMPETITOR" permission="VIEW">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <html:form action="/CompetitorProduct/Forward/Create.do">
                            <td class="button">
                                <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
                            </td>
                        </html:form>
                    </tr>
                </table>
            </app2:checkAccessRight>
            <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
            <fanta:table list="competitorProductList" width="100%" id="competitorProduct"
                         action="CompetitorProduct/List.do" imgPath="${baselayout}">
                <c:set var="editAction"
                       value="CompetitorProduct/Forward/Update.do?dto(competitorProductId)=${competitorProduct.competitorProductId}&dto(productName)=${app2:encode(competitorProduct.productName)}&dto(operation)=update&dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(entryDateU)=${dateValue}"/>
                <c:set var="deleteAction"
                       value="CompetitorProduct/Forward/Delete.do?dto(withReferences)=true&dto(competitorProductId)=${competitorProduct.competitorProductId}&dto(productName)=${app2:encode(competitorProduct.productName)}&dto(entryDateU)=${dateValue}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                    <app2:checkAccessRight functionality="COMPETITORPRODUCT" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update" action="${editAction}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="COMPETITORPRODUCT" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" styleClass="listItem"
                                            headerStyle="listHeader" width="50%" action="${deleteAction}"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="productName" action="${editAction}" styleClass="listItem" title="Product.name"
                                  headerStyle="listHeader" width="32%" orderable="true" maxLength="25"/>
                <fanta:dataColumn name="competitorName" styleClass="listItem" title="Competitor.competitorName"
                                  headerStyle="listHeader" width="32%" orderable="true" maxLength="25"/>

                <fanta:dataColumn name="entryDate" styleClass="listItem" title="Competitor.entryDate"
                                  headerStyle="listHeader" width="15%" orderable="true" renderData="false">
                    <fmt:message var="datePattern" key="datePattern"/>
                    <fmt:formatDate var="dateValue" value="${app2:intToDate(competitorProduct.entryDate)}"
                                    pattern="${datePattern}"/>
                    ${dateValue}
                </fanta:dataColumn>

                <fanta:dataColumn name="price" styleClass="listItem2Right" title="Competitor.price"
                                  headerStyle="listHeader" width="11%" orderable="true" renderData="false">
                    <fmt:formatNumber var="numberValue" value="${competitorProduct.price}" type="number"
                                      pattern="${numberFormat}"/>
                    ${numberValue}
                </fanta:dataColumn>

            </fanta:table>
        </td>
    </tr>
</table>
