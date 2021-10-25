<%@ include file="/Includes.jsp" %>
<table class="${app2:getFantabulousTableClases()}">
    <tr>
        <td>YYYY</td>
        <td>
            <fmt:message key="SequenceRule.help.currentYear"/>
        </td>
    </tr>
    <tr>
        <td>YY</td>
        <td>
            <fmt:message key="SequenceRule.help.lastTwoDigitsYear"/>
        </td>
    </tr>
    <tr>
        <td>MM</td>
        <td>
            <fmt:message key="SequenceRule.help.currentMonth"/>
        </td>
    </tr>
    <tr>
        <td>DD</td>
        <td>
            <fmt:message key="SequenceRule.help.currentDayOfMonth"/>
        </td>
    </tr>
    <tr>
        <td>NNNNNNNNNN</td>
        <td>
            <fmt:message key="SequenceRule.help.ProductContractNumber"/>
        </td>
    </tr>
    <tr>
        <td>CCCCCCCCCC</td>
        <td>
            <fmt:message key="SequenceRule.help.customerNumber"/>
        </td>
    </tr>
    <tr>
        <td>/ - _</td>
        <td>
            <fmt:message key="SequenceRule.help.separators"/>
        </td>
    </tr>
</table>
<div class="example-popup">
    <h4>
        <fmt:message key="SequenceRule.format.titleExample"/>
    </h4>

    <div>
        <fmt:message key="SequenceRule.format.example"/>
    </div>
</div>

