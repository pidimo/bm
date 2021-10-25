<%@ include file="/Includes.jsp"%>
<script>
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
    document.getElementById('isSubmit').value = true
    document.getElementById('listc').submit();
}

</script>
<html:form  action="${actionReload}" styleId="listc" focus="parameter(lastName@_firstName@_login)" >
<table width="80%" border="0" align="center" cellpadding="2" cellspacing="0">
    <tr>
        <td colspan="2" >
        <table width="100%"  border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
        <tr>
            <td height="20" colspan="3"  class="title">
            <fmt:message    key="RoleUser.user.add"/>
            </td>
        </tr>
        <tr>
            <td class="label" width="15%"><fmt:message key="Common.search"/></td>
            <td align="left" class="contain" width="85%">
                <html:text property="parameter(lastName@_firstName@_login)" styleClass="largeText" maxlength="40"/>&nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
            </td>
        </tr>


        <tr>
            <td colspan="3" align="center" class="alpha">
            <fanta:alphabet action="RoleUser/Forward/UserImportList.do?roleId=${param.roleId}&roleName=${param.roleName}"
 parameterName="lastName"/>
            </td>
        </tr>
        </table>
        </td>
    </tr>

    <html:hidden property="isSubmit" value="false" styleId="isSubmit"/>
    <html:hidden property="dto(roleId)" value="${param.roleId}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <tr>
        <td class="button">
        <br/>
        <app2:checkAccessRight functionality="USER" permission="UPDATE">
            <html:button property="" styleClass="button" onclick="goSubmit();" >
                <fmt:message   key="Contact.Organization.Employee.addEmployee"/>
            </html:button>
        </app2:checkAccessRight>
        <html:cancel styleClass="button" ><fmt:message key="Common.cancel"/></html:cancel>
        </td>
    </tr>
    <tr><td>
    <fanta:table  width="100%" id="userGroup"  action="${action}" imgPath="${baselayout}" align="left" list="roleUserSearchList"  >
        <fanta:checkBoxColumn  name="guia" id="aditionals" onClick="javascript:check();" property="userId" headerStyle="listHeader" styleClass="radio listItemCenter" width="5%"  />
        <fanta:dataColumn name="userName" styleClass="listItem" title="Employee.name"  headerStyle="listHeader" width="45%" orderable="true" maxLength="40"/>
        <fanta:dataColumn name="login" styleClass="listItem" title="User.userName"  headerStyle="listHeader" width="25%" orderable="true"/>
        <fanta:dataColumn name="type" styleClass="listItem2" title="User.typeUser"  renderData="false"   headerStyle="listHeader" width="15%" orderable="true">
              <c:if test="${userGroup.type == '1'}">
                <fmt:message key="User.intenalUser" />
              </c:if>
              <c:if test="${userGroup.type == '0'}">
                <fmt:message key="User.externalUser" />
              </c:if>
       </fanta:dataColumn>
    </fanta:table>
    </td></tr>
</table>
</html:form>

