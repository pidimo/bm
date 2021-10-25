<%@ include file="/Includes.jsp" %>
<tags:jQuery/>


<script language="JavaScript" type="text/javascript">

    function getSelectContactMail() {
        var checkboxes = new Array();
        var element = document.getElementById('listMailForm').checkBoxId;
        if("checkbox" == element.type)
            checkboxes[0]= element;
        else
            checkboxes = document.getElementById('listMailForm').checkBoxId;

        var recipients = window.opener.$("#${param.nameStyle}").attr("value");
        if (null == recipients) {
            recipients = "";
        }

        if (null != checkboxes && checkboxes.length > 0) {
            for (var i = 0; i < checkboxes.length; i++) {
                var checkBox = checkboxes[i];
                var isChecked = $(checkBox).attr("checked");
                if (!isChecked) {
                    continue;
                }

                var checkBoxValue = $(checkBox).attr("value");
                var emailId = "email_" + checkBoxValue + "_Id";
                var hiddenElements = document.getElementsByName(emailId);
                if (null == hiddenElements) {
                    continue;
                }

                for (var j = 0; j < hiddenElements.length; j++) {
                    var hidden = hiddenElements[j];
                    var email = $(hidden).attr("id");
                    var text = $(hidden).attr("value");

                    if (recipients.indexOf(text) == -1) {
                        if ("" != recipients) {
                            recipients = recipients + ", ";
                        }
                        recipients = recipients + text;
                    }
                }
            }
        }

        opener.selectField("${param.idStyle}", "unused", "${param.nameStyle}", recipients);
    }

    function disabledCheckbox(){
        var checkboxes = new Array();
        var element = document.getElementById('listMailForm').checkBoxId;
        if(null != element && "checkbox" == element.type)
            checkboxes[0]= element;
        else
            checkboxes = document.getElementById('listMailForm').checkBoxId;
        
        if (null != checkboxes && checkboxes.length > 0) {
            for (var i = 0; i < checkboxes.length; i++) {
                var checkBox = checkboxes[i];
                var checkBoxValue = $(checkBox).attr("value");
                var groupCounter = document.getElementsByName("contEmails_"+checkBoxValue+"_Id").value;
                if (0 == groupCounter) {
                    $(checkBox).attr("disabled", true);
                }
            }
        }
    }
</script>

<c:set var="url" value="/Mail/Forward/AddressGroupImportList.do?dto(op)=getListGroups&idStyle=${param.idStyle}&nameStyle=${param.nameStyle}"/>

<table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" class="container">

    <tr>
        <td colspan="2" valing="top" align="center">
            <html:form action="${url}" styleId="listMailForm">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td height="20" class="title" colspan="3">
                            <fmt:message key="Webmail.group_Contact.search"/>
                        </td>
                    </tr>

                    <TH class='listHeader' width='5%'><fmt:message key="Common.action"/></TH>
                    <TH class='listHeader' width='80%' align='center'><fmt:message
                            key="Webmail.contactGroup.plural"/></TH>
                    <TH class='listHeader' width='15%' align='center'><fmt:message
                            key="Webmail.contactGroup.emails"/></TH>

                    <c:choose>
                        <c:when test="${not empty dto.listGroups}">
                            <c:forEach var="Groups" items="${dto.listGroups}">
                                <c:forEach var="contact" items="${Groups.contacts}">
                                    <html:hidden property="email_${Groups.groupId}_Id" value="${contact.text}"
                                                 styleId="${contact.email}"/>
                                </c:forEach>
                                <tr class="listRow">
                                    <td class='listItemCenter'>
                                        <input type="checkbox" name="selectedMailsGroups" value="${Groups.groupId}"
                                               class="radio" id="checkBoxId">
                                    </td>
                                    <td class='listItem'>
                                        <c:out value="${Groups.groupName}"/>
                                        <html:hidden property="listOfGroup_${Groups.groupId}"
                                                     value="${Groups.listEmails}"
                                                     styleId="listOfGroup_${Groups.groupId}_Id"/>
                                    </td>
                                    <td class='listItem2Center'>
                                        <c:out value="${Groups.numEmails}"/>
                                        <html:hidden property="contEmails_${Groups.groupId}" value="${Groups.numEmails}"
                                                     styleId="contEmails_${Groups.groupId}_Id"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr class="listRow">
                                <td colspan="3">&nbsp;<fmt:message key="Common.list.empty"/></td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </table>

                <table cellSpacing=0 cellPadding=2 width="100%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <html:submit styleClass="button" onclick="getSelectContactMail();"><fmt:message
                                    key="Common.select"/></html:submit>
                        </TD>
                    </TR>
                </table>

            </html:form>

        </td>
    </tr>
</table>

<script language="JavaScript" type="text/javascript">
    <!--
    //disable checkbox
    disabledCheckbox();
    //-->
</script>
