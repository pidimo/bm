<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="personType" value="<%=ContactConstants.ADDRESSTYPE_PERSON%>"/>

<div class="${app2:getListWrapperClasses()}">

    <legend class="title">
        <fmt:message key="SalesProcess.Title.simpleSearch"/>
    </legend>

    <div class="searchContainer">
        <html:form action="/SalesProcess/List.do"
                   focus="parameter(processName)"
                   styleClass="form-horizontal">

            <div class="${app2:getSearchWrapperClasses()}">
                <label class="${app2:getFormLabelOneSearchInput()} label-left" for="processName_id">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormOneSearchInput()}">
                    <div class="input-group">
                        <html:text property="parameter(processName)"
                                   styleId="processName_id"
                                   styleClass="${app2:getFormInputClasses()} largeText"/>

                        <div class="input-group-btn">
                            <html:submit styleClass="${app2:getFormButtonClasses()}">
                                <fmt:message key="Common.go"/>
                            </html:submit>
                        </div>
                    </div>
                </div>
                <div class="pull-left">
                    <app:link action="/AdvancedSearch.do?advancedListForward=SalesProcessAdvancedSearch"
                              styleClass="btn btn-link">
                        <fmt:message key="Common.advancedSearch"/>
                    </app:link>
                </div>
            </div>

        </html:form>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="SalesProcess/List.do" parameterName="processNameAlpha" mode="bootstrap"/>
        </div>

    </div>

    <app2:checkAccessRight functionality="SALESPROCESS" permission="CREATE">
        <c:set var="newButtonsTable" scope="page">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <tags:buttonsTable>
                    <app:url value="/SalesProcess/Forward/Create.do"
                             addModuleParams="false" var="newSalesProcessUrl"/>
                    <input type="button"
                           class="${app2:getFormButtonClasses()}"
                           value='<fmt:message key="Common.new"/>'
                           onclick="window.location ='${newSalesProcessUrl}'">
                </tags:buttonsTable>
            </div>
        </c:set>
    </app2:checkAccessRight>

    <c:out value="${newButtonsTable}" escapeXml="false"/>

    <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
    <fmt:message var="datePattern" key="datePattern"/>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="salesProcessList"
                     width="100%" id="process"
                     action="SalesProcess/List.do"
                     imgPath="${baselayout}"
                     align="center"
                     styleClass="${app2:getFantabulousTableLargeClases()}">
            <c:set var="editLink"
                   value="SalesProcess/Forward/Update.do?processId=${process.processId}&dto(processId)=${process.processId}&dto(processName)=${app2:encode(process.processName)}&addressId=${process.addressId}&index=0"/>
            <c:set var="deleteLink"
                   value="SalesProcess/Forward/Delete.do?processId=${process.processId}&dto(withReferences)=true&dto(processId)=${process.processId}&dto(processName)=${app2:encode(process.processName)}&addressId=${process.addressId}&dto(isAction)=0&index=0"/>

            <%--address link--%>
            <c:choose>
                <c:when test="${personType == process.addressType}">
                    <c:set var="addressEditLink"
                           value="/contacts/Person/Forward/Update.do?contactId=${process.addressId}&dto(addressId)=${process.addressId}&dto(addressType)=${process.addressType}&dto(name1)=${app2:encode(process.addressName1)}&dto(name2)=${app2:encode(process.addressName2)}&dto(name3)=${app2:encode(process.addressName3)}&index=0"/>
                </c:when>
                <c:otherwise>
                    <c:set var="addressEditLink"
                           value="/contacts/Organization/Forward/Update.do?contactId=${process.addressId}&dto(addressId)=${process.addressId}&dto(addressType)=${process.addressType}&dto(name1)=${app2:encode(process.addressName1)}&dto(name2)=${app2:encode(process.addressName2)}&dto(name3)=${app2:encode(process.addressName3)}&index=0"/>
                </c:otherwise>
            </c:choose>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update" action="${editLink}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="SALESPROCESS" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete" action="${deleteLink}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="processName" action="${editLink}" styleClass="listItem"
                              title="SalesProcess.name" headerStyle="listHeader" width="20%"
                              orderable="true" maxLength="40"/>
            <fanta:dataColumn name="addressName" styleClass="listItem" title="SalesProcess.contact"
                              headerStyle="listHeader" width="15%" orderable="true" maxLength="40"
                              renderData="false">
                <fanta:textShorter title="${process.addressName}">
                    <app:link action="${addressEditLink}" contextRelative="true" addModuleName="false">
                        <c:out value="${process.addressName}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="employeeName" styleClass="listItem" title="SalesProcess.employee"
                              headerStyle="listHeader" width="15%" orderable="true"/>
            <fanta:dataColumn name="priorityName" styleClass="listItem" title="SalesProcess.priority"
                              headerStyle="listHeader" width="8%" orderable="true"/>
            <fanta:dataColumn name="probability" styleClass="listItemRight"
                              title="SalesProcess.probability" headerStyle="listHeader" width="8%"
                              orderable="true" renderData="false" nowrap="nowrap">
                ${process.probability}&nbsp;<fmt:message key="Common.probabilitySymbol"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="startDate" styleClass="listItem" title="SalesProcess.startDate"
                              headerStyle="listHeader" width="7%" orderable="true" renderData="false"
                              nowrap="nowrap">
                <fmt:formatDate var="dateValue" value="${app2:intToDate(process.startDate)}"
                                pattern="${datePattern}"/>${dateValue}
            </fanta:dataColumn>
            <fanta:dataColumn name="endDate" styleClass="listItem" title="SalesProcess.endDate"
                              headerStyle="listHeader" width="7%" orderable="true" renderData="false"
                              nowrap="nowrap">
                <fmt:formatDate value="${app2:intToDate(process.endDate)}" pattern="${datePattern}"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="value" styleClass="listItemRight" title="SalesProcess.value"
                              headerStyle="listHeader" width="8%" orderable="true" renderData="false">
                <fmt:formatNumber value="${process.value}" type="number" pattern="${numberFormat}"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="statusName" styleClass="listItem2" title="SalesProcess.status"
                              headerStyle="listHeader" width="7%" orderable="true"/>
        </fanta:table>
    </div>

    <c:out value="${newButtonsTable}" escapeXml="false"/>
</div>