<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">

    <html:form action="/SalesProcess/SearchSalesProcess.do?contact=${param.contact}" focus="parameter(processName)" styleClass="form-horizontal">
        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="processName_id">
                <fmt:message key="Common.search"/>
            </label>
            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(processName)"
                               styleId="processName_id"
                               styleClass="largeText ${app2:getFormInputClasses()}"/>
                    <html:hidden property="parameter(addressId)" value="${param['parameter(addressId)']}"/>
                <span class="input-group-btn">
                    <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message key="Common.go"/></html:submit>
                </span>
                </div>
            </div>
        </div>
    </html:form>
    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet
                mode="bootstrap"
                action="SalesProcess/SearchSalesProcess.do?parameter(addressId)=${param['parameter(addressId)']}&contact=${param.contact}"
                parameterName="processNameAlpha"/>
    </div>

    <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
    <fmt:message var="datePattern" key="datePattern"/>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="salesProcessSearchList"
                     width="100%" id="process"
                     styleClass="${app2:getFantabulousTableClases()}"
                     action="SalesProcess/SearchSalesProcess.do?contact=${param.contact}"
                     imgPath="${baselayout}"
                     align="center">

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <fanta:actionColumn name="" title="Common.select" useJScript="true"
                                    action="javascript:parent.selectMultipleField('${process.processId}', '${app2:jscriptEncode(process.processName)}', '${process.probability}', '${process.addressId}', '${app2:jscriptEncode(process.addressName)}');"
                                    styleClass="listItem" headerStyle="listHeader" width="50%"
                                    glyphiconClass="${app2:getClassGlyphImport()}"/>
            </fanta:columnGroup>

            <fanta:dataColumn name="processName" styleClass="listItem"
                              title="SalesProcess.name" headerStyle="listHeader" width="15%" orderable="true"
                              maxLength="40"/>
            <c:if test="${!param.contact}">

                <fanta:dataColumn name="addressName" styleClass="listItem" title="SalesProcess.contact"
                                  headerStyle="listHeader" width="15%" orderable="true" maxLength="40"/>
            </c:if>
            <fanta:dataColumn name="employeeName" styleClass="listItem" title="SalesProcess.employee"
                              headerStyle="listHeader" width="15%" orderable="true"/>
            <fanta:dataColumn name="priorityName" styleClass="listItem" title="SalesProcess.priority"
                              headerStyle="listHeader" width="6%" orderable="true"/>
            <fanta:dataColumn name="probability" styleClass="listItemRight" title="SalesProcess.probability"
                              headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                ${process.probability}&nbsp;<fmt:message key="Common.probabilitySymbol"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="startDate" styleClass="listItem" title="SalesProcess.startDate"
                              headerStyle="listHeader" width="8%" orderable="true" renderData="false" nowrap="nowrap">
                <fmt:formatDate value="${app2:intToDate(process.startDate)}" pattern="${datePattern}"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="endDate" styleClass="listItem" title="SalesProcess.endDate"
                              headerStyle="listHeader" width="8%" orderable="true" renderData="false" nowrap="nowrap">
                <fmt:formatDate value="${app2:intToDate(process.endDate)}" pattern="${datePattern}"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="value" styleClass="listItemRight" title="SalesProcess.value"
                              headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                <fmt:formatNumber value="${process.value}" type="number" pattern="${numberFormat}"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="statusName" styleClass="listItem2" title="SalesProcess.status"
                              headerStyle="listHeader" width="7%" orderable="true"/>
        </fanta:table>
    </div>

</div>
