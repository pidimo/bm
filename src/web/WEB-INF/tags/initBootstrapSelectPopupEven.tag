<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ attribute name="fields"%>
<script language="JavaScript">
    <!--
    function selectMultipleEvenField(${fields}){

        <c:forTokens items="${fields}" delims="," var="field">
        document.getElementById('${fn:trim(field)}').value = unescape(${field});
        </c:forTokens>

        //close popup
        hideBootstrapPopup();

        if(autoSubmit == 'true') {
            document.forms[0].submit();
        }
    }
    //-->
</script>
