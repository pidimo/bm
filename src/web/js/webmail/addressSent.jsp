
<!--
    /********* functions to add conctacts of form fast********/

    function hiddenTable(emailId){
        var cad = lib_getObj('dto(addressType_' + emailId +')' ).value;

        if(cad==1){ 
            //is person
            lib_getObj("tablePerson_" + emailId ).style.display = "";  //display
            lib_getObj("tableOrg_" + emailId ).style.display = "none";  //hidden
            lib_getObj("edit_" + emailId ).style.display = "none";  //hidden
            lib_getObj("trTelecomType_" + emailId ).style.display = "";  //display
        }else if(cad==0){ 
            //is organization
            lib_getObj("tablePerson_" + emailId ).style.display = "none";  //hidden
            lib_getObj("tableOrg_" + emailId ).style.display = "";  //display
            lib_getObj("edit_" + emailId ).style.display = "none";  //hidden
            lib_getObj("trTelecomType_" + emailId ).style.display = "";  //display
        }else {
            //is blank
            lib_getObj("tablePerson_" + emailId ).style.display = "none";  //hidden
            lib_getObj("tableOrg_" + emailId ).style.display = "none";  //hidden
            lib_getObj("edit_" + emailId ).style.display = "none";  //hidden
            lib_getObj("trTelecomType_" + emailId ).style.display = "none";  //hidden
        }
        clearFields(emailId);
    }

    function lib_getObj(id,d)
    {
    	var i,x;  if(!d) d=document;
    	if(!(x=d[id])&&d.all) x=d.all[id];
    	for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][id];
    	for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=lib_getObj(id,d.layers[i].document);
    	if(!x && document.getElementById) x=document.getElementById(id);
    	return x;
    };

    function putAddress(addressId,contactPersonId,emailId,name){

        lib_getObj('dto(isUpdate_' + emailId + ')').checked = true;
        lib_getObj('dto(EditName_' + emailId + ')').value = unescape(name);

        //addressId
        lib_getObj('dto(addressId_' + emailId + ')' ).value = addressId;

        //contactPersonId
        lib_getObj('dto(contactPersonId_' + emailId + ')' ).value = contactPersonId;

        //hidden tables
        lib_getObj('tablePerson_' + emailId ).style.display = "none";  //hidden
        lib_getObj('tableOrg_' + emailId ).style.display = "none";  //hidden
        lib_getObj('edit_' + emailId ).style.display = "";  //display

        //select hidden
        lib_getObj('trSelect_' + emailId ).style.display = "none";

        //set focus
        lib_getObj('dto(telecomTypeId_'+ emailId + ')' ).focus();

        searchWindow.close();
    }

    function clearFields(emailId){

        lib_getObj('dto(PerName1_' + emailId + ')' ).value ="";
        lib_getObj('dto(PerName2_' + emailId + ')' ).value ="";
        lib_getObj('dto(PerName3_' + emailId + ')' ).value ="";
        lib_getObj('dto(PerSearchName_' + emailId + ')' ).value ="";

        lib_getObj('dto(OrgName1_' + emailId + ')' ).value ="";
        lib_getObj('dto(OrgName2_' + emailId + ')' ).value ="";
        lib_getObj('dto(OrgName3_' + emailId + ')' ).value ="";
        lib_getObj('dto(OrgSearchName_' + emailId + ')' ).value ="";

        //search contact disable
        lib_getObj('dto(isUpdate_' + emailId + ')' ).checked = false;

    }
    
    function clearContactSelectPopup(emailId) {
        lib_getObj('dto(isUpdate_' + emailId + ')' ).checked = false;

        //addressId
        lib_getObj('dto(addressId_' + emailId + ')' ).value ="";

        //select view
        lib_getObj('trSelect_' + emailId ).style.display = "";

        //hidden tables
        hiddenTable( emailId );

        //set focus
        lib_getObj('dto(addressType_'+ emailId + ')' ).focus();
    }

//-->