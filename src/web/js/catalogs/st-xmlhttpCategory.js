function selectedOption_II(sourceId, targetId) {
    var sourceBox = document.getElementById(sourceId);
    var targetBox = document.getElementById(targetId);
    var i = 0;
    while (i < sourceBox.options.length) {
        var opt = sourceBox.options[i];
        if (opt.selected) {
            var nOpt = new Option();
            nOpt.text = opt.text;
            nOpt.value = opt.value;
            targetBox.options[targetBox.options.length] = nOpt;
            sourceBox.remove(i);
        } else {
            i = i + 1;
        }
    }
}

function makeHttpRequest(url, callback_function, categoryId, return_xml, errorprocess_function)
{
    var http_request = false;

    if (window.XMLHttpRequest) { // Mozilla, Safari,...
        http_request = new XMLHttpRequest();
        if (http_request.overrideMimeType) {
            http_request.overrideMimeType('text/xml');
        }
    } else if (window.ActiveXObject) { // IE
        try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                http_request = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {
            }
        }
    }

    if (!http_request) {
        alert('Unfortunatelly you browser doesn\'t support this feature.');
        return false;
    }
    http_request.onreadystatechange = function() {
        if (http_request.readyState == 4) {
            if (http_request.status == 200) {
                if (return_xml) {
                    eval(callback_function + '(http_request.responseXML)');
                } else {
                    eval(callback_function + '(http_request.responseText, categoryId)');
                }
            } else {
                if (errorprocess_function != null && errorprocess_function != undefined) {
                    eval(errorprocess_function + '(http_request.status, categoryId)');
                } else {
                    alert('There was a problem with the request.(Code: ' + http_request.status + ')');
                }
            }
        }
    }
    http_request.open('GET', url, true);
    http_request.setRequestHeader("isAjaxRequest", "true");
    http_request.send(null);
}

function fillMultipleSelectValues() {
    var selectElements = document.getElementsByTagName('select');

    for (i = 1; i <= selectElements.length; i++) {
        var select = selectElements[i];
        var selectId = select.id;
        if (selectId.indexOf('selected_') == 0) {
            fillHiddenSelect(selectId);
        }
    }
}
function fillHiddenSelect(styleId) {
    var box = document.getElementById(styleId);
    var ids = '';
    for (j = 0; j < box.options.length; j++) {
        ids = ids + box.options[j].value + ',';
    }
    document.getElementById('hidden_' + styleId).value = ids;
}