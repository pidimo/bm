<%@ include file="/Includes.jsp" %>

<script language="javascript" type="text/javascript">
<!--
    function selectColor() {
        var arrayMosaic = document.getElementById("listMosaicForm").idMosaicRadio; //Array content radio buttons
        var namePage = window.name;
        var pathMosaic = "";

        if(arrayMosaic != null){
            if(arrayMosaic.length > 0){
                for (var y=0; y < arrayMosaic.length; y++) {
                  if (arrayMosaic[y].checked ) {
                        pathMosaic = arrayMosaic[y].value;
                        break;
                  }
                }
            }
            else{
                  if (arrayMosaic.checked )
                    pathMosaic = arrayMosaic.value;
           }
       }

       opener.putSelectMosaic(namePage, pathMosaic);
   }

   window.focus();

//-->
</script>
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="1" class="container">

<tr>
<td valing="top" align="center">

    <html:form action="/UIManager/Forward/SelectMosaicPicker" styleId="listMosaicForm" >
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="container">

        <c:set var="mosaicConstants" value="${app2:getAttributeValuesForType(param['attributeType'], pageContext.request)}"/>
        <c:forEach var="listMosaic" items="${mosaicConstants}">
            <c:if test="${not empty listMosaic.value && fn:contains(listMosaic.value,'.')}">
                <tr>
                    <td class="contain">
                        <input type="radio" name="radioMosaic" value="${listMosaic.value}" class="radio" id="idMosaicRadio">
                        <img border="0" src="${pageContext.request.contextPath}${listMosaic.value}" width="80" height="17" />
                    </td>
                </tr>
            </c:if>
        </c:forEach>
    </table>
    <table cellSpacing=0 cellPadding=2 width="100%" border=0 align="center">
        <TR>
            <TD class="button">
                <html:button property="insert" styleId="insert" styleClass="button" style="margin-top:3px" onclick="selectColor();"><fmt:message key="Common.select"/></html:button>
            </TD>
        </TR>
    </table>
    </html:form>

</td>
</tr>
</table>
