<script language="JavaScript">
<!--
function selectColorPopup(url, searchName, w, h,scroll){

    searchWindow=window.open(url, searchName, 'resizable=no, dialog=yes, modal=yes, minimizable=no, width='+w+',height='+h+',status,left=370,top=150,scrollbars='+scroll);
    searchWindow.focus();
}

function putSelectColor( inputId, inputValue) {

    document.getElementById(inputId).value = inputValue;
    document.getElementById('previewColor_'+ inputId).style.backgroundColor=inputValue;
    document.getElementById('previewColor_'+ inputId).style.display = "";

    searchWindow.close();
}

function updatePreviewColor(inputId){
    
    var colorValue = document.getElementById(inputId).value;
    if((colorValue.length == 4 || colorValue.length == 7) && isHexadecimalColor(colorValue)){
        document.getElementById('previewColor_'+ inputId).style.backgroundColor = colorValue;
        document.getElementById('previewColor_'+ inputId).style.display = "";
    }
}

function keyUpPreviewColor(inputId){
    var elText = document.getElementById(inputId);
    var colorValue = elText.value;
    var isHexColor = false;

    if(colorValue.length > 0){
        var lastChar = colorValue.substring(colorValue.length - 1, colorValue.length);
        //set # if not have
        if(colorValue.length == 1 && lastChar != "#"){
            colorValue = "#" + colorValue;
            elText.value = colorValue;
        }
        if(colorValue.length > 1){
            if(!isHexadecimalColor(lastChar)){
                colorValue = colorValue.substring(0, colorValue.length - 1);
                elText.value = colorValue;
            }
        }
        //verif if is valid
        if(colorValue.length == 4 || colorValue.length == 7)
            isHexColor = isHexadecimalColor(colorValue);
        else
            isHexColor = false;
    }

    if(isHexColor){
        document.getElementById('previewColor_'+ inputId).style.backgroundColor = colorValue;
        document.getElementById('previewColor_'+ inputId).style.display = "";
    }
}

function isHexadecimalColor(strColor){
    var isHex = true;
    var hexadecimal = new Array("0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F");
    strColor = strColor.toUpperCase();
    var arrayStr = strColor.split("");
    var k = 0;

    if(arrayStr.length > 1){
        k = 1;
        if(arrayStr[0] != "#")
            isHex = false;
    }
    if(isHex){
        for(var i = k; i < arrayStr.length ; i++){
            var isValidChar = false;
            for(var j in hexadecimal){
                var hexChar = hexadecimal[j];
                if(hexChar == arrayStr[i]){
                    isValidChar = true;
                    break;
                }
            }
            if(!isValidChar){
                return false;
            }
        }
    }
    return isHex;
}
//-->
</script>
