<%@ include file="/Includes.jsp" %>

<app2:checkAccessRight functionality="INVOICETEMPLATE" permission="CREATE">
    <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
        <TR>
            <html:form action="/InvoiceTemplate/Forward/Create.do">
                <TD colspan="6" class="button">
                    <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
                </TD>
            </html:form>
        </TR>
    </table>
</app2:checkAccessRight>

<TABLE border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <tr>
        <td>
            <fanta:table list="invoiceTemplateList" width="100%" id="invoiceTemplate"
                         action="InvoiceTemplate/List.do"
                         imgPath="${baselayout}">
                <c:set var="editAction"
                       value="InvoiceTemplate/Forward/Update.do?templateId=${invoiceTemplate.templateId}&dto(templateId)=${invoiceTemplate.templateId}&dto(title)=${app2:encode(invoiceTemplate.title)}"/>
                <c:set var="deleteAction"
                       value="InvoiceTemplate/Forward/Delete.do?templateId=${invoiceTemplate.templateId}&dto(withReferences)=true&dto(templateId)=${invoiceTemplate.templateId}&dto(title)=${app2:encode(invoiceTemplate.title)}"/>
                <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="INVOICETEMPLATE" permission="VIEW">
                        <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="INVOICETEMPLATE" permission="DELETE">
                        <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>

                <fanta:dataColumn name="title"
                                  action="${editAction}" styleClass="listItem2"
                                  title="InvoiceTemplate.title" headerStyle="listHeader" width="95%"
                                  orderable="true" maxLength="25">
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
</table>

