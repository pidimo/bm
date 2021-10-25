<%@ include file="/Includes.jsp" %>

<jsp:useBean id="functionalitiesToCheck" class="java.util.LinkedHashMap" scope="page" />
<c:set target="${functionalitiesToCheck}" property="MAIL"  value="VIEW"/>
<c:set target="${functionalitiesToCheck}" property="WEBMAILGROUP"  value="VIEW"/>
<c:set target="${functionalitiesToCheck}" property="WEBMAILFILTER"  value="VIEW"/>
<c:set target="${functionalitiesToCheck}" property="WEBMAILFOLDER"  value="VIEW"/>
<c:set target="${functionalitiesToCheck}" property="WEBMAILSIGNATURE"  value="VIEW"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
     <td valign="top" align="left" width="30%" class="moduleShortCut">
        &nbsp;
        <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
            |&nbsp;
            
            <app:link page="/webmail/Mail/UploadAndDownloadEmail.do"
                      addModuleParams="false"
                      addModuleName="false"
                      contextRelative="true"
                      onclick="javascript:disableUploadAndDownload(this);return false">
                <fmt:message key="Webmail.uploadAndDownloadEmail"/>
            </app:link>
            |&nbsp;
            <app:link page="/webmail/Mail/Forward/ComposeEmail.do" addModuleParams="false" addModuleName="false" contextRelative="true"  >
                <fmt:message key="Webmail.compose"/>
            </app:link>
            &nbsp;|
        </app2:checkAccessRight>
     </td>

     <td valign="top" align="right" width="30%" class="moduleShortCut">
      <c:if test="${app2:checkSomeAccessRights(pageContext.request, functionalitiesToCheck)}">
        <c:choose>
            <c:when test="${app2:hasAccessRight(pageContext.request,'MAIL','VIEW')}">
                <app:link page="/webmail/NewUserMail.do?module=webmail&dto(userId)=${user.valueMap['userId']}&dto(companyId)=${user.valueMap['companyId']}" contextRelative="true" addModuleParams="false" addModuleName="false">
                    <fmt:message key="Webmail.common.setting"/>
                </app:link>
           </c:when>

           <c:when test="${app2:hasAccessRight(pageContext.request,'MAIL','VIEW')}">
                <app:link page="/webmail/Preferences/Forward/Update.do?dto(userId)=${user.valueMap['userId']}" addModuleParams="false" addModuleName="false" contextRelative="true"  >
                    <fmt:message key="Webmail.common.setting"/>
                </app:link>
           </c:when>
           <c:when test="${app2:hasAccessRight(pageContext.request,'WEBMAILGROUP','VIEW')}">
                <app:link page="/webmail/Mail/ContactGroupList.do" addModuleParams="false" addModuleName="false" contextRelative="true"  >
                    <fmt:message key="Webmail.common.setting"/>
                </app:link>
           </c:when>
           <c:when test="${app2:hasAccessRight(pageContext.request,'WEBMAILFILTER','VIEW')}">
                <app:link page="/webmail/Mail/Forward/ListFilter.do" addModuleParams="false" addModuleName="false" contextRelative="true"  >
                    <fmt:message key="Webmail.common.setting"/>
                </app:link>
           </c:when>
           <c:when test="${app2:hasAccessRight(pageContext.request,'WEBMAILFOLDER','VIEW')}">
                <app:link page="/webmail/Mail/Forward/ListFolder.do" addModuleParams="false" addModuleName="false" contextRelative="true"  >
                    <fmt:message key="Webmail.common.setting"/>
                </app:link>
           </c:when>
           <c:when test="${app2:hasAccessRight(pageContext.request,'WEBMAILSIGNATURE','VIEW')}">
                <app:link page="/webmail/Mail/SignatureList.do" addModuleParams="false" addModuleName="false" contextRelative="true"  >
                    <fmt:message key="Webmail.common.setting"/>
                </app:link>
           </c:when>
        </c:choose>
        &nbsp;
     </c:if>
     </td>
  </tr>
</table>

