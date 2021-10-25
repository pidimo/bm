<%@ include file="/Includes.jsp" %>


<fmt:message key="Webmail.email.title" var="messageTitle"/>
<%--Doc:
This fragment contains webmail notifier logic for upload and download of emails

1.- The DIV tag with id=errorNotificationId displays smtp or pop errors notifications, this notifications arrives only when exists
errors sending or retrieving emails from webmail ( authentication, provider, services ).

2.- The DIV tag with id=newEmailNotificationId displays pop notitications.

3.- The file MailFolders.jsp contains a DIV tag with id=emailfolderId, this tag contain the user folders that is
Inbox, Tray, OutBox, etc. this folders are updated periodically because the unread emails counter must be updated
when some new emails arrives to user webmail, or in the outbox case the email number counter must be decreased, because
the emails of this folder was sent.

4.- The file MailTray.jsp contains a DIV tag with id=emailListId, this tag contain the active list, the contain of this
tag is updated periodically but only when arrives new emails to active folder, only in this case.

--%>

<tags:jscript language="JavaScript" src="/js/cacheable/jquery-ui-1.10.0.custom.min.js"/>
<tags:jscript language="JavaScript" src="/js/cacheable/ui.notificationmsg.js"/>

<link rel="stylesheet" type="text/css" href="<c:url value="/jquery.treeview.css"/>"/>
<tags:jscript language="JavaScript" src="/js/cacheable/jquery.treeview.js"/>


<app2:checkAccessRight functionality="MAIL" permission="EXECUTE">


<c:set var="isAutomaticDownloadConfigurated" value="${app2:isAutomaticDownloadConfigurated(pageContext.request)}"/>

<app2:jScriptUrl url="/webmail/WebmailNotifier/Read.do" var="jsReadNotifierUrl" addModuleParams="false"/>

<style type="text/css">
    #errorNotificationId {
        bottom: 0px;
        right: 0px;
        z-index: 1000;
        position: absolute;
        width: 290px;
        height: 150px;
        border: solid 1px black;
        margin: 0px 0px 0px 20px;
        background-color: #FFFFCC;
        display: none;
        font-size: 13px;
    }

    #errorSmtpNotificationId {
        bottom: 0px;
        right: 0px;
        z-index: 1100;
        position: absolute;
        width: 290px;
        height: 150px;
        border: solid 1px black;
        margin: 0px 0px 0px 20px;
        background-color: #FFFFCC;
        display: none;
        font-size: 13px;
    }

    #newEmailNotificationId {
        bottom: 0px;
        right: 0px;
        z-index: 1200;
        position: absolute;
        width: 290px;
        height: 150px;
        border: solid 1px black;
        margin: 0px 0px 0px 20px;
        background-color: #FFFFCC;
        display: none;
        font-size: 13px;
    }

    td.notifierTitleClass {
        width: 100%;
        background-color: Transparent;
        background-image: url(layout/ui/img/notifierbgtitle.gif);
        background-repeat: repeat-x;
        padding: 4px;
        color: #ffffff;
        text-align: right;
    }

    div.notifierTitle {
        font-family: Arial;
        padding: 4px;
        font-size: 11px;
        font-weight: bold;
        color: #ffffff;
        float: left;
    }

    div.modalbody {
        text-align: left;
    }

    div.modalbodyScroll {
        height: 118px;
        width: 100%;
        overflow: auto;
        overflow-y: auto;
        overflow-x: hidden;
    }

</style>

<script language="JavaScript" type="text/javascript">
    var timmedVar;
    var titleTimmedVar;

    var oldTitle = document.title;

    $(document).ready(function() {
        notifierShooting();
    });

    function newEmailsTitleAlert(numberOfNewEmails) {
        var newTitle = "(" + numberOfNewEmails + ") " + oldTitle;

        titleTimmedVar = setInterval(function() {
            document.title = document.title == newTitle ? oldTitle : newTitle;
        }, 1000);

        document.onmousemove = function() {
            clearInterval(titleTimmedVar);
            document.title = oldTitle;
            window.onmousemove = null;
        };
    }

    function notifierShooting() {
        showWebmailNotifier();
    <c:if test="${isAutomaticDownloadConfigurated}">
        initializeReadNotificationTimer();
    </c:if>
    }

    function initializeReadNotificationTimer() {
        if (timmedVar) {
            clearTimeout(timmedVar);
            timmedVar = null;
        }
        timmedVar = setTimeout("notifierShooting()", 60000);
    }

    function clearReadNotificationTimer() {
        clearTimeout(timmedVar);
        timmedVar = null;
    }

    function disableUploadAndDownload(link) {
        var url = link.href;
        clearReadNotificationTimer();
        link.disable = true;
        link.href = "#";
        location.href = url;
    }

    function showWebmailNotifier() {
        var hasViewWebmailList = false;
        if ($("#emailListId").html() != null)
            hasViewWebmailList = true;

        var hasViewWebmailFolders = false;
        if ($("#emailfolderId").html() != null)
            hasViewWebmailFolders = true;

        var enableFolderSelector = false;
        if ($("#enableFolderSelectorId").attr("value") == "true")
            enableFolderSelector = true;

        $.ajax({
            type: "GET",
            cache: false,
            url:${jsReadNotifierUrl},
            dataType: "html",
            data: "hasViewWebmailList=" + hasViewWebmailList + "&hasViewWebmailFolders=" + hasViewWebmailFolders + "&enableFolderSelector=" + enableFolderSelector + "&isBackgroundProcess=true",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('isAjaxRequest', 'true');
            },
            success: function(html) {
                processResponse(html);
            },
            error: function(ajaxRequest) {
                ajaxErrorProcess(ajaxRequest.status);
            }
        });
    }

    function processResponse(responseHtml) {
        var timerCacheDiv = $("#webmailTimerCache");
        $(timerCacheDiv).html(responseHtml);
        var fantabulousTableId = null;
        var folderReplacement = false;
        if ($(timerCacheDiv).find("#emailfolderReplacementId").html() != null) {
            $("#emailfolderId").html($(timerCacheDiv).find("#emailfolderReplacementId").html());
            folderReplacement = true;
        }

        if ($(timerCacheDiv).find("#emailListIdReplacementId").html() != null) {
            var emailList = $(timerCacheDiv).find("#emailListIdReplacementId");
            fantabulousTableId = $(emailList).children("table").attr("id");

            $("#emailListId").html($(timerCacheDiv).find("#emailListIdReplacementId").html());
        }

        if ($(timerCacheDiv).find("#errorNotificationReplacementId").html() != null) {
            $("#errorNotificationId").html($(timerCacheDiv).find("#errorNotificationReplacementId").html());
            $("#errorNotificationId").notificationmsg({period: 0, animation:'slide'});
            $("#errorNotificationId").notificationmsg("show");
        }

        if ($(timerCacheDiv).find("#errorSmtpNotificationReplacementId").html() != null) {
            $("#errorSmtpNotificationId").html($(timerCacheDiv).find("#errorSmtpNotificationReplacementId").html());
            $("#errorSmtpNotificationId").notificationmsg({period: 0, animation:'slide'});
            $("#errorSmtpNotificationId").notificationmsg("show");
        }

        if ($(timerCacheDiv).find("#newEmailNotificationReplacementId").html() != null) {
            $("#newEmailNotificationId").html($(timerCacheDiv).find("#newEmailNotificationReplacementId").html());
            $("#newEmailNotificationId").notificationmsg({period: 0, animation:'slide'});
            $("#newEmailNotificationId").notificationmsg("show");

            clearInterval(titleTimmedVar);
            document.title = oldTitle;
            var number = $(timerCacheDiv).find("#emailCounterId").html();
            newEmailsTitleAlert(number);
        }

        $(timerCacheDiv).html("");

        if (null != fantabulousTableId) {
            processContentShortening(fantabulousTableId.toString());
        }

        if (null != $('#closeErrorNotificationId')) {
            $('#closeErrorNotificationId').click(function() {
                $('#errorNotificationId').notificationmsg('hide');
            });
        }

        if (null != $('#closeErrorSmtpNotificationId')) {
            $('#closeErrorSmtpNotificationId').click(function() {
                $('#errorSmtpNotificationId').notificationmsg('hide');
            });
        }

        if (null != $('#closePopNotificationId')) {
            $('#closePopNotificationId').click(function() {
                $('#newEmailNotificationId').notificationmsg('hide');
            });
        }

        if (folderReplacement) {
            $("#webmailFolderTree").treeview({
                prerendered: true,
                toggle: function() {
                    changeFolderOpenStatus(this);
                }
            });
        }
    }

    function ajaxErrorProcess(status) {
        clearTimeout(timmedVar);
        if (status == 404 || status == 302) {
        } else {
        }
    }
</script>

<div id="webmailTimerCache" style="display:none;">
</div>

<div id="errorNotificationId">
</div>

<div id="errorSmtpNotificationId">
</div>

<div id="newEmailNotificationId">
</div>

</app2:checkAccessRight>    