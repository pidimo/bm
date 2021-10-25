<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>
<c:set var="TEMPORALIMGIDKEY" value="<%=WebMailConstants.TEMPORALIMAGESTOREID_KEY%>"/>

<app2:jScriptUrl url="/webmail/TemporalImage/Download.do" var="jsDownloadUrl" addModuleParams="false">
  <app2:jScriptUrlParam param="imageStoreId" value="imageStoreId"/>
</app2:jScriptUrl>


<script language="javascript" type="text/javascript">

  /**
   * this method is called from onLoad property
   * @param imageStoreId
   */
  function importTemporalImage(imageStoreId) {

      var URL = ${jsDownloadUrl};

      // Get the parameters passed into the window from the top frame image popup
      var popupParams = top.tinymce.activeEditor.windowManager.getParams();

      //alert(" win " + popupParams.window + " srcInputName " + popupParams.srcInputName);

      var win = popupParams.window;

      // insert information now
      var imageInput = win.document.getElementById(popupParams.srcInputName);
      imageInput.value = URL;
      imageInput.readOnly = true;

      //set in variable the upload image id
      top.tinyMCE.activeEditor.settings.my_uploadImageId_variable = "${TEMPORALIMGIDKEY}" + "=" + imageStoreId;

      // close popup window
      top.tinymce.activeEditor.windowManager.close();
  }

</script>

<c:url value="/webmail/TinyMCE4/TemporalImage/Create.do" var="createUrl"/>

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


