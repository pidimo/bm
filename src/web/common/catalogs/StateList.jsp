<%@ include file="/Includes.jsp" %>
<%
                        pageContext.setAttribute("stages",JSPHelper.getStageTypesState(request, false));
                    %>
<table border="0" cellpadding="0" cellspacing="0" width="77%" class="container" align="center">
    <app2:checkAccessRight functionality="STATE" permission="CREATE">
    <tr>
        <html:form action="${create}">
            <TD class="button">
                <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
            </TD>
        </html:form>
    </tr>
    </app2:checkAccessRight>
    <tr>
        <td>
            <fanta:table width="100%" id="state" action="${action}" imgPath="${baselayout}">
                <c:set var="editAction" value="${edit}?dto(stateName)=${app2:encode(state.name)}&dto(stateId)=${state.id}"/>
                <c:set var="deleteAction" value="${delete}?dto(stateId)=${state.id}&dto(stateName)=${app2:encode(state.name)}"/>
                <c:set var="translateAction" value="Support/State/Translate.do?dto(stateId)=${state.id}&dto(op)=read&dto(stateName)=${app2:encode(state.name)}"/>


                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    
                    <fanta:actionColumn name="trans" title="Common.translate" action="${translateAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/tournicoti.gif"/>

                    <app2:checkAccessRight functionality="STATE" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader" image="${baselayout}/img/edit.gif"/>
                  </app2:checkAccessRight>
                  <app2:checkAccessRight functionality="STATE" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="State.name"
                                  headerStyle="listHeader" width="50%" orderable="true" maxLength="30"/>
                <fanta:dataColumn name="sequence" styleClass="listItemRight" title="State.sequence"
                                  headerStyle="listHeader" width="20%" orderable="true"/>
                <fanta:dataColumn name="" styleClass="listItem2" title="State.stageType" headerStyle="listHeader"
                                  width="25%" renderData="false">
                    ${stages[state.stageType].label}
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
</table>
