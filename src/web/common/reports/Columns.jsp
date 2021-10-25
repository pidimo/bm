<%@ page import="com.piramide.elwis.utils.ReportConstants" %>
<%@ include file="/Includes.jsp" %>

<%
request.setAttribute("order", JSPHelper.getColumnGroupOrder(request));
request.setAttribute("KEYLABEL", ReportConstants.KEY_COLUMNSEPARATOR_LABEL);
request.setAttribute("KEYORDER", ReportConstants.KEY_COLUMNSEPARATOR_ORDER);
request.setAttribute("KEYCOLUMN", ReportConstants.KEY_COLUMNSEPARATOR_COLUMNREF);
request.setAttribute("KEYPATH", ReportConstants.KEY_COLUMNSEPARATOR_PATH);
%>

<script language="JavaScript">
<!--
//get object by id or name
function lib_getObj(id,d){
var i,x;  if(!d) d=document;
if(!(x=d[id])&&d.all) x=d.all[id];
for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][id];
for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=lib_getObj(id,d.layers[i].document);
if(!x && document.getElementById) x=document.getElementById(id);
return x;
}

function moveUpList(listField) {
if ( listField.length == -1) {  // If the list is empty

} else {
  var selected = listField.selectedIndex;
  if (selected == -1) {

  } else {  // Something is selected
     if ( listField.length == 0 ) {  // If there's only one in the list

     } else {  // There's more than one in the list, rearrange the list order
        if ( selected == 0 ) {

        } else {
           // Get the text/value of the one directly above the hightlighted entry as
           // well as the highlighted entry; then flip them
           var moveText1 = listField[selected-1].text;
           var moveText2 = listField[selected].text;
           var moveValue1 = listField[selected-1].value;
           var moveValue2 = listField[selected].value;
           listField[selected].text = moveText1;
           listField[selected].value = moveValue1;
           listField[selected-1].text = moveText2;
           listField[selected-1].value = moveValue2;
           listField.selectedIndex = selected-1; // Select the one that was selected before
        }  // Ends the check for selecting one which can be moved
     }  // Ends the check for there only being one in the list to begin with
  }  // Ends the check for there being something selected
 }  // Ends the check for there being none in the list
}

function moveDownList(listField) {
if ( listField.length == -1) {  // If the list is empty

} else {
  var selected = listField.selectedIndex;
  if (selected == -1) {

  } else {  // Something is selected
     if ( listField.length == 0 ) {  // If there's only one in the list

     } else {  // There's more than one in the list, rearrange the list order
        if ( selected == listField.length-1 ) {

        } else {
           // Get the text/value of the one directly below the hightlighted entry as
           // well as the highlighted entry; then flip them
           var moveText1 = listField[selected+1].text;
           var moveText2 = listField[selected].text;
           var moveValue1 = listField[selected+1].value;
           var moveValue2 = listField[selected].value;
           listField[selected].text = moveText1;
           listField[selected].value = moveValue1;
           listField[selected+1].text = moveText2;
           listField[selected+1].value = moveValue2;
           listField.selectedIndex = selected+1; // Select the one that was selected before
        }  // Ends the check for selecting one which can be moved
     }  // Ends the check for there only being one in the list to begin with
  }  // Ends the check for there being something selected
}  // Ends the check for there being none in the list
}

function removeSelectedColumn(fbox) {
for(var i=0; i< fbox.options.length; i++) {
    if(fbox.options[i].selected && fbox.options[i].value != "") {
        fbox.options[i].value = "";
        fbox.options[i].text = "";
    }
}
BumpUp(fbox);
}

function move(fbox,tbox) {
for(var i=0; i< fbox.options.length; i++) {
    if(fbox.options[i].selected && fbox.options[i].value != "") {
        var no = new Option();
        no.value = fbox.options[i].value;
        no.text = fbox.options[i].text;
        tbox.options[tbox.options.length] = no;
    }
}
BumpUp(fbox);

}

function moveToListColumns(idBox,label,value) {
var no = new Option();
no.value = value;
no.text = label;
no.selected = true;
var ColumnBox = document.getElementById(idBox);
setAllUnSelected(ColumnBox);
if(existThisColumn(getValueByKey(value, '${KEYPATH}')) == "false"){
    ColumnBox.options[ColumnBox.options.length] = no;
}

//show label
changeSelect(lib_getObj("label"),ColumnBox);
}

function BumpUp(box)  {
for(var i=0; i<box.options.length; i++) {
if(box.options[i].value == "")  {
    for(var j=i; j<box.options.length-1; j++)  {
       box.options[j].value = box.options[j+1].value;
       box.options[j].text = box.options[j+1].text;
    }
  var ln = i;
  break;
}
}
if(ln < box.options.length)  {
box.options.length -= 1;
BumpUp(box);
}
}

function send(tbox) {
for(var i=0; i< tbox.options.length; i++) {
  tbox.options[i].selected = true;
}
}

function setLabel(labelText,fbox){

for(var i=0; i< fbox.options.length; i++) {
    if(fbox.options[i].selected && fbox.options[i].value != "") {
        var columnName = getStandartViewToLabel(fbox.options[i].text);
        fbox.options[i].text = labelText.value + " " + columnName;
        fbox.options[i].value = compoundSelectValue(fbox.options[i].value, labelText.value, '${KEYLABEL}');
        break;
    }
}
}

function getStandartViewToLabel(text){
var res;
//column order part
var orderViewText = "";
if(text.charAt(text.length-1) == ')'){
    var startIndex = text.lastIndexOf('(');
    orderViewText = text.substring(startIndex + 1, text.length-1);
    text = trim(text.substring(0,startIndex));
}
//column name part
if(text.charAt(text.length-1) == ']'){
    for(var i=text.length-1; i>=0; i--) {
        if(text.charAt(i) == '[') {
            res = text.substring(i,text.length);
            break;
        }
    }
}else{
    res = '['+text+']';
}
return res + compoundColumnOrderView(orderViewText);
}

function getStandartViewToOrder(text){
var res;
if(text.charAt(text.length-1) == ')'){
    var order = text.substring(text.lastIndexOf('('), text.length);
    res = trim(replace(text,order,""));
}else{
    res = text;
}
return res;
}

function changeSelect(labelText,fbox){
for(var i=0; i< fbox.options.length; i++) {
    if(fbox.options[i].selected && fbox.options[i].value != "") {
        var label = parserLabel(fbox.options[i].text);
        labelText.value = trim(label);

        //column order
        var orderValue = getValueByKey(fbox.options[i].value, '${KEYORDER}');
        columnOrderSelected(orderValue);
        break;
    }
}
}

function parserLabel(text){
var res;
//column order part
if(text.charAt(text.length-1) == ')'){
    var order = text.substring(text.lastIndexOf('('), text.length);
    text = trim(replace(text,order,""));
}
//column name part
if(text.charAt(text.length-1) == ']'){
    for(var i=text.length-1; i>=0; i--) {
        if(text.charAt(i) == '[') {
            res = text.substring(0,i);
            break;
        }
    }
}else{
    res = text;
}
return res;
}

function compoundSelectValue(value, newText, constantKey){
if(value.indexOf(constantKey) > -1){
    var firstPos = value.indexOf(constantKey);
    var lastPos = value.lastIndexOf(constantKey);
    var tempText = value.substring(firstPos,lastPos + constantKey.length);
    value = replace(value,tempText,"");
}
if(trim(newText) != ""){
    value = value + constantKey + newText + constantKey;
}
return value;
}

// search all the "textSearched" and replace
function replace(text,textSearched,newString) {

while (text.indexOf(textSearched) > -1) {
pos= text.indexOf(textSearched);
text = "" + (text.substring(0, pos) + newString + text.substring((pos + textSearched.length), text.length));
}
return text;
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

function setAllUnSelected(box){
for(var i=0; i< box.options.length; i++) {
    if(box.options[i].selected) {
        box.options[i].selected = false;
    }
}
}

function setColumnOrder(){
var columnBox = lib_getObj("listColumnsId");
var orderBox = lib_getObj("columnOrder");
var orderOption;

for(var i=0; i< orderBox.options.length; i++) {
    if(orderBox.options[i].selected) {
        orderOption = orderBox.options[i];
        break;
     }
}

for(var i=0; i< columnBox.options.length; i++) {
    if(columnBox.options[i].selected && columnBox.options[i].value != "") {
        var textView = getStandartViewToOrder(columnBox.options[i].text);
        columnBox.options[i].text = textView + compoundColumnOrderView(orderOption.text);
        columnBox.options[i].value = compoundSelectValue(columnBox.options[i].value, orderOption.value, '${KEYORDER}');
        break;
    }
}
}

function compoundColumnOrderView(viewText){
var res = "";
if(trim(viewText) != ""){
    res = "   (" + viewText + ")";
}
return res;
}

function getValueByKey(text, key){
var res="";
var firstPos = text.indexOf(key);
var lastPos = text.lastIndexOf(key);
if(firstPos != lastPos){
    res = text.substring(firstPos + key.length, lastPos);
}
///alert(res);
return res;
}

function columnOrderSelected(value){
var orderBox = lib_getObj("columnOrder");
for(var i=0; i< orderBox.options.length; i++) {
    if(orderBox.options[i].value == value) {
        orderBox.options[i].selected = true;
        break;
     }
}
}

function existThisColumn(columnPath){
var columnBox = lib_getObj("listColumnsId");
for(var i=0; i< columnBox.options.length; i++) {
    var path = getValueByKey(columnBox.options[i].value, '${KEYPATH}');
    if(columnPath == path) {
        columnBox.options[i].selected = true;
        return "true";
    }
}
return "false";
}

/*ajax management*/
//call through ajax from TreeColumn.jsp
function ajaxSetColumn(xmldoc) {

//hide tooltip, this method may be find in TreeColumn.jsp
hideToolTip();

if(xmldoc != null && xmldoc != undefined ){
    var filter = xmldoc.getElementsByTagName('column');
    if (filter.length == 0) return false;
}else{
    return false;
}

var label = unescape(xmldoc.getElementsByTagName('label').item(0).firstChild.nodeValue);
var value = xmldoc.getElementsByTagName('value').item(0).firstChild.nodeValue;

var str = "label:" + label;
str += "<br>";
str += "value:" + value;

var mydiv = document.getElementById("divView");
//mydiv.innerHTML = str;

//set column
moveToListColumns('listColumnsId',label,value);
}


//-->
</script>

<html:form action="${action}" focus="listColumns">

<html:hidden property="dto(reportId)" value="${param['reportId']}"/>
<%--<html:hidden property="dto(reportId)" value="${columnForm.dtoMap['reportId']}"/>--%>
<html:hidden property="dto(initTable)" value="${initReportTable}"/>

<table cellSpacing=0 cellPadding=4 width="90%" border=0 align="center">
<tr>
    <td class="button">
        <app2:checkAccessRight functionality="COLUMN" permission="UPDATE">
            <html:submit onclick="javascript:send(this.form.listColumns)"  styleClass="button">${button}</html:submit>
        </app2:checkAccessRight>
    </td>
</tr>
</table>

<table id="Columns.jsp" border="0" cellpadding="0" cellspacing="0" width="90%" align="center" class="container">
<tr>
    <td colspan="2" class="title">
        <c:out value="${title}"/>
    </td>
</tr>
<tr>
    <td class="contain">
        <table>
            <tr>
                <td class="contain">
                    <fmt:message key="Report.columns.displayColumns"/>
                </td>
                <td class="contain">&nbsp;</td>
                <td class="contain">
                    <fmt:message key="Report.columns"/>
                </td>
            </tr>
            <tr>
                <td class="containTop">
                    <table>
                        <tr>
                            <td colspan="2">
                                <c:set var="labelValueColumns"
                                       value="${app2:getColumnsWithMessage(dto.columnList,pageContext.request)}"/>
                                <c:forEach var="item" items="${labelValueColumns}" varStatus="x">
                                    <html:hidden property="previousColumns" value="${item.value}" styleId="preColId${x.index}"/>
                                </c:forEach>
                                <html:select property="listColumns" styleId="listColumnsId" multiple="true"
                                             size="15" styleClass="multipleColumnSelect"
                                             onchange="changeSelect(this.form.label,this.form.listColumns)"
                                             tabindex="1">
                                    <c:forEach var="item" items="${labelValueColumns}">
                                        <html:option value="${item.value}"><c:out value="${item.label}"/>
                                        </html:option>
                                    </c:forEach>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <fmt:message key="Report.columns.label"/>
                                <app:text property="label" value="" styleClass="text" maxlength="40"
                                          onkeyup="javascript:setLabel(this.form.label,this.form.listColumns)"/>
                            </td>
                            <td align="right">
                                <fmt:message key="Report.columns.order"/>
                                <html:select property="columnOrder" value="" styleClass="shortSelect" onchange="javascript:setColumnOrder()" onkeyup="javascript:setColumnOrder()" tabindex="2">
                                    <option></option>
                                    <c:forEach var="item" items="${order}">
                                        <html:option value="${item.value}">${item.label}</html:option>
                                    </c:forEach>
                                </html:select>
                            </td>
                        </tr>
                    </table>
                </td>
                <td class="containTop">
                    <table cellpadding="2" cellspacing="0">
                        <tr>
                            <td><input type="button" value="" onclick="moveUpList(this.form.listColumns)"
                                       name="B5" class="adminUpArrow" title="<fmt:message key="Common.up"/>"
                                       tabindex="3"/></td>
                        </tr>
                        <tr>
                            <td><input type="button" value="" onclick="moveDownList(this.form.listColumns)"
                                       name="B5" class="adminDownArrow" title="<fmt:message key="Common.down"/>"
                                       tabindex="4"/></td>
                        </tr>
                        <tr>
                            <td>
                                <input type="button" value="" onclick="removeSelectedColumn(this.form.listColumns)" name="B1"
                                       class="removeButton" title="<fmt:message key="Common.delete"/>"
                                       tabindex="5"/>
                            </td>
                        </tr>
                    </table>
                </td>
                <td class="containTop">
                    <table>
                        <tr>
                            <td>
                                <div>
                                    <c:set var="ajaxCallJavaScript" value="ajaxSetColumn" scope="request"/>
                                    <c:set var="ajaxUrlExecute" value="/reports/Report/Forward/TreeColumns.do" scope="request"/>
                                    <c:import url="/common/reports/TreeColumn.jsp"/>
                                </div>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>
<table cellSpacing=0 cellPadding=4 width="90%" border=0 align="center">
<tr>
    <td class="button">
        <app2:checkAccessRight functionality="COLUMN" permission="UPDATE">
            <html:submit onclick="javascript:send(this.form.listColumns)" styleClass="button"
                         tabindex="10">${button}</html:submit>
        </app2:checkAccessRight>
    </td>
</tr>
</table>
</html:form>