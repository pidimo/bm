<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="/campaign/Campaign/Download/Document.do?campaignId=${param.campaignId}" var="jsDownloadUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="templateId" value="idTemplate"/>
    <app2:jScriptUrlParam param="languageId" value="idLanguage"/>
</app2:jScriptUrl>


<script language="JavaScript">
    function documentDownload(idTemplate, idLanguage) {
        location.href = ${jsDownloadUrl};
    }

</script>


<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <tr>
        <td class="button">
            <html:button property="" styleClass="button" onclick="location.href='${urlGenerateDocumentReturn}'">
                ${button}
            </html:button>
        </td>
    </tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
    <tr>
        <td colspan="2" class="title">
            <fmt:message key="Campaign.document.summary.title"/>
        </td>
    </tr>
    <tr>
        <td class="label" width="50%">
            <fmt:message key="Campaign.activity.docGenerate.template"/>
        </td>
        <td class="contain">
            <c:out value="${templateName}"/>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Campaign.activity.docGenerate.totalContacts"/>
        </td>
        <td class="contain">
            <c:out value="${totalContact}"/>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Campaign.activity.docGenerate.contactProcessed"/>
        </td>
        <td class="contain">
            <c:out value="${totalProcessed}"/>
        </td>
    </tr>
    <c:if test="${totalFail > 0}">
        <tr>
            <td class="label">
                <fmt:message key="Campaign.activity.docGenerate.contactNotProcessed"/>
            </td>
            <td class="contain">
                <c:out value="${totalFail}"/>
            </td>
        </tr>
    </c:if>

    <tr>
        <td colspan="2" class="title">
            <fmt:message key="Campaign.activity.docGenerate.docOutputByLanguage"/>
        </td>
    </tr>
    <c:forEach var="summary" items="${summaryList}">
        <tr>
            <td class="label">
                <c:out value="${summary.language}"/>
            </td>
            <td class="contain">
                <a href="javascript:documentDownload('${templateId}','${summary.languageId}')"
                   title="<fmt:message key="Campaign.document.summary.download"/>">
                    <img src='<c:out value="${sessionScope.baselayout}"/>/img/openfile.png' alt="" border="0">
                </a>
                <fmt:message key="Campaign.activity.docGenerate.contacts">
                    <fmt:param value="${summary.count}"/>
                </fmt:message>
            </td>
        </tr>
    </c:forEach>
</table>
<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <tr>
        <td class="button">
            <html:button property="" styleClass="button" onclick="location.href='${urlGenerateDocumentReturn}'">
                ${button}
            </html:button>
        </td>
    </tr>
</table>
