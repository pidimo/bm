<%@ page import="org.alfacentauro.fantabulous.web.form.SearchForm"%>
<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="obj" var="jsJumpUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(countryId)" value="field"/>
</app2:jScriptUrl>

<script language="JavaScript">
<!--
function jump(obj)
{
    field = document.getElementById('searchForm').country.value;
    cityField = document.getElementById('searchForm').city.value;
    window.location=${jsJumpUrl};
}
//-->
</script>

<%
    SearchForm mySearchForm = (SearchForm) request.getAttribute("listForm");
    if(null != request.getParameter("parameter(countryId)") &&
            !"".endsWith(request.getParameter("parameter(countryId)").toString())){
        try{
            Integer countryId = Integer.valueOf(request.getParameter("parameter(countryId)").toString());
            mySearchForm.setParameter("countryId", countryId);
        }catch(NumberFormatException e){
        }
    }
%>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>
<html:form action="/City/List.do" styleId="searchForm" >
    <TABLE id="CityList.jsp" border="0" cellpadding="3" cellspacing="0" width="70%" class="searchContainer" align="center">
    <tr>
        <td class="label"><fmt:message key="Contact.country"/></td>
        <td align="left" class="contain">
            <fanta:select property="parameter(countryId)" listName="countryList" labelProperty="name"  valueProperty="id" firstEmpty="true" styleClass="mediumSelect" styleId="country" >
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
            </fanta:select>
        </td>
    </tr>
    <tr>
        <td class="label"><fmt:message   key="Contact.cityZip"/></td>
        <td align="left" class="contain">
            <html:text property="parameter(zip)" styleClass="zipText"  titleKey="Contact.zip"/>
            <html:text property="parameter(cityName)" styleClass="mediumText" titleKey="Contact.city" styleId="city" />
            <html:submit styleClass="button"><fmt:message   key="Common.go"/></html:submit>&nbsp;
        </td>
    </tr>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="City/List.do" parameterName="cityNameAlpha" onClick="jump(this);return false;" />
        </td>
    </tr>
    </table>
    <app2:checkAccessRight functionality="CITY" permission="CREATE">
        <TABLE id="CityList.jsp" border="0" cellpadding="2" cellspacing="0" width="70%" class="container" align="center">
        <TR>
            <td colspan="2" class="button">
                <html:submit property="parameter(new)" styleClass="button"><fmt:message key="Common.new"/></html:submit>
            </td>
        </TR>
        </TABLE>
    </app2:checkAccessRight>
</html:form>
    <TABLE id="CityList.jsp" border="0" cellpadding="0" cellspacing="0" width="70%" class="container" align="center">
    <tr>
        <td align="center">
            <fanta:table list="cityList" width="100%" id="city" action="City/List.do" imgPath="${baselayout}"  >
                <c:set var="editAction" value="City/Forward/Update.do?dto(cityId)=${city.id}&dto(cityName)=${app2:encode(city.name)}&dto(cityZip)=${app2:encode(city.zip)}"/>
                <c:set var="deleteAction" value="City/Forward/Delete.do?dto(withReferences)=true&dto(cityId)=${city.id}&dto(cityName)=${app2:encode(city.name)}&dto(cityZip)=${app2:encode(city.zip)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="CITY" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="CITY" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>

                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="City.name"  headerStyle="listHeader" width="40%" orderable="true" maxLength="25" >
                </fanta:dataColumn>
                <fanta:dataColumn name="zip" styleClass="listItem" title="City.zip"  headerStyle="listHeader" width="20%" orderable="true" >
                </fanta:dataColumn>
                <fanta:dataColumn name="country" styleClass="listItem2" title="City.country"  headerStyle="listHeader" width="35%" orderable="true">
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
    </TABLE>
    </td>
</tr>
</table>

