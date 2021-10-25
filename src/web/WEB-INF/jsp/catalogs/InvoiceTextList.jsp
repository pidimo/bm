<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<div class="${app2:getListWrapperClasses()}">

    <app2:checkAccessRight functionality="INVOICETEMPLATE" permission="UPDATE">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app:url value="/InvoiceText/Forward/Create.do" var="url"/>

            <html:button styleClass="${app2:getFormButtonClasses()} button"
                         property="dto(new)"
                         onclick="window.parent.location='${url}'">
                <fmt:message key="Common.new"/>
            </html:button>
        </div>
    </app2:checkAccessRight>

    <legend class="title">
        <fmt:message key="${windowTitle}"/>
    </legend>
    <div class="table-responsive" id="tableId">

        <app:url var="listURL" value="/InvoiceText/List.do" enableEncodeURL="false"/>
        <fanta:table mode="bootstrap" list="invoiceTextList"
                     width="100%"
                     id="invoiceText"
                     action="${listURL}"
                     imgPath="${baselayout}"
                     styleClass="${app2:getFantabulousTableClases()}">

            <app:url var="editAction"
                     value="InvoiceText/Forward/Update.do?dto(templateId)=${invoiceText.templateId}&dto(languageId)=${invoiceText.languageId}"/>
            <app:url var="deleteAction"
                     value="InvoiceText/Forward/Delete.do?dto(templateId)=${invoiceText.templateId}&dto(languageId)=${invoiceText.languageId}&dto(enableLastElementValidation)=true"/>
            <app:url var="downloadAction"
                     value="InvoiceText/Forward/Download.do?dto(templateId)=${invoiceText.templateId}&dto(languageId)=${invoiceText.languageId}&dto(freeTextId)=${invoiceText.freetextId}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                <app2:checkAccessRight functionality="INVOICETEMPLATE" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update"
                                        action="javascript:goParentURL('${editAction}')"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"
                                        useJScript="true"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="INVOICETEMPLATE" permission="DELETE">
                    <c:choose>
                        <c:when test="${1 != invoiceText.isDefault}">
                            <fanta:actionColumn name="" title="Common.delete"
                                                useJScript="true"
                                                action="javascript:goParentURL('${deleteAction}')"
                                                styleClass="listItem" headerStyle="listHeader"
                                                glyphiconClass="${app2:getClassGlyphTrash()}"/>
                        </c:when>
                        <c:otherwise>
                            <fanta:actionColumn name="" title="" styleClass="listItem"
                                                headerStyle="listHeader">
                                &nbsp;
                            </fanta:actionColumn>
                        </c:otherwise>
                    </c:choose>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="INVOICETEMPLATE" permission="VIEW">
                    <fanta:actionColumn name="download" title="Common.download" useJScript="true"
                                        action="javascript:goParentURL('${downloadAction}')"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphOpenFolder()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="languageName" styleClass="listItem" title="InvoiceText.language"
                              action="javascript:goParentURL('${editAction}')"
                              headerStyle="listHeader" width="75%"
                              orderable="true" useJScript="true"/>
            <fanta:dataColumn name="isDefault" styleClass="listItem2" title="InvoiceText.isDefault"
                              headerStyle="listHeader" width="20%" renderData="false">
                <c:if test="${invoiceText.isDefault == '1'}">
                    <span class="${app2:getClassGlyphOk()}"></span>
                </c:if>
            </fanta:dataColumn>
        </fanta:table>

    </div>
</div>