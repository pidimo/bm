<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="/campaign/Campaign/PopUpDownload.do?campaignId=${param.campaignId}" var="jsDownloadUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="dto(languageName)" value="languageName"/>
    <app2:jScriptUrlParam param="dto(language)" value="language"/>
    <app2:jScriptUrlParam param="dto(page)" value="page"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/campaign/Campaign/Generate.do?email=true&campaignId=${param.campaignId}&index=3" var="jsGenerateUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="languageName" value="languageName"/>
    <app2:jScriptUrlParam param="language" value="language"/>
    <app2:jScriptUrlParam param="page" value="page"/>
    <app2:jScriptUrlParam param="to" value="document.getElementById('to').value"/>
    <app2:jScriptUrlParam param="mail" value="mail"/>
    <app2:jScriptUrlParam param="dto(to)" value="document.getElementById('to').value"/>
    <app2:jScriptUrlParam param="companyMail" value="document.getElementById('companyMail').value"/>
    <app2:jScriptUrlParam param="employeeMail" value="document.getElementById('employeeMail').value"/>
</app2:jScriptUrl>

<script language="JavaScript">
    function generatePopup(languageName, language, page) {
        win = window.open(${jsDownloadUrl},'generatePopUp', 'width=420,height=180,top=200,left=350,scrollbars=no');
    }

    function generateEmail(languageName, language, page) {
        var company = document.getElementById('company').checked;
        var employee = document.getElementById('employee').checked;
        var mail;

        if (employee)
            mail = document.getElementById('employeeMail').value;
        else
            mail = document.getElementById('companyMail').value;
        window.location = ${jsGenerateUrl};
    }
</script>
<br>

<html:form action="/Campaign/Result.do" onsubmit="javascript:void();">
    <table width="60%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td class="title" colspan="3">
                <fmt:message key="TelecomType.type.document"/>
            </td>
        </tr>
        <c:set var="resultByTemplate" value="${dto.documentByLanguage}" scope="session"/>
        <c:forEach var="item" items="${documentByLanguage}">
            <tr>
                <td class="label">
                    <fmt:message key="Campaign.resultlist.languages"/> <c:out value=": ${item.key}"/> &nbsp;-&nbsp;
                    <fmt:message key="Campaign.resultlist.recipientsSize"/><c:out value=": ${item.value.size} "/>
                </td>
                <c:forEach var="i" begin="0" end="${item.value.page}">
                    <td class="contain" colspan="2">
                                        
                        <html:button styleClass="button" property="dto(generate)"
                                     onclick="generatePopup('${item.key}','${item.value.languageId}','${i}')">
                            <fmt:message key="Document.generate"/>
                        </html:button>
                    </td>
                </c:forEach>
            </tr>
        </c:forEach>
        <tr>
            <td class="title" colspan="3">
                <fmt:message key="Common.email"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="TelecomType.type.mail"/>
            </td>
            <td class="contain" colspan="2">
                <c:set var="telecomTypes" value="${app2:getTelecomTypesOfEmails(pageContext.request)}"/>
                <html:select property="dto(to)" styleClass="select" styleId="to">
                    <html:options collection="telecomTypes" property="value" labelProperty="label"/>
                </html:select>
            </td>

        </tr>
        <c:set var="resultByTemplate" value="${dto.documentByLanguage}" scope="session"/>
        <c:forEach var="item" items="${documentByLanguage}">
            <tr>
                <td class="label">
                    <fmt:message key="Campaign.resultlist.languages"/> <c:out value=": ${item.key}"/> &nbsp;-&nbsp;
                    <fmt:message key="Campaign.resultlist.recipientsSize"/><c:out value=": ${item.value.size} "/>
                </td>
                <c:forEach var="i" begin="0" end="${item.value.page}">
                    <td class="contain" colspan="2">

                        <html:button styleClass="button" property="dto(email)"
                                     onclick="generateEmail('${item.key}','${item.value.languageId}','${i}')">
                            <fmt:message key="Common.send"/>
                        </html:button>

                    </td>
                </c:forEach>
            </tr>
        </c:forEach>
        <tr>
            <td class="label" rowspan="2">
                <fmt:message key="Campaign.generate.senderemail"/>
            </td>
            <td class="contain">
                <html:radio property="dto(company)" styleId="company"  value="true" styleClass="radio" tabindex="8"/>
                <fmt:message key="Company"/>
            </td>
            <td class="contain">
                <app:telecomSelect property="dto(companyMail)" styleId="companyMail" numberColumn="telecomnumber"
                                   telecomType="EMAIL"  addressId="${sessionScope.user.valueMap['companyId']}"
                                   styleClass="select" optionStyleClass="list"/>
            </td>
        </tr>
        <tr>
            <td class="contain">
                <html:radio property="dto(company)" styleId="employee"  value="false" styleClass="radio" tabindex="9"/>
                <fmt:message key="Document.employee"/>
            </td>
            <td class="contain">
                <app:telecomSelect property="dto(employeeMail)" styleId="employeeMail" numberColumn="telecomnumber"
                                   telecomType="EMAIL" addressId="${sessionScope.user.valueMap['companyId']}"
                                   styleClass="select" optionStyleClass="list"
                                   contactPersonId="${sessionScope.user.valueMap['userAddressId']}"/>
            </td>
        </tr>
    </table>
</html:form>
