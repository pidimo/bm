<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<!--
/******* functions to management dir emails *******/

function selectContactMail(option,mails,sentAddressIDs,sentContactPersonIDs) {
var textAnterior = '';
var stileId;
if(option == 'mailTO'){
stileId='to_email';
}
if(option == 'mailCC'){
stileId='cc_email';
}
if(option == 'mailBCC'){
stileId='bcc_email';
}

//remove the emails repeated of the list anterior
textAnterior = document.getElementById(stileId).value;
var array_emails = mails.split(",");
var array_textAnterior = textAnterior.split(",");
for(var y=0; y<array_emails.length; y++){

if( (array_emails[y].indexOf("@") != -1) && (array_emails[y].lastIndexOf(".") != -1) ){
for(var m=0; m<array_textAnterior.length; m++){
if( trim(array_textAnterior[m]) == trim(array_emails[y]) ){
array_textAnterior[m] = "";
var indexDel = m - 1;
while( (indexDel >= 0) && (array_textAnterior[indexDel].indexOf("@") == -1) &&
(array_textAnterior[indexDel].lastIndexOf(".") == -1)){
array_textAnterior[indexDel] = "";
indexDel--;
}
}
}
}
}

//update list anterior
var newTextAnterior='';
for(var z=0; z<array_textAnterior.length; z++){
if(!isBlank(array_textAnterior[z])){
newTextAnterior = newTextAnterior + trim(array_textAnterior[z]) + ", ";
}
}
//remove ", " in last of the cad (2 characters)
if( newTextAnterior.length > 0 && mails.length == 0 )
newTextAnterior = newTextAnterior.substring(0,newTextAnterior.length-2);

document.getElementById(stileId).value = newTextAnterior + mails;

//set sentAddressIDs
if(sentAddressIDs != undefined){
    var addressIDs = lib_getObj("field_sentAddressIDs").value;
    addressIDs = addressIDs + sentAddressIDs;
    lib_getObj("field_sentAddressIDs").value = addressIDs;
}

//set contactPersonIds
if(sentContactPersonIDs != undefined){
    var contactPersonIDs = lib_getObj("field_sentCPersonIDs").value;
    contactPersonIDs = contactPersonIDs + sentContactPersonIDs;
    lib_getObj("field_sentCPersonIDs").value = contactPersonIDs;
}

searchWindow.close();
document.getElementById(stileId).focus();
}


// search all the "textSearched" and replace
function replace(text,textSearched,newString) {

while (text.indexOf(textSearched) > -1) {
pos= text.indexOf(textSearched);
text = "" + (text.substring(0, pos) + newString + text.substring((pos + textSearched.length), text.length));
}
return text;
}

function isBlank(text){
var res = true;
text = trim(text);
for(var i=0; i<text.length; i++){
if(text[i] != ' '){
res = false;
}
}
return res;
}

function trim(s) {
while (s.substring(0,1) == ' ') {
s = s.substring(1,s.length);
}
while (s.substring(s.length-1,s.length) == ' ') {
s = s.substring(0,s.length-1);
}
return s;
}


/******* functions to attach files *******/

var nextHiddenIndex = 0;
var arrayDeletme = new Array();
var indexArray = 0;
var contDeletme = 0;


function setFileAttachCounter(fileCounter){
nextHiddenIndex = fileCounter;
}

function addFileInput()
{
lib_getObj("fileInput" + nextHiddenIndex).style.display = document.all ? "block" : "table-row";
nextHiddenIndex++;
if(nextHiddenIndex >= 50) lib_getObj("attachMoreLink").style.display = "none";
}

function removeFileInput(idTd, idTr, nameTypefile)
{
lib_getObj(idTd).innerHTML= '<input type=file name='+nameTypefile+' class=largeText >'; //replace with new imput file

var idFile = nextHiddenIndex-1;
if(lib_getObj('dto(file'+idFile+')').value == ''){
nextHiddenIndex--;
var cont = 0;
while(cont < arrayDeletme.length){
if(arrayDeletme[cont] == "fileInput" + nextHiddenIndex){
delete arrayDeletme[cont];
nextHiddenIndex--;
contDeletme--;
cont = 0;
}
else
cont++;
}

lib_getObj("fileInput" + nextHiddenIndex).style.display = "none"; //hidden

if(nextHiddenIndex == contDeletme){
nextHiddenIndex = 0;
contDeletme = 0;
indexArray = 0;
for(var x=0; x<arrayDeletme.length; x++)
delete arrayDeletme[x];
}

}
else{
lib_getObj(idTr).style.display = "none";
arrayDeletme[indexArray] = idTr;
indexArray++;
contDeletme++;
}

}

function lib_getObj(id,d)
{
var i,x; if(!d) d=document;
if(!(x=d[id])&&d.all) x=d.all[id];
for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][id];
for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=lib_getObj(id,d.layers[i].document);
if(!x && document.getElementById) x=document.getElementById(id);
return x;
};

function fileToolTip(obj){
var value = obj.value;
obj.title = obj.value;
}

/******* function to update signatures when from select field change *******/
function updateSignatures(obj) {
        var idx = obj.selectedIndex;
        var opt = obj.options[idx];

        document.getElementById("accountSelected").value = opt.value;
        document.getElementById("hasChangedAccount").value = 'true';

        document.getElementById("mailForm").submit();
    }
//-->

