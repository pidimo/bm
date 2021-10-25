<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="obj" var="jsJumpUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(countryId)" value="field"/>
</app2:jScriptUrl>
<script language="JavaScript">
    <!--
    function jump(obj) {
        field = document.getElementById('searchForm').country.value;
        cityField = document.getElementById('searchForm').city.value;
        cad = ${jsJumpUrl}
                window.location = cad;
    }
    //-->
</script>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/SearchCity.do"
               focus="parameter(cityName)"
               styleId="searchForm"
               styleClass="form-horizontal">
        <html:hidden property="parameter(companyId)"
                     value="${sessionScope.user.valueMap['companyId']}"/>

        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="selectId">
                <fmt:message key="Contact.country"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <fanta:select property="parameter(countryId)"
                              listName="countryBasicList"
                              firstEmpty="true"
                              labelProperty="name"
                              valueProperty="id"
                              module="/catalogs"
                              styleClass="${app2:getFormSelectClasses()} mediumSelect"
                              tabIndex="1"
                              styleId="country">
                    <fanta:parameter field="companyId"
                                     value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </div>
        </div>

        <div class="row">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="selectId">
                <fmt:message key="Contact.cityZip"/>
            </label>

            <div class="row col-sm-10">
                <div class="col-xs-12 col-sm-3 wrapperButton">
                    <html:text property="parameter(zip)"
                               styleClass="${app2:getFormInputClasses()} zipText"
                               maxlength="10"
                               titleKey="Contact.zip"
                               tabindex="2"/>
                </div>
                <div class="col-xs-12 col-sm-5 wrapperButton">
                    <html:text property="parameter(cityName)"
                               styleClass="${app2:getFormInputClasses()} mediumText"
                               maxlength="40"
                               titleKey="Contact.city"
                               tabindex="3"
                               styleId="city"/>
                </div>
                <div class="col-xs-12 col-sm-4 wrapperButton">
                    <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="4">
                        <fmt:message key="Common.search"/>
                    </html:submit>
                    <app2:checkAccessRight functionality="CITY" permission="CREATE">
                        <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="6" property="createButton">
                            <fmt:message key="Common.create"/>
                        </html:submit>
                    </app2:checkAccessRight>
                </div>
            </div>
        </div>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="SearchCity.do" parameterName="cityNameAlpha"
                            onClick="jump(this);return false;"
                            mode="bootstrap"/>
        </div>
    </html:form>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="searchCityList"
                     width="100%" id="city"
                     action="SearchCity.do"
                     imgPath="${baselayout}"
                     align="center"
                     styleClass="${app2:getFantabulousTableClases()}">
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <fanta:actionColumn name="" useJScript="true"
                                    action="javascript:parent.selectCity('${city.countryId}', '${city.cityId}', '${app2:jscriptEncode(city.zip)}', '${app2:jscriptEncode(city.name)}');"
                                    title="Common.select" styleClass="listItem" headerStyle="listHeader"
                                   glyphiconClass="${app2:getClassGlyphImport()}">
                </fanta:actionColumn>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" styleClass="listItem" title="City.name" headerStyle="listHeader"
                              orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="zip" styleClass="listItem" title="City.zip" headerStyle="listHeader"
                              orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="country" styleClass="listItem" title="City.country" headerStyle="listHeader"
                              orderable="true">
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>