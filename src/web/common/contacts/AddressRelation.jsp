<%@include file="/Includes.jsp" %>

<tags:initSelectPopup/>

<html:form action="${action}" focus="dto(relatedAddressName)">

    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(addressId)" value="${param.contactId}"/>

    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(relationId)"/>
    </c:if>
    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>
    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>

    <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
        <tr>
            <td colspan="2" class="button">
                <app2:securitySubmit operation="${op}" functionality="ADDITIONALADDRESS"
                                     tabindex="20" styleClass="button">
                    ${button}
                </app2:securitySubmit>

                <html:cancel styleClass="button" tabindex="21">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="title">
                <c:out value="${title}"/>
            </td>
        </tr>
        <tr>
            <td class="label" width="25%">
                <fmt:message key="AddressRelation.relatedAddress"/>
            </td>
            <td class="contain" width="75%">
                <html:hidden property="dto(relatedAddressId)" styleId="fieldAddressId_id"/>

                <app:text property="dto(relatedAddressName)" styleClass="mediumText" maxlength="40"
                          readonly="true"
                          tabindex="2" view="${op =='delete' || op == 'update'}"
                          styleId="fieldAddressName_id"/>

                <tags:selectPopup url="/contacts/SearchAddress.do?allowCreation=2" name="searchAddress"
                                  titleKey="Common.search"
                                  hide="${op != 'create'}" tabindex="3"/>
                <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                       titleKey="Common.clear" hide="${op != 'create'}" tabindex="4"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="AddressRelation.relationType"/>
            </td>
            <td class="contain">
                <fanta:select property="dto(relationTypeId)"
                              listName="addressRelationTypeList"
                              labelProperty="title"
                              valueProperty="relationTypeId"
                              styleClass="middleSelect"
                              firstEmpty="true"
                              module="/catalogs"
                              tabIndex="5"
                              readOnly="${'delete' == op}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
        </tr>
        <tr>
            <td class="topLabel" colspan="2">
                <fmt:message key="AddressRelation.comment"/>
                <html:textarea property="dto(comment)"
                               styleClass="mediumDetailHigh"
                               style="height:120px;width:99%;"
                               tabindex="6"
                               readonly="${'delete' == op}"/>
            </td>
        </tr>

        <tr>
            <td colspan="2" class="button">
                <app2:securitySubmit operation="${op}" functionality="ADDITIONALADDRESS"
                                     tabindex="9" styleClass="button">
                    ${button}
                </app2:securitySubmit>

                <html:cancel styleClass="button" tabindex="10">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>
</html:form>