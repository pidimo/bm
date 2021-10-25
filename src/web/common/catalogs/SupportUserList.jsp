<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
    <td>

    <table width="90%" border="0" align="center" cellpadding="2" cellspacing="0">
    <TR>

    <html:form  action="${create}">
        <TD class="button">
        <app2:securitySubmit operation="create" functionality="SUPPORTUSER" styleClass="button" >
                    <fmt:message    key="Common.new"/>
          </app2:securitySubmit>
        </TD>
    </html:form>
    </TR>
    </table>

    <TABLE border="0" cellpadding="0" cellspacing="0" width="90%" class="container" align="center">
    <TR align="center">
        <td>
        <fanta:table list="supportUserList" width="100%" id="supportUser" action="${action}" imgPath="${baselayout}"  >
            <c:set var="editAction" value="${edit}?dto(productName)=${app2:encode(supportUser.productName)}&dto(productId)=${supportUser.productId}&dto(userId)=${supportUser.userId}"/>
            <c:set var="deleteAction" value="${delete}?dto(withReferences)=true&dto(productName)=${app2:encode(supportUser.productName)}&dto(productId)=${supportUser.productId}&dto(userId)=${supportUser.userId}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="3%">
                <fanta:actionColumn name="up" action="${editAction}"  title="Common.update" styleClass="listItem" headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif"/>
                <fanta:actionColumn name="delete" action="${deleteAction}" title="Common.delete" styleClass="listItem" headerStyle="listHeader" width="5%" image="${baselayout}/img/delete.gif"/>
            </fanta:columnGroup>
            <fanta:dataColumn name="productName" action="${editAction}" styleClass="listItem" title="SupportUser.supportProduct"  headerStyle="listHeader" width="25%" orderable="true" maxLength="30" />
            <fanta:dataColumn name="userName" styleClass="listItem" title="SupportUser.supportUser"  headerStyle="listHeader" width="30%" orderable="true" />
            <fanta:dataColumn name="emailSupportCase" styleClass="listItem2" title="Admin.CaseEmail"  headerStyle="listHeader" width="40%" orderable="true" />
        </fanta:table>
        </td>
    </tr>
    </table>
    </td>
</tr>
</table>