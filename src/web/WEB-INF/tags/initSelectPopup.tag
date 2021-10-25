<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ attribute name="fields" %>
<script language="JavaScript">
    <!--
    function selectPopup(url, searchName, submitOnSelect, w, h,scroll){
        autoSubmit = submitOnSelect;
        popupName = searchName;
        onSelectFunction = null;

        var winx = (screen.width)/2;
        var winy = (screen.height)/2;
        var posx = winx - w/2;
        var posy = winy - h/2;
        searchWindow=window.open(url, searchName, 'resizable=yes,width='+w+',height='+h+',status,left='+posx+',top='+posy+',scrollbars='+scroll);
        searchWindow.focus();
    }

    function selectPopupExtended(url, searchName, submitOnSelect, w, h,scroll, onSelectFunc){
        selectPopup(url, searchName, submitOnSelect, w, h,scroll);
        onSelectFunction = onSelectFunc;
    }

    function selectField(keyId, keyValue, nameId, nameValue) {
        document.getElementById(keyId).value = keyValue;
        document.getElementById(nameId).value = unescape(nameValue);
        searchWindow.close();

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

    //-->
</script>
