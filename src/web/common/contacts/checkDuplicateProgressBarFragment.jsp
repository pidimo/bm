<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/cacheable/jquery.blockUI-2.31.js"/>
<tags:jscript language="JavaScript" src="/js/cacheable/jquery.timer.js"/>

<link rel="stylesheet" type="text/css" href="<c:url value="/css/cacheable/jquery-ui-1.10.0.smoothness.css"/>"/>
<tags:jscript language="JavaScript" src="/js/cacheable/jquery-ui-1.10.0.custom.min.js"/>

<app2:jScriptUrl url="/contacts/DataImport/ProgressBar/Ajax.do" var="jsProgressBarUrl">
    <app2:jScriptUrlParam param="dto(importStartTime)" value="jsImportStartTime"/>
</app2:jScriptUrl>

<c:set var="addressCount" value="${app2:countCompanyAddress(pageContext.request)}" scope="request"/>

<c:url var="progressImg" value="/layout/ui/img/busy.gif"/>
<div id="waitMessage" style="display:none; text-align:left;">
    <img src="${progressImg}" alt=""/>
    <span style="font-size:10pt; color:#000000; font-family:Verdana,serif;">
        <fmt:message key="DedupliContact.checkingDuplicates.wait"/>
    </span>
    <br/>
    <br/>

    <div id="progressbar"></div>
</div>


<script language="JavaScript" type="text/javascript">

    function makeProgressBarAjaxRequest(urlAction) {
        //OBS: async:false because with 'true' value this not work in chrome browser

        $.ajax({
            async:false,
            type: "POST",
            data: " ",
            dataType: "xml",
            url:urlAction,
            success: function(data, status) {
                processSummaryXMLDoc(data);
            }
        });
    }

    function processSummaryXMLDoc(xmldoc) {
        if (xmldoc.getElementsByTagName('importProgressBar').length > 0) {
            //process xml document with jQuery
            $(xmldoc).find('summary').each(function() {

                var total = $(this).attr('totalRecords');
                if(total > 0) {
                    redefineImportProgressBarTime(total);
                }
            });
        }
    }

    function showCheckDuplicateProgressBar() {
        blockUIScreen();
        initProgressBar();
        progressBarTimer();
    }

    function progressBarTimer() {
        var seconds = -5;
        //timer object
        var timer = $.timer(function() {
            updateProgressBar(++seconds);
        });
        timer.set({ time : 1000, autostart : true }); //timer each 1000 milliseconds
    }

    function blockUIScreen() {
        $.blockUI({ message: $('#waitMessage') , css: {padding: '20px'}});
    }

    function initProgressBar(){
        $("#progressbar").progressbar({ value: 0 });
        //define the max
        $("#progressbar").progressbar( "option", "max", calculateProcessTime());
    }

    function updateProgressBar(timeProcessed){
        var max = $("#progressbar").progressbar( "option", "max" );
        var limit = (97 * max) / 100; //limit defined in 97%

        if(timeProcessed > 0 && timeProcessed < limit) {
            $("#progressbar").progressbar("value", timeProcessed);
        }

        //redefine import progress time if is required
        if('${isImportProgressBar}' == "true" && timeProcessed == 30) {
            var jsImportStartTime = document.getElementById("importStartTime_key").value;
            makeProgressBarAjaxRequest(${jsProgressBarUrl});
        }
    }

    /**
    * Calculate process time in seconds
    * @returns {Number}
    */
    function calculateProcessTime() {
        //factor: 10000 contacts in 120 seconds -> 0.012
        var processTime = parseInt('${addressCount}') * 0.012;
        if(processTime < 5) {
            processTime = 5;
        }
        return parseInt(processTime);
    }

    function redefineImportProgressBarTime(totalImportRecord){

        if(totalImportRecord > 0) {
            var max = $("#progressbar").progressbar( "option", "max" );
            //factor: save and delete 6000 records in 1200 seconds -> 0.2
            var processTime = totalImportRecord * 0.2;
            var newMax = max + processTime;

            $("#progressbar").progressbar( "option", "max", newMax);
        }
    }

</script>

