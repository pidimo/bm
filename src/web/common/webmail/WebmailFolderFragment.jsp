<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="/webmail/Mail/ChangeFolderOpenStatus.do?dto(op)=changeOpenStatus" var="jsChangeOpenStatusURL" addModuleParams="false">
    <app2:jScriptUrlParam param="dto(folderId)" value="folderId"/>
</app2:jScriptUrl>

<fmt:message key="Common.sessionExpired" var="sessionExpiredMessage"/>
<fmt:message key="error.tooltip.unexpected" var="unexpectedErrorMessage"/>
<fmt:message key="Common.message.loading" var="loadingMessage"/>

<script language="JavaScript" type="text/javascript">
    
    function changeFolderOpenStatus(element){
        var folderId= element.id.substring(7);
        makePOSTAjaxRequestForFolders(${jsChangeOpenStatusURL});

    }
    function makePOSTAjaxRequestForFolders(urlAddress) {
        $.ajax({
            async:true,
            type: "POST",
            dataType: "html",
            url:urlAddress
        });
    }
</script>
<table border="0" cellpadding="0" cellspacing="0" align="center" class="container" width="100%">
    <tr>
        <td class="title">
            <fmt:message key="Webmail.mailBoxes"/>
        </td>
        <td class="title" style="text-align: right;">
            <app2:checkAccessRight functionality="WEBMAILFOLDER" permission="CREATE">
                <app:link action="/Mail/Forward/CreateFolder.do?tabKey=Webmail.folder.plural&inCompose=compose">
                    <html:img src="${baselayout}/img/add_blue.gif"
                              titleKey="Webmail.folder.new"
                              border="0" styleClass="imageButton" style="vertical-align:middle;"/>
                </app:link>
            </app2:checkAccessRight>
        </td>
    </tr>
    <tr>
        <td class="contain" colspan="2">
            <div id="msgId" class="messageToolTip" style="display:none">&nbsp;</div>
            ${app2:getFoldersAsXmlString(sessionScope.user.valueMap['userId'], sessionScope.user.valueMap['companyId'],pageContext, selectedFolderId)}
        </td>
    </tr>
</table>