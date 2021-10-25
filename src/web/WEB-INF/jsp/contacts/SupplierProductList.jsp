<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <div class="searchContainer">
        <html:form action="/SupplierProduct/List.do" focus="parameter(productName)" styleClass="form-horizontal">
            <div class="${app2:getSearchWrapperClasses()}">
                <label class="${app2:getFormLabelOneSearchInput()} label-left">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormOneSearchInput()}">
                    <div class="input-group">
                        <html:text property="parameter(productName)"
                                   styleClass="${app2:getFormInputClasses()} largeText"
                                   maxlength="40"/>
                        <div class="input-group-btn">
                            <html:submit styleClass="${app2:getFormButtonClasses()}">
                                <fmt:message key="Common.go"/>
                            </html:submit>
                        </div>
                    </div>
                </div>
            </div>
        </html:form>
        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="SupplierProduct/List.do" parameterName="productNameAlpha" mode="bootstrap"/>
        </div>
    </div>

    <html:form styleId="CREATE_NEW_SELL" action="/SupplierProduct/Forward/Create">
        <div class="${app2:getFormButtonWrapperClasses()}"><!--Button create up -->
            <html:submit styleClass="${app2:getFormButtonClasses()}">
                <fmt:message key="Common.new"/>
            </html:submit>
        </div>
    </html:form>

    <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="supplierProductList" width="100%" id="supplier"
                     styleClass="${app2:getFantabulousTableClases()}" action="SupplierProduct/List.do"
                     imgPath="${baselayout}">
            <c:set var="editLink"
                   value="SupplierProduct/Forward/Update.do?dto(supplierId)=${supplier.supplierId}&dto(productId)=${supplier.productId}&dto(productName)=${app2:encode(supplier.productName)}"/>
            <c:set var="deleteLink"
                   value="SupplierProduct/Forward/Delete.do?dto(supplierId)=${supplier.supplierId}&dto(productId)=${supplier.productId}&dto(productName)=${app2:encode(supplier.productName)}&dto(withReferences)=true"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <fanta:actionColumn name="edit" title="Common.update" action="${editLink}" styleClass="listItem"
                                    headerStyle="listHeader" width="50%" glyphiconClass="${app2:getClassGlyphEdit()}"/>
                <fanta:actionColumn name="delete" title="Common.delete" action="${deleteLink}" styleClass="listItem"
                                    headerStyle="listHeader" width="50%" glyphiconClass="${app2:getClassGlyphTrash()}"/>
            </fanta:columnGroup>
            <fanta:dataColumn name="productName" action="${editLink}" styleClass="listItem" title="Product.name"
                              headerStyle="listHeader" width="45%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="price" styleClass="listItem" title="Product.price" headerStyle="listHeader"
                              width="10%"
                              orderable="true" renderData="false" style="text-align:right">
                &nbsp; <fmt:formatNumber value="${supplier.price}" type="number" pattern="${numberFormat}"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="contact" styleClass="listItem" title="ContactPerson" headerStyle="listHeader"
                              width="25%" orderable="true"/>
            <fanta:dataColumn name="partNumber" styleClass="listItem" title="SupplierProduct.orderNumber"
                              headerStyle="listHeader" width="10%" orderable="true"/>
            <fanta:dataColumn name="active" styleClass="listItem2" title="Common.active" headerStyle="listHeader"
                              width="5%"
                              renderData="false">
                <c:choose>
                    <c:when test="${supplier.active == 1}">
                        <span class="${app2:getClassGlyphOk()}"
                    </c:when>
                    <c:otherwise>
                        &nbsp;
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>
        </fanta:table>
    </div>
    <html:form styleId="CREATE_NEW_SELL" action="/SupplierProduct/Forward/Create">
        <div class="${app2:getFormButtonWrapperClasses()}"><!--Button create up -->
            <html:submit styleClass="${app2:getFormButtonClasses()}">
                <fmt:message key="Common.new"/>
            </html:submit>
        </div>

    </html:form>
</div>