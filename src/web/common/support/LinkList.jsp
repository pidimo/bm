<%@ include file="/Includes.jsp" %>
<script>
    function openURL(value) {
        if (value != '') {
            if (value.indexOf(':') == -1) { //has no protocol symbol
                window.open('http://' + value);//adding a default protocol
            } else {
                window.open(value);//open with the protocol defined
            }
        }
    }
</script>

<table border="0" cellpadding="0" cellspacing="0" width="85%" class="container" align="center">
    <app2:checkAccessRight functionality="ARTICLELINK" permission="CREATE">
        <tr>
            <html:form action="/Article/Link/Forward/Create.do?dto(articleTitle)=${app2:encode(param['dto(articleTitle)'])}">
                <TD class="button">
                    <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
                </TD>
            </html:form>
        </tr>
    </app2:checkAccessRight>
    <tr>
        <td>
            <fmt:message var="dateTimePattern" key="dateTimePattern"/>
<fanta:table width="100%" id="link" action="Article/LinkList.do" imgPath="${baselayout}">
<c:set var="deleteAction"
       value="Article/Link/Forward/Delete.do?dto(linkId)=${link.id}&dto(comment)=${app2:encode(link.comment)}&dto(articleTitle)=${app2:encode(param['dto(articleTitle)'])}&index=4&articleId=${param.articleId}"/>
<c:set var="editAction"
       value="Article/Link/Forward/Update.do?dto(comment)=${app2:encode(link.comment)}&dto(linkId)=${link.id}&dto(articleTitle)=${app2:encode(param['dto(articleTitle)'])}"/>

<fanta:columnGroup title="Common.action" headerStyle="listHeader">
    <app2:checkAccessRight functionality="ARTICLELINK" permission="VIEW">
        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                            headerStyle="listHeader" image="${baselayout}/img/edit.gif"/>
    </app2:checkAccessRight>

    <fanta:actionColumn name="delete" title="Common.delete" styleClass="listItem"
                        headerStyle="listHeader" width="50%">
        <app2:checkAccessRight functionality="ARTICLELINK" permission="DELETE">
            <c:choose>
                <c:when test="${link.createUserId == sessionScope.user.valueMap['userId'] || link.articleOwnerId == sessionScope.user.valueMap['userId']}">
                    <html:link action="${deleteAction}" titleKey="Common.delete">
                        <html:img src="${baselayout}/img/delete.gif" titleKey="Common.delete"
                                  border="0"/>
                    </html:link>
                </c:when>
                <c:otherwise>
                    &nbsp;
                </c:otherwise>
            </c:choose>
        </app2:checkAccessRight>
    </fanta:actionColumn>
        <fanta:actionColumn name="url" title="Common.openLink" styleClass="listItem"
            headerStyle="listHeader" width="50%">

<a href="javascript:openURL('${link.url}')">
    <html:img  align="middle" src="${baselayout}/img/link.gif"  titleKey="Common.openLink" border="0"/>
</a>
            
         </fanta:actionColumn>
</fanta:columnGroup>

<fanta:dataColumn name="comment" action="${editAction}" styleClass="listItem" title="Link.comment"
                  headerStyle="listHeader" width="30%" orderable="true" maxLength="35"/>
<fanta:dataColumn name="url" styleClass="listItem" title="Link.url" maxLength="30"
                  headerStyle="listHeader" width="25%" orderable="true">
</fanta:dataColumn>
<fanta:dataColumn name="creationDate" styleClass="listItem${expireClass}" title="Link.publishDate"
                  headerStyle="listHeader" width="18%" orderable="true" renderData="false">
    ${app2:getDateWithTimeZone(link.creationDate, timeZone, dateTimePattern)}
</fanta:dataColumn>
<fanta:dataColumn name="ownerName" styleClass="listItem2" title="Link.publishBy"
                  headerStyle="listHeader" width="22%" orderable="true"/>
</fanta:table>
        </td>
    </tr>
</table>
