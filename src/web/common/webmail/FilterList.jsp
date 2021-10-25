<%@ include file="/Includes.jsp" %>

<table cellSpacing=0 cellPadding=0 width="60%" border=0 align="center">
    <TR>
        <html:form action="/Mail/Forward/CreateFilter.do">
            <TD class="button">
                <app2:securitySubmit operation="create" functionality="WEBMAILFILTER" styleClass="button"><fmt:message
                        key="Common.new"/></app2:securitySubmit>
            </TD>
        </html:form>
    </TR>
    <tr>
        <td>
            <fanta:table list="filterList" width="100%" id="listF" action="Mail/Forward/ListFilter.do" imgPath="${baselayout}"
                         align="center">

                <app:url var="urlEdit" value="Mail/Forward/UpdateFilter.do?dto(filterId)=${listF.id_filter}&dto(op)=read" enableEncodeURL="false"/>
                <app:url var="urlDelete" value="Mail/Forward/DeleteFilter.do?dto(filterId)=${listF.id_filter}&dto(op)=read" enableEncodeURL="false"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="WEBMAILFILTER" permission="VIEW">
                        <fanta:actionColumn name="edit" label="Common.update" title="Common.update" action="${urlEdit}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="WEBMAILFILTER" permission="DELETE">
                        <fanta:actionColumn name="delete" label="Common.delete" title="Common.delete" action="${urlDelete}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>

                </fanta:columnGroup>

                <fanta:dataColumn name="name_filter" title="Webmail.filter.tiltle.name" action="${urlEdit}" styleClass="listItem"
                                  headerStyle="listHeader" width="50%" orderable="true">
                </fanta:dataColumn>

                <c:choose>
                    <c:when test="${listF.type_folder==4}">
                        <fanta:dataColumn name="" title="Webmail.filter.connectedToFolder" styleClass="listItem2" renderData="false"
                                          headerStyle="listHeader" width="45%" orderable="true">
                            <fmt:message key="Webmail.folder.trash"/>
                        </fanta:dataColumn>
                    </c:when>
                    <c:otherwise>
                        <fanta:dataColumn name="name_folder" title="Webmail.filter.connectedToFolder" styleClass="listItem2"
                                          headerStyle="listHeader" width="45%" orderable="true">
                        </fanta:dataColumn>
                    </c:otherwise>
                </c:choose>
            </fanta:table>
        </td>
    </tr>
</table>

