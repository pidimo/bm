<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>


<%--only one init fragment should be write--%>
<c:if test="${empty initMultipleSelectModalLoaded}">
    <c:set var="initMultipleSelectModalLoaded" value="true" scope="request"/>

    <!-- multiple select Modal -->
    <c:set var="styleId" value="multipleSelectModal"/>

    <div class="modal fade bs-example-modal-sm" id="${styleId}" tabindex="-1"
         role="dialog" aria-labelledby="${styleId}Label">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-header titleModalClearfix">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="${styleId}Label">
                    </h4>
                </div>
                <div class="modal-body">
                    <div id="${styleId}ModalContainer">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="${app2:getFormButtonClasses()}" data-dismiss="modal" onclick="putSelectedItems()">
                        <fmt:message key="Common.select"/>
                    </button>
                    <button type="button" class="${app2:getFormButtonCancelClasses()}" data-dismiss="modal">
                        <fmt:message key="Common.cancel"/>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <%--temp checkbox--%>
    <div class="divCheckboxClsId checkbox checkbox-default" style="display: none">
        <input type="checkbox" id="tempChechbox"  name="tempCheckboxName" value=""/>
        <label for="tempChechbox"></label>
    </div>


    <script language="JavaScript">
        function launchMultipleSelectModal(selectedBoxId, availableBoxId, title, onSelectFunc) {
            modalId = '${styleId}';
            boxSelectedId = selectedBoxId;
            boxAvailableId = availableBoxId;
            onSelectFunction = onSelectFunc;

            setMultipleSelectModalTitle(modalId, title);
            emptyMultipleSelectModalContainer();
            generateModalBody(selectedBoxId, availableBoxId);

            //show popup
            $("#" + modalId).modal({
                backdrop: 'static'
            });
        }

        function setMultipleSelectModalTitle(styleId, title){
            $("#"+styleId+"Label").text(title);
        }

        function putSelectedItems() {
            $("#" + getModalBodyContainerId()).find("[type=checkbox]").each(function (index) {

                if (index == 0) {
                    defineAllOptionsAsAvailable(boxSelectedId, boxAvailableId);
                }

                if($(this).prop('checked')) {
                    setOptionAsSelected(boxSelectedId, boxAvailableId, $(this).val());
                }
            });

            //empty the modal body
            emptyMultipleSelectModalContainer();

            //execute onSelect function
            if(onSelectFunction != null && onSelectFunction != 'null'){
                eval(onSelectFunction);
            }
        }

        function getModalBodyContainerId() {
            return  modalId + "ModalContainer";
        }

        function emptyMultipleSelectModalContainer() {
            $("#" + getModalBodyContainerId()).empty();
        }

        function generateModalBody(selectedBoxId, availableBoxId) {
            var i = 0;

            $("#"+ selectedBoxId +" > option").each(function() {
                addCheckbox($(this).text(), $(this).val(), true, i);
                i++;
            });

            $("#"+ availableBoxId +" > option").each(function() {
                addCheckbox($(this).text(), $(this).val(), false, i);
                i++;
            });
        }

        function addCheckbox(label, value, isSelected, index) {

            var cloneDiv = $(".divCheckboxClsId").first().clone();
            $(cloneDiv).removeAttr("style");
            $(cloneDiv).removeAttr("id");
            $(cloneDiv).removeClass("divCheckboxClsId");

            $(cloneDiv).find("[type=checkbox]").each(function() {
                $(this).val(value);
                $(this).attr("id", "check_" + index);
                $(this).attr("name", "checkboxName");

                if(isSelected) {
                    $(this).prop('checked', true);
                }
            });

            $(cloneDiv).find("label").each(function() {
                $(this).text(label);
                $(this).attr("for", "check_" + index);
            });

            //append to container
            $(cloneDiv).appendTo("#" + getModalBodyContainerId());
        }

        function defineAllOptionsAsAvailable(selectedBoxId, availableBoxId) {
            $("#"+ selectedBoxId +" > option").each(function() {
                var option = new Option($(this).text(), $(this).val());
                $("#" + availableBoxId ).append($(option));
            });

            //clean all selected values
            $("#" + selectedBoxId ).empty();
        }

        function setOptionAsSelected(selectedBoxId, availableBoxId, optionValue) {
            $("#"+ availableBoxId +" > option").each(function() {

                if($(this).val() == optionValue) {
                    var option = new Option($(this).text(), $(this).val());
                    $("#" + selectedBoxId ).append($(option));

                    //remove the selected option
                    $(this).remove();
                    return false;
                }
            });
        }

        function hideMultipleSelectBootstrapPopup() {
            //hide modal dialog
            $("#" + modalId).modal('hide');
        }

    </script>

</c:if>


