<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>
<script language="JavaScript">
    <!--
    function check() {
        field = document.getElementById('listc').selected;
        guia = document.getElementById('listc').guia;
        var i;

        if (guia.checked) {
            if (field.length != undefined) {
                for (i = 0; i < field.length; i++)
                    field[i].checked = true;
            } else
                field.checked = true;
        }
        else {
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
        parent.putSelectedAttach(arrayId, arrayName);
        parent.hideBootstrapPopup();
    }

    function goClose() {
        parent.hideBootstrapPopup();
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


<html:form action="/Attach/Import/List.do" focus="parameter(fileName)" styleClass="form-horizontal">
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelOneSearchInput()} label-left" for="fileName_id">
            <fmt:message key="Common.search"/>
        </label>

        <div class="${app2:getFormOneSearchInput()}">
            <div class="input-group">
                <html:text property="parameter(fileName)"
                           styleId="fileName_id"
                           styleClass="largeText ${app2:getFormInputClasses()}"/>
                    <span class="input-group-btn">
                        <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                    </span>
            </div>
        </div>
    </div>
</html:form>

<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet action="Attach/Import/List.do" mode="bootstrap" parameterName="fileName"/>
</div>

<html:form action="/Attach/Import/List.do?campaignId=${param.campaignId}" styleId="listc" styleClass=""
           onsubmit="return false;">

    <div class="${app2:getFormButtonWrapperClasses()}">
        <input type="hidden" name="isSubmit" value="false" id="isSubmit">
        <c:if test="${size > 0}">
            <html:button property="" styleClass="button ${app2:getFormButtonClasses()}" onclick="getAttachSelected()">
                <fmt:message key="Common.select"/>
            </html:button>
        </c:if>
        <html:button property="" styleClass="button ${app2:getFormButtonCancelClasses()}" onclick="goClose()">
            <fmt:message key="Common.cancel"/>
        </html:button>
    </div>


    <div class="table-responsive">
        <fanta:table action="Attach/Import/List.do"
                     styleClass="${app2:getFantabulousTableClases()}"
                     mode="bootstrap"
                     list="attachList"
                     width="100%"
                     id="attach"
                     withCheckBox="true"
                     withContext="false"
                     imgPath="${baselayout}">

            <c:if test="${size >0}">
                <fanta:checkBoxColumn name="guia" id="selected" onClick="javascript:check();"
                                      property="attachId" headerStyle="listHeader"
                                      styleClass="listItemCenter" width="5%"/>
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
    </div>

    <div class="${app2:getFormButtonWrapperClasses()}">
        <input type="hidden" name="isSubmit" value="false" id="isSubmit">
        <c:if test="${size > 0}">
            <html:button property="" styleClass="button ${app2:getFormButtonClasses()}" onclick="getAttachSelected()">
                <fmt:message key="Common.select"/>
            </html:button>
        </c:if>
        <html:button property="" styleClass="button ${app2:getFormButtonCancelClasses()}" onclick="goClose()">
            <fmt:message key="Common.cancel"/>
        </html:button>
    </div>

</html:form>

