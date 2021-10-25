<%--
Tag to manage sexy combo plugin.

Attributes:
targetStyleId -> the DOM id of select tag
inputName -> text input dto name
hiddenName -> hidden dto name, this is to save the value of tag select
maxlength -> text input max length
enableSexyCombo -> boolean type true or false
--%>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

<%@ attribute name="targetStyleId" required="true" %>
<%@ attribute name="inputName" required="true" %>
<%@ attribute name="hiddenName" required="true" %>
<%@ attribute name="maxlength"%>
<%@ attribute name="enableSexyCombo" required="true" type="java.lang.Boolean"%>

<script language="JavaScript" src="<c:url value="/js/cacheable/bootstrap/sexycombojqueryplugin/jquery.bgiframe.3.0.1.js"/>" type="text/javascript"></script>
<script language="JavaScript" src="<c:url value="/js/cacheable/bootstrap/sexycombojqueryplugin/jquery-jatun-sexy-combo-1.0.4.1.js"/>" type="text/javascript"></script>

<c:if test="${enableSexyCombo}">
    <script language="JavaScript" type="text/javascript">

        $(document).ready(function() {

            $('#'+'${targetStyleId}').sexyCombo({
                triggerSelected: true, //to load as initial values of combo the selected value
                suffix: "${inputName}",
                hiddenSuffix: "${hiddenName}",
                maxlength: "${not empty maxlength ? maxlength : 50}"
            });
        });

    </script>
</c:if>

