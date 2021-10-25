<%@ page session="false" %>

<%@ include file="/Includes.jsp" %>

<%
  String locale = request.getParameter("demoLocale");
  Locale defaultLocale = request.getLocale();
  if (locale != null) {
    defaultLocale = new Locale(locale);
  }
  pageContext.setAttribute("language", defaultLocale.getLanguage());
  pageContext.setAttribute("defaultLocale", defaultLocale);

%>


<c:set var="customCopyrightClass" value="copyrightLogin" scope="request"/>

<div>
  <div id="loginFormContainer">

    <html:form action="/DemoAccount/Front/Create" focus="dto(companyName)">

      <html:hidden property="dto(op)" value="${op}"/>

      <div class="form-group">
        <label class="loginLabel" for="companyName_id"><%= JSPHelper.getMessage(defaultLocale, "DemoAccount.companyName")%> *</label>

        <html:text property="dto(companyName)" styleClass="form-control loginInput" maxlength="60"
                   tabindex="1" styleId="companyName_id"/>
      </div>

      <div class="form-group">
        <label class="loginLabel" for="lastName_id"><%= JSPHelper.getMessage(defaultLocale, "DemoAccount.lastName")%> *</label>

        <html:text property="dto(lastName)" styleClass="form-control loginInput" maxlength="60"
                   tabindex="2" styleId="lastName_id"/>
      </div>

      <div class="form-group">
        <label class="loginLabel" for="firstName_id"><%= JSPHelper.getMessage(defaultLocale, "DemoAccount.firstName")%> *</label>

        <html:text property="dto(firstName)" styleClass="form-control loginInput" maxlength="60"
                   tabindex="3" styleId="firstName_id"/>
      </div>

      <div class="form-group">
        <label class="loginLabel" for="email_id"><%= JSPHelper.getMessage(defaultLocale, "DemoAccount.email")%> *</label>

        <html:text property="dto(email)" styleClass="form-control loginInput" maxlength="100"
                   tabindex="4" styleId="email_id"/>
      </div>

      <div class="form-group">
        <label class="loginLabel" for="phoneNumber_id"><%= JSPHelper.getMessage(defaultLocale, "DemoAccount.phoneNumber")%> *</label>

        <html:text property="dto(phoneNumber)" styleClass="form-control loginInput" maxlength="60"
                   tabindex="5" styleId="phoneNumber_id"/>
      </div>

      <html:submit property="dto(save)" styleClass="btn btn-primary btn-lg btn-block button loginButton" tabindex="6">
        <%= JSPHelper.getMessage(defaultLocale, "DemoAccount.button.startDemo")%>
      </html:submit>

    </html:form>

  </div>

</div>

