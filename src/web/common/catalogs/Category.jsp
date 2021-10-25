<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<script language="JavaScript">
    function onSubmitForm() {
        document.forms[0].submit();
    }
</script>

<c:set var="categoryAllTypeList" value="${app2:getCategoryTablesListExtended(pageContext.request)}"/>
<c:set var="categoryTypeList" value="${app2:getCategoryTypes(pageContext.request)}"/>

<c:set var="ADDRESS_CATEGORY" value="<%=ContactConstants.ADDRESS_CATEGORY%>"/>
<c:set var="CONTACTPERSON_CATEGORY" value="<%=ContactConstants.CONTACTPERSON_CATEGORY%>"/>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>
<html:form action="${action}" focus="dto(categoryName)">

<html:hidden property="dto(customerMSG)" value='<%= JSPHelper.getMessage(request,"Category.customer")%>'/>
<html:hidden property="dto(contactpersonMSG)" value='<%= JSPHelper.getMessage(request,"Category.contactPerson")%>'/>
<html:hidden property="dto(productMSG)" value='<%= JSPHelper.getMessage(request,"Category.product")%>'/>
<html:hidden property="dto(addressMSG)" value='<%= JSPHelper.getMessage(request,"Category.address")%>'/>

<html:hidden property="dto(op)" value="${op}"/>

<c:if test="${'update'==op || 'delete'==op}">
    <html:hidden property="dto(categoryId)"/>
    <html:hidden property="dto(langTextId)"/>
</c:if>

<c:if test="${'update'==op}">
    <html:hidden property="dto(version)"/>
    <html:hidden property="dto(canUpdateLabel)"/>
    <html:hidden property="dto(canUpdateType)"/>
    <html:hidden property="dto(canUpdateHasSubCategories)"/>
    <html:hidden property="dto(canUpdateParentCategory)"/>
    <html:hidden property="dto(haveFirstTranslation)"/>
</c:if>

<table id="Category.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center"
       class="container">
<tr>
    <td colspan="2" class="title">
        <c:out value="${title}"/>
    </td>
</tr>
<tr>
    <td class="label" width="25%" nowrap>
        <fmt:message key="Category.name"/>
    </td>
    <td class="contain" width="75%">
        <app:text property="dto(categoryName)" styleClass="largetext" maxlength="20"
                  view="${'delete' == op}"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Category.sequence"/>
    </td>
    <td class="contain">
        <app:numberText property="dto(sequence)" style="text" numberType="integer" maxlength="3"
                        view="${'delete' == op}"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Category.language"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(languageId)" listName="systemLanguageList" labelProperty="name"
                      valueProperty="id"
                      firstEmpty="true" styleClass="select"
                      readOnly="${op == 'delete'|| true == categoryForm.dtoMap['haveFirstTranslation']}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Category.label"/>
    </td>
    <td class="contain">
        <html:select property="dto(table)" styleClass="select"
                     readonly="${'delete' == op || false == categoryForm.dtoMap['canUpdateLabel']}"
                     onchange="javascript:onSubmitForm();">
            <html:option value=""/>
            <html:options collection="categoryAllTypeList" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Category.type"/>
    </td>
    <td class="contain">
        <html:select property="dto(categoryType)" styleClass="select"
                     readonly="${'delete' == op || false == categoryForm.dtoMap['canUpdateType']}"
                     onchange="javascript:onSubmitForm();">
            <html:option value=""/>
            <html:options collection="categoryTypeList" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>

<!--show this option only category type is single select-->
<c:if test="${(null ==  categoryForm.dtoMap.parentCategory || empty categoryForm.dtoMap.parentCategory) && app2:isCategorySingleSelect(categoryForm.dtoMap.categoryType)}">
    <tr>
        <td class="label">
            <fmt:message key="Category.hasSubCategories"/>
        </td>
        <td class="contain">
            <html:checkbox property="dto(hasSubCategories)" value="true" onclick="javascript:onSubmitForm();"
                           styleClass="adminCheckBox"
                           disabled="${'delete' == op || false == categoryForm.dtoMap.canUpdateHasSubCategories}"/>
            <c:if test="${'delete' == op || false == categoryForm.dtoMap.canUpdateHasSubCategories}">
                <html:hidden property="dto(hasSubCategories)"/>
            </c:if>
        </td>
    </tr>
</c:if>

<!--Show this option only for categories that can not be sub categories-->
<c:if test="${'true' != categoryForm.dtoMap.hasSubCategories && not empty categoryForm.dtoMap.table}">
    <tr>
        <td class="label">
            <fmt:message key="Category.parentCategory"/>
        </td>
        <td class="contain">
            <fanta:select property="dto(parentCategory)"
                          listName="selectCategoryList"
                          labelProperty="categoryName"
                          valueProperty="categoryId"
                          firstEmpty="true"
                          styleClass="select"
                          readOnly="${'delete' == op || false == categoryForm.dtoMap.canUpdateParentCategory}"
                          onChange="javascript:onSubmitForm();">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="tableId" value="${categoryForm.dtoMap.table}"/>
                <c:if test="${'update' == op || 'delete' == op}">
                    <fanta:parameter field="categoryId" value="${categoryForm.dtoMap.categoryId}"/>
                    <c:if test="${false == categoryForm.dtoMap.canUpdateParentCategory}">
                        <fanta:parameter field="parentCategory" value="${categoryForm.dtoMap.parentCategory}"/>
                    </c:if>
                </c:if>
            </fanta:select>
        </td>
    </tr>
</c:if>

<!--Show this option only for root categories-->
<c:if test="${not empty categoryForm.dtoMap.table}">
    <tr>
        <td class="label">
            <c:set var="IS_ADDRESSCONTACTPERSON"
                   value="${app2:isAddressContactPersonCategory(categoryForm.dtoMap.table)}"/>
            <fmt:message key="${IS_ADDRESSCONTACTPERSON ? 'Category.Group.Address': 'Category.Group'}"/>
        </td>
        <td class="contain">
            <fanta:select property="dto(categoryGroupId)"
                          listName="categoryGroupList"
                          labelProperty="label"
                          valueProperty="categoryGroupId"
                          firstEmpty="true"
                          styleClass="select" readOnly="${'delete' == op}">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="tableId"
                                 value="${IS_ADDRESSCONTACTPERSON ? ADDRESS_CATEGORY : categoryForm.dtoMap.table}"/>
            </fanta:select>
        </td>
    </tr>
    <c:if test="${IS_ADDRESSCONTACTPERSON}">
        <tr>
            <td class="label">
                <fmt:message key="Category.Group.ContactPerson"/>
            </td>
            <td class="contain">
                <fanta:select property="dto(secondGroupId)"
                              listName="categoryGroupList"
                              labelProperty="label"
                              valueProperty="categoryGroupId"
                              firstEmpty="true"
                              styleClass="select" readOnly="${'delete' == op}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="tableId" value="${CONTACTPERSON_CATEGORY}"/>
                </fanta:select>
            </td>
        </tr>
    </c:if>
</c:if>

<c:if test="${'delete'== op && null != categoryForm.dtoMap['categoryValuesSize']}">
    <tr>
        <td class="label">
            <fmt:message key="Category.categoryValues"/>
        </td>
        <td class="contain">
                ${categoryForm.dtoMap['categoryValuesSize']}
        </td>
    </tr>
</c:if>

<tr>
    <TD class="topLabel" colspan="2">
        <fmt:message key="Category.description"/>
        <br>
        <html:textarea property="dto(descriptionText)" styleClass="mediumDetail" style="height:120px;width:99%;"
                       tabindex="5"
                       readonly="${op == 'delete'}"/>
        <html:hidden property="dto(freeTextId)"/>
    </TD>
</tr>

<c:if test="${'true' != categoryForm.dtoMap.hasSubCategories && not empty categoryForm.dtoMap.table && not empty categoryForm.dtoMap.parentCategory}">
    <c:set var="hasSubCategories"
           value="${app2:hasSubCategories(categoryForm.dtoMap.parentCategory, pageContext.request)}"/>

    <c:if test="${hasSubCategories}">
        <c:set var="categoryValues"
               value="${app2:getCategoryValues(categoryForm.dtoMap.parentCategory, pageContext.request)}"/>

        <c:if test="${not empty categoryValues}">
            <tr>
                <td class="title" colspan="2">
                    <fmt:message key="Category.relateToParentCategory"/>
                </td>
            </tr>

            <c:forEach var="categoryValue" items="${categoryValues}">
                <c:set var="canUpdateCategoryValue" value="canUpdateCategoryValue_${categoryValue.categoryValueId}"/>
                <tr>
                    <td class="label">
                        <c:out value="${categoryValue.categoryValueName}"/>

                        <html:hidden property="dto(categoryValueName_${categoryValue.categoryValueId})"
                                     value="${categoryValue.categoryValueName}"/>
                        <html:hidden property="dto(categoryValueIds)"
                                     value="${categoryValue.categoryValueId}"
                                     styleId="dto(categoryValueIds)"/>
                    </td>
                    <td class="contain">
                        <html:checkbox property="dto(${categoryValue.categoryValueId})"
                                       value="true"
                                       styleClass="adminCheckBox"
                                       styleId="selectedCategories"
                                       disabled="${'delete' == op || false == categoryForm.dtoMap[canUpdateCategoryValue]}"/>

                        <c:if test="${'delete' == op || false == categoryForm.dtoMap[canUpdateCategoryValue]}">
                            <html:hidden property="dto(canUpdateCategoryValue_${categoryValue.categoryValueId})"/>
                            <html:hidden property="dto(${categoryValue.categoryValueId})"/>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
    </c:if>
</c:if>


</table>
<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">

    <TR>
        <TD class="button">

            <app2:securitySubmit property="dto(save)" operation="${op}" functionality="CATEGORY"
                                 tabindex="6" styleClass="button">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="CATEGORY" styleClass="button"
                                     property="SaveAndNew" tabindex="7">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button" tabindex="8">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </TD>
    </TR>

</table>
</html:form>
</td>
</tr>
</table>


<br/>
<app2:checkAccessRight functionality="CATEGORYVALUE" permission="VIEW">
    <c:set var="type" value="${categoryForm.dtoMap['categoryType']}"/>
    <c:set var="categoryName"
           value="${categoryForm.dtoMap.categoryName}(${app2:getCategoryTableType(categoryForm.dtoMap.table, pageContext.request)})"/>
    <c:set var="categoryId" value="${categoryForm.dtoMap['categoryId']}"/>

    <c:if test="${op=='update' && (app2:isCategorySingleSelect(type) || app2:isCategoryCompoundSelect(type))}">

        <iframe
                name="frame1"
                src="<app:url value="CategoryValue/List.do?categoryId=${categoryId}&dto(categoryId)=${categoryId}&dto(categoryName)=${app2:encode(categoryName)}&dto(categoryType)=${type}" />"
                class="Iframe"
                scrolling="no"
                id="iFrameId"
                frameborder="0">
        </iframe>

    </c:if>
</app2:checkAccessRight>

