<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<table id="tableId" width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <app2:checkAccessRight functionality="ADDITIONALADDRESS" permission="CREATE">
                <app:url value="AddressRelation/Forward/Create.do" var="url"/>
                <html:submit property="new" styleClass="button" onclick="window.parent.location='${url}'">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </app2:checkAccessRight>
        </td>
    </tr>
    <tr>
        <td class="title">
            <fmt:message key="AddressRelation.plural"/>
        </td>
    </tr>
    <tr>
        <td>
            <fanta:table list="addressRelationList"
                         width="100%"
                         id="relation"
                         action="AddressRelation/List.do"
                         imgPath="${baselayout}"
                         align="center">

                <app:url var="editLink"
                       value="AddressRelation/Forward/Update.do?dto(relationId)=${relation.relationId}&dto(relatedAddressName)=${app2:encode(relation.relatedAddressName)}&dto(op)=read"/>
                <app:url var="deleteLink"
                       value="AddressRelation/Forward/Delete.do?dto(relationId)=${relation.relationId}&dto(relatedAddressName)=${app2:encode(relation.relatedAddressName)}&dto(op)=read&dto(withReferences)=true"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="ADDITIONALADDRESS" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update"
                                            useJScript="true"
                                            action="javascript:goParentURL('${editLink}')"
                                            styleClass="listItem"
                                            headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="ADDITIONALADDRESS" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete"
                                            useJScript="true"
                                            action="javascript:goParentURL('${deleteLink}')"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>

                <fanta:dataColumn name="relatedAddressName"
                                  useJScript="true"
                                  action="javascript:goParentURL('${editLink}')"
                                  title="AddressRelation.relatedAddress"
                                  styleClass="listItem"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  width="65%"/>
                <fanta:dataColumn name="title"
                                  title="AddressRelation.relationType"
                                  styleClass="listItem"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  width="30%"/>
            </fanta:table>
        </td>
    </tr>
</table>

<script>
    addAppPageEvent(window, 'load', incrementTableInIframe);
</script>
