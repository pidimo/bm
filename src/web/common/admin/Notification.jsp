<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>

  <br />
  <br />
  <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td>
        <html:form action="/Notification/Update.do"  focus="dto(notificationAppointmentEmail)" >
          <html:hidden property="dto(userId)" />
          <html:hidden property="dto(version)"/>
          <html:hidden property="dto(op)" value="update"/>
          <html:hidden property="dto(companyId)" />
            <table cellSpacing=0 cellPadding=4 width="70%" border=0 align="center">
                <TR>
                  <TD class="button">
                      <app2:securitySubmit operation="UPDATE" functionality="USERSETTINGS" styleClass="button" tabindex="6">${save}</app2:securitySubmit>
                    <%--<html:submit styleClass="button" tabindex="6" >
                      <fmt:message key="Common.save" />
                    </html:submit>--%>
                  </TD>
                </TR>
              </table>
          <table cellSpacing=0 cellPadding=0 width="70%" border=0 align="center">
            <TR>
              <TD colspan="2" class="title">
                <fmt:message key="Admin.Notification" />
              </TD>
            </TR>
            <tr>
              <td class="label" width="40%">
                <fmt:message key="Admin.AppointmentEmail" />
              </td>
              <td class="contain" width="60%">
<app:text property="dto(notificationAppointmentEmail)" styleClass="largeText" maxlength="200" tabindex="3" />
              </td>
            </tr>
            <tr>
              <td class="label">
                <fmt:message key="Admin.TaskEmail" />
              </td>
              <td class="contain">
<app:text property="dto(notificationSchedulerTaskEmail)" styleClass="largeText" maxlength="200" tabindex="3"/>
              </tr>
              <tr>
                <td class="label">
                  <fmt:message key="Admin.CaseEmail" />
                </td>
                <td class="contain">
<app:text property="dto(notificationSupportCaseEmail)" styleClass="largeText" maxlength="200" tabindex="3" />
                </td>
              </tr>
              <tr>
                <td class="label">
                  <fmt:message key="Admin.QuestionEmail" />
                </td>
                <td class="contain">
<app:text property="dto(notificationQuestionEmail)" styleClass="largeText" maxlength="200" tabindex="3" />
                </td>
              </tr>
              <tr>
                <td class="contain" colspan="2" >
                <fmt:message key="Admin.messageMail" /><br>
                <fmt:message key="Admin.comma" />
                </td>
              </tr>
              </table>
              <table cellSpacing=0 cellPadding=4 width="70%" border=0 align="center">
                <TR>
                  <TD class="button">
                      <app2:securitySubmit operation="UPDATE" functionality="USERSETTINGS" styleClass="button" tabindex="6">${save}</app2:securitySubmit>
                    <%--<html:submit styleClass="button" tabindex="6" >
                      <fmt:message key="Common.save" />
                    </html:submit>--%>
                  </TD>
                </TR>
              </table>
            </html:form>
          </td>
        </tr>
      </table>
