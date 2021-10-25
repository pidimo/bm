<%@ page import="org.alfacentauro.fantabulous.web.form.SearchForm" %>
<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="obj" var="jsJumpUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(countryId)" value="field"/>
</app2:jScriptUrl>

<script language="JavaScript">
    <!--
    function jump(obj) {
        field = document.getElementById('searchForm').country.value;
        cityField = document.getElementById('searchForm').city.value;
        window.location =${jsJumpUrl};
    }
    //-->
</script>

<%
    SearchForm mySearchForm = (SearchForm) request.getAttribute("listForm");
    if (null != request.getParameter("parameter(countryId)") &&
            !"".endsWith(request.getParameter("parameter(countryId)").toString())) {
        try {
            Integer countryId = Integer.valueOf(request.getParameter("parameter(countryId)").toString());
            mySearchForm.setParameter("countryId", countryId);
        } catch (NumberFormatException e) {
        }
    }
%>
<div class="${app2:getListWrapperClasses()}">
    <html:form action="/City/List.do" styleId="searchForm" styleClass="form-horizontal">
        <div class="row wrapperSearch">
            <div class="form-group col-sm-4">
                <label class="${app2:getFormLabelClasses()} label-left" for="country">
                    <fmt:message key="Contact.country"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <fanta:select property="parameter(countryId)" listName="countryList" labelProperty="name"
                                  valueProperty="id"
                                  firstEmpty="true" styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                  styleId="country">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    </fanta:select>
                </div>
            </div>
            <div class="form-group col-sm-3">
                <label class="control-label col-xs-11 col-sm-5">
                    <fmt:message key="Contact.cityZip"/>
                </label>

                <div class="col-xs-11 col-sm-6">
                    <html:text property="parameter(zip)" styleClass="${app2:getFormInputClasses()}"
                               titleKey="Contact.zip"/>
                </div>
            </div>
            <div class="form-group col-sm-3">
                <div class="col-xs-11">
                    <html:text property="parameter(cityName)" styleClass="mediumText form-control"
                               titleKey="Contact.city" styleId="city"/>
                </div>
            </div>
            <div class="form-group col-sm-2">
                <div class="col-xs-12">
                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </div>
            </div>
        </div>
        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="City/List.do" parameterName="cityNameAlpha" onClick="jump(this);return false;"
                            mode="bootstrap"/>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="CITY" permission="CREATE">
                <html:submit property="parameter(new)" styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </app2:checkAccessRight>
        </div>
    </html:form>
    <div id="CityList.jsp" class="table-responsive">
        <fanta:table mode="bootstrap" list="cityList" width="100%" styleClass="${app2:getFantabulousTableClases()}" id="city"
                     action="City/List.do" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="City/Forward/Update.do?dto(cityId)=${city.id}&dto(cityName)=${app2:encode(city.name)}&dto(cityZip)=${app2:encode(city.zip)}"/>
            <c:set var="deleteAction"
                   value="City/Forward/Delete.do?dto(withReferences)=true&dto(cityId)=${city.id}&dto(cityName)=${app2:encode(city.name)}&dto(cityZip)=${app2:encode(city.zip)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="CITY" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="CITY" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="City.name"
                              headerStyle="listHeader" width="40%" orderable="true" maxLength="25">
            </fanta:dataColumn>
            <fanta:dataColumn name="zip" styleClass="listItem" title="City.zip" headerStyle="listHeader" width="20%"
                              orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="country" styleClass="listItem2" title="City.country" headerStyle="listHeader"
                              width="35%" orderable="true">
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>

