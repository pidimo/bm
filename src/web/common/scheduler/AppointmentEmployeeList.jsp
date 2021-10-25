<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/scheduler/scheduler.jsp"/>

<table width="60%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
  <td>

<html:form action="/Appointment/Select.do" >
<br>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
<td height="20" class="title" colspan="4" >
    <fmt:message key="Appointment.employeeList"/>
</td>
</tr>
<tr>
    <td>

<fanta:select property="list1" listName="employeeBaseList" size="20" multiple="true"
    labelProperty="employeeName" valueProperty="employeeId" styleClass="multipleSelect"
    readOnly="${op == 'delete'}"  module="/contacts" tabIndex="6" value="${sessionScope.user.valueMap['userId']}" >
<fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
</fanta:select>

    </td>
    <td class="label" >

<input type="button" value="  &gt;  "
    onclick="move(this.form.list1,this.form.array2)" name="B1" class="button"/><br><br>
<input type="button" value=" &gt;&gt; "
    onclick="alls(this.form.list1,this.form.array2)" name="B11" class="button"/><br><br>
<input type="button" value="  &lt;  "
    onclick="move(this.form.array2,this.form.list1)" name="B2" class="button"/> <br>
<input type="button" value=" &lt;&lt; "
    onclick="alls(this.form.array2,this.form.list1)" name="B22" class="button"/>

    </td>
    <td class="contain" >
        <html:select property="array2"  size="20" multiple="true"  styleClass="multipleSelect" />
    </td>
  </tr>
<TR>
<TD class="button" colspan="4">

<html:submit styleClass="button" onclick="send(array2)" ><fmt:message  key="Common.save"/></html:submit>
<html:cancel  styleClass="button" onclick="window.close()"><fmt:message  key="Common.cancel"/></html:cancel>

</td>
  </tr>
</table>

</html:form>
     </td>
 </tr>
</table>