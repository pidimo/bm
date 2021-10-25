<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
        <html:form action="/MailAccount/Forward/Create.do">
            <div class="${app2:getFormGroupClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" action="MailAccount/List.do"
                     list="mailAccountList"
                     styleClass="${app2:getFantabulousTableClases()}"
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
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="MAIL" permission="DELETE">
                    <fanta:actionColumn name="delete"
                                        title="Common.delete"
                                        action="${deleteAction}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
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
                    <span class="${app2:getClassGlyphOk()}"></span>
                    <%--<img align="center" alt="" src="<c:out value="${baselayout}"/>/img/check.gif" border="0"/>--%>
                </c:if>
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>