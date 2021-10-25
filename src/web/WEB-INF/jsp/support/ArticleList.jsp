<%@ include file="/Includes.jsp" %>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>

<div class="${app2:getListWrapperClasses()}">

    <html:form action="/ArticleList.do"
               focus="parameter(articleTitle@_number)"
               styleClass="form-horizontal">

        <legend class="title">
            <fmt:message key="Article.Title.search"/>
        </legend>

        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(articleTitle@_number)"
                               styleClass="largeText ${app2:getFormInputClasses()}"
                               maxlength="80"/>
                        <span class="input-group-btn">
                            <html:submit styleClass="${app2:getFormButtonClasses()}">
                                <fmt:message key="Common.go"/>
                            </html:submit>

                        </span>
                </div>
            </div>

            <div class="pull-left">
                <html:link action="/Article/AdvancedSearch.do" styleClass="btn btn-link">
                    <fmt:message key="Common.advancedSearch"/>
                </html:link>
            </div>
        </div>

    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="ArticleList.do" parameterName="articleTitleAlphabet" mode="bootstrap"/>
    </div>

    <app2:checkAccessRight functionality="ARTICLE" permission="CREATE">
        <c:set var="newButtonsTable" scope="page">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <tags:buttonsTable>
                    <app:url value="/Article/Forward/Create.do"
                             addModuleParams="false" var="newArticleUrl"/>
                    <input type="button" class="${app2:getFormButtonClasses()}" value="<fmt:message key="Common.new"/>"
                           onclick="window.location ='${newArticleUrl}'">
                </tags:buttonsTable>
            </div>
        </c:set>
    </app2:checkAccessRight>

    <c:out value="${newButtonsTable}" escapeXml="false"/>

    <fmt:message var="dateTimePattern" key="dateTimePattern"/>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="articleList" width="100%" id="article" action="ArticleList.do"
                     imgPath="${baselayout}"
                     styleClass="${app2:getFantabulousTableLargeClases()}"
                     align="center">
            <c:set var="editLink"
                   value="Article/Forward/Update.do?dto(articleId)=${article.articleId}&dto(articleTitle)=${app2:encode(article.articleTitle)}&articleId=${article.articleId}&index=0"/>
            <c:set var="deleteLink"
                   value="Article/Forward/Delete.do?dto(withReferences)=true&dto(articleId)=${article.articleId}&dto(articleTitle)=${app2:encode(article.articleTitle)}&articleId=${article.articleId}&index=0"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <fanta:actionColumn name="edit" title="Common.update" styleClass="listItem"
                                    headerStyle="listHeader"
                                    width="50%">
                    <app2:checkAccessRight functionality="ARTICLE" permission="VIEW">
                        <html:link action="${editLink}" titleKey="Common.update">
                            <span class="${app2:getClassGlyphEdit()}"></span>
                        </html:link>
                    </app2:checkAccessRight>
                </fanta:actionColumn>
                <fanta:actionColumn name="delete" title="Common.delete" styleClass="listItem"
                                    headerStyle="listHeader" width="50%">
                    <app2:checkAccessRight functionality="ARTICLE" permission="DELETE">
                        <c:choose>
                            <c:when test="${article.createUserId == sessionScope.user.valueMap['userId']}">
                                <html:link action="${deleteLink}" titleKey="Common.delete">
                                    <span class="${app2:getClassGlyphTrash()}"></span>
                                </html:link>
                            </c:when>
                            <c:otherwise>
                                &nbsp;
                            </c:otherwise>
                        </c:choose>
                    </app2:checkAccessRight>
                </fanta:actionColumn>
            </fanta:columnGroup>
            <fanta:dataColumn name="number" action="${editLink}" styleClass="listItem"
                              title="Article.number"
                              headerStyle="listHeader" width="7%" orderable="true"/>
            <fanta:dataColumn name="articleTitle" action="${editLink}" styleClass="listItem"
                              title="Article.title"
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
    </div>

    <c:out value="${newButtonsTable}" escapeXml="false"/>

</div>