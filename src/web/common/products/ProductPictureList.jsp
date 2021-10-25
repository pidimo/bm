<%@ include file="/Includes.jsp" %>

<c:set var="updateAction" value="/ProductPicture/Forward/Update.do"/>
<c:set var="deleteAction" value="/ProductPicture/Forward/Delete.do"/>
<c:set var="createAction" value="/ProductPicture/Forward/Create.do"/>


<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0">
<tr>
<td>

<table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
    <TR>
        <html:form action="${createAction}">
            <TD class="button">
                <app2:securitySubmit operation="CREATE" functionality="PICTURE" styleClass="button">
                    <fmt:message key="Common.new"/>
                </app2:securitySubmit>
            </TD>
        </html:form>
    </TR>
</table>

<TABLE border="0" cellpadding="0" cellspacing="0" width="100%" class="container" align="center">
    <TR align="center">
        <td>
            <TABLE id="BankList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%"
                   class="container" align="center">
                <tr>
                    <td align="center">
                        <fanta:table list="productPictureList" width="100%" id="productPicture"
                                     action="ProductPicture/List.do" imgPath="${baselayout}" rowHeight="250"
                                     enableRowLighting="false" textShortening="false">
                            <c:set var="editAction"
                                   value="ProductPicture/Forward/Update.do?dto(freeTextId)=${productPicture.graphicsId}&dto(productId)=${productPicture.id}&dto(productPictureName)=${app2:encode(productPicture.pictureName)}"/>
                            <c:set var="deleteAction"
                                   value="ProductPicture/Forward/Delete.do?dto(freeTextId)=${productPicture.graphicsId}&dto(productId)=${productPicture.id}&dto(productPictureName)=${app2:encode(productPicture.pictureName)}"/>
                            <c:set var="downloadAction"
                                   value="ProductPicture/DownloadImage.do?dto(freeTextId)=${productPicture.graphicsId}&dto(productPictureName)=${app2:encode(productPicture.pictureName)}"/>

                            <app2:checkAccessRight functionality="PICTURE" permission="VIEW">
                                <fanta:dataColumn name="graphicsId" styleClass="listItem"
                                                  title="ProductPicture.picture" headerStyle="listHeader"
                                                  width="50%" renderData="false">
                                    <table align="center" width="100%" cellpadding="0" cellspacing="0"
                                           border="0" class="container">
                                        <tr>
                                            <td>
                                                <html:img
                                                        page="/ProductPicture/DownloadImage.do?dto(freeTextId)=${productPicture.graphicsId}&dto(thumbnail)=true"
                                                        border="0" vspace="5" hspace="5"/>
                                            </td>
                                        </tr>
                                    </table>
                                </fanta:dataColumn>
                            </app2:checkAccessRight>

                            <fanta:dataColumn name="pictureName" styleClass="listItem2"
                                              title="ProductPicture.information" headerStyle="listHeader"
                                              width="50%" renderData="false">
                                <app2:checkAccessRight functionality="PICTURE" permission="VIEW">
                                    <html:form action="${downloadAction}" target="blank">
                                        <html:submit styleClass="button" style="width:100px"><fmt:message
                                                key="ProductImage.download"/></html:submit>
                                    </html:form>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="PICTURE" permission="DELETE">
                                    <html:form action="${deleteAction}">
                                        <html:submit styleClass="button" style="width:100px"><fmt:message
                                                key="Common.delete"/>
                                        </html:submit>
                                    </html:form>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="PICTURE" permission="UPDATE">
                                    <html:form action="${editAction}">
                                        <html:submit styleClass="button" style="width:100px"><fmt:message
                                                key="Common.update"/>
                                        </html:submit>
                                    </html:form>
                                </app2:checkAccessRight>
                                <table border="0" cellpadding="0" cellspacing="0" width="90%"
                                       class="container">
                                    <tr>
                                        <TD class="label" width="25%" nowrap><fmt:message
                                                key="ProductPicture.name"/></TD>
                                        <TD class="contain" width="75%">${productPicture.pictureName}</TD>
                                    </tr>
                                    <tr>
                                        <TD class="label" nowrap><fmt:message
                                                key="ProductPicture.size"/></TD>
                                        <TD class="contain" width="75%">${productPicture.pictureSize}&nbsp<fmt:message
                                                key="msg.Kbytes"/></TD>
                                    </tr>
                                    <tr>
                                        <fmt:message var="datePattern" key="datePattern"/>
                                        <fmt:formatDate var="dateValue"
                                                        value="${app2:intToDate(productPicture.pictureUploadDate)}"
                                                        pattern="${datePattern}"/>
                                        <TD class="label" nowrap><fmt:message
                                                key="ProductPicture.uploadDate"/></TD>
                                        <TD class="contain" width="75%">${dateValue}&nbsp;</TD>
                                    </tr>
                                </table>
                            </fanta:dataColumn>
                        </fanta:table>
                    </td>
                </tr>
                <tr>
                    <td <c:out value="${sessionScope.listshadow}" escapeXml="false"/>><img
                            src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5">
                    </td>
                </tr>
            </table>
        </td>
    </TR>
</TABLE>
</td>
</tr>
</table>