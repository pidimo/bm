<%@ include file="/Includes.jsp" %>
<tags:initSelectColorPopup/>

<table align="center" width="60%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <html:form action="${action}" focus="dto(name)">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td colspan="2" class="title" width="100%">
                        <c:out value="${title}"/>
                    </td>
                </tr>
                <tr>
                    <td class="label" width="30%"><fmt:message key="AppointmentType.name"/></td>
                    <td class="contain" width="70%">
                        <app:text property="dto(name)" styleClass="largetext" maxlength="20" view="${'delete' == op}" tabindex="1" />
                    </td>
                </tr>
                <tr>
                    <td class="label" width="30%"><fmt:message key="AppointmentType.color"/></td>
                    <td class="contain" width="70%">
                    <app:text property="dto(color)" styleId="color" styleClass="dateText" maxlength="7" view="${'delete' == op}" tabindex="2" onkeyup="javascript:keyUpPreviewColor('color')"/>
                        <c:if test="${op == 'create' || op=='update'}">
                            <tags:selectColorPopup inputKey="color" preview ="true"/>
                        </c:if>
                    </td>
                </tr>
                </table>
                <table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
                  <tr>
                      <td class="button">
                      <c:if test="${op == 'create' || op=='update'}">
                         <html:submit styleClass="button" tabindex="3" ><fmt:message key="Common.save"/></html:submit>
                      </c:if>
                      <c:if test="${op == 'delete'}">
                        <html:submit styleClass="button" tabindex="4" ><fmt:message key="Common.delete"/></html:submit>
                      </c:if>
                      <c:if test="${op == 'create'}" >
                            <html:submit property="SaveAndNew" styleClass="button" tabindex="3" ><fmt:message key="Common.saveAndNew"/></html:submit>
                      </c:if>
                      <html:cancel styleClass="button" tabindex="5" ><fmt:message   key="Common.cancel"/></html:cancel>
                      </TD>
                  </TR>
               </table>
                <html:hidden property="dto(op)" value="${op}"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                <c:if test="${op=='update' || op=='delete'}">
                    <html:hidden property="dto(appointmentTypeId)"/>
                    <html:hidden property="dto(version)"/>
                </c:if>
            </html:form>
        </td>
    </tr>
</table>