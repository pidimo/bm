<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<div class="${app2:getListWrapperClasses()}">

    <html:form action="/SalePosition/List.do"
               focus="parameter(productName)"
               styleClass="form-horizontal">

        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(productName)"
                               styleClass="${app2:getFormInputClasses()} largeText"/>
                    <span class="input-group-btn">
                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                    </span>
                </div>
            </div>
        </div>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="/SalePosition/List.do"
                            parameterName="productNameAlpha"
                            mode="bootstrap"/>
        </div>
    </html:form>

    <app2:checkAccessRight functionality="SALEPOSITION" permission="CREATE">
        <html:form action="/SalePosition/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <c:set value="SalePosition/List.do" var="listUrl" scope="request"/>
        <c:set var="useJavaScript" value="false" scope="request"/>
        <c:import url="/WEB-INF/jsp/contacts/SalePositionFragmentList.jsp"/>
    </div>

    <app2:checkAccessRight functionality="SALEPOSITION" permission="CREATE">
        <html:form action="/SalePosition/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

</div>