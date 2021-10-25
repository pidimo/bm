<%@ include file="/Includes.jsp" %>

<%
    Object mailFilter = request.getParameter("mailFilter");
    request.setAttribute("mailFilter", (mailFilter != null ? mailFilter.toString() : "0"));

    String locale = request.getParameter("locale");
    Locale defaultLocale = request.getLocale();
    if (locale != null) {
        defaultLocale = new Locale(locale);
    }
%>

<c:url var="urlCancel" value="/webmail/Mail/MailTray.do">
    <c:param name="folderId" value="${folderView}"/>
    <c:param name="index" value="${0}"/>
    <c:param name="mailFilter" value="${mailFilter}"/>
    <c:param name="returning" value="true"/>
</c:url>
<html:form action="${action}" styleClass="form-horizontal">
    <html:hidden property="dto(op)" value="moveAllEmailsToTrash" styleId="aaaa"/>
    <html:hidden property="dto(folderId)" value="${mailTrayForm.dto['folderId']}" styleId="bbb"/>
   <div class="${app2:getFormClassesLarge()}">
       <div class="${app2:getFormPanelClasses()}">
           <fieldset>
               <legend class="title">
                       ${title}
               </legend>
               <div class="${app2:getFormGroupClasses()}">
                   <div class="${app2:getFormContainClasses(null)}">
                       <c:set var="folderName" value="${mailTrayForm.dto['folderName']}"/>
                       <c:set var="numberOfEmails" value="${mailTrayForm.dto['folderSize']}"/>
                       <fmt:message key="Webmail.email.moveToTrash">
                           <fmt:param value="${numberOfEmails}"/>
                           <fmt:param value="${folderName}"/>
                       </fmt:message>
                   </div>
               </div>
           </fieldset>
       </div>
       <div class="${app2:getFormButtonWrapperClasses()}">
           <app2:securitySubmit operation="delete" property="dto(delete)" functionality="MAIL" styleClass="button ${app2:getFormButtonClasses()}"
                                tabindex="3"><fmt:message key="Common.delete"/></app2:securitySubmit>
           <html:button property="" styleClass="button ${app2:getFormButtonCancelClasses()}" onclick="location.href='${urlCancel}'">
               <fmt:message key="Common.cancel"/>
           </html:button>
       </div>
   </div>
</html:form>
