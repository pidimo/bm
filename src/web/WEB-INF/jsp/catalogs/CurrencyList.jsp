<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/Currency/List.do" styleClass="form-horizontal">
        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="currencyName_id">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group col-xs-12">
                    <html:text property="parameter(currencyName)" styleId="currencyName_id"
                               styleClass="largeText form-control"/>
                 <span class="input-group-btn">
                    <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                            key="Common.go"/></html:submit>
                 </span>
                </div>
            </div>
        </div>
        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="Currency/List.do" parameterName="currencyNameAlpha" mode="bootstrap"/>

        </div>
    </html:form>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:checkAccessRight functionality="CURRENCY" permission="CREATE">
            <html:form styleId="CREATE_NEW_CURRENCY" action="/Currency/Forward/Create.do?op=create">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                        key="Common.new"/></html:submit>
            </html:form>
        </app2:checkAccessRight>
    </div>
    <div id="CurrencyList.jsp">
        <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
        <div class="table-responsive">
            <fanta:table mode="bootstrap" width="100%" list="currencyList" styleClass="${app2:getFantabulousTableClases()}" id="currency"
                         action="Currency/List.do" imgPath="${baselayout}">
                <c:set var="editAction"
                       value="Currency/Forward/Update.do?dto(currencyId)=${currency.id}&dto(currencyName)=${app2:encode(currency.name)}"/>
                <c:set var="deleteAction"
                       value="Currency/Forward/Delete.do?dto(withReferences)=true&dto(currencyId)=${currency.id}&dto(currencyName)=${app2:encode(currency.name)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="CURRENCY" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                            headerStyle="listHeader"
                                            glyphiconClass="${app2:getClassGlyphEdit()}"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="CURRENCY" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            glyphiconClass="${app2:getClassGlyphTrash()}"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>

                <fanta:dataColumn name="name" action="${editAction}" title="Currency.name"
                                  headerStyle="listHeader" width="25%" orderable="true" maxLength="25"/>
                <fanta:dataColumn name="label" title="Currency.label" headerStyle="listHeader"
                                  width="15%" orderable="true"/>
                <fanta:dataColumn name="symbol" title="Currency.symbol" headerStyle="listHeader"
                                  width="20%" orderable="true"/>
                <fanta:dataColumn name="unit" title="Currency.unit" headerStyle="listHeader"
                                  width="15%" orderable="true" style="text-align:right" renderData="false">
                    <c:set var="numberValue" value=""/>
                    <c:if test="${currency.unit != null}">
                        <fmt:formatNumber var="numberValue" value="${currency.unit}" type="number"
                                          pattern="${numberFormat}"/>
                    </c:if>
                    ${numberValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="" title="Currency.status" headerStyle="listHeader"
                                  width="25%" renderData="false">
                    <c:if test="${currency.basic == '1'}">
                        <img src='<c:url value="${sessionScope.layout}/img/check.gif"/>'/>
                    </c:if>
                </fanta:dataColumn>
            </fanta:table>
        </div>
    </div>
</div>

