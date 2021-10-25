<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">

    <c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
    <fmt:message var="dateTimePattern" key="dateTimePattern"/>

    <html:form action="/Article/Relation/Forward/Create.do"
               focus="parameter(articleTitle@_number)"
               styleClass="form-horizontal">

        <fieldset>
            <legend class="title">
                <fmt:message key="Article.articleImportList"/>
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
            </div>
        </fieldset>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="Article/Relation/Forward/Create.do"
                        mode="bootstrap"
                        parameterName="articleTitleAlphabet"/>
    </div>

    <html:form action="/Article/Relation/Create.do?cancel=true">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:cancel styleClass="${app2:getFormButtonClasses()}" property="dto(cancel)">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </html:form>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="importArticleList" width="100%" styleClass="${app2:getFantabulousTableClases()}" id="article"
                     action="Article/Relation/Forward/Create.do" imgPath="${baselayout}" align="center">
            <c:set var="importAction"
                   value="Article/Relation/Create.do?dto(articleName)=${app2:encode(article.articleTitle)}&dto(relatedArticleId)=${article.articleId}&dto(op)=create"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <fanta:actionColumn name="import" title="Common.import" action="${importAction}"
                                    styleClass="listItem" headerStyle="listHeader" width="2%"
                                    glyphiconClass="${app2:getClassGlyphImport()}"/>
            </fanta:columnGroup>
            <fanta:dataColumn name="number" styleClass="listItem" title="Article.number"
                              headerStyle="listHeader"
                              width="5%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="articleTitle" styleClass="listItem" title="Article.title"
                              headerStyle="listHeader" width="15%" orderable="true"/>
            <fanta:dataColumn name="categoryName" styleClass="listItem" title="Article.categoryName"
                              headerStyle="listHeader" width="15%" orderable="true"/>
            <fanta:dataColumn name="productName" styleClass="listItem" title="Article.productName"
                              headerStyle="listHeader" width="15%" orderable="true"/>
            <fanta:dataColumn name="ownerName" styleClass="listItem" title="Article.ownerName"
                              headerStyle="listHeader" width="15%" orderable="true"/>
            <fanta:dataColumn name="creationDate" styleClass="listItem${expireClass}" title="Article.createDate"
                              headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                ${app2:getDateWithTimeZone(article.creationDate, timeZone, dateTimePattern)}&nbsp;
            </fanta:dataColumn>
        </fanta:table>
    </div>

</div>