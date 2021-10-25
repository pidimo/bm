<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
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

<html:form action="/Article/AdvancedSearch.do" focus="parameter(number)" styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>

            <legend class="title">
                <fmt:message key="Article.Title.advancedSearch"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Article.number"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(number)"
                                   styleClass="largeText ${app2:getFormInputClasses()}"
                                   tabindex="1"
                                   maxlength="10"/>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Article.keywords"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(keywords)"
                                   styleClass="largeText ${app2:getFormInputClasses()}"
                                   tabindex="2"/>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Article.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(articleTitle)"
                                   styleClass="largeText ${app2:getFormInputClasses()}"
                                   tabindex="3"/>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Article.productName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(productName)"
                                   styleClass="largeText ${app2:getFormInputClasses()}"
                                   tabindex="4"/>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Article.content"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(content)"
                                   styleClass="largeText ${app2:getFormInputClasses()}"
                                   tabindex="5"/>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Common.creationDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="datePattern" var="datePattern"/>
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(creationDateFrom)"
                                                  maxlength="10"
                                                  tabindex="6"
                                                  styleId="startDate"
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
                                                  styleId="endDate"
                                                  mode="bootstrap"
                                                  placeHolder="${to}"
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
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Article.categoryName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(categoryId)"
                                      listName="articleCategoryList"
                                      firstEmpty="true"
                                      labelProperty="name"
                                      valueProperty="id"
                                      module="/catalogs"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      tabIndex="8">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:tree columnId="id" columnParentId="parentCategoryId"
                                        separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
                        </fanta:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="9"><fmt:message
                            key="Common.go"/></html:submit>
                    <html:button property="reset1" tabindex="10" styleClass="${app2:getFormButtonClasses()}"
                                 onclick="myReset()">
                        <fmt:message key="Common.clear"/>
                    </html:button>
                </div>
            </div>

        </fieldset>
    </div>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="Article/AdvancedSearch.do"
                        mode="bootstrap"
                        parameterName="articleTitleAlphabet"/>
    </div>
</html:form>

<app2:checkAccessRight functionality="ARTICLE" permission="CREATE">
    <c:set var="newButtonsTable" scope="page">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app:url value="/Article/Forward/Create.do?advancedListForward=ArticleAdvancedSearch"
                     addModuleParams="false" var="newArticleUrl"/>
            <input type="button" class="${app2:getFormButtonClasses()}" value="<fmt:message key="Common.new"/>"
                   onclick="window.location ='${newArticleUrl}'">
        </div>
    </c:set>
</app2:checkAccessRight>

<c:out value="${newButtonsTable}" escapeXml="false"/>

<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<div class="table-responsive">
    <fanta:table mode="bootstrap" list="articleAdvancedSearch" styleClass="${app2:getFantabulousTableLargeClases()}" width="100%" id="article"
                 action="Article/AdvancedSearch.do"
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
                        <span class="${app2:getClassGlyphEdit()}"></span>
                    </html:link>
                </app2:checkAccessRight>
            </fanta:actionColumn>

            <fanta:actionColumn name="delete" title="Common.delete" styleClass="listItem"
                                headerStyle="listHeader"
                                width="50%">
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
</div>

<c:out value="${newButtonsTable}" escapeXml="false"/>