<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<app2:jScriptUrl url="/contacts/Person/Forward/DetailView.do" var="jsViewInformationPersonUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="dto(addressId)" value="addressId"/>
</app2:jScriptUrl>
<app2:jScriptUrl url="/contacts/Organization/Forward/DetailView.do" var="jsViewInformationOrganizationUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="dto(addressId)" value="addressId"/>
</app2:jScriptUrl>
<!--
/*** common contacts javascript**/
function viewContactDetailInfo(type, addressId)
{

if(type == '0') {//Organization
    window.open(${jsViewInformationOrganizationUrl}, 'viewOrganization', 'resizable=yes,width=870,height=600,left=100, top=150, scrollbars=yes');
    }
    if (type == '1'){//Person
       window.open(${jsViewInformationPersonUrl}, 'viewPerson', 'resizable=yes,width=870,height=600,left=100,top=150,scrollbars=yes');
    }
}

//-->