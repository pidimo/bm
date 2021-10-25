<%@ page import="java.util.Map"%><title>Command Test Page</title>
<link rel="STYLESHEET" type="text/css" href="<%= request.getContextPath() %>/style.css">
<%
   Map dto = (Map) request.getAttribute("dto");
   if(dto!=null){
    if("ok".equals((String)dto.get("result")))
        out.println("<br><h2>OK<h2>");
        else
        out.println("<br><h2>ERROR<h2>");
   }
%>

<table width="100%" height="80%" border="0" bgcolor="ffffff"  >
  <tr>
    <td height="10"><center><h3>Command Tester</h3></center></td>
  </tr>
  <tr>
    <td> <center>
    <form name="form1" method="post" action="test.do">
      <table width="600" border="0" class="container">
        <tr>
          <td width="109"  class="title" >Command</td>
          <td width="600" colspan="2" class="contain">com.piramide.elwis.cmd.<input type="text" name="command" size="40" class="largeText"  >Cmd</td>
        </tr>
         <tr>
          <td class="label" >companyId</td>
          <td class="contain" ><input type="hidden" name="param" value="companyId"> <input type="text" name="value" value="1" ></td>
          <td class="contain">&nbsp;</td>
        </tr>
        <tr>
          <td  class="label"> Param1</td>
          <td class="contain" ><input type="text" name="param"></td>
          <td class="contain" ><input type="text" name="value"></td>
        </tr>
        <tr>
          <td class="label">Param2</td>
          <td class="contain"><input type="text" name="param"></td>
          <td class="contain" ><input type="text" name="value"></td>
        </tr>
        <tr>
          <td class="label">Param3</td>
          <td class="contain"><input type="text" name="param"></td>
          <td class="contain" ><input type="text" name="value"></td>
        </tr>
        <tr>
          <td class="label">Param4</td>
          <td class="contain"><input type="text" name="param"></td>
          <td class="contain"><input type="text" name="value"></td>
        </tr>
        <tr>
          <td class="label">Param5</td>
          <td class="contain"><input type="text" name="param"></td>
          <td class="contain"><input type="text" name="value"></td>
        </tr>
        <tr>
          <td class="label">Param6</td>
          <td class="contain"><input type="text" name="param"></td>
          <td class="contain"><input type="text" name="value"></td>
        </tr>
        <tr>
          <td class="label">Param7</td>
          <td class="contain"><input type="text" name="param"></td>
          <td class="contain"><input type="text" name="value"></td>
        </tr>
        <tr>
          <td class="listHeader">&nbsp;</td>
          <td > <input type="submit" name="Submit" value="Test" class="button"></td>
          <td> <input type="reset" name="reset" value="Clear" class="button"></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td colspan="2">&nbsp;</td>
        </tr>
      </table>
      <p>&nbsp;</p>
      <p>&nbsp;</p>
    </form></center></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
