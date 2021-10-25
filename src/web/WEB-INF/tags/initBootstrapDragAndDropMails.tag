<%--TODO drag an drop mails in to a folder
    USE
    you need to add these properties in the links table:

        ondragstart="dragStart(event,'#mailId')" ondragend="dragEnd(event)"

        only you need change "#mailId" with real id

    and these properties in links mailboxes

        ondrop="drop(event)" ondragover="allowDrop(event)" ondragenter="dragEnter(event)" ondragleave="dragLeave(event)"

--%>
<script language="JavaScript">

    var FLAG_START = false;
    var DRAG_ELEMENT_HIDE = null;

    var DRAG_DEFAULT_COLOR = null; // drag initial color
    var DRAG_CHANGE_COLOR = "yellow"; // drag default color to change

    var DROP_DEFAULT_COLOR = null; // drop initial color
    var DROP_CHANGE_COLOR = "yellow"; // drop default color to change

    function dragStart(event, mailIdAttr) {
        FLAG_START = true;
        DRAG_ELEMENT_HIDE = $(event.target).closest("tr");
        DRAG_DEFAULT_COLOR = DRAG_ELEMENT_HIDE.css("background-color");
        changeColorItem(DRAG_CHANGE_COLOR, DRAG_ELEMENT_HIDE);

        console.log("mail id:", mailIdAttr);
    }

    function dragEnd(event) {
        var element_hide = $(event.target).closest("tr");
        changeColorItem(DRAG_DEFAULT_COLOR, element_hide);
        FLAG_START = false;
    }

    function drop(event) {
        if (FLAG_START) {
            event.preventDefault();
            var element = $(event.target).closest("li");
            hideRowInTable();
            changeColorItem(DROP_DEFAULT_COLOR, element);
            FLAG_START = false;
        };
    }

    function allowDrop(event) {
        event.preventDefault();
    }


    function dragEnter(event) {
        if (FLAG_START) {
            var elementDrop = $(event.target).closest("li");
            DROP_DEFAULT_COLOR = elementDrop.css("background-color");
            changeColorItem(DROP_CHANGE_COLOR, elementDrop);
        };
    }

    function dragLeave(event) {
        if (FLAG_START) {
            var elementDrop = $(event.target).closest("li");
            changeColorItem(DROP_DEFAULT_COLOR, elementDrop);
        };
    }

    function hideRowInTable() {
        var rowInTable = DRAG_ELEMENT_HIDE[0];
        rowInTable.style.display = 'none';
    }

    function changeColorItem(newColor, elementToChange) {
        elementToChange.css("background-color", newColor);
    }

</script>