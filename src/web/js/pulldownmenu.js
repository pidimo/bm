/*

 Elwis- Business Manager On Demand DHTML Pull Down Menu JavaScript.
 @Version 1.0 - Built: Tuesday November 01 2005 - 14:38
 @author Fer
 Copyright 2005 (c) JaTun SRL - pirAMide GmbH Limited. All Rights Reserved.
 */


function PullDownMenu(id, alignment) {
    var align = alignment;

    var rootDiv = $("#" + id);
    var menuDiv = $("#" + id + "div");
    var IfrRef = $("#" + id + "ifr");
    var pullMenuTimeOut;

    rootDiv.mouseover(function() {
        //first clear the timer if exist
        window.clearTimeout(pullMenuTimeOut);

        if(!isMenuVisible()) {
            var rootDivOffset = rootDiv.offset();
            var menuTop = rootDivOffset.top + rootDiv.height();
            var menuLeft;

            if (align == 'right') {
                menuLeft = rootDivOffset.left - (menuDiv.outerWidth() - rootDiv.outerWidth());
            } else {
                menuLeft = rootDivOffset.left;
            }

            //menu div re-dimensions
            menuDiv.css("display","block");
            menuDiv.offset({ top: menuTop, left: menuLeft });

            //iframe re-dimensions
            IfrRef.width(menuDiv.width());
            IfrRef.height(menuDiv.height());
            IfrRef.offset({ top: menuTop, left: menuLeft });
            var ifrZIndex = menuDiv.css("zIndex") - 1;
            IfrRef.css("zIndex",ifrZIndex);

              //if (document.all) //IE only
                //IfrRef.css("display","block");

            menuDiv.css("visibility","visible");
            IfrRef.css("visibility","visible");
        }
    });

    rootDiv.mouseout(function() {
        pullMenuTimeOut = window.setTimeout(
                function() {
                    menuDiv.css("visibility","hidden");
                    IfrRef.css("visibility","hidden");
                }, 1000);
    });

    menuDiv.mouseover(function() {
        window.clearTimeout(pullMenuTimeOut);
        if(!isMenuVisible()) {
            menuDiv.css("visibility","visible");
            IfrRef.css("visibility","visible");
        }
    });

    menuDiv.mouseout(function() {
        rootDiv.mouseout();
    });

    function isMenuVisible() {
        return (menuDiv.css("visibility") == "visible");
    }
}


