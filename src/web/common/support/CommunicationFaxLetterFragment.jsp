<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ include file="/Includes.jsp" %>
<tr>
    <td class="label"><fmt:message key="Document.template"/></td>
    <td class="contain" colspan="3">

        <html:hidden property="dto(rebuildDocument)"/> <!--this field is as flag, set in true when the document (fax,letter) should be regererated-->

        <c:set var="mediatype_WORD" value="<%=CatalogConstants.MediaType.WORD.getConstantAsString()%>"/>
        <fanta:select property="dto(templateId)" styleId="templateId" listName="templateList" labelProperty="description" valueProperty="id" module="/catalogs"
                      firstEmpty="true" styleClass="mediumSelect" readOnly="${onlyView}" tabIndex="15" >
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
            <fanta:parameter field="mediaType" value="${mediatype_WORD}" />
        </fanta:select>

    </td>
</tr>