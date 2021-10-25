<%@ include file="/Includes.jsp" %>

<c:if test="${empty enableCheckBoxes}">
    <c:set var="enableCheckBoxes" value="false" scope="request"/>
</c:if>

<c:if test="${empty downloadAttachmentUrl}">
    <c:set var="downloadAttachmentUrl" value="/webmail/Mail/Download.do" scope="request"/>
</c:if>

<c:if test="${empty enableAddAttachLink}">
    <c:set var="enableAddAttachLink" value="true" scope="request"/>
</c:if>

<c:if test="${empty inputFileOnChangeJs}">
    <c:set var="inputFileOnChangeJs" value="" scope="request"/>
</c:if>

<c:if test="${empty deleteAttachJs}">
    <c:set var="deleteAttachJs" value="" scope="request"/>
</c:if>

<c:set var="newCounter" value="0"/>

<c:set var="UNKNOWN_NAME" value="unknown_name."/>

<c:set var="containOnlyEmbeddedAttachments" value="true"/>
<c:if test="${null != attachments && not empty attachments}">
    <c:forEach var="attach" items="${attachments}">
        <html:hidden property="dto(attachId_${attach.attachId})"
                     value="${attach.attachId}"
                     styleId="attachId_${attach.attachId}_Id"/>
        <html:hidden property="dto(attachFile_${attach.attachId})"
                     value="${attach.attachFile}"
                     styleId="attachFile_${attach.attachId}_Id"/>
        <html:hidden property="dto(visible_${attach.attachId})"
                     value="${attach.visible}"
                     styleId="visible_${attach.attachId}_Id"/>
        <html:hidden property="dto(size_${attach.attachId})"
                     value="${attach.size}"
                     styleId="size_${attach.attachId}_Id"/>
        <c:if test="${false == attach.visible}">
            <c:set var="containOnlyEmbeddedAttachments" value="false"/>
        </c:if>
    </c:forEach>
</c:if>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <c:if test="${(false == containOnlyEmbeddedAttachments)||(true == enableAddAttachLink)}">
        <tr>
            <TD class="title" width="100%">
                <fmt:message key="Mail.attaches"/>
            </TD>
        </tr>
    </c:if>

    <c:if test="${null != attachments && not empty attachments}">
        <c:if test="${false == containOnlyEmbeddedAttachments}">
            <tr>
                <td width="100%" class="label">
                    <table cellpadding="0" cellspacing="0" border="0" width="98%">
                        <c:forEach var="attach" items="${attachments}">
                            <tr>
                            <c:if test="${false == attach.visible}">
                                <td width="2%">
                                    <app:link
                                            action="${downloadAttachmentUrl}?dto(attachId)=${attach.attachId}&dto(freeTextId)=${attach.freeTextId}&dto(attachName)=${attach.attachFile}"
                                            contextRelative="true">
                                        <img src="${baselayout}/img/webmail/paperclip.gif" border="0" alt=""/>
                                    </app:link>
                                </td>
                                <td width="88%">
                                    <app:link
                                            action="${downloadAttachmentUrl}?dto(attachId)=${attach.attachId}&dto(freeTextId)=${attach.freeTextId}&dto(attachName)=${attach.attachFile}"
                                            contextRelative="true">
                                        <c:choose>
                                            <c:when test="${fn:contains(attach.attachFile,UNKNOWN_NAME)}">
                                                <fmt:message key="Webmail.Attach.unknownFile"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${attach.attachFile}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </app:link>
                                </td>
                                <td width="5%" align="right" style="padding:0;border:0;margin:0; right:auto;" nowrap>
                                    <c:choose>
                                        <c:when test="${attach.size < 1024}">
                                            ${1} <fmt:message key="Webmail.mailTray.Kb"/>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatNumber
                                                    value="${fn:substringBefore(attach.size/1024,'.')}"
                                                    type="number" pattern="${numberFormat}"/>
                                            <fmt:message key="Webmail.mailTray.Kb"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </c:if>
                            <c:choose>
                                <c:when test="${true == enableCheckBoxes}">
                                    <td width="5%" align="center">
                                        <c:if test="${false == attach.visible}">
                                            <html:checkbox property="dto(${attach.attachId})"
                                                           styleId="checkBox_${attach.attachId}_Id"
                                                           value="true"
                                                           styleClass="radio listItemCenter"/>
                                        </c:if>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <html:hidden property="dto(${attach.attachId})"
                                                 styleId="checkBox_${attach.attachId}_Id"
                                                 value="true"/>
                                </c:otherwise>
                            </c:choose>
                            </tr>
                        </c:forEach>
                    </table>
                </td>
            </tr>
        </c:if>

    </c:if>

    <c:if test="${true == enableAddAttachLink}">
        <tr>
            <td class="label">
                <table id="fileElements" width="100%" border="0" style="padding:1px;">
                </table>
                <a href='#' id='add_File_Id'>
                    <fmt:message key="Mail.newAttach"/>
                </a>
            </td>
        </tr>
    </c:if>

</table>

<html:hidden property="dto(attachmentCounter)" styleId="attachmentCounterId"/>
<html:hidden property="dto(messageCounter)" styleId="messageCounterId"/>

<fmt:message key="Common.delete" var="deleteMessage"/>
<fmt:message key="Common.file" var="fileMessage"/>
<script type="text/javascript">
    function setFileToolTip(obj) {
        obj.title = obj.value;
    }

    $(document).ready(function() {
        $("#add_File_Id").click(function() {
            var counter = $("#attachmentCounterId").val();
            if ('' == counter)
                counter = 1;

            var messageCounter = $("#messageCounterId").val();
            if ('' == messageCounter)
                messageCounter = 1;

            var onChangeExpression = '';
            <c:if test="${not empty inputFileOnChangeJs}">
                onChangeExpression = " onchange='" + "${inputFileOnChangeJs}" + "' ";
            </c:if>

            $("#fileElements").append("<tr id='row_" + counter + "_Id'><td><div id='messageName_" + counter + "_Id'><b>${fileMessage} " + messageCounter + ": </b></div></td><td><input type='file' name='dto(file" + counter + ")' size='80' value='' onmouseover='javascript:setFileToolTip(this);' " + onChangeExpression + " ></td><td><a href='#' id='delete_link_" + counter + "_Id'>${deleteMessage}</a></td></tr>");

            var linkId = "#delete_link_" + counter + "_Id";

            $(linkId).click(function() {
                var idx = parseInt(counter) - 1;
                hideRow(idx);
            });

            counter++;
            $("#attachmentCounterId").val(counter);

            messageCounter++;
            $("#messageCounterId").val(messageCounter);
        });
    });


    function hideRow(index) {
        var msgCounter = document.getElementById("messageCounterId").value;
        msgCounter--;
        document.getElementById("messageCounterId").value = msgCounter;

        var rowId = "row_" + index + "_Id";
        if (null != document.getElementById(rowId)) {
            var rowElement = document.getElementById(rowId);
            var rowIndex = rowElement.sectionRowIndex;

            document.getElementById("fileElements").deleteRow(rowIndex);

            var messageIndex = 0;

            var counter = document.getElementById("attachmentCounterId").value;

            for (var i = 1; i <= counter; i++) {
                var messageId = "messageName_" + i + "_Id";

                if (null != document.getElementById(messageId)) {
                    messageIndex++;
                    document.getElementById(messageId).innerHTML = "<b>${fileMessage} " + messageIndex + ": </b>";
                }
            }

            <c:if test="${not empty deleteAttachJs}">
                ${deleteAttachJs}
            </c:if>
        }
    }
</script>