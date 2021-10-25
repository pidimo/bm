<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ include file="/Includes.jsp" %>

<div class="${app2:getFormClasses()} form-horizontal">
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:button property="" styleClass="${app2:getFormButtonClasses()}"
                     onclick="location.href='${urlGenerateEmailReturn}'">
            ${button}
        </html:button>
    </div>

    <div class="${app2:getFormPanelClasses()}">
        <legend class="title">
            <fmt:message key="Campaign.activity.emailSend.summary.title"/>
        </legend>

        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelClasses()}">
                <fmt:message key="Campaign.activity.emailGenerate.template"/>
            </label>

            <div class="${app2:getFormContainClasses(true)}">
                <fanta:label listName="templateList" module="/campaign" patron="0"
                             label="description" columnOrder="description">
                    <fanta:parameter field="templateId" value="${templateId}"/>
                </fanta:label>
            </div>
        </div>

        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelClasses()}">
                <fmt:message key="Campaign.emailSend.summary.totalRecipient"/>
            </label>

            <div class="${app2:getFormContainClasses(true)}">
                <c:out value="${totalRecipients}"/>
            </div>
        </div>

        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelClasses()}">
                <fmt:message key="Campaign.emailSend.summary.totalSent"/>
            </label>

            <div class="${app2:getFormContainClasses(true)}">
                <c:out value="${totalSuccess}"/>
            </div>
        </div>

        <c:if test="${totalFail > 0}">
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Campaign.emailSend.summary.totalNotSent"/>
                </label>

                <div class="${app2:getFormContainClasses(true)}">
                    <c:out value="${totalFail}"/>
                </div>
            </div>
        </c:if>

        <legend class="title">
            <fmt:message key="Campaign.emailSend.summary.mailSentByLanguage"/>
        </legend>

        <c:forEach var="summary" items="${summaryList}">
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <c:out value="${summary.label}"/>
                </label>

                <div class="${app2:getFormContainClasses(true)}">
                    <c:out value="${summary.value}"/>
                </div>
            </div>
        </c:forEach>

        <c:if test="${not empty withoutMailList}">
            <legend class="title">
                <fmt:message key="Campaign.emailSend.summary.contactWithoutEmail"/>
            </legend>

            <c:set var="AWITHOUTMAIL" value="<%=CampaignConstants.ADDRESS_WITHOUT_MAIL%>"/>
            <c:set var="CPWITHOUTMAIL" value="<%=CampaignConstants.CONTACTPERSON_WITHOUT_MAIL%>"/>
            <c:forEach var="recipient" items="${withoutMailList}">
                <div class="${app2:getFormGroupClasses()}">
                    <label class="col-xs-12 col-sm-6">
                        <c:out value="${recipient.contactName}"/>
                        <c:if test="${recipient.addressWithoutMail eq AWITHOUTMAIL}">
                            <img src='<c:out value="${sessionScope.baselayout}"/>/img/close.gif' alt=""
                                 title="<fmt:message key="Campaign.emailSend.summary.mailNotFound"/>"/>
                        </c:if>
                    </label>

                    <div class="parentElementInputSearch col-xs-11 col-sm-6 ">
                        <c:out value="${recipient.contactPersonName}"/>
                        <c:if test="${recipient.addressWithoutMail eq CPWITHOUTMAIL}">
                            <img src='<c:out value="${sessionScope.baselayout}"/>/img/close.gif' alt=""
                                 title="<fmt:message key="Campaign.emailSend.summary.mailNotFound"/>"/>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </c:if>

        <c:if test="${not empty failSentList}">
            <legend class="title">
                <fmt:message key="Campaign.emailSend.summary.recipientsWithUnexpectedError"/>
            </legend>

            <c:forEach var="recipient" items="${failSentList}">
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <c:out value="${recipient.contactName}"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                        <c:out value="${recipient.contactPersonName}"/>
                    </div>
                </div>
            </c:forEach>
        </c:if>

    </div>

    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:button property="" styleClass="${app2:getFormButtonClasses()}"
                     onclick="location.href='${urlGenerateEmailReturn}'">
            ${button}
        </html:button>
    </div>
</div>