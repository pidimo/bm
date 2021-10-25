<%@ page import="com.piramide.elwis.utils.ProductConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="PRODUCTTYPETYPE_EVENT" value="<%=ProductConstants.ProductTypeType.EVENT.getConstant()%>"/>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
    <td>
    
    <app2:checkAccessRight functionality="PRODUCTTYPE" permission="CREATE">
        <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
        <TR>
        <html:form  action="/ProductType/Forward/Create.do">
            <TD class="button">
                <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
            </TD>
        </html:form>
        </TR>
        </table>
    </app2:checkAccessRight>
    <TABLE id="ProductTypeList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <TR align="center" height="20">
    <td>
    <fanta:table list="productTypeList" width="100%" id="productType" action="ProductType/List.do" imgPath="${baselayout}"  >
        <c:set var="editAction" value="ProductType/Forward/Update.do?dto(typeId)=${productType.id}&dto(typeName)=${app2:encode(productType.name)}"/>
        <c:set var="deleteAction" value="ProductType/Forward/Delete.do?dto(withReferences)=true&dto(typeId)=${productType.id}&dto(typeName)=${app2:encode(productType.name)}"/>

        <c:set var="isEventType" value="${productType.productTypeType eq PRODUCTTYPETYPE_EVENT}"/>

        <c:choose>
            <c:when test="${isEventType}">
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="50%">
                    </fanta:actionColumn>
                    <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="50%">
                    </fanta:actionColumn>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" styleClass="listItem2" title="ProductType.typeName"  headerStyle="listHeader" width="95%" orderable="true" maxLength="25" renderData="false">
                    <fmt:message key="ProductType.item.event"/>
                </fanta:dataColumn>

            </c:when>
            <c:otherwise>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="PRODUCTTYPE" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="PRODUCTTYPE" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem2" title="ProductType.typeName"  headerStyle="listHeader" width="95%" orderable="true" maxLength="25" />
            </c:otherwise>
        </c:choose>

    </fanta:table>
    </td>
    </tr>
    </table>
    </td>
</tr>
</table>