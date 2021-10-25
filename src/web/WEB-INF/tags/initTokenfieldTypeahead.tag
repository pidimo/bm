<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>


<link href="<c:url value="/js/cacheable/bootstrap/bootstraptokenfield/0.12.1/css/bootstrap-tokenfield.min.css"/>" rel="stylesheet" />
<%--<link href="<c:url value="/js/cacheable/bootstrap/bootstraptokenfield/0.12.1/css/tokenfield-typeahead.min.css"/>" rel="stylesheet" />--%>
<link href="<c:url value="/js/cacheable/bootstrap/bootstraptokenfield/css/customTokenfieldTypehead-1.0.2.css"/>" rel="stylesheet" />

<script src="<c:url value="/js/cacheable/bootstrap/typeahead/0.11.1/typeahead.bundle.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/js/cacheable/bootstrap/bootstraptokenfield/0.12.1/bootstrap-tokenfield.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/js/cacheable/bootstrap/handlebars/handlebars-v4.0.5.js"/>" type="text/javascript"></script>


<%--handlebars template to personalize typeahead menu item--%>
<script id="typeheadResultTemplate" type="text/x-handlebars-template">
    <div class="{{containerCssClass}}">
        <div class="suggestionDetails">
            <div class="suggestionContactName">{{label}}</div>
            <div class="suggestionContactName">{{organizationName}}</div>
            <div class="suggestionEmail">{{value}}</div>
        </div>
    </div>
</script>


<script type="text/javascript">
    //this var is used in tokenfieldTypeahead tag
    var tokenFieldCount = 0;

    function defineTokenFieldZIndex() {
        var zIndexToken = 100;

        $("div.tokenfield").each(function() {
            $(this).css("z-index", zIndexToken);
            zIndexToken--;
        });
    }

    function addTokenField(inputId, email, contactName, addressId, contactPersonOfId) {
        var tokenData = {
            "label" : decodeURI(contactName),
            "value" : decodeURI(email),
            "addressId" : addressId,
            "contactPersonOfId" : contactPersonOfId
        };

        $('#'+inputId).tokenfield('createToken', tokenData);
    }

    function isValidRecipientEmail(email) {
        var expr = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,}|[0-9]{1,3})(\]?)$/;
        return expr.test(email);
    }

</script>