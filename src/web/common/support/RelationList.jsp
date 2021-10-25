<%@ include file="/Includes.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="98%" class="container" align="center">
    <tr>
        <html:form action="/Article/Relation/Forward/Create.do">
            <TD class="button">
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                <%--<c:choose>
                <c:when test="${param.createUserId == sessionScope.user.valueMap['userId']}">--%>
                    <app2:securitySubmit operation="create" functionality="ARTICLERELATED" styleClass="button"  >
                        <fmt:message    key="Common.new"/>
                    </app2:securitySubmit>
                <%--</c:when>
                </c:choose>--%>
            </TD>
        </html:form>
    </tr>
    <tr>
        <td>

    <fanta:table list="relationList" width="100%" id="article" action="Article/RelationList.do" imgPath="${baselayout}" align="center" >
      <c:set var="deleteLink" value="Article/Relation/Forward/Delete.do?dto(relatedArticleId)=${article.relatedArticleId}&dto(articleTitle)=${app2:encode(article.articleTitle)}&index=2&articleId=${param.articleId}"/>
      <c:set var="viewLink" value="Article/Relation/Forward/View.do?dto(relatedArticleId)=${article.relatedArticleId}&dto(articleTitle)=${app2:encode(article.articleTitle)}&operation=view"/>
      <fmt:message  var="dateTimePattern" key="dateTimePattern"/>

    <fanta:columnGroup  title="Common.action" headerStyle="listHeader" width="5%">

    <fanta:actionColumn name="delete" title="Common.delete" styleClass="listItem" headerStyle="listHeader" width="50%" >
        <app2:checkAccessRight functionality="ARTICLERELATED" permission="DELETE">
        <%--<c:choose>
            <c:when test="${article.createUserId == sessionScope.user.valueMap['userId']}">--%>
                 <html:link action="${deleteLink}" titleKey="Common.delete">
                        <html:img src="${baselayout}/img/delete.gif" titleKey="Common.delete" border="0"/>
                 </html:link>
            <%--</c:when>
            <c:otherwise>
                &nbsp;

            </c:otherwise>
        </c:choose>--%>
        </app2:checkAccessRight>
    </fanta:actionColumn>
    </fanta:columnGroup>
    <fanta:dataColumn name="number" styleClass="listItem" title="Article.number"  headerStyle="listHeader" width="5%" orderable="true" maxLength="40"/>
    <fanta:dataColumn name="articleTitle" action="${viewLink}" styleClass="listItem" title="Article.title"  headerStyle="listHeader" width="15%" orderable="true" />
    <fanta:dataColumn name="categoryName" styleClass="listItem" title="Article.categoryName"  headerStyle="listHeader" width="15%" orderable="true"/>
    <fanta:dataColumn name="productName" styleClass="listItem" title="Article.productName"  headerStyle="listHeader" width="15%" orderable="true"/>
    <fanta:dataColumn name="ownerName" styleClass="listItem" title="Article.ownerName"  headerStyle="listHeader" width="15%" orderable="true"/>
    <fanta:dataColumn name="creationDate" styleClass="listItem${expireClass}"  title="Link.publishDate" headerStyle="listHeader" width="18%" orderable="true" renderData="false">
        ${app2:getDateWithTimeZone(article.creationDate, timeZone, dateTimePattern)}
    </fanta:dataColumn>
    </fanta:table>
        </td>
    </tr>
</table>
