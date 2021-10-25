<%@ include file="/Includes.jsp" %>

<br>
<table border="0" cellpadding="0" cellspacing="0" width="98%" class="container" align="center">
<tr>
    <td>

        <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0">
            <tr>
                <td colspan="2" class="title">
                    <fmt:message key="Question.questionList"/>
                    <br>
                </td>
            </tr>
            <TR>
                <html:form action="/QuestionList.do" focus="parameter(summary)">
                    <td class="label"><fmt:message key="Common.search"/></td>
                    <td align="left" class="contain">
                        <html:text property="parameter(summary)" styleClass="largeText" maxlength="80"/>
                        &nbsp;
                        <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
                        <html:link action="/Question/AdvancedSearch.do">&nbsp;<fmt:message
                                key="Common.advancedSearch"/></html:link>

                    </td>
                </html:form>
            </TR>
            <tr>
                <td colspan="2" align="center" class="alpha">
                    <fanta:alphabet action="QuestionList.do" parameterName="summaryAlphabet"/>
                </td>
            </tr>
        </table>

    </td>
</tr>
<tr>
    <td colspan="2">
        <br>
        <app2:checkAccessRight functionality="QUESTION" permission="CREATE">
            <c:set var="newButtonsTable" scope="page">
                <tags:buttonsTable>
                    <app:url value="/Question/Forward/Create.do"
                             addModuleParams="false" var="newQuestionUrl"/>
                    <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                           onclick="window.location ='${newQuestionUrl}'">
                </tags:buttonsTable>
            </c:set>
        </app2:checkAccessRight>
        <c:out value="${newButtonsTable}" escapeXml="false"/>
        <fmt:message var="dateTimePattern" key="dateTimePattern"/>
        <fanta:table width="100%" id="question" action="QuestionList.do" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="Question/Forward/Update.do?dto(summary)=${app2:encode(question.summary)}&dto(questionId)=${question.id}&dto(userId)=${sessionScope.user.valueMap['userId']}"/>

            <c:set var="deleteAction"
                   value="Question/Forward/Delete.do?dto(questionId)=${question.id}&dto(withReferences)=true&dto(summary)=${app2:encode(question.summary)}&dto(userId)=${sessionScope.user.valueMap['userId']}"/>

            <c:set var="viewArticleAction"
                   value="Article/Forward/Update.do?dto(articleTitle)=${app2:encode(question.articleTitle)}&dto(articleId)=${question.articleId}&articleId=${question.articleId}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="QUESTION" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader" image="${baselayout}/img/edit.gif"/>
                </app2:checkAccessRight>
                <fanta:actionColumn name="delete" styleClass="listItem" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="QUESTION" permission="DELETE">
                        <c:choose>
                            <c:when test="${question.createUserId == sessionScope.user.valueMap['userId']}">
                                <html:link action="${deleteAction}" titleKey="Common.delete">
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
                <fanta:actionColumn name="view" styleClass="listItem" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="ARTICLE" permission="VIEW">
                        <c:choose>
                            <c:when test="${(question.rootQuestionId != '' && app2:userWithAccessToArticleOnDataLevel(question.articleId, pageContext.request))}">
                                <html:link action="${viewArticleAction}" titleKey="Article.viewArticle">
                                    <html:img src="${baselayout}/img/answer.gif" titleKey="Article.viewArticle"
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
            <fanta:dataColumn name="summary" action="${editAction}" styleClass="listItem" title="Question.summary"
                              headerStyle="listHeader" width="25%" orderable="true"/>
            <fanta:dataColumn name="categoryName" styleClass="listItem" title="Article.categoryName"
                              headerStyle="listHeader" width="15%" orderable="true"/>
            <fanta:dataColumn name="productName" styleClass="listItem" title="Article.productName"
                              headerStyle="listHeader" width="15%" orderable="true"/>
            <fanta:dataColumn name="ownerName" styleClass="listItem" title="Question.askedBy"
                              headerStyle="listHeader" width="25%" orderable="true"/>
            <fanta:dataColumn name="creationDate" styleClass="listItem2${expireClass}" title="Question.AskedOn"
                              headerStyle="listHeader" width="15%" orderable="true" renderData="false"
                              nowrap="nowrap">
                <fanta:textShorter title="${Question.AskedOn}">
                    ${app2:getDateWithTimeZone(question.creationDate, timeZone, dateTimePattern)}
                </fanta:textShorter>
            </fanta:dataColumn>
        </fanta:table>
        <c:out value="${newButtonsTable}" escapeXml="false"/>
    </td>
</tr>
</table>
