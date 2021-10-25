<%@ include file="/Includes.jsp" %>
<tags:jQuery/>


<script language="JavaScript" type="text/javascript">

    /**
     @deprecated use instead processSelectedContactMail
     */
    function getSelectContactMail() {
        var checkboxes = new Array();
        var element = document.getElementById('listMailForm').checkBoxId;
        if ("checkbox" == element.type)
            checkboxes[0] = element;
        else
            checkboxes = document.getElementById('listMailForm').checkBoxId;

        var recipients = window.parent.$("#${param.nameStyle}").attr("value");
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

        parent.selectField("${param.idStyle}", "unused", "${param.nameStyle}", recipients);
    }

    function processSelectedContactMail() {
        var checkboxes = new Array();
        var element = document.getElementById('listMailForm').checkBoxId;
        if ("checkbox" == element.type)
            checkboxes[0] = element;
        else
            checkboxes = document.getElementById('listMailForm').checkBoxId;

        if (null != checkboxes && checkboxes.length > 0) {
            for (var i = 0; i < checkboxes.length; i++) {
                var checkBox = checkboxes[i];
                var isChecked = $(checkBox).attr("checked");
                if (!isChecked) {
                    continue;
                }

                var checkBoxValue = $(checkBox).attr("value");

                var guiaName = "guia_" + checkBoxValue;
                var guiaElements = document.getElementsByName(guiaName);
                if (null == guiaElements) {
                    continue;
                }

                for (var j = 0; j < guiaElements.length; j++) {
                    var hidden = guiaElements[j];
                    var identifier = $(hidden).attr("value");

                    var email = $("#mail_" + identifier + "_id").val();
                    var name = $("#addressName_" + identifier + "_id").val();

                    var addressId = "";
                    var contactPersonOfId = "";

                    //add as token field in parent
                    parent.addTokenField("${param.nameStyle}", encodeURI(email), encodeURI(name), addressId, contactPersonOfId);
                }
            }
        }
        parent.hideBootstrapPopup();
    }

    function disabledCheckbox() {
        var checkboxes = new Array();
        var element = document.getElementById('listMailForm').checkBoxId;
        if (null != element && "checkbox" == element.type)
            checkboxes[0] = element;
        else
            checkboxes = document.getElementById('listMailForm').checkBoxId;

        if (null != checkboxes && checkboxes.length > 0) {
            for (var i = 0; i < checkboxes.length; i++) {
                var checkBox = checkboxes[i];
                var checkBoxValue = $(checkBox).attr("value");
                var groupCounter = document.getElementsByName("contEmails_" + checkBoxValue + "_Id").value;
                if (0 == groupCounter) {
                    $(checkBox).attr("disabled", true);
                }
            }
        }
    }
</script>

<c:set var="url"
       value="/Mail/Forward/AddressGroupImportList.do?dto(op)=getListGroups&idStyle=${param.idStyle}&nameStyle=${param.nameStyle}"/>

<html:form action="${url}" styleId="listMailForm">
    <div class="table-responsive">
        <table mode="bootstrap" width="100%" border="0" cellpadding="0" cellspacing="0" class="${app2:getFantabulousTableClases()}">
            <th class='listHeader' width='5%'>
                <fmt:message key="Common.action"/>
            </th>
            <th class='listHeader' width='80%' align='center'>
                <fmt:message key="Webmail.contactGroup.plural"/>
            </th>
            <th class='listHeader' width='15%' align='center'>
                <fmt:message key="Webmail.contactGroup.emails"/>
            </th>

            <c:choose>
                <c:when test="${not empty dto.listGroups}">
                    <c:forEach var="Groups" items="${dto.listGroups}">
                        <c:forEach var="contact" items="${Groups.contacts}" varStatus="statusVar">
                            <html:hidden property="email_${Groups.groupId}_Id" value="${contact.text}"
                                         styleId="${contact.email}"/>

                            <c:set var="idenfifier" value="${Groups.groupId}_${statusVar.index}"/>

                            <html:hidden property="guia_${Groups.groupId}" styleId="${idenfifier}" value="${idenfifier}"/>
                            <html:hidden property="mail_${idenfifier}" styleId="mail_${idenfifier}_id" value="${contact.email}"/>
                            <html:hidden property="addressName_${idenfifier}" styleId="addressName_${idenfifier}_id" value="${contact.addressName}"/>

                        </c:forEach>
                        <tr class="listRow">
                            <td class='listItemCenter'>
                                <div class="checkbox checkbox-default listItemCheckbox">
                                    <input type="checkbox" name="selectedMailsGroups" value="${Groups.groupId}"
                                           id="checkBoxId"/>
                                    <label for="checkBoxId"></label>
                                </div>
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
                        <td colspan="3">
                            <fmt:message key="Common.list.empty"/>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </table>
    </div>
    <div class="${app2:getSearchWrapperClasses()}">
        <html:submit styleClass="${app2:getFormButtonClasses()}" onclick="processSelectedContactMail();">
            <fmt:message key="Common.select"/>
        </html:submit>
    </div>

</html:form>


<script language="JavaScript" type="text/javascript">
    <!--
    //disable checkbox
    disabledCheckbox();
    //-->
</script>
