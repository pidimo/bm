<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/Includes.jsp" %>
<html>
<head>
    <!--<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Expires" CONTENT="-1">-->
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>
        <fmt:message key="Template.Title.preview"/>
    </title>

    <style type="text/css">
        BODY {
            background-color: #ffffff;
        }
    </style>
</head>

<script type="text/javascript">
    function getFocus() {
        window.focus();
    }
</script>

<body style="margin-left:0px;margin-right:0px;margin-bottom:10;margin-top:10px;" onload="getFocus();">
<table width="100%">
    <tr>
        <td>
            &nbsp;
        </td>
    </tr>
    <tr>
        <td width="100%" style="text-align:right;font-family: Verdana, Helvetica, sans-serif; font-size: 11px;">
            <a href="javascript:window.close();" title="<fmt:message key="Common.close"/>">
                <fmt:message key="Common.close"/>
            </a>
        </td>
    </tr>
</table>

<table width="100%" border="0">
    <tr>
        <td>
            ${dto.template}
        </td>
    </tr>
</table>


</body>
</html>

