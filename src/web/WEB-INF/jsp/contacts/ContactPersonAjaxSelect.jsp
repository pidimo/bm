<%@ include file="/Includes.jsp" %>

<fanta:select property="dto(invoiceContactPersonId)" listName="searchContactPersonList"
              module="/contacts" firstEmpty="true"
              labelProperty="contactPersonName"
              valueProperty="contactPersonId"
              styleClass="mediumSelect form-control"
              tabIndex="15"
              readOnly="${'delete' == param.op}">
    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
    <fanta:parameter field="addressId" value="${not empty param.addressId ? param.addressId : 0}"/>
</fanta:select>
