<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>

<link rel="stylesheet" href="<c:url value="/js/cacheable/jqueryminicolors/2.2.2/jquery.minicolors.css"/>">
<script src="<c:url value="/js/cacheable/jqueryminicolors/2.2.2/jquery.minicolors.min.js"/>"></script>

<script language="JavaScript">

    function addStyleColorPicker() {
        $('.colorPicker').each(function () {
            $(this).minicolors({
                control: $(this).attr('data-control') || 'wheel',
                defaultValue: $(this).attr('data-defaultValue') || '',
                inline: $(this).attr('data-inline') === 'true',
                letterCase: $(this).attr('data-letterCase') || 'uppercase',
                opacity: $(this).attr('data-opacity'),
                position: $(this).attr('data-position') || 'top right',
                change: function (hex, opacity) {
                    if (!hex) return;
                    if (opacity) hex += ', ' + opacity;
                },
                theme: 'bootstrap'
            });
        });
    }

    function setMiniColorValue(inputId, value) {
        $("#"+inputId).minicolors('value', value);
    }

    //add when DOM is ready
    $(document).ready(function (parameter) {
        addStyleColorPicker();
    });

</script>