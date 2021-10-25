<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <fmt:message var="datePattern" key="datePattern"/>

    <html:form action="/SalesProcess/Sale/List.do" focus="parameter(title)" styleClass="form-horizontal">
        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left">
                <fmt:message key="Common.search"/>
            </label>
            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(title)" styleClass="largeText ${app2:getFormInputClasses()}" maxlength="80"/>
                <span class="input-group-btn">
                    <html:submit styleClass="button  ${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </span>
                </div>
            </div>
        </div>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet
                mode="bootstrap"
                action="/SalesProcess/Sale/List.do"
                parameterName="alphabetTitle"/>
    </div>
    <app2:checkAccessRight functionality="SALE" permission="CREATE">
        <html:form action="/SalesProcess/Sale/Forward/Create.do?dto(processId)=${param.processId}">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="saleBySaleProcessList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%"
                     id="sale"
                     action="SalesProcess/Sale/List.do"
                     imgPath="${baselayout}"
                     align="center">

            <c:set var="editLink"
                   value="SalesProcess/Sale/Forward/Update.do?saleId=${sale.saleId}&dto(saleId)=${sale.saleId}&dto(title)=${app2:encode(sale.title)}&tabKey=SalesProcess.tab.sale"/>
            <c:set var="deleteLink"
                   value="SalesProcess/Sale/Forward/Delete.do?saleId=${sale.saleId}&dto(saleId)=${sale.saleId}&dto(title)=${app2:encode(sale.title)}&dto(withReferences)=true&tabKey=SalesProcess.tab.sale"/>

            <%--customer link--%>
            <tags:addressEditContextRelativeUrl varName="customerEditLink" addressId="${sale.customerAddressId}" addressType="${sale.customerAddressType}" name1="${sale.customerName1}" name2="${sale.customerName2}" name3="${sale.customerName3}"/>

            <%--contact person link--%>
            <c:if test="${not empty sale.contactPersonId}">
                <c:set var="editContactPersonUrl"
                       value="/contacts/ContactPerson/Forward/Update.do?contactId=${sale.customerAddressId}&dto(addressId)=${sale.customerAddressId}&dto(contactPersonId)=${sale.contactPersonId}&tabKey=Contacts.Tab.contactPersons"/>
            </c:if>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="SALE" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="SALE" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="title" action="${editLink}" styleClass="listItem" title="Sale.title"
                              headerStyle="listHeader" width="15%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="customerName" styleClass="listItem" title="Sale.customer"
                              headerStyle="listHeader" width="16%" orderable="true" renderData="false">
                <fanta:textShorter title="${sale.customerName}">
                    <app:link action="${customerEditLink}" contextRelative="true" addModuleName="false">
                        <c:out value="${sale.customerName}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="Sale.contactPerson"
                              headerStyle="listHeader" width="16%" orderable="true" renderData="false">
                <fanta:textShorter title="${sale.contactPersonName}">
                    <app:link action="${editContactPersonUrl}" contextRelative="true" addModuleName="false">
                        <c:out value="${sale.contactPersonName}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="sellerName" styleClass="listItem" title="Sale.seller"
                              headerStyle="listHeader" width="16%" orderable="true"/>
            <fanta:dataColumn name="note" styleClass="listItem" title="Sale.salesProcessAction"
                              headerStyle="listHeader" width="16%" orderable="true"/>
            <fanta:dataColumn name="saleDate" styleClass="listItem2" title="Sale.saleDate"
                              headerStyle="listHeader" width="16%" orderable="true" renderData="false">
                <fmt:formatDate var="saleDateValue" value="${app2:intToDate(sale.saleDate)}"
                                pattern="${datePattern}"/>
                ${saleDateValue}
            </fanta:dataColumn>
        </fanta:table>
    </div>

    <app2:checkAccessRight functionality="SALE" permission="CREATE">
        <html:form action="/SalesProcess/Sale/Forward/Create.do?dto(processId)=${param.processId}">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>
