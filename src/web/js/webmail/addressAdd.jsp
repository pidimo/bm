<!--
/***********functions to add address rapido of form fast **********/
    function hiddenTable(){
        var cad = lib_getObj('dto(addressType)' ).value;

        if(cad==1){
            lib_getObj("tablePerson").style.display = "";  //display
            lib_getObj("tableOrg").style.display = "none";  //hidden
            lib_getObj("tableEdit").style.display = "none";  //hidden
        }else{
            lib_getObj("tablePerson").style.display = "none";  //hidden
            lib_getObj("tableOrg").style.display = "";  //display
            lib_getObj("tableEdit").style.display = "none";  //hidden
        }
        clearFields();
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

        lib_getObj('dto(isUpdate)').checked = true;
        lib_getObj('dto(EditName)').value = unescape(name);

        //addressId
        lib_getObj('dto(addressId)' ).value = addressId;

        //contactPersonId
        lib_getObj('dto(contactPersonId)' ).value = contactPersonId;

        //hidden tables
        lib_getObj('tablePerson').style.display = "none";  //hidden
        lib_getObj('tableOrg').style.display = "none";  //hidden
        lib_getObj('tableEdit').style.display = "";  //display

        //select hidden
        lib_getObj('trSelect').style.display = "none";

        //set focus
        lib_getObj('dto(email)').focus();

        searchWindow.close();
    }

    function clearFields(emailId){

        lib_getObj('dto(PerName1)' ).value ="";
        lib_getObj('dto(PerName2)' ).value ="";
        lib_getObj('dto(PerName3)' ).value ="";
        lib_getObj('dto(PerSearchName)' ).value ="";

        lib_getObj('dto(OrgName1)' ).value ="";
        lib_getObj('dto(OrgName2)' ).value ="";
        lib_getObj('dto(OrgName3)' ).value ="";
        lib_getObj('dto(OrgSearchName)' ).value ="";

        //search contact disable
        lib_getObj('dto(isUpdate)' ).checked = false;
    }
    
    function clearContactSelectPopup() {
        lib_getObj('dto(isUpdate)' ).checked = false;

        //addressId
        lib_getObj('dto(addressId)' ).value ="";

        //select view
        lib_getObj('trSelect').style.display = "";

        //hidden tables
        hiddenTable();

        //set focus
        lib_getObj('dto(addressType)').focus();
    }

//-->