<%@ include file="/Includes.jsp" %>

<jsp:useBean id="functionalitiesToCheck" class="java.util.LinkedHashMap" scope="page"/>
<c:set target="${functionalitiesToCheck}" property="MAIL" value="VIEW"/>
<c:set target="${functionalitiesToCheck}" property="WEBMAILGROUP" value="VIEW"/>
<c:set target="${functionalitiesToCheck}" property="WEBMAILFILTER" value="VIEW"/>
<c:set target="${functionalitiesToCheck}" property="WEBMAILFOLDER" value="VIEW"/>
<c:set target="${functionalitiesToCheck}" property="WEBMAILSIGNATURE" value="VIEW"/>


<ul class="dropdown-menu">

    <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
        <li>
            <app:link page="/webmail/Messages.do"
                      addModuleParams="false"
                      addModuleName="false"
                      contextRelative="true">
                <fmt:message key="Webmail.folder.inbox"/>
            </app:link>
        </li>
        <li>
            <app:link page="/webmail/Mail/UploadAndDownloadEmail.do"
                      addModuleParams="false"
                      addModuleName="false"
                      contextRelative="true"
                      onclick="javascript:disableUploadAndDownload(this);return false">
                <fmt:message key="Webmail.uploadAndDownloadEmail"/>
            </app:link>
        </li>
        <li>
            <app:link page="/webmail/Mail/Forward/ComposeEmail.do" addModuleParams="false" addModuleName="false"
                      contextRelative="true">
                <fmt:message key="Webmail.compose"/>
            </app:link>
        </li>
    </app2:checkAccessRight>

    <c:if test="${app2:checkSomeAccessRights(pageContext.request, functionalitiesToCheck)}">
        <li role="separator" class="divider"></li>
        <li>
            <c:choose>
                <c:when test="${app2:hasAccessRight(pageContext.request,'MAIL','VIEW')}">
                    <app:link
                            page="/webmail/NewUserMail.do?module=webmail&dto(userId)=${user.valueMap['userId']}&dto(companyId)=${user.valueMap['companyId']}"
                            contextRelative="true" addModuleParams="false" addModuleName="false">
                        <fmt:message key="Webmail.common.setting"/>
                    </app:link>
                </c:when>

                <c:when test="${app2:hasAccessRight(pageContext.request,'MAIL','VIEW')}">
                    <app:link page="/webmail/Preferences/Forward/Update.do?dto(userId)=${user.valueMap['userId']}"
                              addModuleParams="false" addModuleName="false" contextRelative="true">
                        <fmt:message key="Webmail.common.setting"/>
                    </app:link>
                </c:when>
                <c:when test="${app2:hasAccessRight(pageContext.request,'WEBMAILGROUP','VIEW')}">
                    <app:link page="/webmail/Mail/ContactGroupList.do" addModuleParams="false" addModuleName="false"
                              contextRelative="true">
                        <fmt:message key="Webmail.common.setting"/>
                    </app:link>
                </c:when>
                <c:when test="${app2:hasAccessRight(pageContext.request,'WEBMAILFILTER','VIEW')}">
                    <app:link page="/webmail/Mail/Forward/ListFilter.do" addModuleParams="false" addModuleName="false"
                              contextRelative="true">
                        <fmt:message key="Webmail.common.setting"/>
                    </app:link>
                </c:when>
                <c:when test="${app2:hasAccessRight(pageContext.request,'WEBMAILFOLDER','VIEW')}">
                    <app:link page="/webmail/Mail/Forward/ListFolder.do" addModuleParams="false" addModuleName="false"
                              contextRelative="true">
                        <fmt:message key="Webmail.common.setting"/>
                    </app:link>
                </c:when>
                <c:when test="${app2:hasAccessRight(pageContext.request,'WEBMAILSIGNATURE','VIEW')}">
                    <app:link page="/webmail/Mail/SignatureList.do" addModuleParams="false" addModuleName="false"
                              contextRelative="true">
                        <fmt:message key="Webmail.common.setting"/>
                    </app:link>
                </c:when>
            </c:choose>
        </li>
    </c:if>
</ul>
