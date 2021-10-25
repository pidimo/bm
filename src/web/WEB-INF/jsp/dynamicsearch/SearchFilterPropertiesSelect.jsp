<%@ include file="/Includes.jsp" %>

<c:set var="propertiesList" value="${app2:getDynamicSearchPropertiesList(dynamicSearchName, pageContext.request)}"/>
<html:select property="searchProperties" value="" styleClass="mediumSelect clsSearchProperty ${app2:getFormSelectClasses()}">
    <html:option value=""/>
    <html:options collection="propertiesList" property="value" labelProperty="label"/>
</html:select>



