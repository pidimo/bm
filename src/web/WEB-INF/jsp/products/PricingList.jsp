<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">

    <html:form action="/Pricing/Forward/Create">
        <html:hidden property="dto(unitName)"/>
        <html:hidden property="dto(productId)" value="${param.productId}"/>
        <div class="${app2:getFormGroupClasses()}">
            <app2:securitySubmit operation="CREATE" functionality="PRICING" styleClass="${app2:getFormButtonClasses()}">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>

    <div class="table-responsive">
        <!--List -->
        <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
        <fanta:table mode="bootstrap" list="pricingList" styleClass="${app2:getFantabulousTableClases()}" width="100%" id="pricing"
                     action="Pricing/List.do"
                     imgPath="${baselayout}">
            <c:set var="editLink"
                   value="Pricing/Forward/Update.do?dto(productId)=${param.productId}&dto(unitName)=${app2:encode(pricing.unit)}&dto(quantity)=${pricing.quantity}"/>
            <c:set var="deleteLink"
                   value="Pricing/Forward/Delete.do?dto(withReferences)=true&productId=${param.productId}&dto(productId)=${param.productId}&dto(unitName)=${app2:encode(pricing.unit)}&dto(quantity)=${pricing.quantity}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="PRICING" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update" action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader" width="5%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="PRICING" permission="DELETE">
                    <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="10%"
                                        title="Common.delete">
                        <c:choose>
                            <c:when test="${pricing.quantity != 1}">
                                <app:link action="${deleteLink}" titleKey="Common.delete">
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
            <fanta:dataColumn name="quantity" action="${editLink}" styleClass="listItem"
                              title="Product.quantity"
                              headerStyle="listHeader" width="20%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="unit" styleClass="listItem" title="Product.unit" headerStyle="listHeader"
                              width="30%" orderable="true"/>
            <fanta:dataColumn name="price" styleClass="listItem2Right" title="Product.price"
                              headerStyle="listHeader" width="20%" orderable="true" renderData="false">
                <fmt:formatNumber value="${pricing.price}" type="number" pattern="${numberFormat}"/>
            </fanta:dataColumn>
        </fanta:table>

    </div>

    <html:form styleId="CREATE_NEW_PRICING" action="/Pricing/Forward/Create">
        <html:hidden property="dto(unitId)"/>
        <html:hidden property="dto(productId)" value="${param.productId}"/>
        <div class="${app2:getFormGroupClasses()}">
            <app2:securitySubmit operation="CREATE" functionality="PRICING" styleClass="${app2:getFormButtonClasses()}">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>

</div>

