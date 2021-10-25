<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<calendar:initialize/>

<c:set var="eventTypeId" value="${app2:findProductTypeIdOfEventType(pageContext.request)}"/>

<%
    pageContext.setAttribute("intervalMinList", JSPHelper.getIntervalMin());
    pageContext.setAttribute("hourList", JSPHelper.getHours());
%>

<app2:jScriptUrl url="/calendar.html" var="jsUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="date" value="field.value"/>
    <app2:jScriptUrlParam param="field" value="field.id"/>
    <app2:jScriptUrlParam param="field_" value="field_.id"/>
</app2:jScriptUrl>

<script language="JavaScript">

    function checkProductType(box) {
        if('${eventTypeId}' == box.value) {
            document.getElementById('tr_initId').style.display = "";
            document.getElementById('tr_endId').style.display = "";
            document.getElementById('tr_closingId').style.display = "";
            document.getElementById('td_maxParticipantLabel').style.display = "";
            document.getElementById('td_maxParticipantContain').style.display = "";
        } else {
            document.getElementById('tr_initId').style.display = "none";
            document.getElementById('tr_endId').style.display = "none";
            document.getElementById('tr_closingId').style.display = "none";
            document.getElementById('td_maxParticipantLabel').style.display = "none";
            document.getElementById('td_maxParticipantContain').style.display = "none";
        }
    }

    //calendar part
    function openCalendarPickerCustom(field, field_) {
        var winx = (screen.width) / 2;
        var winy = (screen.height) / 2;
        var posx = winx - 244 / 2;
        var posy = winy - 190 / 2;
        calendarWindow = window.open(${jsUrl}, 'calendarWindowScheduler', 'resizable=no,scrollbars=no,width=244,height=190,left=' + posx + ',top=' + posy);
        calendarWindow.focus();
    }

    function incrHour(){
        var a = document.getElementById('startHour').value;
        var i=parseInt(a)+1;
        if(a < 23)
            document.getElementById('endHour').value = i;
        else document.getElementById('endHour').value = a;
    }

    function equalMin(){
        document.getElementById('endMin').value = document.getElementById('startMin').value;
    }

    function equalDate(){
        document.getElementById('endDate').value = document.getElementById('startDate').value;
    }

</script>

<html:form action="${action}" focus="dto(productName)" enctype="multipart/form-data">
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<br/>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
<td>
<table cellSpacing="0" cellPadding="4" width="800" border="0" align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="${op}" tabindex="1" property="dto(save)" functionality="PRODUCT"
                                 styleId="saveButtonId" styleClass="button"
                                 onclick="javascript:fillMultipleSelectValues();">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'update'}">

                <app:url var="url"
                         value="/Product/Forward/Delete.do?productId=${param.productId}&dto(productId)=${param.productId}&dto(productName)=${productForm.dtoMap.productName}&dto(withReferences)=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}"/>
                <app2:checkAccessRight functionality="PRODUCT" permission="DELETE">
                    <html:button onclick="location.href='${url}'" styleClass="button" property="dto(delete)"
                                 tabindex="2">
                        <fmt:message key="Common.delete"/>
                    </html:button>
                </app2:checkAccessRight>
            </c:if>

            <c:if test="${op == 'create' || op == 'delete'}">
                <html:cancel styleClass="button" tabindex="3">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </c:if>
        </TD>
    </TR>
</table>

<table id="Product.jsp" border="0" cellpadding="0" cellspacing="0" width="800" align="center" class="container">
<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

<c:if test="${op == 'update'}">
    <html:hidden property="dto(version)"/>
</c:if>
<c:if test="${'update' == op || op == 'delete'}">
    <html:hidden property="dto(productId)" value="${productForm.dtoMap['productId']}"/>
</c:if>
<TR>
    <TD colspan="4" class="title">
        <c:out value="${title}"/>
    </TD>
</TR>
<TR>
    <TD class="label" width="15%">
        <fmt:message key="Product.name"/>
    </TD>
    <TD class="contain" width="35%">
        <app:text property="dto(productName)"
                  styleClass="middleText"
                  maxlength="80"
                  tabindex="4"
                  view="${op == 'delete'}"/>
    </TD>
    <TD class="label" width="15%">
        <fmt:message key="Product.group"/>
    </TD>
    <TD class="contain" width="35%">
        <c:choose>
            <c:when test="${'delete' != op }">
                <fanta:select property="dto(productGroupId)"
                              listName="productGroupList"
                              firstEmpty="true"
                              labelProperty="name"
                              valueProperty="id"
                              module="/catalogs"
                              styleClass="middleSelect"
                              readOnly="${op == 'delete'}"
                              tabIndex="10">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:tree columnId="id" columnParentId="parentProductGroupId"
                                separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
                </fanta:select>
            </c:when>
            <c:otherwise>
                <fanta:select property="dto(productGroupId)"
                              listName="productGroupSimpleList"
                              labelProperty="name"
                              valueProperty="id"
                              module="/catalogs"
                              styleClass="middleSelect"
                              readOnly="true"
                              tabIndex="9">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </c:otherwise>
        </c:choose>
    </TD>
</TR>
<TR>
    <TD class="label">
        <fmt:message key="Product.unit"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(unitId)"
                      listName="productUnitList"
                      firstEmpty="true"
                      labelProperty="name"
                      valueProperty="id"
                      module="/catalogs"
                      styleClass="middleSelect"
                      readOnly="${op == 'delete'}"
                      tabIndex="5">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
    <TD class="label">
        <fmt:message key="Product.type"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(productTypeId)"
                      listName="productTypeList"
                      firstEmpty="true"
                      labelProperty="name"
                      valueProperty="id"
                      module="/catalogs"
                      styleClass="middleSelect"
                      readOnly="${op == 'delete'}"
                      preProcessor="com.piramide.elwis.web.productmanager.el.ProductTypeSelectPreProcessor"
                      onChange="checkProductType(this);"
                      tabIndex="11">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
</TR>
<TR>
    <td class="label">
        <fmt:message key="Product.vat"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(vatId)"
                      listName="vatList"
                      labelProperty="name"
                      valueProperty="id"
                      styleClass="middleSelect"
                      module="/catalogs"
                      firstEmpty="true"
                      readOnly="${'delete' == op}"
                      tabIndex="6">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
    <TD class="label">
        <fmt:message key="Product.version"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(currentVersion)"
                  styleClass="shortText"
                  maxlength="40"
                  tabindex="12"
                  view="${op == 'delete'}"/>
    </TD>
</TR>
<tr>
    <TD class="topLabel">
        <fmt:message key="Product.priceNet"/>
    </TD>
    <TD class="containTop">
        <app:numberText property="dto(price)"
                        styleClass="numberText"
                        tabindex="7"
                        maxlength="12"
                        view="${'delete' == op}"
                        numberType="decimal"
                        maxInt="10"
                        maxFloat="2"/>
    </TD>
    <td class="label">
        <fmt:message key="Product.account"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(accountId)"
                      listName="accountList"
                      labelProperty="name"
                      valueProperty="accountId"
                      styleClass="middleSelect"
                      module="/catalogs"
                      firstEmpty="true"
                      readOnly="${'delete' == op}"
                      tabIndex="13">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<TR>
    <td class="label">
        <fmt:message key="Product.priceGross"/>
    </td>
    <td class="contain">
        <app:numberText property="dto(priceGross)"
                        styleClass="numberText"
                        tabindex="8"
                        maxlength="15"
                        view="${'delete' == op}"
                        numberType="decimal"
                        maxInt="10"
                        maxFloat="2"/>
    </td>
    <TD class="label">
        <fmt:message key="Product.number"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(productNumber)"
                  styleClass="shortText"
                  maxlength="40"
                  tabindex="14"
                  view="${op == 'delete'}"/>
    </TD>
</TR>

    <c:set var="displayEventFields" value="display: none;"/>
    <c:if test="${eventTypeId eq productForm.dtoMap['productTypeId']}">
        <c:set var="displayEventFields" value=""/>
    </c:if>

    <tr>
        <td class="label">
            <fmt:message key="Product.NumberOfCustomers"/>
        </td>
        <td class="contain">
            ${app2:countSalePositionByProduct(productForm.dtoMap['productId'])}
        </td>
        <td id="td_maxParticipantLabel" style="${displayEventFields}" class="label">
            <fmt:message key="Product.eventMaxParticipant"/>
        </td>
        <td id="td_maxParticipantContain" style="${displayEventFields}" class="contain">
            <app:text property="dto(eventMaxParticipant)" styleClass="numberText" maxlength="6" tabindex="15" view="${op == 'delete'}"/>
        </td>
    </tr>

    <tr id="tr_initId" style="${displayEventFields}">
        <td class="label">
            <fmt:message key="Product.startDate"/>
        </td>
        <td class="contain">
            <c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
            <fmt:message var="datePattern" key="datePattern"/>

            <app:dateText property="dto(initDate)" tabindex="8" styleId="startDate" onkeyup="equalDate()"
                          calendarPicker="${false}" datePatternKey="${datePattern}"
                          styleClass="dateText" view="${op == 'delete'}" maxlength="10" currentDate="true"
                          currentDateTimeZone="${currentDateTimeZone}"/>

            <c:if test="${op != 'delete'}">
                <a href="javascript:openCalendarPickerCustom(document.getElementById('startDate'),document.getElementById('endDate'));"
                   alt="<fmt:message   key="Calendar.open"/>">
                    <img src="<c:out value="${sessionScope.baselayout}"/>/img/calendarPicker.gif"
                         alt="<fmt:message   key="Calendar.open"/>" border="0"/>
                </a>
            </c:if>
            &nbsp;
            <abbr id="startDate_Id">
                <html:select property="dto(initHour)" onchange="incrHour()" onkeyup="incrHour()" tabindex="8"
                             styleClass="select" readonly="${op == 'delete'}" styleId="startHour" style="width:43">
                    <html:options collection="hourList" property="value" labelProperty="label"/>
                </html:select>
                :
                <html:select property="dto(initMin)" onchange="equalMin()" onkeyup="equalMin()" styleClass="select"
                             tabindex="8" readonly="${op == 'delete'}" styleId="startMin" style="width:43">
                    <html:options collection="intervalMinList" property="value" labelProperty="label"/>
                </html:select>
            </abbr>
        </td>
        <td class="label">
            <fmt:message key="Product.webSiteLink"/>
        </td>
        <td class="contain">
            <app:text property="dto(webSiteLink)" styleClass="middleText" maxlength="255" tabindex="15" view="${op == 'delete'}"/>
        </td>
    </tr>

    <tr id="tr_endId" style="${displayEventFields}">
        <td class="label"><fmt:message key="Product.endDate"/></TD>
        <td class="contain">
            <app:dateText property="dto(endDate)" tabindex="8" styleId="endDate" calendarPicker="${op != 'delete'}"
                          datePatternKey="${datePattern}"
                          styleClass="dateText" view="${op == 'delete'}" maxlength="10" currentDate="true"
                          currentDateTimeZone="${currentDateTimeZone}"/>
            &nbsp;
            <abbr id="endDate_Id">

                <html:select property="dto(endHour)" tabindex="8" styleClass="select" readonly="${op == 'delete'}"
                             styleId="endHour" style="width:43">
                    <html:options collection="hourList" property="value" labelProperty="label"/>
                </html:select>
                :
                <html:select property="dto(endMin)" tabindex="8" styleClass="select" readonly="${op == 'delete'}"
                             styleId="endMin" style="width:43">
                    <html:options collection="intervalMinList" property="value" labelProperty="label"/>
                </html:select>
            </abbr>
        </td>
        <td class="label">
            <fmt:message key="Product.eventAddress"/>
        </td>
        <td class="contain">
            <html:textarea property="dto(eventAddress)"
                           styleClass="middleDetail"
                           tabindex="15"
                           readonly="${op == 'delete'}"/>

        </td>
    </tr>
    <tr id="tr_closingId" style="${displayEventFields}">
        <td class="label">
            <fmt:message key="Product.closingDate"/>
        </td>
        <td class="contain" colspan="3">
            <app:dateText property="dto(closeDate)" tabindex="8" styleId="closeDate" calendarPicker="${op != 'delete'}"
                          datePatternKey="${datePattern}"
                          styleClass="dateText" view="${op == 'delete'}" maxlength="10"
                          currentDateTimeZone="${currentDateTimeZone}"/>
            &nbsp;
            <abbr id="closeDate_Id">
                <html:select property="dto(closeHour)" tabindex="8" styleClass="select" readonly="${op == 'delete'}"
                             style="width:43">
                    <html:options collection="hourList" property="value" labelProperty="label"/>
                </html:select>

                <c:if test="${not empty productForm.dtoMap['closeDate'] || op != 'delete'}">
                    :
                </c:if>

                <html:select property="dto(closeMin)" tabindex="8" styleClass="select" readonly="${op == 'delete'}"
                             style="width:43">
                    <html:options collection="intervalMinList" property="value" labelProperty="label"/>
                </html:select>
            </abbr>
        </td>
    </tr>

<TR>
    <TD class="topLabel" colspan="4">
        <fmt:message key="Product.description"/>
        <br>
        <html:textarea property="dto(description)"
                       styleClass="middleDetail"
                       tabindex="15"
                       style="height:120px;width:99%;"
                       readonly="${op == 'delete'}"/>
    </TD>
</TR>

<tr>
    <td colspan="4">
        <!--added by ivan-->
        <c:set var="elementCounter" value="${16}" scope="request"/>
        <c:set var="ajaxAction" value="/products/MainPageReadSubCategories.do" scope="request"/>
        <c:set var="downloadAction"
               value="/products/MainPage/DownloadCategoryFieldValue?dto(productId)=${param.productId}&productId=${param.productId}"
               scope="request"/>
        <c:set var="formName" value="productForm" scope="request"/>
        <c:set var="table" value="<%=ContactConstants.PRODUCT_CATEGORY%>" scope="request"/>
        <c:set var="operation" value="${op}" scope="request"/>
        <c:set var="labelWidth" value="15" scope="request"/>
        <c:set var="containWidth" value="85" scope="request"/>
        <c:set var="generalWidth" value="${250}" scope="request"/>
        <c:import url="/common/catalogs/CategoryUtil.jsp"/>
        <c:set var="lastTabIndex" value="${elementCounter+1}" scope="request"/>
    </td>
</tr>

</table>
<table cellSpacing=0 cellPadding=4 width="800" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="${op}" property="dto(save)" functionality="PRODUCT" styleId="saveButtonId"
                                 styleClass="button" tabindex="${lastTabIndex+1}"
                                 onclick="javascript:fillMultipleSelectValues();">
                ${button}
            </app2:securitySubmit>

            <c:if test="${op == 'update'}">
                <app2:checkAccessRight functionality="PRODUCT" permission="DELETE">
                    <html:button onclick="location.href='${url}'" styleClass="button" property="dto(delete)"
                                 tabindex="${lastTabIndex+2}">
                        <fmt:message key="Common.delete"/>
                    </html:button>
                </app2:checkAccessRight>
            </c:if>

            <c:if test="${op == 'create' || op == 'delete'}">
                <html:cancel styleClass="button" tabindex="${lastTabIndex+3}">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </c:if>
        </TD>
    </TR>
</table>
</html:form>
<br/>
</td>
</tr>
</table>
