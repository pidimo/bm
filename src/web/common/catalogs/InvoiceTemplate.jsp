<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(title)" enctype="multipart/form-data">
    <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

        <html:hidden property="dto(op)" value="${op}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(templateId)"/>
        </c:if>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>
        <TR>
            <TD colspan="2" class="title">
                <c:out value="${title}"/>
            </TD>
        </TR>
        <tr>
            <TD class="label" width="25%" nowrap>
                <fmt:message key="InvoiceTemplate.title"/>
            </TD>
            <TD class="contain" width="75%">
                <app:text property="dto(title)" styleClass="text" maxlength="149"
                          view="${'delete' == op}" tabindex="1"/>
            </TD>
        </tr>
        <c:if test="${'create' == op}">
            <TR>
                <TD class="label" width="40%"><fmt:message key="InvoiceTemplate.language"/></TD>
                <TD class="contain" width="60%">
                    <fanta:select property="dto(languageId)"
                                  listName="languageList"
                                  labelProperty="name"
                                  valueProperty="id"
                                  firstEmpty="true"
                                  styleClass="select"
                                  tabIndex="2">
                        <fanta:parameter field="companyId"
                                         value="${sessionScope.user.valueMap['companyId']}"/>
                    </fanta:select>
                </TD>
            </TR>
            <TR>
                <TD class="label" width="25%" nowrap>
                    <fmt:message key="InvoiceTemplate.template"/>
                </TD>
                <TD class="contain" width="75%">
                    <html:file property="dto(file)" accept="application/msword" tabindex="3"/>
                </TD>
            </TR>
        </c:if>
    </table>

    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
        <TR>
            <TD class="button">
                <app2:securitySubmit operation="${op}"
                                     functionality="INVOICETEMPLATE"
                                     styleClass="button"
                                     tabindex="4">
                    ${button}
                </app2:securitySubmit>
                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="INVOICETEMPLATE"
                                         styleClass="button"
                                         property="SaveAndNew"
                                         tabindex="5">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button"
                             tabindex="6">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </TD>
        </TR>
    </table>
</html:form>

<c:if test="${'update' == op}">
    <c:set var="templateId" value="${invoiceTemplateForm.dtoMap['templateId']}"/>
    <c:set var="title" value="${invoiceTemplateForm.dtoMap['title']}"/>
    <table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
        <tr>
            <td>
                <iframe name="frame1"
                        src="<app:url value="/InvoiceText/List.do?templateId=${templateId}&dto(templateId)=${templateId}&dto(title)=${title}"/>"
                        class="Iframe" scrolling="no" frameborder="0" id="iFrameId">
                </iframe>
            </td>
        </tr>
    </table>
</c:if>
