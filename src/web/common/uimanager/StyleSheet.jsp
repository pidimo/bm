<%@ page import="java.util.ArrayList,
                 javax.swing.*,
                 com.piramide.elwis.utils.UIManagerConstants"%>
<%@ include file="/Includes.jsp" %>
<%
    request.setAttribute("setDefault",UIManagerConstants.DEFAULT_KEY);
    request.setAttribute("setIsMosaic",UIManagerConstants.IS_MOSAIC_KEY);
    request.setAttribute("setIsColor",UIManagerConstants.IS_COLOR_KEY);
    request.setAttribute("attributeBackground",UIManagerConstants.ATTRIBUTE_BACKGROUND);
%>

<tags:initSelectColorPopup/>

<script language="JavaScript">
<!--
function specialPutSelectColor( nameId, value) {

    if(lib_getObj(nameId) != null)
        lib_getObj(nameId).value = value;
    else if(lib_getObj('hidden_'+ nameId) != null){
            lib_getObj('hidden_'+ nameId).value = value;
            lib_getObj('color_'+ nameId).value = value;
         }

    //preview
    lib_getObj('previewColor_'+ nameId).style.backgroundColor=value;
    lib_getObj('previewColor_'+ nameId).style.display = "";

    searchWindow.close();
}

function putSelectMosaic(nameId, valuePath){
    if(valuePath.length > 0){
        lib_getObj('mosaic_'+ nameId).src = "${pageContext.request.contextPath}"+valuePath;
        lib_getObj( 'hidden_'+ nameId ).value = valuePath;  //hidden for url of image

        lib_getObj('tempMosaic_'+ nameId).value = valuePath;
    }
    searchWindow.close();
}

function lib_getObj(id,d)
 {
    	var i,x;  if(!d) d=document;
    	if(!(x=d[id])&&d.all) x=d.all[id];
    	for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][id];
    	for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=lib_getObj(id,d.layers[i].document);
    	if(!x && document.getElementById) x=document.getElementById(id);
    	return x;
 }

 function checkedRC(checkedId, imputId, tempId){

    if( lib_getObj(checkedId).checked ){
        lib_getObj(imputId).value = lib_getObj('attrValue_'+ tempId).value;

        //update preview
        updatePreviewColor(imputId);  /*this method can be found in initSelectPopup.tag */

        lib_getObj(imputId).style.display = "none";
        lib_getObj(imputId + "_href").style.display = "none";
    }else{
        ///////lib_getObj(imputId).value = "";
        lib_getObj(imputId).style.display = "";
        lib_getObj(imputId + "_href").style.display = "";
    }
 }

 function selectBackgroundKey(id, backgroundId, tempId){
    var selectOptions = lib_getObj(id).options;
    var selected = lib_getObj(id).value;

    // empty values
    //lib_getObj( 'hidden_'+ backgroundId).value = "";  //hidden for url of image
    //lib_getObj('mosaic_'+ backgroundId).src = "";
    //lib_getObj( 'color_'+ backgroundId).value = "";

    for(var i=0; i<selectOptions.length; i++){
        if(selectOptions[i].value == selected){

            if(selectOptions[i].value == '${setIsMosaic}'){
                var tempKey = lib_getObj('tempBackgroundKey_' + backgroundId).value;
                var valuePath = lib_getObj('attrValue_' + tempId).value;
                var tempMosaicPath = lib_getObj('tempMosaic_' + backgroundId).value;

                if (tempKey == '${setIsMosaic}' && valuePath.indexOf('.') != -1 && valuePath.indexOf('#') == -1) {
                    lib_getObj('hidden_' + backgroundId).value = valuePath;
                    lib_getObj('mosaic_' + backgroundId).src = "${pageContext.request.contextPath}" + valuePath;
                } else if (tempMosaicPath != "") {
                    lib_getObj('hidden_' + backgroundId).value = tempMosaicPath;
                    lib_getObj('mosaic_' + backgroundId).src = "${pageContext.request.contextPath}" + tempMosaicPath;
                } else {
                    lib_getObj('hidden_' + backgroundId).value = "";
                }
            }else{
                var valueColor = lib_getObj('attrValue_'+ tempId).value;
                if(valueColor.length <= 7){
                    lib_getObj( 'color_'+ backgroundId).value = valueColor;
                    //update preview
                    specialUpdatePreviewColor(backgroundId);
                }
                lib_getObj( 'hidden_'+ backgroundId).value = lib_getObj( 'color_'+ backgroundId).value;
            }

            lib_getObj(id +"_"+ selectOptions[i].value).style.display = "";      //dispaly

        }else{
            lib_getObj(id +"_"+ selectOptions[i].value).style.display = "none";
        }
    }
 }

 function textColorOnKeyUp(id, backgroundId){
    var colorValue = lib_getObj(id).value;
    lib_getObj( 'hidden_'+ backgroundId).value = colorValue;
    //////////////keyUpPreviewColor(backgroundId);   /*this method can be found in initSelectPopup.tag */
    
    if((colorValue.length == 4 || colorValue.length == 7) && isHexadecimalColor(colorValue)){
        lib_getObj('previewColor_'+ backgroundId).style.backgroundColor = colorValue;
        lib_getObj('previewColor_'+ backgroundId).style.display = "";
    }
 }

 function specialUpdatePreviewColor(backgroundId){

    var colorValue = lib_getObj('color_'+ backgroundId).value;
    if((colorValue.length == 4 || colorValue.length == 7) && isHexadecimalColor(colorValue)){
        lib_getObj('previewColor_'+ backgroundId).style.backgroundColor = colorValue;
        lib_getObj('previewColor_'+ backgroundId).style.display = "";
    }
 }

 function checkedMosaicDefault(checkboxEventId, backgroundId, tempId){

    if( lib_getObj(checkboxEventId +"_r").checked ){

        //select to mosaic
        var selectId = "select_"+ tempId;
        lib_getObj(selectId).value = lib_getObj('tempBackgroundKey_' + backgroundId).value;
        selectBackgroundKey(selectId, backgroundId, tempId);

        lib_getObj(checkboxEventId).style.display = "none";
        lib_getObj(checkboxEventId + "_href").style.display = "none";
    }else{
        lib_getObj(checkboxEventId).style.display = "";
        lib_getObj(checkboxEventId + "_href").style.display = "";
    }
 }
//-->
</script>

   <table cellSpacing=0 cellPadding=3 width="90%" border=0 align="center">
    <tr>
       <td class="button">
            <c:if test="${updateCheckAccessRight == 'true'}">
               <html:submit styleClass="button" property="dto(save)"><c:out value="${button}"/></html:submit>
               <html:submit property="dto(restorePredetermined)" styleClass="button" ><fmt:message key="UIManager.RestorePredetermined"/></html:submit>
            </c:if>
       </td>
    </tr>
   </table>
   <table id="StyleSheet.jsp" border="0" cellpadding="0" cellspacing="0" width="90%" align="center" class="container">
    <tr>
      <td>
        <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">

        <html:hidden property="initStyle" styleId="initStyleId"/>
        <c:forEach var="sectMap" items="${styleSheetForm.mapSection}" varStatus="k" >
            <html:hidden property="styleWrapper(${sectMap.key}).sectionName"/>
            <html:hidden property="styleWrapper(${sectMap.key}).sectionResource"/>
            <html:hidden property="styleWrapper(${sectMap.key}).sectionConfigurable"/>
            <html:hidden property="styleWrapper(${sectMap.key}).isSectionView"/>

            <c:forEach var="listElements" items="${sectMap.value.elements}" varStatus="i" >
                <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].styleId"/>
                <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].elementName"/>
                <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].elementResource"/>
                <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].elementConfigurable"/>
                <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].elementClass"/>

                <%--element childs--%>
                <c:forEach var="listChild" items="${listElements.elementChild}" varStatus="x" >
                    <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].childWrapperDTO[${x.index}].childName"/>
                </c:forEach>

                <c:if test="${sectMap.value.isSectionView && sectMap.value.sectionConfigurable == 'true' && listElements.elementConfigurable == 'true' }">
                    <tr>
                        <td colspan="2" class="title">
                            <fmt:message key="${listElements.elementResource}"/>
                        </td>
                    </tr>
                </c:if>

                <c:forEach var="listAttributes" items="${listElements.attributes}" varStatus="j" >
                    <c:set var="tempId" value="${k.index}_${i.index}_${j.index}"/>

                    <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeId"/>
                    <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeName"/>
                    <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeResource"/>
                    <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeConfigurable"/>
                    <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeXmlValue" styleId="xmlValue_${tempId}"/>
                    <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeValue" styleId="attrValue_${tempId}"/>
                    <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeType"/>
                    <%--<html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeNewValue"/>
                    <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].isDefault"/>--%>
                    <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].isUrl"/>
                    <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeArguments"/>
                    <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].isInherit"/>
                    <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].overwrite"/>

                    <c:choose>
                        <c:when test="${listAttributes.isDefault}">
                            <c:set var="styleView" value="display:none"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="styleView" value=""/>
                        </c:otherwise>
                    </c:choose>

                    <c:choose>
                        <c:when test="${sectMap.value.isSectionView && sectMap.value.sectionConfigurable == 'true' && listElements.elementConfigurable == 'true' && listAttributes.attributeConfigurable == 'true'}">
                        
                            <tr>
                                <td class="label" width="40%" >
                                    <fmt:message key="${listAttributes.attributeResource}"/>
                                </td>

                                <c:choose>
                                    <c:when test="${app2:attributeShowInSelect(listAttributes.attributeName)}">
                                        <td class="contain" width="60%" >
                                            <c:set var="fontConstants" value="${app2:getAttributeValuesForType(listAttributes.attributeType, pageContext.request)}"/>
                                            <html:select property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeNewValue" styleClass="select" tabindex="1" >
                                                <html:option value="${setDefault}"><fmt:message key="UIManager.Default"/></html:option>
                                                <html:options collection="fontConstants"  property="value" labelProperty="label"/>
                                            </html:select>

                                            <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].isDefault"/>
                                        </td>
                                    </c:when>

                                    <c:when test="${listAttributes.attributeName == attributeBackground && listAttributes.isUrl}">
                                        <c:set var="backgroundId" value="background_${tempId}"/>
                                        <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeNewValue" styleId="hidden_${backgroundId}" />

                                        <html:hidden property="tempBackgroundKey" styleId="tempBackgroundKey_${backgroundId}" value="${listAttributes.backgroundKey}"/>
                                        <c:choose>
                                            <c:when test="${listAttributes.backgroundKey == setIsMosaic}">
                                                <c:set var="styleViewMosaic" value=""/>
                                                <c:set var="styleViewColor" value="display:none"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="styleViewColor" value=""/>
                                                <c:set var="styleViewMosaic" value="display:none"/>
                                                <c:set var="textColorValue" value="${listAttributes.attributeNewValue}"/>
                                            </c:otherwise>
                                        </c:choose>

                                        <td class="contain" width="60%">
                                            <c:set var="checkboxEventId" value="check_${tempId}"/>
                                            <table border="0" cellspacing="0px" cellpadding="0px">
                                                <tr style="${styleView}" id="${checkboxEventId}" >
                                                    <td colspan="2" class="contain">
                                                        <c:set var="selectId" value="select_${tempId}"/>
                                                        <html:select property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].backgroundKey" styleId="${selectId}" onchange="javascript:selectBackgroundKey('${selectId}', '${backgroundId}', '${tempId}')" onkeyup="javascript:selectBackgroundKey('${selectId}', '${backgroundId}', '${tempId}')" styleClass="select" tabindex="1" >
                                                            <html:option value="${setIsColor}"><fmt:message key="UIManager.Color"/></html:option>
                                                            <html:option value="${setIsMosaic}"><fmt:message key="UIManager.Image"/></html:option>
                                                        </html:select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="${styleView}" id="${checkboxEventId}_href" >
                                                        <table>
                                                            <tr style="${styleViewMosaic}"  id="${selectId}_${setIsMosaic}">
                                                                <td class="contain">
                                                                    <c:set var="mosaicId" value="mosaic_${backgroundId}"/>
                                                                    <c:url var="urlSelectMosaic"  value="/UIManager/Forward/SelectMosaicPicker.do?attributeType=${app2:encode(listAttributes.attributeType)}"/>

                                                                    <c:choose>
                                                                        <c:when test="${listAttributes.backgroundKey == setIsMosaic && not empty listAttributes.attributeNewValue}">
                                                                            <c:set var="srcMosaic" value="${pageContext.request.contextPath}${listAttributes.attributeNewValue}"/>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <c:set var="srcMosaic" value="${pageContext.request.contextPath}/layout/ui/img/bgnoimage.gif"/>
                                                                        </c:otherwise>
                                                                    </c:choose>

                                                                    <html:hidden property="tempMosaicPath" value="" styleId="tempMosaic_${backgroundId}"/>
                                                                    <img src="${srcMosaic}" id="${mosaicId}" border="0" align="middle" width="80" height="17"/>
                                                                    <a href="javascript:selectColorPopup('${urlSelectMosaic}','${backgroundId}', '210', '200', 'yes')" title="<fmt:message   key="UIManager.SelectMosaic"/>" >
                                                                        <img src="${pageContext.request.contextPath}/layout/ui/img/forecolor.gif" border="0" align="absmiddle" />
                                                                    </a>
                                                                </td>
                                                            </tr>
                                                            <tr style="${styleViewColor}" id="${selectId}_${setIsColor}">
                                                                <td class="contain">
                                                                    <c:set var="colorStyleId" value="color_${backgroundId}"/>

                                                                    <app:text property="textColor" value="${textColorValue}" styleId="${colorStyleId}" onkeyup="javascript:textColorOnKeyUp('${colorStyleId}', '${backgroundId}')"  styleClass="shortText" maxlength="7" tabindex="1" />
                                                                    <tags:selectColorPopup inputKey="${backgroundId}" preview ="true" openerFunctionSet="specialPutSelectColor" openerFunctionPreview="specialUpdatePreviewColor"/>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                    <td class="contain">
                                                        <html:checkbox property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].isDefault" styleId="${checkboxEventId}_r" styleClass="radio" onclick="javasript:checkedMosaicDefault('${checkboxEventId}', '${backgroundId}', '${tempId}')" onkeypress="javasript:checkedMosaicDefault('${checkboxEventId}', '${backgroundId}', '${tempId}')" tabindex="1" /><fmt:message   key="UIManager.Default"/>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </c:when>
                                    <c:when test="${app2:attributeIsOfTypeColor(listAttributes.attributeName,listAttributes.isUrl)}">

                                        <c:set var="colorStyleId" value="color_${tempId}"/>
                                        <td class="contain" width="60%" >
                                            <table border="0" cellspacing="0px" cellpadding="0px">
                                                <tr>
                                                    <td id="${colorStyleId}_href" style="${styleView}" class="contain">
                                                        <app:text property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeNewValue" styleId="${colorStyleId}" style="${styleView}" styleClass="shortText" maxlength="7" tabindex="1" onkeyup="javascript:keyUpPreviewColor('${colorStyleId}')"/>
                                                        <tags:selectColorPopup inputKey="${colorStyleId}" preview ="true"/>
                                                    </td>
                                                    <td class="contain">
                                                        <html:checkbox property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].isDefault" styleId="${colorStyleId}_r" styleClass="radio" onclick="javasript:checkedRC('${colorStyleId}_r', '${colorStyleId}', '${tempId}')" onkeypress="javasript:checkedRC('${colorStyleId}_r', '${colorStyleId}', '${tempId}')" tabindex="1" /><fmt:message   key="UIManager.Default"/>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </c:when>
                                </c:choose>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeNewValue"/>
                            <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].isDefault"/>
                        </c:otherwise>
                    </c:choose>

                </c:forEach>
            </c:forEach>
        </c:forEach>
        </table>
      </td>
    </tr>
  </table>
  <table cellSpacing=0 cellPadding=3 width="90%" border=0 align="center">
    <tr>
        <td class="button">
            <c:if test="${updateCheckAccessRight == 'true'}">
                <html:submit styleClass="button"  property="dto(save)" tabindex="1" ><c:out value="${button}"/></html:submit>
                <html:submit property="dto(restorePredetermined)"  styleClass="button" tabindex="1" ><fmt:message key="UIManager.RestorePredetermined"/></html:submit>
            </c:if>
        </td>
    </tr>
  </table>


