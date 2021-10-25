<%@ include file="/Includes.jsp" %>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td align="center">
            <table width="100%" border="0" cellpadding="2" cellspacing="0">
                <tr>
                    <td>

                        <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0"
                               class="searchContainer">
                            <html:form action="/Supplier/List.do" focus="parameter(contactSearchName)">
                                <TR>
                                    <td class="label"><fmt:message key="Common.search"/></td>

                                    <td class="contain" colspan="3" nowrap>
                                        <html:text property="parameter(contactSearchName)"
                                                   styleClass="largeText" maxlength="40"/>&nbsp;
                                        <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
                                        &nbsp;
                                    </td>
                                </TR>
                            </html:form>

                            <tr>
                                <td colspan="4" align="center" class="alpha">
                                    <fanta:alphabet action="Supplier/List.do" parameterName="name1"/>
                                </td>
                            </tr>
                        </table>

                        <br>
                    </td>
                </tr>
                <tr>

                    <html:form action="/Supplier/Forward/Create">
                        <html:hidden property="dto(productId)" value="${param.productId}"/>
                        <td class="button"><!--Button create up -->
                            <html:hidden property="dto(viewData)" value="false"/>
                            <app2:securitySubmit operation="CREATE" functionality="PRODUCTSUPPLIER" styleClass="button">
                                <fmt:message key="Common.new"/>
                            </app2:securitySubmit>
                        </td>
                    </html:form>
                </tr>
            </table>
        </td>
    </tr>
    <tr> <!--List -->
        <td>
            <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
            <fanta:table list="productSupplierList" width="100%" id="supplier" action="Supplier/List.do"
                         imgPath="${baselayout}">
                <c:set var="editLink"
                       value="Supplier/Forward/Update.do?dto(productId)=${param.productId}&dto(supplierId)=${supplier.supplierId}&dto(supplierName)=${app2:encode(supplier.supplierName)}"/>
                <c:set var="deleteLink"
                       value="Supplier/Forward/Delete.do?dto(withReferences)=true&dto(productId)=${param.productId}&dto(supplierId)=${supplier.supplierId}&dto(supplierName)=${app2:encode(supplier.supplierName)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="PRODUCTSUPPLIER" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update" action="${editLink}" styleClass="listItem"
                                            headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="PRODUCTSUPPLIER" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete" action="${deleteLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader" width="50%" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="supplierName" action="${editLink}" styleClass="listItem" title="Supplier"
                                  headerStyle="listHeader" width="30%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="price" styleClass="listItem" title="Product.price" headerStyle="listHeader"
                                  width="10%" orderable="true" renderData="false" style="text-align:right">
                    <fmt:formatNumber value="${supplier.price}" type="number" pattern="${numberFormat}"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="contact" styleClass="listItem" title="ContactPerson" headerStyle="listHeader"
                                  width="30%" orderable="true"/>
                <fanta:dataColumn name="partNumber" styleClass="listItem" title="SupplierProduct.orderNumber"
                                  headerStyle="listHeader" width="20%" orderable="true"/>
                <fanta:dataColumn name="active" styleClass="listItem2Center" title="Common.active"
                                  headerStyle="listHeader"
                                  width="5%" renderData="false">
                    <c:if test="${supplier.active == 1}">
                        <img align="middle" src="<c:out value="${sessionScope.baselayout}"/>/img/check.gif" alt=""/>
                    </c:if>
                </fanta:dataColumn>
            </fanta:table>


        </td>
    </tr>
    <%--list end--%>

    <tr>
        <td align="center">
            <table width="100%" border="0" cellpadding="2" cellspacing="0">
                <tr>
                    <html:form styleId="CREATE_NEW_SUPPLIER" action="/Supplier/Forward/Create">
                        <%--       <html:hidden property="dto(unitId)" />--%>
                        <html:hidden property="dto(productId)" value="${param.productId}"/>
                        <td class="button"><!--Button create up -->
                            <app2:securitySubmit operation="CREATE" functionality="PRODUCTSUPPLIER" styleClass="button">
                                <fmt:message key="Common.new"/>
                            </app2:securitySubmit>

                        </td>
                    </html:form>
                </tr>
            </table>
        </td>
    </tr>
</table>
