<%@ include file="/Includes.jsp" %>
<table cellpadding="0" cellspacing="0" border="0"  align="center" width="50%" >
<tr>
    <td>
    <html:form action="/Support/State/Translate/Save.do">

        <html:hidden property="dto(op)" value="update"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(stateId)" value="${stateTranslateForm.dtoMap['stateId']}"/>
        <html:hidden property="dto(langTextId)" value="${stateTranslateForm.dtoMap['langTextId']}"/>
        <html:hidden property="dto(stateName)" value="${stateTranslateForm.dtoMap['stateName']}"/>

        <c:set var="disableChecks" value=""/>
        <c:if test="${stateTranslateForm.dtoMap['haveDefaultTranslation'] == true}">
            <c:set var="disableChecks" value="disabled"/>
        </c:if>


        <table cellpadding="0" cellspacing="0" border="0" class="container" width="100%" align="center">
        <tr>
            <td colspan="3" class="title"><fmt:message  key="State.Title.translate"/></td>
        </tr>
        <tr>
            <td class="label"><fmt:message key="State.translation"/></td>
            <td class="label"><fmt:message key="State.language"/></td>
            <td class="label"><fmt:message key="State.defaultTranslation"/></td>
        </tr>
        <c:forEach var="langText" items="${stateTranslateForm.dtoMap['translatedSystemLanguages']}" varStatus="counter" >
        <tr>
            <td class="contain" width="40%" >

                <c:if test="${langText.text == null || ' ' == langtext.text}">
                    <app:text property="dto(text${counter.count})"  styleClass="text" />
                </c:if>
                <c:if test="${langText.text != null}">
                    <app:text property="dto(text${counter.count})" value="${langText.text}" styleClass="text" />
                </c:if>


            </td>
            <td class="contain" width="40%" >
                <html:hidden property="dto(languageId${counter.count})" value="${langText.languageId}"/>
                <app:text property="dto(languageName${counter.count})" value="${langText.languageName}" readonly="true" styleClass="text" />
            </td>
            <td class="contain" width="20%">
                <c:set var="isChecked" value=""/>
                <c:if test="${langText.isDefault == true}">
                    <c:set var="isChecked" value="checked"/>
                </c:if>

                <c:if test="${'checked' == isChecked}">
                    <html:hidden property="dto(isDefault)" value="${langText.languageId}_${counter.count}"/>
                </c:if>

                <input class="radio" type="radio" name="dto(isDefault)" value="${langText.languageId}_${counter.count}" ${disableChecks} ${isChecked}>


                <%--<html:radio property="dto(isDefaultId${counter.count})" value="${langText.isDefault}" disabled="${dto.haveDefaultTranslation}" />--%>
            </td>
        </tr>
        <c:set var="translations" value="${counter.count}"/>
        </c:forEach>
        <tr>
            <td colspan="3" class="button">
                <html:hidden property="dto(numberOfTranslations)" value="${translations}"/>
                <html:submit styleClass="button"><fmt:message key="Common.save"/></html:submit>
                <html:cancel styleClass="button" ><fmt:message   key="Common.cancel"/></html:cancel>
            </td>
        </tr>
        </table>
    </html:form>
    </td>
</tr>
</table>