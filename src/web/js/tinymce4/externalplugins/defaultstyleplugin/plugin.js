
/***************************************************************/
//TinyMCE 4 plugin configuration
// Creates a new plugin class to font plugin
// this define default styles for body content

tinymce.PluginManager.add('defaultstyleplugin', function(editor, url) {

    editor.on('init', function(args) {

        var styleContent = composeStyleContent();
        if(styleContent != null){
            var styleElement = editor.dom.add(editor.dom.select('head')[0], 'style', {type:'text/css'});
            if(styleElement.styleSheet){// IE
                styleElement.styleSheet.cssText = styleContent;
            } else {// w3c
                editor.dom.setHTML(styleElement, styleContent);
            }
        }
        //alert("XXXX22:"+editor.dom.select('head')[0].innerHTML);
    });

    /*editor.on('SetContent', function (e) {
        alert("set conten event.... : " + e.content);

        //remove <style> tag
        var styleList = editor.dom.select('style');
        if(styleList != undefined){
            editor.dom.remove(styleList);
        }
    });*/

    editor.on('GetContent', function (e) {
        //var docStyle = " <style type=\"text/css\"> BODY { font-size:15pt; font-family:verdana;} </style>";
        var styleContent = composeStyleContent();
        if(styleContent != null){
            var docStyle = " <style type=\"text/css\"> "+ styleContent +" </style>";
            e.content = docStyle + '\n' + e.content;
        }
    });

});

/**
 * function to compose css properties
 */
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

