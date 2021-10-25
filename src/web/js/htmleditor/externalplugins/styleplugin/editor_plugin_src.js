
/***************************************************************/
//TinyMCE plugin configuration
// Creates a new plugin class to font plugin
tinymce.create('tinymce.plugins.DocumentStyle', {

    /**
     * Initializes the plugin, this will be executed after the plugin has been created.
     * This call is done before the editor instance has finished it's initialization so use the onInit event
     * of the editor instance to intercept that event.
     *
     * @param {tinymce.Editor} ed Editor instance that the plugin is initialized in.
     * @param {string} url Absolute URL to where the plugin is located.
     */
    init : function(ed, url) {

        //here set style
        ed.onInit.add(function(ed) {
            var styleContent = composeStyleContent();
            if(styleContent != null){
                var styleElement = ed.dom.add(ed.dom.select('head')[0], 'style', {type:'text/css'});
                if(styleElement.styleSheet){// IE
                    styleElement.styleSheet.cssText = styleContent;
                } else {// w3c
                    ed.dom.setHTML(styleElement, styleContent);
                }
            }
            ///alert("XXXX22:"+ed.dom.select('head')[0].innerHTML);
        });

        ed.onSetContent.add(function(ed, o) {
            //remove <style> tag
            var styleList = ed.dom.select('style');
            if(styleList != undefined){
                ed.dom.remove(styleList);
            }
        });

        ed.onGetContent.add(function(ed, o) {
            //var docStyle = " <style type=\"text/css\"> BODY { font-size:15pt; font-family:verdana;} </style>";
            var styleContent = composeStyleContent();
            if(styleContent != null){
                var docStyle = " <style type=\"text/css\"> "+ styleContent +" </style>";
                o.content = docStyle + '\n' + o.content;
            }
        });
    }
});

function composeStyleContent(){
    var styleContent = null;
    var userFont = StyleConfig.getEditorFont();
    var userFontSize = StyleConfig.getEditorFontSize();
    var styleAttr = null;

    if( userFont != null && userFont != undefined){
        styleAttr = "font-family:" + userFont + ";";
    }

    if( userFontSize != null && userFontSize != undefined){
        if(styleAttr == null) styleAttr = "";
        styleAttr = styleAttr + " font-size:" + userFontSize + ";";
    }

    if(styleAttr != null){
        styleContent = " body, td, pre { "+ styleAttr +" }";
    }
    return styleContent;
}

// Register plugin with a short name
tinymce.PluginManager.add('styleplugin', tinymce.plugins.DocumentStyle);
/***************************************************************/

