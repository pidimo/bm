<%@ include file="/Includes.jsp" %>

<!-- Birth Day tag-->
    <c:set var="birthday">
    <fmt:message   key="Contact.birthDayList"/>
    </c:set>
    <c:set var="search">
    <fmt:message   key="Common.search"/>
    </c:set>
<!------------------------------- INICIO ------------------------------------>
      <table width="100%" height="100%" border="0" align="center" cellpadding="0" cellspacing="0" >

      <tr>
      <td height="15">
          <img height="1" width="1"  src="<c:out value="${sessionScope.baselayout}"/>/img/sp.gif" alt="">
      </td>
      </tr>

      <tr>
      <td height="20" class="title">
          <fmt:message    key="Common.home"/>
      </td>
      </tr>

      <tr>
        <td class="principal">


      <table width="100%"  height="100%" border="0" align="center" cellpadding="3" cellspacing="1" class="container">
      <tr>
      <td bgcolor="#FFFFff" valign="top">

                <table cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
                <tr><td valign="top" class="principal">
                        <table cellpadding="0" cellspacing="0" width="100%">
                        <tr><td width="150">
                        <img height="105" width="100"  src="<c:out value="${sessionScope.baselayout}"/>/img/desktop/computer.jpg" alt="">
                        </td>
                        <td>
                            <font color="#63656A">
                            <jsp:useBean id="now" class="java.util.Date"/>
                            <img alt=""  src="<c:out value="${sessionScope.baselayout}"/>/img/logos/menulogo.gif"/>
                            <br>
                            <fmt:formatDate value="${now}" type="date"  dateStyle="full"/>
                            <br>
                            </font>
                        </td>
                        </tr>
                        </table>
                </td>
                <td width="3" ><img src="<c:out value="${sessionScope.baselayout}"/>/img/sp.gif" width="1" height="1" alt=""></td>
                <td class="principal" width="300" > &nbsp;</td>
                <td width="3" ><img src="<c:out value="${sessionScope.baselayout}"/>/img/sp.gif" width="1" height="1" alt=""></td>
                <td width="1" bgcolor="#90ADB1" ><img src="<c:out value="${sessionScope.baselayout}"/>/img/sp.gif" width="1" height="1" alt=""></td>
                <td width="3" ><img src="<c:out value="${sessionScope.baselayout}"/>/img/sp.gif" width="1" height="1" alt=""></td>
                <td width="300" valign="top" align="left" class="principal" >
                    <img src="<c:out value="${sessionScope.baselayout}"/>/img/birthday.gif" alt="">
                </td>
                 </tr>
                </table>
    </td>
      </tr>
      </table>

      </td></tr>

      </table>
<!------------------------------- FIN ------------------------------------>