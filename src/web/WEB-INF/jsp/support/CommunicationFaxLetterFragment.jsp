<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ include file="/Includes.jsp" %>

<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="templateId">
            <fmt:message key="Document.template"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
            <html:hidden property="dto(rebuildDocument)"/>
            <!--this field is as flag, set in true when the document (fax,letter) should be regererated-->
            <c:set var="mediatype_WORD" value="<%=CatalogConstants.MediaType.WORD.getConstantAsString()%>"/>
            <fanta:select property="dto(templateId)" styleId="templateId" listName="templateList"
                          labelProperty="description" valueProperty="id" module="/catalogs"
                          firstEmpty="true" styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                          readOnly="${onlyView}" tabIndex="15">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="mediaType" value="${mediatype_WORD}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</div>