<%@ include file="/Includes.jsp" %>


<table width="90%" border="0" align="center" cellpadding="2" cellspacing="0">
    <tr>
        <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
            <html:form action="/MailAccount/Forward/Create.do">
                <td class="button">
                    <html:submit styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </td>
            </html:form>
        </app2:checkAccessRight>
    </tr>
</table>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
    <tr>
        <td>
            <fanta:table action="MailAccount/List.do"
                         list="mailAccountList"
                         id="account"
                         imgPath="${baselayout}"
                         align="center" width="100%">
                <c:set var="editAction"
                       value="MailAccount/Forward/Update.do?dto(mailAccountId)=${account.mailAccountId}&dto(op)=read"/>
                <c:set var="deleteAction"
                       value="MailAccount/Forward/Delete.do?dto(mailAccountId)=${account.mailAccountId}&dto(op)=read"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="MAIL" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update"
                                            action="${editAction}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="MAIL" permission="DELETE">
                        <fanta:actionColumn name="delete"
                                            title="Common.delete"
                                            action="${deleteAction}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>

                <fanta:dataColumn name="email" styleClass="listItem"
                                  action="${editAction}"
                                  title="MailAccount.email"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  maxLength="25"
                                  width="25%"/>
                <fanta:dataColumn name="login" styleClass="listItem"
                                  title="MailAccount.login"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  maxLength="25" width="20%"/>
                <fanta:dataColumn name="serverName" styleClass="listItem"
                                  title="MailAccount.serverName"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  maxLength="25" width="20%"/>
                <fanta:dataColumn name="smtpServer" styleClass="listItem"
                                  title="MailAccount.smtpServer"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  maxLength="25" width="20%"/>
                <fanta:dataColumn name="defaultAccount" styleClass="listItem2Center"
                                  title="MailAccount.default"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  maxLength="25"
                                  renderData="false"
                                  width="10%">
                    <c:if test="${account.defaultAccount == '1'}">
                        <img align="center" alt="" src="<c:out value="${baselayout}"/>/img/check.gif" border="0"/>
                    </c:if>
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
</table>