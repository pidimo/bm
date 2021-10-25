<%@ include file="/Includes.jsp" %>

<!--Insert company styles (if it's defined)-->
<c:set var="logonCounter" value="${sessionScope['logonStyleStatus']}"/>
<c:set var="companyStyleCounterValue" value="companyStyleStatus_${sessionScope.user.valueMap['companyId']}"/>
<LINK rel="stylesheet" href="<c:url value="/UIManager/Put/StyleSheet.do?companyChangeCount=${applicationScope[companyStyleCounterValue]}&logonChangeCount=${logonCounter}"/>" type="text/css"/>
<!--Insert user styles (if it's defined)-->
<LINK rel="stylesheet" href="<c:url value="/UIManager/Put/StyleSheet.do?userChangeCount=${sessionScope['userStyleStatus']}&logonChangeCount=${logonCounter}"/>" type="text/css"/>
