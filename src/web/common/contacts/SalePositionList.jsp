<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>


<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
                <TR>
                    <td class="label">
                        <fmt:message key="Common.search"/>
                    </td>
                    <html:form action="/SalePosition/List.do"
                               focus="parameter(productName)">
                        <td class="contain">
                            <html:text
                                    property="parameter(productName)"
                                    styleClass="largeText"/>
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
                        <fanta:alphabet action="/SalePosition/List.do"
                                        parameterName="productNameAlpha"/>
                    </td>
                </tr>
            </table>
            <br/>
        </td>
    </tr>

    <tr>
        <app2:checkAccessRight functionality="SALEPOSITION" permission="CREATE">
            <html:form action="/SalePosition/Forward/Create.do">
                <td class="button">
                    <html:submit styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </td>
            </html:form>
        </app2:checkAccessRight>
    </tr>
    <tr>
        <td>
            <c:set value="SalePosition/List.do" var="listUrl" scope="request"/>
            <c:set var="useJavaScript" value="false" scope="request"/>
            <c:import url="/common/contacts/SalePositionFragmentList.jsp"/>
        </td>
    </tr>

    <tr>
        <app2:checkAccessRight functionality="SALEPOSITION" permission="CREATE">
            <html:form action="/SalePosition/Forward/Create.do">
                <td class="button">
                    <html:submit styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </td>
            </html:form>
        </app2:checkAccessRight>
    </tr>

</table>