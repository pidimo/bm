<%@ include file="/Includes.jsp" %>

<fmt:message var="datePattern" key="datePattern"/>

<table width="99%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
    <tr>
        <td class="title" colspan="2">
            <fmt:message key="Sale.singleSearch"/>
        </td>
    </tr>
    <TR>
        <html:form action="/Sale/SingleList.do"
                   focus="parameter(title)">
            <td class="label"><fmt:message key="Common.search"/></td>
            <td align="left" class="contain">
                <html:text
                        property="parameter(title)"
                        styleClass="largeText" maxlength="80"/>
                &nbsp;
                <html:submit styleClass="button">
                    <fmt:message key="Common.go"/>
                </html:submit>
                &nbsp;&nbsp;
                <app:link action="/Sale/AdvancedList.do?advancedListForward=SaleAdvancedSearch">
                    <fmt:message key="Common.advancedSearch"/>
                </app:link>
            </td>
        </html:form>
    </TR>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="/Sale/SingleList.do" parameterName="alphabetTitle"/>
        </td>
    </tr>
</table>
<br/>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                <tr>
                    <app2:checkAccessRight functionality="SALE" permission="CREATE">
                        <html:form action="/Sale/Forward/Create.do">
                            <td class="button" width="100%">
                                <html:submit styleClass="button">
                                    <fmt:message key="Common.new"/>
                                </html:submit>
                            </td>
                        </html:form>
                    </app2:checkAccessRight>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <fanta:table list="saleSingleList"
                         width="100%"
                         id="sale"
                         action="Sale/SingleList.do"
                         imgPath="${baselayout}"
                         align="center">

                <c:set var="editLink"
                       value="Sale/Forward/Update.do?saleId=${sale.saleId}&dto(saleId)=${sale.saleId}&dto(title)=${app2:encode(sale.title)}&index=0"/>
                <c:set var="deleteLink"
                       value="Sale/Forward/Delete.do?saleId=${sale.saleId}&dto(saleId)=${sale.saleId}&dto(title)=${app2:encode(sale.title)}&dto(withReferences)=true&index=0"/>

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
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                <tr>
                    <app2:checkAccessRight functionality="SALE" permission="CREATE">
                        <html:form action="/Sale/Forward/Create.do">
                            <td class="button" width="100%">
                                <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
                            </td>
                        </html:form>
                    </app2:checkAccessRight>
                </tr>
            </table>
        </td>
    </tr>
</table>