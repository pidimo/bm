<%@ include file="/Includes.jsp" %>

<%--bootstrap file input headers--%>
<link href="<c:url value="/js/cacheable/bootstrap/bootstrapfileinput/4.3.3/css/fileinput.min.css"/>" media="all" rel="stylesheet" type="text/css" />

<!-- canvas-to-blob.min.js is only needed if you wish to resize images before upload. This must be loaded before fileinput.min.js -->
<%--
<script src="<c:url value="/js/cacheable/bootstrap/bootstrapfileinput/4.3.3/js/plugins/canvas-to-blob.min.js"/>" type="text/javascript"></script>
--%>

<!-- sortable.min.js is only needed if you wish to sort / rearrange files in initial preview. This must be loaded before fileinput.min.js -->
<%--
<script src="<c:url value="/js/cacheable/bootstrap/bootstrapfileinput/4.3.3/js/plugins/sortable.min.js"/>" type="text/javascript"></script>
--%>

<!-- purify.min.js is only needed if you wish to purify HTML content in your preview for HTML files. This must be loaded before fileinput.min.js -->
<%--
<script src="<c:url value="/js/cacheable/bootstrap/bootstrapfileinput/4.3.3/js/plugins/purify.min.js"/>" type="text/javascript"></script>
--%>

<!-- the main fileinput plugin file -->
<script src="<c:url value="/js/cacheable/bootstrap/bootstrapfileinput/4.3.3/js/fileinput.min.js"/>"></script>

<!-- optionally if you need a theme like font awesome theme you can include it as mentioned below -->
<%--<script src="<c:url value="/js/cacheable/bootstrap/bootstrapfileinput/4.3.3/themes/fa/fa.js"/>"></script>--%>

<!-- optionally if you need translation for your language then include locale file as mentioned below -->
<c:choose>
    <c:when test="${'es' eq sessionScope.user.valueMap['locale']}">
        <script src="<c:url value="/js/cacheable/bootstrap/bootstrapfileinput/4.3.3/js/locales/es.js"/>"></script>
        <c:set var="filePluginLocale" value="es" scope="request"/>
    </c:when>
    <c:when test="${'de' eq sessionScope.user.valueMap['locale']}">
        <script src="<c:url value="/js/cacheable/bootstrap/bootstrapfileinput/4.3.3/js/locales/de.js"/>"></script>
        <c:set var="filePluginLocale" value="de" scope="request"/>
    </c:when>
    <c:when test="${'fr' eq sessionScope.user.valueMap['locale']}">
        <script src="<c:url value="/js/cacheable/bootstrap/bootstrapfileinput/4.3.3/js/locales/fr.js"/>"></script>
        <c:set var="filePluginLocale" value="fr" scope="request"/>
    </c:when>
</c:choose>




<c:if test="${empty composeObjectForm}">
    <c:set var="composeObjectForm" value="${emailForm}" scope="request"/>
</c:if>
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

<c:set var="UNKNOWN_NAME" value="unknown_name."/>
<fmt:message key="Common.delete" var="deleteMessage"/>

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

<c:if test="${(false == containOnlyEmbeddedAttachments)||(true == enableAddAttachLink)}">
    <legend class="title">
        <fmt:message key="Mail.attaches"/>
    </legend>
</c:if>


<div>

    <div>
        <c:if test="${null != attachments && not empty attachments && empty composeObjectForm.dtoMap['draftId']}">
            <c:if test="${false == containOnlyEmbeddedAttachments}">
                <c:forEach var="attach" items="${attachments}">

                    <c:if test="${false == attach.visible}">
                        <div class="attachContainerUI" style="padding: 4px">

                            <app:link
                                    action="${downloadAttachmentUrl}?dto(attachId)=${attach.attachId}&dto(freeTextId)=${attach.freeTextId}&dto(attachName)=${attach.attachFile}"
                                    contextRelative="true">
                                <img src="${baselayout}/img/webmail/paperclip.gif" border="0" alt=""/>
                            </app:link>

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

                            &nbsp;
                            (
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
                            )
                            &nbsp;
                            <c:if test="${true == enableCheckBoxes}">
                                <div class="checkbox checkbox-default listItemCheckbox" style="display: inline-block">
                                    <html:checkbox property="dto(${attach.attachId})"
                                                   styleId="checkBox_${attach.attachId}_Id"
                                                   value="true"
                                                   styleClass="radio listItemCenter"/>
                                    <label for="checkBox_${attach.attachId}_Id"></label>
                                </div>
                            </c:if>
                        </div>
                    </c:if>

                    <c:if test="${false == enableCheckBoxes}">
                        <html:hidden property="dto(${attach.attachId})"
                                     styleId="checkBox_${attach.attachId}_Id"
                                     value="true"/>
                    </c:if>
                </c:forEach>
            </c:if>
        </c:if>
    </div>

    <div id="attachedContainer_id">
        <div id="cloneContainer_id" class="attachContainerUI" style="display: none">
            <img src="${baselayout}/img/webmail/paperclip.gif" border="0" alt=""/>
            <span class="attachNameClass"></span>
            &nbsp;
            <span class="deleteAttachClass"></span>
        </div>

        <%--add previous temporal attach saved--%>
        <c:set var="tempAttachList" value="${app2:readMailTemporalAttachs(composeObjectForm.dtoMap['tempMailId'], pageContext.request)}"/>
        <c:forEach var="attachTemp" items="${tempAttachList}">
            <div id="row_${attachTemp.attachId}" class="attachContainerUI">
                <img src="${baselayout}/img/webmail/paperclip.gif" border="0" alt=""/>
                <span class="attachNameClass">
                    <c:out value="${attachTemp.attachName}"/>
                </span>
                &nbsp;
                <span class="deleteAttachClass">
                    <a href="javascript:deleteTempAttach('${attachTemp.attachId}')">
                        <span class="glyphicon glyphicon-trash" title="${deleteMessage}"></span>
                    </a>
                </span>
            </div>
        </c:forEach>
    </div>

    <%-- the input file --%>
    <input name="dto(fileInputAttach)" id="fileInput_id" type="file" multiple>
    <br/>

</div>


<app2:jScriptUrl url="/webmail/Mail/Ajax/Save/TemporalAttach.do" var="jsSaveAttachUrl">
</app2:jScriptUrl>

<app2:jScriptUrl url="/webmail/Mail/Ajax/Delete/TemporalAttach.do" var="jsDeleteAttachUrl">
    <app2:jScriptUrlParam param="dto(attachId)" value="tempAttachIdVar"/>
</app2:jScriptUrl>

<script type="text/javascript">

    $(document).ready(function () {

        //initialize bootstrap file input
        $("#fileInput_id").fileinput({
            'uploadUrl': ${jsSaveAttachUrl},
            'uploadExtraData': function(previewId, index) {
                var tempMailId = $('#tempMailId_id').val();
                return {"dto(tempMailId)": tempMailId};
            },
            <c:if test="${not empty filePluginLocale}">
                'language': '${filePluginLocale}',
            </c:if>
            'showUpload':false,
            'showRemove':false,
            'showCancel':false,
            'showClose':false,
            'showCaption':false,
            'allowedPreviewTypes':false, //to hide the file preview
            //'maxFileSize':9000, //en kb
            'fileActionSettings' : {
                showUpload : false,
                showRemove : true,
                showZoom : false
            },
            'previewSettings' : {
                other: {width: "160px", height: "50px"}
            },
            'layoutTemplates' : {
                progress: '' //to hide the progress bar
            }
        });

        //event to upload automatically when select file
        $('#fileInput_id').on('filebatchselected', function(event, files) {
            $('#fileInput_id').fileinput('upload');
        });

        $('#fileInput_id').on('fileuploaded', function(event, data, previewId, index) {
            var form = data.form, files = data.files, extra = data.extra,
                    responseJson = data.response, reader = data.reader;

            if("Success" == responseJson.ajaxForward) {
                var attachIdTemp = responseJson.tempAttachId;
                var attachName = responseJson.attachName;
                addTempAttachInDiv(attachIdTemp, attachName);
            }

            //remove the preview
            $('#' + previewId).remove();
        });

    });


    function addTempAttachInDiv(tempAttachId, attachName) {

        var cloneDiv = $("#cloneContainer_id").clone().removeAttr("style");

        //define element id
        $(cloneDiv).attr("id", composeTrRowElementId(tempAttachId));

        //set attach name
        $(cloneDiv).find(".attachNameClass").each(function() {
            $(this).append(attachName);
        });

        //set delete link
        $(cloneDiv).find(".deleteAttachClass").each(function() {
            $(this).append(composeTempAttachDeleteLink(tempAttachId));
        });

        $(cloneDiv).appendTo("#attachedContainer_id");
    }

    function composeTempAttachDeleteLink(tempAttachId) {
        var delLink = "<a href='javascript:deleteTempAttach("+ tempAttachId +");'>" +
                        "<span class='glyphicon glyphicon-trash' title='${deleteMessage}'></span>" +
                        "</a>";
        return delLink;
    }

    function composeTrRowElementId(attachId) {
        //note that this id also is write in foreach of "tempAttachList"
        return "row_" + attachId;
    }

    /**
     * Delete Attach scripts
     */
    function deleteTempAttach(attachId) {
        var tempAttachIdVar = attachId;
        makeAjaxDeleteAttach(${jsDeleteAttachUrl}, "", attachId);
    }

    function makeAjaxDeleteAttach(urlAction, parameters, attachId) {
        $.ajax({
            async:true,
            type: "POST",
            dataType: "json",
            data:parameters,
            url:urlAction,
            success: function(dataJson) {
                processAjaxDeleteResponse(dataJson, attachId);
            },
            error: function(ajaxRequest) {
                alert("error:" + ajaxRequest);
            }
        });
    }

    function processAjaxDeleteResponse(responseJson, attachId) {
        if("Success" == responseJson.ajaxForward) {
            //remove the tr element
            $('#' + composeTrRowElementId(attachId)).remove();
        }
    }

</script>




