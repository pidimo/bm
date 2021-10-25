<%@ include file="/Includes.jsp" %>
${app2:setDataImportValuesFromAjaxRequest(pageContext, pageContext.request)}
<div id="column_${dtoKey}">
    <html:hidden property="dto(columnNameSelected_${dtoKey})" value="${columnName}"/>
    <html:hidden property="dto(groupNameSelected_${dtoKey})" value="${groupName}"/>
    <html:hidden property="dto(positioinSelected_${dtoKey})" value="${position}"/>
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
            <td class="label" nowrap="true" width="70%" >
              ${app2:filterForHtml(groupName)}: ${app2:filterForHtml(columnName)}
            </td>
            <td class="contain" nowrap="true" width="30%">
                <app:numberText property="dto(dtoKey_${dtoKey})"
                                styleClass="zipText"
                                maxlength="4"
                                numberType="integer"
                                tabindex="7"/>
                <html:button property="close"
                             onclick="javascript:removeColumn('${dtoKey}');"
                             styleClass="removeButton"
                             titleKey="Common.remove">
                    &nbsp;
                </html:button>
            </td>
        </tr>
    </table>
</div>
