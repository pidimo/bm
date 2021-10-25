<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="obj" var="jsJumpUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(countryId)" value="field"/>
</app2:jScriptUrl>

<script language="JavaScript">

    function select(id, name) {
        opener.selectField('fieldCityId_id', id, 'fieldCityName_id', name);
    }

    <!--
    function jump(obj) {
        field = document.getElementById('searchForm').country.value;
        cityField = document.getElementById('searchForm').city.value;
        cad = ${jsJumpUrl}
                window.location = cad;
    }
    //-->
</script>

<br/>
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="2">

            <fmt:message key="City.Title.search"/>

        </td>
    </tr>

    <html:form action="/SearchCity.do" focus="parameter(cityName)" styleId="searchForm">
        <html:hidden property="parameter(companyId)"
                     value="${sessionScope.user.valueMap['companyId']}"/>

        <tr>
            <td class="label"><fmt:message key="Contact.country"/></td>
            <td align="left" class="contain">
                <fanta:select property="parameter(countryId)" listName="countryBasicList"
                              firstEmpty="true" labelProperty="name" valueProperty="id"
                              module="/catalogs" styleClass="mediumSelect" tabIndex="1"
                              styleId="country">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
        </tr>
        <tr>
            <td class="label"><fmt:message key="Contact.cityZip"/></td>
            <td class="contain">
                <html:text property="parameter(zip)" styleClass="zipText" maxlength="10"
                           titleKey="Contact.zip" tabindex="2"/>
                <html:text property="parameter(cityName)" styleClass="mediumText" maxlength="40"
                           titleKey="Contact.city" tabindex="3" styleId="city"/>&nbsp;
                <html:submit styleClass="button" tabindex="4"><fmt:message key="Common.search"/>
                </html:submit>&nbsp;
            </td>
        </tr>

        <tr>
            <td colspan="2" align="center" class="alpha">
                <fanta:alphabet action="SearchCity.do" parameterName="cityNameAlpha"
                                onClick="jump(this);return false;"/>
            </td>
        </tr>
    </html:form>

    <tr>
        <td colspan="2"><br>
            <fanta:table list="searchCityList" width="100%" id="city" action="SearchCity.do" imgPath="${baselayout}"
                         align="center">
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <fanta:actionColumn name="" useJScript="true"
                                        action="javascript:select('${app2:jscriptEncode(city.cityId)}', '${app2:jscriptEncode(city.name)}');"
                                        title="Common.select" styleClass="listItem" headerStyle="listHeader"
                                        image="${baselayout}/img/import.gif"/>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" styleClass="listItem" title="City.name" headerStyle="listHeader"
                                  width="40%" orderable="true">
                </fanta:dataColumn>
                <fanta:dataColumn name="zip" styleClass="listItem" title="City.zip" headerStyle="listHeader" width="20%"
                                  orderable="true">
                </fanta:dataColumn>
                <fanta:dataColumn name="country" styleClass="listItem" title="City.country" headerStyle="listHeader"
                                  width="35%" orderable="true">
                </fanta:dataColumn>
            </fanta:table>

        </td>
    </tr>
</table>
