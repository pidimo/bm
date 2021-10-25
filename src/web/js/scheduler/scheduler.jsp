<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
/*** Javascript functions with utilities for module Scheduler
**/
<!--

 function changeReminderType(){
    var rt = document.getElementById('reminderType_Id').value;
    if(rt == '1'){
        document.getElementById('reminder_1').style.display = "";
        document.getElementById('reminder_2').style.display = "none";
        document.getElementById('timeBefore_2').value = "";
        document.getElementById('timeBefore').selectedIndex = 0;
    }else{
        document.getElementById('reminder_2').style.display = "";
        document.getElementById('reminder_1').style.display = "none";
        document.getElementById('timeBefore').value = "";
        }
    }

 function generalDisable(isAllDay, isReminder, rangeType){
    if(isAllDay)
       deshabilitaAllDay1();
    if(!isReminder)
       deshabilita1();
    if(rangeType == '1')
       deshabilita_1();
    else if(rangeType =='2')
       habilita_1();
    else if(rangeType =='3')
       habilita1_1();

    }

function deshabilitaAllDay1(){

    if(document.getElementById('isAllDay').checked){
        document.getElementById('startDate_Id').style.display = "none";
        document.getElementById('endDate_Id').style.display = "none";
    }else{
        document.getElementById('startDate_Id').style.display = "";
        document.getElementById('endDate_Id').style.display = "";
        }
    }

function habilitaReminder(){

   if(document.getElementById('reminderId').checked == true) {
      document.getElementById('reminderAreaId').style.display = 'block';
      document.getElementById('timeBefore').disabled = false;
      document.getElementById('timeBefore').disabled = false;
      document.getElementById('timeBefore_2').disabled = false;
      document.getElementById('reminderType_Id').disabled = false;
   } else {
     deshabilita1();
   }



}

function deshabilita1(){
   document.getElementById('reminderAreaId').style.display = 'none';
    document.getElementById('timeBefore').disabled = true;
    document.getElementById('timeBefore_2').disabled = true;
    document.getElementById('reminderType_Id').disabled = true;
    document.getElementById('reminderType_Id').value = "1";
    changeReminderType();
}

function habilita_1(){
document.getElementById('rangeValueDate_Id').value = "";
document.getElementById('rangeValueDate_Id').disabled = true;
document.getElementById('rangeValueText_Id').disabled = false;

document.getElementById('disablePicker_calendar').style.display = "none";
document.getElementById('disablePicker').style.display = "";
}

function habilita1_1(){
document.getElementById('rangeValueText_Id').value = "";
document.getElementById('rangeValueDate_Id').disabled = false;
document.getElementById('rangeValueText_Id').disabled = true;

document.getElementById('disablePicker_calendar').style.display = "";
document.getElementById('disablePicker').style.display = "none";
}

function deshabilita_1(){
document.getElementById('rangeValueText_Id').value = "";
document.getElementById('rangeValueDate_Id').value = "";
document.getElementById('rangeValueDate_Id').disabled = true;
document.getElementById('rangeValueText_Id').disabled = true;

document.getElementById('disablePicker_calendar').style.display = "none";
document.getElementById('disablePicker').style.display = "";
}

function incrHour(){

    var a = document.getElementById('startHour').value;

    var i=parseInt(a)+1;
    if(a < 23)
    document.getElementById('endHour').value = i;
    else document.getElementById('endHour').value = a;
}

function equalMin(){
    document.getElementById('endMin').value = document.getElementById('startMin').value;
}

function equalDate(){
    document.getElementById('endDate').value = document.getElementById('startDate').value;
}


function equalDate(){
    document.getElementById('endDate').value = document.getElementById('startDate').value;
}

function isRecurrence(recurBox){
    if(recurBox.checked) {
        document.getElementById('recurrenceAreaTitle').style.display = "";
        document.getElementById('recurrenceAreaContent').style.display = "";

   if(document.getElementById('rt').value == '1'){

    document.getElementById('rangeValueText_Id').value = "";
    document.getElementById('rangeValueText_Id').disabled = true;   
    document.getElementById('disablePicker_calendar').style.display = "none";
    document.getElementById('disablePicker').style.display = "";
     }
    } else {
        document.getElementById('recurrenceAreaTitle').style.display = "none";
        document.getElementById('recurrenceAreaContent').style.display = "none";
    }
}

function changeRuleType(ruleType){
   var rt = ruleType.value;
       if(rt == '1'){
           document.getElementById('ruleType1_1').style.display = "";
           document.getElementById('ruleType2_1').style.display = "none";  
           document.getElementById('ruleType3_1').style.display = "none";
           document.getElementById('ruleType4_1').style.display = "none";
       }else if(rt == '2'){
           document.getElementById('ruleType1_1').style.display = "none";
           document.getElementById('ruleType2_1').style.display = "";
           document.getElementById('ruleType3_1').style.display = "none";
           document.getElementById('ruleType4_1').style.display = "none";
       }else if(rt == '3'){
           document.getElementById('ruleType1_1').style.display = "none";
           document.getElementById('ruleType2_1').style.display = "none";
           document.getElementById('ruleType3_1').style.display = "";
           document.getElementById('ruleType4_1').style.display = "none";
       }else if(rt == '4'){
           document.getElementById('ruleType1_1').style.display = "none";
           document.getElementById('ruleType2_1').style.display = "none";
           document.getElementById('ruleType3_1').style.display = "none";
           document.getElementById('ruleType4_1').style.display = "";
       }else{
           document.getElementById('ruleType1_1').style.display = "none";
           document.getElementById('ruleType2_1').style.display = "none";
           document.getElementById('ruleType3_1').style.display = "none";
           document.getElementById('ruleType4_1').style.display = "none";
       }
   }

   function add(){
   var list = document.getElementById('exception');
   var size = list.options.length;
   var opt = new Option();
   opt.value = '1';
   if(document.getElementById('exceptionDate').value != ""){
       opt.text = document.getElementById('exceptionDate').value;
       opt.value = document.getElementById('exceptionDate').value;
       list.options[size] = opt;
       document.getElementById('exceptionDate').value="";
    }
   }

   function remove(){

   var list = document.getElementById('exception');
   if(list.selectedIndex != -1)
    list.remove(list.selectedIndex);
   }

   function send(tbox) {
       var str="";
   	for(var i=0; i< tbox.options.length; i++) {
   	  tbox.options[i].selected = true;
      }
   }

   function habilita(){
   document.getElementById('rangeValueDate_Id').value = "";
   document.getElementById('rangeValueDate_Id').disabled = true;
   document.getElementById('rangeValueText_Id').disabled = false;
   }

   function habilita1(){
   document.getElementById('rangeValueText_Id').value = "";
   document.getElementById('rangeValueDate_Id').disabled = false;
   document.getElementById('rangeValueText_Id').disabled = true;
   }

   function deshabilitaEmptyRangeType(){

   document.getElementById('rangeValueText_Id').value = "";
   document.getElementById('rangeValueDate_Id').value = "";
   document.getElementById('rangeValueDate_Id').disabled = true;
   document.getElementById('rangeValueText_Id').disabled = true;

   document.getElementById('disablePicker_calendar').style.display = "none";
   document.getElementById('disablePicker').style.display = "";

   }

function returnList(){
     document.getElementById('returnTypeLow').value = document.getElementById('returnTypeUp').value;
}

function returnListLow(){
   document.getElementById('returnTypeUp').value = document.getElementById('returnTypeLow').value;
}
//-->