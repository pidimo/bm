<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/webmail/compose.jsp"/>


<c:set var="phone" value="<%= com.piramide.elwis.utils.CommunicationTypes.PHONE %>" scope="request"/>
<c:set var="meeting" value="<%= com.piramide.elwis.utils.CommunicationTypes.MEETING %>" scope="request"/>
<c:set var="fax" value="<%= com.piramide.elwis.utils.CommunicationTypes.FAX %>" scope="request"/>
<c:set var="letter" value="<%= com.piramide.elwis.utils.CommunicationTypes.LETTER %>" scope="request"/>
<c:set var="other" value="<%= com.piramide.elwis.utils.CommunicationTypes.OTHER %>" scope="request"/>
<c:set var="email" value="<%= com.piramide.elwis.utils.CommunicationTypes.EMAIL%>" scope="request"/>
<c:set var="document" value="<%= com.piramide.elwis.utils.CommunicationTypes.DOCUMENT%>" scope="request"/>

<c:set var="BODY_TYPE_HTML" value="<%=com.piramide.elwis.utils.WebMailConstants.BODY_TYPE_HTML%>" scope="request"/>
<c:set var="BODY_TYPE_TEXT" value="<%=com.piramide.elwis.utils.WebMailConstants.BODY_TYPE_TEXT%>" scope="request"/>
<c:set var="highPriority" value="<%=com.piramide.elwis.utils.WebMailConstants.MAIL_PRIORITY_HIGHT%>" scope="request"/>
<c:set var="defaultPriority" value="<%=com.piramide.elwis.utils.WebMailConstants.MAIL_PRIORITY_DEFAULT%>"
       scope="request"/>
<%--
<c:set var="REDIRECT_ANSWERED" value="<%=com.piramide.elwis.utils.WebMailConstants.MAIL_STATE_ANSWERED%>"
       scope="request"/>
--%>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<script language="JavaScript">

    function changeCommunicationType(obj) {
        document.getElementById("changeCommunicationTypeId").value = "true";
        document.forms[0].submit();
    }

    function checkCommSubmit(submit) {
        checkComm();
        if (submit == true) {
            document.forms[0].submit();
        }
    }

    function checkComm() {
        document.getElementById("comm").value = "s";
    }

    function changeCommunication() {
        checkComm();
        mySubmit();
    }

    function acop(s) {
        document.getElementById("a_o").value = s;
        if (document.getElementById('isExternal').value != "true")
            if ("d_c" == s)
                document.getElementById("isDelete").value = "true";
    }

    function submitForm(obj) {
        if (obj.options[obj.selectedIndex].value != "")
            mySubmit();
    }

    function mySubmit() {
        document.forms[0].submit();
    }

/*
    function reply(submit) {
        document.getElementById("readToComposeForm").elements[0].value = "${REDIRECT_ANSWERED}";
        if (submit)
            document.getElementById("readToComposeForm").submit();
    }
*/
</script>