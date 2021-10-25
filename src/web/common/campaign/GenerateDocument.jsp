<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(templateId)">
    <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
    <html:hidden property="dto(activityId)"/>

    <html:hidden property="dto(documentType)"/>

    <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
    <html:hidden property="dto(userAddressId)" styleId="idUserAddress" value="${sessionScope.user.valueMap['userAddressId']}"/>

    <html:hidden property="dto(isCampaignLight)" value="${isCampaignLight}"/>

    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
        <tr>
            <td class="button">

                <app2:securitySubmit property="dto(generate)" operation="execute" functionality="CAMPAIGNACTIVITY"
                                     styleClass="button" tabindex="1">
                    <fmt:message key="Document.generate"/>
                </app2:securitySubmit>

                <html:cancel styleClass="button" tabindex="2">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>

    <table width="60%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td class="title" colspan="2">
                ${title}
            </td>
        </tr>
        <tr>
            <td class="label" width="17%">
                <fmt:message key="Campaign.activity.docGenerate.template"/>
            </td>
            <td class="contain" width="33%">
                <fanta:select property="dto(templateId)" listName="templateList" module="/campaign" labelProperty="description"  valueProperty="templateId"
                              firstEmpty="${app2:campaignHasOnlyOneTemplate(param.campaignId,documentSendForm.dtoMap['documentType'])?'false':'true'}"
                              styleClass="middleSelect" tabIndex="5" readOnly="${not empty documentSendForm.dtoMap['createAgain']}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
                    <fanta:parameter field="campaignId" value="${not empty param.campaignId ? param.campaignId : 0}" />
                    <fanta:parameter field="documentType" value="${not empty documentSendForm.dtoMap['documentType'] ? documentSendForm.dtoMap['documentType'] : 0}" />
                </fanta:select>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Campaign.activity.emailGenerate.senderEmployee"/>
            </td>
            <td class="contain">
                <c:choose>
                    <c:when test="${isCampaignLight}">
                        <html:hidden property="dto(senderEmployeeId)" styleId="idSenderEmployee"/>

                        <fanta:label listName="userBaseList" module="/admin" patron="0" columnOrder="name">
                            <fanta:parameter field="userId" value="${sessionScope.user.valueMap['userId']}"/>
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:label>
                    </c:when>
                    <c:otherwise>

                        <c:set var="senderEmployees" value="${app2:getSenderEmployeeList(documentSendForm.dtoMap['activityId'], pageContext.request)}"/>
                        <html:select property="dto(senderEmployeeId)" styleId="idSenderEmployee" styleClass="middleSelect" tabindex="6">
                            <html:options collection="senderEmployees" property="value" labelProperty="label"/>
                        </html:select>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Campaign.activity.docGenerate.OrderedPrimarilyBy"/>
            </td>
            <td class="contain">
                <c:set var="orderColumns" value="${app2:getDocumentGenerationOrderBy(pageContext.request)}"/>
                <c:set var="orderTypes" value="${app2:getDocumentColumnOrderType(pageContext.request)}"/>
                <html:select property="dto(firstOrder)" styleClass="middleSelect" tabindex="7">
                    <html:option value="">&nbsp;</html:option>
                    <html:options collection="orderColumns" property="value" labelProperty="label"/>
                </html:select>
                &nbsp;
                <html:select property="dto(firstOrderType)" styleClass="shortSelect" tabindex="8">
                    <html:options collection="orderTypes" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Campaign.activity.docGenerate.OrderedSecondarilyBy"/>
            </td>
            <td class="contain">
                <html:select property="dto(secondOrder)" styleClass="middleSelect" tabindex="9">
                    <html:option value="">&nbsp;</html:option>
                    <html:options collection="orderColumns" property="value" labelProperty="label"/>
                </html:select>
                &nbsp;
                <html:select property="dto(secondOrderType)" styleClass="shortSelect" tabindex="10">
                    <html:options collection="orderTypes" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Campaign.activity.docGenerate.communicationTitle"/>
            </td>
            <td class="contain">
                <app:text property="dto(docCommTitle)" styleClass="middleText" maxlength="100" tabindex="11"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Campaign.activity.docGenerate.createCommunication"/>
            </td>
            <td class="contain">
                <html:hidden property="dto(createAgain)"/>
                <html:checkbox property="dto(createComm)" styleClass="radio" tabindex="12"/>
            </td>
        </tr>
    </table>
    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
        <tr>
            <td class="button">

                <app2:securitySubmit property="dto(generate)" operation="execute" functionality="CAMPAIGNACTIVITY"
                                     styleClass="button" tabindex="13">
                    <fmt:message key="Document.generate"/>
                </app2:securitySubmit>

                <html:cancel styleClass="button" tabindex="14">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>

</html:form>
