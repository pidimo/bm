<%@ include file="/Includes.jsp" %>

<html:form action="/contacts/ContactPerson/List" method="post">

    <div class="label">
        <fmt:message key="Common.search"/>:
    </div>
    <div class="content">
        <html:text property="parameter(lastName@_firstName@_searchName)" tabindex="1"/>
        <html:hidden property="parameter(active)" value="1"/>
    </div>
    <div class="buttons">
        <html:submit styleClass="button" tabindex="2"><fmt:message key="Common.go"/></html:submit>
    </div>

    <div class="content">

        <fanta:table action="/contacts/ContactPerson/List.do" imgPath="${baselayout}" enableRowLighting="false"
                     styleClass="list" id="contactPerson"
                     textShortening="false" showPagination="true" addDefaultWidth="false"
                     addDefaultAlign="false" renderSimplePaginator="true" rowEvenStyleClass="rowEven"
                     rowOddStyleClass="rowOdd">
            <c:set var="viewLink"
                   value="contacts/ContactPerson/Forward/View.do?dto(addressId)=${contactPerson.addressId}&dto(contactPersonId)=${contactPerson.contactPersonId}&dto(name1)=${app2:encode(contactPerson.lastName)}&dto(name2)=${app2:encode(contactPerson.firstName)}"/>
            <fanta:dataColumn name="contactPersonName" action="${viewLink}" styleClass="listItem"
                              title="ContactPerson.name" headerStyle="listHeader" width="100%" orderable="false">
            </fanta:dataColumn>

        </fanta:table>

    </div>

</html:form>


