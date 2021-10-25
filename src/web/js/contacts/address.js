<!--

/*** Javascript functions with utilities for modules **/

/** Open a URL in Browser **/
function openURL(fieldId)
{
    var field = document.getElementById(fieldId);
    var value = field.value;
    if (value != '') {
        if (value.indexOf(':') == -1) { //has no protocol symbol
            window.open('http://' + value);
            //adding a default protocol
        } else {
            window.open(value);
            //open with the protocol defined
        }
    }
}


function hideAddressForm()
{
    document.getElementById('formPanel').style.visibility = "hidden";
    document.getElementById('formPanel').style.position = "absolute";
}

function showAddressForm()
{
    document.getElementById('formPanel').style.position = "static";
    document.getElementById('formPanel').style.visibility = "visible";
    document.getElementById('duplicatedListPanel').style.visibility = "hidden";
    document.getElementById('confirmDuplicatedCreationId').value = "false";
}

function hideImageDiv()
{
    document.getElementById('imageDivId').style.visibility = "hidden";
    document.getElementById('imageDivId').style.position = "absolute";
    document.getElementById('imageRemovedFlagId').value = "true";
}
function callto(dataId, protocol) {
    data = document.getElementById(dataId);
    stripped = data.value.replace(/[\(\)\.\-\ \/\\]/g, '');
    window.location.href = protocol + ':' + stripped;
}

//-->
