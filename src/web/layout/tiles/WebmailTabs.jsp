<%@ include file="/Includes.jsp" %>

<c:if test="${param.category != null}">
    <c:set var="category" value="${param.category}" scope="request"/>
</c:if>
<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
<c:choose>
    <c:when test="${category == '1' || param.category == null}">

        <app2:checkAccessRight functionality="MAIL" permission="VIEW">
            <c:set target="${tabItems}" property="MailAccount.plural" value="/MailAccount/List.do"/>
            <%--<c:set target="${tabItems}" property="Webmail.userMailAcoount"
                   value="/NewUserMail.do?module=webmail&dto(userId)=${user.valueMap['userId']}&dto(companyId)=${user.valueMap['companyId']}"/>--%>
        </app2:checkAccessRight>


        <c:if test="${null  == hideMailConfigurationTabs || false == hideMailConfigurationTabs }">
            <app2:checkAccessRight functionality="MAIL" permission="VIEW">
                <c:set target="${tabItems}" property="Webmail.common.preferences"
                       value="/Preferences/Forward/Update.do?dto(userId)=${user.valueMap['userId']}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="WEBMAILGROUP" permission="VIEW">
                <c:set target="${tabItems}" property="Webmail.contactGroup.plural" value="/Mail/ContactGroupList.do"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="WEBMAILFILTER" permission="VIEW">
                <c:set target="${tabItems}" property="Webmail.filter.plural" value="/Mail/Forward/ListFilter.do"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="WEBMAILFOLDER" permission="VIEW">
                <c:set target="${tabItems}" property="Webmail.folder.plural" value="/Mail/Forward/ListFolder.do"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="WEBMAILSIGNATURE" permission="VIEW">
                <c:set target="${tabItems}" property="Webmail.signature.plural" value="/Mail/SignatureList.do"/>
            </app2:checkAccessRight>

        </c:if>
        <%--<c:set target="${tabItems}" property="WebMail.contact"  value="/Mail/Forward/CreateAddress.do"/>--%>
    </c:when>
</c:choose>
<c:import url="${sessionScope.layout}/submenu.jsp"/>
