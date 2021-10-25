<%@ include file="/Includes.jsp" %>

{
<c:import url="/bmapp/layout/json/generalerrorJson.jsp"/>

"forward": "${forwardJson}",

"mainData": <c:import url="${body}"/>
}
