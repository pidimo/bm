<%@ include file="/Includes.jsp" %>

<c:set var="isBodyInAllWidth" value="true" scope="request"/>

<c:set var="systemFolderCounter"
       value="${app2:getSystemFolders(sessionScope.user.valueMap['userId'], pageContext.request)}" scope="request"/>
<c:set var="customFoldersList"
       value="${app2:getCustomFolders(sessionScope.user.valueMap['userId'], pageContext.request)}" scope="request"/>

<div class="col-xs-12 col-sm-3 col-md-2">
    <app2:checkAccessRight functionality="MAIL" permission="VIEW">
        <div class="${app2:getFormPanelClasses()}">
            <div class="form-group">
                <c:import url="/WEB-INF/jsp/webmail/MailFolders.jsp"/>
            </div>
                <%--<tr>
                    <td><c:import url="/common/webmail/CustomFolders.jsp"/></td>
                </tr>--%>
            <div class="form-group">
                <c:import url="/WEB-INF/jsp/webmail/Search.jsp"/>
            </div>
        </div>
    </app2:checkAccessRight>
</div>

<div class="col-xs-12 col-sm-9 col-md-10">
    <app2:checkAccessRight functionality="MAIL" permission="VIEW">
        <c:import url="${mailBody}"/>
    </app2:checkAccessRight>
</div>


