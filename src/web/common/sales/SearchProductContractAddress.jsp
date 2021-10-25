<%@ include file="/Includes.jsp" %>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<script language="JavaScript" type="text/javascript">
    <!--
    function select(id, name) {
        opener.selectField('fieldProductContractAddress_id', id, 'fieldProductContractAddressName_id', name);
    }
    //-->
</script>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td class="title" colspan="2">
            <fmt:message key="Contact.Title.search"/>
        </td>
    </tr>
    <TR>
        <html:form action="ProductContractAddress/List.do?salePositionId=${param.salePositionId}"
                   focus="parameter(contactSearchName)">
            <html:hidden property="parameter(salePositionId)" value="${param.salePositionId}"/>
            <td class="label">
                <fmt:message key="Common.search"/>
            </td>
            <td class="contain" nowrap="nowrap">
                <html:text property="parameter(contactSearchName)" styleClass="largeText"/>
                &nbsp;
                <html:submit styleClass="button">
                    <fmt:message key="Common.go"/>
                </html:submit>
                &nbsp;
            </td>
        </html:form>
    </TR>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet
                    action="ProductContractAddress/List.do?parameter(salePositionId)=${param.salePositionId}&salePositionId=${param.salePositionId}"
                    parameterName="name1"/>
        </td>
    </tr>
    <TR>
        <td colspan="2" align="center">
            <br/>
            <fanta:table width="100%" id="contact"
                         action="ProductContractAddress/List.do?salePositionId=${param.salePositionId}&parameter(salePositionId)=${param.salePositionId}"
                         imgPath="${baselayout}">
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <fanta:actionColumn name="" title="Common.select"
                                        useJScript="true"
                                        action="javascript:select('${contact.addressId}', '${app2:jscriptEncode(contact.addressName)}');"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        image="${baselayout}/img/import.gif"/>
                </fanta:columnGroup>
                <fanta:dataColumn name="addressName" styleClass="listItem" title="Contact.name"
                                  headerStyle="listHeader" width="50%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="countryCode" styleClass="listItem" title="Contact.countryCode"
                                  headerStyle="listHeader" width="7%" orderable="true"/>
                <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip" headerStyle="listHeader"
                                  width="7%" orderable="true"/>
                <fanta:dataColumn name="cityName" styleClass="listItem2" title="Contact.city" headerStyle="listHeader"
                                  width="20%" orderable="true"/>
            </fanta:table>
        </TD>
    </tr>
</table>