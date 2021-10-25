<%@ include file="/Includes.jsp" %>

<script language="JavaScript" type="text/javascript">
  <!--
  $(window).load(function() {

    jQuery('img.listAddressImage').each(function () {
      var height = jQuery(this).height();
      var new_height = 30; //new height
      if (height > new_height) {
        var width = jQuery(this).width();
        var percent = Math.round((100 * new_height) / height);
        var new_width = Math.round((width * percent) / 100);
        jQuery(this).css({
          width: new_width + 'px',
          height: new_height + 'px'
        });
      }
    });

  });
  //-->
</script>
