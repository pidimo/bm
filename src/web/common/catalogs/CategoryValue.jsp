<%@ include file="/Includes.jsp" %>

<c:set var="categoryId" value="${param['dto(categoryId)']}"/>
<c:set var="categoryName" value="${param['dto(categoryName)']}"/>


<html:form action="${action}" focus="dto(categoryValueName)">
    <html:hidden property="dto(categoryId)"/>
    <html:hidden property="dto(categoryName)"/>
    <html:hidden property="dto(tableId)"/>
    <html:hidden property="dto(op)" value="${op}"/>


    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(categoryValueId)" styleId="categoryValueId"/>
    </c:if>

    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>

    <table id="CategoryValue.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center"
           class="container">

        <TR>
            <TD colspan="2" class="title">
                <c:out value="${title}"/>
            </TD>
        </TR>
        <TR>
            <TD class="label" width="25%" nowrap><fmt:message key="CategoryValue.category"/></TD>
            <TD class="contain" width="75%">
                <html:hidden property="dto(categoryId)" value="${categoryId}"/>
                    ${categoryName}
            </TD>
        </TR>
        <TR>
            <TD class="label" width="25%" nowrap><fmt:message key="CategoryValue.name"/></TD>
            <TD class="contain" width="75%">
                <app:text property="dto(categoryValueName)" styleClass="largetext" maxlength="40"
                          view="${'delete' == op}"/>
            </TD>
        </TR>
        <c:set var="hasSubCategories"
               value="${app2:hasSubCategories(categoryValueForm.dtoMap.categoryId, pageContext.request)}"/>
        <html:hidden property="dto(hasSubCategories)" value="${hasSubCategories}"/>
        <c:if test="${hasSubCategories}">
            <c:if test="${null != childrenCategories && fn:length(childrenCategories) > 0}">
                <tr>
                    <td class="title" colspan="2">
                        <fmt:message key="CategoryValue.relateToSubCategory"/>
                    </td>
                </tr>
            </c:if>
            <c:forEach var="childrenCategory" items="${childrenCategories}">
                <c:set var="canUpdateCategory" value="canUpdateCategory_${childrenCategory.categoryId}"/>
                <tr>
                    <td class="label">
                        <c:out value="${childrenCategory.categoryName}"/>

                        <html:hidden property="dto(categoryName_${childrenCategory.categoryId})"
                                     value="${childrenCategory.categoryName}"/>
                        <html:hidden property="dto(categoryIds)" value="${childrenCategory.categoryId}"
                                     styleId="dto(categoryIds)"/>
                    </td>
                    <td class="contain">
                        <html:checkbox property="dto(${childrenCategory.categoryId})"
                                       value="true"
                                       styleClass="adminCheckBox"
                                       styleId="selectedCategories"
                                       disabled="${'delete' == op || false == categoryValueForm.dtoMap[canUpdateCategory]}"/>

                        <c:if test="${'delete' == op || false == categoryValueForm.dtoMap[canUpdateCategory]}">
                            <html:hidden property="dto(canUpdateCategory_${childrenCategory.categoryId})"/>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
    </table>
    <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
        <TR>
            <TD class="button">
                <app2:securitySubmit operation="${op}" functionality="CATEGORYVALUE" styleClass="button"
                                     property="create">${button}</app2:securitySubmit>
                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}" functionality="CATEGORYVALUE" styleClass="button"
                                         property="SaveAndNew"><fmt:message
                            key="Common.saveAndNew"/></app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
            </TD>
        </TR>
    </table>
</html:form>
