<%@ page import="com.piramide.elwis.utils.RoutePageField" %>
<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <div class="table-responsive">
        <table width="100%"
               border="0"
               align="center"
               cellpadding="0"
               cellspacing="0"
               class="table">
            <%--${app2:getFantabulousTableClases()}--%>
            <thead>
            <tr>
                <th width="20%">
                    <%= RoutePageField.FIELD_LOCALE %>
                </th>
                <th height="80%">
                    <fmt:message key="Company.routePage.field_locale"/>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>
                    <%= RoutePageField.FIELD_SOURCE_COUNTRY %>
                </td>
                <td>
                    <fmt:message key="Company.routePage.field_scountry"/>
                </td>
            </tr>
            <tr>
                <td>
                    <%= RoutePageField.FIELD_SOURCE_ZIP %>
                </td>
                <td>
                    <fmt:message key="Company.routePage.field_szip"/>
                </td>
            </tr>
            <tr>
                <td>
                    <%= RoutePageField.FIELD_SOURCE_CITY %>
                </td>
                <td>
                    <fmt:message key="Company.routePage.field_scity"/>
                </td>
            </tr>
            <tr>
                <td>
                    <%= RoutePageField.FIELD_SOURCE_STREET%>
                </td>
                <td>
                    <fmt:message key="Company.routePage.field_sstreet"/>
                </td>
            </tr>
            <tr>
                <td>
                    <%= RoutePageField.FIELD_SOURCE_HOUSE_NUMBER%>
                </td>
                <td>
                    <fmt:message key="Company.routePage.field_shousenr"/>
                </td>
            </tr>
            <tr>
                <td>
                    <%= RoutePageField.FIELD_DESTINATION_COUNTRY %>
                </td>
                <td>
                    <fmt:message key="Company.routePage.field_dcountry"/>
                </td>
            </tr>
            <tr>
                <td>
                    <%= RoutePageField.FIELD_DESTINATION_ZIP %>
                </td>
                <td>
                    <fmt:message key="Company.routePage.field_dzip"/>
                </td>
            </tr>
            <tr>
                <td>
                    <%= RoutePageField.FIELD_DESTINATION_CITY %>
                </td>
                <td>
                    <fmt:message key="Company.routePage.field_dcity"/>
                </td>
            </tr>
            <tr>
                <td>
                    <%= RoutePageField.FIELD_DESTINATION_STREET%>
                </td>
                <td>
                    <fmt:message key="Company.routePage.field_dstreet"/>
                </td>
            </tr>
            <tr>
                <td>
                    <%= RoutePageField.FIELD_DESTINATION_HOUSE_NUMBER%>
                </td>
                <td>
                    <fmt:message key="Company.routePage.field_dhousenr"/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

</div>