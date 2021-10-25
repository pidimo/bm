<%@ include file="/Includes.jsp" %>

<c:set var="categoryAllTypeList" value="${app2:getCategoryTableTypes(pageContext.request)}"/>

<html:form action="${action}" focus="dto(label)">
    <html:hidden property="dto(op)" value="${op}"/>

    <c:if test="${'update' == op || 'delete' == op}">
        <html:hidden property="dto(categoryTabId)"/>
    </c:if>

    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
        <html:hidden property="dto(canUpdateLabel)"/>
    </c:if>

    <table width="60%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td colspan="2" class="title">
                    ${title}
            </td>
        </tr>
        <tr>
            <td class="label" width="40%">
                <fmt:message key="CategoryTab.label"/>
            </td>
            <td class="contain" width="60%">
                <app:text property="dto(label)" styleClass="text" maxlength="20" view="${'delete' == op}" tabindex="1"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="CategoryTab.sequence"/>
            </td>
            <td class="contain">
                <app:numberText property="dto(sequence)" style="text" numberType="integer" maxlength="3"
                                tabindex="2" view="${'delete' == op}"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="CategoryTab.table"/>
            </td>
            <td class="contain">
                <html:select property="dto(table)"
                             styleClass="select"
                             readonly="${'delete' == op || false == categoryTabForm.dtoMap.canUpdateLabel}"
                             tabindex="3">
                    <html:option value=""/>
                    <html:options collection="categoryAllTypeList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="button" colspan="2">
                <app2:securitySubmit operation="${op}" functionality="CATEGORYTAB" styleClass="button">
                    <c:out value="${button}"/>
                </app2:securitySubmit>
                <c:if test="${'create' == op}">
                    <app2:securitySubmit operation="CREATE"
                                         functionality="CATEGORYTAB"
                                         styleClass="button"
                                         property="SaveAndNew">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
            </td>
        </tr>
    </table>
</html:form>