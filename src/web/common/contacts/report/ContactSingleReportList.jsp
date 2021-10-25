<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<script language="JavaScript" type="text/javascript">
    function changeCommunicationsRowConfigState(checkBoxElement){
        if(checkBoxElement.checked){
            document.getElementById("communicationsNumberTF").style.display="";
        }
        else{
            document.getElementById("communicationsNumberTF").style.display="none";
        }
    }
    function thisFormReset(id) {
        var form = document.getElementById(id);
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text" ) {
                form.elements[i].value = "";
            }
            else if(form.elements[i].type == "select-one"){
                if(form.elements[i].id != 'format' && form.elements[i].id != 'pageSize'){
                    form.elements[i].options.length=0;
                }
            }
            else if (form.elements[i].type == "checkbox") {
                form.elements[i].checked = false;
            }
            else if(form.elements[i].type == "hidden" && (form.elements[i].id).substring(0,6) != "report"){
                form.elements[i].value = "";
            }
        }
        document.getElementById("communicationsNumberTF").style.display="none";
    }
</script>

<table border="0" cellpadding="0" cellspacing="0" width="97%" class="container" align="center">
    <tr>
        <td height="20" class="title" colspan="4"><fmt:message key="Contact.Report.ContactSingleList"/></td>
    </tr>

    <html:form action="/Report/ContactSingleList/Execute.do" focus="parameter(addressId_Selected)"
               styleId="contactSingleList">
        <tr>
            <TD class="label" width="15%"><fmt:message key="Contact"/></TD>
            <TD class="contain" width="35%">

                    <%-- Used when coming from the contact or contact person detail. See Contact shortcuts which passes the params --%>
                <c:if test="${not empty contactSingleReportListForm.params['addressId_Selected'] && empty contactSingleReportListForm.params['address']}">
                    <c:set target="${contactSingleReportListForm.params}" property="address"
                           value="${app2:getAddressName(contactSingleReportListForm.params['addressId_Selected'])}"/>
                </c:if>


                <html:hidden property="parameter(addressId_Selected)" styleId="fieldAddressId_id"/>
                <app:text property="parameter(address)" styleId="fieldAddressName_id" styleClass="middleText"
                          maxlength="40" tabindex="1" readonly="true"/>
                <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"
                                  hide="false" submitOnSelect="true" tabindex="2"/>
                <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                       titleKey="Common.clear" submitOnClear="true" tabindex="2"/>
            </TD>
            <td class="label"><fmt:message key="Contact.Report.showCommunications"/></td>
            <td class="contain"><html:checkbox property="parameter(showCommunications)" value="true" onclick="changeCommunicationsRowConfigState(this)" tabindex="6"/> </td>
        </TR>
        <c:if test="${not empty contactSingleReportListForm.params['addressId_Selected']}">
            <tr>
                <td class="label"><fmt:message key="ProductCustomer.contactPerson"/></td>
                <td class="contain" colspan="3">

                    <fanta:select property="parameter(contactPersonId_selected)" tabIndex="4"
                                  listName="searchContactPersonList"
                                  module="/contacts" firstEmpty="true" labelProperty="contactPersonName"
                                  valueProperty="contactPersonId" styleClass="mediumSelect"
                                  readOnly="${op == 'delete'}">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        <fanta:parameter field="addressId"
                                         value="${contactSingleReportListForm.params['addressId_Selected']}"/>
                    </fanta:select>
                </td>
            </tr>

        </c:if>
        <c:choose>
            <c:when test="${contactSingleReportListForm.params['showCommunications']}">
                <tr id="communicationsNumberTF">
            </c:when>
            <c:otherwise>
                <tr style="display:none;" id="communicationsNumberTF">
            </c:otherwise>
        </c:choose>
            <td class="label" >
                <fmt:message key="ContactSingleReport.numberOfCommunications"/>
            </td>
            <td class="contain" colspan="3">
                <app:numberText property="parameter(communicationsLimit)" maxInt="10" maxlength="10" numberType="integer" styleClass="numberText" tabindex="5"/>
            </td>
        </tr>
        <c:set var="reportFormats" value="${contactSingleReportListForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${contactSingleReportListForm.pageSizes}" scope="request"/>

        <tags:reportOptionsTag/>

        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" property="parameter(generate)" tabindex="57"><fmt:message
                        key="Campaign.Report.generate"/></html:submit>
                <html:button property="reset1" tabindex="58" styleClass="button"
                             onclick="thisFormReset('contactSingleList')">
                    <fmt:message
                            key="Common.clear"/></html:button>
            </td>
        </tr>

        <titus:reportInitializeConstantsTag/>

        <titus:reportTag id="constactSingleList" title="Contact.Report.ContactSingleList"
                         pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
    </html:form>
</table>
