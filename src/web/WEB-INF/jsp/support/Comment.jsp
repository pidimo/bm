<%@ include file="/Includes.jsp" %>

<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<%--            Comments            --%>
<div class="${app2:getFormClassesLarge()}">

    <c:forEach var="comments" items="${commentForm.dtoMap.commentList}">
        <c:set var="deleteAction"
               value="Article/Comment/Forward/Delete.do?dto(commentId)=${comments.commentId}&articleId=${param.articleId}&index=${param.index}&dto(articleTitle)=${app2:encode(param['dto(articleTitle)'])}"/>
        <div class="panel panel-default">
            <div class="panel-heading">
                <div class="row">
                    <div class="col-xs-10">
                        <label class="control-label">
                            <c:out value="${comments.ownerName} - ${app2:getDateWithTimeZone(comments.createDateTime, timeZone, dateTimePattern)}"/>
                        </label>
                    </div>
                    <div class="col-xs-2">
                        <span class="pull-right">
                        <c:if test="${comments.userCreateId == sessionScope.user.valueMap['userId'] || (comments.articleUserId == sessionScope.user.valueMap['userId'])}">
                            <app2:checkAccessRight functionality="ARTICLECOMMENT" permission="DELETE">
                                <html:link action="${deleteAction}" titleKey="Common.delete">
                                    <span class="glyphicon glyphicon-trash"></span>
                                </html:link>
                            </app2:checkAccessRight>
                        </c:if>
                        </span>
                    </div>
                </div>
            </div>
            <div class="panel-body">
                <c:out value="${app2:convertTextToHtml(comments.content)}" escapeXml="false"/>
            </div>
        </div>
    </c:forEach>

    <html:form action="${createAction}" focus="dto(description)" styleClass="form-horizontal">
        <fmt:message var="dateTimePattern" key="dateTimePattern"/>
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(actionHistory)" value="${actionHistory}"/>
        <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(articleId)" value="${param.articleId}"/>
        <div class="${app2:getFormGroupClasses()}">
            <div class="col-xs-12">
                <html:textarea property="dto(description)" styleClass="largeDetail ${app2:getFormInputClasses()}"
                               tabindex="9"
                               style="height:90px;width:100%;"/>
                    <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
            </div>
        </div>
        <div class=" ${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="create" functionality="ARTICLECOMMENT"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 tabindex="10">
                ${button}
            </app2:securitySubmit>
        </div>
    </html:form>
</div>

