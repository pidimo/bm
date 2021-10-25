<%@ include file="/Includes.jsp" %>

{
<c:set var="fantaListName" value="allAddressList" scope="request"/>
<c:import url="/bmapp/common/util/ResultListPageInfoFragment.jsp"/>

"list":
[
<c:forEach var="contact" items="${allAddressList.result}" varStatus="statusVar">
    <c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${contact.imageId}"/>

    <c:if test="${statusVar.index > 0}">, </c:if>
    {
    "addressId" : "${contact.addressId}",
    "contactPersonAddressId" : "${contact.contactPersonAddressId}",
    "addressName" : "${app2:escapeJSON(contact.addressName1)}",
    "contactPersonOfName" : "${app2:escapeJSON(contact.addressName2)}",
    "addressType" : "${contact.addressType}",
    "addressType2" : "${contact.addressType2}",
    "name1" : "${app2:escapeJSON(contact.name1)}",
    "name2" : "${app2:escapeJSON(contact.name2)}",
    "name3" : "${app2:escapeJSON(contact.name3)}",
    "countryCode" : "${app2:escapeJSON(contact.countryCode)}",
    "cityName" : "${app2:escapeJSON(contact.cityName)}",
    "zip" : "${app2:escapeJSON(contact.zip)}",
    "salutationLabel" : "${app2:escapeJSON(contact.salutationLabel)}",
    "titleText" : "${app2:escapeJSON(contact.titleText)}",
    "function" : "${app2:escapeJSON(contact.function)}",
    "imageId" : "${contact.imageId}",
    "imageUrl" : "${(not empty contact.imageId) ? imageUrlVar : ""}"
    }
</c:forEach>
]
}

