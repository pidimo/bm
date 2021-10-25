<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<!--

/*** Javascript functions with utilities for modules **/

/** Open a URL in Browser @deprecated**/
function openURL(field, type)
{
    if(type != 'mail') {
        if(field.value != '') {
            if(field.value.indexOf('http://') == -1)
                window.open('http://' + field.value);
           else
                window.open(field.value);
        }
    } else {
        window.open('mailto:' + field.value);
    }
}

function submitPassValue(name, search) {
var mform = document.forms[0];
var nameField;
var searchField;
for(var i=0; i<mform.elements.length;i++){
    var field = mform.elements[i];
    if(field.name == search){
    searchField = field;
    }

    if(field.name == name){
    nameField = field;
    }
}
searchField.value = nameField.value;
}

function setValueHidden(search1, name1) {
var mform1 = document.forms[0];
var nameField1;
var searchField1;
for(var i=0; i<mform1.elements.length;i++){
    var field1 = mform1.elements[i];
    if(field1.name == search1){
    searchField1 = field1;
    }

    if(field1.name == name1){
    nameField1 = field1;
    }
}
if(trim(nameField1.value)==""){
searchField1.value = "";
}
return true;
}

function trim(inputString) {
   if (typeof inputString != "string") { return inputString; }
   var retValue = inputString;
   var ch = retValue.substring(0, 1);
   while (ch == " ") {
      retValue = retValue.substring(1, retValue.length);
      ch = retValue.substring(0, 1);
   }
   ch = retValue.substring(retValue.length-1, retValue.length);
   while (ch == " ") {
      retValue = retValue.substring(0, retValue.length-1);
      ch = retValue.substring(retValue.length-1, retValue.length);
   }
   while (retValue.indexOf("  ") != -1) {
      retValue = retValue.substring(0, retValue.indexOf("  ")) +
retValue.substring(retValue.indexOf("  ")+1, retValue.length);
   }
   return retValue;
}

  /**
  * Autoresize a textarea taking into account the content of such text area. Used by Category values (products, customers..)
  **/
 function AutoResizeTextArea(txtBox)
  {

        nCols = txtBox.cols;
        sVal = txtBox.value;
        nVal = sVal.length;
        nRowCnt = 1;

        for (i=0;i<nVal;i++)
        {   if (sVal.charAt(i).charCodeAt(0) == 13)
            { nRowCnt +=1; }
        }

        if (nRowCnt < (nVal / nCols))
        {  nRowCnt = 1 + (nVal / nCols);
        }
        txtBox.rows = nRowCnt;

        if(nRowCnt > 1)  {
            txtBox.style.height= nRowCnt * 11;
        }


  }

  function formReset(id) {
    var form = document.getElementById(id);
    for (i = 0; i < form.elements.length; i++) {
        if (form.elements[i].type == "text" || form.elements[i].type == "select-one" ) {
            if(form.elements[i].id != 'format' && form.elements[i].id != 'pageSize' && form.elements[i].id != 'csvDelimiterId')
            form.elements[i].value = "";
        }
        else if (form.elements[i].type == "checkbox") {
            form.elements[i].checked = false;
        }
        else if(form.elements[i].type == "hidden" && (form.elements[i].id).substring(0,6) != "report"){
            form.elements[i].value = "";
        }
    }
}


//add by ivan
/**
* increments size of table that one is in Iframe
* table have id attribute = tableId
* iframe have id attribute = iFrameId
**/
function incrementTableInIframe() {
        var table = document.getElementById('tableId');
        parent.document.getElementById("iFrameId").style.height = (table.offsetHeight + 30 )  + 'px';

}

function addAppPageEvent(obj, evType, fn){
 if (obj.addEventListener){
   obj.addEventListener(evType, fn, false);
   return true;
 } else if (obj.attachEvent){
   var r = obj.attachEvent("on"+evType, fn);
   return r;
 } else {
   return false;
 }
}

function goParentURL(url){
    window.parent.location = url;
}

//add by Alvaro


function resetForm(form) {
    resetForm(form, false, false);
}

function resetForm(form, resetHiddens, submit) {
    for (i = 0; i < form.elements.length; i++) {
        if (form.elements[i].type == "text") {
            form.elements[i].value = "";
        } else if (form.elements[i].type == "select-one") {
            form.elements[i].options.selectedIndex=0;
        } else if (form.elements[i].type == "checkbox") {
            form.elements[i].checked=false;
        } else if(form.elements[i].type == "hidden" && resetHiddens){
            form.elements[i].value = "";
        }
    }
    if(submit)
        form.submit();
}
//-->
