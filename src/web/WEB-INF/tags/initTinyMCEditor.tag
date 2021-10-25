<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

<%@ attribute name="textAreaId" required="true" %>
<%@ attribute name="addElwisPlugin" required="false" %>
<%@ attribute name="addUserEmailStylePlugin" required="false" %>
<%@ attribute name="addDefaultBodyStylePlugin" required="false" %>
<%@ attribute name="addBrowseImagePlugin" required="false" %>
<%@ attribute name="addHtmlSourceEditButton" required="false" %>

<c:set var="path" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="time" value="<%=Long.toString(System.currentTimeMillis())%>" scope="request"/>
<c:set var="addStylePlugin" value="${addUserEmailStylePlugin == true || addDefaultBodyStylePlugin == true}"
       scope="request"/>

<c:set var="htmlSourceButtonCode" value=""/>
<c:if test="${addHtmlSourceEditButton}">
    <c:set var="htmlSourceButtonCode" value="code,"/>
</c:if>

<%--var that save all init scripts to editor--%>
<c:set var="initEditorFragment">

    <c:if test="${empty tinyMCEScriptLoaded}">
        <script type="text/javascript"
                src="<c:url value="/js/htmleditor/tinymce/jscripts/tiny_mce_3_5_8/tiny_mce.js"/>"></script>
        <c:set var="tinyMCEScriptLoaded" value="true" scope="request"/>
        <jsp:useBean id="initHtmlEditorItems" class="java.util.LinkedHashMap" scope="request"/>
    </c:if>

    <c:if test="${addElwisPlugin == true}">
        <c:if test="${empty elwisPluginScriptLoaded}">
            <LINK rel="stylesheet"
                  href="<c:url value="/js/htmleditor/externalplugins/elwisplugin/elwismenu.css?time=${time}"/>"
                  type="text/css"/>


            <script type="text/javascript"
                    src="<c:url value="/js/htmleditor/externalplugins/elwisplugin/config.jsp?time=${time}"/>"></script>
            <script type="text/javascript"
                    src="<c:url value="/js/htmleditor/externalplugins/elwisplugin/editor_plugin_src.js?time=${time}"/>"></script>
            <c:set var="elwisPluginScriptLoaded" value="true" scope="request"/>
        </c:if>
    </c:if>

    <c:if test="${addStylePlugin == true}">
        <c:if test="${empty stylePluginScriptLoaded}">
            <c:choose>
                <c:when test="${addDefaultBodyStylePlugin == true}">
                    <script type="text/javascript"
                            src="<c:url value="/js/htmleditor/externalplugins/styleplugin/bodyDefaultConfig.js?time=${time}"/>"></script>
                </c:when>
                <c:otherwise>
                    <script type="text/javascript"
                            src="<c:url value="/js/htmleditor/externalplugins/styleplugin/userMailConfig.jsp?time=${time}"/>"></script>
                </c:otherwise>
            </c:choose>

            <script type="text/javascript"
                    src="<c:url value="/js/htmleditor/externalplugins/styleplugin/editor_plugin_src.js?time=${time}"/>"></script>
            <c:set var="stylePluginScriptLoaded" value="true" scope="request"/>
        </c:if>
    </c:if>

    <%--image browse part--%>
    <app2:jScriptUrl url="/webmail/TemporalImage/Forward/Create.do" var="urlImageBrowse" addModuleParams="false">
        <app2:jScriptUrlParam param="type" value="type"/>
    </app2:jScriptUrl>


    <script type="text/javascript">

        var customPlugins = "";
        var elwisToolbar = "";
        var browseImageFunc = "";
        <c:if test="${addElwisPlugin == true}">
        var elwisConfigObj = getElwisConfigInstance();
        customPlugins = "-elwisplugin,";
        for (var i in elwisConfigObj.buttonList) {
            var btn = elwisConfigObj.buttonList[i];
            if (btn) {
                if (elwisToolbar.length > 0) {
                    elwisToolbar = elwisToolbar + ",|," + btn.id;
                } else {
                    elwisToolbar = btn.id;
                }
            }
        }
        </c:if>

        <c:if test="${addStylePlugin == true}">
        customPlugins = customPlugins + "-styleplugin,";
        </c:if>
        <c:if test="${addBrowseImagePlugin == true}">
        browseImageFunc = "fileBrowser";
        </c:if>


        tinyMCE.init({
            // General options
            mode : "exact",
            elements : '${textAreaId}',
            theme : "advanced",
            language : "${sessionScope.user.valueMap['locale']}",
            plugins : customPlugins + "safari,style,table,advhr,advimage,advlink,inlinepopups,insertdatetime,print,contextmenu,paste,directionality,fullscreen,noneditable",

            // Theme options
            theme_advanced_buttons1 : "newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,formatselect,fontselect,fontsizeselect",
            theme_advanced_buttons2 : "cut,copy,paste,pasteword,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,image,|,insertdate,inserttime,${htmlSourceButtonCode}|,forecolor,backcolor",
            theme_advanced_buttons3 : "tablecontrols,|,hr,visualaid,|,sub,sup,|,charmap,advhr,|,ltr,rtl,|,styleprops,|,print,|,fullscreen",
            theme_advanced_buttons4 : elwisToolbar,
            theme_advanced_toolbar_location : "top",
            theme_advanced_toolbar_align : "left",
            theme_advanced_statusbar_location : "none",
            //theme_advanced_resizing : true,
            theme_advanced_fonts : "Andale Mono=andale mono,times;"+
                "Arial=arial,helvetica,sans-serif;"+
                "Arial Black=arial black,avant garde;"+
                "Book Antiqua=book antiqua,palatino;"+
                "Century Gothic=century gothic,sans-serif;"+
                "Comic Sans MS=comic sans ms,sans-serif;"+
                "Courier New=courier new,courier;"+
                "Georgia=georgia,palatino;"+
                "Helvetica=helvetica;"+
                "Impact=impact,chicago;"+
                "Segoe UI=segoe ui,sans-serif;"+
                "Segoe UI Light=segoe ui light,sans-serif;"+
                "Symbol=symbol;"+
                "Tahoma=tahoma,arial,helvetica,sans-serif;"+
                "Terminal=terminal,monaco;"+
                "Times New Roman=times new roman,times;"+
                "Trebuchet MS=trebuchet ms,geneva;"+
                "Verdana=verdana,geneva;"+
                "Webdings=webdings;"+
                "Wingdings=wingdings,zapf dingbats",
            extended_valid_elements : "label[accesskey|class|dir<ltr?rtl|for|id|lang|onblur|onclick|ondblclick"
                    + "|onfocus|onkeydown|onkeypress|onkeyup|onmousedown|onmousemove|onmouseout"
                    + "|onmouseover|onmouseup|style|title]",
            width : "100%",
            //height : "500",
            file_browser_callback : browseImageFunc,
            font_size_style_values : "8pt,10pt,12pt,14pt,18pt,24pt,36pt",

            forced_root_block : false,
            force_p_newlines : true

        });


        <c:if test="${empty globalFunctionsLoaded}">
        <c:set var="globalFunctionsLoaded" value="true" scope="request"/>
        /**
         * function to manage image borowse from local disk
         * @param field_name    is the name (and ID) of the dialogue window's input field which needs to be filled with the value your file browser is about to provide
         * @param url    carries the existing link URL if you modify a link (or image URL if you edit an image).
         * @param type  is a string value which is either 'image', 'media' or 'file'
         * @param win   is a reference to the dialogue window itself which is extremely important when it comes to writing back the retrieved value.
         */
        function fileBrowser(field_name, url, type, win) {
            //alert("Field_Name: " + field_name + "\nURL: " + url + "\nType: " + type + "\nWin: " + win); // debug/testing

            if (type == 'image') {

                var cmsURL = ${urlImageBrowse};

                tinyMCE.activeEditor.windowManager.open({
                    file : cmsURL,
                    title : 'My File Browser',
                    width : 500,
                    height : 150,
                    resizable : "yes",
                    inline : "yes",  // This parameter only has an effect if you use the inlinepopups plugin!
                    close_previous : "no"
                }, {
                    window : win,
                    input : field_name,
                    imgId : "id",
                    descId : "alt"
                });
            }

            return false;
        }
        </c:if>


    </script>

</c:set>

<%--add scripts in initHtmlEditorItems Map, this is write in main.jsp--%>
<c:set target="${initHtmlEditorItems}" property="${textAreaId}" value="${initEditorFragment}"/>

