<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ include file="/Includes.jsp" %>

<c:if test="${languageOfTemplate !=''}">

    <script language="JavaScript">
        <!--
        function validateTemplate(id) {
            return true;
        }
        //-->
    </script>
</c:if>

<c:set var="word_template"><%=CampaignConstants.DocumentType.WORD.getConstantAsString()%>
</c:set>
<c:set var="html_template"><%=CampaignConstants.DocumentType.HTML.getConstantAsString()%>
</c:set>

<html:form action="/Template/Forward/Create.do">
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="create" functionality="CAMPAIGNTEMPLATE"
                             styleClass="button ${app2:getFormButtonClasses()}">
            <fmt:message key="Common.new"/>
        </app2:securitySubmit>
    </div>
</html:form>

<div class="table-responsive">
    <fanta:table mode="bootstrap" list="templateList" width="100%"
                 styleClass="${app2:getFantabulousTableClases()}"
                 id="template" action="Template/List.do"
                 imgPath="${baselayout}">
        <c:set var="deleteAction"
               value="Template/Forward/Delete.do?dto(withReferences)=true&dto(templateId)=${template.templateId}&dto(description)=${app2:encode(template.description)}"/>
        <c:set var="editAction"
               value="Template/Forward/Update.do?dto(templateId)=${template.templateId}&dto(description)=${app2:encode(template.description)}"/>

        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <app2:checkAccessRight functionality="CAMPAIGNTEMPLATE" permission="VIEW">
                <fanta:actionColumn name="update" title="Common.update" action="${editAction}"
                                    styleClass="listItem" headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="CAMPAIGNTEMPLATE" permission="DELETE">
                <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                    styleClass="listItem" headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
            </app2:checkAccessRight>
        </fanta:columnGroup>
        <fanta:dataColumn name="description"
                          action="${editAction}"
                          styleClass="listItem" title="Template.description"
                          headerStyle="listHeader"
                          width="45%" orderable="true"/>
        <fanta:dataColumn name="documentType" styleClass="listItem2" title="Template.documenType"
                          headerStyle="listHeader" width="50%" renderData="false">
            <c:if test="${template.documentType == word_template}">
                <fmt:message key="Campaign.documentType.word"/>
            </c:if>
            <c:if test="${template.documentType == html_template}">
                <fmt:message key="Campaign.documentType.html"/>
            </c:if>
        </fanta:dataColumn>
    </fanta:table>
</div>
