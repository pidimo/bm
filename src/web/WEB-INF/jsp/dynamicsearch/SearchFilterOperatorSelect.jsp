<%@ include file="/Includes.jsp" %>

<c:set var="isFromAjax" value="${'true' eq param.isFromAjaxRequest}"/>

<c:if test="${isFromAjax}">
    <c:set var="dynamicSearchName" value="${param.dynamicSearchName}" scope="request"/>
    <c:set var="fieldAlias" value="${param.fieldProperty}" scope="request"/>
</c:if>
<tags:bootstrapDynamicSearchableForm addForm="${isFromAjax}">
    <c:set var="operatorList" value="${app2:getDynamicSearchFieldOperatorList(dynamicSearchName, fieldAlias, pageContext.request)}"/>
    <html:select property="parameter(${fieldAlias}_operator)" styleClass="select clsOperator ${app2:getFormSelectClasses()}">
        <html:option value=""/>
        <html:options collection="operatorList" property="value" labelProperty="label"/>
    </html:select>
</tags:bootstrapDynamicSearchableForm>

