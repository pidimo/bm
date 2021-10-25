<%@ page import="com.piramide.elwis.utils.CampaignConstants"%>
<%@ include file="/Includes.jsp" %>

<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <tr>
        <td class="button">

            <html:button property="" styleClass="button" onclick="location.href='${urlGenerateEmailReturn}'">
                ${button}
            </html:button>
        </td>
    </tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
    <tr>
        <td colspan="2" class="title">
            <fmt:message key="Campaign.activity.emailSend.summary.title"/>
        </td>
    </tr>
    <tr>
        <td class="label" width="50%">
            <fmt:message key="Campaign.activity.emailGenerate.template"/>
        </td>
        <td class="contain">
            <fanta:label listName="templateList" module="/campaign" patron="0"
                         label="description" columnOrder="description">
                <fanta:parameter field="templateId" value="${templateId}"/>
            </fanta:label>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Campaign.emailSend.summary.totalRecipient"/>
        </td>
        <td class="contain">
            <c:out value="${totalRecipients}"/>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Campaign.emailSend.summary.totalSent"/>
        </td>
        <td class="contain">
            <c:out value="${totalSuccess}"/>
        </td>
    </tr>
    <c:if test="${totalFail > 0}">
        <tr>
            <td class="label">
                <fmt:message key="Campaign.emailSend.summary.totalNotSent"/>
            </td>
            <td class="contain">
                <c:out value="${totalFail}"/>
            </td>
        </tr>
    </c:if>

    <tr>
        <td colspan="2" class="title">
            <fmt:message key="Campaign.emailSend.summary.mailSentByLanguage"/>
        </td>
    </tr>
    <c:forEach var="summary" items="${summaryList}">
        <tr>
            <td class="label">
                <c:out value="${summary.label}"/>
            </td>
            <td class="contain">
                <c:out value="${summary.value}"/>
            </td>
        </tr>
    </c:forEach>

    <c:if test="${not empty withoutMailList}">
        <tr>
            <td colspan="2" class="title">
                <fmt:message key="Campaign.emailSend.summary.contactWithoutEmail"/>
            </td>
        </tr>
        <c:set var="AWITHOUTMAIL" value="<%=CampaignConstants.ADDRESS_WITHOUT_MAIL%>"/>
        <c:set var="CPWITHOUTMAIL" value="<%=CampaignConstants.CONTACTPERSON_WITHOUT_MAIL%>"/>
        <c:forEach var="recipient" items="${withoutMailList}">
            <tr>
                <td class="label">
                    <c:out value="${recipient.contactName}"/>
                    <c:if test="${recipient.addressWithoutMail eq AWITHOUTMAIL}">
                        <img src='<c:out value="${sessionScope.baselayout}"/>/img/close.gif' alt="" title="<fmt:message key="Campaign.emailSend.summary.mailNotFound"/>"/>
                    </c:if>
                </td>
                <td class="label">
                    <c:out value="${recipient.contactPersonName}"/>
                    <c:if test="${recipient.addressWithoutMail eq CPWITHOUTMAIL}">
                        <img src='<c:out value="${sessionScope.baselayout}"/>/img/close.gif' alt="" title="<fmt:message key="Campaign.emailSend.summary.mailNotFound"/>"/>
                    </c:if>
                    &nbsp;
                </td>
            </tr>
        </c:forEach>
    </c:if>

    <c:if test="${not empty failSentList}">
        <tr>
            <td colspan="2" class="title">
                <fmt:message key="Campaign.emailSend.summary.recipientsWithUnexpectedError"/>
            </td>
        </tr>
        <c:forEach var="recipient" items="${failSentList}">
            <tr>
                <td class="label">
                    <c:out value="${recipient.contactName}"/>
                </td>
                <td class="label">
                    <c:out value="${recipient.contactPersonName}"/>
                </td>
            </tr>
        </c:forEach>
    </c:if>
</table>

<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <tr>
        <td class="button">
            <html:button property="" styleClass="button" onclick="location.href='${urlGenerateEmailReturn}'">
                ${button}
            </html:button>
        </td>
    </tr>
</table>
