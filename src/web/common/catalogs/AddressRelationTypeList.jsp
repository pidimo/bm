<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="TYPE_INVOICE_ADDRESS" value="<%= CatalogConstants.AddressRelationTypeType.INVOICE_ADDRESS.getConstantAsString()%>"/>
<c:set var="TYPE_HIERACHY" value="<%= CatalogConstants.AddressRelationTypeType.HIERACHY.getConstantAsString()%>"/>
<c:set var="TYPE_OTHERS" value="<%= CatalogConstants.AddressRelationTypeType.OTHERS.getConstantAsString()%>"/>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
    <td>
    <app2:checkAccessRight functionality="ADDRESSRELATIONTYPE" permission="CREATE">
        <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
        <TR>
        <html:form action="/AddressRelationType/Forward/Create.do">
            <TD class="button">
                <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
            </TD>
        </html:form>
        </TR>
        </table>
    </app2:checkAccessRight>
    <TABLE border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <TR>
        <td>
            <fanta:table list="addressRelationTypeList" width="100%" id="addressRelationType"
                         action="AddressRelationType/List.do" imgPath="${baselayout}">
                <c:set var="editAction"
                       value="AddressRelationType/Forward/Update.do?dto(relationTypeId)=${addressRelationType.relationTypeId}&dto(title)=${app2:encode(addressRelationType.title)}"/>
                <c:set var="deleteAction"
                       value="AddressRelationType/Forward/Delete.do?dto(withReferences)=true&dto(relationTypeId)=${addressRelationType.relationTypeId}&dto(title)=${app2:encode(addressRelationType.title)}&dto(op)=read"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="ADDRESSRELATIONTYPE" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update" action="${editAction}" styleClass="listItem"
                                            headerStyle="listHeader" image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="ADDRESSRELATIONTYPE" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete" action="${deleteAction}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="title" action="${editAction}" styleClass="listItem" title="AddressRelationType.title"
                                  headerStyle="listHeader" width="65%" orderable="true" maxLength="25">
                </fanta:dataColumn>
                <fanta:dataColumn name="relationType" styleClass="listItem2" title="AddressRelationType.relationType"
                                  headerStyle="listHeader"
                                  width="30%" renderData="false">
                    <c:choose>
                        <c:when test="${addressRelationType.relationType eq TYPE_INVOICE_ADDRESS}">
                            <fmt:message key="<%= CatalogConstants.AddressRelationTypeType.INVOICE_ADDRESS.getResource()%>"/>
                        </c:when>
                        <c:when test="${addressRelationType.relationType eq TYPE_HIERACHY}">
                            <fmt:message key="<%= CatalogConstants.AddressRelationTypeType.HIERACHY.getResource()%>"/>
                        </c:when>
                        <c:when test="${addressRelationType.relationType eq TYPE_OTHERS}">
                            <fmt:message key="<%= CatalogConstants.AddressRelationTypeType.OTHERS.getResource()%>"/>
                        </c:when>
                    </c:choose>
                </fanta:dataColumn>

            </fanta:table>
        </td>
    </tr>
    </TABLE>
    </td>
</tr>
</table>