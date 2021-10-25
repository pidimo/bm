<%@ include file="/Includes.jsp" %>

<c:set var="orgInfoMap" value="${app2:getAddressMap(dto.addressId)}"/>
<c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${dto.imageId}"/>

{
"entity" : {
    "addressId" : "${dto.addressId}",
    "contactPersonId" : "${dto.contactPersonId}",

    <c:if test="${'true' eq isFromImportAddress}">
      "addressIdToImport" : "${dto.addressIdToImport}",
    </c:if>

    <c:set var="userInfoMap" value="${app2:getUserMapByAddressId(dto.contactPersonId, pageContext.request)}"/>
    "isFavoriteWVApp" : "${userInfoMap.isFavoriteWVApp}",

    "companyId" : "${dto.companyId}",
    "function" : "${app2:escapeJSON(dto.function)}",
    "personTypeId" : "${dto.personTypeId}",
    "departmentId" : "${dto.departmentId}",
    "addAddressLine" : "${app2:escapeJSON(dto.addAddressLine)}",

    "organizationName" : "${app2:escapeJSON(orgInfoMap.addressName)}",
    "orgCountryCode" : "${app2:escapeJSON(orgInfoMap.countryCode)}",
    "orgCountryName" : "${app2:escapeJSON(orgInfoMap.countryName)}",
    "orgZip" : "${app2:escapeJSON(orgInfoMap.zip)}",
    "orgCity" : "${app2:escapeJSON(orgInfoMap.city)}",
    "orgStreet" : "${app2:escapeJSON(orgInfoMap.street)}",
    "orgHouseNumber" : "${app2:escapeJSON(orgInfoMap.houseNumber)}",

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
    "isActive" : "${dto.isActive}",
    "isCustomer" : "${dto.isCustomer}",
    "isSupplier" : "${dto.isSupplier}",
    "recordUserId" : "${dto.recordUserId}",
    "imageId" : "${dto.imageId}",
    "imageUrl" : "${(not empty dto.imageId) ? imageUrlVar : ""}",

    "privateVersion" : "${dto.privateVersion}",
    "version" : "${dto.version}",
    <c:set var="telecomItems" value="${app2:getAddressTelecomsMap(dto.addressId)}" scope="request"/>
    <c:import url="/bmapp/common/contacts/TelecomJsonFragment.jsp"/>
    },

<c:set var="organizationId" value="${dto.addressId}" scope="request"/>
<c:import url="/bmapp/common/contacts/ContactPersonCatalogsJsonFragment.jsp"/>
}
