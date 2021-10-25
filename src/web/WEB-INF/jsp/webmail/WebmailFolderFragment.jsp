<%@ include file="/Includes.jsp" %>

<%--<script src="<c:url value="https://code.jquery.com/jquery-1.8.2.js"/>"></script>--%>

<app2:jScriptUrl url="/webmail/Mail/ChangeFolderOpenStatus.do?dto(op)=changeOpenStatus" var="jsChangeOpenStatusURL"
                 addModuleParams="false">
    <app2:jScriptUrlParam param="dto(folderId)" value="folderId"/>
</app2:jScriptUrl>

<fmt:message key="Common.sessionExpired" var="sessionExpiredMessage"/>
<fmt:message key="error.tooltip.unexpected" var="unexpectedErrorMessage"/>
<fmt:message key="Common.message.loading" var="loadingMessage"/>

<script language="JavaScript" type="text/javascript">
    function changeFolderOpenStatus(element) {
        var folderId = element.id.substring(7);
        makePOSTAjaxRequestForFolders(${jsChangeOpenStatusURL});

    }
    function makePOSTAjaxRequestForFolders(urlAddress) {
        $.ajax({
            async: true,
            type: "POST",
            dataType: "html",
            url: urlAddress
        });
    }
</script>

<form>

    <legend class="title">
        <fmt:message key="Webmail.mailBoxes"/>
        <span class="title pull-right">
            <app2:checkAccessRight functionality="WEBMAILFOLDER" permission="CREATE">
                <app:link action="/Mail/Forward/CreateFolder.do?tabKey=Webmail.folder.plural&inCompose=compose"
                          titleKey="Webmail.folder.new">
                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                </app:link>
            </app2:checkAccessRight>
        </span>
    </legend>
    <div id="msgId" class="messageToolTip" style="display:none">&nbsp;</div>
    <div class="tree">
        ${app2:getFoldersAsXmlString(sessionScope.user.valueMap['userId'], sessionScope.user.valueMap['companyId'],pageContext, selectedFolderId)}
    </div>
</form>
