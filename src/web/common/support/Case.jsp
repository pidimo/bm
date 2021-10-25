<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>
<calendar:initialize/>

<script>
    function changeState() {
        document.getElementById("saveButtonId").click();
    }
</script>

<c:set var="isOpenForThisUser" value="${caseForm.dtoMap.openByUserId == sessionScope.user.valueMap.userId}"/>

<c:set var="isInternal" value="${sessionScope.user.valueMap.userType == 1}"/>
<c:set var="onlyView" value="${op == 'delete' || (op == 'update' && !isInternal)}" scope="request"/>

<c:if test="${!onlyView || isOpenForThisUser}">
    <tags:initTinyMCE4 textAreaId="description_text"/>
</c:if>


<fmt:message var="datePattern" key="datePattern"/>
<html:form action="${action}" focus="dto(caseTitle)"  enctype="multipart/form-data">
<html:hidden property="dto(op)" value="${op}" styleId="op"/>

<c:if test="${op == 'update'}">
    <html:hidden property="dto(userAssigned)"/>
    <c:if test="${caseForm.dtoMap.new_activity != null}">
        <html:hidden property="dto(new_activity)"/>
    </c:if>
    <c:if test="${caseForm.dtoMap.isClosed != null}">
        <html:hidden property="dto(isClosed)"/>
    </c:if>
    <c:if test="${caseForm.dtoMap.reopenCase != null}">
        <html:hidden property="dto(reopenCase)"/>
    </c:if>
</c:if>

<c:if test="${!isInternal}">
    <input type="hidden" name="dto(ut)" value="1"/>
</c:if>
<c:if test="${'update' == op || 'delete' == op}">
    <html:hidden property="dto(version)" styleId="1"/>
    <html:hidden property="dto(caseId)" styleId="2"/>
</c:if>

<%--${caseForm.dtoMap}
<hr>--%>

<table border="0" cellpadding="0" cellspacing="0" width="97%" align="center" class="container">
<tr>
    <td colspan="4" class="button">
        <c:if test="${isInternal || isOpenForThisUser || op == 'create'}">
        <app2:securitySubmit operation="${op}" functionality="CASE" property="dto(save)"
                             styleClass="button" styleId="saveButtonId">
            <c:out value="${button}"/>
        </app2:securitySubmit>
        </c:if>
        <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
    </td>
</tr>
<tr>
    <td colspan="4" class="title">
        <c:out value="${title}"/>
    </td>
</tr>
<c:if test="${caseForm.dtoMap.n !=null && caseForm.dtoMap.n !=''}">
    <tr>
        <td class="label"><fmt:message key="Common.number"/></td>
        <td class="contain" colspan="3">

            <html:hidden property="dto(n)" write="true"/>
        </td>
    </tr>
</c:if>
<tr>
    <td width="15%" class="label"><fmt:message key="Common.title"/></td>
    <td width="35%" class="contain">
        <app:text property="dto(caseTitle)" styleClass="middleText" maxlength="160" tabindex="1" view="${onlyView}" styleId="3"/>
    </td>
    <td class="label" width="16%"><fmt:message key="Common.openBy"/></td>
    <td class="contain" width="34%">
       <c:if test="${op == 'update'}"> 
        <html:hidden property="dto(openByUserId)" />
        </c:if>
        <c:choose>
            <c:when test="${caseForm.dtoMap.openByUserId == null}">
                <c:set var="openBy" value="${sessionScope.user.valueMap['userId']}"/>
            </c:when>
            <c:otherwise>
                <c:set var="openBy" value="${caseForm.dtoMap.openByUserId}"/>
            </c:otherwise>
        </c:choose>

        <c:if test="${op == 'create'}">
            <input type="hidden" name="dto(openByUserId)" value="${openBy}" styleId="4"/>
        </c:if>

        <fanta:label listName="userBaseList" module="/admin" patron="0" columnOrder="name">
            <fanta:parameter field="userId" value="${openBy}"/>
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:label>

    </td>
</tr>
<tr>
    <td width="15%" class="label"><fmt:message key="CaseType.title"/></td>
    <td width="35%" class="contain">
        <c:set var="supportCaseTypes" value="${app2:getSupportCaseType(pageContext.request)}"/>
        <html:select property="dto(caseTypeId)" styleClass="middleSelect" readonly="${onlyView}" styleId="5" tabindex="2">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="supportCaseTypes" property="value" labelProperty="label"/>
        </html:select>
    </td>
    <td width="15%" class="label"><fmt:message key="CaseSeverity.title"/></td>
    <td width="35%" class="contain">
        <c:set var="supportSeverityCase" value="${app2:getSupportSeverityCase(pageContext.request)}"/>
        <html:select property="dto(severityId)" styleClass="middleSelect" readonly="${onlyView}" styleId="6" tabindex="9">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="supportSeverityCase" property="value" labelProperty="label"/>
        </html:select>

    </td>
</tr>
<tr>
    <td class="label" width="16%"><fmt:message key="Product.title"/></td>
    <td class="contain" width="34%">
        <html:hidden property="dto(productId)" styleId="field_key"/>
        <html:hidden property="dto(1)" styleId="field_versionNumber"/>
        <html:hidden property="dto(2)" styleId="field_unitId"/>
        <html:hidden property="dto(3)" styleId="field_price"/>

        <app:text property="dto(productName)" styleId="field_name" styleClass="middleText" maxlength="40" tabindex="3"
                  readonly="true" view="${onlyView}" />

            <tags:selectPopup url="/products/SearchProduct.do?product=true" name="SearchProduct" titleKey="Common.search"
                              hide="${onlyView}" submitOnSelect="true" />
            <tags:clearSelectPopup keyFieldId="field_key" nameFieldId="field_name" titleKey="Common.clear"
                                   hide="${onlyView}" />
    </td>
    <td width="15%" class="label"><fmt:message key="Priority.title"/></td>
    <td width="35%" class="contain">
        <c:set var="supportPriorities" value="${app2:getSupportPriority(pageContext.request)}"/>
        <html:select property="dto(priorityId)" styleClass="middleSelect" readonly="${onlyView}" styleId="7" tabindex="10">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="supportPriorities" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>

<c:if test="${isInternal}">
    <tr>
        <td width="15%" class="label"><fmt:message key="Common.keywords"/></td>
        <td width="35%" class="contain">
            <app:text property="dto(keywords)" styleClass="middleText" maxlength="40" tabindex="4"
                      view="${onlyView}" styleId="8"/>
        </td>
        <td class="label" width="16%"><fmt:message key="Contact.title"/></td>
        <td class="contain" width="34%">
            <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
            <app:text property="dto(contact)" styleId="fieldAddressName_id" styleClass="middleText" maxlength="40"
                      tabindex="11" readonly="true"
                      view="${onlyView}"/>

                <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"
                                  hide="${onlyView}"
                                  submitOnSelect="true"/>
                <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                       titleKey="Common.clear" submitOnClear="true"
                                       hide="${onlyView}"/>
        </td>
    </tr>
    <tr>
        <td width="15%" class="label"><fmt:message key="Common.assignedTo"/></td>
        <td width="35%" class="contain">
            <fmt:message var="externalUser" key="User.externalUser"/>
            <fmt:message var="internalUser" key="User.intenalUser"/>

            <fanta:select property="dto(toUserId)" listName="userBaseList" labelProperty="name" tabIndex="5" styleId="9"
                          valueProperty="id" firstEmpty="true" styleClass="middleSelect" readOnly="${onlyView}"
                          module="/admin" value="${sessionScope.user.valueMap['userId']}" withGroups="true">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:group groupName="${externalUser}" columnDiscriminator="type" valueDiscriminator="0"/>
                <fanta:group groupName="${internalUser}" columnDiscriminator="type" valueDiscriminator="1"/>
            </fanta:select>
        </td>
        <td class="label" width="16%"><fmt:message key="ContactPerson.title"/></td>
        <td class="contain" width="34%">
            <fanta:select property="dto(contactPersonId)" tabIndex="12" listName="searchContactPersonList" styleId="10"
                          module="/contacts" firstEmpty="true" labelProperty="contactPersonName"
                          valueProperty="contactPersonId" styleClass="middleSelect" readOnly="${onlyView}">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap.companyId}"/>
                <fanta:parameter field="addressId" value="${not empty caseForm.dtoMap.addressId?caseForm.dtoMap.addressId:0}"/>
            </fanta:select>
        </td>
    </tr>

    <tr>
        <td width="15%" class="label"><fmt:message key="State.title"/></td>
        <td width="35%" class="contain">
        <c:set var="changeState" value="${caseForm.dtoMap.isClosed != null ? 'changeState()' : ''}"/>
        <c:set var="supportStatus" value="${app2:getSupportStatus(pageContext.request)}"/>
        <html:select property="dto(stateId)" styleClass="middleSelect" readonly="${onlyView}" styleId="11" tabindex="6">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="supportStatus" property="value" labelProperty="label"/>
        </html:select>



        </td>
        <td class="label" width="16%"><fmt:message key="Common.totalHours"/></td>
        <td class="contain" width="34%">
            <app:text property="dto(totalHours)" styleClass="middleText numberText" maxlength="40" tabindex="13"
                      view="${onlyView}" styleId="12"/>
        </td>
    </tr>
</c:if>
<tr>
    <td class="label" width="16%"><fmt:message key="Common.openDate"/></td>
    <td class="contain" width="34%">

        <app:dateText property="dto(openDate)" styleId="openDate" calendarPicker="${!onlyView}"
                      datePatternKey="${datePattern}" styleClass="middleText"
                      view="${onlyView}"
                      maxlength="10" tabindex="7" currentDate="true" />
    </td>
    <td width="15%" class="label"><fmt:message key="WorkLevel.title"/></td>
    <td width="35%" class="contain">
        <c:set var="supportWorkLevels" value="${app2:getSupportWorkLevel(pageContext.request)}"/>
        <html:select property="dto(workLevelId)" styleClass="middleSelect" readonly="${onlyView}" styleId="13" tabindex="14">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="supportWorkLevels" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>
<tr>
    <td class="label" width="16%"><fmt:message key="Common.expireDate"/></td>
    <td class="contain" width="34%">
        <app:dateText property="dto(expireDate)" styleId="expireDate" calendarPicker="${!onlyView}"
                      datePatternKey="${datePattern}" styleClass="middleText" view="${onlyView}"
                      maxlength="10" tabindex="8" currentDate="false" />
    </td>

    <td class="label" width="16%">
        <c:choose>
            <c:when test="${isInternal}">
                <fmt:message key="Common.closeDate"/>
            </c:when>
            <c:when test="${!isInternal && op == 'update'}">
                <fmt:message key="State.title"/>
            </c:when>
        </c:choose></td>
    <td class="contain" width="34%">
        <c:choose>
            <c:when test="${isInternal}">
                <c:catch var="failDate">
                    <fmt:formatDate value="${app2:intToDate(caseForm.dtoMap.cd)}" pattern="${datePattern}"/>
                </c:catch>
                <c:if test="${failDate!=null}">
                    ${caseForm.dtoMap.cd}
                </c:if>
                <html:hidden property="dto(cd)" styleId="14"/>
            </c:when>
            <c:when test="${!isInternal && op == 'update'}">
                <html:hidden property="dto(stn)" write="true" styleId="15"/>
            </c:when>
        </c:choose>
    </td>
</tr>
<c:if test="${caseForm.dtoMap.reopenCase != null}">
    <tr>
        <td class="topLabel" colspan="4">
            <fmt:message key="SupportCase.reopenDescription"/><br>
            <html:textarea property="dto(reopenDescription)" style="width:100%" styleClass="webmailBody" tabindex="30" styleId="16"/>
        </td>
    </tr>
</c:if>
<tr>
    <td class="topLabel" colspan="4">
        <fmt:message key="Common.detail"/><br>
        <c:choose>
            <c:when test="${(!onlyView || isOpenForThisUser ) && op != 'delete'}">
                <html:textarea property="dto(caseDescription)" style="width:100%" styleClass="webmailBody" tabindex="25"
                               styleId="description_text" />
            </c:when>
            <c:otherwise>
                <c:set var="cd" value="${caseForm.dtoMap.caseDescription}" scope="session"/>
                <iframe name="frame2" src="<c:url value="/common/support/PreviusDetail.jsp?var=cd" />"
                        style="width : 100%;height: 240px;background-color:#ffffff" scrolling="yes" frameborder="1">
                </iframe>
            </c:otherwise>
        </c:choose>
    </td>
</tr>
<tr>
    <td colspan="4" class="button">
        <c:if test="${isInternal || isOpenForThisUser || op == 'create'}">
        <app2:securitySubmit operation="${op}" functionality="CASE" property="dto(save)"
                             styleClass="button" styleId="saveButtonId">
            <c:out value="${button}"/>
        </app2:securitySubmit>
        </c:if>
        <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
    </td>
</tr>
</table>
</html:form>