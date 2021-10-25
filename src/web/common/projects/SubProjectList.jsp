<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<table width="50%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <app2:checkAccessRight functionality="PROJECTSUBPROJECT" permission="CREATE">
            <html:form action="/SubProject/Forward/Create.do">
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
            <fanta:table list="subProjectList"
                         width="100%"
                         id="subProject"
                         action="SubProject/List.do"
                         imgPath="${baselayout}"
                         align="center">

                <c:set var="editLink"
                       value="SubProject/Forward/Update.do?dto(subProjectId)=${subProject.subProjectId}&dto(name)=${app2:encode(subProject.subProjectName)}"/>
                <c:set var="deleteLink"
                       value="SubProject/Forward/Delete.do?dto(subProjectId)=${subProject.subProjectId}&dto(name)=${app2:encode(subProject.subProjectName)}&dto(withReferences)=true"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="PROJECTSUBPROJECT" permission="VIEW">
                        <fanta:actionColumn name="edit"
                                            title="Common.update"
                                            action="${editLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            width="50%"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="PROJECTSUBPROJECT" permission="DELETE">
                        <fanta:actionColumn name="delete"
                                            title="Common.delete"
                                            action="${deleteLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="subProjectName" action="${editLink}"
                                  styleClass="listItem" title="SubProject.name"
                                  headerStyle="listHeader" orderable="true"
                                  maxLength="40"
                                  width="55%"/>
                <fanta:dataColumn name=""
                                  styleClass="listItemRight" title="SubProject.totalInvoiceTime"
                                  headerStyle="listHeader" orderable="false" renderData="false"
                                  width="20%">

                    <c:set var="totalTimeMap" value="${app2:calculateProjectTimesBySubProject(subProject.projectId, subProject.subProjectId)}"/>

                    <fmt:formatNumber var="invoiceTimeSum" value="${totalTimeMap.totalInvoiceTime}" type="number" pattern="${numberFormat}"/>
                    ${invoiceTimeSum}
                </fanta:dataColumn>
                <fanta:dataColumn name=""
                                  styleClass="listItem2Right" title="SubProject.totalNoInvoiceTime"
                                  headerStyle="listHeader" orderable="false" renderData="false"
                                  width="20%">
                    <fmt:formatNumber var="noInvoiceTimeSum" value="${totalTimeMap.totalNoInvoiceTime}" type="number" pattern="${numberFormat}"/>
                    ${noInvoiceTimeSum}
                </fanta:dataColumn>

            </fanta:table>

        </td>
    </tr>
    <tr>
        <app2:checkAccessRight functionality="PROJECTSUBPROJECT" permission="CREATE">
            <html:form action="/SubProject/Forward/Create.do">
                <td class="button">
                    <html:submit styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </td>
            </html:form>
        </app2:checkAccessRight>
    </tr>
</table>