<%@ include file="/Includes.jsp" %>

<c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${dto.imageId}"/>

{
"entity" : {
    "addressIdToImport" : "${dto.addressId}",
    "companyId" : "${dto.companyId}",
    "addressType" : "${dto.addressType}",
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
    "zip" : "${app2:escapeJSON(dto.zip)}",
    "cityId" : "${dto.cityId}",
    "city" : "${app2:escapeJSON(dto.city)}",
    "countryId" : "${dto.countryId}",
    "countryCode" : "${app2:escapeJSON(dto.countryCode)}",
    "salutationId" : "${dto.salutationId}",
    "titleId" : "${dto.titleId}",
    "imageId" : "${dto.imageId}",
    "imageUrl" : "${(not empty dto.imageId) ? imageUrlVar : ""}",
    "version" : "${dto.version}"
},
<c:set var="organizationId" value="${param.contactId}" scope="request"/>
<c:import url="/bmapp/common/contacts/ContactPersonCatalogsJsonFragment.jsp"/>

}
