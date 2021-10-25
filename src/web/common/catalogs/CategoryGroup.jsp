<%@ include file="/Includes.jsp" %>

<script language="JavaScript">
    function changeTable() {
        document.getElementById("changeTableId").value = "true";
        document.forms[0].submit();
    }
</script>
<c:set var="categoryAllTypeList" value="${app2:getCategoryTableTypes(pageContext.request)}"/>

<html:form action="${action}" focus="dto(label)">
    <html:hidden property="dto(changeTableId)" styleId="changeTableId" value="false"/>

    <html:hidden property="dto(op)" value="${op}"/>

    <c:if test="${'update' == op || 'delete' == op}">
        <html:hidden property="dto(categoryGroupId)"/>
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
                <fmt:message key="CategoryGroup.label"/>
            </td>
            <td class="contain" width="60%">
                <app:text property="dto(label)" styleClass="text" maxlength="20" view="${'delete' == op}"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="CategoryGroup.sequence"/>
            </td>
            <td class="contain">
                <app:numberText property="dto(sequence)" style="text" numberType="integer" maxlength="3"
                                view="${'delete' == op}"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="CategoryGroup.table"/>
            </td>
            <td class="contain">
                <html:select property="dto(table)"
                             styleClass="select"
                             readonly="${'delete' == op || false == categoryGroupForm.dtoMap.canUpdateLabel}"
                             onchange="javascript:changeTable();">
                    <html:option value=""/>
                    <html:options collection="categoryAllTypeList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>

        <tr>
            <td class="label">
                <fmt:message key="CategoryGroup.categoryTabId"/>
            </td>
            <td class="contain">
                <c:if test="${null != categoryGroupForm.dtoMap.table && not empty categoryGroupForm.dtoMap.table}">
                    <fanta:select property="dto(categoryTabId)"
                                  listName="categoryTabList"
                                  labelProperty="label"
                                  valueProperty="categoryTabId"
                                  firstEmpty="true"
                                  styleClass="select" readOnly="${'delete' == op}">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        <fanta:parameter field="tableId" value="${categoryGroupForm.dtoMap.table}"/>
                    </fanta:select>
                </c:if>
            </td>
        </tr>
        <tr>
            <td class="button" colspan="2">
                <app2:securitySubmit operation="${op}" functionality="CATEGORYGROUP" styleClass="button">
                    <c:out value="${button}"/>
                </app2:securitySubmit>
                <c:if test="${'create' == op}">
                    <app2:securitySubmit operation="CREATE" functionality="CATEGORYGROUP" styleClass="button" property="SaveAndNew">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
            </td>
        </tr>
    </table>
</html:form>