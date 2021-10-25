<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<%@ attribute name="name"%><%@ attribute name="hide"%><%@ attribute name="titleKey"%><%@ attribute name="width"%><%@ attribute name="heigth"%><%@ attribute name="scrollbars"%>
<%@ attribute name="imgPath"%><%@ attribute name="imgWidth"%><%@ attribute name="imgHeight"%>
<%@ attribute name="inputKey"%><%@ attribute name="openerFunctionSet"%><%@ attribute name="openerFunctionPreview"%><%@ attribute name="preview"%>
<c:if test="${!hide}">
    <c:set var="paramInputKey" value="inputKey=${inputKey}"/>
    <c:set var="paramOpenerFunction" value="function=a"/>
    <c:if test="${openerFunctionSet != null}">
        <c:set var="paramOpenerFunction" value="function=${openerFunctionSet}"/>
    </c:if>
    <c:url var="urlPopup" value="/common/uimanager/SelectColor.jsp?${paramInputKey}&${paramOpenerFunction}"/>

    <c:if test="${preview != null && preview == 'true'}">
        <%--<input type="text" name="preview" id="previewColor_${inputKey}" readonly="true" size="1" style="display:none; border:0;">--%>
        <span id="previewColor_${inputKey}" style="display:none; width:20; height:12; vertical-align: middle; border: 1px #555555 solid">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
    </c:if>
    <%--tam 250 * 198  --%>
    <a href="javascript:selectColorPopup('${urlPopup}', '${(name != null) ? name : 'selectColor'}' , ${width != null ? width : 250}, ${(heigth != null) ? heigth : 198},${(scrollbars != null) ? scrollbars: '1'});" title="<fmt:message   key="${(titleKey != null)? titleKey : 'UIManager.SelectColor'}"/>">
        <img src="<c:out value="${sessionScope.baselayout}"/>${(imgPath != null) ? imgPath : '/img/forecolor.gif'}" border="0" align="absmiddle" width="${(imgWidth != null) ? imgWidth : 20 }" height="${(imgHeight != null) ? imgHeight : 20 }"/>
    </a>

    <%--functions to update preview color--%>
    <c:choose>
        <c:when test="${openerFunctionPreview != null}">
            <script language="javascript" >
            <!--
                ${openerFunctionPreview}('${inputKey}');
            //-->
            </script>
        </c:when>
        <c:otherwise>
            <script language="javascript" >
            <!--
                updatePreviewColor('${inputKey}');
            //-->
            </script>
        </c:otherwise>
    </c:choose>
</c:if>


