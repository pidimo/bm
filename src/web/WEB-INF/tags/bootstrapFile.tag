<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ attribute name="property" required="true" %>
<%@ attribute name="accept" %>
<%@ attribute name="tabIndex" %>
<%@ attribute name="styleId" %>
<%@ attribute name="glyphiconClass" %>

<c:if test="${not empty tabIndex}">
    <c:set var="tabIndexVar" value=" tabindex=\"${tabIndex}\""/>
</c:if>

<c:if test="${not empty styleId}">
    <c:set var="styleIdVar" value=" id=\"${styleId}\""/>
</c:if>

<div class="input-group">

    <input type="text" class="${app2:getFormInputClasses()}" disabled="disabled">

    <span class="input-group-btn">
        <span class="btn btn-default btnFile">
            <span class="glyphicon ${(not empty glyphiconClass) ? glyphiconClass : 'glyphicon-folder-open'}"></span>
            	<input type="file"
                       name="${property}"
                       accept="${accept}"
                ${(not empty tabIndex) ? tabIndexVar : ''}
                ${(not empty styleId) ? styleIdVar : ''} />
        </span>
    </span>

</div>