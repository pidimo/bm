/**
 * script to define default style class for body in the editor
 */
function StyleConfig(){
    this.name = "Body style class configuration";
};

StyleConfig.getEditorFont = function(){
    return "Verdana, Arial, Helvetica, sans-serif";
};

StyleConfig.getEditorFontSize = function(){
    return "10pt";
};