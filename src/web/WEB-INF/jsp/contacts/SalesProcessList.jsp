<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <div class="searchContainer">
        <html:form action="/SalesProcess/List.do" focus="parameter(processName)" styleClass="form-horizontal">
            <div class="${app2:getSearchWrapperClasses()}">
                <label class="${app2:getFormLabelOneSearchInput()} label-left">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormOneSearchInput()}">
                    <div class="input-group">
                        <html:text property="parameter(processName)"
                                   styleClass="${app2:getFormInputClasses()} largeText" maxlength="80"/>
                        <div class="input-group-btn">
                            <html:submit styleClass="${app2:getFormButtonClasses()}">
                                <fmt:message key="Common.go"/>
                            </html:submit>
                        </div>
                    </div>
                </div>
            </div>
        </html:form>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="SalesProcess/List.do" parameterName="processNameAlpha" mode="bootstrap"/>
        </div>
    </div>

    <app2:checkAccessRight functionality="SALESPROCESS" permission="CREATE">
        <html:form action="/SalesProcess/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
    <fmt:message var="datePattern" key="datePattern"/>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="salesProcessList" width="100%" id="process" styleClass="${app2:getFantabulousTableClases()}"
                     action="SalesProcess/List.do"
                     imgPath="${baselayout}" align="center">

            <%-- edit url to redirect to sales module--%>
            <c:set var="editLink"
                   value="sales/SalesProcess/Forward/Update.do?processId=${process.processId}&dto(processId)=${process.processId}&dto(processName)=${app2:encode(process.processName)}&addressId=${process.addressId}&index=0"/>
            <c:set var="deleteLink"
                   value="SalesProcess/Forward/Delete.do?processId=${process.processId}&dto(withReferences)=true&dto(processId)=${process.processId}&dto(processName)=${app2:encode(process.processName)}&dto(isAction)=0"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update" styleClass="listItem" render="false"
                                        headerStyle="listHeader" width="50%">
                        <app:link action="${editLink}" addModuleName="false" titleKey="Common.update"
                                  contextRelative="true">
                            <span class="${app2:getClassGlyphEdit()}"></span>
                        </app:link>
                    </fanta:actionColumn>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="SALESPROCESS" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete" action="${deleteLink}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="processName" styleClass="listItem" renderData="false"
                              title="SalesProcess.name" headerStyle="listHeader" width="20%" orderable="true"
                              maxLength="40">
                <app:link action="${editLink}" addModuleName="false" contextRelative="true">
                    <c:out value="${process.processName}"/>
                </app:link>
            </fanta:dataColumn>
            <fanta:dataColumn name="employeeName" styleClass="listItem" title="SalesProcess.employee"
                              headerStyle="listHeader" width="20%" orderable="true"/>
            <fanta:dataColumn name="priorityName" styleClass="listItem" title="SalesProcess.priority"
                              headerStyle="listHeader" width="10%" orderable="true"/>
            <fanta:dataColumn name="probability" styleClass="listItemRight" title="SalesProcess.probability"
                              headerStyle="listHeader" width="5%" orderable="true" renderData="false">
                ${process.probability}&nbsp;<fmt:message key="Common.probabilitySymbol"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="startDate" styleClass="listItem" title="SalesProcess.startDate"
                              headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                <fmt:formatDate var="dateValue" value="${app2:intToDate(process.startDate)}"
                                pattern="${datePattern}"/>${dateValue}&nbsp;
            </fanta:dataColumn>
            <fanta:dataColumn name="endDate" styleClass="listItem" title="SalesProcess.endDate"
                              headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                <fmt:formatDate var="dateValue" value="${app2:intToDate(process.endDate)}"
                                pattern="${datePattern}"/>
                ${dateValue}
            </fanta:dataColumn>
            <fanta:dataColumn name="value" styleClass="listItemRight" title="SalesProcess.value"
                              headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                <fmt:formatNumber var="numberValue" value="${process.value}" type="number"
                                  pattern="${numberFormat}"/>
                ${numberValue}
            </fanta:dataColumn>
            <fanta:dataColumn name="statusName" styleClass="listItem2" title="SalesProcess.status"
                              headerStyle="listHeader" width="10%" orderable="true"/>
        </fanta:table>
    </div>
    <%--NEW button--%>
    <app2:checkAccessRight functionality="SALESPROCESS" permission="CREATE">
        <html:form action="/SalesProcess/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>

