<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<script>
    function check() {
        field = document.getElementById('favoriteForm').checkbox;
        guia = document.getElementById('favoriteForm').checkAll;

        var i;

        if (guia.checked) {
            for (i = 0; i < field.length; i++)
                field[i].checked = true;
        }
        else {
            for (i = 0; i < field.length; i++)
                field[i].checked = false;
        }
    }

</script>


<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>

<html:form action="${action}" styleId="favoriteForm" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <html:hidden property="dto(op)" value="save"/>
                <html:hidden property="dto(sizeOfFavorites)" value="${dto.sizeOfFavorites}"/>
                <html:hidden property="dto(companyId)"/>
                <html:hidden property="dto(userId)"/>
                <html:hidden property="dto(addressType)"/>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
            </fieldset>
            <div>
                <div class="table-responsive">
                    <table class="${app2:getFantabulousTableClases()}">
                        <tr>
                            <th width="20%" class="listHeader">
                                <div class="checkbox checkbox-default">
                                    <input type=checkbox name="checkAll" id="checkAll_id" value="1"
                                           onclick="javascript:check();"
                                           title="<fmt:message key="Common.selectAll"/>">
                                    <label for="checkAll_id"></label>
                                </div>
                            </th>
                            <th width="80%" class="listHeader">
                                <fmt:message key="Favorite.name"/>
                            </th>
                        </tr>
                        <c:forEach var="favorites" items="${dto.favoriteList}" varStatus="index" begin="0"
                                   end="${dto.sizeOfFavorites}">
                            <tr class="listRow">
                                <td class="listItemCenter">
                                    <div class="checkbox checkbox-default">
                                        <input type="checkbox" name="dto(item<c:out value="${index.count}"/>)"
                                               value="<c:out value="${favorites.addressId}"/>" id="checkbox">
                                        <html:hidden property="dto(codes${index.count})"
                                                     value="${favorites.addressId}"/>
                                        <label for="checkbox"></label>
                                    </div>
                                </td>
                                <c:if test="${favorites.addressType == personType}">
                                    <c:choose>
                                        <c:when test="${favorites.name3 == 'null'}">
                                            <td class="listItem">
                                                <html:link
                                                        action="/Person/Forward/Update.do?contactId=${favorites.addressId}&index=0&amp;dto(addressId)=${favorites.addressId}">
                                                    <c:out value="${favorites.name1}"/>, <c:out
                                                        value="${favorites.name2}"/>,
                                                    <c:out
                                                            value="${favorites.name3}"/>
                                                </html:link>
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td class="listItem">
                                                <html:link
                                                        action="/Person/Forward/Update.do?contactId=${favorites.addressId}&index=0&amp;dto(addressId)=${favorites.addressId}">
                                                    <c:out value="${favorites.name1}"/><c:if
                                                        test="${favorites.name2 != ''}"><c:out
                                                        value=", ${favorites.name2}"/></c:if>
                                                </html:link>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <c:if test="${favorites.addressType == organizationType}">
                                    <td class="listItem">
                                        <html:link
                                                action="/Organization/Forward/Update.do?contactId=${favorites.addressId}&index=0&amp;dto(addressId)=${favorites.addressId}">
                                            <c:out value="${favorites.name1}"/> <c:out value="${favorites.name2}"/>
                                            <c:out
                                                    value="${favorites.name3}"/>
                                        </html:link>
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:choose>
                <c:when test="${dto.sizeOfFavorites != 0}">
                    <app2:checkAccessRight functionality="FAVORITE" permission="UPDATE">
                        <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.delete"/>
                        </html:submit>
                    </app2:checkAccessRight>
                    <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}">
                        <fmt:message key="Common.cancel"/>
                    </html:cancel>
                </c:when>
                <c:otherwise>
                    <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}">
                        <fmt:message key="Common.cancel"/>
                    </html:cancel>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</html:form>