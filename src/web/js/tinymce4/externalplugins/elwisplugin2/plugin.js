/**
 * Elwis plugin
 */

// Object that will encapsulate fields of elwis data base
ElwisConfig.buttonType = MenuElwisConfig.buttonType;
ElwisConfig.menuType = MenuElwisConfig.menuType;
ElwisConfig.itemType = MenuElwisConfig.itemType;
ElwisConfig.subMenuType = MenuElwisConfig.subMenuType;

function ElwisConfig() {
    this.itemList = {};
    this.buttonList = new Array();
    this.varTagName = "label";
    ///this.leftDelimiter = "";
    ///this.rightDelimiter = "";
    this.leftDelimiter = "[||";
    this.rightDelimiter = "||]";

    //initialize config
    this.buildMenuItems();
    //	this.buildTestMenuItems();  //only to test
    //	this.addHtmlTestButton();  //only to test

}
;

ElwisConfig.prototype.buildTestMenuItems = function() {
    ///////buttons///////
    var buttonId1 = "ElwisM1";
    var buttonId2 = "ElwisM2";
    var buttonObj1 = {
        type: ElwisConfig.menuType,
        id: buttonId1,
        resource: "Elwis menu1",
        tooltip: "Elwis menu1",
        varId: null,
        varResource: null,
        varTranslations: new Array()
    };
    var buttonObj2 = {
        type: ElwisConfig.menuType,
        id: buttonId2,
        resource: "Elwis menu2",
        tooltip: "Elwis menu2",
        varId: null,
        varResource: null,
        varTranslations: new Array()
    };

    //add buttons to list
    this.buttonList.push(buttonObj1);
    this.buttonList.push(buttonObj2);


    /////////menu items//////////
    var item1 = {
        id    : "T1",
        resource : "item 1",
        varId : "V1",
        varResource : "**item 1**",
        isSubMenu    : false,
        level : "1",
        varTranslations: new Array()
    };
    var item2 = {
        id    : "T2",
        resource : "item 2",
        varId : "V2",
        varResource : "**item 2**",
        isSubMenu    : true,
        level : "1",
        varTranslations: new Array()
    };
    var item3 = {
        id    : "T3",
        resource : "item 3",
        varId : "V3",
        varResource : "**item 3**",
        isSubMenu    : false,
        level : "1",
        varTranslations: new Array()
    };
    //items nivel 2
    var item4 = {
        id    : "T4",
        resource : "item 4",
        varId : "V4",
        varResource : "**item 4**",
        isSubMenu    : true,
        level : "2",
        varTranslations: new Array()
    };
    var item5 = {
        id    : "T5",
        resource : "item 5",
        varId : "V5",
        varResource : "**item 5**",
        isSubMenu    : false,
        level : "2",
        varTranslations: new Array()
    };

    //items nivel 3
    var item6 = {
        id    : "T6",
        resource : "item 6",
        varId : "V6",
        varResource : "**item 6**",
        isSubMenu    : false,
        level : "3",
        varTranslations: new Array()
    };
    var item7 = {
        id    : "T7",
        resource : "item 7",
        varId : "V7",
        varResource : "**item 7**",
        isSubMenu    : false,
        level : "3",
        varTranslations: new Array()
    };


    this.itemList[buttonId1] = [ item1, item2, item3];
    this.itemList[buttonId2] = [ item2, item1];
    this.itemList["T2"] = [item4,item5];
    this.itemList["T4"] = [item6,item7];

};

ElwisConfig.prototype.addHtmlTestButton = function() {
    var buttonObj = {
        type: "showHtml",
        id: "btn_showHtml",
        resource: "show html",
        tooltip: "show html",
        varId: null,
        varResource: null
    };

    //add buttons to list
    this.buttonList.push(buttonObj);
};

ElwisConfig.prototype.buildMenuItems = function() {
    var self = this;
    for (var i in MenuElwisConfig.ElwisMenu) {
        var menuConfig = MenuElwisConfig.ElwisMenu[i];
        var buttonId = "btn_" + i;

        //button object
        var buttonObj = {
            type: menuConfig.type,
            id: buttonId,
            resource: menuConfig.resource,
            tooltip: menuConfig.tooltip || menuConfig.resource,
            varId: menuConfig.varId || null,
            varResource: ((menuConfig.varResource) ? self.composeItemVarResourceLabel(menuConfig.varResource) : null),
            varTranslations: ((menuConfig.varTranslations) ? menuConfig.varTranslations : null)
        };

        //create button list to this plugin
        this.buttonList.push(buttonObj);

        if (menuConfig.type == ElwisConfig.menuType) {
            //register menu items
            this.registerItems(buttonId, menuConfig.items, 1);
        } else if (buttonObj.varId && buttonObj.varResource) {
            //set this button has item
            this.itemList[buttonId] = new Array();
            this.itemList[buttonId].push(buttonObj);
        }
    }
};

ElwisConfig.prototype.registerItems = function(menuItemId, itemConfigList, itemLevel) {
    var self = this;
    if (itemConfigList) {

        this.itemList[menuItemId] = new Array();
        for (var i in itemConfigList) {
            var itemConfig = itemConfigList[i];
            var itemId = "item_" + menuItemId + i;
            var item = {
                id    : itemId,
                resource : itemConfig.resource,
                varId : itemConfig.varId,
                varResource : self.composeItemVarResourceLabel(itemConfig.varResource),
                varTranslations : itemConfig.varTranslations,
                isSubMenu    : (itemConfig.type == ElwisConfig.subMenuType),
                level : itemLevel
            };

            this.itemList[menuItemId].push(item);

            if (itemConfig.type == ElwisConfig.subMenuType) {
                this.registerItems(itemId, itemConfig.items, itemLevel + 1);
            }
        }
    }
};

//compose variable item resource label
ElwisConfig.prototype.composeItemVarResourceLabel = function(itemVarResource) {
    return this.leftDelimiter + itemVarResource + this.rightDelimiter;
};

//build variable, return HTML
ElwisConfig.prototype.buildVariable = function(id, resource) {
    var elVarTag = '<' + this.varTagName + ' id=\"' + id + '\">' + resource + '</' + this.varTagName + '>';
    var parentTag = '<span>&nbsp;' + elVarTag + '&nbsp;</span>';
    return parentTag;
};

//set variable in editor
ElwisConfig.prototype.setVariable = function(varId, varResource) {
    var variable = this.buildVariable(varId, varResource);
    tinyMCE.activeEditor.insertContent(variable);
};

/*get last text node*/
ElwisConfig.prototype.getDOMChildTextNode = function(node) {
    var resultNode = undefined;
    var DOMTextNodeType = 3;
    while (node.firstChild) {
        node = node.firstChild;
    }

    if (node.nodeType == DOMTextNodeType) {
        resultNode = node;
    }
    return resultNode;
};

/*validate variable node child tags, this generally to IE*/
ElwisConfig.prototype.isValidVariableNode = function(node) {
    if (node.childNodes && node.childNodes.length > 1) {
        return false;
    }
    while (node.firstChild) {
        node = node.firstChild;
        if (node.childNodes && node.childNodes.length > 1) {
            return false;
        }
    }
    return true;
};

/*................utils..........................*/
String.prototype.trim = function() {
    a = this.replace(/^\s+/, '');
    return a.replace(/\s+$/, '');
};
/*................utils..........................*/


/***************************************************************/
//initialize elwis config Object
var oElwisConfig = new ElwisConfig();
/***************************************************************/



/***************************************************************/
/***************************************************************/
//TinyMCE 4 plugin configuration
// Creates a new plugin class to elwis variables

tinymce.PluginManager.add('elwisplugin2', function(editor, url) {

    //test menu button
    editor.addButton('mybuttontest', {
        type: 'menubutton',
        text: 'My button',
        icon: false,
        menu: [{
            text: 'Menu item 1',
            menu: [{
                text: 'Menu item 1.1',
                onclick: function() {
                    editor.insertContent('&nbsp;<strong>Menu item 1.1 here!</strong>&nbsp;');
                }
            }, {
                text: 'Menu item 1.2',
                onclick: function() {
                    editor.insertContent('&nbsp;<em>Menu item 1.2 here!</em>&nbsp;');
                }
            }]
        }, {
            text: 'Menu item 2',
            onclick: function() {
                editor.insertContent('&nbsp;<em>Menu item 2 here!</em>&nbsp;');
            }
        }]
    });

    // Add a button like date button
    for (var i in oElwisConfig.buttonList) {
        var btn = oElwisConfig.buttonList[i];
        if (btn) {
            if (btn && btn.type == ElwisConfig.buttonType) {
                //add button
                editor.addButton(btn.id, {
                    text: btn.resource,
                    title : btn.tooltip,
                    elwisVariableHtml : oElwisConfig.buildVariable(btn.varId, btn.varResource),
                    icon: false,
                    onclick: function () {
                        editor.insertContent(this.settings.elwisVariableHtml);
                    }
                });
            }
        }
    }

    //menu creation
    for (var i in oElwisConfig.buttonList) {
        var btn = oElwisConfig.buttonList[i];
        if (btn) {
            if (btn.type == ElwisConfig.menuType) {

                //add menu button
                editor.addButton(btn.id, {
                    type: 'menubutton',
                    text: btn.resource,
                    icon: false,
                    menu: buildEditorMenuItems(editor, oElwisConfig.itemList[btn.id])
                });
            }
        }
    }

    //define editor events
    editor.on('KeyUp', function (e) {
        onUpdateEditorArea(editor);
    });

    editor.on('NodeChange', function (e) {
        onUpdateEditorArea(editor);
    });

});
/********************** End plugin configuration *************************/


function buildEditorMenuItems(editor, elwisVarItems) {
    var menuItemList = [];

    for (var j in elwisVarItems) {
        var elwisItem = elwisVarItems[j];
        var menuItem = {
            text: elwisItem.resource
        };

        if (elwisItem.isSubMenu) {
            menuItem.menu = buildEditorMenuItems(editor, oElwisConfig.itemList[elwisItem.id]);
        } else {
            menuItem.elwisVariableHtml = oElwisConfig.buildVariable(elwisItem.varId, elwisItem.varResource);

            menuItem.onclick = function(e) {
                //alert("var: " +this.settings.elwisVariableHtml);
                editor.insertContent(this.settings.elwisVariableHtml);
            };
        }

        menuItemList.push(menuItem);
    }

    return menuItemList;
}
;



/***************************************************************/
//external functions
function getElwisConfigInstance() {
    return oElwisConfig; //
}
;


/**
 *called when Editor execute onChangeEvent and onKeyUp event
 *usage to select variable when cursor take a stand in this
 */
function onUpdateEditorArea(ed) {
    var node = ed.selection.getNode();
    var ancestors = getAllAncestors(node);

    for (var k in ancestors) {
        if (!ancestors[k]) {
            // the impossible really happens.
            continue;
        }
        var el = ancestors[k];
        if (el && el.tagName.toLowerCase() == oElwisConfig.varTagName && el.id) {
            //select varaible when cursor take a stand in this
            ed.selection.select(el);
            ed.focus();
            var existVariable = false;

            for (var i in oElwisConfig.itemList) {
                var menuList = oElwisConfig.itemList[i];
                for (var j in menuList) {
                    var item = menuList[j];

                    if (item.varId == el.id) {
                        existVariable = true;

                        if (oElwisConfig.isValidVariableNode(el)) {

                            var varTextNode = oElwisConfig.getDOMChildTextNode(el);
                            //alert(item.varResource +" : "+varTextNode.nodeValue);

                            if (varTextNode == undefined || varTextNode.nodeValue == undefined) {
                                //alert("remove 1");
                                el.parentNode.removeChild(el);
                            } else {
                                if (varTextNode.nodeValue.trim() == "") {
                                    //alert("remove 2");
                                    el.parentNode.removeChild(el);

                                } else if (varTextNode.nodeValue != item.varResource) {
                                    var isEqual = false;
                                    for (var x in item.varTranslations) {
                                        if (varTextNode.nodeValue == oElwisConfig.composeItemVarResourceLabel(item.varTranslations[x])) {
                                            isEqual = true;
                                            break;
                                        }
                                    }
                                    if (!isEqual) {
                                        varTextNode.nodeValue = item.varResource;
                                        //select the update value
                                        ed.selection.select(el);
                                        ed.focus();
                                    }
                                }
                            }
                        } else {
                            //replace tag variable
                            el.parentNode.removeChild(el);
                            oElwisConfig.setVariable(item.varId, item.varResource);
                        }

                        break;
                    }
                }
                if (existVariable) break;
            }

            if (!existVariable) {
                //alert("remove 5");
                //OBS:this generally to gecko , remove this tag
                el.parentNode.removeChild(el);
            }
            break;
        }
    }
}
;

function getAllAncestors(el) {
    var a = [];
    while (el && (el.nodeType == 1) && (el.tagName.toLowerCase() != 'body')) {
        a.push(el);
        el = el.parentNode;
    }
    if (el && (el.nodeType == 1) && (el.tagName.toLowerCase() == 'body')) {
        a.push(el);
    }
    return a;
}
;














