<%@ include file="/Includes.jsp" %>

<script>
    function select(id, name) {
        opener.selectField('field_supplierId', id, 'field_supplierNameId', name);
    }
</script>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="2">
            <fmt:message key="Supplier.Title.search"/>
        </td>
    </tr>
    <tr>
        <td class="label"><fmt:message key="Common.search"/></td>
        <html:form action="/${action}" focus="parameter(contactSearchName)">
            <td class="contain" nowrap>
                <html:text property="parameter(contactSearchName)" styleClass="largeText"/>&nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
            </td>
        </html:form>
    </tr>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="${action}" parameterName="name1"/>
        </td>
    </tr>

    <tr>
        <td colspan="2" align="center">
            <br>
            <fanta:table width="100%" id="supplier" action="${action}" imgPath="${baselayout}">
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                    <fanta:actionColumn name="" title="Common.select"
                                        useJScript="true"
                                        action="javascript:select('${supplier.supplierId}', '${app2:jscriptEncode(supplier.supplierName)}');"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        image="${baselayout}/img/import.gif"/>
                </fanta:columnGroup>
                <fanta:dataColumn name="supplierName" styleClass="listItem" title="Contact.name"
                                  headerStyle="listHeader" width="40%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="countryCode" styleClass="listItem" title="Contact.countryCode"
                                  headerStyle="listHeader" width="15%" orderable="true"/>
                <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip" headerStyle="listHeader"
                                  width="15%" orderable="true"/>
                <fanta:dataColumn name="cityName" styleClass="listItem2" title="Contact.city" headerStyle="listHeader"
                                  width="20%" orderable="true"/>
            </fanta:table>
        </td>
    </tr>
</table>

