<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ attribute name="fields" %>
<script language="JavaScript">
    <!--

    function launchBootstrapPopup(styleId, url, searchName, submitOnSelect) {
        modalId = styleId;
        autoSubmit = submitOnSelect;
        popupName = searchName;
        onSelectFunction = null;

        showPreloader("#" + modalId + " iframe");
        $("#" + modalId + " iframe").attr({
            'src': url
        });

        //show popup
        $("#" + modalId).modal({
            backdrop: 'static'
        });
    }

    function launchBootstrapPopupExtended(styleId, url, searchName, submitOnSelect, onShowJSFunction, onSelectFunc){
        launchBootstrapPopup(styleId,url,searchName,submitOnSelect);
        onSelectFunction = onSelectFunc;

        if(onShowJSFunction != null && onShowJSFunction != 'null') {
            eval(onShowJSFunction);
        }
    }

    function setModalTitle(styleId,title){
        $("#"+styleId+"Label").text(title);
    }

    function selectField(keyId, keyValue, nameId, nameValue) {
        document.getElementById(keyId).value = keyValue;
        $("#"+keyId).change();
        $("#"+keyId).valid();
        document.getElementById(nameId).value = unescape(nameValue);

        //close popup
        hideBootstrapPopup();

        //execute onSelect function
        if(onSelectFunction != null && onSelectFunction != 'null'){
            eval(onSelectFunction);
        }

        if(autoSubmit == 'true'){
            addPopupSubmitHidden(popupName);
            document.forms[0].submit();
        }
    }

    function clearSelectPopup(keyFieldId, nameFieldId, submitOnClear, clearName) {
        document.getElementById(keyFieldId).value = '';
        $("#"+keyFieldId).valid();
        document.getElementById(nameFieldId).value = '';
        if (submitOnClear == 'true') {
            addPopupSubmitHidden(clearName);
            document.forms[0].submit();
        }
    }

    function clearSelectPopupExtended(keyFieldId, nameFieldId, submitOnClear, clearName, onClearFunction) {
        document.getElementById(keyFieldId).value = '';
        document.getElementById(nameFieldId).value = '';

        if(onClearFunction != null && onClearFunction != 'null'){
            eval(onClearFunction);
        }

        if (submitOnClear == 'true') {
            addPopupSubmitHidden(clearName);
            document.forms[0].submit();
        }
    }

    function addPopupSubmitHidden(searchPopupName) {
        $('form:first').append("<input type='hidden' name='submitPopupName' value=\""+searchPopupName+"\"/>");
    }

    function hideBootstrapPopup() {
        //hide modal dialog
        $("#" + modalId).modal('hide');
    }
    function recalculateModalHeigth(height,div){
        $("#"+div).css({height: height +50+ "px"})
    }

    function showPreloader(modalIdIframe){
        $(modalIdIframe).contents().find('body').html(getModalPreloader());
    }
    function getModalPreloader(){
        return '<style type="text/css" rel="stylesheet"> ' +
                    '#loader-wrapper { ' +
                        'position: fixed;' +
                        'top: 0;' +
                        'left: 0;' +
                        'width: 100%;' +
                        'height: 100%;' +
                        'z-index: 1000;' +
                    '}' +
                    '#loader {' +
                        'display: block;' +
                        'position: relative;' +
                        'left: 50%;' +
                        'top: 50%;' +
                        'width: 130px;' +
                        'height: 130px;' +
                        'margin: -75px 0 0 -75px;' +
                        'border-radius: 50%;' +
                        'border: 3px solid  transparent;' +
                        'border-top-color: #b0bec5; ' +
                        '-webkit-animation: spin 2s linear infinite; /* Chrome, Opera 15+, Safari 5+ */' +
                        'animation: spin 2s linear infinite; /* Chrome, Firefox 16+, IE 10+, Opera */' +
                    '}' +
                    '#loader:before {' +
                        'content: "";' +
                        'position: absolute;' +
                        'top: 5px;' +
                        'left: 5px;' +
                        'right: 5px;' +
                        'bottom: 5px;' +
                        'border-radius: 50%;' +
                        'border: 3px solid transparent;' +
                        'border-top-color: #78909c; ' +
                        '-webkit-animation: spin 3s linear infinite; /* Chrome, Opera 15+, Safari 5+ */' +
                        'animation: spin 3s linear infinite; /* Chrome, Firefox 16+, IE 10+, Opera */' +
                    '}' +
                    '#loader:after {' +
                        'content: "";' +
                        'position: absolute;' +
                        'top: 15px;' +
                        'left: 15px;' +
                        'right: 15px;' +
                        'bottom: 15px;' +
                        'border-radius: 50%;' +
                        'border: 3px solid transparent;' +
                        'border-top-color: #546e7a; ' +
                        '-webkit-animation: spin 1.1s linear infinite; /* Chrome, Opera 15+, Safari 5+ */' +
                        'animation: spin 1.1s linear infinite; /* Chrome, Firefox 16+, IE 10+, Opera */' +
                    '}' +
                    '@-webkit-keyframes spin {' +
                        '0%   { ' +
                            '-webkit-transform: rotate(0deg);  /* Chrome, Opera 15+, Safari 3.1+ */ ' +
                            '-ms-transform: rotate(0deg);  /* IE 9 */' +
                            'transform: rotate(0deg);  /* Firefox 16+, IE 10+, Opera */' +
                        '}' +
                        '100% {' +
                            '-webkit-transform: rotate(360deg);  /* Chrome, Opera 15+, Safari 3.1+ */ ' +
                            '-ms-transform: rotate(360deg);  /* IE 9 */' +
                            'transform: rotate(360deg);  /* Firefox 16+, IE 10+, Opera */' +
                        '}' +
                    '}' +
                    '@keyframes spin {' +
                        '0%   { ' +
                            '-webkit-transform: rotate(0deg);  /* Chrome, Opera 15+, Safari 3.1+ */ ' +
                            '-ms-transform: rotate(0deg);  /* IE 9 */' +
                            'transform: rotate(0deg);  /* Firefox 16+, IE 10+, Opera */' +
                        '}' +
                        '100% { ' +
                            '-webkit-transform: rotate(360deg);  /* Chrome, Opera 15+, Safari 3.1+ */ ' +
                            '-ms-transform: rotate(360deg);  /* IE 9 */' +
                            'transform: rotate(360deg);  /* Firefox 16+, IE 10+, Opera */' +
                        '}' +
                    '} ' +
                '</style>' +
                '<div id="loader-wrapper">' +
                    '<div id="loader"></div>' +
                '</div>';
    }
    //-->
</script>
