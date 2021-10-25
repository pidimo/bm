<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="${create}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="create" functionality="SUPPORTUSER"
                                 styleClass="${app2:getFormButtonClasses()}">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="supportUserList" styleClass="${app2:getFantabulousTableClases()}" width="100%"
                     id="supportUser"
                     action="${action}"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="${edit}?dto(productName)=${app2:encode(supportUser.productName)}&dto(productId)=${supportUser.productId}&dto(userId)=${supportUser.userId}"/>
            <c:set var="deleteAction"
                   value="${delete}?dto(withReferences)=true&dto(productName)=${app2:encode(supportUser.productName)}&dto(productId)=${supportUser.productId}&dto(userId)=${supportUser.userId}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <fanta:actionColumn name="up" action="${editAction}" title="Common.update" styleClass="listItem"
                                    headerStyle="listHeader" width="50%"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
                <fanta:actionColumn name="delete" action="${deleteAction}" title="Common.delete"
                                    styleClass="listItem" headerStyle="listHeader" width="5%"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
            </fanta:columnGroup>
            <fanta:dataColumn name="productName" action="${editAction}" styleClass="listItem"
                              title="SupportUser.supportProduct" headerStyle="listHeader" width="25%"
                              orderable="true" maxLength="30"/>
            <fanta:dataColumn name="userName" styleClass="listItem" title="SupportUser.supportUser"
                              headerStyle="listHeader" width="30%" orderable="true"/>
            <fanta:dataColumn name="emailSupportCase" styleClass="listItem2" title="Admin.CaseEmail"
                              headerStyle="listHeader" width="40%" orderable="true"/>
        </fanta:table>
    </div>
</div>