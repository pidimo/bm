<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

<app2:jScriptUrl url="/contacts/PopUpDownload.jsp?dto(type)=comm" var="jsDownloadPopupUpdateUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="dto(id)" value="id"/>
    <app2:jScriptUrlParam param="dto(idType)" value="idType"/>
    <app2:jScriptUrlParam param="dto(version)" value="version"/>
    <app2:jScriptUrlParam param="dto(update)" value="update"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/contacts/PopUpDownload.jsp?dto(type)=comm" var="jsDownloadPopupCreateUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="dto(id)" value="id"/>
    <app2:jScriptUrlParam param="dto(idType)" value="idType"/>
    <app2:jScriptUrlParam param="dto(version)" value="version"/>
</app2:jScriptUrl>
<!--
/*** Document download JavaScript functions **/

function downloadPopupUpdate(update, id, idType, version){

        searchWindow=window.open(${jsDownloadPopupUpdateUrl}, 'searchCity', 'resizable=no,width=450,height=150,left=170,top=150,status');
}

function downloadPopupCreate( id, idType, version){
        searchWindow=window.open(${jsDownloadPopupCreateUrl}, 'searchCity', 'resizable=no,width=450,height=150,left=170,top=150,status');
}

//-->