<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
/*** Javascript functions with utilities for module campaign **/

<%@ page import="com.jatun.titus.listgenerator.Constants"%>
<% request.setAttribute("SEPARATOR", Constants.TREEPATH_SEPARATOR);%>

<!--
function showIU(opBox, iu){  //iu=fieldType

    for(var i=0; i< opBox.options.length; i++) {
          if(opBox.options[i].selected && opBox.options[i].value != "" &&       //fieldType = between - number
             opBox.options[i].value == 'BETWEEN' && iu == '1'){

                document.getElementById('betweenNumber_value').style.display = "";

                document.getElementById('number_value').style.display = "none";
                document.getElementById('select_values').style.display = "none";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('decimalNumber_value').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('operator').value = opBox.options[i].value;
                document.getElementById('fieldType').value = "1";
                document.getElementById('IU_Type').value = "5";


            }else if(opBox.options[i].selected && opBox.options[i].value != "" &&                 //fieldType = between - date
                     opBox.options[i].value == 'BETWEEN' && iu == '3'){

                document.getElementById('betweenDate_value').style.display = "";

                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('number_value').style.display = "none";
               // document.getElementById('multipleSelect_value').style.display = "none";
                document.getElementById('select_values').style.display = "none";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('decimalNumber_value').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('operator').value = opBox.options[i].value;
                document.getElementById('fieldType').value = "3";
                document.getElementById('IU_Type').value = "7";


            }else if(opBox.options[i].selected && opBox.options[i].value != "" &&
                     opBox.options[i].value == 'BETWEEN' && (iu == "2")){

                document.getElementById('betweenDecimalNumber_value').style.display = "";        //fieldType = between - decimalNumber
                document.getElementById('decimalNumber_value').style.display = "none";
                document.getElementById('select_values').style.display = "none";
               // document.getElementById('multipleSelect_value').style.display = "none";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('number_value').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('operator').value = opBox.options[i].value;
                document.getElementById('fieldType').value = "2";
                document.getElementById('IU_Type').value = "6";

            }else if(opBox.options[i].selected && opBox.options[i].value != "" && iu == '1'){       //fieldType = number

                document.getElementById('number_value').style.display = "";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('decimalNumber_value').style.display = "none";
               // document.getElementById('multipleSelect_value').style.display = "none";
                document.getElementById('select_values').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('operator').value = opBox.options[i].value;
                document.getElementById('fieldType').value = "1";
                document.getElementById('IU_Type').value = "1";


            }else if(opBox.options[i].selected && opBox.options[i].value != "" && iu == '3'){      //fieldType = date

                document.getElementById('date_value').style.display = "";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('number_value').style.display = "none";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('decimalNumber_value').style.display = "none";
               // document.getElementById('multipleSelect_value').style.display = "none";
                document.getElementById('select_values').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('operator').value = opBox.options[i].value;
                document.getElementById('fieldType').value = "3";
                document.getElementById('IU_Type').value = "3";

            }else if(opBox.options[i].selected && opBox.options[i].value != "" && iu == '4'){   //fieldType = text

                document.getElementById('text_value').style.display = "";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('number_value').style.display = "none";
               // document.getElementById('multipleSelect_value').style.display = "none";
                document.getElementById('select_values').style.display = "none";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('decimalNumber_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('operator').value = opBox.options[i].value;
                document.getElementById('fieldType').value = "4";
                document.getElementById('IU_Type').value = "4";


            }else if(opBox.options[i].selected && opBox.options[i].value != "" && (iu == "2")){    //fieldType = decimalNumber

                document.getElementById('decimalNumber_value').style.display = "";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('select_values').style.display = "none";
               // document.getElementById('multipleSelect_value').style.display = "none";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('number_value').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('operator').value = opBox.options[i].value;
                document.getElementById('fieldType').value = "2";
                document.getElementById('IU_Type').value = "2";
                

            }else if(opBox.options[i].selected && opBox.options[i].value != "" &&       //fieldType = multiple select
                     (opBox.options[i].value == 'IN' || opBox.options[i].value == 'EQUAL' ) && iu =="0"){

               // document.getElementById('multipleSelect_value').style.display = "";
                document.getElementById('select_values').style.display = "none";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('number_value').style.display = "none";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('decimalNumber_value').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";

                document.getElementById('operator').value = opBox.options[i].value;
                document.getElementById('fieldType').value = "0";
                document.getElementById('IU_Type').value = "9";

            }else  if(opBox.options[i].selected && opBox.options[i].value == "" ){

                document.getElementById('select_values').style.display = "none";
                document.getElementById('betweenDate_value').style.display = "none";
                document.getElementById('date_value').style.display = "none";
                document.getElementById('betweenNumber_value').style.display = "none";
                document.getElementById('number_value').style.display = "none";
                document.getElementById('betweenDecimalNumber_value').style.display = "none";
                document.getElementById('decimalNumber_value').style.display = "none";
                document.getElementById('text_value').style.display = "none";
                document.getElementById('product_inUse').style.display = "none";


                document.getElementById('fieldValue').value = null;
                document.getElementById('operator').value = opBox.options[i].value;
            }
       }
}

function viewEmpty(){
    myReset();
    document.getElementById('IU_Type').value = null;    
    document.getElementById('text_value').style.display = "none";
    document.getElementById('number_value').style.display = "none";
    document.getElementById('decimalNumber_value').style.display = "none";
    document.getElementById('date_value').style.display = "none";
    document.getElementById('betweenNumber_value').style.display = "none";
    document.getElementById('betweenDecimalNumber_value').style.display = "none";
    document.getElementById('betweenDate_value').style.display = "none";
    document.getElementById('select_values').style.display = "none";
    document.getElementById('text_value').style.display = "none";
    document.getElementById('product_inUse').style.display = "none";    

   /* document.getElementById('search_list_city').style.display = "none";*/
    document.getElementById('search_list_partner').style.display = "none";
    document.getElementById('contact_type').style.display = "none";
    /*document.getElementById('search_list_employee').style.display = "none";*/
    document.getElementById('product_name').style.display = "none";
    document.getElementById('readOnly_value').style.display = "none";
}

    function myReset() {

        var form = document.campaignCriterionForm;
        for (i = 0; i < form.elements.length; i++) {

            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            }
        }
    }

//-->