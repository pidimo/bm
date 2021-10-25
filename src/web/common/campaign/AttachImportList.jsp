<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>
<script language="JavaScript">
    <!--
    function check()
    {
        field = document.getElementById('listc').selected;
        guia = document.getElementById('listc').guia;
        var i;

        if (guia.checked)
        {
            if (field.length != undefined) {
                for (i = 0; i < field.length; i++)
                    field[i].checked = true;
            } else
                field.checked = true;
        }
        else
        {
            if (field.length != undefined) {
                for (i = 0; i < field.length; i++)
                    field[i].checked = false;
            } else
                field.checked = false;
        }
    }

    function getAttachSelected() {
        var checkboxes = document.getElementById('listc').selected; //Array content the checkbox
        var arrayId = new Array();
        var arrayName = new Array();

        if (checkboxes != null) {
            if (checkboxes.length > 0) {
                for (var x = 0; x < checkboxes.length; x++) {
                    if (checkboxes[x].checked) {
                        var name = document.getElementById('nameId_' + checkboxes[x].value).value;
                        arrayId.push(checkboxes[x].value);
                        arrayName.push(name);
                    }
                }
            } else {
                if (checkboxes.checked) {
                    var name = document.getElementById('nameId_' + checkboxes.value).value;
                    arrayId.push(checkboxes.value);
                    arrayName.push(name);
                }
            }
        }
        opener.putSelectedAttach(arrayId, arrayName);
        window.close(); //close this window
    }

    function goClose() {
        window.close();
    }

    function testSubmit() {
        var ss = document.getElementById('isSubmit').value;
        var issubmit = 'true' == ss;
        return issubmit;
    }

    function setSubmit(issubmit) {
        document.getElementById('isSubmit').value = "" + issubmit;
        var ss = document.getElementById('isSubmit').value;
    }

    //-->
</script>
<%
    if (request.getAttribute("attachList") != null) {
        ResultList resultList = (ResultList) request.getAttribute("attachList");
        pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
    }
%>


<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td class="title" colspan="2">
            <fmt:message key="Attach.searchAttach"/>
        </td>
    </tr>
    <tr>
        <html:form action="/Attach/Import/List.do" focus="parameter(fileName)">
            <td class="label">
                <fmt:message key="Common.search"/>
            </td>
            <td class="contain" nowrap>
                <html:text property="parameter(fileName)" styleClass="largeText"/>
                &nbsp;
                <html:submit styleClass="button">
                    <fmt:message key="Common.go"/>
                </html:submit>
            </td>
        </html:form>
    </tr>
    <tr>
        <td class="alpha" align="center" colspan="2">
            <fanta:alphabet action="Attach/Import/List.do" parameterName="fileName"/>
        </td>
    </tr>
    <html:form action="/Attach/Import/List.do?campaignId=${param.campaignId}" styleId="listc"
               onsubmit="return false;">
        <tr>
            <td class="button" colspan="2">
                <input type="hidden" name="isSubmit" value="false" id="isSubmit">
                <c:if test="${size > 0}">
                    <html:button property="" styleClass="button" onclick="getAttachSelected()">
                        <fmt:message key="Common.select"/>
                    </html:button>
                </c:if>
                <html:button property="" styleClass="button" onclick="goClose()">
                    <fmt:message key="Common.cancel"/>
                </html:button>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <fanta:table action="Attach/Import/List.do" list="attachList" width="100%" id="attach"
                             withCheckBox="true"
                             withContext="false"
                             imgPath="${baselayout}">

                    <c:if test="${size >0}">
                        <fanta:checkBoxColumn name="guia" id="selected" onClick="javascript:check();"
                                              property="attachId" headerStyle="listHeader"
                                              styleClass="radio listItemCenter" width="5%"/>
                    </c:if>

                    <fanta:dataColumn name="fileName" styleClass="listItem"
                                      title="Attach.fileName" headerStyle="listHeader" width="30%"
                                      orderable="true" maxLength="25">
                    </fanta:dataColumn>
                    <fanta:dataColumn name="comment" styleClass="listItem"
                                      title="Attach.comment" headerStyle="listHeader" width="45%"
                                      orderable="true" maxLength="25">
                    </fanta:dataColumn>

                    <fanta:dataColumn name="size" styleClass="listItem"
                                      title="Attach.size" headerStyle="listHeader" width="25%"
                                      orderable="true" maxLength="25" style="text-align:right"
                                      renderData="false">
                        <c:set var="e" value="${app2:divide(attach.size,(1024))}"/>
                        <c:choose>
                            <c:when test="${e > 0}">
                                ${e}
                            </c:when>
                            <c:otherwise>
                                1
                            </c:otherwise>
                        </c:choose>
                        <fmt:message key="Webmail.mailTray.Kb"/>
                    </fanta:dataColumn>
                    <html:hidden property="name_${attach.attachId}"
                                 value="${attach.fileName}" styleId="nameId_${attach.attachId}"/>
                </fanta:table>
            </td>
        </tr>
        <tr>
            <td class="button" colspan="2">
                <input type="hidden" name="isSubmit" value="false" id="isSubmit">
                <c:if test="${size > 0}">
                    <html:button property="" styleClass="button" onclick="getAttachSelected()">
                        <fmt:message key="Common.select"/>
                    </html:button>
                </c:if>
                <html:button property="" styleClass="button" onclick="goClose()">
                    <fmt:message key="Common.cancel"/>
                </html:button>
            </td>
        </tr>
    </html:form>

</table>
