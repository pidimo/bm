<%@ include file="/Includes.jsp" %>

<table width="90%" border="0" align="center" cellpadding="10" cellspacing="0">
  <tr>
    <td align="center">
    <table width="100%" border="0" cellpadding="2" cellspacing="0">
            <tr>
            <td>

        <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
        <html:form action="/SupplierProduct/List.do" focus="parameter(productName)" >
            <TR>
                <td class="label"><fmt:message    key="Common.search"/></td>
                <td  class="contain" colspan="3">
                        <html:text property="parameter(productName)" styleClass="largeText" maxlength="40"/>&nbsp;
                        <html:submit styleClass="button"><fmt:message   key="Common.go"/></html:submit>&nbsp;
                </td>
            </TR>
            </html:form>
            <tr>
                <td colspan="4" align="center" class="alpha">
                    <fanta:alphabet action="SupplierProduct/List.do" parameterName="productNameAlpha"/>
                </td>
            </tr>

        </table>
        </br>
      </td>
   </tr>
<tr>
<html:form styleId="CREATE_NEW_SELL" action="/SupplierProduct/Forward/Create">
          <td class="button"><!--Button create up -->
             <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
          </td>
</html:form>
         </tr>
 </table>

       <fmt:message   var="numberFormat" key="numberFormat.2DecimalPlaces"/>
       <fanta:table list="supplierProductList" width="100%" id="supplier" action="SupplierProduct/List.do" imgPath="${baselayout}"  >
             <c:set var="editLink" value="SupplierProduct/Forward/Update.do?dto(supplierId)=${supplier.supplierId}&dto(productId)=${supplier.productId}&dto(productName)=${app2:encode(supplier.productName)}"/>
             <c:set var="deleteLink" value="SupplierProduct/Forward/Delete.do?dto(supplierId)=${supplier.supplierId}&dto(productId)=${supplier.productId}&dto(productName)=${app2:encode(supplier.productName)}&dto(withReferences)=true"/>
             <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                  <fanta:actionColumn name="edit" title="Common.update" action="${editLink}" styleClass="listItem" headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif" />
                  <fanta:actionColumn name="delete" title="Common.delete" action="${deleteLink}" styleClass="listItem" headerStyle="listHeader" width="50%" image="${baselayout}/img/delete.gif" />
             </fanta:columnGroup>
            <fanta:dataColumn name="productName" action="${editLink}"  styleClass="listItem" title="Product.name"  headerStyle="listHeader" width="45%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="price" styleClass="listItem" title="Product.price"  headerStyle="listHeader" width="10%" orderable="true" renderData="false" style="text-align:right" >
               &nbsp; <fmt:formatNumber value="${supplier.price}" type="number" pattern="${numberFormat}" />
            </fanta:dataColumn>
        <fanta:dataColumn name="contact" styleClass="listItem" title="ContactPerson"  headerStyle="listHeader" width="25%" orderable="true"/>
        <fanta:dataColumn name="partNumber" styleClass="listItem" title="SupplierProduct.orderNumber"  headerStyle="listHeader" width="10%" orderable="true"/>
        <fanta:dataColumn name="active" styleClass="listItem2" title="Common.active" headerStyle="listHeader" width="5%" renderData="false" >
                <c:choose>
                    <c:when test="${supplier.active == 1}">
                        <img align="center" src="<c:out value="${sessionScope.baselayout}"/>/img/check.gif" alt=""/>
                    </c:when>
                    <c:otherwise>
                        &nbsp;
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>
        </fanta:table>

<table width="100%" border="0" cellpadding="2" cellspacing="0">
  <tr>
  <html:form styleId="CREATE_NEW_SELL" action="/SupplierProduct/Forward/Create">
            <td class="button"><!--Button create up -->
               <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
            </td>

      </html:form>
  </tr>

</table>
</td></tr>
</table>