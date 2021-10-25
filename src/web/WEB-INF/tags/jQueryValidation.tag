<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ attribute name="formName" required="true" description="struts-config form name" %>
<%@ attribute name="styleId"  description="id form" %>
<%@ attribute name="isValidate"  description="is validate form" %>
<%@ attribute name="isAllValidation" description="is validate all fields" %>

<c:set var="validationInfoMap" value="${app2:writeJQueryValidationRules(formName, pageContext.request)}"
       scope="request"/>
<c:set var="formValidationRules" value="${validationInfoMap['jQueryValidationRules']}" scope="request"/>
<c:set var="validationPropertyList" value="${validationInfoMap['propertyRulesList']}" scope="request"/>


<c:set var="isAllFieldsValidate" value="true"/>
<c:choose>
    <c:when test="${not empty isAllValidation}">
        <c:set var="isAllFieldsValidate" value="${isAllValidation}"/>
    </c:when>
    <c:otherwise>
        <c:set var="isAllFieldsValidate" value="true"/>
    </c:otherwise>
</c:choose>


<jsp:doBody var="tagBodyVar"/>

<c:if test="${not empty tagBodyVar && not empty formValidationRules}">
    <c:set var="tagBodyVar" value=", ${tagBodyVar}"/>
</c:if>

<c:set var="writeValidator" value="false"/>
<c:choose>
    <c:when test="${not empty isValidate}">
        <c:set var="writeValidator" value="${isValidate}"/>
    </c:when>
    <c:otherwise>
        <c:if test="${'create' eq op || 'update' eq op}">
            <c:set var="writeValidator" value="true"/>
        </c:if>
    </c:otherwise>
</c:choose>

<c:if test="${writeValidator eq 'true'}">
    <script type="text/javascript">
       var validator=$("${(not empty styleId) ? '#'+styleId : 'form'}").validate({
            rules: {
                ${formValidationRules}
                ${tagBodyVar}
            },
            onkeyup: function(element,event){
                if(!((event.keyCode || event.which)==9)){
                    $(element).valid();
                    if($(element).is(':checked')){
                        $(element).valid();
                    }
                }
            },
            onfocusin: function(element,event){
                $(element).change(function () {
                    $(element).valid();
                });
                if($(element).is(':checked')){
                    $(element).valid();
                }
            },
            onfocusout:false,
            ignore: [],
            ignore: ".ignore",
            highlight: markAsError,
            unhighlight: markAsSuccess,
            errorElement: 'span',
            errorClass: 'help-block',
            errorPlacement: errorFeedback
        });
        function markAsError(element) {
            setStatusInputValidation(element, false);
        }
        function markAsSuccess(element) {
            <c:if test="${isAllFieldsValidate eq 'true'}">
                        setStatusInputValidation(element, true);
            </c:if>
        }
        function errorFeedback(error, element) {
            if (element.parent('.input-group').length) {
                error.insertAfter(element.parent());
            } else {
                error.insertAfter(element);
            }
            if ((element.is(":radio")) || (element.is(":checkbox"))) {
                error.appendTo(element.parents('.radiocheck'));
            }
        }
        function setStatusInputValidation(element, status) {
            var nodeParentElement = $(element).closest(".parentElementInputSearch");
            var nodeSpanFeedbackIcon = $(nodeParentElement).find(".iconValidation");
            if (status) {
                $(element).closest('.form-group').removeClass('has-error').addClass('');
                $(element).closest('.threeSmallGrid').removeClass('has-error').addClass('');
                if (nodeSpanFeedbackIcon != undefined) {
                    $(nodeSpanFeedbackIcon).removeClass('glyphicon-remove').addClass('');
                }
            } else {
                $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
                $(element).closest('.threeSmallGrid').removeClass('has-success').addClass('has-error');
                if (nodeSpanFeedbackIcon != undefined) {
                    $(nodeSpanFeedbackIcon).removeClass('glyphicon-ok').addClass('glyphicon-remove');
                }
            }
        }


        //process some task in on ready document
        $(document).ready(function () {
            <c:forEach var="item" items="${validationPropertyList}">
            <c:forEach var="depends" items="${item['dependsList']}">
            <c:if test="${'required' eq depends}">
            preProcessRequiredHiddenProperties('${item['property']}');
            </c:if>
            </c:forEach>
            </c:forEach>
        });

        function preProcessRequiredHiddenProperties(propertyName) {
            $('input[type="hidden"][name="dto(' + propertyName + ')"]').each(function () {
                $(this).addClass('required');
                var nodeParentHidden = $(this).closest(".parentElementInputSearch");
                var nodeInputAsociate = $(nodeParentHidden).find(".form-control");
                $(nodeInputAsociate).addClass('ignore');
            });
            $('input[type="hidden"][name="parameter(' + propertyName + ')"]').each(function () {
                $(this).addClass('required');
                var nodeParentHidden = $(this).closest(".parentElementInputSearch");
                var nodeInputAsociate = $(nodeParentHidden).find(".form-control");
                $(nodeInputAsociate).addClass('ignore');
            });
        }
    </script>
    <style type="text/css">
        .iconValidation {
            right: -17px;
            text-align: center;
            font-size: 1.1em !important;
        }
    </style>
</c:if>