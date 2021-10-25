<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <div class="searchContainer">
        <html:form action="/Supplier/List.do" focus="parameter(contactSearchName)" styleClass="form-horizontal">
            <div class="${app2:getSearchWrapperClasses()}">
                <label class="${app2:getFormLabelOneSearchInput()} label-left">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormOneSearchInput()}">
                    <div class="input-group">
                        <html:text property="parameter(contactSearchName)"
                                   styleClass="${app2:getFormInputClasses()} largeText" maxlength="40"/>
                        <span class="input-group-btn">
                            <html:submit styleClass="${app2:getFormButtonClasses()}">
                                <fmt:message key="Common.go"/>
                            </html:submit>
                        </span>
                    </div>
                </div>
            </div>
        </html:form>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="Supplier/List.do" parameterName="name1" mode="bootstrap"/>
        </div>
    </div>

    <html:form action="/Supplier/Forward/Create">
        <html:hidden property="dto(productId)" value="${param.productId}"/>
        <div class="${app2:getFormGroupClasses()}"><!--Button create up -->
            <html:hidden property="dto(viewData)" value="false"/>
            <app2:securitySubmit operation="CREATE" functionality="PRODUCTSUPPLIER"
                                 styleClass="${app2:getFormButtonClasses()}">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>

    <!--List -->
    <div class="table-responsive">
        <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
        <fanta:table mode="bootstrap" styleClass="${app2:getFantabulousTableClases()}"
                     list="productSupplierList"
                     width="100%" id="supplier" action="Supplier/List.do"
                     imgPath="${baselayout}">
            <c:set var="editLink"
                   value="Supplier/Forward/Update.do?dto(productId)=${param.productId}&dto(supplierId)=${supplier.supplierId}&dto(supplierName)=${app2:encode(supplier.supplierName)}"/>
            <c:set var="deleteLink"
                   value="Supplier/Forward/Delete.do?dto(withReferences)=true&dto(productId)=${param.productId}&dto(supplierId)=${supplier.supplierId}&dto(supplierName)=${app2:encode(supplier.supplierName)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="PRODUCTSUPPLIER" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update" action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="PRODUCTSUPPLIER" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete" action="${deleteLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="supplierName" action="${editLink}" styleClass="listItem" title="Supplier"
                              headerStyle="listHeader" width="30%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="price" styleClass="listItem" title="Product.price" headerStyle="listHeader"
                              width="10%" orderable="true" renderData="false" style="text-align:right">
                <fmt:formatNumber value="${supplier.price}" type="number" pattern="${numberFormat}"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="contact" styleClass="listItem" title="ContactPerson"
                              headerStyle="listHeader"
                              width="30%" orderable="true"/>
            <fanta:dataColumn name="partNumber" styleClass="listItem" title="SupplierProduct.orderNumber"
                              headerStyle="listHeader" width="20%" orderable="true"/>
            <fanta:dataColumn name="active" styleClass="listItem2Center" title="Common.active"
                              headerStyle="listHeader"
                              width="5%" renderData="false">
                <c:if test="${supplier.active == 1}">
                    <span class="${app2:getClassGlyphOk()}"></span>
                </c:if>
            </fanta:dataColumn>
        </fanta:table>
    </div>
    <%--list end--%>

    <html:form styleId="CREATE_NEW_SUPPLIER" action="/Supplier/Forward/Create">
        <%--<html:hidden property="dto(unitId)" />--%>
        <html:hidden property="dto(productId)" value="${param.productId}"/>
        <div class="${app2:getFormGroupClasses()}"><!--Button create up -->
            <app2:securitySubmit operation="CREATE" functionality="PRODUCTSUPPLIER"
                                 styleClass="${app2:getFormButtonClasses()}">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>
</div>
