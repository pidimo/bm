<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="BANKACCOUNT" permission="CREATE">
        <html:form styleId="CREATE_NEW_BANKACCOUNT" action="/BankAccount/Forward/Create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="bankAccountList" styleClass="${app2:getFantabulousTableClases()}" width="100%"
                     id="bankAccount" action="BankAccount/List.do"
                     imgPath="${baselayout}"
                     align="center">
            <c:set var="editLink"
                   value="BankAccount/Forward/Update.do?dto(bankAccountId)=${bankAccount.bankAccountId}&dto(accountNumber)=${app2:encode(bankAccount.accountNumber)}&dto(addressId)=${bankAccount.addressId}"/>
            <fanta:columnGroup headerStyle="listHeader" width="5%" title="Common.action">
                <app2:checkAccessRight functionality="BANKACCOUNT" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem"
                                        headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="BANKACCOUNT" permission="DELETE">
                    <fanta:actionColumn name="" title="Common.delete"
                                        action="BankAccount/Forward/Delete.do?dto(bankAccountId)=${bankAccount.bankAccountId}&dto(accountNumber)=${app2:encode(bankAccount.accountNumber)}&dto(addressId)=${bankAccount.addressId}&dto(withReferences)=true"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="bankName" action="${editLink}" maxLength="40" styleClass="listItem"
                              title="BankAccount.bankName" headerStyle="listHeader" width="32%" orderable="true"/>
            <fanta:dataColumn name="bankCode" styleClass="listItem" title="BankAccount.bankCode"
                              headerStyle="listHeader"
                              width="13%" orderable="true"/>
            <fanta:dataColumn name="accountNumber" styleClass="listItem" title="BankAccount.number"
                              headerStyle="listHeader"
                              width="15%" orderable="true"/>
            <fanta:dataColumn name="accountOwner" styleClass="listItem" title="BankAccount.owner"
                              headerStyle="listHeader"
                              width="25%" orderable="true"/>
            <fanta:dataColumn name="" styleClass="listItem2Center" title="BankAccount.default" headerStyle="listHeader"
                              width="10%" renderData="false">
                <c:if test="${bankAccount.bankAccountId == bankAccount.bankAccountByAddressId}">
                    <span class="${app2:getClassGlyphOk()}"></span>
                </c:if>
            </fanta:dataColumn>
        </fanta:table>
    </div>

    <app2:checkAccessRight functionality="BANKACCOUNT" permission="CREATE">
        <html:form styleId="CREATE_NEW_BANKACCOUNT" action="/BankAccount/Forward/Create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>
