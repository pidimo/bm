<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">

    <html:form action="/QuestionList.do" focus="parameter(summary)" styleClass="form-horizontal">

        <fieldset>
            <legend class="title">
                <fmt:message key="Question.questionList"/>
            </legend>
        </fieldset>

        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="number_id">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(summary)"
                               styleClass="largeText ${app2:getFormInputClasses()}"
                               maxlength="80"/>
                    <div class="input-group-btn">
                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.go"/>
                        </html:submit>

                    </div>
                </div>
            </div>

            <div class="pull-left">
                <html:link action="/Question/AdvancedSearch.do" styleClass="btn btn-link">
                    <fmt:message key="Common.advancedSearch"/>
                </html:link>
            </div>
        </div>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="QuestionList.do" parameterName="summaryAlphabet" mode="bootstrap"/>
    </div>

    <app2:checkAccessRight functionality="QUESTION" permission="CREATE">

        <c:set var="newButtonsTable" scope="page">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <app:url value="/Question/Forward/Create.do"
                         addModuleParams="false" var="newQuestionUrl"/>
                <input type="button" class="${app2:getFormButtonClasses()}" value="<fmt:message key="Common.new"/>"
                       onclick="window.location ='${newQuestionUrl}'">
            </div>
        </c:set>

    </app2:checkAccessRight>

    <c:out value="${newButtonsTable}" escapeXml="false"/>
    <fmt:message var="dateTimePattern" key="dateTimePattern"/>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" width="100%" id="question" styleClass="${app2:getFantabulousTableClases()}"
                     action="QuestionList.do" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="Question/Forward/Update.do?dto(summary)=${app2:encode(question.summary)}&dto(questionId)=${question.id}&dto(userId)=${sessionScope.user.valueMap['userId']}"/>

            <c:set var="deleteAction"
                   value="Question/Forward/Delete.do?dto(questionId)=${question.id}&dto(withReferences)=true&dto(summary)=${app2:encode(question.summary)}&dto(userId)=${sessionScope.user.valueMap['userId']}"/>

            <c:set var="viewArticleAction"
                   value="Article/Forward/Update.do?dto(articleTitle)=${app2:encode(question.articleTitle)}&dto(articleId)=${question.articleId}&articleId=${question.articleId}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="QUESTION" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <fanta:actionColumn name="delete" styleClass="listItem" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="QUESTION" permission="DELETE">
                        <c:choose>
                            <c:when test="${question.createUserId == sessionScope.user.valueMap['userId']}">
                                <html:link action="${deleteAction}" titleKey="Common.delete">
                                    <span class="${app2:getClassGlyphTrash()}"></span>
                                </html:link>
                            </c:when>
                            <c:otherwise>
                                &nbsp;
                            </c:otherwise>
                        </c:choose>
                    </app2:checkAccessRight>
                </fanta:actionColumn>
                <fanta:actionColumn name="view" styleClass="listItem" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="ARTICLE" permission="VIEW">
                        <c:choose>
                            <c:when test="${(question.rootQuestionId != '' && app2:userWithAccessToArticleOnDataLevel(question.articleId, pageContext.request))}">
                                <html:link action="${viewArticleAction}" titleKey="Article.viewArticle">
                                    <span class="${app2:getClassGlyphViewArticleAnswer()}"></span>
                                </html:link>
                            </c:when>
                            <c:otherwise>
                                &nbsp;
                            </c:otherwise>
                        </c:choose>
                    </app2:checkAccessRight>
                </fanta:actionColumn>
            </fanta:columnGroup>
            <fanta:dataColumn name="summary" action="${editAction}" styleClass="listItem"
                              title="Question.summary"
                              headerStyle="listHeader" width="25%" orderable="true"/>
            <fanta:dataColumn name="categoryName" styleClass="listItem" title="Article.categoryName"
                              headerStyle="listHeader" width="15%" orderable="true"/>
            <fanta:dataColumn name="productName" styleClass="listItem" title="Article.productName"
                              headerStyle="listHeader" width="15%" orderable="true"/>
            <fanta:dataColumn name="ownerName" styleClass="listItem" title="Question.askedBy"
                              headerStyle="listHeader" width="25%" orderable="true"/>
            <fanta:dataColumn name="creationDate" styleClass="listItem2${expireClass}"
                              title="Question.AskedOn"
                              headerStyle="listHeader" width="15%" orderable="true" renderData="false"
                              nowrap="nowrap">
                <fanta:textShorter title="${Question.AskedOn}">
                    ${app2:getDateWithTimeZone(question.creationDate, timeZone, dateTimePattern)}
                </fanta:textShorter>
            </fanta:dataColumn>
        </fanta:table>
    </div>

    <c:out value="${newButtonsTable}" escapeXml="false"/>

</div>