<%@ include file="/Includes.jsp" %>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
    <td>
        <br>
        <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0">
            <tr>
                <td colspan="2" class="title">
                    <fmt:message key="Article.Title.search"/>
                    <br>
                </td>
            </tr>
            <tr>
                <html:form action="/ArticleList.do" focus="parameter(articleTitle@_number)">
                    <td class="label"><fmt:message key="Common.search"/></td>
                    <td align="left" class="contain">
                        <html:text property="parameter(articleTitle@_number)" styleClass="largeText"
                                   maxlength="80"/>
                        &nbsp;
                        <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
                        <html:link action="/Article/AdvancedSearch.do">&nbsp;<fmt:message
                                key="Common.advancedSearch"/></html:link>
                    </td>
                </html:form>
            </tr>
            <tr>
                <td colspan="2" align="center" class="alpha">
                    <fanta:alphabet action="ArticleList.do" parameterName="articleTitleAlphabet"/>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td colspan="2">
        <br>
        <app2:checkAccessRight functionality="ARTICLE" permission="CREATE">
            <c:set var="newButtonsTable" scope="page">
                <tags:buttonsTable>
                    <app:url value="/Article/Forward/Create.do"
                             addModuleParams="false" var="newArticleUrl"/>
                    <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                           onclick="window.location ='${newArticleUrl}'">
                </tags:buttonsTable>
            </c:set>
        </app2:checkAccessRight>
        <c:out value="${newButtonsTable}" escapeXml="false"/>

        <fmt:message var="dateTimePattern" key="dateTimePattern"/>
        <fanta:table list="articleList" width="100%" id="article" action="ArticleList.do" imgPath="${baselayout}"
                     align="center">
            <c:set var="editLink"
                   value="Article/Forward/Update.do?dto(articleId)=${article.articleId}&dto(articleTitle)=${app2:encode(article.articleTitle)}&articleId=${article.articleId}&index=0"/>
            <c:set var="deleteLink"
                   value="Article/Forward/Delete.do?dto(withReferences)=true&dto(articleId)=${article.articleId}&dto(articleTitle)=${app2:encode(article.articleTitle)}&articleId=${article.articleId}&index=0"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <fanta:actionColumn name="edit" title="Common.update" styleClass="listItem" headerStyle="listHeader"
                                    width="50%">
                    <app2:checkAccessRight functionality="ARTICLE" permission="VIEW">
                        <html:link action="${editLink}" titleKey="Common.update">
                            <html:img src="${baselayout}/img/edit.gif" titleKey="Common.update" border="0"/>
                        </html:link>
                    </app2:checkAccessRight>
                </fanta:actionColumn>
                <fanta:actionColumn name="delete" title="Common.delete" styleClass="listItem"
                                    headerStyle="listHeader" width="50%">
                    <app2:checkAccessRight functionality="ARTICLE" permission="DELETE">
                        <c:choose>
                            <c:when test="${article.createUserId == sessionScope.user.valueMap['userId']}">
                                <html:link action="${deleteLink}" titleKey="Common.delete">
                                    <html:img src="${baselayout}/img/delete.gif" titleKey="Common.delete"
                                              border="0"/>
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
                              headerStyle="listHeader" width="7%" orderable="true"/>
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
                <fanta:textShorter
                        title="${app2:getDateWithTimeZone(article.creationDate, timeZone, dateTimePattern)}">
                    ${app2:getDateWithTimeZone(article.creationDate, timeZone, dateTimePattern)}
                </fanta:textShorter>
            </fanta:dataColumn>
        </fanta:table>
        <c:out value="${newButtonsTable}" escapeXml="false"/>
    </td>
</tr>
</table>