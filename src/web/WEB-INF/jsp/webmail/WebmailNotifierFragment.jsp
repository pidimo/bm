<%@ include file="/Includes.jsp" %>

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

<fmt:message var="emailfolderReplacementId" key="Webmail.mailBoxes"/>

<fmt:message  var="errors" key="Webmail.errorNotifier.title"/>
<fmt:message var="info" key="Webmail.popNotifier.title"/>

<%--toastr notifications--%>
<link rel="stylesheet" href="<c:url value="/js/cacheable/bootstrap/toastr/css/toastr.min.css"/>">
<script src="<c:url value="/js/cacheable/bootstrap/toastr/js/toastr.min.js"/>"></script>

<link rel="stylesheet" type="text/css" href="<c:url value="/jquery.treeview.css"/>"/>
<tags:jscript language="JavaScript" src="/js/cacheable/jquery.treeview.js"/>

<app2:checkAccessRight functionality="MAIL" permission="EXECUTE">


    <c:set var="isAutomaticDownloadConfigurated" value="${app2:isAutomaticDownloadConfigurated(pageContext.request)}"/>

    <app2:jScriptUrl url="/webmail/WebmailNotifier/Read.do" var="jsReadNotifierUrl" addModuleParams="false"/>


    <script language="JavaScript" type="text/javascript">
        var timmedVar;
        var titleTimmedVar;

        var oldTitle = document.title;

        $(document).ready(function () {
            notifierShooting();
        });

        toastr.options.closeButton = true;
        toastr.options.timeOut=0;
        toastr.options.extendedTimeOut=0;
        toastr.options.positionClass="toast-bottom-right";
        toastr.options.preventDuplicates= true;

        function newEmailsTitleAlert(numberOfNewEmails) {
            var newTitle = "(" + numberOfNewEmails + ") " + oldTitle;

            titleTimmedVar = setInterval(function () {
                document.title = document.title == newTitle ? oldTitle : newTitle;
            }, 1000);

            document.onmousemove = function () {
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
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('isAjaxRequest', 'true');
                },
                success: function (html) {
                    processResponse(html);
                },
                error: function (ajaxRequest) {
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
                console.log("emailList");
                var emailList = $(timerCacheDiv).find("#emailListIdReplacementId");
                fantabulousTableId = $(emailList).children("table").attr("id");
                $("#emailListId").html($(timerCacheDiv).find("#emailListIdReplacementId").html());
            }

            if ($(timerCacheDiv).find("#errorNotificationReplacementId").html() != null) {
                toastr.error($(timerCacheDiv).find("#errorNotificationReplacementId").html(),"${errors}");
            }

            if ($(timerCacheDiv).find("#errorSmtpNotificationReplacementId").html() != null) {
                toastr.error($(timerCacheDiv).find("#errorSmtpNotificationReplacementId").html(),"${errors}");
            }

            if ($(timerCacheDiv).find("#newEmailNotificationReplacementId").html() != null) {
                toastr.info($(timerCacheDiv).find("#newEmailNotificationReplacementId").html(),"${info}");

                clearInterval(titleTimmedVar);
                document.title = oldTitle;
                var number = $(timerCacheDiv).find("#emailCounterId").html();
                newEmailsTitleAlert(number);
            }

            $(timerCacheDiv).html("");

            if (null != fantabulousTableId) {
                processContentShortening(fantabulousTableId.toString());
            }
            if (folderReplacement) {
                $("#webmailFolderTree").treeview({
                    prerendered: true,
                    toggle: function () {
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

    <div id="errorNotificationId" style="display:none;">
    </div>

    <div id="errorSmtpNotificationId" style="display:none;">
    </div>

    <div id="newEmailNotificationId">
    </div>

</app2:checkAccessRight>    