<%@ include file="/Includes.jsp" %>

<c:set var="emailList" value="${employeeEmailList}"/>
<html:select property="dto(employeeMail)" styleClass="middleSelect ${app2:getFormSelectClasses()}" tabindex="9" value="${predeterminedValue}" >
    <html:options collection="emailList" property="value" labelProperty="label"/>
</html:select>
