<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
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


<html:form action="/Question/AdvancedSearch.do" focus="parameter(summary)" styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="Question.Title.advancedSearch"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="summary_id">
                        <fmt:message key="Question.summary"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(summary)"
                                   styleId="summary_id"
                                   styleClass="middleText ${app2:getFormInputClasses()}"
                                   tabindex="1"/>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="ownerName1@_ownerName2@_searchName_id">
                        <fmt:message key="Question.askedBy"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(ownerName1@_ownerName2@_searchName)"
                                   styleId="ownerName1@_ownerName2@_searchName_id"
                                   styleClass="largeText ${app2:getFormInputClasses()}"
                                   tabindex="2"/>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="productName_id">
                        <fmt:message key="Article.productName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(productName)"
                                   styleId="productName_id"
                                   styleClass="middleText ${app2:getFormInputClasses()}"
                                   tabindex="3"/>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="value_id">
                        <fmt:message key="Question.detail"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(value)"
                                   styleId="value_id"
                                   styleClass="largeText ${app2:getFormInputClasses()}"
                                   tabindex="4"/>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="categoryId_id">
                        <fmt:message key="Article.categoryName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(categoryId)"
                                      listName="articleCategoryList"
                                      firstEmpty="true"
                                      labelProperty="name"
                                      styleId="categoryId_id"
                                      valueProperty="id"
                                      module="/catalogs"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      tabIndex="5">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:tree columnId="id" columnParentId="parentCategoryId"
                                        separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
                        </fanta:select>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="ask1">
                        <fmt:message key="Question.AskedOn"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="datePattern" var="datePattern"/>
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(creationDateFrom)"
                                                  maxlength="10" tabindex="6"
                                                  styleId="ask1"
                                                  placeHolder="${from}"
                                                  calendarPicker="true"
                                                  mode="bootstrap"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  parseLongAsDate="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.to" var="to"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(creationDateTo)"
                                                  maxlength="10"
                                                  tabindex="7"
                                                  mode="bootstrap"
                                                  placeHolder="${to}"
                                                  styleId="ask2"
                                                  calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  parseLongAsDate="true"/>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="answer_id">
                        <fmt:message key="Question.answered"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:select property="parameter(answer)"
                                     styleId="answer_id"
                                     styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                     tabindex="8">
                            <html:option value=""/>
                            <html:options collection="answerList" property="value" labelProperty="label"/>
                        </html:select>
                    </div>
                </div>
            </div>

            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="9"><fmt:message
                        key="Common.go"/></html:submit>
                <html:button property="reset" tabindex="10" styleClass="${app2:getFormButtonClasses()}" onclick="myReset()">
                    <fmt:message key="Common.clear"/></html:button>
            </div>
        </fieldset>
    </div>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="Question/AdvancedSearch.do" mode="bootstrap" parameterName="summaryAlphabet"/>
    </div>

</html:form>

<app2:checkAccessRight functionality="QUESTION" permission="CREATE">
    <c:set var="newButtonsTable" scope="page">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <tags:buttonsTable>
                <app:url value="/Question/Forward/Create.do?advancedListForward=QuestionAdvancedSearch"
                         addModuleParams="false" var="newQuestionUrl"/>
                <input type="button" class="${app2:getFormButtonClasses()}" value="<fmt:message key="Common.new"/>"
                       onclick="window.location ='${newQuestionUrl}'">
            </tags:buttonsTable>
        </div>
    </c:set>
</app2:checkAccessRight>


<c:out value="${newButtonsTable}" escapeXml="false"/>

<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<div class="table-responsive">
    <fanta:table mode="bootstrap" width="100%" styleClass="${app2:getFantabulousTableClases()}" id="question" action="Question/AdvancedSearch.do"
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
                                    headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>
            <fanta:actionColumn name="delete" styleClass="listItem" headerStyle="listHeader" width="34%">
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
            <fanta:actionColumn name="view" styleClass="listItem" headerStyle="listHeader" width="32%">
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
</div>

<c:out value="${newButtonsTable}" escapeXml="false"/>
