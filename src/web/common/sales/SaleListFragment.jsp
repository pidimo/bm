<%@ include file="/Includes.jsp" %>

<fmt:message var="datePattern" key="datePattern"/>

<%--
listName   : Fantabulous list name
listAction : List action to execute list
editUrl    : Edit action url
deleteUrl  : Delete action url 
tabKeyName : tab index or tabKey value
--%>

<c:if test="${empty listName}">
    <c:set var="listName" value="saleSingleList" scope="request"/>
</c:if>

<c:if test="${empty listAction}">
    <c:set var="listAction" value="Sale/SingleList.do" scope="request"/>
</c:if>

<c:if test="${empty editUrl}">
    <c:set var="editUrl" value="Sale/Forward/Update.do" scope="request"/>
</c:if>

<c:if test="${empty deleteUrl}">
    <c:set var="deleteUrl" value="Sale/Forward/Delete.do" scope="request"/>
</c:if>

<c:if test="${empty tabKeyName}">
    <c:set var="tabKeyName" value="index=0" scope="request"/>
</c:if>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td>
            <fanta:table list="${listName}"
                         width="100%"
                         id="sale"
                         action="${listAction}"
                         imgPath="${baselayout}"
                         align="center">

                <c:set var="editLink"
                       value="${editUrl}?saleId=${sale.saleId}&dto(saleId)=${sale.saleId}&dto(title)=${app2:encode(sale.title)}&${tabKeyName}"/>
                <c:set var="deleteLink"
                       value="${deleteUrl}?saleId=${sale.saleId}&dto(saleId)=${sale.saleId}&dto(title)=${app2:encode(sale.title)}&dto(withReferences)=true&${tabKeyName}"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="SALE" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update"
                                            action="${editLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="SALE" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete"
                                            action="${deleteLink}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="title" action="${editLink}" styleClass="listItem" title="Sale.title"
                                  headerStyle="listHeader" width="15%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="customerName" styleClass="listItem" title="Sale.customer"
                                  headerStyle="listHeader" width="20%" orderable="true"/>
                <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="Sale.contactPerson"
                                  headerStyle="listHeader" width="20%" orderable="true"/>
                <fanta:dataColumn name="sellerName" styleClass="listItem" title="Sale.seller"
                                  headerStyle="listHeader" width="20%" orderable="true"/>
                <fanta:dataColumn name="saleDate" styleClass="listItem2" title="Sale.saleDate"
                                  headerStyle="listHeader" width="20%" orderable="true" renderData="false">
                    <fmt:formatDate var="saleDateValue" value="${app2:intToDate(sale.saleDate)}"
                                    pattern="${datePattern}"/>
                    ${saleDateValue}
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
</table>