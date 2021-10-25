<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
/*** Javascript functions with utilities for module admin **/
<!--

function send(tbox, tbox1) {
    var str="";
	for(var i=0; i< tbox.options.length; i++) {
	  tbox.options[i].selected = true;
    }
    for(var i=0; i< tbox1.options.length; i++) {
	  tbox1.options[i].selected = true;
    }
}

function removeBox(box){
  for(var i=0; i< box.options.length; i++) {
    if(box.options[i].selected && box.options[i].value != "") {
       var valueOption = "";
       valueOption = box.options[i].value;
       var separator = valueOption.lastIndexOf(":");
       type = valueOption.substring(0,separator);
       if("single" == type){
           //alert("enter here 1");
       }else if("range"){
           //alert("enter here 2");
       }
       box.options[i].value = "";
       box.options[i].text = "";

    }
  }
  BumpUp(box);
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


function categoryTitle(box,category)
{
    if(box.options[box.selectedIndex].value != ""){
        category.value=box.options[box.selectedIndex].text;
    }
}

sortitems = 1;

function alls(fbox,tbox) {
for(var i=0; i< fbox.options.length; i++) {
if(fbox.options[i].value != "") {
fbox.options[i].selected = true;
var no = new Option();
no.value = fbox.options[i].value;
no.text = fbox.options[i].text;
tbox.options[tbox.options.length] = no;
fbox.options[i].value = "";
fbox.options[i].text = "";
 }
}
BumpUp(fbox);
if (sortitems) SortD(tbox);
}


function move(fbox,tbox) {
for(var i=0; i< fbox.options.length; i++) {
if(fbox.options[i].selected && fbox.options[i].value != "") {
var no = new Option();
no.value = fbox.options[i].value;
no.text = fbox.options[i].text;
tbox.options[tbox.options.length] = no;
fbox.options[i].value = "";
fbox.options[i].text = "";
 }
}

BumpUp(fbox);
if (sortitems) SortD(tbox);
}

function SortD(box)  {
var temp_opts = new Array();
var temp = new Object();
for(var i=0; i < box.options.length; i++)  {
temp_opts[i] = box.options[i];
}
for(var x=0; x< temp_opts.length-1; x++)  {
for(var y=(x+1); y< temp_opts.length; y++)  {
if(temp_opts[x].text > temp_opts[y].text)  {
temp = temp_opts[x].text;
temp_opts[x].text = temp_opts[y].text;
temp_opts[y].text = temp;
temp = temp_opts[x].value;
temp_opts[x].value = temp_opts[y].value;
temp_opts[y].value = temp;
    }
 }
}
for(var i=0; i< box.options.length; i++)  {
box.options[i].value = temp_opts[i].value;
box.options[i].text = temp_opts[i].text;
  }
}

function userTypeChange(){
    if(document.getElementById('type').value == '1') {
        document.getElementById('fieldAddressName_id').value = "";
        document.getElementById('employeeAreaTitle').style.display = "";
        document.getElementById('addressAreaTitle').style.display = "none";
    }else if(document.getElementById('type').value == '0'){
        document.getElementById('fieldEmployeeName_id').value = "";
        document.getElementById('addressAreaTitle').style.display = "";
        document.getElementById('employeeAreaTitle').style.display = "none";
    }else if(document.getElementById('type').value == ''){
        document.getElementById('addressAreaTitle').style.display = "none";
        document.getElementById('employeeAreaTitle').style.display = "none";
        document.getElementById('fieldEmployeeName_id').value = "";
        document.getElementById('fieldAddressName_id').value = "";
    }
}

//-->