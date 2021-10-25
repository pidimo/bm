<!--this page is deprecated-->
<%@ page import="com.piramide.elwis.utils.ContactConstants"%>
<%@ include file="/Includes.jsp" %>

<script language="JavaScript">
function cancel(){
    window.close();
}
function check(){
    opener.window.location.reload();
}
</script>

<c:if test="${true != dto.haveErrors && null != dto.test}">
    <script language="JavaScript">
    check();
    window.close();
    </script>
</c:if>

<c:set var="person" value="<%=ContactConstants.ADDRESSTYPE_PERSON%>"/>
<c:set var="organization" value="<%=ContactConstants.ADDRESSTYPE_ORGANIZATION%>"/>
<br/>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>
<html:form action="/Mail/CreateMailCommunication.do" styleId="communicationForm" >

    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(size)" value="${dto.size}"/>
    <html:hidden property="dto(mailId)" value="${dto.mailId}"/>
    <html:hidden property="dto(email)" value="${dto.email}"/>
    <html:hidden property="dto(inOut)" value="${dto.inOut}"/>  

    <table  border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
    <tr>
        <TD colspan="4" class="title"><fmt:message key="Webmail.mailCommunications.plural"/></TD>
    </tr>
    <TR>
        <td class="label" width="10%">&nbsp;</TD>
        <td class="label" width="30%"><fmt:message key="Common.email"/></TD>
        <td class="label" width="30%"><fmt:message key="Webmail.mailCommunications.name"/></TD>
        <td class="label" width="30%"><fmt:message key="Webmail.mailCommunications.contactPersonOf"/></TD>
    </TR>
<c:forEach var="item" items="${dto.webmailCommunications}" varStatus="counter" >


    <tr>
        <td class="contain">
            <c:if test="${null != item.contactId}" >
                <input type="checkbox" name="dto(checkBox_${counter.count})" id="box"
value="addressId=${item.address.addressId}_contactPersonId=${item.chief.contactPersonId}_contactId=${item.contactId}" disabled="disabled" checked="checked" class="radio">
            </c:if>
            <c:if test="${null == item.contactId && null == dto.read}" >
                <input type="checkbox" name="dto(checkBox_${counter.count})" id="box"
value="addressId=${item.address.addressId}_contactPersonId=${item.chief.contactPersonId}_contactId=${item.contactId}" class="radio">
            </c:if>

            <c:if test="${null == item.contactId && null != dto.read}" >
                <input type="checkbox" name="dto(checkBox_${counter.count})" id="box"
value="addressId=${item.address.addressId}_contactPersonId=${item.chief.contactPersonId}_contactId=${item.contactId}" disabled="disabled"  class="radio">
            </c:if>

        </td>
        <td class="contain" >${item.email}</td>
<td class="contain" >


<c:set var="name">
        <c:if test="${item.address.addressType == person}" >${item.address.name1}<c:if test="${item.address.name2 != null}">, ${item.address.name2}</c:if></c:if>
        <c:if test="${item.address.addressType == organization}" >${item.address.name1}<c:if test="${item.address.name2 != null}">&nbsp;${item.address.name2}<c:if test="${item.address.name3 != null}">&nbsp;${item.address.name3}</c:if></c:if></c:if>
</c:set>
${name}
<html:hidden property="dto(name_${counter.count})" value="${name}"  styleId="name_${counter.count}_Id" />


</td>
<td class="contain" >

        <c:if test="${item.chief.addressType == person}" >${item.chief.name1}<c:if test="${item.chief.name2 != null}">, ${item.chief.name2}</c:if></c:if>
        <c:if test="${item.chief.addressType == organization}" >${item.chief.name1}<c:if test="${item.chief.name2 != null}">&nbsp;${item.chief.name2}<c:if test="${item.chief.name3 != null}">&nbsp;${item.chief.name3}</c:if></c:if></c:if>
</td>
    </tr>
</c:forEach>
    </table>

    <table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
    <TR>
        <TD class="button">
<app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
    <c:if test="${0 != dto.size && true == dto.haveAlmostOne && null == dto.read}">
        <html:submit property="dto(save)" styleClass="button" ><fmt:message   key="Common.save"/></html:submit>
    </c:if>
</app2:checkAccessRight>
            <html:button property="dto(cancel)"  styleClass="button" onclick="cancel()" ><fmt:message   key="Common.cancel"/></html:button>
        </TD>
    </TR>
    </table>

    </td>
</html:form>
</tr>
</table>
