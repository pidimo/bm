<%@ include file="/Includes.jsp" %>

<app:url var="addUser" value="/RoleUser/Forward/UserImportList.do"/> <%--AQUI--%>
<script language="JavaScript">
 <!--
function jump(obj){
    window.location='${addUser}';
}

function check()
{
field = document.getElementById('listc').aditionals;
guia = document.getElementById('listc').guia;

var i;

if (guia.checked){
    for (i=0; i < field.length; i++)
    field[i].checked = true;
}else{
        for(i=0; i<field.length; i++)
        field[i].checked = false;
    }
}

function goSubmit() {
    document.getElementById('isSubmit').value = true;
    document.getElementById('listc').submit();
}
//-->
</script>

<table cellpadding="0" cellspacing="0" border="0" align="center" width="95%" >
<html:form action="/RoleUser/RoleUserList.do" styleId="listc" > <%--AQUI--%>
<html:hidden property="isSubmit" value="false" styleId="isSubmit"/>

<tr>
    <td>
    <table cellpadding="2" cellspacing="0" border="0"  align="center" width="65%" class="container" >
    <tr>
        <td height="20" colspan="3"  class="title"><fmt:message key="Admin.User.Title.search"/></td>
    </tr>
    <tr>
        <td class="label" width="15%"><fmt:message key="Common.search"/></td>
        <td align="left" class="contain" width="85%">
            <html:text property="parameter(name1@_name2@_name3)" styleClass="largeText" maxlength="40"/>
            &nbsp;
            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
        </td>
    </tr>
    <tr>
        <td colspan="3" align="center" class="alpha">
            <fanta:alphabet action="RoleUser/RoleUserList.do?roleId=${param.roleId}&roleName=${app2:encode(param.roleName)}" parameterName="name1Alpha"/>
        </td>
    </tr>
    </table>
    <br/>
    </td>
</tr>

<tr>
    <td>
    <table cellpadding="0" cellspacing="2" border="0" width="65%"  align="center" >

    <tr>
        <td class="button">
        <app2:checkAccessRight functionality="USER" permission="UPDATE">
            <html:button property="dto(add)" styleClass="button"  onclick="javascript:jump(this);" >
                <fmt:message key="Common.add"/>
            </html:button>
            &nbsp;
            <html:button property="dto(delete)" styleClass="button" onclick="javascript:goSubmit();" >
                <fmt:message key="Common.deleteSelected"/>
            </html:button>
        </app2:checkAccessRight>
        </td>
    </tr>

    <tr>
        <td>
<fanta:table list="roleUserList" width="100%" id="myUser" action="RoleUser/RoleUserList.do?roleId=${param.roleId}" imgPath="${baselayout}"  >

    <app:url var="deleteAction" value="RoleUser/Forward/Delete.do?dto(userId)=${myUser.userId}&dto(roleId)=${myUser.roleId}" enableEncodeURL="false"/>
    <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
        <app2:checkAccessRight functionality="USER" permission="VIEW">
            <fanta:checkBoxColumn  name="guia" id="aditionals" onClick="javascript:check();" property="userId"
 headerStyle="listHeader" styleClass="radio listItemCenter"/>
            <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}" styleClass="listItem"
 headerStyle="listHeader" image="${baselayout}/img/delete.gif" />
        </app2:checkAccessRight>
    </fanta:columnGroup>
    <fanta:dataColumn name="userName" styleClass="listItem2" title="Contact.name"  headerStyle="listHeader" width="95%"
 orderable="true" maxLength="25" />

</fanta:table>
        </td>
    </tr>

    </table>
    </td>
</tr>
</html:form>
</table>




