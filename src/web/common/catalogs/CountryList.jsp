<%@ page language="java" session="true"%>
<%@ include file="/Includes.jsp" %>

<table width="70%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
    <td>
<html:form action="/Country/List.do">
    <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
    <TR>
        <td class="label"><fmt:message key="Common.search"/></td>
        <td align="left" class="contain">
            <html:text property="parameter(countryName)" styleClass="largeText" />
            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
        </td>
    </TR>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="Country/List.do" parameterName="countryNameAlpha"/>
        </td>
    </tr>
    </table>
</html:form>
    </td>
</tr>

<tr>
    <td>

<app2:checkAccessRight functionality="COUNTRY" permission="CREATE">
    <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0" class="container">
    <TR>
        <html:form styleId="CREATE_NEW_COUNTRY" action="/Country/Forward/Create.do?op=create">
            <TD colspan="6" class="button">
                <html:submit styleClass="button"><fmt:message   key="Common.new"/></html:submit>
            </TD>
        </html:form>
    </TR>
    </table>
</app2:checkAccessRight>

    </td>
</tr>

<tr>
    <td>

    <TABLE border="0" cellpadding="0" cellspacing="0" width="100%" class="container" align="center">
        <TR align="center" height="20">
        <td align="center">
            <fanta:table list="countryList" width="100%" id="country" action="Country/List.do" imgPath="${baselayout}">
                <c:set var="editAction" value="Country/Forward/Update.do?dto(countryId)=${country.id}&dto(currencyId)=${country.myCurrencyId}&dto(countryName)=${app2:encode(country.name)}"/>
                <c:set var="deleteAction" value="Country/Forward/Delete.do?dto(withReferences)=true&dto(countryId)=${country.id}&dto(countryName)=${app2:encode(country.name)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" >
                    <app2:checkAccessRight functionality="COUNTRY" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="COUNTRY" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>

                <fanta:dataColumn name="name" action="${editAction}"  styleClass="listItem" title="Country.name"  headerStyle="listHeader" width="40%" orderable="true" maxLength="25" >
                </fanta:dataColumn>
                <fanta:dataColumn name="areaCode" styleClass="listItem" title="Country.areaCode"  headerStyle="listHeader" width="15%" orderable="true" >
                </fanta:dataColumn>
                <fanta:dataColumn name="prefix" styleClass="listItem" title="Country.prefix"  headerStyle="listHeader" width="15%" orderable="true" >
                </fanta:dataColumn>
                <fanta:dataColumn name="currency" styleClass="listItem2" title="Country.currency" headerStyle="listHeader" width="30%" orderable="true" >
                </fanta:dataColumn>
            </fanta:table>
        </td>
        </tr>
        </TABLE>
    </td>

</tr>
</table>