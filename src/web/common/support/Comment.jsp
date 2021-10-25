<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <fmt:message var="dateTimePattern" key="dateTimePattern"/>
            <c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
            <%--            Comments            --%>

            <c:forEach var="comments" items="${commentForm.dtoMap.commentList}">
                <c:set var="deleteAction"
                       value="Article/Comment/Forward/Delete.do?dto(commentId)=${comments.commentId}&articleId=${param.articleId}&index=${param.index}&dto(articleTitle)=${app2:encode(param['dto(articleTitle)'])}"/>

                <table width="750" border="0" align="center" cellpadding="3" cellspacing="0">
                    <tr>
                        <td class="contain" width="97%">
                            <c:out value="${comments.ownerName} - ${app2:getDateWithTimeZone(comments.createDateTime, timeZone, dateTimePattern)}"/>
                        </td>
                        <td class="contain" width="3">
                            <c:if test="${comments.userCreateId == sessionScope.user.valueMap['userId'] || (comments.articleUserId == sessionScope.user.valueMap['userId'])}">
                                <app2:checkAccessRight functionality="ARTICLECOMMENT" permission="DELETE">
                                    <html:link action="${deleteAction}" titleKey="Common.update">
                                        <html:img src="${baselayout}/img/delete.gif" titleKey="Common.delete"
                                                  border="0"/>
                                    </html:link>
                                </app2:checkAccessRight>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td class="label" colspan="2">
                            <br>
                            <c:out value="${app2:convertTextToHtml(comments.content)}" escapeXml="false"/>
                            <br>
                            <br>
                        </td>
                    </tr>
                </table>

                <br>
                <br>
            </c:forEach>

        </td>
    </tr>
    <tr>
        <td>
            <html:form action="${createAction}" focus="dto(description)">
                <table border="0" cellpadding="0" cellspacing="0" width="750" align="center" class="container">
                    <fmt:message var="dateTimePattern" key="dateTimePattern"/>
                    <html:hidden property="dto(op)" value="${op}"/>
                    <html:hidden property="dto(actionHistory)" value="${actionHistory}"/>
                    <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
                    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                    <html:hidden property="dto(articleId)" value="${param.articleId}"/>
                    <TR>
                        <TD class="contain">
                            <html:textarea property="dto(description)" styleClass="largeDetail" tabindex="9"
                                           style="height:90px;width:99%;"/>
                        </TD>
                    </TR>
                </table>
                <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
                <table cellSpacing=0 cellPadding=4 width="750" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="create" functionality="ARTICLECOMMENT" styleClass="button"
                                                 tabindex="10">
                                ${button}
                            </app2:securitySubmit>

                        </TD>
                    </TR>
                </table>
            </html:form>

        </td>
    </tr>
</table>
