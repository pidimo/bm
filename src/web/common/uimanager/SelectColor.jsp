<%@ page import="com.piramide.elwis.utils.UIManagerConstants" %>
<%@ page import="com.piramide.elwis.web.uimanager.form.StyleSheetForm" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ include file="/Includes.jsp" %>
<!-- note: this version of the color picker is optimized for IE 5.5+ only -->

<html>
<head><title><fmt:message key="ColorPicker.pageTitle"/></title>

    <%--<script type="text/javascript" src="popup.js"></script>--%>

    <script type="text/javascript">

        //window.resizeTo(240, 250);
        function _CloseOnEsc() {
            if (event.keyCode == 27) {
                window.close();
                return;
            }
        }

        function Init() {                                                       // run on page load
            //  __dlg_init();    // <!-- this can be found in popup.js -->
            document.body.onkeypress = _CloseOnEsc;

            var color = window.dialogArguments;
            color = ValidateColor(color) || '000000';
            View(color);                                                          // set default color
        }

        function View(color) {                  // preview color
            document.getElementById("ColorPreview").style.backgroundColor = '#' + color;
        }

        function Set() {                   // select color
            var string = document.getElementById("ColorHex").value;
            var color = ValidateColor(string);
            if (color == null) {
                alert("Invalid color code: " + string);
            }        // invalid color
            else {                                                                // valid color
                View(color);                          // show selected color
                color = "#" + color;
                if ('${param['function']}' != null && ('${param['function']}').length > 1) {
                    opener.${param['function']}('${param['inputKey']}', color);
                } else {
                    opener.putSelectColor('${param['inputKey']}', color);
                }
            }
        }

        function ValidateColor(string) {                // return valid color code, i.e. 'FFFFFF'
            string = string || '';
            string = string + "";
            string = string.toUpperCase();
            var chars = '0123456789ABCDEF';
            var out = '';

            for (var i = 0; i < string.length; i++) {             // remove invalid color chars
                var schar = string.charAt(i);
                if (chars.indexOf(schar) != -1) {
                    out += schar;
                }
            }

            if (out.length != 6) {
                return null;
            }            // check length
            return out;
        }

        /*----------miky--------------*/
        function GiveDec(Hex)
        {
            if (Hex == "A")
                Value = 10;
            else
                if (Hex == "B")
                    Value = 11;
                else
                    if (Hex == "C")
                        Value = 12;
                    else
                        if (Hex == "D")
                            Value = 13;
                        else
                            if (Hex == "E")
                                Value = 14;
                            else
                                if (Hex == "F")
                                    Value = 15;
                                else
                                    Value = eval(Hex);

            return Value;
        }

        function HexToDec(numHex)        //i.e. numHex = 'FFFFFF'
        {
            Input = numHex;
            setHexColor(numHex);

            Input = Input.toUpperCase();

            a = GiveDec(Input.substring(0, 1));
            b = GiveDec(Input.substring(1, 2));
            c = GiveDec(Input.substring(2, 3));
            d = GiveDec(Input.substring(3, 4));
            e = GiveDec(Input.substring(4, 5));
            f = GiveDec(Input.substring(5, 6));


            x = (a * 16) + b;
            y = (c * 16) + d;
            z = (e * 16) + f;

            //start degrade
            degradeWithIniRGB(x, y, z);

        }

        function degradeWithIniRGB(red, green, blue) {
            //SCRIPT TO CREATE DEGRADE VARIABLE
            //COPYRIGHT DESARROLLOWEB.COM
            //color that be present at first
            color_inicio = new Array(red, green, blue);
            //color the that be lying
            color_fin = new Array(255, 255, 255);
            //the steps that be utilize to transfer of an color the other
            pasos = 21;

            hexadecimal = new Array("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F");
            var color_actual = new Array(3);

            //calculate the increment in the that have make the degrade
            diferencia = new Array(3);
            for (i = 0; i < 3; i++)
                diferencia[i] = (color_fin[i] - color_inicio[i]) / pasos;

            //degrade
            for (iteracion = 0; iteracion < pasos; iteracion++) {
                for (i = 0; i < 3; i++)
                    color_actual[i] = (iteracion * diferencia[i]) + color_inicio[i];

                //alert(convierteHexadecimal(Math.round(color_actual[0])) + convierteHexadecimal(Math.round(color_actual[1])) + convierteHexadecimal(Math.round(color_actual[2])));

                var hexcolor = convierteHexadecimal(Math.round(color_actual[0])) + convierteHexadecimal(Math.round(color_actual[1])) + convierteHexadecimal(Math.round(color_actual[2]));

                document.getElementById("degrade" + iteracion).style.backgroundColor = hexcolor;
                document.getElementById("degradeHex" + iteracion).value = hexcolor;
            }
        }

        function convierteHexadecimal(num) {
            var hexaDec = Math.floor(num / 16)
            var hexaUni = num - (hexaDec * 16)
            return hexadecimal[hexaDec] + hexadecimal[hexaUni]
        }

        function setHexColor(color) {
            document.getElementById("ColorHex").value = '#' + color;
            document.getElementById("ColorSelect").style.backgroundColor = '#' + color;
        }

        function setHexDegradeColor(degradeHexId) {
            var valueColor = document.getElementById(degradeHexId).value;
            document.getElementById("ColorHex").value = '#' + valueColor;
            document.getElementById("ColorSelect").style.backgroundColor = '#' + valueColor;
        }

        function viewDegradeColor(degradeHexId) {
            var valueColor = document.getElementById(degradeHexId).value;
            document.getElementById("ColorPreview").style.backgroundColor = '#' + valueColor;
        }

        /*---------------end miky----------------*/

    </script>
</head>
<body style="background:ButtonFace; margin:0px; padding:0px" onload="Init()">

<form method="get" style="margin:0px; padding:0px"
      onSubmit="Set(document.getElementById('ColorHex').value); return false;">
    <table border="0px" cellspacing="0px" cellpadding="2" width="100%">
        <tr>
            <td style="background:buttonface" valign=center>
                <div style="background-color: #000000; padding: 1; height: 21px; width: 35px">
                    <div id="ColorPreview" style="height: 100%; width: 100%"></div>
                </div>
            </td>
            <td align="right" width="100%">
                <table border="0px" cellspacing="0px" cellpadding="2" align="right">
                    <tr>
                        <td style="background:buttonface" valign=center>
                            <input type="text" name="ColorHex" id="ColorHex" value="" maxlength=7 size=7
                                   style="font-size: 12px" readonly="true">
                        </td>
                        <td style="background:buttonface" valign=center>
                            <div style="background-color: #000000; padding: 1; height: 17px; width: 30px">
                                <div id="ColorSelect" style="height: 100%; width: 100%"></div>
                            </div>
                        </td>
                        <td style="background:buttonface">
                            <html:button property="insert" styleId="insert" style="font-size:9px; margin-top:3px;"
                                         onclick="Set();"><fmt:message key="Common.select"/></html:button>
                        </td>
                    </tr>
                </table>
            </td>
            <td style="background:buttonface" width=100%></td>
        </tr>
    </table>
</form>

<table border="0" cellspacing="1px" cellpadding="0px" width="100%" bgcolor="#000000" style="cursor: hand;">
<tr>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#003300 onMouseOver=View(
    '003300') onClick=HexToDec('003300') height="10px" width="10px"></td>
    <td bgcolor=#006600 onMouseOver=View(
    '006600') onClick=HexToDec('006600') height="10px" width="10px"></td>
    <td bgcolor=#009900 onMouseOver=View(
    '009900') onClick=HexToDec('009900') height="10px" width="10px"></td>
    <td bgcolor=#00CC00 onMouseOver=View(
    '00CC00') onClick=HexToDec('00CC00') height="10px" width="10px"></td>
    <td bgcolor=#00FF00 onMouseOver=View(
    '00FF00') onClick=HexToDec('00FF00') height="10px" width="10px"></td>
    <td bgcolor=#330000 onMouseOver=View(
    '330000') onClick=HexToDec('330000') height="10px" width="10px"></td>
    <td bgcolor=#333300 onMouseOver=View(
    '333300') onClick=HexToDec('333300') height="10px" width="10px"></td>
    <td bgcolor=#336600 onMouseOver=View(
    '336600') onClick=HexToDec('336600') height="10px" width="10px"></td>
    <td bgcolor=#339900 onMouseOver=View(
    '339900') onClick=HexToDec('339900') height="10px" width="10px"></td>
    <td bgcolor=#33CC00 onMouseOver=View(
    '33CC00') onClick=HexToDec('33CC00') height="10px" width="10px"></td>
    <td bgcolor=#33FF00 onMouseOver=View(
    '33FF00') onClick=HexToDec('33FF00') height="10px" width="10px"></td>
    <td bgcolor=#660000 onMouseOver=View(
    '660000') onClick=HexToDec('660000') height="10px" width="10px"></td>
    <td bgcolor=#663300 onMouseOver=View(
    '663300') onClick=HexToDec('663300') height="10px" width="10px"></td>
    <td bgcolor=#666600 onMouseOver=View(
    '666600') onClick=HexToDec('666600') height="10px" width="10px"></td>
    <td bgcolor=#669900 onMouseOver=View(
    '669900') onClick=HexToDec('669900') height="10px" width="10px"></td>
    <td bgcolor=#66CC00 onMouseOver=View(
    '66CC00') onClick=HexToDec('66CC00') height="10px" width="10px"></td>
    <td bgcolor=#66FF00 onMouseOver=View(
    '66FF00') onClick=HexToDec('66FF00') height="10px" width="10px"></td>
</tr>
<tr>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#333333 onMouseOver=View(
    '333333') onClick=HexToDec('333333') height="10px" width="10px"></td>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#000033 onMouseOver=View(
    '000033') onClick=HexToDec('000033') height="10px" width="10px"></td>
    <td bgcolor=#003333 onMouseOver=View(
    '003333') onClick=HexToDec('003333') height="10px" width="10px"></td>
    <td bgcolor=#006633 onMouseOver=View(
    '006633') onClick=HexToDec('006633') height="10px" width="10px"></td>
    <td bgcolor=#009933 onMouseOver=View(
    '009933') onClick=HexToDec('009933') height="10px" width="10px"></td>
    <td bgcolor=#00CC33 onMouseOver=View(
    '00CC33') onClick=HexToDec('00CC33') height="10px" width="10px"></td>
    <td bgcolor=#00FF33 onMouseOver=View(
    '00FF33') onClick=HexToDec('00FF33') height="10px" width="10px"></td>
    <td bgcolor=#330033 onMouseOver=View(
    '330033') onClick=HexToDec('330033') height="10px" width="10px"></td>
    <td bgcolor=#333333 onMouseOver=View(
    '333333') onClick=HexToDec('333333') height="10px" width="10px"></td>
    <td bgcolor=#336633 onMouseOver=View(
    '336633') onClick=HexToDec('336633') height="10px" width="10px"></td>
    <td bgcolor=#339933 onMouseOver=View(
    '339933') onClick=HexToDec('339933') height="10px" width="10px"></td>
    <td bgcolor=#33CC33 onMouseOver=View(
    '33CC33') onClick=HexToDec('33CC33') height="10px" width="10px"></td>
    <td bgcolor=#33FF33 onMouseOver=View(
    '33FF33') onClick=HexToDec('33FF33') height="10px" width="10px"></td>
    <td bgcolor=#660033 onMouseOver=View(
    '660033') onClick=HexToDec('660033') height="10px" width="10px"></td>
    <td bgcolor=#663333 onMouseOver=View(
    '663333') onClick=HexToDec('663333') height="10px" width="10px"></td>
    <td bgcolor=#666633 onMouseOver=View(
    '666633') onClick=HexToDec('666633') height="10px" width="10px"></td>
    <td bgcolor=#669933 onMouseOver=View(
    '669933') onClick=HexToDec('669933') height="10px" width="10px"></td>
    <td bgcolor=#66CC33 onMouseOver=View(
    '66CC33') onClick=HexToDec('66CC33') height="10px" width="10px"></td>
    <td bgcolor=#66FF33 onMouseOver=View(
    '66FF33') onClick=HexToDec('66FF33') height="10px" width="10px"></td>
</tr>
<tr>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#666666 onMouseOver=View(
    '666666') onClick=HexToDec('666666') height="10px" width="10px"></td>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#000066 onMouseOver=View(
    '000066') onClick=HexToDec('000066') height="10px" width="10px"></td>
    <td bgcolor=#003366 onMouseOver=View(
    '003366') onClick=HexToDec('003366') height="10px" width="10px"></td>
    <td bgcolor=#006666 onMouseOver=View(
    '006666') onClick=HexToDec('006666') height="10px" width="10px"></td>
    <td bgcolor=#009966 onMouseOver=View(
    '009966') onClick=HexToDec('009966') height="10px" width="10px"></td>
    <td bgcolor=#00CC66 onMouseOver=View(
    '00CC66') onClick=HexToDec('00CC66') height="10px" width="10px"></td>
    <td bgcolor=#00FF66 onMouseOver=View(
    '00FF66') onClick=HexToDec('00FF66') height="10px" width="10px"></td>
    <td bgcolor=#330066 onMouseOver=View(
    '330066') onClick=HexToDec('330066') height="10px" width="10px"></td>
    <td bgcolor=#333366 onMouseOver=View(
    '333366') onClick=HexToDec('333366') height="10px" width="10px"></td>
    <td bgcolor=#336666 onMouseOver=View(
    '336666') onClick=HexToDec('336666') height="10px" width="10px"></td>
    <td bgcolor=#339966 onMouseOver=View(
    '339966') onClick=HexToDec('339966') height="10px" width="10px"></td>
    <td bgcolor=#33CC66 onMouseOver=View(
    '33CC66') onClick=HexToDec('33CC66') height="10px" width="10px"></td>
    <td bgcolor=#33FF66 onMouseOver=View(
    '33FF66') onClick=HexToDec('33FF66') height="10px" width="10px"></td>
    <td bgcolor=#660066 onMouseOver=View(
    '660066') onClick=HexToDec('660066') height="10px" width="10px"></td>
    <td bgcolor=#663366 onMouseOver=View(
    '663366') onClick=HexToDec('663366') height="10px" width="10px"></td>
    <td bgcolor=#666666 onMouseOver=View(
    '666666') onClick=HexToDec('666666') height="10px" width="10px"></td>
    <td bgcolor=#669966 onMouseOver=View(
    '669966') onClick=HexToDec('669966') height="10px" width="10px"></td>
    <td bgcolor=#66CC66 onMouseOver=View(
    '66CC66') onClick=HexToDec('66CC66') height="10px" width="10px"></td>
    <td bgcolor=#66FF66 onMouseOver=View(
    '66FF66') onClick=HexToDec('66FF66') height="10px" width="10px"></td>
</tr>
<tr>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#999999 onMouseOver=View(
    '999999') onClick=HexToDec('999999') height="10px" width="10px"></td>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#000099 onMouseOver=View(
    '000099') onClick=HexToDec('000099') height="10px" width="10px"></td>
    <td bgcolor=#003399 onMouseOver=View(
    '003399') onClick=HexToDec('003399') height="10px" width="10px"></td>
    <td bgcolor=#006699 onMouseOver=View(
    '006699') onClick=HexToDec('006699') height="10px" width="10px"></td>
    <td bgcolor=#009999 onMouseOver=View(
    '009999') onClick=HexToDec('009999') height="10px" width="10px"></td>
    <td bgcolor=#00CC99 onMouseOver=View(
    '00CC99') onClick=HexToDec('00CC99') height="10px" width="10px"></td>
    <td bgcolor=#00FF99 onMouseOver=View(
    '00FF99') onClick=HexToDec('00FF99') height="10px" width="10px"></td>
    <td bgcolor=#330099 onMouseOver=View(
    '330099') onClick=HexToDec('330099') height="10px" width="10px"></td>
    <td bgcolor=#333399 onMouseOver=View(
    '333399') onClick=HexToDec('333399') height="10px" width="10px"></td>
    <td bgcolor=#336699 onMouseOver=View(
    '336699') onClick=HexToDec('336699') height="10px" width="10px"></td>
    <td bgcolor=#339999 onMouseOver=View(
    '339999') onClick=HexToDec('339999') height="10px" width="10px"></td>
    <td bgcolor=#33CC99 onMouseOver=View(
    '33CC99') onClick=HexToDec('33CC99') height="10px" width="10px"></td>
    <td bgcolor=#33FF99 onMouseOver=View(
    '33FF99') onClick=HexToDec('33FF99') height="10px" width="10px"></td>
    <td bgcolor=#660099 onMouseOver=View(
    '660099') onClick=HexToDec('660099') height="10px" width="10px"></td>
    <td bgcolor=#663399 onMouseOver=View(
    '663399') onClick=HexToDec('663399') height="10px" width="10px"></td>
    <td bgcolor=#666699 onMouseOver=View(
    '666699') onClick=HexToDec('666699') height="10px" width="10px"></td>
    <td bgcolor=#669999 onMouseOver=View(
    '669999') onClick=HexToDec('669999') height="10px" width="10px"></td>
    <td bgcolor=#66CC99 onMouseOver=View(
    '66CC99') onClick=HexToDec('66CC99') height="10px" width="10px"></td>
    <td bgcolor=#66FF99 onMouseOver=View(
    '66FF99') onClick=HexToDec('66FF99') height="10px" width="10px"></td>
</tr>
<tr>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#CCCCCC onMouseOver=View(
    'CCCCCC') onClick=HexToDec('CCCCCC') height="10px" width="10px"></td>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#0000CC onMouseOver=View(
    '0000CC') onClick=HexToDec('0000CC') height="10px" width="10px"></td>
    <td bgcolor=#0033CC onMouseOver=View(
    '0033CC') onClick=HexToDec('0033CC') height="10px" width="10px"></td>
    <td bgcolor=#0066CC onMouseOver=View(
    '0066CC') onClick=HexToDec('0066CC') height="10px" width="10px"></td>
    <td bgcolor=#0099CC onMouseOver=View(
    '0099CC') onClick=HexToDec('0099CC') height="10px" width="10px"></td>
    <td bgcolor=#00CCCC onMouseOver=View(
    '00CCCC') onClick=HexToDec('00CCCC') height="10px" width="10px"></td>
    <td bgcolor=#00FFCC onMouseOver=View(
    '00FFCC') onClick=HexToDec('00FFCC') height="10px" width="10px"></td>
    <td bgcolor=#3300CC onMouseOver=View(
    '3300CC') onClick=HexToDec('3300CC') height="10px" width="10px"></td>
    <td bgcolor=#3333CC onMouseOver=View(
    '3333CC') onClick=HexToDec('3333CC') height="10px" width="10px"></td>
    <td bgcolor=#3366CC onMouseOver=View(
    '3366CC') onClick=HexToDec('3366CC') height="10px" width="10px"></td>
    <td bgcolor=#3399CC onMouseOver=View(
    '3399CC') onClick=HexToDec('3399CC') height="10px" width="10px"></td>
    <td bgcolor=#33CCCC onMouseOver=View(
    '33CCCC') onClick=HexToDec('33CCCC') height="10px" width="10px"></td>
    <td bgcolor=#33FFCC onMouseOver=View(
    '33FFCC') onClick=HexToDec('33FFCC') height="10px" width="10px"></td>
    <td bgcolor=#6600CC onMouseOver=View(
    '6600CC') onClick=HexToDec('6600CC') height="10px" width="10px"></td>
    <td bgcolor=#6633CC onMouseOver=View(
    '6633CC') onClick=HexToDec('6633CC') height="10px" width="10px"></td>
    <td bgcolor=#6666CC onMouseOver=View(
    '6666CC') onClick=HexToDec('6666CC') height="10px" width="10px"></td>
    <td bgcolor=#6699CC onMouseOver=View(
    '6699CC') onClick=HexToDec('6699CC') height="10px" width="10px"></td>
    <td bgcolor=#66CCCC onMouseOver=View(
    '66CCCC') onClick=HexToDec('66CCCC') height="10px" width="10px"></td>
    <td bgcolor=#66FFCC onMouseOver=View(
    '66FFCC') onClick=HexToDec('66FFCC') height="10px" width="10px"></td>
</tr>
<tr>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#FFFFFF onMouseOver=View(
    'FFFFFF') onClick=HexToDec('FFFFFF') height="10px" width="10px"></td>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#0000FF onMouseOver=View(
    '0000FF') onClick=HexToDec('0000FF') height="10px" width="10px"></td>
    <td bgcolor=#0033FF onMouseOver=View(
    '0033FF') onClick=HexToDec('0033FF') height="10px" width="10px"></td>
    <td bgcolor=#0066FF onMouseOver=View(
    '0066FF') onClick=HexToDec('0066FF') height="10px" width="10px"></td>
    <td bgcolor=#0099FF onMouseOver=View(
    '0099FF') onClick=HexToDec('0099FF') height="10px" width="10px"></td>
    <td bgcolor=#00CCFF onMouseOver=View(
    '00CCFF') onClick=HexToDec('00CCFF') height="10px" width="10px"></td>
    <td bgcolor=#00FFFF onMouseOver=View(
    '00FFFF') onClick=HexToDec('00FFFF') height="10px" width="10px"></td>
    <td bgcolor=#3300FF onMouseOver=View(
    '3300FF') onClick=HexToDec('3300FF') height="10px" width="10px"></td>
    <td bgcolor=#3333FF onMouseOver=View(
    '3333FF') onClick=HexToDec('3333FF') height="10px" width="10px"></td>
    <td bgcolor=#3366FF onMouseOver=View(
    '3366FF') onClick=HexToDec('3366FF') height="10px" width="10px"></td>
    <td bgcolor=#3399FF onMouseOver=View(
    '3399FF') onClick=HexToDec('3399FF') height="10px" width="10px"></td>
    <td bgcolor=#33CCFF onMouseOver=View(
    '33CCFF') onClick=HexToDec('33CCFF') height="10px" width="10px"></td>
    <td bgcolor=#33FFFF onMouseOver=View(
    '33FFFF') onClick=HexToDec('33FFFF') height="10px" width="10px"></td>
    <td bgcolor=#6600FF onMouseOver=View(
    '6600FF') onClick=HexToDec('6600FF') height="10px" width="10px"></td>
    <td bgcolor=#6633FF onMouseOver=View(
    '6633FF') onClick=HexToDec('6633FF') height="10px" width="10px"></td>
    <td bgcolor=#6666FF onMouseOver=View(
    '6666FF') onClick=HexToDec('6666FF') height="10px" width="10px"></td>
    <td bgcolor=#6699FF onMouseOver=View(
    '6699FF') onClick=HexToDec('6699FF') height="10px" width="10px"></td>
    <td bgcolor=#66CCFF onMouseOver=View(
    '66CCFF') onClick=HexToDec('66CCFF') height="10px" width="10px"></td>
    <td bgcolor=#66FFFF onMouseOver=View(
    '66FFFF') onClick=HexToDec('66FFFF') height="10px" width="10px"></td>
</tr>
<tr>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#FF0000 onMouseOver=View(
    'FF0000') onClick=HexToDec('FF0000') height="10px" width="10px"></td>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#990000 onMouseOver=View(
    '990000') onClick=HexToDec('990000') height="10px" width="10px"></td>
    <td bgcolor=#993300 onMouseOver=View(
    '993300') onClick=HexToDec('993300') height="10px" width="10px"></td>
    <td bgcolor=#996600 onMouseOver=View(
    '996600') onClick=HexToDec('996600') height="10px" width="10px"></td>
    <td bgcolor=#999900 onMouseOver=View(
    '999900') onClick=HexToDec('999900') height="10px" width="10px"></td>
    <td bgcolor=#99CC00 onMouseOver=View(
    '99CC00') onClick=HexToDec('99CC00') height="10px" width="10px"></td>
    <td bgcolor=#99FF00 onMouseOver=View(
    '99FF00') onClick=HexToDec('99FF00') height="10px" width="10px"></td>
    <td bgcolor=#CC0000 onMouseOver=View(
    'CC0000') onClick=HexToDec('CC0000') height="10px" width="10px"></td>
    <td bgcolor=#CC3300 onMouseOver=View(
    'CC3300') onClick=HexToDec('CC3300') height="10px" width="10px"></td>
    <td bgcolor=#CC6600 onMouseOver=View(
    'CC6600') onClick=HexToDec('CC6600') height="10px" width="10px"></td>
    <td bgcolor=#CC9900 onMouseOver=View(
    'CC9900') onClick=HexToDec('CC9900') height="10px" width="10px"></td>
    <td bgcolor=#CCCC00 onMouseOver=View(
    'CCCC00') onClick=HexToDec('CCCC00') height="10px" width="10px"></td>
    <td bgcolor=#CCFF00 onMouseOver=View(
    'CCFF00') onClick=HexToDec('CCFF00') height="10px" width="10px"></td>
    <td bgcolor=#FF0000 onMouseOver=View(
    'FF0000') onClick=HexToDec('FF0000') height="10px" width="10px"></td>
    <td bgcolor=#FF3300 onMouseOver=View(
    'FF3300') onClick=HexToDec('FF3300') height="10px" width="10px"></td>
    <td bgcolor=#FF6600 onMouseOver=View(
    'FF6600') onClick=HexToDec('FF6600') height="10px" width="10px"></td>
    <td bgcolor=#FF9900 onMouseOver=View(
    'FF9900') onClick=HexToDec('FF9900') height="10px" width="10px"></td>
    <td bgcolor=#FFCC00 onMouseOver=View(
    'FFCC00') onClick=HexToDec('FFCC00') height="10px" width="10px"></td>
    <td bgcolor=#FFFF00 onMouseOver=View(
    'FFFF00') onClick=HexToDec('FFFF00') height="10px" width="10px"></td>
</tr>
<tr>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#00FF00 onMouseOver=View(
    '00FF00') onClick=HexToDec('00FF00') height="10px" width="10px"></td>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#990033 onMouseOver=View(
    '990033') onClick=HexToDec('990033') height="10px" width="10px"></td>
    <td bgcolor=#993333 onMouseOver=View(
    '993333') onClick=HexToDec('993333') height="10px" width="10px"></td>
    <td bgcolor=#996633 onMouseOver=View(
    '996633') onClick=HexToDec('996633') height="10px" width="10px"></td>
    <td bgcolor=#999933 onMouseOver=View(
    '999933') onClick=HexToDec('999933') height="10px" width="10px"></td>
    <td bgcolor=#99CC33 onMouseOver=View(
    '99CC33') onClick=HexToDec('99CC33') height="10px" width="10px"></td>
    <td bgcolor=#99FF33 onMouseOver=View(
    '99FF33') onClick=HexToDec('99FF33') height="10px" width="10px"></td>
    <td bgcolor=#CC0033 onMouseOver=View(
    'CC0033') onClick=HexToDec('CC0033') height="10px" width="10px"></td>
    <td bgcolor=#CC3333 onMouseOver=View(
    'CC3333') onClick=HexToDec('CC3333') height="10px" width="10px"></td>
    <td bgcolor=#CC6633 onMouseOver=View(
    'CC6633') onClick=HexToDec('CC6633') height="10px" width="10px"></td>
    <td bgcolor=#CC9933 onMouseOver=View(
    'CC9933') onClick=HexToDec('CC9933') height="10px" width="10px"></td>
    <td bgcolor=#CCCC33 onMouseOver=View(
    'CCCC33') onClick=HexToDec('CCCC33') height="10px" width="10px"></td>
    <td bgcolor=#CCFF33 onMouseOver=View(
    'CCFF33') onClick=HexToDec('CCFF33') height="10px" width="10px"></td>
    <td bgcolor=#FF0033 onMouseOver=View(
    'FF0033') onClick=HexToDec('FF0033') height="10px" width="10px"></td>
    <td bgcolor=#FF3333 onMouseOver=View(
    'FF3333') onClick=HexToDec('FF3333') height="10px" width="10px"></td>
    <td bgcolor=#FF6633 onMouseOver=View(
    'FF6633') onClick=HexToDec('FF6633') height="10px" width="10px"></td>
    <td bgcolor=#FF9933 onMouseOver=View(
    'FF9933') onClick=HexToDec('FF9933') height="10px" width="10px"></td>
    <td bgcolor=#FFCC33 onMouseOver=View(
    'FFCC33') onClick=HexToDec('FFCC33') height="10px" width="10px"></td>
    <td bgcolor=#FFFF33 onMouseOver=View(
    'FFFF33') onClick=HexToDec('FFFF33') height="10px" width="10px"></td>
</tr>
<tr>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#0000FF onMouseOver=View(
    '0000FF') onClick=HexToDec('0000FF') height="10px" width="10px"></td>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#990066 onMouseOver=View(
    '990066') onClick=HexToDec('990066') height="10px" width="10px"></td>
    <td bgcolor=#993366 onMouseOver=View(
    '993366') onClick=HexToDec('993366') height="10px" width="10px"></td>
    <td bgcolor=#996666 onMouseOver=View(
    '996666') onClick=HexToDec('996666') height="10px" width="10px"></td>
    <td bgcolor=#999966 onMouseOver=View(
    '999966') onClick=HexToDec('999966') height="10px" width="10px"></td>
    <td bgcolor=#99CC66 onMouseOver=View(
    '99CC66') onClick=HexToDec('99CC66') height="10px" width="10px"></td>
    <td bgcolor=#99FF66 onMouseOver=View(
    '99FF66') onClick=HexToDec('99FF66') height="10px" width="10px"></td>
    <td bgcolor=#CC0066 onMouseOver=View(
    'CC0066') onClick=HexToDec('CC0066') height="10px" width="10px"></td>
    <td bgcolor=#CC3366 onMouseOver=View(
    'CC3366') onClick=HexToDec('CC3366') height="10px" width="10px"></td>
    <td bgcolor=#CC6666 onMouseOver=View(
    'CC6666') onClick=HexToDec('CC6666') height="10px" width="10px"></td>
    <td bgcolor=#CC9966 onMouseOver=View(
    'CC9966') onClick=HexToDec('CC9966') height="10px" width="10px"></td>
    <td bgcolor=#CCCC66 onMouseOver=View(
    'CCCC66') onClick=HexToDec('CCCC66') height="10px" width="10px"></td>
    <td bgcolor=#CCFF66 onMouseOver=View(
    'CCFF66') onClick=HexToDec('CCFF66') height="10px" width="10px"></td>
    <td bgcolor=#FF0066 onMouseOver=View(
    'FF0066') onClick=HexToDec('FF0066') height="10px" width="10px"></td>
    <td bgcolor=#FF3366 onMouseOver=View(
    'FF3366') onClick=HexToDec('FF3366') height="10px" width="10px"></td>
    <td bgcolor=#FF6666 onMouseOver=View(
    'FF6666') onClick=HexToDec('FF6666') height="10px" width="10px"></td>
    <td bgcolor=#FF9966 onMouseOver=View(
    'FF9966') onClick=HexToDec('FF9966') height="10px" width="10px"></td>
    <td bgcolor=#FFCC66 onMouseOver=View(
    'FFCC66') onClick=HexToDec('FFCC66') height="10px" width="10px"></td>
    <td bgcolor=#FFFF66 onMouseOver=View(
    'FFFF66') onClick=HexToDec('FFFF66') height="10px" width="10px"></td>
</tr>
<tr>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#FFFF00 onMouseOver=View(
    'FFFF00') onClick=HexToDec('FFFF00') height="10px" width="10px"></td>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#990099 onMouseOver=View(
    '990099') onClick=HexToDec('990099') height="10px" width="10px"></td>
    <td bgcolor=#993399 onMouseOver=View(
    '993399') onClick=HexToDec('993399') height="10px" width="10px"></td>
    <td bgcolor=#996699 onMouseOver=View(
    '996699') onClick=HexToDec('996699') height="10px" width="10px"></td>
    <td bgcolor=#999999 onMouseOver=View(
    '999999') onClick=HexToDec('999999') height="10px" width="10px"></td>
    <td bgcolor=#99CC99 onMouseOver=View(
    '99CC99') onClick=HexToDec('99CC99') height="10px" width="10px"></td>
    <td bgcolor=#99FF99 onMouseOver=View(
    '99FF99') onClick=HexToDec('99FF99') height="10px" width="10px"></td>
    <td bgcolor=#CC0099 onMouseOver=View(
    'CC0099') onClick=HexToDec('CC0099') height="10px" width="10px"></td>
    <td bgcolor=#CC3399 onMouseOver=View(
    'CC3399') onClick=HexToDec('CC3399') height="10px" width="10px"></td>
    <td bgcolor=#CC6699 onMouseOver=View(
    'CC6699') onClick=HexToDec('CC6699') height="10px" width="10px"></td>
    <td bgcolor=#CC9999 onMouseOver=View(
    'CC9999') onClick=HexToDec('CC9999') height="10px" width="10px"></td>
    <td bgcolor=#CCCC99 onMouseOver=View(
    'CCCC99') onClick=HexToDec('CCCC99') height="10px" width="10px"></td>
    <td bgcolor=#CCFF99 onMouseOver=View(
    'CCFF99') onClick=HexToDec('CCFF99') height="10px" width="10px"></td>
    <td bgcolor=#FF0099 onMouseOver=View(
    'FF0099') onClick=HexToDec('FF0099') height="10px" width="10px"></td>
    <td bgcolor=#FF3399 onMouseOver=View(
    'FF3399') onClick=HexToDec('FF3399') height="10px" width="10px"></td>
    <td bgcolor=#FF6699 onMouseOver=View(
    'FF6699') onClick=HexToDec('FF6699') height="10px" width="10px"></td>
    <td bgcolor=#FF9999 onMouseOver=View(
    'FF9999') onClick=HexToDec('FF9999') height="10px" width="10px"></td>
    <td bgcolor=#FFCC99 onMouseOver=View(
    'FFCC99') onClick=HexToDec('FFCC99') height="10px" width="10px"></td>
    <td bgcolor=#FFFF99 onMouseOver=View(
    'FFFF99') onClick=HexToDec('FFFF99') height="10px" width="10px"></td>
</tr>
<tr>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#00FFFF onMouseOver=View(
    '00FFFF') onClick=HexToDec('00FFFF') height="10px" width="10px"></td>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#9900CC onMouseOver=View(
    '9900CC') onClick=HexToDec('9900CC') height="10px" width="10px"></td>
    <td bgcolor=#9933CC onMouseOver=View(
    '9933CC') onClick=HexToDec('9933CC') height="10px" width="10px"></td>
    <td bgcolor=#9966CC onMouseOver=View(
    '9966CC') onClick=HexToDec('9966CC') height="10px" width="10px"></td>
    <td bgcolor=#9999CC onMouseOver=View(
    '9999CC') onClick=HexToDec('9999CC') height="10px" width="10px"></td>
    <td bgcolor=#99CCCC onMouseOver=View(
    '99CCCC') onClick=HexToDec('99CCCC') height="10px" width="10px"></td>
    <td bgcolor=#99FFCC onMouseOver=View(
    '99FFCC') onClick=HexToDec('99FFCC') height="10px" width="10px"></td>
    <td bgcolor=#CC00CC onMouseOver=View(
    'CC00CC') onClick=HexToDec('CC00CC') height="10px" width="10px"></td>
    <td bgcolor=#CC33CC onMouseOver=View(
    'CC33CC') onClick=HexToDec('CC33CC') height="10px" width="10px"></td>
    <td bgcolor=#CC66CC onMouseOver=View(
    'CC66CC') onClick=HexToDec('CC66CC') height="10px" width="10px"></td>
    <td bgcolor=#CC99CC onMouseOver=View(
    'CC99CC') onClick=HexToDec('CC99CC') height="10px" width="10px"></td>
    <td bgcolor=#CCCCCC onMouseOver=View(
    'CCCCCC') onClick=HexToDec('CCCCCC') height="10px" width="10px"></td>
    <td bgcolor=#CCFFCC onMouseOver=View(
    'CCFFCC') onClick=HexToDec('CCFFCC') height="10px" width="10px"></td>
    <td bgcolor=#FF00CC onMouseOver=View(
    'FF00CC') onClick=HexToDec('FF00CC') height="10px" width="10px"></td>
    <td bgcolor=#FF33CC onMouseOver=View(
    'FF33CC') onClick=HexToDec('FF33CC') height="10px" width="10px"></td>
    <td bgcolor=#FF66CC onMouseOver=View(
    'FF66CC') onClick=HexToDec('FF66CC') height="10px" width="10px"></td>
    <td bgcolor=#FF99CC onMouseOver=View(
    'FF99CC') onClick=HexToDec('FF99CC') height="10px" width="10px"></td>
    <td bgcolor=#FFCCCC onMouseOver=View(
    'FFCCCC') onClick=HexToDec('FFCCCC') height="10px" width="10px"></td>
    <td bgcolor=#FFFFCC onMouseOver=View(
    'FFFFCC') onClick=HexToDec('FFFFCC') height="10px" width="10px"></td>
</tr>
<tr>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#FF00FF onMouseOver=View(
    'FF00FF') onClick=HexToDec('FF00FF') height="10px" width="10px"></td>
    <td bgcolor=#000000 onMouseOver=View(
    '000000') onClick=HexToDec('000000') height="10px" width="10px"></td>
    <td bgcolor=#9900FF onMouseOver=View(
    '9900FF') onClick=HexToDec('9900FF') height="10px" width="10px"></td>
    <td bgcolor=#9933FF onMouseOver=View(
    '9933FF') onClick=HexToDec('9933FF') height="10px" width="10px"></td>
    <td bgcolor=#9966FF onMouseOver=View(
    '9966FF') onClick=HexToDec('9966FF') height="10px" width="10px"></td>
    <td bgcolor=#9999FF onMouseOver=View(
    '9999FF') onClick=HexToDec('9999FF') height="10px" width="10px"></td>
    <td bgcolor=#99CCFF onMouseOver=View(
    '99CCFF') onClick=HexToDec('99CCFF') height="10px" width="10px"></td>
    <td bgcolor=#99FFFF onMouseOver=View(
    '99FFFF') onClick=HexToDec('99FFFF') height="10px" width="10px"></td>
    <td bgcolor=#CC00FF onMouseOver=View(
    'CC00FF') onClick=HexToDec('CC00FF') height="10px" width="10px"></td>
    <td bgcolor=#CC33FF onMouseOver=View(
    'CC33FF') onClick=HexToDec('CC33FF') height="10px" width="10px"></td>
    <td bgcolor=#CC66FF onMouseOver=View(
    'CC66FF') onClick=HexToDec('CC66FF') height="10px" width="10px"></td>
    <td bgcolor=#CC99FF onMouseOver=View(
    'CC99FF') onClick=HexToDec('CC99FF') height="10px" width="10px"></td>
    <td bgcolor=#CCCCFF onMouseOver=View(
    'CCCCFF') onClick=HexToDec('CCCCFF') height="10px" width="10px"></td>
    <td bgcolor=#CCFFFF onMouseOver=View(
    'CCFFFF') onClick=HexToDec('CCFFFF') height="10px" width="10px"></td>
    <td bgcolor=#FF00FF onMouseOver=View(
    'FF00FF') onClick=HexToDec('FF00FF') height="10px" width="10px"></td>
    <td bgcolor=#FF33FF onMouseOver=View(
    'FF33FF') onClick=HexToDec('FF33FF') height="10px" width="10px"></td>
    <td bgcolor=#FF66FF onMouseOver=View(
    'FF66FF') onClick=HexToDec('FF66FF') height="10px" width="10px"></td>
    <td bgcolor=#FF99FF onMouseOver=View(
    'FF99FF') onClick=HexToDec('FF99FF') height="10px" width="10px"></td>
    <td bgcolor=#FFCCFF onMouseOver=View(
    'FFCCFF') onClick=HexToDec('FFCCFF') height="10px" width="10px"></td>
    <td bgcolor=#FFFFFF onMouseOver=View(
    'FFFFFF') onClick=HexToDec('FFFFFF') height="10px" width="10px"></td>
</table>
<hr size="1"/>
<table border="0" cellspacing="1px" cellpadding="0px" width="100%" bgcolor="#000000" style="cursor: hand;">
    <tr>
        <c:forEach begin="0" end="20" varStatus="cont">
            <html:hidden property="degradeHexColor${cont.index}" value="" styleId="degradeHex${cont.index}"/>
            <td id="degrade${cont.index}" bgcolor=#FFFFFF onMouseOver="viewDegradeColor('degradeHex${cont.index}')"
                onClick="setHexDegradeColor('degradeHex${cont.index}')" height="10px" width="10px"></td>
        </c:forEach>
    </tr>
</table>

</body>
</html>
<script language="javascript">
    //set ini color
    HexToDec('000000');
</script>