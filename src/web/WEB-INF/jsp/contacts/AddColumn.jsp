<%@ include file="/Includes.jsp" %>
${app2:setDataImportValuesFromAjaxRequest(pageContext, pageContext.request)}
<div id="column_${dtoKey}" class="addMarginRow">
    <html:hidden property="dto(columnNameSelected_${dtoKey})" value="${columnName}"/>
    <html:hidden property="dto(groupNameSelected_${dtoKey})" value="${groupName}"/>
    <html:hidden property="dto(positioinSelected_${dtoKey})" value="${position}"/>
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tbody>
        <tr>
            <td nowrap="true" width="60%">
                <label class="control-label">
                    ${app2:filterForHtml(groupName)}: ${app2:filterForHtml(columnName)}
                </label>
            </td>
            <td class="contain" nowrap="true" width="40%">
                <div class="input-group">
                    <app:numberText property="dto(dtoKey_${dtoKey})"
                                    styleClass="${app2:getFormInputClasses()} zipText"
                                    maxlength="4"
                                    numberType="integer"
                                    tabindex="7"/>
                    <span class="input-group-btn">
                        <html:button property="close"
                                     onclick="javascript:removeColumn('${dtoKey}');"
                                     styleClass="${app2:getFormButtonClasses()} removeButton"
                                     titleKey="Common.remove">x
                        </html:button>
                    </span>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
