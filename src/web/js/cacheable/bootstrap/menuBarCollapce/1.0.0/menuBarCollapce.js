//
//   *************************** menuBar collapce *****************
//This is a book to collapse the menuBar menus in a dropdown
// author: J. Carlos Guzman A.
// company: JATUN.SRL
//*****************************************************************

!function ($) {
    $.fn.menuResponsive = function (option) {
        var content = $(option.container);
        var ul1 = $(option.UL1);
        var ulList1 = $(option.ul1List);
        var numOfItems = 0;
        var totalSpace = 0;
        var breakWidths = [];
        var totalSizeLi = 0;

        // Get instant state
        totalSpace = ul1.width();
        numOfItems = ulList1.size();
        var availableSpace, numOfVisibleItems, requiredSpace;

        graphicsUL2();
        //this function is to graphic the UL2
        function graphicsUL2(){
            var ulRigth='<ul class="nav navbar-nav navbar-right" id="myUL2">' +
                '<li class="dropdown dropdown-hover" id="myLi2"> ' +
                '<a class="has-submenu" href="#" data-toggle="dropdown" role="button" aria-haspopup="true"aria-expanded="false"> ' +
                '<span id="tabName"><i class="glyphicon glyphicon-align-justify"></i> </span> ' +
                '<b class="caret"></b> ' +
                '</a> ' +
                '<ul class="dropdown-menu" id="ListULS"> </ul> ' +
                '</li> ' +
                '</ul>';
            if ($('#myUL2').length){
                //Ejecutar si existe el elemento
            }else{
                $(content).append(ulRigth);
            }
        }
        var ul2 = $('#myUL2');
        var listUl2 = $('#ListULS');

        initializeCollapce();

        //this function is to reset the variables
        function clearVar(){
            totalSizeLi = 0;
            breakWidths = [];
        }
        //this function is executed each time the page is updated
        function initializeCollapce(){
            var ul2 = $('#myUL2');
            if($(window).width() <= 755){
                ul2.hide();
            }else{
                var listUl2 = $('#ListULS');
                if(listUl2.size()>0){
                    check();
                    if ($(window).width() >= 1325) {
                        reconstUL1();
                    }
                    $('#myUL2').smartmenus('refresh');
                    ul1.smartmenus('refresh');
                }else{
                    ul2.hide();
                }
            }
        }
        // This function check of the items to be moved or recover between the two uls
        function check(){
            for(var i = 0; i < numOfItems; i++){
                var liElement = ulList1.get(i);
                totalSizeLi=totalSizeLi+$(liElement).width();
                breakWidths.push(totalSizeLi);
            }
            // Get instant state
            availableSpace = (content.width() - (ul2.width()+110));
            numOfVisibleItems = ul1.children().length;
            requiredSpace = breakWidths[numOfVisibleItems - 1];
            if (requiredSpace > availableSpace) {
                ul1.children().last().prependTo(listUl2);
                numOfVisibleItems -= 1;
                check();
                // There is more than enough space
            } else if (availableSpace > breakWidths[numOfVisibleItems]) {
                listUl2.children().first().appendTo(ul1);
                numOfVisibleItems += 1;
            }

            clearVar();

            // Update the button accordingly
            if (numOfVisibleItems === numOfItems) {
                ul2.hide();
            } else {
                var res = document.querySelector('#ListULS li.active');
                if (res == null){
                    $('#tabName').html('<i class="glyphicon glyphicon-align-justify"></i>');
                    $('#myLi2').removeClass('active');
                }else{
                    $('#tabName').html($('#ListULS>li.active>a').text()+'  <i class="glyphicon glyphicon-align-justify"></i>');
                    $('#myLi2').addClass('active');
                }
                ul2.show();
            }
        }

        //This function is to rebuild the UL1
        function reconstUL1(){
            var ul2 = $('#myUL2');
            var listUl2 = $('#ListULS');
            if(listUl2.size()>0){
                for(var i=0; i < listUl2.size()+2; i++){
                    listUl2.children().first().appendTo(ul1);
                }
            }
            ul2.hide();
            ul1.smartmenus('refresh');
        }

        // Window listeners
        $(window).on('resize', function(){
            var win = $(this); //this = window
            if (win.width() > 755) {
                check();
                if (win.width() > 1325) {
                    reconstUL1();
                }
                $('#myUL2').smartmenus('refresh');
                ul1.smartmenus('refresh');
            }else{
                reconstUL1();
            }
        });
    }
}(window.jQuery);

