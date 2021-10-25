<%@ include file="/Includes.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="98%" class="container" align="center">
    <tr>
        <td>
        <c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
         <fmt:message var="dateTimePattern" key="dateTimePattern"/>
            <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0">
                <tr>
                    <td colspan="2" class="title">
                        <fmt:message key="Article.articleImportList"/>
                        <br>
                    </td>
                </tr>
                <TR>
                    <html:form action="/Article/Relation/Forward/Create.do" focus="parameter(articleTitle@_number)">
                        <td class="label"><fmt:message key="Common.search"/></td>
                        <td align="left" class="contain">
                            <html:text property="parameter(articleTitle@_number)" styleClass="largeText"
                                       maxlength="80"/>
                            &nbsp;
                            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>

                        </td>
                    </html:form>
                </TR>
                <tr>
                    <td colspan="2" align="center" class="alpha">
                        <fanta:alphabet action="Article/Relation/Forward/Create.do" parameterName="articleTitleAlphabet"/>
                    </td>
                </tr>
            </table>


        </td>
    </tr>
    <tr>
        <html:form action="/Article/Relation/Create.do?cancel=true">
            <td class="button">
                <html:cancel styleClass="button" property="dto(cancel)">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </html:form>
    </tr>
    <tr>
        <td>
            <fanta:table list="importArticleList" width="100%" id="article" action="Article/Relation/Forward/Create.do" imgPath="${baselayout}" align="center">
                <c:set var="importAction"
                       value="Article/Relation/Create.do?dto(articleName)=${app2:encode(article.articleTitle)}&dto(relatedArticleId)=${article.articleId}&dto(op)=create"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <fanta:actionColumn name="import" title="Common.import" action="${importAction}"
                                        styleClass="listItem" headerStyle="listHeader" width="2%"
                                        image="${baselayout}/img/import.gif"/>
                </fanta:columnGroup>
                <fanta:dataColumn name="number" styleClass="listItem" title="Article.number" headerStyle="listHeader"
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
        </td>
    </tr>
</table>