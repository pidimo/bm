<%@ page import="com.piramide.elwis.web.dashboard.component.configuration.reader.Builder" %>
<%@ page import="com.piramide.elwis.web.dashboard.component.configuration.structure.Component" %>
<%@ include file="/Includes.jsp" %>

<script language="JavaScript">
function moveUpList(listField) {
    if (listField.length == -1) {  // If the list is empty

    } else {
        var selected = listField.selectedIndex;
        if (selected == -1) {

        } else {  // Something is selected
            if (listField.length == 0) {  // If there's only one in the list

            } else {  // There's more than one in the list, rearrange the list order
                if (selected == 0) {

                } else {
                    // Get the text/value of the one directly above the hightlighted entry as
                    // well as the highlighted entry; then flip them
                    var moveText1 = listField[selected - 1].text;
                    var moveText2 = listField[selected].text;
                    var moveValue1 = listField[selected - 1].value;
                    var moveValue2 = listField[selected].value;
                    listField[selected].text = moveText1;
                    listField[selected].value = moveValue1;
                    listField[selected - 1].text = moveText2;
                    listField[selected - 1].value = moveValue2;
                    listField.selectedIndex = selected - 1;
                    // Select the one that was selected before
                }
                // Ends the check for selecting one which can be moved
            }
            // Ends the check for there only being one in the list to begin with
        }
        // Ends the check for there being something selected
    }
    // Ends the check for there being none in the list
}

function moveDownList(listField) {
    if (listField.length == -1) {  // If the list is empty

    } else {
        var selected = listField.selectedIndex;
        if (selected == -1) {

        } else {  // Something is selected
            if (listField.length == 0) {  // If there's only one in the list

            } else {  // There's more than one in the list, rearrange the list order
                if (selected == listField.length - 1) {

                } else {
                    // Get the text/value of the one directly below the hightlighted entry as
                    // well as the highlighted entry; then flip them
                    var moveText1 = listField[selected + 1].text;
                    var moveText2 = listField[selected].text;
                    var moveValue1 = listField[selected + 1].value;
                    var moveValue2 = listField[selected].value;
                    listField[selected].text = moveText1;
                    listField[selected].value = moveValue1;
                    listField[selected + 1].text = moveText2;
                    listField[selected + 1].value = moveValue2;
                    listField.selectedIndex = selected + 1;
                    // Select the one that was selected before
                }
                // Ends the check for selecting one which can be moved
            }
            // Ends the check for there only being one in the list to begin with
        }
        // Ends the check for there being something selected
    }
    // Ends the check for there being none in the list
}


function selectOption(sourceId, targetId) {
    var sourceBox = document.getElementById(sourceId);
    var targetBox = document.getElementById(targetId);

    var selected_index = sourceBox.selectedIndex;
    if (selected_index != -1) {
        var opt = new Option();
        opt.value = sourceBox.options[selected_index].value;
        opt.text = sourceBox.options[selected_index].text;
        targetBox.options[targetBox.options.length] = opt;
        sourceBox.remove(selected_index);
    }
}

function selectAll() {
    var leftBox = document.getElementById("selectedComponentsLeft");
    var rightBox = document.getElementById("selectedComponentsRight");
    var availableComponentsBox = document.getElementById("availableComponents");
    for (var i = 0; i < leftBox.options.length; i++) {
        leftBox.options[i].selected = true;
    }

    for (var i = 0; i < rightBox.options.length; i++) {
        rightBox.options[i].selected = true;
    }

    for (var i = 0; i < availableComponentsBox.options.length; i++) {
        availableComponentsBox.options[i].selected = true;
    }
    document.forms[0].submit();
}
</script>

<html:form action="/Dashboard/Container/Save.do?dashboardContainerId=${param.dashboardContainerId}">
<table align="center" border="0" cellpadding="0" cellspacing="0" width="70%" class="contaner">
<tr>
    <td colspan="3" class="button">
        <app2:checkAccessRight functionality="DASHBOARD" permission="UPDATE">
            <html:button property="s2" onclick="javascript:selectAll();" styleClass="button">
                <fmt:message key="Common.save"/>
            </html:button>
        </app2:checkAccessRight>
        <html:cancel styleClass="button">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
<tr>
<td>
    <table>
        <tr>
            <td>
                <html:button property="upLeft"
                             onclick="javascript:moveUpList(this.form.left)"
                             styleClass="adminUpArrow"
                             titleKey="Common.up">
                    &nbsp;
                </html:button>
                <br/>
                <html:button property="upLeft"
                             onclick="javascript:moveDownList(this.form.left)"
                             styleClass="adminDownArrow"
                             titleKey="Common.down">
                    &nbsp;
                </html:button>
            </td>
            <td>
                <html:select property="left" multiple="true" size="10" styleId="selectedComponentsLeft"
                             styleClass="multipleSelect">
                    <c:forEach var="component" items="${left}">
                        <c:set var="xmlCIdf" value="${component.xmlComponentId}" scope="request"/>

                        <%
                            Integer id = (Integer) request.getAttribute("xmlCIdf");
                            Component c = Builder.i.findComponentById(id.intValue());

                            String p = c.getPermission();
                            String f = c.getFunctionality();

                            request.setAttribute("functionality", f);
                            request.setAttribute("permission", p);
                        %>
                        <app2:checkAccessRight functionality="${functionality}" permission="${permission}">
                            <html:option value="${component.xmlComponentId}">
                                <fmt:message key="${component.nameResource}"/>
                            </html:option>
                        </app2:checkAccessRight>
                    </c:forEach>
                </html:select>
            </td>
        </tr>
    </table>
</td>
<td>
    <table>
        <tr>
            <td align="center">
                <html:button property="select"
                             onclick="javascript:selectOption('availableComponents','selectedComponentsLeft');"
                             styleClass="adminLeftArrow" titleKey="Common.add">
                    &nbsp;
                </html:button>
                <br/>
                <html:button property="select"
                             onclick="javascript:selectOption('selectedComponentsLeft','availableComponents');"
                             styleClass="adminRighttArrow" titleKey="Common.delete">
                    &nbsp;
                </html:button>
            </td>
            <td>
                <html:select property="availableComponents" multiple="true" size="10"
                             styleId="availableComponents" styleClass="multipleSelect">
                    <c:forEach var="component" items="${availableComponents}">
                        <c:set var="xmlCId" value="${component.xmlComponentId}" scope="request"/>

                        <%
                            Integer id = (Integer) request.getAttribute("xmlCId");

                            Component c = Builder.i.findComponentById(id.intValue());
                            String p = c.getPermission();
                            String f = c.getFunctionality();
                            request.setAttribute("functionality", f);
                            request.setAttribute("permission", p);
                        %>
                        <app2:checkAccessRight functionality="${functionality}" permission="${permission}">
                            <html:option value="${component.xmlComponentId}">
                                <fmt:message key="${component.nameResource}"/>
                            </html:option>
                        </app2:checkAccessRight>
                    </c:forEach>
                </html:select>
            </td>
            <td>
                <html:button property="select"
                             onclick="javascript:selectOption('availableComponents','selectedComponentsRight');"
                             styleClass="adminRighttArrow" titleKey="Common.add">&nbsp;
                </html:button>
                <br/>
                <html:button property="select"
                             onclick="javascript:selectOption('selectedComponentsRight','availableComponents');"
                             styleClass="adminLeftArrow" titleKey="Common.delete">
                    &nbsp;
                </html:button>
            </td>
        </tr>
    </table>
</td>
<td>
    <table>
        <tr>
            <td>
                <html:select property="right" multiple="true" size="10" styleId="selectedComponentsRight"
                             styleClass="multipleSelect">
                    <c:forEach var="component" items="${right}">
                        <c:set var="xmlCIdr" value="${component.xmlComponentId}" scope="request"/>

                        <%
                            Integer id = (Integer) request.getAttribute("xmlCIdr");

                            Component c = Builder.i.findComponentById(id.intValue());
                            String p = c.getPermission();
                            String f = c.getFunctionality();
                            request.setAttribute("functionality", f);
                            request.setAttribute("permission", p);
                        %>
                        <app2:checkAccessRight functionality="${functionality}" permission="${permission}">
                            <html:option value="${component.xmlComponentId}">
                                <fmt:message key="${component.nameResource}"/>
                            </html:option>
                        </app2:checkAccessRight>
                    </c:forEach>
                </html:select>
            </td>
            <td>
                <html:button property="upRight"
                             onclick="javascript:moveUpList(this.form.right)"
                             styleClass="adminUpArrow"
                             titleKey="Common.up">
                    &nbsp;
                </html:button>
                <br/>
                <html:button property="upRight"
                             onclick="javascript:moveDownList(this.form.right)"
                             styleClass="adminDownArrow"
                             titleKey="Common.down">
                    &nbsp;
                </html:button>
            </td>
        </tr>
    </table>

</td>
</tr>
<tr>
    <td colspan="3" class="button">
        <app2:checkAccessRight functionality="DASHBOARD" permission="UPDATE">
            <html:button property="s2" onclick="javascript:selectAll();" styleClass="button">
                <fmt:message key="Common.save"/>
            </html:button>
        </app2:checkAccessRight>
        <html:cancel styleClass="button">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
</table>
</html:form>