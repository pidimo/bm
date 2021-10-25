<%@ page import="com.piramide.elwis.utils.RoutePageField"%>
<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
<td>
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td class="button">
                <html:button  property="" onclick="window.close();" styleClass="button" ><fmt:message    key="Common.close"/></html:button>
            </td>
        </tr>
        <tr>
            <td height="20" class="title">
                <fmt:message   key="Company.routePage.allowedFields"/>
            </td>
        </tr>
        <tr>
            <td>
                <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
                    <TR>
                        <td class="label" width="20%" ><%= RoutePageField.FIELD_LOCALE %></td>
                        <td class="contain" height="80%" >
                            <fmt:message   key="Company.routePage.field_locale"/>
                        </td>
                    </TR>
                    <TR>
                        <td class="label"><%= RoutePageField.FIELD_SOURCE_COUNTRY %></td>
                        <td class="contain">
                            <fmt:message   key="Company.routePage.field_scountry"/>
                        </td>
                    </TR>
                    <TR>
                        <td class="label"><%= RoutePageField.FIELD_SOURCE_ZIP %></td>
                        <td class="contain">
                            <fmt:message   key="Company.routePage.field_szip"/>
                        </td>
                    </TR>
                    <TR>
                        <td class="label"><%= RoutePageField.FIELD_SOURCE_CITY %></td>
                        <td class="contain">
                            <fmt:message   key="Company.routePage.field_scity"/>
                        </td>
                    </TR>
                    <TR>
                        <td class="label"><%= RoutePageField.FIELD_SOURCE_STREET%></td>
                        <td class="contain">
                            <fmt:message   key="Company.routePage.field_sstreet"/>
                        </td>
                    </TR>
                    <TR>
                        <td class="label"><%= RoutePageField.FIELD_SOURCE_HOUSE_NUMBER%></td>
                        <td class="contain">
                            <fmt:message   key="Company.routePage.field_shousenr"/>
                        </td>
                    </TR>
                    <TR>
                        <td class="label"><%= RoutePageField.FIELD_DESTINATION_COUNTRY %></td>
                        <td class="contain">
                            <fmt:message   key="Company.routePage.field_dcountry"/>
                        </td>
                    </TR>
                    <TR>
                        <td class="label"><%= RoutePageField.FIELD_DESTINATION_ZIP %></td>
                        <td class="contain">
                            <fmt:message   key="Company.routePage.field_dzip"/>
                        </td>
                    </TR>
                    <TR>
                        <td class="label"><%= RoutePageField.FIELD_DESTINATION_CITY %></td>
                        <td class="contain">
                            <fmt:message   key="Company.routePage.field_dcity"/>
                        </td>
                    </TR>
                    <TR>
                        <td class="label"><%= RoutePageField.FIELD_DESTINATION_STREET%></td>
                        <td class="contain">
                            <fmt:message   key="Company.routePage.field_dstreet"/>
                        </td>
                    </TR>
                    <TR>
                        <td class="label"><%= RoutePageField.FIELD_DESTINATION_HOUSE_NUMBER%></td>
                        <td class="contain">
                            <fmt:message   key="Company.routePage.field_dhousenr"/>
                        </td>
                    </TR>
                </table>
            </td>
        </tr>
    </table>
</TABLE>
