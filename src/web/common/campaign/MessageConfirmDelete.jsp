<%@ include file="/Includes.jsp" %>
<table align="center" width="60%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td>
<html:form action="/Campaign/AditionalCriteria.do?module=campaign&index=${param.index}&campaignId=${param.campaignId}&question=true" >
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td class="title" width="100%">
            <fmt:message key="Campaign.campaignContact.delete"/>
        </td>
    </tr>
    <tr>
        <td class="contain" width="70%" >
           <fmt:message key="Campaign.AditionalCriteria.ConfirmDeleteMessage"/>
        </td>
    </tr>                    
    </table>
    <table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
      <tr>
          <td class="button">
             <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
             <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
             <html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}"/>
              <html:hidden property="addressType"/>
              <html:hidden property="includePartner"/>
              <html:hidden property="contactType"/>
              <html:hidden property="isDouble"/>
              <html:hidden property="deletePrevius"/>
              <html:hidden property="hasEmail"/>
              <html:hidden property="hasEmailTelecomType"/>


              <app2:securitySubmit operation="delete" property="dto(delete)" functionality="CAMPAIGNCONTACTS" styleClass="button" tabindex="10" >
                <fmt:message key="Common.delete"/>
              </app2:securitySubmit>

              <c:url var="urlCancel" value="/campaign/Campaign/AditionalCriteria.do?index=${param.index-1}&campaignId=${param.campaignId}&redirect=cancel"/>
              <html:button onclick="location.href='${urlCancel}'" property="dto(cancel)" styleClass="button" tabindex="11">
                 <fmt:message key="Common.cancel"/>
              </html:button>
          </td>
      </tr>
   </table>
</html:form>
</td>
</tr>
</table>