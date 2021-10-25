<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>

<script>
    function myReset() {
        var form = document.articleListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>

<table border="0" cellpadding="3" cellspacing="0" width="100%" class="container" align="center">
<tr>
    <td height="20" class="title" colspan="4"><fmt:message key="Article.Title.advancedSearch"/></td>
</tr>

<html:form action="/Article/AdvancedSearch.do" focus="parameter(number)">
    <TR>
        <TD width="15%" class="label"><fmt:message key="Article.number"/></TD>
        <TD class="contain" width="35%">
            <html:text property="parameter(number)" styleClass="largeText" tabindex="1" maxlength="10"/>
        </TD>
        <TD width="15%" class="label"><fmt:message key="Article.keywords"/></TD>
        <TD width="35%" class="contain">
            <html:text property="parameter(keywords)" styleClass="largeText" tabindex="5"/>
        </TD>
    </TR>
    <TR>
        <TD class="label"><fmt:message key="Article.title"/></TD>
        <TD class="contain">
            <html:text property="parameter(articleTitle)" styleClass="largeText" tabindex="2"/>
        </TD>
        <TD class="label">
            <fmt:message key="Article.productName"/>
        </td>
        <td class="contain">
            <html:text property="parameter(productName)" styleClass="largeText" tabindex="6"/>
        </TD>
    </TR>

    <TR>
        <TD class="label"><fmt:message key="Article.content"/></TD>
        <TD class="contain">
            <html:text property="parameter(content)" styleClass="largeText" tabindex="3"/>
        </TD>
        <TD class="label">
            <fmt:message key="Common.creationDate"/>
        </td>
        <TD class="contain">
            <fmt:message key="datePattern" var="datePattern"/>
            <app:dateText property="parameter(creationDateFrom)" maxlength="10" tabindex="7" styleId="startDate"
                          calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                          parseLongAsDate="true"/>
            &nbsp;<fmt:message key="Common.to"/>&nbsp;
            <app:dateText property="parameter(creationDateTo)" maxlength="10" tabindex="8" styleId="endDate"
                          calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                          parseLongAsDate="true"/>
        </TD>
    </TR>

    <TR>
        <TD class="label"><fmt:message key="Article.categoryName"/></TD>
        <TD class="contain" colspan="3">
            <fanta:select property="parameter(categoryId)" listName="articleCategoryList" firstEmpty="true"
                          labelProperty="name" valueProperty="id" module="/catalogs" styleClass="middleSelect"
                          tabIndex="4">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:tree columnId="id" columnParentId="parentCategoryId" separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
            </fanta:select>
        </TD>
    </TR>

    <tr>
        <td colspan="4" align="center" class="alpha">
            <fanta:alphabet action="Article/AdvancedSearch.do" parameterName="articleTitleAlphabet"/>
        </td>
    </tr>
    <tr>
        <td colspan="4" class="button">
            <html:submit styleClass="button" tabindex="10"><fmt:message key="Common.go"/></html:submit>
            <html:button property="reset1" tabindex="11" styleClass="button" onclick="myReset()"><fmt:message
                    key="Common.clear"/></html:button>
        </td>
    </tr>
</html:form>


<tr>
    <td colspan="4">
        <br>
        <app2:checkAccessRight functionality="ARTICLE" permission="CREATE">
            <c:set var="newButtonsTable" scope="page">
                <tags:buttonsTable>
                    <app:url value="/Article/Forward/Create.do?advancedListForward=ArticleAdvancedSearch"
                             addModuleParams="false" var="newArticleUrl"/>
                    <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                           onclick="window.location ='${newArticleUrl}'">
                </tags:buttonsTable>
            </c:set>
        </app2:checkAccessRight>
        <c:out value="${newButtonsTable}" escapeXml="false"/>

        <fmt:message var="dateTimePattern" key="dateTimePattern"/>

        <fanta:table list="articleAdvancedSearch" width="100%" id="article" action="Article/AdvancedSearch.do"
                     imgPath="${baselayout}" align="center">
            <c:set var="editLink"
                   value="Article/Forward/Update.do?dto(articleId)=${article.articleId}&dto(articleTitle)=${app2:encode(article.articleTitle)}&articleId=${article.articleId}&index=0&advancedListForward=ArticleAdvancedSearch"/>
            <c:set var="deleteLink"
                   value="Article/Forward/Delete.do?dto(withReferences)=true&dto(articleId)=${article.articleId}&dto(articleTitle)=${app2:encode(article.articleTitle)}&articleId=${article.articleId}&index=0&advancedListForward=ArticleAdvancedSearch"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                <fanta:actionColumn name="edit" title="Common.update" styleClass="listItem" headerStyle="listHeader"
                                    width="50%">
                    <app2:checkAccessRight functionality="ARTICLE" permission="VIEW">
                        <html:link action="${editLink}" titleKey="Common.update">
                            <html:img src="${baselayout}/img/edit.gif" titleKey="Common.delete" border="0"/>
                        </html:link>
                    </app2:checkAccessRight>
                </fanta:actionColumn>

                <fanta:actionColumn name="delete" title="Common.delete" styleClass="listItem" headerStyle="listHeader"
                                    width="50%">
                    <app2:checkAccessRight functionality="ARTICLE" permission="DELETE">
                        <c:choose>
                            <c:when test="${article.createUserId == sessionScope.user.valueMap['userId']}">
                                <html:link action="${deleteLink}" titleKey="Common.delete">
                                    <html:img src="${baselayout}/img/delete.gif" titleKey="Common.delete" border="0"/>
                                </html:link>
                            </c:when>
                            <c:otherwise>
                                &nbsp;
                            </c:otherwise>
                        </c:choose>
                    </app2:checkAccessRight>
                </fanta:actionColumn>
            </fanta:columnGroup>
            <fanta:dataColumn name="number" action="${editLink}" styleClass="listItem" title="Article.number"
                              headerStyle="listHeader"
                              width="7%" orderable="true"/>
            <fanta:dataColumn name="articleTitle" action="${editLink}" styleClass="listItem" title="Article.title"
                              headerStyle="listHeader" width="25%" orderable="true"/>
            <fanta:dataColumn name="categoryName" styleClass="listItem" title="Article.categoryName"
                              headerStyle="listHeader" width="10%" orderable="true"/>
            <fanta:dataColumn name="productName" styleClass="listItem" title="Article.productName"
                              headerStyle="listHeader" width="20%" orderable="true"/>
            <fanta:dataColumn name="ownerName" styleClass="listItem" title="Article.ownerName"
                              headerStyle="listHeader" width="18%" orderable="true"/>
            <fanta:dataColumn name="creationDate" styleClass="listItem2" title="Article.createDate"
                              headerStyle="listHeader" width="15%" orderable="true" renderData="false">
                <fanta:textShorter title="${Article.createDate}">
                    ${app2:getDateWithTimeZone(article.creationDate, timeZone, dateTimePattern)}
                </fanta:textShorter>
            </fanta:dataColumn>
        </fanta:table>
        <c:out value="${newButtonsTable}" escapeXml="false"/>
    </td>
</tr>
</table>