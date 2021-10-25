<%@ page import="com.piramide.elwis.utils.UIManagerConstants" %>
<%@ include file="/Includes.jsp" %>
<%
    request.setAttribute("setDefault", UIManagerConstants.DEFAULT_KEY);
    request.setAttribute("setIsMosaic", UIManagerConstants.IS_MOSAIC_KEY);
    request.setAttribute("setIsColor", UIManagerConstants.IS_COLOR_KEY);
    request.setAttribute("attributeBackground", UIManagerConstants.ATTRIBUTE_BACKGROUND);

    request.setAttribute("VALUE_SEPARATOR", UIManagerConstants.VALUE_SEPARATOR_KEY);
%>

<tags:initJQueryMiniColors/>

<script language="JavaScript">
    <!--
    function specialPutSelectColor(nameId, value) {

        if (lib_getObj(nameId) != null)
            lib_getObj(nameId).value = value;
        else if (lib_getObj('hidden_' + nameId) != null) {
            lib_getObj('hidden_' + nameId).value = value;
            lib_getObj('color_' + nameId).value = value;
        }

        //preview
        lib_getObj('previewColor_' + nameId).style.backgroundColor = value;
        lib_getObj('previewColor_' + nameId).style.display = "";

        searchWindow.close();
    }

    function putSelectMosaic(nameId, valuePath) {
        if (valuePath.length > 0) {
            lib_getObj('mosaic_' + nameId).src = "${pageContext.request.contextPath}" + valuePath;
            lib_getObj('hidden_' + nameId).value = valuePath;  //hidden for url of image

            lib_getObj('tempMosaic_' + nameId).value = valuePath;
        }
        searchWindow.close();
    }

    function lib_getObj(id, d) {
        var i, x;
        if (!d) d = document;
        if (!(x = d[id]) && d.all) x = d.all[id];
        for (i = 0; !x && i < d.forms.length; i++) x = d.forms[i][id];
        for (i = 0; !x && d.layers && i < d.layers.length; i++) x = lib_getObj(id, d.layers[i].document);
        if (!x && document.getElementById) x = document.getElementById(id);
        return x;
    }

    function checkedRC(checkedId, imputId, tempId) {

        if (lib_getObj(checkedId).checked) {
            lib_getObj(imputId).value = lib_getObj('attrValue_' + tempId).value;

            lib_getObj(imputId).style.display = "none";
            lib_getObj(imputId + "_href").style.display = "none";

            setMiniColorValue(imputId, lib_getObj(imputId).value);
        } else {
            ///////lib_getObj(imputId).value = "";
            lib_getObj(imputId).style.display = "";
            lib_getObj(imputId + "_href").style.display = "";
        }
    }

    function selectBackgroundKey(id, backgroundId, tempId) {
        var selectOptions = lib_getObj(id).options;
        var selected = lib_getObj(id).value;

        // empty values
        //lib_getObj( 'hidden_'+ backgroundId).value = "";  //hidden for url of image
        //lib_getObj('mosaic_'+ backgroundId).src = "";
        //lib_getObj( 'color_'+ backgroundId).value = "";

        for (var i = 0; i < selectOptions.length; i++) {
            if (selectOptions[i].value == selected) {

                if (selectOptions[i].value == '${setIsMosaic}') {
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
                } else {
                    var valueColor = lib_getObj('attrValue_' + tempId).value;
                    if (valueColor.length <= 7) {
                        lib_getObj('color_' + backgroundId).value = valueColor;
                        //update preview
                        specialUpdatePreviewColor(backgroundId);
                    }
                    lib_getObj('hidden_' + backgroundId).value = lib_getObj('color_' + backgroundId).value;
                }

                lib_getObj(id + "_" + selectOptions[i].value).style.display = "";      //dispaly

            } else {
                lib_getObj(id + "_" + selectOptions[i].value).style.display = "none";
            }
        }
    }

    function textColorOnKeyUp(id, backgroundId) {
        var colorValue = lib_getObj(id).value;
        lib_getObj('hidden_' + backgroundId).value = colorValue;
        //////////////keyUpPreviewColor(backgroundId);   /*this method can be found in initSelectPopup.tag */

        if ((colorValue.length == 4 || colorValue.length == 7) && isHexadecimalColor(colorValue)) {
            lib_getObj('previewColor_' + backgroundId).style.backgroundColor = colorValue;
            lib_getObj('previewColor_' + backgroundId).style.display = "";
        }
    }

    function specialUpdatePreviewColor(backgroundId) {

        var colorValue = lib_getObj('color_' + backgroundId).value;
        if ((colorValue.length == 4 || colorValue.length == 7) && isHexadecimalColor(colorValue)) {
            lib_getObj('previewColor_' + backgroundId).style.backgroundColor = colorValue;
            lib_getObj('previewColor_' + backgroundId).style.display = "";
        }
    }

    function checkedMosaicDefault(checkboxEventId, backgroundId, tempId) {

        if (lib_getObj(checkboxEventId + "_r").checked) {

            //select to mosaic
            var selectId = "select_" + tempId;
            lib_getObj(selectId).value = lib_getObj('tempBackgroundKey_' + backgroundId).value;
            selectBackgroundKey(selectId, backgroundId, tempId);

            lib_getObj(checkboxEventId).style.display = "none";
            lib_getObj(checkboxEventId + "_href").style.display = "none";
        } else {
            lib_getObj(checkboxEventId).style.display = "";
            lib_getObj(checkboxEventId + "_href").style.display = "";
        }
    }

    function composeGradientValue(color1InputId, color2InputId, tempId) {
        lib_getObj("gradientValue_" + tempId).value = lib_getObj(color1InputId).value + '${VALUE_SEPARATOR}' + lib_getObj(color2InputId).value;
    }

    function splitGradientValue(color1InputId, color2InputId, tempId) {
        var composedValue = lib_getObj("gradientValue_" + tempId).value;
        var colorValues = composedValue.split('${VALUE_SEPARATOR}');

        if(colorValues.length == 2) {
            lib_getObj(color1InputId).value = colorValues[0];
            lib_getObj(color2InputId).value = colorValues[1];

            setMiniColorValue(color1InputId, colorValues[0]);
            setMiniColorValue(color2InputId, colorValues[1]);
        }
    }

    function checkedDefaultGradient(checkedId, color1InputId, color2InputId, tempId) {

        if (lib_getObj(checkedId).checked) {
            var defaultValue = lib_getObj('attrValue_' + tempId).value;

            lib_getObj("gradientValue_" + tempId).value = defaultValue;

            lib_getObj(color1InputId).style.display = "none";
            lib_getObj(color1InputId + "_href").style.display = "none";

            lib_getObj(color2InputId).style.display = "none";
            lib_getObj(color2InputId + "_href").style.display = "none";

            splitGradientValue(color1InputId, color2InputId, tempId);
        } else {

            lib_getObj(color1InputId).style.display = "";
            lib_getObj(color1InputId + "_href").style.display = "";
            lib_getObj(color2InputId).style.display = "";
            lib_getObj(color2InputId + "_href").style.display = "";
        }
    }


    //-->
</script>

<div>
    <c:if test="${updateCheckAccessRight == 'true'}">
        <html:submit styleClass="${app2:getFormButtonClasses()} marginButton" property="dto(save)">
            <c:out value="${button}"/>
        </html:submit>
        <html:submit property="dto(restorePredetermined)" styleClass="${app2:getFormButtonClasses()} marginButton">
            <fmt:message key="UIManager.RestorePredetermined"/>
        </html:submit>
    </c:if>
</div>

<div id="StyleSheet.jsp">
    <html:hidden property="initStyle" styleId="initStyleId"/>
    <c:forEach var="sectMap" items="${styleSheetForm.mapSection}" varStatus="k">
        <html:hidden property="styleWrapper(${sectMap.key}).sectionName"/>
        <html:hidden property="styleWrapper(${sectMap.key}).sectionResource"/>
        <html:hidden property="styleWrapper(${sectMap.key}).sectionConfigurable"/>
        <html:hidden property="styleWrapper(${sectMap.key}).isSectionView"/>

        <c:forEach var="listElements" items="${sectMap.value.elements}" varStatus="i">
            <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].styleId"/>
            <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].elementName"/>
            <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].elementResource"/>
            <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].elementConfigurable"/>
            <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].elementClass"/>

            <%--element childs--%>
            <c:forEach var="listChild" items="${listElements.elementChild}" varStatus="x">
                <html:hidden
                        property="styleWrapper(${sectMap.key}).element[${i.index}].childWrapperDTO[${x.index}].childName"/>
            </c:forEach>

            <c:if test="${sectMap.value.isSectionView && sectMap.value.sectionConfigurable == 'true' && listElements.elementConfigurable == 'true' }">
                <fieldset>
                    <legend class="title">
                        <fmt:message key="${listElements.elementResource}"/>
                    </legend>
                </fieldset>
            </c:if>

            <c:forEach var="listAttributes" items="${listElements.attributes}" varStatus="j">
                <c:set var="tempId" value="${k.index}_${i.index}_${j.index}"/>

                <html:hidden
                        property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeId"/>
                <html:hidden
                        property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeName"/>
                <html:hidden
                        property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeResource"/>
                <html:hidden
                        property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeConfigurable"/>
                <html:hidden
                        property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeXmlValue"
                        styleId="xmlValue_${tempId}"/>
                <html:hidden
                        property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeValue"
                        styleId="attrValue_${tempId}"/>
                <html:hidden
                        property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeType"/>
                <%--<html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeNewValue"/>
                <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].isDefault"/>--%>
                <html:hidden
                        property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].isUrl"/>
                <html:hidden
                        property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeArguments"/>
                <html:hidden
                        property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].isInherit"/>
                <html:hidden
                        property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].overwrite"/>

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
                        <div class="form-group">
                            <label class="control-label col-xs-11 col-sm-4">
                                <fmt:message key="${listAttributes.attributeResource}"/>
                            </label>

                            <div class="parentElementInputSearch col-xs-11 col-sm-7">
                                <c:choose>
                                    <c:when test="${app2:attributeShowInSelect(listAttributes.attributeName)}">
                                        <c:set var="fontConstants"
                                               value="${app2:getAttributeValuesForType(listAttributes.attributeType, pageContext.request)}"/>
                                        <html:select
                                                property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeNewValue"
                                                styleClass="${app2:getFormSelectClasses()} select" tabindex="1">
                                            <html:option value="${setDefault}">
                                                <fmt:message key="UIManager.Default"/>
                                            </html:option>
                                            <html:options collection="fontConstants" property="value"
                                                          labelProperty="label"/>
                                        </html:select>

                                        <html:hidden
                                                property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].isDefault"/>

                                    </c:when>

                                    <c:when test="${app2:attributeIsOfTypeGradient(listAttributes.attributeName, listAttributes.attributeType)}">
                                        <c:set var="color1StyleId" value="color1_${tempId}"/>
                                        <c:set var="color2StyleId" value="color2_${tempId}"/>

                                        <c:set var="value1" value="#ffffff"/>
                                        <c:set var="value2" value="#ffffff"/>
                                        <c:if test="${not empty listAttributes.attributeNewValue}">

                                            <c:set var="colorValues" value="${fn:split(listAttributes.attributeNewValue, VALUE_SEPARATOR)}" />
                                            <c:if test="${fn:length(colorValues) eq 2}">
                                                <c:set var="value1" value="${colorValues[0]}"/>
                                                <c:set var="value2" value="${colorValues[1]}"/>
                                            </c:if>
                                        </c:if>

                                        <html:hidden property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeNewValue"
                                                     styleId="gradientValue_${tempId}"/>

                                        <div class="row">
                                            <div id="${color1StyleId}_href" style="${styleView}"
                                                 class="col-xs-12 col-sm-4">
                                                <tags:jQueryMiniColors
                                                        property="color1Text"
                                                        value="${value1}"
                                                        styleId="${color1StyleId}"
                                                        style="${styleView}"
                                                        tabIndex="1"
                                                        onHideJSFunction="composeGradientValue('${color1StyleId}', '${color2StyleId}', '${tempId}')"
                                                        view="false"/>
                                            </div>
                                            <div id="${color2StyleId}_href" style="${styleView}"
                                                 class="col-xs-12 col-sm-4">
                                                <tags:jQueryMiniColors
                                                        property="color2Text"
                                                        value="${value2}"
                                                        styleId="${color2StyleId}"
                                                        style="${styleView}"
                                                        tabIndex="1"
                                                        onHideJSFunction="composeGradientValue('${color1StyleId}', '${color2StyleId}', '${tempId}')"
                                                        view="false"/>
                                            </div>
                                            <div class="col-xs-12 col-sm-4">
                                                <div class="radiocheck">
                                                    <div class="checkbox checkbox-default">
                                                        <html:checkbox
                                                                property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].isDefault"
                                                                styleId="${color1StyleId}_r"
                                                                onclick="javasript:checkedDefaultGradient('${color1StyleId}_r', '${color1StyleId}', '${color2StyleId}', '${tempId}')"
                                                                onkeypress="javasript:checkedDefaultGradient('${color1StyleId}_r', '${color1StyleId}', '${color2StyleId}', '${tempId}')"
                                                                tabindex="1"/>
                                                        <label for="${color1StyleId}_r"><fmt:message
                                                                key="UIManager.Default"/></label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                    </c:when>

                                    <c:when test="${app2:attributeIsOfTypeBoxShadow(listAttributes.attributeName, listAttributes.attributeType)}">
                                        <c:set var="colorStyleId" value="color_${tempId}"/>
                                        <div class="row">
                                            <div id="${colorStyleId}_href" style="${styleView}"
                                                 class="col-xs-12 col-sm-6">
                                                <tags:jQueryMiniColors
                                                        property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeNewValue"
                                                        styleId="${colorStyleId}"
                                                        style="${styleView}"
                                                        tabIndex="1"
                                                        isFormatRGBA="true"
                                                        view="false"/>
                                            </div>
                                            <div class="col-xs-12 col-sm-4">
                                                <div class="radiocheck">
                                                    <div class="checkbox checkbox-default">
                                                        <html:checkbox
                                                                property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].isDefault"
                                                                styleId="${colorStyleId}_r"
                                                                onclick="javasript:checkedRC('${colorStyleId}_r', '${colorStyleId}', '${tempId}')"
                                                                onkeypress="javasript:checkedRC('${colorStyleId}_r', '${colorStyleId}', '${tempId}')"
                                                                tabindex="1"/>
                                                        <label for="${colorStyleId}_r"><fmt:message
                                                                key="UIManager.Default"/></label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:when>

                                    <c:when test="${app2:attributeIsOfTypeColor(listAttributes.attributeName,listAttributes.isUrl)}">
                                        <c:set var="colorStyleId" value="color_${tempId}"/>
                                        <div class="row">
                                            <div id="${colorStyleId}_href" style="${styleView}"
                                                 class="col-xs-12 col-sm-5">
                                                <tags:jQueryMiniColors
                                                        property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeNewValue"
                                                        styleId="${colorStyleId}"
                                                        style="${styleView}"
                                                        tabIndex="1"
                                                        view="false"/>
                                            </div>
                                            <div class="col-xs-12 col-sm-4">
                                                <div class="radiocheck">
                                                    <div class="checkbox checkbox-default">
                                                        <html:checkbox
                                                                property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].isDefault"
                                                                styleId="${colorStyleId}_r"
                                                                onclick="javasript:checkedRC('${colorStyleId}_r', '${colorStyleId}', '${tempId}')"
                                                                onkeypress="javasript:checkedRC('${colorStyleId}_r', '${colorStyleId}', '${tempId}')"
                                                                tabindex="1"/>
                                                        <label for="${colorStyleId}_r"><fmt:message
                                                                key="UIManager.Default"/></label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:when>
                                </c:choose>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <html:hidden
                                property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].attributeNewValue"/>
                        <html:hidden
                                property="styleWrapper(${sectMap.key}).element[${i.index}].attribute[${j.index}].isDefault"/>
                    </c:otherwise>
                </c:choose>

            </c:forEach>
        </c:forEach>
    </c:forEach>
</div>

<div>
    <c:if test="${updateCheckAccessRight == 'true'}">
        <html:submit styleClass="${app2:getFormButtonClasses()} marginButton" property="dto(save)" tabindex="1">
            <c:out value="${button}"/>
        </html:submit>
        <html:submit property="dto(restorePredetermined)" styleClass="${app2:getFormButtonClasses()} marginButton"
                     tabindex="1">
            <fmt:message key="UIManager.RestorePredetermined"/>
        </html:submit>
    </c:if>
</div>


