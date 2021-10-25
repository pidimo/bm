<%--
Tag to enable token fields for bootstrap, thi use two libraries:
tokenfield for bootstrap
http://sliptree.github.io/bootstrap-tokenfield/
typeahead
https://twitter.github.io/typeahead.js/examples/
--%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

<%@ attribute name="inputTextId" required="true" %>
<%@ attribute name="identifierPrefix" required="true" %>
<%@ attribute name="rewriteTokenFieldMapList" type="java.util.List" %>


<app2:jScriptUrl url="/webmail/Mail/Ajax/Find/Recipients.do" var="jsFindRecipientUrl">
</app2:jScriptUrl>

<%--div to add token fild hiddens--%>
<div id="div_${inputTextId}" style="display: none"></div>

<script type="text/javascript">

    $(document).ready(function () {

        //compile the menu item template
        var resultTemplate = Handlebars.compile($("#typeheadResultTemplate").html());


        //initialize token field
        $("#" + "${inputTextId}").tokenfield({
            createTokensOnBlur: true,
            typeahead: [{
                highlight: true,
                hint: true,
                minLength : 1
            } , {
                name: "ajaxDataSet",
                source: function (query, syncResults, asyncResults) {
                    //var dataTest = [{label: "juan perez", value: "juan@gmail.com",  addressId: "9", contactPersonOfId: "10"}, {label: "pepe aguilar", value: "aguilar@gmail.com",  addressId: "15", contactPersonOfId: ""}];
                    //syncResults(dataTest);

                    $.ajax({
                        url: ${jsFindRecipientUrl},
                        type: "GET",
                        data: "tokenFieldQuery=" + query,
                        dataType: "json",
                        async: true,
                        success: function (responseJson) {

                            var resultJsonArray = [];
                            if("Success" == responseJson.ajaxForward) {
                                resultJsonArray = responseJson.resultDataArray;
                            }
                            //load in plugin
                            asyncResults(resultJsonArray);
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            console.log("Status: " + textStatus);
                            console.log("Error: " + errorThrown);
                        }
                    });
                },
                display: "label",
                async: true,
                limit: 100,
                templates: {
                    suggestion: resultTemplate
                }
            }]

        }).on('tokenfield:createtoken', function (e) {
            //event is target when token will be create, so define the identifier for this
            var tokenId = '${identifierPrefix}_' + String(tokenFieldCount);
            //define the identifier
            e.attrs.id = tokenId;

            tokenFieldCount++;

        }).on('tokenfield:createdtoken', function (e) {
            /*console.log("createdtoken id: " + e.attrs.id);
            console.log("createdtoken calue: " + e.attrs.value);
            console.log("createdtoken label: " + e.attrs.label);
            console.log("createdtoken addressid: " + e.attrs.addressId);
            console.log("createdtoken contactPersonOf id: " + e.attrs.contactPersonOfId);*/

            var guiaId = e.attrs.id;
            var emailAddress = e.attrs.value;

            var guiaElem = $('<input type="hidden">').attr({name: 'dto(tokenFieldIdentifier_'+ guiaId +')', id: guiaId});
            var emailElem = $('<input type="hidden">').attr({name: 'dto(tokenEmail_'+ guiaId +')', id: 'tokenEmail_'+ guiaId});
            var contactNameElem = $('<input type="hidden">').attr({name: 'dto(tokenContactName_'+ guiaId +')', id: 'tokenContactName_'+ guiaId});
            var addressIdElem = $('<input type="hidden">').attr({name: 'dto(tokenAddressId_'+ guiaId +')', id: 'tokenAddressId_'+ guiaId});
            var contactPersonOfElem = $('<input type="hidden">').attr({name: 'dto(tokenContactPersonOfId_'+ guiaId +')', id: 'tokenContactPersonOfId_'+ guiaId});

            $(guiaElem).val(guiaId);
            $(emailElem).val(emailAddress);
            $(contactNameElem).val(e.attrs.label);
            $(addressIdElem).val(e.attrs.addressId);
            $(contactPersonOfElem).val(e.attrs.contactPersonOfId);

            var divElem = $('#div_${inputTextId}');

            $(divElem).append(guiaElem);
            $(divElem).append(emailElem);
            $(divElem).append(contactNameElem);
            $(divElem).append(addressIdElem);
            $(divElem).append(contactPersonOfElem);

            // token e-mail validation
            if (!isValidRecipientEmail(emailAddress)) {
                $(e.relatedTarget).addClass('tokenInvalid');
            }

        }).on('tokenfield:editedtoken', function (e) {
            //event target when token has been edited. So we should be remove the hiddens
            //beacuse this will be created anew
            //console.log("editedtoken id: " + e.attrs.id);

            removeTokenFieldHiddens(e.attrs.id);

        }).on('tokenfield:removedtoken', function (e) {
            //remove the hiddens related with this token
            //console.log("removedtoken id: " + e.attrs.id);

            removeTokenFieldHiddens(e.attrs.id);
        });


        //rewrite old token fields,
        var rewriteData = [
        <c:if test="${not empty rewriteTokenFieldMapList}">
            <c:forEach var="item" items="${rewriteTokenFieldMapList}" varStatus="statusVar">
                <c:if test="${statusVar.index > 0}">, </c:if>
                {
                    "label" : "${app2:escapeJSON(item.contactName)}",
                    "value" : "${app2:escapeJSON(item.email)}",
                    "addressId" : "${item.addressId}",
                    "contactPersonOfId" : "${item.contactPersonOfId}"
                }
            </c:forEach>
        </c:if>
        ];

        $("#" + "${inputTextId}").tokenfield('setTokens', rewriteData);


        //define the z-index, this is required to show menu
        defineTokenFieldZIndex();

    });


    function removeTokenFieldHiddens(guiaId) {
        //remove hiddens
        $('#' + guiaId).remove();
        $('#tokenEmail_' + guiaId).remove();
        $('#tokenContactName_' + guiaId).remove();
        $('#tokenAddressId_' + guiaId).remove();
        $('#tokenContactPersonOfId_' + guiaId).remove();
    }


    function iterateRecipientJsonArray(resultJsonArray) {
        $.each(resultJsonArray, function (i, v) {
            console.log(v.label +" - "+v.value+" - "+ v.addressId+ " - "+ v.contactPersonOfId);
        });
    }

</script>
