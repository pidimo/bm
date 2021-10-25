<%@ include file="/Includes.jsp" %>
<calendar:initialize/>

<%
    pageContext.setAttribute("answerList", JSPHelper.getAnswerList(request));
%>
<script>
    function myReset() {
        var form = document.questionListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>

<br>
<table border="0" cellpadding="0" cellspacing="0" width="98%" class="container" align="center">
<tr>
    <td>

        <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0">
            <tr>
                <td colspan="4" class="title">
                    <fmt:message key="Question.Title.advancedSearch"/>
                    <br>
                </td>
            </tr>

            <html:form action="/Question/AdvancedSearch.do" focus="parameter(summary)">
                <TR>
                    <TD class="label" width="12%">
                        <fmt:message key="Question.summary"/>
                    </td>
                    <td class="contain" width="30%">
                        <html:text property="parameter(summary)" styleClass="middleText" tabindex="1"/>
                    </TD>
                    <TD class="label" width="13%"><fmt:message key="Question.askedBy"/></TD>
                    <TD class="contain" width="40%">
                        <html:text property="parameter(ownerName1@_ownerName2@_searchName)" styleClass="largeText"
                                   tabindex="5"/>
                    </TD>

                </TR>

                <TR>
                    <TD class="label"><fmt:message key="Article.productName"/></TD>
                    <TD class="contain">
                        <html:text property="parameter(productName)" styleClass="middleText" tabindex="2"/>
                    </TD>
                    <TD class="label">
                        <fmt:message key="Question.detail"/>
                    </td>
                    <TD class="contain">
                        <html:text property="parameter(value)" styleClass="largeText" tabindex="6"/>
                    </TD>
                </TR>
                <TR>

                    <TD class="label"><fmt:message key="Article.categoryName"/></TD>
                    <TD class="contain">
                        <fanta:select property="parameter(categoryId)" listName="articleCategoryList" firstEmpty="true"
                                      labelProperty="name" valueProperty="id" module="/catalogs"
                                      styleClass="middleSelect"
                                      tabIndex="3">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:tree columnId="id" columnParentId="parentCategoryId"
                                        separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
                        </fanta:select>
                    </TD>
                    <TD class="label">
                        <fmt:message key="Question.AskedOn"/>
                    </td>
                    <td class="contain">
                        <fmt:message key="datePattern" var="datePattern"/>
                        <fmt:message key="Common.from"/>
                        &nbsp;
                        <app:dateText property="parameter(creationDateFrom)" maxlength="10" tabindex="7" styleId="ask1"
                                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                                      parseLongAsDate="true"/>
                        &nbsp;<fmt:message key="Common.to"/>&nbsp;
                        <app:dateText property="parameter(creationDateTo)" maxlength="10" tabindex="8" styleId="ask2"
                                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                                      parseLongAsDate="true"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="label"><fmt:message key="Question.answered"/></TD>
                    <TD class="contain">
                        <html:select property="parameter(answer)" styleClass="middleSelect" tabindex="4">
                            <html:option value=""/>
                            <html:options collection="answerList" property="value" labelProperty="label"/>
                        </html:select>
                    </TD>
                    <TD colspan="2" class="label" style="text-align:right">
                        <html:submit styleClass="button" tabindex="9"><fmt:message key="Common.go"/></html:submit>
                        <html:button property="reset" tabindex="10" styleClass="button" onclick="myReset()">
                            <fmt:message key="Common.clear"/></html:button>
                    </TD>
                </TR>
                <!-- choose alphbet to simple and advanced search -->
                <tr>
                    <td colspan="4" align="center" class="alpha">
                        <fanta:alphabet action="Question/AdvancedSearch.do" parameterName="summaryAlphabet"/>
                    </td>
                </tr>
            </html:form>
        </table>
    </td>
</tr>
<tr>
    <td colspan="2">
        <br>
        <app2:checkAccessRight functionality="QUESTION" permission="CREATE">
            <c:set var="newButtonsTable" scope="page">
                <tags:buttonsTable>
                    <app:url value="/Question/Forward/Create.do?advancedListForward=QuestionAdvancedSearch"
                             addModuleParams="false" var="newQuestionUrl"/>
                    <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                           onclick="window.location ='${newQuestionUrl}'">
                </tags:buttonsTable>
            </c:set>
        </app2:checkAccessRight>
        <c:out value="${newButtonsTable}" escapeXml="false"/>
        <fmt:message var="dateTimePattern" key="dateTimePattern"/>
        <fanta:table width="100%" id="question" action="Question/AdvancedSearch.do"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="Question/Forward/Update.do?dto(summary)=${app2:encode(question.summary)}&dto(questionId)=${question.id}&dto(userId)=${sessionScope.user.valueMap['userId']}&advancedListForward=QuestionAdvancedSearch"/>

            <c:set var="deleteAction"
                   value="Question/Forward/Delete.do?dto(questionId)=${question.id}&dto(withReferences)=true&dto(summary)=${app2:encode(question.summary)}&dto(userId)=${sessionScope.user.valueMap['userId']}&advancedListForward=QuestionAdvancedSearch"/>

            <c:set var="viewArticleAction"
                   value="Article/Forward/Update.do?dto(articleId)=${question.articleId}&articleId=${question.articleId}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="9%">
                <app2:checkAccessRight functionality="QUESTION" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader" image="${baselayout}/img/edit.gif" width="34%"/>
                </app2:checkAccessRight>
                <fanta:actionColumn name="delete" styleClass="listItem" headerStyle="listHeader" width="34%">
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
                <fanta:actionColumn name="view" styleClass="listItem" headerStyle="listHeader" width="32%">
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
                              headerStyle="listHeader" width="21%" orderable="true"/>
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
