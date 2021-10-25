<%@ include file="/Includes.jsp" %>

<table width="90%" border="0" align="center" cellpadding="10" cellspacing="0">
  <tr>
    <td align="center"> <br>
	 <app2:checkAccessRight functionality="BANKACCOUNT" permission="CREATE">
      <table width="97%" border="0" cellpadding="2" cellspacing="0">
       <tr>
         <html:form styleId="CREATE_NEW_BANKACCOUNT" action="/BankAccount/Forward/Create">
          <td class="button"><!--Button create up -->
            <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
          </td>
        </html:form>
      </tr>
      </table>
     </app2:checkAccessRight>

       <fanta:table list="bankAccountList" width="97%" id="bankAccount" action="BankAccount/List.do" imgPath="${baselayout}" align="center">
        <c:set var="editLink" value="BankAccount/Forward/Update.do?dto(bankAccountId)=${bankAccount.bankAccountId}&dto(accountNumber)=${app2:encode(bankAccount.accountNumber)}&dto(addressId)=${bankAccount.addressId}"/>
        <fanta:columnGroup headerStyle="listHeader" width="5%" title="Common.action" >
            <app2:checkAccessRight functionality="BANKACCOUNT" permission="VIEW">
                <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem" headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="BANKACCOUNT" permission="DELETE">
                <fanta:actionColumn name="" title="Common.delete" action="BankAccount/Forward/Delete.do?dto(bankAccountId)=${bankAccount.bankAccountId}&dto(accountNumber)=${app2:encode(bankAccount.accountNumber)}&dto(addressId)=${bankAccount.addressId}&dto(withReferences)=true" styleClass="listItem" headerStyle="listHeader" width="50%" image="${baselayout}/img/delete.gif"/>
            </app2:checkAccessRight>
        </fanta:columnGroup>
        <fanta:dataColumn name="bankName" action="${editLink}" maxLength="40" styleClass="listItem" title="BankAccount.bankName"  headerStyle="listHeader" width="32%" orderable="true"/>
        <fanta:dataColumn name="bankCode" styleClass="listItem" title="BankAccount.bankCode"  headerStyle="listHeader" width="13%" orderable="true"/>
        <fanta:dataColumn name="accountNumber" styleClass="listItem" title="BankAccount.number"  headerStyle="listHeader" width="15%" orderable="true"/>
        <fanta:dataColumn name="accountOwner" styleClass="listItem" title="BankAccount.owner"  headerStyle="listHeader" width="25%" orderable="true"/>
        <fanta:dataColumn name="" styleClass="listItem2Center" title="BankAccount.default" headerStyle="listHeader" width="10%" renderData="false">
            <c:if test="${bankAccount.bankAccountId == bankAccount.bankAccountByAddressId}">
                <img align="middle" alt="" src="<c:out value="${baselayout}"/>/img/check.gif"/>
            </c:if>
        </fanta:dataColumn>
    </fanta:table>


      <app2:checkAccessRight functionality="BANKACCOUNT" permission="CREATE">
	  <table width="97%" border="0" cellpadding="2" cellspacing="0"> <!--Button create down -->
        <tr>
          <html:form styleId="CREATE_NEW_BANKACCOUNT" action="/BankAccount/Forward/Create">
          <td class="button"><!--Button create up -->
             <html:submit styleClass="button"><fmt:message   key="Common.new"/></html:submit>
          </td>
        </html:form>
        </tr>
      </table>
      </app2:checkAccessRight>
    </td>
  </tr>
</table>