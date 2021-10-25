<%@ include file="/Includes.jsp" %>
<script>
function check()
{
    field = document.getElementById('listMailForm').selectedMails;
    guia = document.getElementById('listMailForm').mail;
    var i;

    if (guia.checked)
    {
     for (i=0;i<field.length;i++)
       field[i].checked=true;
    }
    else
    {
        for(i=0;i<field.length;i++)
        field[i].checked=false;
    }
}


function replaceOne(text,textSearched,newString) {

  while (text.indexOf(textSearched)>-1) {
      pos= text.indexOf(textSearched);
      text = "" + (text.substring(0, pos) + newString + text.substring((pos + textSearched.length), text.length));
  }
  text = text + textSearched;
  return text;

}


function getSelectContactMail() {

    var checkboxes = document.getElementById('listMailForm').selectedMails; //Array content the checkbox
    var checkboxesGroups = document.getElementById('listMailForm').selectedMailsGroups; //Array content the checkbox of contact groups
    var mails='';
    var namePage=window.name;

    //concatenate
    if(checkboxes != null){
        if(checkboxes.length > 0){
            for (var x=0; x < checkboxes.length; x++) {
                  if (checkboxes[x].checked) {
                    mails = mails+checkboxes[x].value+',';
              }
            }
        }
        else{
              if (checkboxes.checked) {
                mails = mails+checkboxes.value+',';
              }
        }
    }
    if(checkboxesGroups != null){
        if(checkboxesGroups.length > 0){
            for (var y=0; y < checkboxesGroups.length; y++) {
              if (checkboxesGroups[y].checked) {
                mails = mails+checkboxesGroups[y].value+',';
              }
            }
        }
        else{
              if (checkboxesGroups.checked) {
                mails = mails+checkboxesGroups.value+',';
              }
       }
    }

    //remove the emails repeated of the list
    var array = mails.split(",");
    for(var y=0; y<array.length-1; y++){
      mails = replaceOne(mails , array[y]+"," , "")
    }

    opener.selectContactMail(namePage,mails);

}


</script>

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td height="20" class="title">
    <fmt:message    key="Webmail.mailContact.search"/>
    </td>
</tr>
<tr>
    <td>

    <table width="100%" border="0" align="center" cellpadding="3" cellspacing="1" class="container">
        <TR>
            <td class="label"><fmt:message   key="Common.search"/></td>
           <html:form action="/Mail/Forward/MailContactImport.do"  focus="parameter(mailContactNick)">
                <td class="contain" nowrap>
                    <html:text property="parameter(mailContactNick)" styleClass="largeText" />&nbsp;
                        <html:submit styleClass="button"><fmt:message   key="Common.go"/></html:submit>&nbsp;
                </td>
            </html:form>
        </TR>
        <tr>
          <td colspan="2" align="center" class="alpha">
              <fanta:alphabet action="${pageContext.request.contextPath}/webmail/Mail/Forward/MailContactImport.do" parameterName="mailContactNick"/>
          </td>
        </tr>
        <tr>
        <td colspan="2" valing="top" align="center">
        <br/>

        <html:form action="/Mail/Forward/MailContactImport.do" styleId="listMailForm">
            <fanta:table list="mailcontactList" width="100%" id="mailcontact" action="${pageContext.request.contextPath}/webmail/Mail/Forward/MailContactImport.do" imgPath="${baselayout}" align="center"  >

               <fanta:checkBoxColumn  styleClass="radio" name="mail" id="selectedMails" onClick="javascript:check();" property="MAILCONTACTEMAIL" headerStyle="listHeader" width="5%"  />

               <fanta:dataColumn name="MAILCONTACTNICK" styleClass="listItem" title="Webmail.mailContact.nick"  headerStyle="listHeader" width="25%" orderable="true"/>
               <fanta:dataColumn name="MAILCONTACTNAME" styleClass="listItem" title="Webmail.mailContact.name"  headerStyle="listHeader" width="30%" orderable="true"/>
               <fanta:dataColumn name="MAILCONTACTEMAIL" styleClass="listItem" title="Webmail.mailContact.email" headerStyle="listHeader" width="35%" orderable="true"/>

            </fanta:table>

            <br>

            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                  <td height="20" class="title" colspan="2" >
                  <fmt:message    key="Webmail.contactGroup.plural"/>
                  </td>
              </tr>

               <TH class='listHeader' width='5%'><fmt:message   key="Common.action"/></TH>
               <TH class='listHeader' width='95%' align='center'><fmt:message   key="Webmail.contactGroup.plural"/> </TH>

               <c:forEach var="Groups" items="${dto.listGroups}">
                    <tr  class="listRow">
                            <td class='radio'>
                                <input type="checkbox" name="selectedMailsGroups" value="${Groups.listEmails}" class="radio" id="selectedMailsGroups">
                            </td>
                            <td class='listItem'><c:out value="${Groups.groupName}"/></td>

                    </tr>
               </c:forEach>

            </table>

            <table cellSpacing=0 cellPadding=2 width="100%" border=0 align="center">
                    <TR>
                         <TD class="button">
                              <html:submit styleClass="button" onclick="getSelectContactMail();"  ><fmt:message   key="Common.add"/></html:submit>
                        </TD>
                    </TR>
            </table>

       </html:form>

    </td>
    </tr>
    </table>

    </td>
</tr>
</table>

