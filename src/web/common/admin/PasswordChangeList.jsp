<%@ include file="/Includes.jsp"%>

<fmt:message   var="dateTimePattern" key="dateTimePattern"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>

<table width="70%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
                <tr>
                    <td height="20" class="title" colspan="2">
                        <fmt:message key="PasswordChange.title.list"/>
                    </td>
                </tr>
                <html:form action="/PasswordChange/List.do" focus="parameter(description)">
                    <TR>
                        <td class="label" width="15%"><fmt:message key="Common.search"/></td>
                        <td align="left" class="contain" width="85%">
                            <html:text property="parameter(description)" styleClass="largeText" maxlength="100"/>&nbsp;
                            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
                        </td>
                    </TR>
                </html:form>
                <tr>
                    <td colspan="2" align="center" class="alpha">
                        <fanta:alphabet action="PasswordChange/List.do" parameterName="descriptionAlpha"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

    <app2:checkAccessRight functionality="PASSWORDCHANGE" permission="CREATE">
        <tr>
            <html:form action="/PasswordChange/Forward/Create.do">
                <td class="button">
                    <br>
                    <html:submit styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </td>
            </html:form>
        </tr>
    </app2:checkAccessRight>
    <tr>
        <td>
            <fanta:table list="passwordChangeList"
                         width="100%"
                         id="passwordChange"
                         action="PasswordChange/List.do"
                         imgPath="${baselayout}"
                         align="center">

                <c:set var="editLink"
                       value="PasswordChange/Forward/Update.do?passwordChangeId=${passwordChange.passwordChangeId}&dto(passwordChangeId)=${passwordChange.passwordChangeId}&dto(op)=read"/>
                <c:set var="deleteLink"
                       value="PasswordChange/Forward/Delete.do?passwordChangeId=${passwordChange.passwordChangeId}&dto(passwordChangeId)=${passwordChange.passwordChangeId}&dto(op)=read&dto(withReferences)=true"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="PASSWORDCHANGE" permission="VIEW">
                        <fanta:actionColumn name="edit"
                                            title="Common.update"
                                            action="${editLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            width="50%"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="PASSWORDCHANGE" permission="DELETE">
                        <fanta:actionColumn name="delete"
                                            title="Common.delete"
                                            action="${deleteLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>

                <fanta:dataColumn name="description" action="${editLink}" styleClass="listItem" title="PasswordChange.description"
                                  headerStyle="listHeader" width="50%" maxLength="100" orderable="true"/>

                <fanta:dataColumn name="totalUser" styleClass="listItemRight" title="PasswordChange.totalUser"
                                  headerStyle="listHeader" width="10%" orderable="true"/>

                <fanta:dataColumn name="userPassChangeCount" styleClass="listItemRight" title="PasswordChange.totalUserChanged"
                                  headerStyle="listHeader" width="10%" orderable="false" renderData="false">
                    <c:out value="${passwordChange.totalUser - passwordChange.userPassChangeCount}"/>
                </fanta:dataColumn>

                <fanta:dataColumn name="changeTime" styleClass="listItem2" title="PasswordChange.changeTime"
                                  headerStyle="listHeader" width="25%" orderable="true" renderData="false">
                    <c:choose>
                      <c:when test="${not empty passwordChange.changeTime}">
                          ${app2:getDateWithTimeZone(passwordChange.changeTime, timeZone, dateTimePattern)}
                      </c:when>
                    </c:choose>
                </fanta:dataColumn>
            </fanta:table>

        </td>
    </tr>
    <app2:checkAccessRight functionality="PASSWORDCHANGE" permission="CREATE">
        <tr>
            <html:form action="/PasswordChange/Forward/Create.do">
                <td class="button">
                    <html:submit styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </td>
            </html:form>
        </tr>
    </app2:checkAccessRight>
</table>
