<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0">
    <tr>
        <td>

            <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
                <TR>

                    <html:form action="${create}">
                        <TD colspan="6" class="button">
                            <app2:securitySubmit operation="create" functionality="CASETYPE" styleClass="button"
                                                 tabindex="10">
                                <fmt:message key="Common.new"/>
                            </app2:securitySubmit>
                        </TD>
                    </html:form>
                </TR>
            </table>

            <TABLE border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
                <TR align="center">
                    <td>
                        <fanta:table list="caseTypeList" width="100%" id="caseType" action="${action}"
                                     imgPath="${baselayout}">
                            <c:set var="editAction"
                                   value="${edit}?dto(caseTypeName)=${app2:encode(caseType.name)}&dto(caseTypeId)=${caseType.id}&dto(langTextId)=${caseType.langTextId}"/>
                            <c:set var="deleteAction"
                                   value="${delete}?dto(withReferences)=true&dto(caseTypeId)=${caseType.id}&dto(caseTypeName)=${app2:encode(caseType.name)}"/>

                            <c:set var="translateAction" value="Support/CaseType/Translate.do?dto(caseTypeId)=${caseType.id}&dto(caseTypeName)=${app2:encode(caseType.name)}&dto(op)=read"/>

                            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                                <fanta:actionColumn name="trans" title="Common.translate" action="${translateAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/tournicoti.gif"/>

                                <app2:checkAccessRight functionality="CASETYPE" permission="VIEW">
                                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/edit.gif"/>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="CASETYPE" permission="DELETE">
                                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/delete.gif"/>
                                </app2:checkAccessRight>
                            </fanta:columnGroup>
                            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem2"
                                              title="CaseType.Name" headerStyle="listHeader" width="95%"
                                              orderable="true" maxLength="30"/>
                        </fanta:table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
