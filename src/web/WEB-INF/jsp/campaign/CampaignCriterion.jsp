<%@ page import="com.jatun.common.web.JavaScriptEncoder" %>
<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ page import="com.piramide.elwis.web.campaignmanager.util.CampaignCriterionTreeGenerate" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<%
    pageContext.setAttribute("contactTypeList", JSPHelper.getContactTypeList(request));
    pageContext.setAttribute("productInUserList", JSPHelper.getProductInUserList(request));
%>

<tags:jscript language="JavaScript" src="/js/contacts/searchcity.jsp"/>

<%
    //constant messages
    request.setAttribute("EXPIRED", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "Common.sessionExpired")));
    request.setAttribute("ERROR", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "error.tooltip.unexpected")));
    request.setAttribute("LOADING", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "Common.message.loading")));
    request.setAttribute("CATEGORY_ISNULL", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "Campaign.categoryIsNull")));
%>
<c:set var="OPERATOR_RELATIONEXISTS" value="<%=CampaignConstants.CriteriaComparator.RELATION_EXISTS.getConstant()%>"/>

<link rel="StyleSheet" href='<c:url value="/js/dtree/dtree.css"/>' type="text/css"/>
<tags:jscript language="JavaScript" src="/js/dtree/dtreecampaign.jsp"/>
<tags:jscript language="JavaScript" src="/js/campaign/campaign.jsp"/>
<tags:jscript language="JavaScript" src="/js/st-xmlhttp.js"/>

<app2:jScriptUrl url="/campaign/CampaignCriterion/LoadValue.do?campaignId=${param.campaignId}" var="showIU_valuesURL"
                 addModuleParams="false">
    <app2:jScriptUrlParam param="categoryId" value="categoryid"/>
    <app2:jScriptUrlParam param="campCriterionValueId" value="campCriterionValueId"/>
    <app2:jScriptUrlParam param="selectType" value="selectType"/>
    <app2:jScriptUrlParam param="fieldType" value="fieldType"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/campaign/CampaignCriterion/FieldType.do?campaignId=${param.campaignId}" var="show_typeURL"
                 addModuleParams="false">
    <app2:jScriptUrlParam param="categoryId" value="categoryid"/>
    <app2:jScriptUrlParam param="campCriterionValueId" value="campCriterionValueId"/>
    <app2:jScriptUrlParam param="operator" value="operator"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/campaign/CampaignCriterion/FieldType.do?campaignId=${param.campaignId}" var="show_typeErrorURL"
                 addModuleParams="false">
    <app2:jScriptUrlParam param="categoryId" value="categoryid"/>
    <app2:jScriptUrlParam param="campCriterionValueId" value="campCriterionValueId"/>
    <app2:jScriptUrlParam param="operator" value="operator"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/campaign/CampaignCriterion/LoadValue.do?campaignId=${param.campaignId}" var="showIU_reloadURL"
                 addModuleParams="false">
    <app2:jScriptUrlParam param="categoryId" value="categoryid"/>
    <app2:jScriptUrlParam param="campCriterionValueId" value="campCriterionValueId"/>
    <app2:jScriptUrlParam param="selectType" value="opeType"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/campaign/CampaignCriterion/LoadOperatorValue.do?campaignId=${param.campaignId}"
                 var="readOperatorValuesURL" addModuleParams="false">
    <app2:jScriptUrlParam param="campCriterionValueId" value="campCriterionValueId"/>
    <app2:jScriptUrlParam param="operator" value="selectType"/>
    <app2:jScriptUrlParam param="fieldType" value="fieldType"/>
</app2:jScriptUrl>


<script type="text/javascript">
    var requestCount = 0;
    function changeStartDateValue() {
        $("#dateValue_2").val($("#dateValue_1").val());
    }
    function showIU_values(fieldType, categoryid, campCriterionValueId, fieldname, opBox) {
        var selectType = "undefined";
        // myReset();
        document.getElementById('fieldName').value = fieldname;

        for (var i = 0; i < opBox.options.length; i++) {
            if (opBox.options[i].selected && opBox.options[i].value != "")
                selectType = opBox.options[i].value;
        }


        if ("BETWEEN" == selectType) myReset();
        if (selectType == "undefined") {
            //viewEmpty();
        } else if (13 == campCriterionValueId) {
            document.getElementById('text_value').style.display = "";
            document.getElementById('IU_Type').value = "4";
        } else if (18 == campCriterionValueId) {
            document.getElementById('search_list_partner').style.display = "";
            document.getElementById('IU_Type').value = "11";
        } else if (22 == campCriterionValueId) {
            document.getElementById('contact_type').style.display = "";
            document.getElementById('IU_Type').value = "12";
        } else if (23 == campCriterionValueId) {
            document.getElementById('product_inUse').style.display = "";
            document.getElementById('IU_Type').value = "13";
        } else if (26 == campCriterionValueId) {
            document.getElementById('product_name').style.display = "";
            document.getElementById('IU_Type').value = "14";
        } else if (selectType == '${OPERATOR_RELATIONEXISTS}') {
            makeHttpRequest(${readOperatorValuesURL}, 'renderRelationExistValue', false, 'error');
            document.getElementById('IU_Type').value = "15";
        } else if (fieldType == "5" || fieldType == "6" || (fieldType == "4" && fieldname != "0" && document.getElementById("categoryId").value == "-100")) {
            document.getElementById('IU_Type').value = "9";
            showToolTip('${LOADING}');
            makeHttpRequest(${showIU_valuesURL}, 'renderSelect', false, 'error');
        } else
            showIU(opBox, fieldType);
    }

    function renderSelect(mySelect) {
        var mydiv = document.getElementById("select_values");
        document.getElementById('select_values').style.display = "";
        hideToolTip();
        mydiv.innerHTML = mySelect;
    }

    function renderRelationExistValue(htmlValue) {
        var mydiv = document.getElementById("readOnly_value");
        mydiv.style.display = "";
        mydiv.innerHTML = htmlValue;
    }

    function error(requestStatusCode) {
        requestCount--;
        if (requestStatusCode == 404) { //session expired http request status code
            showToolTip('${EXPIRED}');
        } else {
            showToolTip('${ERROR}');//other type error
        }
    }

    function showToolTip(message) {
        var msgDiv = document.getElementById("msgId");
        msgDiv.innerHTML = unescape(message);
        msgDiv.style.visibility = "visible";
    }

    function empty() {
        document.getElementById('criterias').style.display = "none";
    }

    function show_type(categoryid, campCriterionValueId, operator) {
        showToolTip('${LOADING}');
        requestCount++;
        makeHttpRequest(${show_typeURL}, 'setType', false, 'error');
    }

    function show_typeError(categoryid, campCriterionValueId, operator) {
        makeHttpRequest(${show_typeErrorURL}, 'setType', false, 'error');

        if ("EMPTY" != operator && document.getElementById('IU_Type').value == '9')
            showIU_reload(categoryid, campCriterionValueId, operator);
    }

    function hideToolTip() {
        var msgDiv = document.getElementById("msgId");
        requestCount--;
        if (requestCount <= 0) {
            msgDiv.style.visibility = "hidden";
            requestCount = 0;
        } else {
            showToolTip('${LOADING}');
        }
    }

    function showIU_reload(categoryid, campCriterionValueId, opeType) {
        makeHttpRequest(${showIU_reloadURL}, 'renderSelect', false, 'error');
    }

    function setType(type) {
        if (type == '') {
            msgDiv.innerHTML = unescape('${CATEGORY_ISNULL}');
            msgDiv.style.visibility = "visible";
            document.getElementById('criterias').style.display = "none";
        } else {

            var title = document.getElementById("criteria_title");
            var operator = document.getElementById("table_operator");
            var separator = '||';
            var i = type.indexOf(separator);
            var a = type.substring(0, i);
            var s = i + separator.length;
            var b = type.substring(s);

            hideToolTip();

            if (a != null && a != "null")
                title.innerHTML = a;

            operator.innerHTML = b;
        }
    }

    function setCriteriaType(categoryId, campValueId, fieldType) {//1=customer, 2=contactPerson, 3=product, 4=address.

        var form = document.campaignCriterionForm;
        document.getElementById('criterias').style.display = "";
        document.getElementById('value').style.display = "";
        hideToolTip();
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
        viewEmpty();
        show_type(categoryId, campValueId, "");
    }

    function emptyNode() {
        document.getElementById('criterias').style.display = "none";
        viewEmpty();
    }

</script>

<%
    pageContext.setAttribute("numberList", JSPHelper.getCriteriaNumberTypeOperatorList(request));
    pageContext.setAttribute("selectList", JSPHelper.getCriteriaSelectTypeOperatorList(request));
%>

<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<html:form action="${action}" method="post" styleClass="form-horizontal">

    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="create" functionality="CAMPAIGNCRITERION"
                             styleClass="${app2:getFormButtonClasses()}">
            <fmt:message key="Common.save"/>
        </app2:securitySubmit>

        <app2:securitySubmit operation="create" functionality="CAMPAIGNCRITERION"
                             styleClass="${app2:getFormButtonClasses()}"
                             property="SaveAndNew">
            <fmt:message key="Common.saveAndNew"/>
        </app2:securitySubmit>

        <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="11">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </div>
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                    ${title}
            </legend>
        </fieldset>

        <div>
            <div class="table-responsive">
                <table class="${app2:getGeneralFantabulousTableClasses()}">
                    <tr>
                        <td class="contain" valign="top" width="25%">
                            <div style="overflow:scroll;height:280px;">
                                <%
                                    CampaignCriterionTreeGenerate treeGenerate = new CampaignCriterionTreeGenerate();
                                    out.println(treeGenerate.draw(request));
                                %>
                            </div>
                        </td>
                        <td width="75%" class="contain">
                            <div style="height:280px;" id="criterias">
                                <div class="clearfix marginButton">
                                    <label class="${app2:getFormLabelClasses()}">
                                        <fmt:message key="CampaignCriterion.field"/>
                                    </label>

                                    <div class="${app2:getFormContainClasses(true)}">
                                        <div id="criteria_title">
                                        </div>
                                    </div>
                                </div>

                                <div class="clearfix marginButton">
                                    <label class="${app2:getFormLabelClasses()}">
                                        <fmt:message key="Report.operator"/>
                                    </label>

                                    <div class="${app2:getFormContainClasses(view)}">
                                        <div id="table_operator">

                                        </div>
                                    </div>
                                </div>

                                <div class="clearfix marginButton">
                                    <label class="${app2:getFormLabelClasses()}" id="value">
                                        <fmt:message key="Campaign.value"/>
                                    </label>

                                    <!-- inputs -->

                                    <div class="${app2:getFormContainClasses(view)}">
                                        <html:hidden property="dto(op)" value="${op}"/>
                                        <fmt:message var="datePattern" key="datePattern"/>
                                        <html:hidden property="dto(IU_Type)" styleId="IU_Type"/>
                                        <html:hidden property="dto(fieldName)" styleId="fieldName"/>

                                        <div id="text_value"   ${(campaignCriterionForm.dtoMap.IU_Type =='4') ? "":"style=\"display: none;position=absolute\""}>
                                            <app:text property="dto(text_value)"
                                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                                      maxlength="40"
                                                      styleId="textValue"/>
                                        </div>
                                        <div id="number_value"   ${(campaignCriterionForm.dtoMap.IU_Type =='1') ? "":"style=\"display: none;position=absolute\""}>
                                            <app:numberText property="dto(number_value)"
                                                            styleClass="${app2:getFormInputClasses()} numberText"
                                                            styleId="numberValue"
                                                            numberType="integer" maxlength="10"/>
                                        </div>
                                        <div id="decimalNumber_value"  ${(campaignCriterionForm.dtoMap.IU_Type =='2') ? "":"style=\"display: none;position=absolute\""}>
                                            <app:numberText property="dto(decimalNumber_value)"
                                                            styleClass="${app2:getFormInputClasses()} decimalNumberValue"
                                                            maxlength="12"
                                                            numberType="decimal" maxInt="10" maxFloat="2"/>
                                        </div>
                                        <div id="date_value"  ${(campaignCriterionForm.dtoMap.IU_Type =='3') ? "":"style=\"display: none;position=absolute\""}>
                                            <div class="input-group date">
                                                <app:dateText property="dto(date_value)"
                                                              maxlength="10"
                                                              styleId="dateValue"
                                                              calendarPicker="true"
                                                              currentDate="true"
                                                              datePatternKey="${datePattern}"
                                                              styleClass="${app2:getFormInputClasses()} dateText"
                                                              mode="bootstrap"/>
                                            </div>
                                        </div>
                                        <div id="betweenNumber_value"   ${(campaignCriterionForm.dtoMap.IU_Type =='5') ? "":"style=\"display: none;position=absolute\""}>
                                            <div class="row">
                                                <div class="col-xs-12 col-sm-6 marginButton">
                                                    <div class="pull-left">
                                                        <label class="control-label">
                                                            <fmt:message key="Common.from"/>
                                                        </label>
                                                    </div>
                                                    <div class="pull-right col-xs-12 col-sm-8 col-md-9 paddingRemove">
                                                        <app:numberText property="dto(numberValue_1)"
                                                                        styleId="numberValue1"
                                                                        styleClass="${app2:getFormInputClasses()} numberText"
                                                                        maxlength="10"
                                                                        numberType="integer"/>
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 col-sm-6">
                                                    <div class="pull-left">
                                                        <label class="control-label">
                                                            <fmt:message key="Common.to"/>
                                                        </label>
                                                    </div>
                                                    <div class="pull-right col-xs-12 col-sm-8 col-md-9 paddingRemove">
                                                        <app:numberText property="dto(numberValue_2)"
                                                                        styleId="numberValue2"
                                                                        styleClass="${app2:getFormInputClasses()} numberText"
                                                                        maxlength="10"
                                                                        numberType="integer"/>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="betweenDecimalNumber_value"  ${(campaignCriterionForm.dtoMap.IU_Type =='6') ? "":"style=\"display: none;position=absolute\""}>
                                            <div class="row">
                                                <div class="col-xs-12 col-sm-6 marginButton">
                                                    <div class="pull-left">
                                                        <label class="control-label">
                                                            <fmt:message key="Common.from"/>
                                                        </label>
                                                    </div>
                                                    <div class="pull-right col-xs-12 col-sm-8 col-md-9 paddingRemove">
                                                        <app:numberText property="dto(decimalNumberValue_1)"
                                                                        styleId="decimalNumberValue1"
                                                                        styleClass="${app2:getFormInputClasses()} numberText"
                                                                        maxlength="12" numberType="decimal" maxInt="10"
                                                                        maxFloat="2"
                                                                        view="${'delete' == op}"/>
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 col-sm-6">
                                                    <div class="pull-left">
                                                        <label class="control-label">
                                                            <fmt:message key="Common.to"/>
                                                        </label>
                                                    </div>
                                                    <div class="pull-right col-xs-12 col-sm-8 col-md-9 paddingRemove">
                                                        <app:numberText property="dto(decimalNumberValue_2)"
                                                                        styleId="decimalNumberValue2"
                                                                        styleClass="${app2:getFormInputClasses()} numberText"
                                                                        maxlength="12" numberType="decimal" maxInt="10"
                                                                        maxFloat="2"
                                                                        view="${'delete' == op}"/>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row"
                                             id="betweenDate_value"   ${(campaignCriterionForm.dtoMap.IU_Type =='7') ? "":"style=\"display: none;position=absolute\""}>
                                            <div class="col-xs-12 col-sm-12 col-md-6 marginButton">
                                                <div class="input-group date">
                                                    <fmt:message key="Common.from" var="from"/>
                                                    <app:dateText property="dto(dateValue_1)" styleId="dateValue_1"
                                                                  onchange="changeStartDateValue()"
                                                                  maxlength="10"
                                                                  convert="true"
                                                                  calendarPicker="true"
                                                                  mode="bootstrap"
                                                                  placeHolder="${from}"
                                                                  datePatternKey="${datePattern}"
                                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                                  currentDate="true"
                                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                                </div>
                                            </div>
                                            <div class="col-xs-12 col-sm-12 col-md-6 marginButton">
                                                <div class="input-group date">
                                                    <fmt:message key="Common.to" var="to"/>
                                                    <app:dateText property="dto(dateValue_2)" styleId="dateValue_2"
                                                                  maxlength="10"
                                                                  convert="true"
                                                                  calendarPicker="true"
                                                                  mode="bootstrap"
                                                                  placeHolder="${to}"
                                                                  datePatternKey="${datePattern}"
                                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                                  currentDate="true"
                                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                                </div>
                                            </div>
                                        </div>

                                        <div id="select_values" ${(campaignCriterionForm.dtoMap.IU_Type =='9') ? "":"style=\"display: none;\""}>

                                        </div>

                                        <div id="search_list_partner" ${(campaignCriterionForm.dtoMap.IU_Type =='11') ? "":"style=\"display: none;\""}>
                                            <div class="input-group">
                                                <app:text property="dto(partnerName)"
                                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                                          readonly="true"
                                                          styleId="fieldAddressName_id"/>
                                                <html:hidden property="dto(partnerId)" styleId="fieldAddressId_id"/>
                                                <span class="input-group-btn">
                                                    <tags:bootstrapSelectPopup styleId="searchPartner_id"
                                                                               url="/contacts/SearchAddress.do"
                                                                               name="searchPartner"
                                                                               titleKey="Common.search"
                                                                               modalTitleKey="Contact.Title.search"
                                                                               isLargeModal="true"/>
                                                    <tags:clearBootstrapSelectPopup
                                                            styleClass="${app2:getFormButtonClasses()}"
                                                            glyphiconClass="glyphicon-erase"
                                                            keyFieldId="fieldAddressId_id"
                                                            nameFieldId="fieldAddressName_id"
                                                            titleKey="Common.clear"/>
                                                </span>
                                            </div>
                                        </div>

                                        <div id="contact_type" ${(campaignCriterionForm.dtoMap.IU_Type =='12') ? "":"style=\"display: none;\""}>
                                            <html:select property="dto(code)"
                                                         styleClass="${app2:getFormSelectClasses()} select">
                                                <html:option value=""/>
                                                <html:options collection="contactTypeList" property="value"
                                                              labelProperty="label"/>
                                            </html:select>
                                        </div>

                                        <div id="product_inUse" ${(campaignCriterionForm.dtoMap.IU_Type =='13') ? "":"style=\"display: none;\""}>
                                            <html:select property="dto(inUse)"
                                                         styleClass="${app2:getFormSelectClasses()} select">
                                                <html:option value=""/>
                                                <html:options collection="productInUserList" property="value"
                                                              labelProperty="label"/>
                                            </html:select>
                                        </div>

                                        <div id="product_name" ${(campaignCriterionForm.dtoMap.IU_Type =='14') ? "":"style=\"display: none;\""}>
                                            <div class="input-group">
                                                <app:text property="dto(productName)" styleId="field_name"
                                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                                          maxlength="40" readonly="true" tabindex="1"
                                                          view="${op != 'create'}"/>
                                                <html:hidden property="dto(productId)" styleId="field_key"/>
                                                <html:hidden property="dto(1)" styleId="field_versionNumber"/>
                                                <html:hidden property="dto(2)" styleId="field_unitId"/>
                                                <html:hidden property="dto(3)" styleId="field_price"/>
                                        <span class="input-group-btn">
                                            <tags:bootstrapSelectPopup styleId="SearchProduct_id"
                                                                       url="/products/SearchProduct.do"
                                                                       name="SearchProduct"
                                                                       titleKey="Common.search"
                                                                       modalTitleKey="Product.Title.SimpleSearch"
                                                                       hide="${op != 'create'}"
                                                                       tabindex="2" isLargeModal="true"/>
                                            <tags:clearBootstrapSelectPopup
                                                    styleClass="${app2:getFormButtonClasses()}"
                                                    glyphiconClass="glyphicon-erase" keyFieldId="field_key"
                                                    nameFieldId="field_name"
                                                    titleKey="Common.clear" hide="${op != 'create'}"
                                                    tabindex="3"/>
                                        </span>
                                            </div>
                                        </div>

                                        <div class="form-control-static"
                                             id="readOnly_value" ${(campaignCriterionForm.dtoMap.IU_Type =='15') ? "":"style=\"display: none;\""}>

                                        </div>

                                        <!--tooltip-->
                                        <div style="position:relative;">
                                            <div id="msgId" class="messageToolTip"
                                                 style="visibility:hidden; position:absolute; top:15px; left:-70px">
                                                <fmt:message key="Common.message.loading"/>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="create" functionality="CAMPAIGNCRITERION"
                             styleClass="${app2:getFormButtonClasses()}">
            <fmt:message key="Common.save"/>
        </app2:securitySubmit>

        <app2:securitySubmit operation="create" functionality="CAMPAIGNCRITERION"
                             styleClass="${app2:getFormButtonClasses()}"
                             property="SaveAndNew">
            <fmt:message key="Common.saveAndNew"/>
        </app2:securitySubmit>
        <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </div>
</html:form>