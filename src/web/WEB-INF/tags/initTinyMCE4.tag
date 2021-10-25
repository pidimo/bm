<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

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


<%--var that save all init scripts to editor--%>
<c:set var="initEditorFragment">

    <c:if test="${empty tinyMCE4ScriptLoaded}">
        <script type="text/javascript" src="<c:url value="/js/tinymce4/tinymce_4.5.8/js/tinymce/tinymce.min.js"/>"></script>

        <c:set var="tinyMCE4ScriptLoaded" value="true" scope="request"/>
        <jsp:useBean id="tinyMCE4EditorItems" class="java.util.LinkedHashMap" scope="request"/>

        <%--load fixedimage plugin, this is required--%>
        <script type="text/javascript" src="<c:url value="/js/tinymce4/externalplugins/fixedimage/plugin.js?time=${time}"/>"></script>
    </c:if>

    <c:if test="${addElwisPlugin == true}">
        <c:if test="${empty elwisPluginScriptLoaded}">

            <script type="text/javascript"
                    src="<c:url value="/js/tinymce4/externalplugins/elwisplugin2/config.jsp?time=${time}"/>"></script>

            <script type="text/javascript"
                    src="<c:url value="/js/tinymce4/externalplugins/elwisplugin2/plugin.js?time=${time}"/>"></script>

            <c:set var="elwisPluginScriptLoaded" value="true" scope="request"/>
        </c:if>
    </c:if>

    <c:if test="${addStylePlugin == true}">
        <c:if test="${empty stylePluginScriptLoaded}">
            <c:choose>
                <c:when test="${addDefaultBodyStylePlugin == true}">
                    <script type="text/javascript"
                            src="<c:url value="/js/tinymce4/externalplugins/defaultstyleplugin/bodyDefaultConfig.js?time=${time}"/>"></script>
                </c:when>
                <c:otherwise>
                    <script type="text/javascript"
                            src="<c:url value="/js/tinymce4/externalplugins/defaultstyleplugin/userMailConfig.jsp?time=${time}"/>"></script>
                </c:otherwise>
            </c:choose>

            <script type="text/javascript"
                    src="<c:url value="/js/tinymce4/externalplugins/defaultstyleplugin/plugin.js?time=${time}"/>"></script>
            <c:set var="stylePluginScriptLoaded" value="true" scope="request"/>
        </c:if>
    </c:if>

    <%--check for language--%>
    <c:choose>
        <c:when test="${'en' eq sessionScope.user.valueMap['locale']}">
            <c:set var="editorLanguage" value="en_CA" scope="request"/>
        </c:when>
        <c:when test="${'fr' eq sessionScope.user.valueMap['locale']}">
            <c:set var="editorLanguage" value="fr_FR" scope="request"/>
        </c:when>
        <c:when test="${'de' eq sessionScope.user.valueMap['locale']}">
            <c:set var="editorLanguage" value="de" scope="request"/>
        </c:when>
        <c:when test="${'es' eq sessionScope.user.valueMap['locale']}">
            <c:set var="editorLanguage" value="es" scope="request"/>
        </c:when>
        <c:otherwise>
            <c:set var="editorLanguage" value="en_CA" scope="request"/>
        </c:otherwise>
    </c:choose>


    <%--image browse part--%>
    <fmt:message var="imageUploadTitle" key="Mail.browseImage.title"/>
    <app2:jScriptUrl url="/webmail/TinyMCE4/TemporalImage/Forward/Create.do" var="urlImageBrowse" addModuleParams="false">
        <app2:jScriptUrlParam param="type" value="type"/>
    </app2:jScriptUrl>

    <c:set var="enableImageBrowse" value="''"/>
    <c:if test="${addBrowseImagePlugin == true}">
        <c:set var="enableImageBrowse" value="startImageUploadPopup"/>
    </c:if>

    <script type="text/javascript">

        var enableCodePlugin = "";
        var customPlugins = "";
        var elwisToolbar = "";

        <c:if test="${addHtmlSourceEditButton == true}">
            enableCodePlugin = "code ";
        </c:if>

        <c:if test="${addElwisPlugin == true}">
            var elwisConfigObj = getElwisConfigInstance();
            customPlugins = "elwisplugin2 ";
            for (var i in elwisConfigObj.buttonList) {
                var btn = elwisConfigObj.buttonList[i];
                if (btn) {
                    if (elwisToolbar.length > 0) {
                        elwisToolbar = elwisToolbar + " " + btn.id;
                    } else {
                        elwisToolbar = btn.id;
                    }
                }
            }
        </c:if>

        <c:if test="${addStylePlugin == true}">
            customPlugins = customPlugins + "defaultstyleplugin ";
        </c:if>

        tinymce.init({
            selector: '#${textAreaId}',
            language: '${editorLanguage}',
            height: 500,
            theme: 'modern',
            plugins: [
                'advlist autolink lists link charmap print preview hr anchor pagebreak',
                'searchreplace wordcount visualblocks visualchars fullscreen autoresize',
                'insertdatetime media nonbreaking save table contextmenu directionality',
                'paste textcolor colorpicker textpattern codesample',
                'fixedimage ' + customPlugins + enableCodePlugin
            ],
            menubar: 'file edit insert view format table tools',
            toolbar1: 'insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image',
            toolbar2: 'print preview | forecolor backcolor fontselect fontsizeselect',
            toolbar3: elwisToolbar,
            image_advtab: true,
            my_uploadImageId_variable: '',
            file_browser_callback : ${enableImageBrowse},
            extended_valid_elements : "label[accesskey|class|dir<ltr?rtl|for|id|lang|onblur|onclick|ondblclick"
            + "|onfocus|onkeydown|onkeypress|onkeyup|onmousedown|onmousemove|onmouseout"
            + "|onmouseover|onmouseup|style|title]",
            browser_spellcheck: true,
            autoresize_bottom_margin: 2,
            autoresize_min_height: 250,
            elementpath: false,
            statusbar: false,
            setup: function(editor) {
                //on init event
                editor.on('init', function(e) {
                    <c:if test="${'true' eq enableCleanParagraphs}">
                        cleanEditorEmptyParagraphs(editor);
                    </c:if>
                });
            }
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
            function startImageUploadPopup(field_name, url, type, win) {
                //alert("Field_Name: " + field_name + "\nURL: " + url + "\nType: " + type + "\nWin: " + win); // debug/testing

                if (type == 'image') {

                    var cmsURL = ${urlImageBrowse};

                    tinymce.activeEditor.windowManager.open({
                        url : cmsURL,
                        title : '${imageUploadTitle}',
                        width : 500,
                        height : 200,
                        resizable : "yes",
                        close_previous : "no"
                    }, {
                        //parameters for upload popup
                        window : win,
                        srcInputName : field_name
                    });
                }

                return false;
            }

            /**
             * Clean all empty paragraphs of editor content
             * @param editor editor instance
             */
            function cleanEditorEmptyParagraphs(editor) {
                //alert("Clean paragraph on editor content:" + editor.getContent());

                //set content in editor to clean invalid elements, tinyMCE by default make this
                editor.setContent(editor.getContent());

                // use editor tinymce.dom.DOMUtils to manage editor content
                //select all paragraph of editor content
                var pArray = editor.dom.select("p");

                for (i = 0; i < pArray.length; i++) {
                    //get the text of paragraph with jquery
                    var pText = $(pArray[i]).text();

                    if(i > 0 && pText.trim() === "") {
                        //get the child tag nodes, remove only if this is empty
                        var pChildArray = $(pArray[i]).children();

                        if(pChildArray.length == 0) {
                            editor.dom.remove(pArray[i]);
                        }
                    }
                }

                //refresh the editor content
                editor.setContent(editor.getContent());
            }

        </c:if>

    </script>

</c:set>

<%--add scripts in tinyMCE4EditorItems Map, this is write in main.jsp--%>
<c:set target="${tinyMCE4EditorItems}" property="${textAreaId}" value="${initEditorFragment}"/>

