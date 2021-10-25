<%@ include file="/Includes.jsp" %>
<!--Insert the general default style css-->
<link rel="stylesheet" type="text/css" href="<c:url value="/elwis_style.css"/>"/>
<!--Insert company styles (if it's defined)-->
<c:set var="logonCounter" value="${sessionScope['logonStyleStatus']}"/>
<c:set var="companyStyleCounterValue" value="companyStyleStatus_${sessionScope.user.valueMap['companyId']}"/>
<LINK rel="stylesheet" href="<c:url value="/UIManager/Put/StyleSheet.do?companyChangeCount=${applicationScope[companyStyleCounterValue]}&logonChangeCount=${logonCounter}"/>" type="text/css"/>
<!--Insert user styles (if it's defined)-->
<LINK rel="stylesheet" href="<c:url value="/UIManager/Put/StyleSheet.do?userChangeCount=${sessionScope['userStyleStatus']}&logonChangeCount=${logonCounter}"/>" type="text/css"/>

<!--Below conditional solves IE ridiculous button large problem-->
<!--[if gt IE 5]>
<style>
INPUT.button{
overflow:visible;
width:1;
}
</style>
<![endif]-->
