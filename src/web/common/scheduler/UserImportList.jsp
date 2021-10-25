<%@ include file="/Includes.jsp" %>


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
    document.getElementById('Import_value').value = true;
    document.forms[1].submit();
}

</script>


<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<br>
<table width="80%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
<tr>
    <td height="20" class="title" colspan="5">
        <fmt:message key="Admin.User.Title"/>
    </td>
</tr>
<html:form action="/${action}" focus="parameter(lastName@_firstName@_login)">
    <tr>
        <td class="label" width="15%"><fmt:message key="Common.search"/></td>
        <td align="left" class="contain" width="30%">
            <html:text property="parameter(lastName@_firstName@_login)" styleClass="mediumText" maxlength="40"/>
            &nbsp;
        </td>
<c:if test="${param.module !='scheduler'}" >
        <td align="left" class="contain" width="50%">
            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
        </td>
</c:if>
<c:if test="${param.module=='scheduler'}" >
        <td  class="label"  width="20%">
          <fmt:message key="User.externalAlso"/>&nbsp;
        </td>
        <td align="left" class="contain" width="10%">
            <html:checkbox property="parameter(userType)" tabindex="2" styleClass="radio"/>
        </td>
        <td align="left" class="contain" width="20%">
            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
        </td>
</c:if>
    </tr>
  
</html:form>
<tr>
    <td colspan="5" align="center" class="alpha">
        <fanta:alphabet action="${action}" parameterName="lastNameAlpha"/>
    </td>
</tr>

<html:form action="${fantaImportAction}" styleId="listc">
    
    <html:hidden property="Import_value" value="false" styleId="Import_value"/>
    <html:hidden property="dto(userGroupId)" value="${param.userGroupId}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <tr>
        <td class="button" colspan="5">
        <html:button property="" styleClass="button" onclick="goSubmit()" ><fmt:message   key="Contact.Organization.Employee.addEmployee"/></html:button>
           <html:cancel styleClass="button" property="dto(cancel)"><fmt:message key="Common.cancel"/></html:cancel>
        </td>
    </tr>
<tr>
   <td colspan="5">
        <fanta:table width="100%" id="elwisUser" action="${action}" imgPath="${baselayout}"
                     align="center">
                <c:set var="importAction" value="${fantaImportAction}&dto(userId)=${elwisUser.userId}&dto(title)=${app2:encode(param['dto(title)'])}"/>
<fanta:checkBoxColumn  name="guia" id="aditionals" onClick="javascript:check();" property="userId" headerStyle="listHeader" styleClass="radio listItemCenter" width="5%"  />
            <fanta:dataColumn name="userName" styleClass="listItem" title="Employee.name" headerStyle="listHeader"
                              width="40%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="login" styleClass="listItem" title="User.userName" headerStyle="listHeader"
                              width="35%" orderable="true"/>
            <fanta:dataColumn name="type" styleClass="listItem" title="User.typeUser" renderData="false"
                              headerStyle="listHeader" width="15%" orderable="true">
                <c:if test="${elwisUser.type == '1'}">
                    <fmt:message key="User.intenalUser"/>
                </c:if>
                <c:if test="${elwisUser.type == '0'}">
                    <fmt:message key="User.externalUser"/>
                </c:if>
            </fanta:dataColumn>
        </fanta:table>
</html:form>
</td>
</tr>
</table>
