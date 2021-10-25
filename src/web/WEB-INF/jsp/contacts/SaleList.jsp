<%@ include file="/Includes.jsp" %>

<fmt:message var="datePattern" key="datePattern"/>

<div class="${app2:getListWrapperClasses()}">
    <div class="searchContainer">
        <html:form action="/Sale/SingleList.do"
                   focus="parameter(title)" styleClass="form-horizontal">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Sale.singleSearch"/>
                </legend>
            </fieldset>
            <div class="${app2:getSearchWrapperClasses()}">
                <label class="${app2:getFormLabelOneSearchInput()} label-left">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormOneSearchInput()}">
                    <div class="input-group">
                        <html:text property="parameter(title)"
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
            <fanta:alphabet action="/Sale/SingleList.do" parameterName="alphabetTitle" mode="bootstrap"/>
        </div>
    </div>

    <app2:checkAccessRight functionality="SALE" permission="CREATE">
        <html:form action="/Sale/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="saleSingleList"
                     width="100%"
                     id="sale"
                     styleClass="${app2:getFantabulousTableClases()}"
                     action="Sale/SingleList.do"
                     imgPath="${baselayout}"
                     align="center">

            <c:set var="editLink"
                   value="Sale/Forward/Update.do?saleId=${sale.saleId}&dto(saleId)=${sale.saleId}&dto(title)=${app2:encode(sale.title)}"/>
            <c:set var="deleteLink"
                   value="Sale/Forward/Delete.do?saleId=${sale.saleId}&dto(saleId)=${sale.saleId}&dto(title)=${app2:encode(sale.title)}&dto(withReferences)=true"/>

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
                              headerStyle="listHeader" width="25%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="Sale.contactPerson"
                              headerStyle="listHeader" width="25%" orderable="true" renderData="false">
                <fanta:textShorter title="${sale.contactPersonName}">
                    <app:link action="${editContactPersonUrl}" contextRelative="true" addModuleName="false">
                        <c:out value="${sale.contactPersonName}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="sellerName" styleClass="listItem" title="Sale.seller"
                              headerStyle="listHeader" width="25%" orderable="true"/>
            <fanta:dataColumn name="saleDate" styleClass="listItem2" title="Sale.saleDate"
                              headerStyle="listHeader" width="20%" orderable="true" renderData="false">
                <fmt:formatDate var="saleDateValue" value="${app2:intToDate(sale.saleDate)}"
                                pattern="${datePattern}"/>
                ${saleDateValue}
            </fanta:dataColumn>
        </fanta:table>
    </div>
    <%--NEW button--%>
    <app2:checkAccessRight functionality="SALE" permission="CREATE">
        <html:form action="/Sale/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>