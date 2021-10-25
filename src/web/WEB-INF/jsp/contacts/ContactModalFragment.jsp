<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="/contacts/Person/Forward/DetailView.do" var="jsViewInformationPersonUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="dto(addressId)" value="addressId"/>
</app2:jScriptUrl>
<app2:jScriptUrl url="/contacts/Organization/Forward/DetailView.do" var="jsViewInformationOrganizationUrl"
                 addModuleParams="false">
    <app2:jScriptUrlParam param="dto(addressId)" value="addressId"/>
</app2:jScriptUrl>

<fmt:message var="organization" key="Organization.Title.view"/>
<fmt:message var="person" key="Person.Title.view"/>

<script type="text/javascript">
    function viewContactDetailInfo(type, addressId){
        var src=null;
        var title="";
        if(type == '0') {//Organization
            src=${jsViewInformationOrganizationUrl};
            title='${organization}';
        }
        if (type == '1'){//Person
            src=${jsViewInformationPersonUrl};
            title='${person}';
        }
        $("#windowModalContactInfo iframe").contents().find('html').html("");
        launchBootstrapPopup("windowModalContactInfo", src, 'name', true);
        setModalTitle("windowModalContactInfo",title);
    }
</script>
<tags:initBootstrapSelectPopup/>
<tags:bootstrapModalPopup
        styleId="windowModalContactInfo"
        isLargeModal="true"/>