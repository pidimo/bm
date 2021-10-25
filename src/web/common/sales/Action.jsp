<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ page import="com.piramide.elwis.web.contactmanager.form.CommunicationFieldValidatorUtil" %>
<%@ include file="/Includes.jsp" %>

<%--
createSaleLink : Link for create Sale Button, to setting this link use :
            <c:set> tag with scope="request" property
--%>

<c:if test="${empty createSaleLink}">
    <c:set var="createSaleLink" value="/sales/SalesProcess/Sale/Forward/Create.do" scope="request"/>
</c:if>

<c:if test="${empty tabName}">
    <c:set var="tabName" value="SalesProcess.tab.sale" scope="request"/>
</c:if>

<c:if test="${empty createTaskLink}">
    <c:set var="createTaskLink" value="/sales/Task/Forward/Create.do" scope="request"/>
</c:if>

<c:if test="${empty taskTabName}">
    <c:set var="taskTabName" value="Scheduler.Tasks" scope="request"/>
</c:if>

<c:if test="${empty createActionLink}">
    <c:set var="createActionLink" value="/sales/SalesProcess/Action/Forward/Create.do" scope="request"/>
</c:if>
<c:if test="${empty actionTabName}">
    <c:set var="actionTabName" value="SalesProcess.Tab.detail" scope="request"/>
</c:if>

<tags:initSelectPopup/>
<calendar:initialize/>

<app2:jScriptUrl
        url="${createSaleLink}?processId=${actionForm.dtoMap['processId']}&dto(contactId)=${actionForm.dtoMap['contactId']}&dto(processId)=${actionForm.dtoMap['processId']}&tabKey=${tabName}"
        var="createSaleUrl"/>

<app2:jScriptUrl
        url="${createTaskLink}?processId=${actionForm.dtoMap['processId']}&dto(processId)=${actionForm.dtoMap['processId']}&addressName=${app2:encode(addressName)}&processName=${app2:encode(processName)}&dto(processName)=${app2:encode(actionForm.dtoMap['processName'])}&tabKey=${taskTabName}"
        var="newTaskForwardUrl"/>

<app2:jScriptUrl
        url="${createActionLink}?processId=${actionForm.dtoMap['processId']}&dto(processId)=${actionForm.dtoMap['processId']}&processName=${app2:encode(processName)}&dto(processName)=${app2:encode(actionForm.dtoMap['processName'])}&tabKey=${actionTabName}"
        var="newActionForwardUrl"/>

<br/>

<script language="JavaScript" type="text/javascript">
    function goToForward(id) {
        selectBox = document.getElementById(id);

        optionObject = selectBox.options[selectBox.selectedIndex];

        if (null != optionObject) {
            if ('1' == optionObject.value) {
                location.href = ${newTaskForwardUrl};
            }

            if ('2' == optionObject.value) {
                location.href = ${newActionForwardUrl};
            }
        }
    }


    function createSale() {
        location.href = ${createSaleUrl};
    }
    function jumpForm(obj) {
        if (obj.options[obj.selectedIndex].value != "") {
            document.forms[0].submit();
        }
    }
    function changeCommunicationType(obj) {
        document.getElementById("changeCommunicationTypeId").value = "true";
        document.forms[0].submit();
    }
    function changeActionType(obj) {
        document.getElementById("changeActionTypeId").value = "true";
        document.forms[0].submit();
    }
</script>

<tags:jscript language="JavaScript" src="/js/webmail/compose.jsp"/>

<c:set var="saveButton" value="<%=CommunicationFieldValidatorUtil.FormButtonProperties.Save.getKey()%>" scope="page"/>
<c:set var="generateButton" value="<%=CommunicationFieldValidatorUtil.FormButtonProperties.Generate.getKey()%>"
       scope="page"/>
<c:set var="sendButton" value="<%=CommunicationFieldValidatorUtil.FormButtonProperties.Send.getKey()%>" scope="page"/>

<c:set var="phone" value="<%= com.piramide.elwis.utils.CommunicationTypes.PHONE %>" scope="page"/>
<c:set var="meeting" value="<%= com.piramide.elwis.utils.CommunicationTypes.MEETING %>" scope="page"/>
<c:set var="fax" value="<%= com.piramide.elwis.utils.CommunicationTypes.FAX %>" scope="page"/>
<c:set var="letter" value="<%= com.piramide.elwis.utils.CommunicationTypes.LETTER %>" scope="page"/>
<c:set var="other" value="<%= com.piramide.elwis.utils.CommunicationTypes.OTHER %>" scope="page"/>
<c:set var="email" value="<%= com.piramide.elwis.utils.CommunicationTypes.EMAIL%>" scope="page"/>
<c:set var="document" value="<%= com.piramide.elwis.utils.CommunicationTypes.DOCUMENT%>" scope="page"/>

<c:set var="BODY_TYPE_HTML" value="<%=com.piramide.elwis.utils.WebMailConstants.BODY_TYPE_HTML%>"/>
<c:set var="BODY_TYPE_TEXT" value="<%=com.piramide.elwis.utils.WebMailConstants.BODY_TYPE_TEXT%>"/>

<fmt:message var="datePattern" key="datePattern"/>
<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>

<c:set var="forwardOptions" value="${app2:getActionForwardSelectOptions(pageContext.request)}"/>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>


<html:form action="${action}" focus="dto(type)" enctype="multipart/form-data" styleId="composeEmailId">

<html:hidden property="dto(changeCommunicationType)" value="false" styleId="changeCommunicationTypeId"/>
<html:hidden property="dto(changeActionType)" value="false" styleId="changeActionTypeId"/>

<html:hidden property="dto(op)" value="${op}" styleId="op"/>
<html:hidden property="dto(processId)"/>
<html:hidden property="dto(isAction)" value="true"/>
<html:hidden property="dto(addressId)" value="${isSalesProcess ? param.addressId : param.contactId}" styleId="delta"/>
<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
<html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>

<c:if test="${op == 'create'}">
    <html:hidden property="dto(status)" value="0"/>
    <html:hidden property="dto(active)" value="true"/>
</c:if>

<c:if test="${'update' == op || 'delete' == op}">
    <html:hidden property="dto(showNetGrossWarningMessage)" styleId="showNetGrossWarningMessageId"/>
    <html:hidden property="dto(contactId)"/>
    <html:hidden property="dto(version)"/>
    <html:hidden property="dto(status)"/>

</c:if>

<table id="Action.jsp" border="0" cellpadding="0" cellspacing="0" width="800" align="center" class="container">
<TR>
    <TD colspan="4" class="button">
        <c:if test="${'update' == op && null != forwardOptions}">
            <html:select property="dto(forwardOption)"
                         styleClass="select"
                         styleId="topForwardOptionSelect">
                <html:options collection="forwardOptions"
                              property="value"
                              labelProperty="label"/>
            </html:select>
            <html:button property=""
                         onclick="goToForward('topForwardOptionSelect')"
                         styleClass="button">
                <fmt:message key="Common.go"/>
            </html:button>
        </c:if>
        <c:choose>
            <c:when test="${actionForm.dtoMap['type'] == letter || actionForm.dtoMap['type'] == fax}">

                <app2:securitySubmit property="${saveButton}" operation="${op}" functionality="SALESPROCESSACTION"
                                     styleClass="button" styleId="saveButtonId">
                    <c:out value="${button}"/>
                </app2:securitySubmit>

                <c:if test="${op != 'delete'}">
                    <app2:checkAccessRight functionality="SALESPROCESSACTION" permission="EXECUTE">
                        <html:submit property="${generateButton}" styleClass="button">
                            <fmt:message key="Document.generate"/>
                        </html:submit>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="SALESPROCESSACTION" permission="VIEW">
                        <c:set var="docInfo"
                               value="${app2:getCommunicationDocumentInfo(actionForm.dtoMap['contactId'])}"/>
                        <c:if test="${not empty docInfo.freeTextId}">
                            <app:url var="openDocUrl"
                                     value="contacts/Download.do?dto(type)=comm&dto(fid)=${docInfo.freeTextId}"
                                     contextRelative="true"/>
                            <html:button property="" styleClass="button" onclick="location.href='${openDocUrl}'">
                                <fmt:message key="Document.open"/>
                            </html:button>
                        </c:if>
                    </app2:checkAccessRight>
                </c:if>
            </c:when>
            <c:when test="${actionForm.dtoMap['type'] == email}">
                <c:choose>
                    <c:when test="${'create' == op}">
                        <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                            <app2:securitySubmit operation="${op}" functionality="SALESPROCESSACTION"
                                                 property="${sendButton}"
                                                 styleClass="button">
                                <fmt:message key="Common.send"/>
                            </app2:securitySubmit>
                        </app2:checkAccessRight>
                    </c:when>
                    <c:when test="${null != op1}">
                        &nbsp;
                    </c:when>
                    <c:otherwise>
                        <app2:securitySubmit property="${saveButton}" operation="${op}"
                                             functionality="SALESPROCESSACTION"
                                             styleClass="button" styleId="saveButtonId">
                            <c:out value="${button}"/>
                        </app2:securitySubmit>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <app2:securitySubmit property="${saveButton}" operation="${op}"
                                     functionality="SALESPROCESSACTION"
                                     styleClass="button" styleId="saveButtonId">
                    <c:out value="${button}"/>
                </app2:securitySubmit>
            </c:otherwise>
        </c:choose>

        <app2:checkAccessRight functionality="SALE" permission="CREATE">
            <c:if test="${'update' == op && 100 == actionForm.dtoMap['probability'] && actionForm.dtoMap['type'] != email}">
                <html:button property="createSaleButton" styleClass="button" onclick="javascript:createSale();">
                    <fmt:message key="SalessProcess.createSale"/>
                </html:button>
            </c:if>
        </app2:checkAccessRight>

        <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
    </TD>
</TR>

<TR>
    <TD colspan="4" class="title">
        <c:out value="${title}"/>
    </TD>
</TR>

<TR>
    <TD class="label" width="15%">
        <fmt:message key="Communication.type"/>
    </TD>
    <TD class="contain" width="35%">
        <c:set var="communicationTypes" value="${app2:defaultMediaTypes(pageContext.request)}"/>
        <html:select property="dto(type)"
                     styleClass="mediumSelect"
                     onchange="changeCommunicationType(this)"
                     readonly="${op != 'create'}"
                     tabindex="1">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="communicationTypes" property="value" labelProperty="label"/>
        </html:select>
    </TD>
    <TD class="label" width="15%">
        <fmt:message key="SalesProcessAction.actionType"/>
    </TD>
    <TD class="contain" width="35%">
        <fanta:select property="dto(actionTypeId)"
                      listName="actionTypeBaseList"
                      labelProperty="name"
                      valueProperty="id"
                      styleClass="mediumSelect"
                      onChange="javascript:changeActionType(this);"
                      readOnly="${op == 'delete'}"
                      module="/sales"
                      firstEmpty="true"
                      tabIndex="12">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
</TR>

<c:if test="${email != actionForm.dtoMap['type']}">
<TR>
    <TD class="label">
        <fmt:message key="Document.subject"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(note)"
                  styleClass="mediumText"
                  maxlength="40"
                  tabindex="2"
                  view="${op == 'delete'}"/>
    </TD>
    <TD class="topLabel">
        <fmt:message key="SalesProcessAction.number"/>
    </TD>
    <TD class="containTop">
        <app:text property="dto(number)"
                  styleClass="mediumText"
                  maxlength="30"
                  tabindex="13"
                  view="${'delete' == op}"/>
    </TD>
</TR>

<TR>
    <TD class="label">
        <fmt:message key="Document.contactPerson"/>
    </TD>
    <TD class="contain">
        <html:hidden property="dto(contactPersonId)" styleId="fieldContactPersonId_id"/>
        <app:text property="dto(contactPersonName)"
                  styleClass="mediumText" view="${op == 'delete'}"
                  readonly="true"
                  styleId="fieldContactPersonName_id"
                  tabindex="3"/>
        <tags:selectPopup
                url="/contacts/ContactPerson/Search.do?contactId=${isSalesProcess ? param.addressId : param.contactId}"
                name="searchContactPerson"
                titleKey="Common.search" hide="${op == 'delete'}"
                submitOnSelect="${actionForm.dtoMap['type'] == email}"
                tabindex="4"/>
        <tags:clearSelectPopup keyFieldId="fieldContactPersonId_id"
                               nameFieldId="fieldContactPersonName_id"
                               titleKey="Common.clear"
                               hide="${op == 'delete'}"
                               submitOnClear="${actionForm.dtoMap['type']==email}"
                               tabindex="5"/>
    </TD>

    <TD class="label">
        <fmt:message key="SalesProcess.probability"/>
    </TD>
    <TD class="contain"
        <c:if test="${actionForm.dtoMap['type'] == null || actionForm.dtoMap['type'] == ''}">colspan="3"</c:if>>
        <c:if test="${op == 'create'}">
            <c:set var="probabilityValue" value="${isSalesProcess ? (probability) : (param.probability) }"/>
        </c:if>
        <c:set var="probabilities" value="${app2:defaultProbabilities()}"/>
        <html:select property="dto(probability)"
                     styleClass="shortSelect"
                     readonly="${op == 'delete'}"
                     tabindex="14"
                     value="${(op == 'create' && actionForm.dtoMap['probability'] == null) ? (probabilityValue) : (actionForm.dtoMap['probability'])}">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="probabilities" property="value" labelProperty="label"/>
        </html:select>
        <fmt:message key="Common.probabilitySymbol"/>
    </TD>
</TR>

<TR>
    <TD class="label">
        <fmt:message key="Document.date"/>
    </TD>
    <TD class="contain">
        <fmt:message var="datePattern" key="datePattern"/>
        <app:dateText property="dto(dateStart)" styleId="dateStart"
                      calendarPicker="${op != 'delete' && actionForm.dtoMap['type']!=email}"
                      datePatternKey="${datePattern}"
                      styleClass="mediumText" view="${op == 'delete'}"
                      maxlength="10"
                      tabindex="6"
                      currentDate="true"
                      readonly="${actionForm.dtoMap['type'] == email}"/>
    </TD>

    <TD class="topLabel">
        <fmt:message key="Common.active"/>
    </TD>
    <TD class="containTop">
        <html:checkbox property="dto(active)"
                       disabled="${op == 'delete'}"
                       styleClass="radio"
                       value="true"
                       tabindex="15"/>
    </TD>
</TR>

<tr>
    <td class="label">
        <fmt:message key="SalesProcessAction.netGross"/>
    </td>
    <td class="contain">
        <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
        <html:select property="dto(netGross)"
                     styleClass="mediumSelect"
                     readonly="${'delete' == op}"
                     tabindex="7">
            <html:option value=""/>
            <html:options collection="netGrossOptions" property="value" labelProperty="label"/>
        </html:select>
    </td>

    <TD class="topLabel">
        <fmt:message key="SalesProcess.employee"/>
    </TD>
    <TD class="containTop">
        <fanta:select property="dto(employeeId)"
                      listName="employeeBaseList"
                      labelProperty="employeeName"
                      valueProperty="employeeId"
                      styleClass="mediumSelect"
                      readOnly="${op == 'delete'|| actionForm.dtoMap['type']==email}"
                      module="/contacts"
                      tabIndex="16"
                      value="${sessionScope.user.valueMap['userAddressId']}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
</tr>

<tr>
    <TD class="label">
        <fmt:message key="SalesProcessAction.currency"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(currencyId)"
                      listName="basicCurrencyList"
                      labelProperty="name"
                      valueProperty="id"
                      firstEmpty="true"
                      styleClass="mediumSelect"
                      readOnly="${'delete' == op}"
                      module="/catalogs"
                      tabIndex="8">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>

    <td class="label">
        <fmt:message key="Action.createDateTime"/>
    </td>
    <td class="contain">
        <c:if test="${'create' != op && null != actionForm.dtoMap['createDateTime']}">
            <html:hidden property="dto(createDateTime)"/>
            <c:out value="${app2:getDateWithTimeZone(actionForm.dtoMap['createDateTime'], currentDateTimeZone, dateTimePattern)}"/>
        </c:if>
    </td>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="SalesProcess.value"/>
    </TD>
    <TD class="contain">
        <app:numberText property="dto(value)"
                        styleClass="numberText"
                        maxlength="18"
                        view="${'delete' == op}"
                        numberType="decimal"
                        maxInt="12"
                        maxFloat="2"
                        tabindex="9"/>
        <c:if test="${op == 'update'}">
            <html:submit property="calculate" value=" " styleClass="calculatorButton"
                         titleKey="ActionPosition.calculate" tabindex="11"/>
        </c:if>
    </TD>

    <td class="label">
        <fmt:message key="Action.creatorUser"/>
    </td>
    <td class="contain">
        <c:if test="${'create' == op}">
            <c:out value="${app2:getAddressName(sessionScope.user.valueMap['userAddressId'])}"/>
        </c:if>
        <c:if test="${'update' == op || 'delete' == op}">
            <app:text property="dto(userName)"
                      styleClass="mediumText"
                      maxlength="20"
                      readonly="true"
                      tabindex="17"
                      view="true"/>
        </c:if>
    </td>
</tr>

<c:choose>
    <c:when test="${actionForm.dtoMap['type'] == fax || actionForm.dtoMap['type'] == letter}">

        <tr>
            <TD class="topLabel">
                <fmt:message key="Document.template"/>
            </TD>
            <TD class="containTop">
                <html:hidden property="dto(rebuildDocument)"/>
                <!--this field is as flag, set in true when the document (fax,letter) should be regererated-->

                <c:set var="mediatype_WORD" value="<%=CatalogConstants.MediaType.WORD.getConstantAsString()%>"/>
                <fanta:select property="dto(templateId)" styleId="templateId" listName="templateList"
                              labelProperty="description" valueProperty="id" module="/catalogs"
                              firstEmpty="true" styleClass="mediumSelect" readOnly="${'delete' == op}" tabIndex="10">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="mediaType" value="${mediatype_WORD}"/>
                </fanta:select>

            </td>

            <td class="label">
                <fmt:message key="Communication.additionalAddress"/>
            </td>
            <td class="contain">
                <fanta:select property="dto(additionalAddressId)" listName="additionalAddressSelectList"
                              labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                              preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                              firstEmpty="true" styleClass="mediumSelect" readOnly="${'delete' == op}" tabIndex="17">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="addressId" value="${isSalesProcess ? param.addressId : param.contactId}"/>
                </fanta:select>
            </td>
        </tr>

        <c:if test="${'update' == op}">
            <tr>
                <td class="label">
                    <fmt:message key="Action.updateDateTime"/>
                </td>
                <td class="contain" colspan="3">
                    <c:if test="${null != actionForm.dtoMap['updateDateTime']}">
                        <html:hidden property="dto(updateDateTime)"/>
                        <c:out value="${app2:getDateWithTimeZone(actionForm.dtoMap['updateDateTime'], currentDateTimeZone, dateTimePattern)}"/>
                    </c:if>
                </td>
            </tr>
        </c:if>

    </c:when>
    <c:when test="${phone == actionForm.dtoMap['type'] || meeting == actionForm.dtoMap['type'] || other == actionForm.dtoMap['type']}">

        <c:set var="colspanRow" value="3"/>
        <c:if test="${'update' == op}">
            <c:set var="colspanRow" value=""/>
        </c:if>

        <tr>
            <td class="label">
                <fmt:message key="Communication.additionalAddress"/>
            </td>
            <td class="contain" colspan="${colspanRow}">
                <fanta:select property="dto(additionalAddressId)" listName="additionalAddressSelectList"
                              labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                              preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                              firstEmpty="true" styleClass="mediumSelect" readOnly="${'delete' == op}" tabIndex="9">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="addressId" value="${isSalesProcess ? param.addressId : param.contactId}"/>
                </fanta:select>
            </td>

            <c:if test="${'update' == op}">
                <td class="label">
                    <fmt:message key="Action.updateDateTime"/>
                </td>
                <td class="contain">
                    <c:if test="${null != actionForm.dtoMap['updateDateTime']}">
                        <html:hidden property="dto(updateDateTime)"/>
                        <c:out value="${app2:getDateWithTimeZone(actionForm.dtoMap['updateDateTime'], currentDateTimeZone, dateTimePattern)}"/>
                    </c:if>
                </td>
            </c:if>
        </tr>

        <tr>
            <td class="topLabel" colspan="4">
                <fmt:message key="Document.content"/>
                <br>
                <html:textarea property="dto(freeText)"
                               styleClass="mediumDetail"
                               style="width:99%;"
                               readonly="${op == 'delete'}"
                               tabindex="19"/>
            </TD>
        </tr>
    </c:when>
    <c:when test="${document == actionForm.dtoMap['type']}">

        <TR>
            <TD class="label">
                <fmt:message key="Communication.document"/>
            </TD>
            <TD class="contain">
                <fmt:message key="Common.download" var="downloadMsg"/>
                <c:if test="${'create' == op || 'update' ==  op}">
                    <html:file property="dto(documentFile)" tabindex="10"/>&nbsp;
                </c:if>
                <c:if test="${'update' == op}">
                    <html:hidden property="dto(documentFileName)"/>
                    <app:link
                            action="/SalesProcess/Action/Document/Download.do?communicationId=${actionForm.dtoMap['contactId']}&processId=${actionForm.dtoMap['processId']}&dto(processId)=${actionForm.dtoMap['processId']}"
                            title="${downloadMsg}"
                            addModuleParams="true">
                        <img src="<c:out value="${sessionScope.baselayout}"/>/img/openfile.png"
                             alt="${downloadMsg}" border="0" align="middle"/>
                    </app:link>
                </c:if>
                <c:if test="${'delete' == op}">
                    <c:out value="${actionForm.dtoMap['documentFileName']}"/>
                </c:if>
            </TD>

            <td class="label">
                <fmt:message key="Communication.additionalAddress"/>
            </td>
            <td class="contain">
                <fanta:select property="dto(additionalAddressId)" listName="additionalAddressSelectList"
                              labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                              preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                              firstEmpty="true" styleClass="mediumSelect" readOnly="${'delete' == op}" tabIndex="17">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="addressId" value="${isSalesProcess ? param.addressId : param.contactId}"/>
                </fanta:select>
            </td>
        </TR>

        <c:if test="${'update' == op}">
            <tr>
                <td class="label">
                    <fmt:message key="Action.updateDateTime"/>
                </td>
                <td class="contain" colspan="3">
                    <c:if test="${null != actionForm.dtoMap['updateDateTime']}">
                        <html:hidden property="dto(updateDateTime)"/>
                        <c:out value="${app2:getDateWithTimeZone(actionForm.dtoMap['updateDateTime'], currentDateTimeZone, dateTimePattern)}"/>
                    </c:if>
                </td>
            </tr>
        </c:if>

    </c:when>
    <c:otherwise>
        &nbsp;
    </c:otherwise>
</c:choose>
</c:if>

<c:if test="${email == actionForm.dtoMap['type']}">
    <tr>
        <TD class="label">
            <fmt:message key="Document.date"/>
        </TD>
        <TD class="contain">
            <fmt:message var="datePattern" key="datePattern"/>
            <app:dateText property="dto(dateStart)" styleId="dateStart"
                          calendarPicker="false"
                          datePatternKey="${datePattern}"
                          styleClass="mediumText"
                          view="${op == 'delete'}"
                          maxlength="10"
                          tabindex="2"
                          currentDate="true"
                          readonly="true"/>
        </TD>
        <TD class="label">
            <fmt:message key="SalesProcessAction.number"/>
        </TD>
        <TD class="contain">
            <app:text property="dto(number)"
                      styleClass="mediumText"
                      maxlength="30"
                      tabindex="13"
                      view="${'delete' == op}"/>
        </TD>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="SalesProcessAction.netGross"/>
        </td>
        <td class="contain">
            <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
            <html:select property="dto(netGross)"
                         styleClass="mediumSelect"
                         readonly="${'delete' == op}"
                         tabindex="3">
                <html:option value=""/>
                <html:options collection="netGrossOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>

        <TD class="topLabel">
            <fmt:message key="SalesProcess.probability"/>
        </TD>
        <TD class="containTop">
            <c:set var="probabilities" value="${app2:defaultProbabilities()}"/>
            <c:if test="${op == 'create'}">
                <c:set var="probabilityValue" value="${isSalesProcess ? (probability) : (param.probability)}"/>
            </c:if>
            <html:select property="dto(probability)"
                         styleClass="shortSelect"
                         readonly="${op == 'delete'}"
                         tabindex="14"
                         value="${(op == 'create' && actionForm.dtoMap['probability'] == null) ? (probabilityValue) : (actionForm.dtoMap['probability'])}">
                <html:option value="">&nbsp;</html:option>
                <html:options collection="probabilities" property="value" labelProperty="label"/>
            </html:select>
            <fmt:message key="Common.probabilitySymbol"/>
        </TD>

    </tr>
    <tr>
        <TD class="label">
            <fmt:message key="SalesProcessAction.currency"/>
        </TD>
        <TD class="contain">
            <fanta:select property="dto(currencyId)"
                          listName="basicCurrencyList"
                          labelProperty="name"
                          valueProperty="id"
                          firstEmpty="true"
                          styleClass="mediumSelect"
                          readOnly="${'delete' == op}"
                          module="/catalogs"
                          tabIndex="4">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </TD>

        <TD class="topLabel">
            <fmt:message key="Common.active"/>
        </TD>
        <TD class="containTop">
            <html:checkbox property="dto(active)"
                           disabled="${op == 'delete'}"
                           tabindex="15"
                           styleClass="radio"
                           value="true"/>
        </TD>
    </tr>
    <tr>
        <TD class="topLabel">
            <fmt:message key="SalesProcess.value"/>
        </TD>
        <TD class="containTop">
            <app:numberText property="dto(value)"
                            styleClass="numberText"
                            maxlength="18"
                            view="${'delete' == op}"
                            numberType="decimal"
                            maxInt="12"
                            maxFloat="2"
                            tabindex="5"/>
            <c:if test="${op == 'update'}">
                <html:submit property="calculate" value=" " styleClass="calculatorButton"
                             titleKey="ActionPosition.calculate" tabindex="6"/>
            </c:if>
        </TD>

        <TD class="topLabel">
            <fmt:message key="SalesProcess.employee"/>
        </TD>
        <TD class="containTop">
            <fanta:select property="dto(employeeId)"
                          listName="employeeBaseList"
                          labelProperty="employeeName"
                          valueProperty="employeeId"
                          styleClass="mediumSelect"
                          readOnly="${op == 'delete'}"
                          module="/contacts"
                          tabIndex="16"
                          value="${sessionScope.user.valueMap['userAddressId']}">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </TD>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Action.createDateTime"/>
        </td>
        <td class="contain">
            <c:if test="${'create' != op && null != actionForm.dtoMap['createDateTime']}">
                <html:hidden property="dto(createDateTime)"/>
                <c:out value="${app2:getDateWithTimeZone(actionForm.dtoMap['createDateTime'], currentDateTimeZone, dateTimePattern)}"/>
            </c:if>
        </td>
        <td class="label">
            <fmt:message key="Action.creatorUser"/>
        </td>
        <td class="contain">
            <c:if test="${'create' == op}">
                <c:out value="${app2:getAddressName(sessionScope.user.valueMap['userAddressId'])}"/>
            </c:if>
            <c:if test="${'update' == op || 'delete' == op}">
                <app:text property="dto(userName)"
                          styleClass="mediumText"
                          maxlength="20"
                          readonly="true"
                          tabindex="17"
                          view="true"/>
            </c:if>
        </td>
    </tr>
    <c:if test="${'update' == op}">
        <tr>
            <td class="label">
                <fmt:message key="Action.updateDateTime"/>
            </td>
            <td class="contain" colspan="3">
                <c:if test="${null != actionForm.dtoMap['updateDateTime']}">
                    <html:hidden property="dto(updateDateTime)"/>
                    <c:out value="${app2:getDateWithTimeZone(actionForm.dtoMap['updateDateTime'], currentDateTimeZone, dateTimePattern)}"/>
                </c:if>
            </td>
        </tr>
    </c:if>
    <tr>
        <td colspan="4">
            <table border="0" width="100%" cellpadding="0" cellspacing="0">
                <c:set var="mainCommunicationForm" value="${actionForm}" scope="request"/>

                <c:set var="labelWidthAttach" value="15%" scope="request"/>
                <c:set var="containWidthAttach" value="85%" scope="request"/>
                <c:import url="/webmail/ComposeEmailCommunication.jsp"/>
            </table>
        </td>
    </tr>
</c:if>

<TR>
    <TD colspan="4" class="button">
        <c:if test="${'update' == op && null != forwardOptions}">
            <html:select property="dto(forwardOption)"
                         styleClass="select"
                         styleId="bottomForwardOptionSelect">
                <html:options collection="forwardOptions"
                              property="value"
                              labelProperty="label"/>
            </html:select>
            <html:button property=""
                         onclick="goToForward('bottomForwardOptionSelect')"
                         styleClass="button">
                <fmt:message key="Common.go"/>
            </html:button>
        </c:if>
        <c:choose>
            <c:when test="${actionForm.dtoMap['type'] == letter || actionForm.dtoMap['type'] == fax}">

                <app2:securitySubmit property="${saveButton}" operation="${op}" functionality="SALESPROCESSACTION"
                                     styleClass="button" styleId="saveButtonId" tabindex="90">
                    <c:out value="${button}"/>
                </app2:securitySubmit>

                <c:if test="${op != 'delete'}">
                    <app2:checkAccessRight functionality="SALESPROCESSACTION" permission="EXECUTE">
                        <html:submit property="${generateButton}"
                                     styleClass="button" tabindex="90">
                            <fmt:message key="Document.generate"/>
                        </html:submit>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="SALESPROCESSACTION" permission="VIEW">
                        <c:if test="${not empty docInfo.freeTextId}">
                            <html:button property="" styleClass="button" onclick="location.href='${openDocUrl}'"
                                         tabindex="90">
                                <fmt:message key="Document.open"/>
                            </html:button>
                        </c:if>
                    </app2:checkAccessRight>
                </c:if>
            </c:when>
            <c:when test="${actionForm.dtoMap['type'] == email}">
                <c:choose>
                    <c:when test="${'create' == op}">
                        <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                            <app2:securitySubmit operation="${op}" functionality="SALESPROCESSACTION"
                                                 property="${sendButton}"
                                                 styleClass="button" tabindex="90">
                                <fmt:message key="Common.send"/>
                            </app2:securitySubmit>
                        </app2:checkAccessRight>
                    </c:when>
                    <c:when test="${null != op1}">
                        &nbsp;
                    </c:when>
                    <c:otherwise>
                        <app2:securitySubmit property="${saveButton}" operation="${op}"
                                             functionality="SALESPROCESSACTION"
                                             styleClass="button" styleId="saveButtonId" tabindex="90">
                            <c:out value="${button}"/>
                        </app2:securitySubmit>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <app2:securitySubmit property="${saveButton}" operation="${op}"
                                     functionality="SALESPROCESSACTION"
                                     styleClass="button" styleId="saveButtonId" tabindex="90">
                    <c:out value="${button}"/>
                </app2:securitySubmit>
            </c:otherwise>
        </c:choose>

        <app2:checkAccessRight functionality="SALE" permission="CREATE">
            <c:if test="${'update' == op && 100 == actionForm.dtoMap['probability'] && actionForm.dtoMap['type'] != email}">
                <html:button property="createSaleButton" styleClass="button" onclick="javascript:createSale();"
                             tabindex="90">
                    <fmt:message key="SalessProcess.createSale"/>
                </html:button>
            </c:if>
        </app2:checkAccessRight>

        <html:cancel styleClass="button" tabindex="90"><fmt:message key="Common.cancel"/></html:cancel>
    </TD>
</TR>
</table>
</html:form>

</td>
</tr>
<tr>
    <td align="left">
        <br>
        <c:if test="${'update' == op }">
            <table border="0" align="center" cellpadding="2" cellspacing="0" width="100%">
                <c:if test="${isSalesProcess==true}">
                    <c:set value="/SalesProcess/ActionPosition/Forward/Create.do?dto(contactId)=${actionForm.dtoMap['contactId']}&dto(note)=${app2:encode(actionForm.dtoMap['note'])}&dto(processId)=${actionForm.dtoMap['processId']}"
                           var="urlCreate"/>
                </c:if>
                <c:if test="${isSalesProcess==false}">
                    <c:set value="/SalesProcess/ActionPosition/Forward/Create.do?dto(contactId)=${actionForm.dtoMap['contactId']}&dto(processId)=${actionForm.dtoMap['processId']}&processId=${actionForm.dtoMap['processId']}&dto(note)=${app2:encode(actionForm.dtoMap['note'])}"
                           var="urlCreate"/>
                </c:if>
                <tr>
                    <html:form action="${urlCreate}">
                        <td class="leftButton">
                            <app2:checkAccessRight functionality="SALESPROCESSPOSITION" permission="CREATE">
                                <html:submit property="new" styleClass="button">
                                    <fmt:message key="Common.new"/>
                                </html:submit>
                            </app2:checkAccessRight>
                        </td>
                    </html:form>

                    <html:form action="SalesProcess/Action/Forward/Update.do?cmd=true">
                        <html:hidden property="dto(contactId)" value="${actionForm.dtoMap['contactId']}"/>
                        <html:hidden property="dto(note)" value="${actionForm.dtoMap['note']}"/>
                        <td class="leftButton" width="100%">
                            <app2:checkAccessRight functionality="SALESPROCESSPOSITION" permission="CREATE">
                                <html:submit property="copyFromButton" styleClass="button">
                                    <fmt:message key="Common.copyFrom"/>
                                </html:submit>
                                <fanta:select property="dto(copyFromContactId)"
                                              listName="actionList"
                                              labelProperty="note"
                                              valueProperty="contactId"
                                              styleClass="mediumSelect"
                                              module="/sales"
                                              firstEmpty="true">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="processId"
                                                     value="${not empty actionForm.dtoMap['processId']?actionForm.dtoMap['processId']:0}"/>
                                    <fanta:parameter field="contactId"
                                                     value="${not empty actionForm.dtoMap['contactId']?actionForm.dtoMap['contactId']:0}"/>
                                </fanta:select>
                            </app2:checkAccessRight>
                        </td>
                    </html:form>
                </tr>
            </table>
            <app2:checkAccessRight functionality="SALESPROCESSPOSITION" permission="VIEW">
                <iframe name="frame1"
                        src="<app:url value="SalesProcess/ActionPosition/List.do?dto(contactId)=${actionForm.dtoMap['contactId']}&dto(processId)=${actionForm.dtoMap['processId']}&dto(note)=${app2:encode(dto.note)}&processId=${actionForm.dtoMap['processId']}"/>"
                        class="Iframe" scrolling="no" frameborder="0" id="iFrameId">
                </iframe>
            </app2:checkAccessRight>
        </c:if>
    </td>
</tr>

</table>
<br>