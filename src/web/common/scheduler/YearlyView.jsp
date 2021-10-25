<%@ page import="com.jatun.common.web.JavaScriptEncoder" %>
<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ page import="com.piramide.elwis.web.schedulermanager.taglib.TableBaseTag" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/scheduler.tld" prefix="cal" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="/scheduler/AppointmentView.do?module=scheduler&type=1&ajax=true" var="jsToolUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="calendar" value="day"/>
    <app2:jScriptUrlParam param="dummy" value="new Date().getTime()"/>
</app2:jScriptUrl>

<tags:jscript language="JavaScript" src="/js/st-xmlhttp.js"/>
<script>
    var reminderIMG = "${baselayout}/img/scheduler/<%=TableBaseTag.reminderIMG%>";
    var recurrenceIMG = "${baselayout}/img/scheduler/<%=TableBaseTag.recurrenceIMG%>";
    var participantIMG = "${baselayout}/img/scheduler/<%=TableBaseTag.participantIMG%>";
    var ownerIMG = "${baselayout}/img/scheduler/<%=TableBaseTag.ownerIMG%>";

    var reminderLBL = "<fmt:message key="Scheduler.Appointment.reminder"/>";
    var reminderTypeLBL = new Array(3);
    reminderTypeLBL[0] = "<fmt:message key="Appointment.minutes"/>";
    reminderTypeLBL[1] = "<fmt:message key="Appointment.hours"/>";
    reminderTypeLBL[2] = "<fmt:message key="Appointment.day"/>";

    var recurrenceLBL = "<fmt:message key="Scheduler.Appointment.recurrence"/>";
    var participantLBL = "<fmt:message key="Scheduler.Appointment.participant"/>";
    var ownerLBL = "<fmt:message key="Scheduler.Appointment.owner"/>";
    var appointmentLBL = "<fmt:message key="ReferencedBy.table.appointment"/>";
    var holidayLBL = "<fmt:message key="holiday.plural"/>";
    var expiredLBL = '<%=JavaScriptEncoder.encode(JSPHelper.getMessage(request,"Common.sessionExpired"))%>';
    var allDayLBL = "<fmt:message key="Scheduler.Appointment.allDay"/>";
    var unexpectedErrorLBL = '<%=JavaScriptEncoder.encode(JSPHelper.getMessage(request,"error.tooltip.unexpected"))%>';

    var msgId;


    function logMe(msg) {
        var logDiv = document.getElementById("log");
        logDiv.innerHTML = logDiv.innerHTML + msg + "<br>";
    }


    function renderAppoinments(appointments) {
        var html = "";
        //logMe("----------------------------------");
        for (j = 0; j < appointments.length; j++) {

            var time = appointments[j].getAttribute("time");
            var allday = appointments[j].getAttribute("allday");
            var owner = appointments[j].getAttribute("owner");
            var reminder = appointments[j].getAttribute("reminder");
            var recurrent = appointments[j].getAttribute("recurrent");
            var title = encodeURI(appointments[j].firstChild.nodeValue);

            var timeValue = time;
            if (allday == "true")
                timeValue = allDayLBL;
            //logMe("title:" + title + " - time:" + time + " - allday:" + allday);

            html += "<div style=\"padding: 0px 0px 0px 3px;\">" + timeValue + "&nbsp;" + title + "&nbsp;";
            html += "<img src=\"" + (owner ? ownerIMG : participantIMG) + "\" border=\"0\" title=\"" + (owner ? ownerLBL : participantLBL) + "\">";

            if (reminder == "true") {
                var type = parseInt(appointments[j].getAttribute("remindertype"));
                var value = appointments[j].getAttribute("remindervalue");
                html += "&nbsp;";
                html += "<img src=\"" + reminderIMG + "\" border=\"0\" title=\"" + reminderLBL.replace(/<time>/, value).replace(/<type>/, reminderTypeLBL[type - 1]) + "\">";
            }
            if (recurrent == "true") {
                html += "&nbsp;";
                html += "<img src=\"" + recurrenceIMG + "\" border=\"0\" title=\"" + recurrenceLBL + "\">";
            }
            html += "</div>";
        }
        /*logMe(html);
         logMe("----------------------------------");*/
        return html;
    }

    function renderHolidays(holidays) {
        var html = "";
        if (holidays.length > 0) {
            //html += "<table width=\"100%\" cellspacing=\"1\" class=calendar_table\"><tr class=\"calendar_header\"><td>"+ holidayLBL +"</td></tr></table>";
            html += "<div class=\"field\">" + holidayLBL + "</div>";
        }

        for (j = 0; j < holidays.length; j++) {

            var country = holidays[j].getAttribute("country");
            var title = holidays[j].firstChild.nodeValue;
            html += "<div style=\"padding: 0px 0px 0px 3px;\">" + country;
            if (country.length > 0)
                html += "&nbsp;";
            html += encodeURI(title) + "</div>";
        }
        /*logMe(html);
         logMe("----------------------------------");*/
        return html;
    }


    function showToolTip(xmlDoc) {
        var scheduler = xmlDoc.getElementsByTagName('scheduler');

        if (scheduler.length == 0) {
            document.getElementById(msgId).innerHTML = encodeURI(expiredLBL);
            return false;
        }
        //logMe("----------------------------------");
        var holidays = xmlDoc.getElementsByTagName('holiday');
        var appointments = scheduler[0].getElementsByTagName('appointments');
        var dailyappointment = xmlDoc.getElementsByTagName('appointment');
        var alldayappointments = xmlDoc.getElementsByTagName('alldayappointment');

        var id = scheduler[0].getAttribute("id");
        var html = "";

        if (holidays.length > 0)
            html = renderHolidays(holidays);
        if (dailyappointment.length > 0 || alldayappointments.length > 0) {
            //html += "<div style=\"background:lightsteelblue; border-style:solid; border-width:thin;font-size:95%;\">" + appointmentLBL + "</div>";
            html += "<div class=\"field\">" + appointmentLBL + "</div>";
            //html += "<table width=\"100%\" cellspacing=\"1\" class=calendar_table\"><tr class=\"calendar_header\"><td>"+ appointmentLBL +"</td></tr></table>";
            html += renderAppoinments(alldayappointments);
            html += renderAppoinments(dailyappointment);
        }

        document.getElementById(id).innerHTML = html;

        /*alert(xmlDoc.text);
         logMe("xmlDocText:"+xmlDoc.text);*/
        /*logMe("xmlDocXML:"+xmlDoc.xml);
         logMe("xmlDoc:"+xmlDoc);*/

    }
    /*
     function pausecomp(millis)
     {
     date = new Date();
     var curDate = null;

     do { var curDate = new Date(); }
     while(curDate-date < millis);
     }

     if( window.captureEvents ) { window.captureEvents(Event.MOUSEMOVE); }
     document.onmouseover = handleMyMouseOver;

     function continueShowToolTip(e, id){
     logMe("Show1:"+e.clientX+ " - "+e.clientY);
     return true;
     }
     function handleMyMouseOver(e){
     //if(e.target.type=="span")


     if(e.target.tagName == "A" || e.target.tagName == "DIV" || e.target.tagName == "SPAN"){
     logMe("---------------:"+e.target.tagName);
     var n1 = e.target.onmouseover;
     var n2 = e.target.id;
     if(n1 != "undefined")
     logMe("n1:"+n1);
     if(n2 != "undefined")
     logMe("n2:"+n2);
     }
     }*/

    function toolTip(day) {
        msgId = day;
        makeHttpRequest(${jsToolUrl}, 'showToolTip', true, 'ajaxErrorProcess');
    }

    /*server response error process*/
    function ajaxErrorProcess(requestStatusCode) {
        if (requestStatusCode == 404) { //session expired http request status code
            document.getElementById(msgId).innerHTML = encodeURI(expiredLBL);
        } else {
            document.getElementById(msgId).innerHTML = encodeURI(unexpectedErrorLBL);
        }
    }
</script>

<!--<div id="log" style="overflow:scroll; width:100%; height:250;display:none;">-->
<!--<div id="log" style="overflow:scroll; width:100%; height:250">-->
<!--</div>-->

<c:set var="returnType" value="<%= SchedulerConstants.RETURN_YEAR%>"/>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="1" class="container">
    <tr>
        <td align="center">
            <cal:YearlyView url="/scheduler/AppointmentView.do?module=${param.module}"
                            todayColor="#DECACA"
                            holidayColor="#ff5d0c"
                            addURL="/scheduler/Appointment/Forward/Create.do?create=true&module=${param.module}&dto(returnType)=${returnType}"
                            modURL="/scheduler/Appointment/Forward/Update.do?update=true&module=scheculer"
                            delURL="/scheduler/Appointment/Forward/Delete.do?dto(withReferences)=true&module=scheduler"
                            tableWithPercent="100"
                            tableWidth="100%" imgPathScheduler="${baselayout}"
                    />
        </td>
    </tr>
</table>


<tags:jscript language="JavaScript" src="/js/common/wz_tooltip.js"/>