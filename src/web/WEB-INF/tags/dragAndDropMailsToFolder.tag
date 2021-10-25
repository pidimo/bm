<%--
Tag to add drag & drop functionality to move mail to folder from mail list. Some identifiers:

tdDragCls: css class to identify row in mail list table
folderDropCls: css class to identify folder
dragMailCls: css class to identify mailId hidden

this functionality is based in, implemented with jquery UI draggable, droppable:
http://blog.petersendidit.com/post/drag-table-row-to-a-div-with-jquery/
http://jsfiddle.net/petersendidit/cZare/2/?utm_source=website&utm_medium=embed&utm_campaign=cZare
--%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

<script src="<c:url value="/js/cacheable/jquery-ui-1.12.0.custom.draggable.droppable.min.js"/>" type="text/javascript"></script>

<app2:jScriptUrl url="/webmail/Mail/Ajax/MoveToFolder.do" var="jsMoveMailToFolderUrl">
    <app2:jScriptUrlParam param="dto(mailId)" value="mailIdVar"/>
    <app2:jScriptUrlParam param="dto(folderId)" value="folderIdVar"/>
</app2:jScriptUrl>

<c:set var="tableClasses" value="${app2:getFantabulousTableClases()}"/>

<div id="tempDragDiv"></div>

<script type="text/javascript">

    $(window).load(function() {
        // executes when complete page is fully loaded, including all frames, objects and images

        var tempDiv = $('#tempDragDiv');

        $(".tdDragCls").draggable({
            helper: function(event) {
                return $('<div class="dragMailItem"><table class="${tableClasses}" width="100%"></table></div>')
                        .find('table')
                        .append($(event.target).closest('tr').clone())
                        .end().appendTo(tempDiv);
            },
            start: function( event, ui ) {
                var elmDrop = $(".folderDropCls").droppable( "instance" );
                if(elmDrop == undefined) {
                    //initialize the droopable element
                    initializeDroppableElement();
                }
            },
            cursorAt: {
                left: 2
            },
            cursor: 'move',
            distance: 10,
            delay: 100,
            scope: 'mailToFolder-item',
            revert: 'invalid'
        });

    });


    function initializeDroppableElement() {
        $('.folderDropCls').droppable({
            scope: 'mailToFolder-item',
            hoverClass: 'folderDropHover',
            tolerance: 'pointer',
            drop: function(event, ui) {
                var helperElem = ui.helper;
                var dragElem = ui.draggable;
                var idAttr = $(this).attr('id');

                var emailId = $(helperElem).find('.dragMailCls').val();
                var folderId = splitFolderId(idAttr);

                //alert("emailid:" + emailId +" folderId:"+ folderId);
                moveMailToFolder(emailId, folderId, dragElem);
            }
        });
    }

    function splitFolderId(idAttr) {
        var idPrefix = "folder_";
        return idAttr.substring(idPrefix.length);
    }

    function moveMailToFolder(mailIdVar, folderIdVar, dragElement) {
        makeAjaxMoveMail(${jsMoveMailToFolderUrl}, "", dragElement);
    }

    function makeAjaxMoveMail(urlAction, parameters, dragElement) {
        $.ajax({
            async:true,
            type: "POST",
            dataType: "json",
            data:parameters,
            url:urlAction,
            success: function(responseJson) {
                // process json response
                if("Success" == responseJson.ajaxForward) {
                    hideRowInMailList(dragElement);
                }
            },
            error: function(ajaxRequest) {
                //alert("error:" + ajaxRequest);
            }
        });
    }

    function hideRowInMailList(dragElement) {
        $(dragElement).parent("tr").hide();
    }

</script>