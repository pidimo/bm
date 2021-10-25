<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>
<c:set var="TEMPORALIMGIDKEY" value="<%=WebMailConstants.TEMPORALIMAGESTOREID_KEY%>"/>

<script language="javascript" type="text/javascript"
        src="<c:url value="/js/htmleditor/tinymce/jscripts/tiny_mce_3_5_8/tiny_mce_popup.js"/>"></script>


<app2:jScriptUrl url="/webmail/TemporalImage/Download.do" var="jsDownloadUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="imageStoreId" value="imageStoreId"/>
</app2:jScriptUrl>


<script language="javascript" type="text/javascript">

    var FileBrowserDialogue = {
        init : function () {
            // Here goes your code for setting your custom things onLoad.

            //remove popup default css
            /*var allLinks = document.getElementsByTagName("link");
             allLinks[allLinks.length-1].parentNode.removeChild(allLinks[allLinks.length-1]);*/

        }
    };

    tinyMCEPopup.onInit.add(FileBrowserDialogue.init, FileBrowserDialogue);


    /**
     * this method is called from onLoad property
     * @param imageStoreId
     */
    function importTemporalImage(imageStoreId) {

        var URL = ${jsDownloadUrl};
        var win = tinyMCEPopup.getWindowArg("window");

        // insert information now
        var imageInput = win.document.getElementById(tinyMCEPopup.getWindowArg("input"));
        imageInput.value = URL;
        imageInput.readOnly = true;

        //img id property
        var imageId = win.document.getElementById(tinyMCEPopup.getWindowArg("imgId"));
        imageId.value = "${TEMPORALIMGIDKEY}" + "=" + imageStoreId;
        imageId.readOnly = true;

        //img description property
        var imageDescription = win.document.getElementById(tinyMCEPopup.getWindowArg("descId"));
        if (imageDescription.value == "") imageDescription.value = " ";

        // are we an image browser
        if (typeof(win.ImageDialog) != "undefined") {
            // we are, so update image dimensions and preview if necessary
            if (win.ImageDialog.getImageData) win.ImageDialog.getImageData();
            if (win.ImageDialog.showPreviewImage) win.ImageDialog.showPreviewImage(URL);
        }

        // close popup window
        tinyMCEPopup.close();
    }

</script>

<c:url value="/webmail/TemporalImage/Create.do" var="createUrl"/>

<form action="${createUrl}" method="post" enctype="multipart/form-data">
    <%--styles inherit from tinymce--%>
    <input type="hidden" name="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <input type="hidden" name="dto(op)" value="${op}"/>

    <div id="general_panel" class="panel_wrapper">
        <fieldset>
            <legend><fmt:message key="Common.upload"/></legend>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                <tr>
                    <td class="column1" width="25%">
                        <fmt:message key="Mail.browseImage.file"/>
                    </td>
                    <td width="75%">
                        <input type="file" name="dto(file)" size="40">
                    </td>
                </tr>
            </table>
        </fieldset>
    </div>
    <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
        <TR>
            <TD colspan="2" align="right">
                <div class="mceActionPanel">
                    <input type="submit" id="insert" value="<fmt:message key="Common.upload"/>" name="insert"
                           style="text-align:center;"/>
                </div>
            </TD>
        </TR>
    </table>

</form>

<%--set in image editor, only if this has been created--%>
<c:if test="${not empty createdImageStoreId}">
    <script language="javascript" type="text/javascript">
        importTemporalImage('${createdImageStoreId}');
    </script>
</c:if>


