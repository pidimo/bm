<%@ include file="/Includes.jsp" %>

<c:set var="addressInfoMap" value="${app2:getAddressMap(companyInfoForm.dtoMap['companyId'])}"/>
<c:set var="logoUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${companyInfoForm.dtoMap['logoId']}"/>


{
  "entity" : {
    "companyId" : "${companyInfoForm.dtoMap['companyId']}",
    "login" : "${app2:escapeJSON(companyInfoForm.dtoMap['login'])}",
    "note" : "${app2:escapeJSON(addressInfoMap.note)}",
    "logoId" : "${companyInfoForm.dtoMap['logoId']}",
    "logoUrl" : "${(not empty companyInfoForm.dtoMap['logoId']) ? logoUrlVar : ""}"
  }
}
