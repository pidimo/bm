<%--
Tag to manage autosave emai when compose this.

Attributes:
formStyleId -> the DOM id of form
autosaveAction -> url action to autosave
tempMailIdStyleId -> the DOM id of "dto(tempMailId)" input hidden
isUseHtmlEditor -> true if use an html editor, false if is only textarea
--%>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

<%@ attribute name="formStyleId" required="true" %>
<%@ attribute name="autosaveAction" required="true" %>
<%@ attribute name="tempMailIdStyleId" required="true" %>
<%@ attribute name="isUseHtmlEditor" required="true" %>

<script language="JavaScript" src="<c:url value="/js/cacheable/jquery.timer.js"/>" type="text/javascript"></script>
<script language="JavaScript" src="<c:url value="/js/cacheable/jqueryformplugin/3.51.0/jquery.form.min.js"/>" type="text/javascript"></script>


<app2:jScriptUrl url="${autosaveAction}" var="jsAutoSaveUrl">
</app2:jScriptUrl>

<script language="JavaScript" type="text/javascript">

    $(document).ready(function() {
        initializeTempEmail();

        // start the timer to autosave
        initAutosaveTimer();
    });


    function initializeTempEmail() {
        var tempMailId =  $('#'+'${tempMailIdStyleId}').val();
        if(tempMailId == "") {
            //first save as temp email, this is necessary to add temporal attach in the email
            makeAutosaveEmail();
        }
    }

    function initAutosaveTimer() {
        //timer object
        var timer = $.timer(function() {
            makeAutosaveEmail();
        });

        //period time in millis, 30000 = 30 seconds
        timer.set({ time : 30000, autostart : true });
    }

    function makeAutosaveEmail() {
        preProcessBeforeSendEmailData();

        var options = {
            //beforeSubmit:  showEmailAutosaveRequest,  // pre-submit callback
            success:       processSuccessResponseAutosaveEmail,  // post-submit callback
            url:       ${jsAutoSaveUrl},         // override for form's 'action' attribute
            dataType:  'json'        // 'xml', 'script', or 'json' (expected server response type)
        };

        //use jquery form plugin to submit form
        $('#'+'${formStyleId}').ajaxSubmit(options);
    }

    // pre-submit callback
    // formData is an array; here we use $.param to convert it to a string to display it
    // but the form plugin does this for you automatically when it submits the data
    function showEmailAutosaveRequest(formData, jqForm, options) {
        var queryString = $.param(formData);

        alert('About to submit: \n\n' + queryString);

        // here we could return false to prevent the form from being submitted;
        // returning anything other than false will allow the form submit to continue
        return true;
    }

    function preProcessBeforeSendEmailData() {

        <c:if test="${'true' eq isUseHtmlEditor}">
            //dump editor content to textarea
            tinyMCE.triggerSave();
        </c:if>
    }

    // if the ajaxSubmit method was passed an Options Object with the dataType
    // property set to 'json' then the first argument to the success callback
    // is the json data object returned by the server
    function processSuccessResponseAutosaveEmail(responseJson, statusText, xhr, $form)  {

        if("Success" == responseJson.ajaxForward) {
            document.getElementById("${tempMailIdStyleId}").value = responseJson.tempMailId;
            //alert("set: "+responseJson.tempMailId);
        }

        //alert('status: ' + statusText + '\n\nresponseText: \n' + responseJson.ajaxForward);
    }

</script>