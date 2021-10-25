<%@ tag import="com.piramide.elwis.cmd.utils.VariableConstants" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<%@ attribute name="formObject" required="true" type="com.piramide.elwis.web.catalogmanager.form.WebDocumentForm" %>
<%@ attribute name="variableTypeConstant" required="true" %>
<%@ attribute name="tabIndex" required="false" %>


<c:set var="variableTypeConstant" value="${variableTypeConstant}" scope="request"/>
<c:set var="typeAsLiteral" value='<%=VariableConstants.VariableType.findLiteral((String) request.getAttribute("variableTypeConstant"))%>'/>

<c:set var="isReadOnly" value="${'delete' == op}"/>
<c:set var="postfixId" value="${variableTypeConstant}"/>

<c:set var="addClass" value="clsAdd${postfixId}"/>
<c:set var="removeClass" value="clsRemove${postfixId}"/>

<fmt:message var="titleAdd" key="Common.add"/>
<fmt:message var="titleRemove" key="Common.delete"/>

<c:set var="addImgVar">
    <span class=" ${addClass} iconAdd fa fa-lg fa-plus  btn-link" title="${titleAdd}"></span>
</c:set>

<c:set var="removeImgVar">
    <span class="${removeClass} iconRemove glyphicon glyphicon-trash  btn-link" title="${titleRemove}"></span>
</c:set>


<script type="text/javascript">


    $(document).ready(function() {

        $(document).on("click",".${addClass}",function(){
            var trParent = findParentTr($(this));
            putRemoveImg(trParent);
            putParameterRowInTable('${postfixId}');
        });

        $(document).on("click",".${removeClass}",function(){
            var trParent = findParentTr($(this));
            $(trParent).remove();
        });

    });

    function findParentTr(element) {
        var tdParent = $(element).parents().get(0);
        var trParent = $(tdParent).parents().get(0);
        return trParent;
    }

    function elementInnerHtml(elm, data) {
        //alert("inner data:" + data);
        $(elm).html(data);
    }

    function findTdAddImage(trElm) {
        return $(trElm).children().get(2);
    }

    function putAddImg(trElm) {
        var tdImg = findTdAddImage(trElm);
        elementInnerHtml(tdImg, '${addImgVar}');
    }

    function putRemoveImg(trElm) {
        var tdImg = findTdAddImage(trElm);
        elementInnerHtml(tdImg, '${removeImgVar}');
    }

    function putParameterRowInTable(postfix) {

        var tableId = "parameterTable" + postfix;

        var cloneTr = $("#" + tableId + " tr:eq(0)").clone().removeAttr("style");
        var newIdentifier = createNewParameterIdentifier();

        //change names of temp row
        $(cloneTr).find("input[name|='dto(newWebParameterTemp)']").each(function() {
            $(this).attr('name', 'newWebParameter');
            $(this).val(newIdentifier);
            $(this).removeAttr("id");
        });

        $(cloneTr).find("input[name|='dto(variableTypeTemp)']").each(function() {
            $(this).attr('name', 'dto(newVariableType_' + newIdentifier + ')');
            $(this).removeAttr("id");
        });

        $(cloneTr).find("input[name|='dto(parameterNameTemp)']").each(function() {
            $(this).attr('name', 'dto(newParameterName_' + newIdentifier + ')');
        });

        $(cloneTr).find("select[name|='dto(variableNameTemp)']").each(function() {
            $(this).attr('name', 'dto(newVariableName_' + newIdentifier + ')');
        });

        //add tr to end
        $(cloneTr).appendTo("#"+tableId);
    }

    function createNewParameterIdentifier() {
        var d = new Date();
        var millis = d.getTime();
        return millis;
    }

    function checkLastTrValues(postfix) {
        var tableId = "parameterTable" + postfix;
        var lastTr = $("#" + tableId + " tr:last");

        if($(lastTr).hasClass( "clsRowParameter" )) {
            putAddImg(lastTr);
        } else {
            putParameterRowInTable(postfix);
        }
    }

</script>
<style type="text/css">
    .iconRemove, .iconAdd{
        margin-top: 8px;
    }
</style>
<c:set var="groupFieldList" value="${app2:getWebDocumentFieldsByVariableType(variableTypeConstant, pageContext.request)}"/>
<c:set var="classTable" value="${app2:getTableClasesIntoForm()}"/>
<div>
    <div class="table-responsive">
        <table id="parameterTable${postfixId}" width="100%" class="${classTable}">
            <tr style="display:none;">
                <td width="45%">
                    <html:hidden property="dto(newWebParameterTemp)" styleId="idParameter${postfixId}"/>
                    <html:hidden property="dto(variableTypeTemp)" value="${variableTypeConstant}" styleId="idVariableType${postfixId}"/>
                    <app:text property="dto(parameterNameTemp)" styleClass="largetext ${app2:getFormInputClasses()}" maxlength="100" tabindex="${tabIndex}"/>
                </td>
                <td width="45%">
                    <html:select property="dto(variableNameTemp)" styleClass="middleSelect ${app2:getFormSelectClasses()}" tabindex="${tabIndex}">
                        <html:option value="">&nbsp;</html:option>
                        <c:forEach var="groupMap" items="${groupFieldList}">
                            <c:choose>
                                <c:when test="${not empty groupMap['groupTitle']}">
                                    <optgroup label="${groupMap['groupTitle']}">
                                        <c:forEach var="item" items="${groupMap['variableFieldList']}">
                                            <html:option value="${item.value}">${item.label}</html:option>
                                        </c:forEach>
                                    </optgroup>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item" items="${groupMap['variableFieldList']}">
                                        <html:option value="${item.value}">${item.label}</html:option>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </html:select>
                </td>
                <td  width="10%" class="text-center">
                    <c:out value="${addImgVar}" escapeXml="false"/>
                </td>
            </tr>

            <tr>
                <th width="45%">
                    <fmt:message key="WebParameter.parameter"/>
                </th>
                <th width="45%">
                    <fmt:message key="WebParameter.variable"/>
                </th>
                <th width="10%" class="text-center">
                </th>
            </tr>

            <c:set var="wrapperObj" value="${formObject.dtoMap['webParameterWrapper']}"/>
            <c:set var="parameterDataMap" value="${wrapperObj.webParameterMap}"/>
            <c:set var="newsParameterDataMap" value="${wrapperObj.newsWebParameterMap}"/>

            <c:forEach var="item" items="${parameterDataMap[typeAsLiteral]}" varStatus="statusVar">
                <tr class="clsRowParameter">
                    <td width="45%">
                        <html:hidden property="webParameterId" value="${item['webParameterId']}" styleId="A_${postfixId}_${statusVar.index}"/>
                        <html:hidden property="dto(variableType_${item['webParameterId']})" value="${item['variableType']}" styleId="B_${postfixId}_${statusVar.index}"/>
                        <app:text property="dto(parameterName_${item['webParameterId']})" value="${item['parameterName']}"
                                  styleClass="largetextv ${app2:getFormInputClasses()}" maxlength="100" view="${isReadOnly}" tabindex="${tabIndex}"/>
                    </td>
                    <td width="45%">
                        <c:choose>
                            <c:when test="${isReadOnly}">
                                <c:forEach var="groupMap" items="${groupFieldList}">
                                    <c:forEach var="fieldItem" items="${groupMap['variableFieldList']}">

                                        <c:if test="${fieldItem.value eq item['variableName']}">
                                            <c:out value="${fieldItem.label}"/>
                                        </c:if>
                                    </c:forEach>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>

                                <html:select property="dto(variableName_${item['webParameterId']})" value="${item['variableName']}"
                                             styleClass="middleSelect ${app2:getFormSelectClasses()}" readonly="${isReadOnly}" tabindex="${tabIndex}">
                                    <html:option value="">&nbsp;</html:option>

                                    <c:forEach var="groupMap" items="${groupFieldList}">
                                        <c:choose>
                                            <c:when test="${not empty groupMap['groupTitle']}">
                                                <optgroup label="${groupMap['groupTitle']}">
                                                    <c:forEach var="item" items="${groupMap['variableFieldList']}">
                                                        <html:option value="${item.value}">${item.label}</html:option>
                                                    </c:forEach>
                                                </optgroup>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="item" items="${groupMap['variableFieldList']}">
                                                    <html:option value="${item.value}">${item.label}</html:option>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </html:select>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td width="10%">
                        <c:if test="${not isReadOnly}">
                            <c:out value="${removeImgVar}" escapeXml="false"/>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>

            <c:forEach var="item" items="${newsParameterDataMap[typeAsLiteral]}" varStatus="statusVar">
                <tr class="clsRowParameter">
                    <td width="45%">
                        <c:set var="identifier" value="${item['newWebParameterKey']}"/>

                        <html:hidden property="newWebParameter" value="${identifier}" styleId="X_${postfixId}_${statusVar.index}"/>
                        <html:hidden property="dto(newVariableType_${identifier})" value="${item['variableType']}" styleId="Y_${postfixId}_${statusVar.index}"/>
                        <app:text property="dto(newParameterName_${identifier})" value="${item['parameterName']}"
                                  styleClass="largetext ${app2:getFormInputClasses()}" maxlength="100" view="${isReadOnly}" tabindex="${tabIndex}"/>
                    </td>
                    <td width="45%">
                        <html:select property="dto(newVariableName_${identifier})" value="${item['variableName']}"
                                     styleClass="middleSelect ${app2:getFormSelectClasses()}" readonly="${isReadOnly}" tabindex="${tabIndex}">
                            <html:option value="">&nbsp;</html:option>

                            <c:forEach var="groupMap" items="${groupFieldList}">
                                <c:choose>
                                    <c:when test="${not empty groupMap['groupTitle']}">
                                        <optgroup label="${groupMap['groupTitle']}">
                                            <c:forEach var="item" items="${groupMap['variableFieldList']}">
                                                <html:option value="${item.value}">${item.label}</html:option>
                                            </c:forEach>
                                        </optgroup>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="item" items="${groupMap['variableFieldList']}">
                                            <html:option value="${item.value}">${item.label}</html:option>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </html:select>
                    </td>
                    <td width="10%" class="text-center">
                        <c:if test="${not isReadOnly}">
                            <c:out value="${removeImgVar}" escapeXml="false"/>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

<script type="text/javascript">
    //put the first row if is necessary
    <c:if test="${not isReadOnly}">
        checkLastTrValues('${postfixId}');
    </c:if>

</script>

