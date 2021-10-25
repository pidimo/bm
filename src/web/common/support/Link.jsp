<%@ include file="/Includes.jsp" %>


<script>
    function openURL()
    {
        var value = document.getElementById('url').value;
        if (value != '') {
            if (value.indexOf(':') == -1) { //has no protocol symbol
                window.open('http://' + value);//adding a default protocol
            } else {
                window.open(value); //open with the protocol defined
            }
        }
    }
</script>

<!--class="containTop"-->
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >

  <tr>
      <td>
         <html:form action="${action}" focus="dto(comment)" >
         <table  border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
          <fmt:message   var="dateTimePattern" key="dateTimePattern"/>
          <html:hidden property="dto(op)" value="${op}"/>
          <html:hidden property="dto(actionHistory)" value="${actionHistory}"/>
          <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>

          <c:set var="view" value="${false}"/>
          <c:if test="${('update' == op) || ('delete' == op)}">
          <html:hidden property="dto(articleOwnerId)" />
          <html:hidden property="dto(linkId)"/>
          <html:hidden property="dto(createUserId)" />
            <c:set var="view" value="${(sessionScope.user.valueMap['userId'] == linkForm.dtoMap.createUserId) || (sessionScope.user.valueMap['userId'] == linkForm.dtoMap.articleOwnerId)}"/>
          </c:if>
          <c:if test="${'update' == op}">
                <html:hidden property="dto(version)"/>
          </c:if>
          <c:if test="${'delete' == op}">
                <html:hidden property="dto(withReferences)" value="true"/>
          </c:if>
          <c:if test="${'create' == op}">
            <html:hidden property="dto(createUserId)" value="${sessionScope.user.valueMap['userId']}"/>
          </c:if>

          <TR>
              <TD colspan="2" class="title">
                  <c:out value="${title}"/>
              </TD>
          </TR>
        <c:if test="${'create' != op}">
          <TR>
              <TD class="label" width="25%" nowrap><fmt:message  key="Link.publishBy"/></TD>
              <TD class="contain" width="75%">
              <app:text property="dto(ownerName)"  maxlength="40" view="true"/>
              </TD>
          </TR>
          <TR>
              <TD class="label" width="25%" nowrap><fmt:message  key="Link.publishDate"/></TD>
              <TD class="contain" width="75%">
              ${app2:getDateWithTimeZone(linkForm.dtoMap.createDateTime, timeZone, dateTimePattern)}
              </TD>
          </TR>
        </c:if>
          <TR>
              <TD class="label" width="25%" nowrap><fmt:message  key="Link.comment"/></TD>
              <TD class="contain" width="75%">
              <app:text property="dto(comment)" styleClass="largetext" maxlength="160" view="${'delete' == op || (!view && op != 'create')}"/>
              </TD>
          </TR>
          <TR>
              <TD class="label"><fmt:message key="Link.url"/></TD>
              <TD class="containTop" >
    <app:text property="dto(url)" styleId="url"  styleClass="largeText" maxlength="200" view="${'delete' == op || (!view && op != 'create')}"/>

<a href="javascript:openURL();" >
    <html:img align="middle" src="${baselayout}/img/link.gif" titleKey="Common.openLink" border="0"/>
</a> 
              </td>
          </tr>
          </table>
<%--CREATE, CANCEL, SAVE AND NEW buttons--%>
           <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
              <TR>
                  <TD class="button">
                  <c:if test="${view || op == 'create'}" >
                       <app2:securitySubmit property="dto(save)"  operation="${op}" functionality="ARTICLELINK" styleClass="button" tabindex="10" >
                            ${button}
                       </app2:securitySubmit>
                   </c:if>

                   <c:if test="${op == 'create'}" >
                        <app2:securitySubmit operation="${op}" functionality="ARTICLELINK" styleClass="button" tabindex="11" property="SaveAndNew">
                            <fmt:message   key="Common.saveAndNew"/>
                        </app2:securitySubmit>
                    </c:if>

                  <html:cancel styleClass="button" tabindex="12" ><fmt:message   key="Common.cancel"/></html:cancel>
                  </TD>
              </TR>
           </table>
      </html:form>
      </td>
  </tr>
</table>
