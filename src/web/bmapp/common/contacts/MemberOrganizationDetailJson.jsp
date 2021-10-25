<%@ include file="/Includes.jsp" %>

<c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${dto.imageId}"/>

<c:set var="orgBusinessAreaVar" value="${app2:readCustomerBusinessAreaCategoryFieldValue(dto.addressId, pageContext.request)}"/>

{
"entity" : {
  "addressId" : "${dto.addressId}",
  "companyId" : "${dto.companyId}",
  "addressType" : "${dto.addressType}",
  "accessUserGroupIds" : "${dto.accessUserGroupIds}",
  "name1" : "${app2:escapeJSON(dto.name1)}",
  "name2" : "${app2:escapeJSON(dto.name2)}",
  "name3" : "${app2:escapeJSON(dto.name3)}",
  "searchName" : "${app2:escapeJSON(dto.searchName)}",
  "keywords" : "${app2:escapeJSON(dto.keywords)}",
  "education" : "${app2:escapeJSON(dto.education)}",
  "birthday" : "${dto.birthday}",
  "birthdayDate" : "${dto.birthdayDate}",
  "languageId" : "${dto.languageId}",
  "street" : "${app2:escapeJSON(dto.street)}",
  "houseNumber" : "${app2:escapeJSON(dto.houseNumber)}",
  "additionalAddressLine" : "${app2:escapeJSON(dto.additionalAddressLine)}",

  "orgBusinessArea" : "${app2:escapeJSON(orgBusinessAreaVar)}",

  "zip" : "${app2:escapeJSON(dto.zip)}",
  "cityId" : "${dto.cityId}",
  "city" : "${app2:escapeJSON(dto.city)}",
  "countryId" : "${dto.countryId}",
  "countryCode" : "${app2:escapeJSON(dto.countryCode)}",
  "salutationId" : "${dto.salutationId}",
  "titleId" : "${dto.titleId}",
  "isActive" : "${dto.isActive}",
  "isCustomer" : "${dto.isCustomer}",
  "isSupplier" : "${dto.isSupplier}",
  "recordUserId" : "${dto.recordUserId}",
  "imageId" : "${dto.imageId}",
  "imageUrl" : "${(not empty dto.imageId) ? imageUrlVar : ""}",
  "version" : "${dto.version}",
  <c:set var="telecomItems" value="${dto.telecomMap}" scope="request"/>
  <c:import url="/bmapp/common/contacts/TelecomJsonFragment.jsp"/>
},
<c:import url="/bmapp/common/contacts/ContactCatalogsJsonFragment.jsp"/>
}
