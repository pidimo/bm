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


<c:set var="addImgVar">
    <html:img src="${baselayout}/img/add.gif" styleClass="${addClass}" titleKey="Common.add" border="0"/>
</c:set>

<c:set var="removeImgVar">
    <html:img src="${baselayout}/img/smalldelete.gif" styleClass="${removeClass}" titleKey="Common.delete" border="0"/>
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

<c:set var="groupFieldList" value="${app2:getWebDocumentFieldsByVariableType(variableTypeConstant, pageContext.request)}"/>

<table id="parameterTable${postfixId}" width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
    <tr style="display:none;">
        <td class="contain" width="35%">
            <html:hidden property="dto(newWebParameterTemp)" styleId="idParameter${postfixId}"/>
            <html:hidden property="dto(variableTypeTemp)" value="${variableTypeConstant}" styleId="idVariableType${postfixId}"/>
            <app:text property="dto(parameterNameTemp)" styleClass="largetext" maxlength="100" tabindex="${tabIndex}"/>
        </td>
        <td class="contain" width="30%">

            <html:select property="dto(variableNameTemp)" styleClass="middleSelect" tabindex="${tabIndex}">
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
        <td class="contain" width="35%">
            <c:out value="${addImgVar}" escapeXml="false"/>
        </td>
    </tr>

    <tr>
        <td class="label" width="35%">
            <fmt:message key="WebParameter.parameter"/>
        </td>
        <td class="label" width="30%">
            <fmt:message key="WebParameter.variable"/>
        </td>
        <td class="label" width="35%">
        </td>
    </tr>

    <c:set var="wrapperObj" value="${formObject.dtoMap['webParameterWrapper']}"/>
    <c:set var="parameterDataMap" value="${wrapperObj.webParameterMap}"/>
    <c:set var="newsParameterDataMap" value="${wrapperObj.newsWebParameterMap}"/>

    <c:forEach var="item" items="${parameterDataMap[typeAsLiteral]}" varStatus="statusVar">
        <tr class="clsRowParameter">
            <td class="contain" width="35%">
                <html:hidden property="webParameterId" value="${item['webParameterId']}" styleId="A_${postfixId}_${statusVar.index}"/>
                <html:hidden property="dto(variableType_${item['webParameterId']})" value="${item['variableType']}" styleId="B_${postfixId}_${statusVar.index}"/>
                <app:text property="dto(parameterName_${item['webParameterId']})" value="${item['parameterName']}"
                          styleClass="largetext" maxlength="100" view="${isReadOnly}" tabindex="${tabIndex}"/>
            </td>
            <td class="contain" width="30%">
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
                                     styleClass="middleSelect" readonly="${isReadOnly}" tabindex="${tabIndex}">
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
            <td class="contain" width="35%">
                <c:if test="${not isReadOnly}">
                    <c:out value="${removeImgVar}" escapeXml="false"/>
                </c:if>
            </td>
        </tr>
    </c:forEach>

    <c:forEach var="item" items="${newsParameterDataMap[typeAsLiteral]}" varStatus="statusVar">
        <tr class="clsRowParameter">
            <td class="contain" width="35%">
                <c:set var="identifier" value="${item['newWebParameterKey']}"/>

                <html:hidden property="newWebParameter" value="${identifier}" styleId="X_${postfixId}_${statusVar.index}"/>
                <html:hidden property="dto(newVariableType_${identifier})" value="${item['variableType']}" styleId="Y_${postfixId}_${statusVar.index}"/>
                <app:text property="dto(newParameterName_${identifier})" value="${item['parameterName']}"
                          styleClass="largetext" maxlength="100" view="${isReadOnly}" tabindex="${tabIndex}"/>
            </td>
            <td class="contain" width="30%">
                <html:select property="dto(newVariableName_${identifier})" value="${item['variableName']}"
                             styleClass="middleSelect" readonly="${isReadOnly}" tabindex="${tabIndex}">
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
            <td class="contain" width="35%">
                <c:if test="${not isReadOnly}">
                    <c:out value="${removeImgVar}" escapeXml="false"/>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>

<script type="text/javascript">
    //put the first row if is necessary
    <c:if test="${not isReadOnly}">
        checkLastTrValues('${postfixId}');
    </c:if>

</script>

