<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>


<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<div class="${app2:getListWrapperClasses()}">
    <html:form action="/CompetitorProduct/List.do" focus="parameter(productName)" styleClass="form-horizontal">
        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="fieldImput_id">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(productName)" styleClass="largeText ${app2:getFormInputClasses()}" styleId="fieldImput_id"/>
                                <span class="input-group-btn">
                                    <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                                            key="Common.go"/></html:submit>
                                </span>
                </div>
            </div>
        </div>
    </html:form>


    <!-- choose alphbet to simple and advanced search -->
    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="CompetitorProduct/List.do" mode="bootstrap" parameterName="productNameAlpha"/>
    </div>

    <app2:checkAccessRight functionality="COMPETITOR" permission="VIEW">

        <html:form action="/CompetitorProduct/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                        key="Common.new"/></html:submit>
            </div>
        </html:form>

    </app2:checkAccessRight>
    <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="competitorProductList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%" id="competitorProduct"
                     action="CompetitorProduct/List.do"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="CompetitorProduct/Forward/Update.do?dto(competitorProductId)=${competitorProduct.competitorProductId}&dto(productName)=${app2:encode(competitorProduct.productName)}&dto(operation)=update&dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(entryDateU)=${dateValue}"/>
            <c:set var="deleteAction"
                   value="CompetitorProduct/Forward/Delete.do?dto(withReferences)=true&dto(competitorProductId)=${competitorProduct.competitorProductId}&dto(productName)=${app2:encode(competitorProduct.productName)}&dto(entryDateU)=${dateValue}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                <app2:checkAccessRight functionality="COMPETITORPRODUCT" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update" action="${editAction}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="COMPETITORPRODUCT" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" styleClass="listItem"
                                        headerStyle="listHeader" width="50%" action="${deleteAction}"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
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
    </div>
</div>


