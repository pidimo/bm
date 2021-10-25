<%@ tag import="com.piramide.elwis.utils.ContactConstants" %>
<%@ tag import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ attribute name="align" required="false" description="The alignment of the pull down menu left or right" %>
<%@ attribute name="titleKey" required="true" description="menu title's resource key" %>
<c:set var="pullDownMenuUniqueId" value="${app2:getCurrentTimeMillis()}"/>
<div class="pullDownMenuRoot" id="${pullDownMenuUniqueId}"
     style="z-index:100;padding-left:3px;padding-right:5px;vertical-align:top;">
    <fmt:message key="${titleKey}"/>
</div>
<div id="${pullDownMenuUniqueId}div" class="pullDownMenu" style="z-index:100;">
    <jsp:doBody/>
</div>
<iframe name="pulldownIframe" id="${pullDownMenuUniqueId}ifr" scrolling="no" frameborder="0"
        style="position:absolute;display:none;visibility:hidden;" src="javascript:false">
</iframe>
<c:if test="${empty canPullDownMenuBeShown}">
    <script language="JavaScript">
        document.getElementById("${pullDownMenuUniqueId}").style.display = "none";
    </script>
</c:if>
<script language="JavaScript" type="text/javascript">
    menu${pullDownMenuUniqueId} = new PullDownMenu("${pullDownMenuUniqueId}", "${(not empty align) ? align : 'left'}");
</script>
