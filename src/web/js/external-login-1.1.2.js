/*

business manager external login script.
@author Fernando Monta�o
Copyright (c) Jatun SRL - pirAMide GmbH. All Rights Reserved.
*/


function processLoginResponse(response) {
    $("#ajaxProcessId").hide();
    if ($(response).find("#loginForm").html() != null) {
        var errorDiv = $(response).find("#errorContainer").html();
        $("#errorContainer").html(errorDiv);
        $("#errorContainer").show();

    } else if($(response).find("#isPlannedPasswordChangeId").html() != null) {
        var passChangeUrl = $(response).find("#urlPasswordChangeId").val() ;
        var contextUrl = location.href.substring(0, location.href.indexOf('/bm/'));

        parent.location.href = contextUrl + '/bm/' + passChangeUrl;

    } else if($(response).find("#isOtherUserConnectedDivId").html() != null) {

        var otherUserUrl = $(response).find("#urlOtherUserConnectedId").val() ;
        var contextUrl = location.href.substring(0, location.href.indexOf('/bm/'));
        parent.location.href = contextUrl + '/bm/' + otherUserUrl;
    } else {
        var url = location.href.substring(0, location.href.indexOf('/bm/'));
        parent.location.href = url + '/bm/e32Fz5Hlvfj8JgrOSr_AWzv5mRGHTUL-NJ_JUFZJ06rg41P3GzXj0Q**' + ';jsessionid=' + response;
    }
}
function showLoading() {
    $("#errorContainer").hide();
    $("#ajaxProcessId").show();
}






