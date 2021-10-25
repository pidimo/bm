<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/campaign/campaign.jsp"/>
<tags:jscript language="JavaScript" src="/js/contacts/searchcity.jsp"/>
<tags:jscript language="JavaScript" src="/js/st-xmlhttp.js"/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>
<calendar:initialize/>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<app2:jScriptUrl url="/campaign/CampaignCriterion/LoadValue.do?campaignId=${param.campaignId}" var="showIU_reloadURL" addModuleParams="false">
    <app2:jScriptUrlParam param="categoryId" value="categoryid"/>
    <app2:jScriptUrlParam param="campCriterionValueId" value="campCriterionValueId"/>
    <app2:jScriptUrlParam param="selectType" value="opeType"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/campaign/CampaignCriterion/LoadValue.do?campaignId=${param.campaignId}" var="showIU_updateValuesURL" addModuleParams="false">
    <app2:jScriptUrlParam param="categoryId" value="categoryid"/>
    <app2:jScriptUrlParam param="campCriterionValueId" value="campCriterionValueId"/>
    <app2:jScriptUrlParam param="criterionId" value="criterionId"/>
    <app2:jScriptUrlParam param="operator" value="operator"/>
    <app2:jScriptUrlParam param="ope" value="ope"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/campaign/CampaignCriterion/LoadValue.do?operation=update&campaignId=${param.campaignId}" var="showIU_valuesURL" addModuleParams="false">
    <app2:jScriptUrlParam param="categoryId" value="categoryid"/>
    <app2:jScriptUrlParam param="campCriterionValueId" value="campCriterionValueId"/>
    <app2:jScriptUrlParam param="selectType" value="selectType"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/campaign/CampaignCriterion/LoadOperatorValue.do?campaignId=${param.campaignId}" var="readOperatorValuesURL" addModuleParams="false">
    <app2:jScriptUrlParam param="campCriterionValueId" value="campCriterionValueId"/>
    <app2:jScriptUrlParam param="operator" value="selectType"/>
</app2:jScriptUrl>

 <%
     pageContext.setAttribute("numberList", JSPHelper.getCriteriaNumberTypeOperatorList(request));
     pageContext.setAttribute("selectList", JSPHelper.getCriteriaSelectTypeOperatorList(request));
     pageContext.setAttribute("textList", JSPHelper.getCriteriaTextTypeOperatorList(request));
     pageContext.setAttribute("contactTypeList", JSPHelper.getContactTypeList(request));
     pageContext.setAttribute("operator_contactType", JSPHelper.getContactTypeOperatorList(request));
     pageContext.setAttribute("operator_productInUse", JSPHelper.getProductInUseOperatorList(request));
     pageContext.setAttribute("productInUserList", JSPHelper.getProductInUserList(request));
     pageContext.setAttribute("operator_relationExists", JSPHelper.getFieldRelationExistsOperatorList(request));
 %>

<c:set var="FIELD_RELATIONEXISTS" value="<%=CampaignConstants.FIELD_RELATION_EXISTS%>"/>
<c:set var="OPERATOR_RELATIONEXISTS" value="<%=CampaignConstants.CriteriaComparator.RELATION_EXISTS.getConstant()%>"/>

<script type="text/javascript">
function showIU_reload(categoryid, campCriterionValueId, opeType) {
  if( document.getElementById('IU_Type').value == '9')
  makeHttpRequest(${showIU_reloadURL}, 'renderSelect', false,'error');
}

function showIU_updateValues(categoryid, campCriterionValueId, criterionId, ope, operator) {
  makeHttpRequest(${showIU_updateValuesURL}, 'renderSelect', false,'error');
}

function showIU_values(categoryid, campCriterionValueId, opBox) {
  var selectType="undefined";
  for(var i=0; i< opBox.options.length; i++) {
        if(opBox.options[i].selected && opBox.options[i].value != "")
          selectType =opBox.options[i].value;
  }
    if(selectType == "undefined"){
        showIU(opBox,10);
    } else if(selectType == '${OPERATOR_RELATIONEXISTS}') {
        makeHttpRequest(${readOperatorValuesURL}, 'renderRelationExistValue', false, 'error');
    } else {
        makeHttpRequest(${showIU_valuesURL}, 'renderSelect', false,'error');
    }
}

function renderSelect(mySelect) {
    var mydiv = document.getElementById("select_values");
    document.getElementById('select_values').style.display = "";
    mydiv.innerHTML = mySelect;
}

function renderRelationExistValue(htmlValue) {
    var mydiv = document.getElementById("readOnly_value");
    mydiv.style.display = "";
    mydiv.innerHTML = htmlValue;
}

function error() {
       alert("error in its navigator ... !!");
}

 function showIU(opBox, iu){
  //  myReset();
    for(var i=0; i< opBox.options.length; i++) {
        if(opBox.options[i].value == 'BETWEEN' && opBox.options[i].selected  ||
           'BETWEEN' == document.getElementById('operator').value) myReset();
          if(opBox.options[i].selected && opBox.options[i].value != "" && //fieldType = between - number
             opBox.options[i].value == 'BETWEEN' && iu == '1'){

                document.getElementById('betweenNumber_value').style.display = "";
                document.getElementById('number_value').style.display = "none";
                document.getElementById('select_values').style.display = "none";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('decimalNumber_value').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('operator').value = 'BETWEEN';
                document.getElementById('fieldType').value = "1";
                document.getElementById('IU_Type').value = "5";

            }else if(opBox.options[i].selected && opBox.options[i].value != "" && //fieldType = between - date
                     opBox.options[i].value == 'BETWEEN' && iu == '3'){

                document.getElementById('betweenDate_value').style.display = "";

                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('number_value').style.display = "none";
               // document.getElementById('multipleSelect_value').style.display = "none";
                document.getElementById('select_values').style.display = "none";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('decimalNumber_value').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('operator').value = 'BETWEEN';
                document.getElementById('fieldType').value = "3";
                document.getElementById('IU_Type').value = "7";


            }else if(opBox.options[i].selected && opBox.options[i].value != "" &&
                     opBox.options[i].value == 'BETWEEN' && (iu == "2")){

                document.getElementById('betweenDecimalNumber_value').style.display = "";        //fieldType = between - decimalNumber
                document.getElementById('decimalNumber_value').style.display = "none";
                document.getElementById('select_values').style.display = "none";
               // document.getElementById('multipleSelect_value').style.display = "none";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('number_value').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('operator').value = 'BETWEEN';
                document.getElementById('fieldType').value = "2";
                document.getElementById('IU_Type').value = "6";

            }else if(opBox.options[i].selected && opBox.options[i].value != "" && iu == '1'){       //fieldType = number

                document.getElementById('number_value').style.display = "";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('decimalNumber_value').style.display = "none";
                document.getElementById('select_values').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('operator').value = opBox.options[i].value;
                document.getElementById('fieldType').value = "1";
                document.getElementById('IU_Type').value = "1";

            }else if(opBox.options[i].selected && opBox.options[i].value != "" && iu == '3'){      //fieldType = date

                document.getElementById('date_value').style.display = "";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('number_value').style.display = "none";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('decimalNumber_value').style.display = "none";
                document.getElementById('select_values').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('operator').value = opBox.options[i].value;
                document.getElementById('fieldType').value = "3";
                document.getElementById('IU_Type').value = "3";

            }else if(opBox.options[i].selected && opBox.options[i].value != "" && iu == '4'
                  && document.getElementById('IU_Type').value != "11"
                  && document.getElementById('IU_Type').value != "14"){   //fieldType = text

                document.getElementById('string_value').value = null;
                document.getElementById('text_value').style.display = "";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('number_value').style.display = "none";
                document.getElementById('select_values').style.display = "none";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('decimalNumber_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('operator').value = opBox.options[i].value;
                document.getElementById('fieldType').value = "4";
                document.getElementById('IU_Type').value = "4";

            }else if((document.getElementById('IU_Type').value == "12")){
                 document.getElementById('contact_type').style.display = "";
                 document.getElementById('code').value = "";

            }else if((document.getElementById('IU_Type').value == "13")){
                 document.getElementById('product_inUse').style.display = "";


            }else if((document.getElementById('IU_Type').value == "14")){
                 document.getElementById('product_name').style.display = "";


            }else if(opBox.options[i].selected && opBox.options[i].value != "" && (iu == "2")){    //fieldType = decimalNumber

                document.getElementById('decimalNumber_value').style.display = "";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('select_values').style.display = "none";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('number_value').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";
                document.getElementById('product_name').style.display = "none";

                document.getElementById('operator').value = opBox.options[i].value;
                document.getElementById('fieldType').value = "2";
                document.getElementById('IU_Type').value = "2";

            }else if(opBox.options[i].selected && opBox.options[i].value != "" &&       //fieldType = multiple select
                     (opBox.options[i].value == 'IN' || opBox.options[i].value == 'EQUAL' ) && iu =="0"){

                document.getElementById('select_values').style.display = "none";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('number_value').style.display = "none";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('decimalNumber_value').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('operator').value ="IN";
                document.getElementById('fieldType').value = "0";
                document.getElementById('IU_Type').value = "9";

            }else  if(opBox.options[i].selected && opBox.options[i].value == "" ){
                document.getElementById('select_values').style.display = "none";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('number_value').style.display = "none";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('decimalNumber_value').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('fieldValue').value = null;
              if((document.getElementById('IU_Type').value == "4"))
                  document.getElementById('text_value').style.display = "";
            }
       }
}

</script>

<table width="50%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>

<html:form action="${action}" method="post">
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td class="button" colspan="2">

        <app2:securitySubmit operation="${op}" functionality="CAMPAIGNCRITERION" styleClass="button" >
            ${button}
        </app2:securitySubmit>

        <html:cancel styleClass="button" tabindex="11"><fmt:message key="Common.cancel"/></html:cancel>
    </td>
</tr>
<tr>
 <td colspan="2" class="title">
     ${title}
 </td>
</tr>
<tr>
   <td class="label" width="25%">
       <fmt:message key="CampaignCriterion.field"/>
       <fmt:message var="datePattern" key="datePattern"/>
       <html:hidden property="dto(IU_Type)" styleId="IU_Type"/>
       <html:hidden property="dto(categoryId)" styleId="categoryId"/>
       <html:hidden property="dto(campCriterionValueId)" styleId="campCriterionValueId"/>
       <html:hidden property="dto(key)" styleId="key"/>
       <html:hidden property="dto(view)" />
       <html:hidden property="dto(fieldType)" styleId="fieldType" />
       <html:hidden property="dto(fieldName_)" styleId="fieldName_"/>
       <html:hidden property="dto(tableId)" styleId="tableId" />
       <html:hidden property="dto(op)" value="${op}"/>
       <html:hidden property="dto(operator)" styleId="operator"/>
       <html:hidden property="dto(fieldValue)" styleId="fieldValue"/>
       <html:hidden property="dto(string_value)" styleId="string_value"/>
       <html:hidden property="dto(campaignCriterionId)"/>
       <html:hidden property="dto(companyId)"/>
       <html:hidden property="dto(version)"/>
   </td>
    <td class="contain" width="75%">
        <html:hidden property="dto(table)" write="true" />
    </td>
      </tr>
     <tr>
    <!--operator **************************-->
    <td class="label">
        <fmt:message key="Report.operator"/>
    </td>
     <td  class="contain" >

<label id="operatorDate"  ${(campaignCriterionForm.dtoMap.fieldType =='3') ? "":"style=\"display: none;\""}>
           <html:select property="dto(operator_date)" styleId="operator_date" onchange="showIU(this, 3)" styleClass="operatorSelect" readonly="${op =='delete'}">
               <html:options collection="numberList"  property="value" labelProperty="label" />
           </html:select>
</label>
<label id="operatorDecimal"  ${((campaignCriterionForm.dtoMap.fieldType =='1') &&  campaignCriterionForm.dtoMap.IU_Type != '12' &&  campaignCriterionForm.dtoMap.IU_Type != '13') ? "":"style=\"display: none;\""}>
      <html:select property="dto(operator_number)" styleId="operator_number" onchange="showIU(this, 1)" styleClass="operatorSelect" readonly="${op =='delete'}">
          <html:options collection="numberList"  property="value" labelProperty="label"  />
      </html:select>
</label>

<label id="operatorNumber"  ${((campaignCriterionForm.dtoMap.fieldType =='2') &&  campaignCriterionForm.dtoMap.IU_Type != '12' &&  campaignCriterionForm.dtoMap.IU_Type != '13') ? "":"style=\"display: none;\""}>
         <html:select property="dto(operator_decimal)" styleId="operator_decimal" onchange="showIU(this, 2)" styleClass="operatorSelect" readonly="${op =='delete'}">
             <html:options collection="numberList"  property="value" labelProperty="label"  />
         </html:select>
</label>
<label id="operatorText"   ${(campaignCriterionForm.dtoMap.IU_Type =='4' || campaignCriterionForm.dtoMap.IU_Type =='11' || campaignCriterionForm.dtoMap.IU_Type =='14') ? "":"style=\"display: none;\""}>
         <html:select property="dto(operator_text)" styleId="operator_text" onchange="javascript:showIU(this, 4)" styleClass="operatorSelect" readonly="${op =='delete'}">
              <html:options collection="textList"  property="value" labelProperty="label"  />
         </html:select>
</label>
<label id="operatorMultipleSelect" ${(campaignCriterionForm.dtoMap.IU_Type =='9') ? "":"style=\"display: none;\""}>
        <html:select property="dto(operator_multiple)" styleId="operator_multiple" onchange="javascript:showIU_values(${campaignCriterionForm.dtoMap.categoryId},${campaignCriterionForm.dtoMap.campCriterionValueId}, this)" styleClass="operatorSelect" readonly="${op =='delete'}" >
             <html:options collection="selectList"  property="value" labelProperty="label"  />
        </html:select>
</label>
<label id="operator_contactType" ${(campaignCriterionForm.dtoMap.IU_Type =='12') ? "":"style=\"display: none;\""}>
     <html:select property="dto(operator_contactType)" styleId="operator_contactType" onchange="showIU(this, 5)" styleClass="operatorSelect" readonly="${op =='delete'}" >
          <html:options collection="operator_contactType"  property="value" labelProperty="label"  />
     </html:select>
</label>

 <label id="operator_productInUse" ${(campaignCriterionForm.dtoMap.IU_Type =='13') ? "":"style=\"display: none;\""}>
      <html:select property="dto(operator_productInUse)" styleId="operator_productInUse" onchange="showIU(this, 5)" styleClass="operatorSelect" readonly="${op =='delete'}" >
           <html:options collection="operator_productInUse"  property="value" labelProperty="label"  />
      </html:select>
 </label>

 <label id="operator_relationExists" ${(campaignCriterionForm.dtoMap.fieldType == FIELD_RELATIONEXISTS) ? "":"style=\"display: none;\""}>
      <html:select property="dto(operator_relationExists)" styleId="options_relationExists" onchange="javascript:showIU_values(${campaignCriterionForm.dtoMap.categoryId},${campaignCriterionForm.dtoMap.campCriterionValueId}, this)" styleClass="operatorSelect" readonly="${op =='delete'}" >
           <html:options collection="operator_relationExists"  property="value" labelProperty="label"  />
      </html:select>
 </label>

   </td>
    </tr>
    <tr>
             <td class="label" valign="top" >
               <fmt:message key="Campaign.value"/>
            </td>
<!--inputs ***************************************-->

             <td  class="contain" >

<div id="text_value"   ${(campaignCriterionForm.dtoMap.IU_Type =='4' ) ? "":"style=\"display: none;\""}>
     <app:text property="dto(text_value)"  styleClass="mediumText" maxlength="40" styleId="textValue" readonly="${op =='delete'}"/>
</div>
<div id="number_value"   ${(campaignCriterionForm.dtoMap.IU_Type =='1') ? "":"style=\"display: none;\""}>
     <app:numberText property="dto(number_value)" styleClass="numberText" styleId="numberValue" readonly="${op =='delete'}"
                        numberType="integer"  maxlength="10"/>
</div>
<div id="decimalNumber_value"  ${(campaignCriterionForm.dtoMap.IU_Type =='2') ? "":"style=\"display: none;\""}>
     <app:numberText property="dto(decimalNumber_value)" styleClass="decimalNumberValue" maxlength="12" readonly="${op =='delete'}"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
</div>
<div id="date_value"  ${(campaignCriterionForm.dtoMap.IU_Type =='3') ? "":"style=\"display: none;\""}>
        <app:dateText property="dto(date_value)" maxlength="10" styleId="dateValue" readonly="${op =='delete'}"
                               calendarPicker="${op !='delete'}" datePatternKey="${datePattern}" styleClass="dateText"/>
</div>
<div id="betweenNumber_value"   ${(campaignCriterionForm.dtoMap.IU_Type =='5') ? "":"style=\"display: none;\""}>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="dto(numberValue_1)" styleId="numberValue1" styleClass="numberText" maxlength="10" numberType="integer" readonly="${op =='delete'}"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="dto(numberValue_2)" styleId="numberValue2" styleClass="numberText" maxlength="10" numberType="integer" readonly="${op =='delete'}"/>
</div>
<div id="betweenDecimalNumber_value"  ${(campaignCriterionForm.dtoMap.IU_Type =='6') ? "":"style=\"display: none;position=absolute\""}>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="dto(decimalNumberValue_1)" styleId="decimalNumberValue1" styleClass="numberText"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2" readonly="${op =='delete'}"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="dto(decimalNumberValue_2)" styleId="decimalNumberValue2" styleClass="numberText"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2" readonly="${op =='delete'}"/>
</div>
<div id="betweenDate_value"   ${(campaignCriterionForm.dtoMap.IU_Type =='7') ? "":"style=\"display: none;position=absolute\""}>
         <fmt:message key="Common.from"/>
         &nbsp;
         <app:dateText property="dto(dateValue_1)" styleId="dateValue_1" maxlength="10" convert="true" readonly="${op =='delete'}"
                       calendarPicker="${op !='delete'}" datePatternKey="${datePattern}" styleClass="dateText" />
         &nbsp;
         <fmt:message key="Common.to"/>
         &nbsp;
         <app:dateText property="dto(dateValue_2)" styleId="dateValue_2" maxlength="10" convert="true" readonly="${op =='delete'}"
                       calendarPicker="${op !='delete'}" datePatternKey="${datePattern}" styleClass="dateText" />
</div>

<div id="select_values" ${(campaignCriterionForm.dtoMap.IU_Type =='9') ? "":"style=\"display: none;\""}>

</div>

<div id="search_list_partner" ${(campaignCriterionForm.dtoMap.IU_Type =='11') ? "":"style=\"display: none;\""}>

         <html:hidden property="dto(partnerId)" styleId="fieldAddressId_id"/>
         <app:text property="dto(partnerName)" styleClass="mediumText" readonly="true" styleId="fieldAddressName_id"/>
         <tags:selectPopup url="/contacts/SearchAddress.do" name="searchPartner" titleKey="Common.search" hide="${op == 'delete'}"/>
         <tags:clearSelectPopup hide="${op == 'delete'}" keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id" titleKey="Common.clear"/>
</div>

<div id="contact_type" ${(campaignCriterionForm.dtoMap.IU_Type =='12') ? "":"style=\"display: none;\""}>
    <html:select property="dto(code)" styleClass="select" styleId="code" readonly="${op == 'delete'}">
        <html:option value=""/>
        <html:options collection="contactTypeList" property="value" labelProperty="label"/>
    </html:select>
</div>

<div id="product_inUse" ${(campaignCriterionForm.dtoMap.IU_Type =='13') ? "":"style=\"display: none;\""}>
     <html:select property="dto(inUse)" styleClass="select" readonly="${op == 'delete'}">
         <html:option value=""/>
         <html:options collection="productInUserList" property="value" labelProperty="label"/>
     </html:select>
</div>

 <div id="product_name" ${(campaignCriterionForm.dtoMap.IU_Type =='14') ? "":"style=\"display: none;\""}>

     <html:hidden property="dto(productId)" styleId="field_key"/>
     <html:hidden property="dto(1)" styleId="field_versionNumber"/>
     <html:hidden property="dto(2)" styleId="field_unitId"/>
     <html:hidden property="dto(3)" styleId="field_price"/>

     <app:text property="dto(productName)" styleId="field_name" styleClass="mediumText" maxlength="40" readonly="true" tabindex="1"/>

     <tags:selectPopup url="/products/SearchProduct.do" name="SearchProduct" titleKey="Common.search" tabindex="2" hide="${op == 'delete'}"/>
     <tags:clearSelectPopup keyFieldId="field_key" nameFieldId="field_name" titleKey="Common.clear" tabindex="3" hide="${op == 'delete'}"/>

 </div>

<div  id="readOnly_value" ${(campaignCriterionForm.dtoMap.IU_Type =='15') ? "":"style=\"display: none;\""}>
    <c:out value="${readOnlyValue}"/>
</div>

  </td>

 </tr>
    <tr>
        <td class="button" colspan="2">
            <app2:securitySubmit operation="${op}" functionality="CAMPAIGNCRITERION" styleClass="button" >
                 ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="button" tabindex="11" ><fmt:message   key="Common.cancel"/></html:cancel>
        </td>
    </tr>
</table>
</html:form>

  </td>
</tr>
</table>
