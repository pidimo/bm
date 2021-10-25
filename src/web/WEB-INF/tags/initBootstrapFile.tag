<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>

<script language="JavaScript">

    function initFunctionsTagFile(){
        $(document).on('change', '.btnFile :file', function () {
            var input = $(this),
                    numFiles = 1,
                    label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
            input.trigger('fileselect', [numFiles, label]);
        });

        $(document).ready(function () {
            $('.btnFile :file').on('fileselect', function (event, numFiles, label) {

                var input = $(this).parents('.input-group').find(':text'),
                        name = label;

                if (input.length) {
                    input.val(name);
                }
            });
        });
    }
    initFunctionsTagFile();
</script>