<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<table width="65%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <app2:checkAccessRight functionality="PROJECTTIMELIMIT" permission="CREATE">
            <html:form action="/ProjectTimeLimit/Forward/Create.do">
                <td class="button">
                    <html:submit styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </td>
            </html:form>
        </app2:checkAccessRight>
    </tr>
    <tr>
        <td>
            <fanta:table list="projectTimeLimitList"
                         width="100%"
                         id="timeLimit"
                         action="ProjectTimeLimit/List.do"
                         imgPath="${baselayout}"
                         align="center">

                <c:set var="editLink"
                       value="ProjectTimeLimit/Forward/Update.do?dto(timeLimitId)=${timeLimit.timeLimitId}&dto(assigneeName)=${app2:encode(timeLimit.assigneeName)}"/>
                <c:set var="deleteLink"
                       value="ProjectTimeLimit/Forward/Delete.do?dto(timeLimitId)=${timeLimit.timeLimitId}&dto(assigneeName)=${app2:encode(timeLimit.assigneeName)}&dto(withReferences)=true"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="PROJECTTIMELIMIT" permission="VIEW">
                        <fanta:actionColumn name="edit"
                                            title="Common.update"
                                            action="${editLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            width="50%"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="PROJECTTIMELIMIT" permission="DELETE">
                        <fanta:actionColumn name="delete"
                                            title="Common.delete"
                                            action="${deleteLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="assigneeName" action="${editLink}"
                                  styleClass="listItem" title="ProjectTimeLimit.assignee"
                                  headerStyle="listHeader" orderable="true"
                                  maxLength="40"
                                  width="30%"/>
                <fanta:dataColumn name="subProjectName"
                                  styleClass="listItem" title="ProjectTimeLimit.subProject"
                                  headerStyle="listHeader" orderable="true"
                                  maxLength="40"
                                  width="25%"/>
                <fanta:dataColumn name="invoiceLimit"
                                  styleClass="listItemRight" title="ProjectTimeLimit.invoiceLimit"
                                  headerStyle="listHeader" orderable="true" renderData="false"
                                  width="20%">
                    <fmt:formatNumber var="invoiceLimit" value="${timeLimit.invoiceLimit}" type="number" pattern="${numberFormat}"/>
                    ${invoiceLimit}
                </fanta:dataColumn>
                <fanta:dataColumn name="noInvoiceLimit"
                                  styleClass="listItem2Right" title="ProjectTimeLimit.noInvoiceLimit"
                                  headerStyle="listHeader" orderable="true" renderData="false"
                                  width="20%">
                    <fmt:formatNumber var="noInvoiceLimit" value="${timeLimit.noInvoiceLimit}" type="number" pattern="${numberFormat}"/>
                    ${noInvoiceLimit}
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
    <tr>
        <app2:checkAccessRight functionality="PROJECTTIMELIMIT" permission="CREATE">
            <html:form action="/ProjectTimeLimit/Forward/Create.do">
                <td class="button">
                    <html:submit styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </td>
            </html:form>
        </app2:checkAccessRight>
    </tr>
</table>