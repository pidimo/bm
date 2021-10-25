<%@ include file="/Includes.jsp" %>

<c:set var="tabHeaderLabel" value="Article" scope="request"/>
<c:set var="articleId"><%=request.getParameter("articleId")%>
</c:set>
<c:set var="tabHeaderValue" scope="request">
    <fanta:label listName="lightArticleList" module="/support" patron="0" columnOrder="articleTitle">
        <fanta:parameter field="articleId" value="${articleId}"/>
    </fanta:label>
</c:set>

<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
<app2:checkAccessRight functionality="ARTICLE" permission="VIEW">
    <c:set target="${tabItems}" property="Article.Tab.detail"
           value="/Article/Forward/Update.do?dto(articleId)=${articleId}&dto(userId)=${sessionScope.user.valueMap['userId']}"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="ARTICLECOMMENT" permission="VIEW">
    <c:set target="${tabItems}" property="Article.Tab.Comments"
           value="/Article/Comment.do?dto(articleTitle)=${app2:encode(tabHeaderValue)}"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="ARTICLERELATED" permission="VIEW">
    <c:set target="${tabItems}" property="Article.Tab.ArticleRelateds"
           value="/Article/RelationList.do?dto(articleTitle)=${app2:encode(tabHeaderValue)}"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="SUPPORTATTACH" permission="VIEW">
    <c:set target="${tabItems}" property="Article.Tab.Attachments"
           value="/ArticleAttach/List.do?dto(articleTitle)=${app2:encode(tabHeaderValue)}"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="ARTICLELINK" permission="VIEW">
    <c:set target="${tabItems}" property="Article.Tab.links"
           value="/Article/LinkList.do?dto(articleTitle)=${app2:encode(tabHeaderValue)}"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="ARTICLEHISTORY" permission="VIEW">
    <c:set target="${tabItems}" property="Article.Tab.histories"
           value="/Article/HistoryList.do?dto(articleTitle)=${app2:encode(tabHeaderValue)}"/>
</app2:checkAccessRight>
<jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>




