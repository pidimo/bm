<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>

<script>
    function setId(i){
    document.getElementById('bankId').value = i;
    document.forms[0].submit();
    }
</script>

<table width="60%" border="0" align="center" cellspacing="0" cellpadding="10">
  <tr>
    <td align="center">
    <html:form action="${action}" focus="dto(accountNumber)" >

    <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(addressId)" value="${param.contactId}"/>
        <html:hidden property="dto(value)" styleId="bankId" />

        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(bankAccountId)"/>
        </c:if>
        <c:if test="${('update' == op)}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <tr>
          <td colspan="2" class="title"><c:out value="${title}"/> </td>
        </tr>
        <tr>
          <td width="18%" class="label" ><fmt:message    key="Contact.BankAccount.number" /></td>
          <td width="34%"class="contain">
            <app:text property="dto(accountNumber)" view="${op == 'delete'}" styleClass="mediumText" maxlength="12" tabindex="1" />
          </td>
        </tr>
        <tr>
          <td width="20%" class="label"><fmt:message   key="Contact.BankAccount.bank"/></td>
          <td width="28%" class="contain">   <!-- Collection banks-->
                <fanta:select property="dto(bankId)" styleId="bankId" listName="bankBaseList" firstEmpty="true" labelProperty="nameCode" valueProperty="id" module="/catalogs" styleClass="largeSelect" readOnly="${op == 'delete'}" tabIndex="2">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
                </fanta:select>

<app2:checkAccessRight functionality="BANK" permission="CREATE">
    <tags:selectPopup url="/contacts/BankAccount/Bank/Create.do?contactId=${param.contactId}" name="Create" titleKey="Common.create" hide="${op == 'delete'}"  submitOnSelect="true" width="650" heigth="250" scrollbars="0" imgPath="/img/add.gif" imgWidth="13" imgHeight="16"/>
</app2:checkAccessRight>

          </td>
        </tr>
        <tr>
          <td class="label" ><fmt:message   key="Contact.BankAccount.owner" /></td>
          <td class="contain">
            <app:text property="dto(accountOwner)" view="${op == 'delete'}" styleClass="mediumText" maxlength="40" tabindex="3" />
          </td>
         </tr>
         <tr>
          <td class="label"><fmt:message   key="Contact.BankAccount.default"/></td>
          <td class="contain">   <!-- Bank account default -->
              <html:checkbox property="dto(defaultBankAccount)" disabled="${op == 'delete'}" tabindex="4" styleClass="radio" />
          </td>
        </tr>
        <tr>
          <td class="label" ><fmt:message   key="Contact.BankAccount.iban" /></td>
          <td class="contain">
            <app:text property="dto(iban)" view="${op == 'delete'}" styleClass="mediumText" maxlength="34" tabindex="5" />

         </td>
        </tr>

        <c:if test="${sessionScope.user.valueMap['companyId'] eq param.contactId}">
            <tr>
                <td class="label">
                    <fmt:message key="Contact.BankAccount.account"/>
                </td>
                <td class="contain">
                    <fanta:select property="dto(accountId)"
                                  listName="accountList"
                                  labelProperty="name"
                                  valueProperty="accountId"
                                  styleClass="largeSelect"
                                  module="/catalogs"
                                  firstEmpty="true"
                                  readOnly="${'delete' == op}"
                                  tabIndex="6">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    </fanta:select>
                </td>
            </tr>
        </c:if>

        <tr>
            <td class="label"><fmt:message key="Contact.BankAccount.description"/></td>
            <td class="contain">
                <app:text property="dto(description)" view="${op == 'delete'}" styleClass="largeText" maxlength="250"
                          tabindex="7"/>
            </td>
        </tr>

        <tr>
            <td colspan="2" <c:out value="${sessionScope.listshadow}" escapeXml="false" />><img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
        </tr>
      </table>

	  <table width="100%" border="0" cellpadding="2" cellspacing="0">
        <tr>
          <td class="button">
              <app2:securitySubmit operation="${op}" functionality="BANKACCOUNT" styleClass="button" tabindex="10" ><c:out value="${button}"/></app2:securitySubmit>
              <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="BANKACCOUNT" styleClass="button" property="SaveAndNew" tabindex="11"  ><fmt:message   key="Common.saveAndNew"/></app2:securitySubmit>
              </c:if>
              <html:cancel styleClass="button" tabindex="12"><fmt:message key="Common.cancel"/></html:cancel>
          </td>
        </tr>
      </table>
      </html:form>
    </td>
  </tr>
</table>
